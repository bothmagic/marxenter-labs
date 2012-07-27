/*
 * $Id: JForm.java 64 2004-09-22 19:43:37Z rbair $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */

package org.jdesktop.jdnc.incubator.rbair.swing.form;

import org.jdesktop.jdnc.incubator.rbair.swing.binding.BindException;
import org.jdesktop.jdnc.incubator.rbair.swing.binding.Binding;
import org.jdesktop.jdnc.incubator.rbair.swing.data.DataModel;
import org.jdesktop.jdnc.incubator.rbair.swing.data.JavaBeanDataModel;
import org.jdesktop.jdnc.incubator.rbair.swing.data.MetaData;
import org.jdesktop.jdnc.incubator.rbair.swing.data.Validator;
import org.jdesktop.jdnc.incubator.rbair.swing.UIAction;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.beans.IntrospectionException;
import java.util.ArrayList;
import java.util.HashMap;


/**
 * <p/>
 * Form component class for enabling display, editing, and actions on
 * a collection of data fields, each of which corresponds to
 * an identifiable element within an application data model.  For
 * example, a form field may map to a named column on a RowSet,
 * a property on a JavaBean, or a keyed element in a map, etc.
 * A single form can be used to display/edit data fields from
 * multiple data models.</p>
 * <p/>
 * For each data field in the form, a user-interface component is
 * created to display and edit (if field is writable) values
 * for that field in the data model.  Each user-interface component
 * is &quot;bound&quot; to the data model field using a <code>Binding</code>
 * instance, which handles the following tasks:
 * <ul>
 * <li>pull: load the current value from the data model into the
 * user-interface component, performing any necessary type
 * conversion from model type to the type required by the
 * user-interface component, which is often string.</li>
 * <li>validate: run any validation logic to determine if the value
 * contained in the user-interface component is valid for
 * the data model</li>
 * <li>push: copy the value contained in the user-interface component
 * to the data model, performing any necessary type conversion
 * from the user-interface value's type to the model's type.
 * Note that only valid values may be pushed.</li>
 * </ul>
 * To use the Form component, an application need only bind the form to
 * the desired fields on its data models, and by default the form will handle
 * creating the necessary user-interface components and bindings, and will
 * add those components to the form using a standard layout scheme.  Applications
 * generally should not need to interact directly with the component or binding
 * objects at all.
 * For example, the following code creates a form which can display/edit each
 * column in a TabularDataModel (and in the future, a RowSet):
 * <pre><code>
 *     TabularDataModel data = new TabularDataModel("http://foo.bar/appdata");
 *     JForm form = new JForm();
 *     try {
 *          form.bind(data); // creates components/bindings for all columns
 *     } catch (BindException e) {
 *     }
 * </code></pre>
 * Once the form is created and bound, as in the example above, the form
 * handles execution of the binding operations as the user interacts with the
 * user-interface components in the form.
 * </p>
 * <p>The following is an example which shows how a form is bound to
 * multiple data models:
 * <pre><code>
 *     Customer customer = new Customer();
 *     Cart cart = new Cart();
 * <p/>
 *     JForm form = new JForm();
 *     try {
 *         form.bind(customer, "firstName");  // binds to "firstName" property
 *         form.bind(customer, "lastName"); // binds to "lastName" property
 *         form.bind(customer, "address"); // binds to "address" property (nested bean)
 *         form.bind(cart, "items"); // binds to "items" property
 *     } catch (BindException e) {
 *     }
 * </code></pre>
 * </p>
 *
 * @author Amy Fowler
 * @version 1.0
 */

public class JForm extends JPanel {

    private FormFactory formFactory;
    private ArrayList bindings;
    private DataModel model;

    private boolean autoLayout = true;

    private HashMap models; // leak? should use weak ref?

    /**
     * Creates a new form component.
     */
    public JForm() {
        initActions();
    }

    /**
     * Creates a new form component and binds it to the specified data model.
     *
     * @param model data model whose fields should be bound in this form
     * @throws BindException if there were errors when binding to the data model
     */
    public JForm(DataModel model) throws BindException {
        initActions();
        bind(model);
        pull();
    }

    protected void initActions() {
        // Register the actions that this class can handle.
        ActionMap map = getActionMap();
        map.put("submit", new Actions("submit"));
        map.put("reset", new Actions("reset"));
    }

