/**
 * 
 */
package org.jdesktop.swingx.demo;

import java.awt.Insets;
import java.text.ParseException;

import javax.swing.JFormattedTextField;

public class InsetsFormatter extends JFormattedTextField.AbstractFormatter {
	public Object stringToValue(String text) throws ParseException {
		String[] s = text.split(",");
		if (s.length == 4) {
			return new Insets(Integer.parseInt(s[0]), Integer.parseInt(s[1]), Integer.parseInt(s[2]), Integer
					.parseInt(s[3]));
		}
		return null;
	}

	public String valueToString(Object value) throws ParseException {
		if (value == null)
			return null;

		Insets i = (Insets) value;

		return i.top + "," + i.left + "," + i.bottom + "," + i.right;
	}
}