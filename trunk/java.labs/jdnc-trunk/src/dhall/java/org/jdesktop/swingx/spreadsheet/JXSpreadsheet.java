// ============================================================================
// $Id: JXSpreadsheet.java 1473 2007-07-04 02:29:01Z david_hall $
// Copyright (c) 2007  David A. Hall
// ============================================================================

package org.jdesktop.swingx.spreadsheet;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Locale;
import javax.script.ScriptEngineManager;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.UIManager;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.decorator.AlternateRowHighlighter;
import org.jdesktop.swingx.decorator.Highlighter;
import org.jdesktop.swingx.decorator.HighlighterPipeline;
import org.jdesktop.swingx.table.ColumnControlButton;
import java.awt.event.MouseEvent;

/**
 * Table that contains a sparse-matrix of cells, each of which holds a script written in
 * a JSR-223 compliant scripting language.
 * <p>
 * Copyright &copy; 2007  David A. Hall
 */

public class JXSpreadsheet extends JComponent {

    // The components used to display the sheet
    private JXTable _table;
    private JScrollPane _pane;
    
    // a pre-cast reference to the model
    private SpreadsheetTableModel _model;

    // Script engine manager used to locate scripting engines, and to
    // set up bindings that expose the Spreadsheet API to the cells'
    // language(s)
    private ScriptEngineManager _manager = new ScriptEngineManager();
    
    // Component used as a row header for the table
    private RowHeader _rowHeader;

    public JXSpreadsheet() {
        this(64,16);
    }
    
    public JXSpreadsheet (int rows, int cols){
        _model = new SpreadsheetTableModel(rows, cols);
        
        _table = new SpreadsheetTable(_model);
        _table.setColumnFactory(new SpreadsheetColumnFactory());
        _table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        _table.setCellSelectionEnabled(true);
        _table.setCellEditor(new Cell.Editor());
        _table.setSortable(false);
        _table.getTableHeader().setReorderingAllowed(false);
        
        _pane = new JScrollPane(_table);
        
        _rowHeader = new RowHeader(_table);

        super.setLayout(new BorderLayout());
        super.add(_pane, BorderLayout.CENTER);
        
        _model.setScriptEngine(_manager.getEngineByName("rhino"));
        _manager.put("sheet", this);
        initSpreadSheetActions();
    }
    
    public static final String ADDCOLUMN_ACTION_COMMAND = 
        ColumnControlButton.COLUMN_CONTROL_MARKER + "addColumn";

    private void initSpreadSheetActions() {
        Action addColumn = new AbstractAction("Add Column") {

            public void actionPerformed(ActionEvent e) {
                _model.setColumnCount(_model.getColumnCount() + 1);
            }
        };
        
        getActionMap().put(ADDCOLUMN_ACTION_COMMAND, addColumn);
    }

    // - - - - - - - - - -
    // Spreadsheet Config
    // - - - - - - - - - -

    public void setColumnCount(int width) {
        int oldWidth = _table.getColumnCount();
        if (width > 0 && width != oldWidth) {
            _model.setColumnCount(width);
        }
    }
    
    public void setRowCount(int height) {
        if (height > 0 && height != _table.getRowCount()) {
            _model.setRowCount(height);
            _rowHeader.setRowCount(height);    
        }
    }

    /**
     * Sets the option that determines if empty cells and newly created cells
     * are editable.
     */
    public void setEditableByDefault(boolean b) {
        _model.setEditableByDefault(b);
    }
    
    /**
     * Returns true if empty and newly created cells are editable.
     */
    public boolean isEditableByDefault() {
        return _model.isEditableByDefault();
    }

    // - - - - - - - - - -
    // GUI methods
    // - - - - - - - - - -

    /**
     * Returns the component used as a row header
     */

    public JComponent getRowHeader() {
        return _rowHeader;
    }

    // - - - - - - - - - -
    // Cell access methods
    // - - - - - - - - - -

