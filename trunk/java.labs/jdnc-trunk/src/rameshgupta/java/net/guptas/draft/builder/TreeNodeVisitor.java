package net.guptas.draft.builder;


import net.guptas.draft.builder.TreeNode;

/**
 * A TreeNodeVisitor participates in the Visitor pattern, visiting nodes on some tree that is
 * being processed--TreeNodes in a TreeNodeStructure. Each node is a reference to the node in
 * the tree that is being visited--in a file system, each node might be a file or directory, for
 * example. Implementations determine what happens when the node is visited (print it out, add it to
 * a tree model, etc.).
 * @author Patrick Wright
 */
public interface TreeNodeVisitor {
    /**
     * Called when a TreeNode in a TreeStructure is visited.
     * @param current The TreeNode to visit.
     * @param depth The "depth" in the tree structure, zero-based. Can be useful in debugging or
     * some conditional operations.
     */
    void visitNode(TreeNode current, int depth);
}
