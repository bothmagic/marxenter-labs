package org.jdesktop.incubator.table;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/*
 * Created by IntelliJ IDEA.
 * User: Richard Osbaldeston
 * Date: 23-Feb-2007
 * Time: 16:49:32
 */

public abstract class ReflectiveTableModel<T>
        extends AbstractGenericTableModel<T> {

    private PropertyDescriptor[] propertyDescriptors;

    public ReflectiveTableModel(Class clazz)
            throws IntrospectionException {
        this(Introspector.getBeanInfo(clazz));
    }

    public ReflectiveTableModel(BeanInfo beanInfo) {
        // filter out any unreadable properties
        PropertyDescriptor[] descriptors = beanInfo.getPropertyDescriptors();
        List<PropertyDescriptor> results =
                new ArrayList<PropertyDescriptor>(descriptors.length);
        for (PropertyDescriptor propertyDescriptor : descriptors) {
            Method readMethod = propertyDescriptor.getReadMethod();
            if (readMethod != null) {
                results.add(propertyDescriptor);
            }
        }
        this.propertyDescriptors = results.toArray(
                new PropertyDescriptor[results.size()]);
    }

    public int getColumnCount() {
        return propertyDescriptors.length;
    }

    public String getColumnName(int column) {
        return propertyDescriptors[column].getDisplayName();
    }

    public Class<?> getColumnClass(int column) {
        return propertyDescriptors[column].getPropertyType();
    }

    public Object getValueAt(int row, int column) {
        T item = getValue(row);
        PropertyDescriptor propertyDescriptor = propertyDescriptors[column];
        Method readMethod = getReadMethod(propertyDescriptor);
        try {
            return readMethod.invoke(item);
        } catch (IllegalAccessException e) {
            throw new Error(e);
        } catch (InvocationTargetException e) {
            throw new Error(e);
        }
    }

    @Override
    public void setValueAt(Object value, int row, int column) {
        PropertyDescriptor propertyDescriptor = propertyDescriptors[column];
        if (isCellEditable(row, column)) {
            Method writeMethod = propertyDescriptor.getWriteMethod();
            try {
                T item = getValue(row);
                writeMethod.invoke(item, value);
                fireTableCellUpdated(row, column);
            } catch (Exception e) {
                throw new Error(e);
            }
        }
    }

    Method getReadMethod(PropertyDescriptor propertyDescriptor) {
        return propertyDescriptor.getReadMethod();
    }

    public boolean isCellEditable(int row, int column) {
        PropertyDescriptor propertyDescriptor = propertyDescriptors[column];
        return getReadMethod(propertyDescriptor) == null
                || propertyDescriptor.getWriteMethod() == null
                || isReadOnly(propertyDescriptor);
    }

    boolean isReadOnly(PropertyDescriptor propertyDescriptor) {
        Object value = propertyDescriptor.getValue("readOnly");     //NON-NLS
        return value != null && Boolean.valueOf((Boolean) value);
    }
}