/*
 * $Id: CompositeGraphicsEffect.java 2628 2008-08-05 16:05:09Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.effect;

import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

/**
 * Applies a generic Composite to the source rendering.
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 */
public class CompositeGraphicsEffect<T> extends AbstractGraphicsEffect<T> {
    private Composite composite;
    private boolean buffered;





    /**
     * Create a new effect with no Composite installed.
     */
    public CompositeGraphicsEffect() {
        this(null);
    }





    /**
     * Create a new effect from the specified Composite instance.
     *
     * @param composite The composite to apply to the source.
     */
    public CompositeGraphicsEffect(Composite composite) {
        this.composite = composite;
    }





    /**
     * Create a new effect using the given composite and buffering strategy.
     *
     * @param composite The composite to apply to the source.
     * @param buffered  Whether the source should be buffered before the effect is applied.
     */
    public CompositeGraphicsEffect(Composite composite, boolean buffered) {
        this.composite = composite;
        this.buffered = buffered;
    }





    /**
     * Applies the set Composite to the EffectSource using optional buffering.
     *
     * @param g            The graphics to paint to.
     * @param effectSource The source to transform.
     */
    public void paintEffect(Graphics g, EffectSource<? extends T> effectSource) {
        Composite composite = getComposite();
        if (composite == null) {
            effectSource.paintSource(g);
            return;
        }

        // prepare graphics
        Graphics2D g2 = (Graphics2D) g;
        Composite old = g2.getComposite();
        g2.setComposite(composite);

        EffectUtilities.paintEffectSource(effectSource, g2, isBuffered());

        g2.setComposite(old);
    }





    /**
     * No transform is applied but true is returned when the composite is not null.
     * <p/>
     * {@inheritDoc}
     */
    public boolean transform(Rectangle area, EffectSource<? extends T> effectSource) {
        return composite != null;
    }





    /**
     * Gets the composite to apply to the source when painting.
     *
     * @return The composite to apply. This can be null which means no composite will be applied.
     * @see #setComposite(java.awt.Composite)
     */
    public Composite getComposite() {
        return composite;
    }





    /**
     * Set the composite to apply to the source. Setting this to null effectively reduces this effect to an Identity
     * effect.
     *
     * @param composite The composite to apply.
     * @see #getComposite()
     */
    public void setComposite(Composite composite) {
        Composite old = getComposite();
        this.composite = composite;
        firePropertyChange("composite", old, getComposite());
    }





    /**
     * Get whether the effect source will be buffered in an image before the effect is applied. The buffer will only be
     * temporary and should act as a way to remove any side-effects that may occur from changing the state of the
     * Graphics object applied to the EffectSource. For example the composite of the Graphics could also be changed by
     * the EffectSource.
     *
     * @return {@code true} if the source is buffered before painting.
     * @see #setBuffered(boolean)
     */
    public boolean isBuffered() {
        return buffered;
    }





    /**
     * Set whether the source should be buffered before the effect is applied.
     *
     * @param buffered {@code true} if the source should be buffered.
     * @see #isBuffered()
     */
    public void setBuffered(boolean buffered) {
        this.buffered = buffered;
    }


}
