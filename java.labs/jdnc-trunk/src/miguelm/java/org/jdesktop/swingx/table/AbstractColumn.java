/*
 * $Id: AbstractColumn.java 2031 2007-12-11 11:02:48Z MiguelM $
 *
 * Copyright 2007 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.table;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import java.util.*;
import java.text.Collator;

import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableStringConverter;

/**
 * Abstract Column <br>
 * _RowType is the class for each row in the table.
 * <p> This class makes no assumptions about the form of the data in _RowType
 * Typically, this class is subclassed to implement a getValue() method, and
 * potentially a setValue() method, but the rest of the properties are set
 * in the constructor. This makes subclasses very easy to write. For example,
 * a column to display the price property of an instance of class Product would
 * look like this:
 * <pre>
 *  private static class PriceColumn extends AbstractColumn{@literal <Product, Double>} {
 *      PriceColumn() {
 *          super("Price",             // Column Name
 *                Double.class,        // Column class
 *                65,                  // Column Width
 *                mMoneyRenderer       // TableCellRenderer
 *          );
 *      }
 *      public Double     getValue(Product row) { return row.getPrice(); }
 *      public void       setValue(Double pr, Product row) { row.setPrice(pr); }
 *      public boolean    isEditable(Product pNum) { return true; }
 *  }
 * </pre>
 * The constructors here can't possibly give us every needed combination of
 * parameters, but developers can easily write a subclass with any other
 * constructors they need. In the example above, a constructor that includes a
 * value for the editable property would be useful, particularly if several
 * columns needed it.
 * <p/>
 * <em>Caution:</em> When subclassing, be sure to say {@code extends
 * AbstractColumn{@literal <R, V>}} instead of {@code extends AbstractColumn}, or you
 * will get confusing error messages with any method you override.
 *
 * @param <_RowType> The type of each row of the JTable
 * @param <_ValueType> The type of value for the cells in this JTable.
 * @author Miguel Mu\u00f1oz
 */
