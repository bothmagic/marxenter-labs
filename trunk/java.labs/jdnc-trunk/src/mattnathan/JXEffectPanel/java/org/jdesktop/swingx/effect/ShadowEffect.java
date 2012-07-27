/*
 * $Id: ShadowEffect.java 2628 2008-08-05 16:05:09Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.effect;

/**
 * Defines an effect which applies a shadow to the source. This is an ease-of-use class that delegates the effect
 * processing and is equivalent to the following effect
 * <p/>
 * <pre><code>
 * new ParallelGraphicsEffect<T>(
 *     new SerialGraphicsEffect<T>(
 *           new RadialTranslationEffect<T>(angle, distance),
 *           new BlurGraphicsEffect<T>(blur),
 *           ColorFilterEffect.ALPHA,
 *           new TranslucentGraphicsEffect<T>(opacity)),
 *     IdentityGraphicsEffect.INSTANCE)
 * </code></pre>
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 * @todo Add color support to shadows.
 */
public class ShadowEffect<T> extends AbstractDelegateEffect<T> {

    private float angle;
    private int distance;
    private float blur;
    private float opacity;





    /**
     * Create a new default ShadowEffect with the shadow 5 pixels from the source at an angle of 45 degrees.
     */
    public ShadowEffect() {
        this((float) Math.PI / 4f, 5, 5, 0.5f);
    }





    /**
     * Create a new ShadowEffect with the given values.
     *
     * @param angle    The angle from positive x axis.
     * @param distance The distance from the source.
     * @param blur     The blur radius.
     * @param opacity  The opacity of the shadow.
     */
    public ShadowEffect(float angle, int distance, float blur, float opacity) {
        if (blur < 0) {
            blur = 0;
        }
        if (opacity < 0) {
            opacity = 0;
        }
        if (opacity > 1) {
            opacity = 1;
        }
        this.angle = angle;
        this.blur = blur;
        this.distance = distance;
        this.opacity = opacity;
    }





    /**
     * Override this method to create the effect that represents this parent effect.
     *
     * @return The effect this delegating effect used to paint.
     */
    @SuppressWarnings("unchecked")
    @Override
    protected GraphicsEffect<T> createCachedEffect() {
        return new ParallelGraphicsEffect<T>(
                new SerialGraphicsEffect<T>(
                        new RadialTranslationEffect<T>(getAngle(), getDistance()),
                        new BlurGraphicsEffect<T>(getBlur()),
                        ColorFilterEffect.ALPHA,
                        new TranslucentGraphicsEffect<T>(getOpacity())),
                IdentityGraphicsEffect.INSTANCE);
    }





    /**
     * Get the angle from horizontal in radians that the shadow will be placed.
     *
     * @return The angle to place the shadow.
     */
    public float getAngle() {
        return angle;
    }





    /**
     * Get the blur radius for the shadow.
     *
     * @return The blur radius.
     */
    public float getBlur() {
        return blur;
    }





    /**
     * Get the distance the shadow is away from the source.
     *
     * @return The distance for the shadow.
     */
    public int getDistance() {
        return distance;
    }





    /**
     * Get the opacity of the shadow.
     *
     * @return The shadow opacity.
     */
    public float getOpacity() {
        return opacity;
    }





    /**
     * Set the angle for the shadow. This is in radians and is measured clockwise from the positive x axis.
     *
     * @param angle The angle for the shadow.
     */
    public void setAngle(float angle) {
        float old = getAngle();
        this.angle = angle;
        if (old != getAngle()) {
            invalidate();
            firePropertyChange("angle", old, getAngle());
        }
    }





    /**
     * Set the blur radius. If this value is < 0 then no blur will be performed.
     *
     * @param blur The blur radius.
     */
    public void setBlur(float blur) {
        if (blur < 0) {
            blur = 0;
        }
        float old = getBlur();
        this.blur = blur;
        if (old != getBlur()) {
            invalidate();
            firePropertyChange("blur", old, getBlur());
        }
    }





    /**
     * Set the distance the blur is away from the source. negative distances are supported.
     *
     * @param distance The distance away from the source the shadow is.
     */
    public void setDistance(int distance) {
        float old = getDistance();
        this.distance = distance;
        if (old != getDistance()) {
            invalidate();
            firePropertyChange("distance", old, getDistance());
        }
    }





    /**
     * Set the opacity of the shadow. If this is outside the range [0,1] then it will be set to the closest value within
     * the range.
     *
     * @param opacity The opacity of the shadow.
     */
    public void setOpacity(float opacity) {
        if (opacity < 0) {
            opacity = 0;
        }
        if (opacity > 1) {
            opacity = 1;
        }
        float old = getOpacity();
        this.opacity = opacity;
        if (old != getOpacity()) {
            invalidate();
            firePropertyChange("opacity", old, getOpacity());
        }
    }
}
