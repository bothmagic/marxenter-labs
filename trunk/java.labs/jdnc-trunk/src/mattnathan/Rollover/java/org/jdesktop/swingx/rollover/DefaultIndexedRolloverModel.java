/*
 * $Id: DefaultIndexedRolloverModel.java 2629 2008-08-06 08:27:49Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.rollover;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * Provides the default implementation for an indexed rollover model. Change
 * events fired from this model take the form of indexed property changes on the
 * STATE_PROPERTY property.
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 * @version 2.0
 */
public class DefaultIndexedRolloverModel extends DefaultRolloverModel implements IndexedRolloverModel {
    /**
     * Property support.
     */
    protected transient PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

    /**
     * The rollover index.
     */
    private int rolloverIndex = -1;

    /**
     * {@inheritDoc}
     */
    public void addPropertyChangeListener(PropertyChangeListener l) {
        propertyChangeSupport.addPropertyChangeListener(l);
    }





    /**
     * {@inheritDoc}
     */
    public void removePropertyChangeListener(PropertyChangeListener l) {
        propertyChangeSupport.removePropertyChangeListener(l);
    }





    /**
     * {@inheritDoc}
     */
    public PropertyChangeListener[] getPropertyChangeListeners() {
        return propertyChangeSupport.getPropertyChangeListeners();
    }





    /**
     * Call when a property has changed.
     *
     * @param propertyName The property name.
     * @param oldValue The old value.
     * @param newValue The new value.
     */
    protected void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
        propertyChangeSupport.firePropertyChange(propertyName, oldValue, newValue);
    }





    /**
     * Call when a property has changed.
     *
     * @param propertyName The property name.
     * @param oldValue The old value.
     * @param newValue The new value.
     */
    protected void firePropertyChange(String propertyName, boolean oldValue, boolean newValue) {
        propertyChangeSupport.firePropertyChange(propertyName, oldValue, newValue);
    }





    /**
     * Call when a property has changed.
     *
     * @param propertyName The property name.
     * @param oldValue The old value.
     * @param newValue The new value.
     */
    protected void firePropertyChange(String propertyName, int oldValue, int newValue) {
        propertyChangeSupport.firePropertyChange(propertyName, oldValue, newValue);
    }





    /**
     * {@inheritDoc}
     */
    public int getRolloverIndex() {
        return rolloverIndex;
    }





    /**
     * {@inheritDoc}
     */
    public void setRolloverIndex(int index) {
        int old = rolloverIndex;
        if (old != index) {
            this.rolloverIndex = index;
            firePropertyChange(ROLLOVER_INDEX_PROPERTY, old, getRolloverIndex());
        }
    }

}
