/*
 * $Id: HibernateDataSource.java 148 2004-10-29 20:43:46Z rbair $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.jdnc.incubator.rbair.hibernate;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import net.sf.hibernate.Query;
import net.sf.hibernate.Session;

import org.hibernate.auction.persistence.HibernateUtil;
import org.jdesktop.jdnc.incubator.rbair.swing.data.AbstractDataSource;
import org.jdesktop.jdnc.incubator.rbair.swing.data.DataModel;
import org.jdesktop.jdnc.incubator.rbair.swing.data.MetaData;

/**
 * A demo data source for using hibernate
 * @author Richard Bair
 */
public class HibernateDataSource extends AbstractDataSource {
	private Session session;
	/**
	 * Valid only if autoCommit is false. True if a save has occured while
	 * autoCommit is false, false otherwise.
	 */
	private boolean currentTx;
	
	public HibernateDataSource() {
		configureConnectionListener();
	}
	
	/**
	 * Adds a data model to the list of data models serviced by this DataSource.
	 * If the given DataModel is already managed by this DataSource, then
	 * nothing will be done.
	 * @param dm cannot be null
	 * @throws Exception
	 */
	public void addDataModel(HibernateDataModel dm) throws Exception {
		if (dm == null) {
			throw new NullPointerException("The data model passed to " +
					"the HibernateDataSource's addDataModel method cannot " +
					"be null");
		}
		if (!dataModels.contains(dm)) {
			dataModels.add(dm);
			//if connected, then execute the dataModel's query 
			//and set the DataModel's results
			if (isConnected()) {
			    refresh(new HibernateDataModel[]{dm});
			}
		}
	}
	
	/**
	 * Refreshes the given DataModel
	 * @param dm must have been previously added to this DataSource via
	 * <code>addDataModel</code>
	 * @throws Exception
	 */
	public void refreshDataModel(HibernateDataModel dm) throws Exception {
		if (dm == null) {
			throw new NullPointerException("The data model passed to " +
					"the HibernateDataSource's refreshDataModel method cannot "+
					"be null");
		}
		
		if (!isConnected()) {
			throw new Exception("Cannot refresh, not connected " +
					"to the database");
		}
		
		if (!dataModels.contains(dm)) {
			throw new IllegalArgumentException("The data model passed to " +
					"the HibernateDataSource's refreshDataModel method must " +
					"have been previously added by addDataModel");
		} else {
			//refresh the data model
		    refresh(new HibernateDataModel[]{dm});
		}
	}
	
	/**
	 * @inheritDoc
	 */
	public void refreshAllDataModels() {
		if (!isConnected()) {
			System.err.println("Cannot refresh, not connected " +
					"to the database");
		}

		//since I'm connected, refresh each TOP LEVEL DataModel
		//If a data model is a detail data model, then I cannot run it yet
		//since it depends on the master to connect and gets its data before
		//the detail has the params it needs to run.
		List dms = new ArrayList();
		for (int i=0; i<dataModels.size(); i++) {
			if (((HibernateDataModel)dataModels.get(i)).getMasterDataModel() == null) {
				dms.add(dataModels.get(i));
			}
		}
		
		refresh((HibernateDataModel[])dms.toArray(new HibernateDataModel[dms.size()]));
		
		//now, for each DataModel that is a detail, cause its MasterDetailHandler
		//to be notified so that it will cause a refresh
		//note that this is unnecessary if the detail data model is
		//a detail to one of the DataModels being managed by this
		//DataSource since it will have been refreshed automatically
		//by the above code. This check exists to ensure that
		//DataModels are refreshed if their master has already been
		//loaded, but this data source was not connected to refresh
		//when the master was loaded. For instance, a JavaBeanDataModel
		//may be the master, and may have been loaded before this
		//RowSetDataSource was connected. Thus, this DataModel must
		//now be connected
		for (int i=0; i<dataModels.size(); i++) {
			HibernateDataModel dm = (HibernateDataModel)dataModels.get(i);
			if (dm.getMasterDataModel() != null
					&& !dataModels.contains(dm.getMasterDataModel())) {
				dm.getMasterDetailHandler().handleMasterChanged(
						dm.getMasterDataModel(), dm);
			}
		}
	}
	
	/**
	 * @param dm must have been previously added to this DataSource via
	 * <code>addDataModel</code>
	 * @throws Exception
	 */
	public void saveDataModel(HibernateDataModel dm) throws Exception {
		if (dm == null) {
			throw new NullPointerException("The data model passed to " +
					"the HibernateDataSource's saveDataModel method cannot " +
					"be null");
		}
		
		if (!isConnected()) {
			throw new Exception("Cannot save, not connected " +
					"to the database");
		}
		
		if (!dataModels.contains(dm)) {
			throw new IllegalArgumentException("The data model passed to " +
					"the HibernateDataSource's saveDataModel method must " +
					"have been previously added by addDataModel");
		} else {
		    save(new HibernateDataModel[]{dm});
			if (!autoCommit) {
				currentTx = true;
			}
		}
	}
	
