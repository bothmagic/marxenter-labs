/*
 * FileMutableTreeNodeFactory.java
 *
 * Created on August 31, 2005, 12:24 PM
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package net.guptas.draft.factory;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;
import net.guptas.draft.MutableTreeModel;
import net.guptas.draft.MutableTreeNode;
import net.guptas.draft.factory.MutableTreeNodeFactory.MutableTreeNodeFilter;



/**
 * A MutableTreeNodeFactory that builds MutableTreeNodes for a directory.
 * The parent MutableTreeNode must have a File as accessory for this to
 * work; the factory builds child MutableTreeNodes based on this file.
 * One can provide a FileFilter to restrict which Files are added as children.
 *
 * @author Patrick Wright
 */
public class FileMutableTreeNodeFactory implements MutableTreeNodeFactory {
    private MutableTreeNodeFilter nodeFilter;
    private FileFilter fileFilter;
    private List<MutableTreeNode> children;
    private MutableTreeModel treeModel;
    private List expandedList;
    private static final Object lock = new Object();
    
    private static final FileFilter ALL_FILE_FILTER = new FileFilter() {
        public boolean accept(File path) { return true; }
    };
    
    /** Creates a new instance of FileMutableTreeNodeFactory */
    public FileMutableTreeNodeFactory(MutableTreeModel treeModel) {
        this(treeModel, ALL_FILE_FILTER);
    }
    
    public FileMutableTreeNodeFactory(MutableTreeModel treeModel, FileFilter filter) {
        this.treeModel = treeModel;
        this.nodeFilter = new FileFilterFilter(filter);
        this.fileFilter = ALL_FILE_FILTER;
        this.children = new ArrayList();
        this.expandedList = new ArrayList();
    }
    
    public <N, X>MutableTreeNode<N, X> createTreeNode(MutableTreeNode parent, N accessory, X userData, int idx) {
        DynamicMutableTreeNode newNode = new DynamicMutableTreeNodeImpl(accessory, userData, this);
        treeModel.add(newNode, parent, idx);
        return newNode;
    }
    
    public java.util.List<MutableTreeNode> getChildren(MutableTreeNode node) {
        List rtn = null;
        synchronized (lock) {
            if ( ! expandedList.contains(node) ) {
                File parentDir = (File)node.getAccessory();
                File[] files = parentDir.listFiles(fileFilter);
                int cnt = 0;
                System.out.println("parent " + parentDir.getName());
                for ( File file : files ) {
                    System.out.println("  adding " + file.getName());
                    MutableTreeNode newNode = createTreeNode(node, file, file, cnt++);
                    children.add(newNode);
                }
                expandedList.add(node);
            }
        }
        return children;
    }
    
    public MutableTreeNodeFilter getFilter(MutableTreeNodeFilter filter) {
        return filter;
    }
    
    public boolean hasChildren(MutableTreeNode node) {
        File file = (File)node.getAccessory();
        System.out.println("  hasChildren(): parent File " + file);
        if ( file == null || file.isFile())
            return false;
        else {
            int cnt = file.listFiles(fileFilter).length;
            System.out.println("    children for " + file + ": " + cnt);
            return cnt > 0;
        }
    }
    
    public boolean isExpandDeferred() {
        return true;
    }
    
    public void setFilter(MutableTreeNodeFilter filter) {
        this.nodeFilter = filter;
    }
    
    private class AllFilesFilter implements MutableTreeNodeFilter {
        public boolean acceptUserData(Object userData) {
            return true;
        }
    }
    
    private class FileFilterFilter implements MutableTreeNodeFilter {
        private FileFilter fileFilter;
        private FileFilterFilter(FileFilter filter) {
            this.fileFilter = filter;
        }
        
        public boolean acceptUserData(Object userData) {
            return userData instanceof File &&
                    fileFilter.accept((File)userData);
        }
    }
}
