/*
 * $Id: ValueChangeEvent.java 15 2004-09-04 23:06:37Z rbair $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */

package org.jdesktop.jdnc.incubator.rbair.swing.data;

import java.util.EventObject;

/**
 * Event indicating the value of a named data field within a
 * DataModel has changed.
 *
 * @see DataModelListener
 * @see DataModel
 *
 * @author Amy Fowler
 * @author Richard Bair
 * @version 1.0
 */
public class ValueChangeEvent extends EventObject {
	/**
	 * Indicates the start index when a range of rows were changed in the
	 * DataModel. This value is always valid because when it is the same as the 
	 * endIndex you have a range of 1. 0 based index into DataModel.
	 */
	private int startIndex = -1;
	/**
	 * Indicates the end index when a range of rows were changed in the
	 * DataModel. This value is always valid because when it is the same as the 
	 * startIndex you have a range of 1. 0 based index into DataModel.
	 */
	private int endIndex = -1;
	/**
	 * Specifies the name of the field that changed. If fieldName is null, then
	 * the entire record has potentially changed and needs to be refreshed.
	 */
    private String fieldName = null;

    /**
     * Instantiates a new value change event for the specified named
     * field in the data model, for the current row.
     * @param source DataModel containing the changed data field
     * @param fieldName String containing the name of the field that has changed
     */
    public ValueChangeEvent(DataModel source, String fieldName) {
    	this(source, source.getRecordIndex(), fieldName);
    }
    
    /**
     * Instantiates a new value change event for the specified named
     * field in the data model, for the row indicated by index.
     * @param source DataModel containing the changed data field
     * @param index Index representing the row that has changed
     * @param fieldName String containing the name of the field that has changed
     */
    public ValueChangeEvent(DataModel source, int index, String fieldName) {
    	this(source, index, index, fieldName);
    }
    
    /**
     * Instantiates a new value change event for the specified named
     * field in the data model, for the given range of rows.
     * @param source DataModel containing the changed data field
     * @param startIndex Index representing the beginning row that has changed
     * @param endIndex Index representing the ending row that has changed
     * @param fieldName String containing the name of the field that has changed
     */
    public ValueChangeEvent(DataModel source, int startIndex, int endIndex,
    		String fieldName) {
    	super(source);
    	//validate the parameters.
    	if (source == null) {
    		throw new IllegalArgumentException("The source DataModel cannot " +
    				"be null in a ValueChangeEvent!");
    	}
    	if (endIndex < startIndex) {
    		throw new IllegalArgumentException("The endIndex in a range of " +
    				"modified records cannot be less than the startIndex");
    	}
    	if (startIndex < 0) {
    		throw new IllegalArgumentException("The startIndex in a range of " +
    				"modified records cannot be less than 0");
    	}
    	if (endIndex >= source.getRecordCount()) {
    		throw new IllegalArgumentException("The endIndex in a range of " +
    				"modified records cannot be greater than the number of " +
    				"records in the DataModel");
    	}

    	//the values are fine, so finish construction of this event object
    	this.fieldName = fieldName;
    	this.startIndex = startIndex;
    	this.endIndex = endIndex;
    }

    /**
     * @return true if all of the fields for the source DataModel have had
     * values changed. This is symantically similar to getFieldName() == null.
     */
    public boolean allFieldsChanged() {
    	return fieldName == null;
    }
    
    /**
     * @return String containing the name of the field that has changed, or null
     * if all of the fields in the source DataModel have changed.
     */
    public String getFieldName() {
        return fieldName;
    }

    /**
     * @return int representing a 0 based index into DataModel. This is the 
     * first index in a range of indexes that have been changed. The start index
     * is always less than or equal to the end index.
     */
    public int getStartIndex() {
    	return startIndex;
    }
    
    /**
     * @return int representing a 0 based index into DataModel. This is the 
     * last index in a range of indexes that have been changed. The end index
     * is always greater than or equal to the start index.
     */
    public int getEndIndex() {
    	return endIndex;
    }
}
