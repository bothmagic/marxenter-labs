/*
 * $Id: OrientationImageLoader.java 2628 2008-08-05 16:05:09Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.icon.range;

import java.awt.Image;
import java.awt.Transparency;
import java.awt.image.BufferedImage;

import org.jdesktop.swingx.image.ImageUtilities;
import org.jdesktop.swingx.image.Orientation;

/**
 * Wraps a standard ImageLoader with an Orientation transform.
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 */
public class OrientationImageLoader implements ImageLoader {

    private final ImageLoader delegate;
    private final Orientation orientation;

    public OrientationImageLoader(Orientation orientaiton, ImageLoader delegate) {
        if (delegate == null) {
            throw new NullPointerException("delegate cannot be null");
        }
        if (orientaiton == null) {
            orientaiton = Orientation.TOP_LEFT;
        }
        this.delegate = delegate;
        this.orientation = orientaiton;
    }





    /**
     * Returns the height of the image as it will be when loaded.
     *
     * @return image height
     */
    public int getImageHeight() {
        return swapDimensions() ? delegate.getImageWidth() : delegate.getImageHeight();
    }





    /**
     * Returns the width of the image as it will be when loaded.
     *
     * @return image width
     */
    public int getImageWidth() {
        return swapDimensions() ? delegate.getImageHeight() : delegate.getImageWidth();
    }





    /**
     * Returns true if the orientation required the dimensions be swapped.
     *
     * @return boolean
     */
    private boolean swapDimensions() {
        boolean result;
        //noinspection EnumSwitchStatementWhichMissesCases
        switch (orientation) {
            case LEFT_TOP:
            case RIGHT_TOP:
            case RIGHT_BOTTOM:
            case LEFT_BOTTOM:
                result = true;
                break;
            default:
                result = false;
                break;

        }
        return result;
    }





    /**
     * Loads the image data. Delegates to the given ImageLoader and applies the
     * given orientation to the result.
     *
     * @return The loaded image.
     * @throws ImageLoaderException when the image could not be loaded.
     */
    public Image loadImageData() throws ImageLoaderException {
        Image image = delegate.loadImageData();
        BufferedImage buffer = ImageUtilities.bufferImage(image, Transparency.TRANSLUCENT);
        BufferedImage result = ImageUtilities.orient(buffer, orientation);
        if (buffer != result) {
            ImageUtilities.releaseImage(buffer);
        }
        if (image != result) {
            ImageUtilities.releaseImage(image);
        }
        return result;
    }
}
