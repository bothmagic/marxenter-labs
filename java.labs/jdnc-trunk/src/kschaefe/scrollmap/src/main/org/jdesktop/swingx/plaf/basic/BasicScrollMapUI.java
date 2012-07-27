/*
 * $Id: BasicScrollMapUI.java 3296 2010-08-03 17:52:57Z kschaefe $
 *
 * Copyright 20102006 Sun Microsystems, Inc., 4150 Network Circle,
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
package org.jdesktop.swingx.plaf.basic;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.CellRendererPane;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.LookAndFeel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;

import org.jdesktop.swingx.JXScrollMap;
import org.jdesktop.swingx.SwingXUtilities;
import org.jdesktop.swingx.plaf.ScrollMapUI;

/**
 * 
 * @author kschaefer
 */
public class BasicScrollMapUI extends ScrollMapUI {
    private class Handler implements MouseListener {
        private Point startLocation;

        /**
         * {@inheritDoc}
         */
        @Override
        public void mousePressed(MouseEvent e) {
            JViewport vp = scrollMap.getViewport();
            
            if (vp != null && vp.getView() != null) {
                //be safe and hide the popup first
                if (popup.isVisible()) {
                    popup.hide();
                }
                
                startLocation = e.getLocationOnScreen();
                popup.show();
            }
        }
        
        /**
         * {@inheritDoc}
         */
        @Override
        public void mouseReleased(MouseEvent e) {
            if (popup.isVisible()) {
                popup.hide();
                
                if (BasicMouseUtilities.canMoveMouse()) {
                    BasicMouseUtilities.moveMouseTo(startLocation);
                }
            }
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void mouseClicked(MouseEvent e) { }

        /**
         * {@inheritDoc}
         */
        @Override
        public void mouseEntered(MouseEvent e) { }

        /**
         * {@inheritDoc}
         */
        @Override
        public void mouseExited(MouseEvent e) { }
    }
    
    /**
     * The scroll map managed by this UI delegate.
     */
    protected JXScrollMap scrollMap;
    
    /**
     * The popup to display for the scroll map.
     */
    protected ScrollMapPopup popup;
    
    protected CellRendererPane rendererPane;
    
    protected MouseListener mouseListener;

    private Handler handler;
    
    /**
     * Returns a new instance of BasicScrollMapUI. BasicScrollMapUI delegates are allocated one per
     * JXScrollMap.
     * 
     * @return A new ScrollMapUI implementation for the Basic look and feel.
     */
    public static ComponentUI createUI(JComponent c) {
        return new BasicScrollMapUI();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void installUI(JComponent c) {
        scrollMap = (JXScrollMap) c;
   
        installDefaults();
        
        popup = createDefaultPopup();
        
        installComponents();
        
        installListeners();
    }
    
    protected void installDefaults() {
        LookAndFeel.installColorsAndFont(scrollMap, "ScrollMap.background", "ScrollMap.foreground",
                "ScrollMap.font");
        LookAndFeel.installBorder(scrollMap, "ScrollMap.border");
        LookAndFeel.installProperty(scrollMap, "opaque", true);
        scrollMap.setLightWeightPopupEnabled(true);
        scrollMap.setFocusable(false);
        
        Icon icon = scrollMap.getIcon();
        
        if (SwingXUtilities.isUIInstallable(icon)) {
            scrollMap.setIcon(UIManager.getIcon("ScrollMap.icon"));
        }
        
        Dimension maxPopupSize = scrollMap.getMaximumPopupSize();
        
        if (SwingXUtilities.isUIInstallable(maxPopupSize)) {
            scrollMap.setMaxPopupSize(UIManager.getDimension("ScrollMap.maxPopupSize"));
        }
    }
    
    /**
     * Creates the popup used by this scroll map UI implementation.
     * 
     * @return the popup to use
     */
    protected ScrollMapPopup createDefaultPopup() {
        return new BasicScrollMapPopup(scrollMap);
    }
    
    protected void installComponents() {
        rendererPane = new CellRendererPane();
        scrollMap.add(rendererPane);
    }
    
    protected void installListeners() {
        if ((mouseListener = createMouseListener()) != null) {
            scrollMap.addMouseListener(mouseListener);
        }
    }
    
    /**
     * Returns the shared listener.
     */
    private Handler getHandler() {
        if (handler == null) {
            handler = new Handler();
        }
        return handler;
    }
    
    protected MouseListener createMouseListener() {
        return getHandler();
    } 

    /**
     * {@inheritDoc}
     */
    @Override
    public void uninstallUI(JComponent c) {
        popup.hide();
        popup.uninstallingUI();

        uninstallListeners();
        uninstallDefaults();
        
        mouseListener = null;
        popup = null;
        scrollMap = null;
    }
    
    protected void uninstallListeners() {
        if (mouseListener != null) {
            scrollMap.removeMouseListener(mouseListener);
        }
    }
    
    protected void uninstallDefaults() {
        LookAndFeel.uninstallBorder(scrollMap);
        LookAndFeel.installColorsAndFont(scrollMap, "ScrollMap.background", "ScrollMap.foreground",
                "ScrollMap.font");
    }

    /**
     * Determines if this should only paint the icon.
     * <p>
     * Most implementations will only paint the icon when the {@code JXScrollMap} is a child of a
     * {@link JScrollPane}.
     * 
     * @return {@code true} if the implementation should only paint the icon; {@code false}
     *         otherwise
     */
    protected boolean isIconOnly() {
        return scrollMap.getParent() instanceof JScrollPane;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void paint(Graphics g, JComponent c) {
        if (isIconOnly()) {
            Icon icon = scrollMap.getIcon();
            
            if (icon != null) {
                icon.paintIcon(scrollMap, g, (scrollMap.getWidth() - icon.getIconWidth()) >> 1,
                        (scrollMap.getHeight() - icon.getIconHeight()) >> 1);
            }
        } else {
            String text = scrollMap.getText();
            int alignment = text == null || text.isEmpty() ? SwingConstants.CENTER : SwingConstants.LEADING;
            JLabel label = new JLabel(scrollMap.getText(), scrollMap.getIcon(), alignment);
            SwingUtilities.paintComponent(g, label, scrollMap, 0, 0, scrollMap.getWidth(), scrollMap.getHeight());
        }
    }
}
