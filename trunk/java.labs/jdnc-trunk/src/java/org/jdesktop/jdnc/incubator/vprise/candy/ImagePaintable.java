package org.jdesktop.jdnc.incubator.vprise.candy;

import java.net.URL;
import java.awt.Graphics;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.awt.Component;
import java.awt.Image;

/**
 * A paintable object that allows us to paint an image in the background
 * this class is designed as a base class for other image related paintable
 * objects.
 *
 * @author Shai Almog
 */
public class ImagePaintable implements Paintable {
    
    private Image image;
    
    public ImagePaintable(URL image) {
        this.image = java.awt.Toolkit.getDefaultToolkit().getImage(image);
        MediaTracker trak = new MediaTracker(null);
        trak.addImage(this.image, 1);
        try {
            trak.waitForAll();
        } catch (InterruptedException ignor) {}
    }
    
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
    public final void paint(Component cmp, Graphics g, int x, int y, int width, int height, boolean opaque) {
        paint(image, cmp, g, x, y, width, height, opaque);
    }

    protected void paint(Image image, Component cmp, Graphics g, int x, int y, int width, int height, boolean opaque) {
        g.drawImage(image, x, y, null);
    }
}

