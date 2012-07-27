/*
 * $Id: LayoutExplosablePainter.java 985 2006-12-22 19:40:13Z xhanin $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.jexplose;

import java.awt.Graphics;
import java.awt.Point;
import java.util.ListIterator;

import javax.swing.JComponent;

import org.jdesktop.swingx.jexplose.core.ExploseLayout;
import org.jdesktop.swingx.jexplose.core.Painter;
import org.jdesktop.swingx.jexplose.core.Thumbnail;


/**
 * Used to "paint" thumbnails in an explosable - For internal use only
 */
class LayoutExplosablePainter implements Painter {
    private Explosable _explosable;
    private ExploseLayout _layout;

    public LayoutExplosablePainter(ExploseLayout layout, Explosable explosable) {
        _layout = layout;
        _explosable = explosable;
    }

    public void prepare(JComponent comp) {
    }

    public void paint(Graphics g) {
        // here we actually do not paint anything in the effect panel, but instead
        // update the explosable
        Point offset = _layout.getCenterOffset();
        if (_layout.moved) {
            for (ListIterator it = _layout.getThumbs().listIterator(); it.hasNext();) {
                Thumbnail thumb = (Thumbnail)it.next();
                // here we the location using layout information (thumb position + offset)
                // and we shift downside of the desktop height, to ensure that no i frame
                // has coordinates visible in the non scaled coordinates system.
                // this prevent flicking of the corresponding area
                // this shift is counter balanced by the explosable itself
                thumb.getIFrame().setLocation(thumb.x + offset.x, thumb.y + offset.y + _explosable.getDesktop().getHeight());
            }
            _explosable.setScale(_layout.getScale());
            _layout.moved = false;
        }
    }

    public void dispose() {
    }

}
