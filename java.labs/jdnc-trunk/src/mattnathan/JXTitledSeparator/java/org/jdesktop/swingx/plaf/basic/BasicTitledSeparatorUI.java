/*
 * $Id: BasicTitledSeparatorUI.java 2762 2008-10-09 14:48:08Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.plaf.basic;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;
import javax.swing.CellRendererPane;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.plaf.ComponentUI;

import org.jdesktop.swingx.JXComponent;
import org.jdesktop.swingx.JXTitledSeparator2;
import org.jdesktop.swingx.LookAndFeelUtilities;
import org.jdesktop.swingx.plaf.TitledSeparatorUI;

/**
 * Basic implementation of the look and feel delegate for a JXTitledSeparator.
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 */
public class BasicTitledSeparatorUI extends TitledSeparatorUI {

    protected static final String RENDERER_PANE_COMPONENT = "rendererPane";

    private static BasicTitledSeparatorUI SINGLETON;
    public static ComponentUI createUI(JComponent c) {
        if (SINGLETON == null) {
            SINGLETON = new BasicTitledSeparatorUI();
        }
        return SINGLETON;
    }





    protected final Rectangle iconRect = new Rectangle();
    protected final Rectangle textRect = new Rectangle();

    protected BasicTitledSeparatorUI() {
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
    protected void installDefaults(JXTitledSeparator2 component) {
        super.installDefaults(component);

        JLabel label = new JLabel();
        label.setName(getUIPropertyString() + '.' + "titleComponent");
        JSeparator separator = new JSeparator(JSeparator.HORIZONTAL);
        separator.setName(getUIPropertyString() + '.' + "separatorComponent");

        addUIProperty(component, "titleComponent", label);
        addUIProperty(component, "separatorComponent", separator);

        // there is no laf setting for this. Would have preferred to use InternalFrame.titleAlignment or similar.
        LookAndFeelUtilities.installProperty(component, "horizontalAlignment",
                                             getUIProperty(component, "horizontalAlignment", SwingConstants.LEADING));

        LookAndFeelUtilities.installProperty(component, "horizontalTextPosition",
                                             getUIProperty(component, "horizontalTextPosition", label.getHorizontalTextPosition()));
        LookAndFeelUtilities.installDefaultProperty(component, "foreground", getUIPropertyString(), "Label");
        LookAndFeelUtilities.installDefaultProperty(component, "font", getUIPropertyString(), "Label");
        LookAndFeelUtilities.installProperty(component, "iconTitleGap", 4);
    }





    /**
     * Install any child components in this method.
     *
     * <p>Example Implementation:</p>
     * <p><pre><code>
     * {@code @Override}
     * protected static final String TITLE_COMPONENT = "title";
     * protected void installComponent(MyComp c) {
     *    addUIComponent(c, createTitleComponent(c), TITLE_COMPONENT);
     * }
     *
     * protected JComponent createTitleComponent(MyComp c) {
     *    return new JLabel();
     * }
     * </code></pre></p>
     *
     * @param component The container to add the children to.
     */
    @Override
    protected void installComponents(JXTitledSeparator2 component) {
        addUIComponent(component, new CellRendererPane(), RENDERER_PANE_COMPONENT);
    }





    /**
     * Get the number of pixels between the header (title and icon) and the separator.
     *
     * @param c The source
     * @return The distance between header and separator.
     */
    protected int getHeaderSeparatorGap(JXTitledSeparator2 c) {
        return getUIProperty(c, "headerSeparatorGap", c.getIconTitleGap());
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
    @Override
    public void paint(Graphics g, JComponent c) {
        JXTitledSeparator2 t = (JXTitledSeparator2) c;
        calcBounds(t);

        Insets i = t.getInsets();
        int x = i.left;
        int y = i.top;
        int w = t.getWidth() - i.left - i.right;
        int h = t.getHeight() - i.top - i.bottom;

        Rectangle r = new Rectangle();

        int minx;
        int maxx;

        getTitleBounds(t, r);
        minx = r.x;
        maxx = r.x + r.width;
        paintTitle(g, t, r);

        getIconBounds(t, r);
        minx = Math.min(minx, r.x);
        maxx = Math.max(maxx, r.x + r.width);
        paintIcon(g, t, r);

        int pad = getHeaderSeparatorGap(t);
        minx -= pad;
        maxx += pad;

        if (minx > x) {
            r.setBounds(x, y, minx - x, h);
            paintSeparator(g, t, r);
        }
        if (maxx < x + w) {
            r.setBounds(maxx, y, (x + w) - maxx, h);
            paintSeparator(g, t, r);
        }
    }





    /**
     * Paint the title for this separator into the given rectangle.
     *
     * @param g The graphics to paint to.
     * @param c The separator to paint.
     * @param r The area to paint the title to.
     */
    protected void paintTitle(Graphics g, JXTitledSeparator2 c, Rectangle r) {
        String title = c.getTitle();
        if (title.length() > 0) {
            CellRendererPane cr = (CellRendererPane) getUIComponent(c, RENDERER_PANE_COMPONENT);
            JLabel l = getUIProperty(c, "titleComponent");
            l.setIcon(null);
            l.setText(title);
            l.setEnabled(c.isEnabled());
            l.setFont(c.getFont());
            l.setForeground(c.getForeground());

            cr.paintComponent(g, l, c, r);
        }
    }





    /**
     * Paint the icon for the separator into the given rectangle.
     *
     * @param g The graphics to paint to.
     * @param c The separator to paint.
     * @param r The rectangle to paint into to.
     */
    protected void paintIcon(Graphics g, JXTitledSeparator2 c, Rectangle r) {
        Icon icon = c.getIcon();
        if (icon != null) {
            CellRendererPane cr = (CellRendererPane) getUIComponent(c, RENDERER_PANE_COMPONENT);
            JLabel l = getUIProperty(c, "titleComponent");
            l.setIcon(icon);
            l.setText(null);
            l.setEnabled(c.isEnabled());

            cr.paintComponent(g, l, c, r);
        }
    }





    /**
     * Paint the separator line for the given titled separator.
     *
     * @param g The graphics to paint to.
     * @param c The separator to paint.
     * @param r The rectangle to paint into.
     */
    protected void paintSeparator(Graphics g, JXTitledSeparator2 c, Rectangle r) {
        CellRendererPane cr = (CellRendererPane) getUIComponent(c, RENDERER_PANE_COMPONENT);
        JSeparator s = getUIProperty(c, "separatorComponent");
        s.setEnabled(c.isEnabled());
        s.setForeground(c.getForeground());
        s.setBackground(c.getBackground());

        // need to center the separator as some look and feels (i.e. Metal)
        // don't do this within the painting code
        int prefh = s.getPreferredSize().height;
        int ny = (r.height - prefh) >> 1;
        cr.paintComponent(g, s, c, r.x, ny, r.width, prefh);
    }





    private static final Rectangle viewBounds = new Rectangle();
    /**
     * Calculated and places into the textRect and iconRect the bounds of the named components.
     *
     * @param c The separator to calculate for.
     */
    protected void calcBounds(JXTitledSeparator2 c) {
        Rectangle view = LookAndFeelUtilities.getInnerBounds(c, viewBounds);
        iconRect.x = iconRect.y = iconRect.height = iconRect.width = 0;
        textRect.x = textRect.y = textRect.height = textRect.width = 0;
        SwingUtilities.layoutCompoundLabel(c, c.getFontMetrics(c.getFont()), c.getTitle(), c.getIcon(), SwingConstants.CENTER,
                                           c.getHorizontalAlignment(), SwingConstants.CENTER, c.getHorizontalTextPosition(),
                                           view, iconRect, textRect, c.getIconTitleGap());
    }





    /**
     * Get the bounds for the title.
     *
     * @param c The separator.
     * @param result The place to put the results.
     */
    protected void getTitleBounds(JXTitledSeparator2 c, Rectangle result) {
        result.setBounds(textRect);
    }





    /**
     * Get the bounds to paint the icon.
     *
     * @param c The separator.
     * @param result The place to put the results.
     */
    protected void getIconBounds(JXTitledSeparator2 c, Rectangle result) {
        result.setBounds(iconRect);
    }





    /**
     * Returns the specified component's preferred size appropriate for
     * the look and feel.  If <code>null</code> is returned, the preferred
     * size will be calculated by the component's layout manager instead
     * (this is the preferred approach for any component with a specific
     * layout manager installed).  The default implementation of this
     * method returns <code>null</code>.
     *
     * @param c the component whose preferred size is being queried;
     *          this argument is often ignored,
     *          but might be used if the UI object is stateless
     *          and shared by multiple components
     * @return The preferred size of the component.
     *
     * @see javax.swing.JComponent#getPreferredSize
     * @see java.awt.LayoutManager#preferredLayoutSize
     */
    @Override
    public Dimension getPreferredSize(JComponent c) {
        JXTitledSeparator2 sep = (JXTitledSeparator2) c;
        calcBounds(sep);
        int h = Math.max(iconRect.height, textRect.height);
        int w = textRect.width + iconRect.width;
        if (sep.getTitle().length() > 0 && sep.getIcon() != null) {
            w += 4; // iconTextGap
        }
        return new Dimension(w, h);
    }





    /**
     * Returns the specified component's maximum size appropriate for
     * the look and feel.  If <code>null</code> is returned, the maximum
     * size will be calculated by the component's layout manager instead
     * (this is the preferred approach for any component with a specific
     * layout manager installed).  The default implementation of this
     * method invokes <code>getPreferredSize</code> and returns that value.
     *
     * @param c the component whose maximum size is being queried;
     *          this argument is often ignored,
     *          but might be used if the UI object is stateless
     *          and shared by multiple components
     * @return a <code>Dimension</code> object or <code>null</code>
     *
     * @see javax.swing.JComponent#getMaximumSize
     * @see java.awt.LayoutManager2#maximumLayoutSize
     */
    @Override
    public Dimension getMaximumSize(JComponent c) {
        return new Dimension(Short.MAX_VALUE, Short.MAX_VALUE);
    }





    /**
     * Returns the specified component's minimum size appropriate for
     * the look and feel.  If <code>null</code> is returned, the minimum
     * size will be calculated by the component's layout manager instead
     * (this is the preferred approach for any component with a specific
     * layout manager installed).  The default implementation of this
     * method invokes <code>getPreferredSize</code> and returns that value.
     *
     * @param c the component whose minimum size is being queried;
     *          this argument is often ignored,
     *          but might be used if the UI object is stateless
     *          and shared by multiple components
     *
     * @return a <code>Dimension</code> object or <code>null</code>
     *
     * @see javax.swing.JComponent#getMinimumSize
     * @see java.awt.LayoutManager#minimumLayoutSize
     * @see #getPreferredSize
     */
    @Override
    public Dimension getMinimumSize(JComponent c) {
        return getPreferredSize(c);
    }





    /**
     * Called by {@link #propertyChange(JXComponent, java.beans.PropertyChangeEvent)} when a property has changed.
     *
     * @param c The source component.
     * @param p The property name.
     * @param oldValue The old value.
     * @param newValue The new value.
     */
    @Override
    protected void propertyChange(JXTitledSeparator2 c, String p, Object oldValue, Object newValue) {
        if ("title".equals(p) ||
            "icon".equals(p) ||
            "border".equals(p) ||
            "font".equals(p) ||
            "iconTitleGap".equals(p) ||
            "headerSeparatorGap".equals(p)) {
            // it may be possible to refine the repaint area based on the
            // old and new values for title and icon but for now we just
            // repaint the whole component
            c.repaint();
        } else {
            c.repaint();
        }
    }

}
