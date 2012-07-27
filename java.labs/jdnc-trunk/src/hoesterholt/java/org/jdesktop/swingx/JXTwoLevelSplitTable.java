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

package org.jdesktop.swingx;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.swing.Icon;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

import org.jdesktop.swingx.decorator.BorderHighlighter;
import org.jdesktop.swingx.decorator.ColorHighlighter;
import org.jdesktop.swingx.decorator.ComponentAdapter;
import org.jdesktop.swingx.decorator.HighlightPredicate;
import org.jdesktop.swingx.decorator.IconHighlighter;
import org.jdesktop.swingx.renderer.JRendererLabel;

import org.jdesktop.swingx.splittable.AbstractTwoLevelSplitTableModel;
import org.jdesktop.swingx.splittable.SplitTableDefaults;
import org.jdesktop.swingx.splittable.AbstractTwoLevelSplitTableModel.CNodeIndex;

public class JXTwoLevelSplitTable extends JXSplitTable {
	
	class ExpandIcon implements Icon {

		public int 		height=10;
		public int 		width=10;
		public boolean  expanded=false;
		public Color    color=Color.gray;
		private Polygon _p=new Polygon();
		
		public ExpandIcon(boolean exp) {
			_p.addPoint(0,0);
			_p.addPoint(0,0);
			_p.addPoint(0,0);
			expanded=exp;
		}
		
		public void setExpanded(boolean b) {
			expanded=b;
		}
		
		public int getIconHeight() {
			return height; 
		}

