/*
 * $Id: BagMarkerMutator.java 2628 2008-08-05 16:05:09Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.slider;

/**
 * Defines a mutator for a BagMarkerGroup
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 * @version 1.0
 */
public class BagMarkerMutator extends AbstractMarkerMutator {
    public BagMarkerMutator() {
        super(BagMarkerGroup.class);
    }





    /**
     * Mutate the currentValue within the given MarkerGroup so that it becomes the new value.
     *
     * @param model The model containing both the MarkerGroup and value.
     * @param group The group to alter the value in.
     * @param currentValue The current value to change.
     * @param newValue The value to set in the given MarkerGroup.
     * @param valueAdjusting {@code true} if the process of changing the value is only partially complete.
     * @return The actual value set on the marker group.
     */
    public long mutate(SliderModel model, MarkerGroup group, long currentValue, long newValue, boolean valueAdjusting) {
        checkError(group);
        newValue = constrainValue(model, newValue);
        BagMarkerGroup g = (BagMarkerGroup) group;
        g.setValueAdjusting(valueAdjusting);
        return g.setValue(currentValue, newValue);
    }
}
