/*
 * $Id: Converter.java 47 2004-09-08 18:57:44Z rbair $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */

package org.jdesktop.jdnc.incubator.rbair.swing.data;

/**
 * Interface for defining objects which perform bi-directional conversion
 * between string values and Java objects.  A unified conversion interface
 * is required for serializing and de-serializing data values to and from
 * a textual representation, which is a common requirement when interacting
 * with a network or web-based data source.
 * <p>
 * For many Java classes (Date, Color, etc), an instance may be
 * represented in a variety of string formats, hence both conversion methods
 * take an optional <code>format</code> parameter for specifying an
 * unambiguous string format to use during conversion.  A Converter class
 * must document the format classes it supports and must also accept a
 * <code>null</code> value for the <code>format</code> parameter,
 * in which case a suitable and well-documented default should be used.
 * Converters should support standard formats whenever possible.</p>
 *
 * @see Converters#getConverter
 *
 * @author Amy Fowler
 * @version 1.0
 */
public interface Converter {
    /**@todo aim: should converters honor null/"" values? */
    /**
     * Converts the specified Object value to a string representation.
     * The value must be an instance of the class associated with
     * this converter, else an exception will be thrown.
     * @param value the object to be converted
     * @param format object containing string format information, or null
     *        if format information is either not relevant or unspecified
     * @return String containing the converted string representation of the
     *         value
     * @throws ConversionException if the conversion could not be performed
     */
    String encode(Object value, Object format) throws ConversionException;

    /**
     * Converts the specified String value to an object that is an
     * instance of the class associated with this converter instance.
     * @param value String object to be converted
     * @param format object containing string format information, or null
     *        if format information is either not relevant or unspecified
     * @return Object which contains the converted value as an instance of
     *         the class associated with this converter
     * @throws ConversionException if the conversion could not be performed
     */
    Object decode(String value, Object format) throws ConversionException;
}
