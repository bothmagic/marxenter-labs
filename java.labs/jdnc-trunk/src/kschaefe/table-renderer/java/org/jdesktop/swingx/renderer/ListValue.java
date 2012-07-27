/*
 * $Id: ListValue.java 3165 2009-06-23 02:59:49Z kschaefe $
 * 
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle, Santa Clara,
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
package org.jdesktop.swingx.renderer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * A converter for {@link org.jdesktop.swingx.renderer.ComponentProvider} to give a {@link List}
 * representation of the supplied value.
 * 
 * @author Karl Schaefer
 */
public interface ListValue extends StringValue{
    /**
     * A default {@code ListValue} converter. If {@code value} is {@code null}, it returns an empty
     * list. If {@code value} is a {@code Collection}, it returns the collection as a {@code List}.
     * If {@code value} is an array, it converts the array to a {@code List}. If {@code value} is
     * any other type, it returns a {@code List} containing only {@code value}.
     */
    @SuppressWarnings("unchecked")
    ListValue DEFAULT_VALUE = new ListValue() {
        /**
         * {@inheritDoc}
         */
        @Override
        public List<?> getList(Object value) {
            if (value == null) {
                return Collections.emptyList();
            }

            if (value instanceof Collection) {
                return new ArrayList((Collection) value);
            }

            Object[] array = value.getClass().isArray()
                    ? (Object[]) value : new Object[] {value};

            return Arrays.asList(array);
        }

        public String getString(Object value) {
            return StringValues.TO_STRING.getString(getList(value));
        }
    };

    /**
     * Gets a list representation of the supplied value.
     * 
     * @param value
     *            the value to convert
     * @return a list representing the converted value
     */
    List<?> getList(Object value);
}
