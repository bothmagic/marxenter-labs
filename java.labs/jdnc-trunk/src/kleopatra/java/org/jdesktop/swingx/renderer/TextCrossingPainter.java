/*
 * Created on 18.09.2008
 *
 */
package org.jdesktop.swingx.renderer;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;

import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import org.jdesktop.swingx.painter.AbstractPainter;

public class TextCrossingPainter<T extends JLabel> extends AbstractPainter<T> {
    Rectangle paintIconR = new Rectangle();
    Rectangle paintViewR = new Rectangle();
    Rectangle paintTextR = new Rectangle();
    Insets insetss = new Insets(0, 0, 0, 0);

    @Override
    protected void doPaint(Graphics2D g, JLabel label, int width,
            int height) {
        Insets insets = label.getInsets(insetss);
        paintViewR.x = insets.left;
        paintViewR.y = insets.top;
        paintViewR.width = width - (insets.left + insets.right);
        paintViewR.height = height - (insets.top + insets.bottom);
        paintIconR.x = paintIconR.y = paintIconR.width = paintIconR.height = 0;
        paintTextR.x = paintTextR.y = paintTextR.width = paintTextR.height = 0;
        SwingUtilities.layoutCompoundLabel(label, 
                label.getFontMetrics(label.getFont()), label.getText(), null,
                label.getVerticalAlignment(), label.getHorizontalAlignment(), 
                label.getVerticalTextPosition(), label.getHorizontalTextPosition(),
                paintViewR, paintIconR, paintTextR, label.getIconTextGap());
        doPaint(g, paintTextR);
    }
    
    private void doPaint(Graphics2D g, Rectangle r) {
        Color old = g.getColor();
        g.setColor(Color.RED);
        g.drawLine(r.x, r.y, r.x + r.width, r.y + r.height);
        g.drawLine(r.x + 1, r.y, r.x + r.width + 1, r.y + r.height);
        g.drawLine(r.x + r.width, r.y, r.x, r.y + r.height);
        g.drawLine(r.x + r.width - 1, r.y, r.x - 1, r.y + r.height);
        g.setColor(old);
        
    }
}