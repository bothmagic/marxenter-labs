/*
 * $Id: SeparatedTableModel.java 1990 2007-11-27 10:23:10Z MiguelM $
 *
 * Copyright 2007 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.table;

import java.util.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.*;
import javax.swing.event.TableModelListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableColumnModelListener;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;

/**
 * This class implements a {@code TableModel} by splitting the model into separate row and
 * column models. This solves many common maintainence issues with TableModels, and makes 
 * them much easier to work with. <p>
 * This can't be used with all {@code TableModel}s, but it can be used with many tables 
 * that share a very common structure. It makes two assumptions about the table structure 
 * in order to separate the model into a row model and some column models. This class then 
 * implements the model's methods by extracting the row and passing it to the specified 
 * column. It is then up to the developer to supply the columns by extending the 
 * {@code AbstractColumn} class, which is very simple.
 * <p/>
 * The assumptions are common to most tables. They are: <br>
 * 1) Each row represents an instance of a the same class. <br>
 * 2) Each column represents a field from that instance, or a value that can be 
 * derived from that instance. <p> 
 * If your TableModel meets these two assumptions, you can use this class. 
 * Each column in a {@code SeparatedTableModel} is a separate class that contains all 
 * the information needed to display that column in the table, including information 
 * used by the {@code JTable}'s column headers. The columns are implemented by 
 * extending the {@code AbstractColumn} class and overriding its <code>getValue()</code> 
 * method, and any other methods that are needed. This makes the TableModel much 
 * easier to maintain, since any changes to a specific column are made in that 
 * column's class, and won't interact with the code for any other column.
 * <p>
 * Column classes tend to be small. In the example below, they are written as static inner 
 * classes of the main {@code TableModel} class for convenience, but this is a matter of
 * personal preference.
 * <p>
 * This example displays some information about people. Each individual is represnted 
 * by an instance of class Person, which has getters and setters for the name and 
 * birthday fields, and a getter for the "age" property. A SeparatedTableModel might 
 * look like this:
 * <pre>
 * public class PeopleTableModel extends SeparatedTableModel{@literal <Person>} {
 *   PeopleTableModel(Collection{@literal <Person>} rows) {
 *     super(rows);
 *     addColumn(new FirstNameColumn());
 *     addColumn(new LastNameColumn());
 *     addColumn(new BirthdayColumn());
 *     addColumn(new AgeColumn());
 *   }
 * 
 *   private static class FirstNameColumn extends AbstractColumn{@literal <Person, String>} {
 *     FirstNameColumn() { super("First Name", String.class, 100); }
 *     public String getValue(Person row) { return row.getFirstName(); }
 *     public void setValue(String firstName, Person row) { row.setFirstName(firstName); }
 *     public Comparable{@literal <?>} getComparableValue(Person row) { return row.getFirstNameKey(); }
 *     public boolean isEditable(Person row) { return true; }
 *   }
 * 
 *   private static class LastNameColumn extends AbstractColumn{@literal <Person, String>} {
 *     LastNameColumn() { super("Last Name", String.class, 100); }
 *     public String getValue(Person row) { return row.getLastName(); }
 *     public void setValue(String lastName, Person row) { row.setLastName(lastName); }
 *     public Comparable{@literal <?>} getComparableValue(Person row) { return row.getLastNameKey(); }
 *     public boolean isEditable(Person row) { return true; }
 *   }
 * 
 *   private static class BirthdayColumn extends AbstractColumn{@literal <Person, Date>} {
 *    BirthdayColumn() { super("Birthday", Date.class, 80, new DateRenderer()); }
 *    public Date getValue(Person row) { return row.getBirthday(); }
 *    public void setValue(Date value, Person row) { row.setBirthday(value); }
 *    public boolean isEditable(Person row) { return true; }
 *   }
 * 
 *   private static class AgeColumn extends AbstractColumn{@literal <Person, Integer>} {
 *     AgeColumn() { super("Age", Integer.class, 40); }
 *     public Integer getValue(Person row) { return row.getAge(); }
 *     public Comparable{@literal <?>} getComparableValue(Person row) { return row.getBirthday(); }
 *   }
 * }
 * </pre>
 * <p>
 * Note that only the first three columns override the {@code isEditable()} method, 
 * and that the third column supplies its own renderer. Also note that three of the 
 * columns implement {@code getComparableValue()}, to return a value that is useful 
 * to sort by. For String classes, this is typically an instance of 
 * {@code java.text.CollationKey}, which lets you ignore case when sorting. Since this 
 * class extends AbstractTableModel, all its familiar table events may be fired.
 * @param <_RowType> The class used to represent each row of the table. (See 
 * assumption 1, below) 
 * @see org.jdesktop.swingx.table.AbstractColumn
 * @see org.jdesktop.swingx.table.RowModel
 * @see org.jdesktop.swingx.table.TableRowKeyedSorter
 * @author Miguel Mu\u00f1oz
 */
