/** DataTableModel.java                          Testx
 *
 * Created 08/06/2007 1:23:02 PM
 * 
 * @author Ray Turnbull
 */
package datatablemodel;

import javax.swing.table.AbstractTableModel;

import org.jdesktop.databuffer.DataRow;
import org.jdesktop.databuffer.DataTable;





/**
 * A TableModel to link a JXTable to a DataTable
 */
public class DataTableModel extends AbstractTableModel {

	protected DataTable dTable;
	protected String[] colNames;
	
	/**
	 * 
	 */
	public DataTableModel(DataTable dTable, String... colNames) {
		this.dTable = dTable;
		this.colNames = colNames;
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#getColumnCount()
	 */
	@Override
	public int getColumnCount() {
		return colNames.length;
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return dTable.getColumn(colNames[columnIndex]).getType();
	}
	
	@Override
	public String getColumnName(int column) {
		return colNames[column];
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#getRowCount()
	 */
	@Override
	public int getRowCount() {
		return dTable.getRowCount();
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#getValueAt(int, int)
	 */
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		DataRow row = dTable.getRow(rowIndex);
		return row.getValue(colNames[columnIndex]);
	}
	
}
