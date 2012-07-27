package org.jdesktop.swingx;
/*
 * HorizontalEditor.java
 *
 * Created on 12 de Janeiro de 2007, 09:41
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

import java.beans.PropertyEditorSupport;

/**
 *
 * @author Mário César
 */
public class HorizontalEditor extends PropertyEditorSupport {
    static private String[] resourceStrings = {
          "LEADING",
          "LEFT",
          "CENTER",
          "RIGHT",
          "TRAILING",
    };
    static private int[] intValues = {
        JXLabel.LEADING,
        JXLabel.LEFT,
        JXLabel.CENTER,
        JXLabel.RIGHT,
        JXLabel.TRAILING,
    };
    static private String[] sourceCodeStrings = {
        "JXLabel.LEADING",
        "JXLabel.LEFT",
        "JXLabel.CENTER",
        "JXLabel.RIGHT",
        "JXLabel.TRAILING",
    };
    public HorizontalEditor() {
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
