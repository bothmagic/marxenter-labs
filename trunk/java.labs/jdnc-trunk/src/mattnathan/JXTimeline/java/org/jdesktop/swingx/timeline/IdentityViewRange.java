/*
 * $Id: IdentityViewRange.java 2638 2008-08-06 09:32:23Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.timeline;

import org.jdesktop.swingx.JXTimeline;

/**
 * Defines a simple ViewRange that encompasses the whole model.
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 */
public class IdentityViewRange implements ViewRange {

    /**
     * The singleton instance.
     */
    private static final ViewRange SINGLETON = new IdentityViewRange();
    /**
     * Returns a shared instance of this class. Use this method in favour of creating new instances.
     *
     * @return The shared instance of this class.
     */
    public static ViewRange getInstance() {
        return SINGLETON;
    }





    /**
     * Create a new instance of this class. You should use the static getInstance method instead as this class has no
     * mutable or configurable properties.
     *
     * @see #getInstance
     */
    public IdentityViewRange() {
    }





    /**
     * Returns the maximum value from the timeline.
     *
     * @param tl The timeline.
     * @param size The size of the timeline in pixels.
     * @return The timeline's maximum.
     */
    public long getViewMaximum(JXTimeline tl, int size) {
        return tl.getMaximum();
    }





    /**
     * Returns the timeline's minimum value.
     *
     * @param tl The timeline.
     * @param size The size of the timeline in pixels.
     * @return The timeline's minimum.
     */
    public long getViewMinimum(JXTimeline tl, int size) {
        return tl.getMinimum();
    }
}
