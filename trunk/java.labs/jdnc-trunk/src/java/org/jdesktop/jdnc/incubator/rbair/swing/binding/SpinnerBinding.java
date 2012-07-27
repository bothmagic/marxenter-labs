/*
 * $Id: SpinnerBinding.java 46 2004-09-08 17:33:01Z rbair $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */

package org.jdesktop.jdnc.incubator.rbair.swing.binding;

import javax.swing.JComponent;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.jdesktop.jdnc.incubator.rbair.swing.data.DataModel;

/**
 * Class which binds a component that supports setting a value within
 * a sequence of values (JSpinner) to a field in a data model.
 * Although this binding is most commonly used for spinners, it may
 * be used with any component that defines a SpinnerModel to represent
 * its current value.

 * @author Amy Fowler
 * @version 1.0
 */
public class SpinnerBinding extends AbstractBinding {
    private JComponent component;
    private SpinnerModel spinnerModel;

    public SpinnerBinding(JSpinner spinner,
                            DataModel dataModel, String fieldName) {
        super(spinner, dataModel, fieldName, AbstractBinding.AUTO_VALIDATE);
        initModel(spinner.getModel());
    }

    public SpinnerBinding(JSpinner spinner,
                            DataModel dataModel, String fieldName,
                           int validationPolicy) {
        super(spinner, dataModel, fieldName, validationPolicy);
        initModel(spinner.getModel());
    }

    public SpinnerBinding(JComponent component, SpinnerModel spinnerModel,
                          DataModel dataModel, String fieldName,
                          int validationPolicy) {
        super(component, dataModel, fieldName, validationPolicy);
        initModel(spinnerModel);
    }

    public JComponent getComponent() {
        return component;
    }

    protected void setComponent(JComponent component) {
        this.component = component;
    }

    protected Object getComponentValue(){
        return spinnerModel.getValue();
    }

    protected void setComponentValue(Object value) {
        if (value != null) {
            spinnerModel.setValue(value);
        }
    }

    private void initModel(SpinnerModel spinnerModel) {
        this.spinnerModel = spinnerModel;
        spinnerModel.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                if (!pulling) {
                    setModified(true);
                }
            }
        });
    }

}