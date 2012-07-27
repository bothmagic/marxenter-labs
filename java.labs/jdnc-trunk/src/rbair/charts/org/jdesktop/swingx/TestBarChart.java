/*
 * TestPieChart.java
 *
 * Created on November 7, 2006, 1:56 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.jdesktop.swingx;

import javax.swing.JFrame;

/**
 *
 * @author richardallenbair
 */
public class TestBarChart {
    private TestBarChart() {}
    
    public static void main(String[] args) {
        JFrame frame = new JFrame("Bar Chart Test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new JXBarChart());
        frame.setSize(800, 600);
        frame.setVisible(true);
    }
}
