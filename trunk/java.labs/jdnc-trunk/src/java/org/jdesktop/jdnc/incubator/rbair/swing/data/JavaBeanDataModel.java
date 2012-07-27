/*
 * $Id: JavaBeanDataModel.java 128 2004-10-18 20:06:27Z rbair $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */

package org.jdesktop.jdnc.incubator.rbair.swing.data;

import org.apache.commons.beanutils.PropertyUtils;
import org.jdesktop.jdnc.incubator.rbair.util.Progression;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.*;

/**
 * This DataModel models a collection of JavaBeans.
 *
 * @author Mark Davidson
 * @author Richard Bair
 */
public class JavaBeanDataModel extends AbstractDataModel {
    /**
     * The actual data in this data model
     */
    private Progression data = new Progression();
    /**
     * If this JavaBeanDataModel is a detail DataModel, then this string must
     * contain the field name in the master DataModel upon which this detail
     * is based
     */
    private String masterFieldName;
    /**
     * This is the actual java bean being modeled by this DataModel. This bean
     * may be a Collection, an array, or any other type of object. If it is a
     * Collection or an Array, the contents of this bean will constitute the
     * contents of the DataModel. Otherwise, the bean itself constitutes the
     * contents of the DataModel.
     */
    private Object javaBean;
    /**
     * The class of allowable java beans. Only beans of this class type
     * may be added to this JavaBeanDataModel 
     */
    private Class beanClass;
    
    
    /**
     * Create a new JavaBeanDataModel based on the given bean class, and the
     * given metaData. If metaData is null, the beanClass will be used to
     * generate default MetaData. By default, a JavaBeanMasterDetailHandler
     * is registered as the handler for this object.
     * @param beanClass
     * @param metaData
     * @throws IntrospectionException
     */
    public JavaBeanDataModel(Class beanClass, MetaData[] metaData) throws IntrospectionException {
    	this.beanClass = beanClass;
        BeanInfo info = Introspector.getBeanInfo(beanClass);
        if (metaData == null) {
            //generate my own meta data from the Introspector
            PropertyDescriptor[] props = info.getPropertyDescriptors();
            metaData = new MetaData[props.length];
            for (int i = 0; i < props.length; i++) {
                metaData[i] = new MetaData(props[i].getName(), props[i]
                        .getPropertyType(), props[i].getDisplayName());
            }
        }
        for (int i = 0; i < metaData.length; i++) {
            addField(metaData[i]);
        }
        super.setMasterDetailHandler(new JavaBeanMasterDetailHandler());
    }

    /**
     * Create a new JavaBeanDataModel
     * @param beanClass
     * @throws IntrospectionException
     */
    public JavaBeanDataModel(Class beanClass) throws IntrospectionException {
        this(beanClass, null);
    }

    /**
     * Constructs a JavaBeanDataModel by introspecting on the class and using
     * the data from the object as the current bean
     *
     * @param bean      the object where the current values will be retrieved and
     *                  stored.
     */
    public JavaBeanDataModel(Object bean)
            throws IntrospectionException {
    	this(bean == null ? Object.class : bean.getClass(), null);
    	setJavaBean(bean);
    }
    
    /**
     * Sets the name of the field on the master DataModel to call in order
     * to get the new JavaBean for this class. This method only has meaning
     * if this DataModel is a detail of a master.
     * @param name
     */
    public void setMasterFieldName(String name) {
    	this.masterFieldName = name;
    }
    
    /**
     * Returns the name of the field on the master DataModel to call in order
     * to get the new JavaBean for this class. This method only has meaning
     * if this DataModel is a detail of a master.
     * @param name
     */
    public String getMasterFieldName() {
    	return masterFieldName;
    }
    