    /**
     * A small class which dispatches actions.
     * TODO: Is there a way that we can make this static?
     */
    private class Actions extends UIAction {
        Actions(String name) {
            super(name);
        }

        public void actionPerformed(ActionEvent evt) {
            if ("submit".equals(getName())) {
                doSubmit();
            } else if ("reset".equals(getName())) {
                doReset();
            }
        }
    }

    /**
     * Sets the &quot;autoLayout&quot; property.  The default is <code>true</code>.
     * If set to <code>false</code>, then the application must take responsibility
     * for adding the bound user-interface components to the form.
     *
     * @param autoLayout boolean value indicating whether or
     *                   not the components created and bound in this form are
     *                   automatically added to and layed out in the form
     * @see #getAutoLayout
     */
    public void setAutoLayout(boolean autoLayout) {
        this.autoLayout = autoLayout;
    }

    /**
     * @return boolean value indicating whether or
     *         not the components created and bound in this form are
     *         automatically added to and layed out in the form
     * @see #setAutoLayout
     */
    public boolean getAutoLayout() {
        return autoLayout;
    }

    /**
     * Sets the form factory for this form.
     *
     * @param factory FormFactory instance used to create the components and
     *                bindings for data model fields bound to the form
     */
    public void setFormFactory(FormFactory factory) {
        /**@todo aim: FormFactory could be replace by FormUI? */
        this.formFactory = factory;
    }

    /**
     * If the form factory was never explicitly set, this method will
     * return the default FormFactory instance obtained from
     * <code>FormFactory:getDefaultFormFactory()</code>.
     *
     * @return FormFactory instance used to create the components and
     *         bindings for data model fields bound to the form
     */
    public FormFactory getFormFactory() {
        if (formFactory == null) {
            return FormFactory.getDefaultFormFactory();
        }
        return formFactory;
    }

    //public void bind(RowSet rowset)
    //public void bind(RowSet rowset, String columnName)

    /**
     * Binds the form to each column in the specified TabularDataModel object.
     * The bind operation will create the best user-interface components to
     * display/edit the data model values based on each column's <code>MetaData</code>
     * object.  It will also create the bindings from those user-interface components
     * to the data model.  And finally, if &quot;autoLayout&quot; is <code>true</code>,
     * it will also add and layout those components within the form according
     * to the mechanism defined by the form factory.
     * @see MetaData
     * @param tabularData TabularDataModel being bound to the form
     * @return array of Binding instances created from bind operation
     * @throws BindException if there were errors when binding to the data model
     */
//    public Binding[] bind(TabularDataModel tabularData) throws BindException {
//        return bind(getDataModelWrapper(tabularData));
//    }

    /**
     * Binds the form to the specified column in the TabularDataModel object.
     * The bind operation will create the best user-interface component to
     * display/edit the data model value based on the column's <code>MetaData</code>
     * object.  It will also create the binding from the user-interface component
     * to the data model.  And finally, if &quot;autoLayout&quot; is <code>true</code>,
     * it will also add and layout the component within the form according
     * to the mechanism defined by the form factory.
     * @param tabularData TabularDataModel being bound to the form
     * @param columnName String containing the name of the column
     * @return Binding instance created from bind operation
     * @throws BindException if there were errors when binding to the data model
     */
//    public Binding bind(TabularDataModel tabularData, String columnName) throws BindException {
//        return bind(getDataModelWrapper(tabularData), columnName);
//    }

    /**
     * Binds the form to each property in the specified JavaBean object.
     * The bind operation will create the best user-interface components to
     * display/edit the data model values based on each property's description.
     * It will also create the bindings from those user-interface components
     * to the data model.  And finally, if &quot;autoLayout&quot; is <code>true</code>,
     * it will also add and layout those components within the form according
     * to the mechanism defined by the form factory.
     *
     * @param bean JavaBean object being bound to the form
     * @return array of Binding instances created from bind operation
     * @throws BindException if there were errors when binding to the data model
     */
    public Binding[] bind(Object bean) throws BindException {
        return bind(getDataModelWrapper(bean));
    }

