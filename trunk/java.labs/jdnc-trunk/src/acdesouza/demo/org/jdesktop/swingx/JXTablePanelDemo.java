package org.jdesktop.swingx;

import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.jdesktop.swingx.table.chart.ChartPlugin;
import org.jdesktop.swingx.table.exporter.ExcelExporterPlugin;

public class JXTablePanelDemo {
	private static final Logger logger = Logger.getLogger(JXTablePanelDemo.class);
	
	public static void main(String [] args){
		configLog4j();
//		configLaF();

		SwingUtilities.invokeLater(
			new Runnable(){
				public void run() {
					JFrame frame = new JFrame(JXTablePanel.class.getSimpleName() + " Demo");
					frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
					frame.addWindowListener(
							new WindowAdapter() {
								public void windowClosing(WindowEvent we) {
									System.exit(0);
								}
							}
					);
					frame.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
					frame.setMinimumSize(new Dimension(550, 500));
//					frame.setMinimumSize(new Dimension(400, 500));
					frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
					
					JXTablePanel qv = createJXTablePanel();
					frame.add(qv);
					
					frame.setLocationRelativeTo(null);
					
					logger.debug("Start Demo window...");
					frame.validate();
					frame.setVisible(true);
				}
			}
		);
	}
	private static JXTablePanel createJXTablePanel() {
		logger.debug("Create "+ JXTablePanel.class.getName());
/* 
 * *******************************************************************************************************************************************
 * *******************************************************************************************************************************************
 * Component creation example. And how to add plugins.
 * *******************************************************************************************************************************************
 */

		JXTablePanel qv = new JXTablePanel( "Client data", getClientes(), getTableColumnNames(), "JXTablePanel Demo App." );
		
//		qv.addPlugin(new JXTablePanelShowSelectedRowPlugin(), false);
		qv.addPlugin(new ChartPlugin(), false);
		qv.addPlugin(new ExcelExporterPlugin(), false);
/*
		EditorPlugin editorPlugin = new EditorPlugin();
		qv.addNotifiablePlugin(editorPlugin, false);
		editorPlugin.setEditorPanel(new ContatoEditorPanel());
		qv.addNotifiablePlugin(new CustomFilterPlugin(), false);
		qv.addNotifiablePlugin(new QuickFilterPlugin(), true);
*/
		qv.resizeAllColumns();
/* 
 * *******************************************************************************************************************************************
 * ****************************************************************************************************************************************** 
 * ****************************************************************************************************************************************** 
 */
		logger.debug(JXTablePanel.class.getName() + " successfully created.");
		return qv;
	}
	private static void configLaF() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch(Exception e) {
			logger.error("Default system LoolAndFeel can't be used. Using save-mode default LaF." + e);
		}
		
	}
	private static final void configLog4j() {
		URL url = JXTablePanelDemo.class.getClassLoader().getResource("log4j.properties");
		PropertyConfigurator.configure(url);
		logger.debug("Logging initialized.");
	}
	
	private static List<Contato> getClientes() {
		Contato c1;
		Calendar cal = Calendar.getInstance();

		List<Contato> data = new ArrayList<Contato>();

		cal.set(1982, 07, 28);
		c1 = new Contato("Antonio Carlos", cal.getTime());
		c1.setStatus(ContatoStatus.COMUM);
		c1.setTotal( new BigDecimal(45) );
		data.add( c1 );

		cal.set(1981, 10, 30);
		c1 = new Contato("Marina", cal.getTime());
		c1.setStatus(ContatoStatus.COMUM);
        c1.setTotal( new BigDecimal(35) );
		data.add( c1 );

		cal.set(1986, 11, 17);
		c1 = new Contato("Aline", cal.getTime());
		c1.setStatus(ContatoStatus.COMUM);
        c1.setTotal( new BigDecimal(70) );
		data.add( c1 );

		cal.set(1952, 9, 20);
		c1 = new Contato("Angela Maria", cal.getTime());
		c1.setStatus(ContatoStatus.ADMINISTRADOR);
        c1.setTotal( new BigDecimal(50) );
		data.add( c1 );

		cal.set(1947, 4, 20);
		c1 = new Contato("Carlos Luis", cal.getTime());
		c1.setStatus(ContatoStatus.ADMINISTRADOR);
        c1.setTotal( new BigDecimal(25) );
		data.add( c1 );

		return data;
	}

	private static final String STATUS = "Status";
	private static final String DATA_DE_CADASTRO = "Birth date";
	private static final String IDADE_DO_CLIENTE = "Age";
	private static final String NOME_DO_CLIENTE = "Name";
	private static final String TOTAL = "Total";
	private static Map<String, String> getTableColumnNames() {
		Map<String, String> paths = new LinkedHashMap<String, String>();

		paths.put("nome", NOME_DO_CLIENTE);
		paths.put("idade", IDADE_DO_CLIENTE);
		paths.put("nascimento", DATA_DE_CADASTRO);
		paths.put("status", STATUS);
		paths.put("total", TOTAL);

		return paths;
	}
}