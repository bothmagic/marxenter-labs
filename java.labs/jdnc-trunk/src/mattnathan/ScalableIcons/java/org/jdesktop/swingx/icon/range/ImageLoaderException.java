/*
 * $Id: ImageLoaderException.java 2628 2008-08-05 16:05:09Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.icon.range;

/**
 * Thrown when an image loader could not load the image for some reason.
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 */
public class ImageLoaderException extends Exception {
    public ImageLoaderException(String message, Throwable cause) {
        super(message, cause);
    }
}
