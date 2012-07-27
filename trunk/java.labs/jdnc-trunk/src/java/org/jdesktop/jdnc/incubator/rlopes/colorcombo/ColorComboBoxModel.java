/*
 * ColorComboBoxModel.java
 *
 * Created on 24 de Fevereiro de 2005, 22:52
 */

package org.jdesktop.jdnc.incubator.rlopes.colorcombo;

import java.awt.Color;
import javax.swing.DefaultComboBoxModel;


/**
 *
 * @author Lopes
 */
public class ColorComboBoxModel extends DefaultComboBoxModel {
    
    public ColorComboBoxModel() {
        super(DEFAULT_COLORS);
    }

    protected static Color[] DEFAULT_COLORS =
        new Color[] { 
            new Color(0, 0, 0),
            new Color(153, 51, 0),
            new Color(51, 51, 0),
            new Color(0, 51, 0),
            new Color(0, 51, 102),            
            new Color(0, 0, 128),
            new Color(51, 51, 153),            
            new Color(51, 51, 51),
            
            new Color(128, 0, 0),
            new Color(255, 102, 0),
            new Color(128, 128, 0),
            new Color(0, 128, 0),
            new Color(0, 128, 128),
            new Color(0, 0, 255),
            new Color(102, 102, 153),
            new Color(128, 128, 128),
            
            new Color(255, 0, 0),  
            new Color(255, 153, 0),
            new Color(153, 204, 0),
            new Color(51, 153, 102),
            new Color(51, 204, 204),
            new Color(51, 102, 255),
            new Color(128, 0, 128),
            new Color(150, 150, 150),
            
            new Color(255, 0, 255),
            new Color(255, 204, 0),            
            new Color(255, 255, 0),
            new Color(0, 255, 0),
            new Color(0, 255, 255),
            new Color(0, 204, 255),
            new Color(153, 51, 102),
            new Color(192, 192, 192),
            
            new Color(255, 153, 204),
            new Color(255, 204, 153),            
            new Color(255, 255, 153),
            new Color(204, 255, 204),
            new Color(204, 255, 255),
            new Color(153, 204, 255),
            new Color(204, 153, 255),
            new Color(255, 255, 255)
        };            
}

