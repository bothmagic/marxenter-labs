package org.jdesktop.swingx;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.ListModel;

import org.jdesktop.swingx.ReorderingTransferHandler.ModelUpdater;

public final class ModelUpdaters {
    public static final ModelUpdater DEFAULT_LIST_MODEL_UPDATER = new ModelUpdater() {
        @Override
        public void update(JList list, int originationIndex, int destinationIndex) {
            ListModel model = list.getModel();
            
            if (!(model instanceof DefaultListModel)) {
                throw new IllegalArgumentException("list with invalid model");
            }
            
            DefaultListModel dlm = (DefaultListModel) model;
            Object o = dlm.remove(originationIndex);
            dlm.insertElementAt(o, destinationIndex == -1 ? dlm.getSize() : destinationIndex);
        }
    };
}
