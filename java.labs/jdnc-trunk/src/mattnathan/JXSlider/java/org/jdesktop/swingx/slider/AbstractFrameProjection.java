/*
 * $Id: AbstractFrameProjection.java 2628 2008-08-05 16:05:09Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.slider;

import org.jdesktop.swingx.JXSlider;
import org.jdesktop.beans.*;

/**
 * Provides a projection which spans a particular ValueMarkerGroup value. The ValueMarkerGroup used as the focus of this
 * frame is determined by the getViewGroup(JXSlider) method whose documentation contains details of which locations are
 * checked for the MarkerGroup. This Projection also provides a boolean flag to specify if the value within the
 * ViewRange should also represent the proportion of the whole model range or if it should remain central to the view.
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 */
public abstract class AbstractFrameProjection extends AbstractBean implements Projection {

    private boolean valueCentric;
    private long minimumSize;
    private long paddingLeft;
    private long paddingRight;

    /**
     * Create a new non-valueCentric FrameProjection with minimum size of 100 and no padding.
     */
    public AbstractFrameProjection() {
        this(100, 0, 0, false);
    }





    /**
     * Create a new frame projection from the given properties.
     *
     * @param minimumSize The minimum size the frame should be
     * @param paddingLeft The padding to apply between the minimum value and minimum model range.
     * @param paddingRight The padding to apply between the maximum value and maximum model range.
     * @param valueCentric {@code true} if the frame should center around the values.
     */
    public AbstractFrameProjection(long minimumSize, long paddingLeft, long paddingRight, boolean valueCentric) {
        this.minimumSize = minimumSize;
        this.paddingLeft = paddingLeft;
        this.paddingRight = paddingRight;
        this.valueCentric = valueCentric;
    }





    /**
     * Set the size of this projection.
     *
     * @param minimumSize The new size for this projection.
     */
    public void setMinimumSize(long minimumSize) {
        long old = getMinimumSize();
        this.minimumSize = minimumSize;
        firePropertyChange("minimumSize", old, getMinimumSize());
    }





    /**
     * Get the size of this projection.
     *
     * @return The size of the projection.
     */
    public long getMinimumSize() {
        return minimumSize;
    }





    /**
     * Set the policy that will be used to frame the value. If set to true then the frame will be centered around the
     * value, if false then the frame will be proportionally positioned so that matching the view within the whole model
     * range.
     *
     * @param valueCentric {@code true} if the frame should be centered around the value.
     */
    public void setValueCentric(boolean valueCentric) {
        boolean old = isValueCentric();
        this.valueCentric = valueCentric;
        firePropertyChange("valueCentric", old, isValueCentric());
    }





    /**
     * Get whether the frame is arranged around the value of the model.
     *
     * @return {@code true} if the frame is centered around the value.
     */
    public boolean isValueCentric() {
        return valueCentric;
    }





    /**
     * Get the minimum value that should be shown.
     *
     * @param slider The slider to get the value from
     * @return The minimum focus value.
     */
    protected abstract long getMinimumFocus(JXSlider slider);





    /**
     * Get the maximum value that should be shown.
     *
     * @param slider the slider to get the value from.
     * @return The maximum value.
     */
    protected abstract long getMaximumFocus(JXSlider slider);





    /**
     * Gets the padding to apply to the minimumFocus value.
     *
     * @return The minimum focus padding
     */
    public long getPaddingLeft() {
        return paddingLeft;
    }





    /**
     * Set the padding to apply between the minimum focus value and the minimum value in the model.
     *
     * @param paddingLeft The minimum value padding.
     */
    public void setPaddingLeft(long paddingLeft) {
        long old = getPaddingLeft();
        this.paddingLeft = paddingLeft;
        firePropertyChange("paddingLeft", old, getPaddingLeft());
    }





    /**
     * Get the padding to apply to the maximum focus point.
     *
     * @return The maximum focus padding value.
     */
    public long getPaddingRight() {
        return paddingRight;
    }





    /**
     * Set the padding to apply between the maximum focus value and the maximum value in the model.
     *
     * @param paddingRight The maximum value padding.
     */
    public void setPaddingRight(long paddingRight) {
        long old = getPaddingRight();
        this.paddingRight = paddingRight;
        firePropertyChange("paddingRight", old, getPaddingRight());
    }





    /**
     * Project a ViewRange onto the given JXSlider to fit within the given pixelSize.
     *
     * @param slider The target for the projection.
     * @param pixelSize The number of pixels the projection will be mapped to.
     * @return The range of values suitable for this projection.
     */
    public ViewRange project(JXSlider slider, int pixelSize) {
        long min = slider.getMinimum();
        long max = slider.getMaximum();

        double left;
        double right;

        long minValue = getMinimumFocus(slider);
        long maxValue = getMaximumFocus(slider);

        if (isValueCentric()) {
            left = minValue - getPaddingLeft();
            right = maxValue + getPaddingRight();

            if (right - left < getMinimumSize()) {
                double middle = left + (right - left) / 2D;
                double hs = getMinimumSize() / 2D;
                left = middle - hs;
                right = middle + hs;

                assert left <= minValue;
                assert right >= maxValue;
            }
        } else {
            long vSize = maxValue - minValue;
            if (vSize < getMinimumSize()) {
                vSize = getMinimumSize();
            }
            double prop = (minValue - min) / (double) (max - min - (maxValue - minValue));
            double anchor = minValue + (prop * (maxValue - minValue));
            left = (anchor - vSize * prop) - getPaddingLeft();
            if (left < min) {
                left = min;
            }
            right = left + vSize + getPaddingRight() + getPaddingLeft();

            if (right > max) {
                right = max;
                left = right - (vSize + getPaddingRight() + getPaddingLeft());
                if (left < min) {
                    left = min;
                }
            }
        }

        return new DefaultViewRange(Math.round(left), Math.round(right));
    }





    /**
     * Returns the ValueMarkerGroup that should be the focus of this Projection. This looks in the given JXSliders
     * MarkerGroups for the first MarkerGroup that is an instance of ValueMarkerGroup and returns that, otherwise
     * returns null.
     *
     * @param slider The source of the groups.
     * @return The group to focus on.
     */
    protected ValueMarkerGroup getValueGroup(JXSlider slider) {
        ValueMarkerGroup result = null;
        for (int i = 0, n = slider.getMarkerGroupCount(); i < n; i++) {
            MarkerGroup g = slider.getMarkerGroup(i);
            if (g instanceof ValueMarkerGroup) {
                result = (ValueMarkerGroup) g;
                break;
            }
        }
        return result;
    }
}
