/*
 * $Id: AbstractScalableIcon.java 2839 2008-10-27 12:13:55Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.icon;

import org.jdesktop.swingx.graphics.AbstractGraphicsRenderer;
import org.jdesktop.swingx.util.ScalePolicy;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

/**
 * Abstract base class for ScalableIcon instances. This implements much of the boilerplate code used by almost all
 * ScalableIcon types. This abstract class requires that sub-classes implement the basic three methods: <ul> <li>{@link
 * #getIconWidth()}</li> <li>{@link #getIconHeight()}</li> <li>{@link #paintIconImpl(java.awt.Component,
 * java.awt.Graphics2D,int,int,int,int)}</li> </ul>
 * <p/>
 * The standard Icon method {@link #paintIcon(java.awt.Component,java.awt.Graphics,int,int)} is implemented such that
 * {@link #paintIcon(java.awt.Component,java.awt.Graphics,int,int,int,int)} is called with the preferred icon width and
 * height.
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 */
public abstract class AbstractScalableIcon extends AbstractGraphicsRenderer implements ScalableIcon {
    /**
     * The scale policy used to determin the preferred size of this icon. This should never be null.
     */
    private ScalePolicy scalePolicy;

    /**
     * If true the painting area is explicitly clipped to fit the area the icon will be painting. This will enforce
     * that no painting can occur outside the icons area.
     */
    private boolean areaClipped = true;

    /**
     * If true then the area required to paint will always be within the fitInto methods constraints.
     */
    private boolean sizeConstrained = false;





    /**
     * Create the default AbstractScalableIcon with a ScalePolicy which fits the icon into the target area while
     * maintaining the aspect ratio.
     */
    public AbstractScalableIcon() {
        this(ScalePolicy.FIXED_RATIO);
    }





    /**
     * Create a new icon with the given scale policy. If a {@code null} policy is used then a non-resizing ScalePolicy
     * is used.
     *
     * @param scalePolicy The policy to use while scaling the icon.
     */
    public AbstractScalableIcon(ScalePolicy scalePolicy) {
        this.scalePolicy = IconUtilities.getDefault(scalePolicy);
    }





    /**
     * Implemented to delegate to {@link #paintIconImpl(java.awt.Component,java.awt.Graphics2D,int,int,int,int)} with a
     * graphics device that has been configured based on the AbstractGraphicsRenderer settings.
     *
     * @param c      The component calling the painting.
     * @param g      The Graphics to paint to.
     * @param x      The x coordinate to paint the icon.
     * @param y      The y coordinate to paint the icon.
     * @param width  The width to paint the icon.
     * @param height The height to paint the icon.
     */
    public void paintIcon(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2 = (Graphics2D) g.create();
        try {
            configureGraphics(g2);
            if (isSizeConstrained()) {
                // adjust the size to match the constraints - anchor top left
                Dimension targetSize = fitInto(c, width, height);
                width = targetSize.width;
                height = targetSize.height;
            }
            if (isAreaClipped()) {
                g2.clipRect(x, y, width, height);
            }
            paintIconImpl(c, g2, x, y, width, height);
        } finally {
            g2.dispose();
        }
    }





    /**
     * Sub-classes should override this method with their own painting code. The graphics passed to this class will be a
     * new instance and may be altered without worrying about side-effects.
     *
     * @param c      The component calling the paint.
     * @param g2     The graphics to paint to.
     * @param x      The x coordinate to paint to.
     * @param y      The y coordinate to paint to.
     * @param width  The width of the icon.
     * @param height The height of the icon.
     */
    protected abstract void paintIconImpl(Component c, Graphics2D g2, int x, int y, int width, int height);





