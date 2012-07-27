/*
 * $Id: MarkerRange.java 2628 2008-08-05 16:05:09Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.slider;

/**
 * Defines a range within a MarkerGroup. This provides a simple reduced view of the marker set applied to a JXSlider via
 * a SliderModel.
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 * @see MarkerGroup
 */
public interface MarkerRange {
    /**
     * Get the number of markers within this marker range. This value will always return >= 0.
     *
     * @return The number of values this range contains.
     */
    public int getSize();





    /**
     * Gets the value of the marker at the given index within this range.
     *
     * @param index The index of the marker within the range.
     * @return The position of the marker within the SliderModel
     * @throws IndexOutOfBoundsException if the given index lies outside the range {@code [0, getSize())}.
     */
    public long get(int index);
}
