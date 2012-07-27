/*
 * $Id: AbstractGraphicsRenderer.java 2631 2008-08-06 09:23:10Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.graphics;

import java.awt.Graphics2D;
import java.awt.RenderingHints;

import org.jdesktop.beans.AbstractBean;
import org.jdesktop.beans.PropertySupporter;
import org.jdesktop.swingx.image.ImageUtilities.*;

/**
 * Provides an abstract base class for creating rendering classes which are designed to act on a Graphics object.
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 */
public class AbstractGraphicsRenderer extends AbstractBean implements PropertySupporter {
    private boolean antialiasing = true;
    private Interpolation interpolation = Interpolation.NEAREST_NEIGHBOUR;

    /**
     * <p>This method is called by the <code>paint</code> method prior to any drawing operations to configure the
     * drawing surface. The default implementation sets the rendering hints that have been specified for this
     * <code>AbstractGraphicsRenderer</code>.</p>
     * <p/>
     * <p>This method can be overriden by subclasses to modify the drawing surface before any painting happens.</p>
     *
     * @param g the graphics surface to configure. This will never be null.
     */
    protected void configureGraphics(Graphics2D g) {
        //configure antialiasing
        if (isAntialiasing()) {
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                               RenderingHints.VALUE_ANTIALIAS_ON);
        } else {
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                               RenderingHints.VALUE_ANTIALIAS_OFF);
        }

        getInterpolation().configureGraphics(g);
    }





    /**
     * Returns if antialiasing is turned on or not. The default value is true. This is a bound property.
     *
     * @return the current antialiasing setting
     */
    public boolean isAntialiasing() {
        return antialiasing;
    }





    /**
     * Sets the antialiasing setting.  This is a bound property.
     *
     * @param value the new antialiasing setting
     */
    public void setAntialiasing(boolean value) {
        boolean old = isAntialiasing();
        antialiasing = value;
        firePropertyChange("antialiasing", old, isAntialiasing());
    }





    /**
     * Gets the current interpolation setting. This property determines if interpolation will be used when drawing
     * scaled images. @see java.awt.RenderingHints.KEY_INTERPOLATION.
     *
     * @return the current interpolation setting
     */
    public Interpolation getInterpolation() {
        return interpolation;
    }





    /**
     * Sets a new value for the interpolation setting. This setting determines if interpolation should be used when
     * drawing scaled images. If the value of null is given this will set the interpolation to NEAREST_NEIGHBOUR.
     *
     * @param value the new interpolation setting
     * @see java.awt.RenderingHints#KEY_INTERPOLATION
     */
    public void setInterpolation(Interpolation value) {
        Object old = getInterpolation();
        this.interpolation = value == null ? Interpolation.NEAREST_NEIGHBOUR : value;
        firePropertyChange("interpolation", old, getInterpolation());
    }
}
