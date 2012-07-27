/*
 * Created on 26.10.2007
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
import java.util.List;

import javax.swing.Icon;
import javax.swing.filechooser.FileSystemView;

/**
 * FileNode - keeps sorted list of children. Used by from-scratch TreeTableModels.
 */
public class FileNode {
    protected static final FileNode[] EMPTY_FILES = new FileNode[0];
    
    private File file;
    private FileNode[] children;
    private FileNode parent;
    
    public FileNode() {
        this(null);
    }
    
    public FileNode(File file) {
       this(file, null); 
    }
    
    public FileNode(File file, FileNode parent) {
        this.file = file;
        this.parent = parent;
    }
    
    public FileNode getParent() {
        return parent;
    }
    
    public File getFile() {
        return file;
    }
    
    public FileNode[] getChildren() {
        if (children == null) {
            children = updateChildren();
        }
        return children;
    }
    public void sortChildren() {
        if (children == null) return;
        Arrays.sort(children, FileComparator.getInstance());
        
    }


    public String getDisplayName() {
        if (file != null) {
            return FileSystemView.getFileSystemView().getSystemDisplayName(file);
        }
        return "Unknown";
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
        for (int i = 0; i < children.length; i++) {
            children[i].updateFileFromParentNode();
        }
    }

    private void updateFileFromParentNode() {
        String name = file.getName();
        file = FileSystemView.getFileSystemView().getChild(parent.getFile(), name);
        updateFileOfChildren();
    }

    public Icon getFileIcon() {
        if (file != null) {
            return FileSystemView.getFileSystemView().getSystemIcon(file);
        }
        return null;
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
        return (file != null) && !FileSystemView.getFileSystemView().isTraversable(file).booleanValue();
    }
    
    public boolean isDrive() {
        return (file != null) && FileSystemView.getFileSystemView().isDrive(file);
    }

    protected FileNode[] updateChildren() {
        if (getFile() == null) {
            return EMPTY_FILES;
        }
        List<File> files = Arrays.asList(file.listFiles());
        if (files.size() == 0) {
            return EMPTY_FILES;
        }
        List<FileNode> fileNodes = new ArrayList<FileNode>();
        for (File file : files) {
            fileNodes.add(new FileNode(file, this));
        }
        Collections.sort(fileNodes, FileComparator.getInstance());
        return fileNodes.toArray(new FileNode[0]);
    }
    
    public static class RootFileNode extends FileNode {
        
        public RootFileNode() {
            updateChildren();
        }

        
        @Override
        public boolean isLeaf() {
            return false;
        }


        @Override
        protected FileNode[] updateChildren() {
            File[] roots = FileSystemView.getFileSystemView().getRoots();
            if (roots.length == 0) return EMPTY_FILES;
            List<FileNode> fileNodes = new ArrayList<FileNode>();
            for (int i = 0; i < roots.length; i++) {
                fileNodes.add(new FileNode(roots[i], this));
            }
            Collections.sort(fileNodes, FileComparator.getInstance());
            return fileNodes.toArray(new FileNode[0]);
        }
        
        
    }
    
    public static class FileComparator implements Comparator {

        Comparator collator = Collator.getInstance();
        private static final FileComparator instance = new FileComparator();
        
        public static FileComparator getInstance() {
            return instance;
        }
        
        public int compare(Object one, Object two) {
                return compare((FileNode) one, (FileNode) two);
        }

        private int compare(FileNode one, FileNode two) {
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

        private boolean comparableNames(FileNode one, FileNode two) {
                boolean isLeafOne = one.isLeaf();
                boolean isLeafTwo = two.isLeaf();
                return (isLeafOne && isLeafTwo) || (!isLeafOne && !isLeafTwo);
        }

        private int compareNames(FileNode one, FileNode two) {
                return collator.compare(one.getDisplayName(), two.getDisplayName());
        }


        private boolean ignoreSort(FileNode one, FileNode two) {
                return one.isDrive() && two.isDrive();
        }

        private boolean isDrive(File one) {
            return FileSystemView.getFileSystemView().isDrive(one);
        }
} // end inner class


}