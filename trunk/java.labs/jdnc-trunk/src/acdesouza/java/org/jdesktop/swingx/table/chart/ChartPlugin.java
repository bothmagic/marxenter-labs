package org.jdesktop.swingx.table.chart;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JTable;

import org.jdesktop.swingx.JXTablePanel;
import org.jdesktop.swingx.JXTablePanelImage;
import org.jdesktop.swingx.table.JXTablePanelPlugin;


public class ChartPlugin implements JXTablePanelPlugin<JButton> {

	private JButton btnChart;
	public JButton getJToolBarAddOn() {
		if( btnChart==null ){
			btnChart = new JButton();
			btnChart.setIcon( JXTablePanelImage.CHART.getImage() );
			btnChart.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if(isOpened){
							qview.closePluginPane( getChartBuilder() );
						}else{
							qview.openPluginPane( getChartBuilder() );
						}
						isOpened = !isOpened;
					}
				}
			);
		}

		return btnChart;
	}

	private boolean isOpened=false;
	private ChartPanePlugin chartPaneBuilder;
	private ChartPanePlugin getChartBuilder(){
		if( chartPaneBuilder==null ){
			chartPaneBuilder = new ChartPanePlugin(qview);
		}
		
		return chartPaneBuilder;
	}

	private JXTablePanel qview;
	public void setJXTablePanel(JXTablePanel qview) {
		this.qview = qview;
	}

	private JTable qviewTable;
	public void setQueryViewTable(JTable qviewTable) {
		this.qviewTable = qviewTable;
	}
}
