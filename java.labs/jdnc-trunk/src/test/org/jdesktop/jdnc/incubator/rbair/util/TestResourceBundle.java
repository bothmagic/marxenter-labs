/*
 * $Id: TestResourceBundle.java 31 2004-09-07 20:20:42Z rbair $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.jdnc.incubator.rbair.util;

import java.io.InputStream;

import junit.framework.TestCase;

import org.jdesktop.jdnc.incubator.rbair.util.ResourceBundle;

/**
 * @author Richard Bair
 */
public class TestResourceBundle extends TestCase {
	private ResourceBundle res;

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		res = ResourceBundle.getBundle("org.jdesktop.jdnc.incubator.rbair.util.test");
	}

	/*
	 * @see TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		res = null;
	}

	/**
	 * Constructor for TestResourceBundle.
	 * @param arg0
	 */
	public TestResourceBundle(String string) {
		super(string);
	}

	/*
	 * Class under test for String getString
	 */
	public void testGetString() {
		assertEquals(res.getString("ok_button_text"), "Ok");
		assertEquals(res.getString("cancel_button_text"), "Cancel");
		assertNull(res.getString("not_in_res_bundle"));
		assertEquals(res.getString("not_in_res_bundle", "Yo"), "Yo");
	}

	/*
	 * Class under test for char getChar
	 */
	public void testGetChar() {
		assertEquals(res.getChar("ok_button_mnemonic"), 'O');
		assertEquals(res.getChar("cancel_button_mnemonic"), 'C');
		assertEquals(res.getChar("not_in_res_bundle_mnemonic"), '\0');
		assertEquals(res.getChar("not_in_res_bundle_mnemonic", 'x'), 'x');
	}

	/*
	 * Class under test for ImageIcon get16x16Icon
	 */
	public void testGet16x16Icon() {
		//TODO
	}

	/*
	 * Class under test for ImageIcon get24x24Icon
	 */
	public void testGet24x24Icon() {
		//TODO
	}

	/*
	 * Class under test for ImageIcon get32x32Icon
	 */
	public void testGet32x32Icon() {
		//TODO
	}

	/*
	 * Class under test for ImageIcon get48x48Icon
	 */
	public void testGet48x48Icon() {
		//TODO
	}

	public void testGetIcon() {
		//TODO
	}

	public void testGetURL() {
		assertNotNull(
			res.getURL("org/jdesktop/jdnc/incubator/rbair/util/test.properties"));		
	}

	public void testGetInputStream() {
		try {
			InputStream is = res.getInputStream(
					"org/jdesktop/jdnc/incubator/rbair/util/test.properties");
			assertNotNull(is);
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
			assertTrue(false);
		}
	}

}
