package org.jdesktop.swingx;
/*
 * RenderBackgroundEditor.java
 *
 * Created on 12 de Janeiro de 2007, 09:43
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

import java.beans.PropertyEditorSupport;

/**
 *
 * @author Mário César
 */
public class RenderBackgroundEditor extends PropertyEditorSupport {
    static private String[] resourceStrings = {
          "BACKGROUND_COLOR",
          "GRADIENT",
          "TILED_IMAGE",
    };
    static private int[] intValues = {
        JXLabel.BACKGROUND_COLOR,
        JXLabel.GRADIENT,
        JXLabel.TILED_IMAGE,
    };
    static private String[] sourceCodeStrings = {
        "JXLabel.BACKGROUND_COLOR",
        "JXLabel.GRADIENT",
        "JXLabel.TILED_IMAGE",
    };
    public RenderBackgroundEditor() {
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
