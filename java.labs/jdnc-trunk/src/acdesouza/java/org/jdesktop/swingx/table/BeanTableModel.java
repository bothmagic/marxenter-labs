package org.jdesktop.swingx.table;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;

import net.sourceforge.stripes.util.bean.BeanUtil;

/**
 * This is an implementation of <code>AbstractTableModel</code> that
 * uses a <code>List</code> of <code>Object</code> to store the
 * cell value objects.
 * <p>
 * 
 * @version 1.2 29/10/08
 * @author AC de Souza
 *
 * @see AbstractTableModel
 */
//public class BeanTableModel extends AbstractTableModel {
public class BeanTableModel extends DefaultTableModel {
	private static final long serialVersionUID = 1L;

	private List data;
	private Map<String, String> columnPaths;

	private List<String> beanPathExpressions;
	private List<String> columnTitles;

	public BeanTableModel(List<?> data, Map<String, String> columnPaths) {
		this.data = data;
		this.columnPaths = columnPaths;

		extractBeanPathExpressions();
		extractColumnTitles();
	}
	private void extractBeanPathExpressions(){ beanPathExpressions = new ArrayList<String>(columnPaths.keySet()); }
	private void extractColumnTitles(){ columnTitles = new ArrayList<String>(columnPaths.values()); }

	public Class<?> getColumnClass(int columnIndex) {
		int qtdRegistros = data.size();
		for (int i = 0; i < qtdRegistros; i++) {
			if (getValueAt(i, columnIndex) != null) {
				return BeanUtil.getPropertyType(beanPathExpressions.get(columnIndex), data.get(i));
			}
		}

		return String.class;
	}
	public int getColumnCount() { return beanPathExpressions.size(); }
	public String getColumnName(int columnIndex) { return columnTitles.get(columnIndex); }

	public int getRowCount() { return data==null? 0 : data.size(); }

	public Object getValueAt(int rowIndex, int columnIndex) {
		return BeanUtil.getPropertyValue( beanPathExpressions.get(columnIndex), data.get(rowIndex) );
	}
	public void setValueAt(Object value, int rowIndex, int columnIndex) {
		BeanUtil.setPropertyValue(beanPathExpressions.get(columnIndex), data.get(rowIndex), value);
		fireTableCellUpdated(rowIndex, columnIndex);
	}
	public boolean isCellEditable(int rowIndex, int columnIndex) { return false; }

	/**
     *  Adds a row to the end of the model.  The new row will contain
     *  <code>null</code> values unless <code>rowData</code> is specified.
     *  Notification of the row being added will be generated.
     *
     * @param   rowData          optional data of the row being added
     */
	public void addRow(Object rowData) {
		data.add(rowData);
		int row = getRowCount();
		fireTableRowsInserted(row, row);
	}
	
	/**
	 * Remove data associated with index passed as param.
	 * @param rowIndex Index associated with the data you want to remove
	 */
	public void removeRow(int rowIndex) {
		data.remove(rowIndex);
		fireTableRowsDeleted(rowIndex, rowIndex);
	}

	/**
	 * Return object data associated with index passed as param.
	 * 
	 * @param row Index associated with data you looking for.
	 * @return Object data you were looking.
	 */
	public Object getValueAt(int row){
		return data.get( row );
	}

	/**
	 * Find rowData and remove it from TableModel
	 * @param rowData
	 */
	public void removeRow(Object rowData) {
		int row = data.indexOf(rowData);
		data.remove(row);
		fireTableRowsDeleted(row, row);
	}
}