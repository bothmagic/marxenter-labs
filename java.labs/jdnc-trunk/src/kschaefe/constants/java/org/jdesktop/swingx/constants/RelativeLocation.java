/*
 * $Id: RelativeLocation.java 2997 2009-01-30 13:48:26Z kschaefe $
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
 * Location constants indicating the the location is relative. The location may be relative to
 * another location, component, or other arbitrary item. {@link Page} constants, for instance, are
 * relative to the current locale.
 * 
 * @author Karl George Schaefer
 */
@SuppressWarnings("serial")
public abstract class RelativeLocation<L> extends Location<RelativeLocation<L>> {
    /**
     * Creates an abstract enum with the specified name.
     * 
     * @param name
     *            the name of the enum
     * @throws IllegalStateException
     *             if two enums of the same class type have the same name
     */
    RelativeLocation(String name) {
        super(name);
    }
}