    /**
     * Discards all information about the given cell.
     */
    public void clearCell(int row, int col) {
        Cell cell = getCellIfPresent(row,col);
        if (cell != null) {
            cell.clear();
        }
    }
    
    /**
     * Returns the cell at the given address, creating one if one does not already
     * exist.
     * @throws IndexOutOfBoundsException if the row or column is out of bounds
     */
    Cell getCell(int row, int col) {
        return _model.getOrCreateCell(row, 
                _table.convertColumnIndexToModel(col));
    }

    /**
     * Returns the cell at the given address if one exists, or null if it does not
     * @throws IndexOutOfBoundsException if the row or column is out of bounds
     */
    Cell getCellIfPresent(int row, int col) {
        return _model.getCell(row,
                _table.convertColumnIndexToModel(col));
    }

    /**
     * Returns a (possibly null) cell whose name is given
     */
    Cell getCellByName(String name) {
        return _model.getCellByName(name);
    }

    /**
     * Returns the current value of the given cell.  The value may be the result of executing
     * a script in the cell, or it may have been directly set in the cell.
     */
    public Object getCellValue(int row, int col) {
        return _model.getValueAt(row, col);
    }

    /**
     * Builds a  cell initialized to the given value. If the value is not serializable, then
     * it will be discarded when the spreadsheet is saved or serialized.
     * @throws IndexOutOfBoundsException if the row or column is out of bounds
     */
    public Cell setCellValue(Object value, int row, int col) {
        return setCellValue(value, row, col, _model.isEditableByDefault());
    }

    /**
     * Builds a possibly editable cell initialized to the given value.  If the value is not
     * serializable, then it will be discarded when the spreadsheet is saved or serialized.
     * @throws IndexOutOfBoundsException if the row or column is out of bounds
     */
    public Cell setCellValue(Object value, int row, int col, boolean editable) {
        return _model.setCellByValue(value, 
                _table.convertRowIndexToModel(row), 
                _table.convertColumnIndexToModel(col),
                editable);
    }

    /**
     * Builds a possibly editable cell to hold the given script.
     * @throws IndexOutOfBoundsException if the row or column is out of bounds
     */
    public Cell setCellScript(String script, int row, int col) {
        return setCellScript(script, row, col,
                isEditableByDefault());
    }

    /**
     * Builds a possibly editable cell to hold the given script.
     * @throws IndexOutOfBoundsException if the row or column is out of bounds
     */
    public Cell setCellScript(String script, int row, int col, boolean editable) {
        return _model.setCellByScript(script,
                _table.convertRowIndexToModel(row), 
                _table.convertColumnIndexToModel(col),
                editable);
    }

    /**
     * Sets the name of the given cell.
     * @throws IllegalArgumentException if the name is already in use.
     */
    public Cell setCellName(String name, int row, int col) {
        Cell cell = _model.setCellName(name,
                _table.convertRowIndexToModel(row), 
                _table.convertColumnIndexToModel(col));
        return cell;
    }

    // From JTable

    /**
     * {@link JXTable.getValueAt(int,int)} 
     */
    public Object getValueAt(int row, int col) {
        return getCellValue(row, col);
    }

    /**
     * {@link JTable.setValueAt(Object,int,int)}
     */
    public void setValueAt(Object value, int row, int col) {
        setCellValue(value, row, col);
    }

    /**
     * {@link JTable.isCellEditable(int,int)}
     */
    public boolean isCellEditable(int row, int col) {
        return _model.isCellEditable(_table.convertRowIndexToModel(row), 
                                     _table.convertColumnIndexToModel(col));
    }
    
//     public Rectangle getCellRect(int, int, boolean);

    // - - - - - - - - - - - - - -
    // other model wrapper methods
    // - - - - - - - - - - - - - -

    /**
     * Erases the contents of the spreadsheet
     */
    public void clear() {
        _model.clear();
        setRowSelectionInterval(0,0);
        setColumnSelectionInterval(0,0);
    }

