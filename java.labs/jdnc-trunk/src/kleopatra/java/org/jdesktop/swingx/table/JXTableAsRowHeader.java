package org.jdesktop.swingx.table;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;

import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.icon.ColumnControlIcon;
import org.jdesktop.swingx.sort.SortController;

/**
 * Example of using JXTable as row header.<p>
 * 
 */
public class JXTableAsRowHeader {
    public static void main(String[] args) {
        final MyTableModel model = new MyTableModel();
        JTable core;
        final JXTable table = new JXTable() {
//            @Override
//            protected void updateOnFilterContentChanged() {
//                super.updateOnFilterContentChanged();
//                // #445-swingx: header not updated
//                getTableHeader().repaint();
//            }
            
        };
        table.setModel(model);
        RowHeaderTable rowHeaderTable = new RowHeaderTable(table);
        // hide the rowHeader column and install a custom 
        // columnControl which doesn't show the specified column
        table.getColumnExt(0).setVisible(false);
        table.setColumnControl(new MyColumnControl(table, 0));
        table.setColumnControlVisible(true);
        JFrame frame = new JFrame("JXTable as RowHeader");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setRowHeaderView(rowHeaderTable);
        scrollPane.setCorner(JScrollPane.UPPER_LEADING_CORNER, rowHeaderTable.getTableHeader());
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(scrollPane, BorderLayout.CENTER);
        frame.add(panel);
        frame.pack();
        frame.setVisible(true);
    }


    /** 
     * Quick example for rowHeader: listen to main pipeline
     * to update this pipeline on sorter change and the other
     * way round (beware of infinite loops, not really tested!).
     * 
     */
    static class RowHeaderTable extends JXTable {
        private JTable mainTable;

        public RowHeaderTable(JTable table) {
            super();
            mainTable = table;
            setAutoCreateRowSorter(false);
            
            setAutoCreateColumnsFromModel(false);
            setModel(mainTable.getModel());
            setRowSorter(mainTable.getRowSorter());
            setSelectionModel(mainTable.getSelectionModel());
            setAutoscrolls(false);
            // move the tableColumn over
            // This is not showing corretly if main hides the column before instantiating
            TableColumn columnExt = mainTable.getColumnModel().getColumn(0);
            addColumn(columnExt);
            mainTable.removeColumn(columnExt);
            // alternatively: create column
            // this requires a custom ColumnControl which doesn't
            // add the header column to the popup.
//            TableColumnExt columnExt = new TableColumnExt(0);
//            columnExt.setResizable(false);
//             addColumn(columnExt);
            getColumnModel().getColumn(0).setCellRenderer(
                    new JTableHeader().getDefaultRenderer());
            getColumnModel().getColumn(0).setPreferredWidth(50);
            setPreferredScrollableViewportSize(getPreferredSize());
            installSortSynchronization();
        }

//        
//        @Override
//        protected SortController getSortController() {
//            return null;
//        }


        private void installSortSynchronization() {
//            mainTable.getFilters().addPipelineListener(new PipelineListener() {
//
//                public void contentsChanged(PipelineEvent e) {
//                    FilterPipeline pipeline = (FilterPipeline) e.getSource();
//                    getFilters().getSortController().setSortKeys(pipeline.getSortController().getSortKeys());
//                }
//                
//            });
//            getFilters().addPipelineListener(new PipelineListener() {
//
//                public void contentsChanged(PipelineEvent e) {
//                    // same as getFilters() .. we are listening to our own.
//                    FilterPipeline pipeline = (FilterPipeline) e.getSource();
//                    SortKey sortKey = SortKey.getFirstSortingKey(pipeline.getSortController().getSortKeys());
//                    FilterPipeline mainPipeline = mainTable.getFilters();
//                    SortKey mainSortKey = SortKey.getFirstSortingKey(mainPipeline.getSortController().getSortKeys());
//                    if ((sortKey != null) && !sortKey.equals(mainSortKey)) {
//                        mainPipeline.getSortController().setSortKeys(pipeline.getSortController().getSortKeys());
//                    } else if ((sortKey == null) && (mainSortKey != null)) {
//                        mainTable.resetSortOrder();
//                    }
//                }
//                
//            });
        }

            public boolean isCellEditable(int row, int column) { return false; }

//            
//         @Override
//            protected void updateOnFilterContentChanged() {
//                super.updateOnFilterContentChanged();
//                // #445-swingx: header not updated
//                getTableHeader().repaint();
//            }

        public int getRowHeight(int row) {
            return mainTable.getRowHeight();
        }
    }

    /**
     * a custom column control which doesn't add all columns to
     * the popup.
     */
    static class MyColumnControl extends ColumnControlButton {
        int columnToHide;
        
        public MyColumnControl(JXTable table, int columnToHide) {
            super(table, new ColumnControlIcon());
            this.columnToHide = columnToHide;
        }

        @Override
        protected ColumnVisibilityAction createColumnVisibilityAction(TableColumn column) {
            if (columnToHide == column.getModelIndex()) return null;
            return super.createColumnVisibilityAction(column);
        }
        
        
    }
    /**
     * quick model ...
     */
    static class MyTableModel extends AbstractTableModel {
        public int getRowCount() {
            return 50;
        }


        public int getColumnCount() {
            return 4;
        }

        public Object getValueAt(int arg0, int arg1) {
            if (arg1 == 0) {
                return arg0;
            }
            return arg0 + "-" + arg1;
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            if (columnIndex == 0) {
                return Integer.class;
            }
            return super.getColumnClass(columnIndex);
        }
        
        
    }
}