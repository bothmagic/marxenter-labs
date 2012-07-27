/*
 * $Id: AbstractContainerIcon.java 2628 2008-08-05 16:05:09Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.icon;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import javax.swing.Icon;

import org.jdesktop.swingx.event.AbstractDynamicContainer;
import org.jdesktop.swingx.util.LocationPolicy;
import org.jdesktop.swingx.util.ScalePolicy;

/**
 * This provides the abstract portion of any Icon that contains other icons. The process of forwarding event and
 * adding/removing listeners is handles by this class.
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 */
public abstract class AbstractContainerIcon extends AbstractDynamicScalableIcon {
    /**
     * Object responsible for handling child event forwarding.
     */
    private ContainerDelegate containerDelegate = new ContainerDelegate();

    /**
     * This is used to scale the child icon when painting if that icon is not scalable. Default is
     * ScalePolicy.FIXED_RATIO.
     */
    private ScalePolicy childScalePolicy = ScalePolicy.FIXED_RATIO;

    /**
     * This is used to locate non-scalable child icons when painting. The default is null which equates to
     * LocationPolicy.CENTER.
     */
    private LocationPolicy childLocaitonPolicy = null;

    /**
     * Create a new Container instance with default settings.
     */
    public AbstractContainerIcon() {
        super();
        setAreaClipped(false);
    }





    /**
     * Create a new container icon with the given policies.
     *
     * @param scalePolicy The policy used to scale this icon.
     */
    public AbstractContainerIcon(ScalePolicy scalePolicy) {
        super(scalePolicy);
        setAreaClipped(false);
    }





    /**
     * Install listeners on the child object only if it is a DynamicObject so that events can be forwarded.
     *
     * @param o The child object.
     */
    protected void installChild(Object o) {
        containerDelegate.installChild(o);
    }





    /**
     * Uninstall previously installed ChangeListeners from the given object.
     *
     * @param o The child object.
     */
    protected void uninstallChild(Object o) {
        containerDelegate.uninstallChild(o);
    }





    /**
     * Paints the child icon to the given graphics. this checks to see if the given icon is a ScalableIcon icon calling
     * the appropriate paint method as needed.
     *
     * @param child The child icon to paint.
     * @param c The component source of this painting call.
     * @param g The graphics to paint to.
     * @param x The x coordinate to paint the icon at.
     * @param y The y coordinate to paint the icon at.
     * @param width The width to paint the icon into.
     * @param height The height to paint the icon into.
     * @return The dimensions of the icon that was painted.
     */
    protected Rectangle paintChildIcon(Icon child, Component c, Graphics g, int x, int y, int width, int height) {
        return IconUtilities.paintChild(child, c, g, x, y, width, height, getChildLocationPolicy(), getChildScalePolicy());
    }





    /**
     * Get the LocationPolicy to use when painting non-scalable child icons.
     *
     * @return The location policy for the child.
     */
    public LocationPolicy getChildLocationPolicy() {
        return childLocaitonPolicy;
    }





    /**
     * Get the ScalePolicy used to scale non-scalable child icons when painting.
     *
     * @return the child's scale policy. This may be null.
     */
    public ScalePolicy getChildScalePolicy() {
        return childScalePolicy;
    }





    /**
     * Set the LocationPolicy used to locate non-scalable child icons when painting. A value of null implies that the
     * default value is used. The default is LocationPolicy.CENTER.
     *
     * @param childLocaitonPolicy The policy used to locate the child.
     */
    public void setChildLocaitonPolicy(LocationPolicy childLocaitonPolicy) {
        LocationPolicy old = getChildLocationPolicy();
        this.childLocaitonPolicy = childLocaitonPolicy;
        firePropertyChange("childLocationPolicy", old, getChildLocationPolicy());
    }





    /**
     * Set the ScalePolicy to use for non-scalable child icons when painting. If set to null the default value of
     * ResizePolicy.NONE is used.
     *
     * @param childScalePolicy The policy used to scale the child.
     */
    public void setChildScalePolicy(ScalePolicy childScalePolicy) {
        ScalePolicy old = getChildScalePolicy();
        this.childScalePolicy = childScalePolicy;
        firePropertyChange("childScalePolicy", old, getChildScalePolicy());
    }





    /**
     * Provides a simple mechanism for getting the size of a child Icon. If the
     * given icon icon is null then the given result remains unchanged, if the
     * given result is null in this case then a dimension with size 0x0 will be
     * returned.
     *
     * @param child The icon to size.
     * @param c The component the icon will be painted onto.
     * @param width The target width.
     * @param height The target height.
     * @param result The place to put the result.
     * @return The preferred size of the child icon.
     */
    protected Dimension fitInto(Icon child, Component c, int width, int height, Dimension result) {
        if (result == null) {
            result = new Dimension();
        }
        if (child instanceof ScalableIcon) {
            if (child instanceof AbstractScalableIcon) {
                ((AbstractScalableIcon) child).fitInto(c, width, height, result);
            } else {
                result.setSize(((ScalableIcon) child).fitInto(c, width, height));
            }
        } else {
            ScalePolicy sp = getChildScalePolicy();
            if (sp == null) {
                sp = ScalePolicy.valueOf(ScalePolicy.DimensionPolicy.FIXED_RATIO, ScalePolicy.ResizePolicy.NONE);
            }
            sp.fitInto(new Dimension(child.getIconWidth(), child.getIconHeight()), new Dimension(width, height), result);
        }
        return result;
    }





    /**
     * Provides support for handling event forwarding.
     *
     * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
     */
    private class ContainerDelegate extends AbstractDynamicContainer.Delegate {
        @Override
        public void fireStateChanged() {
            AbstractContainerIcon.this.fireStateChanged();
        }
    }

}
