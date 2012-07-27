/*
 * $Id: ValidatingCellEditor.java 3307 2010-09-10 14:35:50Z kschaefe $
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
package org.jdesktop.swingx.validator;

import java.awt.Color;
import java.awt.Component;
import java.util.EventObject;

import javax.swing.BorderFactory;
import javax.swing.CellEditor;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.EventListenerList;
import javax.swing.table.TableCellEditor;
import javax.swing.tree.TreeCellEditor;
import javax.swing.tree.DefaultTreeCellEditor.EditorContainer;

/**
 * 
 */
@SuppressWarnings("unchecked")
public class ValidatingCellEditor implements TableCellEditor, TreeCellEditor {
    protected class ValidationCellEditorListener implements CellEditorListener {
        /**
         * {@inheritDoc}
         */
        @Override
        public void editingCanceled(ChangeEvent e) {
            fireEditingCanceled(e);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void editingStopped(ChangeEvent e) {
            if (isValueValid()) {
                fireEditingStopped(e);
            }
        }
    }

    protected EventListenerList listenerList = new EventListenerList();
    
    @SuppressWarnings("rawtypes")
    private Validator validator;

    private CellEditor delegate;

    private Component editorComponent;

    private boolean cleanUp(boolean returnValue) {
        //TODO do we need to perform clean up?
         if (returnValue) {
//         validator = null;
//         delegate = null;
             editorComponent = null;
         }

        return returnValue;
    }

    protected void fireEditingCanceled(ChangeEvent e) {
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == CellEditorListener.class) {
                // Lazily create the event:
                ((CellEditorListener) listeners[i + 1]).editingCanceled(e);
            }
        }
    }

    protected void fireEditingStopped(ChangeEvent e) {
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == CellEditorListener.class) {
                // Lazily create the event:
                ((CellEditorListener) listeners[i + 1]).editingStopped(e);
            }
        }
    }

    protected boolean isValueValid() {
        return validator.validate(getCellEditorValue());
    }

    /**
     * @return Returns the validator.
     */
    public Validator<?> getValidator() {
        return validator;
    }

    /**
     * @param validator
     *                The validator to set.
     */
    public void setValidator(Validator<?> validator) {
        this.validator = validator == null ? Validators.getAlwaysValidator() : validator;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean stopCellEditing() {
        if (isValueValid()) {
            return cleanUp(delegate.stopCellEditing());
        }

        JComponent comp = null;

        if (editorComponent instanceof EditorContainer) {
            Component c = ((EditorContainer) editorComponent).getComponent(0);

            if (c instanceof JComponent) {
                comp = (JComponent) c;
            }
        } else if (editorComponent instanceof JComponent) {
            comp = (JComponent) editorComponent;
        }

        if (comp != null) {
            //TODO get old border to reset during clean up
            comp.setBorder(BorderFactory.createLineBorder(Color.RED));
        }

        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addCellEditorListener(CellEditorListener l) {
        listenerList.add(CellEditorListener.class, l);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void cancelCellEditing() {
        delegate.cancelCellEditing();
        cleanUp(true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getCellEditorValue() {
        Object value = delegate.getCellEditorValue();

        return value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isCellEditable(EventObject anEvent) {
        return delegate.isCellEditable(anEvent);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeCellEditorListener(CellEditorListener l) {
        listenerList.remove(CellEditorListener.class, l);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean shouldSelectCell(EventObject anEvent) {
        return delegate.shouldSelectCell(anEvent);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected,
            int row, int column) {
        editorComponent = ((TableCellEditor) delegate).getTableCellEditorComponent(table, value,
                isSelected, row, column);
        return editorComponent;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Component getTreeCellEditorComponent(JTree tree, Object value, boolean isSelected,
            boolean expanded, boolean leaf, int row) {
        editorComponent = ((TreeCellEditor) delegate).getTreeCellEditorComponent(tree, value,
                isSelected, expanded, leaf, row);
        return editorComponent;
    }

    /**
     * @return the delegate
     */
    public CellEditor getDelegate() {
        return delegate;
    }

    /**
     * @param delegate
     *                the delegate to set
     */
    public void setDelegate(CellEditor delegate) {
        this.delegate = delegate;
        delegate.addCellEditorListener(new ValidationCellEditorListener());
    }
}
