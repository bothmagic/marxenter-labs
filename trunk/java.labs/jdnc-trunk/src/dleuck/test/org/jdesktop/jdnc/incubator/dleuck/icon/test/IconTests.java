/*
 * $Id: IconTests.java 692 2005-09-19 02:50:17Z dleuck $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 *
 * This code is part of the Java icon package (JIC) donated by Ikayzo.com
 */
package org.jdesktop.jdnc.incubator.dleuck.icon.test;

import java.awt.Color;
import java.util.SortedSet;

import javax.swing.Icon;
import static javax.swing.SwingConstants.*;

import org.jdesktop.jdnc.incubator.dleuck.icon.CompoundIcon;
import org.jdesktop.jdnc.incubator.dleuck.icon.JIconFile;
import org.jdesktop.jdnc.incubator.dleuck.icon.ShapeIcon;
import junit.framework.TestCase;

/**
 * @author Richard Bair
 */
public class IconTests extends TestCase {

	/**
	 * Constructor for IconTests.
	 */
	public IconTests(String string) {
		super(string);
	}

	public void testCompoundIcons() {
		Icon icon1 = new CompoundIcon(new ShapeIcon(5,5,Color.red),
				new ShapeIcon(7,7,Color.green));
		
		assertEquals(icon1.getIconWidth(), 12);
		assertEquals(icon1.getIconHeight(), 7);
		
		Icon icon2 = new CompoundIcon(new ShapeIcon(5,5,Color.red),
			new ShapeIcon(7,7,Color.green), SOUTH, CENTER);
		assertEquals(icon2.getIconWidth(), 7);
		assertEquals(icon2.getIconHeight(), 12);		
	}
	
	public void testJIconFile() throws Exception {
		JIconFile file = new JIconFile(IconTests.class
				.getResource("resources/goblet.jic"));
		SortedSet<Integer> sizes = file.getPhysicalSizes("default");
		assertEquals(sizes.size(), 2);
		assertTrue(sizes.contains(16));
		assertTrue(sizes.contains(32));
		
		assertTrue(file.getVariants().contains("shadow"));
		assertTrue(file.getIcon("shadow", 32)!=null);
		
		assertEquals(file.getIcon("shadow", 24).getIconWidth(), 24);
	}
}
