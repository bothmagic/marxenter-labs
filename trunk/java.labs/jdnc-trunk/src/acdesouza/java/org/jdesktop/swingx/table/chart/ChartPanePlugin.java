package org.jdesktop.swingx.table.chart;

import info.clearthought.layout.TableLayout;
import info.clearthought.layout.TableLayoutConstraints;

import java.awt.BorderLayout;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import org.apache.log4j.Logger;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.JXTablePanel;
import org.jdesktop.swingx.LabelValueBean;
import org.jfree.chart.ChartPanel;

//TODO Remove dependency of JGoodies
import com.jgoodies.forms.factories.Borders;
import com.jgoodies.forms.factories.DefaultComponentFactory;
import org.jdesktop.swingx.JXTablePanelImage;


/**
 * Panel to select table columns for chart.
 * 
 * @author AC de Souza
 */
public class ChartPanePlugin extends JPanel {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = Logger.getLogger(ChartPanePlugin.class);

    private final JXTablePanel qview;
	private List<Integer> yIndex;
	
	//TODO Change this to a Map<String, Integer>
	private List<LabelValueBean<Integer>> colunasX = new ArrayList<LabelValueBean<Integer>>();
	private JLabel lblX;
	private JComboBox cboX;
	//TODO Change this to a Map<String, Integer>
	private List<LabelValueBean<Integer>> colunasY = new ArrayList<LabelValueBean<Integer>>();
	private JLabel lblY;
	private JComboBox cboY;
	private JButton btnAdd;
	private JButton btnRemove;
	private JButton btnLineChart;

	public ChartPanePlugin(final JXTablePanel qview) {
		yIndex = new ArrayList<Integer>();
		this.qview = qview;

		setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		setInheritsPopupMenu(true);
		setMinimumSize(new Dimension(485, 230));
		setPreferredSize(new Dimension(493, 264));
		setMaximumSize(new Dimension(12800, 8000));
		setBorder(Borders.DIALOG_BORDER);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		add( getPainelDeSelecaoDosEixosParaGrafico() );
		add( getPainelEixosSelecionadosParaGrafico() );
	}

