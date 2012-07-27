/*
 * TestPieChart.java
 *
 * Created on November 7, 2006, 1:56 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.jdesktop.swingx;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import org.jdesktop.swingx.chart.DefaultPieModel;
import org.jdesktop.swingx.chart.NamedNumber;
import org.jdesktop.swingx.chart.PieModel;

/**
 *
 * @author richardallenbair
 */
public class TestPieChart {
    private TestPieChart() {}
    
    public static void main(String[] args) {
        JFrame frame = new JFrame("Pie Chart Test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JTabbedPane tabs = new JTabbedPane();
        frame.add(tabs);
        tabs.add(new JXPieChart(), "Default Pie Chart");
        JXPieChart chart = new JXPieChart();
        chart.setModel(createPieModel());
        tabs.add(chart, "MapPieModel Pie Chart");
        chart = new JXPieChart();
        configurePieFromMap(chart);
        tabs.add(chart, "Map Based Pie Chart");
        chart = new JXPieChart();
        configurePieFromList(chart);
        tabs.add(chart, "List Based Pie Chart");
        chart = new JXPieChart();
        configurePieFromArray(chart);
        tabs.add(chart, "Array Based Pie Chart");
        frame.setSize(800, 600);
        frame.setVisible(true);
    }
    
    protected static PieModel createPieModel() {
        Map<String, Number> map = new HashMap<String, Number>();
        map.put("Colors", 23.5);
        map.put("Sports", 45.2);
        map.put("Food", 65.7);
        return new DefaultPieModel(map);
    }
    
    protected static void configurePieFromMap(JXPieChart pie) {
        Map<String, Number> map = new HashMap<String, Number>();
        map.put("Colors", 23.5);
        map.put("Sports", 45.2);
        map.put("Food", 65.7);
        pie.setModel(map);
    }
    
    protected static void configurePieFromList(JXPieChart pie) {
        List<Number> slices = new ArrayList<Number>();
        slices.add(new NamedNumber("Colors", 23.5));
        slices.add(new NamedNumber("Sports", 45.2));
        slices.add(new NamedNumber("Food", 65.7));
        pie.setModel(slices);
    }
    
    protected static void configurePieFromArray(JXPieChart pie) {
        pie.setModel(new NamedNumber("Colors", 23.5),
                    new NamedNumber("Sports", 45.2),
                    new NamedNumber("Food", 65.7));
    }
}
