/*
 * $Id: GraphicsEffect.java 2628 2008-08-05 16:05:09Z mattnathan $
 *
 * Copyright 2005 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */

package org.jdesktop.swingx.effect;

import org.jdesktop.beans.PropertySupporter;

import java.awt.Graphics;
import java.awt.Rectangle;

/**
 * Defines an effect on a component.
 *
 * @author Matt
 * @param <T> The type of source this effect supports.
 */
public interface GraphicsEffect<T> extends PropertySupporter {
    /**
     * Paint the effect onto the given graphics. The painting the effect is applied to is created via a call to {@link
     * EffectSource#paintSource(java.awt.Graphics)}.
     *
     * @param g      The graphics to paint the effect to.
     * @param source The source of the effect.
     */
    public void paintEffect(Graphics g, EffectSource<? extends T> source);





    /**
     * Transform the give {@code area} into the coordinate space of the effect. Return {@code true} if the effect is
     * active.
     *
     * @param area   The area to transform.
     * @param source The source of the effect.
     * @return {@code true} if the effect is active.
     */
    public boolean transform(Rectangle area, EffectSource<? extends T> source);

}
