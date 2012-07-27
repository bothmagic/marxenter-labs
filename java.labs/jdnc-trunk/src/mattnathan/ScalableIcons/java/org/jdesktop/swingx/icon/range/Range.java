/*
 * $Id: Range.java 2628 2008-08-05 16:05:09Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.icon.range;

import java.awt.Component;
import java.awt.Graphics;

import org.jdesktop.swingx.event.DynamicObject;
import org.jdesktop.swingx.icon.ScalableIcon;

/**
 * Defines a generic size range for an icon.
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 */
public interface Range extends DynamicObject, LazyLoadable {
    /**
     * Get the width of this range.
     *
     * @return The width of the range.
     */
    public int getRangeWidth();





    /**
     * Get the height of this range.
     *
     * @return The height of this range.
     */
    public int getRangeHeight();





    /**
     * Paint the representation of this range onto the given graphics component.
     *
     * @param container The source icon responsible for this range.
     * @param c The component calling the painting.
     * @param g The graphics to paint to.
     * @param x The x coord to paint to.
     * @param y The y coord to paint to.
     * @param width The width of the area to paint to.
     * @param height The height of the area to paint to.
     */
    public void paint(ScalableIcon container, Component c, Graphics g, int x, int y, int width, int height);
}
