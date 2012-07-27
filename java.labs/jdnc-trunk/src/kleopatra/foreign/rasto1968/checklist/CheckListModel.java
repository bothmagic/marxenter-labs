/*
 * $Id: CheckListModel.java 3163 2009-06-22 17:47:19Z kschaefe $
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
import java.util.Collections;
import java.util.List;

import javax.swing.ListModel;

/**
 * A delegating {@code ListModel} that maintains a selection state for each user object. This model
 * is useful when creating a check list.
 */
public class CheckListModel extends DelegatingListModel<CheckListModel.SelectableNode> {
    /**
     * A {@code ListNode} that maintains a checked state for the user object.
     */
    public static class SelectableNode extends DelegatingListModel.ListNode implements
            Comparable<SelectableNode> {
        private boolean selected;

        /**
         * Creates a checkable node for the specified user object.
         * 
         * @param userObject
         *            the user object
         */
        protected SelectableNode(Object userObject) {
            super(userObject);
        }

        /**
         * Gets the selection state for this node.
         * 
         * @return {@code true} if the node is selected; {@code false} otherwise
         */
        public final boolean isSelected() {
            return selected;
        }

        /**
         * Sets the selection state for this node.
         * <p>
         * Warning: subclasses should not expose a way to set this property. If this property is
         * exposed, it would be possible for the selection state to become unsynchronized with the
         * view.
         * 
         * @param checked
         *            {@code true} to set the node as selected; {@code false} otherwise
         */
        protected final void setSelected(boolean checked) {
            this.selected = checked;
        }

        /**
         * Implemented to compare the user objects only, so this is incompatible
         * with the equals. C&p'ed from ShuttleSorter.
         */
        public int compareTo(SelectableNode o) {
            Object o1 = getUserObject();
            Object o2 = o.getUserObject();
            
            if (o1 == null && o2 == null) {
                return 0;
            } else if (o1 == null) { // Define null less than everything.
                return -1;
            } else if (o2 == null) {
                return 1;
            }
            // not copied: collator
            // patch from Jens Elkner (#189)
            if ((o1.getClass().isInstance(o2)) && (o1 instanceof Comparable)) {
                Comparable c1 = (Comparable) o1;
                return c1.compareTo(o2);
            } else if (o2.getClass().isInstance(o1)
                    && (o2 instanceof Comparable)) {
                Comparable c2 = (Comparable) o2;
                return -c2.compareTo(o1);
            }
            
            return 0;
        }
    }

    /**
     * Creates a new delegating list model with the specified delegate.
     * 
     * @param delegate
     *            the delegate backing this model
     * @throws NullPointerException
     *             if {@code delegate} is {@code null}
     */
    public CheckListModel(ListModel delegate) {
        super(delegate);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected SelectableNode createListNode(Object userObject) {
        return new SelectableNode(userObject);
    }

    /**
     * Determines if the node at the specified index is selected.
     * 
     * @param index
     *            the index to query
     * @return {@code true} if the node is selected; {@code false} otherwise
     */
    public boolean isChecked(int index) {
        return getNodeAt(index).isSelected();
    }

    /**
     * Updates the selection state of the node at the specified index.
     * 
     * @param index
     *            the index to update
     * @param checked
     *            the new selection state
     */
    public void setChecked(int index, boolean checked) {
        if (isChecked(index) == checked) {
            return;
        }
        
        getNodeAt(index).setSelected(checked);
        fireContentsChanged(this, index, index);
    }

    /**
     * A convenience method for inverting the current selection state.
     * 
     * @param index
     *            the index to update
     */
    public void toggleChecked(int index) {
        setChecked(index, !isChecked(index));
    }

    /**
     * Returns an unmodifiable collection of user objects that are currently selected.
     * 
     * @return the currently selected user objects
     */
    public List<Object> getCheckedUserObjects() {
        List<Object> checkedUserObjects = new ArrayList<Object>();
        
        for (SelectableNode checkableNode : nodes) {
            if (checkableNode.isSelected()) {
                checkedUserObjects.add(checkableNode.getUserObject());
            }
        }
        
        return Collections.unmodifiableList(checkedUserObjects);
    }
}
