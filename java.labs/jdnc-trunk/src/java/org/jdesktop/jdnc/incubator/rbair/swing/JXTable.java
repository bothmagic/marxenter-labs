/*
 * $Id: JXTable.java 49 2004-09-08 20:48:33Z gphilipp $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */

package org.jdesktop.jdnc.incubator.rbair.swing;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;
import java.util.regex.Pattern;

import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.swing.ActionMap;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.ListSelectionModel;
import javax.swing.UIDefaults;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

import org.jdesktop.jdnc.incubator.rbair.swing.decorator.ComponentAdapter;
import org.jdesktop.jdnc.incubator.rbair.swing.decorator.FilterPipeline;
import org.jdesktop.jdnc.incubator.rbair.swing.decorator.Highlighter;
import org.jdesktop.jdnc.incubator.rbair.swing.decorator.HighlighterPipeline;
import org.jdesktop.jdnc.incubator.rbair.swing.decorator.PipelineEvent;
import org.jdesktop.jdnc.incubator.rbair.swing.decorator.PipelineListener;
import org.jdesktop.jdnc.incubator.rbair.swing.decorator.Sorter;
import org.jdesktop.jdnc.incubator.rbair.swing.table.ColumnHeaderRenderer;
import org.jdesktop.jdnc.incubator.rbair.swing.table.DefaultTableModelExt;
import org.jdesktop.jdnc.incubator.rbair.swing.table.TableColumnExt;


/**
 * JXTable
 *
 * @author Ramesh Gupta
 * @author Amy Fowler
 * @author Mark Davidson
 */
public class JXTable extends JTable implements PipelineListener, Searchable {

public static boolean TRACE = false;
    /**
     * Printing mode that prints the table at its current size,
     * spreading both columns and rows across multiple pages if necessary.
     */
    public static final int PRINT_MODE_NORMAL = 0;

    /**
     * Printing mode that scales the output smaller, if necessary,
     * to fit the table's entire width (and thereby all columns) on each page;
     * Rows are spread across multiple pages as necessary
     */
    public static final int PRINT_MODE_FIT_WIDTH = 1;

    protected Sorter			sorter = null;
    protected FilterPipeline		filters = null;
    protected HighlighterPipeline	highlighters = null;

    // MUST ALWAYS ACCESS dataAdapter through accessor method!!!
    private final ComponentAdapter dataAdapter = new TableAdapter(this);

    // No need to define a separate JTableHeader subclass!
    private final static MouseAdapter	headerListener = new MouseAdapter() {
        // MouseAdapter must be stateless
        public void mouseClicked(MouseEvent e) {
            JTableHeader	header = (JTableHeader) e.getSource();
            JXTable		table = (JXTable) header.getTable();

            if ((e.getModifiersEx() & e.SHIFT_DOWN_MASK) == e.SHIFT_DOWN_MASK) {
                table.resetSorter();
            }
            else {
                table.setSorter(header.getColumnModel().getColumnIndexAtX(e.getX()));
            }
            header.repaint();
        }
    };

    /**
     * A flag to indicate whether or not the table is currently being printed.
     * Used by print() and prepareRenderer() to disable indication of the
     * selection and focused cell while printing.
     */
    private transient boolean isPrinting = false;

    private boolean sortable = false;
    private int visibleRowCount = 18;

    public JXTable() {
        init();
    }

    public JXTable(TableModel dm) {
        super(dm);
        init();
    }

    public JXTable(TableModel dm, TableColumnModel cm) {
        super(dm, cm);
        init();
    }

    public JXTable(TableModel dm, TableColumnModel cm, ListSelectionModel sm) {
        super(dm, cm, sm);
        init();
    }

    public JXTable(int numRows, int numColumns) {
        super(numRows, numColumns);
        init();
    }

    public JXTable(Vector rowData, Vector columnNames) {
        super(rowData, columnNames);
        init();
    }

    public JXTable(Object[][] rowData, Object[] columnNames) {
        super(rowData, columnNames);
        init();
    }

    protected void init() {
        //    setSortable(true);
        // Register the actions that this class can handle.
        ActionMap map = getActionMap();
        map.put("print", new Actions("print"));
        map.put("find", new Actions("find"));

        // Add a link handler to to the table.
        // XXX note: this listener represents overhead if no columns are links.
        // Beter to detect if the table has a link column and add the handler.
//        LinkHandler handler = new LinkHandler();
//        addMouseListener(handler);
//        addMouseMotionListener(handler);
    }

