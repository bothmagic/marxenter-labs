package org.jdesktop.incubator.util;

import java.util.Collection;

/*
 * Not thread safe! well it is something of an evil early optimisation
 *
 * Created by IntelliJ IDEA.
 * User: Richard Osbaldeston (osbald.co.uk)
 * Date: 12-Mar-02
 * Time: 14:12:03
 */

public class DynamicIntArray {
    private int[] elements = EMPTY;
    private int length = 0;
    private int maximumLength;
    private float scalingFactor = 1.5f;
    private int initialCapacity = 10;

    private static final int[] EMPTY = new int[0];

    public DynamicIntArray() {
    }

    public DynamicIntArray(int initialCapacity) {
        this(initialCapacity, 0);
    }

    public DynamicIntArray(int initialCapacity, int maximumLength) {
        this.initialCapacity = initialCapacity;
        this.maximumLength = maximumLength;
        this.elements = new int[initialCapacity];
    }

    public float getScalingFactor() {
        return scalingFactor;
    }

    public void setScalingFactor(float scalingFactor) {
        if (scalingFactor >= 1) {
            this.scalingFactor = scalingFactor;
        } else {
            throw new IllegalArgumentException("ScalingFactor must be greater than one.");
        }
    }

    public int getMaximumLength() {
        return maximumLength;
    }

    public void setMaximumLength(int maximumLength) {
        this.maximumLength = maximumLength;
    }

    protected void ensureCapacity(int minCapacity) {
        if (minCapacity > elements.length) {
            if (elements.length == 0) {
                elements = new int[Math.max(minCapacity, initialCapacity)];
            } else {
                int newSize = minCapacity;
                if (getMaximumLength() == 0 || minCapacity < getMaximumLength()) {
                    newSize = (int) (minCapacity * getScalingFactor()) + 1;
                    if (getMaximumLength() > 0 && newSize > getMaximumLength()) {
                        newSize = Math.max(minCapacity, getMaximumLength());
                    }
                }
                int newArray[] = new int[newSize];
                System.arraycopy(elements, 0, newArray, 0, length);
                this.elements = newArray;
            }
        }
    }

    public DynamicIntArray add(int value) {
        ensureCapacity(length + 1);
        elements[length++] = value;
        return this;
    }

    //PENDING new addition - not fully tested!
    public DynamicIntArray add(int... values) {
        if (values != null && values.length > 0) {
            ensureCapacity(length + values.length);
            for (int value : values) {
                elements[length++] = value;
            }
        }
        return this;
    }

    //PENDING new addition - not fully tested!
    public DynamicIntArray add(Collection<? extends Number> values) {
        if (values != null && values.size() > 0) {
            ensureCapacity(length + values.size());
            for (Number value : values) {
                elements[length++] = value.intValue();
            }
        }
        return this;
    }

    public int[] toArray() {
        if (length == elements.length) {
            return this.elements;
        } else {
            int[] newArray = new int[length];
            System.arraycopy(elements, 0, newArray, 0, length);
            return newArray;
        }
    }

    public int[] slice(int start, int end) {
        if (end < start) {
            throw new IndexOutOfBoundsException("End index is before the start.");
        }
        if (start == 0 && length == elements.length && end == length - 1) {
            return elements;
        }
        int size = end - start + 1;
        int[] newArray = new int[size];
        System.arraycopy(elements, start, newArray, 0, size);
        return newArray;
    }

    public int size() {
        return this.length;
    }
}
