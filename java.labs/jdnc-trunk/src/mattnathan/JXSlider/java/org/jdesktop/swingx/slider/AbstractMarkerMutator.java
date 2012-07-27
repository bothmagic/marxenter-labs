/*
 * $Id: AbstractMarkerMutator.java 2628 2008-08-05 16:05:09Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.slider;

/**
 * Defines a mutator type that acts on a single marker group sub type.
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 */
public abstract class AbstractMarkerMutator implements MarkerMutator {

    private final Class<? extends MarkerGroup> type;

    /**
     * Create a new mutator which mutates the given type only.
     *
     * @param type Class
     */
    public AbstractMarkerMutator(Class<? extends MarkerGroup> type) {
        this.type = type;
    }





    /**
     * Returns true if the given value can be mutated within the given MarkerGroup.
     *
     * @param model The model containing both the MarkerGroup and value.
     * @param group The group of values containing the given value.
     * @param value The value to check for mutability.
     * @return {@code true} if the given value can be mutated.
     */
    public boolean isMutable(SliderModel model, MarkerGroup group, long value) {
        return validMarkerGroup(group);
    }





    /**
     * Returns whether the given MarkerGroup is a valid type supported by this mutator.
     *
     * @param group The marker group to check.
     * @return {@code true} if the group is valid.
     */
    protected boolean validMarkerGroup(MarkerGroup group) {
        return type.isInstance(group);
    }





    /**
     * Constrains the given value so that it fits within the sliders minimum and maximum values.
     *
     * @param model The slider model
     * @param value The value to constrain.
     * @return The constrained value.
     */
    protected long constrainValue(SliderModel model, long value) {
        if (value < model.getMinimum()) {
            value = model.getMinimum();
        } else if (value > model.getMaximum()) {
            value = model.getMaximum();
        }
        return value;
    }





    /**
     * Throws an IllegalArgumentException if the given MarkerGroup does not match the type for this mutator.
     *
     * @param group The marker group to check.
     */
    protected void checkError(MarkerGroup group) {
        if (!validMarkerGroup(group)) {
            throw new IllegalArgumentException("Mutator for " + type + " cannot operate on " + group.getClass());
        }
    }
}
