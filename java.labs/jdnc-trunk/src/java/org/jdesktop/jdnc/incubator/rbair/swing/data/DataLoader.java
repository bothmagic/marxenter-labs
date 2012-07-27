/*
 * $Id: DataLoader.java 53 2004-09-10 01:55:52Z rbair $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */

package org.jdesktop.jdnc.incubator.rbair.swing.data;

import java.io.IOException;

import javax.swing.SwingUtilities;

import org.jdesktop.jdnc.incubator.rbair.swing.event.MessageListener;
import org.jdesktop.jdnc.incubator.rbair.swing.event.MessageSource;
import org.jdesktop.jdnc.incubator.rbair.swing.event.MessageSourceSupport;
import org.jdesktop.jdnc.incubator.rbair.swing.event.ProgressListener;
import org.jdesktop.jdnc.incubator.rbair.swing.event.ProgressSource;


/**
 * Interface which asynchronously loads data from a source into a model,
 * ensuring that the potentially lengthy operation does not block the user-interface.
 * <p>
 * Swing requires that all operations which directly affect the user-interface
 * component hierarchy execute on a single thread, the event dispatch thread (
 * <a href="http://java.sun.com/docs/books/tutorial/uiswing/misc/threads.html">
 * Swing's single-threaded GUI rule</a>).  Because of this, the task of streaming
 * data (potentially over the network) should not be performed on the event
 * dispatch thread because it would cause the user-interface to freeze and
 * become unresponsive.  And yet, while the reading of the data should be
 * off-loaded to a separate thread, it is desirable to incrementally load
 * portions of that data into the model as it's read so that the user can see
 * more immediate results than if he/she had to wait for the entire data
 * stream to be read before seeing any data at all.</p>
 * <p>
 * Implementations of this class support all the required thread and object synchronization
 * to support this asynchronous, incremental load operations.  It does this by
 * splitting the operation into three distinct steps:
 * <ol>
 * <li>read-meta-data: obtain any available meta-data from the stream which
 *                may provide structural and type information about the data.
 *                Meta-data is typically encoded in the beginning of the stream
 *                and is format-dependent.  This should be called only from
 *                the event dispatch thread since it will modify properties on
 *                the model and possibly generate events which affect user-interface
 *                components. However, this method *should* execute outside of the
 * 								event dispatch thread and then update the DataModel via
 * 								SwingUtilities.invokeAndWait or SwingUtilities.invokeLater.</li>
 * <li>read-data: reading the data from the input-stream and loading it into
 *                a Java data structure which is not connected to the data model.
 *                This step is typically the most time consuming, hence it is
 *                performed in its own &quot;reader&quot; thread which is created by the
 *                <code>startLoading</code> method.</li>
 * <li>load-data: taking the contents of the disconnected data structure and adding it to
 *                the data model.  This step must be performed on the event
 *                dispatch thread because it has the potential to generate
 *                events which affect the user-interface components.</li>
 * </ol>
 * A concrete implementation should implement 2 methods corresponding to these
 * steps:
 * <pre><code>
 *     public void initializeMetaData(Object model, InputStream is)
 *     public void startLoading(Object model, InputStream is)
 * </code></pre>
 *
 * TODO: This class is currently designed to be a one-shot wonder. That is, it is not threadsafe, and thus
 * cannot load multiple data simultaneously. It does interact with two threads (the thread it starts up in
 * the runLoader method, and the event dispatch thread), but that is all.
 * @author Amy Fowler
 * @version 1.0
 */
public abstract class DataLoader implements ProgressSource, MessageSource {
	/**
	 * Reference to a utility class that is used to notify the EDT 
	 */
  private LoadNotifier loadNotifier;
  /**
   * Utility class for handling messaging
   */
  private MessageSourceSupport mss;

  /**
   * Create a new DataLoader
   */
  protected DataLoader() {
      mss = new MessageSourceSupport(this);
  }

  /**
   * Adds the specified progress listener to this data loader.
   * @param l progress listener to be notified as data is loaded or errors occur
   */
  public void addProgressListener(ProgressListener l) {
      mss.addProgressListener(l);
  }

  /**
   * Removes the specified progress listener from this data loader.
   * @param l progress listener to be notified as data is loaded or errors occur
   */
  public void removeProgressListener(ProgressListener l) {
      mss.removeProgressListener(l);
  }

  /**
   *
   * @return array containing all progress listeners registered on this data loader
   */
  public ProgressListener[] getProgressListeners() {
      return mss.getProgressListeners();
  }

