/*
 * $Id: TreeEditing.java 2986 2009-01-22 16:41:28Z kleopatra $
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
package org.jdesktop.swingx.tree;

import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.event.ActionEvent;
import java.net.URL;
import java.util.Date;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellEditor;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeModel;

import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXTree;
import org.jdesktop.swingx.incubatorutil.InteractiveTestCase;
import org.jdesktop.swingx.renderer.DefaultTreeRenderer;
import org.jdesktop.swingx.table.DatePickerCellEditor;
import org.jdesktop.swingx.treetable.FileSystemModel;


public class TreeEditing extends InteractiveTestCase {
    private Icon alternativeLeafIcon;
    
    public void interactiveTreeSelection() {
        JXTree tree = new JXTree(new FileSystemModel());
        tree.setCellRenderer(new DefaultTreeRenderer());
        showWithScrollingInFrame(tree, "tree selection");
    }
    
    
    
    public class DynamicIconRenderer extends DefaultTreeCellRenderer {

        
        @Override
        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
            super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf,
                    row, hasFocus);
            if (leaf) {
                setAlternativeIcon();
            }
            return this;
        }

        private void setAlternativeIcon() {
            String rep = String.valueOf(getText());
            if (rep.startsWith("b")) {
                setIcon(getAlternativeIcon());
            }
            
        }


    }

    private Icon getAlternativeIcon() {
        return alternativeLeafIcon;
    }
    

    /**
     * visualize editing of the hierarchical column, both
     * in a tree and a xTree switching CO.
     *
     * standard editor
     */
    public void interactiveTreeEditingRToL() {
        JTree tree =  new JTree(); 
        TreeCellRenderer renderer = new DynamicIconRenderer();
        tree.setCellRenderer(renderer);
        tree.setEditable(true);
        JXTree xTree = new JXTree();
        xTree.setCellRenderer(renderer);
        xTree.setEditable(true);
        final JXFrame frame = wrapWithScrollingInFrame(tree, xTree, "standard Editing: compare tree and xtree");
        Action toggleComponentOrientation = new AbstractAction("toggle orientation") {

            public void actionPerformed(ActionEvent e) {
                ComponentOrientation current = frame.getComponentOrientation();
                if (current == ComponentOrientation.LEFT_TO_RIGHT) {
                    frame.applyComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
                } else {
                    frame.applyComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);

                }

            }

        };
        addAction(frame, toggleComponentOrientation);
        frame.setVisible(true);
        
    }

    /**
     * visualize editing of the hierarchical column, both
     * in a tree and a xTree switching CO.
     * using DefaultXTreeCellEditor.
     */
    public void interactiveXTreeEditingRToL() {
        JTree tree =  new JTree(); 
        tree.setEditable(true);
        DefaultTreeCellRenderer renderer = new DynamicIconRenderer();
        tree.setCellRenderer(renderer);
        tree.setCellEditor(new DefaultXTreeCellEditor(tree, renderer));
        JXTree xTree = new JXTree();
        xTree.setEditable(true);
        xTree.setCellRenderer(renderer);
        xTree.setCellEditor(new DefaultXTreeCellEditor(xTree, renderer));
        final JXFrame frame = wrapWithScrollingInFrame(tree, xTree, "XEditing: compare tree and xtree");
        Action toggleComponentOrientation = new AbstractAction("toggle orientation") {

            public void actionPerformed(ActionEvent e) {
                ComponentOrientation current = frame.getComponentOrientation();
                if (current == ComponentOrientation.LEFT_TO_RIGHT) {
                    frame.applyComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
                } else {
                    frame.applyComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);

                }

            }

        };
        addAction(frame, toggleComponentOrientation);
        frame.setVisible(true);
        
    }


    /**
     * visualize editing of the hierarchical column, both
     * in a tree and a xTree switching CO.
     * using DefaultXXTreeCellEditor.
     */
    public void interactiveTreeXXEditingRToL() {
        JTree tree =  new JTree(); 
        tree.setEditable(true);
        DefaultTreeCellRenderer renderer = new DynamicIconRenderer();
        tree.setCellRenderer(renderer);
        DefaultTreeEditor treeCellEditor = new DefaultTreeEditor();
        tree.setCellEditor(treeCellEditor);
        JXTree xTree = new JXTree();
        xTree.setCellRenderer(renderer);
        xTree.setEditable(true);
        xTree.setCellEditor(new DefaultTreeEditor());

        final JXFrame frame = wrapWithScrollingInFrame(tree, xTree, "XXEditing: compare tree and xtree");
        Action toggleComponentOrientation = new AbstractAction("toggle orientation") {

            public void actionPerformed(ActionEvent e) {
                ComponentOrientation current = frame.getComponentOrientation();
                if (current == ComponentOrientation.LEFT_TO_RIGHT) {
                    frame.applyComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
                } else {
                    frame.applyComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);

                }

            }

        };
        addAction(frame, toggleComponentOrientation);
        frame.setVisible(true);
        
    }

    /**
     * visualize editing of the hierarchical column, both
     * in a tree and a xTree switching CO.
     *
     * standard editor
     */
    public void interactiveTreeEditingRToLDatePicker() {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode(new Date());
        root.add(new DefaultMutableTreeNode(new Date()));
        TreeModel model = new DefaultTreeModel(root);
        JTree tree =  new JTree(model); 
        DefaultTreeCellRenderer renderer = new DynamicIconRenderer();
        tree.setCellRenderer(renderer);
        tree.setEditable(true);
        tree.setCellEditor(new DefaultTreeCellEditor(tree, renderer, new DatePickerCellEditor()));
        JXTree xTree = new JXTree(model);
        xTree.setCellRenderer(renderer);
        xTree.setEditable(true);
        xTree.setCellEditor(new DefaultTreeCellEditor(tree, renderer, new DatePickerCellEditor()));
        final JXFrame frame = wrapWithScrollingInFrame(tree, xTree, "standard Editing (DatePicker): compare tree and xtree");
        Action toggleComponentOrientation = new AbstractAction("toggle orientation") {

            public void actionPerformed(ActionEvent e) {
                ComponentOrientation current = frame.getComponentOrientation();
                if (current == ComponentOrientation.LEFT_TO_RIGHT) {
                    frame.applyComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
                } else {
                    frame.applyComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);

                }

            }

        };
        addAction(frame, toggleComponentOrientation);
        frame.setVisible(true);
        
    }

    /**
     * visualize editing of the hierarchical column, both
     * in a tree and a xTree switching CO.
     * using DefaultXTreeCellEditor.
     */
    public void interactiveXTreeEditingRToLDatePicker() {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode(new Date());
        root.add(new DefaultMutableTreeNode(new Date()));
        TreeModel model = new DefaultTreeModel(root);
        JTree tree =  new JTree(model); 
        tree.setEditable(true);
        DefaultTreeCellRenderer renderer = new DynamicIconRenderer();
        tree.setCellRenderer(renderer);
        tree.setCellEditor(new DefaultXTreeCellEditor(tree, renderer, new DatePickerCellEditor()));
        JXTree xTree = new JXTree(model);
        xTree.setEditable(true);
        xTree.setCellRenderer(renderer);
        xTree.setCellEditor(new DefaultXTreeCellEditor(xTree, renderer, new DatePickerCellEditor()));
        final JXFrame frame = wrapWithScrollingInFrame(tree, xTree, "XEditing(DatePicker): compare tree and xtree");
        Action toggleComponentOrientation = new AbstractAction("toggle orientation") {

            public void actionPerformed(ActionEvent e) {
                ComponentOrientation current = frame.getComponentOrientation();
                if (current == ComponentOrientation.LEFT_TO_RIGHT) {
                    frame.applyComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
                } else {
                    frame.applyComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);

                }

            }

        };
        addAction(frame, toggleComponentOrientation);
        frame.setVisible(true);
        
    }


    /**
     * visualize editing of the hierarchical column, both
     * in a tree and a xTree switching CO.
     * using DefaultXXTreeCellEditor and datePicker.
     */
    public void interactiveTreeXXEditingRToLDatePicker() {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode(new Date());
        root.add(new DefaultMutableTreeNode(new Date()));
        TreeModel model = new DefaultTreeModel(root);
        JTree tree =  new JTree(model); 
        tree.setEditable(true);
        DefaultTreeCellRenderer renderer = new DynamicIconRenderer();
        tree.setCellRenderer(renderer);
        DefaultTreeEditor treeCellEditor = new DefaultTreeEditor(new DatePickerCellEditor());
        tree.setCellEditor(treeCellEditor);
        JXTree xTree = new JXTree(model);
        xTree.setCellRenderer(renderer);
        xTree.setEditable(true);
        xTree.setCellEditor(new DefaultTreeEditor(new DatePickerCellEditor()));

        final JXFrame frame = wrapWithScrollingInFrame(tree, xTree, "XXEditing(DatePicker): compare tree and xtree");
        Action toggleComponentOrientation = new AbstractAction("toggle orientation") {

            public void actionPerformed(ActionEvent e) {
                ComponentOrientation current = frame.getComponentOrientation();
                if (current == ComponentOrientation.LEFT_TO_RIGHT) {
                    frame.applyComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
                } else {
                    frame.applyComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);

                }

            }

        };
        addAction(frame, toggleComponentOrientation);
        frame.setVisible(true);
        
    }
    @Override
    protected void setUp() {
        URL url = getClass().getResource("wellBottom.gif");
        if (url != null) {
            alternativeLeafIcon = new ImageIcon(url);
        }
    }


    public static void main(String[] args) {
//        setSystemLF(true);
        TreeEditing test = new TreeEditing();
        try {
            test.runInteractiveTests();
//            test.runInteractiveTests("interactive.*TreeXX.*");
        } catch (Exception e) {
            System.err.println("exception when executing interactive tests:");
            e.printStackTrace();
        }
    }

}
