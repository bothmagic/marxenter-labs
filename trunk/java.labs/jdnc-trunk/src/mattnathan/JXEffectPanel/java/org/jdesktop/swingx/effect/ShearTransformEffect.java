/*
 * $Id: ShearTransformEffect.java 2628 2008-08-05 16:05:09Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.effect;

import java.awt.geom.AffineTransform;

/**
 * Component effect for producing a sheared render.
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 */
public class ShearTransformEffect<T> extends AbstractTransformEffect<T> {
    private double shearX = 0;
    private double shearY = 0;





    /**
     * Creates a new effect with initial shear values of 0, 0.
     */
    public ShearTransformEffect() {
        this(0, 0);
    }





    /**
     * Creates a new effect with the given shear values.
     *
     * @param shearX the shear x
     * @param shearY the shear y
     */
    public ShearTransformEffect(double shearX, double shearY) {
        this.shearX = shearX;
        this.shearY = shearY;
    }





    @Override
    protected AffineTransform createTransform(EffectSource<? extends T> effectSource, int width, int height) {
        double x = getShearX();
        double y = getShearY();
        AffineTransform result = null;
        if (x != 0 || y != 1) {
            result = AffineTransform.getShearInstance(x, y);
        }
        return result;
    }





    /**
     * Gets the x-shear value.
     *
     * @return the x-shear value.
     * @see #setShearX(double)
     */
    public double getShearX() {
        return shearX;
    }





    /**
     * Set the x-shear value;
     *
     * @param shearX The value of the x-shear.
     * @see #getShearX()
     */
    public void setShearX(double shearX) {
        double old = getShearX();
        this.shearX = shearX;
        firePropertyChange("shearX", old, getShearX());
    }





    /**
     * Get the y-shear value.
     *
     * @return the y-shear value.
     */
    public double getShearY() {
        return shearY;
    }





    /**
     * Set the value of the y-shear.
     *
     * @param shearY The y-shear value.
     * @see #getShearY()
     */
    public void setShearY(double shearY) {
        double old = getShearY();
        this.shearY = shearY;
        firePropertyChange("shearY", old, getShearY());
    }





    /**
     * Sets both shear values at the same time.
     *
     * @param shearX The x-shear value.
     * @param shearY The y-shear value.
     * @see #setShearX(double)
     * @see #setShearY(double)
     */
    public void setShear(double shearX, double shearY) {
        setShearX(shearX);
        setShearY(shearY);
    }
}
