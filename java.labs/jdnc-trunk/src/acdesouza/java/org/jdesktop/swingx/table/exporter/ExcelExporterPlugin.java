package org.jdesktop.swingx.table.exporter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.Region;
import org.jdesktop.swingx.JXTablePanel;
import org.jdesktop.swingx.table.JXTablePanelPlugin;

import org.jdesktop.swingx.JXTablePanelImage;

/**
 * Plugin respons�vel pela gera��o do arquivo Excel.
 * Este plugin ir� criar um �cone na barra de ferramentas da {@link JXTablePanel}.
 * 
 * @author AC de Souza
 */
public class ExcelExporterPlugin implements JXTablePanelPlugin<JButton> {
	private static final Logger log = Logger.getLogger(ExcelExporterPlugin.class);

    private JXTablePanel jxTablePanel;
    public void setJXTablePanel(JXTablePanel qview) {
    	this.jxTablePanel = qview;
	}

    private JButton btnExportExcel;
	public JButton getJToolBarAddOn() {
		if (btnExportExcel == null) {
			btnExportExcel = new JButton();
			btnExportExcel.setIcon( JXTablePanelImage.EXPORTAR_EXCEL.getImage() );

			btnExportExcel.addActionListener(
				new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent e) {
						exportToExcel();
					}
				}
			);
		}
		return btnExportExcel;
	}

	private String exportFilePath;
	private void exportToExcel(){
		if( hasUserSavedFile() ) {
			String fileName = getSaveDialog().getSelectedFile().getName();
			if( fileName!=null ){
				exportFilePath = getSaveDialog().getSelectedFile().getAbsolutePath();

				exportFilePath = addXLSExtensionIfUserDidNot(exportFilePath);

				export();

				showMessageINFO(ExcelExporterPluginLiteral.EXPORTER_EXCEL_OPEN_DIALOG, ExcelExporterPluginLiteral.EXPORTER_EXCEL_MSG_SUCESS);

				log.debug("Exporta��o executada com sucesso.");
			}
		} else {
			log.debug("User canceled export.");
		}
	}
	private boolean hasUserSavedFile(){
		return JFileChooser.APPROVE_OPTION == getSaveDialog().showSaveDialog(jxTablePanel);
	}
	private String addXLSExtensionIfUserDidNot(String fullPath) {
		String fileExtension = ".xls";
		if( fullPath.lastIndexOf(fileExtension) == -1 )
			return fullPath.concat(fileExtension);
		else
			return fullPath;
	}

	private JFileChooser dlgExcelExporter;
	private JFileChooser getSaveDialog() {
		if( dlgExcelExporter==null ){
			ExcelExporterPluginLiteral.traduzirJFileChooser_pt_BR();
			dlgExcelExporter = new JFileChooser();
		    dlgExcelExporter.setDialogTitle(ExcelExporterPluginLiteral.EXPORTER_EXCEL_OPEN_DIALOG);
		    dlgExcelExporter.setCurrentDirectory( new File(getDesktopPath()) );
		}

		return dlgExcelExporter;
	}
    private String getDesktopPath(){
		return System.getProperty("user.home") + System.getProperty("file.separator") + "Desktop";
    }

	private void showMessageINFO(String titulo, String mensagem) {
		showMessage(mensagem, titulo, JOptionPane.INFORMATION_MESSAGE);
	}
	private void showMessage(String mensagem, String titulo, int messageType) {
		JOptionPane.showMessageDialog(jxTablePanel.getRootPane(), mensagem, titulo, messageType);
	}

	/**
	 * Create XLS file.
	 */
	private void export() {
		HSSFWorkbook workBook = new HSSFWorkbook();
		String sheetName = applySheetNameRestrictions();

		HSSFSheet sheet = workBook.createSheet(sheetName);
		short rowCount = 0;

		rowCount = getHeader(workBook, sheet, rowCount);

		rowCount = getTitle(workBook, sheet, rowCount);

		getData(workBook, sheet, rowCount);

		saveToFile(workBook);
	}
	private String applySheetNameRestrictions() {
		String sheetName = jxTablePanel.getFonte();
		sheetName = sheetName.replaceAll("(\\s|\\/|\\?|\\|\\[|\\])", "_");
		if (sheetName.length() > 31) {
		    sheetName = sheetName.substring(0, 30);
		}
		return sheetName;
    }

	/**
	 * FIXME Throw IO explanation error.
     * @param wb
     */
    private void saveToFile(HSSFWorkbook wb) {
	    FileOutputStream fileOut;
		try {
			log.debug("Generating xls file...");
			if(exportFilePath == null){
				exportFilePath = "jxtablepanel_excel_export.xls";
				fileOut = new FileOutputStream(exportFilePath);
			}else{
				fileOut = new FileOutputStream(exportFilePath);
			}
			wb.write(fileOut);
			fileOut.close();
			log.info( "File created in: " + new File(exportFilePath).getAbsolutePath() );
		} catch (FileNotFoundException e) {
			log.error("Error generating file.", e);
		} catch (IOException e) {
			log.error("Error generating file.", e);
		}
    }

	/**
     * @param wb
     * @param planilha
     * @param rowCount
     */
    private void getData(HSSFWorkbook wb, HSSFSheet planilha, short rowCount) {
	    HSSFRow row;
	    HSSFRichTextString strRichText;
	    HSSFCell cell;
	    log.debug("Fulfilling sheet data.");
		for (short rows = 0; rows < jxTablePanel.getRowCount(); rows++) {
			row = planilha.createRow((short) rows + rowCount);
			for (short columns = 0; columns < jxTablePanel.getColumnCount(); columns++) {
				cell = row.createCell((short) columns);
				Object valorCelula = jxTablePanel.getValueAt(rows, columns);

				if (valorCelula == null) {
					strRichText = new HSSFRichTextString("");
					cell.setCellValue(strRichText);
				} else if (valorCelula instanceof Date) {
					HSSFCellStyle cellStyle = wb.createCellStyle();
					//FIXME Avaliar uma forma de pegar o formato do locale atual.
					cellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy"));
					cell.setCellValue((Date) valorCelula);
					cell.setCellStyle(cellStyle);
				} else if (valorCelula instanceof Integer) {
					cell.setCellValue(Integer.parseInt(valorCelula + ""));
				} else if (valorCelula instanceof Double) {
					cell.setCellValue(Double.parseDouble(valorCelula + ""));
				} else {
					strRichText = new HSSFRichTextString(valorCelula + "");
					cell.setCellValue(strRichText);
				}
			}
		}

		//Formatar a largura das colunas.
		log.debug("Formatar a largura das colunas.");
		for (short columns = 0; columns < jxTablePanel.getColumnCount(); columns++) {
			planilha.autoSizeColumn(columns);
		}
    }

	/**
     * @param wb
     * @param planilha
     * @param rowCount
     * @return
     */
    private short getTitle(HSSFWorkbook wb, HSSFSheet planilha, short rowCount) {
	    HSSFRow row;
	    HSSFRichTextString strRichText;
	    HSSFFont font;
	    HSSFCell cell;
	    log.debug("Preenchimento dos títulos da planilha.");

		row = planilha.createRow(rowCount++);
		for (short columns = 0; columns < jxTablePanel.getColumnCount(); columns++) {
			cell = row.createCell((short) columns);
			String nomeDaColuna = jxTablePanel.getColumnName(columns);

			HSSFCellStyle cellStyle = wb.createCellStyle();
			cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			cell.setCellStyle(cellStyle);

			strRichText = new HSSFRichTextString(nomeDaColuna + "");
			font = wb.createFont();
			font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			strRichText.applyFont(font);

			cell.setCellValue(strRichText);
		}
	    return rowCount;
    }

	/**
     * @param wb
     * @param planilha
     * @param rowCount
     * @return
     */
    private short getHeader(HSSFWorkbook wb, HSSFSheet planilha, short rowCount) {
	    HSSFRow row;
	    row = planilha.createRow(rowCount);
		HSSFRichTextString strRichText = new HSSFRichTextString(jxTablePanel.getTitle());

		HSSFFont font = wb.createFont();
		font.setFontHeightInPoints((short)11);
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		strRichText.applyFont(font);
		HSSFCellStyle cellStyle = wb.createCellStyle();
		cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);

		short inicio = 0;
		short fim = (short)(jxTablePanel.getColumnCount() - 1);
		HSSFCell cell = row.createCell(inicio);
		cell.setCellStyle(cellStyle);
		cell.setCellValue(strRichText);

		planilha.addMergedRegion( new Region(rowCount,inicio, rowCount,fim) );

	    return ++rowCount;
    }
}