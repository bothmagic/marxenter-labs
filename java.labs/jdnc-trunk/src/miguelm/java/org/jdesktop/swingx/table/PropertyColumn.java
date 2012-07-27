/*
 * $Id: PropertyColumn.java 2031 2007-12-11 11:02:48Z MiguelM $
 *
 * Copyright 2007 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.table;

import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableCellEditor;

/**
 * Property-based implementation of AbstractColumn. This class lets you specify property names
 * for your classes. 
 * @param <_RowType> The type of each row of the table.
 * @param <_ValueType> The type of the value of this column's data.
 */
public class PropertyColumn<_RowType, _ValueType> extends AbstractColumn<_RowType, _ValueType> {

	private final Method mGetterMethod;
	private final Method mSetterMethod;
	
	private boolean mUpdateRow = false;
	private boolean mUpdateTable = false;
	private boolean mUpdateSubsequentRows = false;

	/**
	 * Create a PropertyColumn from a property name, column name, row and column classes, and column width.
	 * The render and editor default to null, and updateRow, updateSubsequentRows, and updateTable all default
	 * to false.
	 * <p>This generates an editable column if a setter exists for this property, and a non-editable
	 * property if the setter doesn't exist. For more complex behavior, override {@code isEditable(_RowType)}
	 * @param pPropertyName The name of the property of instances of the specified row class
	 * @param pColumnName The name of the column
	 * @param pRowClass The class of the table row instances
	 * @param pValueClass The class of the values of this column
	 * @param pPrefWidth the preferred width of this column
	 * @throws AssertionError if pRowClass doesn't have a getter for this property name.
	 */
	public PropertyColumn(String pPropertyName, String pColumnName, Class pRowClass, Class pValueClass, int pPrefWidth) {
		super(pColumnName, getWrapper(pValueClass), pPrefWidth);
		try {
			mGetterMethod = generateGetter(pPropertyName, pRowClass, this);
		} catch (NoSuchMethodException e) {
			AssertionError err = new AssertionError(e.getMessage());
			err.initCause(e);
			throw err;
		}
		Method setter = null;
		try {
			setter = generateSetter(pPropertyName, pRowClass, this);
			setEditable(true);
		} catch (NoSuchMethodException nsme) {
			setEditable(false);
		}
		mSetterMethod = setter;
	}

	/**
	 * Create a PropertyColumn from a property name, column name, row and column classes, column width,
	 * Renderer, and Editor. updateRow, updateSubsequentRows, and updateTable all default to false.
	 * <p>This generates an editable column if a setter exists for this property, and a non-editable
	 * property if the setter doesn't exist. For more complex behavior, override {@code isEditable(_RowType)}
	 * 
	 * @param pPropertyName The name of the property of instances of the specified row class
	 * @param pColumnName The name of the column
	 * @param pRowClass The class of the table row instances
	 * @param pValueClass The class of the values of this column
	 * @param pPrefWidth the preferred width of this column
	 * @param pRenderer The renderer for this column's data This may be null, which forces the default renderer.
	 * @param pEditor The editor for this column's data. This may be null, which forces the default editor.
	 * @throws AssertionError if pRowClass doesn't have a getter for this property name.
	 */
	public PropertyColumn(String pPropertyName, String pColumnName, Class pRowClass, Class pValueClass, int pPrefWidth,
	                      TableCellRenderer pRenderer, TableCellEditor pEditor) {
		this(pPropertyName, pColumnName, pRowClass, pValueClass, pPrefWidth);
		setRenderer(pRenderer);
		setEditor(pEditor);
	}

	/**
	 * Create a PropertyColumn from a property name, column name, row and column classes, column width,
	 * Renderer, and Editor, and values for updateRow, updateSubsequentRows, and updateTable. 
	 * <p>This generates an editable column if a setter exists for this property, and a non-editable
	 * property if the setter doesn't exist. For more complex behavior, override {@code isEditable(_RowType)}
	 * 
	 * @param pPropertyName The name of the property of instances of the specified row class
	 * @param pColumnName The name of the column
	 * @param pRowClass The class of the table row instances
	 * @param pValueClass The class of the values of this column
	 * @param pPrefWidth the preferred width of this column
	 * @param pRenderer The renderer for this column's data This may be null, which 
	 * forces the default renderer.
	 * @param pEditor The editor for this column's data. This may be null, which forces 
	 * the default editor.
	 * @param updateRow True if changes to a cell in this column should force an update 
	 * of the whole row, false otherwise.
	 * @param updateSubsequentRows True if changes to a cell in this column should 
	 * force an update of the subsequent rows,
	 * @param updateTable True if changes to a cell in this column should force an 
	 * update of the entire table, false otherwise.
	 * @throws AssertionError if pRowClass doesn't have a getter for this property name.
	 */
	public PropertyColumn(String pPropertyName, String pColumnName, Class pRowClass, Class pValueClass, int pPrefWidth,
	                      TableCellRenderer pRenderer, TableCellEditor pEditor, boolean updateRow, boolean updateSubsequentRows,
	                      boolean updateTable) {
		this(pPropertyName, pColumnName, pRowClass, pValueClass, pPrefWidth, pRenderer, pEditor);
		mUpdateRow = updateRow;
		mUpdateSubsequentRows = updateSubsequentRows;
		mUpdateTable = updateTable;
	}

