/*
 * $Id: JXDropButton.java 935 2006-12-06 23:00:34Z syhaas $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */

package org.jdesktop.swingx;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JPopupMenu;
import javax.swing.plaf.ButtonUI;
import org.jdesktop.swingx.plaf.JXDropButtonAddon;
import org.jdesktop.swingx.plaf.LookAndFeelAddons;

/**
 * DropButton component that encapsulates a JPopupMenu. If used with an action
 * the button can be clicked on and the action is performed or the arrow can be clicked
 * and the popup will be shown. The popup menu will be shown relative to the button.
 *
 * @author Sylvan Haas IV (syhaas [at] gmail.com)
 * @version 1
 */
public class JXDropButton extends JButton
{
    public static final String UI_CLASS_ID = "DropButtonUI";

    //ensure at least the default ui is registered
    static
	{
    	LookAndFeelAddons.contribute(new JXDropButtonAddon());
    }

	/** the popup menu */
	protected JPopupMenu m_Menu;

	/**
	 * Constructs a DropButton without a popup
	 * @param a action
	 */
	public JXDropButton(Action a) { this(a, null); }

	/**
	 * Constructs a DropButton without a popup
	 * @param text text for the button
	 */
	public JXDropButton(String text) { this(text, (JPopupMenu)null); }

	/**
	 * Constructs a DropButton without a popup
	 * @param icon icon for the button
	 */
	public JXDropButton(Icon icon) { this(icon, null); }

	/**
	 * Constructs a DropButton without a popup
	 * @param text text for the button
	 * @param icon icon for the button
	 */
	public JXDropButton(String text, Icon icon) { this(text, icon, null); }

	/**
	 * Constructs a DropButton with the popup
	 * @param a action, this will draw a line on hover
	 * @param menu the popup menu
	 */
	public JXDropButton(Action a, JPopupMenu menu)
	{
		super(a);
		m_Menu = menu;
	}

	/**
	 * Constructs a DropButton with the popup
	 * @param text text of the button
	 * @param menu the popup menu
	 */
	public JXDropButton(String text, JPopupMenu menu)
	{
		super(text);
		m_Menu = menu;
	}

	/**
	 * Constructs a DropButton with the popup
	 * @param icon icon of the button
	 * @param menu the popup menu
	 */
	public JXDropButton(Icon icon, JPopupMenu menu)
	{
		super(icon);
		m_Menu = menu;
	}

	/**
	 * Constructs a DropButton with the popup
	 * @param text text of the button
	 * @param icon icon of the button
	 * @param menu the popup menu
	 */
	public JXDropButton(String text, Icon icon, JPopupMenu menu)
	{
		super(text, icon);
		m_Menu = menu;
	}

	/**
	 * Sets the popup menu
	 * @param menu the menu, or NULL (no popup or arrow will be painted)
	 */
	public void setPopupMenu(JPopupMenu menu)
	{
		JPopupMenu oldMenu = getPopupMenu();
		m_Menu = menu;
		firePropertyChange("popupMenu", oldMenu, m_Menu);
		repaint();
	}

	/**
	 * Retrieve the popup menu
	 * @return the menu
	 */
	public JPopupMenu getPopupMenu() { return m_Menu; }

    /**
     * Returns a string that specifies the name of the L&F class that renders this component
     */
    public String getUIClassID() { return UI_CLASS_ID; }

	/**
     * Resets the UI property to a value from the current look and
     * feel.
	 * Defer to the super class and update the menu (if exists)
     *
     * @see javax.swing.JComponent#updateUI
     */
    public void updateUI()
	{
		//also update our menu
		if(m_Menu != null) m_Menu.updateUI();

		setUI((ButtonUI)LookAndFeelAddons.getUI(this, ButtonUI.class));
	}
}
