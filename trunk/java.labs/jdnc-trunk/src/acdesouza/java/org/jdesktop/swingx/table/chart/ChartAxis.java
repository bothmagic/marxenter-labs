package org.jdesktop.swingx.table.chart;

import java.util.List;

public class ChartAxis<E> {
	private String title;
	private List<E> values;

	public ChartAxis(String title, List<E> values) {
		super();
		this.title = title;
		this.values = values;
	}

	public String getTitle() {
		return title;
	}

	public List<E> getValues() {
		return values;
	}
	
	public Class<?> getValueClass(){
		if( values==null || values.isEmpty() ) throw new IllegalStateException("Lista de valores para o eixo X est� vazia.");
		return values.get(0).getClass();
	}
}