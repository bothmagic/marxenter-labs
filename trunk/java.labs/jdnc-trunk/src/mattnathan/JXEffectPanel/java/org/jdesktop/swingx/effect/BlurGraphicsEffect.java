/*
 * $Id: BlurGraphicsEffect.java 2628 2008-08-05 16:05:09Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.effect;

import org.jdesktop.swingx.image.FastBlurFilter;
import org.jdesktop.swingx.image.GaussianBlurFilter;
import org.jdesktop.swingx.image.ImageUtilities;
import org.jdesktop.swingx.image.StackBlurFilter;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;

/**
 * An effect which applies a blur to the source rendering. This effect can use one of three different blurring techniques
 * each of which balance speed and quality. <dl> <dt>{@link BlurGraphicsEffect.Quality#LINEAR LINEAR}</dt> <dd>Defines a
 * linear blur. This is the fastest blurring algorithm but will give poor visual results especially for large blur
 * radii</dd> <dt>{@link org.jdesktop.swingx.effect.BlurGraphicsEffect.Quality#GAUSSIAN GAUSSIAN}</dt> <dd>Defines a
 * Gaussian blur. This will given the best quality rendering but will take the longest to render</dd> <dt>{@link
 * org.jdesktop.swingx.effect.BlurGraphicsEffect.Quality#STACKED STACKED}</dt> <dd>Defines a stack of linear blurs. This
 * will approximate a Gaussian blur but provide better performance. This is the default setting.</dd> </dl>
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 */
public class BlurGraphicsEffect<T> extends AbstractGraphicsEffect<T> {

    /**
     * Defines the quality setting this effect may have.
     */
    public static enum Quality {
        /**
         * Linear blur effect.
         */
        LINEAR,
        /**
         * Stacked linear blur effect. The default value.
         */
        STACKED,
        /**
         * Gaussian blur effect
         */
        GAUSSIAN
    }


    /**
     * Contains a blurrer for each of the quality settings.
     */
    private static final Blurer[] blurers = {
            new LinearBlurer(),
            new StackedBlurer(),
            new GaussianBlurer()
    };

    private float blurRadius;
    private Quality quality;





    /**
     * Create a new blur effect with a blur of 0 and quality of STACKED.
     */
    public BlurGraphicsEffect() {
        this(0, Quality.STACKED);
    }





    /**
     * Creates a new blur effect with the given radius. This will use the default quality of STACKED.
     *
     * @param blurRadius The blur radius to use.
     */
    public BlurGraphicsEffect(float blurRadius) {
        this(blurRadius, Quality.STACKED);
    }





    /**
     * Create a new effect with the given radius and quality properties.
     *
     * @param blurRadius the blur radius to use for the effect.
     * @param quality    The quality of the effect rendering.
     */
    public BlurGraphicsEffect(float blurRadius, Quality quality) {
        if (blurRadius < 0) {
            throw new IllegalArgumentException("blurRadius cannot be < 0: " + blurRadius);
        }
        if (quality == null) {
            throw new NullPointerException("quality cannot be null");
        }
        this.blurRadius = blurRadius;
        this.quality = quality;
    }





    public void paintEffect(Graphics g, EffectSource<? extends T> effectSource) {
        float blurRadius = getBlurRadius();
        int irad = (int) Math.ceil(blurRadius);
        Graphics paintg = g;
        BufferedImage buffer = null;
        Shape clip = null;
        Rectangle clipBounds = null;
        if (blurRadius > 0) {
            clip = g.getClip();
            if (clip == null) {
                // possible if rendering onto a Image Graphics
                clip = effectSource.getSourceBounds(null);
            }

            clipBounds = clip.getBounds();
            buffer = ImageUtilities.createCompatibleTranslucentImage(g,
                    clipBounds.width + (irad << 1),
                    clipBounds.height + (irad << 1));
            paintg = buffer.createGraphics();
            paintg.setClip(0, 0, buffer.getWidth(), buffer.getHeight());
            paintg.setColor(g.getColor());
            paintg.setFont(g.getFont());
            paintg.translate(-(clipBounds.x - irad), -(clipBounds.y - irad));
        }

        effectSource.paintSource(paintg);

        if (buffer != null) {
            assert paintg != g;
            //noinspection ConstantConditions
            assert clip != null;
            paintg.dispose();

            BufferedImage blur = createBlurredImage(buffer, blurRadius);

            // check blur dimension differences
            int blurw = blur.getWidth();
            int blurh = blur.getHeight();

            g.drawImage(blur,
                    clipBounds.x - ((blurw - buffer.getWidth()) / 2) - irad,
                    clipBounds.y - ((blurh - buffer.getHeight()) / 2) - irad,
                    null);

            g.setClip(clip);
            ImageUtilities.releaseImage(blur);
            ImageUtilities.releaseImage(buffer);
        }
    }





