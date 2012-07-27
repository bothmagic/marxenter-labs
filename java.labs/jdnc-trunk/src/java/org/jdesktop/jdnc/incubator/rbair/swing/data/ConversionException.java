/*
 * $Id: ConversionException.java 47 2004-09-08 18:57:44Z rbair $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */

package org.jdesktop.jdnc.incubator.rbair.swing.data;

/**
 * Thrown by Converter instances when errors occur during value conversion.
 * @see Converter
 *
 * @author Amy Fowler
 * @version 1.0
 */
public class ConversionException extends Exception {

    /**
     * Instantiates conversion exception for error which occurred when
     * attempting to convert the specified object value to a string.
     * @param value object which could not be converted
     * @param fromClass class of object value being converted
     */
    public ConversionException(Object value, Class fromClass) {
        this("could not convert value from class " + fromClass.getName() +
             "to a string");
    }

    /**
     * Instantiates conversion exception for error which occurred when
     * attempting to convert the specified object value to a string.
     * @param value object value which could not be converted
     * @param fromClass class of object value being converted
     * @param cause the specific throwable which caused conversion failure
     */
    public ConversionException(Object value, Class fromClass, Throwable cause) {
        this("could not convert value from class " + fromClass.getName() +
             "to a string", cause);
    }

    /**
     * Instantiates conversion exception for error which occurred when
     * attempting to convert the specified string value to an object
     * of the specified class.
     * @param value string value which could not be converted
     * @param toClass class the value was being converted to
     */
    public ConversionException(String value, Class toClass) {
        this("could not convert string value \"" + value + "\" to " +
             toClass.getName());
    }

    /**
     * Instantiates conversion exception for error which occurred when
     * attempting to convert the specified string value to an object
     * of the specified class.
     * @param value object value which could not be converted
     * @param toClass class of object value being converted
     * @param cause the specific throwable which caused conversion failure
     */
    public ConversionException(String value, Class toClass, Throwable cause) {
        this("could not convert string value \"" + value + "\" to " +
             toClass.getName(), cause);
    }

    /**
     * Instantiates conversion exception.
     * @param message String containing description of why exception occurred
     */
    public ConversionException(String message) {
        super(message);
    }

    /**
     * Instantiates conversion exception.
     * @param message String containing description of why exception occurred
     * @param cause the specific throwable which caused conversion failure
     */
    public ConversionException(String message, Throwable cause) {
        super(message, cause);
    }

}
