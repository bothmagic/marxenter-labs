/*
 * Created by JFormDesigner on Tue Nov 20 14:52:03 CET 2007
 */

package org.jdesktop.swingx.demo;

import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.plaf.ComponentUI;
import javax.swing.text.JTextComponent;

/**
 * @author Peter Weishapl
 */
public class CustomizePanel extends JPanel {
	private JTextComponent field;
	
	public CustomizePanel() {
	}

	public JTextComponent getField() {
		return field;
	}

	public void setField(JTextComponent textComponent) {
		this.field = textComponent;
	}
	
	@Override
	public void updateUI() {
		super.updateUI();
		if(getField() != null){
			setField(getField());
		}
	}
	
	public void changed(){
		if(field == null) return;
		
		field.getParent().validate();
		field.invalidate();
		field.revalidate();
		field.repaint();
	}
}
