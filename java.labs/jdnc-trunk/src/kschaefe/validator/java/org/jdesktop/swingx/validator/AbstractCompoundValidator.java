/*
 * $Id: AbstractCompoundValidator.java 2364 2008-03-27 12:58:16Z kschaefe $
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

import java.util.Arrays;
import java.util.List;

import org.jdesktop.swingx.util.Contract;

/**
 * An abstract base class for handling collections of validators.
 */
public abstract class AbstractCompoundValidator<T> implements Validator<T> {
    protected List<Validator<T>> validators;
    
    /**
     * Creates a validator the validates against the supplied collection of
     * validators.
     * 
     * @param validators
     *            the collection to manage
     */
    public AbstractCompoundValidator(Validator<T>... validators) {
        Contract.asNotNull(validators,
            "validators cannot be null and cannot contain null"); //$NON-NLS-1$
        this.validators = Arrays.asList(validators);
    }

    /**
     * @param e
     * @return
     * @see java.util.List#add(java.lang.Object)
     */
    public boolean add(Validator<T> e) {
        return validators.add(e);
    }

    /**
     * @param o
     * @return
     * @see java.util.List#remove(java.lang.Object)
     */
    public boolean remove(Object o) {
        return validators.remove(o);
    }
}
