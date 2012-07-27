/*
 * $Id: AbstractBinding.java 128 2004-10-18 20:06:27Z rbair $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */

package org.jdesktop.jdnc.incubator.rbair.swing.binding;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;

import javax.swing.InputVerifier;
import javax.swing.JComponent;

import org.jdesktop.jdnc.incubator.rbair.swing.data.AbstractDataModel;
import org.jdesktop.jdnc.incubator.rbair.swing.data.ConversionException;
import org.jdesktop.jdnc.incubator.rbair.swing.data.Converter;
import org.jdesktop.jdnc.incubator.rbair.swing.data.DataModel;
import org.jdesktop.jdnc.incubator.rbair.swing.data.DataModelListenerAdapter;
import org.jdesktop.jdnc.incubator.rbair.swing.data.MetaData;
import org.jdesktop.jdnc.incubator.rbair.swing.data.ModelChangeEvent;
import org.jdesktop.jdnc.incubator.rbair.swing.data.Validator;
import org.jdesktop.jdnc.incubator.rbair.swing.data.ValueChangeEvent;

/**
 * Abstract base class which implements a default mechanism for binding
 * user-interface components to elements in a data model.
 *
 * @author Amy Fowler
 * @version 1.0
 */

public abstract class AbstractBinding implements Binding {

    protected DataModel dataModel;
    protected MetaData metaData;
    protected Object cachedValue;
    protected ArrayList errorList;
    protected boolean modified = false;
    protected int validState = UNVALIDATED;
    protected boolean pulling = false;

    private boolean pushing = false;
    private PropertyChangeSupport pcs;
    private int validationPolicy;
    /**
     * The fieldName needs to be cached because it is quite possible that
     * the meta-data hasn't been loaded yet, in which case, the field name
     * must be kept around
     */
    private String fieldName;

    protected AbstractBinding(JComponent component,
                              DataModel dataModel, String fieldName,
                              int validationPolicy) {
        setComponent(component);
        this.dataModel = dataModel;
        this.pcs = new PropertyChangeSupport(this);
        setValidationPolicy(validationPolicy);
        if (dataModel != null) {
            metaData = dataModel.getMetaData(fieldName);
            this.fieldName = fieldName;
            dataModel.addDataModelListener(new DataModelListenerAdapter() {
                public void valueChanged(ValueChangeEvent e) {
                	if (metaData != null) {
	                    if (e.getFieldName().equals(metaData.getName()) &&
	                          !pushing) {
	                        pull();
	                    }
                	}
                }
            	/**
            	 * @inheritDoc
            	 */
            	public void modelChanged(ModelChangeEvent e) {
                    pull();
            	}
            });
            dataModel.addPropertyChangeListener("recordIndex", new PropertyChangeListener() {
				public void propertyChange(PropertyChangeEvent evt) {
					pull();
				}
			});
        }
    }

