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

import javax.swing.JLabel;
import javax.swing.LookAndFeel;

public class DefaultPickerRenderer implements PickerRenderer {
	public Component getRendererComponent(JXPicker picker, Object value, boolean isSelected) {
		JLabel label = new JLabel();
		label.setOpaque(true);
		if (value != null) {
			label.setText(value.toString());
		}
		if (isSelected) {
			LookAndFeel.installColorsAndFont(label, "Picker.selectionBackground", "Picker.selectionForeground", "Picker.font");
		} else {
			LookAndFeel.installColorsAndFont(label, "Picker.background", "Picker.foreground", "Picker.font");
		}
		if (picker.getClientProperty("TABLE_CELL_EDITOR") != null && picker.getBorder() != null) {
			picker.setBorder(null);
		}
		return label;
	}
}
