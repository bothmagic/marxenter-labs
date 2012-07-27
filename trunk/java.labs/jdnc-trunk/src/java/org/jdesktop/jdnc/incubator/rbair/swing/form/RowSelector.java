/*
 * $Id: RowSelector.java 46 2004-09-08 17:33:01Z rbair $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */

package org.jdesktop.jdnc.incubator.rbair.swing.form;

import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.jdesktop.jdnc.incubator.rbair.swing.JXTable;
import org.jdesktop.jdnc.incubator.rbair.swing.data.DataModel;

/**
 * @author Amy Fowler
 */

public class RowSelector {
    private DataModel dataModel;
    private JComponent component;

    public RowSelector(JTable table, DataModel dataModel) {
        this(table.getSelectionModel(), dataModel);
        component = table;
    }

    public RowSelector(JList list, DataModel dataModel) {
        this(list.getSelectionModel(), dataModel);
        component = list;
    }

    public RowSelector(ListSelectionModel selectModel, DataModel dataModel) {
        this.dataModel = dataModel;
        if (dataModel.getRecordCount() > 0) {
            selectModel.setLeadSelectionIndex(0);
            selectModel.setAnchorSelectionIndex(0);
            dataModel.setRecordIndex(0);
        }
        selectModel.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                ListSelectionModel selectionModel = (ListSelectionModel)e.getSource();
                int selectionIndex = selectionModel.getLeadSelectionIndex();
                if (component != null && selectionModel.isSelectedIndex(selectionIndex)) {
                    if (component instanceof JXTable) {
                        selectionIndex = ((JXTable)component).convertRowIndexToModel(
                            selectionIndex);
                    }
                    RowSelector.this.dataModel.setRecordIndex(selectionIndex);
                }
            }
        });
    }
}