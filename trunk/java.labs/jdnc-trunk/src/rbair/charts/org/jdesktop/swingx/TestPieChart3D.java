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
import javax.swing.JTabbedPane;

/**
 *
 * @author rbair
 */
public class TestPieChart3D {
    private TestPieChart3D() {}
    
    public static void main(String[] args) {
        JFrame frame = new JFrame("3D Pie Chart Test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JTabbedPane tabs = new JTabbedPane();
        frame.add(tabs);
        tabs.add(new JXPieChart3D(), "Default Pie Chart");
        JXPieChart3D chart = new JXPieChart3D();
        chart.setModel(TestPieChart.createPieModel());
        tabs.add(chart, "MapPieModel Pie Chart");
        chart = new JXPieChart3D();
        TestPieChart.configurePieFromMap(chart);
        tabs.add(chart, "Map Based Pie Chart");
        chart = new JXPieChart3D();
        TestPieChart.configurePieFromList(chart);
        tabs.add(chart, "List Based Pie Chart");
        chart = new JXPieChart3D();
        TestPieChart.configurePieFromArray(chart);
        tabs.add(chart, "Array Based Pie Chart");
        frame.setSize(800, 600);
        frame.setVisible(true);
    }
}
