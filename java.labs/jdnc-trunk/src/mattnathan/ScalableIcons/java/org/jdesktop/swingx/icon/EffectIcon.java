/*
 * $Id: EffectIcon.java 2628 2008-08-05 16:05:09Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.icon;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import javax.swing.Icon;

import org.jdesktop.swingx.effect.EffectSource;
import org.jdesktop.swingx.effect.GraphicsEffect;

/**
 * An icon which applies a GraphicsEffect to the icon at paint time. This icon, while scalable, only support the
 * preferred size of child icon. This is dues to an inability to calculate the source size given an effect size.
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 */
public class EffectIcon extends SimpleAbstractContainerIcon {
    /**
     * The effect to apply to the icon.
     */
    private GraphicsEffect<? super EffectIcon> effect;

    // cache variables
    private final Dimension size = new Dimension();
    private boolean valid = false;

    /**
     * Create a new default icon with no effect or child.
     */
    public EffectIcon() {
        super();
    }





    /**
     * Create a new icon around the given child icon.
     *
     * @param child The icon to apply the effect to.
     */
    public EffectIcon(Icon child) {
        this(child, null);
    }





    /**
     * Create a new icon with no child and the given effect.
     *
     * @param effect The effect to apply to the child.
     */
    public EffectIcon(GraphicsEffect<? super EffectIcon> effect) {
        this(null, effect);
    }





    /**
     * Create a new icon with the given child and effect.
     *
     * @param child The icon to apply the effect to.
     * @param effect The effect to apply to this icons child.
     */
    public EffectIcon(Icon child, GraphicsEffect<? super EffectIcon> effect) {
        super(child);
        this.effect = effect;
    }





    /**
     * Get the effect to be applied to the child. This may return null.
     *
     * @return The effect.
     */
    public GraphicsEffect<? super EffectIcon> getEffect() {
        return effect;
    }





    /**
     * Set the effect to apply to the child icon.
     *
     * @param effect The effect.
     */
    public void setEffect(GraphicsEffect<? super EffectIcon> effect) {
        GraphicsEffect<? super EffectIcon> old = getEffect();
        this.effect = effect;
        firePropertyChange("effect", old, getEffect());
    }





    /**
     * {@inheritDoc}
     */
    @Override
    protected void paintIconImpl(Component c, Graphics2D g2, int x, int y, int width, int height) {
        Icon child = getChild();
        if (child != null) {
            GraphicsEffect<? super EffectIcon> effect = getEffect();
            if (effect != null) {
                Rectangle r = new Rectangle(0, 0, child.getIconWidth(), child.getIconHeight());
                ScalableIconEffectSource source = new ScalableIconEffectSource(c, 0, 0, r.width, r.height);
                if (effect.transform(r, source)) {
                    source.x = x - r.x;
                    source.y = y - r.y;

                    effect.paintEffect(g2, source);
                } else {
                    paintChildIcon(c, g2, x, y, width, height);
                }
            } else {
                paintChildIcon(c, g2, x, y, width, height);
            }
        }
    }





    /**
     * Returns whether the cached data this icon keeps is valid.
     *
     * @return {@code true} if the size has been calculated.
     */
    public boolean isValid() {
        return valid;
    }





    /**
     * Validates any cached data this icon may keep.
     */
    public void validate() {
        if (!isValid()) {
            size.setSize(0, 0);
            Icon child = getChild();
            if (child != null) {
                size.setSize(child.getIconWidth(), child.getIconHeight());

                GraphicsEffect<? super EffectIcon> effect = getEffect();
                if (effect != null) {
                    Rectangle result = new Rectangle(0, 0, size.width, size.height);
                    if (effect.transform(result, new ScalableIconEffectSource(null, 0, 0, size.width, size.height))) {
                        size.setSize(result.width, result.height);
                    }
                }
            }
            valid = true;
        }
    }





    /**
     * Invalidates any cached data this icon may keep.
     */
    public void invalidate() {
        valid = false;
    }





    /**
     * Gets the width of the icon after the effect has been applied.
     *
     * @return The preferred width of the icon.
     */
    public int getIconWidth() {
        validate();
        return size.width;
    }





    /**
     * Gets the height of the icon after the effect has been applied.
     *
     * @return The preferred height of the icon.
     */
    public int getIconHeight() {
        validate();
        return size.height;
    }





    /**
     * This icon does not resize correctly as there is no way to get the original size given an effect size.
     *
     * @param c The component calling the paint.
     * @param width The target width for the icon.
     * @param height The target height for the icon.
     * @param result The place to put the results.
     * @return The preferred size of this icon.
     */
    @Override
    public Dimension fitInto(Component c, int width, int height, Dimension result) {
        if (result == null) {
            result = new Dimension();
        }
        validate();
        result.setSize(size);
        return result;
    }





    /**
     * Defines an EffectSource that can be used to apply a GraphicsEffect to an Icon.
     *
     * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
     */
    protected class ScalableIconEffectSource implements EffectSource<EffectIcon> {
        private Component c;
        private int width;
        private int height;
        private int x;
        private int y;

        /**
         * Create a new source for the given paint properties.
         *
         * @param c The component to paint to.
         * @param x The c coord to paint to.
         * @param y The y coord to paint to.
         * @param width The target width.
         * @param height The target height.
         */
        public ScalableIconEffectSource(Component c, int x, int y, int width, int height) {
            this.c = c;
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }





        public void paintSource(Graphics g) {
            getSource().paintChildIcon(c, g, x, y, width, height);
        }





        public Rectangle getSourceBounds(Rectangle dest) {
            if (dest == null) {
                dest = new Rectangle(x, y, width, height);
            } else {
                dest.setBounds(x, y, width, height);
            }
            return dest;
        }





        public EffectIcon getSource() {
            return EffectIcon.this;
        }

    }
}
