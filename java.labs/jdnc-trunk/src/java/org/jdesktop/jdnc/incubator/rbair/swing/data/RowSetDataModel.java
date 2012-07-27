/*
 * $Id: RowSetDataModel.java 128 2004-10-18 20:06:27Z rbair $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */

package org.jdesktop.jdnc.incubator.rbair.swing.data;

import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.sql.rowset.CachedRowSet;

/**
 * This DataModel implementation is used with RowSets.
 * The RowSetMetaData forms the basis for the MetaData used by this DataModel.
 *
 * @author Richard Bair
 */
public class RowSetDataModel extends AbstractDataModel {
    /**
     * The RowSet that this DataModel wraps. This may be null.
     */
    private CachedRowSet rs;
    private String selectSql = "";
    private List selectParams = new ArrayList();
    private String tableName = "";
    
    /**
     * This is the DataSource used to interact with the database. Even though
     * much of the information is stored in the row set, the DataSource handles
     * the actual connection.
     */
    private RowSetDataSource dataSource;

    /**
     * The current row index. This is 0 based (0 being the first row),
     * whereas a RowSet is 1 based.
     * <p/>
     * TODO Gilles Philippart : never used ?
     */
    private int rowIndex = -1;

    /**
     * Create a new RowSetDataModel
     */
    public RowSetDataModel(RowSetDataSource dataSource) {
    	setDataSource(dataSource);
    }

    /**
     * Create a new RowSetDataModel based on the given RowSet. The RowSet may be null.
     */
    public RowSetDataModel(RowSetDataSource dataSource, CachedRowSet data) {
        setRowSet(data);
    	setDataSource(dataSource);
    }

    /**
     * Set the RowSet for this DataModel to be the given RowSet. If null, then this
     * DataModel will have 0 rows, and no meta data. Event listeners will be notified
     * of the changes.
     * <p>
     * If there is a current row set and this new row set replaces it, then
     * keep track of the keyValues for the currently selected row and attempt
     * to find an equivilent row in the new list. This is so that when a
     * data model is refreshed, all of the lists tied to the data model are
     * not reset to index 0
     * @param data
     */
    public void setRowSet(CachedRowSet data) {
    	Object[] keyValues = null;
    	if (rs != null && getRecordCount() > 0 && getRecordIndex() >= 0) {
    		String[] keyFields = getKeyFields();
    		keyValues = new Object[keyFields.length];
    		for (int i=0; i<keyValues.length; i++) {
    			keyValues[i] = getValue(keyFields[i]);
    		}
    	}
    	
        rs = data;
        try {
        	if (keyValues == null) {
        		rs.first();
        	} else {
        		//try to restore to the proper row
        		String[] keyFields = getKeyFields();
        		Object[] kv = new Object[keyValues.length];
        		boolean found = false;
        		for (int i=0; i<getRecordCount(); i++) {
        			for (int j=0; j<kv.length; j++) {
        				kv[j] = getValue(keyFields[j], i);
        			}
        			if (Arrays.equals(keyValues, kv)) {
        				setRecordIndex(i);
        				found = true;
        				break;
        			}
        		}
        		if (!found) {
        			rs.first();
        		}
        	}
//            setMetaData(data == null ? null : data.getMetaData());
        } catch (Exception e) {
            //TODO This is a horrible way to handle errors -- what should happen?
            e.printStackTrace();
        }
        super.fireModelDataChanged();
    }

