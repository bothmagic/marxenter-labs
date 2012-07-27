/*
 * $Id: RowProviderTest.java 3181 2009-07-01 19:51:04Z kschaefe $
 * 
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle, Santa Clara,
 * California 95054, U.S.A. All rights reserved.
 * 
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA
 */
package org.jdesktop.swingx.renderer;

import java.awt.BorderLayout;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

import org.jdesktop.swingx.JXComboBox;
import org.jdesktop.swingx.JXList;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.JXTree;

/**
 *
 */
@SuppressWarnings("serial")
public class RowProviderTest extends JFrame {
    private Object[] data;
    
    /**
     * {@inheritDoc}
     */
    protected void frameInit() {
        super.frameInit();
        
        setTitle("RowProvider Test");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        JComboBox combo = new JXComboBox(getData());
        combo.setRenderer(new DefaultListRenderer(new RowProvider(2)));
        add(combo, BorderLayout.NORTH);
        
        JSplitPane splitter = new JSplitPane();
        add(splitter);
        
        JXTree tree = new JXTree(getTreeData());
        tree.setCellRenderer(new DefaultTreeRenderer(new WrappingProvider(
                new RowProvider(2))));
        splitter.setLeftComponent(tree);
        
        JXList list = new JXList(getData());
        list.setCellRenderer(new DefaultListRenderer(new RowProvider(2)));
        splitter.setRightComponent(list);
        
        JXTable table = new JXTable(getTableData(), new String[] {"A", "B", "C", "D", "E"});
        table.getColumn(2).setCellRenderer(new DefaultTableRenderer(new RowProvider(2)));
        
        add(new JScrollPane(table), BorderLayout.SOUTH);
        
        pack();
    }
    
    private Object[] getData() {
        if (data == null) {
            data = new Object[] {
                new Integer[] {1, 2, },
                new Integer[] {3, 4, },
                new Integer[] {5, 6, },
                new Integer[] {7, 8, },
                new Integer[] {9, 10, },
            };
        }
        
        return data;
    }
    
    private TreeNode getTreeData() {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("root");
        
        for (Object o : getData()) {
            DefaultMutableTreeNode node = new DefaultMutableTreeNode(o);
            
            for (Object x : getData()) {
                node.add(new DefaultMutableTreeNode(x));
            }
            
            root.add(node);
        }
        
        return root;
    }
    
    private Object[][] getTableData() {
        Object[][] tableData = new Object[5][5];
        
        for (int i = 0; i < tableData.length; i++) {
            for (int j = 0; j < tableData[0].length; j++) {
                if (j == 2) {
                    tableData[i][j] = getData()[i];
                } else {
                    tableData[i][j] = i * j;
                }
            }
        }
        
        return tableData;
    }
    
    /**
     * Application entry point.
     * 
     * @param args
     *            unused
     */
    public static void main(String[] args) throws Exception {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new RowProviderTest().setVisible(true);
            }
        });
    }
}
