/*
 * $Id: RowSetDataSource.java 148 2004-10-29 20:43:46Z rbair $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.jdnc.incubator.rbair.swing.data;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.security.AccessControlException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.naming.InitialContext;
import javax.sql.rowset.CachedRowSet;
import javax.swing.Icon;

import org.jdesktop.jdnc.incubator.rbair.swing.data.AbstractDataSource.LoadTask.LoadItem;

import com.sun.rowset.CachedRowSetImpl;

/**
 * A DataSource that saves/retrieves data from a JDBC data store. The transport
 * medium is a RowSet (and thus also possibly a ResultSet). Only
 * RowSetDataModels can be used with this DataSource.
 * <p>
 * Connections to the database may be retrieved by either using the traditional
 * DriverManager approach, or by using the javax.sql.DataSource & JNDI approach.
 * The former uses a DriverManager (specified by class name), a JDBC url, 
 * and either a userName/password combination or a Properties map to connect
 * to the database. The later approach relies on JNDI to retrieve a
 * javax.sql.DataSource, and then uses a userName/password combination to 
 * connect to the database.
 * @author Richard Bair
 */
public class RowSetDataSource extends AbstractDataSource {
	/**
	 * The connection to the database used for this DataSource
	 */
	private Connection conn;
	/**
	 * This is a mutex used to control access to the conn object.
	 * Note that I create a new String object explicitly. This is done
	 * so that a single mutex is unique for each RowSetDataSource instance.
	 */
	private final Object connMutex = new String("Connection_Mutex");
	/**
	 * The 
	 */
	private DriverManager driverManager;
	/**
	 * 
	 */
	private String jndiContext;
	/**
	 * 
	 */
	private String url;
	/**
	 * 
	 */
	private String userName;
	/**
	 * 
	 */
	private String password;
	/**
	 * 
	 */
	private Properties properties;
	/**
	 * Valid only if autoCommit is false. True if a save has occured while
	 * autoCommit is false, false otherwise.
	 */
	private boolean currentTx;
	
	/**
	 * Create a new RowSetDataSource. Be sure to set the JDBC connection
	 * properties (user name, password, connection method, etc)
	 * prior to connecting this DataSource.
	 */
	public RowSetDataSource() {
		configureConnectionListener();
	}
	
	/**
	 * Create a new RowSetDataSource and initialize it to connect to a 
	 * database using the given params.
	 * @param driver
	 * @param url
	 * @param user
	 * @param passwd
	 */
	public RowSetDataSource(String driver, String url, String user, 
			String passwd) {
		try {
			Class.forName(driver);
		} catch (Exception e) {
			System.err.println("WARN: The driver passed to the " +
					"RowSetDataSource constructor could not be loaded. " +
					"This may be due to the driver not being on the classpath");
			e.printStackTrace();
		}
		this.url = url;
		this.userName = user;
		this.password = passwd;
		configureConnectionListener();
	}
	
	/**
	 * Create a new RowSetDataSource and initialize it to connect to a 
	 * database using the given params.
	 * @param driver
	 * @param url
	 * @param props
	 */
	public RowSetDataSource(String driver, String url, Properties props) {
		try {
			Class.forName(driver);
		} catch (Exception e) {
			System.err.println("WARN: The driver passed to the " +
					"RowSetDataSource constructor could not be loaded. " +
					"This may be due to the driver not being on the classpath");
			e.printStackTrace();
		}
		this.url = url;
		this.properties = props;
		configureConnectionListener();
	}
	
	/**
	 * Create a new RowSetDataSource and initialize it to connect to a 
	 * database using the given params.
	 * @param jndiContext
	 * @param user
	 * @param passwd
	 */
	public RowSetDataSource(String jndiContext, String user, String passwd) {
		this.jndiContext = jndiContext;
		this.userName = user;
		this.password = passwd;
		configureConnectionListener();
	}
	
	/**
	 * Adds a data model to the list of data models serviced by this DataSource.
	 * If the given DataModel is already managed by this DataSource, then
	 * nothing will be done.
	 * @param dm cannot be null
	 * @throws Exception
	 */
	public void addDataModel(RowSetDataModel dm) throws Exception {
		if (dm == null) {
			throw new NullPointerException("The data model passed to " +
					"the RowSetDataSource's addDataModel method cannot " +
					"be null");
		}
		if (!dataModels.contains(dm)) {
			dataModels.add(dm);
			//if connected, then execute the dataModel's query 
			//and set the DataModel's results
			if (isConnected()) {
				refresh(new RowSetDataModel[]{dm});
			}
		}
	}
	
	/**
	 * Refreshes the given DataModel
	 * @param dm must have been previously added to this DataSource via
	 * <code>addDataModel</code>
	 * @throws Exception
	 */
	public void refreshDataModel(RowSetDataModel dm) throws Exception {
	    refreshDataModel(dm, "<html><h3><b>Loading data</b></h3></html>");
	}
	
