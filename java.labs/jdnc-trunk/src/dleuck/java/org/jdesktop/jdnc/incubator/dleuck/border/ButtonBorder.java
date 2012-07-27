/*
 * $Id: ButtonBorder.java 703 2005-09-27 20:26:39Z dleuck $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 * 
 * This code was donated by Ikayzo.com.
 */

package org.jdesktop.jdnc.incubator.dleuck.border;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;

/** 
 * <p>This is a compound border that allows you to specify the normal, pressed, 
 * and rollover border for any subclass of AbstractButton.  If this border
 * is set on a component that is not an AbstractButton, it will always paint the
 * &quot;normal&quot; state.</p>
 * 
 * <p>Note: you will have to enable rollover for the button to take advantage of
 * the rollover border.<p>
 * 
 * <p>To create a button with a red border that becomes green when pressed, and 
 * blue when rolled over:</p>
 * <code>
 *     ButtonBorder border = new ButtonBorder(
 *         new PaintBorder(2,Color.red),
 *         new PaintBorder(2,Color.blue),
 *         new PaintBorder(2,Color.green)
 *     );
 *     JButton button = new JButton("hello");
 *     button.setBorder(border);
 *     
 *     // required if the L&F does not enable rollovers by default
 *     button.setRolloverEnabled(true);
 * </code>
 * 
 * @author Daniel Leuck
 */
public class ButtonBorder extends AbstractBorder
	implements java.io.Serializable {
		
	private static Insets ZERO_INSETS = new Insets(0,0,0,0);
	private Border normal, rollover, pressed;
	
	/**
	 * Create a TriButtonBorder.
	 * 
	 * @param normal 	The border when not pressed or in rollover mode
	 * @param rollover 	The border when in rollover mode
	 * @param pressed 	The border when pressed
	 */
	public ButtonBorder(Border normal, Border rollover, Border pressed) {
		this.normal = normal;
		this.rollover = rollover;
		this.pressed = pressed;
	}

    /**
     * Paints the border.
     * 
	 * @param c The component on which we are painting
	 * @param gr The graphics object used for painting
	 * @param x The x location of the border
	 * @param y The y location of the border
	 * @param width The width of the border
	 * @param height The height of the border
     */	
	public void paintBorder(Component c, Graphics g, int x, int y,
		int width, int height) {
			
		Border border = getCurrentBorder(c);
		
		if(border!=null)
			border.paintBorder(c, g, x, y, width, height);
	}

	/**
	 * Returns the insets of the border.
	 * 
	 * @param c the component for which this border insets value applies
	 */
	public Insets getBorderInsets(Component c) {
		Border border = getCurrentBorder(c);
		
		if(border==null)
			return ZERO_INSETS;		
		
		return border.getBorderInsets(c);
	}
	
    /** 
     * Reinitialize the insets parameter with this Border's current Insets. 
     * 
     * @param c 		the component for which this border insets value applies
     * @param insets 	the object to be reinitialized
     */	
	public Insets getBorderInsets(Component c, Insets insets) {
		Border border = getCurrentBorder(c);
		
		if(border==null)
			return ZERO_INSETS;
		
		if(border instanceof AbstractBorder)
			return ((AbstractBorder)border).getBorderInsets(c, insets);
		else
			return border.getBorderInsets(c);
	} 
	
	/**
	 * Get the right border depending on the state of the button.
	 */
	private Border getCurrentBorder(Component c) {
		if(!(c instanceof AbstractButton))
			return normal;
		
		AbstractButton ab = (AbstractButton)c;	
		ButtonModel bm = ab.getModel();		
		
		if(bm.isPressed()) {
			return pressed;	
		} else if(ab.isRolloverEnabled() && bm.isRollover()) {	
			return rollover;			
		} else {
			return normal;		
		}
	}
}