    /**
     * Binds the form to the specified property in the JavaBean object.
     * The bind operation will create the best user-interface component to
     * display/edit the data model value based on the property's description.
     * It will also create the binding from the user-interface component
     * to the data model.  And finally, if &quot;autoLayout&quot; is <code>true</code>,
     * it will also add and layout the component within the form according
     * to the mechanism defined by the form factory.
     *
     * @param bean         JavaBean object being bound to the form
     * @param propertyName String containing the name of the property
     * @return Binding instance created from bind operation
     * @throws BindException if there were errors when binding to the data model
     */
    public Binding bind(Object bean, String propertyName) throws BindException {
        return bind(getDataModelWrapper(bean), propertyName);
    }

    /**
     * Binds the form to each field in the specified DataModel object.
     * The bind operation will create the best user-interface components to
     * display/edit the data model values based on each field's <code>MetaData</code>
     * object.  It will also create the bindings from those user-interface components
     * to the data model.  And finally, if &quot;autoLayout&quot; is <code>true</code>,
     * it will also add and layout those components within the form according
     * to the mechanism defined by the form factory.
     *
     * @param model DataModel object being bound to the form
     * @return array of Binding instances created from bind operation
     * @throws BindException if there were errors when binding to the data model
     */
    public Binding[] bind(DataModel model) throws BindException {
        String fieldNames[] = model.getFieldNames();
        Binding bindings[] = new Binding[fieldNames.length];
        for (int i = 0; i < fieldNames.length; i++) {
            bindings[i] = bind(model, fieldNames[i]);
        }
        return bindings;
    }

    /**
     * Binds the form to the specified field in the DataModel object.
     * The bind operation will create the best user-interface component to
     * display/edit the data model value based on the field's <code>MetaData</code>
     * object.  It will also create the binding from the user-interface component
     * to the data model.  And finally, if &quot;autoLayout&quot; is <code>true</code>,
     * it will also add and layout the component within the form according
     * to the mechanism defined by the form factory.
     *
     * @param model     DataModel object being bound to the form
     * @param fieldName String containing the name of the field
     * @return Binding instance created from bind operation
     * @throws BindException if there were errors when binding to the data model
     */
    public Binding bind(DataModel model, String fieldName) throws BindException {
        JComponent component = getFormFactory().
                createComponent(model.getMetaData(fieldName));

        if (component instanceof JForm) {
            DataModel nestedModel = (DataModel) model.getValue(fieldName);
            if (nestedModel != null) {
                JForm nestedForm = (JForm) component;
                nestedForm.bind(nestedModel);
                return bind(model, fieldName, nestedForm);
            } else {
                throw new BindException(model, fieldName);
            }
        } else {
            return bind(model, fieldName, component);
        }
    }

    /**
     * Binds the specified component to the field in the DataModel object.
     * This bind operation will attempt to create a Binding instance appropriate
     * for the specified component.  If it is unable to do so, a BindException
     * will be thrown.
     * This method can be used when the application wishes to control the
     * type of user-interface component used to display/edit the data model field.
     *
     * @param model     DataModel object being bound to the form
     * @param fieldName String containing the name of the field
     * @param component user-interface component being bound to the data model
     * @return Binding instance created from bind operation
     * @throws BindException if there were errors when binding to the data model
     */
    public Binding bind(DataModel model, String fieldName, JComponent component)
            throws BindException {
        Binding binding = getFormFactory().createBinding(model, fieldName, component);

        if (binding != null) {
            return bind(binding, component);
        } else {
            throw new BindException("could not create binding for component " +
                    component.getClass().getName());
        }
    }

    /**
     * Adds the specified binding to this form.  The user-interface
     * component is the one which should be added to the form if the &quot;autoLayout&quot;
     * property is <code>true</code>.  Note that this component may be different
     * from the component which is contained in the Binding instance.
     * This method is invoked by the other <code>bind</code> methods and is
     * typically not invoked directly by applications unless they require the
     * ability to create their own Binding objects.
     *
     * @param binding   Binding instance being added to this form
     * @param component user-interface component which should be added to the form
     * @return Binding instance created from bind operation
     * @throws BindException if there were errors when binding to the data model
     */
    public Binding bind(Binding binding, JComponent component) throws BindException {
        if (bindings == null) {
            bindings = new ArrayList();
        }
        bindings.add(binding);
        if (autoLayout) {
            DataModel model = binding.getDataModel();
            MetaData metaData = model.getMetaData(binding.getFieldName());
            getFormFactory().addComponent(this, component, metaData);
        }
        return binding;
    }