    // ------------------------
    // JXTable interface
    // ------------------------

    /**
     * {@link JXTable.setRolloverEnabled(boolean)}
     */
    public void setRolloverEnabled(boolean enabled) {
        _table.setRolloverEnabled(enabled);
    }

    /**
     * {@link JXTable.isRolloverEnabled()}
     */
    public boolean isRolloverEnabled() {
        return _table.isRolloverEnabled();
    }

    /**
     * {@link JXTable.isColumnControlVisible()}
     */
    public boolean isColumnControlVisible() {
        return _table.isColumnControlVisible();
    }

    /**
     * {@link JXTable.setColumnContorlVisible(boolean)}
     */
    public void setColumnControlVisible(boolean showColumnControl) {
        _table.setColumnControlVisible(showColumnControl);
    }

    /**
     * {@link JXTable.getColumnControl()}
     */
    public JComponent getColumnControl() {
        return _table.getColumnControl();
    }

    /**
     * {@link JXTable.setColumnControl(JComponent)}
     */
    public void setColumnControl(JComponent comp) {
        _table.setColumnControl(comp);
    }

    /**
     * {@link JXTable.setComponentOrientation(ComponentOrientation)}
     */
    public void setComponentOrientation(ComponentOrientation orientation) {
        _table.setComponentOrientation(orientation);
    }

    /**
     * {@link JXTable.setLocale(Locale)}
     */
    public void setLocale(Locale locale) {
        _table.setLocale(locale);
    }

    /**
     * {@link JXTable.getRowCount()}
     */
    public int getRowCount(){
        return _table.getRowCount();
    }

    // - - - - - - - - - -
    // Sorting/Filtering
    // - - - - - - - - - -

//*    public FilterPipeline getFilters();
//*    public void setFilters(FilterPipeline);
//*    public void setSortable(boolean);
//*    public boolean isSortable();
//*    public void resetSortOrder();
//*    public void toggleSortOrder(int);
//*    public void setSortOrder(int, SortOrder);
//*    public SortOrder getSortOrder(int);
//*    public void toggleSortOrder(Object);
//*    public void setSortOrder(Object, SortOrder);
//*    public SortOrder getSortOrder(Object);
//*    public TableColumn getSortedColumn();
    
    // - - - - - - - - - -
    // Searching
    // - - - - - - - - - -

//*    public Searchable getSearchable();
//*    public void setSearchable(Searchable);

    // - - - - - - - - - -
    // Highlighter Support
    // - - - - - - - - - -

    /**
     * {@link JXTable.getHighlighters()}
     */
    public HighlighterPipeline getHighlighters() {
        return _table.getHighlighters();
    }

    /**
     * {@link JXTable.setHighlighters(HighlighterPipeline)}
     */
    public void setHighlighters(HighlighterPipeline highlighters) {
        _table.setHighlighters(highlighters);
    }

    /**
     * {@link JXTable.setHighlighters(Highlighter[])}
     */
    public void setHighlighters(Highlighter[] highlighters) {
        _table.setHighlighters(highlighters);
    }

    /**
     * {@link JXTable.addHighlighter(Highlighter)}
     */
    public void addHighlighter(Highlighter highlighter) {
        _table.addHighlighter(highlighter);
    }

    /**
     * {@link JXTable.removeHighlighter(Highlighter)}
     */
    public void removeHighlighter(Highlighter highlighter) {
        _table.removeHighlighter(highlighter);
    }
    
    // ------------------------
    // JTable interface
    // ------------------------

    /**
     * {@link JTable.setIntercellSpacing(Dimension)}
     */
    public void setIntercellSpacing(Dimension dim) {
        _table.setIntercellSpacing(dim);
    }

    /**
     * {@link JTable.getIntercellSpacing()}
     */
    public Dimension getIntercellSpacing() {
        return _table.getIntercellSpacing();
    }

    /**
     * {@link JTable.setGridColor(Color)}
     */
    public void setGridColor(Color color) {
        _table.setGridColor(color);
    }

