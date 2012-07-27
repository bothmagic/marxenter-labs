/*
 * $Id: Validators.java 3307 2010-09-10 14:35:50Z kschaefe $
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
package org.jdesktop.swingx.validator;

/**
 * A mechanism for creating object validators.
 */
@SuppressWarnings("unchecked")
public final class Validators {
    /**
     * A {@code Validator} that always returns {@code true}.
     */
    @SuppressWarnings("rawtypes")
    private static Validator ALWAYS_VALID = new Validator() {
        /**
         * @param object
         *            unused
         * @return always {@code true}
         */
        @Override
        public boolean validate(Object object) {
            return true;
        }
    };
    
    /**
     * A {@code Validator} that always returns {@code false}.
     */
    @SuppressWarnings("rawtypes")
    private static Validator NEVER_VALID = new Validator() {
        /**
         * @param object
         *            unused
         * @return always {@code false}
         */
        @Override
        public boolean validate(Object object) {
            return false;
        }
    };
    
    public static <T> Validator<T> getAlwaysValidator() {
        return ALWAYS_VALID;
    }
    
    public static <T> Validator<T> getNeverValidator() {
        return NEVER_VALID;
    }
}
