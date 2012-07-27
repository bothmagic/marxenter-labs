/*
 * $Id: URLImageLoader.java 2628 2008-08-05 16:05:09Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.icon.range;

import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;

import java.awt.Graphics;
import java.awt.Transparency;
import java.awt.image.BufferedImage;

import org.jdesktop.swingx.image.ImageUtilities;

/**
 * Defines an image loader that uses a URL to find the image to load.
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 */
public class URLImageLoader extends AbstractImageLoader<BufferedImage> {

    /**
     * The url of the image.
     */
    private URL url;

    /**
     * Creates a new image loader for the given url.
     *
     * @param url The url of the image.
     * @param width The width the image will be.
     * @param height The height the image will be.
     */
    public URLImageLoader(URL url, int width, int height) {
        super(width, height);
        this.url = url;
    }





    /**
     * Gets the url of the image to load.
     *
     * @return The url of the image.
     */
    public URL getURL() {
        return url;
    }





    /**
     * Loads the image data via ImageIO.read(getURL()).
     *
     * @return The loaded image.
     * @throws ImageLoaderException if an error occurs while loading the image.
     */
    public BufferedImage loadImageData() throws ImageLoaderException {
        try {
            BufferedImage img = ImageIO.read(getURL());
            BufferedImage result = ImageUtilities.createCompatibleImage(img.getWidth(), img.getHeight(), Transparency.TRANSLUCENT);

            Graphics g = result.getGraphics();
            g.drawImage(img, 0, 0, null);
            g.dispose();

            ImageUtilities.releaseImage(img);

            return result;
        } catch (IOException ex) {
            throw new ImageLoaderException(this.toString(), ex);
        }
    }





    /**
     * Returns the url of this loader as a string.
     *
     * @return getURL().toString().
     */
    @Override
    public String toString() {
        return getURL().toString();
    }
}
