/*
 * $Id: CardIcon.java 2628 2008-08-05 16:05:09Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */

package org.jdesktop.swingx.icon;

import org.jdesktop.swingx.IconFactory;
import org.jdesktop.swingx.image.ImageUtilities;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import java.awt.AlphaComposite;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * This Icon defines a series of views that can be flipped like a CardLayout.
 * This Icon makes creating animated icons easy and provides convenience methods
 * for creating animations from a list of images, a single split image or
 * similar Icon instances.
 * <p>
 * The frames in this icon support sub-integer values and these are blended
 * between the two icons at paint time. The smallest frame index is painted
 * first in all occasions but when the frame index > getFrameCount() - 1 when
 * the smallest frame index is painted last.</p>
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 */
public class CardIcon extends AbstractContainerIcon {

    /**
     * Contains the frame icons for this container.
     */
    private List<Icon> painters = null;

    /**
     * The current frame.
     */
    private float frame = -1;
    /**
     * A cache of the icon size.
     */
    private final Dimension size = new Dimension();
    /**
     * Whether this icon is valid.
     */
    private boolean valid = false;

    /**
     * Creates a new CardIcon with no frames.
     */
    public CardIcon() {
        super();
    }





    /**
     * Creates a new CardIcon from the given Icon frames.
     *
     * @param frames The frames for this card icon.
     */
    public CardIcon(Icon ...frames) {
        setFrames(frames);
    }





    /**
     * Create a new CardIcon from the given frames and showing the given current frame.
     *
     * @param frame The frame to show.
     * @param frames The frames for this card.
     */
    public CardIcon(float frame, Icon ...frames) {
        this(frames);
        setFrame(frame);
    }





    /**
     * Creates a new CardIcon which creates its frames from the given image. This
     * assumes that the given image is a collection of horizontally tiled frames,
     * all with the same dimensions (width x height).
     *
     * <p>This class uses the ClipIcon to construct its frames from the single
     * source image, adjusting the clip region to display the region of the image
     * required.</p>
     *
     * @param image The image containing the tiled frames.
     * @param width The width of a frame.
     * @param height The height of a frame.
     */
    public CardIcon(Image image, int width, int height) {
        Icon icon = new ImageIcon(image);
        int w = icon.getIconWidth();
        int n = w / width;
        Icon[] icons = new Icon[n];
        for (int i = 0; i < n; i++) {
            icons[i] = new ClipIcon(icon, new Rectangle(i * width, 0, width, height), true);
        }
        setFrames(icons);
    }





    /**
     * Creates a new instance of a CardIcon wrapping the given images in icons.
     *
     * @param images The images that make up the frames.
     * @return The created card icon.
     */
    public static CardIcon createCardIcon(Image ...images) {
        Icon[] icons = new Icon[images.length];
        for (int i = 0, n = icons.length; i < n; i++) {
            icons[i] = IconFactory.createScalableIcon(images[i]);
        }
        return new CardIcon(icons);
    }





    /**
     * Replaces the current frames with the given Icons. This may update the
     * current frame.
     *
     * @param frames The Icons to set as this containers frames.
     */
    public void setFrames(Icon ...frames) {
        removeAll();
        for (Icon frame : frames) {
            add(frame);
        }
    }





    /**
     * Adds the given Icon to the end of the child list.
     *
     * @param icon The Icon to add.
     * @see #addImpl
     */
    public void add(Icon icon) {
        addImpl(icon, -1);
    }





    /**
     * Adds the given Icon at the given index. If index is {@code -1} then the
     * Icon is appended to the end of the content list.
     *
     * @param index The index to add the icon at.
     * @param icon The Icon to add.
     * @see #addImpl
     */
    public void add(int index, Icon icon) {
        addImpl(icon, index);
    }





    /**
     * The add implementation that all add(XXX) methods delegate to. If you
     * require additional functionality for the addition of frames the override
     * this method. If {@code -1} is given as an index then the Icon is appended
     * to the current list.
     *
     * @param icon The Icon to add.
     * @param index The index at which to add the given Icon.
     * @throws IndexOutOfBoundsException when index is not a valid content index.
     * @see #add(Icon)
     * @see #add(int,Icon)
     */
    protected void addImpl(Icon icon, int index) {
        if (painters == null) {
            painters = new ArrayList<Icon>();
        }
        if (index == -1) {
            painters.add(icon);
        } else {
            painters.add(index, icon);
        }
        invalidate();

        float oldFrame = getFrame();
        if (oldFrame == -1 || oldFrame >= index) {
            setFrame(oldFrame + 1);
        }
    }





    /**
     * Remove the frame at the given index in this container. This will adjust
     * the current frame to try and keep the same frame active.
     *
     * @param index The index of the frame to remove
     * @return The removed frame Icon.
     * @throws IndexOutOfBoundsException when index is not between 0 and
     *   getFrameCount.
     * @see #remove(Icon)
     * @see #add(Icon)
     */
    public Icon remove(int index) {
        if (index < 0 || index >= getFrameCount()) {
            throw new IndexOutOfBoundsException(index + " : " + getFrameCount());
        }
        Icon result = painters.remove(index);
        if (result != null) {
            invalidate();

            float oldFrame = getFrame();
            if (oldFrame >= index) {
                setFrame(Math.max(oldFrame - 1, painters.isEmpty() ? -1 : 0));
            }
        }
        return result;
    }





