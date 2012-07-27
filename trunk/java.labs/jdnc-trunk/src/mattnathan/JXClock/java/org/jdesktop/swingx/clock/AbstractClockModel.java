/*
 * $Id: AbstractClockModel.java 2628 2008-08-05 16:05:09Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.clock;

import java.util.Calendar;

import org.jdesktop.swingx.event.AbstractDynamicObject;

/**
 * Defines an abstract ClockModel. This implements the observer pattern code.
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 */
public abstract class AbstractClockModel extends AbstractDynamicObject implements ClockModel {

    /**
     * Get an instance of a Calendar for use when returning from the getTime method.
     *
     * @return The calendar instance.
     */
    protected Calendar getCalendarInstance() {
        Calendar c = Calendar.getInstance();
        return c;
    }





    /**
     * Create an unmodifiable calendar instance.
     *
     * @param c The calendar to wrap.
     * @return The unmodifiable calendar instance.
     */
    protected Calendar unmodifiableCalendar(Calendar c) {
        return new UnmodifiableCalendar(c);
    }
}
