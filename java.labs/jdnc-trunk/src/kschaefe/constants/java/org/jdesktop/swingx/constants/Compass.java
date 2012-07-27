/*
 * $Id: Compass.java 2997 2009-01-30 13:48:26Z kschaefe $
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
 * Location constants based on a compass. A class can choose to support only
 * the cardinal compass directions by checking {@link #isCardinal()}.
 * 
 * @author Karl George Schaefer
 */
@SuppressWarnings("serial")
public final class Compass extends FixedLocation<Compass> {
    /**
     * The central position in the compass.
     */
    public static final Compass CENTER = new Compass("CENTER", true);

    /**
     * Compass-direction North (up).
     */
    public static final Compass NORTH = new Compass("NORTH", true);

    /**
     * Compass-direction north-east (upper right).
     */
    public static final Compass NORTH_EAST = new Compass("NORTH_EAST",
            false);

    /**
     * Compass-direction east (right).
     */
    public static final Compass EAST = new Compass("EAST", true);

    /**
     * Compass-direction south-east (lower right).
     */
    public static final Compass SOUTH_EAST = new Compass("SOUTH_EAST",
            false);

    /**
     * Compass-direction south (down).
     */
    public static final Compass SOUTH = new Compass("SOUTH", true);

    /**
     * Compass-direction south-west (lower left).
     */
    public static final Compass SOUTH_WEST = new Compass("SOUTH_WEST",
            false);

    /**
     * Compass-direction west (left).
     */
    public static final Compass WEST = new Compass("WEST", true);

    /**
     * Compass-direction north west (upper left).
     */
    public static final Compass NORTH_WEST = new Compass("NORTH_WEST",
            false);

    private final boolean cardinal;

    private Compass(String name, boolean cardinal) {
        super(name);

        this.cardinal = cardinal;
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
    public static Compass valueOf(String name) {
        return valueOf(Compass.class, name);
    }

    /**
     * Returns an array containing the constants of this location type, in
     * the order they are declared. This method may be used to iterate over
     * the constants as follows:
     * 
     * <pre>
     * for (Compass c : Compass.values())
     *     System.out.println(c);
     * </pre>
     * 
     * @return an array containing the constants of this location type, in
     *         the order they are declared
     */
    public static Compass[] values() {
        return values(Compass.class);
    }

    /**
     * Determines if this compass direction is a cardinal direction. Note
     * that {@link #CENTER} is considered a cardinal direction.
     * 
     * @return {@code true} if the direction is cardinal
     */
    public final boolean isCardinal() {
        return cardinal;
    }
}