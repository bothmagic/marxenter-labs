/*
 * $Id: PilePolicy.java 2629 2008-08-06 08:27:49Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.icon.compound;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import javax.swing.Icon;

import java.util.ListIterator;
import java.util.Random;

import org.jdesktop.swingx.icon.CompoundIcon;
import org.jdesktop.swingx.icon.IconUtilities;
import org.jdesktop.swingx.util.LocationPolicy;

/**
 * CompoundPolicy that lays icons directly on top of one another but rotates
 * each subsequent icon by a certain amount. The rotation value is calculated
 * by a RotationType instance and the rotation property.
 * <p>
 * <dl>
 * <dt>FIXED</dt>
 * <dd>A rotationType of FIXED means that the rotation value specifies a fixed number
 * of radians to rotate the next icon by.</dd>
 * <dt>SPANNING</dt>
 * <dd>A rotation type of SPANNING means that the child icons are spread evenly
 * over the rotation radians.</dd>
 * <dt>RANDOM</dt>
 * <dd>A rotation type of RANDOM means that the rotation of children is
 * randomised.</dt>
 * </dl>
 * <p>
 * Note that the icon at the front of the stack is given a rotation of 0. Rotations are
 * performed in an Anti-clockwise direction, as defined by AffineTransform.
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 */
public class PilePolicy extends AbstractPolicy {
    /**
     * Defines how the rotation property is handled in this policy.
     *
     * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
     */
    public static enum RotationType {
        /**
         * The rotation is randomised. The icon at index 0 is always given a
         * rotation of 0.
         */
        RANDOM,
        /**
         * The rotation property defines how much the rotation changes each time.
         */
        FIXED,
        /**
         * The rotation property defines over what rotation the child icons should
         * be evenly spaced.
         */
        SPANNING;

        public double getRotation(CompoundIcon container, PilePolicy policy, int index) {
            double result = 0;
            switch (this) {
                case RANDOM:
                    Random r = new Random(policy.hashCode());
                    for (int i = 1; i < index; i++) {
                        r.nextDouble();
                    }
                    result = index == 0 ? 0 : r.nextDouble() * policy.getRotation();
                    break;
                case FIXED:
                    result = policy.getRotation() * index;
                    break;
                case SPANNING:
                    result = (index / (double) container.getSize()) * policy.getRotation();
                    break;
            }
            return result;
        }
    }

    /**
     * The type of rotation to perform.
     */
    private RotationType rotationType;

    /**
     * the value of the rotation hint.
     */
    private double rotation;

    /**
     * Create a new PilePolicy with a rotation of -1 and a type of FIXED.
     */
    public PilePolicy() {
        this( -0.1, RotationType.FIXED);
    }





    /**
     * Create a new PilePolicy with the given rotation and type.
     *
     * @param rotation The rotation for the background icons.
     * @param rotationType The type of rotation to apply.
     */
    public PilePolicy(double rotation, RotationType rotationType) {
        this(rotation, rotationType, 1f, ALL_VISIBLE);
    }





    /**
     * Create a new PilePolicy with the given scaleFactor and number of visible
     * icons. The rotation will be set to -0.1 and the type will be FIXED.
     *
     * @param scaleFactor The scale factor.
     * @param visibleIcons The number of visible icons.
     */
    public PilePolicy(float scaleFactor, int visibleIcons) {
        this( -0.1, RotationType.FIXED, scaleFactor, visibleIcons);
    }





    /**
     * Create a new PilePolicy with the given properties.
     *
     * @param rotation The rotation for the background icons.
     * @param rotationType The type of rotation to use.
     * @param scaleFactor The scale factor.
     * @param visibleIcons The number of visible icons.
     */
    public PilePolicy(double rotation, RotationType rotationType, float scaleFactor, int visibleIcons) {
        super(scaleFactor, visibleIcons);
        this.rotation = rotation;
        this.rotationType = rotationType;
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
     * Paints the given icons onto the given Graphics object. THis overrides the location policy of the child to be
     * CENTER always.
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
        int n = container.getSize();
        LocationPolicy location = LocationPolicy.valueOf(LocationPolicy.CENTER);
        int index = n - 1;
        for (ListIterator<Icon> iter = getOffsetIcons(container); iter.hasNext(); index--) {
            Icon icon = iter.next();
            paintChild(container, icon, index, c, g, x, y, width, height, location);
        }
    }





    /**
     * Overriden to transform the Graphics object to provide rotation support.
     *
     * @param container The containing Icon.
     * @param child The Icon to paint.
     * @param index the index of the icon to paint
     * @param c The component calling the painting.
     * @param g The graphics to paint to.
     * @param x The x coord of the whole icon.
     * @param y The y coord of the whole icon.
     * @param width The width of the whole icon.
     * @param height The height of the whole icon.
     * @param location The location to paint the child.
     */
    @Override
    protected void paintChild(CompoundIcon container, Icon child, int index, Component c, Graphics g, int x, int y, int width, int height,
                              LocationPolicy location) {
        Graphics2D g2 = (Graphics2D) g;
        double rotation = getRotationType().getRotation(container, this, index);

        AffineTransform old = g2.getTransform();
        AffineTransform trans = (AffineTransform) old.clone();
        trans.rotate(rotation, x + (width >> 1), y + (height >> 1));
        g2.setTransform(trans);

        assert width == height;
        double dim = width / Math.sqrt(2);
        double dif = (width - dim) / 2d;

        super.paintChild(container, child, index, c, g, x + (int) dif, y + (int) dif, (int) dim, (int) dim, location);

        g2.setTransform(old);
    }





    /**
     * {@inheritDoc}
     */
    public int getPolicyWidth(CompoundIcon icon) {
        Dimension result = IconUtilities.getLargestSize(icon);
        int hyp = (int) Math.hypot(result.width, result.height);
        return hyp;
    }





    /**
     * {@inheritDoc}
     */
    public int getPolicyHeight(CompoundIcon icon) {
        return getPolicyWidth(icon);
    }





    /**
     * {@inheritDoc}
     */
    public void removePolicyIcon(CompoundIcon container, Icon icon) {
    }





    /**
     * Get the rotation hint used to rotate child icons.
     *
     * @return The rotation value.
     */
    public double getRotation() {
        return rotation;
    }





    /**
     * Get the rotation type used to interpret the rotation property.
     *
     * @return The type of rotation to use.
     */
    public RotationType getRotationType() {
        return rotationType;
    }





    /**
     * Set the rotation hint for child rotations. The meaning of this property
     * changes depending on the RotationType used.
     *
     * @param rotation The rotation.
     * @see #setRotationType
     */
    public void setRotation(double rotation) {
        this.rotation = rotation;
    }





    /**
     * Set the type of rotation used on child icons. This value determines what
     * the rotation property means and calculates the rotation value from the
     * current context.
     *
     * @param rotationType The type of rotation to use.
     * @throws IllegalArgumentException when rotationType is null.
     * @see #setRotation
     */
    public void setRotationType(RotationType rotationType) {
        if (rotationType == null) {
            throw new IllegalArgumentException("rotationType cannot be null");
        }
        this.rotationType = rotationType;
    }





    /**
     * {@inheritDoc}
     */
    public Dimension fitInto(CompoundIcon icon, int width, int height) {
        return IconUtilities.getDefault(icon.getChildScalePolicy()).fitInto(
              new Dimension(getPolicyWidth(icon), getPolicyHeight(icon)), new Dimension(width, height), null);
    }
}