    /**
     * {@link JTable.getGridColor()}
     */
    public Color getGridColor() {
        return _table.getGridColor();
    }

    /**
     * {@link JTable.setShowGrid(boolean)}
     */
    public void setShowGrid(boolean b) {
        _table.setShowGrid(b);
    }

    /**
     * {@link JTable.setShowHorizontalLines(boolean)}
     */
    public void setShowHorizontalLines(boolean b) {
        _table.setShowHorizontalLines(b);
    }

    /**
     * {@link JTable.setShowVerticalLines(boolean)}
     */
    public void setShowVerticalLines(boolean b) {
        _table.setShowVerticalLines(b);
    }

    /**
     * {@link JTable.getShowHorizontalLines()}
     */
    public boolean getShowHorizontalLines() {
        return _table.getShowHorizontalLines();
    }

    /**
     * {@link JTable.getShowVerticalLines()}
     */
    public boolean getShowVerticalLines() {
        return _table.getShowVerticalLines();
    }

    /**
     * {@link JTable.getToolTipText()}
     */
    public String getToolTipText(MouseEvent e) {
        return _table.getToolTipText(e);
    }

    // - - - - - - - - -
    // Selection Support
    // - - - - - - - - -

    public void setSelectionModel(ListSelectionModel model){
        _table.setSelectionModel(model);
    }
    
    public ListSelectionModel getSelectionModel() {
        return _table.getSelectionModel();
    }
    
    public void setSelectionMode(int mode) {
        _table.setSelectionMode(mode);
    }
    
    public void setRowSelectionAllowed(boolean allowed) {
        _table.setRowSelectionAllowed(allowed);
    }
    
    public boolean getRowSelectionAllowed() {
        return _table.getRowSelectionAllowed();
    }
    
    public void setColumnSelectionAllowed(boolean allowed) {
        _table.setColumnSelectionAllowed(allowed);
    }
    
    public boolean getColumnSelectionAllowed() {
        return _table.getColumnSelectionAllowed();
    }
    
    public void setCellSelectionEnabled(boolean enabled) {
        _table.setCellSelectionEnabled(enabled);
    }
    
    public boolean getCellSelectionEnabled() {
        return _table.getCellSelectionEnabled();
    }
    
    public void selectAll() {
        _table.selectAll();
    }
    
    public void clearSelection() {
        _table.clearSelection();
    }

    public void setRowSelectionInterval(int from, int to){
        _table.setRowSelectionInterval(from, to);
    }
    
    public void setColumnSelectionInterval(int from, int to){
        _table.setColumnSelectionInterval(from, to);
    }
    
//     public void addRowSelectionInterval(int, int);
//     public void addColumnSelectionInterval(int, int);
//     public void removeRowSelectionInterval(int, int);
//     public void removeColumnSelectionInterval(int, int);
//     public int getSelectedRow();
//     public int getSelectedColumn();
//     public int[] getSelectedRows();
//     public int[] getSelectedColumns();
//     public int getSelectedRowCount();
//     public int getSelectedColumnCount();
//     public boolean isRowSelected(int);
//     public boolean isColumnSelected(int);
//     public boolean isCellSelected(int, int);
//     public void changeSelection(int, int, boolean, boolean);
//*    public int getSelectionMode();
//*    public SelectionMapper getSelectionMapper();
    
    public Color getSelectionForeground() {
        return _table.getSelectionForeground();
    }
    
    public void setSelectionForeground(Color color) {
        _table.setSelectionForeground(color);
    }
    
    public Color getSelectionBackground() {
        return _table.getSelectionBackground();
    }
    
    public void setSelectionBackground(Color color) {
        _table.setSelectionBackground(color);
    }
    
    // - - - - - - - - -
    // Row Handling
    // - - - - - - - - -
    
    /**
     * {@link JTable.setRowHeight(int)}
     */
    public void setRowHeight(int ht) {
        _table.setRowHeight(ht);
    }
    
