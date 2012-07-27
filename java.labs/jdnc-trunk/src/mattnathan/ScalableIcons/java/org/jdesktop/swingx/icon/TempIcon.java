/*
 * $Id: TempIcon.java 2628 2008-08-05 16:05:09Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.icon;

import org.jdesktop.swingx.util.PaintUtils;

import java.awt.*;

/**
 * Simple icon that can be used for place holding. This icon paints a rectangle and cross in the foreground colour at the
 * required size. The dimensions given in the constructor determin the preferred size of the icon.
 * <p/>
 * Note that a TempIcon with a foreground of null uses the Components foreground color.
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 */
public class TempIcon extends MatteIcon {

    /**
     * The foreground colour of the icon
     */
    private Paint foreground;

    /**
     * Creates a new TempIcon with dimensions 32x32.
     */
    public TempIcon() {
        this(32, 32);
    }





    /**
     * Creates a temp icon with the given preferred width and height.
     *
     * @param width The preferred width for this icon.
     * @param height The preferred height for this icon.
     */
    public TempIcon(int width, int height) {
        this(width, height, null, null);
    }





    /**
     * Creates a new TempIcon with the given width and height and a foreground of {@code foreground}.
     *
     * @param width The preferred width of this icon.
     * @param height The preferred height of this icon.
     * @param foreground The foreground color for this icon.
     */
    public TempIcon(int width, int height, Paint foreground) {
        this(width, height, foreground, null);
    }





    /**
     * Creates a new TempIcon with the given default values.
     *
     * @param width The preferred with of this icon.
     * @param height The preferred height of this icon.
     * @param foreground The foreground paint for this icon.
     * @param background The background paint for this icon.
     */
    public TempIcon(int width, int height, Paint foreground, Paint background) {
        super(width, height, background);
        this.foreground = foreground;
    }





    /**
     * First paints the background if the background colour is non-null then paints a rectangle and cross in the
     * foreground colour.
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
        super.paintIconImpl(c, g2, x, y, width, height);

        Paint foreground = getForeground();
        if (foreground == null) {
            foreground = c == null ? Color.BLACK : c.getForeground();
        }
        if (foreground != null) {
            if (foreground instanceof Color) {
                g2.setColor((Color) foreground);
            } else {
                foreground = PaintUtils.resizeGradient(foreground, width, height);
                g2.setPaint(foreground);
            }
            g2.drawRect(x, y, width - 1, height - 1);
            g2.drawLine(x, y, x + width - 1, y + height - 1);
            g2.drawLine(x, y + height - 1, x + width - 1, y);
        }
    }





    /**
     * Gets the foreground colour of this icon.
     *
     * @return The foreground paint.
     * @see #setForeground(java.awt.Paint)
     */
    public Paint getForeground() {
        return foreground;
    }





    /**
     * Set the foreground colour of this icon. If set to null the Components foreground will be used.
     *
     * @param foreground The foreground paint.
     * @see #getForeground()
     */
    public void setForeground(Paint foreground) {
        Paint old = getForeground();
        this.foreground = foreground;
        firePropertyChange("foreground", old, getForeground());
    }
}
