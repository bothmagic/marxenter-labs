/**
 * Copyright (C) 2005 by Ramesh Gupta. All rights reserved.
 */

package net.guptas.draft.builder.file;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileFilter;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;

import net.guptas.draft.MutableTreeModel;
import net.guptas.draft.MutableTreeNode;
import net.guptas.draft.impl.DefaultMutableTreeModel;	// use in factory method
import net.guptas.draft.impl.DefaultMutableTreeNode;	// use in factory method


/**
 * A variation on Ramesh Gupta's TreeModelDemo that shows how to use the Visitor
 * pattern to construct a MutableTreeModel made of MutableTreeNodes for a file structure.
 *
 * @author Ramesh Gupta
 * @author Patrick Wright
 */
public class TreeModelFileDemo {
    private static File rootDirectory;
    
    /**
     * Main entry point constructs and displays a sample tree in which the root
     * node contains information for the JTree class (for no particular reason).
     *
     * @param args String[]
     */
    public static void main(String[] args) {
        new TreeModelFileDemo().runDemo();
    }
    
    private void runDemo() {
        final FileFilter filter = new FileFilter() {
            public boolean accept(File file) {
                return file.isFile() || ( file.isDirectory() && ! file.getName().endsWith("CVS"));
            }
        };
        
        final JFrame  frame = new JFrame("Generic Tree Model");
        final JTree jTree = new JTree();
        
        JButton selection = new JButton(new AbstractAction("Select") {
            public void actionPerformed(ActionEvent e) {
                
                //Create a file chooser
                final JFileChooser fc = new JFileChooser();
                fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                
                //In response to a button click:
                while (true) {
                    int returnVal = fc.showOpenDialog(frame);
                    if ( returnVal == JFileChooser.APPROVE_OPTION) {
                        File dir = fc.getSelectedFile();
                        
                        FileTreeStructure d = new FileTreeStructure(dir, filter);
                        MutableTreeModel<File,?>	model = createTreeModel(dir, null);
                        DirectoryTNBuilder builder = new DirectoryTNBuilder(model);
                        d.traverse(builder);
                        
                        jTree.setModel(model);
                        break;
                    } else {
                        System.exit(-1);
                    }
                }
            }
        });
        
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));
        panel.add(selection);
        frame.add(panel, BorderLayout.NORTH);
        frame.add(new JScrollPane(jTree), BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setSize(600, 600);
        frame.setVisible(true);
        
        
    }
    
    /**
     * Factory method constructs a MutableTreeModel whose root node is
     * accessorized with the specified accessory and user data objects.
     * This the only method that refers directly to the model implementation class.
     *
     * @param accessory N arbitrary accessory object for the root node
     * @param userData X arbitrary user data object for the root node
     * @return MutableTreeModel the newly created model
     */
    private static <N,X> MutableTreeModel<N,X> createTreeModel(N accessory, X userData) {
        return new DefaultMutableTreeModel<N,X>(createTreeNode(accessory, userData));
    }
    
    /**
     * Factory method constructs a MutableTreeNode that is accessorized with the
     * specified accessory and user data objects.
     * This the only method that refers directly to the node implementation class.
     *
     * @param accessory N arbitrary accessory object for the new node
     * @param userData X arbitrary user data object for the new node
     * @return MutableTreeNode the newly created node
     */
    private static <N,X> MutableTreeNode<N,X> createTreeNode(N accessory, X userData) {
        return new DefaultMutableTreeNode<N,X>(accessory, userData);
    }
    
}
