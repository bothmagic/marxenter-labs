/*
 * $Id: WindowsDropButtonUI.java 940 2006-12-07 22:47:53Z syhaas $
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

package org.jdesktop.swingx.plaf.windows;

import com.sun.java.swing.plaf.windows.WindowsButtonUI;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import javax.swing.AbstractButton;
import javax.swing.DefaultButtonModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JPopupMenu;
import javax.swing.UIManager;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.plaf.ComponentUI;
import org.jdesktop.swingx.JXDropButton;

/**
 * @author Sylvan Haas IV (syhaas [at] gmail.com)
 * @version 1
 */
public class WindowsDropButtonUI extends WindowsButtonUI
{
	/** the arrow and disabled arrow cached */
	protected static Icon m_Arrow = null, m_DisabledArrow = null;//shared icons
	/** our mouse listener for clicks and when mouse hover */
	protected MouseListener m_ClickListener;
	/** whether the mouse is hovering */
	protected boolean m_IsHovering;
	/** popup menu listener */
	protected PopupMenuListener m_PopupMenuListener;
	/** use this to keep track of the current menu being shown */
	private JPopupMenu m_TmpMenu;
	private boolean m_PopupVisible;

	public static ComponentUI createUI(JComponent c) { return new WindowsDropButtonUI(); }

	protected WindowsDropButtonUI()
	{
		super();
		//kindly initialize arrows
		if(m_Arrow == null)
		{
			m_Arrow = getDropArrow(true);
			m_DisabledArrow = getDropArrow(false);
		}
	}

    public void installDefaults(AbstractButton b)
	{
        super.installDefaults(b);
		b.setModel(new WindowsDropButtonModel(b));
		//b.setRolloverEnabled(true);
    }

	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////// Accessors
	public Color getTextColor() { return UIManager.getColor("Button.foreground"); }
	public Color getDisabledTextColor() { return UIManager.getColor("Button.disabledForeground"); }
	public Color getSelectColor() { return UIManager.getColor("Button.select"); }
	public int getArrowIconWidth()
	{
		if(m_Arrow == null) return 0;
		return m_Arrow.getIconWidth();
	}

	/**
	 * Returns the arrow depending on <code>enabled</code> with the colors used by reference from the <code>UIManager</code>
	 * are <code>Button.foreground</code> and <code>Button.disabledText</code> for enabled and disabled, respectively
	 * @param enabled whether to construct the enabled or disabled arrow
	 * @return the arrow image
	 */
	protected Icon getDropArrow(boolean enabled)
	{
		if(enabled)
		{
			if(m_Arrow == null) m_Arrow = getDropArrow(getTextColor());
			return m_Arrow;
		}
		else
		{
			if(m_DisabledArrow == null) m_DisabledArrow = getDropArrow(getDisabledTextColor());
			return m_DisabledArrow;
		}
	}

