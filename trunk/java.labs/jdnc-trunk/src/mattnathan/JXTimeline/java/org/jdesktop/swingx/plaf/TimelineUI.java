/*
 * $Id: TimelineUI.java 2638 2008-08-06 09:32:23Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.plaf;

import java.awt.Point;
import java.awt.Rectangle;

import org.jdesktop.swingx.JXTimeline;

/**
 * UI Delegate for the JXTimeline component.
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 */
public abstract class TimelineUI extends XComponentUI<JXTimeline> {
    protected TimelineUI() {
        super("Timeline");
    }





    /**
     * Gets the bounds of the given timestamp in the timeline. This will usually return a single pixel width/height
     * rectangle representing the coordinate on the timeline the timestamp maps to. This will return null if the given
     * timestamp is outside the range of the given timeline's model. The width and height of the returned rectangle will
     * never be equal to 0 but if the given timestamp maps to bounds outside of the ViewRange for the given timeline
     * then the width and height will be negative versions of those values.
     *
     * @param tl The source component.
     * @param timestamp The timestamp to convert.
     * @return The rectangle the given timestamp occupies.
     */
    public abstract Rectangle getTimestampBounds(JXTimeline tl, long timestamp);





    /**
     * Convert the given coordinate point into a timestamp within the given timeline. This method should attempt to
     * return the closest timestamp for the given location. This will return -1 if the timeline's model is empty.
     *
     * @param tl The source component.
     * @param point The point to convert.
     * @return The timestamp closest to the given location.
     */
    public abstract long locationToTimestamp(JXTimeline tl, Point point);
}
