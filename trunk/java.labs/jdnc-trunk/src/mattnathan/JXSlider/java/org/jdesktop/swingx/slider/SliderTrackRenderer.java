/*
 * $Id: SliderTrackRenderer.java 2628 2008-08-05 16:05:09Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.slider;

import java.awt.Component;

import org.jdesktop.swingx.JXSlider;

/**
 * Defines the interface for a factory class that creates Component instances to represent the track of a JXSlider. To
 * improve performance the returned Component should be reused with properties being configured to match the required
 * state. The returned components preferred size and alignmentX and alignmentY values will be used to position the track
 * when displayed as part of the JXSlider instance.
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 */
public interface SliderTrackRenderer {
    /**
     * Return a component that can be used to represent the track of a JXSlider. The value passed will be the value of
     * the active marker and can be used in such cases as like in the Ocean metal theme where the slider fills the
     * trailing region of the track.
     *
     * @param slider The slider to represent.
     * @param minimum The minimum value for the track.
     * @param maximum The maximum value for the track.
     * @param value The value currently active.
     * @param view The current view on the track.
     * @param valueEditable {@code true} if the given value is editable.
     * @param enabled {@code true} if the slider and value is enabled.
     * @return The component to use to represent the track of a JXSlider.
     */
    public Component getSliderTrackRendererComponent(JXSlider slider, long minimum, long maximum, long value,
          ViewRange view, boolean valueEditable, boolean enabled);
}
