/*
 * $Id: EffectUtilities.java 2628 2008-08-05 16:05:09Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.effect;

import org.jdesktop.swingx.image.ImageUtilities;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Transparency;
import java.awt.image.BufferedImage;

/**
 * Contains general utility methods for use within the effect package.
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 */
public class EffectUtilities {
    private EffectUtilities() {
    }





    /**
     * Creates a buffer of the given EffectSource object. The returned buffer will be compatible with the current
     * default graphics device.
     *
     * @param source The source to buffer. This cannot be null.
     * @return The buffered image of the source.
     */
    public static BufferedImage bufferEffect(EffectSource<?> source) {
        if (source == null) {
            throw new NullPointerException("source cannot be null");
        }
        Rectangle bounds = source.getSourceBounds(null);
        BufferedImage image = ImageUtilities.createCompatibleImage(bounds.width, bounds.height, getTransparencyType(source));
        return bufferEffect(source, image);
    }





    /**
     * Buffers the given EffectSource in readyness for rendering to the given graphics device.
     *
     * @param source The source to buffer. This cannot be null.
     * @param g      The device the buffer will eventually be rendered to. This cannot be null.
     * @return The buffered image of the source.
     */
    public static BufferedImage bufferEffect(EffectSource<?> source, Graphics g) {
        return bufferEffect(source, g, false);
    }





    /**
     * Buffered the given source onto the given image. No checks are performed to ensure that the image is the correct
     * size for the source, this is left up to the user.
     *
     * @param source The source for the painting operation. This cannot be null.
     * @param image  The image to paint onto. This cannot be null.
     * @return The image given as a parameter after the source has been painted onto it.
     */
    public static BufferedImage bufferEffect(EffectSource<?> source, BufferedImage image) {
        return bufferEffect(source, image, null);
    }





    /**
     * Buffers the given source image enforcing the clip region given. If the given region is null then the whole source
     * area is painted.
     *
     * @param source The source to paint.
     * @param image  The image to paint to.
     * @param clip   The clip area of the source to paint.
     * @return The image containing a buffer of the source.
     */
    public static BufferedImage bufferEffect(EffectSource<?> source, BufferedImage image, Rectangle clip) {
        if (image == null) {
            throw new NullPointerException("image cannot be null");
        }
        if (source == null) {
            throw new NullPointerException("source cannot be null");
        }
        Graphics g2 = image.createGraphics();
        try {
            if (clip != null) {
                g2.translate(-clip.x, -clip.y);
                g2.setClip(clip.x, clip.y, clip.width, clip.height);
            }
            source.paintSource(g2);
        } finally {
            g2.dispose();
        }
        return image;
    }





    /**
     * Buffer the given source optionally enforcing the clip bounds. If the clip bounds are enforced then the
     * BufferedImage returned will be the same size as the clip area, if the clip area is empty then null is returned.
     *
     * @param source      The source for the painting.
     * @param g           The graphics device to get the clip from.
     * @param enforceClip {@code true} if the clip bounds should be enforced.
     * @return The image containing a buffer of the source.
     */
    public static BufferedImage bufferEffect(EffectSource<?> source, Graphics g, boolean enforceClip) {
        if (source == null) {
            throw new NullPointerException("source cannot be null");
        }
        if (g == null) {
            throw new NullPointerException("g cannot be null");
        }
        Rectangle bounds = enforceClip ? g.getClipBounds() : source.getSourceBounds(null);
        if (enforceClip && bounds == null) {
            bounds = source.getSourceBounds(null);
        }
        if (bounds == null) {
            throw new NullPointerException("The bounds returned from EffectSource.getSourceBounds() were null");
        }
        if (bounds.width < 1 || bounds.height < 1) {
            return null; // don't bother buffering to empty bounds
        }

        BufferedImage image = ImageUtilities.createCompatibleImage(g, bounds.width, bounds.height, getTransparencyType(source));
        return bufferEffect(source, image, bounds);
    }





    /**
     * Paints the given effect source onto the given graphics with optional buffering.
     *
     * @param source   The source to paint.
     * @param g        The graphics to paint to.
     * @param buffered The buffered state, if {@code true} the source will be painted to a BufferedImage first.
     */
    public static void paintEffectSource(EffectSource<?> source, Graphics g, boolean buffered) {
        if (buffered) {
            BufferedImage img = bufferEffect(source, g, true);
            Rectangle bounds = g.getClipBounds();
            if (bounds == null) {
                bounds = source.getSourceBounds(null);
                if (bounds == null) {
                    throw new NullPointerException("The bounds returned from EffectSource.getSourceBounds() were null");
                }
            }
            g.drawImage(img, bounds.x, bounds.y, null);
            ImageUtilities.releaseImage(img);

        } else {
            source.paintSource(g);
        }
    }





    /**
     * Utility method for getting the transparency type based on whether the source Object is a Component and if it is
     * opaque or not.
     *
     * @param source The source object.
     * @return The Transparency type for creating an image to represent this source.
     */
    public static int getTransparencyType(EffectSource<?> source) {
        Object s = source.getSource();
        return s instanceof Component && ((Component) s).isOpaque() ?
                Transparency.OPAQUE :
                Transparency.TRANSLUCENT;
    }
}
