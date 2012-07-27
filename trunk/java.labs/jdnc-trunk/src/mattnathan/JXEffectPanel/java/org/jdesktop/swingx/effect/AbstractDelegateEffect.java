/*
 * $Id: AbstractDelegateEffect.java 2628 2008-08-05 16:05:09Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.effect;

import java.awt.Graphics;
import java.awt.Rectangle;

/**
 * Defines an abstract super type for GraphicsEffect instances that are comprised of a collection of other effects. For
 * example the ReflectionEffect would extend this class as would a ShadowEffect.
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 */
public abstract class AbstractDelegateEffect<T> extends AbstractGraphicsEffect<T> {

    /**
     * The cached instance of the effect.
     */
    private GraphicsEffect<T> cachedEffect = null;





    /**
     * Create a new effect.
     */
    public AbstractDelegateEffect() {
    }





    /**
     * Override this method to create the effect that represents this parent effect.
     *
     * @return The effect this delegating effect used to paint.
     */
    protected abstract GraphicsEffect<T> createCachedEffect();





    /**
     * returns true if this effect is valid.
     *
     * @return True if the cached data is valid.
     */
    protected boolean isValid() {
        return cachedEffect != null;
    }





    /**
     * Invalidates the cached data in this effect. Subclasses should call this method when the effect created through
     * createcachedEffect becomes out of sync with this effects properties.
     */
    protected void invalidate() {
        cachedEffect = null;
    }





    /**
     * Makes this effects cached data valid.
     */
    protected void validate() {
        if (!isValid()) {
            cachedEffect = createCachedEffect();
        }
    }





    /**
     * Get the effect used to to the work in this effect.
     *
     * @return The effect used to do the work.
     */
    protected GraphicsEffect<T> getCachedEffect() {
        validate();
        return cachedEffect;
    }





    /**
     * Paint the effect onto the given graphics.
     *
     * @param g      The graphics to paint the effect to.
     * @param source The source of the effect.
     */
    public void paintEffect(Graphics g, EffectSource<? extends T> source) {
        getCachedEffect().paintEffect(g, source);
    }





    /**
     * Transform the give {@code area} into the coordinate space of the effect.
     *
     * @param area   The area to transform.
     * @param source The source of the effect.
     * @return {@code true} if the effect is active.
     */
    public boolean transform(Rectangle area, EffectSource<? extends T> source) {
        return getCachedEffect().transform(area, source);
    }

}
