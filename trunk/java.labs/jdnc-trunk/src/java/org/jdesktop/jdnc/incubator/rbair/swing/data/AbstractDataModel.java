/*
 * $Id: AbstractDataModel.java 162 2004-12-10 22:05:35Z rbair $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */

package org.jdesktop.jdnc.incubator.rbair.swing.data;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Abstract base class for implementing concrete DataModel implementations. This
 * class provides support for managing validators and value change listeners.
 * Subclasses must implement their own mechanism to store field meta-data and
 * values.
 * 
 * @author Amy Fowler
 * @version 1.0
 */
public abstract class AbstractDataModel implements DataModel {
	/**
	 * A Map containing <code>String</codes>s for keys, and
	 * <code>DataModel</code>s for values.
	 */
	protected Map/*<String,DataModel>*/ detailModels = 
		new HashMap/*<String,DataModel>*/();
	/**
	 * The key used by this DataModel's master to identify this DataModel
	 */
	private String key;
	/**
	 * The master DataModel
	 */
	protected AbstractDataModel masterDM;
	/**
	 * List of validators for validating fields
	 */ 
	protected ArrayList/*<Validator>*/ validators = 
		new ArrayList/*<Validator>*/();
	/**
	 * The list of data model listeners
	 */
	private ArrayList/*<DataModelListener>*/ dataModelListeners = 
		new ArrayList/*<DataModelListener>*/();
	/**
	 * Support class used for tracking bound property changes, in particular,
	 * the currentRecordIndex property.
	 */
	private PropertyChangeSupport pcs = new PropertyChangeSupport(this);
	/**
	 */
	private HashMap/*<String,ValueChangeEvent>*/ valueChangeEvents = 
		new HashMap/*<String,ValueChangeEvent>*/();
	/**
	 * The master detail handler containing the logic to use when the master
	 * DataModel changes current records or in some other way requires the
	 * detail data model to reload its contents.
	 */
	private MasterDetailHandler handler;
	/**
	 * A cached list of the field names, as noted in the meta data
	 */
	private ArrayList/*<String>*/ fieldNames = 
		new ArrayList/*<String>*/();
	/**
	 * The meta data associated with this model, indexed by field name
	 */
	private HashMap/*<String,MetaData>*/ metaData = 
		new HashMap/*<String,MetaData>*/();
	/**
	 * Array of field names that represent the key fields for this DataModel.
	 * Key fields are those fields that together form a unique key for the
	 * record. In RDBMS these would be equivilent to the primary key field(s).
	 */
	protected String[] keyFields = new String[0];
	
	/**
	 * I don't like this being here like this...
	 */
    protected DataSource dataSource;

    /**
	 * @inheritDoc
	 */
	public void setKeyFields(String[] keyFields) {
		this.keyFields = keyFields == null ? new String[0] : keyFields;
	}
	
	/**
	 * @inheritDoc
	 */
	public String[] getKeyFields() {
		//TODO is this safe?
		return keyFields;
	}
	
	/**
	 * @inheritDoc
	 */
	public String getKey() {
		return key;
	}
	
	/**
	 * @inheritDoc
	 */
	public void setKey(String key) {
		//if this DataModel has a master, remove it from the master &
		//re-add it so that it is listed in the master with the proper key
		//XXX Ensure that no events are fired
		if (masterDM != null) {
			masterDM.removeDetailDataModel(this);
			masterDM.addDetailDataModel(this, key);
		}
		this.key = key;
	}
	
	/**
	 * @inheritDoc
	 */
	public DataModel getMasterDataModel() {
		return masterDM;
	}
	
	/**
	 * @inheritDoc
	 */
	public void setMasterDataModel(DataModel dm) {
		if (!(dm instanceof AbstractDataModel)) {
			throw new IllegalArgumentException("Please give me an " +
					"AbstractDataModel");
		}
		if (dm != masterDM) {
			if (dm == null && masterDM != null) {
				masterDM.removeDetailDataModel(this);
			}
			masterDM = (AbstractDataModel)dm;
			if (masterDM != null) {
				masterDM.addDetailDataModel(this, key);
			}
		}
	}
	
	/**
	 * Adds the given DataModel to this master's list of detail DataModels.
	 * If the given key is null, a NullPointerException is thrown. If it is
	 * not null, but already in use by a different DataModel, then an
	 * IllegalArgumentException is thrown.
	 * TODO May want to fire events on the detail to have the detail load itself
	 * @param dm
	 * @param key
	 */
	protected void addDetailDataModel(DataModel dm, String key) {
		if (key == null) {
			throw new NullPointerException("The key passed to the master " +
					"DataModel cannot be null");
		}
		if (detailModels.containsKey(key) && dm != detailModels.get(key)) {
			throw new IllegalArgumentException("The specified key '" + key + 
					"' is already in use by this master DataModel");
		}
		detailModels.put(key, dm);
	}
	
