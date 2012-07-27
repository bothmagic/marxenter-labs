/*
 * $Id: TestStringUtils.java 32 2004-09-07 21:36:57Z rbair $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.jdnc.incubator.rbair.util;

import junit.framework.TestCase;

/**
 * @author Richard Bair
 */
public class TestStringUtils extends TestCase {
	private String simpleString = "Hello, my name is Hal";
	
	private String plainString = 
		"The goal of the JDesktop Network Components (JDNC) project is to " +
		"significantly reduce the effort and expertise required to build " +
		"rich, data-centric, Java desktop clients for J2EE-based network " +
		"services. These clients are representative of what enterprise " +
		"developers typically build, such as SQL database frontends, " +
		"forms-based workflow, data visualization applications, and the like.";
	
	private String htmlString = 
		"<html><body><p>The goal of the <strong><font color=\"#FF0000\">" +
		"<b>J</b></font></strong><b><font color=\"#FF0000\">D</font>esktop" +
		"<font color=\"#FF0000\">N</font>etwork <font color=\"#FF0000\">C" +
		"</font>omponents</b>(<font color=\"#FF0000\"><b>JDNC</b></font>) " +
		"project is to significantly reduce the effort and expertise required " +
		"to build rich, data-centric, Java desktop clients for J2EE-based " +
		"network services. These clients are representative of what " +
		"enterprise developers typically build, such as SQL database " +
		"frontends, forms-based workflow, data visualization applications, " +
		"and the like.</p></body></html>";
	
	/**
	 * Constructor for TestStringUtils.
	 * @param string
	 */
	public TestStringUtils(String string) {
		super(string);
	}

	/*
	 * Class under test for String replaceAll(String, String, String)
	 */
	public void testReplaceAllStringStringString() {
		String results = StringUtils.replaceAll(simpleString, "Hal", "Richard");
		assertEquals(results, "Hello, my name is Richard");
	}

	/*
	 * Class under test for void replaceAll(StringBuffer, String, String)
	 */
	public void testReplaceAllStringBufferStringString() {
		StringBuffer buffer = new StringBuffer(simpleString);
		StringUtils.replaceAll(buffer, "Hal", "Richard");
		assertEquals(buffer.toString(), "Hello, my name is Richard");
	}

	public void testReplaceAllOnePass() {
		//replace all of the html in htmlString with nothing, thus constructing
		//the plainString
		//The following substitution will not work until after StringUtils
		//replaceAllOnePass is rewritten. The method works as advertised, but
		//needs to be made to work with all substitutions.
//		StringUtils.Substitution[] subs = new StringUtils.Substitution[] {
//				new StringUtils.DefaultSubstitution("<html>", ""),
//				new StringUtils.DefaultSubstitution("</html>", ""),
//				new StringUtils.DefaultSubstitution("<body>", ""),
//				new StringUtils.DefaultSubstitution("</body>", ""),
//				new StringUtils.DefaultSubstitution("<a>", ""),
//				new StringUtils.DefaultSubstitution("</a>", ""),
//				new StringUtils.DefaultSubstitution("<b>", ""),
//				new StringUtils.DefaultSubstitution("</b>", ""),
//				new StringUtils.DefaultSubstitution("<i>", ""),
//				new StringUtils.DefaultSubstitution("</i>", ""),
//				new StringUtils.DefaultSubstitution("<strong>", ""),
//				new StringUtils.DefaultSubstitution("</strong>", ""),
//				new StringUtils.DefaultSubstitution("<font color=\"#FF0000\">", ""),
//				new StringUtils.DefaultSubstitution("</font>", "")
//		};
//		StringBuffer buffer = new StringBuffer(htmlString);
//		StringUtils.replaceAllOnePass(buffer, subs);
//		assertEquals(buffer.toString(), plainString);
		
		//do an octal substitution
//		String octalString = "\\112\\104\\116\\103";
//		String nonOctalString = "JDNC";
//		StringUtils.Substitution[] subs = new StringUtils.Substitution[] {
//				new StringUtils.OctalSubstitution(3)
//		};
//		StringBuffer buffer = new StringBuffer(octalString);
//		StringUtils.replaceAllOnePass(buffer, subs);
//		assertEquals(buffer.toString(), nonOctalString);
	}

	public void testHasHtml() {
		assertTrue(StringUtils.hasHtml(htmlString));
		assertFalse(StringUtils.hasHtml(plainString));
	}

	public void testNameTitleCase() {
		String caps = "RICHARD A. BAIR";
		String mixed = "RiChArD a. BaIr";
		String lower = "richard a. bair";
		
		assertEquals(StringUtils.nameTitleCase(caps), "Richard A. Bair");
		assertEquals(StringUtils.nameTitleCase(mixed), "Richard A. Bair");
		assertEquals(StringUtils.nameTitleCase(lower), "Richard A. Bair");
	}

	public void testUppercaseFirstLetter() {
		String caps = "RICHARD A. BAIR";
		String mixed = "RiChArD a. BaIr";
		String lower = "richard a. bair";
		
		assertEquals(StringUtils.uppercaseFirstLetter(caps), caps);
		assertEquals(StringUtils.uppercaseFirstLetter(mixed), mixed);
		assertEquals(StringUtils.uppercaseFirstLetter(lower), "Richard a. bair");
	}

	public void testNameTitleCaseJavaName() {
		String simple = "single";
		String complex = "tripleScoopIceCream";
		
		assertEquals(StringUtils.nameTitleCaseJavaName(simple), "Single");
		assertEquals(StringUtils.nameTitleCaseJavaName(complex), "Triple Scoop Ice Cream");
	}

	public void testGetJavaWordEndIndex() {
		String name = "myJavaName";
		assertEquals(StringUtils.getJavaWordEndIndex(3, name), 5);
	}

	public void testSplit() {
		String text = "there|should|be|five|strings";
		String[] results = StringUtils.split(text, '|');
		assertTrue(results.length == 5);
		assertEquals(results[0], "there");
		assertEquals(results[1], "should");
		assertEquals(results[2], "be");
		assertEquals(results[3], "five");
		assertEquals(results[4], "strings");
	}

	public void testCountOf() {
		assertEquals(StringUtils.countOf(plainString, "the"), 3);
	}

	public void testRemoveHtmlOpenCloseTags() {
		String text = "<html>bla blah<p>blah<p>blah blah</p></html>";
		assertEquals(StringUtils.removeHtmlOpenCloseTags(text), 
				"bla blah<p>blah<p>blah blah</p>");
	}

}