    /**
     * Removes the given icon from this container. This will adjust the current
     * frame to try and keep the same frame selected.
     *
     * @param icon The Icon to remove.
     * @return {@code true} is the icon was removed.
     */
    public boolean remove(Icon icon) {
        int index = indexOf(icon);
        return index != -1 && remove(index) != null;
    }





    /**
     * Removes all frames from this container. This will set the current frame to
     * -1 if the contents have changed.
     */
    public void removeAll() {
        if (painters != null && !painters.isEmpty()) {
            painters.clear();
            invalidate();
            setFrame( -1);
        }
    }





    /**
     * Returns the index in this Icon of the given frame icon.
     *
     * @param frame The frame to check the index of
     * @return The index of the given Icon of -1 if it does not exist.
     */
    public int indexOf(Icon frame) {
        return painters == null ? -1 : painters.indexOf(frame);
    }





    /**
     * Invalidates the size of this Icon. This is called when icons are added or
     * removed from this Icon.
     *
     * @see #validate
     */
    protected void invalidate() {
        valid = false;
    }





    /**
     * Validates the size caches of this icon. This method is called before
     * {@link #fitInto}, {@link #getIconHeight()} and {@link #getIconWidth()}
     * return. The implementation of this method will complete very quickly if
     * this Icon is already valid.
     *
     * @see #invalidate()
     */
    protected void validate() {
        if (!valid && painters != null) {
            size.setSize(IconUtilities.getLargestSize(painters));
            valid = true;
        }
    }





    /**
     * Returns the icon's height. This delegates to {@code preferredSize.height}.
     *
     * @return an int specifying the fixed height of the icon.
     */
    public int getIconHeight() {
        validate();
        return size.height;
    }





    /**
     * Returns the icon's width. This delegates to {@code preferredSize.width}.
     *
     * @return an int specifying the fixed width of the icon.
     */
    public int getIconWidth() {
        validate();
        return size.width;
    }





    /**
     * Returns the current frame for this icon.
     *
     * @return The index of the frame to be painted.
     * @see #setFrame(float)
     */
    public float getFrame() {
        return frame;
    }





    /**
     * Gets the number of frames this icon has.
     *
     * @return the total number of frames
     * @see #setFrame(float)
     * @see #getFrame()
     */
    public int getFrameCount() {
        return painters == null ? 0 : painters.size();
    }





    /**
     * Sets the current frame for this icon. Use -1 to show no frame.
     *
     * @param frame The new frame to display.
     * @throws IndexOutOfBoundsException when frame < -1 or >= frameCount
     * @see #getFrame()
     * @see #getFrameCount()
     */
    public void setFrame(float frame) {
        if (frame >= getFrameCount() || (frame < 0 && frame != -1)) {
            throw new IndexOutOfBoundsException(frame + " : " + getFrameCount());
        }
        float old = getFrame();
        this.frame = frame;
        firePropertyChange("frame", old, getFrame());
    }





    /**
     * Paints the current frame. This will blend the two closest frames if the
     * frame property is not an integer. This calls paintFrame to paint an
     * individual frame after blending composites have been set on the graphics
     * object.
     *
     * @param c The component calling the paint.
     * @param g The graphics to paint to.
     * @param x The x coordinate to paint to.
     * @param y The y coordinate to paint to.
     * @param width The width of the icon.
     * @param height The height of the icon.
     */
    @Override
    protected void paintIconImpl(Component c, Graphics2D g, int x, int y, int width, int height) {
        if (getFrameCount() > 0 && getFrame() >= 0) {
            float frame = getFrame();

            int floor = (int) Math.floor(frame);
            int ceil = ((int) Math.ceil(frame)) % getFrameCount();

            if (floor == ceil) {
                paintFrame(c, g, x, y, width, height, floor);
            } else {
                // paint floor and ceil on top of one another
                float percent = frame - floor;

                Shape clip = g.getClip();

                BufferedImage img = ImageUtilities.createCompatibleTranslucentImage(g, width, height);
                Graphics2D g2 = img.createGraphics();
                g2.translate( -x, -y);
                g2.setClip(clip);

                float lastAlpha = (float) Math.sin(Math.PI * (1 - percent) / 2f); // sin based curve
//            float lastAlpha = 1 - percent;
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, lastAlpha));
                paintFrame(c, g2, x, y, width, height, floor);

//            float nextAlpha = (float) Math.sin(Math.PI * (float) percent / 2f);
                //noinspection UnnecessaryLocalVariable
                float nextAlpha = percent;
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, nextAlpha));
                paintFrame(c, g2, x, y, width, height, ceil);

                g2.dispose();
                g.drawImage(img, x, y, null);
                ImageUtilities.releaseImage(img);
            }
        }
    }





    /**
     * Paint the given frame to the given graphics. This simply asks the current
     * frame to paint itself via its paintIcon method.
     *
     * @param c The component to paint the icon to.
     * @param g The graphics to paint the icon to.
     * @param x The x coord for the icon.
     * @param y The y coord for the icon.
     * @param width The width for the icon.
     * @param height The height for the icon.
     * @param frame The frame to paint.
     */
    protected void paintFrame(Component c, Graphics2D g, int x, int y, int width, int height, int frame) {
        paintChildIcon(painters.get(frame), c, g, x, y, width, height);
    }
}