public abstract class AbstractColumn<_RowType, _ValueType>
				implements PropertyChangeListener {
	private int mPrefWidth;
	private int mWidth;
	private int mMaxWidth = -1;
	private int mMinWidth = -1;
	private String mName;
	private TableCellRenderer mRenderer;
	private TableCellEditor mEditor;
	private int mSavedWidth;
	private boolean mIsEditable = false;
	private final Class<?> mClass;
	private final boolean mIsComparable;
	private final boolean mIsString;

	/**
	 * Instantiate a Read-Only column with the specified parameters.
	 *
	 * @param pName      The name of the column in the column header
	 * @param pClass     The class of the column's data
	 * @param pPrefWidth The preferred width of the column
	 */
	protected AbstractColumn(String pName, Class pClass, int pPrefWidth) {
		this(pName, pClass, pPrefWidth, null, false);
	}

	/**
	 * Instantiate a column with the specified parameters.
	 *
	 * @param pName      The name of the column in the column header
	 * @param pClass     The class of the column's data
	 * @param pPrefWidth The preferred width of the column
	 * @param pEditable  The isEditable property.
	 */
	protected AbstractColumn(String pName, Class pClass, int pPrefWidth, boolean pEditable) {
		this(pName, pClass, pPrefWidth, null, pEditable);
	}

	/**
	 * Instantiate a Read-Only column with the specified parameters.
	 *
	 * @param pName      The name of the column in the column header
	 * @param pClass     The class of the column's data
	 * @param pPrefWidth The preferred width of the column
	 * @param pRend      The Default TableCellRenderer. (May be null)
	 */
	protected AbstractColumn(String pName, Class pClass, int pPrefWidth, TableCellRenderer pRend) {
		this(pName, pClass, pPrefWidth, pRend, false);
	}

	/**
	 * Instantiate a column with the specified parameters.
	 *
	 * @param pName      The name of the column in the column header
	 * @param pClass     The class of the column's data
	 * @param pPrefWidth The preferred width of the column
	 * @param pRend      The Default TableCellRenderer. (May be null)
	 * @param pEditable  The isEditable property
	 */
	protected AbstractColumn(String pName, Class pClass, int pPrefWidth, TableCellRenderer pRend, boolean pEditable) {
		mName = pName;
		mClass = pClass;
		mPrefWidth = pPrefWidth;
		mWidth = pPrefWidth;
		mRenderer = pRend;
		mIsEditable = pEditable;
		// Perform these two tests once upon initialization for speed.
		mIsString = pClass == String.class;
		mIsComparable = Comparable.class.isAssignableFrom(pClass);
	}

	/**
	 * Instantiate an editable column with the specified parameters.
	 *
	 * @param pName      The name of the column in the column header
	 * @param pClass     The class of the column's data
	 * @param pPrefWidth The preferred width of the column
	 * @param pRend      The Default TableCellRenderer. (May be null)
	 * @param pEd        The Default TableCellEditor. (May be null)
	 * Since we supply a TableCellEditor, this constructor assumes the 
	 * cells will be editable and sets the isEditable property to true.
	 */
	protected AbstractColumn(String pName,
	                         Class pClass,
	                         int pPrefWidth,
	                         TableCellRenderer pRend,
	                         TableCellEditor pEd) {
		this(pName, pClass, pPrefWidth, pRend);
		mEditor = pEd;
		mIsEditable = true;
	}

	public abstract _ValueType getValue(_RowType pRow);

	public void setValue(_ValueType value, _RowType pRow) {
		//noinspection StringConcatenation,MagicCharacter
		assert false : "SetValue called for non-editable column: " + getClass()
						+ " <vtype=" + value.getClass() + ", rtype=" + pRow.getClass() + '>';
	}

	/**
	 * This is used for sorting, when the KeyedTableRowSorter is used. By default
	 * this method doesn't use keyed sorting. Instead, it follows the following rules:<br>
	 * 1) If the _ValueType is a String, it returns an instance of StringComparable for the
	 * String returned by the getValue() method. <br>
	 * 2) Otherwise, if the _ValueType implements Comparable, it calles getValue() and
	 * returns that if result. <br>
	 * 3) If the _ValueType does not implement Comparable, it calls convertToString() to
	 * convert the object to a String, then returns an instance of StringComparable for
	 * that String.
	 * <p> To use keyed sorting, which is the purpose of this method, you should override
	 * this to return a sorting key derived from the value returned by getValue().
	 * Typically, you would return a CollationKey created from a String, using Collator.
	 * Although the default behavior of the standard TableRowSorter uses these same
	 * CollationKeys, you can speed up the sorting by generating the keys ahead of time,
	 * and returning them from this method. For example:
	 * <pre>
	 * public class RowData {
	 *   String name;
	 *   CollationKey nameKey;
	 *   ...
	 *   public void setName(String name) {
	 *     this.name = name;
	 *     nameKey = Collator.getInstance().getCollationKey(name);
	 *   }
	 *   ...
	 * }
	 * <p/>
	 * public class NameColumn extends AbstractColumn{@literal <RowData, String>} {
	 *   ...
	 *   public Comparable{@literal <?>} getComparableValue(RowData theRow) {
	 *     return theRow.nameKey;
	 *   }
	 * }
	 * </pre>
	 *
	 * @param pRow The row
	 * @return The comparable object to be used in sorting the rows.
	 * @see java.text.Collator
	 * @see java.text.CollationKey
	 * @see org.jdesktop.swingx.table.TableRowKeyedSorter
	 * @see #convertToString(Object)
	 * @see org.jdesktop.swingx.table.AbstractColumn.StringComparable
	 */
	public Comparable<?> getComparableValue(_RowType pRow) {
		_ValueType type = getValue(pRow);
		if (mIsString)
			return getStringComparable((String) getValue(pRow));
//			return Collator.getInstance().getCollationKey((String) type);
		if (mIsComparable)
			return (Comparable<?>) type;
		else
//			return Collator.getInstance().getCollationKey(convertToString(getValue(pRow)));
			return getStringComparable((String) getValue(pRow));
	}

	/**
	 * Used by the default implementation of getComparableValue to perform comparisons
	 * that are sensitive to human-language issues.
	 *
	 * @param ss The String
	 * @return A StringComparable for that String.
	 */
	protected Comparable<StringComparable> getStringComparable(String ss) {
		return new StringComparable(ss);
	}

	public Class getColumnClass() { return mClass; }

	/**
	 * Columns are not editable by default. You may make the columns editable by setting the
	 * isEditable property to true, or by overriding this method.
	 *
	 * @param pRow The row
	 * @return true if the row is editable, false otherwise.
	 */
	public boolean isEditable(_RowType pRow) {
		return mIsEditable;
	}
	
	protected void setEditable(boolean isEditable) { mIsEditable = isEditable; }

	public String getColumnName() { return mName; }

	public void setName(String pName) { mName = pName; }

	public int getPreferredWidth() { return mPrefWidth; }

	public void setPreferredWidth(int pWidth) { mPrefWidth = pWidth; }

	public int getWidth() { return mWidth; }

	public void setWidth(int pWidth) { mWidth = pWidth; }

	/**
	 * Gets the maximum allowed width for the column.
	 *
	 * @return The maximum width.
	 * @see #setMaxWidth(int)
	 */
	public int getMaxWidth() { return mMaxWidth; }

	/**
	 * Sets the maximum allowed width for the column. A negative value means
	 * no maximum is set. This defaults to -1.
	 *
	 * @param pMaxWidth The maximum allowed value, or a negative value for no maximum.
	 * @see #getMaxWidth()
	 */
	public void setMaxWidth(int pMaxWidth) { mMaxWidth = pMaxWidth; }

	/**
	 * Gets the minimum allowed width for the column.
	 *
	 * @return The minimum width.
	 * @see #setMinWidth(int)
	 */
	public int getMinWidth() { return mMinWidth; }

	/**
	 * Sets the minimum allowed width for the column. A negative value means
	 * no minimum is set. This defaults to -1.
	 *
	 * @param pMinWidth The minimum allowed value, or a negative value for no minimum.
	 * @see #getMinWidth()
	 */
	public void setMinWidth(int pMinWidth) { mMinWidth = pMinWidth; }

	public TableCellRenderer getRenderer() { return mRenderer; }

	public void setRenderer(TableCellRenderer pRndr) { mRenderer = pRndr; }

	public TableCellEditor getEditor() { return mEditor; }

	public void setEditor(TableCellEditor pEdtr) { mEditor = pEdtr; }

	public int getSavedWidth() { return mSavedWidth; }

	public void setSavedWidth(int pSavedWidth) { mSavedWidth = pSavedWidth; }

	/**
	 * Specifies if changes to the current cell will cause changes elsewhere in
	 * the same row. By default this returns false. If changes to this cell can
	 * force changes to other cells <i>in the same row</i> then this method
	 * should be overridden to return true.
	 *
	 * @param pRow The row that is changing.
	 * @return true if changes to this cell force changes to other cells in the
	 *         same row, false otherwise.
	 * @see SeparatedTableModel#setValueAt(Object,int,int)
	 */
	public boolean getUpdateRow(_RowType pRow) { return false; }

	/**
	 * Specifies if changes to the current cell will cause changes elsewhere in
	 * the table, in other rows. By default this returns false. If changes to
	 * this cell can force changes to cells <i>in other rows</i> then this
	 * method should be overridden return true.
	 *
	 * @param pRow The row that is changing.
	 * @return true if changes to this cell force changes to other rows in the
	 *         table, false otherwise.
	 * @see SeparatedTableModel#setValueAt(Object,int,int)
	 */
	public boolean getUpdateTable(_RowType pRow) { return false; }

	/**
	 * Specifies if changes to the current cell will cause changes elsewhere in
	 * the table, in subsequent rows, but not in preceeding rows. By default this
	 * returns false. If changes to this cell can force changes to cells <i>in
	 * other rows below this one</i> then this method should be overridden to
	 * return true.
	 *
	 * @param pRow The row that is changing.
	 * @return true if changes to this cell force changes to rows below this cell,
	 *         false otherwise.
	 */
	public boolean getUpdateSubsequentRows(_RowType pRow) { return false; }

	/**
	 * This method gets called when a bound property is changed.
	 * <p/>
	 * <em>Note:</em> This is only useful when the model is used by only one
	 * JTable. If more than one JTable use this model, this would prevent the
	 * user from setting a different column width in each table.
	 * Note2: This method probably doesn't work anyway, because the user
	 * probably doesn't change the <i>preferred</i> width when adjusting
	 * column separators.
	 * todo: Figure out if this works.
	 *
	 * @param evt A PropertyChangeEvent object describing the event source and
	 *            the property that has changed.
	 */
	public void propertyChange(PropertyChangeEvent evt) {
		//noinspection HardCodedStringLiteral,CallToStringEquals
		if ("preferredWidth".equals(evt.getPropertyName())) {
			setPreferredWidth((Integer) evt.getNewValue());
		}
	}

	/**
	 * When using the TableRowKeyedSorter, converts the value to a 
	 * String for sorting purposes. The default behavior just calls 
	 * {@code toString()}, but you may override this for custom 
	 * behavior. This method replaces the functionality of the 
	 * TableStringConverter.
	 *
	 * @param value The value of the table cell.
	 * @return The value converted to a String.
	 * @see org.jdesktop.swingx.table.TableRowKeyedSorter
	 */
	public String convertToString(_ValueType value) { return value.toString(); }

	public Comparator<?> getComparator() { return genericComparator; }

	private Comparator<?> genericComparator = new GenericComparator();

	private static class GenericComparator<M extends Comparable<M>> implements Comparator<M> {
		private GenericComparator() { }

		public int compare(M o1, M o2) { return o1.compareTo(o2); }
	}

	/**
	 * This small static class performs a human-language-sensitive comparison
	 * between two strings. This compares them according to the local rules of the
	 * default Locale. It implements this by calling the default Collator's compare
	 * method. In English, this means the results will be case-insensitive. This class
	 * is used by default for String columns. To get more sophisticated behavior,
	 * override the getComparableValue method.
	 *
	 * @see AbstractColumn#getComparableValue(Object)
	 */
	public static class StringComparable implements Comparable<StringComparable> {
		private String mString;

		StringComparable(String ss) { mString = ss; }

		public int compareTo(StringComparable o) {
			return Collator.getInstance().compare(mString, o.mString);
		}
	}
}
