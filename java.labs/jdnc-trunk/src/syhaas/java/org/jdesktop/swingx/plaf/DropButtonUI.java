/*
 * $Id: DropButtonUI.java 935 2006-12-06 23:00:34Z syhaas $
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

package org.jdesktop.swingx.plaf;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.UIManager;
import javax.swing.plaf.metal.MetalButtonUI;

/**
 * @author Sylvan Haas IV (syhaas [at] gmail.com)
 * @version 1
 */
public class DropButtonUI extends MetalButtonUI
{
	/** the arrow and disabled arrow cached */
	protected static Icon m_Arrow = null, m_DisabledArrow = null;//shared icons

	protected DropButtonUI()
	{
		super();
		//kindly initialize arrows
		if(m_Arrow == null)
		{
			m_Arrow = getDropArrow(true);
			m_DisabledArrow = getDropArrow(false);
		}
	}

	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////// Accessors
	public Color getTextColor() { return UIManager.getColor("Button.foreground"); }
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
}