	/**
	 * Refreshes the given DataModel, and uses the given description in the
	 * ProgressManager.
	 * @param dm
	 * @param description
	 * @throws Exception
	 */
	public void refreshDataModel(RowSetDataModel dm, String description) throws Exception {
		if (dm == null) {
			throw new NullPointerException("The data model passed to " +
					"the RowSetDataSource's refreshDataModel method cannot "+
					"be null");
		}
		
		if (!isConnected()) {
			throw new Exception("Cannot refresh, not connected " +
					"to the database");
		}
		
		if (!dataModels.contains(dm)) {
			throw new IllegalArgumentException("The data model passed to " +
					"the RowSetDataSource's refreshDataModel method must " +
					"have been previously added by addDataModel");
		} else {
			//refresh the data model
			refresh(new RowSetDataModel[]{dm});
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

		//now that I'm connected, refresh each TOP LEVEL DataModel
		//If a data model is a detail data model, then I cannot run it yet
		//since it depends on the master to connect and gets its data before
		//the detail has the params it needs to run.
		List dms = new ArrayList();
		for (int i=0; i<dataModels.size(); i++) {
			if (((RowSetDataModel)dataModels.get(i)).getMasterDataModel() == null) {
				dms.add(dataModels.get(i));
			}
		}
		
		refresh((RowSetDataModel[])dms.toArray(new RowSetDataModel[dms.size()]));
		
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
			RowSetDataModel dm = (RowSetDataModel)dataModels.get(i);
			if (dm.getMasterDataModel() != null
					&& !dataModels.contains(dm.getMasterDataModel())) {
				dm.getMasterDetailHandler().handleMasterChanged(
						dm.getMasterDataModel(), dm);
			}
		}
	}
	
	/**
	 * Since this dataModel doesn't connect to a data store, save does
	 * nothing. 
	 * @param dm must have been previously added to this DataSource via
	 * <code>addDataModel</code>
	 * @throws Exception
	 */
	public void saveDataModel(RowSetDataModel dm) throws Exception {
		if (dm == null) {
			throw new NullPointerException("The data model passed to " +
					"the RowSetDataSource's saveDataModel method cannot " +
					"be null");
		}
		
		if (!isConnected()) {
			throw new Exception("Cannot save, not connected " +
					"to the database");
		}
		
		if (!dataModels.contains(dm)) {
			throw new IllegalArgumentException("The data model passed to " +
					"the RowSetDataSource's saveDataModel method must " +
					"have been previously added by addDataModel");
		} else {
			save(new RowSetDataModel[]{dm});
			if (!autoCommit) {
				currentTx = true;
			}
		}
	}
	
	/**
	 * Connect to the database
	 */
	protected void connect() throws Exception {
		//if the jndiContext is not null, then try to get the DataSource to use
		//from jndi
		if (jndiContext != null) {
			try {
				connectByJNDI();
			} catch (Exception e) {
				try {
					connectByDriverManager();
				} catch (Exception ex) {
					throw new Exception("Failed to connect to the database", e);
				}
			}
		} else {
			try {
				connectByDriverManager();
			} catch (Exception ex) {
				throw new Exception("Failed to connect to the database", ex);
			}
		}
	}

	/**
	 * Attempts to get a JDBC Connection from a JNDI javax.sql.DataSource
	 * @throws Exception
	 */
	private void connectByJNDI() throws Exception {
		InitialContext ctx = new InitialContext();
		javax.sql.DataSource ds = (javax.sql.DataSource)ctx.lookup(jndiContext);
		synchronized(connMutex) {
			conn = ds.getConnection(userName, password);
			conn.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
		}
	}

	/**
	 * Attempts to get a JDBC Connection from a DriverManager. If properties
	 * is not null, it tries to connect with those properties. If that fails,
	 * it then attempts to connect with a user name and password. If that fails,
	 * it attempts to connect without any credentials at all.
	 * <p>
	 * If, on the other hand, properties is null, it first attempts to connect
	 * with a username and password. Failing that, it tries to connect without
	 * any credentials at all.
	 * @throws Exception
	 */
	private void connectByDriverManager() throws Exception {
		synchronized(connMutex) {
			if (properties != null) {
				try {
					conn = DriverManager.getConnection(url, properties);
					conn.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
				} catch (Exception e) {
					try {
						conn = DriverManager.getConnection(url, userName, password);
						conn.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
					} catch (Exception ex) {
						conn = DriverManager.getConnection(url);
						conn.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
					}
				}
			} else {
				try {
					conn = DriverManager.getConnection(url, userName, password);
					conn.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
				} catch (Exception e) {
					e.printStackTrace();
					//try to connect without using the userName and password
					conn = DriverManager.getConnection(url);
					conn.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
				}
			}
		}
	}
	
