package com.thierry.filtering.report;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.util.Arrays;
import java.util.Collection;


public class TableReportGenerator {

    private static final int[] EMPTY_ARRAY = new int[0];

    private static final Collection<String> STRIP_TAGS = Arrays.asList(
            "<html>", "<body>", "</body>", "</html>",
            "<HTML>", "<BODY>", "</BODY>", "</HTML>"
    );

    private JTable table;

    public TableReportGenerator(JTable table) {
        this.table = table;
    }

    public String getSelection(ReportBuilder builder) {
        int[] rows = table.getSelectedRows();
        boolean columnSelectionAllowed =
                table.getColumnModel().getColumnSelectionAllowed();
        int columns[] =
                columnSelectionAllowed ? table.getSelectedColumns() : EMPTY_ARRAY;
        int columnCount =
                columnSelectionAllowed ? columns.length : table.getColumnCount();

        builder.open();
        builder.startTable();

        for (int i = 0; i < rows.length; i++) {
            builder.startLine();
            for (int j = 0; j < columnCount; j++) {
                if (!columnSelectionAllowed
                        || table.isColumnSelected(columns[j])) {
                    addHTMLFormattedCell(
                            builder,
                            rows[i],
                            columnSelectionAllowed ? columns[j] : j);
                } else {
                    //TODO builder.addCell("");
                    builder.addCell(null, null, "");
                }
            }
            builder.endLine();
        }

        builder.endTable();
        builder.close();

        return builder.toString();
    }

    //PENDING get rid of the hack!

    public String generateFullReport(ReportBuilder builder) {
        ListSelectionModel previousModel = table.getSelectionModel();
        try {
            //TODO hacky means to an end - need to hide SelectionModel from any renderers until we're done
            table.setSelectionModel(previousModel.getClass().newInstance());
            builder.open();
            builder.startTable();

            int columnCount = table.getColumnCount();
            int rowCount = table.getRowCount();

            builder.startTableHeader();
            for (int i = 0; i < columnCount; i++) {
                builder.addColumnHeader(table.getColumnName(i));
            }
            builder.endTableHeader();

            for (int i = 0; i < rowCount; i++) {
                builder.startLine();
                for (int j = 0; j < columnCount; j++) {
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

    private void addHTMLFormattedCell(ReportBuilder builder, int row, int column) {
        Component stamp = getRendererComponent(row, column);
        Color fgColor =
                stamp.getForeground().equals(table.getForeground()) ? null : stamp.getForeground();
        Color bgColor =
                stamp.getBackground().equals(table.getBackground()) ? null : stamp.getBackground();

        if (table instanceof org.jdesktop.swingx.JXTable) {
            builder.addCell(bgColor, fgColor, removeHTMLBodyTag(
                    ((org.jdesktop.swingx.JXTable) table).getStringAt(row, column)));
        } else {
            String text = "";
            if (stamp instanceof JLabel) {
                text = ((JLabel) stamp).getText();
            } else if (stamp instanceof AbstractButton) {
                text = ((AbstractButton) stamp).getText();
            } else if (stamp instanceof JTextComponent) {
                text = ((JTextComponent) stamp).getText();
            }
            builder.addCell(bgColor, fgColor, removeHTMLBodyTag(text));
        }
    }

    private Component getRendererComponent(int row, int column) {
        TableCellRenderer renderer = table.getCellRenderer(row, column);
        // PENDING rjo - problem here JXTables do all their highlighting in prepareRenderer() is hard-coded to show only current state
        // PENDING return table.prepareRenderer(renderer, row, column);
        // PENDING rjo ..but we need sans selection, focus status of cells, or else only get selection states (iterating the selected cells)
        Object value = table.getValueAt(row, column);
        return renderer.getTableCellRendererComponent(table, value, false, false, row, column);
    }

    private String removeHTMLBodyTag(String text) {
        if (text != null && text.length() > 0) {
            for (String tag : STRIP_TAGS) {
                text = text.replaceFirst(tag, "");
            }
            return text.trim();
        } else {
            return "";
        }
    }

}
