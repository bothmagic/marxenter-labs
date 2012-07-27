/*
 * $Id: AbstractRangeValidator.java 3307 2010-09-10 14:35:50Z kschaefe $
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
 * {@code AbstractRangeValidator} ensures that the comparable is between an upper
 * and lower bound (inclusive).
 */
public class AbstractRangeValidator<T extends Comparable<T>> implements Validator<T> {
    private T lowerBound;
    private T upperBound;
    
    /**
     * Creates a validator for ensuring that comparables are between
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
    public AbstractRangeValidator(T lowerBound, T upperBound) {
        if (lowerBound == null) {
            throw new NullPointerException(
                    "lowerBound cannot be null"); //$NON-NLS-1$
        }
        
        if (upperBound == null) {
            throw new NullPointerException(
                    "upperBound cannot be null"); //$NON-NLS-1$
        }
        
        if (lowerBound.compareTo(upperBound) > 0) {
            throw new IllegalArgumentException(
                    "lowerBound cannot exceed upperBound"); //$NON-NLS-1$
        }
        
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean validate(T object) {
        if (object == null) {
            return false;
        }
        System.out.println("AbstractRangeValidator.validate()");
        System.out.println(object);
        
        boolean result = lowerBound.compareTo(object) <= 0;
        System.out.println("lower = " + result);
        
        result = object.compareTo(upperBound) <= 0;
        System.out.println("upper = " + result);
        
        return lowerBound.compareTo(object) <= 0
                && object.compareTo(upperBound) <= 0;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return getClass().getName() + "[lowerBound=" + lowerBound + ",upperBound=" + upperBound + "]";
    }
}
