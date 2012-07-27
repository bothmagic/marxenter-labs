/*
 * $Id: JXMouseInputListener.java 2653 2008-08-10 20:18:36Z oasuncion $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */

package org.jdesktop.swingx;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JComponent;
import javax.swing.event.MouseInputAdapter;

class JXMouseInputListener extends MouseInputAdapter implements
		PropertyChangeListener {

	private Color color;

	@Override
	public void mouseExited(MouseEvent e) {
		JComponent element = (JComponent) e.getSource();
		element.setForeground(((JXMenuElement) element).getRegularForeground());
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		JComponent element = (JComponent) e.getSource();
		// modifying the JPanel
		element.getParent().getParent().setBackground(color);
		// modifying the JLayeredPane of the JPanel
		element.getParent().getParent().getParent().setBackground(color);
		// modifying the JRootPane
		element.getParent().getParent().getParent().getParent().setBackground(
				color);
	}

	@Override
	public void propertyChange(PropertyChangeEvent e) {
		color = (Color) e.getNewValue();

	}

	/**
	 * Indicates what is the color of this component
	 * 
	 * @return the color of this component
	 */
	public Color getColor() {
		return color;
	}
}