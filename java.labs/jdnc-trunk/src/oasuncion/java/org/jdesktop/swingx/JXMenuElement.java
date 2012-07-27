/*
 * $Id: JXMenuElement.java 2617 2008-08-03 18:06:59Z oasuncion $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */

package org.jdesktop.swingx;

import java.awt.Color;

import javax.swing.MenuElement;

public interface JXMenuElement extends MenuElement {

	/**
	 * Indicates the alpha value of this component
	 * 
	 * @return the alpha value of this component
	 */
	public float getAlpha();

	/**
	 * Used to set the alpha value of this component
	 * 
	 * @param alpha
	 *            the alpha value to set
	 */
	public void setAlpha(float alpha);

	/**
	 * Used to set the foreground color when the element is selected
	 * 
	 * @param color
	 *            the foreground color when the element is selected
	 */
	public void setSelectionForeground(Color color);

	/**
	 * Indicates the foreground color when the element is selected
	 * 
	 * @return the foreground color when the element is selected
	 */
	public Color getSelectionForeground();

	/**
	 * Used to set the initial foreground color of this component
	 * 
	 * @param color
	 *            the initial foreground color of this component
	 */
	public void setRegularForeground(Color color);

	public Color getRegularForeground();

}