    /**
     * {@link JTable.getRowHeight()}
     */
    public int getRowHeight() {
        return _table.getRowHeight();
    }

    /**
     * {@link JTable.setRowHeight(int,int)}
     */
    public void setRowHeight(int row, int ht) {
        _table.setRowHeight(row, ht);
    }

    /**
     * {@link JTable.getRowHeight(int)}
     */
    public int getRowHeight(int row) {
        return _table.getRowHeight(row);
    }

    /**
     * {@link JXTable.setRowMargin(int)}
     */
    public void setRowMargin(int margin) {
        _table.setRowMargin(margin);
    }

    /**
     * {@link JXTable.getRowMargin()}
     */
    public int getRowMargin() {
        return _table.getRowMargin();
    }
    
//*    public int convertRowIndexToModel(int);
//*    public int convertRowIndexToView(int);
//*    public void setRowHeightEnabled(boolean);
//*    public boolean isRowHeightEnabled();
//*    public int rowAtPoint(Point);

    // - - - - - - - - -
    // Column Handling
    // - - - - - - - - -
    
//     public void setAutoCreateColumnsFromModel(boolean);
//     public boolean getAutoCreateColumnsFromModel();
//*    public void createDefaultColumnsFromModel();
//     public TableColumn getColumn(Object);
//     public int convertColumnIndexToModel(int);
//     public int convertColumnIndexToView(int);
//     public String getColumnName(int);
//     public Class getColumnClass(int);
    
//     public void addColumn(TableColumn);
//     public void removeColumn(TableColumn);
//     public void moveColumn(int, int);
//     public int columnAtPoint(Point);
//     public void sizeColumnsToFit(boolean);
//     public void sizeColumnsToFit(int);
    
//*    public TableColumn getColumn(int);
//*    public List getColumns();
//*    public List getColumns(boolean);
//*    public TableColumnExt getColumnExt(Object);
//*    public TableColumnExt getColumnExt(int);
//*    public void setColumnSequence(Object[]);
//*    public ColumnFactory getColumnFactory();
//*    public void setColumnFactory(ColumnFactory);

    /**
     * {@link JXTable.getColumnMargin()}
     */
    public int getColumnMargin() {
        return _table.getColumnMargin();
    }

    /**
     * {@link JXTable.setColumnMargin(int)}
     */
    public void setColumnMargin(int margin) {
        _table.setColumnMargin(margin);
    }

    /**
     * {@link JXTable.getColumnCount(boolean)}
     */
    public int getColumnCount(boolean includeHidden) {
        return _table.getColumnCount(includeHidden);
    }
    
    // - - - - - - - - -
    // L&F Configuration
    // - - - - - - - - -
    
//     public TableUI getUI();
//     public void setUI(TableUI);
//*    public void updateUI();
//     public String getUIClassID();
//     public void addNotify();
//     public void removeNotify();
    

//*    public void doLayout();
//*    public void packAll();
//*    public void packSelected();
//*    public void packTable(int);
//*    public void packColumn(int, int);
//*    public void packColumn(int, int, int);
    
    // - - - - - - - - - 
    // Table Config
    // - - - - - - - - -
    
//*    public void setModel(TableModel);
//     public TableModel getModel();
//     public void setColumnModel(TableColumnModel);
//     public TableColumnModel getColumnModel();
//     public void setTableHeader(JTableHeader);
//     public JTableHeader getTableHeader();
//*    public void setAutoResizeMode(int);
//     public int getAutoResizeMode();
//     public void setDragEnabled(boolean);
//     public boolean getDragEnabled();
//*    public void setDefaultMargins(boolean, boolean);
    
    // - - - - - - - - -
    // Event handlers
    // - - - - - - - - -

//*    public void tableChanged(TableModelEvent);
//     public void columnAdded(TableColumnModelEvent);
//*    public void columnRemoved(TableColumnModelEvent);
//     public void columnMoved(TableColumnModelEvent);
//     public void columnMarginChanged(ChangeEvent);
//*    public void columnSelectionChanged(ListSelectionEvent);
//     public void valueChanged(ListSelectionEvent);
//*    public void columnMarginChanged(ChangeEvent);
//*    public void columnPropertyChange(PropertyChangeEvent);
    
