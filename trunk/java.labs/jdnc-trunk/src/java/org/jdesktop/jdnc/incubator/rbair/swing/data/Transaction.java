/*
 * $Id: Transaction.java 128 2004-10-18 20:06:27Z rbair $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.jdnc.incubator.rbair.swing.data;

import java.util.Set;

/**
 * A transaction represents a unit of work that can either be rolled back,
 * or committed. If committed, all of the work is committed or none of it is.
 * <p>
 * This Transaction interface represents a single transaction in this client
 * VM. The transaction may include multiple DataModels. DataModels bound
 * in a master/detail heiarchy are by nature part of the same transaction.
 * Because it is possible for a master/detail heirarchy to span multiple
 * DataSources (that is, since a master/detail heirarchy can theoretically be 
 * established between DataModels tied to different DataSources), a single
 * transaction can span multiple DataSources. However, it is not possible for o
 * nly part of a DataSource to be involved in a transaction. 
 * These considerations must be taken into account when coding a custom
 * DataSource.
 * <p>
 * The transaction interface supports the concepts of committing a transaction
 * and rolling back a transaction. A transaction is always in progress, except
 * when committing or rolling back. If auto-commit is true, then any time a
 * value change is made in any DataModel within this transaction, that change
 * is propogated to the data store via the DataSource. Also, at any time
 * during program execution the GUI may request that the DataSource save
 * information to the data store <em>while in the current transaction</em>. If
 * a DataSource was implemented to connect to a JDBC 2.0 compliant database,
 * for example, it could start a transaction with the database and save the
 * information to the database.
 * <p>
 * If your application supports distributed client transaction functionality,
 * the default implementation of the Transaction interface will not be
 * sufficient, and you will need to write a custom distributed Transaction
 * implementation. That custom implementation should bypass the DataSource
 * objects when communicating with the remote transaction peers. Use of the
 * DataSources should be limited to commit/rollback functionality.
 * <p>
 * The Transaction interface also supports undo/redo functionality.
 * Transactions, therefore, contain a log of what edits were perfomed to which
 * DataModels so that those edits can be repealed or replayed as the situation
 * may demand. Note that some Swing components contain their own Undo framework.
 * This is not an attempt to supercede that framework, but rather, to support
 * it. For example, the user may have added an item to a list, then changed the
 * description of that item, and then made many changes in a JEditorPane for
 * that item. The JEditorPane, using the Swing Undo framework, would allow the
 * user to type 'ctrl-z' repeatedly to undo the changes in that component.
 * However, if the user wanted to undo the changes in that component, and the
 * description component and also wanted to remove the item added to the list,
 * then a more encompassing undo framework must exist that spans components.
 * This Transaction architecture is just such a framework. Operations can be
 * undone up to the last commit point. Thus, if in auto-commit mode, undo/redo
 * are null ops.
 * <p>
 * Another very important task of the Transaction is to propogate changes
 * from one DataModel within the Transaction to another. For example, say you
 * have an object of type Person p, and two DataModels called A and B.
 * If both of those DataModels are bound to the same Person 
 * (in JavaBeanDataModel speak, A.setJavaBean(p), B.setJavaBean(p)), 
 * then when A.setValue("firstName", "Richard") is called the components bound 
 * to A will be updated and A must then somehow propogate notice of that change
 * to B so that B may update the components bound to B. However, this should
 * only occur if both A and B are in the same transaction. Thus, the transaction
 * takes responsibility for propogating these changes throughout the DataModels
 * within the active Transaction.
 * <p>
 * @author Richard Bair
 */
public interface Transaction {
	/**
	 * Commits the current transaction. If the Transaction is in auto-commit
	 * mode, then nothing will be done. If an exception occurs during the
	 * commit phase (for example, if one of the DataSources involved has lost
	 * its connection to the data store), then the entire commit will fail and
	 * an exception will be thrown. The transaction is not automatically rolled
	 * back, however, so that the user does not loose their data if the server
	 * has gone offline, for instance. 
	 * @throws Exception
	 */
	public void commit() throws Exception;
	/**
	 * Rolls back the current transaction, and initiates a refresh from
	 * the data store(s). In particular, this method will ask each root
	 * DataModel's DataSource (that is, the DataSource associated with a
	 * DataModel that has no master) to refresh the DataModel. Detail DataModels
	 * will automatically be refreshed by the DataSource and the master/detail
	 * API.
	 * @throws Exception
	 */
	public void rollback() throws Exception;
	/**
	 * Sets the auto-commit field to be equal to the given val. If val equals
	 * the current state, nothing occurs. If the current auto-commit state is
	 * true and val is false, then a new transaction is begun and the
	 * auto-commit field changes to false. If auto-commit is false and val is
	 * true, then an exception will be thrown. The calling method should ensure
	 * that this situation does not arise by committing or rolling back the
	 * transaction first.
	 * @param val
	 * @throws Exception
	 */
	public void setAutoCommit(boolean val) throws Exception;
	/**
	 * Indicates whether this Transaction is in auto-commit mode or not.
	 * @return
	 */
	public boolean isAutoCommit();
	/**
	 * Undoes the last edit performed in this Transaction. If canUndo is true,
	 * then this method will undo the last edit and return true. It will return
	 * false if for any reason it was unable to undo an edit. 
	 */
	public boolean undo();
	/**
	 * @return true if there are edits that can be undone
	 */
	public boolean canUndo();
	/**
	 * Re-does an edit that has been undone in this Transaction. If canRedo is
	 * true, then this method will redo the next edit and return true. It will
	 * return false if it was unable to redo an edit.
	 * @return
	 */
	public boolean redo();
	/**
	 * @return true if there are edits that can be redone
	 */
	public boolean canRedo();
	/**
	 * Adds a DataSource to the set of DataSources within this transaction. No
	 * DataSource can be in more than one transaction concurrently. Also, if 
	 * a DataModel within the DataSource is part of a master/detail heirarchy, 
	 * then all DataModels (and thus, the DataSources) within that heirarchy
	 * will be moved into this Transaction. If any one of them were previously
	 * part of an uncommitted transaction, then an exception will be thrown.
	 * @param ds
	 */
	public void addDataSource(DataSource ds) throws Exception;
	/**
	 * Removes the given DataSource, and thus all of the DataModels managed by
	 * that DataSource, from the set of DataSources within this
	 * transaction. If a DataModel within this DataSource is part of a 
	 * master/detail heirarchy, then all of the DataModels (and their respective
	 * DataSources, recursively) within that heirarchy will be removed from this
	 * Transaction.
	 * @param ds
	 * @throws Exception If this is in a currently uncommitted/unrolled back
	 * transaction (that is, if the transaction is current).
	 */
	public void removeDataSource(DataSource ds) throws Exception;
	/**
	 * @return the set of DataSources within this Transaction.
	 */
	public Set getDataSources();
	/**
	 * @return the Set of DataModels in this Transaction.
	 */
	public Set getDataModels();
}
