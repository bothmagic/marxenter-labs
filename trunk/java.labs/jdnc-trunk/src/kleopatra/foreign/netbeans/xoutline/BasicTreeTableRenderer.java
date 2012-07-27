/*
 * $Id: BasicTreeTableRenderer.java 2524 2008-07-07 10:52:02Z kleopatra $
 *
 * Copyright 2006 Sun Microsystems, Inc., 4150 Network Circle,
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
 *
 */
package netbeans.xoutline;


import java.awt.Component;
import java.util.EventObject;

import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.event.CellEditorListener;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import org.jdesktop.swingx.renderer.AbstractRenderer;
import org.jdesktop.swingx.renderer.CellContext;
import org.jdesktop.swingx.renderer.ComponentProvider;
import org.jdesktop.swingx.renderer.IconValue;
import org.jdesktop.swingx.renderer.LabelProvider;
import org.jdesktop.swingx.renderer.MappedValue;
import org.jdesktop.swingx.renderer.StringValue;


/**
 * Quick shot for a CellRenderer/Editor for the hierarchical column of 
 * a JXXTreeTable. The basic approach is to wrap the renderer/editor
 * obtained by the standard lookup into this. It's responsible for
 * the "tree-like" look, that is the indentation and showing the icons
 * as appropriate, bidi compliant.<p>
 * 
 * NOTE: this should be an implemenation secret of the BasicTreeTableUI!
 * Would require to c&p all paintXX methods from BasicTableUI - so keep
 * it here (and let the table be aware of this service) until we know
 * better what we really need.
 * 
 * 
 * <p>
 * 
 * 
 * PENDING JW: border around editor missing
 * 
 * @author Jeanette Winzenburg
 * 
 * @see ComponentProvider
 * @see LabelProvider
 * @see StringValue
 * @see IconValue
 * @see MappedValue
 * @see CellContext
 * 
 */
public class BasicTreeTableRenderer extends AbstractRenderer
        implements TableCellRenderer, TableCellEditor {

    private TreeTableCellContext cellContext;
    private TableCellEditor cellEditorDelegate;
    private EditorProvider editorProvider;
    
    
    /**
     * Instantiates a default table renderer with the default component
     * provider. 
     * 
     * @see #DefaultTableRenderer(ComponentProvider)
     */
    public BasicTreeTableRenderer() {
        this((ComponentProvider<?>) null);
    }

    /**
     * Instantiates a default table renderer with the given component provider.
     * If the controller is null, creates and uses a default. The default
     * provider is of type <code>LabelProvider</code>.
     * 
     * @param componentProvider the provider of the configured component to
     *        use for cell rendering
     */
    public BasicTreeTableRenderer(ComponentProvider<?> componentProvider) {
        super(componentProvider);
        this.cellContext = new TreeTableCellContext();
        this.editorProvider = new EditorProvider();
    }

    /**
     * Instantiates a default table renderer with a default component
     * provider using the given converter. 
     * 
     * @param converter the converter to use for mapping the
     *   content value to a String representation.
     *   
     * @see #DefaultTableRenderer(ComponentProvider)  
     */
    public BasicTreeTableRenderer(StringValue converter) {
        this(new BasicTreeTablePanelProvider(converter));
    }

    /**
     * Instantiates a default table renderer with a default component
     * provider using the given converter and horizontal 
     * alignment. 
     * 
     * @param converter the converter to use for mapping the
     *   content value to a String representation.
     *   
     * @see #DefaultTableRenderer(ComponentProvider)  
     */
//    public DefaultOutlineRenderer(StringValue converter, int alignment) {
//        this(new LabelProvider(converter, alignment));
//    }

    // -------------- implements javax.swing.table.TableCellRenderer
    /**
     * 
     * Returns a configured component, appropriate to render the given
     * list cell.  
     * 
     * @param table the <code>JTable</code>
     * @param value the value to assign to the cell at
     *        <code>[row, column]</code>
     * @param isSelected true if cell is selected
     * @param hasFocus true if cell has focus
     * @param row the row of the cell to render
     * @param column the column of the cell to render
     * @return the default table cell renderer
     */
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
        
        boolean expanded = true;
        boolean leaf = true;
        if (table instanceof JXXTreeTable) {
            JXXTreeTable treeTable = (JXXTreeTable) table;
                expanded = treeTable.isExpanded(row);
                leaf = treeTable.isLeaf(row);
        }
        cellContext.installContext(table, value, row, column, isSelected, hasFocus,
                expanded, leaf);
        return componentController.getRendererComponent(cellContext);
    }

    /**
     * {@inheritDoc}
     */ 
    @Override
    protected ComponentProvider<?> createDefaultComponentProvider() {
        return new BasicTreeTablePanelProvider();
    }

    
//----------------- editor
    
    public void setCellEditorDelegate(TableCellEditor delegate) {
        this.cellEditorDelegate = delegate;
    }
    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
            boolean isSelected, int row, int column) {
        JComponent comp = (JComponent) cellEditorDelegate.getTableCellEditorComponent(
                table, value, isSelected, row, column);
        editorProvider.setEditingComponent(comp);
        
        boolean expanded = true;
        boolean leaf = true;
        if (table instanceof JXXTreeTable) {
            JXXTreeTable treeTable = (JXXTreeTable) table;
                expanded = treeTable.isExpanded(row);
                leaf = treeTable.isLeaf(row);
        }
        cellContext.installEditingContext(table, value, row, column, isSelected, true ,
                expanded, leaf);
        ((BasicTreeTablePanelProvider) componentController).setWrappee(editorProvider);
        return componentController.getRendererComponent(cellContext);
        
    }

    @Override
    public void addCellEditorListener(CellEditorListener l) {
        cellEditorDelegate.addCellEditorListener(l);
        
    }

    @Override
    public void cancelCellEditing() {
        cellEditorDelegate.cancelCellEditing();
        
    }

    @Override
    public Object getCellEditorValue() {
        return cellEditorDelegate.getCellEditorValue();
    }

    @Override
    public boolean isCellEditable(EventObject anEvent) {
        return cellEditorDelegate.isCellEditable(anEvent);
    }

    @Override
    public void removeCellEditorListener(CellEditorListener l) {
        cellEditorDelegate.removeCellEditorListener(l);
        
    }

    @Override
    public boolean shouldSelectCell(EventObject anEvent) {
        return cellEditorDelegate.shouldSelectCell(anEvent);
    }

    @Override
    public boolean stopCellEditing() {
        return cellEditorDelegate.stopCellEditing();
    }


    private static class EditorProvider extends ComponentProvider {

        public void setEditingComponent(JComponent editor) {
            rendererComponent = editor;
        }
        @Override
        protected void configureState(CellContext context) {
            // TODO Auto-generated method stub
            
        }

        @Override
        protected JComponent createRendererComponent() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        protected void format(CellContext context) {
            // TODO Auto-generated method stub
            
        }
        
    }
}


