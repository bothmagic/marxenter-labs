/*
 * $Id: IdentityGraphicsEffect.java 2628 2008-08-05 16:05:09Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.effect;

import java.awt.Graphics;
import java.awt.Rectangle;

/**
 * Simple effect that paints the source unaltered. This can be used to duplicate the source when combined with other
 * effects.
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 */
public class IdentityGraphicsEffect<T> extends AbstractGraphicsEffect<T> {
    /**
     * Singleton instance of if this effect.
     */
    public static final GraphicsEffect<Object> INSTANCE = new IdentityGraphicsEffect<Object>();





    /**
     * Simply calls source.paintSource(g).
     *
     * @param g            The graphics to paint to
     * @param effectSource the source of the effect.
     */
    public void paintEffect(Graphics g, EffectSource<? extends T> effectSource) {
        effectSource.paintSource(g);
    }





    /**
     * Always returns false.
     *
     * @param area         The area to transform
     * @param effectSource The source of the effect.
     * @return {@code false};
     */
    public boolean transform(Rectangle area, EffectSource<? extends T> effectSource) {
        return false;
    }
}