		public int getIconWidth() {
			return width; 
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

	
	private static final long serialVersionUID = 1L;

	public interface SelectionListener {
		
		/**
		 * Indicates that nodeRow in nodeIndex at col has been choosen in the left or the right 
		 * part of the Split Table. If nodeRow==-1, then only the node has been choosen, no
		 * row within. (choosen==double click)
		 * 
		 * @param nodeIndex
		 * @param nodeRow
		 * @param col
		 * @param left
		 */
		public void choosen(int nodeIndex,int nodeRow,int col,boolean left);
		
		/**
		 * Indicates that nodeRow in nodeIndex at col has been selected in the left or the right 
		 * part of the Split Table. If nodeRow==-1, then only the node has been selected, no
		 * row within. (selected==single click)
		 * 
		 * @param nodeIndex
		 * @param nodeRow
		 * @param col
		 * @param left
		 */
		public void selected(int nodeIndex,int nodeRow,int col,boolean left);
		
		/**
		 * Indicates that a previous selection in the left (left==true) or the right part
		 * is unselected, i.e. after this, no selection is active anymore in the left or
		 * the right part.
		 *  
		 * @param left
		 */
		public void unSelected(boolean left);
		
	}
	
	
	private AbstractTwoLevelSplitTableModel _model;
	private Set<SelectionListener>          _listeners;
	

	/*private TableCellRenderer 				_leftNodeRenderer;
	private TableCellRenderer				_rightNodeRenderer;
	private TableCellRenderer               _leftRenderer;
	private TableCellRenderer               _rightRenderer;*/
	
	/*public TableCellRenderer getLeftNodeRenderer() {
		return _leftNodeRenderer;
	}
	
	public TableCellRenderer getRightNodeRenderer() {
		return _rightNodeRenderer;
	}
	
	public void setLeftNodeRenderer(TableCellRenderer renderer) {
		_leftNodeRenderer=renderer;
	}

	public void setRightNodeRenderer(TableCellRenderer renderer) {
		_rightNodeRenderer=renderer;
	}*/
	
	/*
	public void setNodeIconColor(Color c) throws TwoLevelSplitTableException {
		if (_leftNodeRenderer instanceof TwoLevelSplitTableNodeCellRenderer) {
			((TwoLevelSplitTableNodeCellRenderer) _leftNodeRenderer).setIconColor(c);
		} else {
			throw new TwoLevelSplitTableException("Need an instance of TwoLevelSplitTableNodeCellRenderer to set the Icon color");
		}
	}
	
	public void setNodeBackgroundColor(Color c) throws TwoLevelSplitTableException {
		if (_leftNodeRenderer instanceof TwoLevelSplitTableNodeCellRenderer) {
			((TwoLevelSplitTableNodeCellRenderer) _leftNodeRenderer).setNodeExpandedBackground(c);
			((TwoLevelSplitTableNodeCellRenderer) _leftNodeRenderer).setNodeExpandedBackground(c);
			((TwoLevelSplitTableNodeCellRenderer) _leftNodeRenderer).setNodeUnExpandedBackground(c);
			((TwoLevelSplitTableNodeCellRenderer) _leftNodeRenderer).setNodeUnExpandedBackground(c);
		} else {
			throw new TwoLevelSplitTableException("Need an instance of TwoLevelSplitTableNodeCellRenderer to set the background color");
		}
	}
	
	public void setLeftRenderer(TableCellRenderer renderer) {
		_leftRenderer=new TableCellRendererWrapper(true,_model,renderer);
		super.setDefaultLeftRenderer(Object.class, _leftRenderer);
	}
	
	public void setRightRenderer(TableCellRenderer renderer) {
		_rightRenderer=new TableCellRendererWrapper(false,_model,renderer);
		super.setDefaultRightRenderer(Object.class, _rightRenderer);
	}
	*/
	
	public void addSelectionListener(SelectionListener l) {
		_listeners.add(l);
	}
	
	public void removeSelectionListener(SelectionListener l) {
		_listeners.remove(l);
	}

	/*
	class TableCellRendererWrapper implements TableCellRenderer {
		
		private TableCellRenderer 				_previous;
		private AbstractTwoLevelSplitTableModel _model;
		private boolean                         _left;
		private CompoundBorder                  _border;
		private CompoundBorder					_topBorder;
		private CompoundBorder					_topLeftBorder;
		private CompoundBorder					_leftBorder;
		
		public TableCellRendererWrapper(boolean left,AbstractTwoLevelSplitTableModel model,TableCellRenderer prev) {
			_previous=(TableCellRenderer) prev;
			_model=model;
			_left=left;
			_border=new CompoundBorder(new MatteBorder(0,0,1,1,Color.gray),new EmptyBorder(1,1,1,1));
			_topBorder=new CompoundBorder(new MatteBorder(1,0,1,1,Color.gray),new EmptyBorder(1,1,1,1));
			_topLeftBorder=new CompoundBorder(new MatteBorder(1,1,1,1,Color.gray),new EmptyBorder(1,1,1,1));
			_leftBorder=new CompoundBorder(new MatteBorder(0,1,1,1,Color.gray),new EmptyBorder(1,1,1,1));
		}

		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {
			AbstractTwoLevelSplitTableModel.CNodeIndex cnode=_model.getCNodeIndex(row, column);
			if (cnode.nodeRow==-1) {
				if (_left) {
					return _leftNodeRenderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
				} else {
					return _rightNodeRenderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
				}
			} else {
				Component c;
				c=_previous.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
				return c;
				if (c instanceof JRendererLabel || c instanceof DefaultTableCellRenderer || c instanceof JPanel) { 
					if (cnode.nodeRow==0 && cnode.column==0) {
						if (_left) {
							((JRendererLabel) c).setBorder( _topLeftBorder );
						} else {
							((JRendererLabel) c).setBorder( _topBorder );
						}
					} else if (cnode.nodeRow==0) {
						((JRendererLabel) c).setBorder( _topBorder );
					} else if (cnode.column==0) {
						if (_left) {
							((JRendererLabel) c).setBorder( _leftBorder );
						} else {
							((JRendererLabel) c).setBorder( _border );
						}
					} else {
						((JRendererLabel) c).setBorder( _border );
					}
				}
				return c;
				
			}
		}
	}*/
	
	public JXTwoLevelSplitTable(String name, AbstractTwoLevelSplitTableModel model,
			int verticalScrollPolicy, int horizontalScrollPolicy) {
		super(name, model, verticalScrollPolicy, horizontalScrollPolicy);
		
		super.setShowGrid(false);
		super.setIntercellSpacing(new Dimension(0,1));
		super.setSortable(false);
		
		_model=model;
		_listeners=new HashSet<SelectionListener>();
		
		/*_leftNodeRenderer=new TwoLevelSplitTableNodeCellRenderer(true,_model,super.getDefaultLeftRenderer(Object.class));
		_rightNodeRenderer=new TwoLevelSplitTableNodeCellRenderer(false,_model,super.getDefaultRightRenderer(Object.class));
		_leftRenderer=new TableCellRendererWrapper(true,_model,super.getDefaultLeftRenderer(Object.class));
		_rightRenderer=new TableCellRendererWrapper(false,_model,super.getDefaultRightRenderer(Object.class));
		super.setDefaultLeftRenderer(Object.class, _leftRenderer);
		super.setDefaultRightRenderer(Object.class, _rightRenderer);*/
		
		HighlightPredicate paintIconExpanded=new HighlightPredicate() {
			public boolean isHighlighted(Component renderer, ComponentAdapter adapter) {
				if (adapter.column==0) {
					CNodeIndex cnode=_model.getCNodeIndex(adapter.row, 0);
					if (cnode.nodeRow==-1) {
						return _model.getNodeExpanded(cnode.nodeIndex);
					} else {
						return false;
					}
				} else {
					return false;
				}
			}
		};
		HighlightPredicate paintIconClosed=new HighlightPredicate() {
			public boolean isHighlighted(Component renderer, ComponentAdapter adapter) {
				if (adapter.column==0) {
					CNodeIndex cnode=_model.getCNodeIndex(adapter.row, 0);
					if (cnode.nodeRow==-1) {
						return !_model.getNodeExpanded(cnode.nodeIndex);
					} else {
						return false;
					}
				} else {
					return false;
				}
			}
		};
		HighlightPredicate splitRow=new HighlightPredicate() {
			public boolean isHighlighted(Component renderer, ComponentAdapter adapter) {
				CNodeIndex cnode=_model.getCNodeIndex(adapter.row, adapter.column);
				if (cnode.nodeRow==-1) {
					return true;
				} else {
					return false;
				}
			}
		};
		HighlightPredicate top=new HighlightPredicate() {
			public boolean isHighlighted(Component renderer, ComponentAdapter adapter) {
				return (adapter.row==0);
			}
		};
		HighlightPredicate left=new HighlightPredicate() {
			public boolean isHighlighted(Component rederer, ComponentAdapter adapter) {
				return (adapter.column==0);
			}
		};
		
		
		super.addHighlighter(new IconHighlighter(paintIconExpanded,new ExpandIcon(true)),true);
		super.addHighlighter(new IconHighlighter(paintIconClosed,new ExpandIcon(false)),true);
		super.addHighlighter(new ColorHighlighter(splitRow,SplitTableDefaults.tablePartOfBg(),Color.black),true);
		super.addHighlighter(new ColorHighlighter(splitRow,SplitTableDefaults.tablePartOfBg(),Color.black),false);
		super.addHighlighter(new BorderHighlighter(top,SplitTableDefaults.topCellBorder()), true);
		super.addHighlighter(new BorderHighlighter(left,SplitTableDefaults.leftCellBorder()), false);
		
		
		super.addSelectionListener(new JXSplitTable.SelectionListener() {
			public void choosen(int row, int col, boolean left) {
				AbstractTwoLevelSplitTableModel.CNodeIndex cnode=_model.getCNodeIndex(row,col);
				if (cnode.isValid()) {
					Iterator<SelectionListener> it=_listeners.iterator();
					
					if (cnode.nodeRow==-1) {
						boolean expanded=_model.getNodeExpanded(cnode.nodeIndex);
						boolean newExpanded=_model.setNodeExpanded(cnode.nodeIndex, !expanded);
						if (newExpanded!=expanded) { _model.fireTableDataChanged(); }
					}
					
					while(it.hasNext()) {
						it.next().choosen(cnode.nodeIndex, cnode.nodeRow, cnode.column, left);
					}
				}
			}

			public void selected(int row, int col, boolean left) {
				AbstractTwoLevelSplitTableModel.CNodeIndex cnode=_model.getCNodeIndex(row,col);
				if (cnode.isValid()) {
					Iterator<SelectionListener> it=_listeners.iterator();
					while(it.hasNext()) {
						it.next().selected(cnode.nodeIndex,cnode.nodeRow,cnode.column,left);
					}
				}
			}

			public void unSelected(boolean left) {
				Iterator<SelectionListener> it=_listeners.iterator();
				while(it.hasNext()) {
					it.next().unSelected(left);
				}
			}
		});
	}
	
}
