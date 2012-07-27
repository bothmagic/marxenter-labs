/*
 * $Id: ImageLoader.java 2628 2008-08-05 16:05:09Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.icon.range;

import java.awt.Image;

/**
 * Simple interface to define an image and its loading functionality.
 *
 * @param <T> The type of image this ImageLoader loads.
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 */
public interface ImageLoader<T extends Image> {
    /**
     * Loads the image data. This should not perform any threading as that should
     * be handled externally of this class.
     *
     * @return The loaded image.
     * @throws ImageLoaderException when the image could not be loaded.
     */
    public T loadImageData() throws ImageLoaderException;





    /**
     * Returns the width of the image as it will be when loaded. Results are
     * undefined if this method returns a width different to that of the loaded
     * image. This method should not load any image data.
     *
     * @return image width
     */
    public int getImageWidth();





    /**
     * Returns the height of the image as it will be when loaded. Results are
     * undefined if this method returns a width different to that of the loaded
     * image. This method should not load any image data.
     *
     * @return image height
     */
    public int getImageHeight();
}
