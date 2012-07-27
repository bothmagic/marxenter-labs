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

package org.jdesktop.swingx;

import java.awt.Component;

import javax.swing.AbstractButton;
import javax.swing.JComponent;
import javax.swing.UIManager;

import org.jdesktop.swingx.plaf.JXPickerAddon;
import org.jdesktop.swingx.plaf.LookAndFeelAddons;
import org.jdesktop.swingx.plaf.PickerUI;
import org.jdesktop.swingx.plaf.basic.BasicPickerUI;

public class JXPicker extends JComponent {
	
	public final static String uiClassID = "PickerUI";
	
	protected Component popupComponent;
	
	protected AbstractButton triggerButton;
	
	protected Object selectedItem;
	
	protected PickerRenderer renderer;
	
	static {
		LookAndFeelAddons.contribute(new JXPickerAddon());
	}
		
	public JXPicker() {
		
		//TODO removing this line causes an error.  It shouldn't.  Don't know why.
		UIManager.put(getUIClassID(), BasicPickerUI.class.getName());

		updateUI();
	}
	
	public JXPicker(Component popupComponent) {
		this();
		setPopupComponent(popupComponent);
	}

		
	
	public void setSelectedItem(Object item) {
		firePropertyChange("selectedItem", this.selectedItem, item);
		this.selectedItem = item;
		getUI().renderItem(this, selectedItem);		
	}
	
	public Object getSelectedItem() {
		return selectedItem;
	}
	
	
	public void setPopupComponent(Component c) {
		firePropertyChange("popupComponent", this.popupComponent, c);
		this.popupComponent = c;
		getUI().setPopupComponent(this, c);
	}
	
	public Component getPopupComponent() {
		return popupComponent;
	}
	
	
	public void setButton(AbstractButton button) {
		firePropertyChange("triggerButton", this.triggerButton, button);
		this.triggerButton = button;
		getUI().setButton(this, button, triggerButton);
	}
	
	public AbstractButton getButton() {
		return triggerButton;
	}
	
	
	// popup control
	
	public void showPopup() {
		getUI().setPopupVisible(this, true);
	}
	
	public void hidePopup() {
		getUI().setPopupVisible(this, false);
	}
	
	public void togglePopup() {
		if (isPopupVisible()) {
			hidePopup();
		} else {
			showPopup();
		}
	}
	
	public boolean isPopupVisible() {
		return getUI().isPopupVisible();
	}
	
	
	// plaf related
	
	public PickerUI getUI() {
		return (PickerUI) ui;
	}
	
	@Override
	public void updateUI() {
		setUI((PickerUI) LookAndFeelAddons.getUI(this, PickerUI.class));
	}
	
	protected void setUI(PickerUI ui) {
		super.setUI(ui);
	}
	
	@Override
	public String getUIClassID() {
		return uiClassID;
	}	
	
	
	// rendering
	public void setRenderer(PickerRenderer renderer) {
		firePropertyChange("renderer", this.renderer, renderer);
		this.renderer = renderer;
		getUI().setRenderer(this, renderer);
	}
	
	public PickerRenderer getRenderer() {
		return renderer;
	}

	
}
