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

import java.awt.Dimension;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.swing.JLabel;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;


abstract public class AbstractSplitTableModel extends AbstractTableModel {
	
	//static Logger logger=zc3Logger.getLogger(AbstractSplitTableModel.class);

	class SplitModel extends AbstractTableModel {
		
		private boolean 					_left;
		private AbstractSplitTableModel 	_model;
		
		public SplitModel(AbstractSplitTableModel m,boolean left) {
			_left=left;
			_model=m;
		}

		public int getColumnCount() {
			if (_left) {
				return _model.getSplitColumn();
			} else {
				return _model.getColumnCount()-_model.getSplitColumn();
			}
		}

		public int getRowCount() {
			return _model.getRowCount();
		}

		public Object getValueAt(int row, int col) {
			if (_left) {
				return _model.getValueAt(row,col);
			} else {
				return _model.getValueAt(row, _model.getSplitColumn()+col);
			}
		}
		
		public String getColumnName(int col) {
			if (_left) {
				return _model.getColumnName(col);
			} else {
				return _model.getColumnName(_model.getSplitColumn()+col);
			}
		}
		
		public Class<?> getColumnClass(int columnIndex) {
			return _model.getColumnClass(columnIndex);
		}
		
		public boolean isCellEditable(int rowIndex,int columnIndex) {
			return _model.isCellEditable(rowIndex, columnIndex);
		}
		
		public void setValueAt(Object val,int rowIndex,int colIndex) {
			_model.setValueAt(val,rowIndex,colIndex);
		}
		
	}
	
	
	public AbstractSplitTableModel() {
		_left=new SplitModel(this,true);
		_right=new SplitModel(this,false);	
	}
	
	private AbstractTableModel _left;
	private AbstractTableModel _right;
	
	public AbstractTableModel modelLeft() {
		return _left;
	}
	
	public AbstractTableModel modelRight() {
		return _right;
	}
	
	public interface ColumnWidthListener {
		public void prefferedWidthForColumn(int col,int width);
	}
	
	private Set<ColumnWidthListener> _listeners=new HashSet<ColumnWidthListener>();  
	
	public void addColumnWidthListener(ColumnWidthListener l) {
		_listeners.add(l);
		informListener(l);
	}
	
	public void removeColumnWidthListener(ColumnWidthListener l) {
		_listeners.remove(l);
	}

	private SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private Date             ctime=new Date();
	
	//private void logtime(String msg) {
	//	ctime.setTime(System.currentTimeMillis());
	//	logger.debug(format.format(ctime)+" "+msg);
	//}

	private JLabel _label=new JLabel(" ");
	
	private void informListener(ColumnWidthListener l) {
		int i,N;
		//logtime("Calc widths B ");
		for(i=0,N=this.getColumnCount();i<N;i++) {
			String s=this.getMaxString(i);
			if (s!=null) {
				_label.setText(s);
				Dimension d=_label.getPreferredSize();
				int width=d.width;
				l.prefferedWidthForColumn(i, width);
			}
		}
		//logtime("Calc widths E ");
	}
	
	private JLabel q=new JLabel();
	
	public void informListeners() {
		int i,N;
		for(i=0,N=this.getColumnCount();i<N;i++) {
			String s=this.getMaxString(i);
			if (s!=null) {
				q.setText(s);
				Dimension d=q.getPreferredSize();
				int width=d.width;
				Iterator<ColumnWidthListener> it=_listeners.iterator();
				while (it.hasNext()) {
					ColumnWidthListener c=it.next();
					if (c!=null) {
						c.prefferedWidthForColumn(i, width);
					}
				}
			}
		}
	}
	
	/**
	 * Voor intern gebruik.
	 * 
	 * @param lm
	 * @param rm
	 */
	private void setModels(AbstractTableModel lm,AbstractTableModel rm) {
		_left=lm;
		_right=rm;
	}

	/**
	 * Moet de colom terug geven waar moet worden gedeeld.
	 * 
	 * @return
	 */
	abstract public int getSplitColumn();
	
	
	/** 
	 * Geef een string met maximale lengte terug, om de breedte van een 
	 * colom te bepalen. null ==> er is geen maximale lengte om te gebruiken.
	 *   
	 * @param col
	 * @return
	 */
	public String getMaxString(int col) {
		return null;
	}
	
	public void fireTableDataChanged() {
		if (_left!=null) {
			_left.fireTableDataChanged();
			_right.fireTableDataChanged();
		}
	}
	
	public void fireTableChanged(TableModelEvent e) {
		if (_left!=null) {
			_left.fireTableChanged(e);
			_right.fireTableChanged(e);
		}
	}
	
	public void fireTableCellUpdated(int row,int column) {
		if (_left!=null) {
			_left.fireTableCellUpdated(row, column);
			_right.fireTableCellUpdated(row, column);
		}
	}
	
	public void fireTableRowsDeleted(int firstRow,int lastRow) {
		if (_left!=null) {
			_left.fireTableRowsDeleted(firstRow, lastRow);
			_right.fireTableRowsDeleted(firstRow, lastRow);
		}
	}
	
	public void fireTableRowsInserted(int firstRow,int lastRow) {
		if (_left!=null) {
			_left.fireTableRowsInserted(firstRow, lastRow);
			_right.fireTableRowsInserted(firstRow, lastRow);
		}
	}
	
	public void fireTableRowsUpdated(int firstRow,int lastRow) {
		if (_left!=null) {
			_left.fireTableRowsUpdated(firstRow, lastRow);
			_right.fireTableRowsUpdated(firstRow, lastRow);
		}
	}
	
	public void fireTableStructureChanged() {
		if (_left!=null) {
			_left.fireTableStructureChanged();
			_right.fireTableStructureChanged();
			informListeners();
		}
	}
	
	public TableModelListener[] getTableModelListeners() {
		return _right.getTableModelListeners();
	}

	


	
}
