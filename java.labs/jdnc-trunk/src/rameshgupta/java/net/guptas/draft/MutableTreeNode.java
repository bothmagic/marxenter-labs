/**
 * Copyright (C) 2005 by Ramesh Gupta. All rights reserved.
 */

package net.guptas.draft;

import java.util.Enumeration;


public interface MutableTreeNode<N,X> extends javax.swing.tree.MutableTreeNode {
	public Enumeration<? extends MutableTreeNode> children();
	public MutableTreeNode getChildAt(int index);
	public MutableTreeNode getParent();
	public MutableTreeNode[] getPath();
	public N getAccessory();
	public X getUserData();
	public void setAccessory(N accessory);
	public void setUserData(X userData);
}
