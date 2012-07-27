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


package demo;

import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

import org.jdesktop.swingx.JXPickerTableCellEditor;
import org.jdesktop.swingx.calendar.DateSpan;

public class JXPickerDemo {
	
	public static void main(String[] args) {		
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				
				final JFrame frame = new JFrame("JXPicker Test");
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);				
				
				final JXPickerDemoForm form = new JXPickerDemoForm();
				
				final ActionListener lookAndFeelListener = new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						String cmd = e.getActionCommand();
						try {
							if (cmd.startsWith("Metal")) {
								UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
							} else if (cmd.startsWith("System")) {
								UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
							} else if (cmd.startsWith("Motif")) {
								UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
							} else if (cmd.startsWith("JGoodies")) {
								UIManager.setLookAndFeel("com.jgoodies.looks.plastic.PlasticXPLookAndFeel");
							}
							SwingUtilities.updateComponentTreeUI(frame);
							frame.pack();
						} catch (Exception e2brute) {
							e2brute.printStackTrace();
						}
					}
				};
				
				form.lookAndFeelMetal.addActionListener(lookAndFeelListener);
				form.lookAndFeelSystem.addActionListener(lookAndFeelListener);
				form.lookAndFeelMotif.addActionListener(lookAndFeelListener);
				form.lookAndFeelGoodies.addActionListener(lookAndFeelListener);
				
				
				form.list.addListSelectionListener(new ListSelectionListener() {
					public void valueChanged(ListSelectionEvent e) {
						if (! e.getValueIsAdjusting()) {
							form.listPicker.setSelectedItem(form.list.getSelectedValue());
							form.listPicker.togglePopup();
						}
					}
				});
				
				
				form.tree.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						if (e.getClickCount() != 2) return;
						Object o = form.tree.getLastSelectedPathComponent();
						if (! (o instanceof DefaultMutableTreeNode)) {
							return;
						}
						DefaultMutableTreeNode node = (DefaultMutableTreeNode) o;
						if (node.isLeaf()) {
							form.treePicker.setSelectedItem(node.getUserObject());
							form.treePicker.togglePopup();
						}
					}
				});
				
				
				JButton dotDot = new JButton("...");
				dotDot.setMargin(new Insets(2,2,2,2));
				form.datePicker.setButton(dotDot);
				form.monthView.setSelectedDateSpan(new DateSpan(new Date(), new Date()));
				form.monthView.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						if (e.getClickCount() >= 2 && form.monthView.getSelectedDateSpan() != null) {
							form.datePicker.setSelectedItem(form.monthView.getSelectedDateSpan().getStartAsDate());
							form.datePicker.togglePopup();							
						}
					}
				});
				
								
				form.table.setPreferredScrollableViewportSize(form.table.getPreferredSize());
				form.tableEditorTree.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						if (e.getClickCount() != 2) return;
						Object o = form.tableEditorTree.getLastSelectedPathComponent();
						if (! (o instanceof DefaultMutableTreeNode)) {
							return;
						}
						DefaultMutableTreeNode node = (DefaultMutableTreeNode) o;
						if (node.isLeaf()) {
							form.tableEditorPicker.setSelectedItem(node.getUserObject());
							form.tableEditorPicker.togglePopup();
						}
					}
				});
				form.table.getColumnModel().getColumn(0).setCellEditor(new JXPickerTableCellEditor(form.tableEditorPicker));

				
				frame.setContentPane(form);

				
				frame.pack();
				frame.setLocationRelativeTo(null);
				frame.setVisible(true);
			}
		});
	}
	
}
