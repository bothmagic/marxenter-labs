package com.peralex.utilities.ui.table;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.swing.table.AbstractTableModel;



/**
 * Provides a table model that maps columns onto fields of JavaBeans.
 * 
 * Note that you can be lazy and define your JavaBeans without accessor methods - useful for GUI coding when you
 * have lots of tables and the JavaBean class is only being used to model a row of table data.
 * e.g.
 * <code>
 *   public class MyTableRow {
 *      public String name;
 *      public String surname;
 *   }
 *   
 *   public class MyTableModel extends BeanTableModel<MyTableRow> {
 *   
 *     public MyTableModel(ResourceBundle textRes) {
 *       super(MyTableRow.class, new String [] { "name", "surname" });
 *       resetColumnNames(textRes);
 *     }
 *       
 *     public void resetColumnNames(ResourceBundle textRes) {
 *       setColumnName("name", textRes.getString("name"));
 *       setColumnName("surname", textRes.getString("surname"));
 *       fireTableStructureChanged();
 *     }
 *     
 *   }
 * </code>
 * 
 * @author Noel Grandin
 */
public class BeanTableModel<T> extends AbstractTableModel
{

	private interface IBeanReaderWriter {
		Object read(Object bean);
		void write(Object bean, Object value);
	}
	
	private final List<T> list = new ArrayList<T>();

	private final Map<String, Integer> fieldToColumnIndexMap = new HashMap<String, Integer>();

	private final String[] beanFieldNames;
	
	private final String[] columnNames;
	
	private final Class<?>[] columnClasses;

	private final boolean[] isColumnEditable;
	
	private final IBeanReaderWriter[] beanFieldReaderWriters;
	
	/**
	 * 
	 * @param beanClass this is here so we can check the field names and fail early if there is a mistake.
	 * @param columnBeanFields
	 */
	public BeanTableModel(Class<T> beanClass, String... columnBeanFields)
	{
		try
		{
			this.beanFieldNames = columnBeanFields;
			for (int i=0; i<columnBeanFields.length; i++) {
				fieldToColumnIndexMap.put(columnBeanFields[i], Integer.valueOf(i));				
			}
			
			this.columnNames = new String[this.beanFieldNames.length];
			
			this.isColumnEditable = new boolean[this.beanFieldNames.length];
			Arrays.fill(isColumnEditable, false);
			
			
			this.beanFieldReaderWriters = new IBeanReaderWriter[this.beanFieldNames.length];
			for (int i = 0; i < beanFieldReaderWriters.length; i++) {
				this.beanFieldReaderWriters[i] = createBeanReadWrite(beanClass, beanFieldNames[i]);
			}
			
			// determine the column classes
			this.columnClasses = new Class[this.beanFieldNames.length];
			for (int i = 0; i < columnClasses.length; i++)
			{
				Class<?> columnClass = beanClass.getField(this.beanFieldNames[i]).getType();
				// as far as the rest of the code is concerned, we always wrap stuff in the primitive wrapper classes
				if (columnClass==Float.TYPE) {
					columnClass = Float.class;
				} else if (columnClass==Double.TYPE) {
					columnClass = Double.class;
				} else if (columnClass==Byte.TYPE) {
					columnClass = Byte.class;
				} else if (columnClass==Short.TYPE) {
					columnClass = Short.class;
				} else if (columnClass==Integer.TYPE) {
					columnClass = Integer.class;
				} else if (columnClass==Long.TYPE) {
					columnClass = Long.class;
				} else if (columnClass==Boolean.TYPE) {
					columnClass = Boolean.class;
				} else if (columnClass==Character.TYPE) {
					columnClass = Character.class;
				}
				this.columnClasses[i] = columnClass;
			}
		}
		catch (SecurityException ex)
		{
			throw new RuntimeException(ex);
		}
		catch (NoSuchFieldException ex)
		{
			throw new RuntimeException(ex);
		}

	}

	protected void setColumnName(String beanFieldName, String columnName)
	{
		final Integer columnIndex = fieldToColumnIndexMap.get(beanFieldName);
		if (columnIndex==null) throw new IllegalArgumentException("unknown bean field " + beanFieldName);
		this.columnNames[columnIndex] = columnName;
		
		fireTableStructureChanged();
	}
	
	/**
	 * returns a non-modifiable List view of the bean data
	 */
	public List<T> values() {
		return Collections.unmodifiableList(this.list);
	}

	public void addRow(T newRow) {
		list.add(newRow);
		fireTableRowsInserted(list.size()-1, list.size()-1);
	}
	
	public void insertRow(int rowIndex, T newRow) {
		list.add(rowIndex, newRow);
		fireTableRowsInserted(rowIndex, rowIndex);
	}
	
	public void insertRows(int rowIndex, Collection<T> newRows) {
		list.addAll(rowIndex, newRows);
		fireTableRowsInserted(rowIndex, rowIndex + newRows.size() - 1);
	}
	
	public T removeRow(int rowIndex) {
		T row = list.remove(rowIndex);
		fireTableRowsDeleted(rowIndex, rowIndex);
		return row;
	}
	
	public void removeRows(int... rowIndices)
	{
		// remove in reverse order, otherwise the indexes will be wrong after the first one.
		for (int i = rowIndices.length - 1; i >= 0; --i)
		{
			removeRow(rowIndices[i]);
		}
	}
	
	public void updateRow(int rowIndex, T updatedRow)
	{
		list.set(rowIndex, updatedRow);
		fireTableRowsUpdated(rowIndex, rowIndex);
	}
	
