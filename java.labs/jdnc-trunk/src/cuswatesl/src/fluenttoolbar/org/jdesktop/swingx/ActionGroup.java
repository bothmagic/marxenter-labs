package org.jdesktop.swingx;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.HashMap;
import java.util.Map;

import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;

public class ActionGroup extends JComponent {

	private Dimension size;
	private Map<String, JButton> actionButtons = new HashMap<String, JButton>();
	private Map<String, Object> layoutConstraints = new HashMap<String, Object>();
	
	public ActionGroup(Dimension size) {
		this.size = size;
		setBorder(BorderFactory.createEtchedBorder());
		setLayout(new BorderLayout());
	}
	
	@Override
	public Dimension getMinimumSize() {
		return this.size;
	}
	
	@Override
	public Dimension getMaximumSize() {
		return this.size;
	}
	
	@Override
	public Dimension getPreferredSize() {
		return this.size;
	}
	
	public void addAction(String name, Action action, Object layoutConstraint, int htextPosition, int vtextPosition) {
		getActionMap().put(name, action);
		this.actionButtons.put(name, new JButton(action));
		this.actionButtons.get(name).setHorizontalTextPosition(htextPosition);
		this.actionButtons.get(name).setVerticalTextPosition(vtextPosition);
		this.layoutConstraints.put(name, layoutConstraint);
		add(this.actionButtons.get(name), layoutConstraint);
	}

}
