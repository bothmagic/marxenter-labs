/*
 * $Id: LayeredBorder.java 712 2005-09-28 21:03:47Z dleuck $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 * 
 * This code was donated by Ikayzo.com.
 */
package org.jdesktop.jdnc.incubator.dleuck.border;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;

import javax.swing.border.AbstractBorder;
import javax.swing.border.Border;

/**
 * A border containing two borders with the second painted on top of the first.
 * 
 * @author Daniel Leuck
 */
public class LayeredBorder extends AbstractBorder
	implements java.io.Serializable {
		
	private Border lowerBorder, upperBorder;
	private Insets insets;
	
	/**
	 * Create a layered border
	 * 
	 * @param lowerBorder The border painted on the bottom
	 * @param upperBorder The border painted on top
	 */
	public LayeredBorder(Border lowerBorder, Border upperBorder) {
		this.lowerBorder=lowerBorder;
		this.upperBorder=upperBorder;
		
		calculateInsets();
	}

    /**
     * Paints the border.
     * 
	 * @param c The component on which we are painting
	 * @param g The graphics object used for painting
	 * @param x The x location of the border
	 * @param y The y location of the border
	 * @param width The width of the border
	 * @param height The height of the border
     */
	public void paintBorder(Component c, Graphics g, int x, int y,
		int width, int height) {

		Color oldColor = g.getColor();
		lowerBorder.paintBorder(c, g, x, y, width, height);
		upperBorder.paintBorder(c, g, x, y, width, height);		
		g.setColor(oldColor);
	}

    /**
     * Returns the insets of the border.
     * 
     * @param c the component for which this border insets value applies
     */	
	public Insets getBorderInsets(Component c) {
		return insets;
	} 
	
	private void calculateInsets() {
		Insets lbi=lowerBorder.getBorderInsets(null);
		Insets ubi=upperBorder.getBorderInsets(null);
		
		int top=Math.max(lbi.top, ubi.top);
		int left=Math.max(lbi.left, ubi.left);
		int bottom=Math.max(lbi.bottom, ubi.bottom);
		int right=Math.max(lbi.right, ubi.right);
		
		insets=new Insets(top, left, bottom, right);
	}	
}