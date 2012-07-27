/*
 * $Id: WaitPainter.java 985 2006-12-22 19:40:13Z xhanin $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.jexplose.core;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;

import javax.swing.JComponent;
import javax.swing.Timer;

/**
 * For internal use only
 */
public class WaitPainter implements Painter {
    private int _nbTrace = 4;
    private int _nbStep = 10;

    private JComponent _comp;
    int _step = 0;
    final Timer _timer = new Timer(40, new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            _step++;
            _step%=_nbStep;
            _comp.repaint();
        }
    });

    public WaitPainter() {
    }
    
    public void prepare(JComponent comp) {
        _comp = comp;
        _timer.start();
    }
    public void dispose() {
        _timer.stop();
    }

    public void paint(Graphics g) {
        for (int i = 0; i < _nbTrace; i++) {
            drawRect((Graphics2D)g, (_step-i)%_nbStep , new Color(255 - (i * (200 / _nbTrace))));
        }
    }
    private void drawRect(Graphics2D g2, int s, Color color) {
        AffineTransform t = g2.getTransform();
        g2.translate((_comp.getWidth() / 2), (_comp.getHeight() / 2));
        g2.rotate(((2 * Math.PI) / _nbStep)*s);
        g2.translate(0, - 100);
        g2.setColor(color);
        g2.fillRect(-5, -15, 10, 30);
        g2.setTransform(t);
    }


}
