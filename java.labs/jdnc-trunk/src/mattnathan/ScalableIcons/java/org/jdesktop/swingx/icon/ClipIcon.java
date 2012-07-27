/*
 * $Id: ClipIcon.java 2628 2008-08-05 16:05:09Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.icon;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import javax.swing.Icon;

import org.jdesktop.swingx.util.ScalePolicy;

/**
 * Provides a simple icon that clips the underlying Icon before painting. This
 * icon provides two methods of clipping: in place clipping and size clipping.
 *
 * When using size clipping the preferred size of this icon will change to match
 * that of the clipped area. With in-place clipping the original size of the
 * icon will remain valid even though only a clipped area will be painted.
 *
 * This icon caches its preferred size to avoid recalculating is, call
 * invalidate if the size of the icon should be recalculated.
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 */
public class ClipIcon extends AbstractContainerIcon {
    /**
     * The clip applied to this child Icon. This can be {@code null}.
     *
     * @see #setClip
     * @see #getClip
     */
    private Shape clip;
    /**
     * The child icon which performs the painting.
     *
     * @see #setChild
     * @see #getChild
     */
    private Icon child;
    /**
     * {@code true} is the size should be taken from the clip area.
     *
     * @see #isSizeClipped
     * @see #setSizeClipped
     */
    private boolean sizeClipped;

    /**
     * Flag to hold the valid state of this icon.
     */
    private boolean valid = false;
    /**
     * The cache for the icon size.
     */
    private final Dimension size = new Dimension();

    /**
     * Create a new ClipIcon with no child or clip.
     */
    public ClipIcon() {
        this(null, null, false);
    }





    /**
     * Creates a new ClipIcon with the given child Icon.
     *
     * @param child The Icon which paints the content.
     */
    public ClipIcon(Icon child) {
        this(child, null, false);
    }





    /**
     * Create a new ClipIcon from the given clip and icon.
     *
     * @param child The child that performs the painting.
     * @param clip The clip area shape.
     */
    public ClipIcon(Icon child, Shape clip) {
        this(child, clip, false);
    }





    /**
     * Create a new fully configured ClipIcon.
     *
     * @param child The child Icon which paints the content.
     * @param clip The clip area.
     * @param sizeClipped {@code true} if the clip should determine the icons
     *   size.
     */
    public ClipIcon(Icon child, Shape clip, boolean sizeClipped) {
        super();
        setChild(child);
        this.clip = clip;
        this.sizeClipped = sizeClipped;
    }





    /**
     * Returns the icon's height.
     *
     * @return an int specifying the fixed height of the icon.
     */
    public int getIconHeight() {
        validate();
        return size.height;
    }





    /**
     * Returns the icon's width.
     *
     * @return an int specifying the fixed width of the icon.
     */
    public int getIconWidth() {
        validate();
        return size.width;
    }





    /**
     * Validates the size of this icon. This returns immediately if the size is
     * already valid.
     */
    public void validate() {
        if (!isValid()) {
            if (isSizeClipped()) {
                Rectangle bounds = getClipBounds();
                if (bounds == null) {
                    Icon child = getChild();
                    if (child == null) {
                        size.setSize(0, 0);
                    } else {
                        size.setSize(child.getIconWidth(), child.getIconHeight());
                    }
                } else {
                    size.setSize(bounds.getSize());
                    Icon child = getChild();
                    if (child != null) {
                        size.width = Math.min(child.getIconWidth(), size.width);
                        size.height = Math.min(child.getIconHeight(), size.height);
                    }

                }
            } else {
                Icon child = getChild();
                if (child == null) {
                    size.setSize(0, 0);
                } else {
                    size.setSize(child.getIconWidth(), child.getIconHeight());
                }
            }
            valid = true;
        }
    }





    /**
     * Returns true if the size of this icon is valid.
     *
     * @return {@code true} if the icons size is valid.
     */
    public boolean isValid() {
        return valid;
    }





    /**
     * Invalidates this icons size. The icon will be revalidated when an
     * operation requiring the icons size is invoked.
     */
    public void invalidate() {
        valid = false;
    }





