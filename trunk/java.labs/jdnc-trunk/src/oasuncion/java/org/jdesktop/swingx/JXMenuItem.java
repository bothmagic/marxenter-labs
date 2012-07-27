/*
 * $Id: JXMenuItem.java 2654 2008-08-10 20:19:37Z oasuncion $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */

package org.jdesktop.swingx;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseListener;

import javax.swing.JComponent;
import javax.swing.JMenuItem;

@SuppressWarnings("serial")
public class JXMenuItem extends JMenuItem implements JXMenuElement {

	private Color color;
	private Color regularForeground;
	private Color selectionForeground;

	private float alpha = 1.0f;

	public JXMenuItem(String name) {

		super(name);
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
	public float getAlpha() {
		return alpha;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jdesktop.swingx.JXMenuElement#setAlpha(float)
	 */
	public void setAlpha(float alpha) {
		this.alpha = alpha;
		color = new Color((float) getBackground().getRed() / 255,
				(float) getBackground().getGreen() / 255,
				(float) getBackground().getBlue() / 255, alpha);
	}

	/**
	 * Overridden paint method to manage the opacity of the component's
	 * hierarchy
	 * 
	 * @param g
	 *            the Graphics used to paint
	 */
	@Override
	public void paint(Graphics g) {

		// makes the JPanel of the JPopupMenu non opaque
		((JComponent) getParent().getParent()).setOpaque(false);
		// idem for the JLayeredPane of the JPanel previously modified
		((JComponent) getParent().getParent().getParent()).setOpaque(false);
		// idem for the JRootPane of the JPanel previously modified
		((JComponent) getParent().getParent().getParent().getParent())
				.setOpaque(false);

		super.paint(g);

	}

	/**
	 * Indicates what is the color of this component
	 * 
	 * @return the color of this component
	 */
	public Color getColor() {
		return color;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jdesktop.swingx.JXMenuElement#setRegularForeground(java.awt.Color)
	 */
	public void setRegularForeground(Color color) {
		setForeground(color);
		regularForeground = color;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jdesktop.swingx.JXMenuElement#getRegularForeground()
	 */
	public Color getRegularForeground() {
		return regularForeground;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jdesktop.swingx.JXMenuElement#getSelectionForeground()
	 */
	@Override
	public Color getSelectionForeground() {
		return selectionForeground;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jdesktop.swingx.JXMenuElement#setSelectionForeground(java.awt.Color)
	 */
	@Override
	public void setSelectionForeground(Color color) {
		selectionForeground = color;
	}
}