  /**
   * Adds the specified message listener to this data loader.
   * @param l message listener to be notified as data is loaded or errors occur
   */
  public void addMessageListener(MessageListener l) {
      mss.addMessageListener(l);
  }

  /**
   * Removes the specified message listener from this data loader.
   * @param l message listener to be notified as data is loaded or errors occur
   */
  public void removeMessageListener(MessageListener l) {
      mss.removeMessageListener(l);
  }

  /**
   *
   * @return array containing all message listeners registered on this data loader
   */
  public MessageListener[] getMessageListeners() {
      return mss.getMessageListeners();
  }
  
  /**
   * Opens any necessary connections to the source. For instance, this could be used 
   * to open a connection to a database, or to a URL
   * @throws IOException
   */
  protected abstract void open() throws Exception;
  
  /**
   * Closes any open connections
   */
  protected abstract void close();

  /**
   * Reads the meta data from the source and returns an array containing that
   * meta data. If the metaData is not available, then return an empty array
   * @return An empty array if no meta data is available, or an array of meta data
   * @throws IOException
   */
  protected abstract MetaData[] readMetaData() throws Exception;
  
  /**
   * Initializes the model with any meta-data available in the loader source.
   * The amount of meta-data available from the loader source is format-dependent.
   * This method is synchronous and will be invoked from the event dispatch thread.
   * @param metaData the meta-data recovered from the stream
   */
  protected abstract void loadMetaData(MetaData[] metaData);

  /**
   * Starts the asynchronous load operation by spinning up a separate thread
   * that will read the data from the loader source's input stream. This method
   * will return immediately.  Prior to calling this method, a MessageListener
   * should be registered to be notified as data is loaded or errors occur.
   * If the data model being loaded requires meta-data to describe its
   * structure (such as the number of columns in a tabular data row), then
   * that meta-data must be initialized before <code>startLoading</code>
   * is called and must not be modified while the load operation executes,
   * otherwise synchronization errors will occur.<br>
   * This method will hand off the actual construction and running of the
   * loading thread to the <code>runLoader</code> method. In this way,
   * concrete implementations may choose to construct a new thread for
   * every load operation, or to use a thread pool or a more complex
   * java 1.5 Executor based strategy.<br>
   * This method is protected, and is designed to be called ONLY from extending
   * classes. Each DataLoader implementation may want to specify a distinct subclass
   * of DataModel that it supports. For instance, a RowSetDataLoader only supports
   * RowSetDataModel's. By making this method protected, the RowSetDataLoader can specify
   * a <code>startLoading</code> method that takes only a RowSetDataModel, enforcing this
   * natural association. It can then pass the data model up to this method to actually
   * do the loading.
   * @see #addProgressListener
   * @param model the data model being loaded from the input stream
   */
  protected void startLoading(final Object model) {
    loadNotifier = new LoadNotifier(this, model);
    Runnable task = new Runnable() {
      public void run() {
      	fireProgressStarted(1,1); // indeterminate
        try {
        	//open the source (for instance, the URL's input stream)
        	open();
        	//ask the subclass to read meta data from the source.
        	final MetaData[] metaData = readMetaData();
        	//If meta data is read, then in the EDT set the meta data on the model
        	if (metaData != null) {
        		SwingUtilities.invokeLater(new Runnable() {
        			public void run() {
        				loadMetaData(metaData);
        			}
        		});
        	}
          readData();
          scheduleLoad();
          fireProgressEnded();
        }
        catch (Exception e) {
          final Throwable error = e;
         	fireException(error);
         	fireProgressEnded();
        } finally {
        	close();
        }
      }
    };
  	runLoader(task);
  }

  /**
   * Invoked by the <code>startLoading</code> method. This method will be called
   * on the EventDispatch thread, and therefore must not block. This method is
   * provided to allow concrete subclasses to provide a custom thread creation /
   * scheduling implementation.
   * @param runner
   */
  protected abstract void runLoader(Runnable runner);
  
  /**
   * Invoked by the <code>startLoading</code> method.  This method will be
   * called on a separate &quot;reader&quot; thread.  Subclasses must implement
   * this method
   * to read the data from the stream and place it in a data structure which
   * is <b>disconnected</b> from the data model.   When increments of data
   * are ready to be loaded into the model, this method should invoke
   * <code>scheduleLoad</code>, which will cause <code>loadData</code>
   * to be called on the event dispatch thread, where the model may be
   * safely updated.  Progress events may be fired from this method.<br>
   * A final <code>scheduleLoad</code> will be called automatically at the conclusion
   * of this method, so it is not technically necessary for this method to call
   * <code>scheduleLoad</code> at all unless you want to do partial loads.
   * @see #scheduleLoad
   * @throws IOException if errors occur while reading data from the input stream
   */
  protected abstract void readData() throws IOException;

