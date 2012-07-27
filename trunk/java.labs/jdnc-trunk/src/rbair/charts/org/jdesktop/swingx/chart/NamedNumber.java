/*
 * NamedNumber.java
 *
 * Created on November 7, 2006, 4:18 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.jdesktop.swingx.chart;

/**
 *
 * @author rbair
 */
public class NamedNumber extends Number {
    private String name;
    private Number value;
    public NamedNumber(String n, Number v) {
        this.name = n;
        this.value = v;
    }
    public String toString() {
        return name;
    }

    public int intValue() {
        return value.intValue();
    }

    public long longValue() {
        return value.longValue();
    }

    public float floatValue() {
        return value.floatValue();
    }

    public double doubleValue() {
        return value.doubleValue();
    }
}