    /**
     * Returns a new instance of the default renderer for the specified class.
     * This differs from <code>getDefaultRenderer()</code> in that it returns a <b>new</b>
     * instance each time so that the renderer may be set and customized on
     * a particular column.
     *
     * @param columnClass Class of value being rendered
     * @return TableCellRenderer instance which renders values of the specified type
     */
    public TableCellRenderer getNewDefaultRenderer(Class columnClass) {
        TableCellRenderer renderer = getDefaultRenderer(columnClass);
        if (renderer != null) {
            try {
                return (TableCellRenderer) renderer.getClass().newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private void setLazyValue(Hashtable h, Class c, String s) {
        h.put(c, new UIDefaults.ProxyLazyValue(s));
    }

    private void setLazyRenderer(Class c, String s) {
        setLazyValue(defaultRenderersByColumnClass, c, s);
    }

    /**
     * Creates default cell renderers for objects, numbers, doubles, dates,
     * booleans, icons, and links.
     *
     */
    protected void createDefaultRenderers() {
        // This duplicates JTable's functionality in order to make the renderers
        // available in getNewDefaultRenderer();  If JTable's renderers either
        // were public, or it provided a factory for *new* renderers, this would
        // not be needed

        defaultRenderersByColumnClass = new UIDefaults();

        // Objects
        setLazyRenderer(Object.class,
                        "javax.swing.table.DefaultTableCellRenderer");

        // Numbers
        setLazyRenderer(Number.class, "org.jdesktop.swing.JXTable$NumberRenderer");

        // Doubles and Floats
        setLazyRenderer(Float.class, "org.jdesktop.swing.JXTable$DoubleRenderer");
        setLazyRenderer(Double.class, "org.jdesktop.swing.JXTable$DoubleRenderer");

        // Dates
        setLazyRenderer(Date.class, "org.jdesktop.swing.JXTable$DateRenderer");

        // Icons and ImageIcons
        setLazyRenderer(Icon.class, "org.jdesktop.swing.JXTable$IconRenderer");
        setLazyRenderer(ImageIcon.class, "org.jdesktop.swing.JXTable$IconRenderer");

        // Booleans
        setLazyRenderer(Boolean.class, "org.jdesktop.swing.JXTable$BooleanRenderer");

        // Other
//        setLazyRenderer(Link.class, "org.jdesktop.swing.JXTable$LinkRenderer");
    }


    /**
     * A small class which dispatches actions.
     * TODO: Is there a way that we can make this static?
     */
    private class Actions extends UIAction {
        Actions(String name) {
            super(name);
        }

        public void actionPerformed(ActionEvent evt) {
            if ("print".equals(getName())) {
                try {
                    print();
                }
                catch (PrinterException ex) {
                    //REMIND(aim): should invoke pluggable application error handler
                    ex.printStackTrace();
                }
            }
            else if ("find".equals(getName())) {
                find();
            }
        }
    }

    private JXFindDialog dialog = null;

    private void find() {
        if (dialog == null) {
            dialog = new JXFindDialog(this);
        }
        dialog.setVisible(true);
    }

    public FilterPipeline getFilters() {
        return filters;
    }

    public void setFilters(FilterPipeline pipeline) {
        filters = pipeline;
        use(filters);
    }

    public HighlighterPipeline getHighlighters() {
        return highlighters;
    }

    public void setHighlighters(HighlighterPipeline pipeline) {
        highlighters = pipeline;
    }

    private void removeSorter() {
        sorter = null;
        getTableHeader().repaint();
    }

    public void tableChanged(TableModelEvent e) {
        Selection	selection = new Selection(this);
        if (filters != null) {
            filters.flush();	// will call contentsChanged()
        }
        else if (sorter != null) {
            sorter.refresh();
        }
        restoreSelection(selection);
        super.tableChanged(e);
    }

    public void contentsChanged(PipelineEvent e) {
        removeSorter();
        clearSelection();

        // Force private rowModel in JTable to null;
        setRowHeight(getRowHeight());	// Ugly!

        if (getAutoCreateColumnsFromModel()) {
            // This will effect invalidation of the JTable and JTableHeader.
            createDefaultColumnsFromModel();
        }

        revalidate();
        repaint();
    }

    public int getRowCount() {
        int count;
        if (filters == null) {
            count = getModel().getRowCount();
        }
        else {
            count = filters.getOutputSize();
        }
        return count;
    }

    /**
     * Convert row index from view coordinates to model coordinates
     * accounting for the presence of sorters and filters.
     *
     * @param row row index in view coordinates
     * @return row index in model coordinates
     */
    public int convertRowIndexToModel(int row) {
        if (sorter == null) {
            if (filters == null) {
                return row;
            }
            else {
                // delegate conversion to the filters pipeline
                return filters.convertRowIndexToModel(row);
            }
        }
        else {
            // after performing its own conversion, the sorter
            // delegates the conversion to the filters pipeline, if any
            return sorter.convertRowIndexToModel(row);
        }
    }

    /**
     * Convert row index from model coordinates to view coordinates
     * accounting for the presence of sorters and filters.
     *
     * @param row row index in model coordinates
     * @return row index in view coordinates
     */
    public int convertRowIndexToView(int row) {
        if (sorter == null) {
            if (filters == null) {
                return row;
            }
            else {
                // delegate conversion to the filters pipeline
                return filters.convertRowIndexToView(row);
            }
        }
        else {
            // before performing its own conversion, the sorter
            // delegates the conversion to the filters pipeline, if any
            return sorter.convertRowIndexToView(row);
        }
    }

    /**
     * {@inheritDoc}
     */
    public Object getValueAt(int row, int column) {
        if (sorter == null) {		// have interactive sorter?
            if (filters == null) {	// have filter pipeline?
                return super.getValueAt(row, column);	// unsorted, unfiltered
            }
            else {	// filtered
                return filters.getValueAt(row, convertColumnIndexToModel(column));
            }
        }
        else {	// interactively sorted, and potentially filtered
            return sorter.getValueAt(row, convertColumnIndexToModel(column));
        }
    }

    public void setValueAt(Object aValue, int row, int column) {
        if (sorter == null) {
            if (filters == null) {
                super.setValueAt(aValue, row, column);
            }
            else {
                filters.setValueAt(aValue, row, convertColumnIndexToModel(column));
            }
        }
        else {
            sorter.setValueAt(aValue, row, convertColumnIndexToModel(column));
        }
    }

    public boolean isCellEditable(int row, int column) {
        if (sorter == null) {
            if (filters == null) {
                return super.isCellEditable(row, column);
            }
            else {
                return filters.isCellEditable(row, convertColumnIndexToModel(column));
            }
        }
        else {
            return sorter.isCellEditable(row, convertColumnIndexToModel(column));
        }
    }

    public void setModel(TableModel newModel) {
        super.setModel(newModel);
        use(filters);
    }

    /**
     * setModel() and setFilters() may be called in either order.
     *
     * @param pipeline
     */
    private void use(FilterPipeline pipeline) {
        if (pipeline != null) {
            pipeline.addPipelineListener(this);
            pipeline.assign(getComponentAdapter());
            pipeline.flush();
        }
    }

    /**
     * Adds private mouse listener to the table header (for sorting support)
     * before handing it off to the super class for processing.
     *
     * @param tableHeader
     */
    public void setTableHeader(JTableHeader tableHeader) {
        // This method is also called during construction of JTable
        if (tableHeader != null) {
            tableHeader.addMouseListener(headerListener);
            tableHeader.setDefaultRenderer(new ColumnHeaderRenderer());
        }
        super.setTableHeader(tableHeader);
    }

/*
    protected JTableHeader createDefaultTableHeader() {
        return new JTableHeaderExt(columnModel);
    }
*/
    private void restoreSelection(Selection selection) {
        clearSelection();	// call overridden version

        for (int i = 0; i < selection.selected.length; i++) {
            if (selection.selected[i] != selection.lead) {
                int	index = convertRowIndexToView(selection.selected[i]);
                selectionModel.addSelectionInterval(index, index);
            }
        }

        if (selection.lead >= 0) {
            selection.lead = convertRowIndexToView(selection.lead);
            selectionModel.addSelectionInterval(selection.lead, selection.lead);
        }
    }

    /*
     * Used by headerListener
     */
    protected void resetSorter() {
        if (sorter != null) {
            Selection selection = new Selection(this);
            removeSorter();
            restoreSelection(selection);
        }
    }

    private Sorter refreshSorter(int columnIndex) {
        TableColumn col = getColumnModel().getColumn(columnIndex);
        if (col instanceof TableColumnExt) {
            TableColumnExt column = (TableColumnExt) col;
            Sorter	newSorter = column.getSorter();
            if (newSorter != null) {
                // filter pipeline may be null!
                newSorter.interpose(filters, dataAdapter, sorter);	// refresh
                return newSorter;
            }
        }
        return sorter;
    }

    /*
     * Used by headerListener
     */
    protected void setSorter(int columnIndex) {
        Selection	selection = new Selection(this);
        if (sorter == null) {
            sorter = refreshSorter(columnIndex);	// create and refresh
        }
        else {
            int	modelColumnIndex = convertColumnIndexToModel(columnIndex);
            if (sorter.getColumnIndex() == modelColumnIndex) {
                sorter.toggle();
            }
            else {
                sorter = refreshSorter(columnIndex);	// create and refresh
            }
        }
        restoreSelection(selection);
    }

    /*
     * Used by ColumnHeaderRenderer.getTableCellRendererComponent()
     */
    public Sorter getSorter(int columnIndex) {
        return sorter == null ? null :
            sorter.getColumnIndex() == convertColumnIndexToModel(columnIndex) ?
            sorter : null;
    }

    /**
     * Remove all columns
     */
    protected void removeColumns() {
        /** @todo promote this method to superclass, and
         * change createDefaultColumnsFromModel() to call this method */
        TableColumnModel cm = getColumnModel();
        while (cm.getColumnCount() > 0) {
            cm.removeColumn(cm.getColumn(0));
        }
    }

    public List getColumns() {
        return null; /** @todo Implement this */
    }

    public TableColumnExt getColumnExt(Object identifier) {
        return (TableColumnExt)super.getColumn(identifier);
    }

    public TableColumnExt getColumnExt(int viewColumnIndex) {
        return (TableColumnExt) getColumnModel().getColumn(viewColumnIndex);
    }

    /**
     * Sets &quot;sortable&quot; property indicating whether or not this table
         * supports sortable columns.  If <code>sortable</code> is <code>true</code>
     * then sorting will be enabled on all columns whose <code>sortable</code>
         * property is <code>true</code>.  If <code>sortable</code> is <code>false</code>
         * then sorting will be disabled for all columns, regardless of each column's
         * individual <code>sorting</code> property.  The default is <code>true</code>.
     * @see TableColumnExt#isSortable
     * @see TableColumnExt#setSortable
     * @param sortable boolean indicating whether or not this table supports
     *        sortable columns
     */
    /*    public void setSortable(boolean sortable) {
            boolean old = this.sortable;
            this.sortable = sortable;
            if (!old && sortable) {
                TableModel model = getModel();
                if (model != null) {
                    // wrap model
                    super.setModel(new TableSorter(getModel()));
                }
                setTableHeader(new SortableTableHeader(getColumnModel()));
            } else if (old && !sortable) {
                TableModel model = getModel();
                if (model instanceof TableSorter) {
                    // unwrap model
                    super.setModel(((TableSorter)model).getModel());
                }
                setTableHeader(createDefaultTableHeader());
            }
            firePropertyChange("sortable", old, sortable);
        }
        public boolean isSortable() {
            return sortable;
        }
     */

    /**
     * Returns the default table model object, which is
     * a <code>DefaultTableModel</code>.  A subclass can override this
     * method to return a different table model object.
     *
     * @return the default table model object
     * @see DefaultTableModelExt
     */
    protected TableModel createDefaultDataModel() {
        return new DefaultTableModelExt();
    }

    public void createDefaultColumnsFromModel() {
        TableModel model = getModel();
        if (model != null) {
            // Create new columns from the data model info
            // Note: it's critical to create the new columns before
            // deleting the old ones. Why?
            int modelColumnCount = model.getColumnCount();
            TableColumn newColumns[] = new TableColumn[modelColumnCount];
            for (int i = 0; i < newColumns.length; i++) {
                newColumns[i] = createColumn(i);
            }

            // Remove any current columns
            TableColumnModel columnModel = getColumnModel();
            while (columnModel.getColumnCount() > 0) {
                columnModel.removeColumn(columnModel.getColumn(0));
            }

            // Now add the new columns to the column model
            for (int i = 0; i < newColumns.length; i++) {
                addColumn(newColumns[i]);
            }
        }
    }

    protected TableColumn createColumn(int modelIndex) {
        return new TableColumnExt(modelIndex);
    }


    /**
     * Returns the margin between columns.
     *
     * @return the margin between columns
     */
    public int getColumnMargin() {
        return getColumnModel().getColumnMargin();
    }

    /**
     * Sets the margin between columns.
     *
         * @param value margin between columns; must be greater than or equal to zero.
     */
    public void setColumnMargin(int value) {
        getColumnModel().setColumnMargin(value);
    }

    /**
     * Returns the selection mode used by this table's selection model.
     *
     * @return the selection mode used by this table's selection model
     */
    public int getSelectionMode() {
        return getSelectionModel().getSelectionMode();
    }

    /**
     * Returns the decorated <code>Component</code> used as a stamp to render
     * the specified cell. Overrides superclass version to provide support for
     * cell decorators.
     *
     * @param renderer the <code>TableCellRenderer</code> to prepare
     * @param row the row of the cell to render, where 0 is the first row
         * @param column the column of the cell to render, where 0 is the first column
     * @return the decorated <code>Component</code> used as a stamp to render the specified cell
     * @see Highlighter
     */
    public Component prepareRenderer(TableCellRenderer renderer, int row,
                                     int column) {
        Component stamp = super.prepareRenderer(renderer, row, column);
        if (highlighters == null) {
            return stamp;	// no need to decorate renderer with highlighters
        }
        else {
            // MUST ALWAYS ACCESS dataAdapter through accessor method!!!
            ComponentAdapter	adapter = getComponentAdapter();
            adapter.row = row;
            adapter.column = column;
            return highlighters.apply(stamp, adapter);
        }
    }

    protected ComponentAdapter getComponentAdapter() {
        // MUST ALWAYS ACCESS dataAdapter through accessor method!!!
        return dataAdapter;
    }

    public int search(String searchString) {
        return search(searchString, -1);
    }

    public int search(String searchString, int columnIndex) {
        Pattern pattern = null;
        if (searchString != null) {
            return search(Pattern.compile(searchString, 0), columnIndex);
        }
        return -1;
    }

    public int search(Pattern pattern) {
        return search(pattern, -1);
    }

    public int search(Pattern pattern, int startIndex) {
        return search(pattern, startIndex, false);
    }

    // Save the last column with the match.
    private int lastCol = 0;

    /**
     * @param startIndex row to start search
     * @return row with a match.
     */
    public int search(Pattern pattern, int startIndex, boolean backwards) {
        if (pattern == null) {
            lastCol = 0;
            return -1;
        }
        int rows = getRowCount();
        int endCol = getColumnCount();

        int startRow = startIndex + 1;
        int matchRow = -1;

        if (backwards == true) {
            for (int r = startRow; r >= 0 && matchRow == -1; r--) {
                for (int c = endCol; c >= 0; c--) {
                    Object value = getValueAt(r, c);
                    if ( (value != null) &&
                        pattern.matcher(value.toString()).find()) {
                        changeSelection(r, c, false, false);
                        matchRow = r;
                        lastCol = c;
                        break; // No need to search other columns
                    }
                }
                if (matchRow == -1) {
                    lastCol = endCol;
                }
            }
        }
        else {
            for (int r = startRow; r < rows && matchRow == -1; r++) {
                for (int c = lastCol; c < endCol; c++) {
                    Object value = getValueAt(r, c);
                    if ( (value != null) &&
                        pattern.matcher(value.toString()).find()) {
                        changeSelection(r, c, false, false);
                        matchRow = r;
                        lastCol = c;
                        break; // No need to search other columns
                    }
                }
                if (matchRow == -1) {
                    lastCol = 0;
                }
            }
        }

        if (matchRow != -1) {
            Object viewport = getParent();
            if (viewport instanceof JViewport) {
                Rectangle rect = getCellRect(getSelectedRow(), 0, true);
                Point pt = ( (JViewport) viewport).getViewPosition();
                rect.setLocation(rect.x - pt.x, rect.y - pt.y);
                ( (JViewport) viewport).scrollRectToVisible(rect);
            }
        }
        return matchRow;
    }

    public void setVisibleRowCount(int visibleRowCount) {
        this.visibleRowCount = visibleRowCount;
    }

    public int getVisibleRowCount() {
        return visibleRowCount;
    }

    public Dimension getPreferredScrollableViewportSize() {
        Dimension prefSize = super.getPreferredScrollableViewportSize();

        // JTable hardcodes this to 450 X 400, so we'll calculate it
        // based on the preferred widths of the columns and the
        // visibleRowCount property instead...

        if (prefSize.getWidth() == 450 && prefSize.getHeight() == 400) {
            TableColumnModel columnModel = getColumnModel();
            int columnCount = columnModel.getColumnCount();

            int w = 0;
            for (int i = 0; i < columnCount; i++) {
                TableColumn column = columnModel.getColumn(i);
                initializeColumnPreferredWidth(column);
                w += column.getPreferredWidth();
            }
            prefSize.width = w;
            JTableHeader header = getTableHeader();
            //remind(aim): height is still off...???
            int rowCount = getVisibleRowCount();
            prefSize.height = rowCount * getRowHeight() +
                (header != null? header.getPreferredSize().height : 0);
            setPreferredScrollableViewportSize(prefSize);
        }
        return prefSize;
    }

    /**
     * Initialize the preferredWidth of the specified column based on the
     * column's prototypeValue property.  If the column is not an
     * instance of <code>TableColumnExt</code> or prototypeValue is <code>null</code>
     * then the preferredWidth is left unmodified.
     * @see TableColumnExt#setPrototypeValue
     * @param column TableColumn object representing view column
     */
    protected void initializeColumnPreferredWidth(TableColumn column) {

        if (column instanceof TableColumnExt) {
            TableColumnExt columnx = (TableColumnExt) column;
            Object prototypeValue = columnx.getPrototypeValue();
            if (prototypeValue != null) {
                // calculate how much room the prototypeValue requires
                TableCellRenderer renderer = getCellRenderer(0,
                    convertColumnIndexToView(columnx.getModelIndex()));
                Component comp = renderer.getTableCellRendererComponent(this,
                    prototypeValue, false, false, 0, 0);
                int prefWidth = comp.getPreferredSize().width;

                // now calculate how much room the column header wants
                renderer = columnx.getHeaderRenderer();
                if (renderer == null) {
                    JTableHeader header = getTableHeader();
                    if (header != null) {
                        renderer = header.getDefaultRenderer();
                    }
                }
                if (renderer != null) {
                    comp = renderer.getTableCellRendererComponent(this,
                             columnx.getHeaderValue(), false, false, 0,
                             convertColumnIndexToView(columnx.getModelIndex()));

                    prefWidth = Math.max(comp.getPreferredSize().width, prefWidth);
                }
                prefWidth += getColumnModel().getColumnMargin();
                columnx.setPreferredWidth(prefWidth);
            }
        }
    }

// Printing Support extracted from 1.5

    /**
     *
     * @return boolean indicating whether or not this <code>JTable</code>
     *         is currently being printed
     */
    public boolean isPrinting() {
        return isPrinting;
    }

    /**
     * A convenience method that displays a printing dialog, and then prints
     * this <code>JTable</code> in mode <code>PRINT_MODE_FIT_WIDTH</code>,
     * with no header or footer text.
     *
     * @return true, unless the user cancels the print dialog
     * @throws PrinterException if an error in the print system causes the job
     *                          to be aborted
     * @see #print(int, MessageFormat, MessageFormat, boolean, PrintRequestAttributeSet)
     * @see #getPrintable
     *
     * @since 1.5
     */
    public boolean print() throws PrinterException {

        return print(PRINT_MODE_FIT_WIDTH);
    }

    /**
     * A convenience method that displays a printing dialog, and then prints
     * this <code>JTable</code> in the given printing mode,
     * with no header or footer text.
     *
         * @param  printMode        the printing mode that the printable should use:
     *                          <code>PRINT_MODE_NORMAL</code> or
     *                          <code>PRINT_MODE_FIT_WIDTH</code>
     * @return true, unless the user cancels the print dialog
     * @throws PrinterException if an error in the print system causes the job
     *                          to be aborted
     * @throws IllegalArgumentException if passed an invalid print mode
     * @see #print(int, MessageFormat, MessageFormat, boolean, PrintRequestAttributeSet)
     * @see #getPrintable
     *
     * @since 1.5
     */
    public boolean print(int printMode) throws PrinterException {

        return print(printMode, null, null);
    }

    /**
     * A convenience method that displays a printing dialog, and then prints
     * this <code>JTable</code> in the given printing mode,
     * with the specified header and footer text.
     *
         * @param  printMode        the printing mode that the printable should use:
     *                          <code>PRINT_MODE_NORMAL</code> or
     *                          <code>PRINT_MODE_FIT_WIDTH</code>
         * @param  headerFormat     a <code>MessageFormat</code> specifying the text
     *                          to be used in printing a header,
     *                          or null for none
         * @param  footerFormat     a <code>MessageFormat</code> specifying the text
     *                          to be used in printing a footer,
     *                          or null for none
     * @return true, unless the user cancels the print dialog
     * @throws PrinterException if an error in the print system causes the job
     *                          to be aborted
     * @throws IllegalArgumentException if passed an invalid print mode
     * @see #print(int, MessageFormat, MessageFormat, boolean, PrintRequestAttributeSet)
     * @see #getPrintable
     *
     * @since 1.5
     */
    public boolean print(int printMode,
                         MessageFormat headerFormat,
                         MessageFormat footerFormat) throws PrinterException {

        return print(printMode, headerFormat, footerFormat, true, null);
    }

    /**
     * Print this <code>JTable</code>. Takes steps that the majority of
     * developers would take in order to print a <code>JTable</code>.
     * In short, it prepares the table, calls <code>getPrintable</code> to
     * fetch an appropriate <code>Printable</code>, and then sends it to the
     * printer.
     * <p>
     * A <code>boolean</code> parameter allows you to specify whether or not
     * a printing dialog is displayed to the user. When it is, the user may
     * use the dialog to change printing attributes or even cancel the print.
     * Another parameter allows for printing attributes to be specified
     * directly. This can be used either to provide the initial values for the
     * print dialog, or to supply any needed attributes when the dialog is not
     * shown.
     * <p>
     * Before fetching the printable, this method prepares the table in order
     * to get the most desirable printed result. If the table is currently
     * in an editing mode, it terminates the editing as gracefully as
     * possible. It also ensures that the the table's current selection and
     * focused cell are not indicated in the printed output. This is handled on
     * the view level, and only for the duration of the printing, thus no
     * notification needs to be sent to the selection models.
     * <p>
     * REMIND(aim): This method is temporarily hacked to execute the print
     * asynchronously to get around the problem of having the damage from
     * the print dialogs remain in the application window while the print
     * process ties up the event dispatch thread; ultimately we need to
     * find a way NOT to tie up the EDT during the print processing.
     *
     * See {@link #getPrintable} for further description on how the
     * table is printed.
     *
         * @param  printMode        the printing mode that the printable should use:
     *                          <code>PRINT_MODE_NORMAL</code> or
     *                          <code>PRINT_MODE_FIT_WIDTH</code>
         * @param  headerFormat     a <code>MessageFormat</code> specifying the text
     *                          to be used in printing a header,
     *                          or null for none
         * @param  footerFormat     a <code>MessageFormat</code> specifying the text
     *                          to be used in printing a footer,
     *                          or null for none
     * @param  showPrintDialog  whether or not to display a print dialog
     * @param  attr             a <code>PrintRequestAttributeSet</code>
     *                          specifying any printing attributes,
     *                          or null for none
     * @return true, unless the print dialog is shown and the user cancels it
     * @throws PrinterException if an error in the print system causes the job
     *                          to be aborted
     * @throws IllegalArgumentException if passed an invalid print mode
     * @see #getPrintable
     *
     * @since 1.5
     */
    public boolean print(int printMode,
                         MessageFormat headerFormat,
                         MessageFormat footerFormat,
                         boolean showPrintDialog,
                         PrintRequestAttributeSet attr) throws PrinterException {

        if (isEditing()) {
            // try to stop cell editing, and failing that, cancel it
            if (!getCellEditor().stopCellEditing()) {
                getCellEditor().cancelCellEditing();
            }
        }

        if (attr == null) {
            attr = new HashPrintRequestAttributeSet();
        }
        final PrintRequestAttributeSet attrset = attr;

        final PrinterJob job = PrinterJob.getPrinterJob();
        job.setPrintable(getPrintable(printMode, headerFormat, footerFormat));

        if (showPrintDialog && !job.printDialog(attrset)) {
            return false;
        }

        //REMIND(aim): temporary bandaid...
        // Before we tie up the EDT with the printing process (which could
        // take awhile), we want to ensure any damaged areas which appear
        // when the print dialog(s) is dismissed are repainted, otherwise
        // garbage will appear in the window while the print request executes.
        // To ensure this, we must delay the print request momentarily while
        // we wait for the native window system to deliver the paint event
        // which results in the repaint of the areas damaged by the now
        // hidden print dialogs.
        //
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            public void run() {
                EventQueue.invokeLater(new Runnable() {
                    public void run() {
                        // set a flag to disable indication of the selection and focused cell
                        isPrinting = true;
                        try {
                            // do the printing
                            job.print(attrset);
                        }
                        catch (PrinterException e) {
                            //REMIND(aim): how to handle throwing exception from asynchronous call?
                            e.printStackTrace();
                        }
                        finally {
                            // restore the flag
                            isPrinting = false;
                        }
                    }
                });
            }
        };
        timer.schedule(task, 100);

        //REMIND(aim): return value is currently bogus with above hack
        return true;
    }

    /**
     * Return a <code>Printable</code> for use in printing this JTable.
     * <p>
         * The <code>Printable</code> can be requested in one of two printing modes.
     * In both modes, it spreads table rows naturally in sequence across
     * multiple pages, fitting as many rows as possible per page.
     * <code>PRINT_MODE_NORMAL</code> specifies that the table be
     * printed at its current size. In this mode, there may be a need to spread
     * columns across pages in a similar manner to that of the rows. When the
     * need arises, columns are distributed in an order consistent with the
     * table's <code>ComponentOrientation</code>.
     * <code>PRINT_MODE_FIT_WIDTH</code> specifies that the output be
     * scaled smaller, if necessary, to fit the table's entire width
     * (and thereby all columns) on each page. Width and height are scaled
     * equally, maintaining the aspect ratio of the output.
     * <p>
     * The <code>Printable</code> heads the portion of table on each page
     * with the appropriate section from the table's <code>JTableHeader</code>,
     * if it has one.
     * <p>
     * Header and footer text can be added to the output by providing
     * <code>MessageFormat</code> arguments. The printing code requests
     * Strings from the formats, providing a single item which may be included
         * in the formatted string: an <code>Integer</code> representing the current
     * page number.
     * <p>
     * You are encouraged to read the documentation for
     * <code>MessageFormat</code> as some characters, such as single-quote,
     * are special and need to be escaped.
     * <p>
     * Here's an example of creating a <code>MessageFormat</code> that can be
     * used to print "Duke's Table: Page - " and the current page number:
     * <p>
     * <pre>
     *     // notice the escaping of the single quote
     *     // notice how the page number is included with "{0}"
         *     MessageFormat format = new MessageFormat("Duke''s Table: Page - {0}");
     * </pre>
     * <p>
     * The <code>Printable</code> constrains what it draws to the printable
     * area of each page that it prints. Under certain circumstances, it may
     * find it impossible to fit all of a page's content into that area. In
     * these cases the output may be clipped, but the implementation
     * makes an effort to do something reasonable. Here are a few situations
     * where this is known to occur, and how they may be handled by this
     * particular implementation:
     * <ul>
     *   <li>In any mode, when the header or footer text is too wide to fit
     *       completely in the printable area -- print as much of the text as
     *       possible starting from the beginning, as determined by the table's
     *       <code>ComponentOrientation</code>.
     *   <li>In any mode, when a row is too tall to fit in the
     *       printable area -- print the upper-most portion of the row
     *       and paint no lower border on the table.
     *   <li>In <code>JTable.PRINT_MODE_NORMAL</code> when a column
     *       is too wide to fit in the printable area -- print the center
     *       portion of the column and leave the left and right borders
     *       off the table.
     * </ul>
     * <p>
     * It is entirely valid for this <code>Printable</code> to be wrapped
     * inside another in order to create complex reports and documents. You may
     * even request that different pages be rendered into different sized
     * printable areas. The implementation must be prepared to handle this
     * (possibly by doing its layout calculations on the fly). However,
     * providing different heights to each page will likely not work well
     * with <code>PRINT_MODE_NORMAL</code> when it has to spread columns
     * across pages.
     * <p>
     * It is important to note that this <code>Printable</code> prints the
     * table at its current visual state, using the table's existing renderers.
     * <i>Before</i> calling this method, you may wish to <i>first</i> modify
     * the state of the table (such as to change the renderers, cancel editing,
     * or hide the selection).
     * <p>
     * Here's a simple example that calls this method to fetch a
     * <code>Printable</code>, shows a cross-platform print dialog, and then
     * prints the <code>Printable</code> unless the user cancels the dialog:
     * <p>
     * <pre>
         *     // prepare the table for printing here first (for example, hide selection)
     *
         *     // wrap in a try/finally so table can be restored even if something fails
     *     try {
     *         // fetch the printable
         *         Printable printable = table.getPrintable(JTable.PRINT_MODE_FIT_WIDTH,
     *                                                  new MessageFormat("My Table"),
     *                                                  new MessageFormat("Page - {0}"));
     *
     *         // fetch a PrinterJob
     *         PrinterJob job = PrinterJob.getPrinterJob();
     *
     *         // set the Printable on the PrinterJob
     *         job.setPrintable(printable);
     *
         *         // create an attribute set to store attributes from the print dialog
         *         PrintRequestAttributeSet attr = new HashPrintRequestAttributeSet();
     *
     *         // display a print dialog and record whether or not the user cancels it
     *         boolean printAccepted = job.printDialog(attr);
     *
     *         // if the user didn't cancel the dialog
     *         if (printAccepted) {
     *             // do the printing (may need to handle PrinterException)
     *             job.print(attr);
     *         }
     *     } finally {
     *         // restore the original table state here (for example, restore selection)
     *     }
     * </pre>
     *
     * @param  printMode     the printing mode that the printable should use:
     *                       <code>PRINT_MODE_NORMAL</code> or
     *                       <code>PRINT_MODE_FIT_WIDTH</code>
         * @param  headerFormat  a <code>MessageFormat</code> specifying the text to
     *                       be used in printing a header, or null for none
         * @param  footerFormat  a <code>MessageFormat</code> specifying the text to
     *                       be used in printing a footer, or null for none
     * @return a <code>Printable</code> for printing this JTable
     * @throws IllegalArgumentException if passed an invalid print mode
     * @see #PRINT_MODE_NORMAL
     * @see #PRINT_MODE_FIT_WIDTH
     * @see Printable
     * @see PrinterJob
     *
     * @since 1.5
     */
    public Printable getPrintable(int printMode,
                                  MessageFormat headerFormat,
                                  MessageFormat footerFormat) {

        return new TablePrintable(this, printMode, headerFormat, footerFormat);
    }

    static class TableAdapter extends ComponentAdapter {
        private final JTable table;

        /**
         * Constructs a <code>TableDataAdapter</code> for the specified
         * target component.
         *
         * @param component the target component
         */
        public TableAdapter(JTable component) {
            super(component);
            table = component;
        }

        /**
         * Typesafe accessor for the target component.
         *
         * @return the target component as a {@link javax.swing.JTable}
         */
        public JTable getTable() {
            return table;
        }

        /**
         * {@inheritDoc}
         */
        public boolean hasFocus() {
            //REMIND(aim): think through printing implications on decorators
            if (table instanceof JXTable && ( (JXTable) table).isPrinting()) {
                return false;
            }
            boolean rowIsAnchor = (table.getSelectionModel().
                                   getAnchorSelectionIndex() == row);
            boolean colIsAnchor =
                (table.getColumnModel().getSelectionModel().
                 getAnchorSelectionIndex() ==
                 column);
            return table.isFocusOwner() && (rowIsAnchor && colIsAnchor);

        }

        public String getColumnName(int columnIndex) {
            TableColumnModel	columnModel = table.getColumnModel();
            if (columnModel == null){
                return "Column " + columnIndex;
            }
            TableColumn			column = columnModel.getColumn(columnIndex);

            return column == null ? "" : column.getHeaderValue().toString();
        }

        public int getColumnCount() {
            return table.getModel().getColumnCount();
        }

        public int getRowCount() {
            return table.getModel().getRowCount();
        }

        /**
         * {@inheritDoc}
         */
        public Object getValueAt(int row, int column) {
            return table.getModel().getValueAt(row, viewToModel(column));
        }

        public Object getFilteredValueAt(int row, int column) {
            return table.getValueAt(row, column);	// in view coordinates
        }

        public void setValueAt(Object aValue, int row, int column) {
            table.getModel().setValueAt(aValue, row, viewToModel(column));
        }

        public boolean isCellEditable(int row, int column) {
            return table.getModel().isCellEditable(row, viewToModel(column));
        }

        /**
         * {@inheritDoc}
         */
        public boolean isSelected() {
            //REMIND(aim): think through printing implications on decorators
            if (table instanceof JXTable && ( (JXTable) table).isPrinting()) {
                return false;
            }
            return table.isCellSelected(row, column);
        }

        /**
         * {@inheritDoc}
         */
        public int modelToView(int columnIndex) {
            return table.convertColumnIndexToView(columnIndex);
        }

        /**
         * {@inheritDoc}
         */
        public int viewToModel(int columnIndex) {
            return table.convertColumnIndexToModel(columnIndex);
        }

    }

    private static class Selection {
        protected	final int[]	selected;	// used ONLY within save/restoreSelection();
        protected	int		lead = -1;
        protected Selection(JXTable table) {
            selected = table.getSelectedRows();	// in view coordinates
            for (int i = 0; i < selected.length; i++) {
                selected[i] = table.convertRowIndexToModel(selected[i]);	// model coordinates
            }

            if (selected.length > 0) {
                // convert lead selection index to model coordinates
                lead = table.convertRowIndexToModel(
                    table.getSelectionModel().getLeadSelectionIndex());
            }
        }
    }

    /*
     * Default Type-based Renderers:
     * JTable's default table cell renderer classes are private and
     * JTable:getDefaultRenderer() returns a *shared* cell renderer instance,
     * thus there is no way for us to instantiate a new instance of one of its
     * default renderers.  So, we must replicate the default renderer classes
     * here so that we can instantiate them when we need to create renderers
     * to be set on specific columns.
     */
    static class NumberRenderer extends DefaultTableCellRenderer {
        public NumberRenderer() {
            super();
            setHorizontalAlignment(JLabel.RIGHT);
        }
    }

    static class DoubleRenderer extends NumberRenderer {
        NumberFormat formatter;
        public DoubleRenderer() {
            super();
        }

        public void setValue(Object value) {
            if (formatter == null) {
                formatter = NumberFormat.getInstance();
            }
            setText( (value == null) ? "" : formatter.format(value));
        }
    }

    static class DateRenderer extends DefaultTableCellRenderer {
        DateFormat formatter;
        public DateRenderer() {
            super();
        }

        public void setValue(Object value) {
            if (formatter == null) {
                formatter = DateFormat.getDateInstance();
            }
            setText( (value == null) ? "" : formatter.format(value));
        }
    }

    static class IconRenderer extends DefaultTableCellRenderer {
        public IconRenderer() {
            super();
            setHorizontalAlignment(JLabel.CENTER);
        }

        public void setValue(Object value) {
            setIcon( (value instanceof Icon) ? (Icon) value : null);
        }
    }

    static class BooleanRenderer extends JCheckBox
        implements TableCellRenderer {
        public BooleanRenderer() {
            super();
            setHorizontalAlignment(JLabel.CENTER);
        }

        public Component getTableCellRendererComponent(JTable table,
            Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
            if (isSelected) {
                setForeground(table.getSelectionForeground());
                super.setBackground(table.getSelectionBackground());
            }
            else {
                setForeground(table.getForeground());
                setBackground(table.getBackground());
            }
            setSelected( (value != null && ( (Boolean) value).booleanValue()));
            return this;
        }
    }

    /**
     * Renders a Link type the link in the table column
     */
//    public static class LinkRenderer extends DefaultTableCellRenderer {
//
//        // Should have a way of setting these statically
//        private static Color colorLive = new Color(0, 0, 238);
//        private static Color colorVisited = new Color(82, 24, 139);
//
//        public void setValue(Object value) {
//            if (value != null && value instanceof Link) {
//                Link link = (Link) value;
//
//                setText(link.getText());
//                setToolTipText(link.getURL().toString());
//
//                if (link.getVisited()) {
//                    setForeground(colorVisited);
//                }
//                else {
//                    setForeground(colorLive);
//                }
//            }
//            else {
//                super.setValue(value != null ? value.toString() : "");
//            }
//        }
//
//        public void paintComponent(Graphics g) {
//            super.paintComponent(g);
//            if (!getText().equals("")) {
//                // Render an underline. A really smart person
//                // would actually render an underline font but
//                // that's too much for my little brain.
//                Rectangle rect = PaintUtils.getTextBounds(g, this);
//
//                FontMetrics fm = g.getFontMetrics();
//                int descent = fm.getDescent();
//
//                //REMIND(aim): should we be basing the underline on
//                //the font's baseline instead of the text bounds?
//
//                g.drawLine(rect.x, (rect.y + rect.height) - descent + 1,
//                           rect.x + rect.width,
//                           (rect.y + rect.height) - descent + 1);
//            }
//        }
//    }
}