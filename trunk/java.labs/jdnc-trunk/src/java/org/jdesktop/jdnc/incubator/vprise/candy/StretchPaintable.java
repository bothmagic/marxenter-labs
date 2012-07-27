package org.jdesktop.jdnc.incubator.vprise.candy;

import java.net.URL;
import java.awt.Graphics;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.awt.Image;
import java.awt.Component;

/**
 * A paintable object that allows us to tile an image in the background
 *
 * @author Shai Almog
 */
public class StretchPaintable extends ImagePaintable {
    
    
    public StretchPaintable(URL image) {
        super(image);
    }
    
    /**
     * This method will be called to paint the paintable content within the
     * given coordinates. The graphics object should not be modified and the
     * painting code should not make assumptions regarding the clipping region
     * or x == 0/y == 0 that may not be applicable in all situations.
     *
     * @param g the graphics object that may not be modified by this method
     * @param x the x position from which to begin may not match the clipping region
     * @param y the y position from which to begin may not match the clipping region
     * @param opaque if true this paint method should make sure to fill every
     *   pixel in the region of x/y to width/height 
     */
    protected void paint(Image image, Component cmp, Graphics g, int x, int y, int width, int height, boolean opaque) {
        g.drawImage(image, x, y, width, height, null);
    }
}

