/*
 * $Id: RelativeTranslationEffect.java 2628 2008-08-05 16:05:09Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.effect;

import java.awt.geom.AffineTransform;

/**
 * Provides an effect to translate the source in 2D space. The transform is represented as relative values with 0 being
 * no translation. A value of 1 will mean that the source is translated exactly one dimension. That is to say that if it
 * were 100 pixels width then it would be translated by 100 pixels. A value of 2.0 would translate by 200 pixels and so
 * on.
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 */
public class RelativeTranslationEffect<T> extends AbstractTranslationEffect<T> {

    private double translateX;
    private double translateY;





    /**
     * Create a new translation with default values of 0, 0.
     */
    public RelativeTranslationEffect() {
        this(0, 0);
    }





    /**
     * Create a new translation effect with the given translation values.
     *
     * @param translateX The x-axis translation.
     * @param translateY The y-axis translation.
     */
    public RelativeTranslationEffect(double translateX, double translateY) {
        this.translateX = translateX;
        this.translateY = translateY;
    }





    /**
     * Creates a transform to translate the source by the current amounts.
     *
     * @param effectSource The source.
     * @param width        The width.
     * @param height       The height.
     * @return The transform.
     */
    @Override
    protected AffineTransform createTransform(EffectSource<? extends T> effectSource, int width, int height) {
        double x = getTranslateX();
        double y = getTranslateY();
        AffineTransform trans = null;
        if (x != 0 || y != 0) {
            trans = AffineTransform.getTranslateInstance(width * x, height * y);
        }
        return trans;
    }





    /**
     * Get the translation value along the x axis.
     *
     * @return The x axis translation.
     * @see #setTranslateX(double)
     * @see #getTranslateY()
     */
    public double getTranslateX() {
        return translateX;
    }





    /**
     * Set the translation along the x axis. The default value is 0.
     *
     * @param translateX The new translation.
     * @see #getTranslateX()
     * @see #setTranslateY(double)
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
     * @see #getTranslateX()
     */
    public double getTranslateY() {
        return translateY;
    }





    /**
     * Set the translation along the y axis. The default value is 0.
     *
     * @param translateY the new y-axis translation.
     * @see #getTranslateY()
     * @see #setTranslateX(double)
     */
    public void setTranslateY(double translateY) {
        double old = getTranslateY();
        this.translateY = translateY;
        firePropertyChange("translateY", old, getTranslateY());
    }





    /**
     * Convenience method for setting both axes translations.
     *
     * @param translateX The new x-axis translation.
     * @param translateY The new y-axis translation.
     * @see #setTranslateX(double)
     * @see #setTranslateY(double)
     */
    public void setTranslation(double translateX, double translateY) {
        setTranslateX(translateX);
        setTranslateY(translateY);
    }
}
