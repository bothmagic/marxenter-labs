/*
 * $Id: IconLabelUI.java 862 2006-08-22 09:25:35Z rasto1968 $
 *
 * Copyright 2006 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx;

import java.awt.BasicStroke;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Stroke;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.plaf.basic.BasicLabelUI;

/**
 *  A special version of LabelUI that handles focus and selection painting of
 *  a label when it is used as part of an IconPanel
 *
 * @author Rob Stone
 */
public class IconLabelUI extends BasicLabelUI
{
    // Used when truncating text
    private static final String DOTS="...";

    // Used to provide a focus rectangle
    private static final Stroke DASHED_STROKE=new BasicStroke(
        1f,
        BasicStroke.CAP_BUTT,
        BasicStroke.JOIN_MITER,
        1f,
        new float[] {1f},
        0f);

    private Rectangle textR;
    private IconPanel iconPanel;

    @Override
    protected String layoutCL(
        final JLabel label,
        final FontMetrics fontMetrics,
        String text,
        final Icon icon,
        final Rectangle viewR,
        final Rectangle iconR,
        final Rectangle textR)
    {
        // Let BasicLabelUI do its calculations
        String result=super.layoutCL(label, fontMetrics, text, icon, viewR, iconR, textR);

        // Now modify the rectangles to cope with text being truncated, also
        // make sure the icon is always drawn in the center.
        if (icon!=null)
        {
            if (textR.width>viewR.width)
            {
                int dotsWidth=fontMetrics.stringWidth(DOTS);
                for (int i=0; i<text.length(); i++)
                {
                    if (fontMetrics.stringWidth(text.substring(0, i)) + dotsWidth > viewR.width)
                    {
                        text=text.substring(0,i-1) + DOTS;
                        result=text;
                        break;
                    }
                }
                textR.width=viewR.width;
            }
            else
            {
                textR.x=(viewR.width-textR.width)/2;
            }

            // Force the icon to be drawn in the center
            iconR.x=(viewR.width-iconR.width)/2;
        }

        // Save away the text rectangle, we need this when drawing the focus
        // and selection boxes around the text. Don't really like this approach,
        // but this is the only way to get access to this information without
        // recalculating it (textR is private in BasicLabelUI)
        this.textR=textR;

        return result;
    }

    /**
     *  Draws a focus rectangle
     *  @param l the label
     *  @param g the graphics context
     */
    private void drawFocus(final JLabel l, final Graphics g)
    {
        if (l instanceof IconPanel.IconLabel)
        {
            final IconPanel.IconLabel label=(IconPanel.IconLabel)l;

            // Only draw focus if the label has focus and the container has focus
            if (label.hasFocus() && label.getIconPanel().hasFocus())
            {
                final Graphics2D g2D=(Graphics2D)g;
                Stroke bs=new BasicStroke();
                g2D.setStroke(DASHED_STROKE);

                // Do we need to provide some way of changing these insets ?
                g2D.drawRect(textR.x-2, textR.y-1, textR.width+2, textR.height+1);
            }
        }
    }

    @Override
    protected void paintEnabledText(JLabel l, Graphics g, String s, int textX, int textY)
    {
        super.paintEnabledText(l, g, s, textX, textY);
        drawFocus(l, g);
    }


    @Override
    protected void paintDisabledText(JLabel l, Graphics g, String s, int textX, int textY)
    {
        // 'Borrow' disabled mode to paint our text differently when selected.
        g.setColor(IconPanel.SELECTION_BACKGROUND);
        g.fillRect(textR.x-2, textR.y-1, textR.width+3, textR.height+5);
        g.setColor(IconPanel.SELECTION_FOREGROUND);
        g.drawString(s, textX, textY);
        drawFocus(l, g);
    }

    /**
     * Create the custom LabelUI
     * @param iconPanel the panel being drawn to
     */
    IconLabelUI(final IconPanel iconPanel)
    {
        this.iconPanel=iconPanel;
    }
}
