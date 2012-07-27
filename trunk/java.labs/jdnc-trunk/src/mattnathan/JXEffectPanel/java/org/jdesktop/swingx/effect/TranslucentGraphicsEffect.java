/*
 * $Id: TranslucentGraphicsEffect.java 2628 2008-08-05 16:05:09Z mattnathan $
 *
 * Copyright 2005 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */

package org.jdesktop.swingx.effect;

import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.Graphics;

/**
 * Provides a simple translucency effect for the given EffectSource. This provides two paths for rendering the effect:
 * buffered and unbuffered. The buffered approach will be safe for all sources, even if they alter the AlphaComposite
 * unintelligently, but incurs the performance hit of requiring a back buffer to render correctly. The unbuffered
 * approach requires no back buffer and as such gives much better performance but may show visual artifacts when used
 * with certain sources, namely when used with most Components under the Windows Vista look and feel.
 *
 * @author Matt Nathan
 * @version 1.0
 */
public class TranslucentGraphicsEffect<T> extends CompositeGraphicsEffect<T> {
    /**
     * Alpha component to use for this effect.
     */
    private float alpha;





    /**
     * Create a new default translucent effect. This creates a buffered effect with alpha of 1.
     */
    public TranslucentGraphicsEffect() {
        this(1, true);
    }





    /**
     * Create a new buffered effect with the given alpha.
     *
     * @param alpha The alpha value of the effect.
     */
    public TranslucentGraphicsEffect(float alpha) {
        this(alpha, true);
    }





    /**
     * Create a new effect with the given properties.
     *
     * @param alpha    The alpha of the effect.
     * @param buffered {@code true} if the effect should be buffered.
     */
    public TranslucentGraphicsEffect(float alpha, boolean buffered) {
        super(alpha == 1 ? null : AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha), buffered);
        this.alpha = alpha;
    }





    /**
     * Gets the alpha value for this GraphicsEffect.
     *
     * @return The alpha value.
     */
    public float getAlpha() {
        return alpha;
    }





    /**
     * {@inheritDoc}
     */
    @Override
    public void paintEffect(Graphics g, EffectSource<? extends T> effectSource) {
        if (getAlpha() > 0) {
            super.paintEffect(g, effectSource);
        }
    }





    /**
     * Set the alpha property for this GraphicsEffect.
     *
     * @param alpha The new alpha value.
     */
    public void setAlpha(float alpha) {
        float old = getAlpha();
        this.alpha = alpha;
        alpha = getAlpha();

        if (alpha != old) {
            firePropertyChange("alpha", old, alpha);
            if (alpha == 1) {
                super.setComposite(null);
            } else {
                super.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
            }
        }
    }





    /**
     * Extends the default to require that only AlphaComposite classes can be added as a composite to this effect. Only
     * the alpha component of the given Composite will be taken, the type of the composition will be ignored, this
     * effect only supports AlphaComposite.SRC_OVER.
     * <p/>
     * {@inheritDoc}
     */
    @Override
    public void setComposite(Composite composite) {
        if (composite == null || composite instanceof AlphaComposite) {
            if (composite == null) {
                setAlpha(1);
            } else {
                setAlpha(((AlphaComposite) composite).getAlpha());
            }
        } else {
            throw new IllegalArgumentException("Only AlphaComposite is supported by this class: " + composite.getClass());
        }
    }
}
