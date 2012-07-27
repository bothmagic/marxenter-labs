/*
 * $Id: ClockModel.java 2628 2008-08-05 16:05:09Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.clock;

import java.util.Calendar;

import org.jdesktop.swingx.event.DynamicObject;

/**
 * Defines the model for a clock. This contains a single property; time, this is represented as a Calendar object. The
 * returned Calendar should be treated as immutable and should not alter the internal state of this model instance.
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 * @see org.jdesktop.swingx.JXClock
 */
public interface ClockModel extends DynamicObject {
    /**
     * Get the current time for this ClockModel. The returned Calendar should be either immutable or a copy of the
     * internal representation for this model, i.e. changed to the returned calendar should not affect future calls to
     * getTime().
     *
     * @return The current time this model represents.
     */
    public Calendar getTime();
}
