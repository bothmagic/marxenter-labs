/*
 * $Id: ShapeIcon.java 673 2005-09-15 23:24:57Z dleuck $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 *
 * This code is part of the Java icon package (JIC) donated by Ikayzo.com
 */
package org.jdesktop.jdnc.incubator.dleuck.icon;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.RectangularShape;

import javax.swing.Icon;

/**
 * <p>An icon that paints a shape filled with a paint.</p>
 * 
 * <p>To create a blue circle with a diameter of 16:</p>
 * <code>
 *     ShapeIcon sicon = new ShapeIcon(
 *         new Ellipse2D.Float(0,0,16,16),
 *         Color.blue
 *     );
 * </code>
 * 
 * @author Daniel Leuck
 */
public class ShapeIcon implements Icon {
	
	private RectangularShape shape;
	private Dimension size;
	private Paint paint;
	
	/**
	 * Create a shape icon.
	 * 
	 * @param shape The shape of the icon
	 * @param paint The fill paint
	 */
	public ShapeIcon(RectangularShape shape, Paint paint) {
		this.shape=shape;	
		this.paint=paint;
		size=shape.getBounds().getSize();
	}
	
	/**
	 * Create a rectangular icon.
	 * 
	 * @param width The icon width
	 * @param height The icon height
	 * @param paint The fill paint
	 */
	public ShapeIcon(int width, int height, Paint paint) {
		this(new Rectangle(0,0,width,height), paint);
	}
	
	/**
	 * @return The icon's width
	 */
	public int getIconWidth() {
		return size.width;	
	}
	
	/**
	 * @return The icon's height
	 */
	public int getIconHeight() {
		return size.height;	
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
		if(paint==null)
			return;
		
		Graphics2D g = (Graphics2D)gr;
		int width = getIconWidth();		
		int height = getIconHeight();
		Paint oldPaint = g.getPaint();

		g.setPaint(paint);
		
		if(shape instanceof Rectangle) {
			((Rectangle)shape).setLocation(x,y);	
			g.fill(shape);
		} else {
			Object oldAA = g.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
			Object oldFM = g.getRenderingHint(
					RenderingHints.KEY_FRACTIONALMETRICS);
			Object oldRQ = g.getRenderingHint(RenderingHints.KEY_RENDERING);			
			
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);
			g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS,
				RenderingHints.VALUE_FRACTIONALMETRICS_ON);
			g.setRenderingHint(RenderingHints.KEY_RENDERING,
					RenderingHints.VALUE_RENDER_QUALITY);
			
			RectangularShape rs = (RectangularShape)shape;
			rs.setFrame((double)x,(double)y,rs.getWidth(),rs.getHeight());	

			g.fill(shape);
				
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, oldAA);
			g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, oldFM);
			g.setRenderingHint(RenderingHints.KEY_RENDERING, oldRQ);
		} 
		
		g.setPaint(oldPaint);
	}
}