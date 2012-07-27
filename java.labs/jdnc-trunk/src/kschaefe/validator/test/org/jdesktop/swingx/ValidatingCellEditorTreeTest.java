/*
 * $Id: ValidatingCellEditorTreeTest.java 2364 2008-03-27 12:58:16Z kschaefe $
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

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;

import org.jdesktop.swingx.validator.PatternValidator;

/**
 *
 */
public class ValidatingCellEditorTreeTest extends JFrame {
    /**
     * {@inheritDoc}
     */
    protected void frameInit() {
        super.frameInit();
        
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("root");
        root.add(new DefaultMutableTreeNode("a"));
        root.add(new DefaultMutableTreeNode("b"));
        root.add(new DefaultMutableTreeNode("c"));
        
        ValidatingTree tree = new ValidatingTree(root);
        tree.setValidator(new PatternValidator("..."));
        tree.setEditable(true);
        add(new JScrollPane(tree));
        
        pack();
    }
    
    /**
     * @param args
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new ValidatingCellEditorTreeTest().setVisible(true);
            }
        });
    }
}
