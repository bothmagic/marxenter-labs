/*
 * PieModel.java
 *
 * Created on November 7, 2006, 4:04 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.jdesktop.swingx.chart;

/**
 *
 * @author rbair
 */
public interface PieModel<T extends Number> {
    public String getName(int index);
    public T getValue(int index);
    public int size();
}
