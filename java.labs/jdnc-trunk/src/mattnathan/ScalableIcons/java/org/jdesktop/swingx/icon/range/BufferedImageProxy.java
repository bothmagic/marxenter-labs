/*
 * $Id: BufferedImageProxy.java 2628 2008-08-05 16:05:09Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.icon.range;

import java.awt.Image;
import java.awt.image.BufferedImage;

/**
 * ImageProxy implementation for use with BufferedImages.
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 */
public class BufferedImageProxy implements ImageProxy {
    /**
     * The image instance that this proxy represents.
     */
    private BufferedImage image;

    /**
     * Creates a new image proxy for the given image.
     *
     * @param image BufferedImage
     */
    public BufferedImageProxy(BufferedImage image) {
        if (image == null) {
            throw new IllegalArgumentException("image cannot be null");
        }
        this.image = image;
    }





    /**
     * Always returns true.
     *
     * @return {@code true}.
     */
    public boolean isReady() {
        return true;
    }





    /**
     * Does nothing and returns true.
     *
     * @param wait boolean
     * @return {@code true}.
     */
    public boolean makeReady(boolean wait) {
        return true;
    }





    /**
     * Returns the BufferedImage instance.
     *
     * @return Image
     */
    public Image getImage() {
        return image;
    }





    /**
     * The width of the BufferedImage source.
     *
     * @return The image width
     */
    public int getImageWidth() {
        return image.getWidth();
    }





    /**
     * the height of the BufferedImage source.
     *
     * @return the image height
     */
    public int getImageHeight() {
        return image.getHeight();
    }
}
