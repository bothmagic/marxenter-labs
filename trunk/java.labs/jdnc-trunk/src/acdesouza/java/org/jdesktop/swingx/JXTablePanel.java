package org.jdesktop.swingx;


import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import org.apache.log4j.Logger;
import org.jdesktop.swingx.decorator.HighlighterFactory;
import org.jdesktop.swingx.table.BeanTableModel;
import org.jdesktop.swingx.table.JXTablePanelNotifiablePlugin;
import org.jdesktop.swingx.table.JXTablePanelPlugin;



public class JXTablePanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(JXTablePanel.class);

	private String tableTitle;
	private String fonteDosDadosDaTabela;
	private List<?> data;
	private Map<String, String> beanPathsAndTitles;

	public JXTablePanel(final String tableTitle, final List<?> data, final Map<String, String> beanPathsAndTitles, final String fonteDosDadosDaTabela){
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		this.tableTitle = tableTitle;
		this.fonteDosDadosDaTabela = fonteDosDadosDaTabela;
		this.data = data;
		this.beanPathsAndTitles = beanPathsAndTitles;

		addToContentPane(getTitle(), getTable(), false);

		add( getDefaultToolBar() );
		add( getPluginPropertiesPanel() );

		add( getContentPane() );
	}

	private JToolBar defaultToolBar;
	private JToolBar getDefaultToolBar() {
		if( defaultToolBar == null ){
			defaultToolBar = new JToolBar();
			defaultToolBar.setFloatable(false);
			defaultToolBar.setMaximumSize(new Dimension(Short.MAX_VALUE, 32));
		}

		return defaultToolBar;
	}

	private JPanel pluginPropertiesPanel;
	private JPanel getPluginPropertiesPanel(){
		if( pluginPropertiesPanel==null ){
			pluginPropertiesPanel = new JPanel();
			BoxLayout experimentLayout = new BoxLayout(pluginPropertiesPanel, BoxLayout.PAGE_AXIS);
			pluginPropertiesPanel.setLayout(experimentLayout);
			pluginPropertiesPanel.setBackground(Color.GREEN);
		}
		
		return pluginPropertiesPanel;
	}
	public final void openPluginPane(JComponent content) {
		getPluginPropertiesPanel().add(content);
		getPluginPropertiesPanel().invalidate();
		getPluginPropertiesPanel().revalidate();
	}
	public final void closePluginPane(JComponent content) {
		getPluginPropertiesPanel().remove(content);
		getPluginPropertiesPanel().invalidate();
		getPluginPropertiesPanel().revalidate();
	}

	private JTabbedPane contentPane;
	private JTabbedPane getContentPane() {
		if (contentPane == null) {
			contentPane = new JTabbedPane();
			contentPane.setOpaque(true);
			contentPane.setAlignmentX(CENTER_ALIGNMENT);
			contentPane.setTabPlacement(JTabbedPane.TOP);
			contentPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		}

		return contentPane;
	}

	public final void addToContentPane(final String tabTitle, final JComponent content, final boolean isClosable) {
/*
		final DocumentComponent aba = new DocumentComponent(new JScrollPane(content), tabTitle);
*/
		final int nextChartTab = getContentPane().getTabCount() +1;
		final String finalTabTitle = tabTitle +" ("+ nextChartTab +")";

//		aba.setClosable(isClosable);

		getContentPane().add( finalTabTitle, new JScrollPane(content) );
		getContentPane().setSelectedIndex(getContentPane().getTabCount() -1);
	}

	private JXTable tblData;
	private BeanTableModel model;
	private JXTable getTable(){
		if( tblData == null ){

			model = new BeanTableModel(data, beanPathsAndTitles);
			tblData = new JXTable(model);
			tblData.setSortable(true);

			tblData.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			tblData.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
			tblData.setInheritsPopupMenu(true);
			tblData.setIntercellSpacing(new Dimension(7, 1));

			tblData.setHorizontalScrollEnabled(true);

//			tblData.setColumnControlVisible(true);

			//Stripe table rows
			tblData.addHighlighter(
				HighlighterFactory.createAlternateStriping()
			);
		}

		return tblData;
	}

	public final void resizeAllColumns(){
//		TableUtils.autoResizeAllColumns(getTable());
	}

	private void addComponentInDefaultToolbar(JComponent newComponent, boolean hasSeparator) {
		if( hasSeparator ) getDefaultToolBar().addSeparator();
		getDefaultToolBar().add(newComponent);
	}

	public final void addPlugin(JXTablePanelPlugin<?> plugin, boolean shouldAddSeparator){
		plugin.setJXTablePanel(this);
		addComponentInDefaultToolbar(plugin.getJToolBarAddOn(), shouldAddSeparator);
		
		if( plugin instanceof TableModelDecoratorPlugin ){
			addTableModelDecoratorPlugin( (TableModelDecoratorPlugin)plugin );
		}
	}

	private List<TableModelDecoratorPlugin> listTableModelDecoratorPlugin;
	private final void addTableModelDecoratorPlugin(final TableModelDecoratorPlugin plugin){
		if( listTableModelDecoratorPlugin==null ) listTableModelDecoratorPlugin = new ArrayList<TableModelDecoratorPlugin>();
		
		listTableModelDecoratorPlugin.add(plugin);
	}

	private List<JXTablePanelNotifiablePlugin<?>> displayPlugins;
	public final void addNotifiablePlugin(final JXTablePanelNotifiablePlugin<?> plugin, boolean shouldAddSeparator){
		addPlugin(plugin, shouldAddSeparator);

		if( displayPlugins == null ) {
			displayPlugins = new ArrayList<JXTablePanelNotifiablePlugin<?>>();
		}
		addPluginAsTableModelListener(plugin);
		addPluginAsSelectionModelListener(plugin);

		displayPlugins.add(plugin);
	}
	private void addPluginAsTableModelListener(final JXTablePanelNotifiablePlugin<?> plugin) {
		TableModel _model = getTable().getModel();
		_model.addTableModelListener(
				new TableModelListener(){
					public void tableChanged(TableModelEvent e) {
						logger.debug("Notify plugin "+ plugin.getClass().getSimpleName() + " about SelectionModel event.");
						plugin.notifyTableModelChange();
					}
				}
		);
	}
	private void addPluginAsSelectionModelListener(final JXTablePanelNotifiablePlugin<?> plugin) {
		ListSelectionModel _model = getTable().getSelectionModel();
		_model.addListSelectionListener(
			new ListSelectionListener(){
				public void valueChanged(ListSelectionEvent e) {
					//http://www.java2s.com/Code/JavaAPI/javax.swing.text/ListSelectionEventgetValueIsAdjusting.htm
					if( !e.getValueIsAdjusting() ){
						logger.debug("Notify plugin "+ plugin.getClass().getSimpleName() + " about SelectionModel event.");
						plugin.notifySelectionModelChange();
					}
				}
			}
		);
	}

	public final String getTitle() {
		return tableTitle;
	}
	public final String getFonte(){
		return fonteDosDadosDaTabela;
	}
	public final void addRow(Object rowData){
		model.addRow(rowData);
	}
	public final void removeRow(int rowIndex){
		model.removeRow(rowIndex);
	}

	public void removeRowSelection(){
		int rowCount = getTable().getModel().getRowCount();
    	if( rowCount > 0 ){
    		getTable().removeRowSelectionInterval(0, rowCount - 1);
    	}
    }

	public Object getSelectedRow() {
		int selectedRow = getTable().getSelectedRow();
		logger.debug("Selected row, ignoring decorators: "+ selectedRow);

		if( selectedRow >= 0 ){
			if( listTableModelDecoratorPlugin != null && !listTableModelDecoratorPlugin.isEmpty() ) {
				for (TableModelDecoratorPlugin plugin : listTableModelDecoratorPlugin) {
					selectedRow = plugin.getActualSelectedRow(selectedRow);
					logger.debug("Selected row, after decorator("+ plugin.getClass().getName() +"): "+ selectedRow);
				}
			}

			logger.debug("Selected row: "+ selectedRow);
			return model.getValueAt(selectedRow);
		}else{
			return null;
		}
	}

	/*
	 * Let those delegates methods to avoid this:
     * <a href="http://forums.java.net/jive/thread.jspa?messageID=342181#342181">[...]could allow the classes to escape into the wild[...]</a>
     *
     * The point is:
     * Someone could get a reference to the JXTable and preventing Garbage Collector to remove an JXTablePanel's instance.
	 */

	/**
	 * @return Count of visible columns in table.
	 */
	public int getColumnCount() {
		return tblData.getModel().getColumnCount();
	}
	public Class<?> getColumnClass(int i) {
		return tblData.getModel().getColumnClass(i);
	}
	public String getColumnName(Integer columnIndex) {
		return tblData.getColumnName(columnIndex);
	}

	/**
	 * @return Count of visible rows in table.
	 */
	public int getRowCount() {
		return tblData.getRowCount();
	}
	public Object getValueAt(int row, int column) {
		return tblData.getValueAt(row, column);
	}
	public String getColumnName(int columnIndex) {
		return tblData.getColumnName(columnIndex);
	}
}