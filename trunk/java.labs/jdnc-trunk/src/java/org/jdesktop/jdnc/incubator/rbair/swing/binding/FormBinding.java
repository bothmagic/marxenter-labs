/*
 * $Id: FormBinding.java 46 2004-09-08 17:33:01Z rbair $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */

package org.jdesktop.jdnc.incubator.rbair.swing.binding;

import javax.swing.JComponent;

import org.jdesktop.jdnc.incubator.rbair.swing.data.DataModel;
import org.jdesktop.jdnc.incubator.rbair.swing.form.JForm;

/**
 * Class which binds a JForm component to a data model field which is
 * type DataModel in order to support nested data models.
 *
 * @author Amy Fowler
 * @version 1.0
 */

public class FormBinding extends AbstractBinding {
    protected JForm form;

    public FormBinding(JForm form, DataModel dataModel, String fieldName) {
        super(form, dataModel, fieldName, Binding.AUTO_VALIDATE_NONE);
    }

    public boolean pull() {
        return form.pull();
    }

    public boolean isModified() {
        return form.isModified();
    }

    /**
     *
     */
    public boolean isValid() {
        return form.isFormValid();
    }

    public JComponent getComponent() {
        return form;
    }

    protected void setComponent(JComponent component) {
        form = (JForm)component;
    }

    protected Object getComponentValue() {
        // no-op
        return null;
    }

    protected void setComponentValue(Object value) {
        // no-op
    }

    public boolean push() {
        return form.push();
    }
}
