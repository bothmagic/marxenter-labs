/*
 * DynamicMutableTreeNodeImpl.java
 *
 * Created on August 31, 2005, 1:12 PM
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package net.guptas.draft.factory;

import java.util.Collections;
import java.util.Enumeration;
import net.guptas.draft.MutableTreeNode;

import net.guptas.draft.impl.DefaultMutableTreeNode;

/**
 *
 * @author patrick
 */
public class DynamicMutableTreeNodeImpl extends DefaultMutableTreeNode implements DynamicMutableTreeNode {
    private MutableTreeNodeFactory nodeFactory;
    
    /** Creates a new instance of DynamicMutableTreeNodeImpl */
    public DynamicMutableTreeNodeImpl(Object userData, Object accessory, MutableTreeNodeFactory factory) {
        super(userData, accessory);
        this.setNodeFactory(factory);
    }
    
    public void setNodeFactory(MutableTreeNodeFactory factory) {
        this.nodeFactory = factory;
    }
    
    public Enumeration children() {
        System.out.println("children()");
        return Collections.enumeration(nodeFactory.getChildren(this));
    }

    public DefaultMutableTreeNode getChildAt(int idx) {
        System.out.println("getChildAt(" + idx + ")");
        return (DefaultMutableTreeNode)nodeFactory.getChildren(this).get(idx);
    }
    
    public int getChildCount() {
        System.out.println("getChildCount() factory " + nodeFactory + " parent " + parent);
        System.out.println("   accessory: " + getAccessory());
        return nodeFactory.getChildren(this).size();
    }
    
    public boolean isLeaf() {
        System.out.println("isLeaf()");
        return ! nodeFactory.hasChildren(this);
    }

    public boolean getAllowsChildren() {
        System.out.println("getAllowsChildren()");
        return true;
    }
}
