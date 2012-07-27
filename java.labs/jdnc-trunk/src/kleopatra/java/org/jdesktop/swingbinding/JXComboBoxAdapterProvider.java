/*
 * Copyright (C) 2006-2007 Sun Microsystems, Inc. All rights reserved. Use is
 * subject to license terms.
 */

package org.jdesktop.swingbinding;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JList;

import org.jdesktop.beansbinding.ext.BeanAdapterProvider;
import org.jdesktop.swingbinding.JXComboBoxBinding.BindingComboBoxModel;
import org.jdesktop.swingbinding.adapters.BeanAdapterBase;
import org.jdesktop.swingbinding.adapters.JComboBoxAdapterProvider;

/**
 * @author Shannon Hickey
 */
public final class JXComboBoxAdapterProvider implements BeanAdapterProvider {

//    private static final String SELECTED_ITEM_P = "selectedItem";
    private static final String SELECTED_ELEMENT = "selectedElement";
    
    public static final class Adapter extends BeanAdapterBase {
        private JComboBox combo;
        private Handler handler;
//        private Object cachedItem;
        private Object cachedElement;
        
        private Adapter(JComboBox combo) {
//            super(SELECTED_ITEM_P);
            super(SELECTED_ELEMENT);
            this.combo = combo;
            
        }

//        public Object getSelectedItem() {
//            return combo.getSelectedItem();
//        }
//
//        public void setSelectedItem(Object item) {
//            combo.setSelectedItem(item);
//        }
        
        public void setSelectedElement(Object item) {
            ComboBoxModel model = combo.getModel();
            if (model instanceof BindingComboBoxModel) {
                ((BindingComboBoxModel) model).setSelectedElement(item);
            } else {
                model.setSelectedItem(item);
            }
            
        }
        
        public Object getSelectedElement() {
            ComboBoxModel model = combo.getModel();
            if (model instanceof BindingComboBoxModel) {
                return ((BindingComboBoxModel) model).getSelectedElement();
            }
            return model.getSelectedItem();
        }
        
        protected void listeningStarted() {
            handler = new Handler();
            cachedElement = getSelectedElement();
//            cachedItem = combo.getSelectedItem();
            combo.addActionListener(handler);
            combo.addPropertyChangeListener("model", handler);
        }

        protected void listeningStopped() {
            combo.removeActionListener(handler);
            combo.removePropertyChangeListener("model", handler);
            handler = null;
            cachedElement = null;
//            cachedItem = null;
        }

        private class Handler implements ActionListener, PropertyChangeListener {
            private void comboSelectionChanged() {
                Object oldValue = cachedElement;
                cachedElement = getSelectedElement();
                firePropertyChange(oldValue, cachedElement);
//                Object oldValue = cachedItem;
//                cachedItem = getSelectedItem();
//                firePropertyChange(oldValue, cachedItem);
            }

            public void actionPerformed(ActionEvent ae) {
                comboSelectionChanged();
            }

            public void propertyChange(PropertyChangeEvent pce) {
                comboSelectionChanged();
            }
        }
    }

    public boolean providesAdapter(Class<?> type, String property) {
        return JComboBox.class.isAssignableFrom(type) 
            && property.intern() == SELECTED_ELEMENT;
//            && property.intern() == SELECTED_ITEM_P;
    }

    public Object createAdapter(Object source, String property) {
        if (!providesAdapter(source.getClass(), property)) {
            throw new IllegalArgumentException();
        }

        return new Adapter((JComboBox)source);
    }

    public Class<?> getAdapterClass(Class<?> type) {
        return JList.class.isAssignableFrom(type) ? 
            JComboBoxAdapterProvider.Adapter.class :
            null;
    }

}
