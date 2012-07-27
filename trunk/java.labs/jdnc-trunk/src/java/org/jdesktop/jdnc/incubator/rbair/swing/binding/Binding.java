/*
 * $Id: Binding.java 46 2004-09-08 17:33:01Z rbair $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */

package org.jdesktop.jdnc.incubator.rbair.swing.binding;

import java.beans.PropertyChangeListener;

import javax.swing.JComponent;

import org.jdesktop.jdnc.incubator.rbair.swing.data.DataModel;

/**
 * Class which binds a user-interface component to a specific element
 * in a data model.  A Binding instance implements the following tasks:
 * <ul>
 * <li>pulls values from the data model into the UI component</li>
 * <li>performs element-level validation on the value contained
 *     in the UI component to determine whether it can/should be
 *     pushed to the model.</li>
 * <li>pushes validated values from the UI component to the data model.</li>
 * </ul>
 *
 * @author Amy Fowler
 * @version 1.0
 */

public interface Binding {

    public static final int AUTO_VALIDATE = 0;
    public static final int AUTO_VALIDATE_STRICT = 1;
    public static final int AUTO_VALIDATE_NONE = 2;

    public static final int UNVALIDATED = 0;
    public static final int VALID = 1;
    public static final int INVALID = 2;

    JComponent getComponent();

    DataModel getDataModel();

    String getFieldName();

    /**
     * Pulls the value of this binding's data model element
     * into its UI component.
     *
     * @return boolean indicating whether or not the value was pulled from the
     *         data model
     */
    boolean pull();

    /**
     *
     * @return boolean indicating whether or not the value contained in
     *         this binding's UI component has been modified since the
     *         value was last pushed or pulled
     */
    boolean isModified();

    /**
     * @return boolean indicating whether or not the value contained in
     *         this binding's UI component is valid
     */
    boolean isValid();

    int getValidState();

    /**
     * Returns validation error messages generated from the most
     * recent element-level validation pass.
     *
     * @return array containing any error messages which occurred during
     *         element-level validation
     */
    String[] getValidationErrors();

    /**
     * Pushes the current value contained in this binding's UI component
     * to this binding's data model element.  Only valid values
     * should be pushed to the model.
     * @return boolean indicating whether or not the value was pushed to the
     *         data model
     */
    boolean push();

    void addPropertyChangeListener(PropertyChangeListener pcl);

    void removePropertyChangeListener(PropertyChangeListener pcl);

    PropertyChangeListener[] getPropertyChangeListeners();

}
