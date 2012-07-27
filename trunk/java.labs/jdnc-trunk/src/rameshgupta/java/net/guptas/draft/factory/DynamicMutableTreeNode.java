/*
 * DynamicMutableTreeNode.java
 *
 * Created on August 31, 2005, 1:11 PM
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package net.guptas.draft.factory;

import net.guptas.draft.MutableTreeNode;

/**
 *
 * @author patrick
 */
public interface DynamicMutableTreeNode extends MutableTreeNode {
    void setNodeFactory(MutableTreeNodeFactory factory);
}
