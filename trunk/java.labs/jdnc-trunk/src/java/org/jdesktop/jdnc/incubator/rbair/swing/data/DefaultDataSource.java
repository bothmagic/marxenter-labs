/*
 * $Id: DefaultDataSource.java 148 2004-10-29 20:43:46Z rbair $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.jdnc.incubator.rbair.swing.data;


/**
 * Default implementation of AbstractDataSource. This implementation doesn't
 * interact with a data store of any kind. Rather, it is a simple &quot;
 * null-op&quot; implementation useful for DataModels that don't require a
 * connection to a data store. In practice, this kind of DataSource will
 * probably not be very useful (almost every program requires persistence of
 * some kind), but fills in an important gap in the implementation, namely,
 * to allow simple programs that don't require persistence to exist. This
 * class may also prove to be useful in testing frameworks.
 * @author Richard Bair
 */
public class DefaultDataSource extends AbstractDataSource {
	
	/**
	 * Adds a data model to the list of data models serviced by this DataSource.
	 * If the given DataModel is already managed by this DataSource, then
	 * nothing will be done.
	 * @param dm cannot be null
	 * @throws Exception
	 */
	public void addDataModel(DataModel dm) throws Exception {
		if (dm == null) {
			throw new NullPointerException("The data model passed to " +
					"the DefaultDataSource's addDataModel method cannot " +
					"be null");
		}
		if (!dataModels.contains(dm)) {
			dataModels.add(dm);
		}
	}
	
	/**
	 * Since this dataModel doesn't connect to a data store, refresh does
	 * nothing. 
	 * @param dm must have been previously added to this DataSource via
	 * <code>addDataModel</code>
	 * @throws Exception
	 */
	public void refreshDataModel(DataModel dm) throws Exception {
		if (dm == null) {
			throw new NullPointerException("The data model passed to " +
					"the DefaultDataSource's refreshDataModel method cannot " +
					"be null");
		}
		
		if (!dataModels.contains(dm)) {
			throw new IllegalArgumentException("The data model passed to " +
					"the DefaultDataSource's refreshDataModel method must " +
					"have been previously added by addDataModel");
		}
	}
	
	/**
	 * Since this dataModel doesn't connect to a data store, save does
	 * nothing. 
	 * @param dm must have been previously added to this DataSource via
	 * <code>addDataModel</code>
	 * @throws Exception
	 */
	public void saveDataModel(DataModel dm) throws Exception {
		if (dm == null) {
			throw new NullPointerException("The data model passed to " +
					"the DefaultDataSource's saveDataModel method cannot " +
					"be null");
		}
		
		if (!dataModels.contains(dm)) {
			throw new IllegalArgumentException("The data model passed to " +
					"the DefaultDataSource's saveDataModel method must " +
					"have been previously added by addDataModel");
		}
	}

	/* (non-Javadoc)
	 * @see org.jdesktop.jdnc.incubator.rbair.swing.data.AbstractDataSource#isCurrentTx()
	 */
	public boolean isCurrentTx() {
		return false;
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
		//do nothing
	}

	/* (non-Javadoc)
	 * @see org.jdesktop.jdnc.incubator.rbair.swing.data.DataSource#rollback()
	 */
	public void rollback() throws Exception {
		//do nothing
	}

	/* (non-Javadoc)
	 * @see org.jdesktop.jdnc.incubator.rbair.swing.data.DataSource#refreshAllDataModels()
	 */
	public void refreshAllDataModels() {

	}

    /**
     * @inheritDoc
     */
    protected SaveTask createSaveTask(DataModel[] models) {
        return new SaveTask(models) {
            protected void saveData(DataModel[] models) throws Exception {
                // TODO Auto-generated method stub
            }
        };
    }

    /**
     * @inheritDoc
     */
    protected LoadTask createLoadTask(DataModel[] models) {
        // TODO Auto-generated method stub
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
                // TODO Auto-generated method stub
                
            }

            /**
             * @inheritDoc
             */
            protected void loadData(LoadItem[] items) {
                // TODO Auto-generated method stub
                
            }
        };
    }
	
}
