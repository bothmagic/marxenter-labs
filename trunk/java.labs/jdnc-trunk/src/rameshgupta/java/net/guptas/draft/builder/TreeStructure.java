package net.guptas.draft.builder;


import java.io.File;
import net.guptas.draft.*;

/**
 * The Object Structure in a Visitor pattern; must know how to "traverse" the object
 * structure, which is assumed to be a tree composed of TreeNodes. The traverse method
 * calls the Visitor's visitNode(TreeNode current, int depth) for each node in the tree.
 * The particular order of the enumeration is not currently specified.
 *
 * @author Patrick Wright
 */
public interface TreeStructure {
    /** 
     * traverses all the elements in the tree, and calls the 
     * Visitor's visitNode(TreeNode current, int depth) for each one
     */
    void traverse(TreeNodeVisitor visitor);
}
