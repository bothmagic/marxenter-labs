/*
 * $Id: JNTable.java 46 2004-09-08 17:33:01Z rbair $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */

package org.jdesktop.jdnc.incubator.rbair;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.print.PrinterException;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

import org.jdesktop.jdnc.incubator.rbair.swing.JXTable;
import org.jdesktop.jdnc.incubator.rbair.swing.decorator.FilterPipeline;
import org.jdesktop.jdnc.incubator.rbair.swing.decorator.Highlighter;
import org.jdesktop.jdnc.incubator.rbair.swing.decorator.HighlighterPipeline;
import org.jdesktop.jdnc.incubator.rbair.swing.icon.ColumnControlIcon;
import org.jdesktop.jdnc.incubator.rbair.swing.table.ColumnHeaderRenderer;
import org.jdesktop.jdnc.incubator.rbair.swing.table.TableCellRenderers;
import org.jdesktop.jdnc.incubator.rbair.swing.table.TableColumnExt;

/**
 * High level table component which displays tabular data from a data model
 * in a scrollable view.
 *
 * @author Ramesh Gupta
 * @author Amy Fowler
 * 
 * @javabean.class
 *    displayName="Table Component"
 *    name="JNTable"
 *    shortDesctiption="A simplified table component"
 *
 * @javabean.icons
 *    color16="/javax/swing/beaninfo/images/JTableColor16.gif"
 *    color32="/javax/swing/beaninfo/images/JTableColor32.gif"
 */
public class JNTable extends JNComponent {
    public	int DEFAULT_ROW_HEIGHT = 22;
    public	int DEFAULT_ROW_MARGIN = 1;
    public  int DEFAULT_VISIBLE_ROW_COUNT = 20;
    public	int DEFAULT_COLUMN_MARGIN = 0;

    private boolean hasColumnControl = false;
    private boolean	rowHeaderLocked = false;
    private JTable	rowHeaderTable = null;
    private TableColumn	firstColumn = null;

    protected final JScrollPane	scrollPane;
    protected JXTable jxtable;
    private ColumnControlButton	columnControlButton = null;

    private ColumnPropertyHighlighter highlighter;


    /**
     * Constructs a JNTable component with a DefaultTableModel instance as the
     * data model.
     */
    public JNTable() {
        this(new DefaultTableModel());
    }

    /**
     * Constructs a JNTable component that displays a row/column view for
     * the specified data model.
     *
     * @param model data model which holds the data for this table
     * @exception throws IllegalArgumentException if model is null
     */
    public JNTable(TableModel model) {
        this(new JXTable(model));
    }

    protected JNTable(JTable jxtable) {
        setTable((JXTable) jxtable);
        jxtable.getModel().addTableModelListener(new TableModelAdapter());
        setRowHeight(DEFAULT_ROW_HEIGHT);
        setRowMargin(DEFAULT_ROW_MARGIN);
        scrollPane = new JScrollPane(jxtable);
        add(scrollPane); // defaults to BorderLayout.CENTER
    }

    protected void setTable(JXTable jxtable) {
        this.jxtable = jxtable;
        setComponent(jxtable);
    }

    /**
     *
     * @return the internal JXTable instance for this component
     */
    public JXTable getTable() {
        return jxtable;
    }

    /**
     *
     * @param model data model which holds the data for this table
     */
    public void setModel(TableModel model) {
        jxtable.setModel(model);
    }

    /**
     *
     * @return data model which holds the data for this table
     */
    public TableModel getModel() {
        return jxtable.getModel();
    }

    /**
     * Adds the specified view column to this table
     * @param column TableColumnExt which holds state for the view column
     */
    public void addColumn(TableColumnExt column) {
        jxtable.addColumn(column);
    }

    public void removeColumn(TableColumnExt column) {
        jxtable.removeColumn(column);
    }

    /**
     *
     * @param name String containing the logical name of the column
     * @return TableColumnExt which holds state for the view column
     */
    public TableColumnExt getColumn(String name) {
        return (TableColumnExt)jxtable.getColumn(name);
    }

