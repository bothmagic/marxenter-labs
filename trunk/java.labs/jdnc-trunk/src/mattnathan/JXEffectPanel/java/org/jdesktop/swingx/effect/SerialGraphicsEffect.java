/*
 * $Id: SerialGraphicsEffect.java 2628 2008-08-05 16:05:09Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.effect;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Collection;

/**
 * An effect comprising of a series of child effects applied to each other. this can be used if you want to apply more
 * than one effect to a source, for example if you want to invert the blur a source you would use the effect: <code>new
 * SerialGraphicsEffect<Object>(ScaleTransformEffect.FLIP_VERTICAL, new BlurGraphicsEffect<Object>(2));</code>
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 */
public class SerialGraphicsEffect<T> extends AbstractContainerEffect<T> {
    /**
     * Creates a new effect with no children.
     *
     * @see #setChildren(GraphicsEffect[])
     */
    public SerialGraphicsEffect() {
    }





    /**
     * Creates a new effect with the given children.
     *
     * @param children The children of this effect.
     * @see #setChildren(GraphicsEffect[])
     */
    public SerialGraphicsEffect(GraphicsEffect<? super T>... children) {
        super(children);
    }





    /**
     * Creates a new effect with the given children.
     *
     * @param children The children for this effect
     * @see #setChildren(GraphicsEffect[])
     */
    public SerialGraphicsEffect(Collection<GraphicsEffect<? super T>> children) {
        super(children);
    }





    public void paintEffect(Graphics g, EffectSource<? extends T> effectSource) {
        EffectSource<? extends T> tail = effectSource;
        for (GraphicsEffect<? super T> effect : this) {
            tail = new HierarchyEffectSource<T>(effect, tail);
        }
        if (tail != null) {
            tail.paintSource(g);
        }
    }





    public boolean transform(Rectangle area, EffectSource<? extends T> effectSource) {
        boolean result = false;
        for (GraphicsEffect<? super T> effect : this) {
            result = effect.transform(area, effectSource) || result;
        }
        return result;
    }





    /**
     * Utility class used to stack each effect and source on top of each other.
     */
    private static class HierarchyEffectSource<T> implements EffectSource<T> {

        private GraphicsEffect<? super T> effect;
        private EffectSource<? extends T> source;





        public HierarchyEffectSource(GraphicsEffect<? super T> effect, EffectSource<? extends T> source) {
            this.effect = effect;
            this.source = source;
        }





        public void paintSource(Graphics g) {
            effect.paintEffect(g, source);
        }





        public Rectangle getSourceBounds(Rectangle dest) {
            return source.getSourceBounds(dest);
        }





        public T getSource() {
            return source.getSource();
        }
    }
}
