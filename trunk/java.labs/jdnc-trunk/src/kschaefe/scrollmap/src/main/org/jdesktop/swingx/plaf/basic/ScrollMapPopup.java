/*
 * $Id: ScrollMapPopup.java 3296 2010-08-03 17:52:57Z kschaefe $
 *
 * Copyright 20102006 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */
package org.jdesktop.swingx.plaf.basic;

/**
 * The interface which defines the methods required for the implementation of the popup portion of a
 * scroll map.
 * 
 * @author kschaefer
 */
public interface ScrollMapPopup {

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
     * @return {@code true} if the component is visible; {@code false} otherwise.
     */
    boolean isVisible();

    /**
     * Called to inform the ComboPopup that the UI is uninstalling. If the ComboPopup added any
     * listeners in the component, it should remove them here.
     */
    void uninstallingUI();
}
