/*
 * $Id: LayeredIcon.java 689 2005-09-16 06:10:09Z dleuck $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 *
 * This code is part of the Java icon package (JIC) donated by Ikayzo.com
 */
package org.jdesktop.jdnc.incubator.dleuck.icon;

import javax.swing.*;
import java.awt.*;

/**
 * <p>An icon allowing one icon to be layered on top of another in various
 * positions with optional offsets.</p>
 * 
 * <p>An example demonstrating how to position icon2 on top of icon1 positioned
 * in the upper left corner inset 2 pixels:</p>
 * 
 * <code>
 *     LayeredIcon icon = new LayeredIcon(
 *         icon1, // bottomIcon
 *         icon2, // topIcon
 *         SwingConstants.LEFT, // halign
 *         SwingConstants.TOP, // valign
 *         2, // xOffset
 *         2  // yOffset
 *     );
 * </code>
 * 
 * @author Daniel Leuck
 */
public class LayeredIcon implements Icon, SwingConstants {
	
	private Icon bottomIcon, topIcon;
	private int halign, valign;
	private int xOffset, yOffset;
	
	/**
	 * Create a layered icon.
	 * 
	 * @param bottomIcon The icon painted on the bottom
	 * @param topIcon The icon painted on the top
	 * @param halign The horizontal alignment of the top icon relative to the
	 *     bottom icon
	 * @param valign The vertical alignment of the top icon relative to the
	 *     bottom icon
	 * @param xOffset The x offset
	 * @param yOffset The y offset
	 */
	public LayeredIcon(Icon bottomIcon, Icon topIcon, int halign, int valign,
		int xOffset, int yOffset) {
			
		this.bottomIcon=bottomIcon;	
		this.topIcon=topIcon;
		this.halign=halign;
		this.valign=valign;
		this.xOffset=xOffset;
		this.yOffset=yOffset;
	}
	
	/**
	 * Create a layered icon with LEFT horizontal alignment and TOP vertical
	 * alignment.  Insets default to zero.
	 * 
	 * @param bottomIcon The icon painted on the bottom
	 * @param topIcon The icon painted on the top
	 */	
	public LayeredIcon(Icon bottomIcon, Icon topIcon) {
		this(bottomIcon, topIcon, LEFT, TOP, 0, 0);
	}
	
	/**
	 * @return The icon's width
	 */
	public int getIconWidth() {	
		return bottomIcon.getIconWidth(); 
	}

	/**
	 * @return The icon's height
	 */	
	public int getIconHeight() {
		return bottomIcon.getIconHeight(); 
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

		bottomIcon.paintIcon(comp, g, x, y);

		int w = bottomIcon.getIconWidth();
		int h = bottomIcon.getIconHeight();

		int tw = topIcon.getIconWidth();
		int th = topIcon.getIconHeight();
		
		int tx = 0, ty = 0;
		
		if(valign==CENTER) {
			ty = h/2-th/2;
		} else if(valign==BOTTOM){
			ty = h-th;
		}
		
		if(halign==CENTER) {
			tx=w/2-tw/2;
		} else if(halign==RIGHT) {
			tx=w-tw;
		}
		
		topIcon.paintIcon(comp, g, x+tx+xOffset, y+ty+yOffset);
	}
}