    /**
     * Creates the blurred buffered image for the given image and radii.
     *
     * @param image      the image to blur
     * @param blurRadius the radius of the blur effect.
     * @return the blurred image.
     */
    protected BufferedImage createBlurredImage(BufferedImage image, float blurRadius) {
        Quality quality = getQuality();
        Blurer blurer = blurers[quality.ordinal()];
        return blurer.blur(image, blurRadius);
    }





    public boolean transform(Rectangle area, EffectSource<? extends T> effectSource) {
        float blurRadius = getBlurRadius();
        boolean result = blurRadius > 0;
        if (result) {
            int d = (int) Math.ceil(blurRadius);
            area.x -= d;
            area.y -= d;
            area.width += d + d;
            area.height += d + d;
        }
        return result;
    }





    /**
     * Gets the radius of the blur effect.
     *
     * @return The radius of the blur.
     * @see #setBlurRadius(float)
     */
    public float getBlurRadius() {
        return blurRadius;
    }





    /**
     * Set the radius of the blur effect.
     *
     * @param blurRadius The new blur radius.
     * @throws IllegalArgumentException when blurRadius is less than 0.
     * @see #getBlurRadius()
     */
    public void setBlurRadius(float blurRadius) {
        if (blurRadius < 0) {
            throw new IllegalArgumentException("blurRadius cannot be < 0");
        }
        float old = getBlurRadius();
        this.blurRadius = blurRadius;
        firePropertyChange("blurRadius", old, getBlurRadius());
    }





    /**
     * Get the quality of the blur rendering. This will never be null.
     *
     * @return The quality of the rendering.
     * @see #setQuality(org.jdesktop.swingx.effect.BlurGraphicsEffect.Quality)
     */
    public Quality getQuality() {
        return quality;
    }





    /**
     * Set the quality of the blur rendering.
     *
     * @param quality the rendering quality.
     * @throws NullPointerException when quality is null.
     * @see #getQuality()
     */
    public void setQuality(Quality quality) {
        if (quality == null) {
            throw new NullPointerException("quality cannot be null");
        }
        Quality old = getQuality();
        this.quality = quality;
        firePropertyChange("quality", old, getQuality());
    }





    /**
     * Defines a interface for abstracting the bluring process.
     */
    private static interface Blurer {
        public BufferedImage blur(BufferedImage img, float blurRadius);
    }


    /**
     * Defines the rendering process for a quality setting.
     */
    private abstract static class AbstractBlurer implements Blurer {
        public BufferedImage blur(BufferedImage img, float blurRadius) {
            BufferedImageOp op = createOp(blurRadius);
            return op.filter(img, null);
        }





        protected abstract BufferedImageOp createOp(float blurRadius);
    }


    /**
     * defines the rendering process for a linear blur.
     */
    private static class LinearBlurer extends AbstractBlurer {
        @Override
        protected BufferedImageOp createOp(float blurRadius) {
            return new FastBlurFilter((int) Math.ceil(blurRadius));
        }
    }


    /**
     * Defines the rendering process for a stacked blur.
     */
    private static class StackedBlurer extends AbstractBlurer {
        @Override
        protected BufferedImageOp createOp(float blurRadius) {
            return new StackBlurFilter(1, (int) Math.ceil(blurRadius));
        }
    }


    /**
     * defines the rendering process for a gaussian blur.
     */
    private static class GaussianBlurer extends AbstractBlurer {
        @Override
        protected BufferedImageOp createOp(float blurRadius) {
            return new GaussianBlurFilter((int) Math.ceil(blurRadius));
        }
    }
}
