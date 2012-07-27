/*
 * $Id: JXScrollMap.java 3296 2010-08-03 17:52:57Z kschaefe $
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
package org.jdesktop.swingx;

import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JViewport;

import org.jdesktop.swingx.plaf.LookAndFeelAddons;
import org.jdesktop.swingx.plaf.ScrollMapAddon;
import org.jdesktop.swingx.plaf.ScrollMapUI;
import org.jdesktop.swingx.util.Contract;

/**
 * {@code JXScrollMap} provides another method of controlling the viewport location for a {@code
 * JScrollPane}. The scroll map provides a small image or "mini-map" of the viewport contents. By
 * moving the mouse over the map, you can adjust the viewport to display the selected region.
 * 
 * @author Karl George Schaefer
 * @author weebib (original ScrollPaneSelector)
 */
public class JXScrollMap extends JComponent {
    private class ViewportSynchronizer implements PropertyChangeListener {
        /**
         * {@inheritDoc}
         */
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            setViewport((JViewport) evt.getNewValue());
        }
    }
    
    /**
     * The UI class ID constant.
     */
    public static final String uiClassID = "scrollMap";
    
    private JViewport viewport;
    
    private ViewportSynchronizer synchronizer;
    
    private String text;
    
    private Icon icon;

    private Dimension maxPopupSize;
    
    private boolean lightWeightPopupEnabled;
    
    private boolean synchronizedScrolling;
    
    // ensure at least the default ui is registered
    static {
        LookAndFeelAddons.contribute(new ScrollMapAddon());
    }
    
    /**
     * Creates a scroll map with no viewport to scroll.
     */
    public JXScrollMap() {
        this(null);
    }

    /**
     * Creates a scroll map for the specified viewport.
     * 
     * @param viewport
     *            the viewport to manage; may be {@code null}
     */
    public JXScrollMap(JViewport viewport) {
        setViewport(viewport);
        updateUI();
    }
    
    private ViewportSynchronizer getsSynchronizer() {
        if (synchronizer == null) {
            synchronizer = new ViewportSynchronizer();
        }
        
        return synchronizer;
    }

    /**
     * If this {@code JXScrollMap} is the child of a {@link JScrollPane} (the usual situation),
     * configure this scroll map to maintain the scroll pane's {@code viewport} as this map's
     * {@code viewport}. When a {@code JXScrollMap} is added to a {@code JScrollPane} in the usual
     * way, {@link #addNotify} is called in the {@code JXScrollMap}. {@code JXScrollMap}'s {@code
     * addNotify} method in turn calls this method, which is protected so that this default
     * installation procedure can be overridden by a subclass.
     * 
     * @see #addNotify
     */
    protected void configureEnclosingScrollPane() {
        if (getParent() instanceof JScrollPane) {
            JScrollPane sp = (JScrollPane) getParent();
            setViewport(sp.getViewport());
            
            getParent().addPropertyChangeListener("viewport", getsSynchronizer());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addNotify() {
        super.addNotify();
        
        configureEnclosingScrollPane();
    }

    /**
     * Reverses the effect of {@code configureEnclosingScrollPane} by removing the synchronization
     * with the scroll pane's viewport. {@code JXScrollMap}'s {@code removeNotify} method calls this
     * method, which is protected so that this default uninstallation procedure can be overridden by
     * a subclass.
     * 
     * @see #removeNotify
     * @see #configureEnclosingScrollPane
     */
    protected void unconfigureEnclosingScrollPane() {
        if (getParent() instanceof JScrollPane) {
            getParent().removePropertyChangeListener("viewport", getsSynchronizer());
            synchronizer = null;
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void removeNotify() {
        unconfigureEnclosingScrollPane();
        
        super.removeNotify();
    }

    /**
     * Get the viewport managed by this scroll map.
     * <p>
     * Note: this value if guaranteed to be in sync with a {@link JScrollPane}'s viewport, if this
     * is a child of a {@code JScrollPane}.
     * 
     * @return the currently managed viewport
     * @see #setViewport(JViewport)
     * @see #configureEnclosingScrollPane()
     */
    public JViewport getViewport() {
        return viewport;
    }

    /**
     * The viewport to manage.
     * 
     * @param viewport
     *            the viewport to manage
     * @throws IllegalArgumentException
     *             if the parent is a scrollpane and the specified viewport is not the viewport
     *             managed by the parent scroll pane
     * @see #configureEnclosingScrollPane()
     */
    public void setViewport(JViewport viewport) {
        if (getParent() instanceof JScrollPane && ((JScrollPane) getParent()).getViewport() != viewport) {
            throw new IllegalArgumentException("invalid viewport");
        }
        
        JViewport oldValue = getViewport();
        this.viewport = viewport;
        firePropertyChange("viewport", oldValue, getViewport());
    }

    /**
     * The text to display.
     * <p>
     * Note the default implementation does not display text when the scroll map is a child of a
     * {@code JScrollPane}.
     * 
     * @return the text
     */
    public String getText() {
        return text;
    }

    /**
     * Sets the displayed text.
     * <p>
     * Note the default implementation does not display text when the scroll map is a child of a
     * {@code JScrollPane}.
     * 
     * @param text
     *            the text
     */
    public void setText(String text) {
        String oldValue = getText();
        this.text = text;
        firePropertyChange("text", oldValue, getText());
    }
    
    /**
     * The icon to display.
     * 
     * @return the icon
     */
    public Icon getIcon() {
        return icon;
    }

    /**
     * The new icon.
     * 
     * @param icon
     *            the icon
     */
    public void setIcon(Icon icon) {
        Icon oldValue = getIcon();
        this.icon = icon;
        firePropertyChange("icon", oldValue, getIcon());
    }

    /**
     * Determines whether scrolling in the scroll map is synchronize with the viewport. When this
     * returns {@code true}, the viewport will update as the user scrolls in the scroll map.
     * 
     * @return {@code true} to scroll the viewport as the scroll map moves; {@code false} to scroll
     *         once the scroll map movement is complete
     */
    public boolean isSynchronizedScrolling() {
        return synchronizedScrolling;
    }

    /**
     * Sets the scroll map to use synchronized scrolling. If the scrolling is synchronized, then
     * moving the mouse over the scroll map's popup will cause the viewport location to update.
     * <p>
     * The default implementation uses security sensitive mechanisms to place the mouse at the
     * currently correct position for the viewport location. If you are using the scroll map in an
     * insecure environment, it is recommended that you do not synchronize scrolling.
     * 
     * @param synchronizedScrolling
     *            {@code true} to synchronize the viewport location in real time; {@code false} to
     *            update once user interaction is complete
     */
    public void setSynchronizedScrolling(boolean synchronizedScrolling) {
        boolean oldValue = isSynchronizedScrolling();
        this.synchronizedScrolling = synchronizedScrolling;
        firePropertyChange("synchronizedScrolling", oldValue, isSynchronizedScrolling());
    }

    /**
     * Gets the value of the {@code lightWeightPopupEnabled} property.
     * 
     * @return the value of the {@code lightWeightPopupEnabled} property
     * @see #setLightWeightPopupEnabled
     */
    public boolean isLightWeightPopupEnabled() { 
        return lightWeightPopupEnabled;
    }

    /**
     * Sets the {@code lightWeightPopupEnabled} property, which provides a hint as to whether or not
     * a lightweight {@code Component} should be used to contain the {@code JXScrollMap}, versus a
     * heavyweight {@code Component} such as a {@code Panel} or a {@code Window}. The decision of
     * lightweight versus heavyweight is ultimately up to the {@code JXScrollMap}. Lightweight
     * windows are more efficient than heavyweight windows, but lightweight and heavyweight
     * components do not mix well in a GUI. If your application mixes lightweight and heavyweight
     * components, you should disable lightweight popups. The default value for the {@code
     * lightWeightPopupEnabled} property is {@code true}, unless otherwise specified by the look and
     * feel. Some look and feels always use heavyweight popups, no matter what the value of this
     * property.
     * <p>
     * See the article <a
     * href="http://java.sun.com/products/jfc/tsc/articles/mixing/index.html">Mixing Heavy and Light
     * Components</a> on <a href="http://java.sun.com/products/jfc/tsc">
     * <em>The Swing Connection</em></a>. This method fires a property changed event.
     * 
     * @param lightWeightPopupEnabled
     *            if {@code true}, lightweight popups are desired
     * 
     * @beaninfo bound: true expert: true description: Set to {@code false} to require heavyweight
     *           popups.
     */
    public void setLightWeightPopupEnabled(boolean lightWeightPopupEnabled) {
        boolean oldValue = isLightWeightPopupEnabled();
        this.lightWeightPopupEnabled = lightWeightPopupEnabled;
        firePropertyChange("lightWeightPopupEnabled", oldValue, isLightWeightPopupEnabled());
    }

    /**
     * Gets the maximum size for the scroll map popup.
     * 
     * @return the maximum popup size
     */
    public Dimension getMaximumPopupSize() {
        return maxPopupSize;
    }

    /**
     * Sets the maximum size for the scroll map popup.
     * 
     * @param maxPopupSize
     *            the new maximum size
     * @throws NullPointerException
     *             if {@code maxPopupSize} is {@code null}
     */
    public void setMaxPopupSize(Dimension maxPopupSize) {
        Dimension oldValue = getMaximumPopupSize();
        this.maxPopupSize = Contract.asNotNull(maxPopupSize, "maxPopupSize cannot be null");
        firePropertyChange("maxPopupSize", oldValue, getMaximumPopupSize());
    }

    /**
     * Returns the look and feel (L&F) object that renders this component.
     *
     * @return the {@code ScrollMapUI} object that renders this component
     */
    public ScrollMapUI getUI() {
        return (ScrollMapUI) ui;
    }
  
    /**
     * Notification from the {@code UIManager} that the L&F has changed.
     * Replaces the current UI object with the latest version from the
     * {@code UIManager}.
     * 
     * @see javax.swing.JComponent#updateUI
     */
    @Override
    public void updateUI() {
        setUI((ScrollMapUI) LookAndFeelAddons.getUI(this, ScrollMapUI.class));
    }

    /**
     * Sets the L&F object that renders this component.
     * 
     * @param ui the {@code ScrollMapUI} L&F object
     * @see javax.swing.UIDefaults#getUI
     * 
     * @beaninfo bound: true 
     *          hidden: true 
     *     description: The UI object that implements the scroll map's LookAndFeel.
     */
    public void setUI(ScrollMapUI ui) {
        super.setUI(ui);
    }

    /**
     * Returns the name of the L&F class that renders this component.
     * 
     * @return the string {@link #uiClassID}
     * @see javax.swing.JComponent#getUIClassID
     * @see javax.swing.UIDefaults#getUI
     */
    @Override
    public String getUIClassID() {
        return uiClassID;
    }
}
