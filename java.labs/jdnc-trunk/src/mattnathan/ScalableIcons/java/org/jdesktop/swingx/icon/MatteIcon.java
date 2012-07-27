/*
 * $Id: MatteIcon.java 2628 2008-08-05 16:05:09Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.icon;

import org.jdesktop.swingx.util.PaintUtils;
import org.jdesktop.swingx.util.ScalePolicy;

import java.awt.*;

/**
 * A simple icon which fills the icon area with a solid Paint instance. This icon will stretch any non-solid background
 * Paints so that they fill the whole area. This icon does not require a fixed aspect ratio.
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 */
public class MatteIcon extends AbstractScalableIcon {

    /**
     * The preferred width of this icon.
     */
    private int width = 0;
    /**
     * The preferred height of this icon.
     */
    private int height = 0;

    /**
     * The painter used to paint this icon.
     */
    private Paint background;

    /**
     * Create a new MatteIcon with a size of 32x32 and no paint background.
     */
    public MatteIcon() {
        this(32, 32, null);
    }





    /**
     * Creates a matte icon with the given dimensions and no fill.
     *
     * @param width The preferred width of the icon.
     * @param height The preferred height of the icon.
     */
    public MatteIcon(int width, int height) {
        this(width, height, null);
    }





    /**
     * Creates a new matte icon with the given background and 32x32 dimensions.
     *
     * @param background The background paint for the icon.
     */
    public MatteIcon(Paint background) {
        this(32, 32, background);
    }





    /**
     * Creates a new matte icon with the given preferred size and background.
     *
     * @param width The preferred with of the icon.
     * @param height The preferred height of the icon.
     * @param background The background paint for the icon. If null the components background will be used.
     */
    public MatteIcon(int width, int height, Paint background) {
        super(ScalePolicy.valueOf(ScalePolicy.DimensionPolicy.BOTH, ScalePolicy.ResizePolicy.BEST_FIT));
        this.width = width;
        this.height = height;
        this.background = background;
    }





    /**
     * Fills the target are with the current background Paint. If the background paint is null this will use the given
     * components background property. If the background paint is
     *
     * @param c The component calling the paint.
     * @param g2 The graphics to paint to.
     * @param x The x coordinate to paint to.
     * @param y The y coordinate to paint to.
     * @param width The width of the icon.
     * @param height The height of the icon.
     */
    @Override
    protected void paintIconImpl(Component c, Graphics2D g2, int x, int y, int width, int height) {
        Paint background = getBackground();
        if (background == null) {
            background = c == null ? Color.WHITE : c.getBackground();
        }

        if (background != null) {
            if (background instanceof Color) {
                g2.setColor((Color) background);
            } else {
                background = PaintUtils.resizeGradient(background, width, height);
                g2.setPaint(background);
            }
            g2.fillRect(x, y, width, height);
        }
    }





    /**
     * Returns the icon's height.
     *
     * @return an int specifying the fixed height of the icon.
     */
    public int getIconHeight() {
        return height;
    }





    /**
     * Returns the icon's width.
     *
     * @return an int specifying the fixed width of the icon.
     */
    public int getIconWidth() {
        return width;
    }





    /**
     * Gets the paint instance used to fill this icon.
     *
     * @return The paint used to fill this icon.
     * @see #setBackground(java.awt.Paint)
     */
    public Paint getBackground() {
        return background;
    }





    /**
     * Set the background paint for this icon.
     *
     * @param background The paint used to fill this icon.
     * @see #getBackground()
     */
    public void setBackground(Paint background) {
        Paint old = getBackground();
        this.background = background;
        firePropertyChange("background", old, getBackground());
    }

}
