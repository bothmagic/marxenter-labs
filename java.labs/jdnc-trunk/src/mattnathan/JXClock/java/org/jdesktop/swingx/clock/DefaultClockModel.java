/*
 * $Id: DefaultClockModel.java 2628 2008-08-05 16:05:09Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.clock;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;

import java.util.Calendar;

/**
 * Defines the default implementation of a ClockModel. This provides a mechanism to periodically update the models time
 * value to represent the current time. To start this model simply call the {@link #start} method, to stop call the
 * {@link #stop} method. The periodic change of time is provided by the ClockTimer interface which abstracts away the
 * actual implementation of the timer so it can be changed by sub-classes. The delay between the creation of this model
 * and the start of the timer has no effect on this model no mater how long this delay is. For example if the object
 * were created with an initial value of 100 then start was called 20 seconds later the model will show on its first
 * update a value 20 seconds past 100 then 21 seconds past 100, then 22 seconds past 100 etc, assuming no delay in the
 * observers on the change to the model. This feature of implementation means that no matter how long the processing of
 * a model value change the time will always be accurate.
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 * @see #start
 * @see #stop
 */
public class DefaultClockModel extends AbstractClockModel {

    private long timeOffset;
    private long currentTime;
    private ClockTimer clockTimer;

    /**
     * Create a new model using the current time as the initial value.
     */
    public DefaultClockModel() {
        this( -1);
    }





    /**
     * Create a new model with the given initial time. If a number < 0 is passed for currentTime then
     * {@link System#currentTimeMillis} will be used.
     *
     * @param currentTime The initial time for the model.
     */
    public DefaultClockModel(long currentTime) {
        if (currentTime < 0) {
            this.currentTime = System.currentTimeMillis();
            timeOffset = 0;
        } else {
            this.currentTime = currentTime;
            timeOffset = System.currentTimeMillis() - this.currentTime;
        }
        this.clockTimer = createClockTimer();
    }





    /**
     * Starts this model ticking.
     *
     * @see #stop
     * @see ClockTimer
     */
    public void start() {
        clockTimer.start();
    }





    /**
     * Stops this model ticking.
     *
     * @see #start
     * @see ClockTimer
     */
    public void stop() {
        clockTimer.stop();
    }





    /**
     * Create the timer to use to update this classes timer value. This method should not return null.
     *
     * @return The timer object.
     */
    protected ClockTimer createClockTimer() {
        return new SwingClockTimer();
    }





    /**
     * Get the current time for this ClockModel.
     *
     * @return The current time this model represents.
     */
    public Calendar getTime() {
        Calendar c = getCalendarInstance();
        c.setTimeInMillis(currentTime);
        return unmodifiableCalendar(c);
    }





    /**
     * Attempt to stop any threads.
     *
     * @throws Throwable
     */
    @Override
    protected void finalize() throws Throwable {
        stop();
        super.finalize();
    }





    /**
     * Defines a timer to periodically change the time in the containing model. Generally this will simply increment the
     * time by one second so as to update clocks using this model.
     *
     * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
     */
    protected static interface ClockTimer {
        /**
         * Start the clock.
         */
        public abstract void start();





        /**
         * Stop the clock.s
         */
        public abstract void stop();
    }







    /**
     * Clock timer that uses a javax.swing.Timer to perform the timing. This repeats every 1 second.
     *
     * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
     */
    protected class SwingClockTimer implements ClockTimer, ActionListener {
        private Timer timer;
        public void start() {
            if (timer == null) {
                timer = new Timer(1000, this);
                timer.setCoalesce(true);
                timer.setRepeats(true);
            }
            if (timer != null && !timer.isRunning()) {
                timer.setInitialDelay(1000 - ((int) System.currentTimeMillis() % 1000));
                timer.start();
            }
        }





        public void stop() {
            if (timer != null) {
                timer.stop();
            }
        }





        public void actionPerformed(ActionEvent e) {
            currentTime = System.currentTimeMillis() + timeOffset;
            fireStateChanged();
        }

    }
}
