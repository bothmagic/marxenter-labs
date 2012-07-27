/*
 * $Id: SwingTask.java 726 2005-10-14 19:06:20Z rbair $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingworker;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.Icon;

/**
 * Represents an item that progresses, and can be managed by the
 * ProgressManager. Such an item has a percent completed, a description,
 * a status message, and is cancellable. SwingTask delegates to a SwingWorker
 * to perform the actual work, and simply provides the necessary UI information
 * for creating a reasonable progress manager dialog.
 *
 * @author Richard Bair
 */
public class SwingTask {
    private SwingWorker worker;
    private SwingPropertyChangeSupport pcs;
    private String description;
    private Icon icon;
    private String message;
    private boolean indeterminate;
    private boolean modal;
    private boolean canCancel;
    
    public SwingTask(SwingWorker worker) {
	//TODO should support JB pattern
	this.worker = worker;
	worker.addPropertyChangeListener(new PropertyChangeListener() {
	    public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName().equals("progress")) {
		    setProgress((Integer)evt.getNewValue());
		}
	    }
	});
	//attach a listener to the progress
	pcs = new SwingPropertyChangeSupport(this, false); //TaskManager may be running on background thread
    }
    
    protected SwingWorker getSwingWorker() {
	return worker;
    }
    
    /**
     * @return a description of the overall task. This description will be read
     * once when the task has been added to the ProgressManager, and not again.
     */
    public String getDescription() {
	return description;
    }
    
    public void setDescription(String description) {
	if (this.description != description) {
	    Object oldValue = this.description;
	    this.description = description;
	    pcs.firePropertyChange("description", oldValue, description);
	}
    }
    
    /**
     * @return an icon associated with this Progressable
     */
    public Icon getIcon() {
	return icon;
    }
    
    public void setIcon(Icon icon) {
	if (this.icon != icon) {
	    Object oldValue = this.icon;
	    this.icon = icon;
	    pcs.firePropertyChange("icon", oldValue, icon);
	}
    }
    
    /**
     * @return a message describing the current action being performed by the
     * task. This may change at any time. When the ProgressDialog gui is
     * repainted, it will ask the ProgressManager for this message. The
     * ProgressManager will in turn ask this Progressable for the message
     * to display. Commonly, this would be something like &quot;Connecting
     * to server...&quot; or &quot;Loading Customer 1 of 20&quot;, etc.
     */
    public String getMessage() {
	return message;
    }
    
    public void setMessage(String message) {
	if (this.message != message) {
	    Object oldValue = this.message;
	    this.message = message;
	    pcs.firePropertyChange("message", oldValue, message);
	}
    }
    
    public int getMinimum() {
	return 0;
    }
    
    public int getMaximum() {
	return 100;
    }
    
    /**
     * @see javax.swing.JProgressBar#getValue()
     * @return The current progress. This is a value between 0 and
     * 100.
     */
    public int getProgress() {
	return worker.getProgress();
    }

    public void setProgress(int progress) {
	worker.setProgress(progress);
    }

    /** Calculates the progress as a percentage */
    public void setProgress(int progress, int min, int max) {
	assert progress >= min && progress <= max;
	progress -= min;
	int diff = max - min;
	worker.setProgress((progress * 100) / diff);
    }
    
    /**
     * @return true if the progress is indeterminate.
     */
    public boolean isIndeterminate() {
	return indeterminate;
    }
    
    public void setIndeterminate(boolean indeterminate) {
	if (this.indeterminate != indeterminate) {
	    Object oldValue = this.indeterminate;
	    this.indeterminate = indeterminate;
	    pcs.firePropertyChange("indeterminate", oldValue, indeterminate);
	}
    }
    
    /**
     * @return true if the Progressable task should cause the ProgressDialog
     * to be displayed in a modal manner. This would be used to cause the user
     * to have to wait for a task to complete before allowing the user to use
     * the GUI again. False if the task can occur in the background.
     */
    public boolean isModal() {
	return modal;
    }
    
    public void setModal(boolean modal) {
	if (this.modal != modal) {
	    Object oldValue = this.modal;
	    this.modal = modal;
	    pcs.firePropertyChange("modal", oldValue, modal);
	}
    }
    
    /**
     * Causes the task to be cancelled. If the task cannot be cancelled, then
     * this method returns false. If the task is cancelled successfully, then
     * this method returns true. If any exception occurs that would prevent
     * cancellation, and that the user should be notified of, then an exception
     * should be thrown.
     * @return
     * @throws Exception
     */
    public boolean cancel() throws Exception {
	worker.cancel(true);
	return true;
    }
    
    /**
     * @return true if the task can be cancelled, false otherwise
     */
    public boolean isCanCancel() {
	return canCancel;
    }

    public void setCanCancel(boolean canCancel) {
	if (this.canCancel != canCancel) {
	    Object oldValue = this.canCancel;
	    this.canCancel = canCancel;
	    pcs.firePropertyChange("canCancel", oldValue, canCancel);
	}
    }
    
}
