/*
 * $Id: IntegerRangeValidator.java 2364 2008-03-27 12:58:16Z kschaefe $
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
 * {@code IntegerRangeValidator} ensures that the integer is between an upper
 * and lower bound (inclusive).
 */
public class IntegerRangeValidator extends AbstractRangeValidator<Integer> {
    /**
     * Creates a validator for ensuring that integers are between
     * {@code lowerBound} and {@code upperBound}.
     * 
     * @param lowerBound
     *            the lowest valid value
     * @param upperBound
     *            the highest valid value
     * @throws NullPointerException
     *             if {@code lowerBound} or {@code upperBound} is {@code null}
     * @throws IllegalArgumentException
     *             if {@code lowerBound} is larger than {@code upperBound}
     */
    public IntegerRangeValidator(Integer lowerBound, Integer upperBound) {
        super(lowerBound, upperBound);
    }
}
