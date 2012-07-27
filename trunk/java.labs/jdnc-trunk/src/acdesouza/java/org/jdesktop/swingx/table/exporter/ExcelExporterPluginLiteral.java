package org.jdesktop.swingx.table.exporter;

import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.UIManager;

/**
 * @author AC de Souza
 */
public class ExcelExporterPluginLiteral {
	private static final ResourceBundle bundle = ResourceBundle.getBundle(ExcelExporterPluginLiteral.class.getCanonicalName());
	
	public static final String EXPORTER_EXCEL_OPEN_DIALOG = bundle.getString("EXPORTER_EXCEL_OPEN_DIALOG");
	public static final String EXPORTER_EXCEL_MSG_SUCESS = bundle.getString("EXPORTER_EXCEL_MSG_SUCESS");

	public static void traduzirJFileChooser_pt_BR() {
		// Traduz o JFileChooser:
		// http://jsms.com.br/2005/11/08/feito-problema-com-agenda/ e
		// http://www.rgagnon.com/javadetails/java-0299.html
		if (new Locale("pt", "BR").equals(Locale.getDefault())) {
			UIManager.put("FileChooser.saveInLabelText", "Salvar em: ");
			UIManager.put("FileChooser.saveInLabelMnemonic", new Integer('S')); // S
			UIManager.put("FileChooser.lookInLabelText", "Examinar: ");
			UIManager.put("FileChooser.lookInLabelMnemonic", new Integer('E')); // E
			
			UIManager.put("FileChooser.upFolderToolTipText", "Um nível acima");
			UIManager.put("FileChooser.newFolderToolTipText", "Criar nova pasta");
			UIManager.put("FileChooser.newFolderAccessibleName", "Criar nova pasta");
			UIManager.put("FileChooser.listViewButtonToolTipText", "Lista");
			UIManager.put("FileChooser.detailsViewButtonToolTipText", "Detalhes");
			
			UIManager.put("FileChooser.fileNameLabelText", "Nome do arquivo: ");
			UIManager.put("FileChooser.fileNameLabelMnemonic", new Integer('N')); // N
			
			UIManager.put("FileChooser.filesOfTypeLabelText", "Arquivos do Tipo: ");
			UIManager.put("FileChooser.filesOfTypeLabelMnemonic", new Integer('R')); // R
			UIManager.put("FileChooser.acceptAllFileFilterText", "Todos os arquivos (*.*)");
			
			UIManager.put("FileChooser.fileNameHeaderText", "Nome");
			UIManager.put("FileChooser.fileSizeHeaderText", "Tamanho");
			UIManager.put("FileChooser.fileTypeHeaderText", "Tipo");
			UIManager.put("FileChooser.fileDateHeaderText", "Data");
			UIManager.put("FileChooser.fileAttrHeaderText", "Atributos");
			
			UIManager.put("FileChooser.openButtonText", "Abrir");
			UIManager.put("FileChooser.openButtonToolTipText", "Escolhe o diretório selecionado!");
			
			UIManager.put("FileChooser.saveButtonText", "Gravar");
			UIManager.put("FileChooser.saveButtonToolTipText", "Grava o arquivo com o nome informado.");
			
			UIManager.put("FileChooser.cancelButtonText", "Cancelar");
			UIManager.put("FileChooser.cancelButtonToolTipText", "Cancelar e deixar diretório padrão!");
		}
	}
}