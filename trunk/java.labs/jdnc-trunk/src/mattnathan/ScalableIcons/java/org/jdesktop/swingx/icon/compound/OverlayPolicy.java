/*
 * $Id: OverlayPolicy.java 2629 2008-08-06 08:27:49Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.icon.compound;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.Icon;

import java.util.ListIterator;

import org.jdesktop.swingx.icon.CompoundIcon;
import org.jdesktop.swingx.icon.IconUtilities;
import org.jdesktop.swingx.util.LocationPolicy;

/**
 * Defines a simple policy which paints all the children directly on top of each
 * other.
 * <p/>
 * This policy can be used for adding extras to a standard icon; shine or an
 * emblem for example.
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 */
public class OverlayPolicy extends AbstractPolicy {
    // cache variables
    private boolean valid = false;
    private final Dimension size = new Dimension( -1, -1);

    /**
     * Create the default OverlayPolicy.
     */
    public OverlayPolicy() {
    }





    /**
     * Create a new policy with the given scale factor that fades out after the
     * given number of icons.
     *
     * @param scaleFactor The scale factor for each subsequent icon.
     * @param visibleIcons The number of visible icons to paint.
     */
    public OverlayPolicy(float scaleFactor, int visibleIcons) {
        super(scaleFactor, visibleIcons);
    }





    /**
     * Paints all the required icons one on top of the other. This respects the
     * offset and visibility setting of AbstractPolicy and the
     * childLocationPolicy of CompoundIcon.
     *
     * @param container The parent icon.
     * @param c The component to paint to.
     * @param g The graphics device to paint.
     * @param x The x coordinate of the icon.
     * @param y The y coordinate of the icon.
     * @param width The width for the icon.
     * @param height The height for the icon.
     */
    public void paintIcons(CompoundIcon container, Component c, Graphics g, int x, int y, int width, int height) {
        LocationPolicy location = IconUtilities.getDefault(container.getChildLocationPolicy());
        for (ListIterator<Icon> iter = getOffsetIcons(container); iter.hasNext(); ) {
            int index = iter.nextIndex();
            Icon icon = iter.next();
            paintChild(container, icon, index, c, g, x, y, width, height, location);
        }
    }





    /**
     * Gets the preferred width of this layout policy.
     *
     * @param icon The container for the icons.
     * @return The preferred width.
     */
    public int getPolicyWidth(CompoundIcon icon) {
        validate(icon);
        return size.width;
    }





    /**
     * Gets the preferred height of this layout policy.
     *
     * @param icon The container for the icons.
     * @return The preferred height.
     */
    public int getPolicyHeight(CompoundIcon icon) {
        validate(icon);
        return size.height;

    }





    /**
     * Fits this policy into the given target area.
     *
     * @param icon The container for the icons.
     * @param width The target width.
     * @param height The target height.
     * @return The preferred size of this icon in the container area.
     */
    public Dimension fitInto(CompoundIcon icon, int width, int height) {
        validate(icon);
        return IconUtilities.getDefault(icon.getChildScalePolicy()).fitInto(size, new Dimension(width, height), null);
    }





    /**
     * Invalidates this policy.
     *
     * @param container The container for the icons.
     * @param icon The icon to add.
     * @param constraints The constraints for the icon.
     */
    public void addPolicyIcon(CompoundIcon container, Icon icon, Object constraints) {
        invalidatePolicy(container);
    }





    /**
     * Invalidates this policy.
     *
     * @param container The container for the icons.
     * @param icon The icon to remove.
     */
    public void removePolicyIcon(CompoundIcon container, Icon icon) {
        invalidatePolicy(container);
    }





    /**
     * Returns true if the size of this policy is valid.
     *
     * @return {@code true} if this policy is valid.
     */
    public boolean isValid() {
        return valid;
    }





    /**
     * Validates this layout policies size.
     *
     * @param icon The container for the icons.
     */
    public void validate(CompoundIcon icon) {
        if (!isValid()) {
            size.setSize(IconUtilities.getLargestSize(icon));
            valid = true;
        }
    }





    /**
     * Invalidates the size of this policy. This is fast and simply sets the
     * valid flag to false.
     *
     * @param container The container.
     */
    public void invalidatePolicy(CompoundIcon container) {
        valid = false;
    }
}
