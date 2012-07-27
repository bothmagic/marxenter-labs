/*
 * $Id: DefaultViewRange.java 2628 2008-08-05 16:05:09Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.slider;

/**
 * Defines the default implementation of the ViewRange interface. This class is immutable.
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 */
public class DefaultViewRange implements ViewRange {

    private final long minimum;
    private final long maximum;
    /**
     * Create a new ViewRange with the given minimum and maximum values.
     *
     * @param minimum The minimum viewable value.
     * @param maximum The maximum viewable value.
     */
    public DefaultViewRange(long minimum, long maximum) {
        this.minimum = minimum;
        this.maximum = maximum;
    }





    /**
     * Get the maximum viewable value of the SliderModel.
     *
     * @return The maximum viewable value.
     */
    public long getMaximumView() {
        return maximum;
    }





    /**
     * Get the minimum viewable value of the SliderModel.
     *
     * @return The minimum viewable value.
     */
    public long getMinimumView() {
        return minimum;
    }
}
