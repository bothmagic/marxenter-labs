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

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;

import net.miginfocom.swing.MigLayout;

import org.jdesktop.swingx.decorator.Highlighter;
import org.jdesktop.swingx.table.TableColumnExt;

import org.jdesktop.swingx.splittable.AbstractSplitTableModel;
import org.jdesktop.swingx.splittable.Jzc3Table;
//import widgets.Jzc3Table;

class MyJzc3Table extends Jzc3Table {
	
	public String getTTT(MouseEvent e) {
		return super.getToolTipText(e);
	}
	
	public MyJzc3Table(String id,AbstractTableModel m) {
		super(id,m);
	}
	
}

public class JXSplitTable extends JPanel {
	
	/**
	 * NB. A select() or choosen() in the right part (left==false), will result in a unSelected() in
	 * the left part (left==true);
	 * @author Hans Oesterholt
	 *
	 */
	public interface SelectionListener {
		
		/**
		 * Choosen is called on doubleclick
		 * If left==true, a column to the left of the column split has been choosen.
		 * @param row
		 * @param col
		 */
		public void choosen(int row,int col,boolean left);
		
		/**
		 * Selected is called when a cell gets selected
		 * If left==true, a column to the left of the column split has been selected.
		 * @param row
		 * @param col
		 */
		public void selected(int row,int col,boolean left);
		
		/**
		 * unSelected is called when no cell is selected
		 * If left==true, the column to the left has been unselected.
		 */
		public void unSelected(boolean left);
	}
	
	
	public static int VERTICAL_SCROLLBAR_NEVER=JScrollPane.VERTICAL_SCROLLBAR_NEVER;
	public static int VERTICAL_SCROLLBAR_ALWAYS=JScrollPane.VERTICAL_SCROLLBAR_ALWAYS;
	public static int VERTICAL_SCROLLBAR_AS_NEEDED=JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED;
	public static int HORIZONTAL_SCROLLBAR_AS_NEEDED=JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED;
	public static int HORIZONTAL_SCROLLBAR_NEVER=JScrollPane.HORIZONTAL_SCROLLBAR_NEVER;
	public static int HORIZONTAL_SCROLLBAR_ALWAYS=JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS;
	
	private MyJzc3Table 	_tleft;
	private MyJzc3Table 	_tright;
	
	private JScrollPane _spane;
	
	private Set<SelectionListener> _listeners;
	
	public JScrollPane getScrollPane() {
		return _spane;
	}
	
	public void addSelectionListener(SelectionListener l) {
		_listeners.add(l);
	}
	
	public void removeSelectionListener(SelectionListener l) {
		_listeners.remove(l);
	}
	
	private void informChoosen(int row,int col,boolean left) {
		Iterator<SelectionListener> it=_listeners.iterator();
		while (it.hasNext()) { it.next().choosen(row,col, left); }
	}
	
	private void informSelected(int row,int col,boolean left) {
		Iterator<SelectionListener> it=_listeners.iterator();
		while (it.hasNext()) { it.next().selected(row,col, left); }
	}
	
	private void informNoSelection(boolean left) {
		Iterator<SelectionListener> it=_listeners.iterator();
		while (it.hasNext()) { it.next().unSelected(left); }
	}
	
	public Jzc3Table leftTable() {
		return _tleft;
	}
	
	public Jzc3Table rightTable() {
		return _tright;
	}
	
	public TableCellRenderer getDefaultLeftRenderer(Class<?> columnClass) {
		return _tleft.getDefaultRenderer(columnClass);
	}
	
	public TableCellRenderer getDefaultRightRenderer(Class<?> columnClass) {
		return _tright.getDefaultRenderer(columnClass);
	}
	
	public void setDefaultLeftRenderer(Class<?> columnClass,TableCellRenderer renderer) {
		_tleft.setDefaultRenderer(columnClass, renderer);
	}
	
	public void setDefaultRightRenderer(Class<?> columnClass,TableCellRenderer renderer) {
		_tright.setDefaultRenderer(columnClass, renderer);
	}
	
	public void setShowGrid(boolean g) {
		_tleft.setShowGrid(g);
		_tright.setShowGrid(g);
	}
	
	public void setIntercellSpacing(Dimension d) {
		_tleft.setIntercellSpacing(d);
		_tright.setIntercellSpacing(d);
	}
	
	public void setSortable(boolean b) {
		_tleft.setSortable(b);
		_tright.setSortable(b);
	}
	
