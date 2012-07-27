/*
 * Copyright 2007 Sun Microsystems, Inc., 4150 Network Circle,
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
package org.jdesktop.swingx;

import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.swing.UIManager;
import javax.swing.text.DateFormatter;

/**
 * Formatter for the <code>JXDatePicker</code> component that extends <code>DateFormatter</code>
 * and allows to spin through the date fields with the up and down cursor keys.
 * Otherwise it behaves like the default formatter <code>JXDatePickerFormatter</code>.
 * This factory creates and returns a formatter that can handle a variety of date
 * formats.
 *
 * @author Joshua Outwater
 * @author Markus Zehnder
 */
public class JXDatePickerDateFormatter 
        extends DateFormatter implements IDatePickerFormats {
	private static final long serialVersionUID = 1L;
    
	private DateFormat _formats[] = null;

    /**
     * Constructs a new <code>JXDatePickerFormatter</code> and initializes it 
     * with the three formats defined in the UIManager properties: 
     * JXDatePicker.longFormat, JXDatePicker.mediumFormat and JXDatePicker.shortFormat
     */
    public JXDatePickerDateFormatter() {
        _formats = new DateFormat[3];
        _formats[0] = new SimpleDateFormat(UIManager.getString("JXDatePicker.longFormat"));
        _formats[1] = new SimpleDateFormat(UIManager.getString("JXDatePicker.mediumFormat"));
        _formats[2] = new SimpleDateFormat(UIManager.getString("JXDatePicker.shortFormat"));
    }

    /**
     * Constructs a new <code>JXDatePickerFormatter</code> and initializes it with the specified
     * <code>DateFormat(s)</code>. If there are more than one <code>DateFormat</code> specified,
     * they must be ordered according to their complexity with the most complex one first, otherwise
     * the format detection might assign a simpler format over another more complex format!<p>
     *  
     * Example: "EEE MM/dd/yyyy", "MM/dd/yyyy", "MM/dd"
     * 
     * @param formats one or more <code>DateFormat</code> objects starting with the most complex
     * format pattern first. May not be null or a <code>NullPointerException</code> will be thrown
     */
    public JXDatePickerDateFormatter(DateFormat... formats) {
    	if (formats == null) {
    		throw new NullPointerException("At least one DateFormat must be specified");
    	}
    	
        _formats = formats;
    }

    /**
     * Returns an array of the formats used in this formatter.
     *
     * @return array of formats, never null.
     */
    public DateFormat[] getFormats() {
    	return _formats;
    }
    
    /**
     * Returns the first format that dictates the legal values that can be edited
     * and displayed.
     *
     * @return Format instance used for converting from/to Strings
     * @see getFormats
     */
//	@Override
	public Format getFormat() {
		return _formats[0];
	}

    /**
     * {@inheritDoc}
     */
    public Object stringToValue(String text) throws ParseException {
        Object result = null;
        ParseException pex = null;

        if (text == null || text.trim().length() == 0) {
            return null;
        }

        // If the current formatter did not work loop through the other
        // formatters and see if any of them can parse the string passed
        // in.
        for (DateFormat _format : _formats) {
            try {
                result = (_format).parse(text);
                pex = null;
                break;
            } catch (ParseException ex) {
                pex = ex;
            }
        }

        if (pex != null) {
            throw pex;
        }

        return result;
    }

    /**
     * {@inheritDoc}
     */
    public String valueToString(Object value) throws ParseException {
        if (value != null) {
        	if (_formats == null || _formats[0] == null) {
        		return value.toString();
        	}
        	
            return _formats[0].format(value);
        }
        
        return null;
    }
}