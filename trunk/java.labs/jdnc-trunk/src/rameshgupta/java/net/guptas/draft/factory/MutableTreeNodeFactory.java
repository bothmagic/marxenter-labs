package net.guptas.draft.factory;

import java.util.List;
import net.guptas.draft.*;


/**
 * A MutableTreeNodeFactory is given to a MutableTreeNode so it can "build"
 * its children. The factory provides child nodes at any level of the tree, but
 * as necessary can install a new (and different) factory for its children to
 * accomodate nodes by type. The factory supports deferred expansion, but must be
 * able to answer hasChildren() at any time, accurately; though it may do this by
 * "peeking" at its underlying data source instead of actually building the child nodes.
 * If isExpandDeferred() returns true, a MutableTreeNode should not call getChildren()
 * until/unless expansion 0of the child nodes is actually needed.
 *
 * A MutableTreeNodeFactory also supports a MutableTreeNodeFilter, which allows
 * the underlying data source to be according to the user data for the node.
 *
 * Last, the factory also has a separate factory method for creating MutableTreeNode
 * instances for its children. This allows one to plug in different MutableTreeNode instances
 * as necessary at different levels of the tree.
 *
 * In order to reuse a single factory across multiple nodes in a tree, the various
 * methods in the factory take a MutableTreeNode as an argument. If unique factories
 * are associated with each node, the parent node can be stored in the implmenting class
 * and passed in.
 */
public interface MutableTreeNodeFactory {
    /** Returns true if this factory will return any child nodes for the given parent. */
    boolean hasChildren(MutableTreeNode parent);
    
    /**
     * Returns a List of MutableTreeNode children for the given parent. After this method
     * is called for the first time, isExpanded() will return true.
     */
    List<MutableTreeNode> getChildren(MutableTreeNode parent);
    
    /**
     * Returns true if this factory will defer node expansion until getChildren() is
     * first called.
     */
    boolean isExpandDeferred();
    
    /**
     * Associates a MutableTreeNodeFilter with this factory. By convention, the filter's
     * acceptUserData() will be called once for each potential candidate child node;
     * user data for which false is returned will not result in child nodes returned
     * by this factory.
     */
    void setFilter(MutableTreeNodeFilter filter);
    
    /**
     * Returns the current filter for this factory.
     */
    MutableTreeNodeFilter getFilter(MutableTreeNodeFilter filter);
    
    /**
     * Controls whether the factory generates nodes for given user data.
     */
    interface MutableTreeNodeFilter {
        /**
         * Returns true if the user data meets criteria for inclusion as
         * a child node
         */
        boolean acceptUserData(Object userData);
    }
    
    /**
     * Factory method to generate new MutableTreeNode instances when the factory
     * is building the child list. Can be overridden in subclasses to control
     * instantiation.
     */
    <N,X> MutableTreeNode<N,X> createTreeNode(MutableTreeNode parent, N accessory, X userData, int idx);
}



/*
 * Usage notes:
 * An MTNF is provided to a MutableTreeNode on construction.
 * File rootDir = new File("\temp");
 * FileTreeNodeFactory factory = new FileTreeNodeFactory(rootDir);
 * MutableTreeNode root = new DefaultMutableTreeNodeImpl( userData, acc, factory );
 *
 * if the MutableTreeNode has a factory, it queries the factory for its nodes;
 * if expand is deferred, it waits until it either needs the child count or until
 * expansion takes place before the request
 */