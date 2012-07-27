package org.jdesktop.swingx.table.chart;

import java.util.List;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.general.Series;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class ChartLineStrategyNumberX extends ChartLineStrategyImpl<Double> {

	public JFreeChart createJFreeChart(String title, ChartAxis<Double> xAxis, List<ChartAxis> yAxies, boolean ignoreNullValues) {
		return ChartFactory.createXYLineChart(title, xAxis.getTitle(), "Escala de valores", createDataset(xAxis, yAxies, ignoreNullValues), PlotOrientation.VERTICAL, true, true, false);
	}

    public Double getConvertXvalue(Object xValue) {
	    return Double.valueOf( String.valueOf(xValue) );
    }
	
	public XYDataset initDataset(){
		return new XYSeriesCollection();
	}

    public Series initSeries(String columnName) {
	    return new XYSeries( columnName );
    }

    public void addParOrdenado(Double valorX, Double valorY, Series xyseries) {
		((XYSeries)xyseries).add(valorX, valorY);
    }

    public void addSeriesInDataset(XYDataset xyDataset, Series xySeries) {
		((XYSeriesCollection)xyDataset).addSeries((XYSeries)xySeries);
    }	
}