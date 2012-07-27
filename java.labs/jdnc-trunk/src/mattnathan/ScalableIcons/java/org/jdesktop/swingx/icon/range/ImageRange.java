/*
 * $Id: ImageRange.java 2628 2008-08-05 16:05:09Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.icon.range;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.util.ArrayList;
import java.util.List;

import org.jdesktop.swingx.icon.ScalableIcon;
import org.jdesktop.swingx.image.ImageUtilities.*;
import org.jdesktop.swingx.image.*;
import org.jdesktop.swingx.icon.*;

/**
 * This class defines a Range which represents an image. The image itself is
 * represented by an ImageProxy instance.
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 */
public class ImageRange extends AbstractRange {

    private ImageProxy imageProxy;

    /**
     * Creates a new ImageRange for the given Image. See the constructor with
     * width and height arguments for more details.
     *
     * @param image Image
     */
    public ImageRange(Image image) {
        this(image, -1, -1);
    }





    /**
     * Creates a new ImageRange for the given image. If the image given is a
     * BufferedImage then the width and height arguments are ignored else the
     * width and height should represent the width and height of the given image.
     *
     * @param image Image
     * @param width int
     * @param height int
     */
    public ImageRange(Image image, int width, int height) {
        if (image instanceof BufferedImage) {
            imageProxy = new BufferedImageProxy((BufferedImage) image);
        } else {
            imageProxy = new ToolkitImageProxy(image, width, height);
        }
    }





    /**
     * Creates a new ImageRange for the given ImageProxy representation. If the
     * image proxy is an instance of DynamicProxy then a listener is added to it
     * to forward ChangeEvents to interested parties.
     *
     * @param imageProxy ImageProxy
     */
    public ImageRange(ImageProxy imageProxy) {
        if (imageProxy == null) {
            throw new IllegalArgumentException("imageProxy cannot be null");
        }
        this.imageProxy = imageProxy;
        if (imageProxy instanceof DynamicProxy) {
            ((DynamicProxy) imageProxy).addChangeListener(new ChangeListener() {
                public void stateChanged(ChangeEvent e) {
                    fireStateChanged();
                }
            });
        }
    }





    /**
     * Creates a new ImageRange for the image represented by the given url. The
     * default ImageProxy created for this url is a ThreadedImageProxy with a
     * URLImageLoader.
     *
     * @param url URL
     * @param width int
     * @param height int
     */
    public ImageRange(URL url, int width, int height) {
        this(new URLImageLoader(url, width, height));
    }





    /**
     * Creates a new ImageRange for the given ImageLoader. This uses a
     * ThreadedImageProxy to handle the image loading setup.
     *
     * @param imageLoader ImageLoader
     */
    public ImageRange(ImageLoader imageLoader) {
        this(new ThreadedImageProxy(imageLoader));
    }





    /**
     * Converts the given path pattern and sizes into an array of ImageRanges.
     * If baseClass is null then the result of applying a size to the pattern is
     * assumed to be a valid File path. If baseClass is not null then only
     * results from baseClass.getResource(String.format(pattern, size)) which are
     * not null are converted into ImageRanges and returned.
     *
     * @param baseClass Class
     * @param pattern String
     * @param sizes int[]
     * @return ImageRange[]
     */
    public static ImageRange[] convert(Class<?> baseClass, String pattern, int ...sizes) {
        List<ImageRange> result = new ArrayList<ImageRange>(sizes.length);

        for (int size : sizes) {
            String tmp = String.format(pattern, size);
            URL url = null;
            if (baseClass == null) {
                try {
                    url = new File(tmp).toURI().toURL();
                } catch (MalformedURLException ex) {
                    // handle this later
                }

            } else {
                url = baseClass.getResource(tmp);
            }
            if (url != null) {
                result.add(new ImageRange(url, size, size));
            }
        }

        return result.toArray(new ImageRange[result.size()]);
    }





    /**
     * Converts the given images into ImageRanges for use in a
     * DefaultScalableIcon instance.
     *
     * @param images Image[]
     * @return ImageRange[]
     */
    public static ImageRange[] convert(Image ...images) {
        ImageRange[] result = new ImageRange[images.length];

        for (int i = 0, n = images.length; i < n; i++) {
            result[i] = new ImageRange(images[i]);
        }

        return result;
    }





