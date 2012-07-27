/*
 * $Id: ScaleTransformEffect.java 2628 2008-08-05 16:05:09Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.effect;

import java.awt.geom.AffineTransform;

/**
 * Provides a scaling transform effect. The transform acts as an AffineTransform would
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 */
public class ScaleTransformEffect<T extends Object> extends AbstractTransformEffect<T> {

    /**
     * An effect that will flip the source vertically so that the top becomes the bottom.
     */
    public static final GraphicsEffect<Object> FLIP_VERTICAL = new ScaleTransformEffect<Object>(1, -1);
    /**
     * An effect that will flip the source horizontally so that the left becomes the right.
     */
    public static final GraphicsEffect<Object> FLIP_HORIZONTAL = new ScaleTransformEffect<Object>(-1, 1);

    private double scaleY;
    private double scaleX;





    /**
     * Creates a new default ScaleTransformEffect with the values 1.0, 1.0. This delegates to {@link
     * #ScaleTransformEffect(double,double)}.
     *
     * @see #ScaleTransformEffect(double,double)
     */
    public ScaleTransformEffect() {
        this(1, 1);
    }





    /**
     * Create a new ScaleTransformEffect using the given axis values.
     *
     * @param scaleX The x-axis scale.
     * @param scaleY The y-axis scale.
     */
    public ScaleTransformEffect(double scaleX, double scaleY) {
        this.scaleY = scaleY;
        this.scaleX = scaleX;
    }





    @Override
    protected AffineTransform createTransform(EffectSource<? extends T> source, int width, int height) {
        double x = getScaleX();
        double y = getScaleY();
        AffineTransform result = null;
        if (x != 1 || y != 1) {
            result = new AffineTransform();
            result.translate(width / 2d, height / 2d);
            result.scale(x, y);
            result.translate(-width / 2d, -height / 2d);
        }
        return result;
    }





    /**
     * Gets the scale factor in the x direction. The default is 1.0.
     *
     * @return the x axis scale factor.
     * @see #setScaleX(double)
     * @see #setScale(double,double)
     */
    public double getScaleX() {
        return scaleX;
    }





    /**
     * Set the scale factor for the x direction. The default value is 1.0.
     *
     * @param scaleX the scale for the x axis.
     * @see #getScaleX()
     * @see #setScale(double,double)
     */
    public void setScaleX(double scaleX) {
        double old = getScaleX();
        this.scaleX = scaleX;
        firePropertyChange("scaleX", old, getScaleX());
    }





    /**
     * Get the scale factor for the y axis. The default value is 1.0.
     *
     * @return The y axis scale factor.
     * @see #setScaleY(double)
     * @see #setScale(double,double)
     */
    public double getScaleY() {
        return scaleY;
    }





    /**
     * Set the y-axis scale factor. The default value is 1.0.
     *
     * @param scaleY the y-axis scale factor.
     * @see #getScaleY()
     * @see #setScale(double,double)
     */
    public void setScaleY(double scaleY) {
        double old = getScaleY();
        this.scaleY = scaleY;
        firePropertyChange("scaleY", old, getScaleY());
    }





    /**
     * Set the scale for this effect.
     *
     * @param scale The scale factor
     */
    public void setScale(double scale) {
        setScale(scale, scale);
    }





    /**
     * Set the scale for this transform. This is a convenience method for setting both scales in one.
     *
     * @param scaleX The x-axis scale.
     * @param scaleY The y-axis scale.
     * @see #setScaleX(double)
     * @see #setScaleY(double)
     */
    public void setScale(double scaleX, double scaleY) {
        setScaleX(scaleX);
        setScaleY(scaleY);
    }
}
