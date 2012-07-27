package org.jdesktop.swingx.icon;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.Icon;

import java.util.ArrayList;
import java.util.Iterator;

import org.jdesktop.swingx.icon.compound.OverlayPolicy;
import org.jdesktop.swingx.util.EmptyIterator;
import org.jdesktop.swingx.util.LocationPolicy;

public class CompoundIcon extends AbstractContainerIcon implements Iterable<Icon> {
    /**
     * The policy used to paint the icons.
     */
    private Policy policy;
    /**
     * The list of icons that represent this icons children.
     */
    private java.util.List<Icon> icons;

    /**
     * Creates a new default CompoundIcon with an OverlayPolicy.
     */
    public CompoundIcon() {
        this(new OverlayPolicy());
    }





    /**
     * Create a new CompoundIcon with an OverlayPolicy and the given child Icons.
     *
     * @param icons The icons to use as children for this container.
     */
    public CompoundIcon(Icon ...icons) {
        this(new OverlayPolicy(), icons);
    }





    /**
     * Create  a new CompoundIcon with the given policy.
     *
     * @param policy The policy to use for laying out these icons.
     */
    public CompoundIcon(Policy policy) {
        this(policy, (Icon[])null);
    }





    /**
     * Create a new CompoundIcon with the given CompundPolicy and children.
     * <p>
     * All {@code CompoundIcon} constructors delegate here.
     *
     * @param policy The policy to use to lay out these icons.
     * @param icons The icons to add as children for this container.
     */
    public CompoundIcon(Policy policy, Icon ...icons) {
        this.policy = policy;
        setIcons(icons);
    }





    /**
     * Replaces the child list with the given icons.
     *
     * @param icons The icons to set as this containers children.
     */
    public void setIcons(Icon ...icons) {
        Policy policy = getCompoundPolicy();
        Icon[] old = getIcons();
        this.icons = null;
        if (old != null && policy != null) {
            for (int i = old.length - 1; i >= 0; i--) {
                remove(i);
            }
        }
        if (icons != null) {
            for (Icon icon : icons) {
                add(icon);
            }
        }
        firePropertyChange("icons", old, getIcons());
    }





    /**
     * Add the given icon to the end of the child list.
     *
     * @param icon Icon
     */
    public void add(Icon icon) {
        addImpl(icon, null, -1);
    }





    /**
     * Add the given icon to the child list at the given index.
     *
     * @param index int
     * @param icon Icon
     */
    public void add(Icon icon, int index) {
        addImpl(icon, null, index);
    }





    /**
     * Add an icon with the given constraints to the end of the child list.
     *
     * @param icon Icon
     * @param constraints Object
     */
    public void add(Icon icon, Object constraints) {
        addImpl(icon, constraints, -1);
    }





    /**
     * Add an Icon to the specified index with the given constraints.
     *
     * @param icon Icon
     * @param constraints Object
     * @param index int
     */
    public void add(Icon icon, Object constraints, int index) {
        addImpl(icon, constraints, index);
    }





    /**
     * Performs the logic behind removing an Icon from this Compound Icon. All
     * add method delegate here.
     *
     * @param icon Icon
     * @param constraints Object
     * @param index int
     */
    protected void addImpl(Icon icon, Object constraints, int index) {
        if (icons == null) {
            icons = new ArrayList<Icon>();
        }

        if (index == -1) {
            icons.add(icon);
        } else {
            icons.add(index, icon);
        }
        Policy policy = getCompoundPolicy();
        if (policy != null) {
            policy.addPolicyIcon(this, icon, constraints);
        }

    }





    /**
     * Gets the Icon at the specified index in this CompoundIcon.
     *
     * @param index int
     * @return Icon
     */
    public Icon getIcon(int index) {
        if (icons == null) {
            throw new IndexOutOfBoundsException(index + ":0");
        }
        return icons.get(index);
    }





    /**
     * Returns the number of child icons this compound icon contains.
     *
     * @return int
     */
    public int getSize() {
        return icons == null ? 0 : icons.size();
    }





    /**
     * Returns the index of the given icon in the child list. {@code -1} is
     * returned if the icon cannot be found.
     *
     * @param icon Icon
     * @return int
     */
    public int indexOf(Icon icon) {
        return icons == null ? -1 : icons.indexOf(icon);
    }





