/*
 * $Id: DefaultSliderModel.java 2628 2008-08-05 16:05:09Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.slider;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.util.ArrayList;
import java.util.List;
import org.jdesktop.swingx.event.*;

/**
 * The default implementation of the SliderModel. This keeps the list of MarkerGroups in a java.util.ArrayList instance
 * and allows changes to be made to the slider model via setXXX, addXXX and removeXXX methods
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 */
public class DefaultSliderModel extends AbstractSliderModel {
    private MarkerGroupAdapter markerGroupAdapter;

    private long minimum;
    private long maximum;
    private List<MarkerGroup> markerGroups;

    /**
     * Create a new slider model with minimum of 0 and maximum of 100.
     */
    public DefaultSliderModel() {
        this(0, 100);
    }





    /**
     * Create a new slider model with the given minimum and maximum values.
     *
     * @param minimum The minimum value for this model.
     * @param maximum The maximum value for this model.
     */
    public DefaultSliderModel(long minimum, long maximum) {
        this(minimum, maximum, (MarkerGroup[])null);
    }





    /**
     * Creates a new slider model with the given range and adds a ValueMarkerGroup to represent the given value
     *
     * @param minimum The minimum value.
     * @param maximum The maximum value.
     * @param value The value marked in the model.
     */
    public DefaultSliderModel(long minimum, long maximum, long value) {
        this(minimum, maximum, new ValueMarkerGroup(value));
    }





    /**
     * Create a new model with the given minimum, maximum and marker groups. If maximum is < minimum then maximum will
     * be set to minimum. Any null values in markerGroups will be omitted and a null markerGroups array will be treated
     * as an empty array setting the marker group list to empty.
     *
     * @param minimum The minimum range value.
     * @param maximum The maximum range value.
     * @param markerGroups The list of marker groups to apply to this model.
     */
    public DefaultSliderModel(long minimum, long maximum, MarkerGroup ...markerGroups) {
        if (maximum < minimum) {
            maximum = minimum;
        }
        this.minimum = minimum;
        this.maximum = maximum;
        if (markerGroups == null) {
            this.markerGroups = new ArrayList<MarkerGroup>();
        } else {
            this.markerGroups = new ArrayList<MarkerGroup>(markerGroups.length);
            for (MarkerGroup markerGroup : markerGroups) {
                if (markerGroup != null) {
                    if (markerGroupAdapter == null) {
                        markerGroupAdapter = new MarkerGroupAdapter();
                    }
                    markerGroup.addMarkerChangeListener(markerGroupAdapter);
                    this.markerGroups.add(markerGroup);
                }
            }
        }
    }





    /**
     * Gets a collection of markers that have been applied to this model.
     *
     * @param index The index of the group to return.
     * @return The group of markers that this model contains.
     */
    public MarkerGroup getMarkerGroup(int index) {
        return markerGroups.get(index);
    }





    /**
     * Gets the number of marker groups this model has applied.
     *
     * @return The number of marker groups this model has.
     */
    public int getMarkerGroupCount() {
        return markerGroups.size();
    }





    /**
     * Returns the index in this marker group list of the first occurrence of the specified
     * element, or -1 if this model does not contain this element.
     *
     * @param markerGroup element to search for.
     * @return the index in this list of the first occurrence of the specified
     * 	       element, or -1 if this model does not contain this element.
     */
    @Override
    public int indexOf(MarkerGroup markerGroup) {
        return markerGroups.indexOf(markerGroup);
    }





    /**
     * Get the maximum value in this model range (inclusive).
     *
     * @return The models maximum range.
     */
    public long getMaximum() {
        return maximum;
    }





    /**
     * Get the minimum value in this models range.
     *
     * @return The models minimum range.
     */
    public long getMinimum() {
        return minimum;
    }





    /**
     * Set the minimum value for this models range. This delegates to setRangeValues.
     *
     * @param minimum The minimum range value.
     * @see #setMaximum
     * @see #setRangeValues
     */
    public void setMinimum(long minimum) {
        setRangeValues(minimum, getMaximum());
    }





    /**
     * Set the maximum value for this models range. This delegates to setRangeValues.
     *
     * @param maximum The maximum range value.
     * @see #setMinimum
     * @see #setRangeValues
     */
    public void setMaximum(long maximum) {
        setRangeValues(getMinimum(), maximum);
    }





    /**
     * Set both the minimum and maximum values of the model together. If minimum > maximum then the values will be
     * corrected following these rules:
     * <ul>
     * <li>If only one value is different from the current value then that value will be set as both minimum and
     * maximum.</li>
     * <li>Else the maximum value will be given the value of the minimum.</li>
     * </ul>
     *
     * @param minimum The minimum value.
     * @param maximum The maximum value.
     */
    public void setRangeValues(long minimum, long maximum) {
        long oldMin = getMinimum();
        long oldMax = getMaximum();
        if (minimum != oldMin || maximum != oldMax) {
            if (maximum < minimum) {
                if (minimum != oldMin) {
                    maximum = minimum;
                } else {
                    minimum = maximum;
                }
            }
            this.minimum = minimum;
            this.maximum = maximum;
            fireSliderRangeChanged();
        }
    }





    /**
     * Adds a marker group to this model at the end of the list.
     *
     * @param markerGroup The marker group to add.
     * @throws IllegalArgumentException if the given markerGroup is null.
     */
    public void addMarkerGroup(MarkerGroup markerGroup) {
        addMarkerGroup(getMarkerGroupCount(), markerGroup);
    }





    /**
     * Adds the given MArkerGroup at the given index.
     *
     * @param index The index to add the group to.
     * @param markerGroup The marker group to add.
     * @throws IllegalArgumentException if the given markerGroup is null.
     * @throws IndexOutOfBoundsException if the given index is outside the range [0, markerGroupCount].
     */
    public void addMarkerGroup(int index, MarkerGroup markerGroup) {
        if (markerGroup == null) {
            throw new IllegalArgumentException("markerGroup cannot be null");
        }
        if (markerGroupAdapter == null) {
            markerGroupAdapter = new MarkerGroupAdapter();
        }
        markerGroup.addMarkerChangeListener(markerGroupAdapter);
        markerGroups.add(index, markerGroup);
        fireSliderMarkerGroupAdded(index, index);
    }





    /**
     * Remove the marker group at the specified index.
     *
     * @param index The index to remove.
     * @throws IndexOutOfBoundsException if the index is out of range (index &lt; 0 || index &gt;=
     *   getMarkerGroupCount()).
     */
    public void removeMarkerGroup(int index) {
        MarkerGroup removed = markerGroups.remove(index);
        if (removed != null) {
            removed.removeMarkerChangeListener(markerGroupAdapter);
            fireSliderMarkerGroupRemoved(index, index);
        }
    }





    /**
     * Forwards change events to SliderChangeListeners added on this object from MarkerGroups.
     *
     * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
     */
    private class MarkerGroupAdapter implements MarkerChangeListener {
        public void changed(MarkerChangeEvent e, MarkerGroup group) {
            int index = markerGroups.indexOf(group);
            if (index >= 0) {
                fireSliderMarkerGroupChanged(index, index, e);
            }
        }





        public void markerChanged(MarkerChangeEvent e) {
            changed(e, (MarkerGroup) e.getSource());
        }





        public void markerAdded(MarkerChangeEvent e) {
            changed(e, (MarkerGroup) e.getSource());
        }





        public void markerRemoved(MarkerChangeEvent e) {
            changed(e, (MarkerGroup) e.getSource());
        }
    }
}
