/*=======================================================================
                 WESTINGHOUSE ELECTRIC COMPANY LLC
                         NUCLEAR SERVICES
                 Westinghouse Proprietary Class II

   This document is the property of and contains Proprietary Information
   owned by Westinghouse Electric Company LLC and/or its subcontractors
   and suppliers.  It is transmitted to you in confidence and trust, and
   you agree to treat this document in strict accordance with the terms
   and conditions of the agreement under which it was provided to you.

   Copyright 2006-2007 Westinghouse Electric Company LLC
   All Rights Reserved.
=======================================================================*/
package org.jdesktop.swingx.combobox;

import javax.swing.AbstractListModel;
import javax.swing.MutableComboBoxModel;
import javax.swing.event.ListDataListener;

import org.jdesktop.swingx.util.Contract;

/**
 * A model for a combo box that contains a special item, which allows the user to select a new item
 * to add to the list. This model is useful when editing the textual display of a combo box does not
 * provide enough information for the combo box to correctly add a new item.
 * <p>
 * The most common use for this model is with {@link java.io.File}s.
 * 
 * @author Karl George Schaefer
 */
@SuppressWarnings("serial")
public abstract class ExtensibleComboBoxModel extends AbstractListModel implements
        MutableComboBoxModel {
    /**
     *
     */
    protected static class NewItem {
        private Object contents;

        /**
         * @return the contents
         */
        public Object getContents() {
            return contents;
        }

        /**
         * @param contents the contents to set
         */
        public void setContents(Object contents) {
            this.contents = contents;
        }
    }

    /**
     * The new item marker used by this model to determine when to call {@link #generateNewItem()}.
     */
    protected final NewItem newItem;

    private final MutableComboBoxModel model;
    
    /**
     * Creates a new model with the specified backing model.
     * 
     * @param model
     *            the model backing this model
     * @throws NullPointerException
     *             if {@code model} is {@code null}
     */
    public ExtensibleComboBoxModel(MutableComboBoxModel model) {
        Contract.asNotNull(model, "model cannt be null");

        this.model = model;
        newItem = new NewItem();
    }

    /**
     * This method creates and returns a new item suitable for adding to the backing model. This
     * method is called from the EDT, when invoked by a combo box. Any long-running tasks should
     * take care not to block the EDT.
     * <p>
     * This method will return {@link #newItem} when a user cancels new item generation. This allows
     * the user to generate {@code null} items, if appropriate.
     * 
     * @return a new object suitable for the backing model or {@code newItem} to cancel generation
     */
    protected abstract Object generateNewItem();

    /**
     * Gets the new item marker contents.
     * 
     * @return the contents of the new item marker
     * @see org.jdesktop.swingx.combobox.ExtensibleComboBoxModel.NewItem#getContents()
     */
    public Object getContents() {
        return newItem.getContents();
    }

    /**
     * Sets the new item marker contents.
     * 
     * @param contents
     *            the new contents of hte new item marker
     * @see org.jdesktop.swingx.combobox.ExtensibleComboBoxModel.NewItem#setContents(java.lang.Object)
     */
    public void setContents(Object contents) {
        newItem.setContents(contents);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getElementAt(int index) {
        if (index == model.getSize()) {
            return newItem.getContents();
        }

        return model.getElementAt(index);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getSize() {
        return model.getSize() + 1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addElement(Object obj) {
        model.addElement(obj);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void insertElementAt(Object obj, int index) {
        model.insertElementAt(obj, index);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeElement(Object obj) {
        model.removeElement(obj);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeElementAt(int index) {
        model.removeElementAt(index);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object getSelectedItem() {
        return model.getSelectedItem();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setSelectedItem(Object anItem) {
        Object newSelection = null;

        if (anItem == newItem.getContents()) {
            newSelection = generateNewItem();

            if (newSelection == newItem) {
                newSelection = null;
            } else {
                addElement(newSelection);
                model.setSelectedItem(newSelection);
            }
        } else {
            model.setSelectedItem(anItem);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addListDataListener(ListDataListener l) {
        super.addListDataListener(l);
        
        model.addListDataListener(l);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeListDataListener(ListDataListener l) {
        model.removeListDataListener(l);
        
        super.removeListDataListener(l);
    }
}