    /**
     * Returns the column which is currently located at the specified column
     * view index.  Note that since columns may be re-ordered in the view,
     * either programmatically or by the user, a column's view index may
     * change over time, therefore it is recommended to retrieve column objects
     * by name when accessing specific columns.
     * @see #getColumn
     * @param currentViewIndex integer index which contains the position of the
     *        column
     * @return TableColumnExt which holds state for the view column
     */
    public TableColumnExt getColumn(int currentViewIndex) {
        return (TableColumnExt)jxtable.getColumnModel().getColumn(currentViewIndex);
    }

    /**
     *
     * @return Color background color used to render odd rows in the table.
     */
    public Color getOddRowBackground() {
        return highlighter != null? highlighter.getOddRowBackground() : null;
    }

    /**
     * Sets the background color for odd rows in the table.
     * @param color background color used to render odd rows in the table.
     */
    public void setOddRowBackground(Color color) {
        initHighlighter();
        Color old = highlighter.getOddRowBackground();
        highlighter.setOddRowBackground(color);
        firePropertyChange("oddRowBackground", old, color);
    }

    /**
     *
     * @return Color background color used to render even rows in the table.
     */
    public Color getEvenRowBackground() {
        return highlighter != null? highlighter.getEvenRowBackground() : null;
    }

    /**
     * Sets the background color for even rows in the table.
     * @param color background color used to render even rows in the table.
     */
    public void setEvenRowBackground(Color color) {
        initHighlighter();
        Color old = highlighter.getEvenRowBackground();
        highlighter.setEvenRowBackground(color);
        firePropertyChange("evenRowBackground", old, color);
    }

    private void initHighlighter() {
        if (highlighter == null) {
            Highlighter highlighters[] = null;
            highlighter = new ColumnPropertyHighlighter();

            HighlighterPipeline pipeline = jxtable.getHighlighters();
            if (pipeline != null) {
                // insert private highlighter in beginning of pipeline
                Highlighter oldHighlighters[] = pipeline.getHighlighters();
                highlighters = new Highlighter[oldHighlighters.length+1];
                highlighters[0] = highlighter;
                for(int i = 1; i < highlighters.length; i++) {
                    highlighters[i] = oldHighlighters[i-1];
                }
            } else {
                highlighters = new Highlighter[1];
                highlighters[0] = highlighter;
            }
            jxtable.setHighlighters(new HighlighterPipeline(highlighters));
        }
    }

    /**
     *
     * @return boolean indicating whether the table provides a control which
     *         enables the end user to modify column visibility
     */
    public boolean getHasColumnControl() {
        return hasColumnControl;
    }

    /**
     *
     * @param hasControl boolean indicating whether the table provides a control which
     *         enables the end user to modify column visibility
     */
    public void setHasColumnControl(boolean hasControl) {
        boolean old = this.hasColumnControl;
        if (hasControl) {
            if (columnControlButton == null) {
                // Create cloak button
                columnControlButton = new ColumnControlButton(new ColumnControlIcon());
            }
            columnControlButton.bind((JTable) this.getTable());
            // Put button in scrollpane corner
            scrollPane.setCorner(JScrollPane.UPPER_RIGHT_CORNER, columnControlButton);
        }
        else {
            try {
                scrollPane.setCorner(JScrollPane.UPPER_RIGHT_CORNER, null);
            }
            catch (Exception ex) {
                // Ignore spurious exception thrown by JScrollPane. This is a Swing bug!
            }
        }
        hasColumnControl = hasControl;
        this.firePropertyChange("hasColumnControl", old, hasControl);
    }

    /**
     *
     */
    public boolean isRowHeaderLocked() {
        return rowHeaderLocked;
    }

    /**
     * Initialization property. Do not set after table has been shown.
     *
     * @param lockState
     */
    public void setRowHeaderLocked(boolean lockState) {
        if (rowHeaderLocked != lockState) {
            if (firstColumn == null) {
                firstColumn = initRowHeader();
            }
            if (lockState) {
                lockRowHeader();
            }
            else {
                unlockRowHeader();
            }
            rowHeaderLocked = lockState;
        }
    }

