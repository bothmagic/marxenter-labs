package org.jdesktop.swingx;
/*
 * TextOrientationEditor.java
 *
 * Created on 12 de Janeiro de 2007, 09:38
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

import java.beans.PropertyEditorSupport;

/**
 *
 * @author Mário César
 */
public class TextOrientationEditor extends PropertyEditorSupport {
    static private String[] resourceStrings = {
          "NORMAL",
          "INVERTED",
          "VERTICAL_LEFT",
          "VERTICAL_RIGHT",
    };
    static private int[] intValues = {
        JXLabel.NORMAL,
        JXLabel.INVERTED,
        JXLabel.VERTICAL_LEFT,
        JXLabel.VERTICAL_RIGHT,
    };
    static private String[] sourceCodeStrings = {
        "JXLabel.NORMAL",
        "JXLabel.INVERTED",
        "JXLabel.VERTICAL_LEFT",
        "JXLabel.VERTICAL_RIGHT",
    };
    public TextOrientationEditor() {
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
