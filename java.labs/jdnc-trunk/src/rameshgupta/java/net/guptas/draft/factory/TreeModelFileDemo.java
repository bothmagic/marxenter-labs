/**
 * Copyright (C) 2005 by Ramesh Gupta. All rights reserved.
 */

package net.guptas.draft.factory;

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

                        MutableTreeModel  model = new DefaultMutableTreeModel(null);
                        MutableTreeNodeFactory factory = new FileMutableTreeNodeFactory(model);
                        model.setRoot(new DynamicMutableTreeNodeImpl(dir, dir, factory));
                        
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
}
