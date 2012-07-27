/*
 * $Id: ValidatingTable.java 3307 2010-09-10 14:35:50Z kschaefe $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */
package org.jdesktop.swingx;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.swing.ListSelectionModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

import org.jdesktop.swingx.table.ColumnFactory;
import org.jdesktop.swingx.table.TableColumnExt;
import org.jdesktop.swingx.table.ValidatingTableColumnExt;
import org.jdesktop.swingx.validator.ValidatingCellEditor;
import org.jdesktop.swingx.validator.Validator;
import org.jdesktop.swingx.validator.Validators;

/**
 * @author Karl George Schaefer
 *
 */
public class ValidatingTable extends JXTable {

    protected transient Map<Class<?>, Validator<?>> defaultValidatorsByColumnClass;

    private ValidatingCellEditor validatingEditor = new ValidatingCellEditor();
    
    /**
     * 
     */
    public ValidatingTable() {
        super();
        init();
    }

    /**
     * @param numRows
     * @param numColumns
     */
    public ValidatingTable(int numRows, int numColumns) {
        super(numRows, numColumns);
        init();
    }

    /**
     * @param rowData
     * @param columnNames
     */
    public ValidatingTable(Object[][] rowData, Object[] columnNames) {
        super(rowData, columnNames);
        init();
    }

    /**
     * @param dm
     * @param cm
     * @param sm
     */
    public ValidatingTable(TableModel dm, TableColumnModel cm, ListSelectionModel sm) {
        super(dm, cm, sm);
        init();
    }

    /**
     * @param dm
     * @param cm
     */
    public ValidatingTable(TableModel dm, TableColumnModel cm) {
        super(dm, cm);
        init();
    }

    /**
     * @param dm
     */
    public ValidatingTable(TableModel dm) {
        super(dm);
        init();
    }

    /**
     * @param rowData
     * @param columnNames
     */
    public ValidatingTable(Vector rowData, Vector columnNames) {
        super(rowData, columnNames);
        init();
    }

    private void init() {
        //TODO remove this line when merging to SwingX
        setColumnFactory(new ColumnFactory() {
            /**
             * {@inheritDoc}
             */
            @Override
            public TableColumnExt createTableColumn(int modelIndex) {
                return new ValidatingTableColumnExt(modelIndex);
            }
        });
        createDefaultColumnsFromModel();
        createDefaultValidators();
    }
    
    protected void createDefaultValidators() {
        defaultValidatorsByColumnClass = new HashMap<Class<?>, Validator<?>>();
        
        defaultValidatorsByColumnClass.put(Object.class, Validators.getAlwaysValidator());
    }

    @Override
    public TableCellEditor getCellEditor(int row, int column) {
        TableCellEditor editor = super.getCellEditor(row, column);
        
        validatingEditor.setDelegate(editor);
        validatingEditor.setValidator(getValidator(row, column));
        
        return validatingEditor;
    }

    /**
     * @param row
     * @param column
     * @return
     */
    public Validator<?> getValidator(int row, int column) {
        TableColumnExt columnExt = getColumnExt(column);
        Validator<?> validator = null;
        
        if (columnExt instanceof ValidatingTableColumnExt) {
            validator = ((ValidatingTableColumnExt) columnExt).getValidator();
        }
        
        if (validator == null) {
            Class<?> columnClass = getColumnClass(column);
            
            validator = defaultValidatorsByColumnClass.get(columnClass);
        }
        
        return validator;
    }
}
