/*
 * $Id: RotationTransformEffect.java 2628 2008-08-05 16:05:09Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.effect;

import java.awt.geom.AffineTransform;

/**
 * Applies a rotation to the source. All rotations in this effect are in radians.
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 */
public class RotationTransformEffect<T> extends AbstractTransformEffect<T> {

    private double rotation;





    /**
     * Creates a new effect with a rotation of 0.
     */
    public RotationTransformEffect() {
        this(0d);
    }





    /**
     * Create a new effect with the given rotation.
     *
     * @param rotation the rotation
     */
    public RotationTransformEffect(double rotation) {
        this.rotation = rotation;
    }





    @Override
    protected AffineTransform createTransform(EffectSource<? extends T> effectSource, int width, int height) {
        AffineTransform result = null;
        double rotation = getRotation();
        if (rotation != 0) {
            result = AffineTransform.getRotateInstance(rotation, width / 2d, height / 2d);
        }
        return result;
    }





    /**
     * Gets the rotation applied as part of this effect.
     *
     * @return the rotation in radians
     * @see #setRotation(double)
     */
    public double getRotation() {
        return rotation;
    }





    /**
     * Sets the rotation for this effect. The default value is 0 radians.
     *
     * @param rotation The new rotation value.
     * @see #getRotation()
     */
    public void setRotation(double rotation) {
        double old = getRotation();
        this.rotation = rotation;
        firePropertyChange("rotation", old, getRotation());
    }
}
