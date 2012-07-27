/*
 * $Id: RolloverModel.java 2628 2008-08-05 16:05:09Z mattnathan $
 *
 * Copyright 2005 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */

package org.jdesktop.swingx;

import java.awt.event.ActionListener;
import java.awt.event.ItemListener;

import org.jdesktop.swingx.event.DynamicObject;

/**
 * Model for specifying rollover state. This is a subset of the states available
 * through a ButtonModel. This model is most effective when managed by the
 * RolloverManager which will keep this models state up to date.
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 * @version 2.0
 */
public interface RolloverModel extends DynamicObject {

    /**
     * Indicates partial commitment towards pressing the
     * component.
     *
     * @return true if the component is armed, and ready to be pressed
     * @see #setArmed
     */
    boolean isArmed();





    /**
     * Indicates if the component has been selected. Only needed for
     * certain types of buttons - such as radio buttons and check boxes.
     *
     * @return true if the component is selected
     */
    boolean isSelected();





    /**
     * Indicates if the component can be selected or pressed by
     * an input device (such as a mouse pointer). (Check boxes
     * are selected, regular buttons are "pressed".)
     *
     * @return true if the component is enabled, and therefore
     *         selectable (or pressable)
     */
    boolean isEnabled();





    /**
     * Indicates if the component has been pressed.
     *
     * @return true if the component has been pressed
     */
    boolean isPressed();





    /**
     * Indicates that the mouse is over the component.
     *
     * @return true if the mouse is over the component
     */
    boolean isRollover();





    /**
     * Marks the component as "armed". If the mouse component is
     * released while it is over this item, the component's action event
     * fires. If the mouse component is released elsewhere, the
     * event does not fire and the component is disarmed.
     *
     * @param b true to arm the component so it can be selected
     */
    public void setArmed(boolean b);





    /**
     * Selects or deselects the component.
     *
     * @param b true selects the component,
     *          false deselects the component.
     */
    public void setSelected(boolean b);





    /**
     * Enables or disables the component.
     *
     * @param b true to enable the component
     * @see #isEnabled
     */
    public void setEnabled(boolean b);





    /**
     * Sets the component to pressed or unpressed.
     *
     * @param b true to set the component to "pressed"
     * @see #isPressed
     */
    public void setPressed(boolean b);





    /**
     * Sets or clears the component's rollover state
     *
     * @param b true to turn on rollover
     * @see #isRollover
     */
    public void setRollover(boolean b);





    /**
     * Sets the actionCommand string that gets sent as part of the
     * event when the component is pressed.
     *
     * @param s the String that identifies the generated event
     */
    public void setActionCommand(String s);





    /**
     * Returns the action command for this component.
     *
     * @return the String that identifies the generated event
     * @see #setActionCommand
     */
    public String getActionCommand();





    /**
     * Adds an ActionListener to the component.
     *
     * @param l the listener to add
     */
    void addActionListener(ActionListener l);





    /**
     * Removes an ActionListener from the component.
     *
     * @param l the listener to remove
     */
    void removeActionListener(ActionListener l);





    /**
     * Adds an ItemListener to the component.
     *
     * @param l the listener to add
     */
    void addItemListener(ItemListener l);





    /**
     * Removes an ItemListener from the component.
     *
     * @param l the listener to remove
     */
    void removeItemListener(ItemListener l);
}
