/*
 * $Id: LayoutStandardPainter.java 985 2006-12-22 19:40:13Z xhanin $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.jexplose.core;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.RepaintManager;

/**
 * Used to paint a layout in an effect panel, using direct iframes paint
 */
public class LayoutStandardPainter implements Painter {
    private ExploseLayout _layout;
    private double _scale;
    private Dimension _dim;
    private boolean _prepared = false;
    private JDesktopPane _desktop;
    private static BufferedImage _buffer;

    /**
     * @param layout
     */
    public LayoutStandardPainter(JDesktopPane desktop, ExploseLayout layout, double scale) {
        _desktop = desktop;
        _layout = layout;
        _scale = scale;
        _dim = new Dimension(_layout.getFinalDimension());
        _dim.width *= _scale;
        _dim.height *= _scale;
    }

    public void prepare(JComponent comp) {
    }
    
    public void paint(Graphics g) {
        long start = System.currentTimeMillis();
        Graphics2D g2 = (Graphics2D)g;
        List thumbs = _layout.getThumbs();
        if (_dim.width > 0 && _dim.height > 0) {
            synchronized(_layout) {
            double scale = _layout.getScale();
            g2.scale(scale, scale);
            
            RepaintManager repaintManager = RepaintManager.currentManager(null);
            boolean doubleBuffered = repaintManager.isDoubleBufferingEnabled();
            repaintManager.setDoubleBufferingEnabled(false);
            for (int i = thumbs.size() -1; i>=0; i--) {
                Thumbnail thumb = (Thumbnail)thumbs.get(i);
                if (thumb == _layout.getSelected()) {
                    continue;
                }
                synchronized(thumb) {
                g.translate(thumb.x + _layout.getTotalOffset().x, thumb.y + _layout.getTotalOffset().y);
                thumb.getIFrame().paint(g);
                g.translate(-(thumb.x + _layout.getTotalOffset().x), -(thumb.y + _layout.getTotalOffset().y));
                }
            }
            Thumbnail thumb = _layout.getSelected();
            if (thumb != null) {
                g.translate(thumb.x + _layout.getTotalOffset().x, thumb.y + _layout.getTotalOffset().y);
                thumb.getIFrame().paint(g);
                g.translate(-(thumb.x + _layout.getTotalOffset().x), -(thumb.y + _layout.getTotalOffset().y));
            }
            repaintManager.setDoubleBufferingEnabled(doubleBuffered);
            g.dispose();
            }
        }
        long duration = System.currentTimeMillis() - start;
//        System.out.println("painting took "+duration+" ms");
    }
    public void dispose() {
    }
}
