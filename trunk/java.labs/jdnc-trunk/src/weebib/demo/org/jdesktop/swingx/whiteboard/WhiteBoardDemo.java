/*
 * $Id: WhiteBoardDemo.java 830 2006-08-02 00:39:24Z weebib $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */

package org.jdesktop.swingx.whiteboard;

import org.jdesktop.swingx.whiteboard.tools.*;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * A demo class which allows the interaction with an EditableWhiteBoard given a list of ITool instances.
 */
public class WhiteBoardDemo extends JPanel {
	private EditableWhiteBoard theEditableWhiteBoard;
	private JList theToolList;
	private JPanel theParameterPanel;

	public WhiteBoardDemo() {
		super(new BorderLayout(5, 5));
		setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		JToolBar toolBar = new JToolBar();
		toolBar.setFloatable(false);
		toolBar.add(new AbstractAction("Clear") {
			public void actionPerformed(ActionEvent e) {
				theEditableWhiteBoard.clearWhiteBoard();
			}
		});
		add(toolBar, BorderLayout.NORTH);

		theToolList = new JList(new ITool[]{
				new RectangleTool(),
				new SquareTool(),
				new EllipseTool(),
				new CircleTool(),
				new PencilTool(),
				new PolygonTool(),
				new LineTool()});
		theToolList.setCellRenderer(new DefaultListCellRenderer() {
			public Component getListCellRendererComponent(JList list, Object value, int index,
														  boolean isSelected, boolean cellHasFocus) {
				return super.getListCellRendererComponent(list, ((ITool)value).getName(), index,
														  isSelected, cellHasFocus);
			}
		});
		theToolList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		theToolList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				updateTool();
			}
		});
		theParameterPanel = new JPanel(new BorderLayout());
		JSplitPane leftSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
											  new JScrollPane(theToolList),
											  new JScrollPane(theParameterPanel));
		LookAndFeel.uninstallBorder(leftSplitPane);
		theEditableWhiteBoard = new EditableWhiteBoard(new Dimension(800, 600));
		JSplitPane mainSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
												  leftSplitPane,
												  new JScrollPane(theEditableWhiteBoard));
		LookAndFeel.uninstallBorder(mainSplitPane);
		add(mainSplitPane,
			BorderLayout.CENTER);
	}

	private void updateTool() {
		ITool tool = (ITool)theToolList.getSelectedValue();
		theEditableWhiteBoard.setEditingTool(tool);
		theParameterPanel.removeAll();
		if (tool != null) {
			JPanel toolPanel = tool.getParameterPanel();
			if (toolPanel != null) {
				theParameterPanel.add(toolPanel, BorderLayout.CENTER);
			}
		}
		theParameterPanel.revalidate();
		theParameterPanel.repaint();
	}

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				JFrame frame = new JFrame("WhiteBoard demo");
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setContentPane(new WhiteBoardDemo());
				frame.pack();
				frame.setLocationRelativeTo(null);
				frame.setVisible(true);
			}
		});
	}
}