    protected TableColumn initRowHeader() {
        rowHeaderTable = new JXTable(jxtable.getModel());
        TableColumnModel	tableColumnModel = jxtable.getColumnModel();
        TableColumn			firstColumn = tableColumnModel.getColumn(0);
        //table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        rowHeaderTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        rowHeaderTable.getTableHeader().setReorderingAllowed(false);
        rowHeaderTable.getTableHeader().setResizingAllowed(false);
        rowHeaderTable.setPreferredScrollableViewportSize(
            new Dimension(firstColumn.getPreferredWidth() +
                rowHeaderTable.getColumnModel().getColumnMargin(), 0));

        TableColumnModel	headerTableColumnModel = rowHeaderTable.getColumnModel();
        for (int i = headerTableColumnModel.getColumnCount() - 1; i > 0; i--) {
            headerTableColumnModel.removeColumn(headerTableColumnModel.getColumn(i));
        }

        return firstColumn;
    }

    protected void lockRowHeader() {
        TableColumnModel	tableColumnModel = jxtable.getColumnModel();
        tableColumnModel.removeColumn(firstColumn);
        scrollPane.setRowHeaderView(rowHeaderTable);
        scrollPane.setCorner(JScrollPane.UPPER_LEFT_CORNER,
                             rowHeaderTable.getTableHeader());
        jxtable.revalidate();
    }

    protected void unlockRowHeader() {
        TableColumnModel	tableColumnModel = jxtable.getColumnModel();
        tableColumnModel.addColumn(firstColumn);
        int numColumns = tableColumnModel.getColumnCount();
        tableColumnModel.moveColumn(numColumns - 1, 0);
        scrollPane.setRowHeaderView(null);
        jxtable.revalidate();
    }

    /**
     *
     * @return integer containing the height of a table row in pixels
     */
    public int getRowHeight() {
        return jxtable.getRowHeight();
    }

    /**
     *
     * @param value integer containing the height of a table row in pixels
     */
    public void setRowHeight(int value) {
        int old = jxtable.getRowHeight();
        jxtable.setRowHeight(value);
        firePropertyChange("rowHeight", old, value);
    }

    /**
     *
     * @return integer containing the number of visible rows which should
     *         be displayed in the scrollable view when the table is first shown
     */
    public int getPreferredVisibleRowCount() {
        return jxtable.getVisibleRowCount();
    }

    /**
     * Sets the number of visible rows which should be displayed in the
     * scrollable view when the table is first shown.  If set to a value greater
     * or equal to 0, then the table will attempt to initialize the viewport size
     * to display exactly this number of rows.  Subsequent size changes to the
     * user-interface (due to end-user or programmatic resizing) may cause the
     * number of visible rows to change.
     * @param value integer containing the number of visible rows which should
     *         be displayed in the scrollable view when the table is first shown
     */
    public void setPreferredVisibleRowCount(int value) {
        int old = jxtable.getVisibleRowCount();
        jxtable.setVisibleRowCount(value);
        firePropertyChange("preferredVisibleRowCount", old, value);
    }

    public int getColumnMargin() {
        return jxtable.getColumnMargin();
    }

    public void setColumnMargin(int value) {
        jxtable.setColumnMargin(value);
    }

    public int getRowMargin() {
        return jxtable.getRowMargin();
    }

    public void setRowMargin(int value) {
        jxtable.setRowMargin(value);
    }

    public int getSelectionMode() {
        return jxtable.getSelectionMode();
    }

    public void setSelectionMode(int mode) {
        jxtable.setSelectionMode(mode);
    }

    public boolean getShowHorizontalLines() {
        return jxtable.getShowHorizontalLines();
    }

    public void setShowHorizontalLines(boolean value) {
        jxtable.setShowHorizontalLines(value);
    }

    public boolean getShowVerticalLines() {
        return jxtable.getShowVerticalLines();
    }

    public void setShowVerticalLines(boolean value) {
        jxtable.setShowVerticalLines(value);
    }

    public FilterPipeline getFilters() {
        return jxtable.getFilters();
    }

    public void setFilters(FilterPipeline pipeline) {
        jxtable.setFilters(pipeline);
    }

