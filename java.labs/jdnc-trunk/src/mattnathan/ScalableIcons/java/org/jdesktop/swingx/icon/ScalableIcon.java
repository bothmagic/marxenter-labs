/*
 * $Id: ScalableIcon.java 2833 2008-10-23 14:17:20Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.icon;

import javax.swing.Icon;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;

/**
 * <p>A reusable and scalable painting routine (or icon).</p>
 *
 * <p>This extends the default Icon interface by altering the specification so that it is no longer the case that the
 * 'picture' needs to be of fixed size. Instead this interface defines a picture that can be painted at any size while
 * also providing a default size and scaling hints.</p>
 *
 * <p>The preferred (or default) size for the picture is provided by the {@link javax.swing.Icon#getIconWidth()} and
 * {@link javax.swing.Icon#getIconHeight()} methods.</p>
 *
 * <p>To paint the picture onto a Graphics target call the {@link org.jdesktop.swingx.icon.ScalableIcon#paintIcon}
 * method, providing the required bounds that the picture should be painted into. If the
 * {@link javax.swing.Icon#paintIcon} method is called intead then the required behaviour is to paint the icon at the
 * default size, i.e. in the form:
 * <pre><code>
 * public void paintIcon(Component c, Graphics g, int x, int y) {
 *     // forward Icon.paintIcon calls to ScalableIcon.paintIcon at default size
 *     paintIcon(c, g, x, y, getWidth(), getHeight());
 * }
 * </code></pre></p>
 *
 * <p>This API does not impose any restrictions on how a scalable icon is to be scaled, as such it is possible for
 * parts of the pictre to scale at a different rate to the rest of the picture. A notable example of this could be when
 * a border is painted around a central picture, the border may need to be exactly 1 pixel wide but the icon needs to be
 * scaled. To provide this level of control it is needed for the icon its self to determin what it's preferred painting
 * area is given a requested painting area, this is the purpose of the {@link #fitInto} method.</p>
 *
 * <p>Possible examples of using the {@code fitInto} method are to create icons that only scale horizontally, (like a
 * banner), icons that have a maximum/minimum size (to avoit artifacts), icons with non-linear aspect ratio
 * (like having a fixed sized border) or even icons that have a stepped scale behaviour (i.e. 16x16 and 32x32 sizeonly)
 * </p>
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 */
public interface ScalableIcon extends Icon {
    /**
     * Paint this icon onto the given {@link Graphics} at the specified coordinates with the given dimensions. If the
     * dimensions passed are larger than this icons maximum size or smaller that this icons minimum size then it is up
     * to this icon instance to determin what to do. The recommended action is to honour the given parameters wherever
     * possible however when this cannot be achieved the icon should try to paint as close to these dimensions as
     * possible.
     *
     * @param c      The component which caused the painting.
     * @param g      The graphics device to paint to.
     * @param x      The x coordinate for the icon to paint at.
     * @param y      The y coordinate for the icon to paint at.
     * @param width  The target width for the icon.
     * @param height The target height for the icon.
     */
    public void paintIcon(Component c, Graphics g, int x, int y, int width, int height);





    /**
     * Allows the icon to specify the best supported size which will fit into the given dimensions. This method should
     * never return {@code null}.
     *
     * @param c      The target component which will be passed when paintIcon is called.
     * @param width  The target width for the icon.
     * @param height The target height for the icon.
     * @return The best fitting supported dimension for this icon inside the given dimensions.
     */
    public Dimension fitInto(Component c, int width, int height);
}
