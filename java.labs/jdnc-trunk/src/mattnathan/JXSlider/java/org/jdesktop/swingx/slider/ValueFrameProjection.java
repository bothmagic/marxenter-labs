/*
 * $Id: ValueFrameProjection.java 2628 2008-08-05 16:05:09Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.slider;

import org.jdesktop.swingx.JXSlider;

/**
 * A frame projection who looks in the given JXSlider for any ValueMarkerGroups and collects the minimum and maximum
 * values from them.
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 * @see ValueMarkerGroup
 */
public class ValueFrameProjection extends AbstractFrameProjection {
    /**
     * Create a new non-valueCentric FrameProjection with minimum size of 100 and no padding.
     */
    public ValueFrameProjection() {
        super();
    }





    /**
     * Create a new frame projection from the given properties.
     *
     * @param minimumSize The minimum size the frame should be
     * @param paddingLeft The padding to apply between the minimum value and minimum model range.
     * @param paddingRight The padding to apply between the maximum value and maximum model range.
     * @param valueCentric {@code true} if the frame should center around the values.
     */
    public ValueFrameProjection(long minimumSize, long paddingLeft, long paddingRight, boolean valueCentric) {
        super(minimumSize, paddingLeft, paddingRight, valueCentric);
    }





    /**
     * Get the maximum value that should be shown.
     *
     * @param slider the slider to get the value from.
     * @return The maximum value.
     */
    @Override
    protected long getMaximumFocus(JXSlider slider) {
        long min = 0;
        boolean valueSet = false;
        for (int i = 0, n = slider.getMarkerGroupCount(); i < n; i++) {
            MarkerGroup mg = slider.getMarkerGroup(i);
            if (mg instanceof ValueMarkerGroup) {
                if (!valueSet || ((ValueMarkerGroup) mg).getValue() < min) {
                    valueSet = true;
                    min = ((ValueMarkerGroup) mg).getValue();
                }
            }
        }
        return min;
    }





    /**
     * Get the minimum value that should be shown.
     *
     * @param slider The slider to get the value from
     * @return The minimum focus value.
     */
    @Override
    protected long getMinimumFocus(JXSlider slider) {
        long max = 0;
        boolean valueSet = false;
        for (int i = 0, n = slider.getMarkerGroupCount(); i < n; i++) {
            MarkerGroup mg = slider.getMarkerGroup(i);
            if (mg instanceof ValueMarkerGroup) {
                if (!valueSet || ((ValueMarkerGroup) mg).getValue() > max) {
                    valueSet = true;
                    max = ((ValueMarkerGroup) mg).getValue();
                }
            }
        }
        return max;

    }
}