    public HighlighterPipeline getHighlighters() {
        return jxtable.getHighlighters();
    }

    public void setHighlighters(HighlighterPipeline pipeline) {
        /**@todo(aim) how to reconcile this with internal highligher used for
         * column-specific properties & alternate row colors? for now, this will override those.
         */
        jxtable.setHighlighters(pipeline);
    }

    public void setBackground(Color color) {
        // UI delegate installs colors/fonts in constructor before jxtable is initialized
        if (jxtable != null) {
            jxtable.setBackground(color);
        }
        super.setBackground(color);
    }

    public void setForeground(Color color) {
        // UI delegate installs colors/fonts in constructor before jxtable is initialized
        if (jxtable != null) {
            jxtable.setForeground(color);
        }
        super.setForeground(color);
    }

    public void setFont(Font font) {
        // UI delegate installs colors/fonts in constructor before jxtable is initialized
        if (jxtable != null) {
            jxtable.setFont(font);
        }
        super.setFont(font);
    }

    public void setGridColor(Color color) {
        jxtable.setGridColor(color);
    }

    /*********************************************************
     * Convenience methods for setting table header properties
     *********************************************************/

    /**
     * Sets the background of the table's column headers to the specified color.
     * If the table has no header, this method does nothing.
     * @param headerBackground Color used to render background of table column headers
     */
    public void setHeaderBackground(Color headerBackground) {
        JTableHeader header = jxtable.getTableHeader();
        if (header != null) {
            header.setBackground(headerBackground);
        }
    }

    /**
     * @return Color used to render background of table column headers or
     *         null if the table has no header.
     */
    public Color getHeaderBackground() {
        JTableHeader header = jxtable.getTableHeader();
        if (header != null) {
            return header.getBackground();
        }
        return null;
    }

    /**
     * Sets the foreground of the table's column headers to the specified color.
     * If the table has no header, this method does nothing.
     * @param headerForeground Color used to render foreground of table column headers
     */
    public void setHeaderForeground(Color headerForeground) {
        JTableHeader header = getTable().getTableHeader();
        if (header != null) {
            header.setForeground(headerForeground);
        }
    }

    /**
     * @return Color used to render foreground of table column headers or
     *         null if the table has no header.
     */
    public Color getHeaderForeground() {
        JTableHeader header = getTable().getTableHeader();
        if (header != null) {
            return header.getForeground();
        }
        return null;
    }

    /**
     * Sets the font of the table's column headers to the specified font.
     * If the table has no header, this method does nothing.
     * @param headerFont Font used to render text in the table column headers
     */
    public void setHeaderFont(Font headerFont) {
        JTableHeader header = getTable().getTableHeader();
        if (header != null) {
            header.setFont(headerFont);
        }
    }

    /**
     * @return Font used to render text in the table column headers or null
     *         if the table has no header.
     */
    public Font getHeaderFont() {
        JTableHeader header = getTable().getTableHeader();
        if (header != null) {
            return header.getFont();
        }
        return null;
    }

    private ColumnHeaderRenderer getColumnHeaderRenderer() {
        JTableHeader header = getTable().getTableHeader();
        if (header != null) {
            TableCellRenderer hr = header.getDefaultRenderer();
            if (hr instanceof ColumnHeaderRenderer) {
                return (ColumnHeaderRenderer) hr;
            }
        }
        return null;
    }

    /**
     * Sets the icon which is displayed in a column's header when it has been
     * sorted in ascending order.
     * If the table has no header, this method does nothing.
     * @param upIcon Icon which indicates ascending sort on a column
     */
    public void setHeaderSortUpIcon(Icon upIcon) {
        ColumnHeaderRenderer headerRenderer = getColumnHeaderRenderer();
        if (headerRenderer != null) {
            headerRenderer.setUpIcon(upIcon);
        }
    }

    /**
     *
     * @return Icon which indicates ascending sort on a column
     */
    public Icon getHeaderSortUpIcon() {
        ColumnHeaderRenderer headerRenderer = getColumnHeaderRenderer();
        if (headerRenderer != null) {
            return headerRenderer.getUpIcon();
        }
        return null;
    }