public class SeparatedTableModel<_RowType>
				extends AbstractTableModel
				implements BasicRowHeaderModel {
	private RowModel<_RowType> mRows;                   // Here is the row model...
	private List<AbstractColumn<_RowType, ?>> mColumns; // and column model.

	private final PropertyChangeListener columnWidthWatcher = new ColumnWidthWatcher();
//	private static String WIDTH = "width"; // NON-NLS
	private static final String PREFERRED_WIDTH = "preferredWidth"; // NON-NLS
	
	/**
	 * Construct a SeparatedTableModel from a Collection of objects of _RowType.
	 * Columns will be added later. If pRows is a List, this will just use that List
	 * in the model. If pRows is some other type of Collection, this will
	 * construct an ArrayList from the elements and use that in the model.
	 *
	 * @param pRows The Collection
	 */
	public SeparatedTableModel(Collection<_RowType> pRows) {
		this(new RowModel.ListRowModel<_RowType>(pRows));
	}

	/**
	 * Construct a SeparatedTableModel from an array of objects of _RowType.
	 * Columns will be added later.
	 *
	 * @param pRows The array.
	 */
	public SeparatedTableModel(_RowType[] pRows) {
		this(new RowModel.ArrayRowModel<_RowType>(pRows));
	}

	/**
	 * Construct a SeparatedTableModel from a RowModel. The other constructors
	 * call this constructor. Columns will be added later. The other constructors
	 * call this constructor.
	 *
	 * @param pRows The row model.
	 */
	public SeparatedTableModel(RowModel<_RowType> pRows) {
		mColumns = makeColumnList();
		mRows = pRows;
	}

	/**
	 * Constructs the empty List of columns. The default implementation constructs an
	 * ArrayList, but subclasses may override this behavior.
	 *
	 * @return The empty List of columns.
	 */
	final protected List<AbstractColumn<_RowType, ?>> makeColumnList() {
		return new ArrayList<AbstractColumn<_RowType, ?>>();
	}

	protected List<? extends AbstractColumn<_RowType, ?>> getColumnList() {
		return mColumns;
	}

	/**
	 * Appends the column to the current list of columns. <p><em>Note:</em> This
	 * fires a TableStructureChanged event, so it is safe to do after the table
	 * has been displayed.
	 *
	 * @param pCol The column to add.
	 * @see #fireTableStructureChanged()
	 */
	public void addColumn(AbstractColumn<_RowType, ?> pCol) {
		mColumns.add(mColumns.size(), pCol);
		fireTableStructureChanged();
	}

	/**
	 * Inserts the column into the specified place in the list of columns.
	 * <p><em>Note:</em> This fires a TableStructureChanged event, so it
	 * is safe to do after the table has been displayed.
	 *
	 * @param pWhere The model column index
	 * @param pCol   The column to insert
	 */
	public void insertColumn(int pWhere, AbstractColumn<_RowType, ?> pCol) {
		mColumns.add(pWhere, pCol);
		fireTableStructureChanged();
	}
	
	public void removeColumn(int pWhere) {
		mColumns.remove(pWhere);
		fireTableStructureChanged();
	}

	/**
	 * Gets the column at the specified index.
	 *
	 * @param colIndex The index of the column to retrieve.
	 * @return The Column instance.
	 */
	public AbstractColumn<_RowType, ?> getColumn(int colIndex) {
		return mColumns.get(colIndex);
	}

	/**
	 * Returns the number of rows in the model.
	 *
	 * @return the number of rows in the model
	 * @see #getColumnCount
	 */
	public int getRowCount() {
		return mRows.getRowCount();
	}

	/**
	 * Returns the name of the row for the TableRowHeader. The default implementation
	 * just returns the row index.
	 *
	 * @param ii The row index
	 * @return The name of the row.
	 */
	public Object getRowName(int ii) {
		return String.valueOf(ii);
	}

	/**
	 * Returns the number of columns in the model.
	 *
	 * @return the number of columns in the model
	 * @see #getRowCount
	 */
	public int getColumnCount() {
		return mColumns.size();
	}

	public RowModel.Expandable<_RowType> getExpandableRows() { return (RowModel.Expandable<_RowType>) mRows; }

	public RowModel.Editable<_RowType> getEditableRows() { return (RowModel.Editable<_RowType>) mRows; }

	public RowModel<_RowType> getRows() {
		return mRows;
	}

	/**
	 * Returns the value for the cell at <code>columnIndex</code> and
	 * <code>rowIndex</code>. Delegates this work to the column instance.
	 *
	 * @return the value Object at the specified cell
	 * @param	rowIndex	the row whose value is to be queried
	 * @param	columnIndex the column whose value is to be queried
	 */
	public Object getValueAt(int rowIndex, int columnIndex) {
		return getColumn(columnIndex).getValue(mRows.getRow(rowIndex));
	}

	/**
	 * Sets the value in the cell at <code>columnIndex</code> and
	 * <code>rowIndex</code> to <code>aValue</code>. Delegates this work to the
	 * column instance.
	 * <p/>
	 * This also queries the column's updateTable and (if needed) updateRow
	 * properties. If either of those returns true, this calls
	 * fireTableRowsUpdated() with either the whole table (if updateTable is
	 * true) or the current row (if updateRow is true). If updatTable is true,
	 * this doesn't bother to query the updateRow property.
	 *
	 * @param aValue      value to assign to cell
	 * @param rowIndex    row of cell
	 * @param columnIndex column of cell
	 * @see AbstractColumn#getUpdateRow
	 * @see AbstractColumn#getUpdateTable
	 * @see javax.swing.table.AbstractTableModel#fireTableRowsUpdated(int,int)
	 */
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		if (!isCellEditable(rowIndex, columnIndex)) {
			return;
		}
		AbstractColumn<_RowType, ?> column = getColumn(columnIndex);
		setTheValue(rowIndex, column, aValue);
	}

	/**
	 * I do the work of setValue in this private method because it lets me specify a generic type. I can't
	 * do that in setValue because it needs to match the signature of the method it's overriding.
	 *
	 * @param rowIndex     The model row index
	 * @param pColumn      The AbstractColumn subclass
	 * @param unknownValue The value to set.
	 */
	private <V> void setTheValue(int rowIndex, AbstractColumn<_RowType, V> pColumn, Object unknownValue) {
		//noinspection unchecked
		V aValue = (V) unknownValue;
		_RowType theRow = mRows.getRow(rowIndex);
		assert theRow != null;
		pColumn.setValue(aValue, theRow);
		if (pColumn.getUpdateTable(theRow)) {
			fireTableRowsUpdated(0, mRows.getRowCount() - 1);
		} else if (pColumn.getUpdateSubsequentRows(theRow)) {
			fireTableRowsUpdated(rowIndex, mRows.getRowCount() - 1);
		} else if (pColumn.getUpdateRow(theRow)) {
			fireTableRowsUpdated(rowIndex, rowIndex);
		}
	}

	/**
	 * Returns the name of the specified column
	 *
	 * @param column the column being queried
	 * @return the name of <code>column</code>, from the column model
	 */
	public String getColumnName(int column) {
		return getColumn(column).getColumnName();
	}

	/**
	 * Returns the most specific superclass for all the cell values in the
	 * column.  This is used by the <code>JTable</code> to set up a default
	 * renderer and editor for the column. Delegates this to the column
	 * instance.
	 *
	 * @param columnIndex the column being queried
	 * @return the Object.class
	 */
	public Class<?> getColumnClass(int columnIndex) {
		return getColumn(columnIndex).getColumnClass();
	}

	/**
	 * Returns true if the cell at <code>rowIndex</code> and
	 * <code>columnIndex</code> is editable.  Otherwise, <code>setValueAt</code>
	 * on the cell will not change the value of that cell. Delegates this work
	 * to the column instance.
	 *
	 * @param rowIndex    the row being queried
	 * @param columnIndex the column being queried
	 * @return false
	 */
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return getColumn(columnIndex).isEditable(mRows.getRow(rowIndex));
	}

	/**
	 * Returns the preferred width of the column. <p> <b>Note:</b> For this to
	 * work, you must call <code>setUpHeader(JTable tbl)</code> on the table
	 * after setting this as the table model.
	 *
	 * @param column The column index
	 * @return the preferredWidth from the AbsractColumn.
	 * @see #setUpHeader(javax.swing.JTable)
	 */
	public int getPreferredWidth(int column) {
		return getColumn(column).getPreferredWidth();
	}

	/**
	 * Returns the custom renderer for the specified column. If this renderer is
	 * not null, it overrides the default renderer for the column class.<p>
	 * <b>Note:</b> For this to work, you must call <code>setUpHeader(JTable
	 * tbl)</code> on the table after setting this as the table model.
	 *
	 * @param column The column index
	 * @return The custom renderer for the column.
	 * @see #setUpHeader(javax.swing.JTable)
	 */
	public TableCellRenderer getRenderer(int column) {
		return getColumn(column).getRenderer();
	}

	/**
	 * Returns the custom cell editor for specified column. <p> If this editor
	 * is not null, it overrides the default cell editor for the column
	 * class.<p> <b>Note:</b> For this to work, you must call
	 * <code>setUpHeader(JTable tbl)</code> on the table after setting this as
	 * the table model.
	 *
	 * @param column The column index
	 * @return The custom editor for the column
	 * @see #setUpHeader(javax.swing.JTable)
	 */
	public TableCellEditor getEditor(int column) {
		return getColumn(column).getEditor();
	}

	/**
	 * Returns a Comparable for sorting the table's rows by the specified column.
	 *
	 * @param rowIndex    The model index of the row
	 * @param columnIndex The model index of the column
	 * @return A comparable object used by the table row sorter.
	 */
	public Comparable<?> getComparableValue(int rowIndex, int columnIndex) {
		//noinspection unchecked
		return getColumn(columnIndex).getComparableValue(mRows.getRow(rowIndex));
	}

