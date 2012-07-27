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

import junit.framework.TestCase;

import java.util.Calendar;
import java.util.Date;

import javax.swing.JFormattedTextField;
import javax.swing.text.DefaultFormatterFactory;

/**
 * Test cases for JXDatePicker with the alternative JXDatePickerDateFormatter.
 * All test cases have been copied from the original JXDatePickerTest and 
 * enhanced with the JXDatePickerDateFormatter.
 * 
 * @author mzehnder
 */
public class JXDatePickerWithDateFormatterTest extends TestCase {
    private Calendar cal;

    public void setUp() {
        cal = Calendar.getInstance();
    }

    public void teardown() {
    }

    public void testDefaultConstructor() {
        JXDatePicker datePicker = applyDateFormatter(new JXDatePicker());
        cal.setTimeInMillis(System.currentTimeMillis());
        Date today = cleanupDate(cal);
        assertTrue(today.equals(datePicker.getDate()));
        assertTrue(today.getTime() == datePicker.getDateInMillis());
    }

    public void testConstructor() {
        cal.setTimeInMillis(System.currentTimeMillis());
        cal.add(Calendar.DAY_OF_MONTH, 5);
        Date expectedDate = cleanupDate(cal);
        JXDatePicker datePicker = applyDateFormatter(new JXDatePicker(cal.getTimeInMillis()));
        assertTrue(expectedDate.equals(datePicker.getDate()));
        assertTrue(expectedDate.getTime() == datePicker.getDateInMillis());
    }

    public void testNullSelection() {
        JXDatePicker datePicker = applyDateFormatter(new JXDatePicker());
        datePicker.setDate(null);
        assertTrue(null == datePicker.getDate());
        assertTrue(-1 == datePicker.getDateInMillis());
    }

    public void testSetDate() {
        cal.setTimeInMillis(System.currentTimeMillis());
        cal.add(Calendar.DAY_OF_MONTH, 5);
        Date expectedDate = cleanupDate(cal);
        JXDatePicker datePicker = applyDateFormatter(new JXDatePicker());
        datePicker.setDate(cal.getTime());
        assertTrue(expectedDate.equals(datePicker.getDate()));
        assertTrue(expectedDate.equals(datePicker.getEditor().getValue()));

        datePicker.setDate(null);
        assertTrue(null == datePicker.getDate());
        assertTrue(null == datePicker.getEditor().getValue());
    }
    
    private JXDatePicker applyDateFormatter(JXDatePicker datePicker) {
        // replace the default formatter with another one
        JFormattedTextField dateField = datePicker.getEditor();
        dateField.setFormatterFactory(new DefaultFormatterFactory(
                new JXDatePickerDateFormatter(datePicker.getFormats())));
        return datePicker;
    }

    private Date cleanupDate(Calendar cal) {
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }
}