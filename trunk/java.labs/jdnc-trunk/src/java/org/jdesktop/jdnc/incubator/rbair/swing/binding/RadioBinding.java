/*
 * $Id: RadioBinding.java 46 2004-09-08 17:33:01Z rbair $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */

package org.jdesktop.jdnc.incubator.rbair.swing.binding;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComponent;

import org.jdesktop.jdnc.incubator.rbair.swing.JXRadioGroup;
import org.jdesktop.jdnc.incubator.rbair.swing.data.DataModel;


/**
 * Class which binds a component that supports setting a one-of-many
 * value (JXRadioGroup) to a data model field which is may be an arbitrary type.
 * @author Amy Fowler
 * @version 1.0
 */
public class RadioBinding extends AbstractBinding {
    private JXRadioGroup radioGroup;

    public RadioBinding(JXRadioGroup radioGroup,
                           DataModel dataModel, String fieldName) {
        super(radioGroup, dataModel, fieldName, Binding.AUTO_VALIDATE_NONE);
    }

    public JComponent getComponent() {
        return radioGroup;
    }

    protected void setComponent(JComponent component) {
        radioGroup = (JXRadioGroup) component;
        radioGroup.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!pulling) {
                    setModified(true);
                }
            }
        });
    }

    protected Object getComponentValue(){
        return radioGroup.getSelectedValue();
    }

    protected void setComponentValue(Object value) {
        radioGroup.setSelectedValue(value);
    }

}