    /**
     * Sub classes should override this method to implement their own painting
     * process.
     *
     * @param c The component calling the paint operation.
     * @param g The graphics to paint to.
     * @param x The x coordinate to paint the icon at.
     * @param y The y coordinate to paint the icon at.
     * @param width The width to paint the icon.
     * @param height The height to paint the icon.
     */
    @Override
    protected void paintIconImpl(Component c, Graphics2D g, int x, int y, int width, int height) {
        Icon child = getChild();
        if (child != null) {
            Shape clip = getClip();
            if (clip == null) {
                paintChildIcon(child, c, g, x, y, width, height);
            } else {
                if (isSizeClipped()) {
                    Rectangle clipBounds = clip.getBounds();
                    AffineTransform at = g.getTransform();
                    AffineTransform newTrans = AffineTransform.getTranslateInstance(x, y);
                    newTrans.concatenate(at);
//                    g.setTransform(newTrans);
                    g.translate(x, y);

                    g.translate( -clipBounds.getX(), -clipBounds.getY());
                    g.clip(clip);
                    g.translate(clipBounds.getX(), clipBounds.getY());

                    paintChildIcon(child, c, g, -clipBounds.x, -clipBounds.y, child.getIconWidth(), child.getIconHeight());

                    g.setTransform(at);

                } else {
                    Shape oldClip = g.getClip();
                    Area a = new Area(clip);
                    a.intersect(new Area(oldClip));
                    g.setClip(a);

                    paintChildIcon(child, c, g, x, y, width, height);
                    g.setClip(oldClip);
                }
            }
        }
    }





    /**
     * Gets the child Icon used for painting.
     *
     * @return The child icon.
     * @see #setChild(Icon)
     */
    public Icon getChild() {
        return child;
    }





    /**
     * Gets the clip area for this Icon. Changing properties for the returned
     * Shape will effect this Icon and should not be done.
     *
     * @return The clip area for the icon.
     * @see #getClip()
     */
    public Shape getClip() {
        return clip;
    }





    /**
     * Gets the bounds for this clip area. If the clip is {@code null} then this
     * returns {@code null}.
     *
     * @return The clips bounds.
     * @see #getClip()
     */
    public Rectangle getClipBounds() {
        return clip == null ? null : clip.getBounds();
    }





    /**
     * Returns true if the clip property determines the size of this Icon.
     *
     * @return {@code true} if the clip determines the size, {@code false} if the
     *   child Icon does.
     * @see #setSizeClipped(boolean)
     * @see #setChild(Icon)
     * @see #setClip(Shape)
     */
    public boolean isSizeClipped() {
        return sizeClipped;
    }





    /**
     * Sets the child to clip when painting. If isSizeClipped() returns {@code
     * false} the the size of this Icon is determined by this child Icon. {@code
     * null} values are allowed for the child icon and if given will paint
     * nothing.
     *
     * @param child The icon to paint inside the clip.
     * @see #getChild
     * @see #isSizeClipped
     */
    public void setChild(Icon child) {
        invalidate();
        Icon old = getChild();
        if (old != null) {
            uninstallChild(old);
        }
        this.child = child;
        if (child != null) {
            installChild(child);
        }
        firePropertyChange("child", old, getChild());
    }





    /**
     * Sets the clip area for this Icon. If the sizeClipped property is {@code
     * true} then this also changes the sizes of this Icon.
     *
     * @param clip The clip for painting the child Icon.
     * @see #getClip
     * @see #isSizeClipped
     */
    public void setClip(Shape clip) {
        invalidate();
        Shape old = getClip();
        this.clip = clip;
        firePropertyChange("clip", old, getClip());
    }





    /**
     * Sets whether the clip for this Icon represent the size. If {@code false}
     * then the original size of the child Icon is used to determine the size of
     * this Icon instead of the clip.
     *
     * @param sizeClipped {@code false} to use the original size of the child
     *   Icon.
     * @see #isSizeClipped
     * @see #getClip
     */
    public void setSizeClipped(boolean sizeClipped) {
        invalidate();
        boolean old = isSizeClipped();
        this.sizeClipped = sizeClipped;
        firePropertyChange("sizeClipped", old, isSizeClipped());
    }





    /**
     * Provides the size of the icon as it will depending on the clipping of the
     * icon.
     *
     * @param c The component this icon will be painted to.
     * @param width The target width for the icon.
     * @param height The target height for the icon.
     * @param result The object to place the result into.
     * @return The preferred size for this icon.
     */
    @Override
    public Dimension fitInto(Component c, int width, int height, Dimension result) {
        validate();
        if (result == null) {
            result = new Dimension();
        }
        Shape clip = getClip();
        if (clip == null) {
            fitInto(getChild(), c, width, height, result);
        } else {
            if (isSizeClipped()) {
                result.setSize(size);
            } else {
                ScalePolicy sp = getChildScalePolicy();
                if (sp == null) {
                    result.setSize(size);
                } else {
                    sp.fitInto(size, new Dimension(width, height), result);
                }
            }
        }
        return result;
    }
}
