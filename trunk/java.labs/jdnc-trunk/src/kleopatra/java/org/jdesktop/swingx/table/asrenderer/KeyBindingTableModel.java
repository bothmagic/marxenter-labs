/*
 * $Id: KeyBindingTableModel.java 938 2006-12-07 15:25:54Z kleopatra $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.table.asrenderer;

import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.KeyStroke;
import javax.swing.table.AbstractTableModel;

/**
 * Convenience TableMap showing all entries of a ActionMap.
 * 
 * @author Jeanette Winzenburg
 */
public class KeyBindingTableModel extends AbstractTableModel {

    private ActionMap actionMap;
    private InputMap inputMap;

    public KeyBindingTableModel(InputMap inputMap, ActionMap actionMap) {
        this.actionMap = actionMap;
        this.inputMap = inputMap;
    }

    public int getRowCount() {
        return inputMap.allKeys().length;
    }

    public int getColumnCount() {
        return 3;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        KeyStroke keyStroke = inputMap.allKeys()[rowIndex];
        Object actionKey = inputMap.get(keyStroke);
        Action action = actionMap.get(actionKey);
        switch (columnIndex) {
        case 0:
            return keyStroke;
        case 2:
            return action != null ? action.getValue(Action.ACTION_COMMAND_KEY) : null;
        case 1:
            return action != null ? action.getValue(Action.NAME) : null;
        default:
            return null;
        }
    }

    public String getColumnName(int column) {
        switch (column) {
        case 0:
            return "Bound KeyStroke";
        case 1:
            return "Action Command";
        case 2:
            return "Action Name";
        default:
            return "Column " + column;
        }
    }

}