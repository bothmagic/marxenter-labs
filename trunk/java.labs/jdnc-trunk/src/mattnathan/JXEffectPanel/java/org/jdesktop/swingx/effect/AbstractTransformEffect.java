/*
 * $Id: AbstractTransformEffect.java 2628 2008-08-05 16:05:09Z mattnathan $
 *
 * Copyright 2005 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */

package org.jdesktop.swingx.effect;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;

/**
 * Defines an abstract super class for a component of an affine transform effect.
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 */
public abstract class AbstractTransformEffect<T> extends AbstractGraphicsEffect<T> {
    /**
     * Creates a new instance of AbstractTransformEffect
     */
    public AbstractTransformEffect() {
    }





    public void paintEffect(Graphics g, EffectSource<? extends T> source) {
        Rectangle sourceArea = source.getSourceBounds(null);
        AffineTransform trans = createTransform(source, sourceArea.width, sourceArea.height);
        AffineTransform old = null;
        boolean paint = true;
        if (trans != null) {
            old = ((Graphics2D) g).getTransform();
            AffineTransform t = new AffineTransform();
            t.concatenate(old);
            t.translate(sourceArea.x, sourceArea.y);
            t.concatenate(trans);
            t.translate(-sourceArea.x, -sourceArea.y);
            ((Graphics2D) g).setTransform(t);
            paint = g.getClip() != null;
        }
        if (paint) {
            source.paintSource(g);
        }
        if (old != null) {
            ((Graphics2D) g).setTransform(old);
        }
    }





    /**
     * Transforms the given area by the current transform property. The transformation is computed via the {@link
     * #createTransform(EffectSource,int,int)} method.
     *
     * @param area   The area to transform.
     * @param source The source of the effect.
     * @return boolean
     * @see #createTransform(EffectSource,int,int)
     */
    public boolean transform(Rectangle area, EffectSource<? extends T> source) {
        Rectangle totalArea = source.getSourceBounds(null);
        AffineTransform trans = createTransform(source, totalArea.width, totalArea.height);

        if (trans != null) {
            double[] src = {
                    area.x - totalArea.x, area.y - totalArea.y,
                    area.x - totalArea.x + area.width, area.y - totalArea.y,
                    area.x - totalArea.x, area.y - totalArea.y + area.height,
                    area.x - totalArea.x + area.width, area.y - totalArea.y + area.height
            };
            double[] dst = new double[src.length];
            trans.transform(src, 0, dst, 0, 4);

            double minx = dst[0];
            double miny = dst[1];
            double maxx = dst[0];
            double maxy = dst[1];
            for (int i = 2; i < dst.length; i += 2) {
                if (dst[i] > maxx) {
                    maxx = dst[i];
                }
                if (dst[i] < minx) {
                    minx = dst[i];
                }
                if (dst[i + 1] > maxy) {
                    maxy = dst[i + 1];
                }
                if (dst[i + 1] < miny) {
                    miny = dst[i + 1];
                }
            }

            // need to get rid of rounding errors for when we cast to an int
            minx = Math.floor(minx);
            miny = Math.floor(miny);
            maxx = Math.ceil(maxx);
            maxy = Math.ceil(maxy);

            area.setBounds((int) minx + totalArea.x, (int) miny + totalArea.y, (int) (maxx - minx), (int) (maxy - miny));
        }

        return trans != null;
    }





    /**
     * Returns the transform to use to translate the component for the given dimensions.
     *
     * @param source The source the transform will be applied to.
     * @param width  The width of the area to transform.
     * @param height The height of the area to transform.
     * @return The transform used to translate the painting.
     */
    protected abstract AffineTransform createTransform(EffectSource<? extends T> source, int width, int height);
}
