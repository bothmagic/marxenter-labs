/*
 * $Id: EffectSource.java 2628 2008-08-05 16:05:09Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.effect;

import java.awt.Graphics;
import java.awt.Rectangle;

/**
 * <p/>
 * Provides the painting code which the effect is to be applied to. </p>
 * <p/>
 * <p/>
 * <b>Example:</b> </p>
 * <p/>
 * To apply an effect to a JComponent you would override the component with the following code:
 * <p/>
 * <pre><code>
 * class EffectComponent extends JComponent {
 *    private final SourcePainter&lt;EffectComponent> sourcePainter =
 *       new SourcePainter&lt;EffectComponent>() {
 *          public void paintSource(Graphics g) {
 *             EffectComponent.super.paint(g);
 *          }
 * <p/>
 *          public Rectangle getSourceBounds() {
 *             return EffectComponent.this.getBounds();
 *          }
 * <p/>
 *          public EffectComponent getSource() {
 *             return EffectComponent.this;
 *          }
 *       };
 * <p/>
 *    public void paint(Graphics g) {
 *       GraphicsEffect$lt;? super EffectComponent> effect = ...;
 *       effect.paintEffect(g, effectSource);
 *    }
 * }
 * </code></pre>
 * </p>
 */
public interface EffectSource<C> {
    /**
     * Paint the source graphics. The given graphics is never null.
     *
     * @param g The destination Graphics to paint to.
     */
    public void paintSource(Graphics g);





    /**
     * Get the bounds of the total area to paint. The result should be placed in {@code dest} if not null then returned.
     * The returned rectangle cannot be null.
     *
     * @param dest Place the result in here if not null.
     * @return the total area to paint.
     */
    public Rectangle getSourceBounds(Rectangle dest);





    /**
     * Get the object which is invoking the effect. The returned object cannot be null.
     *
     * @return The source object.
     */
    public C getSource();
}
