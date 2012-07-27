package org.jdesktop.swingx;

import java.awt.FlowLayout;

import javax.swing.JComponent;

public class ActionTab extends JComponent {

	public ActionTab() {
		setLayout(new FlowLayout(FlowLayout.LEFT));
	}
	
	public void addActionGroup(ActionGroup ag, String groupName) {
		this.add(ag);
	}
	
	
}