	/**
	 * Disconnects from the database and causes all of the attached DataModels
	 * to flush their contents.
	 */
	protected void disconnect() throws Exception {
		synchronized(connMutex) {
			if (conn != null) {
				conn.close();
			}
		}
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
					"RowSetDataSource", e);
		}
	}
	
	/**
	 * @inheritDoc
	 */
	public boolean isCurrentTx() {
		try {
			if (conn == null) {
				return false;
			} else {
				return conn.getAutoCommit() ? false : currentTx;
			}
		} catch (Exception e){
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * @inheritDoc
	 * FIXME how do I do this?
	 */
	public boolean canCommit() {
		return true;
	}
	
	/**
	 * @inheritDoc
	 */
	public void commit() throws Exception {
		conn.commit();
	}
	
	/**
	 * @inheritDoc
	 */
	public void rollback() throws Exception {
		conn.rollback();
	}

    /**
     * @inheritDoc
     */
    protected SaveTask createSaveTask(DataModel[] models) {
        // TODO Auto-generated method stub
        return new SaveTask(models) {

            protected void saveData(DataModel[] models) throws Exception {
                for (int index=0; index<models.length; index++) {
                    RowSetDataModel dm = (RowSetDataModel)models[index];
	    			synchronized (connMutex) {
	    				StringBuffer sql = new StringBuffer();
	    				sql.append("update ");
	    				sql.append(dm.getTableName());
	    				sql.append(" set ");
	    				String[] fieldNames = dm.getFieldNames();
	    				for (int i=0; i<fieldNames.length; i++) {
	    					if (i > 0) {
	    						sql.append(", ");
	    					}
	    					sql.append(fieldNames[i]);
	    					sql.append(" = ?");
	    				}
	    				sql.append(" where ");
	    				String[] keyFields = dm.getKeyFields();
	    				for (int i=0; i<keyFields.length; i++) {
	    					if (i > 0) {
	    						sql.append(", ");
	    					}
	    					sql.append(keyFields[i]);
	    					sql.append(" = ?");
	    				}
	    				
	    				PreparedStatement ps = conn.prepareStatement(sql.toString());
	    				for (int i=0; i<dm.getRecordCount(); i++) {
	    					for (int j=0; j<fieldNames.length; j++) {
	    						ps.setObject(j+1, dm.getValue(fieldNames[j], i));
	    					}
	    					for (int j=0; j<keyFields.length; j++) {
	    						ps.setObject(j + fieldNames.length + 1, dm.getValue(keyFields[j], i));
	    					}
	    					ps.executeUpdate();
	    				}
	//    				CachedRowSet rs = dm.getRowSet();
	//    				rs.acceptChanges(conn);
	    			}
                }
            }
        };
    }

    /**
     * @inheritDoc
     */
    protected LoadTask createLoadTask(DataModel[] models) {
        // Auto-generated method stub
        return new LoadTask(models) {
            /**
             * @inheritDoc
             */
            protected void readData(DataModel[] models) throws Exception {
        		//set the progess count
                setMinimum(0);
                setMaximum(models.length);
        		//construct and execute a rowset for each data model in turn.
        		//as each data model is finished, call notifyLoad.
        		RowSetDataModel[] dataModels = (RowSetDataModel[])models;
        		for (int i=0; i<dataModels.length; i++) {
        			try {
        				synchronized(connMutex) {
        					//TODO turn commented out code below into logging statements
        					PreparedStatement ps = conn.prepareStatement(dataModels[i].getSelectSql());
//        					System.out.println("Execute sql: " + dataModels[i].getSelectSql());
        					List params = dataModels[i].getSelectParams();
//        					System.out.print("With params [");
        					for (int j=0; j<params.size(); j++) {
        						ps.setObject(j+1, params.get(j));
        						if (j > 0) {
//        							System.out.print(", " + params.get(j));
        						} else {
//        							System.out.print(params.get(j));
        						}
        					}
//        					System.out.println("]");
        					ResultSet resultSet = ps.executeQuery();
        					CachedRowSet crs = null;
        					try {
        						crs = new CachedRowSetImpl();
        					} catch (AccessControlException ace) {
        						//HACK this is a workaround for an AccessControlException
        						//that I get from CachedRowSetImpl (grumble grumble)
        						//if I get it, then try again (it seems to come just
        						//once, the first time)
        						crs = new CachedRowSetImpl();
        					}
        					crs.populate(resultSet);
        					resultSet.close();
        					ps.close();
        					LoadItem item = new LoadItem(dataModels[i], crs);
        					scheduleLoad(item);
        					setProgress(i+1);
        				}
        			} catch (Exception e) {
        				e.printStackTrace();
//        				fireException(e);
        			}
        		}
            }

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
        		//do nothing
            }

            /**
             * @inheritDoc
             */
            protected void loadData(LoadItem[] items) {
        		for (int i=0; i<items.length; i++) {
        		    ((RowSetDataModel)items[i].dataModel).setRowSet((CachedRowSet)items[i].model);
        		}
            }

        };
    }

}
