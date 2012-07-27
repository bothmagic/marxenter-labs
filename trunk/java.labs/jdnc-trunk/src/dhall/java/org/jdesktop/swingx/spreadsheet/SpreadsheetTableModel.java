/*
 * Created on 08.05.2007
 *
 */
package org.jdesktop.swingx.spreadsheet;

import java.awt.Point;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import javax.script.ScriptEngine;
import javax.swing.table.AbstractTableModel;

import org.jdesktop.swingx.spreadsheet.CellAddressScheme;

// ===========================================
// Spreadsheet Model
// ===========================================

/**
 * Extracted from JXSpreadsheet. Moved some methods from view to
 * here.
 * 
 */
public class SpreadsheetTableModel extends AbstractTableModel implements Observer {
    
    static final long serialVersionUID = -6455541616661139146L;
    
    // The contents of the model: a set of cells keyed by their address...
    private Map<Point,Cell> _cellmap = new HashMap<Point,Cell>();

    // ... and by their names
    private Map<String,Cell> _namemap = new HashMap<String,Cell>();
    
    // The number of rows in the model
    private int _numRows;

    // The number of columns in the model
    private int _numCols;

    // Script engine used to parse & interpret the contents of cells
    private ScriptEngine _engine;

    // Addressing scheme used by this worksheet to recognize cell references
    // inscripts.
    private CellAddressScheme _scheme = new DefaultCellAddressScheme();

    /**
     * Builds a SpreadsheetTableModel of the default size (16x16)
     */
    public SpreadsheetTableModel() {
        this(16, 16);
    }
    
    /**
     * Builds a SpreadsheetTableModel of the given size
     */
    public SpreadsheetTableModel(int rows, int cols) {
        _numRows = rows;
        _numCols = cols;
    }

    public void setScriptEngine(ScriptEngine engine) {
        _engine = engine;
        fireTableStructureChanged();
    }

    public ScriptEngine getScriptEngine() {
        return _engine;
    }

    public Cell getCellReference(String key) {
        Cell cell = getCellByName(key);
        if (cell != null) {
            return cell;
        }
        
        if (getScheme().isCellAddress(key)) {
            cell = getCell(getScheme().getRowNum(key), getScheme().getColumnNum(key));
            return cell;
        }
        
        return null;
    }
    
    public CellAddressScheme getScheme() {
        return _scheme;
    }

    public boolean isCellReference(String key) {
        return getScheme().isCellAddress(key) || _namemap.containsKey(key) ;
    }

    // The value of uninitialized cells
    private Object _defaultValue = new Integer(0);


    /**
     * Returns the default value of cells that have not been initialized.
     */
    public Object getDefaultCellValue() { return _defaultValue; }
    

    /**
     * Sets the value returned by cells that have not been initialized.  By default,
     * this value is a java.lang.Integer.ZERO.  When called, if the new value is not
     * an instance of the existing default type, then the default type will be changed
     * to the class of the new value as a side-effect.
     */
    public void setDefaultCellValue(Object value) {
        _defaultValue = value;
    }


    /**
     * Removes all information from the model
     */
    public void clear() {
        _cellmap = new HashMap<Point,Cell>();
        _namemap = new HashMap<String,Cell>();
        fireTableDataChanged();
    }
    
    /**
     * Returns the cell at the given address, or null if no such cell exists.
     * @throws IndexOutOfBoundsException if the row or column is out of bounds
     */
    public Cell getCell(int row, int col) throws IndexOutOfBoundsException {
        if (checkCellAddress(row,col))
            return _cellmap.get(new Point(row, col));

        if (row < 0 || row >= _numRows) {
            String msg = "Row " +row +" out of range: 0.." +(_numRows - 1);
            throw new IndexOutOfBoundsException(msg);
        }
    
        String msg = "Col " +col +" out of range: 0.." +(_numCols - 1);
        throw new IndexOutOfBoundsException(msg);
    }

    /**
     * Builds a possibly editable cell to hold the given value.  If the value is not
     * serializable, then it will be discarded when the spreadsheet is saved or serialized.
     * @throws IndexOutOfBoundsException if the row or column is out of bounds
     */
    Cell setCellByValue(Object value, int row,int col, boolean editable){
        // @SuppressWarnings
        // the Cell returned by getCell is non-generic.
        Cell cell = getCell(row,col);
        if (cell != null) {
            cell.setValue(value);
            return cell;
        }

        cell = createDefaultCell(row, col);
        cell.setEditable(editable);
        cell.setValue(value);
        return setCell(cell);
    }

    /**
     * @param row
     * @param col
     * @return
     */
    protected Cell createDefaultCell(int row, int col) {
        return new Cell(this, row, col);
    }

