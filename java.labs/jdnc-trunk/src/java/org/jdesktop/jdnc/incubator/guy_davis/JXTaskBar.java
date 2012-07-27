/**
 * $Id: JXTaskBar.java 810 2006-06-25 21:33:16Z guy_davis $
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */

package org.jdesktop.jdnc.incubator.guy_davis;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.awt.event.MouseEvent;
import java.beans.PropertyVetoException;

import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.ButtonGroup;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;

/**
 * A complete implementation of a Windows-style taskbar for the JDesktopPane
 * multiple document interface (MDI) under Swing. Based on a blog entry by
 * Patrick Gotthardt at http://www.jroller.com/page/pago/.
 *
 * @author Guy Davis
 * @author Patrick Gotthardt
 */
public class JXTaskBar extends JPanel {

	/** The buttons on the task bar; only one selected at a time at most. */
	private ButtonGroup buttonGroup;

	/**
	 * Generate a task bar based on the windows of this desktop.
	 * 
	 * @param desktop
	 *            The desktop to create a task bar for.
	 */
	public JXTaskBar(final JDesktopPane desktop) {
		super(new EqualSizesLayout(EqualSizesLayout.LEFT, 0));
		this.buttonGroup = new ButtonGroup();

		desktop.addContainerListener(new ContainerListener() {
			public void componentRemoved(@SuppressWarnings("unused")
			ContainerEvent e) {
				desktop.revalidate();  // Redo layout to avoid weird position
			}

			public void componentAdded(ContainerEvent e) {
				if (e.getChild() instanceof JInternalFrame
						&& !contains((JInternalFrame) e.getChild())) {
					add(new TaskPaneAction((JInternalFrame) e.getChild()));
				}
			}
		});

		JInternalFrame[] frames = desktop.getAllFrames();
		for (int i = 0; i < frames.length; i++) {
			add(new TaskPaneAction(frames[i]));
		}
	}