  /**
   * Invoked internally once the <code>readData</code> method calls
   * <code>scheduleLoad</code> to schedule the loading of an increment of
   * data to the model.  This method is called on the event dispatch
   * thread, therefore it is safe to mutate the model from this method.
   * Subclasses must implement this method to load the current contents of the
   * disconnected data structure into the data model.  Note that because
   * there is an unpredictable delay between the time <code>scheduleLoad</code>
   * is called from the &quot;reader&quot; thread and <code>loadData</code>
   * executes on the event dispatch thread, there may be more data available
   * for loading than was available when <code>scheduleLoad</code> was
   * invoked.  All available data should be loaded from this method.
   * <p>
   * This method should fire an appropriate progress event to notify progress
   * listeners when:
   * <ul>
   * <li>incremental load occurs(for determinate load operations)</li>
   * <li>load completes</li>
   * <li>exception occurs</li>
   * </ul>
   * </p>
   * @see #fireProgressStarted(int, int)
   * @see #fireProgressEnded()
   * @see #fireException
   */
  protected abstract void loadData(Object model);

  /**
   * Invoked by the <code>readData</code> method from the &quot;reader&quot;
   * thread to schedule a subsequent call to <code>loadData</code> on the
   * event dispatch thread.  If <code>readData</code> invokes
   * <code>scheduleLoad</code> multiple times before <code>loadData</code>
   * has the opportunity to execute on the event dispatch thread, those
   * requests will be collapsed, resulting in only a single call to
   * <code>loadData</code>.
   * @see #readData
   * @see #loadData
   */
  protected void scheduleLoad() {
      synchronized (loadNotifier) {
          if (!loadNotifier.isPending()) {
              loadNotifier.setPending(true);
              SwingUtilities.invokeLater(loadNotifier);
          }
      }
  }

  /**
   * Fires event indicating that the load operation has started.
   * For a determinite progress operation, the minimum value should be less than
   * the maximum value. For inderminate operations, set minimum equal to maximum.
   *
   * @param minimum the minimum value of the progress operation
   * @param maximum the maximum value of the progress operation
   */
  protected void fireProgressStarted(final int minimum, final int maximum) {
  	SwingUtilities.invokeLater(new Runnable() {
  		public void run() {
  			mss.fireProgressStarted(minimum, maximum);
  		}
  	});
  }

  /**
   * Fires event indicating that an increment of progress has occured.
   * @param progress total value of the progress operation. This
   *                 value should be between the minimum and maximum values
   */
  protected void fireProgressIncremented(final int progress) {
  	SwingUtilities.invokeLater(new Runnable() {
  		public void run() {
  			mss.fireProgressIncremented(progress);
  		}
  	});
  }

  /**
   * Fires event indicating that an increment of progress has occured.
   * @param progress total value of the progress operation. This
   *                 value should be between the minimum and maximum values
   */
  protected void fireProgressIncremented(final int progress, final int min, final int max, final String msg) {
  	SwingUtilities.invokeLater(new Runnable() {
  		public void run() {
//  			mss.fireProgressIncremented(progress, min, max, msg);
  		}
  	});
  }

  /**
   * Fires event indicating that the load operation has completed
   */
  protected void fireProgressEnded() {
  	SwingUtilities.invokeLater(new Runnable() {
  		public void run() {
  			mss.fireProgressEnded();
  		}
  	});
  }

  protected void fireException(final Throwable t) {
  	SwingUtilities.invokeLater(new Runnable() {
  		public void run() {
  			mss.fireException(t);
  		}
  	});
  }

  protected void fireMessage(final String message) {
  	SwingUtilities.invokeLater(new Runnable() {
  		public void run() {
  			mss.fireMessage(message);
  		}
  	});
  }

  private class LoadNotifier implements Runnable {
      private DataLoader loader;
      private boolean pending = false;
      private Object model;

      LoadNotifier(DataLoader loader, Object model) {
          this.loader = loader;
          this.model = model;
      }

      public synchronized void setPending(boolean pending) {
          this.pending = pending;
      }

      public synchronized boolean isPending() {
          return pending;
      }

      public void run() {
          synchronized (this) {
              loader.loadData(model);
              setPending(false);
          }
      }
  }

}
