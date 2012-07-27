/*
 * Created on 11.02.2007
 *
 */
package org.jdesktop.swingx.table;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.JXTableHeader;
import org.jdesktop.swingx.event.TableColumnModelExtListener;
import org.jdesktop.swingx.incubatorutil.InteractiveTestCase;
import org.jdesktop.swingx.sort.SortController;

public class FilterCoupledTables extends InteractiveTestCase {

    public void interactiveColumnDragged() {
        final JXTable table = new JXTable(new MyTableModel()) {

            @Override
            protected JTableHeader createDefaultTableHeader() {
                return createXTableHeader(getColumnModel());
            }
            
        };
        table.setColumnControlVisible(true);
        final JXTable coupled = new JXTable(table.getModel());
        coupled.getTableHeader().setReorderingAllowed(false);
        coupled.getTableHeader().setResizingAllowed(false);
        coupled.setColumnControlVisible(true);
        table.getTableHeader().addPropertyChangeListener(new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                if ("resizingColumn".equals(evt.getPropertyName())) {
                    TableColumn column = (TableColumn) evt.getNewValue();
                    if (column != null) {
                        int viewIndex = table.getColumns().indexOf(column);
                        column = coupled.getColumn(viewIndex);
                    }
                    coupled.getTableHeader().setResizingColumn(column);
                }
                if ("draggedColumn".equals(evt.getPropertyName())) {
                    TableColumn column = (TableColumn) evt.getNewValue();
                    if (column != null) {
                        int viewIndex = table.getColumns().indexOf(column);
                        column = coupled.getColumn(viewIndex);
                    }
                    coupled.getTableHeader().setDraggedColumn(column);
                }
                if ("draggedDistance".equals(evt.getPropertyName())) {
                    coupled.getTableHeader().setDraggedDistance((Integer) evt.getNewValue());
                }
                
            }
        });
        table.getColumnModel().addColumnModelListener(new TableColumnModelExtListener() {

            public void columnMoved(TableColumnModelEvent e) {
                // need to fire always - table and header rely on it to repaint
//                if (e.getFromIndex() == e.getToIndex()) return;
                // will have a problem on showing invisible columns
                // because the visibility of the column in the coupled 
                // model is not updated correctly.
                coupled.getColumnModel().moveColumn(e.getFromIndex(), e.getToIndex());
                
            }


            public void columnPropertyChange(PropertyChangeEvent event) {
                if ("width".equals(event.getPropertyName())) {
                    TableColumn column = (TableColumn) event.getSource();
                    if (column != null) {
                        int viewIndex = table.getColumns().indexOf(column);
                        coupled.getColumn(viewIndex).setWidth((Integer) event.getNewValue());
                    }
                    
                }
                // should be possible - but isn't complete because of 
                // DefaultTableColumnModelExt not firing propertyChangeEvents
                // for invisible columns (see Issue #369-swingx)
                if ("visible".equals(event.getPropertyName())) {
                    TableColumn column = (TableColumn) event.getSource();
                    if (column != null) {
                        List<TableColumn> columns = coupled.getColumns(true);
                        for (TableColumn column2 : columns) {
                            if (column.getModelIndex() == column2.getModelIndex()) {
                                ((TableColumnExt) column2).setVisible((Boolean) event.getNewValue());
                                break;
                            }
                        }
                    }
                    
                }
                
            }
            public void columnAdded(TableColumnModelEvent e) {
                // TODO Auto-generated method stub
                
            }

            public void columnRemoved(TableColumnModelEvent e) {
                // TODO Auto-generated method stub
                
            }

            public void columnSelectionChanged(ListSelectionEvent e) {
                // TODO Auto-generated method stub
                
            }
            public void columnMarginChanged(ChangeEvent e) {
                // TODO Auto-generated method stub
                
            }

            
        });
        showWithScrollingInFrame(coupled, table, "left column moves coupled to right");
    }
    
    private JXTableHeader createXTableHeader(TableColumnModel model) { 
        JXTableHeader h = new JXTableHeader(model) {

            @Override
            public void setDraggedColumn(TableColumn aColumn) {
                TableColumn oldValue = getDraggedColumn();
                super.setDraggedColumn(aColumn);
                firePropertyChange("draggedColumn", oldValue, getDraggedColumn());
            }

            @Override
            public void setDraggedDistance(int distance) {
                int old = getDraggedDistance();
                super.setDraggedDistance(distance);
                firePropertyChange("draggedDistance", old, getDraggedDistance());
            }

            @Override
            public void setResizingColumn(TableColumn aColumn) {
                Object oldValue = getResizingColumn();
                super.setResizingColumn(aColumn);
                firePropertyChange("resizingColumn", oldValue, getResizingColumn());
            }
            
            
            
        };
        return h;
        
    }
    
    /**
     * Requirement: left is synched to right sort-state, but not sortable
     * by user. Still need a wrapper around the shared sorter?
     */
    public void interactiveCoupledRows() {
        JXTable table = new JXTable(new MyTableModel());
        JXTable coupled = new JXTable() {
            /**
             * Overridden to not touch touch the shared sorter.
             * Not enough ..
             */
            @Override
            protected SortController getSortController() {
                return null;
            }
            
        };
        coupled.setAutoCreateRowSorter(false);
        coupled.setRowSorter(table.getRowSorter());
        coupled.setModel(table.getModel());
        coupled.setSelectionModel(table.getSelectionModel());
        coupled.setSortable(false);
        showWithScrollingInFrame(coupled, table, "left is coupled to second");
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
    public static void main(String args[]) {
        FilterCoupledTables  test = new FilterCoupledTables();
//        setSystemLF(true);
        try {
          test.runInteractiveTests();
        } catch (Exception e) {
            System.err.println("exception when executing interactive tests:");
            e.printStackTrace();
        }
    }
}
