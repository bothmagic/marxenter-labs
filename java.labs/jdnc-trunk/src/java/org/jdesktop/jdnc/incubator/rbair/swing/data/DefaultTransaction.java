/*
 * $Id: DefaultTransaction.java 148 2004-10-29 20:43:46Z rbair $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.jdnc.incubator.rbair.swing.data;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Default implementation of the Transaction interface. This implementation
 * should be sufficient for all single-VM transactions. Distributed client VM
 * transactions are not supported by this implementation.
 * @author Richard Bair
 */
public class DefaultTransaction implements Transaction {
	/**
	 * The Set of DataSources within this Transaction
	 */
	private Set/*<DataSource>*/ dataSources = new HashSet/*<DataSource>*/();
	/**
	 * The auto-commit flag
	 */
	private boolean autoCommit = true;
	/**
	 * Indicates whether this transaction is current. It is current if and only
	 * if edits within this transaction have been made but not committed. If
	 * autoCommit is true, then this will <em>always</em> be false.
	 */
	private boolean currentTx = false;
	
	/**
	 * @inheritDoc
	 */
	public void commit() throws Exception {
		boolean canCommit = true;
		
		Iterator itr = dataSources.iterator();
		while (itr.hasNext()) {
			DataSource ds = (DataSource)itr.next();
			if (!ds.canCommit()) {
				canCommit = false;
				break;
			}
		}

		if (canCommit) {
			itr = dataSources.iterator();
			while (itr.hasNext()) {
				DataSource ds = (DataSource)itr.next();
				ds.commit();
				ds.refreshAllDataModels();
			}
		} else {
			System.err.println("Couldn't commit because a data source said no");
		}
	}

	/**
	 * @inheritDoc
	 */
	public void rollback() throws Exception {
		Iterator itr = dataSources.iterator();
		while (itr.hasNext()) {
			DataSource ds = (DataSource)itr.next();
			ds.rollback();
			ds.refreshAllDataModels();
		}
	}

	/**
	 * @inheritDoc
	 */
	public void setAutoCommit(boolean val) throws Exception {
		if (val != autoCommit) {
			if (!val && autoCommit) {
				autoCommit = false;
			} else if (val && !autoCommit && currentTx){
				throw new IllegalArgumentException("Cannot set auto-commit " +
						"to true while in the middle of a transaction. " +
						"The current Transaction must either be committed " +
						"or rolled back prior to changing auto-commit to true");
			} else {
				autoCommit = true;
			}
			//change all of the DataSources autoCommit method to match the
			//autoCommit status of this Transaction
			Iterator itr = dataSources.iterator();
			while (itr.hasNext()) {
				((DataSource)itr.next()).setAutoCommit(autoCommit);
			}
		}
	}

	/**
	 * @inheritDoc
	 */
	public boolean isAutoCommit() {
		return autoCommit;
	}

	/**
	 * @inheritDoc
	 */
	public boolean undo() {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * @inheritDoc
	 */
	public boolean canUndo() {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * @inheritDoc
	 */
	public boolean redo() {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * @inheritDoc
	 */
	public boolean canRedo() {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * @inheritDoc
	 */
	public void addDataSource(DataSource ds) throws Exception {
		if (ds == null) {
			throw new NullPointerException("The DataSource argument to " +
					"DefaultTransaction.addDataSource cannot be null");
		}

		//get the set of DataSources involved
		Set dss = new HashSet();
		dss.add(ds);
		Iterator itr = ds.getDataModels().iterator();
		while (itr.hasNext()) {
			DataModel dm = (DataModel)itr.next();

			//locate the root data model in this data model heirarchy
			DataModel root = locateRootDataModel(dm);
			
			//get all of this heirarchies DataSources
			getHeirarchyDataSources(dss, root);
		}
		
		//now, remove each DataSource from its current transaction (if it
		//is part of a transaction)
		itr = dss.iterator();
		while (itr.hasNext()) {
			DataSource dataSource = (DataSource)itr.next();
			if (dataSource.getTransaction() != null) {
				dataSource.getTransaction().removeDataSource(dataSource);
			}
			//now set its transaction
			dataSource.setTransaction(this);
			dataSources.add(dataSource);
		}
	}
	
	/**
	 * @inheritDoc
	 */
	public void removeDataSource(DataSource ds) throws Exception {
		if (ds == null) {
			throw new NullPointerException("Cannot pass a null value to " +
					"DefaultTransaction.removeDataSource");
		}
		
		if (currentTx) {
			throw new Exception("Cannot remove a DataSource from a current " +
					"transaction! The transaction must either be rolled back," +
					" or committed prior to removing the DataSource");
		}
		
		//get the set of DataSources involved
		Set dss = new HashSet();
		dss.add(ds);
		Iterator itr = ds.getDataModels().iterator();
		while (itr.hasNext()) {
			DataModel dm = (DataModel)itr.next();

			//locate the root data model in this data model heirarchy
			DataModel root = locateRootDataModel(dm);
			
			//get all of this heirarchies DataSources
			getHeirarchyDataSources(dss, root);
		}
		
		//remove each DataSource
		itr = dss.iterator();
		while (itr.hasNext()) {
			DataSource dataSource = (DataSource)itr.next();
			dataSource.setTransaction(null);
		}
		dataSources.removeAll(dss);
	}

	/**
	 * Utility method for locating the root DataModel in a DataModel
	 * master/detail heirarchy
	 * @param dm
	 * @return
	 */
	private DataModel locateRootDataModel(DataModel dm) {
		DataModel root = dm;
		DataModel parent = null;
		while ((parent = root.getMasterDataModel()) != null) {
			root = parent;
		}
		return root;
	}

	/**
	 * Utility method to collect all of the DataSources used by a
	 * DataModel master/detail heirarchy into a Set
	 * @param dataSources
	 * @param dm
	 * @throws Exception
	 */
	private void getHeirarchyDataSources(Set dataSources, DataModel dm) throws Exception {
		dataSources.add(dm.getDataSource());
		
		Set keys = dm.getDetailKeys();
		Iterator itr = keys.iterator();
		while (itr.hasNext()) {
			DataModel detailDM = (DataModel)dm.getDetailDataModel((String)itr.next());
			getHeirarchyDataSources(dataSources, detailDM);
		}
	}
	
	/**
	 * Returns a copy of the Set so that modifications to the Set do not
	 * affect the actual Set of DataSources in this Transaction
	 * @inheritDoc
	 */
	public Set/*<DataSource*/ getDataSources() {
		return new HashSet(dataSources);
	}
	
	/**
	 * Returns a copy of the Set so that modifications to the Set do not
	 * affect the actual Set of DataModels in this Transaction
	 * @inheritDoc
	 */
	public Set/*<DataModel>*/ getDataModels() {
		Set dataModels = new HashSet();
		Iterator itr = dataSources.iterator();
		while (itr.hasNext()) {
			DataSource ds = (DataSource)itr.next();
			dataModels.addAll(ds.getDataModels());
		}
		return dataModels;
	}
}