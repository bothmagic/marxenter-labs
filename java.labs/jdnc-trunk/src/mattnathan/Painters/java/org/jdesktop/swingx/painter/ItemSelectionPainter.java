/*
 * $Id: ItemSelectionPainter.java 2629 2008-08-06 08:27:49Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.painter;

import java.awt.Color;
import java.awt.Component;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Area;

import org.jdesktop.swingx.border.RoundLineBorder;

/**
 * A painter which resembles a Windows Vista style list selection background. This is a painter which paints a vertical
 * gradient with an inner and outer rounded border in solid colours. The vista painters are provided as class level
 * constants and give painters for different rollover states.
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 * @version 2.0
 */
public class ItemSelectionPainter extends AbstractPainter<Component> {
    /**
     * Default border roundness.
     */
    private static final int ROUNDEDNESS = 6;

    /**
     * Painter representing Windows vistas selected list item background.
     */
    public static final Painter<Component> VISTA_SELECTION = new ItemSelectionPainter(
            new Color(51, 189, 251, 127), new Color(255, 255, 255, 127),
            new Color(227, 241, 251, 127), new Color(171, 223, 249, 127));
    /**
     * Painter representing Windows vistas selected list item background when the
     * list doesn't have focus.
     */
    public static final Painter<Component> VISTA_SELECTION_NOFOCUS = new ItemSelectionPainter(
            new Color(179, 179, 179, 127), new Color(255, 255, 255, 127),
            new Color(241, 241, 241, 127), new Color(203, 203, 203, 127));

    /**
     * Painter representing Windows vistas rollover list item background.
     */
    public static final Painter<Component> VISTA_ROLLOVER = new ItemSelectionPainter(
            new Color(117, 225, 245, 127), new Color(255, 255, 255, 127),
            new Color(235, 245, 251, 127), new Color(209, 235, 251, 127));

    /**
     * Painter representing Windows vistas rollover and selected list item background.
     */
    public static final Painter<Component> VISTA_SELECTED_ROLLOVER = new ItemSelectionPainter(
            new Color(109, 205, 247, 127), new Color(255, 255, 255, 127),
            new Color(209, 237, 251, 127), new Color(137, 209, 245, 127));

    /**
     * Painter representing Windows vistas pressed list item background.
     */
    public static final Painter<Component> VISTA_PRESSED = new ItemSelectionPainter(
            new Color(0, 193, 245, 127), new Color(255, 255, 255, 127),
            new Color(173, 223, 249, 127), new Color(93, 193, 243, 127));

    private MattePainterTmp<Component> background;
    private RoundLineBorder outerBorder;
    private RoundLineBorder innerBorder;

    /**
     * Create a new Painter identical to VISTA_SELECTION.
     */
    public ItemSelectionPainter() {
        this(new Color(217, 217, 217), new Color(255, 255, 255, 200),
             new Color(248, 248, 248), new Color(229, 229, 229));
    }





    /**
     * Create a new painter with the given gradient colours and a outer border color of bottomColor and an inner border
     * color of topColor.
     *
     * @param topColor The gradients top color and inner border color.
     * @param bottomColor The gradients bottom color and outer border color.
     */
    public ItemSelectionPainter(Color topColor, Color bottomColor) {
        this(null, null, topColor, bottomColor);
    }





    /**
     * Create a new painter using the given colours.
     *
     * @param outerColor The outer border color.
     * @param innerColor The inner border color.
     * @param topColor The gradients top color.
     * @param bottomColor The gradients bottom color.
     */
    public ItemSelectionPainter(Color outerColor, Color innerColor, Color topColor, Color bottomColor) {
        if (topColor != null && bottomColor != null) {
            background = new MattePainterTmp<Component>(new GradientPaint(0, 0, topColor, 0, 1, bottomColor));
        }

        if (outerColor == null) {
            outerColor = bottomColor;
        }
        if (outerColor != null) {
            outerBorder = new RoundLineBorder(outerColor, ROUNDEDNESS, ROUNDEDNESS);
        }
        if (innerColor == null) {
            innerColor = topColor;
        }
        if (innerColor != null) {
            innerBorder = new RoundLineBorder(innerColor, ROUNDEDNESS - 2, ROUNDEDNESS - 2);
        }

    }





