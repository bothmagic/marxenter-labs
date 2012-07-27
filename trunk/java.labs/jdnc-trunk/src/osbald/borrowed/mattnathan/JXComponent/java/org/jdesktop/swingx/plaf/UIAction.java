/*
 * $Id: UIAction.java 2578 2008-07-29 23:16:17Z osbald $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.plaf;

import java.beans.PropertyChangeListener;

import javax.swing.Action;

/**
 * Similar to the sun.swing.UIAction this class provides an simple immutable action with a name only.
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 */
public abstract class UIAction implements Action {
    private final String name;

    public UIAction(String name) {
        this.name = name;
    }





    public String getName() {
        return name;
    }





    /**
     * Does nothing as this class is immutable.
     *
     * @param listener a <code>PropertyChangeListener</code> object
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
    }





    /**
     * Returns null unless key is NAME.
     *
     * @param key String
     * @return Object
     */
    public Object getValue(String key) {
        if (key == NAME) {
            return getName();
        }
        return null;
    }





    /**
     * Returns true.
     *
     * @return true if this <code>Action</code> is enabled
     */
    public boolean isEnabled() {
        return true;
    }





    /**
     * Does nothing as this action is immutable.
     *
     * @param key a <code>String</code> containing the key
     * @param value an <code>Object</code> value
     */
    public void putValue(String key, Object value) {
    }





    /**
     * Does nothing as this is an immutable action.
     *
     * @param listener a <code>PropertyChangeListener</code> object
     */
    public void removePropertyChangeListener(PropertyChangeListener listener) {
    }





    /**
     * Does nothing as this action is immutable.
     *
     * @param b true to enable this <code>Action</code>, false to disable it
     */
    public void setEnabled(boolean b) {
    }
}
