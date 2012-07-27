/*
 * $Id: TableRowKeyedSorter.java 1990 2007-11-27 10:23:10Z MiguelM $
 *
 * Copyright 2007 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.table;

import javax.swing.table.TableRowSorter;
import javax.swing.table.TableStringConverter;
import java.util.Comparator;

/**
 * An implementation of TableRowSorter for a JTable that uses a SeparatedTableModel.
 * <p>
 * While the {@code TableRowKeyedSorter} isn't necessary to sort a {@code JTable} that 
 * uses a SeparatedTableModel, it allows you to take advantage of the 
 * SeparatedTableModel's features and paradigm. Instead of writing a StringConverter, 
 * you can override AbstractColumn.convertToString(). (Consequently, the 
 * {@code useToString()} method always returns false.) Likewise, you don't set a 
 * Comparator for each column. Instead, you override 
 * {@code AbstractColumn.getComparableValue()}. Both of these changes allow you to
 * keep all the information about a column's behavior in the column class. 
 * @author Miguel Mu\u00f1oz
 * @param <M> The SeparatedTableModel or its subclass.
 * @see org.jdesktop.swingx.table.SeparatedTableModel
 */
public class TableRowKeyedSorter<M extends SeparatedTableModel> extends TableRowSorter<M> {
	private M mTableModel;

	/**
	 * Creates a {@code TableRowKeyedSorter} with an empty model
	 */
	public TableRowKeyedSorter() { }

	/**
	 * Creates a {@code TableRowKeyedSorter} using {@code pModel} as the underlying {@code TableModel}.
	 * @param pModel The underlying {@code TableModel} to use, {@code null} is treated as an 
	 * empty model.
	 */
	public TableRowKeyedSorter(M pModel) {
		super(pModel);
	}

	/**
	 * Sets the {@code SeparatedTableModel} to use as the underlying model for this 
	 * {@code TableRowKeyedSorter}. A value of {@code null} can be used to set an empty model.
	 * @param model The {@code SeparatedTableModel}
	 */
	@Override
	public void setModel(M model) {
		super.setModel(model);
		mTableModel = model;
		setModelWrapper(new KeyedSortModelWrapper());
	}

	/**
	 * Returns false. This sorter does not convert to a {@code String}, since 
	 * {@code AbstractColumn.convertToString()} already does so.
	 *
	 * @param column Unused
	 * @return false
	 */
	@Override
	protected boolean useToString(int column) {
		return false;
	}

	/**
	 * Do not use. Override {@code AbstractColumn.getComparableValue()} instead.
	 * <p>
	 * Comparisons are done by overriding {@code AbstractColumn.getComparableValue()}.
	 * This method throws a {@code RuntimeException}.
	 * @param column Unused
	 * @param comparator Unused
	 * @see org.jdesktop.swingx.table.AbstractColumn#getComparableValue(Object) 
	 * @throws UnusedMethodException
	 */
	public void setComparator(int column, Comparator<?> comparator) {
		throw new UnusedMethodException("TableRowKeyedSorter does not use the setComparator() method. Override AbstractColumn.getComparableValue() instead.");
	}

	/**
	 * Do not use. Override {@code AbstractColumn.convertToString()} instead.
	 * <p>
	 * String conversions are done by overriding {@code AbstractColumn.convertToString()}.
	 * @param stringConverter Unused.
	 * @see org.jdesktop.swingx.table.AbstractColumn#convertToString(Object)
	 * throws UnusedMethodException 
	 */
	public void setStringConverter(TableStringConverter stringConverter) {
		throw new UnusedMethodException("TableRowKeyedSorter does not use the TableStringConverter. Override AbstractColumn.convertToString() instead.");
	}

	/**
	 * gets a generic comparator that works with the column.
	 * @param column The column
	 * @return A comparator.
	 */
	public Comparator<?> getComparator(int column) {
		return mTableModel.getColumn(column).getComparator();
	}

	private class KeyedSortModelWrapper extends ModelWrapper<M, Integer> {

		public M getModel() { return mTableModel; }

		public int getColumnCount() { return getModel().getColumnCount(); }

		public int getRowCount() { return getModel().getRowCount(); }

		public Object getValueAt(int row, int column) {
			return getModel().getComparableValue(row, column);
		}

		public Integer getIdentifier(int row) { return row; }
	}
	
	public static class UnusedMethodException extends RuntimeException {

		public UnusedMethodException(String message) {
			super(message);
		}
	}
}
