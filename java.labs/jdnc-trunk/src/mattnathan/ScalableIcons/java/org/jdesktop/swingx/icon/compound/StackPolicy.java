/*
 * $Id: StackPolicy.java 2758 2008-10-09 10:51:35Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.icon.compound;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ListIterator;
import javax.swing.Icon;
import javax.swing.SwingConstants;
import static javax.swing.SwingConstants.NORTH_EAST;
import static javax.swing.SwingConstants.NORTH_WEST;
import static javax.swing.SwingConstants.SOUTH_EAST;
import static javax.swing.SwingConstants.SOUTH_WEST;
import org.jdesktop.swingx.icon.CompoundIcon;
import org.jdesktop.swingx.icon.IconUtilities;
import org.jdesktop.swingx.util.LocationPolicy;

/**
 * CompoundPolicy which stacks its icons on top of one another with a slight offset. This allows the direction of the
 * stack to be specified as one of SwingConstants NORTH_EAST, NORTH_WEST, SOUTH_EAST or SOURTH_WEST indicating the
 * position of the last (bottom) icon in the stack. The stack can be configured using a number of different spacing
 * properties to support the most common cases. The proportion properties (proportionX and proportionY) provide a size
 * independent mechanism for specifying the space used for background icons (those that are not on top). The offset
 * properties (offsetX and offsetY) provide a mechanism for specifying the offset of subsequent icons relative the the
 * previous icon in pixels. This type of positioning has a lower priority than the proportional method and is only used
 * when both proportion properties are 0.
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 */
public class StackPolicy extends AbstractPolicy {

    /**
     * Defines how many pixels subsequent children will be offset in the x
     * direction.
     */
    private int offsetX = 0;
    /**
     * Defines how many pixels subsequent children will be offset in the y
     * direction.
     */
    private int offsetY = 0;

    /**
     * Defines how much of the icons x dimension is taken up by background
     * children.
     */
    private float proportionX;
    /**
     * Defines how much of the icons y dimension is taken up by background
     * children.
     */
    private float proportionY;

    /**
     * The direction the stack cascades in. One of NORTH_EAST, NORTH_WEST,
     * SOUTH_EAST or SOUTH_WEST.
     */
    private int stackDirection = SwingConstants.NORTH_EAST;

    /**
     * Create a new StackPolicy with a default proportion dimensions of 0.3 and
     * 0.2.
     */
    public StackPolicy() {
        this(0.3f, 0.2f);
    }





    /**
     * Create a new StackPolicy with the given proportions.
     *
     * @param proportionX The proportion of the target width that should be taken with background icons.
     * @param proportionY The proportion of the target height that should be taken with background icons.
     */
    public StackPolicy(float proportionX, float proportionY) {
        this(proportionX, proportionY, 1, ALL_VISIBLE);
    }





    /**
     * Create a new stack policy with the given scale factor and visibleIcons
     * icons visible before opacity is 0.
     *
     * @param scaleFactor The scale factor for the background images.
     * @param visibleIcons The number of icons visible before opacity of 0 is reached.
     */
    public StackPolicy(float scaleFactor, int visibleIcons) {
        this(0.3f, 0.2f, scaleFactor, visibleIcons);
    }





    /**
     * Create a new StackPolicy with 0 proportions and the given fixed offsets.
     *
     * @param offsetX The fixed offset of each subsequent icon.
     * @param offsetY The fixed offset of each subsequent icon.
     */
    public StackPolicy(int offsetX, int offsetY) {
        this(offsetX, offsetY, 1, ALL_VISIBLE);
    }





    /**
     * Create a stack policy that uses static offsets and the given scale factor
     * and visible icons.
     *
     * @param offsetX The fixed offset of each subsequent icon.
     * @param offsetY The fixed offset of each subsequent icon.
     * @param scaleFactor The scale factor of subsequent icons.
     * @param visibleIcons The number of icons visible before opacity is 0.
     */
    public StackPolicy(int offsetX, int offsetY, float scaleFactor, int visibleIcons) {
        this(0f, 0f, scaleFactor, visibleIcons);
        this.offsetX = offsetX;
        this.offsetY = offsetY;
    }





