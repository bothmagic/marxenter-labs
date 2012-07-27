/*
 * $Id: BackgroundPainter.java 985 2006-12-22 19:40:13Z xhanin $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.jexplose.core;

import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JComponent;

/**
 * paint logo
 */
public class BackgroundPainter implements Painter {
    private static final float ALPHA = 0.25f;
    private static final float ALPHA_STEP = 0.025f;
    
    private static final double FITTING = 0.80;

    private static ImageIcon _logo;
    private static int _width;
    private static int _height;
    private static URL _backgroundURL = BackgroundPainter.class.getResource("logo.gif");
    
    private ExploseLayout _layout;
    private JComponent _comp;

    private int _x;
    private int _y;
    
    private BufferedImage _buffer;
    
    private double _scale; // scaling of the logo to fit about 70% of the desktop
    private transient boolean _bufferReady;
    private transient float _alpha;
    private transient boolean _disposed = false;

    public BackgroundPainter(ExploseLayout layout) {
        _layout = layout;
        if (_logo == null || _width == 0 || _height == 0) {
            URL resource = getBackgroundURL();
            if (resource != null) {
                _logo = new ImageIcon(resource);
                
                _width = _logo.getIconWidth();
                _height = _logo.getIconHeight();
            }
        }
    }

    private static URL getBackgroundURL() {
        return _backgroundURL;
    }

    public void prepare(JComponent comp) {
        _disposed = false;
        _comp = comp;
        
        _scale = Math.min(
                _comp.getWidth() * FITTING / _width,
                _comp.getHeight() * FITTING / _height);
        
        _x = _comp.isOpaque() ? 0 : (int)(_layout.getDesktopOffset().x*_layout.getScale());
        _y = _comp.isOpaque() ? 0 : (int)(_layout.getDesktopOffset().y*_layout.getScale());

        _x += (_comp.getWidth() - _width * _scale) / 2;
        _y += (_comp.getHeight() - _height * _scale) / 2;
        

        _bufferReady = false;
        Thread t = new Thread() {
            public void run() {
                if (_logo != null) {
                    try {
                        sleep(300);
                    } catch (InterruptedException e) {
                        return;
                    }
                    if (_disposed) {
                        return;
                    }
                    _alpha = 0f;
                    _buffer = _comp.getGraphicsConfiguration().createCompatibleImage((int)(_width * _scale), (int)(_height * _scale), Transparency.TRANSLUCENT);
                    Graphics2D g = (Graphics2D)_buffer.getGraphics();
                    g.scale(_scale, _scale);
                    g.drawImage(_logo.getImage(), 0, 0, null);
                    _bufferReady = true;
                    while (_alpha < ALPHA) {
                        _alpha += ALPHA_STEP;
                        try {
                            sleep(50);
                            if (_disposed) {
                                return;
                            }
                            _comp.repaint();
                        } catch (InterruptedException e) {
                            return;
                        }
                    }
                }
            }
        };
        t.setPriority(Thread.MIN_PRIORITY);
        t.start();
    }

    public void paint(Graphics g) {
        if (_bufferReady) {
            Graphics2D g2 = (Graphics2D)g;
            
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, _alpha));

            g2.drawImage(_buffer, _x, _y, null);
        }
    }

    public void dispose() {
        _disposed = true;
    }
    
    public static void setBackgroundURL(URL url) {
        _logo = null;
        _backgroundURL = url;
    }
}
