/*
 * $Id: RowModel.java 1995 2007-11-29 08:32:28Z MiguelM $
 *
 * Copyright 2007 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.table;

import java.util.Comparator;
import java.util.List;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Arrays;

/**
 * Support class for SeparatedTableModel. In most cases you don't use this class directly. 
 * Instead, you let the SeparatedTableModel instantiate an inner class of this interface 
 * in its constructor.
 * @see org.jdesktop.swingx.table.SeparatedTableModel 
 * @param <_RowType> The type of each row in the JTable
 * @author Miguel Mu\u00f1oz
 */
public interface RowModel<_RowType> {
	/**
	 * Return a row specified by rowIndex.
	 * @param rowIndex The index of the row
	 * @return The row.
	 */
	public _RowType getRow(int rowIndex);

	/**
	 * Returns the number of rows.
	 * @return The number of rows.
	 */
	public int getRowCount();

	/**
	 * Sorts the rows. This is no longer used when using the TableRowSorter introduced 
	 * in JDK 1.6
	 * @param cmp The comparator to sort with.
	 */
	public void sort(Comparator<? super _RowType> cmp);

	/**
	 * Editable RowModels will implement this interface.
	 */
	public interface Editable<_RowType> extends RowModel<_RowType> {
		/**
		 * Sets the row at the specified index
		 * @param theRow The row to place in the RowModel
		 * @param where The index where the row should go
		 * @return The previously set row at that index;
		 */
		public _RowType setRow(_RowType theRow, int where);

		/**
		 * Swaps the rows at the specified indexes. I don't remember why I ever needed
		 * this method.
		 * @param ii The first row to swap
		 * @param jj The second row to swap
		 */
		public void swapRows(int ii, int jj);
	}

	/**
	 * RowModels that may be expanded by adding new rows will implement this interface.
	 */
	public interface Expandable<_RowType> extends Editable<_RowType> {
		/**
		 * Appends a row to the end of the RowModel.
		 * @param addedRow The row to add
		 */
		public void addRow(_RowType addedRow);

		/**
		 * Returns the List that backs this RowModel. <p>
		 * (I'm not sure if this method should be here.)
		 * @return The List of rows
		 * @see List
		 */
		public List<_RowType> getBackingList();
	}

	/**
	 * Standard implemetation of RowModel.Expandable, which stores the rows in the 
	 * specified List.
	 */
	public class ListRowModel<_RowType> implements RowModel.Expandable<_RowType> {
		private List<_RowType> mList;

		/**
		 * Wraps the specified List as the RowModel. The List must be mutable
		 * if the user will be making changes.
		 *
		 * @param rows The rows.
		 */
		public ListRowModel(Collection<_RowType> rows) {
			this(rows, false);
		}

		/**
		 * Create a ListRowModel of type _RowType from a collection. The
		 * duplicate parameter determines if it duplilcates the List parameter,
		 * or if it generates a separate ArrayList
		 *
		 * @param rows      The Collection of rows.
		 * @param duplicate If true, and rows is a List, sets the row list to
		 *                  {@code rows}. Otherwise generates a new ArrayList from the
		 *                  collection, with the order determined by the Collection's iterator().
		 */
		public ListRowModel(Collection<_RowType> rows, boolean duplicate) {
			if (rows instanceof List && !duplicate) {
				mList = (List<_RowType>) rows;
			} else {
				mList = new ArrayList<_RowType>(rows);
			}
		}

		/** {@inheritDoc} */
		public _RowType getRow(int rowIndex) {
			return mList.get(rowIndex);
		}

		/** {@inheritDoc} */
		public int getRowCount() {
			return mList.size();
		}

		/** {@inheritDoc} */
		public void sort(Comparator<? super _RowType> comparator) {
			Collections.sort(mList, comparator);
		}

		/** {@inheritDoc} */
		public _RowType setRow(_RowType rowType, int where) {
			_RowType oldOne = mList.get(where);
			mList.set(where, rowType);
			return oldOne;
		}

		/** {@inheritDoc} */
		public void swapRows(int ii, int jj) {
			_RowType first = mList.get(ii);
			mList.set(ii, mList.get(jj));
			mList.set(jj, first);
		}

		/** {@inheritDoc} */
		public void addRow(_RowType theRow) {
			mList.add(theRow);
		}

		/** {@inheritDoc} */
		public List<_RowType> getBackingList() { return mList; }
	}

	/**
	 * Standard implementation of RowModel.Editable, which stores the rows in the 
	 * specified array. 
	 */
	public class ArrayRowModel<_RowType> implements RowModel.Editable<_RowType> {
		private _RowType[] mRows;

		/**
		 * All five methods call this method. So if your row model needs to
		 * represent a volatile array, you can override this method and all
		 * three implementations in this class will still work.
		 *
		 * @return an array of the rows.
		 */
		protected _RowType[] getRows() {
			return mRows;
		}

		/**
		 * Create an Editable but not Expandable RowModel from an array of rows.
		 * @param array The array of rows.
		 */
		public ArrayRowModel(_RowType[] array) {
			mRows = array;
		}

		/** {@inheritDoc} */
		public _RowType getRow(int rowIndex) {
			return getRows()[rowIndex];
		}

		/** {@inheritDoc} */
		public int getRowCount() {
			return getRows().length;
		}

		/** {@inheritDoc} */
		public void sort(Comparator<? super _RowType> cmp) {
			Arrays.sort(getRows(), cmp);
		}

		/** {@inheritDoc} */
		public _RowType setRow(_RowType rowType, int where) {
			_RowType[] data = getRows();
			_RowType previous = data[where];
			getRows()[where] = rowType;
			return previous;
		}

		/** {@inheritDoc} */
		public void swapRows(int ii, int jj) {
			_RowType[] rows = getRows();
			_RowType first = rows[ii];
			rows[ii] = rows[jj];
			rows[jj] = first;
		}
	}
}
