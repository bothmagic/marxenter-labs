package net.guptas.draft.builder.file;

import java.io.File;
import java.io.FileFilter;
import java.util.HashMap;
import java.util.Map;
import net.guptas.draft.MutableTreeModel;
import net.guptas.draft.MutableTreeNode;
import net.guptas.draft.builder.AbstractTreeNodeBuilder;
import net.guptas.draft.builder.TreeNode;


/**
 * A Visitor that builds MutableTreeNodes for a MutableTreeModel as it
 * visits FileTreeNodes.
 *
 * @author patrick
 */
public class DirectoryTNBuilder extends AbstractTreeNodeBuilder implements FileTreeNodeVisitor {
    private Map nodeMap;
    private File rootDir;
    private FileFilter fileFilter;
    
    private static final FileFilter ALL_FILES = new FileFilter() {
        public boolean accept(File f) { return true; }
    };
    
    public DirectoryTNBuilder(MutableTreeModel tree) {
        this(tree, ALL_FILES);
    }
    
    public DirectoryTNBuilder(MutableTreeModel tree, FileFilter filter) {
        super(tree);
        this.rootDir = (File)tree.getRoot().getAccessory();
        this.fileFilter = ALL_FILES;
        this.nodeMap = new HashMap();
        this.nodeMap.put(rootDir.getAbsoluteFile(), treeModel.getRoot());
    }
    
    public void visitNode(TreeNode node, int depth) {
        throw new RuntimeException("Should not reach here");        
    }
    
    public void visitFile(FileTreeNode fileNode, int depth) {
        if (! isRoot(fileNode)) {
            addNewNode(fileNode);
        }
    }
    
    public void visitDirectory(FileTreeNode fileNode, int depth) {
        if (! isRoot(fileNode)) {
            nodeMap.put(fileNode.getFile().getAbsoluteFile(), addNewNode(fileNode));
        }
    }
    
    private boolean isRoot(FileTreeNode fileNode) {
        return fileNode.getFile().getAbsolutePath().equals(rootDir.getAbsolutePath());
    }
    
    private MutableTreeNode addNewNode(FileTreeNode fileNode) {
        MutableTreeNode mtn = (MutableTreeNode)nodeMap.get(fileNode.getFile().getParentFile().getAbsoluteFile());
        return add(fileNode.getFile(), null, mtn, treeModel);
    }
}