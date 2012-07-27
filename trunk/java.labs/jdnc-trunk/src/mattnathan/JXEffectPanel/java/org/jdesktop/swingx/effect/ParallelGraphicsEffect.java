/*
 * $Id: ParallelGraphicsEffect.java 2628 2008-08-05 16:05:09Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.effect;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Collection;

/**
 * Defines an effect which applies each of its child effect separately one after the other.
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 */
public class ParallelGraphicsEffect<T> extends AbstractContainerEffect<T> {

    /**
     * Creates a new effect with no children.
     *
     * @see #setChildren(GraphicsEffect[])
     */
    public ParallelGraphicsEffect() {
    }





    /**
     * Creates a new effect with the given children.
     *
     * @param children The children of this effect.
     * @see #setChildren(GraphicsEffect[])
     */
    public ParallelGraphicsEffect(GraphicsEffect<? super T>... children) {
        super(children);
    }





    /**
     * Creates a new effect with the given children.
     *
     * @param children The children for this effect
     * @see #setChildren(GraphicsEffect[])
     */
    public ParallelGraphicsEffect(Collection<GraphicsEffect<? super T>> children) {
        super(children);
    }





    public void paintEffect(Graphics g, EffectSource<? extends T> effectSource) {
        int count = 0;
        for (GraphicsEffect<? super T> child : this) {
            count++;
            child.paintEffect(g, effectSource);
        }
        if (count == 0) {
            effectSource.paintSource(g);
        }
    }





    public boolean transform(Rectangle area, EffectSource<? extends T> effectSource) {
        boolean result = false;
        Rectangle total = new Rectangle(area);
        Rectangle clone = new Rectangle();
        for (GraphicsEffect<? super T> child : this) {
            clone.setBounds(area);
            result = child.transform(clone, effectSource) || result;
            total.add(clone);
        }
        area.setBounds(total);
        return result;
    }
}
