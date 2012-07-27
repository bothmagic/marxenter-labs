/*
 * $Id: Box.java 2997 2009-01-30 13:48:26Z kschaefe $
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
 * Location constants based on a box.
 * 
 * @author Karl George Schaefer
 */
@SuppressWarnings("serial")
public final class Box extends FixedLocation<Box> {
    /**
     * The central position in the box.
     */
    public static final Box CENTER = new Box("CENTER");

    /**
     * Constant used to specify the top of a box.
     */
    public static final Box TOP = new Box("TOP");

    /**
     * Constant used to specify the left side of a box.
     */
    public static final Box LEFT = new Box("LEFT");

    /**
     * Constant used to specify the bottom of a box.
     */
    public static final Box BOTTOM = new Box("BOTTOM");

    /**
     * Constant used to specify the right side of a box.
     */
    public static final Box RIGHT = new Box("RIGHT");

    private Box(String name) {
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
    public static Box valueOf(String name) {
        return valueOf(Box.class, name);
    }

    /**
     * Returns an array containing the constants of this location type, in
     * the order they are declared. This method may be used to iterate over
     * the constants as follows:
     * 
     * <pre>
     * for (Box c : Box.values())
     *     System.out.println(c);
     * </pre>
     * 
     * @return an array containing the constants of this location type, in
     *         the order they are declared
     */
    public static Box[] values() {
        return values(Box.class);
    }
}