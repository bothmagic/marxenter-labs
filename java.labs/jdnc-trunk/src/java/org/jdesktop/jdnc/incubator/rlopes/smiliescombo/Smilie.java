/*
 * Smilie.java
 *
 * Created on 28. Februar 2005, 18:33
 */

package org.jdesktop.jdnc.incubator.rlopes.smiliescombo;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JComponent;

/**
 *
 * @author Thomas
 */
public class Smilie extends JComponent {
    
    protected String text;
    protected String filename;
    protected JComponent renderer;
    
    
    public Smilie(String lFilename, String lText) {
        this.text = lText;
        this.filename = lFilename;
        renderer = getNewRenderer();
    }
    
    public JComponent getCachedRenderer() {
        return renderer;
    }
    
    public JComponent getNewRenderer() {
        return new SmilieRenderer(filename, this);
    }
    
    public String toString() {
        return text;
    }
    
    public String getFilename() {
        return filename;
    }    
}