	public T getRow(int rowIndex)
	{
		T bean = list.get(rowIndex);
		return bean;
	}

	public int getRowCount()
	{
		return list.size();
	}

	public int getColumnCount()
	{
		return beanFieldNames.length;
	}

  public void resetColumnNames(@SuppressWarnings("unused")
	ResourceBundle textRes) 
  {
  }

  @Override
	public String getColumnName(int column)
	{
		return columnNames[column];
	}

	@Override
	public Class<?> getColumnClass(int columnIndex)
	{
		return columnClasses[columnIndex];
	}

	public Object getValueAt(int rowIndex, int columnIndex)
	{
		if (columnIndex == -1)
		{
			// magic column, to give me the real data
			return list.get(rowIndex);
		}
		return beanFieldReaderWriters[columnIndex].read(list.get(rowIndex));
	}

	/**
	 * clears existing data and copies from parameter
	 */
	public void setData(List<T> data)
	{
		this.list.clear();
		this.list.addAll(data);
		this.fireTableDataChanged();
	}

	/**
	 * clear existing rows
	 */
	public void removeAll()
	{
		this.list.clear();
		this.fireTableDataChanged();
	}
	
	/**
	 * sort the stored data using a comparator
	 */
	public void sort(Comparator<T> comparator)
	{
		Collections.sort(list, comparator);
		fireTableDataChanged();
	}
	
	public void setColumnEditable(int columnIndex, boolean editable)
	{
		this.isColumnEditable[columnIndex] = editable;
		
		fireTableStructureChanged();
	}
	
	public void setColumnEditable(String beanFieldName, boolean editable)
	{
		final Integer columnIndex = fieldToColumnIndexMap.get(beanFieldName);
		if (columnIndex==null) throw new IllegalArgumentException("unknown bean field " + beanFieldName);
		this.isColumnEditable[columnIndex] = editable;
		
		fireTableStructureChanged();
	}
	
	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex)
	{
		return this.isColumnEditable[columnIndex];
	}
	
	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex)
	{
		beanFieldReaderWriters[columnIndex].write(list.get(rowIndex), aValue);
		fireTableCellUpdated(rowIndex, columnIndex);
	}
	
	/**
	 * @return the index of the row, or -1 if the row is not in the model
	 */
	public int indexOf(T row)
	{
		return this.list.indexOf(row);
	}
	
	private static <T> IBeanReaderWriter createBeanReadWrite(Class<T> beanClass, String fieldName)
	{
		try
		{
      final PropertyDescriptor descriptors[] = Introspector.getBeanInfo(beanClass).getPropertyDescriptors();
      for (int i = 0; i < descriptors.length; i++) {
        if (fieldName.equals(descriptors[i].getName())) {
            final Method readMethod = descriptors[i].getReadMethod();
            final Method writeMethod = descriptors[i].getWriteMethod();
            if (readMethod==null) {
      				throw new RuntimeException(new IllegalArgumentException("field " + fieldName + " looks like a normal javabean method to the Introspector, has no method for reading the field."));
            }
            if (writeMethod==null) {
      				throw new RuntimeException(new IllegalArgumentException("field " + fieldName + " looks like a normal javabean method to the Introspector, has no method for writing the field."));
            }
            return new IBeanReaderWriter() {
            	public Object read(Object bean)
            	{
            		try
								{
									return readMethod.invoke(bean, new Object [0]);
								}
								catch (IllegalArgumentException ex)
								{
									throw new RuntimeException(ex);
								}
								catch (IllegalAccessException ex)
								{
									throw new RuntimeException(ex);
								}
								catch (InvocationTargetException ex)
								{
									throw new RuntimeException(ex);
								}
            	}
            	public void write(Object bean, Object value)
            	{
            		try
								{
									writeMethod.invoke(bean, new Object [] { value });
								}
								catch (IllegalArgumentException ex)
								{
									throw new RuntimeException(ex);
								}
								catch (IllegalAccessException ex)
								{
									throw new RuntimeException(ex);
								}
								catch (InvocationTargetException ex)
								{
									throw new RuntimeException(ex);
								}
            	}
            };
      	}
			}
		}
		catch (IntrospectionException ex)
		{
			// ignore
		}
		
		// try accessing it as a "naked" bean i.e. one with public fields and no accessor methods.
		try
		{
			final Field field = beanClass.getField(fieldName);
			if (field==null) {
				throw new RuntimeException(new NoSuchMethodException("cannot find field " + fieldName));
			}
      return new IBeanReaderWriter() {
      	public Object read(Object bean)
      	{
    			try
					{
						return field.get(bean);
					}
					catch (IllegalArgumentException ex)
					{
						throw new RuntimeException(ex);
					}
					catch (IllegalAccessException ex)
					{
						throw new RuntimeException(ex);
					}
      	}
      	public void write(Object bean, Object value)
      	{
      		try
					{
						field.set(bean, value);
					}
					catch (IllegalArgumentException ex)
					{
						throw new RuntimeException(ex);
					}
					catch (IllegalAccessException ex)
					{
						throw new RuntimeException(ex);
					}
      	}
      };
		}
		catch (SecurityException ex)
		{
			throw new RuntimeException(ex);
		}
		catch (NoSuchFieldException ex)
		{
			throw new RuntimeException(ex);
		}
		catch (IllegalArgumentException ex)
		{
			throw new RuntimeException(ex);
		}
	}
}
