/*
 * $Id: DOMTreeTableModel.java 2828 2008-10-22 19:33:51Z kschaefe $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */
package org.jdesktop.swingx.treetable;

import java.util.ArrayList;
import java.util.List;

import javax.swing.tree.TreePath;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * A {@code TreeTableModel} for representing DOM-based trees.
 * 
 * @author Karl Schaefer
 */
public class DOMTreeTableModel extends AbstractTreeTableModel {
    /**
     * Creates a {@code TreeTableModel} that models a DOM {@code Document}.
     * 
     * @param doc
     *            the document to model
     */
    public DOMTreeTableModel(Document doc) {
        super(doc);
    }
    
    protected Node[] getPathToRoot(Node node) {
        List<Node> nodes = new ArrayList<Node>();
        Node n = node;
        
        while (n != root) {
            nodes.add(0, n);
            
            n = n.getParentNode();
        }
        
        if (n == root) {
            nodes.add(0, n);
        }
        
        return nodes.toArray(new Node[0]);
    }
    
    protected boolean isValidNode(Object node) {
        boolean result = false;
        
        if (node instanceof Node) {
            Node n = (Node) node;
            
            while (!result && n != null) {
                result = n == root;
                
                //we'll assume the attr is part of the document
                if (n instanceof Attr) {
                    n = ((Attr) n).getOwnerElement();
                } else {
                    n = n.getParentNode();
                }
            }
        }
        
        return result;
    }
    
    public Document getRoot() {
        return (Document) root;
    }
    
    public void setRoot(Document doc) {
        root = doc;
        
        modelSupport.fireNewRoot();
    }
    
    /**
     * {@inheritDoc}
     */
    public int getColumnCount() {
        return 1;
    }

    /**
     * {@inheritDoc}
     */
    public Object getValueAt(Object node, int column) {
        if (!isValidNode(node)) {
            throw new IllegalArgumentException("node is not a node governed by this model");
        }
        
        return ((Node) node).getNodeName();
    }

    /**
     * {@inheritDoc}
     */
    public Object getChild(Object parent, int index) {
        if (!isValidNode(parent)) {
            throw new IllegalArgumentException("parent is not a node governed by this model");
        }
        
        return ((Node) parent).getChildNodes().item(index);
    }

    /**
     * {@inheritDoc}
     */
    public int getChildCount(Object parent) {
        if (!isValidNode(parent)) {
            throw new IllegalArgumentException("parent is not a node governed by this model");
        }
        
        return ((Node) parent).getChildNodes().getLength();
    }

    /**
     * {@inheritDoc}
     */
    public int getIndexOfChild(Object parent, Object child) {
        if (!isValidNode(parent)) {
            throw new IllegalArgumentException("parent is not a node governed by this model");
        }
        
        if (!isValidNode(child)) {
            throw new IllegalArgumentException("child is not a node governed by this model");
        }
        
        NodeList list = ((Node) parent).getChildNodes();
        
        for (int i = 0, len = list.getLength(); i < len; i++) {
            Node n = list.item(i);
            
            if (n.isSameNode((Node) child)) {
                return i;
            }
        }
        
        return -1;
    }
    
    public void insertNode(Node parent, Node child, int index) {
        if (!isValidNode(parent)) {
            throw new IllegalArgumentException("node is not a node governed by this model");
        }
        
        //do not check child, since it may be new to this model
        //check it's current parent and remove it
        Node n = child.getParentNode();
        
        if (n != null) {
            if (isValidNode(n)) {
                removeNode(child);
            } else {
                n.removeChild(child);
            }
        }
        
        Node current = (Node) getChild(parent, index);
        parent.insertBefore(child, current);
        
        modelSupport.fireChildAdded(new TreePath(getPathToRoot(parent)), index, child);
    }
    
    public void removeNode(Node node) {
        if (!isValidNode(node)) {
            throw new IllegalArgumentException("node is not a node governed by this model");
        }
        
        if (node instanceof Attr) {
            throw new IllegalArgumentException("cannot remove attribute");
        }
        
        if (node instanceof Document) {
            throw new IllegalArgumentException("cannot remove document");
        }
        
        Node n = (Node) node;
        Node parent = n.getParentNode();
        int index = getIndexOfChild(parent, n);
        
        parent.removeChild(n);
        
        modelSupport.fireChildRemoved(new TreePath(getPathToRoot(parent)), index, n);
    }
}