    /**
     * Create a new StackPolicy with the given properties.
     *
     * @param proportionX The proportion of the target width that should be taken with background icons.
     * @param proportionY The proportion of the target height that should be taken with background icons.
     * @param scaleFactor The scale factor of subsequent icons.
     * @param opacityFactor The value opacity is multiplied by in each subsequent icon.
     */
    public StackPolicy(float proportionX, float proportionY, float scaleFactor, float opacityFactor) {
        this(proportionX, proportionY, scaleFactor, ALL_VISIBLE);
        setOpcaityFactor(opacityFactor);
    }





    /**
     * Create a new StackPolicy with the given properties.
     *
     * @param proportionX The proportion of the target width that should be taken with background icons.
     * @param proportionY The proportion of the target height that should be taken with background icons.
     * @param scaleFactor The scale factor of subsequent icons.
     * @param visibleIcons The number of icons visible before opacity is 0.
     */
    public StackPolicy(float proportionX, float proportionY, float scaleFactor, int visibleIcons) {
        super(scaleFactor, visibleIcons);
        if (proportionX >= 1 || proportionY >= 1) {
            throw new IllegalArgumentException("proportion values cannot be >= 1");
        }
        this.proportionX = proportionX;
        this.proportionY = proportionY;
    }





    /**
     * {@inheritDoc}
     */
    public void addPolicyIcon(CompoundIcon container, Icon icon, Object constraints) {
    }





    /**
     * {@inheritDoc}
     */
    public void invalidatePolicy(CompoundIcon container) {
    }





    /**
     * Paints the given icons onto the given Graphics object.
     *
     * @param container The container for this policy.
     * @param c The Component that originated the call.
     * @param g The Graphics to paint the icons onto.
     * @param x The x coordinate for the painting to initiate at
     * @param y The y coordinate for the painting to originate at
     * @param width The width of the icon.
     * @param height The height of the icon.
     */
    public void paintIcons(CompoundIcon container, Component c, Graphics g, int x, int y, int width, int height) {
        int fw;
        int fh;

        float propx = getProportionX();
        float propy = getProportionY();

        int n = container.getSize();

        float offsetx;
        float offsety;

        if (propx > 0) {
            fw = (int) (width * (1 - propx));
        } else {
            fw = width - ((n - 1) * getOffsetX());
            if (fw > width) {
                fw = width;
            }
        }

        if (propy > 0) {
            fh = (int) (height * (1 - propy));
        } else {
            fh = height - ((n - 1) * getOffsetY());
            if (fh > height) {
                fh = height;
            }
        }

        offsetx = -(width - fw) / (float) (n - 1);
        offsety = -(height - fh) / (float) (n - 1);

        float iconx = x;
        float icony = y;

        switch (getStackDirection()) {
            case SOUTH_EAST:
                iconx += width - fw;
                icony += height - fh;
                break;
            case NORTH_EAST:
                iconx += width - fw;
                offsety = -offsety;
                break;
            case NORTH_WEST:
                offsetx = -offsetx;
                offsety = -offsety;
                break;
            case SOUTH_WEST:
                icony += height - fh;
                offsetx = -offsetx;
                break;
            default:
                assert false;
        }

        int index = n - 1;
        LocationPolicy location = LocationPolicy.valueOf(getStackDirection());

        for (ListIterator<Icon> iter = getOffsetIcons(container); iter.hasNext(); index--) {
            Icon icon = iter.next();
            paintChild(container, icon, index, c, g, (int) iconx, (int) icony, fw, fh, location);
            iconx += offsetx;
            icony += offsety;
        }
    }





    /**
     * Gets the preferred size for this icon compound policy.
     *
     * @param container The container for the icons.
     * @return The preferred size of this policy.
     */
    protected Dimension getSize(CompoundIcon container) {
        float propx = getProportionX();
        float propy = getProportionY();

        Dimension result = IconUtilities.getLargestSize(container);

        if (propx > 0) {
            result.width /= (1 - propx);
        } else {
            result.width = Math.max(result.width + getOffsetX() * (container.getSize() - 1), result.width);
        }

        if (propy > 0) {
            result.height /= (1 - propy);
        } else {
            result.height = Math.max(result.height + getOffsetY() * (container.getSize() - 1), result.height);
        }

        return result;

    }





    /**
     * {@inheritDoc}
     */
    public void removePolicyIcon(CompoundIcon container, Icon icon) {
    }





