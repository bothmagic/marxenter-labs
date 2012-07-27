/*
 * $Id: JNForm.java 46 2004-09-08 17:33:01Z rbair $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */

package org.jdesktop.jdnc.incubator.rbair;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;

import javax.swing.Action;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.border.EmptyBorder;

import org.jdesktop.jdnc.incubator.rbair.swing.actions.BoundAction;
import org.jdesktop.jdnc.incubator.rbair.swing.binding.BindException;
import org.jdesktop.jdnc.incubator.rbair.swing.data.DataModel;
import org.jdesktop.jdnc.incubator.rbair.swing.form.JForm;

/**
 * @author Amy Fowler
 * @version 1.0
 *
 * @javabean.class
 *    displayName="Form Component"
 *    name="JNForm"
 *    shortDesctiption="A form control"
 */
public class JNForm extends JNComponent {
    private JForm form;
    private JComponent buttonPanel;
    private Component trailGlue;

    public JNForm() {
        super();
        setLayout(new BorderLayout());

        form = new JForm();
        add(BorderLayout.CENTER, form);

        buttonPanel = createButtonPanel();
        addAction(form.getActionMap().get("reset"));
        addAction(form.getActionMap().get("submit"));
        add(BorderLayout.SOUTH, buttonPanel);

        /**
        Icon[] icons = new Icon[] {
            new ImageIcon(klass.getResource("resources/wellTopLeft.gif")),
            new ImageIcon(klass.getResource("resources/wellTop.gif")),
            new ImageIcon(klass.getResource("resources/wellTopRight.gif")),
            new ImageIcon(klass.getResource("resources/wellRight.gif")),
            new ImageIcon(klass.getResource("resources/wellBottomRight.gif")),
            new ImageIcon(klass.getResource("resources/wellBottom.gif")),
            new ImageIcon(klass.getResource("resources/wellBottomLeft.gif")),
            new ImageIcon(klass.getResource("resources/wellLeft.gif")),
        };
        setBorder(new MatteBorderExt(14, 14, 14, 14, icons));
            **/

    }

    public JForm getForm() {
        return form;
    }

    /**
     * Binds the form to each column in the specified TabularDataModel object.
     * The bind operation will create the best user-interface components to
     * display/edit the data model values based on each column's <code>MetaData</code>
     * object.  And finally, if &quot;autoLayout&quot; is <code>true</code>,
     * it will also add and layout those components within the form.
     * @see MetaData
     * @param tabularData TabularDataModel being bound to the form
     * @throws BindException if there were errors when binding to the data model
     */
//    public void bind(TabularDataModel tabularData) throws BindException {
//        form.bind(tabularData);
//    }

    /**
     * Binds the form to the specified column in the TabularDataModel object.
     * The bind operation will create the best user-interface component to
     * display/edit the data model value based on the column's <code>MetaData</code>
     * object.  If &quot;autoLayout&quot; is <code>true</code>,
     * it will also add and layout the component within the form.
     * @param tabularData TabularDataModel being bound to the form
     * @param columnName String containing the name of the column
     * @throws BindException if there were errors when binding to the data model
     */
//    public void bind(TabularDataModel tabularData, String columnName) throws BindException {
//        form.bind(tabularData, columnName);
//    }

    /**
     * Binds the form to each property in the specified JavaBean object.
     * The bind operation will create the best user-interface components to
     * display/edit the data model values based on each property's description.
     * If &quot;autoLayout&quot; is <code>true</code>,
     * it will also add and layout those components within the form.
     * @param bean JavaBean object being bound to the form
     * @throws BindException if there were errors when binding to the data model
     */
    public void bind(Object bean) throws BindException {
        form.bind(bean);
    }

    /**
     * Binds the form to the specified property in the JavaBean object.
     * The bind operation will create the best user-interface component to
     * display/edit the data model value based on the property's description.
     * If &quot;autoLayout&quot; is <code>true</code>,
     * it will also add and layout the component within the form.
     * @param bean JavaBean object being bound to the form
     * @param propertyName String containing the name of the property
     * @throws BindException if there were errors when binding to the data model
     */
    public void bind(Object bean, String propertyName) throws BindException {
        form.bind(bean, propertyName);
    }

    /**
     * Binds the form to each field in the specified DataModel object.
     * The bind operation will create the best user-interface components to
     * display/edit the data model values based on each field's <code>MetaData</code>
     * object.  If &quot;autoLayout&quot; is <code>true</code>,
     * it will also add and layout those components within the form.
     * @param model DataModel object being bound to the form
     * @throws BindException if there were errors when binding to the data model
     */
    public void bind(DataModel model) throws BindException {
        form.bind(model);
    }

    /**
     * Binds the form to the specified field in the DataModel object.
     * The bind operation will create the best user-interface component to
     * display/edit the data model value based on the field's <code>MetaData</code>
     * object.  If &quot;autoLayout&quot; is <code>true</code>,
     * it will also add and layout the component within the form.
     * @param model DataModel object being bound to the form
     * @param fieldName String containing the name of the field
     * @throws BindException if there were errors when binding to the data model
     */
    public void bind(DataModel model, String fieldName) throws BindException {
        form.bind(model, fieldName);
    }


    public JButton addAction(Action action) {
        JButton button = new JButton(action);
        button.setBackground(getBackground());

        if (action instanceof BoundAction) {
            // XXX - msd Action method registraion.
            // This isn't general enough. Ideally, the
            // Reset and Submit actions should know how to
            // register themselves.
            BoundAction ba = (BoundAction) action;
            Object id = ba.getActionCommand();
            if ("submit".equals(id)) {
                ba.registerCallback(form, "doSubmit");
            }
            else if ("reset".equals(id)) {
                ba.registerCallback(form, "doReset");
            }
        }
        addToButtonPanel(button);
        return button;
    }

    /**
     * @javabean.property
     *     shortDescription="Sets the background color of this component"
     */
    public void setBackground(Color background) {
        super.setBackground(background);
        if (form != null) {
            form.setBackground(background);
        }
        if (buttonPanel != null) {
            buttonPanel.setBackground(background);
            Component children[] = buttonPanel.getComponents();
            for(int i = 0; i < children.length; i++) {
                children[i].setBackground(background);
            }
        }
    }

    protected JComponent createButtonPanel() {
        Box box = Box.createHorizontalBox();
        box.setBorder(new EmptyBorder(6,6,6,6));
        box.add(Box.createHorizontalGlue());
        return box;
    }

    protected void addToButtonPanel(JComponent component) {
        int childCount = buttonPanel.getComponentCount();
        // if we have more than one child, then a component
        // was already added, so peel off the trailing glue
        // and add a strut, then component, then trailing glue
        if (childCount > 1) {
            buttonPanel.remove(trailGlue);
            buttonPanel.add(Box.createHorizontalStrut(20));
        }
        buttonPanel.add(component);
        if (trailGlue == null) {
            trailGlue = Box.createHorizontalGlue();
        }
        buttonPanel.add(trailGlue);
    }

}