    /**
     * Subclasses should implement this method and perform custom painting operations here.
     *
     * @param g The Graphics2D object in which to paint
     * @param component The JComponent that the Painter is delegate for.
     * @param w int
     * @param h int
     */
    @Override
    protected void doPaint(Graphics2D g, Component component, int w, int h) {
        int x = 0;
        int y = 0;
        Shape oldClip = g.getClip();
        if (innerBorder != null) {
            Area newClip = new Area(oldClip);
            newClip.intersect(new Area(innerBorder.getShape(x + 1, y, w - 2, h)));
            g.setClip(newClip);
        } else if (outerBorder != null) {
            Area newClip = new Area(oldClip);
            newClip.intersect(new Area(innerBorder.getShape(x, y, w, h)));
            g.setClip(newClip);
        }

        background.paint(g, component, w, h);

        g.setClip(oldClip);

        if (outerBorder != null) {
            outerBorder.paintBorder(component, g, x, y, w, h);
        }
        if (innerBorder != null) {
            innerBorder.paintBorder(component, g, x + 1, y + 1, w - 2, h - 2);
        }
    }





    /**
     * Get the color used to paint the outer border. This can be null.
     *
     * @return The outer border color.
     * @see #setOuterBorderColor
     */
    public Color getOuterBorderColor() {
        return outerBorder == null ? null : outerBorder.getColor();
    }





    /**
     * Get the color used to paint the inner border color. This can be null.
     *
     * @return The inner border color.
     * @see #setInnerBorderColor
     */
    public Color getInnerBorderColor() {
        return innerBorder == null ? null : innerBorder.getColor();
    }





    /**
     * Get the color used for the gradients top color. This can be null.
     *
     * @return The gradients top color.
     * @see #setTopColor
     */
    public Color getTopColor() {
        return background == null ? null : ((GradientPaint) background.getFillPaint()).getColor1();
    }





    /**
     * Get the color used for the gradients bottom color. This can be null.
     *
     * @return The gradients bottom color.
     * @see #setBottomColor
     */
    public Color getBottomColor() {
        return background == null ? null : ((GradientPaint) background.getFillPaint()).getColor2();
    }





    /**
     * Sets the outer border color. If null is passed then the bottom color is used.
     *
     * @param color The new outer border color.
     * @see #getOuterBorderColor
     * @see #getBottomColor
     */
    public void setOuterBorderColor(Color color) {
        Color old = getOuterBorderColor();

        if (color == null) {
            color = getBottomColor();
        }

        if (outerBorder == null) {
            if (color != null) {
                outerBorder = new RoundLineBorder(color, ROUNDEDNESS, ROUNDEDNESS);
            }
        } else {
            if (color == null) {
                outerBorder = null;
            } else {
                outerBorder.setColor(color);
            }
        }
        firePropertyChange("outerBorderColor", old, color);
    }





    /**
     * Set the inner border color. If null is given then the gradients top color is used.
     *
     * @param color The new inner border color.
     * @see #getInnerBorderColor
     * @see #getTopColor
     */
    public void setInnerBorderColor(Color color) {
        Color old = getInnerBorderColor();

        if (color == null) {
            color = getTopColor();
        }

        if (innerBorder == null) {
            if (color != null) {
                innerBorder = new RoundLineBorder(color, ROUNDEDNESS - 2, ROUNDEDNESS - 2);
            }
        } else {
            if (color == null) {
                innerBorder = null;
            } else {
                innerBorder.setColor(color);
            }
        }
        firePropertyChange("innerBorderColor", old, color);
    }





    /**
     * Set the top color for this painter. If null is given then the background gradient will not be painted and the
     * bottom color will also be set to null.
     *
     * @param color The new gradients top color.
     * @see #getTopColor
     * @see #getBottomColor
     */
    public void setTopColor(Color color) {
        Color old = getTopColor();
        if (background == null) {
            if (color != null) {
                background = new MattePainterTmp<Component>(new GradientPaint(0, 0, color, 0, 1, color, true));
            }
        } else {
            if (color == null) {
                background = null;
            } else {
                background.setFillPaint(new GradientPaint(0, 0, color, 0, 1,
                      ((GradientPaint) background.getFillPaint()).getColor2(), true));
            }
        }
        firePropertyChange("topColor", old, color);
    }





    /**
     * Set the bottom color for this painter. If null is given then the background gradient will not be painted and the
     * top color will also be set to null.
     *
     * @param color The new gradients bottom color.
     * @see #getBottomColor
     * @see #getTopColor
     */
    public void setBottomColor(Color color) {
        Color old = getBottomColor();
        if (background == null) {
            if (color != null) {
                background = new MattePainterTmp<Component>(new GradientPaint(0, 0, color, 0, 1, color, true));
            }
        } else {
            if (color == null) {
                background = null;
            } else {
                background.setFillPaint(new GradientPaint(0, 0,
                      ((GradientPaint) background.getFillPaint()).getColor1(), 0, 1, color, true));
            }
        }
        firePropertyChange("bottomColor", old, color);
    }
}
