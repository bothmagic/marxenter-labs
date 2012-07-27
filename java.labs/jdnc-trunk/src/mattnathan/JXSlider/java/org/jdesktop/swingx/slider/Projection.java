/*
 * $Id: Projection.java 2628 2008-08-05 16:05:09Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.slider;

import org.jdesktop.swingx.JXSlider;

/**
 * Defines a view projection for reducing the 'infinite' range of the SliderModel to a manageable amount. This interface
 * defines a single method {@link #project} which takes the JXSlider instance that is requesting the projection and an
 * int argument representing the number of pixels the ViewRange is to rendered onto.
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 */
public interface Projection {
    /**
     * Project a ViewRange onto the given JXSlider to fit within the given pixelSize.
     *
     * @param slider The target for the projection.
     * @param pixelSize The number of pixels the projection will be mapped to.
     * @return The range of values suitable for this projection.
     */
    ViewRange project(JXSlider slider, int pixelSize);
}
