/*
 * $Id: ValidatingTree.java 3307 2010-09-10 14:35:50Z kschaefe $
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

import java.util.Hashtable;
import java.util.Vector;

import javax.swing.tree.TreeCellEditor;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;

import org.jdesktop.swingx.validator.ValidatingCellEditor;
import org.jdesktop.swingx.validator.Validator;

/**
 *
 * @author Karl George Schaefer
 */
public class ValidatingTree extends JXTree {
    protected ValidatingCellEditor validatingEditor;
    
    /**
     * 
     */
    public ValidatingTree() {
        super();
        init();
    }

    /**
     * @param value
     */
    public ValidatingTree(Hashtable<?, ?> value) {
        super(value);
        init();
    }

    /**
     * @param value
     */
    public ValidatingTree(Object[] value) {
        super(value);
        init();
    }

    /**
     * @param newModel
     */
    public ValidatingTree(TreeModel newModel) {
        super(newModel);
        init();
    }

    /**
     * @param root
     * @param asksAllowsChildren
     */
    public ValidatingTree(TreeNode root, boolean asksAllowsChildren) {
        super(root, asksAllowsChildren);
        init();
    }

    /**
     * @param root
     */
    public ValidatingTree(TreeNode root) {
        super(root);
        init();
    }

    /**
     * @param value
     */
    public ValidatingTree(Vector<?> value) {
        super(value);
        init();
    }

    private void init() {
        setInvokesStopCellEditing(true);
    }
    
    private ValidatingCellEditor getValidatingEditor() {
        if (validatingEditor == null) {
            validatingEditor = new ValidatingCellEditor();
        }
        
        return validatingEditor;
    }
    
    /**
     * Returns the editor used to edit entries in the tree.
     *
     * @return the <code>TreeCellEditor</code> in use,
     *          or <code>null</code> if the tree cannot be edited
     */
    @Override
    public TreeCellEditor getCellEditor() {
        return getValidatingEditor();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setCellEditor(TreeCellEditor cellEditor) {
        getValidatingEditor().setDelegate(cellEditor);
        
        super.setCellEditor(validatingEditor);
    }

    /**
     * @return
     * @see org.jdesktop.swingx.validator.ValidatingCellEditor#getValidator()
     */
    public Validator<?> getValidator() {
        return validatingEditor.getValidator();
    }

    /**
     * @param validator
     * @see org.jdesktop.swingx.validator.ValidatingCellEditor#setValidator(org.jdesktop.swingx.validator.Validator)
     */
    public void setValidator(Validator<?> validator) {
        validatingEditor.setValidator(validator);
    }
}