	public TableColumnExt getColumnExt(int viewColumnIndex,boolean left) {
		if (left) {
			return _tleft.getColumnExt(viewColumnIndex);
		} else {
			return _tright.getColumnExt(viewColumnIndex);
		}
	}
	
	public int getColumnMargin(boolean left) {
		if (left) {
			return _tleft.getColumnMargin();
		} else {
			return _tright.getColumnMargin();
		}
	}
	
	public void packColumn(int column,int margin,boolean left) {
		if (left) {
			_tleft.packColumn(column, margin);
		} else {
			_tright.packColumn(column,margin);
		}
	}
	
	public JTableHeader getTableHeader() {
		return _tright.getTableHeader();
	}
	
	public void addHeaderListener(Jzc3Table.HeaderListener h,boolean left) {
		if (left) {
			_tleft.addHeaderListener(h);
		} else {
			_tright.addHeaderListener(h);
		}
	}
	
	public void removeHeaderListener(Jzc3Table.HeaderListener h,boolean left) {
		if (left) {
			_tleft.removeHeaderListener(h);
		} else {
			_tright.removeHeaderListener(h);
		}
	}
	
	public Object getValueAt(boolean left,int row,int col) {
		return (left) ? _tleft.getValueAt(row, col) : _tright.getValueAt(row, col);
	}
	
	public int rowAtPoint(boolean left,Point p) {
		return (left) ? _tleft.rowAtPoint(p) : _tright.rowAtPoint(p);
	}
	
	public int columnAtPoint(boolean left,Point p) {
		return (left) ? _tleft.columnAtPoint(p) : _tright.columnAtPoint(p);
	}
	
	
	public String getToolTipText(boolean left,MouseEvent e) {
		if (left) {
			return _tleft.getTTT(e);
		} else {
			return _tright.getTTT(e);
		}
	}
	
	public void setToolTipText(boolean left,String txt) {
		if (left) {
			_tleft.setToolTipText(txt);
		} else {
			_tright.setToolTipText(txt);
		}
	}
	
	public void addHighlighter(Highlighter h,boolean left) {
		if (left) {
			_tleft.addHighlighter(h);
		} else {
			_tright.addHighlighter(h);
		}
	}
	
	public void removeHighlighter(Highlighter h,boolean left) {
		if (left) 
			_tleft.removeHighlighter(h);
		else
			_tright.removeHighlighter(h);
	} 
	
	
	@SuppressWarnings("serial")
	public JXSplitTable(String name,AbstractSplitTableModel model,int verticalScrollPolicy,int horizontalScrollPolicy) {//,int minimumWidth) {
		_listeners=new HashSet<SelectionListener>();
		
		_tleft=new MyJzc3Table(name+".left",model.modelLeft()) {
			public String getToolTipText(MouseEvent e) {
				return JXSplitTable.this.getToolTipText(true,e);
			}
		};
		
		_tright=new MyJzc3Table(name+".right",model.modelRight()) {
			public String getToolTipText(MouseEvent e) {
				return JXSplitTable.this.getToolTipText(false,e);
			}
		};
		
		_tright.setHorizontalScrollEnabled(true);
		_tright.setCellSelectionEnabled(true);
		
		_tleft.setCellSelectionEnabled(true);
		
		_tleft.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		_tright.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		_tright.addSelectionListener(new Jzc3Table.SelectionListener() {
			public void choosen(int row) {
				_tleft.clearSelection();
				informChoosen(row,_tright.getSelectedColumn(),false);
			}
			public void noSelection() {
				informNoSelection(false);
			}
			public void selected(int row) {
				_tleft.clearSelection();
				informSelected(row,_tright.getSelectedColumn(),false);
			}
		});
		
		_tleft.addSelectionListener(new Jzc3Table.SelectionListener() {
			public void choosen(int row) {
				_tright.clearSelection();
				informChoosen(row,_tleft.getSelectedColumn(),true);
			}
			public void noSelection() {
				informNoSelection(true);
			}
			public void selected(int row) {
				_tright.clearSelection();
				informSelected(row,_tleft.getSelectedColumn(),true);
			}
		});
		
		super.setLayout(new MigLayout("insets 0,fill"));	// TODO: Change this to something not relying on MigLayout
		_spane=new JScrollPane(_tright,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED); 
		_spane.setRowHeaderView(_tleft);
		
		super.add(_spane,"growx,growy");
		
		
	}

}
