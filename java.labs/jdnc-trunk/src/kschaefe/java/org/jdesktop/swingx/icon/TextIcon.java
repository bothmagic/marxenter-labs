/**
 * 
 */
package org.jdesktop.swingx.icon;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.lang.ref.WeakReference;

import javax.swing.Icon;
import javax.swing.JPanel;

/**
 *
 */
public class TextIcon implements Icon {
    public static final Font DEFAULT_FONT = new Font("Dialog", Font.PLAIN, 11);
    
    private static final Component DEAFULT_CACHE_COMPONENT = new JPanel();
    
    private String text;
    private Font font;
    private Color fontColor;
    private transient WeakReference<Component> cache;
    private transient int startY;
    private transient int iconHeight;
    private transient int iconWidth;
    
    public TextIcon() {
        this(null);
    }
    
    public TextIcon(String text) {
        this(text, DEFAULT_FONT);
    }
    
    public TextIcon(String text, Color fontColor) {
        this(text, DEFAULT_FONT, fontColor);
    }
    
    public TextIcon(String text, Font font) {
        this(text, font, Color.BLACK);
    }
    
    public TextIcon(String text, Font font, Color fontColor) {
        setText(text);
        setFont(font);
        this.fontColor = fontColor;
        
        calculateSize();
    }
    
    private void calculateSize() {
        //attempting to set sizes before ever receiving a paint request
        if (cache == null || cache.get() == null) {
            cache = new WeakReference<Component>(DEAFULT_CACHE_COMPONENT);
        }
        
        FontMetrics fm = cache.get().getFontMetrics(font);
        iconHeight = fm.getHeight();
        startY = fm.getAscent();
        iconWidth = fm.stringWidth(text);
    }
    
    private void setCache(Component c) {
        if (cache.get() == c) {
            return;
        }
        
        cache = new WeakReference<Component>(c);
        
        calculateSize();
    }
    
    /**
     * {@inheritDoc}
     */
    public int getIconHeight() {
        return iconHeight;
    }

    /**
     * {@inheritDoc}
     */
    public int getIconWidth() {
        return iconWidth;
    }

    /**
     * {@inheritDoc}
     */
    public void paintIcon(Component c, Graphics g, int x, int y) {
        setCache(c);
        
        //ensure that we do not mess up the original graphics
        Graphics2D g2 = (Graphics2D) g.create();
        
        try {
            g2.translate(x, y);
            g2.setFont(font);
            g2.setColor(fontColor);
            g2.drawString(text, 0, startY);
        } finally {
            g2.dispose();
        }
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text == null ? "" : text;
    }

    public Font getFont() {
        return font;
    }

    public void setFont(Font font) {
        this.font = font == null ? DEFAULT_FONT : font;
        
        calculateSize();
    }

    public Color getFontColor() {
        return fontColor;
    }

    public void setFontColor(Color fontColor) {
        this.fontColor = fontColor;
    }
}
