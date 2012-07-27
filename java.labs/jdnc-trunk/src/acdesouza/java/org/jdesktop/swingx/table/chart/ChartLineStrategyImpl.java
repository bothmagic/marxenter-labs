package org.jdesktop.swingx.table.chart;

import java.awt.Color;
import java.util.List;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.general.Series;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.RectangleInsets;


public abstract class ChartLineStrategyImpl<XType> implements ChartLineStrategy {
	private static final Logger log = Logger.getLogger(ChartLineStrategyImpl.class);
	
	/**
	 * @see ChartPloter#plot(MatrizDados, int, List, boolean)
	 * @param tableView
	 * @param xIndex
	 * @param yIndexes
	 * @param ignoreNullValues
	 * @throws IllegalStateException Caso sej� selecionada uma coluna para o eixo X que n�o era Double ou Date.
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public ChartPanel plot(String title, ChartAxis xAxis, List<ChartAxis> yAxies, boolean ignoreNullValues) {

		JFreeChart jfreechart = createJFreeChart(title, xAxis, yAxies, ignoreNullValues);

		jfreechart.setBackgroundPaint(Color.white);

		XYPlot xyplot = (XYPlot)jfreechart.getPlot();
        xyplot.setBackgroundPaint(Color.lightGray);
        xyplot.setAxisOffset(new RectangleInsets(5D, 5D, 5D, 5D));
        xyplot.setDomainGridlinePaint(Color.white);
        xyplot.setRangeGridlinePaint(Color.white);

        adjustStandartTickUnits(xyplot, xAxis, yAxies);

        XYLineAndShapeRenderer xylineandshaperenderer = (XYLineAndShapeRenderer)xyplot.getRenderer();
        xylineandshaperenderer.setShapesVisible(true);
        xylineandshaperenderer.setShapesFilled(true);

		return new ChartPanel(jfreechart);
	}

	/**
	 * Ajustar a escala do gr�fico a partir dos tipos de dados dos eixos.
	 */
	@SuppressWarnings("unchecked")
	private void adjustStandartTickUnits(XYPlot xyplot, ChartAxis xAxis, List<ChartAxis> yAxies){
		NumberAxis numberaxis = (NumberAxis)xyplot.getRangeAxis();
        numberaxis.setAutoRangeIncludesZero(false);

		boolean hasDoubleAxis = false; 
		for (ChartAxis yAxis : yAxies) {
			if( yAxis.getValueClass().isAssignableFrom(Double.class) ){
				numberaxis.setStandardTickUnits( NumberAxis.createStandardTickUnits(Locale.getDefault()) );
				hasDoubleAxis = true;
			}
		}

        if( hasDoubleAxis ){
        	numberaxis.setStandardTickUnits( NumberAxis.createStandardTickUnits(Locale.getDefault()) );
		}else if( xAxis.getValueClass().isAssignableFrom(Integer.class) ){
			numberaxis.setStandardTickUnits( NumberAxis.createIntegerTickUnits(Locale.getDefault()) );
		}
	}
	
	protected final boolean canAddToChart(Object xValue, Double yValue, boolean ignoreNullValue){
		if (xValue == null) {
			if (!ignoreNullValue) throw new IllegalArgumentException("O eixo X possui dados com valores nulos.");
			return false;
		}

		if (yValue == null) {
			if (!ignoreNullValue) throw new IllegalArgumentException("O eixo Y possui dados com valores nulos.");
			return false;
		}

		return true;
	}
	
	protected XYDataset createDataset(ChartAxis<XType> xAxis, List<ChartAxis> yAxies, boolean ignoreNullValues) {
		XYDataset xyDataset = initDataset();
		
		for (ChartAxis yAxis : yAxies) {
			Series xyseries = initSeries( yAxis.getTitle() );
			for (int linhasEixoY = 0; linhasEixoY < yAxis.getValues().size(); linhasEixoY++) {
				XType valorX = null;
				Double valorY = null;
				try {
					valorX = getConvertXvalue( xAxis.getValues().get(linhasEixoY) );
					valorY = Double.valueOf(  String.valueOf( yAxis.getValues().get(linhasEixoY) )  );
				} catch (NumberFormatException e) {
					log.debug("Valores nulos est�o sendo ignorados.", e);
				}
				if ( canAddToChart(valorX, valorY, ignoreNullValues) ) {
					addParOrdenado(valorX, valorY, xyseries);
				}
			}// Valores do eixo Y atual.
			addSeriesInDataset(xyDataset, xyseries);
		}// Todos os eixos Y selecionados.

		return xyDataset;
	}

	protected abstract JFreeChart createJFreeChart(String title, ChartAxis<XType> xAxis, List<ChartAxis> yAxies, boolean ignoreNullValues);
	protected abstract XType getConvertXvalue(Object xValue);
	protected abstract XYDataset initDataset();
	protected abstract Series initSeries(String columnName);
	protected abstract void addParOrdenado( XType xValue, Double yValue, Series xyseries );
	protected abstract void addSeriesInDataset( XYDataset xyDataset, Series xySeries );
}