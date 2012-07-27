/*
 * $Id: PropertySupporter.java 2631 2008-08-06 09:23:10Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.beans;

import java.beans.PropertyChangeListener;

/**
 * A runtime interface which can be used to denote a property supporting
 * JavaBean but which cannot extend AbstractBean.
 */
public interface PropertySupporter {
   /**
    * Add a property change listener to this object.
    * @param l The listener to add.
    */
   public void addPropertyChangeListener(PropertyChangeListener l);




   /**
    * remove a property change listener from this object.
    * @param l The listener to remove.
    */
   public void removePropertyChangeListener(PropertyChangeListener l);




   /**
    * Get the property change listeners added to this object.
    * @return array of listeners added to this class.
    */
   public PropertyChangeListener[] getPropertyChangeListeners();
}
