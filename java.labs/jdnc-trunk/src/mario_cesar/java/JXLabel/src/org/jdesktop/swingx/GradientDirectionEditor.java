package org.jdesktop.swingx;
/*
 * GradientDirectionEditor.java
 *
 * Created on 11 de Janeiro de 2007, 18:24
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

import java.beans.PropertyEditorSupport;

/**
 *
 * @author Mário César
 */
public class GradientDirectionEditor extends PropertyEditorSupport {
    static private String[] resourceStrings = {
          "TOP_TO_BOTTOM",
          "BOTTOM_TO_TOP",
          "RIGHT_TO_LEFT",
          "LEFT_TO_RIGHT",
    };
    static private int[] intValues = {
        JXLabel.TOP_TO_BOTTOM,
        JXLabel.BOTTOM_TO_TOP,
        JXLabel.RIGHT_TO_LEFT,
        JXLabel.LEFT_TO_RIGHT,
    };
    static private String[] sourceCodeStrings = {
        "JXLabel.TOP_TO_BOTTOM",
        "JXLabel.BOTTOM_TO_TOP",
        "JXLabel.RIGHT_TO_LEFT",
        "JXLabel.LEFT_TO_RIGHT",
    };
    public GradientDirectionEditor() {
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
