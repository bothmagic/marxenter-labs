/*
 * $Id: IconRange.java 2628 2008-08-05 16:05:09Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.icon.range;

import java.awt.Component;
import java.awt.Graphics;
import javax.swing.Icon;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.jdesktop.swingx.icon.AbstractContainerIcon;
import org.jdesktop.swingx.icon.IconUtilities;
import org.jdesktop.swingx.icon.ScalableIcon;
import org.jdesktop.swingx.util.LocationPolicy;
import org.jdesktop.swingx.event.DynamicObject;

import java.util.*;

/**
 * Defines a range representing an icon.
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 */
public class IconRange extends AbstractRange {

    /**
     * The represented icon.
     */
    private Icon icon;
    /**
     * The location to place the icon if it is not scalable.
     */
    private LocationPolicy iconLocation;

    /**
     * Create a new IconRange for the given icon. The icon will be centered if it
     * cannot be scaled.
     *
     * @param icon Icon
     */
    public IconRange(Icon icon) {
        this(icon, LocationPolicy.valueOf(LocationPolicy.CENTER));
    }





    /**
     * Creates a new IconRange for the given Icon using the given location
     * policy.
     *
     * @param icon Icon
     * @param iconLocation LocationPolicy
     */
    public IconRange(Icon icon, LocationPolicy iconLocation) {
        if (icon == null) {
            throw new IllegalArgumentException("icon cannot be null");
        }
        if (iconLocation == null) {
            throw new IllegalArgumentException("iconLocation cannot be null");
        }
        this.icon = icon;
        if (icon instanceof DynamicObject) {
            ((DynamicObject) icon).addChangeListener(new ChangeListener() {
                public void stateChanged(ChangeEvent e) {
                    fireStateChanged();
                }
            });
        }
        this.iconLocation = iconLocation;
    }





    /**
     * Returns the icon this range represents.
     *
     * @return Icon
     */
    public Icon getIcon() {
        return icon;
    }





    /**
     * Gets the location policy of the icon when not scaled.
     *
     * @return LocationPolicy
     */
    public LocationPolicy getIconLocaiton() {
        return iconLocation;
    }





    /**
     * Always returns true.
     *
     * @return {@code true}.
     */
    public boolean isReady() {
        return true;
    }





    /**
     * Does nothing and returns true.
     *
     * @param wait boolean
     * @return {@code true}.
     */
    public boolean makeReady(boolean wait) {
        return true;
    }





    /**
     * Paints the source icon. If the source icon is a ScalableIcon then its
     * scalable paint icon method is called otherwise the LocationPolicy is used
     * to determine where to paint the ordinary icon on the graphics.
     *
     * @param parent ScalableIcon
     * @param c Component
     * @param g Graphics
     * @param x int
     * @param y int
     * @param width int
     * @param height int
     */
    public void paint(ScalableIcon parent, Component c, Graphics g, int x, int y, int width, int height) {
        Icon icon = getIcon();
        if (parent instanceof AbstractContainerIcon) {
            AbstractContainerIcon cicon = (AbstractContainerIcon) parent;
            IconUtilities.paintChild(icon, c, g, x, y, width, height, cicon.getChildLocationPolicy(), cicon.getChildScalePolicy());
        } else {
            IconUtilities.paintChild(icon, c, g, x, y, width, height);
        }
    }





    /**
     * Utility method for converting a collection of icons into ranges.
     *
     * @param icons The list of icons to convert.
     * @return The ranges created from the icons.
     */
    public static Range[] convert(Icon ...icons) {
        Range[] result = new Range[icons.length];
        for (int i = 0, n = icons.length; i < n; i++) {
            result[i] = new IconRange(icons[i]);
        }
        return result;
    }





    /**
     * Utility method for converting a collection of icons into ranges.
     *
     * @param icons The list of icons to convert.
     * @return The ranges created from the icons.
     */
    public static Range[] convert(List<Icon> icons) {
        Range[] result = new Range[icons.size()];
        int i = 0;
        for (Icon icon : icons) {
            result[i++] = new IconRange(icon);
        }
        return result;
    }





    /**
     * {@inheritDoc}
     */
    public int getRangeWidth() {
        return getIcon().getIconWidth();
    }





    /**
     * {@inheritDoc}
     */
    public int getRangeHeight() {
        return getIcon().getIconHeight();
    }
}