	/**
	 * Connect to the database
	 */
	protected void connect() throws Exception {
		//create the session & transaction
		session = HibernateUtil.getSession();
	}

	/**
	 * Disconnects from the database and causes all of the attached DataModels
	 * to flush their contents.
	 */
	protected void disconnect() throws Exception {
		session.connection().commit();
		session.close();
		//TODO force all connected DataModels to flush their data
	}

	/**
	 * This method configures a property change listener on the super classes
	 * connected field. This is used to populate DataModels if the connection
	 * status is changed from false to true.
	 */
	private void configureConnectionListener() {
		super.addPropertyChangeListener("connected", new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				if (evt.getOldValue().equals(Boolean.FALSE) 
						&& evt.getNewValue().equals(Boolean.TRUE)) {
					refreshAllDataModels();
				}
			}
		});
	}

	/**
	 * @inheritDoc
	 */
	public void setAutoCommit(boolean val) throws Exception {
		boolean oldVal = autoCommit;
		super.setAutoCommit(val);
		try {
			Connection conn = session.connection();
			conn.setAutoCommit(val);
			if (val != oldVal) {
				//sets currentTx to false if autoCommit is changed to
				//true (since currentTx means nothing in that case)
				//or if it is changed to false (since it would be the
				//beginning of a transaction)
				currentTx = false;
			}
		} catch (Exception e) {
			autoCommit = oldVal;
			throw new Exception("Failed to set autoCommit on " +
					"HibernateDataSource", e);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.jdesktop.jdnc.incubator.rbair.swing.data.AbstractDataSource#isCurrentTx()
	 */
	public boolean isCurrentTx() {
		try {
			return session.connection().getAutoCommit() ? false : currentTx;
		} catch (Exception e){
			e.printStackTrace();
			return false;
		}
	}

	/* (non-Javadoc)
	 * @see org.jdesktop.jdnc.incubator.rbair.swing.data.DataSource#canCommit()
	 */
	public boolean canCommit() {
		return true;
	}

	/* (non-Javadoc)
	 * @see org.jdesktop.jdnc.incubator.rbair.swing.data.DataSource#commit()
	 */
	public void commit() throws Exception {
		session.flush();
		session.connection().commit();
		session.clear();
	}

	/* (non-Javadoc)
	 * @see org.jdesktop.jdnc.incubator.rbair.swing.data.DataSource#rollback()
	 */
	public void rollback() throws Exception {
		session.connection().rollback();
		session.clear();
	}

    /**
     * @inheritDoc
     */
    protected SaveTask createSaveTask(DataModel[] models) {
        return new SaveTask(models) {
            protected void saveData(DataModel[] models) throws Exception {
    			//FIXME Apparently Hibernate doesn't flush to the database within
    			//the context of a transaction -- or at least if it does it hides
    			//this from me. So, I don't have to do anything here
            }
        };
    }

    /**
     * @inheritDoc
     */
    protected LoadTask createLoadTask(DataModel[] models) {
        return new LoadTask(models) {
            /**
             * @inheritDoc
             */
            protected MetaData[] readMetaData(DataModel dm) throws Exception {
                return new MetaData[0];
            }

            /**
             * @inheritDoc
             */
            protected void loadMetaData(DataModel dm, MetaData[] metaData) {
                // TODO Auto-generated method stub
            }

            /**
             * @inheritDoc
             */
            protected void readData(DataModel[] models) throws Exception {
                HibernateDataModel[] dms = (HibernateDataModel[])models;
        		for (int i=0; i<dms.length; i++) {
        			HibernateDataModel dm = (HibernateDataModel)dms[i];
        			try {
        				List queryParams = dm.getQueryParams();
        				Query q = dm.getQuery();
        				for (int j=0; j<queryParams.size(); j++) {
        					q.setParameter(j, queryParams.get(j));
        				}
        				scheduleLoad(new LoadItem(dm, q.list()));
        			} catch (Exception e) {
        				e.printStackTrace();
        			}
        		}
            }

            /**
             * @inheritDoc
             */
            protected void loadData(LoadItem[] items) {
                for (int i=0; i<items.length; i++) {
                    ((HibernateDataModel)items[i].dataModel).setJavaBean(items[i].model);
                }
            }
        };
    }
}
