/*
 * $Id: SimpleAbstractContainerIcon.java 2628 2008-08-05 16:05:09Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.icon;

import java.awt.Component;
import java.awt.Graphics;
import javax.swing.Icon;

import org.jdesktop.swingx.util.ScalePolicy;

/**
 * Simple extension to the abstract ContainerIcon instance that provides a single child support.
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 */
public abstract class SimpleAbstractContainerIcon extends AbstractContainerIcon {
    /**
     * The child icon.
     */
    private Icon child;

    /**
     * Create a new icon with a null child.
     */
    public SimpleAbstractContainerIcon() {
        this(null, null);
    }





    /**
     * Create a new icon with the given ScalePolicy and a null child.
     *
     * @param scalePolicy The scale policy for this icon.
     */
    public SimpleAbstractContainerIcon(ScalePolicy scalePolicy) {
        this(null, scalePolicy);
    }





    /**
     * Create a new icon with the given child.
     *
     * @param child The child for this icon.
     */
    public SimpleAbstractContainerIcon(Icon child) {
        this(child, null);
    }





    /**
     * Create a new icon with the given properties.
     *
     * @param child The child icon.
     * @param scalePolicy The ScalePolicy for this icon.
     */
    public SimpleAbstractContainerIcon(Icon child, ScalePolicy scalePolicy) {
        super(scalePolicy);
        setChild(child);
    }





    /**
     * Paints the child icon to the given graphics. Calls paintChildIcon with the current child if it is not null.
     *
     * @param c The component source of this painting call.
     * @param g The graphics to paint to.
     * @param x The x coordinate to paint the icon at.
     * @param y The y coordinate to paint the icon at.
     * @param width The width to paint the icon into.
     * @param height The height to paint the icon into.
     */
    protected void paintChildIcon(Component c, Graphics g, int x, int y, int width, int height) {
        Icon child = getChild();
        if (child != null) {
            paintChildIcon(child, c, g, x, y, width, height);
        }
    }





    /**
     * Get the child for this icon. This may be null.
     *
     * @return This icons child.
     */
    public Icon getChild() {
        return child;
    }





    /**
     * Sets the child for this icon. You can set the child to null.
     *
     * @param child The new child of this icon.
     */
    public void setChild(Icon child) {
        Icon old = getChild();
        if (old != null) {
            uninstallChild(old);
        }
        this.child = child;
        child = getChild();
        if (child != null) {
            installChild(child);
        }
        if (old != child) {
            firePropertyChange("child", old, child);
        }
    }
}
