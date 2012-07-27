/*
 * JXPieChart3D.java
 *
 * Created on November 7, 2006, 2:01 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.jdesktop.swingx;

import java.util.ArrayList;
import java.util.List;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.chart.plot.Plot;
import org.jfree.data.general.AbstractDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;

/**
 *
 * @author rbair
 */
public class JXPieChart3D extends JXPieChart {
    
    /** Creates a new instance of JXPieChart3D */
    public JXPieChart3D() {
    }
    
    @Override
    protected Plot createPlot() {
        PiePlot3D plot = new PiePlot3D(createPieDataset());
        plot.setStartAngle(180);
        return plot;
    }
    
}
