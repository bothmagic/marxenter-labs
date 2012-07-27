/*
 * $Id: Page.java 2997 2009-01-30 13:48:26Z kschaefe $
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
 * Location constants based on a page. {@code Page} constants are relative to the current locale.
 * 
 * @author Karl George Schaefer
 */
@SuppressWarnings("serial")
public final class Page extends RelativeLocation<Page> {
    /**
     * The central region of a page, where the content is.
     */
    public static final Page BODY = new Page("BODY");

    /**
     * In LR-TB printing, this is the top of the page, where the header is.
     */
    public static final Page BEFORE = new Page("BEFORE");

    /**
     * In LR-TB printing, this is the left of the page.
     */
    public static final Page LEADING = new Page("LEADING");

    /**
     * In LR-TB printing, this is the right of the page.
     */
    public static final Page TRAILING = new Page("TRAILING");

    /**
     * In LR-TB printing, this is the bottom of the page, where the footer
     * is.
     */
    public static final Page AFTER = new Page("AFTER");

    private Page(String name) {
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
    public static Page valueOf(String name) {
        return valueOf(Page.class, name);
    }

    /**
     * Returns an array containing the constants of this location type, in
     * the order they are declared. This method may be used to iterate over
     * the constants as follows:
     * 
     * <pre>
     * for (Page c : Page.values())
     *     System.out.println(c);
     * </pre>
     * 
     * @return an array containing the constants of this location type, in
     *         the order they are declared
     */
    public static Page[] values() {
        return values(Page.class);
    }
}