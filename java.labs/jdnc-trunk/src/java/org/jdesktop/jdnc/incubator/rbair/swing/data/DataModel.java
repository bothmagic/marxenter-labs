/*
 * $Id: DataModel.java 128 2004-10-18 20:06:27Z rbair $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */

package org.jdesktop.jdnc.incubator.rbair.swing.data;

import java.beans.PropertyChangeListener;
import java.util.Set;

/**
 * <p/>
 * Abstract model interface for representing a collection of records of named 
 * data fields.
 * The map provides a uniform API for accessing data which may be contained
 * in a variety of data model constructs underneath, such as RowSet,
 * TabularDataModel, or arbitrary JavaBean classes.  The user-interface
 * Binding classes use this interface to &quot;bind&quot; user-interface
 * components to field elements in a data model without having to understand
 * the specific flavor of data model being used by the application.
 * For example, a field element may map to a named column on a RowSet
 * or a property on a JavaBean, but the binding classes don't need to
 * understand those underlying data structures in order to read and write
 * values.</p>
 * <p/>
 * For each named field, the data model provides access to:
 * <ul>
 * <li>meta-data: information describing the data field, such as type
 * and edit constraints</li>
 * <li>value: the current value of the field</li>
 * </ul>
 * </p>
 * <p/>
 * Often data models are collections of like-objects, such as the rows in a
 * RowSet, or a list of JavaBeans.  This interface provides a mechanism
 * to index into such a collection such that at any given time, the data model
 * contains the element values associated with the &quot;current&quot; record index
 * into that collection (the current row, or the current bean, etc).
 * </p>
 * <em>Please Note: </em> I would very much like to have generics available to
 * me, because this interface <b>should</b> have strongly typed
 * setDataSource and getDataSource methods. Instead, I am
 * either left with not specifying these methods and leaving them up to the
 * particular implementations of DataModel, or defining a generic case
 * (such as setDataSource(DataSource ds)). The generic case would then throw
 * IllegalArgumentException if an illegal DataMSource type for the DataModel
 * implementation were added. As a result, I am not specifying the methods at
 * this time, so that when generics are supported I can add the methods without
 * having to deprecate anything. Besides, since a generic DataModel
 * implementation will not work with any DataSource implementation, nothing is
 * really lost (the only thing lost is forcing implementations to use similar
 * function signatures).
 *
 * @author Amy Fowler
 * @version 1.0
 */
public interface DataModel {
	
//	public void setDataSource(DataSource ds);
	public DataSource getDataSource();
	
	/////////////  Methods associated with being a detail DataModel ///////////
	/**
	 * Sets the key which the master should associate with this detail. This
	 * method is specified separately from a single
	 * setMasterDataModel(DataModel dm, String key) method to allow gui builders
	 * to modify this setting in their guis.
	 * @param key The key which the master should associate with this detail. If
	 *            this key has already been specified in the master by another
	 *            DataModel, an IllegalArgumentException is thrown
	 */
	public void setKey(String key);
	
	/**
	 * @return the key by which this DataModel is known to its master DataModel,
	 * or null if there is no master DataModel.
	 */
	public String getKey();
	
	/**
	 * Sets the master DataModel. If non-null, invoking this method causes this
	 * DataModel to become a detail to the specified master.
	 * @param dm
	 */
	public void setMasterDataModel(DataModel dm);
	
	/**
	 * Returns the DataModel that is set as the master for this DataModel. If
	 * this DataModel has no master, null is returned.
	 * @return
	 */
	public DataModel getMasterDataModel();
	
	/**
	 * Sets the MasterDetailHandler for this DataModel
	 * @param handler
	 */
	public void setMasterDetailHandler(MasterDetailHandler handler);
	
	/**
	 * @return the MasterDetailHandler to use for this DataModel
	 */
	public MasterDetailHandler getMasterDetailHandler();
	
	/////////////  Methods associated with being a master DataModel ///////////
	/**
	 * Returns a detail DataModel corrosponding to the given key. If the key
	 * is not recognized, an IllegalArgumentException will be thrown. The set
	 * of proper keys can be retrieved via the <code>getKeys()</code> method.
	 * @param key a String representing the DataModel to retrieve
	 */
	public DataModel getDetailDataModel(String key);
	
	/**
	 * @return the set of all valid keys for retrieving detail data models.
	 */
	public Set getDetailKeys();
	
	/////////////  Methods associated with being a DataModel ///////////
	
	/**
	 * Used to notify the DataModel of a change in another DataModel. This
	 * DataModel should use this information to determine whether it contains
	 * the same field value, and whether to update the field value to the new
	 * value contained in the given DataModel.
	 * <p>
	 * For example, consider the following situation. Let there be a class
	 * called Person p, which is wrapped by two JavaBeanDataModels, A and B.
	 * If somebody calls A.setValue("name", "Richard"), and DataModels A and B
	 * are in the same transaction, then B should be notified that A's "name"
	 * field has been changed.
	 * @param sourceDM
	 * @param String fieldName
	 * @param index
	 */
	public void notifyUpdated(DataModel sourceDM, String fieldName, int index);
	
