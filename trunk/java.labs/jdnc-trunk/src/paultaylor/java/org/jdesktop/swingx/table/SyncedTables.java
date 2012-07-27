package org.jdesktop.swingx.table;

import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.decorator.*;
import org.jdesktop.swingx.table.DefaultTableColumnModelExt;
import org.jdesktop.swingx.table.TableColumnExt;
import org.jdesktop.swingx.table.TableColumnModelExt;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryUsage;
import java.util.*;
import java.util.List;

/**
 * Sync tableds with row header, to try and find performance issues
 */
public class SyncedTables
{
    public static int NO_OF_FILTERS = 5;

    //Flags for different behaviour
    //Sort the rows of other tables to same as sorted table
    public static boolean isSyncRowSorting = true;

    //Moved/resized the columns of other tables as they are moved/resized in one table
    public static boolean isSyncColSortingAndResizing = true;

    //Install NO_OF_FILTERS onto filter pipeline of tables
    public static boolean isFiltersInstalled = true;

    //Sync vertical scrolling
    public static boolean isVerticalScrollingSync = true;

    //Sync Horizontal Scrolling
    public static boolean isHorizontalScrollingSync = true;

    public static final String SYNCCOL = "synccol";

    /**
     * Create 'tables' tables with 'rows' rows and 'cols' columns
     *
     * @param tables
     * @param rows
     * @param cols
     */
    public void run(final int tables, final int rows, final int cols) throws Exception
    {

        SwingUtilities.invokeAndWait(new Runnable()
        {
            public void run()
            {
                //Column Names
                Vector colNames = new Vector();
                for (int j = 0; j < cols; j++)
                {
                    colNames.add("col" + j);
                }

                List<TableWithRowHeader> tablePanes = new ArrayList<TableWithRowHeader>();
                for (int i = 0; i < tables; i++)
                {
                    tablePanes.add(new TableWithRowHeader(i, new DefaultTableModel(genData(rows, cols), colNames)));
                }

                //When column is sorted in one table, sort by corresponding column in others tables - works both way
                TableWithRowHeader.syncTables(tablePanes);

                //Display in Frame
                final JFrame frame = new JFrame("SyncedTables");
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                int paneheight = 1000 / tablePanes.size();

                if (tablePanes.size() == 1)
                {
                    frame.add(tablePanes.get(0).getScrollPane());
                    frame.pack();
                    frame.setVisible(true);
                    return;
                }
                JSplitPane lastSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, tablePanes.get(0).getScrollPane(), tablePanes.get(1).getScrollPane());
                lastSplitPane.setDividerLocation(paneheight);
                if (tablePanes.size() > 2)
                {
                    for (int i = 2; i < tablePanes.size(); i++)
                    {
                        JSplitPane sp = new JSplitPane(JSplitPane.VERTICAL_SPLIT, lastSplitPane, tablePanes.get(i).getScrollPane());
                        lastSplitPane = sp;
                        lastSplitPane.setDividerLocation(paneheight * i);
                    }
                }
                final JSplitPane endSplitPane = lastSplitPane;
                frame.add(endSplitPane);
                frame.pack();
                frame.repaint();
                frame.setVisible(true);
            }
        });

    }

    //make some random data
    //In this test we explicity store rowno in first column and last column for the sorting to work
    //but really should be hidden at this point
    private Vector genData(int noOfRows, int noOfCols)
    {
        Random rand = new Random();
        Vector data = new Vector();
        for (int i = 0; i < noOfRows; i++)
        {
            Vector v = new Vector();
            v.add(i);
            for (int j = 1; j <= (noOfCols - 2); j++)
            {
                v.add(Math.abs(rand.nextInt() % noOfRows));
            }
            v.add(i);
            data.add(v);
        }
        return data;
    }

    /**
     * Bring together a main table and row header table and put them into a scrollpanel
     */
    public static class TableWithRowHeader
    {
        private RowHeaderTable rowHeaderTable;
        private SyncTable syncTable;
        private JScrollPane sp;
        private static List<ListenerGate> scrollBarHListeners;
        private static List<ListenerGate> scrollBarVListeners;

        static
        {
            scrollBarHListeners = new ArrayList<ListenerGate>();
            scrollBarVListeners = new ArrayList<ListenerGate>();
        }
        private static TableSyncColumnModel tscm = new TableSyncColumnModel();

        public TableWithRowHeader(int identifier, TableModel tm)
        {

            if (isSyncColSortingAndResizing)
            {
                // Create new columns from the data model info unless already initilized
                if (tscm.getColumnCount() == 0)
                {
                    for (int i = 0; i < tm.getColumnCount(); i++)
                    {
                        TableColumn newColumn = new TableColumnExt(i);
                        newColumn.setHeaderValue("Col" + i);
                        newColumn.setIdentifier(String.valueOf(i));
                        newColumn.setWidth(100);
                        newColumn.setMinWidth(100);

                        tscm.addColumn(newColumn);
                    }
                    ((TableColumnModelExt) tscm).getColumns(true).get(0).setIdentifier("0");
                    ((TableColumnExt) ((TableColumnModelExt) tscm).getColumns(true).get(0)).setVisible(false);
                    ((TableColumnExt) ((TableColumnModelExt) tscm).getColumns(true).get(tm.getColumnCount() - 1)).setIdentifier(SYNCCOL);
                    ((TableColumnExt) ((TableColumnModelExt) tscm).getColumns(true).get(tm.getColumnCount() - 1)).setHeaderValue(SYNCCOL);
                    ((TableColumnExt) ((TableColumnModelExt) tscm).getColumns(true).get(tm.getColumnCount() - 1)).setVisible(false);
                }
                syncTable = new SyncTable(tm);
                syncTable.setColumnModel(tscm.createTableColumnExtModel(syncTable));
            }
            else
            {
                //Just uses DefaultExtTabelModel and auto populates it
                syncTable = new SyncTable(tm);
                ((TableColumnModelExt) syncTable.getColumnModel()).getColumns(true).get(0).setIdentifier("0");
                ((TableColumnExt) ((TableColumnModelExt) syncTable.getColumnModel()).getColumns(true).get(0)).setVisible(false);
                ((TableColumnExt) ((TableColumnModelExt) syncTable.getColumnModel()).getColumns(true).get(tm.getColumnCount() - 1)).setIdentifier(SYNCCOL);
                ((TableColumnExt) ((TableColumnModelExt) syncTable.getColumnModel()).getColumns(true).get(tm.getColumnCount() - 1)).setHeaderValue(SYNCCOL);
                ((TableColumnExt) ((TableColumnModelExt) syncTable.getColumnModel()).getColumns(true).get(tm.getColumnCount() - 1)).setVisible(false);

                List<TableColumn> cols = ((TableColumnModelExt) syncTable.getColumnModel()).getColumns(true);
                for(TableColumn col:cols)
                {
                    col.setWidth(100);
                    col.setMinWidth(100);
                }
            }

            rowHeaderTable = new RowHeaderTable(syncTable);
            ((SyncSorterFilterPipeline) syncTable.getFilters()).id = identifier;
            syncTable.getFilters().addPipelineListener(rowHeaderTable);
            rowHeaderTable.getTableHeader().addMouseListener(new RecNoHeaderListener(syncTable));
            syncTable.putClientProperty("id", String.valueOf(identifier));
            rowHeaderTable.putClientProperty("id", String.valueOf(identifier));

            sp = new JScrollPane(syncTable);
            sp.setCorner(JScrollPane.UPPER_RIGHT_CORNER, new JLabel((String) syncTable.getClientProperty("id")));
            sp.setRowHeaderView(rowHeaderTable);
            sp.setCorner(JScrollPane.UPPER_LEFT_CORNER, rowHeaderTable.getTableHeader());
            sp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

        }


        public RowHeaderTable getRowHeaderTable()
        {
            return rowHeaderTable;
        }


        public SyncTable getMainTable()
        {
            return syncTable;
        }

        public JScrollPane getScrollPane()
        {
            return sp;
        }

        public static void syncTables(List<TableWithRowHeader> tablePanes)
        {
            if (isSyncRowSorting)
            {
                //When column is sorted in one table, sort by corresponding column in others tables - works both way.
                for (TableWithRowHeader tablePane : tablePanes)
                {
                    for (TableWithRowHeader tablePaneToCompare : tablePanes)
                    {
                        if (!tablePane.getMainTable().getClientProperty("id").equals(tablePaneToCompare.getMainTable().getClientProperty("id")))
                        {
                            tablePaneToCompare.getMainTable().addSortListener(tablePane.getMainTable());
                        }
                    }
                }
            }

            if(isVerticalScrollingSync)
            {
                for (TableWithRowHeader tablePane : tablePanes)
                {
                    final ScrollPaneAdjustmentListener sPVAL  = new CompleteScrollPaneAdjustmentListener(tablePane.getScrollPane().getVerticalScrollBar(),scrollBarVListeners);
                    ListenerGate listenerGate = new ListenerGate(true, sPVAL);
                    listenerGate.setOpen(true);
                    tablePane.getScrollPane().getVerticalScrollBar().addAdjustmentListener(listenerGate);
                    tablePane.getScrollPane().getVerticalScrollBar().addMouseListener(listenerGate);
                    scrollBarVListeners.add(listenerGate);
                }
            }

            if(isHorizontalScrollingSync)
            {
                for (TableWithRowHeader tablePane : tablePanes)
                {
                    final ScrollPaneAdjustmentListener sPVAL  = new CompleteScrollPaneAdjustmentListener(tablePane.getScrollPane().getVerticalScrollBar(),scrollBarHListeners);
                    ListenerGate listenerGate = new ListenerGate(true, sPVAL);
                    listenerGate.setOpen(true);
                    tablePane.getScrollPane().getVerticalScrollBar().addAdjustmentListener(listenerGate);
                    tablePane.getScrollPane().getVerticalScrollBar().addMouseListener(listenerGate);
                    scrollBarHListeners.add(listenerGate);
                }
            }
        }
    }

    /**
     * An Event that preserves indexes of the sorted rows
     */
    public static class SortEvent extends EventObject
    {
        private int[] newOrder;

        /**
         * Constructs a SortEvent object.
         *
         * @param source   the source Object (typically <code>this</code>)
         * @param newOrder of records
         */
        public SortEvent(Object source, int[] newOrder)
        {
            super(source);
            this.newOrder = newOrder;
        }

        /**
         * Returns a string representation of this event
         *
         * @return a string representation of this event.
         */
        public String toString()
        {
            return getClass().getName();
        }

        public int[] getNewOrder()
        {
            return newOrder;
        }
    }

    /**
     * Table must implement this in order to listen for other table sbeing sorted
     */
    public static interface SortListener extends EventListener
    {
        public void sortReceived(SyncedTables.SortEvent event);
    }

    /* Listens for click on row header header and then sorts the main table by the hidden column zero that
      contains the row numbers
    */
    public static final class RecNoHeaderListener extends MouseAdapter
    {
        final JXTable table;

        public RecNoHeaderListener(JXTable datasheetTable)
        {
            this.table = datasheetTable;
        }

        public final void mouseClicked(final MouseEvent e)
        {
            //Sorting by hidden col 0
            table.toggleSortOrder(String.valueOf("0"));
        }
    }

    /**
     * Row header table
     * Listens on changes to main tables pipeline
     */
    public static class RowHeaderTable extends JXTable implements PipelineListener
    {
        private JXTable mainTable;

        public RowHeaderTable(JXTable mainTable)
        {
            this.mainTable = mainTable;
            //We enable sorting on main datasheet
            setSortable(true);
            setModel(mainTable.getModel());
            //Table Column Model just take first column of model
            final TableColumnModel tcm = new DefaultTableColumnModel();
            tcm.setColumnSelectionAllowed(false);
            final TableColumn tc = new TableColumn(0);
            tcm.addColumn(tc);
            setColumnModel(tcm);

            List<Filter> filters = new ArrayList<Filter>();
            if (isFiltersInstalled)
            {
                for (int i = 0; i < NO_OF_FILTERS; i++)
                {
                    filters.add(new RowsKnownFilter(i));
                }
            }
            FilterPipeline pipeline = new FilterPipeline(filters.toArray(new Filter[0]));
            setFilters(pipeline);
        }

        /**
         * Retrieve the record number as currently sorted, shares the model with the main table
         */
        public Object getValueAt(int row, int column)
        {
            return getModel().getValueAt(mainTable.convertRowIndexToModel(row), 0);
        }

        /**
         * Called when rowheaders filter/sorter changes
         */
        protected void updateOnFilterContentChanged()
        {
            super.updateOnFilterContentChanged();
            // #445-swingx: header not updated
            getTableHeader().repaint();
        }

        /**
         * Called when main tables content changes
         *
         * @param e
         */
        public void contentsChanged(PipelineEvent e)
        {
            if (e.getType() == PipelineEvent.CONTENTS_CHANGED)
            {
                FilterPipeline pipeline = (FilterPipeline) e.getSource();
                if (!getFilters().getSortController().getSortKeys()
                        .equals(pipeline.getSortController().getSortKeys()))
                {
                    getFilters().getSortController().setSortKeys(pipeline.getSortController().getSortKeys());
                }
            }
        }

        /* @Override
        */
        protected boolean shouldSortOnChange(TableModelEvent e)
        {
            if (isUpdate(e))
            {
                repaint(e);
                return false;
            }
            return super.shouldSortOnChange(e);
        }

        /**
         * Hack to repaint at the correct row in terms of
         * view coordinates.
         *
         * @param e
         */
        private void repaint(TableModelEvent e)
        {
            int firstRow = convertRowIndexToView(e.getFirstRow());
            Rectangle rowRect = getCellRect(firstRow, 0, true);
            rowRect.width = getWidth();
            repaint(rowRect);
        }
    }

    /**
     * Sort column the same as the corresponding column in other table that event has been received for
     */
    public static class SyncTable extends JXTable implements SyncedTables.SortListener
    {
        public SyncTable(TableModel dm)
        {
            super(dm);

            //We enable sorting on main datasheet
            setSortable(true);
            List<Filter> filters = new ArrayList<Filter>();
            if (isFiltersInstalled)
            {
                for (int i = 0; i < NO_OF_FILTERS; i++)
                {
                    filters.add(new RowsKnownFilter(i));
                }
            }
            FilterPipeline pipeline = new SyncedTables.SyncSorterFilterPipeline(filters.toArray(new Filter[0]));
            setFilters(pipeline);
        }

        public SyncTable(TableModel dm, TableColumnModel tcm)
        {
            super(dm, tcm);

            //We enable sorting on main datasheet
            setSortable(true);
            List<Filter> filters = new ArrayList<Filter>();
            if (isFiltersInstalled)
            {
                for (int i = 0; i < NO_OF_FILTERS; i++)
                {
                    filters.add(new RowsKnownFilter(i));
                }
            }
            FilterPipeline pipeline = new SyncedTables.SyncSorterFilterPipeline(filters.toArray(new Filter[0]));
            setFilters(pipeline);
        }

        public void sortReceived(SyncedTables.SortEvent e)
        {
            if (e.getSource() != this)
            {
                sortToMatchTable(e);
            }
        }

        /**
         * Always returns the row as displayed for the synccol column
         *
         * @param row is visible row
         * @param col is visible column
         * @return
         */
        public final Object getValueAt(final int row, final int col)
        {
            //Remember cols indexed from zero and column 0 is hidden, here wa are getting the synccol which holds
            //the visible row order
            if (col == (this.getModel().getColumnCount() - 2))
            {
                return row;
            }
            else
            {
                return super.getValueAt(row, col);
            }
        }

        EventListenerList listenerList = new EventListenerList();


        public synchronized void addSortListener(SyncedTables.SortListener l)
        {
            listenerList.add(SyncedTables.SortListener.class, l);
        }

        public synchronized void removeSortListener(SyncedTables.SortListener l)
        {
            listenerList.remove(SyncedTables.SortListener.class, l);
        }

        public synchronized void fireSortEvent(int[] newOrder, Object Source)
        {
            SyncedTables.SortEvent sortEvent = null;
            Object[] listeners = listenerList.getListenerList();

            for (int i = listeners.length - 2; i >= 0; i -= 2)
            {
                if (listeners[i] == SyncedTables.SortListener.class)
                {
                    // Lazily create the event:
                    if (sortEvent == null)
                    {
                        sortEvent = new SyncedTables.SortEvent(this, newOrder);
                    }
                    ((SyncedTables.SortListener) listeners[i + 1]).sortReceived(sortEvent);
                }
            }
        }

        @Override
        /** Called by RecNolistener when sort on recno header
         *
         */
        public void toggleSortOrder(Object identifier)
        {
            if (!isSortable(identifier))
            {
                return;
            }

            SyncSorterFilterPipeline.CustomSortController controller = (SyncSorterFilterPipeline.CustomSortController) getSortController();
            TableColumnExt columnExt = getColumnExt(identifier);

            int[] newOrder = controller.performSortOrder(columnExt.getModelIndex(), columnExt != null ? columnExt.getComparator() : null);
            fireSortEvent(newOrder, this);
        }

        @Override
        /**
         * Called when user clicks on column header
         */
        public void toggleSortOrder(int columnIndex)
        {
            if (!isSortable(columnIndex))
            {
                return;
            }
            SyncSorterFilterPipeline.CustomSortController controller = (SyncSorterFilterPipeline.CustomSortController) getSortController();
            TableColumnExt columnExt = getColumnExt(columnIndex);

            int[] newOrder = controller.performSortOrder(convertColumnIndexToModel(columnIndex), columnExt != null ? columnExt.getComparator() : null);
            fireSortEvent(newOrder, this);
        }

        /**
         * Called when user SHIFT-clicks on column header
         */
        public void resetSortOrder()
        {
            super.resetSortOrder();
            if (listenerList != null)
            {
                fireSortEvent(null, this);
            }
        }

        private void resetLocally()
        {
            SortController controller = getSortController();
            if (controller != null)
            {
                controller.setSortKeys(null);
            }
            if (getTableHeader() != null)
            {
                getTableHeader().repaint();
            }
        }

        public void sortToMatchTable(SortEvent event)
        {

            if (event.getNewOrder() == null)
            {
                resetLocally();
                return;
            }

            if (getSortedColumn() != null)
            {
                resetLocally();
            }

            //Sort
            TableColumnExt columnExt = getColumnExt(SYNCCOL);
            SyncSorterFilterPipeline.CustomSortController controller = (SyncSorterFilterPipeline.CustomSortController) getSortController();
            controller.syncSortOrder(columnExt.getModelIndex(), columnExt != null ? columnExt.getComparator() : null, event.getNewOrder());
        }


        @Override
        protected boolean shouldSortOnChange(TableModelEvent e)
        {
            if (isUpdate(e))
            {
                repaint(e);
                return false;
            }
            return super.shouldSortOnChange(e);
        }

        /**
         * Hack to repaint at the correct row in terms of
         * view coordinates.
         *
         * @param e
         */
        private void repaint(TableModelEvent e)
        {
            int firstRow = convertRowIndexToView(e.getFirstRow());
            Rectangle rowRect = getCellRect(firstRow, 0, true);
            rowRect.width = getWidth();
            repaint(rowRect);
        }

    }

    /**
     * Extends pipeline to allow sorting based on another
     */
    public static class SyncSorterFilterPipeline extends FilterPipeline
    {
        public int id;

        public SyncSorterFilterPipeline()
        {
            this(new Filter[]{});
        }


        public SyncSorterFilterPipeline(Filter... inList)
        {
            super(inList);
        }

        public SyncedTables.SyncTableSorter getSyncSorter()
        {
            return (SyncedTables.SyncTableSorter) this.getSorter();
        }

        protected Sorter createDefaultSorter()
        {
            return new SyncedTables.SyncTableSorter();
        }

        //@Override
        protected SortController createDefaultSortController()
        {
            return new SyncedTables.SyncSorterFilterPipeline.CustomSortController();
        }

        //@Override
        protected class CustomSortController extends SorterBasedSortController
        {
            protected Sorter createDefaultSorter()
            {
                return new SyncedTables.SyncTableSorter();
            }

            /**
             * Performs sort
             */
            public int[] performSortOrder(int column, Comparator comparator)
            {
                SyncedTables.SyncTableSorter currentSorter = (SyncedTables.SyncTableSorter) getSorter();
                if ((currentSorter != null) && (currentSorter.getColumnIndex() == column))
                {
                    currentSorter.toggle();
                }
                else
                {
                    currentSorter = (SyncedTables.SyncTableSorter) createDefaultSorter(new SortKey(SortOrder.ASCENDING, column, comparator));
                    setSorter(currentSorter);

                }
                return currentSorter.getToPrevious();

            }

            /**
             * Sort to newOrder
             */
            public void syncSortOrder(int column, Comparator comparator, int[] newOrder)
            {
                SyncedTables.SyncTableSorter syncSorter = (SyncedTables.SyncTableSorter) createDefaultSorter(new SortKey(SortOrder.ASCENDING, column, comparator));
                syncSorter.setNewOrder(newOrder);
                setSorter(syncSorter);
            }
        }
    }

    /**
     * Sorts record to predefined Order (neworder) rather than using the Comparator
     * I wanted to just subclass ShuttleSorter but because its toPrevious is private
     * I couldn't get it to work, so this code is a copy of ShuttleSorter with neworder
     * added and filter overridden
     */
    public static class SyncTableSorter extends Sorter
    {
        private boolean isSyncSort = false;
        private int[] newOrder;
        private int[] toPrevious;
        private int[] toPreviousCopy;

        public int[] getToPrevious()
        {
            return toPreviousCopy;
        }

        /**
         * Set the order we want the records to be sorted to
         *
         * @param newOrder
         */
        public void setNewOrder(int[] newOrder)
        {
            this.newOrder = newOrder;
            isSyncSort = true;
        }

        public SyncTableSorter()
        {
            this(0, true);
        }

        public SyncTableSorter(int col, boolean ascending)
        {
            // pending .. chain to this
            super(col, ascending);
        }

        public SyncTableSorter(int col, boolean ascending, Comparator comparator)
        {
            super(col, ascending, comparator);
        }

        protected void init()
        {
            // JW: ?? called from super, so toPrevious is still null after running
            // this method??
            toPrevious = new int[0];
        }


        /**
         * Resets the internal row mappings from this filter to the previous filter.
         */
        protected void reset()
        {
            int inputSize = getInputSize();
            toPrevious = new int[inputSize];
            fromPrevious = new int[inputSize];
            for (int i = 0; i < inputSize; i++)
            {
                toPrevious[i] = i;    // reset before sorting
            }
        }

        /**
         * Performs the sort. Calls sort only if canFilter(),
         * regards all values as equal (== doesn't sort) otherwise.
         *
         * @see #canFilter()
         */
        protected void filter()
        {
            if (canFilter())
            {
                if (isSyncSort)
                {
                    //Sort
                    toPrevious = newOrder.clone();

                    //Reset because this sorter is used for all columns
                    isSyncSort = false;
                }
                else
                {
                    sort(toPrevious.clone(), toPrevious, 0, toPrevious.length);
                    toPreviousCopy = toPrevious.clone();
                }
            }
            // Generate inverse map for implementing convertRowIndexToView();
            for (int i = 0; i < toPrevious.length; i++)
            {
                fromPrevious[toPrevious[i]] = i;
            }

        }

        protected boolean canFilter()
        {
            return adapter != null && (getColumnIndex() < adapter.getColumnCount());
        }

        public int getSize()
        {
            return toPrevious.length;
        }

        protected int mapTowardModel(int row)
        {
            return toPrevious[row];
        }


        protected void sort(int from[], int to[], int low, int high)
        {
            if (high - low < 2)
            {
                return;
            }
            int middle = (low + high) >> 1;

            sort(to, from, low, middle);
            sort(to, from, middle, high);

            int p = low;
            int q = middle;


            if (high - low >= 4 && compare(from[middle - 1], from[middle]) <= 0)
            {
                System.arraycopy(from, low, to, low, high - low);
                return;
            }

            // A normal merge.

            for (int i = low; i < high; i++)
            {
                if (q >= high || (p < middle && compare(from[p], from[q]) <= 0))
                {
                    to[i] = from[p++];
                }
                else
                {
                    to[i] = from[q++];
                }
            }
        }
    }

    /**
     * Filter rows to only show rows that are in matchingRows (unless matchingrows are null
     * in which case show all rows)
     */
    public static class RowsKnownFilter extends Filter
    {
        //Only rows with model index in this list should be shown
        //TODO is this the most efficient storage device
        private List<Integer> matchingRows;

        private ArrayList<Integer> toPrevious;


        /**
         * Instantiate empty filter
         */
        public RowsKnownFilter(int modelColumnId)
        {
            super(modelColumnId);
        }

        public RowsKnownFilter(int modelColumnId, List<Integer> matchingRows)
        {
            super(modelColumnId);
            setMatchingRows(matchingRows);
        }


        public void setMatchingRows(List<Integer> matchingRows)
        {
            if (this.matchingRows == null && matchingRows == null)
            {
                //Do nothing, nothing has changed
                return;
            }
            this.matchingRows = matchingRows;
            refresh();
        }

        public List<Integer> getMatchingRows()
        {
            return matchingRows;
        }

        /**
         * Resets the internal row mappings from this filter to the previous filter.
         */
        @Override
        protected void reset()
        {
            toPrevious.clear();
            int inputSize = getInputSize();
            fromPrevious = new int[inputSize];  // fromPrevious is inherited protected
            for (int i = 0; i < inputSize; i++)
            {
                fromPrevious[i] = -1;
            }
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected void filter()
        {
            //Change so that we dont try to access an index in fromPrevious that doesnt exist
            int inputSize = fromPrevious.length;
            int current = 0;
            for (int i = 0; i < inputSize; i++)
            {
                if (test(i))
                {
                    toPrevious.add(i);
                    // generate inverse map entry while we are here
                    fromPrevious[i] = current++;
                }
            }
        }

        /**
         * Tests whether the given row (in this filter's coordinates) should
         * be added. <p>
         * <p/>
         * PENDING JW: why is this public? called from a protected method?
         *
         * @param row the row to test
         * @return true if the row should be added, false if not.
         */
        public boolean test(int row)
        {
            //Pass through
            if (matchingRows == null)
            {
                return true;
            }

            //Find the model row no by looking at the recno column
            Integer value = (Integer) getInputValue(row, 0);
            return matchingRows.contains(value);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int getSize()
        {
            return toPrevious.size();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected int mapTowardModel(int row)
        {
            if (row >= toPrevious.size())
            {
                return row;
            }
            return toPrevious.get(row);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected void init()
        {
            toPrevious = new ArrayList<Integer>();
        }
    }

    /**
     * Table Column Model shared between tables, to synchronize column resizing and moving but has own selection
     * listener for independent table selections
     */
    public static class TableSyncColumnModel extends DefaultTableColumnModelExt
    {
        public TableSyncColumnModel()
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

        public TableColumnModelExt createTableColumnExtModel(JTable callback)
        {
            return new InternalTableSyncColumnModel(callback, this);
        }

        static class InternalTableSyncColumnModel implements TableColumnModelExt
        {
            /**
             * Model for keeping track of column selections
             */
            protected ListSelectionModel selectionModel;
            protected SelectionListener selectionListener;
            private TableSyncColumnModel delegate;

            public InternalTableSyncColumnModel(JTable callback, TableSyncColumnModel delegate)
            {
                super();
                this.delegate = delegate;
                selectionListener = new SelectionListener(callback);
                setSelectionModel(createSelectionModel());
                setColumnSelectionAllowed(true);
            }

            protected ListSelectionModel createSelectionModel()
            {
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
                                if (listeners[i + 1] == table)
                                {
                                    ((TableColumnModelListener) listeners[i + 1]).columnSelectionChanged(e);
                                }
                            }

                        }
                    }
                }
            }

            public static final String IDENT = "$Id: SyncedTables.java 2715 2008-09-25 09:49:38Z paultaylor $";
        }
    }

    static class ListenerGate implements AdjustmentListener, MouseListener
    {
        private boolean isOpen;
        private EventListener listener;

        /**
         * Create initially open.
         */
        public ListenerGate(EventListener listener)
        {
            this.listener = listener;
            isOpen = true;
        }

        /**
         * Create with initial state.
         */
        public ListenerGate(boolean initiallyOpen, EventListener listener)
        {
            this.listener = listener;
            isOpen = initiallyOpen;
        }

        public EventListener getRealListener()
        {
            return listener;
        }

        /**
         * Is this open?
         */
        public boolean isOpen()
        {
            return (isOpen);
        }

        /**
         * Set this open or closed, returning prior state.
         */
        public void setOpen(boolean isOpen)
        {
            this.isOpen = isOpen;
        }

        /**
         * @param event
         */
        public void adjustmentValueChanged(final AdjustmentEvent event)
        {
            if (isOpen())
            {
                ((AdjustmentListener) listener).adjustmentValueChanged(event);
            }
        }

        /**
         * @param e
         */
        public void mousePressed(final MouseEvent e)
        {
            if (isOpen())
            {
                ((MouseListener) listener).mousePressed(e);
            }
        }

        /**
         * @param e
         */
        public void mouseClicked(final MouseEvent e)
        {
            if (isOpen())
            {
                ((MouseListener) listener).mouseClicked(e);
            }
        }

        /**
         * @param e
         */
        public void mouseReleased(final MouseEvent e)
        {
            if (isOpen())
            {
                ((MouseListener) listener).mouseReleased(e);
            }
        }

        /**
         * @param e
         */
        public void mouseEntered(final MouseEvent e)
        {
            if (isOpen())
            {
                ((MouseListener) listener).mouseEntered(e);
            }
        }

        /**
         * @param e
         */
        public void mouseExited(final MouseEvent e)
        {
            if (isOpen())
            {
                ((MouseListener) listener).mouseExited(e);
            }
        }
    }

    interface ScrollPaneAdjustmentListener extends AdjustmentListener, MouseListener
    {

    }

    public static class CompleteScrollPaneAdjustmentListener extends MouseAdapter implements ScrollPaneAdjustmentListener

    {
        /**
         * If this is set the listener wont respond to adjustments made
         */
        protected boolean unlisten = false;

        /**
         * The ScrollBar that we are listening on adjustments for
         */
        protected JScrollBar parent = null;

        protected List siblings = null;

        /**
         * Set to true so when a mouse button has been clicked on a scrollPane to adjust it but has not yet been released
         */
        protected boolean isPressedOnScrollPane = false;


        /**
         * Initilise with its parent and sibling listeners
         */
        public CompleteScrollPaneAdjustmentListener(final JScrollBar par, final List siblings)
        {
            parent = par;
            this.siblings = siblings;
        }

        /**
         * The ScrollPane we are listening on.
         *
         * @return
         */
        public final JScrollBar getParent()
        {
            return parent;
        }

        /**
         * Enable/Disable listening on the event.
         *
         * @param unl
         */
        public final void setUnlisten(final boolean unl)
        {
            unlisten = unl;
        }

        /**
         * If in the process of adjusting with mouse or if this adjustment listener has been
         * disabled return.
         * <p/>
         * Otherwise if adjustment has changed on this scrollbar,disable
         * all other adjustment listeners, pass the adjustment
         * value to the others scrollbar. Then allow the other adjustment
         * listeners to relisten
         */
        public void adjustmentValueChanged(final AdjustmentEvent e)
        {
            //MainWindow.logger.finest("AdjustmentEventEnter" + e.getValue());
            if (isPressedOnScrollPane)
            {
                return;
            }

            if (unlisten)
            {
                return;
            }

            //MainWindow.logger.finest("AdjustmentEvent:value1" + e.getValue());
            updateOtherPanes(e.getValue());
        }

        /**
         * Started an Adjustment using Mouse
         *
         * @param e
         */
        public final void mousePressed(final MouseEvent e)
        {
            isPressedOnScrollPane = true;
        }

        /**
         * Finished an Adjustment using Mouse, we do the update here because the Mouse will have blocked all
         * adjustments even the last one so we need to do the update here.
         *
         * @param e
         */
        public final void mouseReleased(final MouseEvent e)
        {
            isPressedOnScrollPane = false;
            //MainWindow.logger.finest("MouseEvent:value1" + e.paramString());
            updateOtherPanes(this.getParent().getValue());
        }

        /**
         * This does the update to the other Panes
         */
        private void updateOtherPanes(int value)
        {
            for (Object sibling : siblings)
            {
                final EventListener tmpSpAj = ((ListenerGate) sibling).getRealListener();
                if (tmpSpAj != this)
                {
                    if (tmpSpAj instanceof CompleteScrollPaneAdjustmentListener)
                    {
                        ((CompleteScrollPaneAdjustmentListener) tmpSpAj).setUnlisten(true);
                        ((CompleteScrollPaneAdjustmentListener) tmpSpAj).getParent().setValue(value);
                        ((CompleteScrollPaneAdjustmentListener) tmpSpAj).setUnlisten(false);
                    }
                    else
                    {
                        //Do Nothing
                    }
                }
            }
        }
    }

    public static void main(String args[]) throws Exception
    {
        //Create tables
        SyncedTables test = new SyncedTables();
        test.run(3, 20000, 22);

        //Memory Used
        MemoryUsage mu = ManagementFactory.getMemoryMXBean().getHeapMemoryUsage();
        System.out.println("Using " + (mu.getUsed() / 1024 / 1024) + "Mb" + "Max Allowed " + (mu.getMax() / 1024 / 1024) + "Mb");
    }
}
