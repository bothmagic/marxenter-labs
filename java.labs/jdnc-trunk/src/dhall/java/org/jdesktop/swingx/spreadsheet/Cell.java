// ============================================================================
// $Id: Cell.java 1473 2007-07-04 02:29:01Z david_hall $
// Copyright (c) 2007  David A. Hall
// ============================================================================

package org.jdesktop.swingx.spreadsheet;

import java.awt.Component;
import java.awt.Point;
import java.text.MessageFormat;
import java.util.HashSet;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import javax.script.Bindings;
import javax.script.SimpleBindings;
import javax.swing.DefaultCellEditor;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import org.jdesktop.swingx.spreadsheet.CircularReferenceException;
import org.jdesktop.swingx.spreadsheet.InvalidReferenceException;

/**
 * A single cell in spreadsheet.  Encapsulates information about the script
 * that provides the value of the cell, the cell's formatting information, and
 * the editor and/or renderer used to display/update the cell's contents.
 * <p>
 * 
 * JW: c&p'ed and removed coupling to view (replaced by coupling to model). <p>
 * 
 * Copyright &copy; 2007  David A. Hall
 * @author <a href="mailto:davidahall@users.sf.net">David A. Hall</a>
 */

class Cell extends Observable implements Observer {

    // Implies that a dependency value is not of the expected type
    static public final String CLASS_CAST_ERR   = "### CLASS ###";

    // Implies that a script encounters an error when evaluated 
    static public final String EVALUATION_ERR   = "### EVAL ###";

    // Implies that a cell's script depends directly or indirectly on itself
    static public final String CIRCULAR_REF_ERR = "### CIRC ###";

    // Implies that a cell's script encounted an unexpected null value when evaluated
    static public final String NULL_POINTER_ERR = "### NULL ###";

    // Implies that a cell reference cannot be resolved
    static public final String REFERENCE_ERR    = "### REF ###";

    // Implies that a cell has been referenced before it is initialized
    static public final String UNDEFINED_ERR    = "### UNDEF ###";

    // The spreadsheet model that holds the cell
    private SpreadsheetTableModel _model;
    
    // The name of the cell
    private String _name;
    
    // caches the value held by the cell
    private Object _value;
    
    // the script that describes the value of the cell
    private String _script;

    // The address of this cell (rows & columns won't be inserted into spreadsheets (for
    // a long while, anyway): otherwise we'll need to update the address of a cell or
    // provide a map)
    private Point _addr;

    // Flag that controls editablilty: we can make cells read-only
    private boolean _editable;

    // Component used to render cells in the paint method 
    private TableCellRenderer _renderer;

    // Component used to edit cell values
    private TableCellEditor _editor;

    // The last exception caught by the cell
    private Throwable _exception;
    
    // The string shown on the UI when a cell encounters an error
    private String _errMsg;

    // The set of cells on which this cell depends.
    private Set<Cell> _dependencies = new HashSet<Cell>();

    // Bindings used to evaluate the script
    private Bindings _bindings = new SimpleBindings(){
            public final Object get(final Object key) {
                Object val = super.get(key);
                if (val != null)
                    return val;

                String str = String.valueOf(key);
                if (str.equals("cell"))
                    return Cell.this;
                
                if (str.equals("row"))
                    return _addr.x;
                
                if (str.equals("col"))
                    return _addr.y;
                
                Cell cell = _model.getCellReference(str);
                if (cell != null) {
                    if (cell.references(Cell.this)) {
                        String fmt = "Circular Reference in cell {0}";
                        throw new CircularReferenceException(MessageFormat.format(fmt, Cell.this));
                    }
                    
                    _dependencies.add(cell);
                    return cell.getValue();
                }
                
                return null;
            }

            public final boolean containsKey(final Object key) {
                if (super.containsKey(key))
                    return true;

                if ("cell".equals(key) || "row".equals(key) || "col".equals(key))
                    return true;
                
                return _model.isCellReference(String.valueOf(key));
            }
        };
            
    /**
     * Builds a possibly editable cell at the given address, using the script to build
     * a generator to retrieve the value.  
     */
    Cell(SpreadsheetTableModel model, Point addr, String script, boolean editable) {
        if (script == null || script.trim().length() <= 0)
            throw new IllegalArgumentException("Script must be supplied");

        _model = model;
        _addr = addr;
        _editable = editable;
        setScript(script);

        _renderer = new Renderer();
    }

