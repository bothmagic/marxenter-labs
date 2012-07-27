/*
 * $Id: AbstractFilterEffect.java 2628 2008-08-05 16:05:09Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.effect;

import org.jdesktop.swingx.image.ImageUtilities;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

/**
 * Provides an abstract base for effects that provide a filtering of a buffer. This class is not intended to be extended
 * when the repaint area is altered (as with a ConvolveOp) but should form the basis of filtering effects.
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 */
public abstract class AbstractFilterEffect<T> extends AbstractGraphicsEffect<T> {
    /**
     * Provides the boiler plate code for an effect that filters the source. this calls filter to do the actual work on
     * the source.
     *
     * @param g            The graphics to paint to.
     * @param effectSource The source of the effect.
     */
    public void paintEffect(Graphics g, EffectSource<? extends T> effectSource) {
        boolean enabled = isFilterEnabled();
        if (enabled) {
            BufferedImage buffer = EffectUtilities.bufferEffect(effectSource, g, true);
            BufferedImage result = filter(buffer);
            Rectangle r = g.getClipBounds();
            g.drawImage(result, r.x, r.y, null);
            ImageUtilities.releaseImage(result);
            ImageUtilities.releaseImage(buffer);
        } else {
            effectSource.paintSource(g);
        }
    }





    /**
     * Doesn not alter the repaint area but returns true if the filter is enabled.
     *
     * @param area         Unused.
     * @param effectSource Unused
     * @return {@code isFilterEnabled()}.
     */
    public boolean transform(Rectangle area, EffectSource<? extends T> effectSource) {
        return isFilterEnabled();
    }





    /**
     * Subclasses should implement their filter code here. The returned BufferdImage should not be null. It is allowed
     * that the returned image be the same as the passed in image.
     *
     * @param image The image to filter.
     * @return The result of the filter operation.
     */
    protected abstract BufferedImage filter(BufferedImage image);





    /**
     * Sub classes should return true only when the filter will have an effect. This is designed to provide performance
     * hints when the filter will not affect the image.
     *
     * @return {@code true} if the current state would alter the source image is filter were called.
     */
    protected abstract boolean isFilterEnabled();
}
