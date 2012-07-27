/*
 * $Id: MarkerSelectionModel.java 2628 2008-08-05 16:05:09Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.slider;

import org.jdesktop.swingx.event.DynamicObject;

/**
 * Defines the interface for describing the active or selected value in a SliderModel. The active value consists of a
 * tuple of the value and the MArkerGroup the value originated from.
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 */
public interface MarkerSelectionModel extends DynamicObject {
    /**
     * Get the value selected within the MarkerGroup. This will throw an IllegalStateException if no value is selected.
     *
     * @return The value selected.
     */
    public long getSelectedValue();





    /**
     * Set the selected value. This will throw an IllegalArgumentException if the selected MarkerGroup is null.
     *
     * @param selectedValue the value selected.
     */
    public void setSelectedValue(long selectedValue);





    /**
     * Returns true if a value is selected, false otherwise.
     *
     * @return Whether a value is selected or not.
     */
    public boolean isValueSelected();





    /**
     * Clears the selected value from this model.
     */
    public void clearValueSelection();





    /**
     * Return the selected group of markers. This may return null if no marker group is selected, in this case
     * isValueSelected() will return false and getSelectedValue() will throw an exception.
     *
     * @return The marker group.
     */
    public MarkerGroup getSelectedGroup();





    /**
     * Set the selected marker group. This will clear the current selected value.
     *
     * @param group The new marker group.
     */
    public void setSelectedGroup(MarkerGroup group);
}
