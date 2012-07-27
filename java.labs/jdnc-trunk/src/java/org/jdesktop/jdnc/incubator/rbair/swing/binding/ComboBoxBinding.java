/*
 * $Id: ComboBoxBinding.java 46 2004-09-08 17:33:01Z rbair $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */

package org.jdesktop.jdnc.incubator.rbair.swing.binding;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JComponent;

import org.jdesktop.jdnc.incubator.rbair.swing.data.DataModel;

/**
 * Class which binds a component that supports setting a one-of-many
 * value (JComboBox) to a data model field which is may be an arbitrary type.
 * @author Amy Fowler
 * @version 1.0
 */
public class ComboBoxBinding extends AbstractBinding {
    private JComboBox comboBox;
    /* Note: we cannot support binding to any component with a ComboBoxModel
     * because ComboBoxModel fires no event when the value changes!
     */
    public ComboBoxBinding(JComboBox combobox,
                           DataModel dataModel, String fieldName) {
        super(combobox, dataModel, fieldName, Binding.AUTO_VALIDATE_NONE);
    }

    public JComponent getComponent() {
        return comboBox;
    }

    protected void setComponent(JComponent component) {
        comboBox = (JComboBox) component;
        comboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!pulling) {
                    setModified(true);
                }
            }
        });
    }

    protected Object getComponentValue(){
        return comboBox.getSelectedItem();
    }

    protected void setComponentValue(Object value) {
        comboBox.setSelectedItem(value);
    }

}
