package org.jdesktop.jdnc.incubator.rbair.caveatemptor;


import org.jdesktop.jdnc.incubator.rbair.swing.data.JavaBeanDataModel;

import javax.swing.table.AbstractTableModel;
import java.util.List;

/**
 * @author Gilles Philippart
 */
public class JavaBeanTableModelAdapter extends AbstractTableModel {

    public JavaBeanDataModel beanDataModel;
    private String[] propertyNames;

    public JavaBeanTableModelAdapter(JavaBeanDataModel beanDataModel, String[] propertyNames) {
        this.beanDataModel = beanDataModel;
        this.propertyNames = propertyNames;
    }

    public int getColumnCount() {
        int columnCount = beanDataModel.getFieldNames().length;
        return columnCount;
    }

    public int getRowCount() {
    	return beanDataModel.getRecordCount();
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        return beanDataModel.getValue(propertyNames[columnIndex], rowIndex);
    }

}
