/*
 * $Id: PlotIcon.java 716 2005-10-03 00:36:49Z dleuck $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 * 
 * This code is part of the Java icon package (JIC) donated by Ikayzo.com
 */
package org.jdesktop.jdnc.incubator.dleuck.icon;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.io.Serializable;

import javax.swing.Icon;

/**
 * <p>A plot icon allows you to easily create a small icon with colors at
 * paricular coordinates.</p>
 * 
 * <p>To create a checkerboard black and white icon:</p>
 * <code>
 *     PlotIcon icon = new PlotIcon(
 *         new Color[] { Color.white, Color.black },
 *         new int[][] {
 *             {1,0,1},
 *             {0,1,0},
 *             {1,0,1}
 *         }
 *     );
 * </code>
 * 
 * @author Daniel Leuck
 */
public class PlotIcon implements Icon, Serializable {
	
	private Color[] palette;
	private int[][] data;
	
	/**
	 * Create a plot icon.  Use -1 in the data array for transparency.
	 * 
	 * @param palette 	The palette
	 * @param data 		A 2D array containing indexes from the palette (see
	 *                      example)
	 */
	public PlotIcon(Color[] palette, int[][] data) {
		this.palette=palette;	
		this.data=data;
	}
	
	/**
	 * @return The icon's width
	 */
	public int getIconWidth() {
		return data[0].length;
	}
	
	/**
	 * @return The icon's height
	 */	
	public int getIconHeight() {
		return data.length;
	}
	
	/**
	 * Paint the icon.
	 * 
	 * @param comp 		The component on which we are painting
	 * @param g			The graphics object used for painting
	 * @param xStart 	The x location of the icon
	 * @param yStart 	The y location of the icon
	 */		
	public void paintIcon(Component comp, Graphics g, int xStart, int yStart) {
		
		int width = getIconWidth();
		int height = getIconHeight();
		
		Color old = g.getColor();
		
		for(int y=0; y<height; y++) {
			for(int x=0; x<width; x++) {
				int val = data[y][x];
				
				if(val!=-1) {
					Color c = palette[val];
					g.setColor(c);
					g.drawLine(xStart + x, yStart + y, xStart + x, yStart + y);
				}
			}
		}
		
		g.setColor(old);
	}
}