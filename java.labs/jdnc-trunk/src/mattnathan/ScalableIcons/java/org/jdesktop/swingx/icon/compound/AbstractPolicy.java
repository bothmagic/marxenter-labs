/*
 * $Id: AbstractPolicy.java 2629 2008-08-06 08:27:49Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.icon.compound;

import java.awt.AlphaComposite;
import java.awt.Component;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import javax.swing.Icon;

import java.util.ListIterator;
import java.util.NoSuchElementException;

import org.jdesktop.swingx.icon.CompoundIcon;
import org.jdesktop.swingx.icon.IconUtilities;
import org.jdesktop.swingx.util.LocationPolicy;

/**
 * Defines a set of common properties for CompoundIcon.Policy implementations. This policy provides support for
 * specifying a scale factor which can be used to reduce the size of subsequent icons and a visibleIcons property which
 * can be used to specify how many icons are painted before the opacity fades to 0. Use CONTAINER_SIZE to make all icons
 * be painted but fading to transparent after the last icon.
 *
 * <p> For convenience a paintOffset property is included which can be used to specify which icon should be at the front
 * of the stack. This is a lot more performant than rearranging the child icons in CompoundIcon and should be used where
 * this behaviour is required.
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 */
public abstract class AbstractPolicy implements CompoundIcon.Policy {

    /**
     * The variable to use for setVisibleIcons if you want the icons to fade to
     * nothing at the containers size.
     */
    public static final int CONTAINER_SIZE = -1;
    /**
     * The variable to use if you want all icons to be opaque. this is the
     * default value.
     */
    public static final int ALL_VISIBLE = -2;

    /**
     * Defines the proportion of the size subsequent children will be painted at.
     */
    private float scaleFactor;
    /**
     * Defines how many icons will be visible before the opacity setting reaches
     * 0.
     */
    private int visibleIcons;

    /**
     * Defines which child icon should be at the front of the stack.
     */
    private int paintOffset = 0;

    public AbstractPolicy() {
        this(1, ALL_VISIBLE);
    }





    public AbstractPolicy(float scaleFactor, int visibleIcons) {
        this.scaleFactor = scaleFactor;
        this.visibleIcons = visibleIcons;
    }





    /**
     * Paint a given child on the graphics given.
     *
     * @param container The containing Icon.
     * @param child The Icon to paint.
     * @param index the index of the icon to paint
     * @param c The component calling the painting.
     * @param g The graphics to paint to.
     * @param x The x coordinate of the whole icon.
     * @param y The y coordinate of the whole icon.
     * @param width The width of the whole icon.
     * @param height The height of the whole icon.
     * @param location The location to paint the child.
     */
    protected void paintChild(CompoundIcon container, Icon child, int index, Component c, Graphics g, int x, int y, int width, int height,
                              LocationPolicy location) {

        float scale = (float) Math.pow(getScaleFactor(), index);
        float opacity = getOpcaityFactor();
        float alpha = 1;
        if (opacity < 0) {
            if (opacity == ALL_VISIBLE) {
                alpha = 1;
            } else if (opacity == CONTAINER_SIZE) {
                alpha = 1 - (index / (float) container.getSize());
            }
        } else {
            alpha = 1 - ((1 - getOpcaityFactor()) * index);
        }
        if (alpha > 0 && scale > 0) {
            Graphics2D g2 = (Graphics2D) g;
            // alpha
            Composite comp = g2.getComposite();
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));

            // scale
            int nw = (int) (width * scale);
            int nh = (int) (height * scale);

            Point p = location.locate(new Dimension(nw, nh), new Dimension(width, height), null);
            x += p.x;
            y += p.y;

            height = nh;
            width = nw;

            IconUtilities.paintChild(child, c, g, x, y, width, height, container.getChildLocationPolicy(), container.getChildScalePolicy());

