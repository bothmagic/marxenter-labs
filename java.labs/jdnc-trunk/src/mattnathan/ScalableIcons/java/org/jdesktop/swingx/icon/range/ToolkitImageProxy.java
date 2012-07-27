/*
 * $Id: ToolkitImageProxy.java 2628 2008-08-05 16:05:09Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.icon.range;

import java.awt.Component;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.awt.image.ImageObserver;

import org.jdesktop.swingx.image.ImageUtilities;

/**
 * Image proxy implementation for Toolkit images.
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 */
public class ToolkitImageProxy implements ImageProxy {

    /**
     * The loaded image.
     */
    private Image image;
    /**
     * The width of the image. -1 if not loaded.
     */
    private int width = -1;
    /**
     * The height of the image. -1 if not loaded.
     */
    private int height = -1;
    /**
     * True if the image is ready to be painted.
     */
    private boolean ready = false;

    /**
     * The tracker used to monitor image load status.
     */
    private MediaTracker tracker = null;

    /**
     * <p>Creates an ImageProxy for the given Toolkit image. The width and height
     * will be loaded synchronously but the image data will be loaded when asked
     * for by makeReady.</p>
     *
     * <p><emph>Note</emph>: Toolkit image may be loaded anyway even if makeReady
     * has not been called.</p>
     *
     * @param image Image
     */
    public ToolkitImageProxy(Image image) {
        this(image, -1, -1);
    }





    /**
     * Creates an ImageProxy for the given image with the given width and height.
     * If width or height are -1 then the dimensions of the image are loaded
     * immediately from the image itself returning only when the information is
     * available.
     *
     * @param image Image
     * @param width int
     * @param height int
     */
    public ToolkitImageProxy(Image image, int width, int height) {
        this.image = image;
        if (width == -1 || height == -1) {
            loadImageSize(image);
        } else {
            this.width = width;
            this.height = height;
        }
    }





    /**
     * Loads the image size waiting for the load to complete before returning.
     *
     * @param image Image
     */
    protected void loadImageSize(Image image) {
        try {
            ImageUtilities.ensureDimensionsLoaded(image);
            width = image.getWidth(null);
            height = image.getHeight(null);
        } catch (InterruptedException ex) {
            // continue
        }

    }





    /**
     * Checks with the default Toolkit to see if the image is loaded.
     *
     * @return boolean
     */
    public boolean isReady() {
        if (!ready) {
            ready = (Toolkit.getDefaultToolkit().checkImage(getImage(), -1, -1, null) & ImageObserver.ALLBITS) > 0;
        }
        return ready;
    }





    /**
     * Uses a MediaTracker for loading the image data.
     *
     * @param wait boolean
     * @return boolean
     */
    public boolean makeReady(boolean wait) {
        if (!ready) {

            if (tracker == null) {
                tracker = new MediaTracker(new Component() {});
                tracker.addImage(getImage(), 0);
            }

            if (wait) {
                try {
                    tracker.waitForAll();
                    ready = true;
                    tracker = null;
                } catch (InterruptedException ex) {
                    // continue
                }
            }
        } else if (tracker != null) {
            tracker = null;
        }

        return ready;
    }





    /**
     * {@inheritDoc}
     */
    public Image getImage() {
        return image;
    }





    /**
     * {@inheritDoc}
     */
    public int getImageWidth() {
        return width;
    }





    /**
     * {@inheritDoc}
     */
    public int getImageHeight() {
        return height;
    }
}