    /**
     * Sets the icon which is displayed in a column's header when it has been
     * sorted in descending order.
     * If the table has no header, this method does nothing.
     * @param downIcon Icon which indicates descending sort on a column
     */
    public void setHeaderSortDownIcon(Icon downIcon) {
        ColumnHeaderRenderer headerRenderer = getColumnHeaderRenderer();
        if (headerRenderer != null) {
            headerRenderer.setDownIcon(downIcon);
        }
    }

    /**
     *
     * @return Icon which indicates descending sort on a column
     */
    public Icon getHeaderSortDownIcon() {
        ColumnHeaderRenderer headerRenderer = getColumnHeaderRenderer();
        if (headerRenderer != null) {
            return headerRenderer.getDownIcon();
        }
        return null;
    }

    /*********************************************************
     * Convenience methods for setting table column properties
     *********************************************************/

    /**
     * Convenience method for obtaining the class of a named column in the table.
     * This method will obtain the class from the underlying data model.
     * @param columnName String containing the logical name of the column
     * @return Class representing the data-type for values in the column
     */
    public Class getColumnClass(String columnName) {
        return jxtable.getColumnClass(jxtable.getColumnModel().
                                     getColumnIndex(columnName));
    }

    /**
     * Sets the background color for all cells in the specified named column.
     * By default, cells are rendered in the background color of the table component
     * or using the alternate row color properties (if set),
     * so this method should only be used if a column needs to have an individual
     * background color.
     * @param columnName String containing the logical name of the column
     * @param background Color used to render the background of cells in the column
     */
    public void setColumnBackground(String columnName, Color background) {
        initHighlighter();
        highlighter.setColumnBackground(columnName, background);
    }

    /**
     * @param columnName String containing the logical name of the column
     * @return Color used to render the background of cells in the column or
     *         null if no background color was set on the column
     */
    public Color getColumnBackground(String columnName) {
        return highlighter != null? highlighter.getColumnBackground(columnName) : null;
    }

    /**
     * Sets the foreground color for all cells in the specified named column.
     * By default, cells are rendered in the foreground color of the table component
     * so this method should only be used if a column needs to have an individual
     * foreground color.  Setting the column foreground to <code>null</code>
     * will cause the column to be rendered with the default (table foreground).
     * @param columnName String containing the logical name of the column
     * @param background Color used to render the foreground of cells in the column
     */
    public void setColumnForeground(String columnName, Color foreground) {
        initHighlighter();
        highlighter.setColumnForeground(columnName, foreground);
    }

    /**
     * @param columnName String containing the logical name of the column
     * @return Color used to render the foreground of cells in the column or
     *         null if no foreground color was set on the column
     */
    public Color getColumnForeground(String columnName) {
        return highlighter != null? highlighter.getColumnForeground(columnName) : null;
    }

    /**
     * Sets the font for all text rendering of cells in the specified named column.
     * By default, cells are rendered in the font of the table component
     * so this method should only be used if a column needs to be rendered in
     * an individual font.
     * @param columnName String containing the logical name of the column
     * @param font Font used to render text inside cells in the column
     */
    public void setColumnFont(String columnName, Font font) {
        initHighlighter();
        highlighter.setColumnFont(columnName, font);
    }

    /**
     * @param columnName String containing the logical name of the column
     * @return Font used to render text within the cells in the column or
     *         null if no font was set on the column
     */
    public Font getColumnFont(String columnName) {
        return highlighter != null? highlighter.getColumnFont(columnName) : null;
    }

