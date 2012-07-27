/*
 * $Id: AbstractDataSource.java 148 2004-10-29 20:43:46Z rbair $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */

package org.jdesktop.jdnc.incubator.rbair.swing.data;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;

import javax.swing.Icon;
import javax.swing.SwingUtilities;

import org.jdesktop.jdnc.incubator.rbair.swing.Application;
import org.jdesktop.jdnc.incubator.rbair.swing.Progressable;
import org.jdesktop.jdnc.incubator.rbair.swing.event.MessageEvent;
import org.jdesktop.jdnc.incubator.rbair.swing.event.MessageListener;
import org.jdesktop.jdnc.incubator.rbair.swing.event.ProgressEvent;
import org.jdesktop.jdnc.incubator.rbair.swing.event.ProgressListener;

/**
 * An abstract implementation of the DataSource interface. This implementation
 * handles the management of listeners, and provides <code>fireXXX</code>
 * methods for firing events.
 * <p>
 * This class also handles the logical &quot;connected&quot; state of the
 * DataSource. When the state changes from !connected to connected, the
 * protected <code>connect</code> method is called. When the state is changed
 * from connected to !connected, the protected <code>disconnect</code> method
 * is called. These methods provide an extension point for specific connection
 * and disconnection behavior. Child class implementations may override these
 * methods to perform connection management. The default implementations of
 * these methods do nothing.
 * 
 * @author Richard Bair
 */
public abstract class AbstractDataSource implements DataSource {
    /**
     * Helper used for notifying of bean property changes. In particular, this
     * is used to notify of changes in the connection status
     */
    private PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    /**
     * Flag indicating the logical connection status of this DataSource. It is
     * possible for the DataSource to be logically connected but physically
     * disconnected (ie HTTP), or vice versa.
     */
    private boolean connected = false;

    /**
     * List of DataModels managed by this DataSource
     */
    protected List dataModels = new ArrayList();

    /**
     * The Transaction that this DataSource is associated with
     */
    private Transaction tx = null;

    /**
     * The auto-commit flag
     */
    protected boolean autoCommit = true;

