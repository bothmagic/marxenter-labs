/*
 * JXBarChart.java
 *
 * Created on November 7, 2006, 2:05 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.jdesktop.swingx;

import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

/**
 *
 * @author rbair
 */
public class JXLineChart extends JXChart {
    
    /** Creates a new instance of JXBarChart */
    public JXLineChart() {
    }

    @Override
    protected Plot createPlot() {
        DefaultCategoryDataset ds = new DefaultCategoryDataset();
        String colors = "colors";
        String sports = "sports";
        String food = "food";
        ds.setValue(23.5, "colors", "blue");
        ds.setValue(33.1, "colors", "violet");
        ds.setValue(27.2, "colors", "red");
        ds.setValue(17.8, "colors", "yellow");

        ds.setValue(42.1, "sports", "basketball");
        ds.setValue(87.9, "sports", "soccor");
        ds.setValue(43.7, "sports", "football");
        ds.setValue(38.2, "sports", "hockey");

        ds.setValue(13.5, "food", "hot dogs");
        ds.setValue(65.2, "food", "pizza");
        ds.setValue(55.5, "food", "ravioli");
        ds.setValue(45.2, "food", "bananas");
        CategoryPlot plot = new CategoryPlot(ds, new CategoryAxis("Things I Can't Live Without"),
                new NumberAxis("Percentage of Respondents Who Agree"), new LineAndShapeRenderer());
        return plot;
    }
    
}
