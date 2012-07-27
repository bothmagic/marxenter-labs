/*
 * $Id: PaddedIcon.java 673 2005-09-15 23:24:57Z dleuck $
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
import java.awt.Paint;
import java.awt.Rectangle;

import javax.swing.Icon;
import javax.swing.SwingUtilities;

/**
 * <p>An icon that pads a delegate icon with a border.  The border can be
 * transparent or painted.<p>
 * 
 * <p>To create an icon with a two pixel red border:</p>
 * <code>
 *     PaddedIcon picon = new PaddedIcon(icon, 2, 2, 2, 2, Color.red);
 * </code>
 * 
 * @author Daniel Leuck
 */
public class PaddedIcon implements Icon {
	
	private Icon icon;
	private int top, left, bottom, right;
	private Paint paint;

	/**
	 * Create a padded icon with the given border paint.
	 * 
	 * @param icon The icon to pad
	 * @param top Top padding
	 * @param left Left padding
	 * @param bottom Bottom padding
	 * @param right Right padding
	 * @param paint The border paint or null (for a transparent border)
	 */
	public PaddedIcon(Icon icon, int top, int left, int bottom, int right,
		Paint paint) {
			
		this.icon=icon;	
		this.top=top;
		this.left=left;
		this.bottom=bottom;
		this.right=right;
		this.paint=paint;
	}
	
	/**
	 * Create a padded icon with a transparent border.
	 * 
	 * @param icon The icon to pad
	 * @param top Top padding
	 * @param left Left padding
	 * @param bottom Bottom padding
	 * @param right Right padding
	 */	
	public PaddedIcon(Icon icon, int top, int left, int bottom, int right) {
		this(icon, top, left, bottom, right, null);
	}
	
	/**
	 * @return The icon's width
	 */
	public int getIconWidth() {
		return icon.getIconWidth() + left + right;	
	}
	
	/**
	 * @return The icon's height
	 */
	public int getIconHeight() {
		return icon.getIconHeight() + top + bottom;	
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

		int w = getIconWidth();
		int h = getIconHeight();		
		
		int iw = icon.getIconWidth();
		int ih = icon.getIconHeight();
		
		if(paint!=null) {
			Paint old = g.getPaint();
			
			g.setPaint(paint);
			
			Rectangle[] borders = SwingUtilities.computeDifference(
					new Rectangle(x,y,w,h), new Rectangle(x+left, y+top,iw, ih)
				);
			for(int i=0; i<borders.length; i++)
				g.fill(borders[i]);
			
			g.setPaint(old);
		}
		
		icon.paintIcon(comp, g, x+left, y+top);
	}
}