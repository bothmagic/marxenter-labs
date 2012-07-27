/*
 * $Id: ComponentAdapter.java 2273 2008-02-18 16:18:20Z headw01 $
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

package org.jdesktop.swingx.decorator;

import javax.swing.JComponent;

/**
 * Abstract base class for all component data adapter classes. A
 * <code>ComponentAdapter</code> allows a {@link Filter}, {@link Sorter}, or
 * {@link Highlighter} to interact with a {@link #target} component through a
 * common API.
 * 
 * It has two aspects:
 * <ul>
 * <li> interact with the view state for a given data element. The row/cloumn
 * fields and the parameterless methods service this aspect. The coordinates are
 * in view coordinate system. Typical clients of this service are
 * HighlightPredicates and Highlighters.
 * <li> interact with the data of the component. The methods for this are those
 * taking row/column indices as parameters. The coordinates are in model
 * coordinate system. Typical clients of this service are Filters.
 * </ul>
 * 
 * Typically, application code is interested in the first aspect. An example is
 * highlighting the background of a row in a JXTable based on the value of a
 * cell in a specific column. The solution is to implement a custom
 * HighlightPredicate which decides if a given cell should be highlighted and
 * configure a ColorHighlighter with the predicate and an appropriate background
 * color.
 * 
 * <pre><code>
 * HighlightPredicate feverWarning = new HighlightPredicate() {
 *     int temperatureColumn = 10;
 * 
 *     public boolean isHighlighted(Component component, ComponentAdapter adapter) {
 *         return hasFever(adapter.getValue(temperatureColumn));
 *     }
 * 
 *     private boolean hasFever(Object value) {
 *         if (!value instanceof Number)
 *             return false;
 *         return ((Number) value).intValue() &gt; 37;
 *     }
 * };
 * 
 * Highlighter hl = new ColorHighlighter(feverWarning, Color.RED, null);
 * </code></pre>
 * 
 * The adapter is responsible for mapping column coordinates.
 * 
 * All input column indices are in model coordinates with exactly two
 * exceptions:
 * <ul>
 * <li> {@link #column} in column view coordinates
 * <li> the mapping method {@link #viewToModel(int)} in view coordinates
 * </ul>
 * 
 * All input row indices are in model coordinates with exactly two exceptions:
 * <ul>
 * <li> {@link #row} in row view coordinates
 * <li> the getter for the filtered value {@link #getFilteredValueAt(int, int)}
 * takes the row in view coordinates.
 * </ul>
 * 
 * 
 * 
 * PENDING JW: anything to gain by generics here?
 * 
 * @author Ramesh Gupta
 * @author Karl Schaefer
 * 
 * @see org.jdesktop.swingx.decorator.HighlightPredicate
 * @see org.jdesktop.swingx.decorator.Highlighter
 * @see org.jdesktop.swingx.decorator.Filter
 */
public abstract class ComponentAdapter<T extends MatchAssistant<U>, U extends JComponent> {
    /** current row in view coordinates. */
    public int row = 0;
    /** current column in view coordinates. */
    public int column = 0;
    protected final U	target;

    /** MatchAssistant for this class */
    private T matchAssistant = null;
    
    
    /**
     * Constructs a ComponentAdapter, setting the specified component as the
     * target component.
     *
     * @param component target component for this adapter
     */
    public ComponentAdapter(U component) {
        target = component;
    }

    public U getComponent() {
        return target;
    }

    public T getMatchAssistant() {
        if (null == matchAssistant) {
            matchAssistant = createMatchAssistant();
        }
        return matchAssistant;
    }
    
    /**
     * Create a custom default instance of a MatchAssistant.<br>
     * This method may return <code>null</code> if there isn't a default
     * implementation.
     * @return
     */
    protected T createMatchAssistant() {
        return null;
    }

    public void setMatchAssistant(T matchAssistant) {
        this.matchAssistant = matchAssistant;
    }

//---------------------------- accessing the target's model
    
    /**
     * Returns the column's display name (= headerValue) of the column
     * at columnIndex in model coordinates.
     * 
     * Used f.i. in SearchPanel to fill the field with the 
     * column name.<p>
     * 
     * Note: it's up to the implementation to decide for which
     * columns it returns a name - most will do so for the
     * subset with isTestable = true.
     * 
     * @param columnIndex in model coordinates
     * @return column name or null if not found/not testable.
     */
    public abstract String getColumnName(int columnIndex);

    /**
     * Returns the columns logical name (== identifier) of the column at 
     * columnIndex in model coordinates.
     * 
     * Note: it's up to the implementation to decide for which
     * columns it returns a name - most will do so for the
     * subset with isTestable = true.
     * 
     * @param columnIndex in model coordinates
     * @return the String value of the column identifier at columnIndex
     *   or null if no identifier set
     */
    public abstract String getColumnIdentifier(int columnIndex);

    /**
     * Returns the number of columns in the target's data model.
     *
     * @return the number of columns in the target's data model.
     */
    public int getColumnCount() {
        return 1;	// default for combo-boxes, lists, and trees
    }

    /**
     * Returns the number of rows in the target's data model.
     *
     * @return the number of rows in the target's data model.
     */
    public int getRowCount() {
        return 0;
    }

    /**
     * Returns the value of the target component's cell identified by the
     * specified row and column in model coordinates.
     *
     * @param row in model coordinates
     * @param column in model coordinates
     * @return the value of the target component's cell identified by the
     *          specified row and column
     */
    public abstract Object getValueAt(int row, int column);
    
    /**
     * Sets the value of the target component's cell identified by the
     * specified row and column in model coordinates.
     * 
     * @param aValue the value to set
     * @param row in model coordinates
     * @param column in model coordinates
     */
    public abstract void setValueAt(Object aValue, int row, int column);

