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


package org.jdesktop.swingx.plaf.basic;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.ScrollPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPopupMenu;
import javax.swing.LookAndFeel;
import javax.swing.Scrollable;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicArrowButton;

import org.jdesktop.swingx.DefaultPickerRenderer;
import org.jdesktop.swingx.JXPicker;
import org.jdesktop.swingx.PickerRenderer;
import org.jdesktop.swingx.plaf.PickerUI;

public class BasicPickerUI extends PickerUI {
	
	/**
	 * The popup widget which displays the popupComponent inside.
	 */
	protected BasicPickerPopupMenu popup;
		
	/**
	 * The renderer which produces a component which displays the JXPicker's selected item.
	 */
	protected PickerRenderer renderer;
	
	/**
	 * The component returned from the renderer.
	 */
	protected Component renderedItem;
	
	/**
	 * This stores the pressed state of the trigger button.  It's needed to 
	 * help with the popup menu closing.  There's a race condition where the
	 * popup menu will close because the mouse is clicked outside of its bounds
	 * even though the user click on the trigger button.  Ie. with the popup
	 * menu visible, if the user clicks the trigger button, the popup will
	 * disappear and then reappear.  This helps prevent such a thing.
	 */
	private boolean buttonPressed;
	
	
	// implementation of PickerUI
	
	@Override
	public void setPopupVisible(final JXPicker picker, boolean visible) {
		picker.requestFocusInWindow();
		if (visible) {
			int height = 100;
			if (picker.getPopupComponent() instanceof ScrollPane) {
				height = ((Scrollable) picker.getPopupComponent()).getPreferredScrollableViewportSize().height;
			} else {
				height = picker.getPopupComponent().getPreferredSize().height;
			}
			popup.setPopupSize(picker.getWidth(), height);
			
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					popup.show(picker, 0, picker.getHeight());					
				}
			});
			return;
		}
		
		popup.setVisible(false);
	}

	@Override
	public boolean isPopupVisible() {
		return popup.isVisible();
	}

	
	@Override
	public void setButton(final JXPicker picker, AbstractButton newButton, AbstractButton oldButton) {

		if (oldButton != null) {
			picker.remove(oldButton);
			oldButton.removeAll();
			oldButton = null;
		}
		newButton.setFocusable(false);
		newButton.setRequestFocusEnabled(false);
		
		picker.add(newButton, BorderLayout.EAST);
		newButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				buttonPressed = true;
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				buttonPressed = false;
			}
		});
		newButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				picker.togglePopup();
			}
		});
		picker.revalidate();
		picker.repaint();
	}
	
	
	@Override
	public void setPopupComponent(JXPicker picker, Component c) {
		popup.removeAll();
		if (c == null) {
			c = new JLabel("No popup component defined.  Use JXPicker.setPopupComponent()");
		}
		c.setFocusable(false);
		popup.add(c);
		popup.revalidate();
		popup.repaint();
	}
	
	
	public void setRenderer(JXPicker picker, PickerRenderer renderer) {
		this.renderer = renderer;
	}
	
	public PickerRenderer getRenderer() {
		return renderer;
	}

	
	@Override
	public void renderItem(final JXPicker picker, Object item) {
		if (renderedItem != null) {
			picker.remove(renderedItem);			
		}
		renderedItem = renderer.getRendererComponent(picker, picker.getSelectedItem(), picker.hasFocus());
		renderedItem.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				picker.togglePopup();
			}
		});
		picker.add(renderedItem);
		picker.revalidate();
		picker.repaint();
	}
	

	// thus begins the UI setup

	@Override
	public static ComponentUI createUI(JComponent c) {
		return new BasicPickerUI();
	}
	
	@Override
	public void installUI(JComponent c) {
		super.installUI(c);
		
		final JXPicker picker = (JXPicker) c;
		picker.setOpaque(true);
		
		picker.addFocusListener(new FocusListener() {
			public void focusGained(FocusEvent evt) {
				renderItem(picker, picker.getSelectedItem());
			}
			public void focusLost(FocusEvent evt) {
				renderItem(picker, picker.getSelectedItem());
			}
		});

		picker.setFocusable(true);
		picker.setRequestFocusEnabled(true);
		
		if (picker.getLayout() == null) {
			picker.setLayout(new BorderLayout());
		}
		
		LookAndFeel.installBorder(picker, "Picker.border");
		LookAndFeel.installColorsAndFont(picker, "Picker.background", "Picker.foreground", "Picker.font");
		
		if (picker.getButton() == null) {
			setButton(picker, new BasicArrowButton(SwingConstants.SOUTH), null);
		} else {
			setButton(picker, picker.getButton(), null);
		}
		
		if (popup == null) {
			popup = new BasicPickerPopupMenu();			
		}

		setPopupComponent(picker, picker.getPopupComponent());			
				
		if (getRenderer() == null) {
			setRenderer(picker, new DefaultPickerRenderer());
		}

		renderItem(picker, picker.getSelectedItem());
		
	}
	
	@Override
	public void uninstallUI(JComponent c) {
		super.uninstallUI(c);
		popup.setVisible(false);
		c.removeAll();
	}
	
	
	
	
	/**
	 * A class which looks at the state of the trigger button in order
	 * to help determine its behavior.  Specifically, if the trigger button
	 * is being pressed, we want the popup to toggle, not disappear and reappear.
	 * This needs to be invoked later on the EDT because we won't have the right
	 * state of button press otherwise.
	 */
	private class BasicPickerPopupMenu extends JPopupMenu {
		public BasicPickerPopupMenu() {
			super();
			setBorder(BorderFactory.createEmptyBorder());
		}
		@Override
		public void setVisible(final boolean v) {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					if (buttonPressed) {
						BasicPickerPopupMenu.super.setVisible(! v);
						return;
					}
					BasicPickerPopupMenu.super.setVisible(v);
				}
			});
		}
	}
	
}
