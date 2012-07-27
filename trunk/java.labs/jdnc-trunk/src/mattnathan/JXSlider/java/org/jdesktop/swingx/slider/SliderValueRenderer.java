/*
 * $Id: SliderValueRenderer.java 2628 2008-08-05 16:05:09Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.slider;

import java.awt.Component;

import org.jdesktop.swingx.JXSlider;

/**
 * Defines the method for creating a component to render the values on a JXSlider. A number of properties of the
 * returned component are used for the layout and positioning of the final value representation. Generally the value
 * component will be painted at its preferred size centrally around the given value and the center of the slider track.
 * It is possible to adjust the position of the painted value by adjusting the alignment values of the returned
 * component. These alignment values will be used in conjunction with the value position to place the value rendering on
 * the track.
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 */
public interface SliderValueRenderer {

    /**
     * Return a component that can be used to layout and paint the given value on the given JXSlider. Null values may be
     * passed for the MarkerGroup and MarkerRange values only in the case where the renderer component is being used for
     * layout purposes and not for rendering.
     *
     * @param slider The slider the value belongs to.
     * @param value The value to render.
     * @param groupIndex The index within the sliders model of the MarkerGroup the value originated from.
     * @param group The group the value and MarkerRange originated from.
     * @param rangeIndex The index within the MarkerRange of the value.
     * @param range The marker range the value is collected from.
     * @param hasFocus Whether the given value has the active focus.
     * @param enabled Whether the JXSlider and the MarkerGroup are enabled.
     * @param valueAdjusting {@code true} if the value given has not yet been finalised (i.e. when dragging a thumb on a
     *   slider).
     * @return The component used to layout and paint the given value on the slider.
     */
    public Component getSliderValueRendererComponent(JXSlider slider, long value,
          int groupIndex, MarkerGroup group,
          int rangeIndex, MarkerRange range,
          boolean hasFocus, boolean enabled, boolean valueAdjusting);
}