	/**
	 * Removes the given DataModel from the map of detail DataModels. It is
	 * possible that the key has changed since the detail DataModel was added
	 * to the map, so instead of relying on the key, the map is traversed
	 * for the data model. This should be inexpensive since the total number
	 * of detail DataModels is not expected to be very large
	 * TODO May want to fire events on the detail to have the detail flush itself
	 * @param dm
	 */
	protected void removeDetailDataModel(DataModel dm) {
		Iterator itr = detailModels.entrySet().iterator();
		String key = null;
		while (itr.hasNext()) {
			Map.Entry entry = (Map.Entry)itr.next();
			if (entry.getValue() == dm) {
				key = (String)entry.getKey();
				break;
			}
		}
		if (key != null) {
			detailModels.remove(key);
		}
	}
	
	/**
	 * @inheritDoc
	 */
	public DataModel getDetailDataModel(String key) {
		if (!detailModels.containsKey(key)) {
			throw new IllegalArgumentException("The key '" + key + 
					"' specified in getDataModel is invalid");
		}
		
		return (DataModel)detailModels.get(key);
	}
	
	/**
	 * @inheritDoc
	 */
	public Set getDetailKeys() {
		return detailModels.keySet();
	}
	
	/**
	 * @inheritDoc
	 */
	public MasterDetailHandler getMasterDetailHandler() {
		return handler;
	}
	
	/**
	 * @inheritDoc
	 */
	public void setMasterDetailHandler(MasterDetailHandler handler) {
		this.handler = handler;
	}
	
	/**
	 * @return Returns the dataSource.
	 */
	public DataSource getDataSource() {
		return dataSource;
	}
	
	/**
	 * Internal method for adding a field's meta data without firing any events
	 * @param fieldMetaData
	 */
	protected void addFieldNoEvent(MetaData fieldMetaData) {
		String name = fieldMetaData.getName();
		fieldNames.add(name); // track the order in which fields were added
		metaData.put(name, fieldMetaData);
	}
	
	/**
	 * Add a field definition to this DataModel. This will fire a model meta-data changed
	 * event.
	 * @param fieldMetaData
	 */
	public void addField(MetaData fieldMetaData) {
		addFieldNoEvent(fieldMetaData);
		fireModelMetaDataChanged();
	}

	/**
	 * Removes a field definition from this DataModel without firing an event.
	 * @param fieldMetaData
	 */
	protected void removeFieldNoEvent(MetaData fieldMetaData) {
		if(fieldMetaData != null) {
		    String name = fieldMetaData.getName();
			fieldNames.remove(name);
			metaData.remove(name);		    
		}
	}
	
	/**
	 * Removes the given meta data definition from this DataModel, and fires a meta-data changed
	 * event.
	 * @param fieldMetaData
	 */
	public void removeField(MetaData fieldMetaData) {
		removeFieldNoEvent(fieldMetaData);
		fireModelMetaDataChanged();
	}

	/**
	 * A convenience method for getting the names of the fields. This is the same as
	 * iterating through all of the MetaData and extracting the name's from the meta data.
	 */
	public String[] getFieldNames() {
		return (String[]) fieldNames.toArray(new String[fieldNames.size()]);
	}

	/**
	 * Returns the MetaData associated with this data model. The MetaData contains all of the
	 * information necessary to bind objects to a data model.
	 */
	public MetaData getMetaData(String fieldName) {
		return (MetaData) metaData.get(fieldName);
	}

	/**
	 * Returns the number of fields (columns) in this DataModel
	 */
	public int getFieldCount() {
		return metaData.size();
	}

	/**
	 * Sets the value for the field specified by fieldName. This will fire a value changed event
	 * if the field's value is actually changed from what it was. This method should not normally
	 * be overridden. TODO: should it be final?
	 */
	public void setValue(String fieldName, Object value) {
		setValue(fieldName, value, getRecordIndex());
	}
	
