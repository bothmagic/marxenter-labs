/*
 * Created on 25.10.2007
 *
 */
package org.jdesktop.swingx.table.treetable.file;

import java.io.File;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.Icon;
import javax.swing.filechooser.FileSystemView;
import javax.swing.tree.TreeNode;

import org.jdesktop.swingx.treetable.TreeTableNode;

/**
 * File node: keeps sorted list of children.
 * For usage in DefaultTTM - implement TreeTableNode from scratch.
 */
public class FileScratchTTNode implements TreeTableNode {
    @SuppressWarnings("unused")
    private static final Logger LOG = Logger.getLogger(FileScratchTTNode.class
            .getName());
    protected static final FileScratchTTNode[] EMPTY_FILES = new FileScratchTTNode[0];
    
    private File file;
    private List<FileScratchTTNode> children;
    private FileScratchTTNode parent;
    
    public FileScratchTTNode() {
        this(null);
    }
    
    public FileScratchTTNode(File file) {
       this(file, null); 
    }
    
    public FileScratchTTNode(File file, FileScratchTTNode parent) {
        this.file = file;
        this.parent = parent;
    }

    public File getFile() {
        return file;
    }
    
    public List<FileScratchTTNode> getChildren() {
        if (children == null) {
            children = updateChildrenList();
        }
        return children;
    }
    
    public void sortChildren() {
        if (children == null) return;
        Collections.sort(children, FileComparator.getInstance());
    }


    public String getDisplayName() {
        if (file != null) {
            return FileSystemView.getFileSystemView().getSystemDisplayName(file);
        }
        return "Unknown";
    }

    public Icon getFileIcon() {
        if (file != null) {
            return FileSystemView.getFileSystemView().getSystemIcon(file);
        }
        return null;
    }
    
    public void invalidateChildren() {
        children = null;
    }
    /**
     * @param value
     * @param file
     */
    public void renameFile(Object value) {
        File parent = file.getParentFile();
        String name = String.valueOf(value);
        File newFile = new File(parent, name);
        file.renameTo(newFile);
        // reset the node's file reference
        file = FileSystemView.getFileSystemView().getChild(parent, name);
        updateFileOfChildren();
    }

    /**
     * @param parent
     * @param name
     */
    private void updateFileOfChildren() {
        if (children == null) return;
        for (int i = 0; i < children.size(); i++) {
            children.get(i).updateFileFromParentNode();
        }
    }

    private void updateFileFromParentNode() {
        String name = file.getName();
        file = FileSystemView.getFileSystemView().getChild(parent.getFile(), name);
        updateFileOfChildren();
    }

    
    public Date getLastChanged() {
        if ((file != null) && (!FileSystemView.getFileSystemView().isFloppyDrive(file))) {
            return new Date(file.lastModified());
        }
        return null;
    }
    
    public Integer getFileSize() {
        if ((file != null) && 
                !FileSystemView.getFileSystemView().isFloppyDrive(file) &&
                (file.isFile())) { 
            return new Integer((int) file.length());
        }
        return null;
    }
    
    public boolean isLeaf() {
        return (file != null) && !FileSystemView.getFileSystemView().isTraversable(file);
    }
    
    public boolean isDrive() {
        return (file != null) && FileSystemView.getFileSystemView().isDrive(file);
    }

    
    protected List<FileScratchTTNode> updateChildrenList() {
        if (getFile() == null) {
            return Collections.emptyList();
        }
//        List<File> files = Arrays.asList(file.listFiles());
        List<File> files = Arrays.asList(FileSystemView.getFileSystemView().getFiles(getFile(), true));
        if (files.size() == 0) {
            return Collections.emptyList();
        }
        List<FileScratchTTNode> fileNodes = new ArrayList<FileScratchTTNode>();
        for (File file : files) {
            fileNodes.add(new FileScratchTTNode(file, this));
        }
        Collections.sort(fileNodes, FileComparator.getInstance());
        return fileNodes;
    }
    
    //----------------- implement TreeTableNode
    
    public int getColumnCount() {
        return 3;
    }
    
    public Object getValueAt(int column) {
        switch(column) {
        case 0:
            return getUserObject();
        case 1:
            return getFileSize();
        case 2:
            return getLastChanged();
        }
        return null;
    }
    
    public boolean isEditable(int column) {
        return (column == 0) && !isDrive();
    }

    public void setValueAt(Object aValue, int column) {
        // TODO Auto-generated method stub
    }
    
    public Object getUserObject() {
        return getFile();
    }
    
    public void setUserObject(Object userObject) {
        // do nothing
    }
    
    //------------ implement TreeNode
    
    public FileScratchTTNode getParent() {
        return parent;
    }
    public Enumeration<? extends TreeTableNode> children() {
        return Collections.enumeration(updateChildrenList());
    }

    public TreeTableNode getChildAt(int childIndex) {
        return getChildren().get(childIndex);
    }
    
    public boolean getAllowsChildren() {
        // TODO Auto-generated method stub
        return false;
    }

    public int getChildCount() {
        return getChildren().size();
    }

    public int getIndex(TreeNode node) {
        return getChildren().indexOf(node);
    }
    
    public static class RootFileNode extends FileScratchTTNode {
        
        public RootFileNode() {
            updateChildrenList();
        }

        
        @Override
        public boolean isLeaf() {
            return false;
        }

        
        @Override
        protected List<FileScratchTTNode> updateChildrenList() {
            File[] roots = FileSystemView.getFileSystemView().getRoots();
            if (roots.length == 0) return Collections.emptyList();
            List<FileScratchTTNode> fileNodes = new ArrayList<FileScratchTTNode>();
            for (int i = 0; i < roots.length; i++) {
                fileNodes.add(new FileScratchTTNode(roots[i], this));
            }
            Collections.sort(fileNodes, FileComparator.getInstance());
            return fileNodes;
        }
        
    }
    
    public static class FileComparator implements Comparator {

        Comparator collator = Collator.getInstance();
        private static final FileComparator instance = new FileComparator();
        
        public static FileComparator getInstance() {
            return instance;
        }
        
        public int compare(Object one, Object two) {
                return compare((FileScratchTTNode) one, (FileScratchTTNode) two);
        }

        private int compare(FileScratchTTNode one, FileScratchTTNode two) {
                if (one.equals(two)) return 0;
                if (ignoreSort(one, two)) return 0;
                if (comparableNames(one, two)) {
                        return compareNames(one, two);
                }
                if (two.isLeaf()) {
                        return -1;
                }
                return 1;
        }

        private boolean comparableNames(FileScratchTTNode one, FileScratchTTNode two) {
                boolean isLeafOne = one.isLeaf();
                boolean isLeafTwo = two.isLeaf();
                return (isLeafOne && isLeafTwo) || (!isLeafOne && !isLeafTwo);
        }

        private int compareNames(FileScratchTTNode one, FileScratchTTNode two) {
                return collator.compare(one.getDisplayName(), two.getDisplayName());
        }


        private boolean ignoreSort(FileScratchTTNode one, FileScratchTTNode two) {
                return one.isDrive() && two.isDrive();
        }

} // end inner class

}