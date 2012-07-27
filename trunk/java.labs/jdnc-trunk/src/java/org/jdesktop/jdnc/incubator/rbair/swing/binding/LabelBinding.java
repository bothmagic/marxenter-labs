/*
 * $Id: LabelBinding.java 46 2004-09-08 17:33:01Z rbair $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */

package org.jdesktop.jdnc.incubator.rbair.swing.binding;

import java.awt.Image;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;

import org.jdesktop.jdnc.incubator.rbair.swing.data.DataModel;

/**
 * Class which binds an uneditable component (JLabel) to a data model field
 * of arbitrary type.  If the field is type Image, then the image will be
 * displayed as an icon in the component.  For all other types, the data model
 * value will be converted and displayed as a String.
 * @author Amy Fowler
 * @version 1.0
 */
public class LabelBinding extends AbstractBinding {
    private JLabel label;

    public LabelBinding(JLabel label,
                        DataModel model, String fieldName) {
        super(label, model, fieldName, AbstractBinding.AUTO_VALIDATE_NONE);
    }

    public boolean isModified() {
        return false;
    }

    public boolean isValid() {
        return true;
    }

    public boolean push() {
        // do nothing, value was not edited
        return true;
    }

    public JComponent getComponent() {
        return label;
    }

    protected void setComponent(JComponent component) {
        label = (JLabel)component;
    }

    protected Object getComponentValue() {
	Class clz = metaData.getElementClass();

        if (clz == Image.class) {
            Icon icon = label.getIcon();
            if (icon instanceof ImageIcon) {
                Image image = ( (ImageIcon) icon).getImage();
                return image;
            }
        }
//	if (clz == Link.class) {
//	    return (Link)label.getClientProperty("jdnc.link.value");
//	}
        return label.getText();
    }

    protected void setComponentValue(Object value) {
	Class clz = metaData.getElementClass();

        if (clz == Image.class) {
            if (value != null) {
                ImageIcon icon = new ImageIcon( (Image) value);
                label.setIcon(icon);
            }
        }
//	if (clz == Link.class) {
//	    if (value != null) {
//		label.setText("<html>" + convertFromModelType(value) + "</html>");
//		label.putClientProperty("jdnc.link.value", (Link)value);
//	    }
//	}
        else {
            label.setText(convertFromModelType(value));
        }
    }
}