            g2.setComposite(comp);
        }
    }





    /**
     * Returns the next icon in the list.
     *
     * @param icon The container.
     * @param index The current index.
     * @param numIcons The total number of icons.
     * @return The next index.
     */
    protected int getNextIconIndex(CompoundIcon icon, int index, int numIcons) {
        return (index + 1) % numIcons;
    }





    /**
     * Gets the index in container of the icon which should be placed at the
     * front.
     *
     * @param container The container.
     * @return The first icon given the offset.
     */
    protected int getFrontIconIndex(CompoundIcon container) {
        int offset = getPaintOffset();
        int size = container.getSize();
        int index = -1;
        if (size > 0) {
            index = offset % size;
        }
        return index;
    }





    /**
     * Returns an iterator that iterates through the children in the given icon
     * starting at the paintOffset and wrapping around where necessary. The
     * returned iterators add, set and remove methods are not supported.
     * <p>
     * The returned iterator will determine the starting element from the first
     * call to next or previous. If next is called first then the starting
     * position will be the paintOffset, if previous is called first then the
     * initial element will be the item before the paintOffset, wrapping if
     * necessary.
     *
     * @param icon The container.
     * @return An iterator which starts at the offset icon.
     */
    protected ListIterator<Icon> getOffsetIcons(final CompoundIcon icon) {
        return new ListIterator<Icon>() {
            private int size = icon.getSize();
            private int current = size == 0 ? 0 : getPaintOffset() % size;
            private int count = 0;
            private boolean countSet = false;
            public boolean hasNext() {
                return countSet ? count < size : size > 0;
            }





            public Icon next() {
                if (!countSet) {
                    count = 0;
                    countSet = true;
                }
                if (count >= size) {
                    throw new NoSuchElementException("Cannot return the next entry when none exist");
                }

                Icon result = icon.getIcon(current);
                current = nextIndex();
                count++;
                return result;
            }





            public int nextIndex() {
                return countSet ? (current + 1) % size : current;
            }





            public boolean hasPrevious() {
                return countSet ? count >= 0 : size > 0;
            }





            public Icon previous() {
                if (!countSet) {
                    countSet = true;
                    count = size;
                    current = previousIndex();
                }
                if (count < 0) {
                    throw new ArrayIndexOutOfBoundsException("Cannot return the previous entry when none exist");
                }
                Icon result = icon.getIcon(current);
                current = previousIndex();
                count--;
                return result;
            }





            public int previousIndex() {
                return countSet ? (current + size - 1) % size : (current + size - 1) % size;
            }





            public void remove() {
                throw new UnsupportedOperationException();
            }





            public void set(Icon o) {
                throw new UnsupportedOperationException();
            }





            public void add(Icon o) {
                throw new UnsupportedOperationException();
            }

        };
    }





    /**
     * Gets the factor by which subsequent children's opacity will be set.
     *
     * @return The opacity factor.
     */
    public float getOpcaityFactor() {
        return getVisibleIcons() < 0 ? getVisibleIcons() : 1 - (1 / (float) getVisibleIcons());
    }





    /**
     * Returns the number of icons that will be visible before the opacity
     * reaches 0.
     *
     * @return Number of visible icons.
     */
    public int getVisibleIcons() {
        return visibleIcons;
    }





    /**
     * Get the offset of the child which should be painted on top.
     *
     * @return The offset for the first icon.
     */
    public int getPaintOffset() {
        return paintOffset;
    }





    /**
     * Gets the factor by which subsequent child icons will be scaled.
     *
     * @return The scale factor.
     */
    public float getScaleFactor() {
        return scaleFactor;
    }





    /**
     * Set the factor by which subsequent children's opacity will be set.
     *
     * @param opcaityFactor The opacity value multiplier.
     */
    public void setOpcaityFactor(float opcaityFactor) {
        this.visibleIcons = (int) Math.ceil(1 / (1 - opcaityFactor));
    }





    /**
     * Set the number of icons that will be visible before their opacity reaches
     * 0. Set to -1 for all icons to be visible.
     *
     * @param visibleIcons The total number of visible icons.
     */
    public void setVisibleIcons(int visibleIcons) {
        this.visibleIcons = visibleIcons;
    }





    /**
     * Set the offset of the child icon which would be painted on top. The
     * actual index of the top child will be {@code paintOffset % numChildren}
     * and the order of children painted will follow in an increasing order
     * wrapping when necessary.
     *
     * @param paintOffset The first icon offset.
     */
    public void setPaintOffset(int paintOffset) {
        this.paintOffset = paintOffset;
    }





    /**
     * Set the factor by which subsequent children will be scaled. The size of
     * children is calculated via the formula: {@code s - (1 - scaleFactor) * s *
     * i} where s is the top child's size and i is the index of the subsequent
     * child.
     *
     * @param scaleFactor The factor by which icons will be scaled.
     */
    public void setScaleFactor(float scaleFactor) {
        this.scaleFactor = scaleFactor;
    }

}
