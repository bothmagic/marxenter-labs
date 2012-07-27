package org.jdesktop.jdnc.incubator.vprise.candy;

import javax.swing.*;
import java.awt.Graphics;
import java.awt.MediaTracker;
import java.awt.Image;
import java.awt.LayoutManager;
import java.net.*;

/**
 * This class should replace JPanel where ever possible to allow skinning of UI elements
 * more easily without using overly complex UI tricks to reach this goal.
 *
 * @author Shai Almog
 */
public class BackgroundPanel extends JPanel {
    private Paintable paintable;
    public BackgroundPanel(Paintable paintable) {
        this.paintable = paintable;
    }

    public BackgroundPanel(LayoutManager l, Paintable paintable) {
        super(l);
        this.paintable = paintable;
    }

    public BackgroundPanel(LayoutManager l) {
        super(l);
    }
    
    public BackgroundPanel() {
    }
    
    public void setPaintable(Paintable paintable) {
        this.paintable = paintable;
        repaint();
    }

    public void paint(Graphics g) {
        if(isOpaque()) {
            g.setColor(getBackground());
            g.fillRect(0, 0, getWidth(), getHeight());
        }
        if(paintable != null) {
            paintable.paint(this, g, 0, 0, getWidth(), getHeight(), isOpaque());
        }
        super.paintChildren(g);
    }
}