    /**
     * Returns an array of ImageRanges which represent the given ImageLoaders.
     * Each ImageRange uses a ThreadedImageProxy to manager the image loading
     * process.
     *
     * @param imageLoaders ImageLoader[]
     * @return ImageRange[]
     */
    public static ImageRange[] convert(ImageLoader ...imageLoaders) {
        ImageRange[] result = new ImageRange[imageLoaders.length];
        for (int i = 0, n = imageLoaders.length; i < n; i++) {
            result[i] = new ImageRange(imageLoaders[i]);
        }
        return result;
    }





    /**
     * Get this source image for this range.
     *
     * @return Image
     * @throws ImageLoaderException if there was a problem loading the image.
     */
    public Image getImage() throws ImageLoaderException {
        return imageProxy.getImage();
    }





    /**
     * {@inheritDoc}
     */
    public int getRangeHeight() {
        return imageProxy.getImageHeight();
    }





    /**
     * {@inheritDoc}
     */
    public int getRangeWidth() {
        return imageProxy.getImageWidth();
    }





    /**
     * {@inheritDoc}
     */
    public boolean isReady() {
        return imageProxy.isReady();
    }





    /**
     * {@inheritDoc}
     */
    public boolean makeReady(boolean wait) {
        return imageProxy.makeReady(wait);
    }





    /**
     * Paint the representation of this range onto the given graphics component.
     *
     * @param container ScalableIcon
     * @param c Component
     * @param g Graphics
     * @param x int
     * @param y int
     * @param width int
     * @param height int
     */
    public void paint(ScalableIcon container, Component c, Graphics g, int x, int y, int width, int height) {
        try {
            Image image = getImage();
            if (width == imageProxy.getImageWidth() && height == imageProxy.getImageHeight()) {
                g.drawImage(image, x, y, null);
            } else {
                Interpolation interp;
                if (container instanceof AbstractScalableIcon) {
                    interp = ((AbstractScalableIcon) container).getInterpolation();
                } else {
                    interp = Interpolation.NEAREST_NEIGHBOUR;
                }
                ImageUtilities.drawScaledImage(g, image, x, y, width, height, interp);
//                g.drawImage(image, x, y, width, height, null);
            }

        } catch (ImageLoaderException ex) {
            paintError(container, c, g, x, y, width, height, ex);
        }
    }





    /**
     * Paint a representation of the given error. This will delegate to the given container if it is a
     * ImageRangeErrorPainter otherwise will paint a red cross across the target area.
     *
     * @param container The icon containing this range.
     * @param c The component to paint on.
     * @param g The graphics to paint on.
     * @param x The x coord to paint to.
     * @param y The y coord to paint to.
     * @param width The width of the target area.
     * @param height The height of the target area.
     * @param e The exception causing the range to not load correctly.
     */
    protected void paintError(ScalableIcon container, Component c, Graphics g, int x, int y, int width, int height, ImageLoaderException e) {
        if (container instanceof ImageRangeErrorPainter) {
            ((ImageRangeErrorPainter) container).paintError(container, c, g, x, y, width, height, e);
        } else {
            g.setColor(Color.RED);
            g.drawLine(x, y, x + width, y + height);
            g.drawLine(x, y + height, x + width, y);
        }
    }





    /**
     * Paints the icon given an error while loading the range. If the calling ScalableIcon implements this interface
     * then that error painting is delegated there.
     *
     * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
     */
    public static interface ImageRangeErrorPainter {
        /**
         * Paint a representation of the given exception.
         *
         * @param container The icon containing this range.
         * @param c The component to paint on.
         * @param g The graphics to paint on.
         * @param x The x coord to paint to.
         * @param y The y coord to paint to.
         * @param width The width of the target area.
         * @param height The height of the target area.
         * @param e The exception causing the range to not load correctly.
         */
        public void paintError(ScalableIcon container, Component c, Graphics g, int x, int y, int width, int height, ImageLoaderException e);
    }
}
