/*
 * Copyright 2007 Sun Microsystems, Inc., 4150 Network Circle, Santa Clara,
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
package org.jdesktop.swingx;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.UIManager;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for the JXDatePickerDateFormatter
 * 
 * @author mzehnder
 */
public class JXDatePickerDateFormatterTest {

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		UIManager.put("JXDatePicker.longFormat", 
			((SimpleDateFormat)SimpleDateFormat.getDateInstance(DateFormat.LONG)).toPattern());
		UIManager.put("JXDatePicker.mediumFormat",
			((SimpleDateFormat)SimpleDateFormat.getDateInstance(DateFormat.MEDIUM)).toPattern());
		UIManager.put("JXDatePicker.shortFormat",
			((SimpleDateFormat)SimpleDateFormat.getDateInstance(DateFormat.SHORT)).toPattern());
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link org.jdesktop.swingx.JXDatePickerDateFormatter#JXDatePickerDateFormatter()}.
	 */
	@Test
	public void testDefaultConstructor() {
		JXDatePickerDateFormatter formatter = new JXDatePickerDateFormatter();
		
		assertNotNull(formatter.getFormat());
		assertNotNull(formatter.getFormats());
	}

	/**
	 * Test method for {@link org.jdesktop.swingx.JXDatePickerDateFormatter#JXDatePickerDateFormatter(java.text.DateFormat[])}.
	 */
	@Test
	public void testConstructorWithDateFormatArray() {
		DateFormat[] dateFormatArray = null;
		JXDatePickerDateFormatter formatter = null;
		
		try {
			formatter = new JXDatePickerDateFormatter(dateFormatArray);
			fail("Constructor may not accept null");
		}catch (NullPointerException e) {
			// NPE is expected
		}
		
		dateFormatArray = new DateFormat[] { SimpleDateFormat.getDateInstance() };
		formatter = new JXDatePickerDateFormatter(dateFormatArray);
		
		assertEquals(dateFormatArray, formatter.getFormats());
		assertEquals(dateFormatArray[0], formatter.getFormat());
	}

	
	/**
	 * Test method for {@link org.jdesktop.swingx.JXDatePickerDateFormatter#getFormats()}.
	 */
	@Test
	public void testGetFormats() {
		JXDatePickerDateFormatter formatter = new JXDatePickerDateFormatter();

		DateFormat[] dateFormatArray = formatter.getFormats();

		assertNotNull(dateFormatArray);
		assertTrue(dateFormatArray.length == 3);
		
		assertEquals(dateFormatArray[0], new SimpleDateFormat(UIManager.getString("JXDatePicker.longFormat")));
		assertEquals(dateFormatArray[1], new SimpleDateFormat(UIManager.getString("JXDatePicker.mediumFormat")));
		assertEquals(dateFormatArray[2], new SimpleDateFormat(UIManager.getString("JXDatePicker.shortFormat")));
	}

	/**
	 * Test method for {@link org.jdesktop.swingx.JXDatePickerDateFormatter#getFormat()}.
	 */
	@Test
	public void testGetFormat() {
		JXDatePickerDateFormatter formatter = new JXDatePickerDateFormatter();

		DateFormat[] dateFormatArray = formatter.getFormats();
		
		assertNotNull(dateFormatArray);
		assertNotNull(formatter.getFormat());
		assertEquals(dateFormatArray[0], formatter.getFormat());
	}

	/**
	 * Test method for {@link org.jdesktop.swingx.JXDatePickerDateFormatter#stringToValue(java.lang.String)}.
	 */
	@Test
	public void testStringToValueString() {

        JXDatePickerDateFormatter formatter = new JXDatePickerDateFormatter();
        
        try {
        	assertNull(formatter.stringToValue(null));
        	assertNull(formatter.stringToValue(""));
        } catch (ParseException e) {
			fail(e.toString());
		}
        
        try {
        	formatter.stringToValue("invalid, this is not a date");
        	fail("invalid input accepted without throwing an exception");
		} catch (ParseException e) {
			// expected
		}
		
		DateFormat[] dateFormatArray = formatter.getFormats();
		
		Date now = new Date();
		
		for (DateFormat format : dateFormatArray) {
			String dateString = format.format(now);
        
			try {
				Object obj = formatter.stringToValue(dateString);
				
				assertNotNull(obj);
				assertEquals(format.format(obj), dateString);
			} catch (ParseException e) {
				fail("Unable to parse date string: " + dateString);
			}
		}
		
	}

	/**
	 * Test method for {@link org.jdesktop.swingx.JXDatePickerDateFormatter#valueToString(java.lang.Object)}.
	 */
	@Test
	public void testValueToStringObject() {
        JXDatePickerDateFormatter formatter = new JXDatePickerDateFormatter();
        
        try {
        	assertNull(formatter.valueToString(null));
        } catch (ParseException e) {
			fail(e.toString());
		}

        try {
        	formatter.valueToString("");
        	fail("valueToString accepted invalid value");
        } catch (Exception e) {
			// expected
		}
        
		DateFormat[] dateFormatArray = formatter.getFormats();
		Date now = new Date();
		
		try {
			assertEquals(dateFormatArray[0].format(now), formatter.valueToString(now));
		} catch (ParseException e) {
			fail("Unable to parse date");
		}	
	}

}
