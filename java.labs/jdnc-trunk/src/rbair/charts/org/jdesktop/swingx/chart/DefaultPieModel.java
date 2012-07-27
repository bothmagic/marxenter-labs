/*
 * DefaultPieModel.java
 *
 * Created on November 7, 2006, 4:10 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.jdesktop.swingx.chart;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 *
 * @author rbair
 */
public class DefaultPieModel implements PieModel {
    String[] names;
    Number[] values;
    
    /** Creates a new instance of DefaultPieModel */
    public DefaultPieModel(List<Number> slices) {
        if (slices == null) {
            names = new String[0];
            values = new Number[0];
        } else {
            names = new String[slices.size()];
            values = new Number[slices.size()];
            int index = 0;
            for (Number slice : slices) {
                names[index] = slice == null ? null : slice.toString();
                values[index] = slice;
                index++;
            }
        }
    }
    
    public DefaultPieModel(Map<String, Number> slices) {
        if (slices == null) {
            names = new String[0];
            values = new Number[0];
        } else {
            names = new String[slices.size()];
            values = new Number[slices.size()];
            int index = 0;
            for (Map.Entry<String, Number> entry : slices.entrySet()) {
                names[index] = entry.getKey();
                values[index] = entry.getValue();
                index++;
            }
        }
    }
    
    public DefaultPieModel(Number... slices) {
        if (slices == null) {
            names = new String[0];
            values = new Number[0];
        } else {
            names = new String[slices.length];
            values = new Number[slices.length];
            int index = 0;
            for (Number slice : slices) {
                names[index] = slice == null ? null : slice.toString();
                values[index] = slice;
                index++;
            }
        }
    }
    
    public String getName(int index) {
        return names[index];
    }

    public Number getValue(int index) {
        return values[index];
    }

    public int size() {
        return names.length;
    }
    
}
