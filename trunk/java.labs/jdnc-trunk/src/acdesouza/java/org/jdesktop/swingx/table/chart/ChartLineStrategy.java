package org.jdesktop.swingx.table.chart;

import java.util.List;

import org.jfree.chart.ChartPanel;


public interface ChartLineStrategy {
	public ChartPanel plot(String title, ChartAxis xAxis, List<ChartAxis> yAxies, boolean ignoreNullValues);
}