    /**
     * Sets the horizontal alignment of content rendered inside cells in the
     * specified named column.
     * By default, cells are rendered with a horizontal alignment which is
     * determined by the column's type (e.g. strings are leading-aligned,
     * numbers are trailing-aligned, dates are center-aligned, etc).
     * This method should only be used if a column needs to override the
     * default alignment for the column's type.  Setting the column alignment
     * to -1 will restore the alignment to its default setting.
     * @see javax.swing.JLabel#setHorizontalAlignment
     * @param columnName String containing the logical name of the column
     * @param alignment integer representing the horizontal alignment within the column
     */
    public void setColumnHorizontalAlignment(String columnName, int alignment) {
        // Unfortunately we cannot use highlighters to set column alignment on
        // the shared table renderers because there is no way to reset the alignment
        // property after rendering (whereas background, foreground, and font are
        // almost always re-initialized for each cell rendering op).  Thus,
        // to customize alignment safely, we need to install a dedicated column
        // renderer so the alignment setting does not disturb other columns which
        // use the shared renderers.

        TableColumnExt column = getColumn(columnName);
        TableCellRenderer renderer = column.getCellRenderer();
        if (renderer == null) {
            renderer = TableCellRenderers.getNewDefaultRenderer(getColumnClass(
                columnName));
            column.setCellRenderer(renderer);
            if (renderer instanceof DefaultTableCellRenderer) {
                column.putClientProperty("saveAlignment",
                       new Integer(((DefaultTableCellRenderer)renderer).getHorizontalAlignment()));
            }
        }
        column.putClientProperty("alignment", new Integer(alignment));
        if (renderer != null && renderer instanceof DefaultTableCellRenderer) {
            if (alignment != -1) {
                ((DefaultTableCellRenderer)renderer).setHorizontalAlignment(alignment);
            } else {
                // restore default alignment
                Integer restoreAlignment = (Integer)column.getClientProperty("saveAlignment");
                if (restoreAlignment != null) {
                    ((DefaultTableCellRenderer)renderer).setHorizontalAlignment(
                        restoreAlignment.intValue());
                }
            }
        }
    }

    /**
     * @param columnName String containing the logical name of the column
     * @return integer representing the horizontal alignment within the column
     *         or -1 if column alignment was not explicitly set
     */
    public int getColumnHorizontalAlignment(String columnName) {
        TableColumnExt column = getColumn(columnName);
        Integer alignment = (Integer)column.getClientProperty("alignment");
        return alignment != null? alignment.intValue() : -1;
    }

    /**
     * Sets the prototype value for the specified named column.
     * The prototype value is used to compute the preferred width of the column
     * when the table's size if first initialized, therefore this property
     * should be set before the table is shown.
     * The value should be an instance of the column's type.
     * Setting the prototype value to <code>null</code> will cause the the
     * preferred width to fallback to a static pixel width.
     * @see #getColumnClass
     * @param columnName String containing the logical name of the column
     * @param prototype Object used to calculate preferred width of column
     */
    public void setColumnPrototypeValue(String columnName, Object prototype) {
        TableColumnExt column = getColumn(columnName);
        column.setPrototypeValue(prototype);
    }

    /**
     * @param columnName String containing the logical name of the column
     * @return Object used to calculate preferred width of column or null
     *         if the preferred width should be based on a static pixel value
     */
    public Object getColumnPrototypeValue(String columnName) {
        TableColumnExt column = getColumn(columnName);
        return column.getPrototypeValue();
    }

    public boolean print() throws PrinterException {
        boolean printed = getTable().print();
        sendMessage("Print " + (printed? "complete." : "cancelled."));
        return printed;
    }

    /*

    public int search(String searchString) {
        return table.search(searchString);
    }

    public int search(String searchString, int columnIndex) {
        return table.search(searchString, columnIndex);
    }

    public int search(Pattern pattern) {
        return table.search(pattern);
    }

    public int search(Pattern pattern, int columnIndex) {
        return table.search(pattern, columnIndex);
    }

    public int search(Pattern pattern, int columnIndex, boolean backward) {
        return table.search(pattern, columnIndex, backward);
    }
    */

    private class TableModelAdapter implements TableModelListener {

        public void tableChanged(TableModelEvent e) {
            if (e.getFirstRow() == TableModelEvent.HEADER_ROW) {
                if (columnControlButton != null) {
                    columnControlButton.bind(getTable());
                }
            }
            // Update the status message with the number of rows/columns.
            setRowColumnStatus();
        }
    }

