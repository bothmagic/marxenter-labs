/*
 * $Id: PropertyChangeSupportInterface.java 281 2005-01-12 07:56:44Z wadechandler $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */

/*
 * PropertyChangeSupportInterface.java
 *
 * Created on January 5, 2005, 10:18 PM
 */

package org.jdesktop.jdnc.incubator.wadechandler.beansupport;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Interface used to make it easier to create a good bean class by helping
 * make it a little easier to proxy java.beansupport.PropertyChangeSupport.  This isn't
 * so useful for swing extensions, but it comes in real handy for home grown data
 * objects or other non extension beans.  The usefulness simply means it helps one
 * take care of all of the needed property change patterns.  This interface comes in most
 * handy when used within an IDE, code synchronizing, and using a PropertyChangeSupport
 * object as a proxy.
 * @author Wade Chandler
 * @version 1.0
 */
public interface PropertyChangeSupportInterface {
    void addPropertyChangeListener(PropertyChangeListener listener);
    void addPropertyChangeListener(String propertyName, PropertyChangeListener listener);
    void firePropertyChange(PropertyChangeEvent evt);
    void firePropertyChange(String propertyName, Object oldValue, Object newValue);
    void firePropertyChange(String propertyName, byte oldValue, byte newValue);
    void firePropertyChange(String propertyName, char oldValue, char newValue);
    void firePropertyChange(String propertyName, short oldValue, short newValue);
    void firePropertyChange(String propertyName, int oldValue, int newValue);
    void firePropertyChange(String propertyName, long oldValue, long newValue);
    void firePropertyChange(String propertyName, double oldValue, double newValue);
    void firePropertyChange(String propertyName, float oldValue, float newValue);
    PropertyChangeListener[] getPropertyChangeListeners();
    PropertyChangeListener[] getPropertyChangeListeners(String propertyName);
    boolean hasListeners(String propertyName);
    void removePropertyChangeListener(PropertyChangeListener listener);
    void removePropertyChangeListener(String propertyName, PropertyChangeListener listener);
}
