/*
 * $Id: RadialTranslationEffect.java 2628 2008-08-05 16:05:09Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.effect;

import java.awt.geom.AffineTransform;

/**
 * Provides a translation effect based on radial coordinates. This class uses a length and angle to determine the
 * transform to apply. The angle given is measured in radians anti-clockwise from the positive x axis.
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 */
public class RadialTranslationEffect<T> extends AbstractTranslationEffect<T> {
    private double angle;
    private double length;





    /**
     * Creates a new translation with 0 angle and 0 length.
     */
    public RadialTranslationEffect() {
        this(0, 0);
    }





    /**
     * Create a new translation with the given angle and length.
     *
     * @param angle  The angle for the translation.
     * @param length The length of the translation.
     */
    public RadialTranslationEffect(double angle, double length) {
        this.angle = angle;
        this.length = length;
    }





    @Override
    protected AffineTransform createTransform(EffectSource<? extends T> effectSource, int width, int height) {
        AffineTransform result = null;
        double length = getLength();
        if (length != 0) {
            double angle = getAngle();
            result = AffineTransform.getTranslateInstance(Math.cos(angle) * length, Math.sin(angle) * length);
        }
        return result;
    }





    /**
     * Gets the angle used for the translation of the source. This is in radians.
     *
     * @return The angle used to translate the source.
     * @see #setAngle(double)
     */
    public double getAngle() {
        return angle;
    }





    /**
     * Sets the angle used to translate the source. The angle should be in radians and is measure anti-clockwise from
     * the positive x axis.
     *
     * @param angle The angle for this coordinate space.
     * @see #getAngle()
     */
    public void setAngle(double angle) {
        double old = getAngle();
        this.angle = angle;
        firePropertyChange("angle", old, getAngle());
    }





    /**
     * Gets the length or distance the source will be translated away from the origin. This can be negative.
     *
     * @return The length of the translation.
     * @see #setLength(double)
     */
    public double getLength() {
        return length;
    }





    /**
     * Set the length or size of the translation away from the origin. This can be negative.
     *
     * @param length The length of the translation.
     * @see #getLength()
     */
    public void setLength(double length) {
        double old = getLength();
        this.length = length;
        firePropertyChange("length", old, getLength());
    }
}