    /**
     * Gets the number of pixels each subsequent child will be offset by in the x
     * axis.
     *
     * @return The offset for each subsequent icon.
     */
    public int getOffsetX() {
        return offsetX;
    }





    /**
     * Gets the number of pixels each subsequent child will be offset by in the y
     * axis.
     *
     * @return The offset for each subsequent icon.
     */
    public int getOffsetY() {
        return offsetY;
    }





    /**
     * Gets the proportion of the icon paint width which should be taken up by
     * the background icons.
     *
     * @return The proportion of space given to background icons.
     */
    public float getProportionX() {
        return proportionX;
    }





    /**
     * Gets the proportion of the icons paint height which should be taken up by
     * the background icons.
     *
     * @return The proportion if space given to background icons.
     */
    public float getProportionY() {
        return proportionY;
    }





    /**
     * Get the direction the stack will cascade in. Will be one of NORTH_EAST, NORTH_WEST, SOUTH_EAST or SOUTH_WEST.
     *
     * @return The direction of the stack.
     */
    public int getStackDirection() {
        return stackDirection;
    }





    /**
     * Sets the offset by which subsequent children will be painted. This value
     * is only used when the proportionX property is <= 0.
     *
     * @param offsetX The offset in pixels.
     */
    public void setOffsetX(int offsetX) {
        this.offsetX = offsetX;
    }





    /**
     * Sets the offset by which subsequent children will be painted. This value
     * is only used when proportionY is <= 0.
     *
     * @param offsetY The offset in pixels.
     */
    public void setOffsetY(int offsetY) {
        this.offsetY = offsetY;
    }





    /**
     * Set both offset values at the same time.
     *
     * @param offsetX The x offset in pixels.
     * @param offsetY The y offset in pixels.
     */
    public void setOffset(int offsetX, int offsetY) {
        this.offsetX = offsetX;
        this.offsetY = offsetY;
    }





    /**
     * Set the proportion of this icons paint width that will be used for
     * background icons. This property takes precedence over the offsetX property
     * when set to a value > 0.
     *
     * @param proportionX The proportion of the view for background icons.
     */
    public void setProportionX(float proportionX) {
        if (proportionX >= 1) {
            throw new IllegalArgumentException("proportionX cannot be >= 1");
        }
        this.proportionX = proportionX;
    }





    /**
     * Set the proportion of this icons paint height that will be used for
     * background icons. This property takes precedence over the offsetY property
     * when set to a value > 0.
     *
     * @param proportionY The proportion of the view for background icons.
     */
    public void setProportionY(float proportionY) {
        if (proportionY >= 1) {
            throw new IllegalArgumentException("proportionY cannot be >= 1");
        }

        this.proportionY = proportionY;
    }





    /**
     * Sets the direction the stack cascades in. For example if a value of
     * NORTH_EAST is set then the front most icon will be positioned at the
     * bottom left and the back-most icon will be positioned at the top right.
     * <p>
     * Possible values are:
     * <ul>
     * <li>SwingConstants.NORTH_EAST</li>
     * <li>SwingConstants.NORTH_WEST</li>
     * <li>SwingConstants.SOUTH_EAST</li>
     * <li>SwingConstants.SOUTH_WEST</li>
     * </ul>
     * All other values cause an exception to be thrown.
     *
     * @param stackDirection The stack direction.
     */
    public void setStackDirection(int stackDirection) {
        switch (stackDirection) {
            case NORTH_EAST:
            case NORTH_WEST:
            case SOUTH_EAST:
            case SOUTH_WEST:
                break;
            default:
                throw new IllegalArgumentException("stackDirection must be one of SwingConstants diagonal directions: " + stackDirection);
        }
        this.stackDirection = stackDirection;
    }





    /**
     * {@inheritDoc}
     */
    public int getPolicyWidth(CompoundIcon icon) {
        return getSize(icon).width;
    }





    /**
     * {@inheritDoc}
     */
    public int getPolicyHeight(CompoundIcon icon) {
        return getSize(icon).height;
    }





    /**
     * {@inheritDoc}
     */
    public Dimension fitInto(CompoundIcon icon, int width, int height) {
        return IconUtilities.getDefault(icon.getChildScalePolicy()).fitInto(getSize(icon), new Dimension(width, height), null);
    }
}
