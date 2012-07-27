/*
 * $Id: StaticTranslationEffect.java 2628 2008-08-05 16:05:09Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.effect;

import java.awt.geom.AffineTransform;

/**
 * Statically translates the source by translateX, translateY.
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 */
public class StaticTranslationEffect<T> extends AbstractTranslationEffect<T> {
    private double translateX;
    private double translateY;





    /**
     * Creates a new translation with 0 x and y values.
     */
    public StaticTranslationEffect() {
        this(0, 0);
    }





    /**
     * Creates a new translation with the given static x and y values.
     *
     * @param translateX The distance to translate along the x axis.
     * @param translateY The distance to translate along the y axis.
     */
    public StaticTranslationEffect(double translateX, double translateY) {
        this.translateX = translateX;
        this.translateY = translateY;
    }





    @Override
    protected AffineTransform createTransform(EffectSource<? extends T> effectSource, int width, int height) {
        AffineTransform result = null;
        double translateX = getTranslateX();
        double translateY = getTranslateY();
        if (translateX != 0 || translateY != 0) {
            result = AffineTransform.getTranslateInstance(translateX, translateY);
        }
        return result;
    }





    /**
     * Get the translation in the x axis.
     *
     * @return The distance translated in the x axis.
     * @see #setTranslateX(double)
     */
    public double getTranslateX() {
        return translateX;
    }





    /**
     * Set the translation in the x axis.
     *
     * @param translateX The translation in the x axis.
     * @see #getTranslateX()
     */
    public void setTranslateX(double translateX) {
        double old = getTranslateX();
        this.translateX = translateX;
        firePropertyChange("translateX", old, getTranslateX());
    }





    /**
     * Get the translation along the y axis.
     *
     * @return The y-axis translation.
     * @see #setTranslateY(double)
     */
    public double getTranslateY() {
        return translateY;
    }





    /**
     * Set the translation along the y axis.
     *
     * @param translateY The y-axis translation.
     * @see #getTranslateY()
     */
    public void setTranslateY(double translateY) {
        double old = getTranslateY();
        this.translateY = translateY;
        firePropertyChange("translateY", old, getTranslateY());
    }
}
