/*
 * $Id: BasicMouseUtilities.java 3296 2010-08-03 17:52:57Z kschaefe $
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

import java.awt.Component;
import java.awt.Point;
import java.awt.Robot;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;

import javax.swing.SwingUtilities;

/**
 * A singleton {@link Robot} wrapper that provides query mechanisms to ensure actions can be
 * performed. This class is safe and will not cause exception when used inside a secure environment.
 * <p>
 * All uses should check whether it is safe to call the mouse manipulation methods.
 * <pre>
 * if (canMoveMouse()) {
 *     [...]
 *     moveMouseTo(locationOnScreen);
 * } else {
 *     [do something else]
 * }
 * </pre>
 * 
 * @author kschaefer
 */
final class BasicMouseUtilities {
    private static final Robot MOUSE_MOVER;
    
    static {
        Robot r = null;
        try {
            r = AccessController.doPrivileged(new PrivilegedExceptionAction<Robot>() {
                @Override
                public Robot run() throws Exception {
                    return new Robot();
                }
            });
        } catch (PrivilegedActionException ignore) {
        } catch (SecurityException ignore) { }
        
        MOUSE_MOVER = r;
    }
    
    private BasicMouseUtilities() {
        //prevent instantiation
    }
    
    /**
     * Determines whether is it safe to call any other method from this class.
     * 
     * @return {@code true} if it is safe to call other methods; {@code false} otherwise
     */
    public static boolean canMoveMouse() {
        return MOUSE_MOVER != null;
    }

    /**
     * Moves the mouse to the specified location in the component.
     * 
     * @param location
     *            the location to place the mouse
     * @param inComponent
     *            the component space for the specified location
     * @throws NullPointerException
     *             if {@code location} is {@code null}
     * @throws AssertionError
     *             if assertions are enabled and {{@link #canMoveMouse()} returns {@code false}
     */
    public static void moveMouseTo(Point location, Component inComponent) {
        SwingUtilities.convertPointToScreen(location, inComponent);
        moveMouseTo(location);
    }

    /**
     * Moves the mouse to the specified location on the screen.
     * 
     * @param locationOnScreen
     *            the location to place the mouse
     * @throws NullPointerException
     *             if {@code locationOnScreen} is {@code null}
     * @throws AssertionError
     *             if assertions are enabled and {{@link #canMoveMouse()} returns {@code false}
     */
    public static void moveMouseTo(Point locationOnScreen) {
        assert canMoveMouse();
        
        if (MOUSE_MOVER != null) {
            MOUSE_MOVER.mouseMove(locationOnScreen.x, locationOnScreen.y);
        }
    }
}
