package org.jdesktop.swingx;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.plaf.basic.BasicTabbedPaneUI;

public class TestActionTab {

	public static void main(String[] args) {
		JFrame f = new JFrame();
		f.getContentPane().setLayout(new BorderLayout());
		f.getContentPane().add(new JPanel(), BorderLayout.CENTER);
		
		FluentToolbar ft = new FluentToolbar();
		ActionTab a1 = new ActionTab();
		ActionGroup g1 = new ActionGroup(new java.awt.Dimension(120, 100));
		g1.setLayout(new GridBagLayout());
		GridBagConstraints gc = new GridBagConstraints();
		gc.insets = new Insets(5, 5, 5, 5);
		gc.weightx = 1;
		gc.weighty = 1;
		gc.fill = GridBagConstraints.BOTH;
		
		g1.addAction("a1", new AbstractAction("a") {public void actionPerformed(ActionEvent e) {} } , gc, 0, 0);
		gc.gridx = 1;
		g1.addAction("a2", new AbstractAction("b") {public void actionPerformed(ActionEvent e) {} } , gc, 0, 0);
		gc.gridx = 0;
		gc.gridy = 1;
		g1.addAction("a3", new AbstractAction("c") {public void actionPerformed(ActionEvent e) {} } , gc, 0, 0);
		gc.gridx = 1;
		g1.addAction("a4", new AbstractAction("d") {public void actionPerformed(ActionEvent e) {} } , gc, 0, 0);
		a1.addActionGroup(g1, "t");
		ft.addActionTab(a1, "test");
		ft.addActionTab(new ActionTab(), "2");
		ft.addActionTab(new ActionTab(), "Blaat");

		ActionGroup g2 = new ActionGroup(new Dimension(200, 100));
		g2.setLayout(new GridBagLayout());
		gc.gridx = 0;
		gc.gridy = 0;
		gc.gridwidth = 3;
		g2.add(new JTextField("1"), gc);
		//g2.addAction("a1", new AbstractAction("1") {public void actionPerformed(ActionEvent e) {} } , gc, 0, 0);
		gc.gridwidth = 1;
		gc.gridy = 1;
		g2.addAction("a2", new AbstractAction("2") {public void actionPerformed(ActionEvent e) {} } , gc, 0, 0);
		gc.gridx = 1;
		g2.addAction("a3", new AbstractAction("3") {public void actionPerformed(ActionEvent e) {} } , gc, 0, 0);
		gc.gridx = 2;
		g2.addAction("a4", new AbstractAction("4") {public void actionPerformed(ActionEvent e) {} } , gc, 0, 0);
		a1.addActionGroup(g2, "F");
		
		
		f.getContentPane().add(ft, BorderLayout.NORTH);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setSize(800, 600);
		f.setVisible(true);
	}
	
}
