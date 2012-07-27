/*
 * $Id: RoundLineBorder.java 2628 2008-08-05 16:05:09Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.border;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;
import javax.swing.border.AbstractBorder;

public class RoundLineBorder extends AbstractBorder {

    private Color color;
    private int arcWidth;
    private int arcHeight;

    public RoundLineBorder(Color color, int arcWidth, int arcHeight) {
        super();
        this.color = color;

        this.arcWidth = arcWidth;
        this.arcHeight = arcHeight;
    }





    @Override
    public Insets getBorderInsets(Component c) {
        return getBorderInsets(c, null);
    }





    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2 = (Graphics2D) g.create();

        if (color != null) {
            g2.setColor(color);
        }
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.drawRoundRect(x, y, width - 1, height - 1, arcWidth, arcHeight);

        g2.dispose();
    }





    public RoundRectangle2D getShape(int x, int y, int width, int height) {
        return new RoundRectangle2D.Float(x, y, width, height, arcWidth, arcHeight);
    }





    @Override
    public Insets getBorderInsets(Component c, Insets insets) {
        if (insets == null) {
            insets = new Insets(0, 0, 0, 0);
        }
        insets.top = insets.bottom = arcHeight >> 1;
        insets.left = insets.right = arcWidth >> 1;
        return insets;
    }





    public int getArcHeight() {
        return arcHeight;
    }





    public int getArcWidth() {
        return arcWidth;
    }





    public Color getColor() {
        return color;
    }





    public void setArcHeight(int arcHeight) {
        this.arcHeight = arcHeight;
    }





    public void setArcWidth(int arcWidth) {
        this.arcWidth = arcWidth;
    }





    public void setColor(Color color) {
        this.color = color;
    }
}
