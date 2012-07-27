/*
 * $Id: IconUtilities.java 2628 2008-08-05 16:05:09Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.icon;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import javax.swing.Icon;

import org.jdesktop.swingx.util.LocationPolicy;
import org.jdesktop.swingx.util.ScalePolicy;

/**
 * A collection of common icon utilities used by the icon package.
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 */
public class IconUtilities {
    private IconUtilities() {
    }

    /**
     * Returns the default ScalePolicy if the given policy is null, otherwise
     * returns the given policy.
     *
     * @param policy The policy to return if non-null.
     * @return The policy to use. Guaranteed to be non-null.
     */
    public static ScalePolicy getDefault(ScalePolicy policy) {
        return policy == null ? ScalePolicy.valueOf(ScalePolicy.DimensionPolicy.FIXED_RATIO, ScalePolicy.ResizePolicy.NONE) : policy;
    }





    /**
     * Returns the default LocationPolicy if the given policy is null, otherwise
     * returns the given policy.
     *
     * @param policy The policy to return if non-null.
     * @return The policy to use. Guaranteed to be non-null.
     */
    public static LocationPolicy getDefault(LocationPolicy policy) {
        return policy == null ? LocationPolicy.valueOf(LocationPolicy.CENTER) : policy;
    }





    public static Rectangle paintChild(Icon child, Component c, Graphics g, int x, int y, int width, int height) {
        return paintChild(child, c, g, x, y, width, height, LocationPolicy.valueOf(LocationPolicy.CENTER));
    }





    public static Rectangle paintChild(Icon child, Component c, Graphics g, int x, int y, int width, int height,
                                       LocationPolicy location) {
        return paintChild(child, c, g, x, y, width, height, location,
                          ScalePolicy.valueOf(ScalePolicy.DimensionPolicy.FIXED_RATIO, ScalePolicy.ResizePolicy.NONE));
    }





    public static Rectangle paintChild(Icon child, Component c, Graphics g, int x, int y, int width, int height,
                                       ScalePolicy scale) {
        return paintChild(child, c, g, x, y, width, height, LocationPolicy.valueOf(LocationPolicy.CENTER), scale);
    }





    /**
     * Paints the given icon onto the given graphics filling the space if it is a ScalableIcon or using the
     * LocationPolicy to position if not. Null values for the ScalePolicy will result in a SclaePolicy using a
     * ResizePolicy of NONE and a null value for the LocationPolicy will result in using a LocationPolicy which centers
     * the child icon.
     *
     * @param child The icon to paint.
     * @param c The component used to call this paint method.
     * @param g The graphics to paint to.
     * @param x The x coordinate to paint the icon to.
     * @param y The y coordinate to paint the icon to.
     * @param width The target width for the icon.
     * @param height The target height for the icon.
     * @param location If null 0, 0 is used to offset
     * @param scale The scale policy used for icons that do not match the target size.
     * @return the bounds of the painted child icon.
     */
    public static Rectangle paintChild(Icon child, Component c, Graphics g, int x, int y, int width, int height,
                                       LocationPolicy location, ScalePolicy scale) {

        Rectangle result;

        if (scale != null && location == null) {
            result = paintChild(child, c, g, x, y, width, height, scale);
        } else if (scale == null && location != null) {
            result = paintChild(child, c, g, x, y, width, height, location);
        } else if (scale == null) {
            result = paintChild(child, c, g, x, y, width, height);
        } else {
            boolean scaled = false;
            Dimension sourceSize;
            Dimension targetSize = new Dimension(width, height);

            if (child instanceof ScalableIcon) {
                ScalableIcon sIcon = (ScalableIcon) child;
                sourceSize = sIcon.fitInto(c, width, height);
            } else {
                sourceSize = new Dimension(child.getIconWidth(), child.getIconHeight());
            }

            Dimension scaledSize = new Dimension(sourceSize);

            if (!sourceSize.equals(targetSize)) {
                // scaling and translation are needed
                scale.fitInto(sourceSize, targetSize, scaledSize);
                scaled = !scaledSize.equals(sourceSize);

                if (scaled) {
                    double sx = scaledSize.width / (double) sourceSize.width;
                    double sy = scaledSize.height / (double) sourceSize.height;

                    Point loc = location.locate(scaledSize, targetSize, null);
                    x += loc.x;
                    y += loc.y;

                    g = g.create();
                    Graphics2D g2 = (Graphics2D) g;
                    AffineTransform old = g2.getTransform();
                    double tx = old.getTranslateX();
                    double ty = old.getTranslateY();
                    double osx = old.getScaleX();
                    double osy = old.getScaleY();
                    AffineTransform trans = new AffineTransform(); // scale from origin
                    trans.translate(x * osx + tx, y * osy + ty);
                    trans.scale(sx, sy);
                    trans.translate( -x * osx - tx, -y * osy - ty);
                    trans.concatenate(g2.getTransform());
                    g2.setTransform(trans);

                } else {
                    Point loc = location.locate(sourceSize, targetSize, null);
                    x += loc.x;
                    y += loc.y;
                }
            }

            if (child instanceof ScalableIcon) {
                ((ScalableIcon) child).paintIcon(c, g, x, y, sourceSize.width, sourceSize.height);
            } else {
                child.paintIcon(c, g, x, y);
            }

            if (scaled) {
                g.dispose();
            }

            result = new Rectangle(x, y, scaledSize.width, scaledSize.height);
        }
        return result;

    }





