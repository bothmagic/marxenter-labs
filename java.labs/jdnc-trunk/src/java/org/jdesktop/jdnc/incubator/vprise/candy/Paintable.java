package org.jdesktop.jdnc.incubator.vprise.candy;

import java.awt.Graphics;
import java.awt.Component;

/**
 * This interface allows us to represent an element that can be painted
 * in varying resolutions unlike an icon that has a fixed width/height.
 *
 * @see BackgroundPanel
 * @author Shai Almog
 */
public interface Paintable {
    /**
     * This method will be called to paint the paintable content within the
     * given coordinates. The graphics object should not be modified and the
     * painting code should not make assumptions regarding the clipping region
     * or x == 0/y == 0 that may not be applicable in all situations.
     *
     * @param cmp the component on whom this paintable is drawn, a paintable may
     *   be reused for multiple components and this component must not be cached
     *   or assumed.
     * @param g the graphics object that may not be modified by this method
     * @param x the x position from which to begin may not match the clipping region
     * @param y the y position from which to begin may not match the clipping region
     * @param opaque if true this paint method should make sure to fill every
     *   pixel in the region of x/y to width/height 
     */
    public void paint(Component cmp, Graphics g, int x, int y, int width, int height, boolean opaque);
}