    /**
     * Determines whether this cell is editable.
     * 
     * @param row the row to query in model coordinates
     * @param column the column to query in model coordinates
     * @return <code>true</code> if the cell is editable, <code>false</code>
     *         otherwise
     */
    public abstract boolean isCellEditable(int row, int column);

    /**
     * Returns true if the column should be included in testing.<p>
     * 
     * Here: returns true if visible (that is modelToView gives a valid
     * view column coordinate). 
     * 
     * @param column the column index in model coordinates
     * @return true if the column should be included in testing
     */
    public boolean isTestable(int column) {
        return modelToView(column) >= 0;
    }
    
//----------------------- accessing the target's view state
    
    /**
     * Returns the value of the cell identified by this adapter. That is,
     * for the at position (adapter.row, adapter.column) in view coordinates.<p>
     * 
     * NOTE: this implementation assumes that view coordinates == model 
     * coordinates, that is simply calls getValueAt(this.row, this.column). It is
     * up to subclasses to override appropriately is they support model/view
     * coordinate transformation.
     * 
     * @return the value of the cell identified by this adapter
     * @see #getValueAt(int, int)
     * @see #getFilteredValueAt(int, int)
     * @see #getValue(int)
     */
    public Object getValue() {
        return getValueAt(row, column);
    }

    
    /**
     * Returns the value of the cell identified by the current 
     * adapter row and the given column index in model coordinates.<p>
     * 
     * @param modelColumnIndex the column index in model coordinates 
     * @return the value of the cell identified by this adapter
     * @see #getValueAt(int, int)
     * @see #getFilteredValueAt(int, int)
     * @see #getValue(int)
     */
    public Object getValue(int modelColumnIndex) {
        return getFilteredValueAt(row, modelColumnIndex);
    }
    
    /**
     * Returns the filtered value of the cell identified by the row
     * in view coordinate and the column in model coordinates.
     * 
     * Note: the asymetry of the coordinates is intentional - clients like
     * Highlighters are interested in view values but might need to access
     * non-visible columns for testing. While it is possible to access 
     * row coordinates different from the current (that is this.row) it is not
     * safe to do so for row > this.row because the adapter doesn't allow to
     * query the count of visible rows.
     * 
     * @param row the row of the cell in view coordinates
     * @param column the column of the cell in model coordinates.
     * @return the filtered value of the cell identified by the row
     * in view coordinate and the column in model coordiantes
     */
    public abstract Object getFilteredValueAt(int row, int column);

    /**
     * Returns true if the cell identified by this adapter currently has focus.
     * Otherwise, it returns false.
     *
     * @return true if the cell identified by this adapter currently has focus;
     * 	Otherwise, return false
     */
    public abstract boolean hasFocus();

    /**
     * Returns true if the cell identified by this adapter is currently selected.
     * Otherwise, it returns false.
     *
     * @return true if the cell identified by this adapter is currently selected;
     * 	Otherwise, return false
     */
    public abstract boolean isSelected();

    /**
     * Returns {@code true} if the cell identified by this adapter is editable,
     * {@code false} otherwise.
     * 
     * @return {@code true} if the cell is editable, {@code false} otherwise
     */
    public abstract boolean isEditable();
    
    /**
     * Returns true if the cell identified by this adapter is currently expanded.
     * Otherwise, it returns false. For components that do not support
     * hierarchical data, this method always returns true because the cells in
     * such components can never be collapsed.
     *
     * @return true if the cell identified by this adapter is currently expanded;
     * 	Otherwise, return false
     */
    public boolean isExpanded() {
        return true; // sensible default for JList and JTable
    }

    /**
     * Returns true if the cell identified by this adapter is a leaf node.
     * Otherwise, it returns false. For components that do not support
     * hierarchical data, this method always returns true because the cells in
     * such components can never have children.
     *
     * @return true if the cell identified by this adapter is a leaf node;
     * 	Otherwise, return false
     */
    public boolean isLeaf() {
        return true; // sensible default for JList and JTable
    }

    /**
     * Returns true if the cell identified by this adapter displays the hierarchical node.
     * Otherwise, it returns false. For components that do not support
     * hierarchical data, this method always returns false because the cells in
     * such components can never have children.
     *
     * @return true if the cell identified by this adapter displays the hierarchical node;
     * 	Otherwise, return false
     */
    public boolean isHierarchical() {
        return false; // sensible default for JList and JTable
    }

    /**
     * Returns the depth of this row in the hierarchy where the root is 0. For
     * components that do not contain hierarchical data, this method returns 1.
     * 
     * @return the depth for this adapter
     */
    public int getDepth() {
        return 1; // sensible default for JList and JTable
    }
    
    /**
     * For target components that support multiple columns in their model,
     * along with column reordering in the view, this method transforms the
     * specified columnIndex from model coordinates to view coordinates. For all
     * other types of target components, this method returns the columnIndex
     * unchanged.
     *
     * @param columnIndex index of a column in model coordinates
     * @return index of the specified column in view coordinates
     */
    public int modelToView(int columnIndex) {
        return columnIndex; // sensible default for JList and JTree
    }

   /**
     * For target components that support multiple columns in their model, along
     * with column reordering in the view, this method transforms the specified
     * columnIndex from view coordinates to model coordinates. For all other
     * types of target components, this method returns the columnIndex
     * unchanged.
     * 
     * @param columnIndex index of a column in view coordinates
     * @return index of the specified column in model coordinates
     */
    public int viewToModel(int columnIndex) {
        return columnIndex; // sensible default for JList and JTree
    }

    /**
     * Updates the target component on screen. This implementation revalidates and
     * repaints. 
     */
    public void refresh() {
        target.revalidate();
        target.repaint();
    }
}