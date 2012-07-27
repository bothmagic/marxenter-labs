package org.jdesktop.swingx.table.chart;

import java.util.Date;
import java.util.List;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.Series;
import org.jfree.data.time.Month;
import org.jfree.data.time.TimePeriodAnchor;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;

public class ChartLineStrategyTimeX extends ChartLineStrategyImpl<Date> {

	public JFreeChart createJFreeChart(String title, ChartAxis<Date> xAxis, List<ChartAxis> yAxies, boolean ignoreNullValues) {
		return ChartFactory.createTimeSeriesChart(title, xAxis.getTitle(), "Escala de valores", createDataset(xAxis, yAxies, ignoreNullValues), true, true, false);
	}

	@Override
	public XYDataset createDataset(ChartAxis<Date> xAxis, List<ChartAxis> yAxies, boolean ignoreNullValues) {
		TimeSeriesCollection xyseriescollection = (TimeSeriesCollection)super.createDataset(xAxis, yAxies, ignoreNullValues);
		//xyseriescollection.setDomainIsPointsInTime(true);
		xyseriescollection.setXPosition(TimePeriodAnchor.MIDDLE);

		return xyseriescollection;
	}

    public Date getConvertXvalue(Object xValue) {
	    return (Date)xValue;
    }

    public XYDataset initDataset() {
	    return new TimeSeriesCollection();
    }

    public Series initSeries(String columnName) {
	    return new TimeSeries( columnName, org.jfree.data.time.Month.class);
    }

    public void addParOrdenado(Date valorX, Double valorY, Series xyseries) {
		((TimeSeries)xyseries).addOrUpdate(new Month(valorX), valorY);
    }

    public void addSeriesInDataset(XYDataset xyDataset, Series xySeries) {
		((TimeSeriesCollection)xyDataset).addSeries((TimeSeries)xySeries);
    }
}