/*
 * $Id: MarkerGroup.java 2628 2008-08-05 16:05:09Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.slider;

import org.jdesktop.swingx.event.DynamicObject;
import org.jdesktop.swingx.event.*;

/**
 * Defines a set of markers that apply to a single SliderModel. The markers can be accessed via the getMarkers method
 * which will return a MarkerRange giving access to a number of markers between certain model values.
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 * @see SliderModel
 * @see MarkerRange
 */
public interface MarkerGroup {
    /**
     * Get the range of markers specified by the given start and end model values. The returned range will not change
     * under any circumstance and can be treated as a static snapshot of this MarkerGroup over the given range. It is
     * possible that implementations of this interface will disallow access to the returned range throwing an exception
     * if this MarkerGroup changes and an already existing range is accessed.
     *
     * @param startRange The start value the returned range should view.
     * @param endRange The end range (inclusive) that the returned range should view.
     * @return A snapshot of this marker group between the given ranges.
     */
    public MarkerRange getMarkers(long startRange, long endRange);





    /**
     * Add a listener for notification of changes to the values of this marker group.
     *
     * @param l The listener to add.
     */
    public void addMarkerChangeListener(MarkerChangeListener l);





    /**
     * Remove a listener previously added by addMarkerChangeListener
     *
     * @param l The listener to remove.
     */
    public void removeMarkerChangeListener(MarkerChangeListener l);





    /**
     * Get all listeners added via the addMarkerChangeListener method.
     *
     * @return The list of listeners, never null.
     */
    public MarkerChangeListener[] getMarkerChangeListeners();
}
