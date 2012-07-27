/*
 * $Id: BasicDropButtonUI.java 940 2006-12-07 22:47:53Z syhaas $
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

package org.jdesktop.swingx.plaf.basic;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Graphics2D;
import java.awt.GradientPaint;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.List;
import javax.swing.AbstractButton;
import javax.swing.JComponent;
import javax.swing.JPopupMenu;
import javax.swing.UIManager;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.plaf.ComponentUI;
import org.jdesktop.swingx.JXDropButton;
import org.jdesktop.swingx.plaf.DropButtonUI;

/**
 * @author Sylvan Haas IV (syhaas [at] gmail.com)
 * @version 1
 */
public class BasicDropButtonUI extends DropButtonUI
{
	/** our mouse listener for clicks and when mouse hover */
	protected MouseListener m_ClickListener;
	/** whether the mouse is hovering */
	protected boolean m_IsHovering;
	/** cached gradient painter */
	protected BufferedImage m_Gradient;
	/** popup menu listener */
	protected PopupMenuListener m_PopupMenuListener;
	/** use this to keep track of the current menu being shown */
	private JPopupMenu m_TmpMenu;
	private boolean m_PopupVisible;

	public static ComponentUI createUI(JComponent c) { return new BasicDropButtonUI(); }

    public void installDefaults(AbstractButton b)
	{
		super.installDefaults(b);
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
		//if the popup is visible, we want to show it as pressed
		if(m_PopupVisible) paintButtonPressed(g, btn);
		super.paint(g, c);
		//simulate the gradient painting
		if((btn.getParent() != null) && (btn.getAction() != null) && (btn.getPopupMenu() != null) && btn.getModel().isArmed())
		{
			if(m_Gradient == null)
			{
				//this was copied from javax.swing.plaf.metal.MetalUtils and javax.swing.plaf.metal.CachedPainter
				// both are "package-private"
				//used here to simulate the gradient of the metal buttons
				List gradient = (List) UIManager.get("Button.gradient");
				float ratio1 = ((Number)gradient.get(0)).floatValue();
				float ratio2 = ((Number)gradient.get(1)).floatValue();
				Color c1 = (Color)gradient.get(2);
				Color c2 = (Color)gradient.get(3);
				Color c3 = (Color)gradient.get(4);
				int h = 10;//use for a solid base number for division
				int w = 1;//so we dont have to store a large image in memory, it'll strech perfectly

				m_Gradient = new BufferedImage(1, 10, BufferedImage.TYPE_INT_ARGB);
				Graphics2D g2d = (Graphics2D) m_Gradient.getGraphics();
				int mid = (int)(ratio1 * h);
				int mid2 = (int)(ratio2 * h);
				if(mid > 0)
				{
					g2d.setPaint(new GradientPaint((float)0, (float)0, c1, (float)0, (float)mid, c2, true));
					g2d.fillRect(0, 0, w, mid);
				}

				if(mid2 > 0)
				{
					g2d.setColor(c2);
					g2d.fillRect(0, mid, w, mid2);
				}

				if(mid > 0)
				{
					g2d.setPaint(new GradientPaint((float)0, (float)mid + mid2, c2, (float)0, (float)mid * 2 + mid2, c1, true));
					g2d.fillRect(0, mid + mid2, w, mid);
				}

				if(h - mid * 2 - mid2 > 0)
				{
					g2d.setPaint(new GradientPaint((float)0, (float)mid * 2 + mid2, c1, (float)0, (float)h, c3, true));
					g2d.fillRect(0, mid * 2 + mid2, w, h - mid * 2 - mid2);
				}
				g2d.dispose();
			}

			//paint gradient - from line to end
			g.drawImage(m_Gradient, getActionX(btn), 0, btn.getInsets().right+getArrowIconWidth(), btn.getHeight(), null);
		}

		if(btn.getPopupMenu() != null)
		{
			int x = getActionX(btn);
			int y = (btn.getHeight()/2)-2;//center it

			//paint a line if there is an action associate with the button itself
			if((btn.getAction() != null) && m_IsHovering && !m_PopupVisible)
			{
				Color tmp = g.getColor();
				g.setColor(getTextColor());
				g.drawLine(x, 0, x, btn.getHeight());
				g.setColor(tmp);
			}

			//draw the arrow - get the x that centers the arrow
			//x now is the location of the line
			x += ((btn.getWidth()-x) / 2)-(getArrowIconWidth()/2);
			getDropArrow(btn.isEnabled()).paintIcon(btn, g, x, y);
		}
	}

	/**
	 * Copied from MetailButtonUI and made to only paint the "action" side of the button
	 * @param g graphics
	 * @param b the drop button
	 */
	protected void paintButtonPressed(Graphics g, AbstractButton b)
	{
		if(b.isContentAreaFilled() && (b.getAction() != null) && (((JXDropButton)b).getPopupMenu() != null) && !m_PopupVisible)
		{
            Dimension size = b.getSize();
	    	g.setColor(getSelectColor());
			int width = getActionX((JXDropButton)b);
			g.fillRect(0, 1, width, size.height-2);
		}
		else
		{
			super.paintButtonPressed(g, b);
		}
	}

	/**
	 * Overwritten to move the icon to the right inset point on the button instead of the center.
	 * This is need because the default location for the button's text (and icon) is centered.
	 * TODO might be able to remove this method in exchange for setting the horizontal alignment to LEADING (or LEFT)
	 *
	 * @param g graphics
	 * @param c drop button
	 * @param iconRect rectangle area for the icon
	 */
	protected void paintIcon(Graphics g, JComponent c, Rectangle iconRect)
	{
		if((c instanceof JXDropButton) && (((JXDropButton)c).getPopupMenu() != null))
		{
			iconRect.x -= (iconRect.x - c.getInsets().left);
		}
		super.paintIcon(g, c, iconRect);
	}

	/**
	 * Overwritten to move the text to the right inset point plus icon width and icon text gap on the
	 * button instead of the center.
	 * This is need because the default location for the button's text (and icon) is centered.
	 * TODO might be able to remove this method in exchange for setting the horizontal alignment to LEADING (or LEFT)
	 *
	 * @param g graphics
	 * @param c drop button
	 * @param textRect rectangle area for the text
	 * @param text the text of the button
	 */
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

	protected int getActionX(JXDropButton btn) { return btn.getWidth()-btn.getInsets().right-getArrowIconWidth()+2; }
}
