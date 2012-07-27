package org.jdesktop.jdnc.incubator.vprise.form;

import java.net.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.Insets;
import java.awt.Component;
import java.awt.Graphics;

/**
 * This class allows us to work with borders that are hard to express in code.
 * Such a border can be developed by a UI designer who supplies "parts" for every
 * part of the border. This allows a border to be composed of 8 different images
 * that can be replicated around a container.
 *
 * @author Shai Almog
 */
public class ImageBorder implements Border {
    private Icon topLeftCorner;
    private Icon topRightCorner;
    private Icon bottomLeftCorner;
    private Icon bottomRightCorner;
    private Icon top;
    private Icon bottom;
    private Icon left;
    private Icon right;
    
    public ImageBorder(Icon topLeftCorner, Icon topRightCorner, 
        Icon bottomLeftCorner, Icon bottomRightCorner, Icon top,
        Icon bottom, Icon left, Icon right) {
        if(topLeftCorner == null) {
            topLeftCorner = new NullIcon();
        }
            
        if(topRightCorner == null) {
            topRightCorner = new NullIcon();
        }

        if(bottomLeftCorner == null) {
            bottomLeftCorner = new NullIcon();
        }

        if(bottomRightCorner == null) {
            bottomRightCorner = new NullIcon();
        }

        if(left == null) {
            left = new NullIcon();
        }
            
        if(right == null) {
            right = new NullIcon();
        }

        if(top == null) {
            top = new NullIcon();
        }

        if(bottom == null) {
            bottom = new NullIcon();
        }
        this.topLeftCorner = topLeftCorner;
        this.topRightCorner = topRightCorner;
        this.bottomLeftCorner = bottomLeftCorner;
        this.bottomRightCorner = bottomRightCorner;
        this.left = left;
        this.right = right;
        this.top = top;
        this.bottom = bottom;
    }
    
    public ImageBorder(URL topLeftCorner, URL topRightCorner, 
        URL bottomLeftCorner, URL bottomRightCorner, URL top,
        URL bottom, URL left, URL right) {
        this(createIcon(topLeftCorner), createIcon(topRightCorner), 
            createIcon(bottomLeftCorner), createIcon(bottomRightCorner),
            createIcon(top), createIcon(bottom), createIcon(left), 
            createIcon(right));
    }

    public ImageBorder(String topLeftCorner, String topRightCorner, 
        String bottomLeftCorner, String bottomRightCorner, String top,
        String bottom, String left, String right) {
        this(createIcon(topLeftCorner), createIcon(topRightCorner), 
            createIcon(bottomLeftCorner), createIcon(bottomRightCorner),
            createIcon(top), createIcon(bottom), createIcon(left), 
            createIcon(right));
    }
    
    private static Icon createIcon(String icon) {
        if(icon == null) {
            return(null);
        }
        
        return(new ImageIcon(ImageBorder.class.getResource(icon)));
    }

    private static Icon createIcon(URL icon) {
        if(icon == null) {
            return(null);
        }
        
        return(new ImageIcon(icon));
    }
    
    public Insets getBorderInsets(Component c) {
        return(new Insets(top.getIconHeight(), left.getIconWidth(), 
            bottom.getIconHeight(), right.getIconWidth()));
    }
    
    public boolean isBorderOpaque() {
        return(false);
    }
    
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        topLeftCorner.paintIcon(c, g, x, y);
        topRightCorner.paintIcon(c, g, x + width - topRightCorner.getIconWidth(), y);
        bottomLeftCorner.paintIcon(c, g, x, y + height - bottomLeftCorner.getIconHeight());
        bottomRightCorner.paintIcon(c, g, x + width - bottomRightCorner.getIconWidth(), 
            y + height - bottomRightCorner.getIconHeight());
        
        if(!(top instanceof NullIcon)) {
            Graphics clipped = g.create(x + topLeftCorner.getIconWidth(), y, 
                width - topLeftCorner.getIconWidth() - topRightCorner.getIconWidth(), 
                this.top.getIconHeight());
            int widthInc = this.top.getIconWidth();
            for(int top = 0 ; top < width ; top += widthInc) {
                this.top.paintIcon(c, clipped, top, 0);
            }
        }

        if(!(bottom instanceof NullIcon)) {
            Graphics clipped = g.create(x + bottomLeftCorner.getIconWidth(), 
                y + height - bottom.getIconHeight(), 
                width - bottomLeftCorner.getIconWidth() - bottomRightCorner.getIconWidth(), 
                bottom.getIconHeight());
            int widthInc = this.bottom.getIconWidth();
            for(int bottom = 0 ; bottom < width ; bottom += widthInc) {
                this.bottom.paintIcon(c, clipped, bottom, 0);
            }
        }

        if(!(left instanceof NullIcon)) {
            Graphics clipped = g.create(x, 
                y + topLeftCorner.getIconHeight(), 
                this.left.getIconWidth(), 
                y + height - bottomLeftCorner.getIconHeight() - topLeftCorner.getIconHeight());
            int heightInc = this.left.getIconHeight();
            for(int left = 0 ; left < height ; left += heightInc) {
                this.left.paintIcon(c, clipped, 0, left);
            }
        }

        if(!(right instanceof NullIcon)) {
            Graphics clipped = g.create(x + width - this.right.getIconWidth(), 
                y + topRightCorner.getIconHeight(), 
                this.right.getIconWidth(), 
                y + height - bottomRightCorner.getIconHeight() - topRightCorner.getIconHeight());
            int heightInc = this.right.getIconHeight();
            for(int right = 0 ; right < height ; right += heightInc) {
                this.right.paintIcon(c, clipped, 0, right);
            }
        }
    }    
    
    static class NullIcon implements Icon {
        
        public int getIconHeight() {
            return(0);
        }
        
        public int getIconWidth() {
            return(0);
        }
        
        public void paintIcon(Component c, Graphics g, int x, int y) {
        }
        
    }
}
