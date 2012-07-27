/*
 * $Id: RenderingPanel.java 3178 2009-07-01 08:03:18Z kleopatra $
 *
 * Copyright 2008 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */
package rasto1968.checklist;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Point;
import java.util.logging.Logger;

import javax.swing.JComponent;
import javax.swing.JLabel;

import org.jdesktop.swingx.JXPanel;

/**
 * Compound component provider.
 */
// TODO this is probably not the correct location for HotSpotAware, but it's a start
// Having HotSpotAware here does not make it generic, unless we add some kind of
// HotSpotAwareResolver or similar
public class RenderingPanel extends JXPanel implements HotSpotAware {
    
    @SuppressWarnings("unused")
    private static final Logger LOG = Logger.getLogger(RenderingPanel.class
            .getName());
    JComponent mainDelegate;

    JComponent leadingDelegate;

    String mainPosition = BorderLayout.CENTER; // 2;

    String leadingPosition = BorderLayout.LINE_START;

    /**
     * 
     */
    public RenderingPanel() {
        setOpaque(true);
        setBorder(null);
        setLayout(new BorderLayout(new JLabel().getIconTextGap(), 20));
    }

    public void setMainDelegate(JComponent comp) {
        if (mainDelegate != null) {
            remove(mainDelegate);
        }
        mainDelegate = comp;
        if (mainDelegate != null) {
            add(mainDelegate, mainPosition);
        }
        validate();
    }

    public void setLeadingDelegate(JComponent comp) {
        if (leadingDelegate != null) {
            remove(leadingDelegate);
        }
        leadingDelegate = comp;
        if (comp != null) {
            add(leadingDelegate, leadingPosition);
        }
        validate();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setBackground(Color bg) {
        super.setBackground(bg);
        if (leadingDelegate != null) {
            leadingDelegate.setBackground(bg);
        }
        if (mainDelegate != null) {
            mainDelegate.setBackground(bg);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setForeground(Color bg) {
        super.setForeground(bg);
        if (leadingDelegate != null) {
            leadingDelegate.setForeground(bg);
        }
        if (mainDelegate != null) {
            mainDelegate.setForeground(bg);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isHotSpot(Point pt) {
        // JW - contains expects component coordinate system, that is needs to 
        // be translated to work in LToR
//        LOG.info("point: " + pt + "/" + leadingDelegate.getBounds() + leadingDelegate.contains(pt));
        pt.translate(- leadingDelegate.getX(), - leadingDelegate.getY());
        return leadingDelegate.contains(pt);
    }
}
