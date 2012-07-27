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


package org.jdesktop.swingx.plaf;

import java.awt.Component;

import javax.swing.AbstractButton;
import javax.swing.plaf.ComponentUI;

import org.jdesktop.swingx.JXPicker;
import org.jdesktop.swingx.PickerRenderer;

public abstract class PickerUI extends ComponentUI {
		
	public abstract void setPopupVisible(JXPicker picker, boolean visible);
	
	public abstract boolean isPopupVisible();
	
	public abstract void setButton(JXPicker picker, AbstractButton newButton, AbstractButton oldButton);
	
	public abstract void setPopupComponent(JXPicker picker, Component c);
	
	public abstract void setRenderer(JXPicker picker, PickerRenderer renderer);
	
	public abstract PickerRenderer getRenderer();
	
	public abstract void renderItem(JXPicker picker, Object item);
	
}
