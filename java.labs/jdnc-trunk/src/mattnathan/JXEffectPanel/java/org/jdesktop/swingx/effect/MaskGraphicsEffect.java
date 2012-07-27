/*
 * $Id: MaskGraphicsEffect.java 2628 2008-08-05 16:05:09Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.effect;

import org.jdesktop.swingx.image.ImageUtilities;
import org.jdesktop.swingx.painter.MattePainterTmp;
import org.jdesktop.swingx.painter.Painter;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.image.BufferedImage;

/**
 * Applies a mask to the source. The mask is represented as a Painter where the alpha channel of the painter is used to
 * denote the amount of the source rendering will be shown. this acts in the same way as applying an
 * AlphaComposite.DstIn to the source rendering.
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 */
public class MaskGraphicsEffect<T> extends AbstractGraphicsEffect<T> {

    public static final GraphicsEffect<Object> FADE_DOWN = new MaskGraphicsEffect<Object>(
            new MattePainterTmp<Object>(new GradientPaint(0, 0, Color.BLACK, 0, 1, new Color(0, 0, 0, 0), true)));
    public static final GraphicsEffect<Object> FADE_UP = new MaskGraphicsEffect<Object>(
            new MattePainterTmp<Object>(new GradientPaint(0, 1, Color.BLACK, 0, 0, new Color(0, 0, 0, 0), true)));
    public static final GraphicsEffect<Object> FADE_RIGHT = new MaskGraphicsEffect<Object>(
            new MattePainterTmp<Object>(new GradientPaint(0, 0, Color.BLACK, 1, 0, new Color(0, 0, 0, 0), true)));
    public static final GraphicsEffect<Object> FADE_LEFT = new MaskGraphicsEffect<Object>(
            new MattePainterTmp<Object>(new GradientPaint(1, 0, Color.BLACK, 0, 0, new Color(0, 0, 0, 0), true)));

    private Painter<? super T> mask;





    /**
     * Creates a new effect with no mask.
     */
    public MaskGraphicsEffect() {
        this(null);
    }





    /**
     * Creates a new mask effect with the given mask.
     *
     * @param mask The mask.
     */
    public MaskGraphicsEffect(Painter<? super T> mask) {
        this.mask = mask;
    }





    public void paintEffect(Graphics g, EffectSource<? extends T> effectSource) {
        Painter<? super T> mask = getMask();

        Graphics2D paintg = (Graphics2D) g;
        BufferedImage buffer = null;
        Shape clip;
        Rectangle clipBounds = null;
        if (mask != null) {
            clip = g.getClip();
            if (clip == null) {
                clip = effectSource.getSourceBounds(null);
            }
            clipBounds = clip.getBounds();

            buffer = ImageUtilities.createCompatibleTranslucentImage(g, clipBounds.width, clipBounds.height);

            paintg = buffer.createGraphics();
            paintg.translate(-clipBounds.x, -clipBounds.y);
            paintg.setClip(clip);
            paintg.setColor(g.getColor());
            paintg.setFont(g.getFont());
        }

        effectSource.paintSource(paintg);

        if (mask != null) {
            assert paintg != g;

            paintg.setComposite(AlphaComposite.DstIn);
            Rectangle bounds = effectSource.getSourceBounds(null);
            paintg.translate(bounds.x, bounds.y);

            mask.paint(paintg, effectSource.getSource(), bounds.width, bounds.height);
            g.drawImage(buffer, clipBounds.x, clipBounds.y, null);

            paintg.dispose();
            ImageUtilities.releaseImage(buffer);
        }
    }





    public boolean transform(Rectangle area, EffectSource<? extends T> effectSource) {
        return getMask() != null;
    }





    /**
     * Get the mask used to render this effect.
     *
     * @return The rendering mask.
     * @see #setMask(org.jdesktop.swingx.painter.Painter)
     */
    public Painter<? super T> getMask() {
        return mask;
    }





    /**
     * Set the mask for this effect.
     *
     * @param mask The new mask. This can be null.
     * @see #getMask()
     */
    public void setMask(Painter<? super T> mask) {
        Painter<? super T> old = getMask();
        this.mask = mask;
        firePropertyChange("mask", old, getMask());
    }
}
