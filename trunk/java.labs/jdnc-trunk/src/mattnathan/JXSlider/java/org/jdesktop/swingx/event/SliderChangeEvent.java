/*
 * $Id: SliderChangeEvent.java 2629 2008-08-06 08:27:49Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.event;

import java.util.EventObject;

/**
 * Defines an event that describes the changes of a SliderModel.
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 * @see org.jdesktop.swingx.slider.SliderModel
 */
public class SliderChangeEvent extends EventObject {
    /**
     * Defines the types of event this class can represent.
     */
    public static enum Type {
        /**
         * The range of the model has changed.
         */
        RANGE_CHANGED,
        /**
         * A number of marker groups have been added to the model.
         */
        MARKER_GROUP_ADDED,
        /**
         * A number of marker groups have been removed from the model.
         */
        MARKER_GROUP_REMOVED,
        /**
         * A number of marker groups have changed in value. This does <em>not</em> mean that they have been replaced but
         * that they have changed their internal values.
         */
        MARKER_GROUP_CHANGED
    }







    private final Type type;
    private final int index0;
    private final int index1;
    private final MarkerChangeEvent cause;

    /**
     * Create a new event that represents a RANGE_CHANGED type event.
     *
     * @param source The source of the event.
     */
    public SliderChangeEvent(Object source) {
        this(source, Type.RANGE_CHANGED, -1, -1);
    }





    /**
     * Create a new event representing the given type. If the given type is RANGE_CHANGED the the index0 and index1
     * values will be ignored and set to -1. If not of type RANGE_CHANGED and either index0 or index1 are -1 then an
     * IllegalArgumentException will be thrown.
     *
     * @param source The source of the event.
     * @param type The type of event.
     * @param index0 The first index of the range or -1.
     * @param index1 The last index of the range or -1.
     * @throws IllegalArgumentException if -1 is passed for either index0 or index1 and type == RANGE_CHANGED.
     */
    public SliderChangeEvent(Object source, Type type, int index0, int index1) {
        this(source, type, index0, index1, null);
    }





    public SliderChangeEvent(Object source, Type type, int index0, int index1, MarkerChangeEvent cause) {
        super(source);

        if (type == Type.RANGE_CHANGED) {
            index0 = index1 = -1;
        } else if (index0 == -1 || index1 == -1) {
            throw new IllegalArgumentException("index0 and index1 cannot be -1 if type is now RANGE_CHANGED: " +
                                               "type=" + type +
                                               ", index0=" + index0 +
                                               ", index1=" + index1);
        }

        this.type = type;
        if (index0 > index1) {
            int tmp = index0;
            index0 = index1;
            index1 = tmp;
        }
        this.index0 = index0;
        this.index1 = index1;
        this.cause = cause;
    }





    /**
     * Return the first index of the marker groups that this event describes. This can return -1 if this type is
     * RANGE_CHANGED.
     *
     * @return The first marker group index.
     */
    public int getIndex0() {
        return index0;
    }





    /**
     * Return the last index of the marker groups that this event describes. This can return -1 if this type is
     * RANGE_CHANGED.
     *
     * @return The last marker group index.
     */
    public int getIndex1() {
        return index1;
    }





    /**
     * Get the event type for this event.
     *
     * @return The events type.
     */
    public Type getType() {
        return type;
    }





    /**
     * Get the cause of this event. This will be null unless of type MARKER_GROUP_CHANGED.
     *
     * @return The event causing this event to be created.
     */
    public MarkerChangeEvent getCause() {
        return cause;
    }
}
