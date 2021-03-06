/*
 * $Id: DefaultToStringTranslator.java 2230 2008-02-13 05:24:09Z headw01 $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
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

package org.jdesktop.swingx.util;


/**
 * A default helper class to translate an Object to a String.
 *
 */
public class DefaultToStringTranslator implements ToStringTranslator {
    /**
     * Shared instance of this class.
     */
    private static final DefaultToStringTranslator inst = new DefaultToStringTranslator();

    public DefaultToStringTranslator() {
    }

    /**
     * Get the shared instance of this class.
     */
    public static DefaultToStringTranslator getInstance() {
        return inst;
    }

    /**
     * Return a String for the given Object.<br>
     *
     * This is implemented as such:<br>
     * <code>
     * public String translateToString(Object obj) {
     *     return null != obj?obj.toString():null;
     * }
     * </code>
     *
     * @param obj The Object to translate into a String.
     */
    @Override
    public String translateToString(Object obj) {
        return null != obj?obj.toString():null;
    }
}
