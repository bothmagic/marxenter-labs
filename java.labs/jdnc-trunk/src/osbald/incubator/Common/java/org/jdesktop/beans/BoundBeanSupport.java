package org.jdesktop.beans;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/*
 * Interface for bound JavaBean support because Sun doesn't provide one for <code>PropertyChangeSupport</code>.
 *
 * Created by IntelliJ IDEA.
 * User: Richard Osbaldeston
 * Date: 29-Mar-2006
 * Time: 16:33:25
 */

//TODO add a removeAllListeners() as listeners can be a PITA to clean up (need to remember names bound)?

public interface BoundBeanSupport {
    void addPropertyChangeListener(PropertyChangeListener listener);

    void removePropertyChangeListener(PropertyChangeListener listener);

    PropertyChangeListener[] getPropertyChangeListeners();

    void addPropertyChangeListener(String propertyName, PropertyChangeListener listener);

    void removePropertyChangeListener(String propertyName, PropertyChangeListener listener);

    PropertyChangeListener[] getPropertyChangeListeners(String propertyName);

    void firePropertyChange(String propertyName, Object oldValue, Object newValue);

    void firePropertyChange(String propertyName, int oldValue, int newValue);

    void firePropertyChange(String propertyName, boolean oldValue, boolean newValue);

    void firePropertyChange(PropertyChangeEvent event);

    void fireIndexedPropertyChange(String propertyName, int index, boolean oldValue, boolean newValue);

    void fireIndexedPropertyChange(String propertyName, int index, int oldValue, int newValue);

    void fireIndexedPropertyChange(String propertyName, int index, Object oldValue, Object newValue);

    boolean hasListeners(String propertyName);
}
