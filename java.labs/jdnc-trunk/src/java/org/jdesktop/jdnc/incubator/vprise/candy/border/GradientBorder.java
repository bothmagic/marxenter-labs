package org.jdesktop.jdnc.incubator.vprise.form;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Font;
import java.awt.Paint;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.GradientPaint;
import java.awt.ComponentOrientation;
import java.awt.Insets;
import java.awt.SystemColor;

/**
 * This class allows us to place a gradient title border similar to the Eclipse
 * gradients. It should normally be used with the shadown border as a compound
 * border.
 *
 * @author Shai Almog
 */
public class GradientBorder implements Border {
    private String title;
    private Color sourceColor;
    private Color destinationColor;
    private Color textColor;
    public GradientBorder(String title) {
        this(title, SystemColor.activeCaption, SystemColor.window, SystemColor.activeCaptionText);
    }
            
    public GradientBorder(String title, Color sourceColor, Color destinationColor, 
        Color textColor) {
        this.title = title;
        this.sourceColor = sourceColor;
        this.destinationColor = destinationColor;
        this.textColor = textColor;
    }
    
    public Insets getBorderInsets(Component c) {
        int top = c.getFont().getSize() + 6;
        return(new Insets(top, 2, 2, 2));
    }
    
    public boolean isBorderOpaque() {
        // better performance
        return true;
    }
            
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2d = (Graphics2D) g;
        GradientPaint paint;
        Paint oldPaint = g2d.getPaint();
        int textX;
        Font f = c.getFont();
        g2d.setFont(f);
        x += 2;
        width -= 1;
        if (c.getComponentOrientation() == ComponentOrientation.RIGHT_TO_LEFT) {
            textX = x + width - 4 - g2d.getFontMetrics().stringWidth(title);
            paint = new GradientPaint(x, y, destinationColor, width, height, sourceColor);
        } else {
            y += 2;
            textX = x + 4;
            paint = new GradientPaint(x, y, sourceColor, width, height, destinationColor);
        }
        
        height = f.getSize() + 6;
        g2d.setPaint(paint);
        g2d.fillRect(x, y, width, height);
        g2d.setPaint(oldPaint);
        
        g2d.setColor(textColor);
        g2d.drawString(title, textX, y + height - 5);
    }
}
