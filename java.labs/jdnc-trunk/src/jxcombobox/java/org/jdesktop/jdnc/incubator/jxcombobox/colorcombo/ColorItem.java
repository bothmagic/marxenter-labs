/*
 * ColorItem.java
 *
 * Created on 24 de Fevereiro de 2005, 22:17
 */

package org.jdesktop.jdnc.incubator.jxcombobox.colorcombo;


import javax.swing.JComponent;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
/**
 *
 * @author  Ricardo Lopes
 *
 * @todo use UIManager Colors for the over colors
 */
public class ColorItem extends JComponent {
    
    protected static final int DEFAULT_SIZE = 12;
    protected static final int BORDER_SIZE = 3;
    protected static final Color BORDER_COLOR = new Color(128, 128, 128);
    protected static final Color OVER_COLOR = new Color(182, 189, 210, 100);
    protected static final Color OVER_BORDER_COLOR = new Color(10, 30, 106);
    
    protected boolean over;
    

    public ColorItem(Color color) {
        super();
        setColor(color);
        setPreferredSize(new Dimension(DEFAULT_SIZE + BORDER_SIZE*2+1, DEFAULT_SIZE + BORDER_SIZE*2+1));
        over = false;
    }
    
    public void setColor(Color newColor) {
        setBackground(newColor);
    }
    
    public Color getColor() {
        return getBackground();
    }
    
    public void setOver(boolean isOver) {
        over = isOver;
        repaint();
    }
    
    public boolean isOver() {
        return over;
    }
    
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        g.setColor(getColor());
        g.fillRect(BORDER_SIZE, BORDER_SIZE, DEFAULT_SIZE - BORDER_SIZE + 2, DEFAULT_SIZE - BORDER_SIZE + 2);
        g.setColor(BORDER_COLOR);
        g.drawRect(BORDER_SIZE, BORDER_SIZE, DEFAULT_SIZE - BORDER_SIZE + 2, DEFAULT_SIZE - BORDER_SIZE + 2);
        
        if (over) {
            g.setColor(OVER_COLOR);
            g.fillRect(0, 0, DEFAULT_SIZE + 2*BORDER_SIZE - 1, DEFAULT_SIZE + 2*BORDER_SIZE - 1);
            g.setColor(OVER_BORDER_COLOR);
            g.drawRect(0, 0, DEFAULT_SIZE + 2*BORDER_SIZE - 1, DEFAULT_SIZE + 2*BORDER_SIZE -1);
        }
        
    }
}



