package org.jdesktop.swingx.table;

import javax.swing.table.TableModel;

/**
 * An interface that describes a mutable table model. This model allows for the
 * addition, insertion, and deletion of rows and optionally the addition of
 * columns.
 * <p>
 * The methods of this model were reverse-engineered from
 * {@code DefaultTableModel}, so that it would be possible to add this
 * interface to {@code DefaultTableModel} without altering it.
 * 
 * @param <R>
 *            the type of row data. If this interface were applied to
 *            {@code DefaultTableModel}, {@code T} would be {@code Vector}.
 * @param <C>
 *            the type of column data. If this interface were applied to
 *            {@code DefaultTableModel}, {@code T} would be {@code Vector}.
 * 
 * @author Karl Schaefer
 * @see javax.swing.table.DefaultTableModel
 */
public interface MutableTableModel<R, C> extends TableModel {
    //Row manipulation
    /**
     * Adds a row to the end of the model. The new row will contain {@code null}
     * values unless {@code rowData} is specified.
     * <p>
     * A {@code TableModelEvent} will be generated to notify listeners that a
     * row has been added.
     * 
     * @param rowData
     *            optional data of the row being added
     */
    void addRow(R rowData);
    
    /**
     * Inserts a row at {@code row} in the model. The new row will contain
     * {@code null} values unless {@code rowData} is specified.
     * <p>
     * A {@code TableModelEvent} will be generated to notify listeners that a
     * row has been inserted.
     * 
     * @param row
     *            the row index of the row to be inserted
     * @param rowData
     *            optional data of the row being added
     * @throws IndexOutOfBoundsException
     *                if row was invalid
     */
    void insertRow(int row, R rowData);
    
    /**
     * Removes the row at {@code row} from the model.
     * <p>
     * A {@code TableModelEvent} will be generated to notify listeners that a
     * row has been removed.
     * 
     * @param row
     *            the row index of the row to be removed
     * @throws IndexOutOfBoundsException
     *             if the row was invalid
     */
    void removeRow(int row);
    
    //Extended row manipulation
    /**
     * Moves one or more rows from the inclusive range {@code start} to
     * {@code end} to the {@code to} position in the model. After the move, the
     * row that was at index {@code start} will be at index {@code to}.
     * <p>
     * A {@code TableModelEvent} will be generated to notify listeners that the
     * table data has changed.
     * <p>
     * 
     * <pre>
     *  Examples of moves:
     * <p>
     *  1. moveRow(1,3,5);
     *          a|B|C|D|e|f|g|h|i|j|k   - before
     *          a|e|f|g|h|B|C|D|i|j|k   - after
     * <p>
     *  2. moveRow(6,7,1);
     *          a|b|c|d|e|f|G|H|i|j|k   - before
     *          a|G|H|b|c|d|e|f|i|j|k   - after
     * <p> 
     *  </pre>
     * 
     * @param start
     *            the starting row index to be moved
     * @param end
     *            the ending row index to be moved
     * @param to
     *            the destination of the rows to be moved
     * @throws IndexOutOfBoundsException
     *             if any of the elements would be moved out of the table's
     *             range
     */
    void moveRow(int start, int end, int to);    
    
    //Column manipulation
    /**
     * Adds a column to the model (optional operation). The new column will have
     * the identifier {@code columnName}, which may be {@code null}. The new
     * column will contain {@code null} values unless {@code columnData} is
     * specified.
     * <p>
     * A {@code TableModelEvent} will be generated to notify listeners that the
     * table data has changed.
     * 
     * @param columnName
     *            the identifier of the column being added
     * @param columnData
     *            optional data of the column being added
     * @throws UnsupportedOperationException
     *             if the {@code addColumn} operation is not supported by this
     *             table model
     */
    void addColumn(Object columnName, C columnData);
}
