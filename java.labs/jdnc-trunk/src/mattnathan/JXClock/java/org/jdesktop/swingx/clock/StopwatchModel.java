/*
 * $Id: StopwatchModel.java 2628 2008-08-05 16:05:09Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.clock;

import java.util.Calendar;

/**
 * Defines a Stopwatch model. This extends the standard ClockModel to add more control over the processing of time to
 * allow for the creation of stopwatch style data models. This model provides a mechanism for storing a number of
 * markers as the time progresses (see {@link #mark}, {@link #getMarker}). To start the model timing call
 * {@link #start}.
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 * @see org.jdesktop.swingx.JXStopwatch
 * @see StopwatchModel#start
 */
public interface StopwatchModel extends ClockModel {
    /**
     * Start this model. This will start the model timing.
     */
    public void start();





    /**
     * Returns whether this model is currently started. This will be true if {@link #start} has been called and mark has
     * not been called with a value of {@code true}.
     *
     * @return Whether this model is running.
     * @see #start
     * @see #mark
     */
    public boolean isRunning();





    /**
     * Create a mark at the current time in this model. If {@code true} is passed the timer will be stopped and the
     * current time will be the same as the marker at index {@code getMarkerCount() - 1}.
     *
     * @param stopTimer {@code true} to stop the time as well as creating a mark.
     */
    public void mark(boolean stopTimer);





    /**
     * Reset this model. This will set the current time to 0 and remove any markers collected during a run. Calling this
     * method on a model that is not running and has no markers will have no effect. Note that resetting a running
     * model will not stop the model but simply reset the time and markers.
     */
    public void reset();





    /**
     * Get the number of markers set on this model.
     *
     * @return The number of markers.
     * @see #mark
     */
    public int getMarkerCount();





    /**
     * Get the time of the marker at the given index. Marks will always be in chronological order, that is to say that
     * {@code for all i > n => getMarker(i) >= getMarker(n)}.
     *
     * @param index The index of the mark to get
     * @return The mark on the model. This will never be null.
     * @throws IndexOutOfBoundsException if the given index is < 0 or >= getMarkerCount().
     */
    public Calendar getMarker(int index);
}
