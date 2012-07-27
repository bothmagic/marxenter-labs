/*
 * $Id: JXCheckBoxMenuItem.java 2654 2008-08-10 20:19:37Z oasuncion $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */

package org.jdesktop.swingx;

import java.awt.Color;
import java.awt.event.MouseListener;

import javax.swing.JCheckBoxMenuItem;

@SuppressWarnings("serial")
public class JXCheckBoxMenuItem extends JCheckBoxMenuItem implements
		JXMenuElement {

	private float alpha = 1.0f;
	private Color color;
	private Color regularForeground;
	private Color selectionForeground;

	public JXCheckBoxMenuItem() {

		super();
		for (MouseListener l : getMouseListeners()) {
			removeMouseListener(l);
		}
		JXMouseInputListener mouseInputListener = new JXMouseInputListener();
		addMouseListener(mouseInputListener);
		addPropertyChangeListener("color", mouseInputListener);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jdesktop.swingx.JXMenuElement#getAlpha()
	 */
	@Override
	public float getAlpha() {
		return alpha;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jdesktop.swingx.JXMenuElement#setAlpha(float)
	 */
	@Override
	public void setAlpha(float alpha) {
		this.alpha = alpha;
		color = new Color((float) getBackground().getRed() / 255,
				(float) getBackground().getGreen() / 255,
				(float) getBackground().getBlue() / 255, alpha);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jdesktop.swingx.JXMenuElement#setSelectionForeground(java.awt.Color)
	 */
	public void setSelectionForeground(Color color) {
		this.selectionForeground = color;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jdesktop.swingx.JXMenuElement#getSelectionForeground()
	 */
	public Color getSelectionForeground() {
		return selectionForeground;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jdesktop.swingx.JXMenuElement#setRegularForeground(java.awt.Color)
	 */
	public void setRegularForeground(Color color) {
		setForeground(color);
		regularForeground = color;
		;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jdesktop.swingx.JXMenuElement#getRegularForeground()
	 */
	public Color getRegularForeground() {
		return regularForeground;
	}

}
