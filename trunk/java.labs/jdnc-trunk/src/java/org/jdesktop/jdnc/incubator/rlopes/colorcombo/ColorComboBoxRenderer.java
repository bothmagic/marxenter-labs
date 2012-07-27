/*
 * ColorComboBoxRenderer.java
 *
 * Created on 24 de Fevereiro de 2005, 22:23
 */

package org.jdesktop.jdnc.incubator.rlopes.colorcombo;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;


/**
 *
 * @author  Ricardo Lopes
 *
 */
public class ColorComboBoxRenderer extends DefaultListCellRenderer {
    
    protected static final String RENDERER_IMAGE_FILE = "/org/jdesktop/jdnc/incubator/rlopes/colorcombo/resources/color_fill.png";
    protected Renderer renderer;
    protected Image image;
    
    public ColorComboBoxRenderer() {
        renderer = new Renderer();
        
        URL url = ColorComboBoxRenderer.class.getResource(RENDERER_IMAGE_FILE);
        if (url != null) {
            try {
                image = ImageIO.read(url);
                if (image != null) {
                    return;
                }
            } catch (IOException ioe) { 
                System.err.println(ioe.getMessage());
            }
        }    
        System.err.println("Error loading image :" + url);
        image = new BufferedImage(16, 16, BufferedImage.TYPE_INT_RGB);
    }
    
    public Component getListCellRendererComponent(JList list, Object value, int index,
                                       boolean isSelected, boolean cellHasFocus) {
                                           
        renderer.setColor((Color) value);
        return renderer;
    }
    
    
    class Renderer extends Component {
        
        public void setColor(Color color) {
            this.color = color;
        }
        
        public Color getColor() {
            return color;
        }
        
        public void paint(Graphics g) {
            g.setColor((Color) color);
            g.fillRect(1, 19, 23, 5);
            g.setColor(Color.darkGray);
            g.draw3DRect(1, 19, 23, 5, true);
            g.drawImage(image, 4, 1, this);
        }
        
        private Color color;
    }
}