    /**
     * Builds an empty, editable cell at the given address.
     */
    Cell(SpreadsheetTableModel model, int row, int col) {
        _model = model;
        _addr = new Point(row, col);
        _editable = true;
        _errMsg = UNDEFINED_ERR;
        _value = model.getDefaultCellValue();
    }

    /**
     * @return the sheet that contains this cell
     */
    SpreadsheetTableModel getSpreadsheetModel() { return _model; }

    /**
     * Returns the address of the cell.
     */
    public Point getAddress() { return _addr; }

    /**
     * Returns the name of the cell, if a name has been assigned.
     * @return the name of the cell, if any
     */
   public String getName() { return _name; }

    /**
     * Sets the name of the cell, if a name has been assigned.  If a cell is named,
     * then the name may be used in scripts in other cells to refer to the contents of
     * this cell.
     * @return the sheet that contains this cell
     */
    void setName(String name) { _name = name; }
    
    /**
     * Returns true if the cell is editable, false if it is read-only.
     */
    public boolean isEditable() { return _editable; }

    /**
     * Sets the cell as editable (or not).
     */
    public void setEditable(boolean b) { _editable = b; }
    
    /**
     * Returns the script that describes the cell's contents.
     */
    String getScript() { return _script; }

    /**
     * Sets the contents of the cell to the given script.
     */
    public final void setScript(String script) throws CircularReferenceException {
        if (script == null || "".equals(script)) {
            clear();
            return;
        }

        if (script.startsWith("="))
            script = script.substring(1);
     
        clearError();
        _script = script;

        updateValueImpl();
        notifyObservers();
    }

    /**
     * Returns the renderer used to paint the cell
     */
    public TableCellRenderer getRenderer() { return _renderer; }

    /**
     * Sets the component used to paint the cell
     */
    public void setRenderer(TableCellRenderer renderer) {
        _renderer = renderer;
    }
   
    /**
     * Returns the component used to edit the cell
     */
    public TableCellEditor getEditor() {
        if (_editor == null)
            _editor = new Editor();
        
        return _editor;
    }
    
    /**
     * Sets the component used to edit the cell
     */
    public void setEditor(TableCellEditor editor) {
        _editor = editor;
    }

    /**
     * Returns true if the cell's value can be retrieved without error
     */
    public boolean isValid ()   {
        return _errMsg == null;
    }

    // TODO: fix this: it's bad to use _errMsg in this way, as the first thing
    // that setScript does is clear this information.  (It might be OK, but if
    // so, its more of an accident than anything)
    
    /**
     * Returns true if the cell's value is undefined
     */
    public boolean isUndefined ()   {
        return _errMsg == UNDEFINED_ERR;
    }

    /**
     * Returns the textual description of the last error associated with cell.
     * The string will be used by the UI.  If the cell's value can be retrieved
     * without error, then this field will be null.
     */
    public String getErrorMsg() { return _errMsg; }

    public Throwable getException() { return _exception; }
    
    /**
     * Puts the cell into the error display mode: the invalid script is preserved
     * as a string, and the error message is set
     */
    private void setError(Throwable t, String script, String msg) {
        _exception = t;
        _script = script;
        _errMsg = msg;
        _value = t;
        notifyObservers();
    }

    /**
     * Puts the cell into the standard mode.
     */
    private void clearError() {
        _exception = null;
        _errMsg = null;
    }

    /**
     * Returns the value of the cell.  If an exception is caught while
     * evaluating the cell, then this will return null and the getErrorMsg()
     * method will return a description of the problem.
     */
    public Object getValue() {
        return _value;
    }

    /**
     * Sets the value of the cell to the given object, clearing the script as a side-effect.
     */
    public void setValue(Object value) {
        clearError();
        unregister();
        _script = null;
        _value = value;
        notifyObservers();
    }

    /**
     * Clears the contents of the cell, and sets its state to an invalid state to
     * force all dependant cells to reparse.
     */
    void unlink() {
        doClear(REFERENCE_ERR, new InvalidReferenceException("Cell("+_addr.x+","+_addr.y+")"));
    }

    /**
     * Clears the contents of the cell, setting to the default type and value.
     */
    public void clear() {
        doClear(null, _model.getDefaultCellValue());
    }

    private void doClear(String msg, Object value) {
        _editable = true;
        _value = _model.getDefaultCellValue();
        _script = null;
        _errMsg = null;
        notifyObservers();
    }
    
