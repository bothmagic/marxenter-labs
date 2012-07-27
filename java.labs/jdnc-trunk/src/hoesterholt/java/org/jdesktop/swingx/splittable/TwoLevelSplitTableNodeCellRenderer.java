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

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RenderingHints;

import javax.swing.Icon;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import org.jdesktop.swingx.renderer.DefaultTableRenderer;
import org.jdesktop.swingx.renderer.JRendererLabel;

class ExpandIcon implements Icon {

	public int 		height=10;
	public int 		width=10;
	public boolean  expanded=false;
	private boolean cleared=false;
	public Color    color=Color.gray;
	private Polygon _p=new Polygon();
	
	public ExpandIcon() {
		_p.addPoint(0,0);
		_p.addPoint(0,0);
		_p.addPoint(0,0);
	}
	
	public void setCleared(boolean b) {
		System.out.println("Object="+super.hashCode()+", SetCleared="+b);
		cleared=b;
	}
	
	public void setExpanded(boolean b) {
		expanded=b;
	}
	
	public int getIconHeight() {
		if (cleared) { return 0; }
		else { return height; }
	}

	public int getIconWidth() {
		if (cleared) { return 0; }
		else { return width; }
	}

	private void drawRight(int x,int y, int w,int h, Graphics2D g) {
		g.setColor(color);
		_p.xpoints[0]=x;_p.ypoints[0]=y;
		_p.xpoints[1]=x+w;_p.ypoints[1]=y+(h/2);
		_p.xpoints[2]=x;_p.ypoints[2]=y+h;
		g.fillPolygon(_p);
	}

	private void drawDown(int x,int y, int w,int h, Graphics2D g) {
		g.setColor(color);
		_p.xpoints[0]=x;_p.ypoints[0]=y;
		_p.xpoints[1]=x+w;_p.ypoints[1]=y;
		_p.xpoints[2]=x+(w/2);_p.ypoints[2]=y+h;
		g.fillPolygon(_p);
	}
	
	public void paintIcon(Component c, Graphics _g, int x, int y) {
		System.out.println("Object="+super.hashCode()+", Cleared="+cleared);
		if (cleared) { return; }
		
		Graphics2D g=(Graphics2D) _g;
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		x=x-2;
		y=y-2;
		
		x+=2;
		y+=2;
		int w=width-2;
		int h=height-2;
		
		if (expanded) {
			drawDown(x,y,w,h,g);
		} else {
			drawRight(x,y,w,h,g);
		}
	}
	
}

public class TwoLevelSplitTableNodeCellRenderer extends JPanel implements TableCellRenderer {
		
		private static final long serialVersionUID = 1L;
		
		private DefaultTableRenderer 	 _previous;
		private TableCellRenderer 		 _previous1;
		
		private Color     _nodeExpandedBackground;
		private Color     _nodeUnExpandedBackground;
		private Color     _nodeExpandedForeground;
		private Color	  _nodeUnExpandedForeground;
		
		private AbstractTwoLevelSplitTableModel _model;
		
		private boolean 		_left;
		private ExpandIcon 		_eicon;
		