	/**
	 * @inheritDoc
	 */
	public void setValue(String fieldName, Object value, int index) {
		Object oldValue = getValue(fieldName);
		setValueImpl(fieldName, value, index);
		if ((oldValue != null && !oldValue.equals(value))
				|| (oldValue == null && value != null)) {
			fireValueChanged(fieldName);
		}
		//notify all other DataModels in my transaction of this change
		Transaction tx = getDataSource().getTransaction();
		if (tx != null) {
			Set dmsInTx = tx.getDataModels();
			Iterator itr = dmsInTx.iterator();
			while (itr.hasNext()) {
				DataModel dm = (DataModel)itr.next();
				if (dm != this) {
					//tell the DataModel that it needs to locate the row
					//with the given keys and update its value.
					//The DataModels should do it in such a way that
					//the bindings are notified, but none of the other
					//DataModels are re-notified
					dm.notifyUpdated(this, fieldName, index);
				}
			}
		}
	}
	
	/**
	 * The default implementation of this method is to:
	 * <ol>
	 * <li>Retrieve the key values from the specified <code>sourceDM</code></li>
	 * <li>Retrieve the key values from this DataModel for all rows</li>
	 * <li>For each row, compare the two sets of key values.
	 * If they all match AND this DataModel contains a field with the name 
	 * <code>fieldName</code>, and the value at that field is different from 
	 * the value at <code>sourceDM.getValue(fieldName, index)</code>, then 
	 * update the field value in this DataModel at fieldName at the appropriate
	 * index.</li>
	 * </ol>
	 * The actual algorithm may use a map to retrieve the proper row index
	 * rather than traversing the DataModel, but those are implementation
	 * details.
	 * <p>
	 * This algorithm falls short in some situtations, such as when comparing
	 * a Person RowSetDataModel that contains info from a Person table, and a
	 * Summary RowSetDataModel formed from a view that contains some Person
	 * info in addition to other info gleaned from other tables, if it also
	 * has more than one key field since the Person table may have one key field
	 * and the Summary view may have 3 key fields.
	 * <p>
	 * TODO update the above scenario description because it is poorly written,
	 * while true.
	 * @param sourceDM
	 * @param fieldName
	 * @param index
	 */
	public void notifyUpdated(DataModel sourceDM, String fieldName, int index) {
		String[] kf = sourceDM.getKeyFields();
		//if the two sets of key fields are not the same length, then short
		//circuit this method
		if (keyFields.length != kf.length) {
			return;
		}
		//if the two data models don't contain comparable data, then short
		//circuit this method
		if (!containsComparableData(sourceDM)) {
			return;
		}
		//if this DataModel does not contain 'fieldName', then short circuit
		//this method
		String[] fieldNames = getFieldNames();
		boolean found = false;
		for (int i=0; i<fieldNames.length; i++) {
			if (fieldNames[i].equals(fieldName)) {
				found = true;
			}
		}
		if (!found) {
			return;
		}
		//if the two sets of key fields don't have the same field names, then
		//short circuit this method. Make this check while retrieving the key
		//values from the sourceDM
		Object[] sourceKeyValues = new Object[kf.length];
		for (int i=0; i<kf.length; i++) {
			sourceKeyValues[i] = sourceDM.getValue(kf[i], index);
			//look for an equivilent field name in keyFields
			found = false;
			for (int j=0; j<keyFields.length; j++) {
				if (kf[i].equals(keyFields[j])) {
					found = true;
				}
			}
			if (!found) {
				return;
			}
		}
		//search through each record for a record that has the sourceKeyValues
		Object[] keyValues = new Object[keyFields.length];
		for (int i=0; i<getRecordCount(); i++) {
			for (int j=0; j<keyFields.length; j++) {
				keyValues[j] = getValue(keyFields[j], i);
			}
			if (Arrays.equals(sourceKeyValues, keyValues)) {
				//I've found a record. Update it
				Object oldValue = getValue(fieldName, i);
				Object value = sourceDM.getValue(fieldName, index);
				setValueImpl(fieldName, value, i);
				//this check wasn't working because DataModel A and B were both
				//wrapping the same physical JavaBean, and by the time B was
				//going to be updated the oldValue equaled the value since A
				//had already set the value on the bean. Thus, the fireValueChanged
				//wasn't happening. I don't think I need this check, but its
				//late tonight and I'm not sure.
				//
//				if ((oldValue != null && !oldValue.equals(value))
//						|| (oldValue == null && value != null)) {
					fireValueChanged(fieldName);
//				}
				//TODO should I leave the method at this point? I'm not going
				//to, just in case there are duplicate records
			}
		}
	}
	
	/**
	 * This method is used as a "shortcut" to determine if two DataModels
	 * (this one and the specified one) contain comparable kinds of data.
	 * By default, this implementation says "yes" they have the same kind of
	 * data, which in essence will force the DataModel to compare all records
	 * when comparing one DataModel to another.
	 * @param dm
	 * @return
	 */
	protected boolean containsComparableData(DataModel dm) {
		return true;
	}
	