    /**
     * Remove the Icon at the given index from the child list.
     *
     * @param index int
     * @return The removed icon or null if no icon was removed.
     */
    public Icon remove(int index) {
        Icon result = null;
        if (icons != null && index >= 0 && index < icons.size()) {
            result = icons.remove(index);
            if (result != null) {
                Policy policy = getCompoundPolicy();
                if (policy != null) {
                    policy.removePolicyIcon(this, result);
                }
            }
        }
        return result;
    }





    /**
     * Removes the given Icon from the child list.
     *
     * @param icon Icon
     * @return true if the icon was removed.
     */
    public boolean remove(Icon icon) {
        return remove(indexOf(icon)) != null;
    }





    /**
     * Returns the icon's height.
     *
     * @return an int specifying the fixed height of the icon.
     */
    public int getIconHeight() {
        Policy policy = getCompoundPolicy();
        return policy == null ? 0 : policy.getPolicyHeight(this);
    }





    /**
     * Returns the icon's width.
     *
     * @return an int specifying the fixed width of the icon.
     */
    public int getIconWidth() {
        Policy policy = getCompoundPolicy();
        return policy == null ? 0 : policy.getPolicyWidth(this);
    }





    /**
     * Gets the policy responsible for painting the child icons.
     *
     * @return Policy
     */
    public Policy getCompoundPolicy() {
        return policy;
    }





    /**
     * Sub classes should override this method to implement their own painting
     * process.
     *
     * @param c Component
     * @param g Graphics
     * @param x int
     * @param y int
     * @param width int
     * @param height int
     */
    @Override
    protected void paintIconImpl(Component c, Graphics2D g, int x, int y, int width, int height) {
        Policy policy = this.policy;
        if (policy != null) {
            policy.paintIcons(this, c, g, x, y, width, height);
        } else {
            LocationPolicy locaiton = getChildLocationPolicy();
            for (Icon icon : getIcons()) {
                IconUtilities.paintChild(icon, c, g, x, y, width, height, locaiton);
            }
        }
    }





    /**
     * Gets the child icons for this Icon. This is guaranteed to not return null.
     *
     * @return Icon[]
     */
    public Icon[] getIcons() {
        return icons == null ? new Icon[0] : icons.toArray(new Icon[icons.size()]);
    }





    /**
     * Returns an Iterator that will traverse the child Icons in this Compound
     * Icon.
     *
     * @return Iterator
     */
    public Iterator<Icon> iterator() {
        return icons == null ? EmptyIterator.<Icon> getInstance() : icons.iterator();
    }





    @Override
    public Dimension fitInto(Component c, int width, int height, Dimension result) {
        result = super.fitInto(c, width, height, result);
        Policy policy = getCompoundPolicy();
        if (policy == null) {
        } else {
            result.setSize(policy.fitInto(this, result.width, result.height));
        }
        return result;
    }





    /**
     * This interface represents the layout and painting policy of a collection of
     * Icons in a CompoundIcon.
     *
     * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
     */
    public static interface Policy {
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
        public void paintIcons(CompoundIcon container, Component c, Graphics g, int x, int y, int width, int height);





        /**
         * Get the preferred width of this policy.
         *
         * @param icon the source for this policy.
         * @return The width of the policy.
         */
        public int getPolicyWidth(CompoundIcon icon);





        /**
         * Get the preferred height of this policy.
         *
         * @param icon The source for this policy
         * @return The height of the policy.
         */
        public int getPolicyHeight(CompoundIcon icon);





        /**
         * Get the size of this policy within the given bounds.
         *
         * @param icon the source for this policy
         * @param width The width of the bounding rectangle.
         * @param height The height of the bounding rectangle.
         * @return The size of the resulting layout.
         */
        public Dimension fitInto(CompoundIcon icon, int width, int height);





        /**
         * This method is called when an icon is added to the parent.
         *
         * @param container The container the icon was added to.
         * @param icon The icon to add.
         * @param constraints Any constraints associated with the icon.
         * @see #removePolicyIcon
         */
        public void addPolicyIcon(CompoundIcon container, Icon icon, Object constraints);





        /**
         * This method is called when an Icon is removed from the parent.
         *
         * @param container The container the icon was removed from.
         * @param icon The icon to remove.
         * @see #addPolicyIcon
         */
        public void removePolicyIcon(CompoundIcon container, Icon icon);





        /**
         * Clear any cached information.
         *
         * @param container The container icon.
         */
        public void invalidatePolicy(CompoundIcon container);
    }

}
