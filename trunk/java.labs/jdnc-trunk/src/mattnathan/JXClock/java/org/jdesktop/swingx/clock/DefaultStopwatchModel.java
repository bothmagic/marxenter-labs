/*
 * $Id: DefaultStopwatchModel.java 2628 2008-08-05 16:05:09Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.clock;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.jdesktop.swingx.clock.AbstractStopwatchModel.TimeKeeper;

/**
 * The default implementation of the StopwatchModel. This class uses a SwingTimer to provide the updating of the model
 * and stores marks as a List of longs.
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 */
public class DefaultStopwatchModel extends AbstractStopwatchModel {

    /**
     * lazily created list of markers.
     */
    private List<Long> markers;

    /**
     * Used to accommodate the starting time and paused periods. If -1 then we should start from 0.
     */
    private long offset = -1;
    /**
     * The current timestamp of the model. This is used instead of getting the time directly from
     * System.currentTimeInMillis() so that multiple calls can be made with predictable results.
     */
    private long timestamp = 0;

    /**
     * Create a new model.
     */
    public DefaultStopwatchModel() {
        super();
    }





    /**
     * Returns the SwingTimeKeeper instance.
     *
     * @return The time keeper for this model.
     */
    @Override
    protected TimeKeeper createTimeKeeper() {
        return new SwingTimeKeeper();
    }





    /**
     * {@inheritDoc}
     */
    @Override
    protected void addMarker(Calendar time) {
        if (markers == null) {
            markers = new ArrayList<Long>();
        }
        markers.add(time.getTimeInMillis());
    }





    /**
     * {@inheritDoc}
     */
    public void reset() {
        if (markers != null) {
            markers.clear();
        }
        if (isRunning()) {
            offset = System.currentTimeMillis();
        } else {
            offset = -1;
        }
        updateTime();
    }





    /**
     * {@inheritDoc}
     */
    public int getMarkerCount() {
        return markers == null ? 0 : markers.size();
    }





    /**
     * {@inheritDoc}
     */
    public Calendar getMarker(int index) {
        if (index < 0 || index >= getMarkerCount()) {
            throw new IndexOutOfBoundsException(index + ":" + getMarkerCount());
        }

        Calendar c = getCalendarInstance();
        c.setTimeInMillis(markers.get(index));
        return unmodifiableCalendar(c);
    }





    /**
     * {@inheritDoc}
     */
    public Calendar getTime() {
        Calendar result = ZERO_CALENDAR;
        if (offset >= 0) {
            result = getCalendarInstance();
            result.setTimeInMillis(timestamp - offset);
            result = unmodifiableCalendar(result);
        }
        return result;
    }





    /**
     * {@inheritDoc}
     */
    @Override
    protected void updateTime() {
        timestamp = System.currentTimeMillis();
        fireStateChanged();
    }





    /**
     * Uses a swing timer to update the model for timing events.
     *
     * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
     */
    protected class SwingTimeKeeper extends AbstractTimeKeeper implements ActionListener {
        private Timer timer;
        public void start() {
            if (timer == null) {
                timer = new Timer(60, this);
                timer.setRepeats(true);
                timer.setCoalesce(true);
            }
            // this code is used to keep the time correct when the model is stopped then started again.
            if (offset < 0) {
                offset = System.currentTimeMillis();
            } else {
                offset += System.currentTimeMillis() - timestamp;
            }

            timer.start();

        }





        public void stop() {
            if (timer != null) {
                timer.stop();
            }
        }





        /**
         * Updated the time in the model.
         *
         * @param e Event, ignored.
         */
        public void actionPerformed(ActionEvent e) {
            updateTime();
        }

    }
}
