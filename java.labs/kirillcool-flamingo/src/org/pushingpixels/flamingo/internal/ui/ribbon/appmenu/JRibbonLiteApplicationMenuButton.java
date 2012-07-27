/*
 * Copyright (c) 2005-2010 Flamingo Kirill Grouchnikov. All Rights Reserved.
 *
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 * 
 *  o Redistributions of source code must retain the above copyright notice, 
 *    this list of conditions and the following disclaimer. 
 *     
 *  o Redistributions in binary form must reproduce the above copyright notice, 
 *    this list of conditions and the following disclaimer in the documentation 
 *    and/or other materials provided with the distribution. 
 *     
 *  o Neither the name of Flamingo Kirill Grouchnikov nor the names of 
 *    its contributors may be used to endorse or promote products derived 
 *    from this software without specific prior written permission. 
 *     
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" 
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, 
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR 
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR 
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, 
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, 
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; 
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, 
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE 
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, 
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. 
 */
package org.pushingpixels.flamingo.internal.ui.ribbon.appmenu;

import java.awt.*;
import java.beans.PropertyChangeEvent;

import java.beans.PropertyChangeListener;
import javax.swing.UIManager;

import org.pushingpixels.flamingo.api.common.*;
import org.pushingpixels.flamingo.api.common.icon.EmptyResizableIcon;
import org.pushingpixels.flamingo.api.common.icon.ResizableIcon;
import org.pushingpixels.flamingo.api.ribbon.JRibbon;
import org.pushingpixels.flamingo.api.ribbon.JRibbonFrame;
import org.pushingpixels.flamingo.internal.ui.common.BasicCommandButtonUI;
import org.pushingpixels.flamingo.internal.utils.FlamingoUtilities;

/**
 * The main application menu button for {@link JRibbon} component placed in a
 * {@link JRibbonFrame}. This class is for internal use only and is intended for
 * look-and-feel layer customization.
 * 
 * @author Kirill Grouchnikov
 */
public class JRibbonLiteApplicationMenuButton extends JCommandButton 
    implements RibbonProvider, PropertyChangeListener {
	private JRibbon ribbon;
	/**
	 * The UI class ID string.
	 */
	public static final String uiClassID = "RibbonLiteApplicationMenuButtonUI";

	/**
	 * Creates a new application menu button.
	 */
	public JRibbonLiteApplicationMenuButton(JRibbon ribbon) {
		super(ribbon.getApplicationMenuText(), new EmptyResizableIcon(16));
		this.setCommandButtonKind(CommandButtonKind.POPUP_ONLY);
		this.setDisplayState(CommandButtonDisplayState.MEDIUM);
		this.ribbon = ribbon;
                setFont(FlamingoUtilities.getFont(this, "Ribbon.font",
				"Button.font", "Panel.font"));
                ribbon.addPropertyChangeListener(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.JButton#updateUI()
	 */
	@Override
	public void updateUI() {
		if (UIManager.get(getUIClassID()) != null) {
			setUI((BasicCommandButtonUI) UIManager.getUI(this));
		} else {
			setUI(BasicRibbonLiteApplicationMenuButtonUI.createUI(this));
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.JButton#getUIClassID()
	 */
	@Override
	public String getUIClassID() {
		return uiClassID;
	}

	public JRibbon getRibbon() {
		return this.ribbon;
	}

    @Override
    public void setIcon(ResizableIcon defaultIcon) {
        //super.setIcon(defaultIcon);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("applicationMenuText")) {
            setText((String)evt.getNewValue());
        }
    }
        
        
}
