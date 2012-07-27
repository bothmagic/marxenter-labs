package net.guptas.draft.builder.file;


import net.guptas.draft.builder.TreeNode;
import net.guptas.draft.builder.TreeNodeVisitor;



/**
 * A Visitor over FileTreeNodes that delegates to visitFile() and visitDirectory().
 *
 * @author Patrick Wright
 */
public abstract class AbstractFileTreeNodeVisitor implements FileTreeNodeVisitor {
    public void visitNode(TreeNode node, int depth) {
        assert node instanceof FileTreeNode;
        
        FileTreeNode fileNode = (FileTreeNode)node;
        if ( fileNode.getFile().isFile()) 
            visitFile(fileNode, depth);
        else 
            visitDirectory(fileNode, depth);
    }
    
    public abstract void visitFile(FileTreeNode fileNode, int depth);
    public abstract void visitDirectory(FileTreeNode fileNode, int depth);
}