/*
 * $Id: ValueMarkerGroup.java 2628 2008-08-05 16:05:09Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.slider;

/**
 * A simple MarkerRange that represents a single value within the model.
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 */
public class ValueMarkerGroup extends AbstractMarkerGroup {

    /**
     * The instance of the MarkerRange that returns the value of this group.
     */
    private MarkerRange rangeInstance = new ValueMarkerRange();

    /**
     * The value for this marker.
     */
    private long value;

    /**
     * Create a new value marker with value 0.
     */
    public ValueMarkerGroup() {
        this(0);
    }





    /**
     * Create a new marker with the given value.
     *
     * @param value The value for this marker.
     */
    public ValueMarkerGroup(long value) {
        this.value = value;
    }





    /**
     * Set the value for this marker.
     * @param value The value for this marker.
     */
    public void setValue(long value) {
        long old = getValue();
        this.value = value;
        value = getValue();
        if (value != old) {
            fireMarkerChanged(old, value);
        }
    }





    /**
     * Gets the value set on this group.
     *
     * @return The value for this marker.
     */
    public long getValue() {
        return value;
    }





    /**
     * Get the range of markers specified by the given start and end model values.
     *
     * @param startRange The start value the returned range should view.
     * @param endRange The end range (inclusive) that the returned range should view.
     * @return A snapshot of this marker group between the given ranges.
     */
    public MarkerRange getMarkers(long startRange, long endRange) {
        MarkerRange result;
        long value = getValue();
        if (startRange <= value && endRange >= value) {
            result = rangeInstance;
        } else {
            result = EMPTY_RANGE;
        }
        return makeSafe(result);
    }





    /**
     * Simple range that return the value as set on the enclosing class.
     *
     * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
     */
    private class ValueMarkerRange implements MarkerRange {
        /**
         * @return 1
         */
        public int getSize() {
            return 1;
        }





        /**
         * Gets the value for the enclosing MarkerGroup.
         *
         * @param index the index, only 0 is supported.
         * @return The enclosing classes value property.
         */
        public long get(int index) {
            if (index != 0) {
                throw new IndexOutOfBoundsException(index + ":1");
            }
            return getValue();
        }

    }
}
