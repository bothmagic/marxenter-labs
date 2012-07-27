/*
 * $Id: CompoundIcon.java 673 2005-09-15 23:24:57Z dleuck $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 *
 * This code is part of the Java icon package (JIC) donated by Ikayzo.com
 */
package org.jdesktop.jdnc.incubator.dleuck.icon;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.Icon;
import javax.swing.SwingConstants;

/**
 * <p>An icon that allows two icons to be composited using a number of
 * different layouts.</p>
 * 
 * <p>To get two icons positioned next to one another horizontally using centered
 * vertical alignment.</p>
 *
 * <code>
 * CompoundIcon ci = new CompoundIcon(
 *     icon1,
 *     icon2,
 *     SwingConstants.EAST,
 *     SwingContants.CENTER
 * );
 * </code>
 * 
 * @author Daniel Leuck
 */
public class CompoundIcon implements Icon, SwingConstants {
	
	private Icon icon1, icon2;

	private int location, alignment;

	/**
	 * Create a component icon.
	 * 
	 * @param icon1 The main icon
	 * @param icon2 The icon to be positioned relative to the main icon
	 * @param location The location of the icon2 relative to icon1.
	 *     Options include NORTH, WEST, SOUTH, or EAST.
	 * @param alignment The alignment of icon2 relative to icon1.  Options
	 *     for NORTH and SOUTH alignments are LEFT, CENTER, or RIGHT.  Options
	 *     for WEST and EAST alignments are TOP, CENTER, and BOTTOM
	 */
	public CompoundIcon(Icon icon1, Icon icon2, int location, int alignment) {	
		this.icon1=icon1;	
		this.icon2=icon2;
		this.location=location;
		this.alignment=alignment;
	}

	/**
	 * Create a component icon using CENTER alignment.
	 * 
	 * @param icon1 The main icon
	 * @param icon2 The icon to be positioned relative to the main icon
	 * @param location The location of the icon2 relative to icon1.
	 *     Options include NORTH, WEST, SOUTH, or EAST.
	 */
	public CompoundIcon(Icon icon1, Icon icon2, int location) {	
		this(icon1, icon2, location, CENTER);
	}

	/**
	 * Defaults to EAST position and CENTER alignment.
	 * 
	 * @param icon1 The main icon
	 * @param icon2 The icon to be positioned relative to the main icon
	 * @param location The location of the icon2 relative to icon1.
	 *     Options include NORTH, WEST, SOUTH, or EAST.
	 */
	public CompoundIcon(Icon icon1, Icon icon2) {	
		this(icon1, icon2, EAST, CENTER);
	}
	
	/**
	 * @return The icon's width
	 */
	public int getIconWidth() {
		if(location==WEST || location==EAST)
			return icon1.getIconWidth() + icon2.getIconWidth();
		return Math.max(icon1.getIconWidth(), icon2.getIconWidth());
	}
	
	/**
	 * @return The icon's height
	 */	
	public int getIconHeight() {
		if(location==NORTH || location==SOUTH)
			return icon1.getIconHeight() + icon2.getIconHeight();
		return Math.max(icon1.getIconHeight(), icon2.getIconHeight());
	}
	
	/**
	 * Paint the icon.
	 * 
	 * @param comp The component on which we are painting
	 * @param gr The graphics object used for painting
	 * @param x The x location of the icon
	 * @param y The y location of the icon
	 */
	public void paintIcon(Component comp, Graphics gr, int x, int y) {
		Graphics2D g = (Graphics2D)gr;

		int w1 = icon1.getIconWidth();
		int h1 = icon1.getIconHeight();
		
		int w2 = icon2.getIconWidth();
		int h2 = icon2.getIconHeight();				
		
		int x1=0, y1=0;
		int x2=0, y2=0;
		
		if(location==NORTH) {
			y1=h2;
			
			if(alignment==LEFT) {
				// nothing
			} else if(alignment==CENTER) {
				int maxw = Math.max(w1, w2);
				int minw = Math.min(w1, w2);
				int wdif = maxw-minw;					
				
				if(w2<w1)
					x2=wdif/2;
				else if(w2>w1)
					x1=wdif/2;
			} else if(alignment==RIGHT) {
				if(w2<w1)
					x2=w1-w2;
				else if(w2>w1)
					x1=w2-w1;
			} 
		} else if(location==WEST) {
			x1=w2;
			
			if(alignment==TOP) {
				// nothing
			} else if(alignment==CENTER) {
				int maxh = Math.max(h1, h2);
				int minh = Math.min(h1, h2);
				int hdif = maxh-minh;					
				
				if(h2<h1)
					y2=hdif/2;
				else if(h2>h1)
					y1=hdif/2;
			} else if(alignment==BOTTOM) {
				if(h2<h1)
					y2=h1-h2;
				else if(h2>h1)
					y1=h2-h1;
			} 		
		} else if(location==SOUTH) {
			y2=h1;
			
			if(alignment==LEFT) {
				// nothing
			} else if(alignment==CENTER) {
				int maxw = Math.max(w1, w2);
				int minw = Math.min(w1, w2);
				int wdif = maxw-minw;					
				
				if(w2<w1)
					x2=wdif/2;
				else if(w2>w1)
					x1=wdif/2;
			} else if(alignment==RIGHT) {
				if(w2<w1)
					x2=w1-w2;
				else if(w2>w1)
					x1=w2-w1;
			} 	
		} else if(location==EAST) {
			x2=w1;
			
			if(alignment==TOP) {
				// nothing
			} else if(alignment==CENTER) {
				int maxh = Math.max(h1, h2);
				int minh = Math.min(h1, h2);
				int hdif = maxh-minh;					
				
				if(h2<h1)
					y2=hdif/2;
				else if(h2>h1)
					y1=hdif/2;
			} else if(alignment==BOTTOM) {
				if(h2<h1)
					y2=h1-h2;
				else if(h2>h1)
					y1=h2-h1;
			} 	
		}
		
		icon1.paintIcon(comp, g, x+x1, y+y1);
		icon2.paintIcon(comp, g, x+x2, y+y2);
	}
}