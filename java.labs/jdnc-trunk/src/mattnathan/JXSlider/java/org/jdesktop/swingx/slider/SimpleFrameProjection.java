/*
 * $Id: SimpleFrameProjection.java 2628 2008-08-05 16:05:09Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.slider;

import org.jdesktop.swingx.JXSlider;

/**
 * Simple frame projection which allows the user to set the specific values that will be in focus.
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 */
public class SimpleFrameProjection extends AbstractFrameProjection {
    private long minimumFocus;
    private long maximumFocus;

    /**
     * Create a new frame projection with minimum and maximum values of 0.
     */
    public SimpleFrameProjection() {
        this(0, 0);
    }





    /**
     * Create a new frame projection with the given minimum and maximum focus values.
     *
     * @param minimumFocus The minimum focus value.
     * @param maximumFocus The maximum focus value.
     */
    public SimpleFrameProjection(long minimumFocus, long maximumFocus) {
        this.minimumFocus = minimumFocus;
        this.maximumFocus = maximumFocus;
    }





    /**
     * Get the maximum value that should be shown. This returns the user set maximum focus value.
     *
     * @param slider the slider to get the value from.
     * @return The maximum value.
     * @see #setMaximumFocus(long)
     * @see #getMaximumFocus()
     */
    @Override
    protected long getMaximumFocus(JXSlider slider) {
        return getMaximumFocus();
    }





    /**
     * Get the minimum value that should be shown. This returns the user set minimum focus value.
     *
     * @param slider The slider to get the value from
     * @return The minimum focus value.
     * @see #setMinimumFocus(long)
     * @see #getMinimumFocus()
     */
    @Override
    protected long getMinimumFocus(JXSlider slider) {
        return getMinimumFocus();
    }





    /**
     * Get the user-set maximum focus for the frame.
     *
     * @return The maximum focus.
     */
    public long getMaximumFocus() {
        return maximumFocus;
    }





    /**
     * Get the user-set minimum focus for the frame.
     *
     * @return The minimum focus.
     */
    public long getMinimumFocus() {
        return minimumFocus;
    }





    /**
     * Set the maximum focus for this frame.
     *
     * @param maximumFocus The maximum focus for the frame.
     */
    public void setMaximumFocus(long maximumFocus) {
        long old = getMaximumFocus();
        this.maximumFocus = maximumFocus;
        firePropertyChange("maximumFocus", old, getMaximumFocus());
    }





    /**
     * Set the minimum focus for this frame.
     *
     * @param minimumFocus The minimum focus for the frame.
     */
    public void setMinimumFocus(long minimumFocus) {
        long old = getMinimumFocus();
        this.minimumFocus = minimumFocus;
        firePropertyChange("minimumFocus", old, getMinimumFocus());
    }
}
