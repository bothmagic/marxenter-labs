/*
 * $Id: FormFactory.java 46 2004-09-08 17:33:01Z rbair $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */

package org.jdesktop.jdnc.incubator.rbair.swing.form;

import javax.swing.JComponent;

import org.jdesktop.jdnc.incubator.rbair.swing.binding.Binding;
import org.jdesktop.jdnc.incubator.rbair.swing.data.DataModel;
import org.jdesktop.jdnc.incubator.rbair.swing.data.MetaData;

/**
 * Form factory class which provides support for constructing the user-interface
 * components and associated bindings for a JForm component.
 * <p>
 * Generally, applications need not interact directly with this class because
 * the default operation of the JForm component will invoke this factory as
 * necessary to construct the form based on how the application binds the form
 * to the application's data models.</p>
 * <p>
 * This factory provides methods for 3 key form-building operations:
 * <ol>
 * <li>createComponent: given a MetaData object which describes a data field
 *     (name, type, edit constraints, etc), return the user-interface component
 *     which can best display and/or edit values for that field.</li>
 * <li>createBinding: given a user-interface component and a named field on a
 *     data model, return the Binding instance required to bind that component
 *     to that field.</li>
 * <li>addComponent: adds the provided component to the specified form container,
 *     including laying it out according to the layout paradigm of the form
 *     factory</li>
 * </ol>
 * Note that a form factory is stateless and these methods operate independent
 * of each other, thus an application may invoke only those methods it requires
 * to construct the form.  For example, an application that wishes to use the
 * form factory's components and bindings, but intends to handle the layout
 * itself, may choose to invoke only the first two methods.
 *
 * @author Amy Fowler
 * @version 1.0
 */

public abstract class FormFactory {
    private static FormFactory defaultFormFactory;

    /**
     *
     * @return FormFactory instance which is shared across the application
     */
    public static FormFactory getDefaultFormFactory() {
        if (defaultFormFactory == null) {
            defaultFormFactory = new DefaultFormFactory();
        }
        return defaultFormFactory;
    }

    /**
     * Sets the default FormFactory instance which is shared across the application.
     * @param formFactory factory to be used as the default form factory
     */
    public static void setDefaultFormFactory(FormFactory formFactory) {
        FormFactory.defaultFormFactory = formFactory;
    }

    /**
     * Factory method for returning the user-interface component best suited
     * to edit/display values for the data model field represented by the metaData
     * object. The returned component is not only based on the metadata's type, but
     * also on it's edit constraints.
     * @param metaData object which describes the named field
     * @return JComponent which can display/edit values defined by the metaData
     *         object
     */
    public abstract JComponent createComponent(MetaData metaData);

    /**
     * Factory method for returning the binding object which connects the
     * user-interface component to the specified field in a data model.
     * @param model data model object to which the component is being bound
     * @param fieldName String containing the name of the field within the data model
     * @param component JComponent which can display/edit values defined by the metaData
     *        object
     * @return Binding instance which binds the component to the field in the data model
     */
    public abstract Binding createBinding(DataModel model, String fieldName, JComponent component);

    /**
     * Adds the component to the specified parent container and configures its
     * layout within that container according to the form factory's layout
     * paradigm.  If the metaData argument is not null, then a label will be
     * automatically created and aligned with the component.
     * Note that the component being added need not be the component
     * which has the binding.  For example, an edit component may be contained
     * within another container (scrollpane, panel, etc); the edit component
     * will have the binding, but the container is what must be added to the form.
     *
     * @param parent Container where the component is being added
     * @param component JComponent being added to the container
     * @param metaData object which describes the named field
     */
    public abstract void addComponent(JComponent parent, JComponent component, MetaData metaData);

}