	/**
	 * Determine if the given frame is already in the task bar.
	 * 
	 * @param frm
	 *            The frame to check for.
	 * @return True if already in task bar.
	 */
	protected boolean contains(JInternalFrame frm) {
		int limit = getComponentCount();
		for (int i = 0; i < limit; i++) {
			AbstractButton btn = (AbstractButton) getComponent(i);
			if (((TaskPaneAction) btn.getAction()).getFrame().equals(frm)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Create a button for a new window in the task bar.
	 * 
	 * @param act
	 *            The action for the button being created.
	 */
	protected void add(TaskPaneAction act) {
		TaskButton btn = new TaskButton(act);

		act.setButton(btn);
		this.buttonGroup.add(btn);
		add(btn);
	}

	/**
	 * Remove the button for this frame/action from the task bar.
	 * 
	 * @param act
	 *            The action for the button being created.
	 */
	protected void remove(Action act) {
		int limit = getComponentCount();
		for (int i = 0; i < limit; i++) {
			TaskButton btn = (TaskButton) getComponent(i);
			if (btn.getAction() == act) {
				remove(btn);
				this.buttonGroup.remove(btn);
				this.getLayout().layoutContainer(this.getParent());
				return;
			}
		}
	}

	/**
	 * The action which will be invoked when a given button is selected by
	 * operating on the given frame.
	 */
	private class TaskPaneAction extends AbstractAction implements
			InternalFrameListener {

		/** The frame for the task bar button */
		private JInternalFrame frm;

		/** The button on the task bar */
		private TaskButton btn;

		/** The contextual menu attached to the button */
		private WindowPopupMenu menu;

		/**
		 * Create an action that dictates the behavior of the button for a given
		 * frame in the desktop.
		 * 
		 * @param frm
		 *            The frame for the task bar button
		 */
		public TaskPaneAction(JInternalFrame frm) {
			super(frm.getTitle(), frm.getFrameIcon());

			this.frm = frm;
			frm.addInternalFrameListener(this);
			menu = new WindowPopupMenu(frm);
		}

		/**
		 * @param btn
		 *            The new button for this frame.
		 */
		public void setButton(TaskButton btn) {
			this.btn = btn;
			btn.setComponentPopupMenu(this.menu);
			btn.setSelected(!frm.isIcon());
		}

		/**
		 * @return The frame being monitored.
		 */
		public JInternalFrame getFrame() {
			return frm;
		}

		/**
		 * Button was pushed so either select the window if not selected, or
		 * iconify it if selected.
		 * 
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(@SuppressWarnings("unused")
		ActionEvent e) {
			try {
				if (frm.isSelected()) {
					frm.setIcon(true);
				} else {
					if (frm.isIcon()) {
						frm.setIcon(false);
					} else {
						frm.setSelected(true);
					}
				}
			} catch (PropertyVetoException ex) {
				ex.printStackTrace();
			}
		}

		/**
		 * @see javax.swing.event.InternalFrameListener#internalFrameOpened(javax.swing.event.InternalFrameEvent)
		 */
		public void internalFrameOpened(@SuppressWarnings("unused")
		InternalFrameEvent e) {
			btn.setSelected(true);
			menu.updateMenuStates();
		}

		/**
		 * @see javax.swing.event.InternalFrameListener#internalFrameIconified(javax.swing.event.InternalFrameEvent)
		 */
		public void internalFrameIconified(InternalFrameEvent e) {
			e.getInternalFrame().setVisible(false); // Hide from view
			btn.setSelected(false);
			menu.updateMenuStates();
		}

		/**
		 * @see javax.swing.event.InternalFrameListener#internalFrameDeiconified(javax.swing.event.InternalFrameEvent)
		 */
		public void internalFrameDeiconified(InternalFrameEvent e) {
			e.getInternalFrame().setVisible(true); // Show again
			btn.setSelected(true);
			menu.updateMenuStates();
		}

		/**
		 * @see javax.swing.event.InternalFrameListener#internalFrameDeactivated(javax.swing.event.InternalFrameEvent)
		 */
		public void internalFrameDeactivated(@SuppressWarnings("unused")
		InternalFrameEvent e) {
			btn.setSelected(false);
			menu.updateMenuStates();
		}

		/**
		 * @see javax.swing.event.InternalFrameListener#internalFrameClosing(javax.swing.event.InternalFrameEvent)
		 */
		public void internalFrameClosing(@SuppressWarnings("unused")
		InternalFrameEvent e) {
			btn.setSelected(false);
			menu.updateMenuStates();
		}

		/**
		 * @see javax.swing.event.InternalFrameListener#internalFrameClosed(javax.swing.event.InternalFrameEvent)
		 */
		public void internalFrameClosed(@SuppressWarnings("unused")
		InternalFrameEvent e) {
			remove(this);
		}

		/**
		 * @see javax.swing.event.InternalFrameListener#internalFrameActivated(javax.swing.event.InternalFrameEvent)
		 */
		public void internalFrameActivated(@SuppressWarnings("unused")
		InternalFrameEvent e) {
			btn.setSelected(true);
			menu.updateMenuStates();
		}
	}

	/**
	 * The contextual menu on the task bar buttons allowing for the user to
	 * minimize, maximize, restore, and close windows.
	 */
	private class WindowPopupMenu extends JPopupMenu {

		/** The frame for this menu */
		private JInternalFrame frame;

		/** The Restore menu to be toggled */
		private JMenuItem restoreMenuItem;

		/** The Minimize menu to be toggled */
		private JMenuItem minimizeMenuItem;

		/** The Maximize menu to be toggled */
		private JMenuItem maximizeMenuItem;

		/**
		 * Creates the contextual menu for the task bar button.
		 * 
		 * @param frame
		 *            The frame the task bar button controls.
		 */
		WindowPopupMenu(final JInternalFrame frame) {
			this.frame = frame;
			restoreMenuItem = new JMenuItem("Restore");
			restoreMenuItem.addActionListener(new ActionListener() {
				public void actionPerformed(@SuppressWarnings("unused")
				ActionEvent e) {
					try {
						if (frame.isMaximum()) {
							frame.setMaximum(false);
						} else if (frame.isIcon()) {
							frame.setIcon(false);
						}
					} catch (PropertyVetoException ex) {
						ex.printStackTrace();
					}
				}
			});
			restoreMenuItem.setEnabled(frame.isIcon() || frame.isMaximum());
			add(restoreMenuItem);

			minimizeMenuItem = new JMenuItem("Minimize");
			minimizeMenuItem.addActionListener(new ActionListener() {
				public void actionPerformed(@SuppressWarnings("unused")
				ActionEvent e) {
					try {
						frame.setIcon(true);
					} catch (PropertyVetoException ex) {
						ex.printStackTrace();
					}
				}
			});
			minimizeMenuItem.setEnabled(frame.isIconifiable());
			add(minimizeMenuItem);

			maximizeMenuItem = new JMenuItem("Maximize");
			maximizeMenuItem.addActionListener(new ActionListener() {
				public void actionPerformed(@SuppressWarnings("unused")
				ActionEvent e) {
					try {
						frame.setMaximum(true);
					} catch (PropertyVetoException ex) {
						ex.printStackTrace();
					}
				}
			});
			maximizeMenuItem.setEnabled(frame.isMaximizable());
			add(maximizeMenuItem);

			addSeparator();

			JMenuItem closeItem = new JMenuItem("Close");
			closeItem.addActionListener(new ActionListener() {
				public void actionPerformed(@SuppressWarnings("unused")
				ActionEvent e) {
					try {
						frame.setClosed(true);
					} catch (PropertyVetoException ex) {
						ex.printStackTrace();
					}
				}
			});
			closeItem.setEnabled(frame.isClosable());
			add(closeItem);
		}

		/**
		 * Toggle the enabled state of the restore, minimize, and maximize
		 * buttons depending on the current state of the monitored frame.
		 */
		void updateMenuStates() {
			if (frame.isIcon()) {
				this.minimizeMenuItem.setEnabled(false);
				this.maximizeMenuItem.setEnabled(frame.isMaximizable());
				this.restoreMenuItem.setEnabled(true);
			} else if (frame.isMaximum()) {
				this.minimizeMenuItem.setEnabled(frame.isIconifiable());
				this.maximizeMenuItem.setEnabled(false);
				this.restoreMenuItem.setEnabled(true);
			} else {
				this.minimizeMenuItem.setEnabled(frame.isIconifiable());
				this.maximizeMenuItem.setEnabled(frame.isMaximizable());
				this.restoreMenuItem.setEnabled(false);
			}
		}
	}

	/**
	 * Custom button which places it's tooltip above itself. Otherwise the
	 * default tooltip position means it is covered by the Windows task bar when
	 * the application is maximized.
	 */
	private class TaskButton extends JToggleButton {
		TaskButton(TaskPaneAction action) {
			super(action);
			setHorizontalAlignment(SwingConstants.LEFT);
			setPreferredSize(new Dimension(160, 24));
			setToolTipText(action.getFrame().getTitle());
		}

		/**
		 * Regular tooltip placement hides the tooltip under the Windows taskbar
		 * when the main window is maximized. This will ensure it is visible.
		 * 
		 * @see javax.swing.JComponent#getToolTipLocation(java.awt.event.MouseEvent)
		 */
		public Point getToolTipLocation(MouseEvent event) {
			Point location = ((Component) event.getSource())
					.getLocationOnScreen();
			location.y -= 20;
			location.x += 25;
			SwingUtilities.convertPointFromScreen(location, this);
			return location;
		}
	}
}