	/**
	 * Concrete subclasses must override this method to perform the actual 'set' operation.
	 * @param fieldName
	 * @param value
	 * @param index
	 */
	protected abstract void setValueImpl(String fieldName, Object value, int index);

	/**
	 * Adds a validator to the list of validators
	 */
	public void addValidator(Validator validator) {
		validators.add(validator);

	}

	/**
	 * Removes a validator from the list of validators
	 */
	public void removeValidator(Validator validator) {
		validators.remove(validator);
	}

	/**
	 * Returns an array of all the validators
	 */
	public Validator[] getValidators() {
		return (Validator[]) validators.toArray();
	}

	/**
	 * Fires the value change event. This is called normally during the set operation
	 * @param fieldName
	 */
	protected void fireValueChanged(String fieldName) {
		ValueChangeEvent e = getCachedEvent(fieldName);
		for (int i = 0; i < dataModelListeners.size(); i++) {
			DataModelListener dml = (DataModelListener)dataModelListeners.get(i);
			try {
			    dml.valueChanged(e);
			} catch (Exception ex) {
			    ex.printStackTrace();
			}
		}
		//If the row has changed, then details of this master
		//need to be updated as well. Loop through the detail model
		//and tell them to reload themselves, etc.
		notifyMasterChanged();
	}

	/**
	 * Fires the model change event.
	 */
	protected void fireModelDataChanged() {
		ModelChangeEvent e = new ModelChangeEvent(this);
		for (int i = 0; i < dataModelListeners.size(); i++) {
			DataModelListener dml = (DataModelListener)dataModelListeners.get(i);
			try {
				dml.modelChanged(e);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		//If the row has changed, then details of this master
		//need to be updated as well. Loop through the detail model
		//and tell them to reload themselves, etc.
		notifyMasterChanged();
	}

	protected void fireModelMetaDataChanged() {
		MetaDataChangeEvent e = new MetaDataChangeEvent(this);
		for (int i = 0; i < dataModelListeners.size(); i++) {
			DataModelListener dml = (DataModelListener)dataModelListeners.get(i);
			try {
				dml.metaDataChanged(e);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		//If the row has changed, then details of this master
		//need to be updated as well. Loop through the detail model
		//and tell them to reload themselves, etc.
		notifyMasterChanged();
	}
	
	protected void fireRowChanged(int oldValue) {
		//use bound property event mechanism
		pcs.firePropertyChange("recordIndex", oldValue, getRecordIndex());
		//If the row has changed, then details of this master
		//need to be updated as well. Loop through the detail model
		//and tell them to reload themselves, etc.
		notifyMasterChanged();
	}
	
	/**
	 * Internal method that notifies detail DataModels that the master
	 * has changed.
	 */
	private void notifyMasterChanged() {
		Iterator itr = detailModels.entrySet().iterator();
		while (itr.hasNext()) {
			Map.Entry entry = (Map.Entry)itr.next();
			((AbstractDataModel)entry.getValue()).masterChanged();
		}
	}
	
	/**
	 * Adds a value change listener
	 */
	public void addDataModelListener(DataModelListener l) {
		dataModelListeners.add(l);
	}

	/**
	 * removes a value change listener
	 */
	public void removeDataModelListener(DataModelListener l) {
		dataModelListeners.remove(l);
	}

	/**
	 * Returns an array of value change listeners
	 */
	public DataModelListener[] getDataModelListeners() {
		return (DataModelListener[])dataModelListeners.toArray(new DataModelListener[1]);
	}
	
	/**
	 * A utility method used for getting an event to fire. This method, along with the valueChangeEvents map, exist
	 * for colescing events in the case where multiple events are fired off quickly. Rather than notifying of redundant
	 * events, they are colesced here.
	 * @param fieldName
	 * @return
	 */
	private ValueChangeEvent getCachedEvent(String fieldName) {
		ValueChangeEvent event = (ValueChangeEvent)valueChangeEvents.get(fieldName);
		if (event == null) {
			event = new ValueChangeEvent(this, fieldName);
			valueChangeEvents.put(fieldName, event);
		}
		return event;
	}

	/**
	 * Reload the contents of this bean based on the master
	 * This method is called whenever the master DataModel has changed,
	 * informing the detail DataModel that it may have to revalidate itself
	 * and possibly reload data from the data store (or from some cache);
	 */
	public void masterChanged() {
		if (handler != null) {
			handler.handleMasterChanged(masterDM, this);
		}
	}
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		pcs.addPropertyChangeListener(listener);
	}
	public void addPropertyChangeListener(String propertyName,
			PropertyChangeListener listener) {
		pcs.addPropertyChangeListener(propertyName, listener);
	}
}
