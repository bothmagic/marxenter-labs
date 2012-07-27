package org.jdesktop.swingx;
/*
 * VerticalEditor.java
 *
 * Created on 12 de Janeiro de 2007, 09:40
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

import java.beans.PropertyEditorSupport;
import javax.swing.SwingConstants;

/**
 *
 * @author Mário César
 */
public class VerticalEditor extends PropertyEditorSupport implements SwingConstants {
    static private String[] resourceStrings = {
          "TOP",
          "CENTER",
          "BOTTOM",
    };
    static private int[] intValues = {
        JXLabel.TOP,
        JXLabel.CENTER,
        JXLabel.BOTTOM,
    };
    static private String[] sourceCodeStrings = {
        "JXLabel.TOP",
        "JXLabel.CENTER",
        "JXLabel.BOTTOM",
    };
    public VerticalEditor() {
    }
    public String[] getTags() {
        return resourceStrings;
    }
    public String getJavaInitializationString() {
        Object value = getValue();
        for (int i = 0; i < intValues.length; i++) {
            if (value.equals(new Integer(intValues[i]))) {
                return sourceCodeStrings[i];
            }
        }
        return null;
    }
    public String getAsText() {
        Object value = getValue();
        for (int i = 0; i < intValues.length; i++) {
            if (value.equals(new Integer(intValues[i]))) {
                return resourceStrings[i];
            }
        }
        return null;
    }
    public void setAsText(String text) throws IllegalArgumentException {
        for (int i = 0; i < resourceStrings.length; i++) {
            if (text.equals(resourceStrings[i])) {
                setValue(new Integer(intValues[i]));
                return;
            }
        }
        throw new IllegalArgumentException();
    }
}