//	public <_ValueType, _SortKey extends Comparable<? super _SortKey>>void sort(int column) {
//		 Comparator<? super _RowType> cmp = makeComparator(column);
//		mRows.sort(cmp);
//		fireTableRowsUpdated(0, mRows.getRowCount() - 1);
//	}

	//

	/**
	 * Since this table model includes information handled by the Table Header
	 * as well as the JTable, we need to send the information to the
	 * JTableHeader. The user should call this method after setting the model on
	 * the JTable. <p>Without this call, this TableModel won't support column
	 * widths, column tool tips, or custom renderers and editors.
	 *
	 * @param tbl The JTable
	 * @see #setUpHeader(javax.swing.JTable)
	 */
	public static void setUpHeader(final JTable tbl) {
		TableModel tMdl = tbl.getModel();
		if (tMdl instanceof SeparatedTableModel) {
			final SeparatedTableModel sMdl = (SeparatedTableModel) tMdl;
			applyHeader(tbl, sMdl);
//			sMdl.fireTableStructureChanged();
			TableModelListener tml = new TableModelListener() {
				public void tableChanged(TableModelEvent e) {
					if (e.getType() == TableModelEvent.UPDATE && e.getLastRow() == TableModelEvent.HEADER_ROW) {
						// Delay processing of event until the TableColumnModel has been updated.
						Runnable evtProcessor = new Runnable() {
							public void run() {
								applyHeader(tbl, sMdl);
							}
						};
						SwingUtilities.invokeLater(evtProcessor);
					}
				}
			};
			sMdl.addTableModelListener(tml);
		}
	}

	private static void applyHeader(JTable pTable, final SeparatedTableModel pModel) {
		JTableHeader hdr = pTable.getTableHeader();
		TableColumnModel columnModel = hdr.getColumnModel();
		for (int col = 0; col < pModel.getColumnCount(); ++col) {
			TableColumn colMdl = columnModel.getColumn(col);
			applyColumnToHeader(pModel.getColumn(col), colMdl, pModel.columnWidthWatcher);
		}
		TableColumnModelListener listener = new TableColumnModelListener() {
			public void columnAdded(TableColumnModelEvent e) { fixColumns(e); }
			// For some reason, a call to fixColumns here is unnecessary and counterproductive.
			// I don't know why, but if I put it in, the one in columnAdded() stops working.
			public void columnRemoved(TableColumnModelEvent e) { }
			public void columnMoved(TableColumnModelEvent e) { }
			public void columnMarginChanged(ChangeEvent e) { }
			public void columnSelectionChanged(ListSelectionEvent e) { }
			private void fixColumns(TableColumnModelEvent e) {
				TableColumnModel source = (TableColumnModel) e.getSource();
				for (int ii = 0; ii < source.getColumnCount(); ++ii)
					applyColumnToHeader(
									pModel.getColumn(ii), 
									source.getColumn(ii), 
									pModel.columnWidthWatcher
					);
			}

		};
		columnModel.addColumnModelListener(listener);
	}

	private static void applyColumnToHeader(AbstractColumn col,
	                                        TableColumn colMdl,
	                                        PropertyChangeListener widthWatcher) {
		TableCellRenderer rndr = col.getRenderer();
		if (rndr != null) {
			colMdl.setCellRenderer(rndr);
		}
		TableCellEditor edt = col.getEditor();
		if (edt != null) {
			colMdl.setCellEditor(edt);
		}
		colMdl.setPreferredWidth(col.getPreferredWidth());
		colMdl.setWidth(col.getWidth());
		int minWidth = col.getMinWidth();
		if (minWidth >= 0)
			colMdl.setMinWidth(minWidth);
		int maxWidth = col.getMaxWidth();
		if (maxWidth >= 0)
			colMdl.setMaxWidth(maxWidth);

		// After a column was added, all the columnModels get rebuilt from scratch, so 
		// we need to set this listener to all of them again.
		colMdl.addPropertyChangeListener(widthWatcher);
	}

	private class ColumnWidthWatcher implements PropertyChangeListener {

		public void propertyChange(PropertyChangeEvent evt) {
			//noinspection CallToStringEquals
//			if (evt.getPropertyName().equals(WIDTH)) {
//				//noinspection StringConcatenation,StringContatenationInLoop,MagicCharacter,HardcodedLineSeparator
//				System.err.println("* Change " + evt.getPropertyName() + " from " + evt.getOldValue() + " to " + evt.getNewValue()); // NON-NLS
//				TableColumn tableColumn = (TableColumn) evt.getSource();
//				int modelIndex = tableColumn.getModelIndex();
//				AbstractColumn column = getColumn(modelIndex);
//				column.setWidth((Integer) evt.getNewValue());
//			}
//			
//			else
			//noinspection CallToStringEquals
			if (evt.getPropertyName().equals(PREFERRED_WIDTH)) {
				TableColumn tableColumn = (TableColumn) evt.getSource();
				int modelIndex = tableColumn.getModelIndex();
				AbstractColumn column = getColumn(modelIndex);
				column.setPreferredWidth((Integer) evt.getNewValue());
			}
		}
	}
}
