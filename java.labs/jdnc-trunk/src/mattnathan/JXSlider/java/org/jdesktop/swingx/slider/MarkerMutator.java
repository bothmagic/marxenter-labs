/*
 * $Id: MarkerMutator.java 2628 2008-08-05 16:05:09Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.slider;

/**
 * Defines an interface for mutating MarkerGroups. The core method of this interface is the mutate method which allows
 * you to modify a MarkerGroups value, changing it from one value to another. This interface additionally provides a
 * mechanism for specifying, down to a specific value within a MarkerGroup, the mutable state.
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 */
public interface MarkerMutator {
    /**
     * Mutate the currentValue within the given MarkerGroup so that it becomes the new value. If valueAdjusting is true,
     * this change can be classed as a temporary, or partial change, usually in the case where the value is changing
     * during the process of being set (i.e. while a slider thumb is being dragged).
     *
     * @param model The model containing both the MarkerGroup and value.
     * @param group The group to alter the value in.
     * @param currentValue The current value to change.
     * @param newValue The value to set in the given MarkerGroup.
     * @param valueAdjusting {@code true} if the process of changing the value is only partially complete.
     * @return The actual value set on the marker group.
     */
    long mutate(SliderModel model, MarkerGroup group, long currentValue, long newValue, boolean valueAdjusting);





    /**
     * Returns true if the given value can be mutated within the given MarkerGroup.
     *
     * @param model The model containing both the MarkerGroup and value.
     * @param group The group of values containing the given value.
     * @param value The value to check for mutability.
     * @return {@code true} if the given value can be mutated.
     */
    boolean isMutable(SliderModel model, MarkerGroup group, long value);
}
