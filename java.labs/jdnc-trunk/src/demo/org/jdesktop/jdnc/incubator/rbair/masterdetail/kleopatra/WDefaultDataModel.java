/*
 * $Id: WDefaultDataModel.java 137 2004-10-22 11:57:57Z kleopatra $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */

package org.jdesktop.jdnc.incubator.rbair.masterdetail.kleopatra;

import java.util.ArrayList;
import java.util.HashMap;

import org.jdesktop.jdnc.incubator.rbair.swing.data.*;
import org.jdesktop.swing.data.TabularDataModelAdapter;


/**
 * Default data model implementation designed to hold a single record of
 * field values.  This class provides storage of the model's values and
 * may be used when there is no underlying data model.
 *
 * @see TabularDataModelAdapter
 * @see JavaBeanDataModel
 *
 * @author Amy Fowler
 * @version 1.0
 */

public class WDefaultDataModel extends AbstractDataModel {
    private ArrayList fieldNames = new ArrayList();
    private HashMap values = new HashMap();
    private HashMap metaData = new HashMap();
    private HashMap fieldAdapters = new HashMap();

    public WDefaultDataModel() {
    }

    public WDefaultDataModel(MetaData fieldMetaData[]) {
        for(int i = 0; i < fieldMetaData.length; i++) {
            addField(fieldMetaData[i], null);
        }
    }

    public void addField(MetaData fieldMetaData,
                           Object defaultValue) {
        String name = fieldMetaData.getName();
        addField(fieldMetaData);
        values.put(name, defaultValue);
    }

    public void addField(MetaData fieldMetaData) {
        String name = fieldMetaData.getName();
        fieldNames.add(name); // track order fields were added
        metaData.put(name, fieldMetaData);
    }

    public void removeField(MetaData fieldMetaData) {
        String name = fieldMetaData.getName();
        fieldNames.remove(name);
        metaData.remove(name);
    }

    public String[] getFieldNames() {
        return (String[])fieldNames.toArray(new String[fieldNames.size()]);
    }

    public MetaData getMetaData(String fieldName) {
        return (MetaData)metaData.get(fieldName);
    }

    public int getFieldCount() {
        return metaData.size();
    }

    public Object getValue(String fieldName) {
        return values.get(fieldName);
    }

    protected void setValueImpl(String fieldName, Object value) {
        values.put(fieldName, value);
    }

    public void setDataSource(DataSource dataSource) {
      this.dataSource = dataSource;
    }
  
    public int getRecordCount() {
        return 1;
    }

    public int getRecordIndex() {
        return 0;
    }

    public void setRecordIndex(int index) {
        if (index != 0) {
            throw new IndexOutOfBoundsException("DefaultDataModel contains only 1 record");
        }
    }

    protected void setValueImpl(String fieldName, Object value, int index) {
      setValueImpl(fieldName, value);
      
    }

    public Object getValue(String fieldName, int index) {
      return getValue(fieldName);
    }

    public boolean firstRecord() {
      return true;
    }

    public boolean prevRecord() {
      return false;
    }

    public boolean nextRecord() {
      // TODO Auto-generated method stub
      return false;
    }

    public boolean lastRecord() {
      // TODO Auto-generated method stub
      return true;
    }

    public boolean hasNext() {
      // TODO Auto-generated method stub
      return false;
    }

    public boolean hasPrev() {
      // TODO Auto-generated method stub
      return false;
    }


}
