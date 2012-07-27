/*
 * $Id: DefaultMarkerSelectionModel.java 2628 2008-08-05 16:05:09Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.slider;

/**
 * The default implementation of a slider models marker selection.
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 */
public class DefaultMarkerSelectionModel extends AbstractMarkerSelectionModel {
    private long selectedValue;
    private boolean valueSelected = false;
    private MarkerGroup selectedGroup;

    public DefaultMarkerSelectionModel() {
        super();
    }





    /**
     * Create a new model with the given initial group.
     *
     * @param selectedGroup The selected marker group.
     */
    public DefaultMarkerSelectionModel(MarkerGroup selectedGroup) {
        this.selectedGroup = selectedGroup;
    }





    /**
     * Create a new selection model with the given value and group.
     *
     * @param selectedGroup The group the value is from.
     * @param selectedValue The value selected.
     */
    public DefaultMarkerSelectionModel(MarkerGroup selectedGroup, long selectedValue) {
        this.selectedGroup = selectedGroup;
        this.selectedValue = selectedValue;
        this.valueSelected = selectedGroup != null;
    }





    /**
     * Return the selected group of markers.
     *
     * @return The marker group.
     */
    public MarkerGroup getSelectedGroup() {
        return selectedGroup;
    }





    /**
     * Set the selected value. This will throw an IllegalArgumentException if the selected MarkerGroup is null.
     *
     * @param value the value selected.
     */
    public void setSelectedValue(long value) {
        if (getSelectedGroup() == null) {
            throw new IllegalArgumentException("cannot select a value when the select MarkerGroup is null");
        }
        if (!isValueSelected()) {
            valueSelected = true;
            this.selectedValue = value;
            fireStateChanged();
        } else {
            long old = getSelectedValue();
            this.selectedValue = value;
            if (getSelectedValue() != old) {
                fireStateChanged();
            }
        }
    }





    /**
     * Clears the selected value from this model.
     */
    public void clearValueSelection() {
        if (valueSelected) {
            valueSelected = false;
            fireStateChanged();
        }
    }





    /**
     * Set the selected marker group. This will clear the current selected value.
     *
     * @param group The new marker group.
     */
    public void setSelectedGroup(MarkerGroup group) {
        MarkerGroup old = getSelectedGroup();
        this.selectedGroup = group;
        group = getSelectedGroup();
        if (group != old) {
            valueSelected = false;
            fireStateChanged();
        }
    }





    /**
     * Get the value selected within the MarkerGroup.
     *
     * @return The value selected.
     */
    public long getSelectedValue() {
        if (!isValueSelected()) {
            throw new IllegalStateException("cannot return a value if none exist");
        }
        return selectedValue;
    }





    /**
     * Returns true if a value is selected, false otherwise.
     *
     * @return Whether a value is selected or not.
     */
    public boolean isValueSelected() {
        return valueSelected;
    }
}
