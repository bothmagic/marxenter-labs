/*
 * $Id: AbstractImageLoader.java 2628 2008-08-05 16:05:09Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.icon.range;

import java.awt.Image;

/**
 * This class provides the common-case implementation of an image loader where
 * the width and height are specified at construction time.
 *
 * @param <T> The type of image this ImageLoader loads.
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 */
public abstract class AbstractImageLoader<T extends Image> implements ImageLoader<T> {
    /** The width the image should be */
    private int width;
    /** The height the image should be */
    private int height;

    /**
     * Create a new image loader with the given width and height.
     *
     * @param width The width of the image.
     * @param height The height of the image.
     */
    public AbstractImageLoader(int width, int height) {
        this.width = width;
        this.height = height;
    }





    /**
     * Returns the height of the image as it will be when loaded.
     *
     * @return image The height of the image.
     */
    public int getImageHeight() {
        return height;
    }





    /**
     * Returns the width of the image as it will be when loaded.
     *
     * @return image The width of the image.
     */
    public int getImageWidth() {
        return width;
    }
}