    /**
     * thread pool from which to get threads to execute the loader 1.5
     * dependant! Oh, I pine for the time when we compile to 1.5... This will be
     * the default implementation of runTask in 1.5
     */
    //    private final Executor EX = Executors.newCachedThreadPool();
    /**
     * @inheritDoc
     */
    public void setAutoCommit(boolean val) throws Exception {
        if (val != autoCommit) {
            if (!val && autoCommit) {
                autoCommit = false;
            } else if (val && !autoCommit && isCurrentTx()) {
                throw new IllegalArgumentException(
                        "Cannot set auto-commit "
                                + "to true while in the middle of a transaction. "
                                + "The current Transaction must either be committed "
                                + "or rolled back prior to changing auto-commit to true");
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
     * @return true if a Transaction is currently under way. If auto-commit is
     *         true, then this method always returns false. If auto-commit is
     *         false, then this method returns true if, and only if, an update
     *         has been performed against the data store during the current
     *         transaction.
     */
    public abstract boolean isCurrentTx();

    /**
     * 
     * @param listener
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    /**
     * 
     * @param propertyName
     * @param listener
     */
    public void addPropertyChangeListener(String propertyName,
            PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(propertyName, listener);
    }

    /**
     * @inheritDoc
     */
    public List getDataModels() {
        return new ArrayList(dataModels);
    }

    /**
     * @inheritDoc
     */
    public Transaction getTransaction() {
        return tx;
    }

    /**
     * @inheritDoc
     */
    public void setTransaction(Transaction t) throws Exception {
        //TODO May need to add code to remove this DataSource from the
        //old Transaction, but that is also handled in DefaultTransaction
        tx = t;
        if (t == null) {
            setAutoCommit(true);
        }
    }

    /**
     * @inheritDoc
     */
    public void setConnected(boolean b) {
        if (b && !connected) {
            try {
                connect();
                connected = true;
                pcs.firePropertyChange("connected", false, true);
            } catch (Exception e) {
                e.printStackTrace();
                connected = false;
            }
        } else if (!b && connected) {
            try {
                disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                connected = false;
                pcs.firePropertyChange("connected", true, false);
            }
        }
    }

    /**
     * @inheritDoc
     */
    public boolean isConnected() {
        return connected;
    }

    /**
     * Optional method to make a connection to the data store. This method is
     * called whenever the connection state changes from !connected to
     * connected. It is up to the child class implementation to manage the
     * physical connection to the data store. This method may or may not be
     * useful in that context.
     * 
     * @throws Exception
     */
    protected void connect() throws Exception {
    }

    /**
     * Optional method to disconnect from the data store. This method is called
     * whenever the connection state changes from connected to !connected. It is
     * up to the child class implementation to manage the physical connection to
     * the data store. This method may or may not be useful in that context.
     * 
     * @throws Exception
     */
    protected void disconnect() throws Exception {
    }

    //Callback methods for extending DataSources to implement. These methods
    //are all executed on a background thread.

    /**
     * Starts an asynchronous refresh operation by spinning up a separate thread
     * that will read the data from the backing data store. This method will
     * return immediately. Prior to calling this method, a MessageListener
     * should be registered to be notified as data is loaded or errors occur. If
     * the data model being loaded requires meta-data to describe its structure
     * (such as the number of columns in a tabular data row), then that
     * meta-data must be initialized before <code>refresh</code> is called and
     * must not be modified while the load operation executes, otherwise
     * synchronization errors will occur.
     * <p>
     * This method will hand off the actual construction and running of the
     * loading thread to the <code>runTask</code> method. In this way,
     * concrete implementations may choose to construct a new thread for every
     * load operation, or to use a thread pool or a more complex java 1.5
     * Executor based strategy.
     * <p>
     * This method is protected, and is designed to be called ONLY from
     * extending classes. Each AbstractDataSource implementation may want to
     * specify a distinct subclass of DataModel that it supports. For instance,
     * a RowSetDataSource only supports RowSetDataModel's. By making this method
     * protected, the RowSetDataSource can specify a <code>refresh</code>
     * method that takes only a RowSetDataModel, enforcing this natural
     * association. It can then pass the data model up to this method to
     * actually do the loading.
     * 
     * @see #addProgressListener
     * @param models
     *            an array of DataModels being refreshed
     */
    protected void refresh(DataModel[] models) {
        Task task = createLoadTask(models);
        runTask(task);
    }

    /**
     * Starts an asynchronous save operation by spinning up a separate thread
     * that will save the given DataModels' data to the backing data store. This
     * method will return immediately. Prior to calling this method, a
     * MessageListener should be registered to be notified as data is saved or
     * errors occur.
     * <p>
     * This method will hand off the actual construction and running of the
     * loading thread to the <code>runTask</code> method. In this way,
     * concrete implementations may choose to construct a new thread for every
     * load operation, or to use a thread pool or a more complex java 1.5
     * Executor based strategy.
     * <p>
     * This method is protected, and is designed to be called ONLY from
     * extending classes. Each AbstractDataSource implementation may want to
     * specify a distinct subclass of DataModel that it supports. For instance,
     * a RowSetDataSource only supports RowSetDataModel's. By making this method
     * protected, the RowSetDataSource can specify a <code>save</code> method
     * that takes only RowSetDataModels, enforcing this natural association. It
     * can then pass the DataModels up to this method to actually do the saving.
     * 
     * @see #addProgressListener
     * @param models
     *            an array of DataModels being saved
     */
    protected void save(DataModel[] models) {
        Task task = createSaveTask(models);
        runTask(task);
    }

    protected abstract SaveTask createSaveTask(DataModel[] models);
    
    protected abstract LoadTask createLoadTask(DataModel[] models);
    
    /**
     * Invoked by the <code>refresh</code> or <code>save</code> methods.
     * This method will be called on the EventDispatch thread, and therefore
     * must not block. This method is provided to allow concrete subclasses to
     * provide a custom thread creation/scheduling implementation.
     * 
     * @param runner
     */
    protected void runTask(Task runner) {
        //			EX.execute(runner);
        Thread th = new Thread(runner);
        Application.getInstance().getProgressManager().addProgressable(runner);
        th.start();
    }

    protected interface Task extends Runnable, Progressable {
        
    }
    
    protected abstract static class AbstractTask implements Task {
        private int min = 0;
        private int max = 100;
        private int progress = 0;
        private boolean indeterminate = true;
        private boolean cancellable = false;
        private boolean modal = true;

        protected void setMinimum(int val) {
            min = val < 0 || val > max ? 0 : val;
        }
        
        protected void setMaximum(int val) {
            max = val < 0 || val < min ? min : val;
        }
        
        protected void setProgress(int progress) {
            this.progress = progress < 0 ? 0 : progress;
        }
        
        protected void setIndeterminate(boolean b) {
            indeterminate = b;
        }

        public int getMinimum() {
            return min;
        }

        public int getMaximum() {
            return max;
        }

        public int getProgress() {
            return progress;
        }

        public boolean isIndeterminate() {
            return indeterminate;
        }

        public boolean isModal() {
            return modal;
        }

        public boolean canCancel() {
            return cancellable;
        }
        
        public void setModel(boolean b) {
            modal = b;
        }
        
        public void setCanCancel(boolean b) {
            cancellable = b;
        }
        
    }
    
    protected abstract static class LoadTask extends AbstractTask {
        /**
         * This linked list contains "LoadItem" objects. As a specific unit of data
         * is loaded (for instance, all of the data needed for a specific
         * DataModel), a new LoadItem is created and placed at the tail of the
         * loadQueue. The scheduleLoad method then executes and the current items in
         * the queue are removed and passed to the loadData method. This queue is
         * accessed from multiple threads (many threads put data into the queue, one
         * thread removes items from the queue). Synchronization occurs on the
         * loadQueue.
         */
        private LinkedList loadQueue = new LinkedList();

        /**
         * Object used for collescing multiple calls to scheduleLoad into one call
         * to loadData.
         */
        private LoadNotifier loadNotifier = new LoadNotifier();
        
        private DataModel[] models;

        public LoadTask(DataModel[] models) {
            this.models = models == null ? new DataModel[0] : models;
        }
        
        public void run() {
            setIndeterminate(true);
            try {
                //ask the subclass to read meta data from the source.
                for (int currentIndex = 0; currentIndex < models.length; currentIndex++) {
                    final MetaData[] metaData = readMetaData(models[currentIndex]);
                    //If meta data is read, then in the EDT set the meta data
                    // on the model
                    if (metaData != null) {
                        final int index = currentIndex;
                        SwingUtilities.invokeLater(new Runnable() {
                            public void run() {
                                loadMetaData(models[index], metaData);
                            }
                        });
                    }
                }
                readData(models);
                scheduleLoad();
                setProgress(getMaximum());
            } catch (Exception e) {
                final Throwable error = e;
                e.printStackTrace();
//                fireException(error);
                setProgress(getMaximum());
            } finally {
//          	        	close();
            }
        }
        
        /**
         * Invoked by the <code>refresh</code> method. This method will be called
         * on a separate &quot;reader&quot; thread. Subclasses must implement this
         * method to read the data from the data store and place it in a data
         * structure which is <b>disconnected </b> from the DataModel. When
         * increments of data are ready to be loaded into the model, this method
         * should invoke <code>scheduleLoad</code>, which will cause
         * <code>loadData</code> to be called on the event dispatch thread, where
         * the model may be safely updated. Progress events may be fired from this
         * method.
         * <p>
         * A final <code>scheduleLoad</code> will be called automatically at the
         * conclusion of the refreshing process, so it is not technically necessary
         * for this method to call <code>scheduleLoad</code> at all unless you
         * want to support partial loads.
         * 
         * @see #scheduleLoad
         * @throws Exception
         *             if errors occur while reading data
         */
        protected abstract void readData(final DataModel[] models) throws Exception;

        /**
         * Reads MetaData from the data store for the given DataModel and returns an
         * array containing that MetaData. If the MetaData is not available, then
         * return an empty array
         * 
         * @param dm
         *            The DataModel whose MetaData is to be retrieved and returned.
         *            This DataModel will be one of the DataModels managed by the
         *            DataSource
         * @return An empty array if no MetaData is available, or an array of
         *         MetaData
         * @throws IOException
         */
        protected abstract MetaData[] readMetaData(DataModel dm) throws Exception;

        /**
         * Initializes the given DataModel with any meta-data available from a
         * previous call to <code>readMetaData</code>. The amount of meta-data
         * available is DataSource implementation dependent. This method is
         * synchronous and will be invoked from the event dispatch thread.
         * 
         * @param dm
         *            the DataModel whose MetaData should be set to
         *            <code>MetaData</code>
         * @param metaData
         *            the meta-data recovered from the stream
         */
        protected abstract void loadMetaData(DataModel dm, MetaData[] metaData);

        /**
         * Invoked internally once the <code>readData</code> method calls
         * <code>scheduleLoad</code> to schedule the loading of an increment of
         * data to the model. This method is called on the event dispatch thread,
         * therefore it is safe to mutate the model from this method. Subclasses
         * must implement this method to load the current contents of the
         * disconnected data structure into the data model. Note that because there
         * is an unpredictable delay between the time <code>scheduleLoad</code> is
         * called from the &quot;reader&quot; thread and <code>loadData</code>
         * executes on the event dispatch thread, there may be more data available
         * for loading than was available when <code>scheduleLoad</code> was
         * invoked. All available data should be loaded from this method.
         * <p>
         * This method should fire an appropriate progress event to notify progress
         * listeners when:
         * <ul>
         * <li>incremental load occurs(for determinate load operations)</li>
         * <li>load completes</li>
         * <li>exception occurs</li>
         * </ul>
         * </p>
         * 
         * @see #fireProgressStarted(int, int)
         * @see #fireProgressEnded()
         * @see #fireException
         */
        protected abstract void loadData(LoadItem[] items);

        /**
         * Invoked by the <code>readData</code> method from the &quot;reader&quot;
         * thread to schedule a subsequent call to <code>loadData</code> on the
         * event dispatch thread. If <code>readData</code> invokes
         * <code>scheduleLoad</code> multiple times before <code>loadData</code>
         * has the opportunity to execute on the event dispatch thread, those
         * requests will be collapsed, resulting in only a single call to
         * <code>loadData</code>.
         * 
         * @see #readData
         * @see #loadData
         */
        protected void scheduleLoad(LoadItem item) {
            synchronized (loadQueue) {
                if (item != null) {
                    loadQueue.addLast(item);
                }
                if (!loadNotifier.isPending()) {
                    loadNotifier.setPending(true);
                    SwingUtilities.invokeLater(loadNotifier);
                }
            }
        }

        /**
         * Same as <code>scheduleLoad(LoadItem)</code>, except that this method
         * will simply schedule a load operation for any remaining items in the
         * queue, but will not add any items to the queue.
         */
        protected void scheduleLoad() {
            scheduleLoad(null);
        }

        public String getDescription() {
            return "<html><h3>Loading data</h3></html>";
        }

        public Icon getIcon() {
            // TODO Auto-generated method stub
            return null;
        }

        public String getMessage() {
            return "Loading item " + (getProgress() + 1) + " of " + getMaximum();
        }

        public boolean cancel() throws Exception {
            // TODO Auto-generated method stub
            return false;
        }

        public static final class LoadItem {
            public DataModel dataModel;

            public Object model; // could be a rowset, or perhaps a JavaBean
                                 // object...

            public LoadItem(DataModel dm, Object model) {
                this.dataModel = dm;
                this.model = model;
            }
        }

        private class LoadNotifier implements Runnable {
            private boolean pending = false;

            LoadNotifier() {
            }

            public synchronized void setPending(boolean pending) {
                this.pending = pending;
            }

            public synchronized boolean isPending() {
                return pending;
            }

            public void run() {
                synchronized (loadQueue) {
                    if (loadQueue.size() > 0) {
                        LoadItem[] items = (LoadItem[]) loadQueue
                                .toArray(new LoadItem[loadQueue.size()]);
                        loadQueue.clear();
                        loadData(items);
                    }
                    setPending(false);
                }
            }
        }
    }
    
    protected abstract static class SaveTask extends AbstractTask {
        private DataModel[] models;

        public SaveTask(DataModel[] models) {
            this.models = models == null ? new DataModel[0] : models;
        }
        
        public void run() {
            setIndeterminate(true);
            try {
                saveData(models);
                setProgress(getMaximum());
            } catch (Exception e) {
                final Throwable error = e;
                e.printStackTrace();
//                fireException(error);
                setProgress(getMaximum());
            } finally {
//          	        	close();
            }
        }
        
        protected abstract void saveData(DataModel[] models) throws Exception;

        public String getDescription() {
            // TODO Auto-generated method stub
            return "<html><h3>Saving data</h3></html>";
        }

        public Icon getIcon() {
            // TODO Auto-generated method stub
            return null;
        }

        public String getMessage() {
            // TODO Auto-generated method stub
            return "Saving item " + (getProgress() + 1) + " of " + getMaximum();
        }

        public boolean cancel() throws Exception {
            // TODO Auto-generated method stub
            return false;
        }
        
    }
}