	/**
	 * Create a PropertyColumn from a property name, column name, row and column classes, column width,
	 * and values for updateRow, updateSubsequentRows, and updateTable. The renderer and editor default to null 
	 * <p>This generates an editable column if a setter exists for this property, and a non-editable
	 * property if the setter doesn't exist. For more complex behavior, override {@code isEditable(_RowType)}
	 * 
	 * @param pPropertyName The name of the property of instances of the specified row class
	 * @param pColumnName The name of the column
	 * @param pRowClass The class of the table row instances
	 * @param pValueClass The class of the values of this column
	 * @param pPrefWidth the preferred width of this column
	 * @param updateRow True if changes to a cell in this column should force an update 
	 * of the whole row, false otherwise.
	 * @param updateSubsequentRows True if changes to a cell in this column should 
	 * force an update of the subsequent rows,
	 * @param updateTable True if changes to a cell in this column should force an 
	 * update of the entire table, false otherwise.
	 * @throws AssertionError if pRowClass doesn't have a getter for this property name.
	 */
	public PropertyColumn(String pPropertyName, String pColumnName, Class pRowClass, Class pValueClass, int pPrefWidth,
	                      boolean updateRow, boolean updateSubsequentRows,
	                      boolean updateTable) {
		this(pPropertyName, pColumnName, pRowClass, pValueClass, pPrefWidth);
		mUpdateRow = updateRow;
		mUpdateSubsequentRows = updateSubsequentRows;
		mUpdateTable = updateTable;
	}

	public _ValueType getValue(_RowType pRow) {
		try {
			//noinspection unchecked
			return (_ValueType)mGetterMethod.invoke(pRow);
		} catch (IllegalAccessException e) {
			e.printStackTrace(); 
		} catch (InvocationTargetException e) {
			e.printStackTrace(); // shouldn't happen
		}
		//noinspection ReturnOfNull
		return null; // only if it threw an exception.
	}


	@Override
	public void setValue(_ValueType value, _RowType pRow) {
		try {
			mSetterMethod.invoke(pRow, value);
		} catch (IllegalAccessException e) {
			e.printStackTrace(); // Shouldn't happen
		} catch (InvocationTargetException e) {
			e.printStackTrace(); // shouldn't happen
		}
	}

	private static <R, V> Method generateGetter (String prop, Class pRowClass, PropertyColumn<R, V> column) throws NoSuchMethodException {
		Class aClass = column.getColumnClass();
		String prefix = aClass == Boolean.class? "is" : "get"; // NON-NLS
		String methodName = deriveMethodName(prefix, prop);
		Method method = pRowClass.getMethod(methodName);
		//noinspection StringConcatenation,MagicCharacter,HardcodedLineSeparator
		assert verifyReturn(aClass, method.getReturnType()) : "Incorrect Return Type for getter method " + pRowClass + '.' + methodName + 
						"\n\tExpected " + aClass + "\n\tFound " + method.getReturnType();
		return method;
	}
	
	private static <R, V> Method generateSetter(String prop, Class pRowClass, PropertyColumn<R, V> column) throws NoSuchMethodException {
		Class aClass = column.getColumnClass();
		String methodName = deriveMethodName("set", prop); // NON-NLS
		Method method = pRowClass.getMethod(methodName, aClass);
		//noinspection StringConcatenation,MagicCharacter
		assert method.getParameterTypes().length == 1 : "Incorrect number of parameters for setter method " + pRowClass + '.' + methodName;
		//noinspection StringConcatenation
		assert verifyReturn(aClass, method.getParameterTypes()[0]) : "Incorrect parameter type: " + aClass;
		return method;
	}

	private static boolean verifyReturn(Class columnClass, Class returnType) {
		if (columnClass.isAssignableFrom(returnType))
			return true;
		if (returnType.isPrimitive()) {
			return (columnClass.isAssignableFrom(getWrapper(returnType)));
		}
		return false;
	}

	/**
	 * This class exists to work around a bug in Java. If I call this:
	 * Integer.class.isAssignableFrom(Integer.Type), it incorrectly 
	 * returns false. (Integer.Type is the class object for the int primitive.)
	 * This bug applies to all primitives. Furthermore, there's no way to 
	 * get the wrapper class for a primitive. 
	 */
	private static Map<Class, Class> sWrappers = new HashMap<Class, Class>();
	static {
		sWrappers.put(Integer.TYPE, Integer.class);
		sWrappers.put(Short.TYPE, Short.class);
		sWrappers.put(Long.TYPE, Long.class);
		sWrappers.put(Character.TYPE, Character.class);
		sWrappers.put(Byte.TYPE, Byte.class);
		sWrappers.put(Float.TYPE, Float.class);
		sWrappers.put(Double.TYPE, Double.class);
		sWrappers.put(Boolean.TYPE, Boolean.class);
	}

	private static Class<?> getWrapper(Class theClass) {
		if (theClass.isPrimitive())
			return sWrappers.get(theClass);
		return theClass;
	}
	
	private static String deriveMethodName(String pPrefix, String prop) {
		StringBuilder bldr = new StringBuilder(pPrefix);
		bldr.append(Character.toUpperCase(prop.charAt(0)));
		bldr.append(prop.substring(1));
		return bldr.toString();
	}


	@Override public boolean getUpdateRow(_RowType pRow) { return mUpdateRow; }
	@Override public boolean getUpdateTable(_RowType pRow) { return mUpdateTable; }
	@Override public boolean getUpdateSubsequentRows(_RowType pRow) { return mUpdateSubsequentRows; }

	public void setUpdateRow(boolean pUpdateRow) { mUpdateRow = pUpdateRow; }
	public void setUpdateTable(boolean pUpdateTable) { mUpdateTable = pUpdateTable; }
	public void setUpdateSubsequentRows(boolean pUpdateSubsequentRows) { mUpdateSubsequentRows = pUpdateSubsequentRows; }
}