    /**
     * Sets the meta data for this DataModel based on the given ResultSetMetaData.
     * If the meta data is null, then no meta data will exist for this DataModel
     * (effectively rendering it useless).
     *
     * @param rsmd
     */
    public void setMetaData(ResultSetMetaData rsmd) {
        //remove all of the old meta data
        String[] fieldNames = super.getFieldNames();
        if (fieldNames != null) {
            for (int i = 0; i < fieldNames.length; i++) {
                MetaData md = super.getMetaData(fieldNames[i]);
                super.removeField(md);
            }
        }
        //add the new meta data
        if (rsmd != null) {
            try {
                for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                    String name = rsmd.getColumnName(i);
                    Class klass = Class.forName(rsmd.getColumnClassName(i));
                    String label = rsmd.getColumnLabel(i);
                    int labelWidth = rsmd.getColumnDisplaySize(i);
                    MetaData metaData = new MetaData(name, klass, label);
                    metaData.setDisplayWidth(labelWidth);
                    super.addFieldNoEvent(metaData);
                }
                super.fireModelMetaDataChanged();
            } catch (Exception e) {
                //TODO do some real error handling
                e.printStackTrace();
            }
        }
    }

    /**
     * Return the record count. This is computed by going to the end of the RowSet,
     * getting the row number, and then returning to the previous location. If the
     * RowSet implementation does not support random-access movement, then this
     * method will not work!
     *
     * @inheritDoc
     */
    public int getRecordCount() {
        int count = 0;
        try {
            if (rs != null) {
                int currentIndex = rs.getRow();
                boolean isBeforeFirst = rs.isBeforeFirst();
                boolean isAfterLast = rs.isAfterLast();
                rs.last();
                count = rs.getRow(); //since getRow is 1 based, this is accurate
                if (currentIndex > 0) {
                    rs.absolute(currentIndex);
                } else if (isBeforeFirst) {
                    rs.beforeFirst();
                } else if (isAfterLast) {
                    rs.afterLast();
                } else {
                    //wow, this is one messed up row set. Don't do anything to restore
                    //position
                }
            }
        } catch (Exception e) {
            //TODO Need to do some real error handling
            e.printStackTrace();
        }
        return rs == null ? 0 : count;
    }

    /**
     * @inheritDoc
     */
    public int getRecordIndex() {
        try {
            //since rs.getRow is 1 based, subtract 1 from it to get the 0 based index
            return rs == null ? -1 : rs.getRow() - 1;
        } catch (Exception e) {
            //TODO need to do real error handling
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * @inheritDoc
     */
    public Object getValue(String fieldName) {
        try {
            return rs == null ? null : rs.getObject(fieldName);
        } catch (Exception e) {
            //TODO real error handling
        	System.err.println("Failed to get field " + fieldName);
        	System.err.println("My sql was " + selectSql);
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @inheritDoc
     */
    public void setRecordIndex(int index) {
        try {
        	int oldRow = rs.getRow();
            if (rs != null) {
                //RowSet is 1 based because ResultSet is 1 based. Why THAT is 1
            	//based is a mystery to me and a real pain in the neck
                rs.absolute(index + 1);
            }
            //remember to subtract 1 from oldRow because oldRow is 1 based and
            //the value passed to fireRowChanged must be 0 based
            fireRowChanged(oldRow-1);
        } catch (Exception e) {
            //TODO real error handling
            e.printStackTrace();
        }
    }

    /**
     * @inheritDoc
     */
    protected void setValueImpl(String fieldName, Object value, int index) {
        try {
            if (rs != null) {
                int currentRow = rs.getRow();
                rs.absolute(index + 1);//index is 0 based, rs is 1 based
                rs.updateObject(fieldName, value);
                rs.absolute(currentRow);
            }
        } catch (Exception e) {
            //TODO real error handling
            e.printStackTrace();
        }
    }

    /* (non-Javadoc)
     * @see org.jdesktop.swing.data.DataModel#getValueAt(java.lang.String, int)
     */
    public Object getValue(String fieldName, int index) {
        if (rs == null) {
            return null;
        }

        try {
            int currentRow = rs.getRow();
            rs.absolute(index + 1);//index is 0 based, rs is 1 based
            Object val = getValue(fieldName);
            rs.absolute(currentRow);
            return val;
        } catch (Exception e) {
            //TODO this is a massive problem if this doesn't work....
            //TODO real error handling
            return null;
        }
    }

    /* (non-Javadoc)
     * @see org.jdesktop.swing.data.DataModel#appendRecord()
     */
    public void appendRecord() {
        insertRecord(getRecordCount());
    }

    /* (non-Javadoc)
     * @see org.jdesktop.swing.data.DataModel#insertRecord(int)
     */
    public void insertRecord(int index) {
        try {
            rs.moveToInsertRow();
            rs.insertRow();
            setRecordIndex(getRecordCount() - 1);
            fireModelDataChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

	/* (non-Javadoc)
	 * @see org.jdesktop.jdnc.incubator.rbair.swing.data.DataModel#firstRecord()
	 */
	public boolean firstRecord() {
		if (getRecordCount() > 0) {
			setRecordIndex(0);
			return true;
		} else {
			return false;
		}
	}

	/* (non-Javadoc)
	 * @see org.jdesktop.jdnc.incubator.rbair.swing.data.DataModel#prevRecord()
	 */
	public boolean prevRecord() {
		if (hasPrev()) {
			setRecordIndex(getRecordIndex() - 1);
			return true;
		} else {
			return false;
		}
	}

	/* (non-Javadoc)
	 * @see org.jdesktop.jdnc.incubator.rbair.swing.data.DataModel#nextRecord()
	 */
	public boolean nextRecord() {
		if (hasNext()) {
			setRecordIndex(getRecordIndex() + 1);
			return true;
		} else {
			return false;
		}
	}

	/* (non-Javadoc)
	 * @see org.jdesktop.jdnc.incubator.rbair.swing.data.DataModel#lastRecord()
	 */
	public boolean lastRecord() {
		if (getRecordCount() > 0) {
			setRecordIndex(getRecordCount() - 1);
			return true;
		} else {
			return false;
		}
	}

	/* (non-Javadoc)
	 * @see org.jdesktop.jdnc.incubator.rbair.swing.data.DataModel#hasNext()
	 */
	public boolean hasNext() {
		if (getRecordIndex() < getRecordCount() - 1) {
			return true;
		} else {
			return false;
		}
	}

	/* (non-Javadoc)
	 * @see org.jdesktop.jdnc.incubator.rbair.swing.data.DataModel#hasPrev()
	 */
	public boolean hasPrev() {
		if (getRecordIndex() > 0 && getRecordCount() > 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Returns the rowset. This method is provided for the master/detail handler
	 * @return
	 */
	CachedRowSet getRowSet() {
		return rs;
	}
	
	/**
	 * @return Returns the selectSql.
	 */
	public String getSelectSql() {
		return selectSql;
	}
	
	/**
	 * @param selectSql The selectSql to set.
	 */
	public void setSelectSql(String selectSql) {
		this.selectSql = selectSql;
	}
	
	/**
	 * @return Returns the selectParams.
	 */
	public List getSelectParams() {
		return selectParams;
	}
	
	/**
	 * @param selectParams The selectParams to set.
	 */
	public void setSelectParams(List selectParams) {
		this.selectParams = selectParams == null ? new ArrayList() : selectParams;
	}
	
	/**
	 * @param dataSource The dataSource to set.
	 */
	public void setDataSource(RowSetDataSource dataSource) {
		if (dataSource == null) {
			throw new NullPointerException("The dataSource for a " +
					"RowSetDataModel cannot be null");
		}
		this.dataSource = dataSource;
		super.dataSource = dataSource; //hack
		try {
			this.dataSource.addDataModel(this);
		} catch (Exception e) {
			System.err.println("Failed to associate a dataSource with a RowSetDataModel");
			e.printStackTrace();
		}
	}
	
	public String getTableName() {
		return tableName;
	}
	
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
}