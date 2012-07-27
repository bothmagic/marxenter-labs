package thierry.reporter;

import java.awt.Color;
import java.awt.Component;

import javax.swing.ListSelectionModel;
import javax.swing.table.TableCellRenderer;

import org.jdesktop.swingx.JXTable;


public class XTableReportGenerator {

	private JXTable table;
	
	public XTableReportGenerator(JXTable table) {
		this.table = table;
	}

	private Component getRendererComponent(int row, int column) {
		TableCellRenderer renderer = table.getCellRenderer(row, column);
		return table.prepareRenderer(renderer, row, column);
	}

	public String generateFullReport(ReportBuilder builder) {
		ListSelectionModel previousModel = table.getSelectionModel();
		try {
			table.setSelectionModel(previousModel.getClass().newInstance());
			builder.open();
			builder.startTable();

			builder.startTableHeader();
			for (int i = 0; i < table.getColumnCount(); i++) {
				builder.addColumnHeader(table.getColumnName(i));
			}
			builder.endTableHeader();

			for (int i = 0; i < table.getRowCount(); i++) {
				builder.startLine();
				for (int j = 0; j < table.getColumnCount(); j++) {
					addHTMLFormattedCell(builder, i, j);
				}
				builder.endLine();
			}
			builder.endTable();
			builder.close();
			return builder.toString();
		} catch (InstantiationException e) {
			return null;
		} catch (IllegalAccessException e) {
			return null;
		} finally {
			if (table != null) {
				table.setSelectionModel(previousModel);
			}
		}
	}

	public String getSelection(ReportBuilder builder) {
		ListSelectionModel previousModel = table.getSelectionModel();
		int[] rows = table.getSelectedRows();
		try {
			table.setSelectionModel(previousModel.getClass().newInstance());
			int columnCount = table.getColumnCount();
			builder.open();
			builder.startTable();

			for (int i = 0; i < rows.length; i++) {
				builder.startLine();
				for (int j = 0; j < columnCount; j++) {
					addHTMLFormattedCell(
							builder,
							rows[i], 
							j);
				}
				builder.endLine();
			}
			builder.endTable();
			builder.close();
			return builder.toString();
		} catch (InstantiationException e) {
			return null;
		} catch (IllegalAccessException e) {
			return null;
		} finally {
			if (table != null) {
				table.setSelectionModel(previousModel);
			}
		}
	}

   private void addHTMLFormattedCell(ReportBuilder builder, int row, int column) {
        Component comp = getRendererComponent(row, column);
        Color bgColor = comp.getBackground();
        Color fgColor = comp.getForeground();
        builder.addCell(bgColor, fgColor, removeHTMLBodyTag(table.getStringAt(
                row, column)));
    }


    private String removeHTMLBodyTag(String text) {
        text = text.replaceFirst("<html><body>", "");
		text = text.replaceFirst("</body></html>", "");
		return text;
    }


}