    // - - - - - - - - -
    // Scrolling
    // - - - - - - - - -

//     public static JScrollPane createScrollPaneForTable(JTable);
//     public void setPreferredScrollableViewportSize(Dimension);
//*    public Dimension getPreferredScrollableViewportSize();
//     public int getScrollableUnitIncrement(Rectangle, int, int);
//     public int getScrollableBlockIncrement(Rectangle, int, int);
//*    public boolean getScrollableTracksViewportWidth();
//*    public boolean getScrollableTracksViewportHeight();
//*    public void setHorizontalScrollEnabled(boolean);
//*    public boolean isHorizontalScrollEnabled();
//*    public void setFillsViewportHeight(boolean);
//*    public boolean getFillsViewportHeight();
//*    public void scrollRowToVisible(int);
//*    public void scrollColumnToVisible(int);
//*    public void scrollCellToVisible(int, int);
    
//*    public int getVisibleRowCount();
//*    public void setVisibleRowCount(int);

    // - - - - - - - - -
    // Cell rendering
    // - - - - - - - - -
    
    private TableCellRenderer _defaultRenderer = new Cell.Renderer();
    
    public TableCellRenderer getCellRenderer(int row, int col) {
        Cell cell = getCellIfPresent(row,col);
        if (cell == null)
            return _defaultRenderer;
        
        TableCellRenderer renderer = cell.getRenderer();
        return (renderer != null) ? renderer : _defaultRenderer; 
    }
    
//     public void setDefaultRenderer(Class, TableCellRenderer);
//     public TableCellRenderer getDefaultRenderer(Class);
//*    public Component prepareRenderer(TableCellRenderer, int, int);
//*    public TableCellRenderer getNewDefaultRenderer(Class);

    // - - - - - - - - -
    // Cell Editing
    // - - - - - - - - -
    
//     public void setDefaultEditor(Class, TableCellEditor);
//     public TableCellEditor getDefaultEditor(Class);
//*    public Component prepareEditor(TableCellEditor, int, int);
//     public void setSurrendersFocusOnKeystroke(boolean);
//     public boolean getSurrendersFocusOnKeystroke();
//     public boolean editCellAt(int, int);
//     public boolean editCellAt(int, int, EventObject);
//     public boolean isEditing();
//     public Component getEditorComponent();
//     public int getEditingColumn();
//     public int getEditingRow();

//     public void editingStopped(ChangeEvent);
//     public void editingCanceled(ChangeEvent);
//     public TableCellEditor getCellEditor();
//     public void setCellEditor(TableCellEditor);
//     public void setEditingColumn(int);
//     public void setEditingRow(int);
    
    public TableCellEditor getCellEditor(int row, int col) {
        Cell cell = getCellIfPresent(row,col);
        if (cell == null) {
            return _table.getCellEditor(row,col);
        }
                
        TableCellEditor editor = cell.getEditor();
        return (editor != null) ? editor : _table.getCellEditor(row,col);
    }
    
//     public void removeEditor();
//*    public boolean isEditable();
//*    public void setEditable(boolean);
//*    public boolean isTerminateEditOnFocusLost();
//*    public void setTerminateEditOnFocusLost(boolean);
//*    public boolean isAutoStartEditOnKeyStroke();
//*    public void setAutoStartEditOnKeyStroke(boolean);

    // - - - - - - - - - 
    // Printing Support
    // - - - - - - - - -
    
//     public boolean print() throws PrinterException;
//     public boolean print(JTable$PrintMode) throws PrinterException;
//     public boolean print(JTable$PrintMode, MessageFormat, MessageFormat) throws PrinterException;
//     public boolean print(JTable$PrintMode, MessageFormat, MessageFormat, boolean, PrintRequestAttributeSet, boolean) throws PrinterException, HeadlessException;
//     public Printable getPrintable(JTable$PrintMode, MessageFormat, MessageFormat);
//     public javax.accessibility.AccessibleContext getAccessibleContext();

