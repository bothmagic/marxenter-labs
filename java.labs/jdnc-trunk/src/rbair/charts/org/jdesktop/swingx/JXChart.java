/*
 * JChart.java
 *
 * Created on November 4, 2006, 10:14 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.jdesktop.swingx;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.geom.Rectangle2D;
import javax.swing.JComponent;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.Plot;
import org.jfree.data.general.Dataset;

/**
 * Thankfully, this component has no PLAF :-)
 *
 * Simply wraps the JFreeChart component, since JFreeChart is not a JavaBean
 * (no default constructor).
 *
 * @author rbair
 */
public class JXChart extends JComponent {
    private JFreeChart delegate;
    private Plot plot;
    
    /** Creates a new instance of JChart */
    public JXChart() {
        plot = createPlot();
        delegate = new JFreeChart(plot);
    }
    
    protected void paintComponent(Graphics g) {
        Dimension size = getSize();
        Insets insets = getInsets();
        
        if (delegate != null) {
            delegate.draw((Graphics2D)g, new Rectangle2D.Double(
                    insets.left,
                    insets.top,
                    size.width - insets.left - insets.right,
                    size.height - insets.top - insets.bottom
                    ));
        } else {
            super.paintComponent(g);
        }
    }

    protected Plot createPlot() {
        return null;
    }
    
    protected Plot getPlot() {
        return plot;
    }
    
    //----------------------------------------------------------- Properties
    
    public void setBackgroundAlpha(float alpha) {
        float old = getBackgroundAlpha();
        plot.setBackgroundAlpha(alpha);
        firePropertyChange("backgroundAlpha", old, getBackgroundAlpha());
    }
    
    public final float getBackgroundAlpha() {
        return plot.getBackgroundAlpha();
    }
    
    public void setNoDataMessage(String message) {
        String old = getNoDataMessage();
        plot.setNoDataMessage(message);
        firePropertyChange("noDataMessage", old, getNoDataMessage());
    }
    
    public final String getNoDataMessage() {
        return plot.getNoDataMessage();
    }
}