	/**
	 * Returns the arrow by color
	 * @param c the color to use to draw the arrow
	 * @return the arrow image, drawn as polygon
	 */
	protected Icon getDropArrow(Color c)
	{
		BufferedImage bi = new BufferedImage(12, 6, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = (Graphics2D)bi.getGraphics();
		g.setColor(c);
		g.fillPolygon(new int[] { 2, 5, 9, 1},
					  new int[] { 1, 5, 1, 1},
					  4);
		g.dispose();
		return new ImageIcon(bi);
	}

	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////// Listeners
    protected void installListeners(AbstractButton b)
	{
        super.installListeners(b);
		final JXDropButton btn = (JXDropButton)b;
		m_ClickListener = new MouseAdapter()
		{
			public void mousePressed(MouseEvent e)
			{
				if((btn.getAction() == null) || (e.getX() >= getActionX(btn)))
				{
					//we dont have an action or we are to the left of the arrow
					e.consume();
					showPopup(btn);
				}
			}

			public void mouseReleased(MouseEvent e)
			{
				if((btn.getAction() == null) || (e.getX() >= getActionX(btn)))
				{
					e.consume();
				}
			}

			public void mouseEntered(MouseEvent e) { m_IsHovering = true; }
			public void mouseExited(MouseEvent e)  { m_IsHovering = false; }
		};
		btn.addMouseListener(m_ClickListener);

		//create popup menu listener
		m_PopupMenuListener = new PopupMenuListener()
		{
			public void popupMenuCanceled(PopupMenuEvent e) {}
			public void popupMenuWillBecomeVisible(PopupMenuEvent e)
			{
				m_PopupVisible = true;
			}

			public void popupMenuWillBecomeInvisible(PopupMenuEvent e)
			{
				m_PopupVisible = false;
			}
		};
	}

    protected void uninstallListeners(AbstractButton b)
	{
        super.uninstallListeners(b);
		b.removeMouseListener(m_ClickListener);
		if(m_TmpMenu != null) m_TmpMenu.removePopupMenuListener(m_PopupMenuListener);
    }

	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////// Paint
    public void paint(Graphics g, JComponent c)
	{
        JXDropButton btn = (JXDropButton) c;
		super.paint(g, c);
		//if the popup is visible, we want to show it as pressed
		if(m_PopupVisible) paintButtonPressed(g, btn);

		if(btn.getPopupMenu() != null)
		{
			int x = getActionX(btn);
			int y = (btn.getHeight()/2)-2;//center it

			//paint a line if there is an action associate with the button itself
			if((btn.getAction() != null) && m_IsHovering && !m_PopupVisible)
			{
				Color tmp = g.getColor();
				g.setColor(getTextColor());
				g.drawLine(x, btn.getInsets().top, x, btn.getHeight()-btn.getInsets().bottom);
				g.setColor(tmp);
			}

			//draw the arrow - get the x that centers the arrow
			//x now is the location of the line
			x += ((btn.getWidth()-x) / 2)-(getArrowIconWidth()/2);
			getDropArrow(btn.getModel().isEnabled()).paintIcon(btn, g, x, y);
		}
	}

	/**
	 * Copied from MetailButtonUI and made to only paint the "action" side of the button
	 * @param g graphics
	 * @param b the drop button
	 */
	protected void paintButtonPressed(Graphics g, AbstractButton b)
	{
		super.paintButtonPressed(g, b);
		if(b.isContentAreaFilled() && (b.getAction() != null) && (((JXDropButton)b).getPopupMenu() != null) && !m_PopupVisible)
		{
			Dimension size = b.getSize();
			g.setColor(getSelectColor());
			int x = getActionX((JXDropButton)b);
			g.fillRect(x, 2, b.getInsets().right-2, size.height-4);
		}
	}

	protected void paintIcon(Graphics g, JComponent c, Rectangle iconRect)
	{
		if((c instanceof JXDropButton) && (((JXDropButton)c).getPopupMenu() != null))
		{
			iconRect.x -= (iconRect.x - c.getInsets().left);
		}
		super.paintIcon(g, c, iconRect);
	}

	protected void paintText(Graphics g, JComponent c, Rectangle textRect, String text)
	{
		if((c instanceof JXDropButton) && (((JXDropButton)c).getPopupMenu() != null))
		{
			JXDropButton btn = (JXDropButton)c;
			textRect.x -= (textRect.x - c.getInsets().left);
			textRect.x += ((btn.getIcon() == null) ? 0 : (btn.getIcon().getIconWidth() + btn.getIconTextGap()));
		}
		super.paintText(g, c, textRect, text);
	}

	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////// Layout
	/**
	 * @return the preferred dimensions, adds the arrow icon width
	 */
    public Dimension getPreferredSize(JComponent c)
	{
        Dimension d = super.getPreferredSize(c);
		if((c instanceof JXDropButton) && (((JXDropButton)c).getPopupMenu() != null)) d.width += getArrowIconWidth();
		return d;
	}

	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////// Protected
	/**
	 * shows the popup relative to the button
	 * @param btn .
	 */
	protected void showPopup(JXDropButton btn)
	{
		if(!btn.isEnabled()) return;
		JPopupMenu menu = btn.getPopupMenu();
		if(menu == null) return;
		if(m_TmpMenu == null)
		{
			//add the popup listener to when it closes
			m_TmpMenu = menu;
			m_TmpMenu.addPopupMenuListener(m_PopupMenuListener);
		}
		else
		{
			if(m_TmpMenu != menu)
			{
				//remove the listener from the previous menu
				m_TmpMenu.removePopupMenuListener(m_PopupMenuListener);
				//add the listener to the new menu and set our tmp
				m_TmpMenu = menu;
				m_TmpMenu.addPopupMenuListener(m_PopupMenuListener);
			}
			//otherwise they are the same and we dont need to do anything
		}

		if(menu.isVisible() || m_PopupVisible)
		{
			//it's already shown...hide it
			//this doesnt happen becuz the focus on the menu is lost when the user presses the mouse outside of the
			// menu and then it becomes hidden but when the user clicks on the button again since the menu is not shown
			// becuz of the change in focus (button now has focus) the menu is shown again
			menu.setVisible(false);
		}
		else
		{
			menu.show(btn, 0, btn.getHeight());
		}
	}

	protected int getActionX(JXDropButton btn) { return btn.getWidth()-btn.getInsets().right; }

	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////// WindowsDropButtonModel
	/**
	 * Custom DropButton model to keep the button looking "pressed" when the popup menu is open.
	 * This is a work-around for Windows LNF, Metal doesnt need a custom model but might be easier with one in which
	 * case this class should become public or package-private if it works out (or embedded in JXDropButton)
	 */
	private class WindowsDropButtonModel extends DefaultButtonModel
	{
		public WindowsDropButtonModel(AbstractButton btn)
		{
			super();
			DefaultButtonModel model = (DefaultButtonModel)btn.getModel();
			setActionCommand(model.getActionCommand());
			setArmed(model.isArmed());
			setEnabled(model.isEnabled());
			setSelected(model.isSelected());
			setPressed(model.isPressed());
			setRollover(model.isRollover());
			setMnemonic(model.getMnemonic());
			setGroup(model.getGroup());
		}

		public boolean isPressed() { return m_PopupVisible || super.isPressed(); }
		public boolean isRollover() { return m_PopupVisible || super.isRollover(); }
		public boolean isArmed() { return m_PopupVisible || super.isArmed(); }
	}
}