	/**
	 * Sets the key fields for this DataModel.
	 * Key fields are those fields that together form a unique key for the
	 * record. In RDBMS these would be equivilent to the primary key field(s).
	 * @param keyFields
	 */
	public void setKeyFields(String[] keyFields);
	
	/**
	 * Returns the key fields for this DataModel.
	 * @return
	 */
	public String[] getKeyFields();
	
	/**
     * @return array containing the names of all data fields in this map
     */
    public String[] getFieldNames();

    /**
     * @param fieldName String containing the name of the field
     * @return MetaData object which describes the named field
     */
    public MetaData getMetaData(String fieldName);

    /**
     * @param fieldName String containing the name of the field
     * @return Object containing the current value of the named field
     */
    public Object getValue(String fieldName);

    /**
     * @param fieldName String containing the name of the field
     * @param index     0 based index into the DataModel representing the
     *                  record to retrieve the value from. If the index is invalid, an
     *                  IllegalArgumentException is thrown.
     * @return Object containing the current value of the named field
     */
    public Object getValue(String fieldName, int index);

    /**
     * @param fieldName String containing the name of the field
     * @param value     Object containing the current value of the named field
     */
    public void setValue(String fieldName, Object value);

    /**
     * @param fieldName String containing the name of the field
     * @param value     Object containing the current value of the named field
     * @param index     0 based index into the DataModel. Represents the recordIndex
     *                  at which the value should be modified. If the index is invalid, an
     *                  IllegalArgumentException is thrown.
     */
    public void setValue(String fieldName, Object value, int index);

    /**
     * @return integer containing the number of fields in this data model
     */
    public int getFieldCount();

    /**
     * @return integer containing the number of records accessible
     *         from this data model
     */
    public int getRecordCount();

    /**
     * Sets the current record index such that the field values
     * in this data model represent the values contained at the specified index
     * in the underlying data model.
     *
     * @param index integer representing the current index into the underlying
     *              data model's records
     */
    public void setRecordIndex(int index);

    /**
     * @return integer representing the current index into the underlying
     *         data model's records
     */
    public int getRecordIndex();

    /**
     * Moves the current record index to the first index. On success, true is
     * returned. If for any reason this cannot be done (for instance, if the
     * DataModel is empty) false is returned.
     * @return
     */
    public boolean firstRecord();
    /**
     * Moves the current record index to the previous index. On success, true is
     * returned. If for any reason this cannot be done (for instance, if the
     * DataModel is empty, or you are already on the first record) false is
     * returned.
     * @return
     */
    public boolean prevRecord();
    /**
     * Moves the current record index to the next index. On success, true is
     * returned. If for any reason this cannot be done (for instance, if the
     * DataModel is empty, or you are already on the last record) false is
     * returned.
     * @return
     */
    public boolean nextRecord();
    /**
     * Moves the current record index to the last index. On success, true is
     * returned. If for any reason this cannot be done (for instance, if the
     * DataModel is empty) false is returned.
     * @return
     */
    public boolean lastRecord();
    /**
     * Returns true if there is another record following the current record.
     * Generally, if this returns true nextRecord will return true.
     * @return
     */
    public boolean hasNext();
    /**
     * Returns true if there is another record preceding the current record.
     * Generally, if this returns true prevRecord will return true.
     * @return
     */
    public boolean hasPrev();

    /**
     * Adds the specified validator for the fields represented by this
     * data model.
     * A validator object may be used to perform validation checks which
     * require analyzing more than one field value in a single check.
     * This DataModel instance will be passed in as the <code>value</code>
     * parameter to the validator's <code>validate</code> method.
     *
     * @param validator Validator object which performs validation checks on
     *                  this set of data field values
     * @see #removeValidator
     * @see #getValidators
     */
    public void addValidator(Validator validator);

    /**
     * Removes the specified validator from this data model.
     *
     * @param validator Validator object which performs validation checks on
     *                  this set of data field values
     * @see #addValidator
     */
    public void removeValidator(Validator validator);

    /**
     * @return array containing the validators registered for data model
     */
    Validator[] getValidators();

    /**
     * Adds the specified data model change listener
     *
     * @param dataModelListener
     */
    void addDataModelListener(DataModelListener dataModelListener);

    /**
     * Removes the specified data model change listener from this data model.
     *
     * @param dataModelListener
     */
    void removeDataModelListener(DataModelListener dataModelListener);

    /**
     * @return array containing the DataModelListener objects registered
     *         on this data model
     */
    DataModelListener[] getDataModelListeners();
    
    void addPropertyChangeListener(PropertyChangeListener listener);
    
    void addPropertyChangeListener(String propertyName, 
    		PropertyChangeListener listener);
}
