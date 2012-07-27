/*
 * $Id: ImageUtilities.java 2770 2008-10-10 08:51:24Z mattnathan $
 *
 * Copyright 2005 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */

package org.jdesktop.swingx.image;

import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;

import java.util.concurrent.CountDownLatch;

public class ImageUtilities {
    private ImageUtilities() {
    }

    /**
     * An enum representing the possible interpolation values of Bicubic, Bilinear, and Nearest Neighbour. These map to
     * the underlying RenderingHints, but are easier to use and serialization safe.
     */
    public static enum Interpolation {
        /**
         * use bicubic interpolation
         */
        BICUBIC(RenderingHints.VALUE_INTERPOLATION_BICUBIC),
        /**
         * use bilinear interpolation
         */
        BILINEAR(RenderingHints.VALUE_INTERPOLATION_BILINEAR),
        /**
         * Use BILINEAR but multi-step for better results.
         */
        BILINEAR_MULTISTEP(RenderingHints.VALUE_INTERPOLATION_BILINEAR),
        /**
         * use nearest neighbour interpolation
         */
        NEAREST_NEIGHBOUR(RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);





        private final Object value;

        Interpolation(Object value) {
            this.value = value;
        }





        /**
         * Configure the given graphics object to match this Interpolation value.
         *
         * @param g The graphics to configure.
         */
        public void configureGraphics(Graphics2D g) {
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, value);
        }
    }







    /**
     * Draws the given image at the given width and height. This will use the Interpolation to scale the image.
     *
     * @param g The graphics to paint to.
     * @param image The image to paint.
     * @param x The x coord to paint to.
     * @param y The y coord to paint to.
     * @param width The target width for the image.
     * @param height The target height for the image.
     * @param interpolation The interpolation to use. If null then NEAREST_NEIGHBOUR will be used.
     */
    public static void drawScaledImage(Graphics g, Image image, int x, int y, int width, int height, Interpolation interpolation) {
        if (width <= 0 || height <= 0) {
            return;
        }
        if (interpolation == null) {
            interpolation = Interpolation.NEAREST_NEIGHBOUR;
        }
        Graphics2D g2 = (Graphics2D) g;
        Object old = g2.getRenderingHint(RenderingHints.KEY_INTERPOLATION);

        if (interpolation == Interpolation.BILINEAR_MULTISTEP) {
            drawMultistepScaledImage(g2, image, x, y, width, height);

        } else {
            interpolation.configureGraphics(g2);
            g2.drawImage(image, x, y, width, height, null);

        }

        if (old != null) {
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, old);
        }
    }





    @SuppressWarnings({"ConstantConditions"}) // for some reason this inspection can't handle do while loops
    private static void drawMultistepScaledImage(Graphics2D g2, Image image, int x, int y, int width, int height) {
        int transparency = Transparency.TRANSLUCENT;
        if (image instanceof BufferedImage) {
            transparency = ((BufferedImage) image).getTransparency();
        }
        BufferedImage bi = bufferImage(image, transparency);

        int iw = bi.getWidth();
        int ih = bi.getHeight();

        if (iw < width || ih < height) {
            // revert back to BILINEAR
            drawScaledImage(g2, image, x, y, width, height, Interpolation.BILINEAR);
        } else {
            BufferedImage thumb = bi;
            BufferedImage temp;

            int previousWidth = iw;
            int previousHeight = ih;

            do {
                if (iw > width) {
                    iw /= 2;
                    if (iw < width) {
                        iw = width;
                    }
                }

                if (ih > height) {
                    ih /= 2;
                    if (ih < height) {
                        ih = height;
                    }
                }

                temp = createCompatibleImage(g2, iw, ih, bi.getTransparency());
                Graphics2D imgg = temp.createGraphics();
                imgg.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                                      RenderingHints.VALUE_INTERPOLATION_BILINEAR);

                imgg.drawImage(thumb, 0, 0, iw, ih,
                               0, 0, previousWidth, previousHeight, null);

                previousWidth = iw;
                previousHeight = ih;

                thumb = temp;
                imgg.dispose();
            }
            while (iw != width || ih != height);

            g2.drawImage(thumb, x, y, null);
            if (thumb != image) {
                releaseImage(thumb);
            }
            if (temp != image) {
                releaseImage(temp);
            }
        }

        if (bi != image) {
            releaseImage(bi);
        }
    }





    /**
     * Applies the given orientation to the given image. If the orientation will
     * not transform the image then the original image is returned.
     *
     * @param image The source image.
     * @param orientation The image orientation.
     * @return An oriented image.
     */
    public static BufferedImage orient(BufferedImage image, Orientation orientation) {
        double rotation = orientation.getRotation();
        boolean mirrored = orientation.isMirrored();
        AffineTransform trans = null;

        if (rotation != 0) {
            trans = getRotationTransform(image, rotation);
        }
        if (mirrored) {
            if (trans == null) {
                trans = AffineTransform.getTranslateInstance(image.getWidth(), 0); // flip along y axis
            } else {
                trans.translate(image.getWidth(), 0);
            }
            trans.scale( -1, 1);
        }
        if (trans != null) {
            // no need to use quality transform as only 90 degree rotations or flips
            AffineTransformOp op = new AffineTransformOp(trans, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);

            image = op.filter(image, null);
        }

        return image;
    }





    /**
     * Get the transform that will rotate the given image so that it will be in
     * the center of a trimmed image. This can be used when rotating an image so
     * that it will take the size of the rotated image.
     *
     * @param source The source image.
     * @param rotation The rotation in radians
     * @return The transform to be applied to the image.
     */
    public static AffineTransform getRotationTransform(BufferedImage source, double rotation) {
        if (source == null) {
            throw new NullPointerException("source cannot be null");
        }
        Rectangle2D bounds = getTransformedBounds(source, rotation);
        bounds.setRect(0, 0, bounds.getWidth(), bounds.getHeight());

        // create the translation
        AffineTransform trans = AffineTransform.getTranslateInstance(bounds.getCenterX(), bounds.getCenterY());
        trans.rotate(rotation);
        trans.translate( -source.getWidth() / 2d, -source.getHeight() / 2d);
        return trans;

    }





    /**
     * This is a copy of the getBounds2D method in AffineTransformOp. It is used
     * to calculate how big the image needs to be to fit the given dimension
     * after the given transformation.
     *
     * @param w int
     * @param h int
     * @param rotate AffineTransform
     * @return Rectangle2D
     */
    private static Rectangle2D getTransformedBounds(int w, int h, AffineTransform rotate) {

        // Get the bounding box of the src and transform the corners
        float[] pts = {0, 0, w, 0, w, h, 0, h};
        rotate.transform(pts, 0, pts, 0, 4);

        // Get the min, max of the dst
        float fmaxX = pts[0];
        float fmaxY = pts[1];
        float fminX = pts[0];
        float fminY = pts[1];

        for (int i = 2; i < 8; i += 2) {
            if (pts[i] > fmaxX) {
                fmaxX = pts[i];
            } else if (pts[i] < fminX) {
                fminX = pts[i];
            }
            if (pts[i + 1] > fmaxY) {
                fmaxY = pts[i + 1];
            } else if (pts[i + 1] < fminY) {
                fminY = pts[i + 1];
            }
        }

        return new Rectangle2D.Float(fminX, fminY, fmaxX - fminX, fmaxY - fminY);
    }





    private static Rectangle2D getTransformedBounds(BufferedImage source, double rotation) {
        return getTransformedBounds(source.getWidth(), source.getHeight(), AffineTransform.getRotateInstance(rotation));

    }





    /**
     * Converts the given image into a BufferedImage. If image is an instance of
     * BufferedImage then it is returned otherwise a BufferedImage is created
     * through the createCompatibleImage method and painted with the given image.
     *
     * @param image Image
     * @param transparency int
     * @return BufferedImage
     */
    public static BufferedImage bufferImage(Image image, int transparency) {
        switch (transparency) {
            case Transparency.BITMASK:
            case Transparency.OPAQUE:
            case Transparency.TRANSLUCENT:
                break;
            default:
                throw new IllegalArgumentException("transparency must be one of Transparencys BITMASK, OPAQUE or TRANSLUCENT");
        }
        if (image == null) {
            throw new IllegalArgumentException("image cannot be null");
        }

        BufferedImage result = null;

        if (image instanceof BufferedImage) {
            result = (BufferedImage) image;
        } else {
            try {
                ensureDimensionsLoaded(image);
            } catch (InterruptedException ex) {
                // continue
            }
            int width = image.getWidth(null);
            int height = image.getHeight(null);

            if (width >= 0 && height >= 0) {
                result = createCompatibleImage(width, height, transparency);

                Graphics g = result.getGraphics();
                try {
                    g.drawImage(image, 0, 0, null);
                } finally {
                    g.dispose();
                }
            }
        }
        return result;
    }





    /**
     * Creates a new BufferedImage which is compatible with the given Graphics
     * object. This is a convenience method for the code {@code ((Graphics2D)
     * g).getDeviceConfiguration().createCompatibleImage(width, height)}.
     *
     * @param g The graphic the resultant image will be compatible with.
     * @param width The width of the returned image.
     * @param height The height of the returned image.
     * @return A BufferedImage compatible with the given graphics and with the
     *   given dimensions.
     */
    public static BufferedImage createCompatibleImage(Graphics g, int width, int height) {
        return ((Graphics2D) g).getDeviceConfiguration().createCompatibleImage(width, height);
    }





    /**
     * Creates a new BufferedImage which is compatible with the given Graphics
     * object and is translucent. This is a convenience method an simply calls
     * {@code createCompatibleImage(g, width, height, Transparency.TRANSLUCENT)}.
     *
     * @param g The graphic the resultant image will be compatible with.
     * @param width The width of the returned image.
     * @param height The height of the returned image.
     * @return A BufferedImage compatible with the given graphics and with the
     *   given dimensions.
     */
    public static BufferedImage createCompatibleTranslucentImage(Graphics g, int width, int height) {
        return createCompatibleImage(g, width, height, Transparency.TRANSLUCENT);
    }





    /**
     * Creates a BufferedImage compatible with the given Graphics object and with
     * the given transparency. The transparency parameter must be one of
     * <ul>
     * <li>{@link Transparency#BITMASK}</li>
     * <li>{@link Transparency#OPAQUE}</li>
     * <li>{@link Transparency#TRANSLUCENT}</li>
     * </ul>
     *
     * @param g The graphic the resultant image will be compatible with.
     * @param width The width of the returned image.
     * @param height The height of the returned image.
     * @param transparency the transparency for the returned image.
     * @return A BufferedImage compatible with the given graphics and with the
     *   given dimensions.
     * @throws IllegalArgumentException if the transparency is not a valid value
     * @see Transparency#OPAQUE
     * @see Transparency#BITMASK
     * @see Transparency#TRANSLUCENT
     */
    public static BufferedImage createCompatibleImage(Graphics g, int width, int height, int transparency) {
        return ((Graphics2D) g).getDeviceConfiguration().createCompatibleImage(width, height, transparency);
    }





    /**
     * Clears the given area in the given image object. This completely
     * removes any pixel data from the given area including transparency
     * information. This is equivalent to a AlphaComposite.Clear fill of the same
     * area.
     *
     * @param image The image to clear pixel information from.
     * @param x The x coordinate of the area to clear.
     * @param y The y coordinate of the area to clear.
     * @param width The width of the area to clear.
     * @param height The height of the area to clear.
     */
    public static void clearArea(BufferedImage image, int x, int y, int width, int height) {
        Graphics g = image.createGraphics();
        try {
            clearArea(g, x, y, width, height);
        } finally {
            g.dispose();
        }
    }





    /**
     * Clears the given area in the given Graphics object. This completely
     * removes any pixel data from the given area including transparency
     * information. This is equivalent to a AlphaComposite.Clear fill of the same
     * area.
     *
     * @param g The graphics to clear pixel data from.
     * @param x The x coordinate of the area to clear.
     * @param y The y coordinate of the area to clear.
     * @param width The width of the area to clear.
     * @param height The height of the area to clear.
     */
    public static void clearArea(Graphics g, int x, int y, int width, int height) {
        Graphics2D g2 = (Graphics2D) g;
        Composite c = g2.getComposite();
        g2.setComposite(AlphaComposite.Clear);
        g2.fillRect(x, y, width, height);
        g2.setComposite(c);
    }





    /**
     * <p> Trims the image so that it only contains the given area. If the width
     * and height are smaller than the trim area and x and y are 0 then the
     * original image is returned else a new BufferedImage is created with width,
     * height size and the image is painted onto that image at the given
     * coordinates. </p>
     *
     * <p>When speed is an issue this method should be used instead of
     * BufferedImage.getSubimage as that method will cancel any hardware
     * acceleration for the image. If space is the deciding factor then use
     * BufferedImage.getSubimage as this will share the underlying data array
     * where as this method creates a completely new image.</p>
     *
     * @param image The source image to trim.
     * @param x The x coordinate of the area to return.
     * @param y The y coordinate of the area to return.
     * @param width The width of the area to return.
     * @param height The height of the area to return.
     * @return The trimmed image.
     */
    public static Image trimImage(Image image, int x, int y, int width, int height) {
        if (image == null) {
            throw new NullPointerException("image cannot be null");
        }
        Image result = image;

        if (x != 0 || y != 0 || image.getWidth(null) >= width || image.getHeight(null) >= height) {
            // need to trim image
            BufferedImage buf = createCompatibleImage(width, height, Transparency.TRANSLUCENT);
            Graphics g = buf.createGraphics();
            try {
                g.drawImage(image, -x, -y, null);
            } finally {
                g.dispose();
            }
            result = buf;
        }

        return result;
    }





    /**
     * Clears image resources. You should call this method with any image that
     * you have finished using.
     *
     * @param image The image to clear resource from.
     */
    public static void releaseImage(Image image) {
        if (image != null) {
            image.flush();
            //noinspection UnusedAssignment
            image = null;
        }
    }





    /**
     * Creates a BufferedImage which is compatible with the default screen
     * device.
     *
     * @param width int
     * @param height int
     * @param transparency int
     * @return BufferedImage
     */
    public static BufferedImage createCompatibleImage(int width, int height, int transparency) {
        return GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration().
              createCompatibleImage(
                    width, height, transparency);
    }





    /**
     * Blocks until the given information about an image is loaded. The imageData
     * flag is a bitwise set of ImageObserver flags.
     *
     * @param image Image
     * @param imageData int
     * @throws InterruptedException If the image loading is interrupted.
     */
    public static void ensureImageLoaded(Image image, final int imageData) throws InterruptedException {
        if (image instanceof BufferedImage) {
            // pass
        } else {
            /**
             * Simple class that handles the image loading notification.
             *
             * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
             * @version 1.0
             */
            final class Observer implements ImageObserver {
                /**
                 * We wait on this object for the process to complete.
                 */
                final CountDownLatch trigger = new CountDownLatch(1);

                public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
                    boolean complete = (infoflags & imageData) == imageData;
                    if (complete) {
                        // notify thread
                        trigger.countDown();
                    }
                    return!complete;
                }





                /**
                 * Call this method to wait for the image to load its width and
                 * height properties.
                 *
                 * @throws InterruptedException if interrupted
                 */
                public void waitForCompletion() throws InterruptedException {
                    trigger.await();
                }
            }

            Observer io = new Observer();
            image.getWidth(io);
            image.getHeight(io);
            io.waitForCompletion();
        }
    }





    /**
     * Ensures that the dimensions of the given image have been loaded. Calls
     * ensureImageLoaded(image, ImageObserver.HEIGHT | ImageObserver.WIDTH).
     *
     * @param image Image
     * @throws InterruptedException if the loading is interrupted
     */
    public static void ensureDimensionsLoaded(Image image) throws InterruptedException {
        ensureImageLoaded(image, ImageObserver.HEIGHT | ImageObserver.WIDTH);
    }
}
