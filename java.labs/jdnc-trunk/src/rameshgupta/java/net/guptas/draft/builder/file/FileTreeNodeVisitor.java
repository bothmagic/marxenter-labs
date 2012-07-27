package net.guptas.draft.builder.file;

import net.guptas.draft.builder.TreeNodeVisitor;

/**
 * A TreeNodeVisitor that with separate visit commands for files and directories,
 * respectively.
 *
 * @author Patrick Wright
 */
public interface FileTreeNodeVisitor extends TreeNodeVisitor {
    public abstract void visitFile(FileTreeNode fileNode, int depth);
    public abstract void visitDirectory(FileTreeNode fileNode, int depth); 
}
