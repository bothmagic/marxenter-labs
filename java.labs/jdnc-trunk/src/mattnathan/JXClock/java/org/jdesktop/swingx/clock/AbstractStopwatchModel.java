/*
 * $Id: AbstractStopwatchModel.java 2628 2008-08-05 16:05:09Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.clock;

import java.util.Calendar;

/**
 * Abstract implementation of a StopwatchModel. This provides the framework for periodically updating the time of the
 * model to suit the stopwatch needs.
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 */
public abstract class AbstractStopwatchModel extends AbstractClockModel implements StopwatchModel {
    /**
     * Singleton for use when returning the start of the model.
     */
    protected static final Calendar ZERO_CALENDAR;
    static {
        // use GMT to avoid time offsets in clock.
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(0);
        ZERO_CALENDAR = new UnmodifiableCalendar(c);
    }





    /**
     * Used to track the time period of updates.
     */
    private TimeKeeper timeKeeper;
    /**
     * will be true if the timer is running.
     */
    private boolean running = false;

    /**
     * Create a new abstract model.
     */
    public AbstractStopwatchModel() {
        super();
    }





    /**
     * Create the time keeping instance to use for updating this model.
     *
     * @return The time keeper instance.
     */
    protected abstract TimeKeeper createTimeKeeper();





    /**
     * Gets this models TimeKeeper. This will lazily create the time keeper via the {@link #createTimeKeeper} method.
     *
     * @return The time keeper for this model.
     * @see #createTimeKeeper()
     */
    protected TimeKeeper getTimerKeeper() {
        if (timeKeeper == null) {
            timeKeeper = createTimeKeeper();
        }
        return timeKeeper;
    }





    /**
     * {@inheritDoc}
     */
    public void start() {
        if (!isRunning()) {
            getTimerKeeper().start();
            running = true;
        }
    }





    /**
     * {@inheritDoc}
     */
    public boolean isRunning() {
        return running;
    }





    /**
     * {@inheritDoc}
     */
    public void mark(boolean stopTimer) {
        if (isRunning()) {
            Calendar time = getTime();
            addMarker(time);
            if (stopTimer) {
                getTimerKeeper().stop();
                running = false;
            }
            fireStateChanged();
        }
    }





    /**
     * Add the given calendar as a marker for this model.
     *
     * @param time The calendar to mark on this model.
     */
    protected abstract void addMarker(Calendar time);





    /**
     * Update the time this model returns from {@link #getTime} with the current time.
     */
    protected abstract void updateTime();





    /**
     * Defines an abstract super type for periodically updating this models time.
     *
     * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
     */
    protected static interface TimeKeeper {
        /**
         * Start the timer.
         */
        public abstract void start();





        /**
         * Stop the timer.
         */
        public abstract void stop();
    }







    /**
     * An abstract base class which provides the method for updating the models time.
     *
     * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
     */
    protected abstract class AbstractTimeKeeper implements TimeKeeper {
        /**
         * Call this method when the models time should be updated.
         */
        protected void updateTime() {
            AbstractStopwatchModel.this.updateTime();
        }
    }
}
