/**
 * Copyright (C) 2005 by Ramesh Gupta. All rights reserved.
 */

package net.guptas.draft;

import javax.swing.tree.TreeModel;

public interface MutableTreeModel<N,X> extends TreeModel {
	public void setRoot(MutableTreeNode<N,X> newRoot);
	public MutableTreeNode<N,X> getRoot();
	public MutableTreeNode getChild(MutableTreeNode parent, int index);
	public int getChildCount(MutableTreeNode parent);
	public int getIndexOfChild(MutableTreeNode parent, MutableTreeNode child);
	public boolean isLeaf(MutableTreeNode node);
	public void add(MutableTreeNode child, MutableTreeNode parent, int index);
	public void remove(MutableTreeNode node);
	public void reload(MutableTreeNode node);
}
