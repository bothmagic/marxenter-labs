/*
 * $Id: Progressable.java 148 2004-10-29 20:43:46Z rbair $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.jdnc.incubator.rbair.swing;

import javax.swing.Icon;

/**
 * Represents an item that progresses, and can be managed by the
 * ProgressManager. Such an item has a percent completed, a description,
 * a status message, and is cancellable.
 * @author Richard Bair
 */
public interface Progressable {
    /**
     * @return a description of the overall task. This description will be read
     * once when the task has been added to the ProgressManager, and not again.
     */
    public String getDescription();
    /**
     * @return an icon associated with this Progressable
     */
    public Icon getIcon();
    /**
     * @return a message describing the current action being performed by the
     * task. This may change at any time. When the ProgressDialog gui is
     * repainted, it will ask the ProgressManager for this message. The
     * ProgressManager will in turn ask this Progressable for the message
     * to display. Commonly, this would be something like &quot;Connecting
     * to server...&quot; or &quot;Loading Customer 1 of 20&quot;, etc.
     */
    public String getMessage();
    /**
     * @see javax.swing.JProgressBar#getMinimum()
     * @return The minimum value in the progress bar.
     */
    public int getMinimum();
    /**
     * @see javax.swing.JProgressBar#getMaximum()
     * @return The maximum value in the progress bar
     */
    public int getMaximum();
    /**
     * @see javax.swing.JProgressBar#getValue()
     * @return The current progress. This is a value between minimum and
     * the maximum values. If the progress is >= the maximum, the task is 
     * considered closed.
     */
    public int getProgress();
    /**
     * @return true if the progress is indeterminate.
     */
    public boolean isIndeterminate();
    /**
     * @return true if the Progressable task should cause the ProgressDialog
     * to be displayed in a modal manner. This would be used to cause the user
     * to have to wait for a task to complete before allowing the user to use
     * the GUI again. False if the task can occur in the background.
     */
    public boolean isModal();
    /**
     * Causes the task to be cancelled. If the task cannot be cancelled, then
     * this method returns false. If the task is cancelled successfully, then
     * this method returns true. If any exception occurs that would prevent
     * cancellation, and that the user should be notified of, then an exception
     * should be thrown.
     * @return
     * @throws Exception
     */
    public boolean cancel() throws Exception;
    /**
     * @return true if the task can be cancelled, false otherwise
     */
    public boolean canCancel();
}
