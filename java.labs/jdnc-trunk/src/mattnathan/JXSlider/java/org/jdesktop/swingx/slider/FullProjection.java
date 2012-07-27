/*
 * $Id: FullProjection.java 2628 2008-08-05 16:05:09Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.slider;

import org.jdesktop.swingx.JXSlider;

/**
 * Simple projection that always returns the complete model as its ViewRange. As this has no state it is recommended you
 * use the getInstance method instead of creating a new instance each time.
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 */
public class FullProjection implements Projection {
    private static final Projection SINGLETON = new FullProjection();
    /**
     * Get a shared instance of this class. This is recommended creating your own instances.
     *
     * @return The shared Projection instance.
     */
    public static Projection getInstance() {
        return SINGLETON;
    }





    /**
     * Create a new FullProjection. It is recommended that you use getInstance instead of this method as this Projection
     * type has no state.
     *
     * @see #getInstance()
     */
    public FullProjection() {
    }





    /**
     * Project a ViewRange onto the given JXSlider to fit within the given pixelSize.
     *
     * @param slider The target for the projection.
     * @param pixelSize The number of pixels the projection will be mapped to.
     * @return The range of values suitable for this projection.
     */
    public ViewRange project(JXSlider slider, int pixelSize) {
        return new DefaultViewRange(slider.getMinimum(), slider.getMaximum());
    }
}
