/*
 * $Id: BorderIcon.java 2758 2008-10-09 10:51:35Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.icon;

import java.awt.*;
import javax.swing.Icon;
import javax.swing.border.AbstractBorder;
import javax.swing.border.Border;

/**
 * Represents an Icon which is wrapped in a Border.
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 */
public class BorderIcon extends AbstractContainerIcon {
    /**
     * Component used to pass into getBorderInsets to avoid null pointer Exceptions
     */
    private static final Component BORDER_INSET_COMPONENT = new Component() {
    };

    /**
     * The icon that paints the content.
     */
    private Icon icon;
    /**
     * The border that paints around the icon.
     */
    private Border border;





    /**
     * Create a new empty BordreIcon
     */
    public BorderIcon() {
        super();
    }





    /**
     * Create a new icon that wraps a border around the given Icon. Null values are supported.
     *
     * @param icon   The icon to paint a border around.
     * @param border The border to paint around the icon.
     */
    public BorderIcon(Icon icon, Border border) {
        setIcon(icon);
        setBorder(border);
    }





    /**
     * Sets the icon responsible for the painting of this icon. Null values are supported.
     *
     * @param icon The icon to paint a border around.
     */
    public void setIcon(Icon icon) {
        Icon old = getIcon();
        uninstallChild(old);
        this.icon = icon;
        installChild(icon);
        firePropertyChange("icon", old, getIcon());
    }





    /**
     * Set the border around the current icon. Null values supported.
     *
     * @param border The border to paint around the icon.
     */
    public void setBorder(Border border) {
        Border old = getBorder();
        this.border = border;
        firePropertyChange("border", old, getBorder());
    }





    /**
     * The border that is painted around the icon. This may be null.
     *
     * @return The border to paint around the icon.
     */
    public Border getBorder() {
        return border;
    }





    /**
     * Get the icon that is responsible for the painting of this icon. This may be null.
     *
     * @return The icon to paint the border around.
     */
    public Icon getIcon() {
        return icon;
    }





    /**
     * Returns the icon's height.
     *
     * @return an int specifying the fixed height of the icon.
     */
    public int getIconHeight() {
        return getIconDimension(false);
    }





    /**
     * Returns the icon's width.
     *
     * @return an int specifying the fixed width of the icon.
     */
    public int getIconWidth() {
        return getIconDimension(true);
    }





    /**
     * Utility method to combine the shared code for both getIconWidth and getIconHeight.
     *
     * @param width {@code true} if the returned dimension should represent the width, false to represent the height.
     * @return Either the preferred width or height for this Icon.
     */
    protected int getIconDimension(boolean width) {
        Border border = getBorder();
        Icon icon = getIcon();
        int w = 0;
        int h = 0;
        if (icon != null) {
            w = icon.getIconWidth();
            h = icon.getIconHeight();
        }

        if (border != null) {
            Insets i = getBorderInsets(border, w, h);
            if (width) {
                w += i.left + i.right;
            } else {
                h += i.top + i.bottom;
            }
        }
        return width ? w : h;
    }





    /**
     * Utility method for getting the insets of the border.
     *
     * @param border The border to get the insets for.
     * @param width The width of the component.
     * @param height The height of the component.
     * @return the insets of the border.
     */
    private Insets getBorderInsets(Border border, int width, int height) {
        BORDER_INSET_COMPONENT.setSize(width, height);
        Insets i;
        if (border instanceof AbstractBorder) {
            i = ((AbstractBorder) border).getBorderInsets(BORDER_INSET_COMPONENT, new Insets(0, 0, 0, 0));
        } else {
            i = border.getBorderInsets(BORDER_INSET_COMPONENT);
        }
        return i;
    }





    /**
     * Paints the child icon with a border around it.
     *
     * @param c      The component calling the paint.
     * @param g2     The graphics to paint to.
     * @param x      The x coordinate to paint to.
     * @param y      The y coordinate to paint to.
     * @param width  The width of the icon.
     * @param height The height of the icon.
     */
    @Override
    protected void paintIconImpl(Component c, Graphics2D g2, int x, int y, int width, int height) {
        Border border = getBorder();
        Icon icon = getIcon();

        if (icon != null) {
            int iconx = x;
            int icony = y;
            int iconw = width;
            int iconh = height;

            Insets i = null;

            if (border != null) {
                // can't use c here as this may give different results from when getIconWidth/Height were called.
                i = getBorderInsets(border, width, height);
                iconx += i.left;
                icony += i.top;
                iconw -= i.left + i.right;
                iconh -= i.top + i.bottom;
            }

            Rectangle result = paintChildIcon(icon, c, g2, iconx, icony, iconw, iconh);

            if (i != null) {
                x = result.x - i.left;
                y = result.y - i.top;
                width = result.width + i.left + i.right;
                height = result.height + i.top + i.bottom;
            }
        }

        if (border != null) {
            border.paintBorder(c, g2, x, y, width, height);
        }
    }





    /**
     * Fits this icon into the given space and places the result into {@code result}. This take the border insets into
     * account and uses the child icon as a basis.
     *
     * @param c      The component that called the paint.
     * @param width  The width of the target area.
     * @param height The height of the target area.
     * @param result The object to place the results in.
     * @return The size of this icon in the target area.
     */
    @Override
    public Dimension fitInto(Component c, int width, int height, Dimension result) {
        if (result == null) {
            result = new Dimension();
        }
        Icon icon = getIcon();
        Border border = getBorder();
        Insets i = border == null ? null : getBorderInsets(border, width, height);

        if (i != null) {
            width -= i.left + i.right;
            height -= i.bottom + i.top;
        }

        if (icon != null) {
            fitInto(icon, c, width, height, result);
        }

        if (i != null) {
            result.width += i.left + i.right;
            result.height += i.top + i.bottom;
        }

        return result;
    }
}
