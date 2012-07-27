/*
 * $Id: FixedProjection.java 2628 2008-08-05 16:05:09Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.slider;

import org.jdesktop.swingx.JXSlider;

/**
 * Defines a fixed size projection from the static long value range. This means that the values set will ignore the
 * given JXSlider and return the same values from project no matter what arguments are given.
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 */
public class FixedProjection implements Projection {

    /**
     * Keeps track of the values and is returned from project.
     */
    private ViewRange viewRange;

    /**
     * Creates a new projection with the values 0, 100.
     */
    public FixedProjection() {
        this(0, 100);
    }





    /**
     * Create a new projection with the given start and length values.
     *
     * @param startValue The start of the projection view.
     * @param length The length of the projection view.
     */
    public FixedProjection(long startValue, long length) {
        this.viewRange = new DefaultViewRange(startValue, startValue + length);
    }





    /**
     * Get the start value for this projection.
     *
     * @return The start value.
     */
    public long getStartValue() {
        return viewRange.getMinimumView();
    }





    /**
     * Get the length of this projection.
     *
     * @return The length of the projection.
     */
    public long getLength() {
        return viewRange.getMaximumView() - viewRange.getMinimumView();
    }





    /**
     * Set the start of the projection.
     *
     * @param startValue The start value for the projection.
     */
    public void setStartValue(long startValue) {
        setValues(startValue, getLength());
    }





    /**
     * Set the length of the projection.
     *
     * @param length The projection length.
     */
    public void setLength(long length) {
        setValues(getStartValue(), length);
    }





    /**
     * Set both the start value and length of the projection in one go.
     *
     * @param startValue The start value of the projection.
     * @param length The length of the projection.
     */
    public void setValues(long startValue, long length) {
        this.viewRange = new DefaultViewRange(startValue, startValue + length);
    }





    /**
     * Project a ViewRange onto the given JXSlider to fit within the given pixelSize.
     *
     * @param slider The target for the projection.
     * @param pixelSize The number of pixels the projection will be mapped to.
     * @return The range of values suitable for this projection.
     */
    public ViewRange project(JXSlider slider, int pixelSize) {
        return viewRange;
    }
}