    /**
     * Builds a possibly editable cell to hold the given script
     * @throws IndexOutOfBoundsException if the row or column is out of bounds
     */
    Cell setCellByScript(String script, int row,int col, boolean editable){
        if (script == null || "".equals(script))
            return null;

        // @SuppressWarnings
        // the Cell returned by getCell is non-generic.
        Cell cell = getCell(row,col);
        if (cell != null) {
            cell.setScript(script);
            return cell;
        }

        cell = createDefaultCell(row, col);
        cell.setScript(script);
        cell.setEditable(editable);
        return setCell(cell);
    }

    /**
     */
    private Cell setCell(Cell cell) {
        String name = cell.getName();
        if (name != null) {
            if (_namemap.get(name) != null) {
                String err = "Duplicate cell name "+name;
                throw new IllegalArgumentException(err);
            }
        
            _namemap.put(name, cell);
        }
        
        cell.addObserver(this);

        Point p = cell.getAddress();
        _cellmap.put(p, cell);
        fireTableCellUpdated(p.x, p.y);
        return cell;
    }

    /**
     * Returns a (possibly null) cell whose name is given
     */
    Cell getCellByName(String name) {
        return _namemap.get(name);
    }

    Cell getOrCreateCell(int row, int col) {
        Cell cell = getCell(row, col);
        if (cell == null) {
            cell = createDefaultCell(row, col);
            setCell(cell);
        }
        return cell;
    }
    /**
     * Sets the name of a cell
     */
    Cell setCellName(String name, int row, int col) {
        if (name != null) {
            Cell cell = _namemap.get(name);
            if (cell != null) {
                String err = "Duplicate cell name "+name;
                throw new IllegalArgumentException(err);
            }
        }
        
        Cell cell = getOrCreateCell(row, col);
        String oldname = cell.getName();
        if (oldname != null)
            _namemap.remove(oldname);

        if (name != null)
            _namemap.put(name, cell);
        
        cell.setName(name);
        
        // TODO: need to notify the cell's observers that the name has changed.
        // This may force a change from an Observable/Observer to the creation
        // of cell events (CellValueChanged, CellNameChanged, CellFormatChanged)
        return cell;
        
    }

    // implementation of the EditableByDefault property
    private boolean _editableByDefault;
    void setEditableByDefault(boolean b) { _editableByDefault = b; }
    boolean isEditableByDefault() { return _editableByDefault; }


    public void setRowCount(int height) {
        int oldHeight = _numRows;
        _numRows = height;
            
        if (oldHeight < height) {
            fireTableRowsInserted(oldHeight,height-1);
        }
        else {
            removeCells();
            fireTableRowsDeleted(height,oldHeight-1);
        }
    }
    
    public void setColumnCount(int width) {
        int oldWidth = _numCols;
        _numCols = width;
        if (width < oldWidth) {
            removeCells();
        }
        
        fireTableStructureChanged();
    }

    private void removeCells() {
        for (Iterator iter = _cellmap.values().iterator(); iter.hasNext(); ) {
            Cell cell = (Cell) iter.next();
            Point p = cell.getAddress();
            if (p.x >= _numRows || p.y >= _numCols) {
                iter.remove();

                String name = cell.getName();
                if(name != null)
                    _namemap.remove(cell.getName());
                
                cell.unlink();
            }
        }
    }

    // - - - - - - - - - - -
    // TableModel interface
    // - - - - - - - - - - -

    public int getRowCount() {
        return _numRows;
    }

    
    public int getColumnCount() {
        return _numCols;
    }

    
    public Object getValueAt(int row, int col) {
        if (!checkCellAddress(row, col))
            return Cell.REFERENCE_ERR;
            
        Cell cell = _cellmap.get(new Point(row, col));
        if (cell == null)
            return null;
        
        Object obj = cell.getValue();
        return cell.isUndefined() ? "" : cell.isValid() ? obj : cell.getErrorMsg();
    }

    @Override
    public void setValueAt(Object value, int row, int col) {
        Cell cell = getCell(row,col);
        if (cell != null) {
            cell.setScript(String.valueOf(value));
        }
        else if (value != null) {
            setCellByScript(String.valueOf(value), row, col, true);
        }
    }

    
    public void setScript(String script, int row, int col) {
        Cell cell = getCell(row,col);
        if (cell != null) {
            cell.setScript(script);
        }
        else if (script != null) {
            setCellByScript(script, row, col, true);
        }
    }

    @Override
    public boolean isCellEditable(int row, int col) {
        Cell cell = getCell(row, col);
        if (cell != null)
            return cell.isEditable();
        
        return _editableByDefault;
    }

    @Override
    public String getColumnName(int column) {
        return getScheme().getColumnHdr(column);
    }
    
    // - - - - - - - - - - - -
    // Observer Interface
    // - - - - - - - - - - - -


    public void update(Observable observable, Object object) {
        Cell cell = (Cell) observable;
        Point addr = cell.getAddress();
        fireTableCellUpdated(addr.x, addr.y);
    }

    // - - - - - - - - - - - -
    // implementation details
    // - - - - - - - - - - - -

    private boolean checkCellAddress(int row, int col) {
        return row >= 0 && row < _numRows && col >=0 && col < _numCols;
    }

  
}
