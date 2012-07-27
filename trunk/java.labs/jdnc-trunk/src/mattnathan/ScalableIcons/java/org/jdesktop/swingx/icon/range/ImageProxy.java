/*
 * $Id: ImageProxy.java 2628 2008-08-05 16:05:09Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.icon.range;

import java.awt.Image;

/**
 * Delegate class for loading and getting image information. Subclasses
 * should implement specific loading mechanisms, for example a BufferedImage
 * requires no loading, but an image defined through a URL will require a lot
 * of loading.
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 */
public interface ImageProxy extends LazyLoadable {

    /**
     * Gets the image ready to be rendered. This may return null if the image has not yet been loaded.
     *
     * @return The loaded image.
     * @throws ImageLoaderException if there was an error loading the image.
     */
    public Image getImage() throws ImageLoaderException;





    /**
     * Gets the width of the image.
     *
     * @return The width of the image or -1 if the image has not yet been loaded.
     */
    public int getImageWidth();





    /**
     * Gets the height of the image.
     *
     * @return The height of the image or -1 if the image has not yet been loaded.
     */
    public int getImageHeight();
}
