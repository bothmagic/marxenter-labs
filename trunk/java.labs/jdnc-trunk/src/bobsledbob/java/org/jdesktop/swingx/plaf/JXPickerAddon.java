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


package org.jdesktop.swingx.plaf;

import java.awt.Color;
import java.awt.Font;
import java.util.Arrays;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.plaf.BorderUIResource;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.FontUIResource;

import org.jdesktop.swingx.JXPicker;
import org.jdesktop.swingx.plaf.AbstractComponentAddon;
import org.jdesktop.swingx.plaf.LookAndFeelAddons;

public class JXPickerAddon extends AbstractComponentAddon {
	
	public JXPickerAddon() {
		super("JXPicker");
	}
		
	
	@Override
	protected void addBasicDefaults(LookAndFeelAddons addon, List<Object> defaults) {
		super.addBasicDefaults(addon, defaults);
		
		Color background = UIManager.getColor("ComboBox.background");
		if (background == null) {
			background = new ColorUIResource(255,255,255);
		}
		
		Color foreground = UIManager.getColor("ComboBox.foreground");
		if (foreground == null) {
			foreground = new ColorUIResource(0,0,0);
		}
		
		Color selectionBackground = UIManager.getColor("ComboBox.selectionBackground");
		if (selectionBackground == null) {
			selectionBackground = new ColorUIResource(49,106,197);
		}
		
		Color selectionForeground = UIManager.getColor("ComboBox.selectionForeground");
		if (selectionForeground == null) {
			selectionForeground = new ColorUIResource(0,0,0);
		}
		
		Font font = UIManager.getFont("ComboBox.font");
		if (font == null) {
			font = new FontUIResource("Dialog", Font.BOLD, 12);
		}
		
		Border border = UIManager.getBorder("ComboBox.border");
		if (border == null) {
			border = BorderFactory.createLineBorder(new Color(127,157,185));
		}
		border = new BorderUIResource(border);
		
		
		/*
		System.out.println("background: " + background);
		System.out.println("foreground: " + foreground);
		System.out.println("selectionBackground: " + selectionBackground);
		System.out.println("selectionForeground: " + selectionForeground);
		System.out.println("font: " + font);
		System.out.println("border: " + border);
		*/

		
		defaults.addAll(Arrays.asList(new Object[] {
				JXPicker.uiClassID,
				"org.jdesktop.jdnc.incubator.bobsledbob.picker.plaf.PickerUI",
				"Picker.background",
				background,
				"Picker.foreground",
				foreground,
				"Picker.selectionBackground",
				selectionBackground,
				"Picker.selectionForeground",
				selectionForeground,
				"Picker.font",
				font,
				"Picker.border",
				border
		}));
		
	}
	
	@Override
	protected void addWindowsDefaults(LookAndFeelAddons addon, List<Object> defaults) {
		super.addWindowsDefaults(addon, defaults);
		
		Color background = UIManager.getColor("ComboBox.background");
		if (background == null) {
			background = new ColorUIResource(255,255,255);
		}
		
		Border border = UIManager.getBorder("ComboBox.border");
		if (border == null) {
			border = BorderFactory.createLineBorder(new Color(127,157,185));
		}
		border = BorderFactory.createCompoundBorder(border, BorderFactory.createLineBorder(background, 2));
		border = new BorderUIResource(border);
		
		defaults.addAll(Arrays.asList(new Object[] {
				"Picker.border",
				border
		}));
		
	}

}
