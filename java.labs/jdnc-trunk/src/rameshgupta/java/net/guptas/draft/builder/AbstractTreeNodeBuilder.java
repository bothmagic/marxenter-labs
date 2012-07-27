package net.guptas.draft.builder;


import net.guptas.draft.MutableTreeModel;
import net.guptas.draft.MutableTreeNode;
import net.guptas.draft.impl.DefaultMutableTreeNode;    // use in factory method


  
/**
 * A Visitor that adds MutableTreeNodes for a MutableTreeModel as it
 * visits TreeNodes. Subclasses must implement the visitNode() method to determine
 * what is added to the tree.
 *
 * @author Patrick Wright
 */
public abstract class AbstractTreeNodeBuilder implements TreeNodeVisitor {
    /** The TreeModel we are adding to */
    protected MutableTreeModel treeModel;
    
    public AbstractTreeNodeBuilder(MutableTreeModel tree) {
        this.treeModel = tree;
    }
    
    /** Implement in subclass */
    public abstract void visitNode(TreeNode node, int depth);
    
    /** Utility method, adds a single MutableTreeNode to the TreeModel */
    protected <N,X> MutableTreeNode<N,X> add(
            N accessory,
            X userData,
            MutableTreeNode parent,
            MutableTreeModel tree) {
        
        MutableTreeNode<N,X>	newNode = createTreeNode(accessory, userData);
        tree.add(newNode, parent, parent.getChildCount());
        return newNode;
    }
    
    /** 
     * Utility method, creates a single TreeNode. This creates a DefaultMutableTreeNode;
     * subclasses can provide their own.
     */
    protected <N,X> MutableTreeNode<N,X> createTreeNode(N accessory, X userData) {
        return new DefaultMutableTreeNode<N,X>(accessory, userData);
    }
}