    /**
     * Uses the ScalePolicy for this object to fit this icon in the given dimension. The default ScalePolicy has the
     * configuration {@link org.jdesktop.swingx.util.ScalePolicy.DimensionPolicy#FIXED_RATIO FIXED_RATIO}, {@link
     * org.jdesktop.swingx.util.ScalePolicy.ResizePolicy#BEST_FIT BEST_FIT}.
     *
     * @param c      The component calling the paint.
     * @param width  The target width for the icon.
     * @param height the target height for the icon.
     * @param result The results will be placed in here.
     * @return The dimension the icon will support.
     */
    public Dimension fitInto(Component c, int width, int height, Dimension result) {
        return getScalePolicy().fitInto(
                new Dimension(getIconWidth(), getIconHeight()),
                new Dimension(width, height),
                result);
    }





    /**
     * Uses the ScalePolicy for this object to fit this icon in the given dimension. The default ScalePolicy has the
     * configuration {@link org.jdesktop.swingx.util.ScalePolicy.DimensionPolicy#FIXED_RATIO FIXED_RATIO}, {@link
     * org.jdesktop.swingx.util.ScalePolicy.ResizePolicy#BEST_FIT BEST_FIT}.
     *
     * @param c      The component calling the paint.
     * @param width  The target width for the icon.
     * @param height the target height for the icon.
     * @return The dimension the icon will support.
     */
    public Dimension fitInto(Component c, int width, int height) {
        return fitInto(c, width, height, null);
    }





    /**
     * Calls {@code #paintIcon(c,g,x,y,getIconWidth(), getIconHeight())}
     *
     * @param c The component calling the paint.
     * @param g The graphics to paint to.
     * @param x The x coordinate to paint the icon at.
     * @param y The y coordinate to paint the icon at.
     * @see #paintIcon(java.awt.Component,java.awt.Graphics,int,int,int,int)
     */
    public void paintIcon(Component c, Graphics g, int x, int y) {
        paintIcon(c, g, x, y, getIconWidth(), getIconHeight());
    }





    /**
     * Get the policy used to determine this icons size within a given area. This will never return null.
     *
     * @return The policy used to scale this icon.
     * @see #setScalePolicy(org.jdesktop.swingx.util.ScalePolicy)
     */
    public ScalePolicy getScalePolicy() {
        return scalePolicy;
    }





    /**
     * Returns true if the area to be painted is clipped. This will remove the possibility of icons painting outside
     * their size if true.
     *
     * @return {@code true} if the icon should not be able to paint outside its area.
     */
    public boolean isAreaClipped() {
        return areaClipped;
    }





    /**
     * Returns whether the icon will always paint at a preferred size no matter what dimensions are passed to the
     * paintIcon method. This may have a small performance hit as generally if used properly this check will be
     * performed by the calling class. If constrained then the painting will be anchored in the top-left of the
     * requested area.
     *
     * @return {@code true} if the size constraints are enforced for painting.
     */
    public boolean isSizeConstrained() {
        return sizeConstrained;
    }





    /**
     * Set the scale policy for this icon. This policy is used by the {@link #fitInto(java.awt.Component,int,int)}
     * method to determin this icons preferred size.
     *
     * @param scalePolicy The scale policy for this icon.
     * @see #getScalePolicy()
     */
    public void setScalePolicy(ScalePolicy scalePolicy) {
        scalePolicy = IconUtilities.getDefault(scalePolicy);
        ScalePolicy old = getScalePolicy();
        this.scalePolicy = scalePolicy;
        firePropertyChange("scalePolicy", old, getScalePolicy());
    }





    /**
     * Sets whether the area that is going to be painted will be clipped to avoid bleeding.
     *
     * @param areaClipped {@code true} to clip the paint area.
     */
    public void setAreaClipped(boolean areaClipped) {
        boolean old = isAreaClipped();
        this.areaClipped = areaClipped;
        firePropertyChange("areaClipped", old, isAreaClipped());
    }





    /**
     * Sets whether the size is constrained on paint based on the fitInto method.
     *
     * @param sizeConstrained Whether the size should be constrained in the paint method.
     */
    public void setSizeConstrained(boolean sizeConstrained) {
        boolean old = isSizeConstrained();
        this.sizeConstrained = sizeConstrained;
        firePropertyChange("sizeConstrained", old, isSizeConstrained());
    }
}
