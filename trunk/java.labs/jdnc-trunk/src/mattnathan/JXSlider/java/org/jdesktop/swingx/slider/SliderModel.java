/*
 * $Id: SliderModel.java 2628 2008-08-05 16:05:09Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.slider;

import org.jdesktop.swingx.event.SliderChangeListener;

/**
 * Defines the model for a JXSlider. This defines a minimum and maximum range and a number of markers between that
 * range. Each marker is collated by a MarkerGroup object which keeps markers of the same type together.
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 * @see MarkerGroup
 */
public interface SliderModel {
    /**
     * Get the minimum value in this models range. This can take any value positive or negative but must be <= to
     * {@code getMaximum()}.
     *
     * @return The models minimum range.
     */
    public long getMinimum();





    /**
     * Get the maximum value in this model range (inclusive). This can take any value positive or negative but must be
     * >= to {@code getMinimum()}.
     *
     * @return The models maximum range.
     */
    public long getMaximum();





    /**
     * Gets a collection of markers that have been applied to this model.
     *
     * @param index The index of the group to return.
     * @return The group of markers that this model contains.
     * @see #getMarkerGroupCount()
     */
    public MarkerGroup getMarkerGroup(int index);





    /**
     * Gets the number of marker groups this model has applied. This will return a value >= 0.
     *
     * @return The number of marker groups this model has.
     */
    public int getMarkerGroupCount();





    /**
     * Add a listener to this model for notification of changes to the MarkerGroup list and the minimum and maximum
     * values. {@code null} values will be ignored.
     *
     * @param l The slider listeners.
     */
    public void addSliderChangeListener(SliderChangeListener l);





    /**
     * Remove a listener from this model previously added via the addSliderChangeListener method. {@code null} values
     * will be ignored.
     *
     * @param l The listener to remove.
     */
    public void removeSliderChangeListener(SliderChangeListener l);





    /**
     * Gets an array of all listeners added to this model via the addSliderChangeListener method. This will never return
     * null but may return an empty array.
     *
     * @return The list of added listeners.
     */
    public SliderChangeListener[] getSliderChangeListeners();
}
