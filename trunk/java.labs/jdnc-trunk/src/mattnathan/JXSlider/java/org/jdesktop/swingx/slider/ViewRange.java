/*
 * $Id: ViewRange.java 2628 2008-08-05 16:05:09Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.slider;

/**
 * Defines an immutable range on a JXSliders model. This is used to reduce the visible or active part of the slider to
 * a more manageable level. Instances of this class are created by the Projection class which is applied to a JXSlider.
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 * @see DefaultViewRange
 * @see Projection#project
 */
public interface ViewRange {
    /**
     * Get the minimum viewable value of the SliderModel.
     *
     * @return The minimum viewable value.
     */
    long getMinimumView();





    /**
     * Get the maximum viewable value of the SliderModel.
     *
     * @return The maximum viewable value.
     */
    long getMaximumView();
}
