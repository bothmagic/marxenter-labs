/*
 * $Id: AbstractClockPainter.java 2628 2008-08-05 16:05:09Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.clock;

import java.awt.geom.Point2D;

import java.util.Calendar;

import org.jdesktop.swingx.JXClock;
import org.jdesktop.swingx.painter.AbstractPainter;

/**
 * Provides common functionality between clock painter implementations.
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 */
public abstract class AbstractClockPainter extends AbstractPainter<JXClock> {
    public AbstractClockPainter() {
        super();
    }





    /**
     * Get the clocks current second.
     *
     * @param clock The clock.
     * @return The clocks current second.
     */
    protected int getSecond(JXClock clock) {
        return getTargetTime(clock).get(Calendar.SECOND);
    }





    /**
     * Get the clocks current minute.
     *
     * @param clock The clock.
     * @return The clocks current minute.
     */
    protected int getMinute(JXClock clock) {
        return getTargetTime(clock).get(Calendar.MINUTE);
    }





    /**
     * Get the clock current hour.
     *
     * @param clock The clock.
     * @return The clocks current hour.
     */
    protected int getHour(JXClock clock) {
        return getTargetTime(clock).get(Calendar.HOUR);
    }





    /**
     * Get the clocks current hour of the day.
     *
     * @param clock The clock.
     * @return The clocks current hour of the day.
     */
    protected int getHourOfDay(JXClock clock) {
        return getTargetTime(clock).get(Calendar.HOUR_OF_DAY);
    }





    /**
     * Get the time in the clock in the clocks locale and timezone.
     *
     * @param clock The clock.
     * @return The clocks current time.
     */
    protected Calendar getTargetTime(JXClock clock) {
        return clock.getTime();
    }





    /**
     * Get a point on the clock at the specified angle and distance from cx, cy.
     *
     * @param angle float
     * @param cx float
     * @param cy float
     * @param length float
     * @param point Point2D
     */
    protected void getPoint(float angle, float cx, float cy, float length, Point2D point) {
        double endx;
        double endy;

        if (angle >= 0 && angle < Math.PI / 2f) {
            endx = cx + (length * Math.sin(angle));
            endy = cy - (length * Math.cos(angle));

        } else if (angle >= Math.PI / 2f && angle < Math.PI) {
            angle -= Math.PI / 2f;
            endx = cx + (length * Math.cos(angle));
            endy = cy + (length * Math.sin(angle));

        } else if (angle >= Math.PI && angle < Math.PI * 1.5f) {
            angle -= Math.PI;
            endx = cx - (length * Math.sin(angle));
            endy = cy + (length * Math.cos(angle));
        } else {
            angle -= Math.PI * 1.5f;
            endx = cx - (length * Math.cos(angle));
            endy = cy - (length * Math.sin(angle));
        }

        point.setLocation(endx, endy);
    }

}
