/*
 * $Id: DataSource.java 148 2004-10-29 20:43:46Z rbair $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */

package org.jdesktop.jdnc.incubator.rbair.swing.data;

import java.io.IOException;
import java.util.List;

import org.jdesktop.jdnc.incubator.rbair.swing.event.MessageListener;
import org.jdesktop.jdnc.incubator.rbair.swing.event.ProgressListener;

/**
 * The DataSource manages the communication between a data store (such as
 * a database, or a remote web service server, or a filesystem) and one or
 * more DataModels. Communication between a DataSource and its DataModels is
 * asynchronous. The DataModel typically executes within the event-dispatch
 * thread while the DataSource will typically execute on one or more background
 * threads, depending on the DataSource implementation.
 * <p/>
 * DataSource and DataModel implementations come in pairs. The DataSource knows
 * how to get the information from the data store, but the DataModel
 * implementation must know both how to ask for the data (for instance, the sql
 * query to execute or the remote method to call) and it must know how to
 * configure itself with the results (for example, how to handle an incoming
 * RowSet or SOAP response). Hence, a specific implementation of DataSource will
 * generally support a specific subset of implementations of DataModel, and
 * vice versa.
 * <p/>
 * Two specific benefits of this architecture are:
 * <ol>
 * <li>The DataModel manages master/detail information. It constructs the proper
 * query (or whatnot) to retrieve the proper data based on the current record
 * in the master</li>
 * <li>If the DataModel was not already attached to this DataSource and 
 * becomes attached and if the DataSource is connected, then this DataSource
 * will immediately populate the given DataModel.</li>
 * </ol>
 * As part of the JDNC data modeling and binding architecture, Transactions can
 * be defined and used from within the gui. At various times during the
 * execution of the gui it may become necessary to temporarily persist some
 * data to the data store. This persistence may occur within the context of
 * a transaction. Therefore, it is necessary for the DataSource API to support
 * the concept of transactions on the data store. The DataSource API has the
 * following four methods:
 * <ul>
 * <li><b>setAutoCommit</b> - If set to true, then any &quot;saves&quot; that
 * occur will be committed to the underlying data store immediately. In this
 * mode, transactions in essence do not occur. If set to false, then any
 * &quot;saves&quot; that occur will be within the context of a transaction,
 * which will &quot;live&quot; until either commit or rollback is called.</li>
 * <li><b>canCommit</b> - Returns true if a subsequent call to commit will
 * succeed on the data store. This is part of a two-phase commit process.
 * <li><b>commit</b> - Commits the current transaction in the data store.
 * This call does nothing if autoCommit is true.</li>
 * <li><b>rollback</b> - Rolls back the current transaction in the data store.
 * This call does nothing if autoCommit is true.</li>
 * </ul>
 * TODO The message/progress portion of this interface was copied directly
 * from the old DataLoader class. Before making this a final portion of the
 * API, consider the comments in thread 
 * http://www.javadesktop.org/forums/thread.jspa?forumID=53&threadID=4585
 * @author Richard Bair
 */
public interface DataSource {
	/**
	 * Manages the connected state of this DataSource. The connected state
	 * is this DataSources "logical" connection status. The underlying
	 * connection may be disconnected, and yet the DataSource may still be
	 * "logically" connected. For example, an HTTP based connection
	 * may not be persistent, but is always "logically" connected.
	 * <p/>
	 * When the logical connection is terminated, DataModels are told to
	 * flush themselves of all data, and consider themselves in an invalid
	 * state. New records should not be added to DataModels that are not
	 * connected, nor should records be deleted.
	 * <p/>
	 * The actual method by which a DataSource causes a DataModel to flush its
	 * data (that is, clear itself) is an implementation detail between the
	 * DataSource implementation and the DataModel implementation, and is not
	 * specified in either this or the DataModel interface.
	 * @param b If true, opens any necessary connections to the source.
	 *          For instance, this could be used to open a connection to a 
	 *          database, or to a URL. If false, then any current connections
	 *          are logically closed, and may be physically closed, depending
	 *          on the DataSource implementation
	 * @throws IOException
	 */
	public void setConnected(boolean b);
	  
	/**
	 * @return whether the DataSource is logically connected or not.
	 */
	public boolean isConnected();
	  
	/**
	 * Sets the transaction that this DataSource is associated with. If the
	 * transaction cannot be set to the given Transaction <code>t</code>, then
	 * an exception will be thrown.
	 * <p>
	 * This method is *NOT* responsible for detecting other DataSources in the
	 * DataModel master/detail graph. That responsibility lies with the
	 * Transaction implementation. The DataSource must simply handle switching
	 * itself to the given transaction.
	 * <p>
	 * @param t may be null. If <code>t</code> is null, then auto-commit must
	 * be set to true.
	 */
	public void setTransaction(Transaction t) throws Exception;
	
	/**
	 * @return the transaction associated with this DataModel. May be null.
	 */
	public Transaction getTransaction();

	/**
	 * Sets the auto-commit field to be equal to the given val. If val equals
	 * the current state, nothing occurs. If the current auto-commit state is
	 * true and val is false, then a new transaction is begun and the
	 * auto-commit field changes to false. If auto-commit is false and val is
	 * true, then an exception will be thrown. The calling method should ensure
	 * that this situation does not arise by committing or rolling back the
	 * transaction first.
	 * <p>
	 * It is also possible that this DataSource does not support Transactions
	 * and therefore is always in auto-commit mode. For such a DataSource, if
	 * <code>val</code> is ever true, it should throw an
	 * <code>IllegalArgumentException</code>. TODO Should we rather create a
	 * TransactionNotSupported exception?
	 * @param val
	 * @throws Exception
	 */
	public void setAutoCommit(boolean val) throws Exception;
	
	/**
	 * @return true if a subsequent call to commit() will succeed, false
	 * otherwise. Used as part of a two step commit process. If canCommit
	 * returns true, but the actual commit fails, it is possible to end up
	 * with a corrupted transaction which is half committed.
	 */
	public boolean canCommit();
	
	/**
	 * Commits the current transaction in the data store.
	 * If the Transaction is in auto-commit mode, then nothing will be done.
	 * If an exception occurs during the commit phase, then an exception will 
	 * be thrown. The transaction is not automatically rolled back, however,
	 * so that the users do not loose their data if the server has gone
	 * offline, for instance.
	 * @throws Exception
	 */
	public void commit() throws Exception;
	
	/**
	 * Rolls back the current transaction in the data store. An exception is
	 * thrown if the data store cannot honor the rollback request for whatever
	 * reason. This method should not refresh any DataModels. The Transaction
	 * implementation is responsible for refresing DataModels after rolling
	 * back or committing a transaction.
	 * @throws Exception
	 */
	public void rollback() throws Exception;
	
	/**
	 * @return a copy of the list of DataModels associated with this DataSource
	 */
	public List getDataModels();
  
	/**
	 * Refreshes all of the data models associated with this DataSource
	 */
	public void refreshAllDataModels();
}