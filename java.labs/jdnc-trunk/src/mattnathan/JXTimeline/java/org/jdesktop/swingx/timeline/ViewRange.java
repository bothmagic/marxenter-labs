/*
 * $Id: ViewRange.java 2638 2008-08-06 09:32:23Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.timeline;

import org.jdesktop.swingx.JXTimeline;

/**
 * Defines a limited view of a timeline. This class can be applied to a JXTimeline to refine the visible range of dates
 * displayed in order to improve the visibility of areas of interest. This interface should be implemented so that:
 * <pre><code>
 *    timeline.minimum <= view.minimum <= timeline.value <= timeline.value + timeline.extent <= view.maximum <= timeline.maximum
 * </code></pre>.
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 */
public interface ViewRange {
    /**
     * Get the minimum timestamp to display for the given timeline and size in pixels.
     *
     * @param tl The timeline.
     * @param size The size of the timeline in pixels.
     * @return The minimum timestamp to display.
     */
    long getViewMinimum(JXTimeline tl, int size);





    /**
     * Get the maximum timestamp to display for the given timeline and size in pixels.
     *
     * @param tl The timeline.
     * @param size The size of the timeline in pixels.
     * @return The maximum timestamp to display.
     */
    long getViewMaximum(JXTimeline tl, int size);
}