    /**
     * Gets the largest independent sizes for the given icons.
     *
     * @param icons The icons to check.
     * @return The largest dimension containing all icons.
     */
    public static Dimension getLargestSize(Icon ...icons) {
        int maxw = 0;
        int maxh = 0;
        for (Icon icon : icons) {
            int iw = icon.getIconWidth();
            int ih = icon.getIconHeight();
            if (iw > maxw) {
                maxw = iw;
            }
            if (ih > maxh) {
                maxh = ih;
            }
        }
        return new Dimension(maxw, maxh);
    }





    /**
     * Gets the largest independent sizes for the given icons.
     *
     * @param icons The list of icons to check.
     * @return The largest dimension containing all icons.
     */
    public static Dimension getLargestSize(Iterable<Icon> icons) {
        int maxw = 0;
        int maxh = 0;
        for (Icon icon : icons) {
            int iw = icon.getIconWidth();
            int ih = icon.getIconHeight();
            if (iw > maxw) {
                maxw = iw;
            }
            if (ih > maxh) {
                maxh = ih;
            }
        }
        return new Dimension(maxw, maxh);
    }





    /**
     * Get the preferred size of an Icon. This will return a 0x0 dimension if the given Icon is null.
     *
     * @param c The component requesting the size.
     * @param icon The icon
     * @return The result.
     */
    public static Dimension getPreferredSize(Component c, Icon icon) {
        return icon == null ? new Dimension() : new Dimension(icon.getIconWidth(), icon.getIconHeight());
    }





    /**
     * Get the minimum size of the icon.
     *
     * @param c The component requesting the size.
     * @param icon The icon.
     * @return The result.
     */
    public static Dimension getMinimumSize(Component c, Icon icon) {
        Dimension result;
        if (icon instanceof ScalableIcon) {
            result = ((ScalableIcon) icon).fitInto(c, 0, 0);
        } else {
            result = getPreferredSize(c, icon);
        }
        return result;
    }





    /**
     * Get the maximum size of the icon.
     *
     * @param c The component requesting the size.
     * @param icon The icon.
     * @return The result.
     */
    public static Dimension getMaximumSize(Component c, Icon icon) {
        Dimension result;
        if (icon instanceof ScalableIcon) {
            result = ((ScalableIcon) icon).fitInto(c, Short.MAX_VALUE, Short.MAX_VALUE);
        } else {
            result = getPreferredSize(c, icon);
        }
        return result;
    }
}
