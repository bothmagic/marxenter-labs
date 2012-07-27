/*
 * $Id: MarkerChangeEvent.java 2629 2008-08-06 08:27:49Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.event;

import java.util.EventObject;

/**
 * Event object containing information about the changed which caused a MarkerChangeListener to be notified.
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 */
public class MarkerChangeEvent extends EventObject {
    /**
     * Defines the different types of event.
     *
     * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
     */
    public static enum Type {
        /**
         * Signifies that markers have changed values.
         */
        MARKER_CHANGED,
        /**
         * Markers have been added.
         */
        MARKER_ADDED,
        /**
         * Markers have been removed.
         */
        MARKER_REMOVED
    }







    private final Type type;
    private final long oldValue;
    private final long newValue;
    private final boolean valueAdjusting;

    /**
     * Create a new instance of this class.
     *
     * @param source The source of the event.
     * @param oldValue The old value which changed.
     * @param newValue The new value of the change.
     * @param valueAdjusting {@code true} if this is not the end of changes to this value.
     */
    public MarkerChangeEvent(Object source, long oldValue, long newValue, boolean valueAdjusting) {
        this(source, oldValue, newValue, valueAdjusting, Type.MARKER_CHANGED);
    }





    public MarkerChangeEvent(Object source, long value, boolean valueAdjusting, Type type) {
        this(source, value - 1, value, valueAdjusting, type);
    }





    public MarkerChangeEvent(Object source, long val1, long val2, boolean valueAdjusting, Type type) {
        super(source);
        if (type == null) {
            throw new NullPointerException("type cannot be null");
        }
        this.oldValue = val1;
        this.newValue = val2;
        this.valueAdjusting = valueAdjusting;
        this.type = type;
    }





    /**
     * Get the new value set which caused this event.
     *
     * @return The new value.
     */
    public long getNewValue() {
        return newValue;
    }





    /**
     * Get the old value
     *
     * @return The old value.
     */
    public long getOldValue() {
        return oldValue;
    }





    /**
     * Gets the value added or removed under MARKER_ADDED and MARKER_REMOVED types.
     *
     * @return The marker added or removed.
     */
    public long getValue() {
        return newValue;
    }





    /**
     * Get whether the change is still ongoing.
     *
     * @return {@code true} if this value will change more.
     */
    public boolean isValueAdjusting() {
        return valueAdjusting;
    }





    /**
     * Get the type of this event.
     *
     * @return Type
     */
    public Type getType() {
        return type;
    }





    /**
     * Returns true if this event represents a change to all values. For example if of type MARKER_ADDED and true it
     * signifies that all markers are new markers, if MARKER_CHANED it tells that all markers have changed.
     *
     * @return boolean
     */
    public boolean isUniversalEvent() {
        return oldValue == newValue;
    }
}
