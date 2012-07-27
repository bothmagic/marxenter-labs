/*
 * $Id: BasicClockUI.java 2629 2008-08-06 08:27:49Z mattnathan $
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
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.ComponentUI;

import org.jdesktop.swingx.JXClock;
import org.jdesktop.swingx.JXComponent;
import org.jdesktop.swingx.LookAndFeelUtilities;
import org.jdesktop.swingx.clock.DefaultClockIcon;
import org.jdesktop.swingx.icon.IconUtilities;
import org.jdesktop.swingx.plaf.ClockUI;
import org.jdesktop.swingx.plaf.UIListener;
import org.jdesktop.swingx.util.ScalePolicy;

/**
 * Defines a basic clock ui delegate instance.
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 */
public class BasicClockUI<C extends JXClock> extends ClockUI<C> {

    private static BasicClockUI<JXClock> SINGLETON;
    public static ComponentUI createUI(JComponent c) {
        if (SINGLETON == null) {
            SINGLETON = new BasicClockUI<JXClock>();
        }
        return SINGLETON;
    }





    private Adapter adapter;

    public BasicClockUI() {
        super();
        setInstallDefaultPropertySupport(true);
    }





    /**
     * Installs the common defaults for the source component. This installs the background, foreground, font, border,
     * backgroundPainter and foregroundPainter properties. Default background, foreground and font properties will be
     * provided if no ui specified values can be found.
     *
     * @param component The component to install the defaults on.
     */
    @Override
    protected void installDefaults(C component) {
        super.installDefaults(component);
        LookAndFeelUtilities.installDefaultColorsAndFonts(component, getUIPropertyString(), "Label");
    }





    @Override
    protected void installListeners(C c) {
        super.installListeners(c);
        c.addChangeListener(createChangeListener(c));
    }





    protected ChangeListener createChangeListener(C c) {
        return getAdapter(c);
    }





    private Adapter getAdapter(C c) {
        if (adapter == null) {
            adapter = new Adapter();
        }
        return adapter;
    }





    protected void stateChanged(C c) {
        c.repaint();
    }





    /**
     * Returns the specified component's preferred size appropriate for the look and feel. If <code>null</code> is
     * returned, the preferred size will be calculated by the component's layout manager instead (this is the preferred
     * approach for any component with a specific layout manager installed). The default implementation of this method
     * returns <code>null</code>.
     *
     * @param c the component whose preferred size is being queried; this argument is often ignored, but might be used
     *   if the UI object is stateless and shared by multiple components
     * @see javax.swing.JComponent#getPreferredSize
     * @see java.awt.LayoutManager#preferredLayoutSize
     * @return Dimension
     */
    @SuppressWarnings("unchecked")
    @Override
    public Dimension getPreferredSize(JComponent c) {
        return IconUtilities.getPreferredSize(c, getForegroundIcon((C) c));
    }





    /**
     * Get the foreground icon for the clock. The foreground icon is the icon that is responsible for painting and
     * sizing this component.
     *
     * @param c The source component.
     * @return The icon to paint this component.
     */
    protected Icon getForegroundIcon(C c) {
        Icon i = getUIProperty(c, "foregroundIcon", null);
        if (i == null) {
            addUIProperty(c, "foregroundIcon", i = createForegroundIcon(c));
        }
        return i;
    }





    /**
     * Create the icon used to paint this component.
     *
     * @param c The source component to paint.
     * @return The icon to paint the component.
     */
    protected Icon createForegroundIcon(C c) {
        return new DefaultClockIcon();
    }





    /**
     * Paints the specified component appropriate for the look and feel.
     * This method is invoked from the <code>ComponentUI.update</code> method when
     * the specified component is being painted.  Subclasses should override
     * this method and use the specified <code>Graphics</code> object to
     * render the content of the component.
     *
     * @param g the <code>Graphics</code> context in which to paint
     * @param c the component being painted;
     *          this argument is often ignored,
     *          but might be used if the UI object is stateless
     *          and shared by multiple components
     *
     * @see #update
     */
    @SuppressWarnings("unchecked")
    @Override
    public void paint(Graphics g, JComponent c) {
        C cl = (C) c;
        Insets i = c.getInsets();
        IconUtilities.paintChild(getForegroundIcon(cl), cl, g,
                                 i.left, i.top, cl.getWidth() - i.left - i.right, cl.getHeight() - i.top - i.bottom,
                                 ScalePolicy.FIXED_RATIO);
    }





    /**
     * Called by {@link #propertyChange(JXComponent, java.beans.PropertyChangeEvent)} when a property has changed.
     *
     * @param c The source component.
     * @param property The property name.
     * @param oldValue The old value.
     * @param newValue The new value.
     */
    @Override
    protected void propertyChange(C c, String property, Object oldValue, Object newValue) {
        if ("foregroundIcon".equals(property)) {
            c.repaint();
        }
    }





    @UIListener
    private static class Adapter implements ChangeListener {
        @SuppressWarnings("unchecked")
        public void stateChanged(ChangeEvent e) {
            JXClock c = (JXClock) e.getSource();
            BasicClockUI<JXClock> ui = (BasicClockUI<JXClock>) c.getUI();
            ui.stateChanged(c);
        }
    }
}
