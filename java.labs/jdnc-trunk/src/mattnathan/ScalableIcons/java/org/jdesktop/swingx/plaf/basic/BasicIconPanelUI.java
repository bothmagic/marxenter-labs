/*
 * $Id: BasicIconPanelUI.java 2643 2008-08-06 09:35:00Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.plaf.basic;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import javax.swing.Icon;
import javax.swing.JComponent;

import org.jdesktop.swingx.JXIconPanel;
import org.jdesktop.swingx.LookAndFeelUtilities;
import org.jdesktop.swingx.icon.IconUtilities;
import org.jdesktop.swingx.plaf.IconPanelUI;

/**
 * Provides the UI delegate for an JXIconPanel.
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 */
public class BasicIconPanelUI extends IconPanelUI {

    private static BasicIconPanelUI SINGLETON = null; // singleton instance of this UI

    public static BasicIconPanelUI createUI(JComponent c) {
        if (SINGLETON == null) {
            SINGLETON = new BasicIconPanelUI();
        }
        return SINGLETON;
    }





    public BasicIconPanelUI() {
        super();
    }





    /**
     * Returns the specified component's maximum size appropriate for the look and feel.
     *
     * @param c the component whose maximum size is being queried; this argument is often ignored, but might be used
     *   if the UI object is stateless and shared by multiple components
     * @return a <code>Dimension</code> object or <code>null</code>
     */
    @Override
    public Dimension getMaximumSize(JComponent c) {
        return LookAndFeelUtilities.addInsets(c, IconUtilities.getMaximumSize(c, ((JXIconPanel) c).getIcon()));
    }





    /**
     * Returns the specified component's minimum size appropriate for the look and feel.
     *
     * @param c the component whose minimum size is being queried; this argument is often ignored, but might be used
     *   if the UI object is stateless and shared by multiple components
     * @return a <code>Dimension</code> object or <code>null</code>
     */
    @Override
    public Dimension getMinimumSize(JComponent c) {
        return LookAndFeelUtilities.addInsets(c, IconUtilities.getMinimumSize(c, ((JXIconPanel) c).getIcon()));
    }





    /**
     * Returns the specified component's preferred size appropriate for the look and feel.
     *
     * @param c the component whose preferred size is being queried; this argument is often ignored, but might be
     *   used if the UI object is stateless and shared by multiple components
     * @return Dimension
     */
    @Override
    public Dimension getPreferredSize(JComponent c) {
        return LookAndFeelUtilities.addInsets(c, IconUtilities.getPreferredSize(c, ((JXIconPanel) c).getIcon()));
    }





    /**
     * Overriden to paint the child icon if not null;
     *
     * @param g The graphics to paint to.
     * @param c The component to paint.
     */
    @Override
    public void paint(Graphics g, JComponent c) {
        JXIconPanel ip = (JXIconPanel) c;
        Icon icon = ip.getIcon();
        if (icon != null) {
            Insets in = c.getInsets();
            IconUtilities.paintChild(icon, c, g, in.left, in.top, c.getWidth() - in.left - in.right, c.getHeight() - in.top - in.bottom,
                                     ip.getLocationPolicy(), ip.getScalePolicy());
        }
    }
}