		public TwoLevelSplitTableNodeCellRenderer(boolean left,AbstractTwoLevelSplitTableModel model,TableCellRenderer previous) {
			_left=left;
			if (previous.getClass().equals(DefaultTableRenderer.class)) {
				_previous=(DefaultTableRenderer) previous;
				_previous1=null;
			} else {
				_previous=null;
				_previous1=previous;
			}
			_model=model;
			_nodeExpandedBackground=SplitTableDefaults.tablePartOfBg();
			_nodeUnExpandedBackground=SplitTableDefaults.tablePartOfBg();
			_nodeExpandedForeground=Color.black;
			_nodeUnExpandedForeground=Color.black;
			_eicon=null; 
		}

		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {
			
			AbstractTwoLevelSplitTableModel.CNodeIndex cnode=_model.getCNodeIndex(row,column);
			System.out.println(cnode+" row="+row+", column="+column);
			if (column==0 && row==3) {
				_model.getCNodeIndex(row,column);
			}

			Component c;
			Object value1=value;
			if (value instanceof AbstractTwoLevelSplitTableModel.NoValue) {
				value1=new String("");
			}

			if (_previous!=null) {
				c=_previous.getTableCellRendererComponent(table, value1, isSelected, hasFocus, row, column);
			} else {
				c=_previous1.getTableCellRendererComponent(table, value1, isSelected, hasFocus, row, column);
			}
			//System.out.println(c.getClass().getName());
			System.out.println("isSelected:"+isSelected);
			
			// TODO: This code is not very good. 
			if (c instanceof JRendererLabel) {
				
				if (_eicon==null) { 
					_eicon=new ExpandIcon();
					((JRendererLabel) c).setIcon(_eicon);
				}
				
				if (cnode.column==0 && _left) {
					_eicon.height=table.getRowHeight(row)/2;
					_eicon.width=_eicon.height; 
					((JRendererLabel) c).setIcon(_eicon);
					_eicon.setCleared(false);
					_eicon.setExpanded(_model.getNodeExpanded(cnode.nodeIndex));
					((JRendererLabel) c).setIcon(_eicon);
				} else {
					_eicon.setCleared(true);
					((JRendererLabel) c).setIcon(_eicon);
				}
			} /* TODO: Doesn't work yet.
			else if (c instanceof SubstanceDefaultTableCellRenderer) {
				
				if (_eicon==null) { 
					_eicon=new ExpandIcon();
					((SubstanceDefaultTableCellRenderer) c).setIcon(_eicon);
				}
				
				if (cnode.column==0 && cnode.nodeIndex!=-1 && _left) {
					System.out.println("setcleared false, cnode="+cnode);
					_eicon.height=table.getRowHeight(row)/2;
					_eicon.width=_eicon.height; 
					((SubstanceDefaultTableCellRenderer) c).setIcon(_eicon);
					_eicon.setCleared(false);
					_eicon.setExpanded(_model.getNodeExpanded(cnode.nodeIndex));
				} else {
					System.out.println("setcleared true");
					_eicon.setCleared(true);
					((SubstanceDefaultTableCellRenderer) c).setIcon(_eicon);
				}
			}
			*/
			
			/*if (!isSelected) {
				if (c instanceof SubstanceDefaultTableCellRenderer) {
					((SubstanceDefaultTableCellRenderer) c).setOpaque(true);
				}
				if (_model.getNodeExpanded(cnode.nodeIndex)) {
					c.setBackground(_nodeExpandedBackground);
					//c.setForeground(_nodeExpandedForeground);
				} else {
					c.setBackground(_nodeUnExpandedBackground);
					//c.setForeground(_nodeUnExpandedForeground);
				}
			} else {
				if (c instanceof SubstanceDefaultTableCellRenderer) {
					((SubstanceDefaultTableCellRenderer) c).setOpaque(false);
				}
			}*/
			return c;
		}
		
		public void setNodeExpandedBackground(Color b) {
			_nodeExpandedBackground=b;
		}
		
		public void setNodeUnExpandedBackground(Color b) {
			_nodeUnExpandedBackground=b;
		}
		
		public void setNodeExpandedForeground(Color b) {
			_nodeExpandedForeground=b;
		}
		
		public void setNodeUnExpandedForeground(Color b) {
			_nodeUnExpandedForeground=b;
		}
		
		public Color getNodeExpandedBackground() {
			return _nodeExpandedBackground;
		}
		
		public Color getNodeUnExpandedBackground() {
			return _nodeUnExpandedBackground;
		}
		
		public Color getNodeExpandedForeground() {
			return _nodeExpandedForeground;
		}
		
		public Color getNodeUnExpandedForeground() {
			return _nodeUnExpandedForeground;
		}
		
		public Color getIconColor() {
			return _eicon.color;
		}
		
		public void setIconColor(Color c) {
			_eicon.color=c;
		}
}
