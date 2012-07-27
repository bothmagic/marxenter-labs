/*
    Copyright (c) 2006  Adam Taft bobsledbob@dev.java.net
    Copyright (c) 2006  Sun Microsystems, Inc., 4150 Network Circle, Santa Clara, California 95054, U.S.A.
    All rights reserved.

    This library is free software; you can redistribute it and/or
    modify it under the terms of the GNU Lesser General Public
    License as published by the Free Software Foundation; either
    version 2.1 of the License, or (at your option) any later version.

    This library is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
    Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public
    License along with this library; if not, write to the Free Software
    Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
*/


package org.jdesktop.swingx;

import java.awt.Component;

import javax.swing.AbstractCellEditor;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;


public class JXPickerTableCellEditor extends AbstractCellEditor implements TableCellEditor {
	
	protected JXPicker picker;
	
	public JXPickerTableCellEditor(JXPicker picker) {
		this.picker = picker;
	}
	
	public Object getCellEditorValue() {
		return picker.getSelectedItem();
	}
	
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		table.requestFocusInWindow();
		picker.putClientProperty("TABLE_CELL_EDITOR", Boolean.TRUE);
		picker.setSelectedItem(value);
		picker.togglePopup();
		return picker;
	}
}
