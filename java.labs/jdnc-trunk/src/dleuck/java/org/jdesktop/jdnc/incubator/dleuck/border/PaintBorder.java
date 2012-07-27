/*
 * $Id: PaintBorder.java 712 2005-09-28 21:03:47Z dleuck $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 * 
 * This code was donated by Ikayzo.com.
 */
package org.jdesktop.jdnc.incubator.dleuck.border;

import javax.swing.border.*;
import java.awt.*;

/**
 * <p>A border filled with paint.</p>
 * 
 * <p>To create a ten pixel border with a gradient</p>
 * <code>
 *     GradientPaint paint=new GradientPaint(75, 75, Color.white, 95, 95,
 *         Color.gray, true);
 *     PaintBorder border = new PaintBorder(10,paint);
 * </code>
 * 
 * @author Daniel Leuck
 */
public class PaintBorder extends EmptyBorder {
	
    private Paint paint;
    
    /**
     * Create a paint border.
     * 
     * @param top 		The top inset of the border
     * @param left 		The left inset of the border
     * @param bottom 	The bottom inset of the border
     * @param right 	The right inset of the border
     * @param paint 	The paint rendered for the border
     */
    public PaintBorder(int top, int left, int bottom, int right, Paint paint)   {
        super(top, left, bottom, right);
        this.paint = paint;
    }

    /**
     * Creates a paint border with the specified insets and paint.
     * 
     * @param borderInsets 	The insets of the border
     * @param paint 		The paint rendered for the border
     */
    public PaintBorder(Insets borderInsets, Paint paint)   {
        super(borderInsets);
        this.paint = paint;
    }
	
    /**
     * Creates a paint border with the specified insets and paint.
     * 
     * @param thickness	The thickness of this border
     * @param paint 	The paint rendered for the border
     */
    public PaintBorder(int thickness, Paint paint)   {
		super(new Insets(thickness,thickness,thickness,thickness));
		this.paint = paint;
    }

    /**
     * Paints the border.
     * 
	 * @param c 		The component on which we are painting
	 * @param gr 		The graphics object used for painting
	 * @param x 		The x location of the border
	 * @param y 		The y location of the border
	 * @param width 	The width of the border
	 * @param height	The height of the border
     */
    public void paintBorder(Component c, Graphics gr, int x, int y, int width,
    	int height) {
    	
        Graphics2D g = (Graphics2D)gr;
		
		Insets insets = getBorderInsets(c);
        Paint oldPaint = g.getPaint();
        g.translate(x, y);

        if (paint != null) {
            g.setPaint(paint);
            g.fillRect(0, 0, width - insets.right, insets.top);
            g.fillRect(0, insets.top, insets.left, height - insets.top);
            g.fillRect(insets.left, height - insets.bottom, width - insets.left,
            		insets.bottom);
            g.fillRect(width - insets.right, 0, insets.right, height -
            		insets.bottom);
        }
		
        g.translate(-x, -y);
        g.setPaint(oldPaint);
    }

    /**
     * @return This border's paint
     */
    public Paint getPaint() {
        return paint;
    }

    /**
     * @return true if this border is opaque
     */
    public boolean isBorderOpaque() { 
    	int trans = paint.getTransparency();
		return (trans != Paint.TRANSLUCENT && trans != Paint.BITMASK);
    }
}