    // guards against re-entering the recover method
    private boolean _inRecovery = false;
    
    /**
     * Attempts one recovery from the given exception.  The script stored in the cell
     * is reparsed, to allow recovery in cases where there is a type mismatch between the
     * old script functor and new values in input cells.
     */
    synchronized private void recover(Exception x, String script, String msg) {
        if (_inRecovery) {
            setError(x, script, msg);
        }
        else {
            _inRecovery = true;
            setScript(_script);
            _inRecovery = false;
        }
    }

    /**
     * Returns true if this cell references the given cell, either directly or
     * indirectly.  Also returns true if this cell <i>is</i> the given cell.
     */
    public boolean references(Cell c) {
        if (getAddress().equals(c.getAddress())) {
            return true;
        }

        for (Cell c1 : _dependencies) {
            if (c1 == c) {
                return true;
            }
            if (c1.references(c)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Removes this cell as an observer from all cells in the dependency set
     */
    private void unregister() {
        for(Cell cell : _dependencies) {
            cell.deleteObserver(this);
        }

        _dependencies.clear();
    }

    /**
     * Registers this cell as an observer for all cells in the dependency set
     */
    private void register() {
        for(Cell cell : _dependencies) {
            cell.addObserver(this);
        }
    }
    
    // - - - - - - - - - - - -
    // Observer Interface
    // - - - - - - - - - - - -

    public void update(Observable observable, Object object) {
        updateValueImpl();
        notifyObservers();
    }

    /**
     * Evaluates the cell's script and caches the result.  This can cause the cell to enter
     * an error state, if the forumula throws one of several exceptions.
     */
    private void updateValueImpl() {
        try {
            clearError();
            unregister();

            if (_script != null) {
                ScriptEngine engine = _model.getScriptEngine();
                if (engine == null) {
                    _value = _script;
                }
                else {
                    Object val = _model.getScriptEngine().eval(_script, _bindings);
                    register();
                    _value = val;
                }
            }
        }
        catch (CircularReferenceException x) { setError(x, _script, CIRCULAR_REF_ERR); } 
        catch (InvalidReferenceException x)  { recover (x, _script, REFERENCE_ERR); }
        catch (NullPointerException x)       { setError(x, _script, NULL_POINTER_ERR); } 
        catch (ClassCastException x)         { recover (x, _script, CLASS_CAST_ERR); }
        catch (IllegalArgumentException x)   { recover (x, _script, EVALUATION_ERR); }
        catch (Exception x)                  { setError(x, _script, EVALUATION_ERR); }
    }

    /**
     * Triggers cells (and other observers) that this cell's contents have been
     * changed.
     */
    public void notifyObservers() {
        setChanged();
        notifyObservers(_value);
        clearChanged();
    }

    // - - - - - - - - - - -
    // Editor class
    // - - - - - - - - - - -

    static class Editor extends DefaultCellEditor {
        
        static final long serialVersionUID = -9010644547311806213L;

        public Editor() {
            super(new JTextField());
        }
     
        public Object getCellEditorValue() {
            // The superclass' editorComponent is the textField we passed at construction
            // the super class returns the text fields contents.  we'll parse it and pass
            // the result
            String script = (String) super.getCellEditorValue();
            if (script.startsWith("="))
                script = script.substring(1);
        
            return script;
        }
        
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected,
                                                     int row, int col)
        {
            SpreadsheetTableModel sheet = (SpreadsheetTableModel) table.getModel();
            Cell cell = sheet.getOrCreateCell(row,col);
            return super.getTableCellEditorComponent(table, cell._script, isSelected, row, col);
        }
    }

    // - - - - - - - - - - -
    // Renderer class
    // - - - - - - - - - - -

    static class Renderer extends DefaultTableCellRenderer {
        
        public Component getTableCellRendererComponent(JTable table, Object val, boolean isSelected,
                                                       boolean hasFocus, int row, int column)
        {
            SpreadsheetTableModel sheet = (SpreadsheetTableModel) table.getModel();
            Cell cell = sheet.getCell(row,column);
            boolean editable = (cell != null) ? cell.isEditable() : sheet.isEditableByDefault();
            return super.getTableCellRendererComponent(table, val, isSelected && editable,
                                                       hasFocus && editable, row, column);
        }
    }
}

