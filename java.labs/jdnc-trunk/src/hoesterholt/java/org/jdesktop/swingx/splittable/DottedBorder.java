/* ******************************************************************************
 *
 *       Copyright 2008-2010 Hans Oesterholt-Dijkema
 *       This file is part of the JDesktop SwingX library
 *       and part of the SwingLabs project
 *
 *   SwingX is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU Lesser General Public License as 
 *   published by the Free Software Foundation, either version 3 of 
 *   the License, or (at your option) any later version.
 *
 *   SwingX is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public License
 *   along with SwingX.  If not, see <http://www.gnu.org/licenses/>.
 *   
 * ******************************************************************************/

package org.jdesktop.swingx.splittable;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;

import javax.swing.border.Border;

public class DottedBorder implements Border {
	
	private Insets _insets;
	private Color  _color;

	public Insets getBorderInsets(Component c) {
		return _insets;
	}

	public boolean isBorderOpaque() {
		return false;
	}

	public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
		Graphics2D g2=(Graphics2D) g;
		g2.setColor(_color);
		float[] dashPattern=new float[]{1.0f};
		if (_insets.top!=0) {
			BasicStroke s=new BasicStroke(_insets.top,BasicStroke.CAP_BUTT,BasicStroke.JOIN_BEVEL,1.0f,dashPattern,0.0f);
			g2.setStroke(s);
			g2.drawLine(x, y, x+width-1, y);
		}
		if (_insets.left!=0) {
			BasicStroke s=new BasicStroke(_insets.left,BasicStroke.CAP_BUTT,BasicStroke.JOIN_BEVEL,1.0f,dashPattern,0.0f);
			g2.setStroke(s);
			g2.drawLine(x, y+height-1, x, y);
		}
		if (_insets.bottom!=0) {
			BasicStroke s=new BasicStroke(_insets.bottom,BasicStroke.CAP_BUTT,BasicStroke.JOIN_BEVEL,1.0f,dashPattern,0.0f);
			g2.setStroke(s);
			g2.drawLine(x, y+height-1, x+width, y+height-1);
		}
		if (_insets.right!=0) {
			BasicStroke s=new BasicStroke(_insets.right,BasicStroke.CAP_BUTT,BasicStroke.JOIN_BEVEL,1.0f,dashPattern,0.0f);
			g2.setStroke(s);
			g2.drawLine(x+width-1, y, x+width-1, y+height-1);
		}
	}
	
	public DottedBorder(Color color,int top,int left,int bottom,int right) {
		_insets=new Insets(top,left,bottom,right);
		_color=color;
	}

}
