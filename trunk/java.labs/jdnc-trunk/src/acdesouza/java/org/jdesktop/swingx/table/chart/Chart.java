package org.jdesktop.swingx.table.chart;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.jfree.chart.ChartPanel;


public class Chart {
	private static final Logger log = Logger.getLogger(Chart.class);
	
	private String title;

	private ChartAxis xAxis;
	private List<ChartAxis> yAxies;
	
	public Chart title(String chartTitle){
		this.title = chartTitle;
		return this;
	}
	
	public Chart xAxis( ChartAxis xAxis ){
		this.xAxis = xAxis;

		changeChartLineStrategyBasedOnXaxisType( xAxis.getValues().get(0).getClass() );

		return this;
	}

	public Chart yAxies( List<ChartAxis> yAxies ){
		this.yAxies = yAxies;

		return this;
	}
	public Chart addYaxis(ChartAxis yAxis){
		if( yAxies == null ) yAxies = new ArrayList<ChartAxis>();

		this.yAxies.add(yAxis);

		return this;
	}

	private ChartLineStrategy chartLineStrategy;
	private void changeChartLineStrategyBasedOnXaxisType(Class<?> classeDosObjetosDaColuna){
		if( classeDosObjetosDaColuna.isAssignableFrom(Double.class)
			|| classeDosObjetosDaColuna.isAssignableFrom(Float.class)
			|| classeDosObjetosDaColuna.isAssignableFrom(Long.class)
			|| classeDosObjetosDaColuna.isAssignableFrom(Integer.class) ){
			chartLineStrategy = new ChartLineStrategyNumberX();
		}else if( classeDosObjetosDaColuna.isAssignableFrom(Date.class)
			|| classeDosObjetosDaColuna.isAssignableFrom(Timestamp.class) ){
			chartLineStrategy = new ChartLineStrategyTimeX();
		}else{
			log.debug("Foi selecionado uma coluna, para o eixo X, que n�o era inteiro, double ou Date: "+ classeDosObjetosDaColuna);
			throw new IllegalStateException("O tipo de dados, da coluna selecionada para o eixo X, n�o � suportado pelo gerador de gr�fico: "+ classeDosObjetosDaColuna);
		}
	}
	
	/**
	 * Create a char with 3 lines in Y axis.
	 * 
	 * <code>
	 * new Chart()<br/>
	 * 	.{@link #title(chartTitle)}<br/>
	 * 	.{@link #xAxis(ChartAxis)}<br/>
	 * 	.{@link #addYaxis(ChartAxis)}<br/>
	 * 	.{@link #addYaxis(ChartAxis)}<br/>
	 *  .{@link #plotLineChart()}
	 * </code>
	 * @return
	 */
	public ChartPanel plotLineChart(){
        return chartLineStrategy.plot(title, xAxis, yAxies, true);
	}
}