/*
 * $Id: ShapeIcon.java 711 2005-09-27 22:23:14Z dleuck $
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
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.io.Serializable;

import javax.swing.Icon;
import javax.swing.SwingConstants;

/**
 * <p>An icon that paints a shape filled with a paint.</p>
 * 
 * <p>To create a blue circle with a diameter of 16:</p>
 * <code>
 *     ShapeIcon sicon = new ShapeIcon(
 *         new Ellipse2D.Float(0,0,16,16),
 *         Color.blue
 *     );
 *     
 *     // or, using the ellipse convenience method
 *     
 *     ShapeIcon sicon = ShapeIcon.ellipse(16, 16, Color.blue);  
 *     
 * </code>
 * 
 * @author Daniel Leuck
 */
public class ShapeIcon implements Icon, SwingConstants, Serializable {
	
	private Shape shape;
	private Dimension size;
	private Paint paint;
	
	/**
	 * Create a shape icon.
	 * 
	 * @param shape The shape of the icon
	 * @param paint The fill paint
	 */
	public ShapeIcon(Shape shape, Paint paint) {
		this.shape=shape;	
		this.paint=paint;
		size=shape.getBounds().getSize();
	}
	
	/**
	 * Create a rectangular icon.
	 * 
	 * @param width		The icon width
	 * @param height 	The icon height
	 * @param paint 	The fill paint
	 */
	public ShapeIcon(int width, int height, Paint paint) {
		this(new Rectangle(0,0,width,height), paint);
	}
	
	/**
	 * Create an elliptical icon.
	 * 
	 * @param width 	The icon width
	 * @param height 	The icon height
	 * @param paint 	The fill paint
	 * @return An elliptical icon
	 */
	public static ShapeIcon ellipse(int width, int height, Paint paint) {
		return new ShapeIcon(new Ellipse2D.Float(0,0,width,height), paint);
	}
	
	/**
	 * Create an arrow icon.
	 * 
	 * @param width 	The icon width
	 * @param height 	The icon height
	 * @param direction	The direction the arrow is pointing (TOP, LEFT, BOTTOM,
	 * 					    or RIGHT)
	 * @param paint 	The fill paint
	 * @return An arrow Icon
	 */
	public static ShapeIcon arrow(int width, int height, int direction,
			Paint paint) {
		
		GeneralPath path = new GeneralPath();
		
		if(direction==TOP) {
			path.moveTo(0, height);
			path.lineTo(((float)width)/2f, 0);
			path.lineTo(width,height);
			path.lineTo(0,height);
		} else if(direction==LEFT) {
			path.moveTo(width, 0);
			path.lineTo(0, ((float)height)/2f);
			path.lineTo(width,height);
			path.lineTo(width, 0);			
		} else if(direction==BOTTOM) {
			path.moveTo(0, 0);
			path.lineTo(((float)width)/2f, height);
			path.lineTo(width,0);
			path.lineTo(0, 0);			
		} else if(direction==RIGHT){
			path.moveTo(0, 0);
			path.lineTo(width, ((float)height)/2f);
			path.lineTo(0,height);
			path.lineTo(0, 0);				
		}
		
		path.closePath();
		
		return new ShapeIcon(path, paint);
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
				
			g.translate(x,y);
			g.fill(shape);
			g.translate(-x,-y);
			
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, oldAA);
			g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, oldFM);
			g.setRenderingHint(RenderingHints.KEY_RENDERING, oldRQ);
		} 
		
		g.setPaint(oldPaint);
	}
}