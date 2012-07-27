/*
 * $Id: AbstractFileSystemModel.java 2674 2008-08-28 17:56:42Z kschaefe $
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

package org.jdesktop.swingx.treetable;

import java.io.File;
import java.util.Arrays;

/**
 * A tree table model to simulate a file system.
 * <p>
 * This tree table model implementation extends {@code AbstractTreeTableModel}.
 * The file system metaphor demonstrates that it is often easier to directly
 * implement tree structures directly instead of using intermediaries, such as
 * {@code TreeTableNode}.
 * <p>
 * This abstract model leaves requires the user to implement:
 * <pre>
 * getColumnCount(Object node);
 * getValueAt(Object node, int index);
 * </pre>
 * 
 * @author Ramesh Gupta
 * @author Karl Schaefer
 */
public abstract class AbstractFileSystemModel extends AbstractTreeTableModel {
    /**
     * The size used for directories.
     */
    public static final Long DIRECTORY = 0L;

    /**
     * Creates a file system model using the specified {@code root}.
     * 
     * @param root
     *            the root for this model; this may be different than the root
     *            directory for a file system.
     */
    public AbstractFileSystemModel(File root) {
        super(root);
    }

    /**
     * Determines if the object is a valid {@code File} managed by this model.
     * 
     * @param file
     *                the object to evaluate
     * @return {@code true} if the object is a file managed by this model;
     *         {@code false} otherwise
     */
    protected boolean isValidFileNode(Object file) {
        boolean result = false;
        
        if (file instanceof File) {
            File f = (File) file;
            
            while (!result && f != null) {
                result = f.equals(root);
                
                f = f.getParentFile();
            }
        }
        
        return result;
    }
    
    /**
     * {@inheritDoc}
     */
    public File getChild(Object parent, int index) {
        if (!isValidFileNode(parent)) {
            throw new IllegalArgumentException("parent is not a file governed by this model");
        }
        
        File parentFile = (File) parent;
        String[] children = parentFile.list();
        
        if (children != null) {
            return new File(parentFile, children[index]);
        }
        
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public int getChildCount(Object parent) {
        if (parent instanceof File) {
            String[] children = ((File) parent).list();
            
            if (children != null) {
                return children.length;
            }
        }

        return 0;
    }

    /**
     * {@inheritDoc}
     */
    public int getIndexOfChild(Object parent, Object child) {
        if (parent instanceof File && child instanceof File) {
            File parentFile = (File) parent;
            File[] files = parentFile.listFiles();
            
            Arrays.sort(files);
            
            for (int i = 0, len = files.length; i < len; i++) {
                if (files[i].equals(child)) {
                    return i;
                }
            }
        }
        
        return -1;
    }

    /**
     * {@inheritDoc}
     */
    public File getRoot() {
        return (File) root;
    }

    /**
     * Sets the root for this tree table model. This method will notify
     * listeners that a change has taken place.
     * 
     * @param root
     *            the new root node to set
     */
    public void setRoot(File root) {
        this.root = root;
        
        modelSupport.fireNewRoot();
    }

    /**
     * {@inheritDoc}
     */
    public boolean isLeaf(Object node) {
        if (node instanceof File) {
            //do not use isFile(); some system files return false
            return ((File) node).list() == null;
        }
        
        return true;
    }
}
