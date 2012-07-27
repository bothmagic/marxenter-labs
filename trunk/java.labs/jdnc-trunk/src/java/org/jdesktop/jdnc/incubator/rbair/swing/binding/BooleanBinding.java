/*
 * $Id: BooleanBinding.java 46 2004-09-08 17:33:01Z rbair $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */

package org.jdesktop.jdnc.incubator.rbair.swing.binding;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.ButtonModel;
import javax.swing.JComponent;
import javax.swing.JToggleButton;

import org.jdesktop.jdnc.incubator.rbair.swing.data.DataModel;


/**
 * Class which binds a component that supports setting a boolean
 * value (JCheckBox) to a data model field which is type Boolean.
 * Although this binding is most commonly used for checkboxes, it may
 * be used with any component that defines a ButtonModel to represent
 * its selected state.
 *
 * @author Amy Fowler
 * @version 1.0
 */
public class BooleanBinding extends AbstractBinding {
    private JComponent component;
    private ButtonModel buttonModel;

    public BooleanBinding(JToggleButton toggleButton,
                           DataModel dataModel, String fieldName) {
        // checkboxes don't need to be validated because they cannot have a null
        // value and type conversion should not be necessary.
        super(toggleButton, dataModel, fieldName, AbstractBinding.AUTO_VALIDATE_NONE);
        initModel(toggleButton.getModel());
    }

    public BooleanBinding(JComponent component, ButtonModel buttonModel,
                           DataModel dataModel, String fieldName) {
        super(component, dataModel, fieldName, AbstractBinding.AUTO_VALIDATE_NONE);
        initModel(buttonModel);
    }

    public JComponent getComponent() {
        return component;
    }

    protected void setComponent(JComponent component) {
        this.component = component;
    }

    protected Object getComponentValue(){
        return Boolean.valueOf(buttonModel.isSelected());
    }

    protected void setComponentValue(Object value) {
        if (value != null) {
            buttonModel.setSelected( ( (Boolean) value).booleanValue());
        } else {
            buttonModel.setSelected(false);
        }
    }

    private void initModel(ButtonModel model) {
        buttonModel = model;
        buttonModel.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (!pulling) {
                    setModified(true);
                }
            }
        });
    }

}