package org.jdesktop.swingx.table;

import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.table.TableColumnExt;
import org.jdesktop.swingx.table.DefaultTableColumnModelExt;
import org.jdesktop.swingx.table.TableColumnModelExt;

import javax.swing.table.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.*;

/**
 * Tests an implemenation of TableColumnModelExt thatin effect uses one column model for two
 * tables for handling column visibility/width and order, but gives each table its own selection model
 * for identifying table column selections independently.
 *
 * Note:There is code in DefaultTableModel that looks at the selected cells (moveColumn, removeColumn)
 * this instancee of selection model is never initilised, but it does seem to cause a problem
 */
public class SyncTableColumnsTest
{
    private static final String TABLE_ID = "tableid";

    public static void main(String []argvc)
    {
        Vector colNames = new Vector();
        colNames.add("col0");
        colNames.add("col1");
        colNames.add("col2");
        colNames.add("col3");

        Vector data = new Vector();
        for (int i = 0; i < 4; i++)
        {
            Vector v = new Vector();
            v.add(i);
            v.add(i + 1);
            v.add(i);
            v.add(i + 2);
            data.add(v);
        }


        JXTable table1 = new JXTable(new DefaultTableModel(data, colNames));
        table1.putClientProperty(SyncTableColumnsTest.TABLE_ID, 1);
        table1.setColumnControlVisible(true);
        JXTable table2 = new JXTable(new DefaultTableModel(data, colNames));
        table2.putClientProperty(SyncTableColumnsTest.TABLE_ID, 2);
        table2.setColumnControlVisible(true);

        List<JTable> tablesWithSharedSelectionModel = new ArrayList<JTable>();
        tablesWithSharedSelectionModel.add(table1);
        tablesWithSharedSelectionModel.add(table2);

        //Create shared Model
        TableSharedColumnModel tcm = new TableSharedColumnModel();
        tcm.addColumn(new TableColumnExt(0));
        tcm.addColumn(new TableColumnExt(1));
        tcm.addColumn(new TableColumnExt(2));
        tcm.addColumn(new TableColumnExt(3));
        System.out.println("Col Count Shared is:"+tcm.getColumnCount());
        Enumeration e = tcm.getColumns();
        int i = 0;
        while (e.hasMoreElements())
        {
            TableColumn tc = (TableColumn) e.nextElement();
            tc.setHeaderValue(colNames.get(i));
            i++;
        }
        tcm.setColumnSelectionAllowed(true);
        table1.setColumnModel(tcm.createTableColumnExtModel(table1));
        table2.setColumnModel(tcm.createTableColumnExtModel(table2));



        JXFrame frame = new JXFrame("SyncColumnResizesButNotSelections");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JScrollPane scrollPane1 = new JScrollPane(table1);
        JScrollPane scrollPane2 = new JScrollPane(table2);
        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, scrollPane1, scrollPane2);
        frame.add(split);
        frame.pack();
        frame.setVisible(true);
    }


    static class TableSharedColumnModel extends DefaultTableColumnModelExt
    {
        public TableSharedColumnModel()
        {
            super();
        }

        public Vector<TableColumn> getTableColumnsVector()
        {
            return tableColumns;
        }

        public EventListenerList getListenerList()
        {
            return listenerList;
        }

        TableColumnModelExt createTableColumnExtModel(JTable callback)
        {
            return new TableSyncColumnModel(callback, this);
        }

        static class TableSyncColumnModel implements TableColumnModelExt
        {
            /**
             * Model for keeping track of column selections
             */
            protected ListSelectionModel selectionModel;
            protected SelectionListener selectionListener;
            private TableSharedColumnModel delegate;

            public TableSyncColumnModel(JTable callback, TableSharedColumnModel delegate)
            {
                super();
                this.delegate = delegate;
                selectionListener = new SelectionListener(callback);
                setSelectionModel(createSelectionModel());
                setColumnSelectionAllowed(true);
            }

            protected ListSelectionModel createSelectionModel() {
                   return new DefaultListSelectionModel();
               }


            public int getColumnCount(boolean includeHidden)
            {
                return delegate.getColumnCount(includeHidden);
            }


            public List<TableColumn> getColumns(boolean includeHidden)
            {
                return delegate.getColumns(includeHidden);
            }


            public TableColumnExt getColumnExt(Object identifier)
            {
                return delegate.getColumnExt(identifier);
            }


            public TableColumnExt getColumnExt(int columnIndex)
            {
                return delegate.getColumnExt(columnIndex);
            }

            public void addColumn(TableColumn aColumn)
            {
                delegate.addColumn(aColumn);
            }

            public void removeColumn(TableColumn column)
            {
                delegate.removeColumn(column);
            }

            public void moveColumn(int columnIndex, int newIndex)
            {
                delegate.moveColumn(columnIndex, newIndex);
            }

            public void setColumnMargin(int newMargin)

            {
                delegate.setColumnMargin(newMargin);
            }

            public int getColumnCount()
            {
                return delegate.getColumnCount();
            }

            public Enumeration<TableColumn> getColumns()
            {
                return delegate.getColumns();
            }


            public int getColumnIndex(Object columnIdentifier)
            {
                return delegate.getColumnIndex(columnIdentifier);
            }

            public TableColumn getColumn(int columnIndex)
            {
                return delegate.getColumn(columnIndex);
            }

            public int getColumnMargin()
            {
                return delegate.getColumnMargin();
            }

            public int getColumnIndexAtX(int xPosition)
            {
                return delegate.getColumnIndexAtX(xPosition);
            }

            public int getTotalColumnWidth()
            {
                return delegate.getTotalColumnWidth();
            }


            public void setColumnSelectionAllowed(boolean flag)
            {
                delegate.setColumnSelectionAllowed(flag);
            }


            public boolean getColumnSelectionAllowed()
            {
                return delegate.getColumnSelectionAllowed();
            }

            public void setSelectionModel(ListSelectionModel newModel)
            {
                if (newModel == null)
                {
                    throw new IllegalArgumentException("Cannot set a null SelectionModel");
                }

                ListSelectionModel oldModel = selectionModel;

                if (newModel != oldModel)
                {
                    if (oldModel != null)
                    {
                        oldModel.removeListSelectionListener(selectionListener);
                    }

                    selectionModel = newModel;
                    newModel.addListSelectionListener(selectionListener);
                }
            }


            public ListSelectionModel getSelectionModel()
            {
                return selectionModel;
            }


            public int[] getSelectedColumns()
            {
                if (selectionModel != null)
                {
                    int iMin = selectionModel.getMinSelectionIndex();
                    int iMax = selectionModel.getMaxSelectionIndex();

                    if ((iMin == -1) || (iMax == -1))
                    {
                        return new int[0];
                    }

                    int[] rvTmp = new int[1 + (iMax - iMin)];
                    int n = 0;
                    for (int i = iMin; i <= iMax; i++)
                    {
                        if (selectionModel.isSelectedIndex(i))
                        {
                            rvTmp[n++] = i;
                        }
                    }
                    int[] rv = new int[n];
                    System.arraycopy(rvTmp, 0, rv, 0, n);
                    return rv;
                }
                return new int[0];
            }

            // implements javax.swing.table.TableColumnModel

            /**
             * Returns the number of columns selected.
             *
             * @return the number of columns selected
             */
            public int getSelectedColumnCount()
            {
                if (selectionModel != null)
                {
                    int iMin = selectionModel.getMinSelectionIndex();
                    int iMax = selectionModel.getMaxSelectionIndex();
                    int count = 0;

                    for (int i = iMin; i <= iMax; i++)
                    {
                        if (selectionModel.isSelectedIndex(i))
                        {
                            count++;
                        }
                    }
                    return count;
                }
                return 0;
            }

            public void addColumnModelListener(TableColumnModelListener x)
            {
                delegate.addColumnModelListener(x);
            }


            public void removeColumnModelListener(TableColumnModelListener x)
            {
                delegate.removeColumnModelListener(x);
            }


            /**
             * Listens to changes in table column selection on this table
             */
            class SelectionListener implements ListSelectionListener
            {
                private JTable table;

                public SelectionListener(JTable table)
                {
                    this.table = table;
                }

                public void valueChanged(ListSelectionEvent e)
                {
                    fireColumnSelectionChanged(e);
                }

                /**
                 * We only want the table that this column model relates to
                 * to receive this event
                 *
                 * @param e
                 */
                protected void fireColumnSelectionChanged(ListSelectionEvent e)
                {
                    Object[] listeners = delegate.getListenerList().getListenerList();
                    for (int i = listeners.length - 2; i >= 0; i -= 2)
                    {
                        if (listeners[i] == TableColumnModelListener.class)
                        {
                            if (listeners[i + 1] instanceof JTable)
                            {
                                //Only fire for the selected table
                                if (((JTable) listeners[i + 1]) == table)
                                {
                                    System.out.println("Firing Event for:" + table.getClientProperty(SyncTableColumnsTest.TABLE_ID));
                                    ((TableColumnModelListener) listeners[i + 1]).columnSelectionChanged(e);
                                }
                            }

                        }
                    }
                }
            }

            public static final String IDENT = "$Id: SyncTableColumnsTest.java 1597 2007-07-31 12:48:39Z paultaylor $";
        }
    }


}
