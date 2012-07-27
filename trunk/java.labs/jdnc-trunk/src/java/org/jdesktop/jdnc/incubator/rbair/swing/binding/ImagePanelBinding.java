/*
 * $Id: ImagePanelBinding.java 46 2004-09-08 17:33:01Z rbair $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */

package org.jdesktop.jdnc.incubator.rbair.swing.binding;

import java.awt.Image;

import javax.swing.Icon;
import javax.swing.JComponent;

import org.jdesktop.jdnc.incubator.rbair.swing.JXImagePanel;
import org.jdesktop.jdnc.incubator.rbair.swing.data.DataModel;

public class ImagePanelBinding extends AbstractBinding {

    private JXImagePanel imagePanel;

    public ImagePanelBinding(JXImagePanel imagePanel,
			     DataModel model, String fieldName) {
        super(imagePanel, model, fieldName, AbstractBinding.AUTO_VALIDATE_NONE);
    }

    public JComponent getComponent() {
	return imagePanel;
    }

    public void setComponent(JComponent component) {
	this.imagePanel = (JXImagePanel)component;
    }

    protected Object getComponentValue() {
	Class klazz = metaData.getElementClass();
	if (klazz == Image.class) {
	    return imagePanel.getImage();
	}
	else if (klazz == Icon.class) {
	    return imagePanel.getIcon();
	}
	// default?
	return null;
    }

    protected void setComponentValue(Object value) {
	Class klazz = metaData.getElementClass();
	if (klazz == Image.class) {
	    imagePanel.setImage((Image)value);
	}
	else if (klazz == Icon.class) {
	    imagePanel.setIcon((Icon)value);
	}
    }

}
