/*
 * $Id: Validator.java 47 2004-09-08 18:57:44Z rbair $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */

package org.jdesktop.jdnc.incubator.rbair.swing.data;

import java.util.Locale;

/**
 * Interface for defining an object which performs validation checks
 * on a value object to determine whether or not it is valid.
 *
 * @author Amy Fowler
 * @version 1.0
 */
public interface Validator {
    /**@todo aim: change String array to StringBuffer */
    /**
     * Determines whether or not the specified value is valid.  If
     * validation passes, returns <code>true</code>.  If
     * validation fails, returns <code>false</code> and an
     * appropriate localized error message will be placed in the
     * first index of the error String array.
     *
     * @param value the value to be validated
     * @param locale Locale object which should be used to encode any
     *        returned error messages
     * @param error String array used to return an error message if
     *        validation fails
     * @return boolean indicating whether or not the specified object
     *         is valid
     */
    boolean validate(Object value, Locale locale, String[] error);

}
