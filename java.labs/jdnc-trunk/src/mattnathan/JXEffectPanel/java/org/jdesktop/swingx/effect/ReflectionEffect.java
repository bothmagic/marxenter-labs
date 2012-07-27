/*
 * $Id: ReflectionEffect.java 2628 2008-08-05 16:05:09Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.effect;

import org.jdesktop.swingx.painter.MattePainterTmp;

import java.awt.Color;
import java.awt.GradientPaint;

/**
 * Adds a reflection to the effect source. The reflection can have the length and opacity of the reflection altered, the
 * gap between the reflection and the component and the blur radius of the reflection can also be modified. This effect
 * can be represented as a combination of other effects, namely:
 * <p/>
 * <pre><code>
 * new ParallelGraphicsEffect<T>(
 *     new SerialGraphicsEffect<T>(
 *           ScaleTransformEffect.FLIP_VERTICAL,
 *           new MaskGraphicsEffect<T>(
 *                 new MattePainterTmp<T>(
 *                       new GradientPaint(
 *                             0, 0, new Color(0f, 0f, 0f, opacity),
 *                             0, length, new Color(0, 0, 0, 0)))),
 *           new RelativeTranslationEffect<T>(0, 1),
 *           new StaticTranslationEffect<T>(0, gap),
 *           new BlurGraphicsEffect<T>(blurRadius)),
 *     IdentityGraphicsEffect.INSTANCE);
 * </code></pre>
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 */
public class ReflectionEffect<T> extends AbstractDelegateEffect<T> {

    private int gap = 6;
    private float length = 0.4f;
    private float opacity = 0.8f;
    private float blurRadius = 0;





    /**
     * Create a default reflection effect with length of 0.4, opacity of 0.8 and blur radius of 0.
     */
    public ReflectionEffect() {
    }





    /**
     * Create a new reflection effect with the given gap between the source and reflection.
     *
     * @param gap The reflection gap.
     */
    public ReflectionEffect(int gap) {
        this.gap = gap;
    }





    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    protected GraphicsEffect<T> createCachedEffect() {
        return new ParallelGraphicsEffect<T>(
                new SerialGraphicsEffect<T>(
                        ScaleTransformEffect.FLIP_VERTICAL,
                        new MaskGraphicsEffect<T>(
                                new MattePainterTmp<T>(
                                        new GradientPaint(
                                                0, 0, getLength() == 0 ? new Color(0, 0, 0, 0) : new Color(0f, 0f, 0f, getOpacity()),
                                                0, getLength(), new Color(0, 0, 0, 0)))),
                        new RelativeTranslationEffect<T>(0, 1),
                        new StaticTranslationEffect<T>(0, getGap()),
                        new BlurGraphicsEffect<T>(getBlurRadius(), BlurGraphicsEffect.Quality.STACKED)),
                IdentityGraphicsEffect.INSTANCE);
    }





    /**
     * Gets the blur radius for this effect.
     *
     * @return The blur radius.
     */
    public float getBlurRadius() {
        return blurRadius;
    }





    /**
     * Gets the gap between the source and reflection.
     *
     * @return The reflection gap.
     */
    public int getGap() {
        return gap;
    }





    /**
     * Gets length of the reflection as a proportion of the source image.
     *
     * @return The reflection length.
     */
    public float getLength() {
        return length;
    }





    /**
     * Get the start opacity of the reflection.
     *
     * @return The start opacity of the reflection.
     */
    public float getOpacity() {
        return opacity;
    }





    /**
     * Set the radius for the blur for the reflection. If set to 0 no blur will be done.
     *
     * @param blurRadius The blur for the reflection.
     */
    public void setBlurRadius(float blurRadius) {
        float old = getBlurRadius();
        this.blurRadius = blurRadius;
        invalidate();
        firePropertyChange("blurRadius", old, getBlurRadius());
    }





    /**
     * Set the gap between the reflection and the source.
     *
     * @param gap The reflection gap.
     */
    public void setGap(int gap) {
        int old = getGap();
        this.gap = gap;
        invalidate();
        firePropertyChange("gap", old, getGap());
    }





    /**
     * Set the length of the reflection.
     *
     * @param length The reflection length.
     */
    public void setLength(float length) {
        float old = getLength();
        this.length = length;
        invalidate();
        firePropertyChange("length", old, getLength());
    }





    /**
     * Set the opacity for the start of the reflection.
     *
     * @param opacity The reflection opacity.
     */
    public void setOpacity(float opacity) {
        float old = getOpacity();
        this.opacity = opacity;
        invalidate();
        firePropertyChange("opacity", old, getOpacity());
    }
}