	private JPanel painelDeSelecaoDosEixosParaGrafico;
	private JPanel getPainelDeSelecaoDosEixosParaGrafico(){
		if(painelDeSelecaoDosEixosParaGrafico==null){
			painelDeSelecaoDosEixosParaGrafico = new JPanel();
			painelDeSelecaoDosEixosParaGrafico.setMaximumSize(new Dimension(2147483647, 2147483647));
			painelDeSelecaoDosEixosParaGrafico.setPreferredSize(new Dimension(272, 60));
			painelDeSelecaoDosEixosParaGrafico.setMinimumSize(new Dimension(272, 60));
			painelDeSelecaoDosEixosParaGrafico.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);

			findColunasCandidatasAEixo();

			JLabel lblX = new JLabel(ChartPanePluginLiteral.CHART_ABSCISSAS_X);
			JLabel lblY = new JLabel(ChartPanePluginLiteral.CHART_ORDENADAS_Y);

			painelDeSelecaoDosEixosParaGrafico.setLayout(
				new TableLayout(
					new double[][] {
						{200, 200, 42},
						{TableLayout.PREFERRED, TableLayout.PREFERRED}
					}
				)
			);
			((TableLayout)painelDeSelecaoDosEixosParaGrafico.getLayout()).setHGap(5);
			((TableLayout)painelDeSelecaoDosEixosParaGrafico.getLayout()).setVGap(5);
	
			lblX = new JLabel();
			lblX.setText(ChartPanePluginLiteral.CHART_ABSCISSAS_X);
			painelDeSelecaoDosEixosParaGrafico.add(lblX, new TableLayoutConstraints(0, 0, 0, 0, TableLayoutConstraints.FULL, TableLayoutConstraints.FULL));
	
			lblY = new JLabel();
			lblY.setText(ChartPanePluginLiteral.CHART_ORDENADAS_Y);
			painelDeSelecaoDosEixosParaGrafico.add(lblY, new TableLayoutConstraints(1, 0, 1, 0, TableLayoutConstraints.FULL, TableLayoutConstraints.FULL));
	
			painelDeSelecaoDosEixosParaGrafico.add(getBtnAdd(), new TableLayoutConstraints(2, 0, 2, 0, TableLayoutConstraints.FULL, TableLayoutConstraints.FULL));
	
			painelDeSelecaoDosEixosParaGrafico.add(getCboX(), new TableLayoutConstraints(0, 1, 0, 1, TableLayoutConstraints.FULL, TableLayoutConstraints.FULL));
			painelDeSelecaoDosEixosParaGrafico.add(getCboY(), new TableLayoutConstraints(1, 1, 1, 1, TableLayoutConstraints.FULL, TableLayoutConstraints.FULL));
			painelDeSelecaoDosEixosParaGrafico.add(getBtnRemove(), new TableLayoutConstraints(2, 1, 2, 1, TableLayoutConstraints.FULL, TableLayoutConstraints.FULL));
		}
		return painelDeSelecaoDosEixosParaGrafico;
	}


	/**
	 * Gerar a lista de campos que podem ser utilizados para montar o gr�fico.<br/>
	 * Todos os campos eleg�veis devem ser {@link Number} ou {@link java.util.Date}
	 */
	private void findColunasCandidatasAEixo() {
		//Adicionar as colunas do tipo Number em ambos os eixos
		Set<Integer> colunasNumber = new HashSet<Integer>(); 
		colunasNumber.addAll( getColumnIndexsByColumnType(Number.class) );
		for (Integer columnIndex : colunasNumber) {
			colunasX.add( new LabelValueBean<Integer>(qview.getColumnName(columnIndex), Integer.valueOf(columnIndex)) );
			colunasY.add( new LabelValueBean<Integer>(qview.getColumnName(columnIndex), Integer.valueOf(columnIndex)) );
		}
		
		//Adicionar as colunas do tipo Date somente no eixo X
		Set<Integer> colunasDate = new HashSet<Integer>(); 
		colunasDate.addAll( getColumnIndexsByColumnType(java.util.Date.class) );
		for (Integer columnIndex : colunasDate) {
			colunasX.add( new LabelValueBean<Integer>(qview.getColumnName(columnIndex), Integer.valueOf(columnIndex)) );
		}
	
		Collections.sort(colunasY);
		Collections.sort(colunasX);
	}

	private List<Integer> getColumnIndexsByColumnType(Class<?> clazz){
		List<Integer> columnIndexes = new ArrayList<Integer>();
		
		int columnCount = qview.getColumnCount();
		for(int i=0; i < columnCount; i++){
			if( clazz.isAssignableFrom(qview.getColumnClass(i)) ){
				columnIndexes.add(i);
			}
		}
	
		return columnIndexes;
	}

	private JComboBox getCboX(){
		if( cboX==null ){
			cboX = createAxisComboBoxes(colunasX);
		}
		return cboX;
	}
	private JComboBox getCboY(){
		if( cboY==null ){
			cboY = createAxisComboBoxes(colunasY);
		}
		return cboY;
	}
	private JComboBox createAxisComboBoxes(List<LabelValueBean<Integer>> axis) {
		final JComboBox cboAxis;
		cboAxis = new JComboBox(axis.toArray());
		cboAxis.setMaximumSize(new Dimension(450, 22));
		cboAxis.setMinimumSize(new Dimension(110, 22));
		cboAxis.setPreferredSize(new Dimension(200, 22));
		
		return cboAxis;
	}


	private JButton getBtnAdd(){
		if( btnAdd==null ){
			btnAdd = new JButton();
	
			btnAdd.setToolTipText(ChartPanePluginLiteral.CHART_BT_ADD_TIP);
			btnAdd.setIcon(JXTablePanelImage.FILTER_ADD.getImage());
			btnAdd.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					adicionarEixoAoGrafico();
				}
			});
		}
	
		return btnAdd;
	}

	private JPanel painelEixosSelecionadosParaGrafico;
	private void adicionarEixoAoGrafico() {
		Integer newYIndex = (Integer)((LabelValueBean)cboY.getSelectedItem()).getValue();
		
		if( yIndex.contains(newYIndex) ){
			JOptionPane.showMessageDialog(this, ChartPanePluginLiteral.CHART_TITULO_PAR_ORDENADO_EXISTENTE, ChartPanePluginLiteral.CHART_ADICIONAR_PAR_ORDENADO, JOptionPane.INFORMATION_MESSAGE);
		}else{
			DefaultTableModel model = (DefaultTableModel) getTblAxisList().getModel();
			model.addRow( new Object[] {(LabelValueBean)cboX.getSelectedItem(), (LabelValueBean)cboY.getSelectedItem()} );
			yIndex.add( (Integer)((LabelValueBean)cboY.getSelectedItem()).getValue() );
		}
	}
	class ChartAxisPair {
		private LabelValueBean yAxis;
		private LabelValueBean xAxis;
		public ChartAxisPair(LabelValueBean xAxis, LabelValueBean yAxis) {
			super();
			yAxis = yAxis;
			xAxis = xAxis;
		}
	}

	private JButton getBtnRemove(){
		if( btnRemove == null ){
			btnRemove = new JButton();
			btnRemove.setEnabled(false);
			btnRemove.setIcon(JXTablePanelImage.FILTER_DELETE.getImage());
			btnRemove.setToolTipText(ChartPanePluginLiteral.CHART_BT_REMOVER_TIP);
			btnRemove.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						removerEixoDoGrafico();
					}
				}
			);
		}
		
		return btnRemove;
	}
	private void removerEixoDoGrafico(){
		DefaultTableModel model = (DefaultTableModel) getTblAxisList().getModel();

		int[] linhasSelecionadas = getTblAxisList().getSelectedRows();
		for (int i=linhasSelecionadas.length-1; i >= 0; i-- ) {
			//Retirar do cache
			yIndex.remove( linhasSelecionadas[i] );

			//Retirar da tabela.
			model.removeRow( linhasSelecionadas[i] );
		}
	}

	/**
	 * Habilitar e desabilitar os bot�es da tela, relacionados com os dados na tabela de eixos do gr�fico.
	 */
	private void tratarAcessoBotoes(){
		//Se tiver algum par ordenado, habilitar os bot�es de cria��o de gr�ficos.
		getBtnLineChart().setEnabled( hasAxisChoosed() );

		//Se n�o tiver nenhum par ordenado na lista desabilita os bot�es de exclus�o e os de cria��o de gr�ficos.
		getCboX().setEnabled( !hasAxisChoosed() );

		//Se n�o tiver uma linha selecionada desabilita o bot�o de excluir.
		getBtnRemove().setEnabled( hasAxisSelected() );
	}
	private boolean hasAxisSelected(){
		return getTblAxisList().getSelectedRowCount() > 0;
	}
	private boolean hasAxisChoosed() {
		return getTblAxisList().getRowCount() > 0;
	}

	private JPanel getPainelEixosSelecionadosParaGrafico(){
		if(painelEixosSelecionadosParaGrafico == null){
			painelEixosSelecionadosParaGrafico = new JPanel();
			painelEixosSelecionadosParaGrafico.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
			painelEixosSelecionadosParaGrafico.setLayout(new BorderLayout(5, 5));
	
			DefaultComponentFactory compFactory = DefaultComponentFactory.getInstance();
			JComponent sptFiltro = compFactory.createSeparator(ChartPanePluginLiteral.CHART_SEPARATOR_LABEL);
			painelEixosSelecionadosParaGrafico.add(sptFiltro, BorderLayout.NORTH);
	
			painelEixosSelecionadosParaGrafico.add(new JScrollPane(getTblAxisList()), BorderLayout.CENTER);
			painelEixosSelecionadosParaGrafico.add(new JScrollPane(getBtnLineChart()), BorderLayout.EAST);
		}
		
		return painelEixosSelecionadosParaGrafico;
	}

	private JXTable tblAxisList;
	private JXTable getTblAxisList(){
		if(tblAxisList == null){
			tblAxisList = new JXTable( new DefaultTableModel(new Object[0][0], getColumnNames()) );
//			tblAxisList.setModel( new BeanTableModel(new ArrayList(), new HashMap<String, String>() ) );

			tblAxisList.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			tblAxisList.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
			tblAxisList.setInheritsPopupMenu(true);
			tblAxisList.setIntercellSpacing(new Dimension(7, 1));
//			tblAxisList.setPreferredScrollableViewportSize(new Dimension(450, 100));
			
			tblAxisList.setHorizontalScrollEnabled(true);

			//Remover o par-ordenado quando selecionar um item na tabela a apertar "Delete"
			tblAxisList.addKeyListener(
				new KeyAdapter() {
					public void keyPressed(KeyEvent e) {
						if(e.getKeyCode() == KeyEvent.VK_DELETE){
							removerEixoDoGrafico();
						}
					}
				}
			);

			//Alterar o acesso aos bot�es quando clicar em uma linha da lista de pares ordenados.
			ListSelectionModel rowSM = tblAxisList.getSelectionModel();
			rowSM.addListSelectionListener(
				new ListSelectionListener(){
					/**
					 * @see javax.swing.event.ListSelectionListener#valueChanged(javax.swing.event.ListSelectionEvent)
					 */
					public void valueChanged(ListSelectionEvent e) {
						tratarAcessoBotoes();
					}
				}
			);
		}
		return tblAxisList;
	}
	private Object[] getColumnNames(){
		return new Object[]{
			ChartPanePluginLiteral.CHART_ABSCISSA
			, ChartPanePluginLiteral.CHART_ORDENADA
		};
	}

	private JButton getBtnLineChart(){
		if( btnLineChart==null ){
			btnLineChart = new JButton();
			btnLineChart.setIcon( JXTablePanelImage.CHART_LINE.getImage() );
			btnLineChart.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						final String title = ChartPanePluginLiteral.CHART_BTN;
						final String msg = ChartPanePluginLiteral.CHART_MSG_ERRO_SELECIONAR_EIXO;
						try {
							qview.addToContentPane(title, plot(), true);
						} catch (IllegalArgumentException ex) {
							logger.warn(ex);
							showMessageWARN(title, msg);
						} catch (IllegalStateException ex) {
							logger.warn(ex);
							showMessageWARN(title, ex.getMessage());
						}
					}
				}
			);
		}
		return btnLineChart;
	}

	/**
	 * TODO Refactoring para receber o tipo de gr�fico(Linhas, barras etc) que deve ser utilizado.
	 *  
	 * @return O painel do JFreeChart com o gr�fico desenhado.
	 * @throws IllegalArgumentException Quando um item da lista estiver nulo.
	 * @throws IllegalStateException Quanto n�o tiver selecionado nenhum par de eixos.
	 */
	public ChartPanel plot(){
		if( !hasAxisChoosed() ){
			final String msg = ChartPanePluginLiteral.CHART_MSG_ERRO_SELECIONAR_EIXO;
			throw new IllegalStateException(msg);
		}
		ChartPanel chartpanel = new Chart()
			.title(qview.getTitle())
			.xAxis(getXaxis())
			.yAxies(getYaxies())
			.plotLineChart();

		return chartpanel;
	}

	@SuppressWarnings("unchecked")
	private ChartAxis getXaxis(){
		int xColumn = (Integer)((LabelValueBean)getCboX().getSelectedItem()).getValue();
		List xValues = new ArrayList();
		for( int xRow=0; xRow < qview.getRowCount(); xRow++ ){
			xValues.add( qview.getValueAt(xRow, xColumn) );
		}
		
		ChartAxis xAxis = new ChartAxis(qview.getColumnName(xColumn), xValues);

		return xAxis;
	}
	
	private List<ChartAxis> getYaxies(){
		List<ChartAxis> yAxies = new ArrayList<ChartAxis>();

		for (int ySelectedColumns=0; ySelectedColumns < yIndex.size(); ySelectedColumns++) {
			yAxies.add(  getYaxis( yIndex.get(ySelectedColumns) )  );
		}

		return yAxies;
	}
	private ChartAxis<?> getYaxis(int yColumn){
		int xColumn = yColumn;
		List xValues = new ArrayList();
		for( int xRow=0; xRow < qview.getRowCount(); xRow++ ){
			xValues.add( qview.getValueAt(xRow, xColumn) );
		}
		
		ChartAxis xAxis = new ChartAxis(qview.getColumnName(xColumn), xValues);
		
		return xAxis;
	}

	private void showMessageWARN(String titulo, String mensagem) {
		showMessage(mensagem, titulo, JOptionPane.WARNING_MESSAGE);
	}

	private void showMessage(String mensagem, String titulo, int messageType) {
		JOptionPane.showMessageDialog(qview.getRootPane(), mensagem, titulo, messageType);
	}
}