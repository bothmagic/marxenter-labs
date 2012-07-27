/*
 * $Id: ExploseLayout.java 985 2006-12-22 19:40:13Z xhanin $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.jexplose.core;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;

/**
 * Represents the layout information of all an explosed desktop
 */
public class ExploseLayout {
    // all coordinates and dimensions are not scaled 
    private Dimension _dim = new Dimension(0,0);
    private List _thumbs = new ArrayList();

    private double _scale;
    private Point _desktopOffset = new Point();
    private Point _centerOffset = new Point();
    private Point _totalOffset = new Point();

    private Thumbnail _selected;
    private boolean _isFinal;
    public boolean moved;
    private Dimension _finalDim = new Dimension(0,0);
    private JDesktopPane _desktop;

    public ExploseLayout(JDesktopPane desktop) {
        _desktop = desktop;
    }
    
    public Thumbnail place(JInternalFrame iframe, int x, int y) {
        Thumbnail thumb = new Thumbnail(iframe, x, y);
        _finalDim.width = Math.max(_finalDim.width, x + thumb.width);
        _finalDim.height = Math.max(_finalDim.height, y + thumb.height);
        _thumbs.add(thumb);
        return thumb;
    }
    
    public void computeDimension() {
        _dim.width = 0;
        _dim.height = 0;
        for (ListIterator it = _thumbs.listIterator(); it.hasNext();) {
            Thumbnail thumb = (Thumbnail)it.next();
            _dim.width = Math.max(_dim.width, thumb.x + thumb.width);
            _dim.height = Math.max(_dim.height, thumb.y + thumb.height);
        }
    }

    /**
     * @param point scaled coordinates point with offset
     * @return the thumbnail present under the given point, null if not found
     */
    public Thumbnail getThumbnailAt(Point point) {
        int x = (int)(((point.x) / _scale) - _totalOffset.x);
        int y = (int)(((point.y) / _scale) - _totalOffset.y);
        for (ListIterator it = _thumbs.listIterator(); it.hasNext();) {
            Thumbnail thumb = (Thumbnail)it.next();
            if (thumb.contains(x, y)) {
                return thumb;
            }
        }
        return null;
    }

    public void dispose() {
        _thumbs.clear();
    }

    public void updateScale() {
        Dimension size = _desktop.getSize();
        double scale = computeScale(_dim, size);
        updateScale(scale, computeOffset(size, scale, _dim));
    }
    
    public Point getFinalOffset() {
        return computeOffset(_desktop.getSize(), getFinalScale(), getFinalDimension());
    }

    private Point computeOffset(Dimension size, double scale, Dimension target) {
        return new Point(
                (int)((size.width / scale) - target.width) / 2, 
                (int)((size.height / scale) - target.height) / 2);
    }
    
    public void updateScale(double scale, Point offset) {
        _scale = scale;
        
        _desktopOffset.x = 0;
        _desktopOffset.y = 0;
        Component c = _desktop;
        while (c != _desktop.getRootPane()) {
            _desktopOffset.x += c.getX();
            _desktopOffset.y += c.getY();
            c = c.getParent();
        }
        _desktopOffset.x = (int)(_desktopOffset.x / _scale);
        _desktopOffset.y = (int)(_desktopOffset.y / _scale);

        _centerOffset.x = offset.x;
        _centerOffset.y = offset.y;
        
        _totalOffset.x = _desktopOffset.x + _centerOffset.x;
        _totalOffset.y = _desktopOffset.y + _centerOffset.y;
    }
    
    public double computeScale(Dimension from, Dimension to) {
        return Math.min(Math.min((double)(to.width - 24) / from.width, (double)(to.height - 24) / from.height), 1.0);
    }
    
    public synchronized void setFinal(boolean fin) {
        if (fin) {
            for (ListIterator it = _thumbs.listIterator(); it.hasNext();) {
                Thumbnail thumb = (Thumbnail)it.next();
                synchronized (thumb) {
                    thumb.setLocation(thumb.getFinalLocation());
                }
            }
            moved = true;
        }
        _isFinal = fin;
    }
    
    public boolean isFinal() {
        return _isFinal;
    }

    public boolean select(Thumbnail selected) {
        boolean selChanged = _selected != selected;
        _selected = selected;
        return selChanged;
    }

    public Dimension getDimension() {
        return _dim;
    }

    public double getScale() {
        return _scale;
    }

    public List getThumbs() {
        return _thumbs;
    }

    public Thumbnail getSelected() {
        return _selected;
    }
    public Point getCenterOffset() {
        return _centerOffset;
    }
    public Point getDesktopOffset() {
        return _desktopOffset;
    }
    public Point getTotalOffset() {
        return _totalOffset;
    }

    public Dimension getFinalDimension() {
        return _finalDim;
    }

    public double getFinalScale() {
        return computeScale(_finalDim, _desktop.getSize());
    }
}
