/*
 * $Id: TableColumnExt.java,v 1.1 2008/03/27 03:14:13 kschaefe Exp $
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
package org.jdesktop.swingx.table;

import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import org.jdesktop.swingx.plaf.UIDependent;
import org.jdesktop.swingx.validator.Validator;

/**
 * An extension of {@link TableColumnExt} for demonstrating the simple validation framework.
 * 
 * @author Karl Schaefer
 * 
 * @see TableColumnModelExt
 */
public class ValidatingTableColumnExt extends TableColumnExt implements UIDependent {

    /** per-column validator  */
    protected Validator<?> validator;
    
    /**
     * Creates new table view column with a model index = 0.
     */
    public ValidatingTableColumnExt() {
        this(0);
    }

    /**
     * Creates new table view column with the specified model index.
     * @param modelIndex index of table model column to which this view column
     *        is bound.
     */
    public ValidatingTableColumnExt(int modelIndex) {
        this(modelIndex, 75);	// default width taken from javax.swing.table.TableColumn
    }

    /**
     * Creates new table view column with the specified model index and column width.
     * @param modelIndex index of table model column to which this view column
     *        is bound.
     * @param width pixel width of view column
     */
    public ValidatingTableColumnExt(int modelIndex, int width) {
        this(modelIndex, width, null, null);
    }

    /**
     * Creates new table view column with the specified model index, column
     * width, cell renderer and cell editor.
     * @param modelIndex index of table model column to which this view column
     *        is bound.
     * @param width pixel width of view column
     * @param cellRenderer the cell renderer which will render all cells in this
     *        view column
     * @param cellEditor the cell editor which will edit cells in this view column
     */
    public ValidatingTableColumnExt(int modelIndex, int width,
                          TableCellRenderer cellRenderer, TableCellEditor cellEditor) {
        super(modelIndex, width, cellRenderer, cellEditor);
    }

    /**
     * Instantiates a new table view column with all properties copied from the 
     * given original.
     * 
     * @param columnExt the column to copy properties from
     * @see #copyFrom(ValidatingTableColumnExt)
     */
    public ValidatingTableColumnExt(ValidatingTableColumnExt columnExt) {
        this(columnExt.getModelIndex(), columnExt.getWidth(), columnExt
                .getCellRenderer(), columnExt.getCellEditor());
        copyFrom(columnExt);
    }


    /**
     * Sets the validator to use for this column.
     * 
     * @param validator a custom validator to use during editing
     * 
     * @see #getValidator
     */
    public void setValidator(Validator<?> validator) {
        Validator<?> old = getValidator();
        this.validator = validator;
        firePropertyChange("validator", old, getValidator());
    }
    
    /**
     * Returns the validator to use for the column. 
     * The default is <code>null</code>.
     * 
     * @return <code>Validator</code> to use for this column
     * @see #setValidator
     */
    public Validator<?> getValidator() {
        return validator;
    }

    /**
      * Copies properties from original. Handles all properties except
      * modelIndex, width, cellRenderer, cellEditor. Called from copy 
      * constructor.
      *  
      * @param original the tableColumn to copy from
      *
      * @see #TableColumnExt(ValidatingTableColumnExt)
      */
     protected void copyFrom(ValidatingTableColumnExt original) {
         super.copyFrom(original);
         setValidator(original.getValidator());
     }
}
