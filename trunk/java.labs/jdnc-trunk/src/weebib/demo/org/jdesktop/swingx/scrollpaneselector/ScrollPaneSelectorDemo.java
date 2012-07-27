/*
 * $Id: ScrollPaneSelectorDemo.java 828 2006-07-29 01:56:06Z weebib $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */

package org.jdesktop.swingx.scrollpaneselector;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Demo class for ScrollPaneSelector widget.
 * Users may install/uninstall the selector, toggle the scrollPane's orientation and choose a different viewport view
 *
 * @author weebib
 */
public class ScrollPaneSelectorDemo {
	private static class BoolRef {
		private boolean theValue;

		public BoolRef(boolean aValue) {
			theValue = aValue;
		}

		public boolean getValue() {
			return theValue;
		}

		public void toggle() {
			theValue = !theValue;
		}
	}

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				JFrame frame = new JFrame(ScrollPaneSelector.class.getName());
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

				JPanel mainPanel = new JPanel(new BorderLayout());

				final JLabel imageComponent = new JLabel(new ImageIcon(ScrollPaneSelectorDemo.class.getResource(
						"/org/jdesktop/swingx/scrollpaneselector/stonehenge-wallpaper-1.jpg")));

				final JTree standardComponent = new JTree();

				final JScrollPane scrollPane = new JScrollPane(imageComponent);
				ScrollPaneSelector.installScrollPaneSelector(scrollPane);
				final BoolRef isInstalled = new BoolRef(true);
				mainPanel.add(scrollPane, BorderLayout.CENTER);

				JToolBar toolBar = new JToolBar();
				toolBar.setFloatable(false);
				toolBar.add(new AbstractAction("Toggle install/uninstall") {
					public void actionPerformed(ActionEvent e) {
						if (isInstalled.getValue()) {
							ScrollPaneSelector.uninstallScrollPaneSelector(scrollPane);
						} else {
							ScrollPaneSelector.installScrollPaneSelector(scrollPane);
						}
						isInstalled.toggle();
					}
				});
				toolBar.add(new AbstractAction("Toggle component orientation") {
					public void actionPerformed(ActionEvent e) {
						if (scrollPane.getComponentOrientation().isLeftToRight()) {
							scrollPane.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
						} else {
							scrollPane.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
						}
					}
				});
				toolBar.add(new AbstractAction("Toggle viewportview") {
					public void actionPerformed(ActionEvent e) {
						if (scrollPane.getViewport().getView() == imageComponent) {
							scrollPane.setViewportView(standardComponent);
						} else {
							scrollPane.setViewportView(imageComponent);
						}
					}
				});
				mainPanel.add(toolBar, BorderLayout.NORTH);


				JLabel helpLabel = new JLabel("<html>Click on the little button at the scrollPane's bottom corner." +
											  "Keep it pressed while moving the selection rectangle and then release " +
											  "it to have the scrollPane scroll accordingly.</html>");
				helpLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
				mainPanel.add(helpLabel, BorderLayout.SOUTH);



				frame.setContentPane(mainPanel);
				frame.pack();
				Dimension pref = frame.getSize();
				Dimension min = frame.getMinimumSize();
				pref.width = Math.max((int)(pref.width * 0.5), min.width);
				pref.height = Math.max((int)(pref.height * 0.5), min.height);
				frame.setSize(pref);
				frame.setLocationRelativeTo(null);
				frame.setVisible(true);
			}
		});
	}
}