/*
 * $Id: DatePickerPopup.java 2043 2007-12-17 00:58:28Z kschaefe $
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
package org.jdesktop.swingx.plaf.basic;

import java.awt.event.KeyListener;
import java.awt.event.MouseListener;

import org.jdesktop.swingx.JXMonthView;

/**
 * The interface which defines the methods required for the implementation of
 * the popup portion of a date picker.
 * 
 * @author Karl George Schaefer
 * @version 1.0
 */
public interface DatePickerPopup {
    /**
     * Shows the popup.
     */
    void show();

    /**
     * Hides the popup.
     */
    void hide();

    /**
     * Returns true if the popup is visible (currently being displayed).
     * 
     * @return <code>true</code> if the component is visible; <code>false</code> otherwise.
     */
    boolean isVisible();

    /**
     * Returns the view that is being used to select dates in the date picker.
     * This method is highly implementation specific and should not be used for
     * general manipulation.
     * 
     * @return the month view managed by this popup
     */
    JXMonthView getMonthView();
    
    /**
     * Returns a mouse listener that will be added to the date picker or null.
     * If this method returns null then it will not be added to the date picker.
     * 
     * @return a <code>MouseListener</code> or null
     */
    MouseListener getMouseListener();

    /**
     * Returns a key listener that will be added to the date picker or null.
     * If this method returns null then it will not be added to the date picker.
     */
    KeyListener getKeyListener();
}
