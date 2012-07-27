/*
 * $Id: JXMenu.java 2652 2008-08-10 20:14:54Z oasuncion $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */

package org.jdesktop.swingx;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;

import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

@SuppressWarnings("serial")
public class JXMenu extends JMenu implements JXMenuElement {
	private float alpha = 1.0f;
	private Color regularForeground;
	private Color selectionForeground;

	public JXMenu(String name) {
		super(name);
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

		Color c = new Color(
				((float) getPopupMenu().getBackground().getRed() / 255),
				((float) getPopupMenu().getBackground().getGreen() / 255),
				((float) getPopupMenu().getBackground().getBlue() / 255), alpha);

		Color selColor = new Color(((float) c.getRed() / 255), ((float) c
				.getGreen() / 255), ((float) c.getBlue() / 255),
				((float) (alpha * 0.9)));
		changeMenusColor("Back", selColor);

		getPopupMenu().setBackground(c);
		for (Component item : getMenuComponents()) {
			if (item instanceof JXMenu)
				((JXMenu) item).setAlpha(alpha);
		}

	}

	public JMenuItem add(JMenuItem menuItem) throws IllegalArgumentException {

		if (!(menuItem instanceof JXMenuElement))
			throw new IllegalArgumentException(
					"The Menu Item must be a JXMenuElement");
		else {
			menuItem = super.add(menuItem);
			menuItem.setOpaque(false);

			return menuItem;
		}
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

		// makes the JLayeredPane of the JXMenuBar non opaque
		((JComponent) getParent().getParent()).setOpaque(false);
		// idem for the JRootPane of the JLayeredPane previously modified
		((JComponent) getParent().getParent().getParent()).setOpaque(false);

		super.paint(g);

	}

	/**
	 * To set the background of the submenus
	 * 
	 * @param color
	 *            the background of the submenus
	 */
	public void setMenusBackground(Color color) {
		if (color != null) {
			Color c = new Color(color.getRed(), color.getGreen(), color
					.getBlue(), (int) (alpha * 255));
			Color selColor = new Color(((float) c.getRed() / 255), ((float) c
					.getGreen() / 255), ((float) c.getBlue() / 255),
					((float) (alpha * 0.9)));
			changeMenusColor("Back", selColor);

			getPopupMenu().setBackground(c);
			for (Component item : getMenuComponents()) {
				if (item instanceof JXMenu)
					((JXMenu) item).setMenusBackground(color);

			}
		}
	}

	/**
	 * To set the foreground of the submenus
	 * 
	 * @param color
	 *            the foreground of the submenus
	 */
	public void setMenusForeground(Color color) {
		if (color != null) {
			if (!(getParent() instanceof JXMenuBar))
				setRegularForeground(color);

			for (Component item : getMenuComponents()) {
				if (item instanceof JXMenu)

					((JXMenu) item).setMenusForeground(color);
				if (item instanceof JXMenuItem
						|| item instanceof JXCheckBoxMenuItem
						|| item instanceof JXRadioButtonMenuItem)

					((JXMenuElement) item).setRegularForeground(color);

			}
		}
	}

	/**
	 * To set the foreground of the submenus's selected elements
	 * 
	 * @param color
	 *            the foreground of the submenus's selected elements
	 */
	public void setMenusSelectionForeground(Color color) {

		changeMenusColor("Fore", color);
		for (Component item : getMenuComponents()) {
			if (item instanceof JXMenu)

				((JXMenu) item).setMenusSelectionForeground(color);
			if (item instanceof JXMenuItem
					|| item instanceof JXCheckBoxMenuItem
					|| item instanceof JXRadioButtonMenuItem)

				((JXMenuElement) item).setSelectionForeground(color);

		}
	}

	/**
	 * change UI properties and propagate the update in the menu hierarchy
	 * 
	 * @param part
	 * @param color
	 */
	private void changeMenusColor(String part, Color color) {
		StringBuffer bufPart = new StringBuffer(part);
		bufPart.append("ground");
		UIManager.put("Menu.selection" + bufPart, color);
		UIManager.put("MenuItem.selection" + bufPart, color);
		UIManager.put("RadioButtonMenuItem.selection" + bufPart, color);
		UIManager.put("CheckBoxMenuItem.selection" + bufPart, color);
		SwingUtilities.updateComponentTreeUI(this);
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jdesktop.swingx.JXMenuElement#setSelectionForeground(java.awt.Color)
	 */
	public void setSelectionForeground(Color color) {
		selectionForeground = color;
		;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jdesktop.swingx.JXMenuElement#getSelectionForeground()
	 */
	public Color getSelectionForeground() {
		return selectionForeground;
	}

}
