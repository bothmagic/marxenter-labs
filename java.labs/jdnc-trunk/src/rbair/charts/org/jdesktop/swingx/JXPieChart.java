/*
 * JXPieChart.java
 *
 * Created on November 7, 2006, 1:45 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.jdesktop.swingx;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.jdesktop.swingx.chart.DefaultPieModel;
import org.jdesktop.swingx.chart.NamedNumber;
import org.jdesktop.swingx.chart.PieModel;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.Plot;
import org.jfree.data.general.AbstractDataset;
import org.jfree.data.general.PieDataset;

/**
 *
 * @author rbair
 */
public class JXPieChart extends JXChart {
    private PieModel model;
    private PiePlot delegate;
    
    //---------------------------------------------------------- Constructor
    /** Creates a new instance of JXPieChart */
    public JXPieChart() {
        setModel(new DefaultPieModel(
                new NamedNumber("Colors", 23.5),
                new NamedNumber("Sports", 45.2),
                new NamedNumber("Food", 65.7)));
        delegate = getPlot();
    }

    //-------------------------------------------------------- Model Methods
    public void setModel(PieModel model) {
        PieModel old = getModel();
        this.model = model;
        firePropertyChange("model", old, getModel());
        revalidate();
        repaint();
    }
    
    public void setModel(Map<String, Number> slices) {
        setModel(new DefaultPieModel(slices));
    }
    
    public void setModel(List<Number> slices) {
        setModel(new DefaultPieModel(slices));
    }
    
    public void setModel(Number... slices) {
        setModel(new DefaultPieModel(slices));
    }
    
    public PieModel getModel() {
        return model;
    }
    
    //----------------------------------------------------------- Properties
    
    public void setCircular(boolean circular) {
        boolean old = isCircular();
        delegate.setCircular(circular);
        firePropertyChange("circular", old, isCircular());
    }
    
    public boolean isCircular() {
        return delegate.isCircular();
    }
    
    public void setIgnoreNullValues(boolean flag) {
        boolean old = getIgnoreNullValues();
        delegate.setIgnoreNullValues(flag);
        firePropertyChange("ignoreNullValues", old, getIgnoreNullValues());
    }
    
    public boolean getIgnoreNullValues() {
        return delegate.getIgnoreNullValues();
    }
    
    public void setIgnoreZeroValues(boolean flag) {
        boolean old = getIgnoreZeroValues();
        delegate.setIgnoreZeroValues(flag);
        firePropertyChange("ignoreZeroValues", old, getIgnoreZeroValues());
    }
    
    public boolean getIgnoreZeroValues() {
        return delegate.getIgnoreZeroValues();
    }
    
    public void setStartAngle(double angle) {
        double old = getStartAngle();
        delegate.setStartAngle(angle);
        firePropertyChange("startAngle", old, getStartAngle());
    }
    
    public double getStartAngle() {
        return delegate.getStartAngle();
    }
    
    //------------------------------------------------------- Non-public API
    @Override 
    protected PiePlot getPlot() {
        return (PiePlot)super.getPlot();
    }
    
    @Override
    protected Plot createPlot() {
        PiePlot plot = new PiePlot(createPieDataset());
        plot.setStartAngle(180);
        return plot;
    }
    
    protected PieDataset createPieDataset() {
        return new SwingPieDataset();
    }
    
    //adapts the PieModel to the JFreeChart PiePlot
    private final class SwingPieDataset extends AbstractDataset implements PieDataset {
        public Comparable getKey(int i) {
            return model == null ? null : model.getName(i);
        }

        public int getIndex(Comparable comparable) {
            for (int i=0; i<getItemCount(); i++) {
                Comparable c = getKey(i);
                if (c == comparable || (c != null && c.equals(comparable))) {
                    return i;
                }
            }
            return -1;
        }

        public List getKeys() {
            List<Comparable> keys = new ArrayList<Comparable>();
            for (int i=0; i<getItemCount(); i++) {
                keys.add(getKey(i));
            }
            return keys;
        }

        public Number getValue(Comparable comparable) {
            return getValue(getIndex(comparable));
        }

        public int getItemCount() {
            return model == null ? 0 : model.size();
        }

        public Number getValue(int i) {
            return model == null ? null : model.getValue(i);
        }
    }
}
