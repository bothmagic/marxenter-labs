package org.jdesktop.incubator.combobox;

import org.jdesktop.incubator.list.GenericListModel;

import javax.swing.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

/*
 * Created by IntelliJ IDEA.
 * User: Richard Osbaldeston
 * Date: 12-Mar-2006
 * Time: 17:21:55
 */

//TODO mirror list models by creating EditableComboBoxModel/GenericEditableComboBoxModel?

public class GenericComboBoxModel<T> extends GenericListModel<T> implements ComboBoxModel {
    private Object selectedItem;

    public GenericComboBoxModel() {
    }

    public GenericComboBoxModel(T... values) {
        this(values != null ? Arrays.asList(values) : Collections.<T>emptyList());
    }

    public GenericComboBoxModel(Collection<T> values) {
        super(values);
        if (getSize() > 0) {
            setSelectedItem(getElementAt(0));
        }
    }

    @SuppressWarnings("SuspiciousMethodCalls")
    public void setValues(Collection<T> values) {
        super.setValues(values);
        if (!this.values.contains(getSelectedItem())) {
            //NB if combo is editable does changing the selected value make sense here?
            setSelectedItem(getSize() > 0 ? getElementAt(0) : null);
        }
    }

    //TODO generify selectedItem accessors? how? (why doesn't T satisfy the interface given its just an Object?)

    public Object getSelectedItem() {
        return this.selectedItem;
    }

    @SuppressWarnings("SuspiciousMethodCalls")
    public void setSelectedItem(Object value) {
        if (value != null && !value.equals(getSelectedItem())) {
            this.selectedItem = value;
            fireContentsChanged(this, -1, -1);  //TODO missing an event? ..a sensible event?
        } else if (value == null && getSelectedItem() != null) {
            this.selectedItem = value;
            fireContentsChanged(this, -1, -1);  //TODO missing an event? ..a sensible event?
        }
    }
}
