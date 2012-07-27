/*
 * $Id: Movement.java 2997 2009-01-30 13:48:26Z kschaefe $
 *
 * Copyright 2009 Sun Microsystems, Inc., 4150 Network Circle,
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
package org.jdesktop.swingx.constants;

/**
 * @author Karl George Schaefer
 *
 */
@SuppressWarnings("serial")
public class Movement extends Direction<Movement> {
    /**
     * Identifies the current item in a sequence.
     */
    public static final Movement UP = new Movement("UP");

    /**
     * Identifies the current item in a sequence.
     */
    public static final Movement DOWN = new Movement("DOWN");
    
    /**
     * Identifies the current item in a sequence.
     */
    public static final Movement LEFT = new Movement("LEFT");
    
    /**
     * Identifies the current item in a sequence.
     */
    public static final Movement RIGHT = new Movement("RIGHT");
    
    private Movement(String name) {
        super(name);
    }

    /**
     * Returns the location constant of this type with the specified name.
     * The string must match exactly an identifier used to declare a
     * location constant in this type. (Extraneous whitespace characters are
     * not permitted.)
     * 
     * @param name
     *            the name of the location constant to be returned.
     * @return the location constant with the specified name
     * @throws IllegalArgumentException
     *             if this location type has no constant with the specified
     *             name
     * @throws NullPointerException
     *             if the argument is null
     */
    public static Movement valueOf(String name) {
        return valueOf(Movement.class, name);
    }

    /**
     * Returns an array containing the constants of this location type, in
     * the order they are declared. This method may be used to iterate over
     * the constants as follows:
     * 
     * <pre>
     * for (Flow c : Flow.values())
     *     System.out.println(c);
     * </pre>
     * 
     * @return an array containing the constants of this location type, in
     *         the order they are declared
     */
    public static Movement[] values() {
        return values(Movement.class);
    }
}
