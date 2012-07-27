/*
 * $Id: Thumbnail.java 985 2006-12-22 19:40:13Z xhanin $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.jexplose.core;

import java.awt.Point;
import java.awt.Rectangle;
import java.beans.PropertyVetoException;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JInternalFrame;
import javax.swing.SwingUtilities;

/**
 * Represents the layout information about an explosed internal frame.
 */
public class Thumbnail extends Rectangle {
    private JInternalFrame _iframe;
    private Point _originalLocation;
    private Point _finalLocation;
    private boolean _isIcon;

    public Thumbnail(JInternalFrame iframe, int x, int y) {
        super(iframe.getBounds());
        _iframe = iframe;
        _originalLocation = iframe.getLocation();
        _finalLocation = new Point(x, y);
        _isIcon = iframe.isIcon();
        if (_isIcon) {
            Runnable runnable = new Runnable() {
                public void run() {
                    try {
                        _iframe.setIcon(false);
                    } catch (PropertyVetoException e) {
                    }
                }
            };
            if (SwingUtilities.isEventDispatchThread()) {
                runnable.run();
            } else {
                try {
                    SwingUtilities.invokeAndWait(runnable);
                } catch (InterruptedException e) {
                } catch (InvocationTargetException e) {
                }
            }
        }
    }

    public JInternalFrame getIFrame() {
        return _iframe;
    }
    public Point getOriginalLocation() {
        return _originalLocation;
    }
    public Point getFinalLocation() {
        return _finalLocation;
    }
    public void restore() {
        if (_isIcon) {
            Runnable runnable = new Runnable() {
                public void run() {
                    try {
                        _iframe.setIcon(true);
                    } catch (PropertyVetoException e) {
                    }
                }
            };
            if (SwingUtilities.isEventDispatchThread()) {
                runnable.run();
            } else {
                try {
                    SwingUtilities.invokeAndWait(runnable);
                } catch (InterruptedException e) {
                } catch (InvocationTargetException e) {
                }
            }
        }
    }
}
