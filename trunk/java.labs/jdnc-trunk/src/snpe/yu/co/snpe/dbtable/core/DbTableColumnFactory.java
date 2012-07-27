
package yu.co.snpe.dbtable.core;

import java.awt.Component;

import javax.swing.table.TableCellRenderer;

import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.table.ColumnFactory;
import org.jdesktop.swingx.table.TableColumnExt;

/** 
 * 
 * compute column's width with only 50 rows
 * @author snpe
 *
 */
public class DbTableColumnFactory extends ColumnFactory {

	private static final int MAX_ROW_COUNT = 50;

	private static ColumnFactory columnFactory;

	public static synchronized ColumnFactory getInstance() {
		if (columnFactory == null) {
			columnFactory = new DbTableColumnFactory();
		}
		return columnFactory;
	}

	public static synchronized void setInstance(ColumnFactory factory) {
		columnFactory = factory;
	}

	@Override
	public void packColumn(JXTable table, TableColumnExt col, int margin,
			int max) {
		/* Get width of column header */
		TableCellRenderer renderer = col.getHeaderRenderer();
		if (renderer == null)
			renderer = table.getTableHeader().getDefaultRenderer();

		int width = 0;

		Component comp = renderer.getTableCellRendererComponent(table, col
				.getHeaderValue(), false, false, 0, 0);
		width = comp.getPreferredSize().width;

		int column = table.convertColumnIndexToView(col.getModelIndex());
		if (table.getRowCount() > 0)
			renderer = table.getCellRenderer(0, column);
		int rowCount = table.getRowCount();
		if (rowCount > MAX_ROW_COUNT)
			rowCount = MAX_ROW_COUNT;
		for (int r = 0; r < rowCount; r++) {
			comp = renderer.getTableCellRendererComponent(table, table
					.getValueAt(r, column), false, false, r, column);
			width = Math.max(width, comp.getPreferredSize().width);
		}
		width += 2 * margin;

		/* Check if the width exceeds the max */
		if (max != -1 && width > max)
			width = max;

		col.setPreferredWidth(width);

	}

}
