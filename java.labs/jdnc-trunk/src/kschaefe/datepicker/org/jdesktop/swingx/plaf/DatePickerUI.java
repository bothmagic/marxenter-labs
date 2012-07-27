/*
 * $Id: DatePickerUI.java 1773 2007-09-27 20:47:20Z kschaefe $
 * 
 * Copyright 2006 Sun Microsystems, Inc., 4150 Network Circle, Santa Clara,
 * California 95054, U.S.A. All rights reserved.
 * 
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA
 */
package org.jdesktop.swingx.plaf;

import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;

import org.jdesktop.swingx.JXDatePicker;

/**
 * Pluggable look and feel interface for JXDatePicker.
 * 
 * @author Joshua Outwater
 * @author Jeanette Winzenburg
 * @author Karl Schaefer
 * @version 1.0
 */
public abstract class DatePickerUI extends ComponentUI {
    /**
     * Returns the baseline.  The baseline is measured from the top of
     * the component.  This method is primarily meant for
     * <code>LayoutManager</code>s to align components along their
     * baseline.  A return value less than 0 indicates this component
     * does not have a reasonable baseline and that
     * <code>LayoutManager</code>s should not align this component on
     * its baseline.
     * <p>
     * This method returns -1.  Subclasses that have a meaningful baseline
     * should override appropriately.
     *
     * @param c <code>JComponent</code> baseline is being requested for
     * @param width the width to get the baseline for
     * @param height the height to get the baseline for
     * @throws NullPointerException if <code>c</code> is <code>null</code>
     * @throws IllegalArgumentException if width or height is &lt; 0
     * @return baseline or a value &lt; 0 indicating there is no reasonable
     *                  baseline
     */
    public int getBaseline(JComponent c, int width, int height) {
        //copied from 1.6 ComponentUI.getBaseline(Component, int, int)
        if (c == null) {
            throw new NullPointerException("Component must be non-null");
        }
        if (width < 0 || height < 0) {
            throw new IllegalArgumentException("Width and height must be >= 0");
        }
        return -1;
    }

    /**
     * Determine the visibility of the popup.
     * 
     * @param c
     *                the component to determine the visibility for
     * @return {@code true} if the popup is visible, {@code false} otherwise
     */
    public abstract boolean isPopupVisible(JXDatePicker c);
    
    /**
     * Sets the visiblity of the popup.
     * 
     * @param c
     *                the component to set the popup visibility for
     * @param v
     *                {@code true} to make the popup visible, {@code false}
     *                otherwise
     */
    public abstract void setPopupVisible(JXDatePicker c, boolean v);

    /** 
     * Determine whether or not the date picker itself is traversable. 
     * 
     * @param c
     *                the component to test
     */
    public abstract boolean isFocusTraversable(JXDatePicker c);
}