    private void setRowColumnStatus() {
        TableColumnModel columnModel = jxtable.getColumnModel();
        TableModel model = jxtable.getModel();

        if (support != null) {
            support.fireMessage(model.getRowCount() + " rows, " +
                                columnModel.getColumnCount() + " columns");
        }
    }

    public String toString() {
        return super.toString() + "; " + getTable().toString();
    }

    public static class ColumnControlButton extends JButton {
        public final static String	TITLE = "x";

        public ColumnControlButton() {
            super(TITLE);
            init();
        }
        public ColumnControlButton(Action action) {
            super(action);
            init();
        }
        public ColumnControlButton(Icon icon) {
            super(icon);
            init();
        }
        public ColumnControlButton(String title) {
            super(title);
            init();
        }
        public ColumnControlButton(String title, Icon icon) {
            super(title, icon);
            init();
        }

        private void init() {
            //setBackground(java.awt.Color.orange);
            //setBorderPainted(false);
            setFocusPainted(false);
            setMargin(new Insets(1,2,2,1));	// Make this LAF-independent
        }

        public void bind(JTable table) {
            this.table = table;
            copyColumnModel();
            createPopupMenu();	// Create cloak popup
            // Hook popup to button (default button action is to show popup)
            addMouseListener(new MouseAdapter() {
                public void mousePressed(MouseEvent ev) {
                    if (popupMenu.getComponentCount() > 0) {
                        JButton button = ColumnControlButton.this;
                        Dimension buttonSize = button.getSize();
                        popupMenu.show(button,
                                       buttonSize.width -
                                       popupMenu.getPreferredSize().width,
                                       buttonSize.height);
                    }
                }
            });
        }

        private void copyColumnModel() {
            TableColumnModel	originalColumnModel = table.getColumnModel();
            columnModelCopy = new DefaultTableColumnModel();
            int count = originalColumnModel.getColumnCount();
            for (int i = 0; i < count; i++) {
                columnModelCopy.addColumn(originalColumnModel.getColumn(i));
            }
        }

        protected void createPopupMenu() {
            if (popupMenu == null) {
                popupMenu = new JPopupMenu();
            }
            populatePopupMenu();	// must populate initially
            /*
            popupMenu.addPopupMenuListener(new PopupMenuListener() {
                public void popupMenuCanceled(PopupMenuEvent ev) {
                    //    JPopupMenu	popupMenu = (JPopupMenu) ev.getSource();
                }
                public void popupMenuWillBecomeVisible(PopupMenuEvent ev) {
                    //    JPopupMenu	popupMenu = (JPopupMenu) ev.getSource();
                }
                public void popupMenuWillBecomeInvisible(PopupMenuEvent ev) {
                    //    JPopupMenu	popupMenu = (JPopupMenu) ev.getSource();
                }
            });
            */
        }

        protected void populatePopupMenu() {
            popupMenu.removeAll();
            // For each column in the view, add a JCheckBoxMenuItem to popup
            int count = columnModelCopy.getColumnCount();
            for (int i = 0; i < count; i++) {
                TableColumn			column = columnModelCopy.getColumn(i);
                // Create a new JCheckBoxMenuItem
                JCheckBoxMenuItem	item = new JCheckBoxMenuItem(
                    column.getHeaderValue().toString(), true);
                item.putClientProperty("column", column);
                // Attach column visibility action to each menu item
                item.addActionListener(columnVisibilityAction);
                popupMenu.add(item);	// Add item to popup menu
            }
        }

        private JTable				table;
        private TableColumnModel	columnModelCopy;
        private JPopupMenu			popupMenu = null;
        private AbstractAction		columnVisibilityAction = new AbstractAction() {
            public void actionPerformed(ActionEvent ev) {
                JCheckBoxMenuItem	item = (JCheckBoxMenuItem) ev.getSource();
                TableColumn			column = (TableColumn) item.getClientProperty("column");
                if (item.isSelected()) {	// was not selected, but is now...
                    table.addColumn(column);
                    /** @todo Figure out how to restore column index */
                }
                else {
                    /** @todo DON'T REMOVE LAST ONE! */
                    table.removeColumn(column);
                }
            }
        };
    }
}