    /**
     * Removes the specified binding from this form.
     *
     * @param binding Binding instance being removed
     */
    public void unbind(Binding binding) {
        if (bindings != null) {
            bindings.remove(binding);
        }
    }

    /**
     * @return array containing all Binding objects currently in this form
     */
    public Binding[] getBindings() {
        if (bindings != null) {
            return (Binding[]) bindings.toArray(new Binding[0]);
        }
        return new Binding[0];
    }

    /**
     * For each binding defined in this form, pull the value from the data model
     * and load it into the user-interface component.
     *
     * @return boolean indicating whether or not the data model values were
     *         successfully pulled into the user-interface components
     */
    public boolean pull() {
        boolean result = true;
        Binding bindings[] = getBindings();
        for (int i = 0; i < bindings.length; i++) {
            if (!bindings[i].pull()) {
                result = false;
            }
        }
        return result;
    }

    /**
     * @return boolean value indicating whether or not all fields in this form
     *         have a valid state
     */
    public boolean isFormValid() {
        boolean result = true;
        Binding bindings[] = getBindings();
        ArrayList models = new ArrayList();
        for (int i = 0; i < bindings.length; i++) {
            DataModel bindingModel = bindings[i].getDataModel();
            if (!models.contains(bindingModel)) {
                models.add(bindingModel);
            }
            if (!bindings[i].isValid()) {
                result = false;
            }
        }
        if (result) {
            for (int i = 0; i < models.size(); i++) {
                DataModel model = (DataModel) models.get(i);
                Validator validators[] = model.getValidators();
                for (int j = 0; i < validators.length; j++) {
                    String error[] = new String[1];
                    /**@todo aim: where to put error? */
                    if (!validators[j].validate(model, getLocale(), error)) {
                        result = false;
                    }
                }
            }

        }
        return result;
    }

    /**
     * @return boolean value indicating whether or not any form field values
     *         have pending edits in the user-interface components since the
     *         last time values were pulled or pushed.
     */
    public boolean isModified() {
        boolean result = false;
        Binding bindings[] = getBindings();
        for (int i = 0; i < bindings.length; i++) {
            if (bindings[i].isModified()) {
                result = true;
            }
        }
        return result;
    }

    /**
     * Public callback for submit action.
     */
    public void doSubmit() {
        if (push()) {
            executeSubmit();
        } else {
            JOptionPane.showMessageDialog(JForm.this,
                    "Form contains invalid values.\nPlease correct values before submitting.",
                    "Form Submission Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Public callback for reset action.
     */
    public void doReset() {
        pull();
    }

    /**
     * If all form fields are in a valid state, push each
     * value contained in a user-interface component its associated data model.
     *
     * @return boolean indicating whether or not the form field values were
     *         successfully pushed to the data models
     * @see #isFormValid
     */
    public boolean push() {
        if (!isFormValid()) {
            return false;
        }
        boolean result = true;
        Binding bindings[] = getBindings();
        for (int i = 0; i < bindings.length; i++) {
            if (!bindings[i].push()) {
                result = false;
            }
        }
        return result;
    }

    /**
     * Invoked from the submit action if and only if
     * all form component values were successfully pushed to the
     * form's data model.
     */
    protected void executeSubmit() {
        JOptionPane.showMessageDialog(this,
                "Form submitted.\nThanks",
                "Form Submission", JOptionPane.PLAIN_MESSAGE);
    }

    private DataModel getDataModelWrapper(Object model) throws BindException {
        if (models == null) {
            models = new HashMap();
        }
        DataModel wrapper = (DataModel) models.get(model);
        if (wrapper == null) {
//            if (model instanceof TabularDataModel) {
//                wrapper = new TabularDataModelAdapter((TabularDataModel)model);
//            } else {
            try {
                wrapper = new JavaBeanDataModel(model);
            } catch (IntrospectionException e) {
                throw new BindException(model, e);
            }
//            }
            models.put(model, wrapper);
        }
        return wrapper;
    }
}
