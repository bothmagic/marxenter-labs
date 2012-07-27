/*
 * $Id: BagMarkerGroup.java 2628 2008-08-05 16:05:09Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.slider;

/**
 * Defines a collection of markers as a set. This group can be modified by adding or removing marker values. A maximum
 * of Integer.MAX_VALUE values can be added to this group. Duplicate entries will be ignored.
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 */
public class BagMarkerGroup extends AbstractMarkerGroup {

    private long[] values;
    private int length = 0;
    private boolean valueAdjusting = false;

    /**
     * Create a new marker group with initial capacity of 10.
     */
    public BagMarkerGroup() {
        this(10);
    }





    /**
     * Create a new BagMarkerGroup with the given initial capacity.
     *
     * @param initialCapacity The initial capacity.
     */
    public BagMarkerGroup(int initialCapacity) {
        if (initialCapacity < 0) {
            throw new IllegalArgumentException("Illegal Capacity: " + initialCapacity);
        }

        values = new long[initialCapacity];
    }





    /**
     * Get whether additions or removals are being performed in a batch.
     *
     * @return {@code true} if it is likely that more that one change will be made.
     */
    public boolean isValueAdjusting() {
        return valueAdjusting;
    }





    /**
     * Set a global vlaueAdjusting property. This will be used to create any events fired which adding or removing
     * values.
     *
     * @param valueAdjusting Set the property used to construct change events.
     */
    public void setValueAdjusting(boolean valueAdjusting) {
        this.valueAdjusting = valueAdjusting;
    }





    /**
     * Remove the given value from the marker bag.
     *
     * @param value The value to remove.
     */
    public void removeValue(long value) {
        int index = binarySearch(values, value, 0, length);
        if (index >= 0) {
            assert length > 0;
            System.arraycopy(values, index + 1, values, index, length - index - 1);
            length--;
            fireMarkerRemoved(value, isValueAdjusting());
        }
    }





    /**
     * changes the given oldValue into the newValue. Depending on the contents of this bag a markerChanged or
     * markerRemoved event may be fired. If the newValue already exists in this bag then the old value is remove and the
     * corresponding event fired.
     *
     * @param oldValue The old value.
     * @param newValue The new value.
     * @return The value set to, this will be the oldValue if nothing has changed; i.e. if the oldValue does not exist.
     */
    public long setValue(long oldValue, long newValue) {
        long result = oldValue;
        if (oldValue != newValue) {
            int index = binarySearch(values, oldValue, 0, length);
            if (index >= 0) {
                assert length > 0;
                int newIndex = binarySearch(values, newValue, 0, length);
                result = newValue;
                if (newIndex < 0) {
                    newIndex = -newIndex - 1;
                    if (newIndex > index + 1) {
                        System.arraycopy(values, index + 1, values, index, newIndex - index + 1);
                        values[newIndex - 1] = newValue;
                    } else if (newIndex < index) {
                        System.arraycopy(values, newIndex, values, newIndex + 1, index - newIndex);
                        values[newIndex] = newValue;
                    } else {
                        values[index] = newValue;
                    }
                    fireMarkerChanged(oldValue, newValue);
                } else {
                    System.arraycopy(values, index + 1, values, index, length - index - 1);
                    length--;
                    fireMarkerRemoved(oldValue);
                }
            }
        }
        return result;
    }





    /**
     * Add the given value to the marker bag.
     *
     * @param value The value to add.
     */
    public void addValue(long value) {
        int index = -1;
        if (length > 0) {
            assert values != null;
            index = binarySearch(values, value, 0, length);
        }

        if (index < 0) { // don't allow duplicate entries

            if (length == Integer.MAX_VALUE) {
                throw new IllegalArgumentException("Cannot add more than Integer.MAX_VALUE (" +
                      Integer.MAX_VALUE + ") unique values");
            }

            index = -index - 1;
            if (values.length == length) {
                // copied from ArrayList.ensureCapacity
                long[] tmp = values;
                int minCapacity = length + 1;
                int newCapacity = (length * 3) / 2 + 1;
                if (newCapacity < minCapacity) {
                    newCapacity = minCapacity;
                }
                values = new long[newCapacity];
                if (length > 0) {
                    System.arraycopy(tmp, 0, values, 0, length);
                }
            }

            System.arraycopy(values, index, values, index + 1, length - index);
            values[index] = value;
            length++;

            fireMarkerAdded(value, isValueAdjusting());
        }
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
        if (length == 0) {
            result = EMPTY_RANGE;
        } else {
            int index0;
            int index1;

            index0 = binarySearch(values, startRange, 0, length);
            if (index0 < 0) {
                index0 = -index0 - 1;
            }
            index1 = binarySearch(values, endRange, 0, length);
            if (index1 < 0) {
                index1 = -index1 - 1;
            }
            if (index0 == index1) {
                result = EMPTY_RANGE;
            } else {
                result = new BagMarkerRange(index0, index1 - index0);
            }
        }

        return makeSafe(result);
    }





    /**
     * Copied from Arrays.binarySearch to include bounds on the array.
     *
     * @param a the array to be searched.
     * @param key the value to be searched for.
     * @param offset The offset to start looking
     * @param length The length of the area to look in.
     * @return index of the search key, if it is contained in the list; otherwise, <tt>(-(<i>insertion point</i>) -
     *   1)</tt>. The <i>insertion point</i> is defined as the point at which the key would be inserted into the list:
     *   the index of the first element greater than the key, or <tt>list.size()</tt>, if all elements in the list are
     *   less than the specified key. Note that this guarantees that the return value will be &gt;= 0 if and only if
     *   the key is found.
     */
    private static int binarySearch(long[] a, long key, int offset, int length) {
        int low = offset;
        int high = length - 1 + offset;

        while (low <= high) {
            int mid = (low + high) >> 1;
            long midVal = a[mid];

            if (midVal < key) {
                low = mid + 1;
            } else if (midVal > key) {
                high = mid - 1;
            } else {
                return mid; // key found
            }
        }
        return -(low + 1); // key not found.
    }





    /**
     * Uses the enclosing classes data as a reference.
     *
     * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
     */
    private class BagMarkerRange implements MarkerRange {

        private final int offset;
        private final int length;

        public BagMarkerRange(int offset, int length) {
            this.offset = offset;
            this.length = length;
        }





        public int getSize() {
            return length;
        }





        public long get(int index) {
            return values[index + offset];
        }
    }
}
