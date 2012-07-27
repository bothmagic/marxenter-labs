/*
 * $Id: DelegatingListModel.java 3184 2009-07-02 13:19:42Z kschaefe $
 *
 * Copyright 2008 Sun Microsystems, Inc., 4150 Network Circle,
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
package rasto1968.checklist;

import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractListModel;
import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import org.jdesktop.swingx.util.Contract;

/**
 * Creates a list model that delegates to the underlying list model. This wrapper maintains a
 * collection of {@link ListNode}s that wrap the user objects held by the delegate model.
 * Implementors of this class should provide a custom {@code ListNode} implementation that hold any
 * additional data that is required.
 * 
 * @param <N>
 *            the type of nodes used by this model
 */
public abstract class DelegatingListModel<N extends DelegatingListModel.ListNode> extends
        AbstractListModel {
    /**
     * A simple node for holding a user object. This class should be extended if the
     * {@link DelegatingListModel wrapping model} needs to maintain additional information.
     */
    public static class ListNode {
        private Object userObject;

        /**
         * Creates a node for the specified user object.
         * 
         * @param userObject
         *            the user object
         */
        protected ListNode(Object userObject) {
            this.userObject = userObject;
        }

        /**
         * The user object for this node.
         * 
         * @return the user object
         */
        public final Object getUserObject() {
            return userObject;
        }
        
        /**
         * {@inheritDoc}
         */
        public final String toString() {
            return userObject == null ? null : userObject.toString();
        }
    }
    
    private class DelegateSynchronizer implements ListDataListener {
        /**
         * {@inheritDoc}
         */
        @Override
        public void contentsChanged(ListDataEvent e) {
            //TODO do we need to clear and reload everything?
            for (int i = e.getIndex0(), len = e.getIndex1(); i <= len; i++) {
                if (i < nodes.size()) {
                    nodes.remove(i);
                }
                
                nodes.add(i, createListNode(delegate.getElementAt(i)));
            }
            
            fireContentsChanged(DelegatingListModel.this, e.getIndex0(), e.getIndex1());
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void intervalAdded(ListDataEvent e) {
            for (int i = e.getIndex0(), len = e.getIndex1(); i <= len; i++) {
                nodes.add(i, createListNode(delegate.getElementAt(i)));
            }
            
            fireIntervalAdded(DelegatingListModel.this, e.getIndex0(), e.getIndex1());
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void intervalRemoved(ListDataEvent e) {
            for (int i = e.getIndex0(), len = e.getIndex1(); i <= len; i++) {
                nodes.remove(i);
            }
            
            fireIntervalRemoved(DelegatingListModel.this, e.getIndex0(), e.getIndex1());
        }
    }
    
    /**
     * The list nodes managed by this model.
     */
    protected final List<N> nodes;
    
    private final ListModel delegate;

    private boolean unwrapUserObject;
    
    /**
     * Creates a new delegating list model with the specified delegate.
     * 
     * @param delegate
     *            the delegate backing this model
     * @throws NullPointerException
     *             if {@code delegate} is {@code null}
     */
    public DelegatingListModel(ListModel delegate) {
        this.delegate = Contract.asNotNull(delegate, "delegate cannot be null");
        
        nodes = new ArrayList<N>(delegate.getSize());
        
        for (int i = 0, len = delegate.getSize(); i <= len; i++) {
            nodes.add(createListNode(delegate.getElementAt(i)));
        }
        
        delegate.addListDataListener(new DelegateSynchronizer());
    }

    /**
     * Creates a {@code ListNode} for the specified user object.
     * 
     * @param userObject
     *            the user object
     * @return a {@code ListNode} containing the user object
     */
    protected abstract N createListNode(Object userObject);
    
    /**
     * {@inheritDoc}
     */
    @Override
    public final Object getElementAt(int index) {
        return unwrapUserObject ? delegate.getElementAt(index) : nodes.get(index);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final int getSize() {
        return delegate.getSize();
    }

    /**
     * Gets the user object at the specified index.
     * 
     * @param index
     *            the requested index
     * @return the value at {@code index}
     * @throws IndexOutOfBoundsException
     *             if {@code index < 0 || index >= getSize()}
     */
    public final Object getUserObjectAt(int index) {
        return delegate.getElementAt(index);
    }
    
    /**
     * Gets the list node at the specified index.
     * 
     * @param index
     *            the requested index
     * @return the value at {@code index}
     * @throws IndexOutOfBoundsException
     *             if {@code index < 0 || index >= getSize()}
     */
    public final N getNodeAt(int index) {
        return nodes.get(index);
    }

    /**
     * Determines if the {@code getElementAt} will return the list node used by this model or the
     * actual user object used by the delegate model.
     * 
     * @return {@code true} if {@code getElementAt} should return the user object; {@code false} to
     *         return the list node
     * @see #setUnwrapUserObject(boolean)
     */
    public final boolean getUnwrapUserObject() {
        return unwrapUserObject;
    }

    /**
     * Determines if the {@code getElementAt} will return the list node used by this model or the
     * actual user object used by the delegate model.
     * 
     * @param unwrapUserObject
     *            {@code true} to return the user object from {@code getElementAt}; {@code false} to
     *            return the list node
     * @see #getUnwrapUserObject()
     * @see #getElementAt(int)
     */
    public final void setUnwrapUserObject(boolean unwrapUserObject) {
        this.unwrapUserObject = unwrapUserObject;
        //FIXME
//        if (getExposeNodes() == expose) return;
//        this.exposeNodes = expose;
//        if (getSize() > 0) {
//            fireContentsChanged(this, 0, getSize() - 1);
//        }
    }
}
