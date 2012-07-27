package org.jdesktop.incubator.combobox;

import javax.swing.*;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

/**
 * Adapts any old ListModel into a functional ComboBoxModel.
 */

/*
 * Created by IntelliJ IDEA.
 * User: Richard Osbaldeston
 * Date: 27-Mar-2003
 * Time: 17:21:00
 */

public class ListModelComboBoxAdapter implements ComboBoxModel, ListModel {
    private AbstractListModel listModel;
    private Object selectedItem;

    public ListModelComboBoxAdapter(AbstractListModel listModel) {
        this.listModel = listModel;
    }

    public Object getSelectedItem() {
        return selectedItem;
    }

    public void setSelectedItem(Object selectedItem) {
        this.selectedItem = selectedItem;
        //PENDING gah! had to use AbstractListModel above as getListDataListeners() isnt exposed via ListModel
        fireContentsChanged(this, -1, -1);
    }

    public int getSize() {
        return listModel.getSize();
    }

    public Object getElementAt(int index) {
        return listModel.getElementAt(index);
    }

    public ListDataListener[] getListDataListeners() {
        return listModel.getListDataListeners();
    }

    public void addListDataListener(ListDataListener l) {
        listModel.addListDataListener(l);
    }

    public void removeListDataListener(ListDataListener l) {
        listModel.removeListDataListener(l);
    }

    //PENDING matching method was protected AbstractListModel too
    protected void fireContentsChanged(Object source, int index0, int index1) {
        ListDataEvent event = null;
        ListDataListener[] listeners = getListDataListeners();
        for (int i = listeners.length - 1; i > -1; i--) {
            if (event == null) {
                event = new ListDataEvent(source, ListDataEvent.CONTENTS_CHANGED, index0, index1);
            }
            listeners[i].contentsChanged(event);
        }
    }
}