    /**
     * Set the JavaBean instance that this model will use. This replaces the 
     * current bean (that is, the current bean is removed from the DataModel 
     * and replaced with this one)
     */
    public void setJavaBean(Object bean) {
    	//ensure that the new object, bean, is of the proper type
    	//TODO It is quite probable that the 'bean' object is a collection,
    	//in which case we don't want to do the check below on the bean, but
    	//on its contents
//        if (bean != null && !beanClass.isAssignableFrom(bean.getClass())) {
//            throw new RuntimeException("ERROR: argument is not a "
//                    + beanClass.toString());
//        }

        //only update this DataModel if the bean is different from the JavaBean
        //currently encapsulated by this DataModel
        if (bean != javaBean) {
        	Object[] keyValues = null;
        	if (javaBean != null && getRecordCount() > 0 && getRecordIndex() >= 0) {
        		String[] keyFields = getKeyFields();
        		keyValues = new Object[keyFields.length];
        		for (int i=0; i<keyValues.length; i++) {
        			keyValues[i] = getValue(keyFields[i]);
        		}
        	}
        	
        	//remove all of the items from this DataModel
        	data.removeAll();
        	//append this bean to the DataModel (expanding if necessary)
            appendData(bean);
            //set the local javaBean
            javaBean = bean;
            //try to reset the position
            try {
            	if (keyValues == null) {
            		setRecordIndex(0);
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
            			setRecordIndex(0);
            		}
            	}
//                setMetaData(data == null ? null : data.getMetaData());
            } catch (Exception e) {
                //TODO This is a horrible way to handle errors -- what should happen?
                e.printStackTrace();
            }
            // Notify listeners that the model has changed
            fireModelDataChanged();
        }
    }

    /**
     * Get the JavaBean instance encapsulated by this DataModel
     */
    public Object getJavaBean() {
        return javaBean;
    }

    /**
     * Returns the value associated with a given field name. The fieldName must
     * be the java bean name of a field, or property, within the bean.
     */
    public Object getValue(String fieldName) {
    	//if this DataModel is not encapsulating a bean, then there is no
    	//field value and null is returned rather than throwing an exception
        if (javaBean == null) {
            return null;
        }
        //otherwise, use PropertyUtils to try to get the field value
        try {
            return PropertyUtils.getProperty(data.getCurrentItem(), fieldName);
        } catch (Exception e) {
        	e.printStackTrace();
            return "Can't read " + fieldName;
        }
    }

    /**
     * Method that actually sets the given value to the given field name
     */
    protected void setValueImpl(String fieldName, Object value, int index) {
    	//do nothing if the javaBean is null
        if (javaBean == null) {
            return;
        }
        //attempt to set the value
        try {
            //temporarily change the index (without any notifications via listeners)
            int currentIndex = data.getCurrentIndex();
            data.setCurrentIndex(index);
            PropertyUtils.setProperty(data.getCurrentItem(), fieldName, value);
            data.setCurrentIndex(currentIndex);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * @inheritDoc
     */
    public int getRecordCount() {
        return data.size();
    }

    /**
     * @inheritDoc
     */
    public int getRecordIndex() {
        return data.getCurrentIndex();
    }

    /**
     * @inheritDoc
     */
    public void setRecordIndex(int index) {
    	int oldRow = data.getCurrentIndex();
        data.setCurrentIndex(index);
        fireRowChanged(oldRow);
    }

    /**
     * @inheritDoc
     */
    public void appendData(Object newData) {
        if (newData != null) {
        	int oldSize = data.size();
        	int oldIndex = data.getCurrentIndex();
        	Class klass = newData.getClass();
            if (Collection.class.isAssignableFrom(klass)) {
                Collection c = (Collection) newData;
                data.addAll(c);
            } else if (Iterator.class.isAssignableFrom(klass)) {
                Iterator itr = (Iterator) newData;
                while (itr.hasNext()) {
                	data.add(itr.next());
                }
            } else if (Enumeration.class.isAssignableFrom(klass)) {
                data.addAll(Collections.list((Enumeration) newData));
            } else if (klass.isArray()) {
                Object[] arrayValue = (Object[]) newData;
                data.addAll(arrayValue);
            } else {
            	data.add(newData);
            }
            if (oldSize == 0 && data.size() > 0) {
            	setRecordIndex(0);
            } else if (oldSize > 0 && data.size() > 0 && oldIndex >= 0) {
            	setRecordIndex(oldIndex);
            }
        }
    }

    /* (non-Javadoc)
     * @see org.jdesktop.swing.data.DataModel#appendRecord()
     */
    public void appendRecord() {
        throw new IllegalArgumentException("Unfinished");
    }

    /* (non-Javadoc)
     * @see org.jdesktop.swing.data.DataModel#insertRecord(int)
     */
    public void insertRecord(int index) {
        throw new IllegalArgumentException("Unfinished");
    }

    /**
     * @inheritDoc
     */
    public boolean firstRecord() {
    	int oldRow = data.getCurrentIndex();
    	boolean result = data.first() != null;
    	if (result) {
    		fireRowChanged(oldRow);
    	}
        return result;
    }

    /**
     * @inheritDoc
     */
    public boolean prevRecord() {
    	int oldRow = data.getCurrentIndex();
    	boolean result = data.previous() != null;
    	if (result) {
    		fireRowChanged(oldRow);
    	}
        return result;
    }

    /**
     * @inheritDoc
     */
    public boolean nextRecord() {
    	int oldRow = data.getCurrentIndex();
    	boolean result = data.next() != null;
    	if (result) {
    		fireRowChanged(oldRow);
    	}
        return result;
    }

    /**
     * @inheritDoc
     */
    public boolean lastRecord() {
    	int oldRow = data.getCurrentIndex();
    	boolean result = data.last() != null;
    	if (result) {
    		fireRowChanged(oldRow);
    	}
        return result;
    }

    /**
     * @inheritDoc
     */
    public boolean hasNext() {
        return data.hasNext();
    }

    /**
     * @inheritDoc
     */
    public boolean hasPrev() {
        return data.hasPrevious();
    }

    /**
     * @inheritDoc
     */
    public Object getValue(String fieldName, int index) {
        //temporarily change the index (without any notifications via listeners)
        int currentIndex = data.getCurrentIndex();
        data.setCurrentIndex(index);
        Object val = getValue(fieldName);
        data.setCurrentIndex(currentIndex);
        return val;
    }
    
    /**
     * FIXME: This method is a total hack -- in place for transaction demo
     * purposes
     * @param ds
     */
    public void setDataSource(DataSource ds) {
    	if (dataSource != null) {
    		//TODO remove me from the old DataSource
    	}
    	dataSource = ds;
    	if (dataSource != null) {
    		try {
    			((DefaultDataSource)dataSource).addDataModel(this);
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
    	}
    }

}