    public DataModel getDataModel() {
        return dataModel;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setValidationPolicy(int policy) {
        this.validationPolicy = policy;
        getComponent().setInputVerifier(new InputVerifier() {
            public boolean verify(JComponent input) {
                if (validationPolicy != AUTO_VALIDATE_NONE) {
                    boolean isValid = isValid();
                    if (!isValid && validationPolicy == AUTO_VALIDATE_STRICT) {
                        return false;
                    }
                    return true;
                }
                return true;
            }
        });
    }

    public int getValidationPolicy() {
        return validationPolicy;
    }

    public boolean pull() {
        pulling = true;
        if (metaData != null && dataModel != null) {
        	cachedValue = dataModel.getValue(metaData.getName());
        	setComponentValue(cachedValue);
        }
        setModified(false);
        setValidState(UNVALIDATED);
        pulling = false;
        return true;
    }

    public boolean isModified() {
        return modified;
    }

    protected void setModified(boolean modified) {
        if (pulling) {
            return;
        }
        boolean oldModified = this.modified;
        this.modified = modified;
        if (modified) {
            cachedValue = null;
            setValidState(UNVALIDATED);
        }
        pcs.firePropertyChange("modified", oldModified, modified);
    }

    public boolean isValid() {
        if (validState != UNVALIDATED) {
            return validState == VALID;
        }
        // need to validate
        clearValidationErrors();
        Object componentValue = getComponentValue();

        // step 1: ensure a required element has non-null value
        boolean ok = checkRequired(componentValue);

        // step 2: if necessary, convert value from component to data type
        //         appropriate for model
        Object convertedValue = null;
        if (ok) {
            try {
                convertedValue = convertToModelType(componentValue);
            } catch (Exception e) {
                ok = false;
                /**@todo aim: very nerdy message */
                addError("value must be type " + metaData.getElementClass().getName());
            }
        }

        // step 3: run any registered element-level validators
        if (ok) {
            ok = executeValidators(convertedValue);
        }

        if (ok) {
            cachedValue = convertedValue;
        }
        setValidState(ok? VALID : INVALID);

        return validState == VALID;

    }

    public int getValidState() {
        return validState;
    }

    private void setValidState(int validState) {
        int oldValidState = this.validState;
        this.validState = validState;
        if (oldValidState != validState &&
            validState == UNVALIDATED) {
            clearValidationErrors();
        }
        pcs.firePropertyChange("validState", oldValidState, validState);
    }

    private boolean checkRequired(Object componentValue) {
        if (metaData != null && metaData.isRequired() &&
            (componentValue == null ||
             (componentValue instanceof String && ((String)componentValue).equals("")))) {
            addError("requires a value");
            return false;
        }
        return true;
    }

    protected Object convertToModelType(Object componentValue) throws ConversionException {
        Object convertedValue = null;
        // if the element is not required and the value is null, then it's
        // okay to skip conversion
        if (componentValue == null ||
            (componentValue instanceof String && componentValue.equals(""))) {
            return convertedValue;
        }
        Class elementClass = metaData.getElementClass();
        if (componentValue instanceof String) {
            String stringValue = (String) componentValue;
            Converter converter = metaData.getConverter();
            if (converter != null) {
                convertedValue = converter.decode(stringValue,
                                                      metaData.getDecodeFormat());
            } else if (metaData.getElementClass() == String.class) {
                convertedValue = componentValue;
            }
        }
        else {
            if (!elementClass.isAssignableFrom(componentValue.getClass())) {
                throw new ConversionException("cannot assign component value");
            } else {
                convertedValue = componentValue;
            }
        }
        return convertedValue;
    }

    protected String convertFromModelType(Object modelValue) {
        if (modelValue != null) {
            try {
                Converter converter = metaData.getConverter();
                return converter.encode(modelValue, metaData.getEncodeFormat());
            }
            catch (Exception e) {
                /**@todo aim: how to handle conversion failure? */
                return modelValue.toString();
            }
        }
        return "";
    }

    protected boolean executeValidators(Object value) {
    	if (metaData != null) {
	        Validator validators[] = metaData.getValidators();
	        boolean isValid = true;
	        for (int i = 0; i < validators.length; i++) {
	            String error[] = new String[1];
	            boolean passed = validators[i].validate(value, null, error);
	            if (!passed) {
	                String errorMessage = error[0];
	                if (errorMessage != null) {
	                    addError(errorMessage);
	                }
	                isValid = false;
	            }
	        }
	        return isValid;
    	}
    	return true;
    }

    public String[] getValidationErrors() {
        if (errorList != null) {
            return (String[])errorList.toArray(new String[1]);
        }
        return new String[0];
    }

    public void clearValidationErrors() {
        if (errorList != null) {
            errorList.clear();
        }
    }

    public abstract JComponent getComponent();
    protected abstract void setComponent(JComponent component);
    protected abstract Object getComponentValue();
    protected abstract void setComponentValue(Object value);

    protected void addError(String error) {
        if (errorList == null) {
            errorList = new ArrayList();
        }
        errorList.add(error);
    }

    public boolean push() {
        if (isValid()) {
            pushing = true;
            dataModel.setValue(metaData.getName(), cachedValue);
            setModified(false);
            pushing = false;
            return true;
        }
        return false;
    }

    /**
     * Adds the specified property change listener to this binding object.
     * @param pcl PropertyChangeListener object to receive events when binding
     *        properties change
     */
    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        pcs.addPropertyChangeListener(pcl);
    }

    /**
     * Removes the specified property change listener from this binding object.
     * @param pcl PropertyChangeListener object to receive events when binding
     *        properties change
     */
    public void removePropertyChangeListener(PropertyChangeListener pcl) {
        pcs.removePropertyChangeListener(pcl);
    }

    /**
     *
     * @return array containing the PropertyChangeListener objects registered
     *         on this binding object
     */
    public PropertyChangeListener[] getPropertyChangeListeners() {
        return pcs.getPropertyChangeListeners();
    }



}