    // ------------------------
    // JScrollPane interface
    // ------------------------
    
    /**
     * see {@link JScrollPane#getVerticalScrollBarPolicy}
     */
    public int getVerticalScrollBarPolicy() {
        return _pane.getVerticalScrollBarPolicy();
    }
    
    /**
     * see {@link JScrollPane#setVerticalScrollBarPolicy}
     */
    public void setVerticalScrollBarPolicy(int policy) {
        _pane.setVerticalScrollBarPolicy(policy);
    }

    /**
     * see {@link JScrollPane#getHorizontalScrollBarPolicy}
     */
    public int getHorizontalScrollBarPolicy() {
        return _pane.getHorizontalScrollBarPolicy();
    }
    
    /**
     * see {@link JScrollPane#setHorizontalScrollBarPolicy}
     */
    public void setHorizontalScrollBarPolicy(int policy) {
        _pane.setHorizontalScrollBarPolicy(policy);
    }

    /**
     * see {@link JScrollPane#isWheelScrollingEnabled}
     */
    public boolean isWheelScrollingEnabled() {
        return _pane.isWheelScrollingEnabled();
    }

    /**
     * see {@link JScrollPane#setWheelScrollingEnabled}
     */
    public void setWheelScrollingEnabled(boolean handleWheel) {
        _pane.setWheelScrollingEnabled(handleWheel);
    }
    
    // ------------------------
    // Modified JXTable class
    // ------------------------

    class SpreadsheetTable extends JXTable {
        public SpreadsheetTable(SpreadsheetTableModel model) {
            super(model);
        }

        public JXSpreadsheet getSpreadsheet() {
            return JXSpreadsheet.this;
        }
        
        /**
         * In addition to the JTable behaviour of this method (which takes care to add the
         * table's columnheader to an enclosing scrollpane's viewport), add a rowheader to
         * the enclosing scrollpane's viewport as well.
         */
        protected void configureEnclosingScrollPane() {
            super.configureEnclosingScrollPane();
            
            // The base class logic is reproduced: we only want to set the row header under
            // the same conditions as those in which the column header will be set
            Container p = getParent();
            if (p instanceof JViewport) {
                Container gp = p.getParent();
                if (gp instanceof JScrollPane) {
                    JScrollPane scrollPane = (JScrollPane)gp;
                    JViewport viewport = scrollPane.getViewport();
                    if (viewport == null || viewport.getView() != this) {
                        return;
                    }
                    
                    scrollPane.setRowHeaderView(getRowHeader());
                }
            }
        }
    }

    
    // ------------------------
    // Standalone entry point
    // ------------------------

    static public void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        }
        catch (Exception x) {
            System.err.println("Error loading L&F:" +x);
        }
        
        final JXSpreadsheet sheet = new JXSpreadsheet(10,5);
        sheet.addHighlighter(AlternateRowHighlighter.quickSilver);
        sheet.addHighlighter(new CellErrorHighlighter());
//         sheet.setColumnControlVisible(true);
//         sheet.setPreferredScrollableViewportSize(new java.awt.Dimension(400,250));
        sheet.setEditableByDefault(true);

        sheet.setRowSelectionInterval(0,0);
        sheet.setColumnSelectionInterval(0,0);

        JFrame frame = new JFrame("SwingX JXSpreadsheet");
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
                    System.exit(0);
                }
            });
        
        Container rootPane = frame.getContentPane();
        rootPane.setLayout(new BorderLayout(5,5));

        JScrollPane pane = new JScrollPane(sheet);
        rootPane.add(pane, BorderLayout.CENTER);

        frame.pack();
        frame.setVisible(true);
    }
}
