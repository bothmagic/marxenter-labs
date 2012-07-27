/*
 * Copyright (C) 2007 Sun Microsystems, Inc. All rights reserved. Use is
 * subject to license terms.
 */

package org.jdesktop.swingbinding;

import static org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ;
import static org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import org.jdesktop.beansbinding.AutoBinding;
import org.jdesktop.beansbinding.ObjectProperty;
import org.jdesktop.beansbinding.Property;
import org.jdesktop.beansbinding.PropertyStateEvent;
import org.jdesktop.beansbinding.PropertyStateListener;
import org.jdesktop.swingbinding.impl.AbstractColumnBinding;
import org.jdesktop.swingbinding.impl.ListBindingManager;

/**
 * C&P'ed from beansbinding to support detail bindings. Goal is to have 
 * different details for drop-down and selected.
 * 
 * <b>Beware</b>: this is most probably not working - just made it compilable
 * on the fly, not tested in any way!
 * 
 * Binds a {@code List} of objects to act as the items of a {@code JComboBox}.
 * Each object in the source {@code List} is an item in the {@code JComboBox}.
 * <p>
 * If the {@code List} is an instance of {@code ObservableList}, then changes
 * to the {@code List} are reflected in the {@code JComboBox}.
 * <p>
 * Instances of {@code JComboBoxBinding} are obtained by calling one of the
 * {@code createJComboBoxBinding} methods in the {@code SwingBindings} class. There
 * are methods for creating a {@code JComboBoxBinding} using direct references to a
 * {@code List} and/or {@code JComboBox} and methods for creating a {@code JComboBoxBinding} by
 * providing the {@code List} and/or {@code JComboBox} as {@code Property} instances
 * that derive the {@code List}/{@code JComboBox} from the binding's source/target objects.
 * <p>
 * {@code JComboBoxBinding} works by installing a custom model on the target {@code JComboBox},
 * at bind time if the {@code JComboBox} property is readable, or whenever it becomes
 * readable after binding. This model is uninstalled when the property becomes unreadable
 * or the binding is unbound. It is also uninstalled, and installed on the replacement,
 * when the value of the {@code JComboBox} property changes. When the model is uninstalled from a
 * {@code JComboBox}, the {@code JComboBox's} model is replaced with an empty {@code DefaultComboBoxModel}
 * so that it is left functional.
 * <p>
 * This class is a subclass of {@code AutoBinding}. The update strategy dictates how
 * the binding applies the value of the source {@code List} property to the model
 * used for the {@code JComboBox}. At bind time, if the source {@code List} property and
 * the target {@code JComboBox} property are both readable, the source {@code List}
 * becomes the source of items in the model. If the strategy is {@code READ_ONCE}
 * then there is no further automatic syncing after this point, including if the
 * target {@code JComboBox} property changes or becomes readable; the new {@code JComboBox} gets the model,
 * but no items. If the strategy is {@code READ}, however, the {@code List} is synced
 * to the model every time the source {@code List} property changes value, or the
 * target {@code JComboBox} property changes value or becomes readable. For
 * {@code JComboBoxBinding}, the {@code READ_WRITE} strategy behaves identical to {@code READ}.
 * <p>
 * Here is an example of creating a binding from a {@code List} of {@code Country}
 * objects to a {@code JComboBox}:
 * <p>
 * <pre><code>
 *    // create the country list
 *    List<Country> countries = createCountryList();
 *
 *    // create the binding from List to JList
 *    JComboBoxBinding cb = SwingBindings.createJComboBoxBinding(READ, countries, jComboBox);
 *
 *    // realize the binding
 *    cb.bind();
 * </code></pre>
 * <p>
 * In addition to binding the items of a {@code JComboBox}, it is possible to
 * bind to the selected item of a {@code JComboBox}.
 * See the list of <a href="package-summary.html#SWING_PROPS">
 * interesting swing properties</a> in the package summary for more details.
 *
 * @param <E> the type of elements in the source {@code List}
 * @param <SS> the type of source object (on which the source property resolves to {@code List})
 * @param <TS> the type of target object (on which the target property resolves to {@code JComboBox})
 *
 * 
 * @author Shannon Hickey
 */
public final class JXComboBoxBinding<E, SS, TS> extends AutoBinding<SS, List<E>, TS, List> {
    
    private Property<TS, ? extends JComboBox> comboP;
    private ElementsProperty<TS> elementsP;
    private Handler handler = new Handler();
    private BindingComboBoxModel model;
    private JComboBox combo;
    private DetailBinding detailBinding;
    
    
    /**
     * Constructs an instance of {@code JComboBoxBinding}.
     *
     * @param strategy the update strategy
     * @param sourceObject the source object
     * @param sourceListProperty a property on the source object that resolves to the {@code List} of elements
     * @param targetObject the target object
     * @param targetJComboBoxProperty a property on the target object that resolves to a {@code JComboBox}
     * @param name a name for the {@code JComboBoxBinding}
     * @throws IllegalArgumentException if the source property or target property is {@code null}
     */
    protected JXComboBoxBinding(UpdateStrategy strategy, SS sourceObject, Property<SS, List<E>> sourceListProperty, TS targetObject, Property<TS, ? extends JComboBox> targetJComboBoxProperty, String name) {
        super(strategy == READ_WRITE ? READ : strategy,
                sourceObject, sourceListProperty, targetObject, new ElementsProperty<TS>(), name);

          if (targetJComboBoxProperty == null) {
              throw new IllegalArgumentException("target JComboBox property can't be null");
          }

          comboP = targetJComboBoxProperty;
          elementsP = (ElementsProperty<TS>)getTargetProperty();
        setDetailBinding(null);
    }
    
    @Override
    protected void bindImpl() {
        model = new BindingComboBoxModel();
        elementsP.setAccessible(isComboAccessible());
        comboP.addPropertyStateListener(getTargetObject(), handler);
        elementsP.addPropertyStateListener(null, handler);

//        // order is important for the next two lines
//        ep.addPropertyStateListener(null, handler);
//        ep.installBinding(this);
        super.bindImpl();
    }
    
    @Override
    protected void unbindImpl() {
        // order is important for the next two lines
//        ep.uninstallBinding();
//        ep.removePropertyStateListener(null, handler);
        elementsP.removePropertyStateListener(null, handler);
        comboP.removePropertyStateListener(getTargetObject(), handler);
        elementsP.setAccessible(false);
        cleanupForLast();

        model = null;
        super.unbindImpl();
    }
    
    private boolean isComboAccessible() {
        return comboP.isReadable(getTargetObject()) && comboP.getValue(getTargetObject()) != null;
    }

    private boolean isComboAccessible(Object value) {
        return value != null && value != PropertyStateEvent.UNREADABLE;
    }

    private void cleanupForLast() {
        if (combo == null) {
            return;
        }

        combo.setSelectedItem(null);
        combo.setModel(new DefaultComboBoxModel());
        model.updateElements(null, combo.isEditable());
        combo = null;
        model = null;
    }

    /**
     * Creates a {@code DetailBinding} and sets it as the {@code DetailBinding}
     * for this {@code JListBinding}. A {@code DetailBinding} specifies the property
     * of the objects in the source {@code List} to be used as the elements of the
     * {@code JList}. If the {@code detailProperty} parameter is {@code null}, the
     * {@code DetailBinding} specifies that the objects themselves be used.
     *
     * @param detailProperty the property with which to derive each list value
     *        from its corresponding object in the source {@code List}
     * @return the {@code DetailBinding}
     */
    public DetailBinding setDetailBinding(Property<E, ?> detailProperty) {
        return setDetailBinding(detailProperty, null);
    }


    /**
     * {@code DetailBinding} represents a binding between a property of the elements
     * in the {@code JListBinding's} source {@code List}, and the values shown in
     * the {@code JList}. Values in the {@code JList} are aquired by fetching the
     * value of the {@code DetailBinding's} source property for the associated object
     * in the source {@code List}.
     * <p>
     * A {@code Converter} may be specified on a {@code ColumnBinding}. Specifying a
     * {@code Validator} is also possible, but doesn't make sense since {@code JList}
     * values aren't editable.
     * <p>
     * {@code ColumnBindings} are managed by their {@code JListBinding}. They are not
     * to be explicitly bound, unbound, added to a {@code BindingGroup}, or accessed
     * in a way that is not allowed for a managed binding.
     *
     * @see org.jdesktop.swingbinding.JListBinding#setDetailBinding(Property, String)
     */
    public final class DetailBinding extends AbstractColumnBinding {

        private DetailBinding(Property<E, ?> detailProperty, String name) {
            super(0, detailProperty, DETAIL_PROPERTY, name);
        }

    }

    private class Handler implements PropertyStateListener {
        public void propertyStateChanged(PropertyStateEvent pse) {
            if (!pse.getValueChanged()) {
                return;
            }

            if (pse.getSourceProperty() == comboP) {
                cleanupForLast();
                
                boolean wasAccessible = isComboAccessible(pse.getOldValue());
                boolean isAccessible = isComboAccessible(pse.getNewValue());

                if (wasAccessible != isAccessible) {
                    elementsP.setAccessible(isAccessible);
                } else if (elementsP.isAccessible()) {
                    elementsP.setValueAndIgnore(null, null);
                }
            } else {
                if (((ElementsProperty.ElementsPropertyStateEvent)pse).shouldIgnore()) {
                    return;
                }

                if (combo == null) {
                    combo = comboP.getValue(getTargetObject());
                    combo.setSelectedItem(null);
                    model = new BindingComboBoxModel();
                    combo.setModel(model);
                }

                model.updateElements((List)pse.getNewValue(), combo.isEditable());
            }
        }
    }


//    private class Handler implements PropertyStateListener {
//        public void propertyStateChanged(PropertyStateEvent pse) {
//            if (!pse.getValueChanged()) {
//                return;
//            }
//            
//            Object newValue = pse.getNewValue();
//            
//            if (newValue == PropertyStateEvent.UNREADABLE) {
//                combo.setModel(new DefaultComboBoxModel());
//                combo = null;
//                model.setElements(null);
//            } else {
//                combo = ep.getComponent();
//                model.setElements((List<E>)newValue);
//                combo.setModel(model);
//            }
//        }
//    }

    protected final class BindingComboBoxModel extends ListBindingManager implements ComboBoxModel  {
        private final List<ListDataListener> listeners;
        private Object selectedItem = null;
        private int selectedModelIndex = -1;
        private Object selectedElement;

        
        public BindingComboBoxModel() {
            listeners = new CopyOnWriteArrayList<ListDataListener>();
        }

        public void updateElements(List<?> elements, boolean isEditable) {
            setElements(elements, false);
            setSelectedIndex(size() <= 0 ? -1 : 0);

//            if (!isEditable || selectedModelIndex != -1) {
//                selectedItem = null;
//                selectedModelIndex = -1;
//            }
//            
//            if (size() <= 0) {
//                if (selectedModelIndex != -1) {
//                    selectedModelIndex = -1;
//                    selectedItem = null;
//                }
//            } else {
//                if (selectedItem == null) {
//                    selectedModelIndex = 0;
//                    selectedItem = getElementAt(selectedModelIndex);
//                }
//            }
//
            allChanged();
        }

//        public void setElements(List<?> elements) {
//            super.setElements(elements, false);
//            setSelectedIndex(size() <= 0 ? -1 : 0);
//            if (size() <= 0) {
//                if (selectedModelIndex != -1) {
//                    selectedModelIndex = -1;
//                    selectedItem = null;
//                    selectedElement = null;
//                }
//            } else {
//                if (selectedItem == null) {
//                    selectedModelIndex = 0;
//                    selectedItem = getElementAt(selectedModelIndex);
//                    selectedElement = getElement(selectedModelIndex);
//                }
//            }
//        }

        protected AbstractColumnBinding[] getColBindings() {
            return new AbstractColumnBinding[] {getDetailBinding()};
        }

//        protected AbstractColumnBinding[] getColBindings() {
//            return new AbstractColumnBinding[0];
//        }

        public Object getSelectedElement() {
            return selectedElement;
        }
        
        public void setSelectedElement(Object element) {
            int index = getElements().indexOf(element);
            setSelectedIndex(index);
            contentsChanged(-1, -1);
        }
        
        private void setSelectedIndex(int index) {
            if (index < 0) {
                selectedModelIndex = -1;
                selectedItem = null;
                selectedElement = null;
            } else {
                selectedModelIndex = index;
                selectedItem = getElementAt(index);
                selectedElement = getElement(index);
            }
            
        }

        public Object getSelectedItem() {
            return selectedItem;
        }

        public void setSelectedItem(Object item) {
            setSelectedIndex(getValueIndex(item));
            
            // This is what DefaultComboBoxModel does (yes, yuck!)
//            if ((selectedItem != null && !selectedItem.equals(item)) 
//                    || selectedItem == null && item != null) {
//                selectedItem = item;
//                contentsChanged(-1, -1);
//                selectedModelIndex = getValueIndex(item);
//            }
        }

        /**
         * @param item
         */
        private int getValueIndex(Object item) {
            int selectedModelIndex = -1;
            if (item != null) {
                int size = size();
                for (int i = 0; i < size; i++) {
                    if (item.equals(getElementAt(i))) {
                        selectedModelIndex = i;
                        break;
                    }
                }
            }
            return selectedModelIndex;
        }

        @Override
        protected void allChanged() {
            contentsChanged(0, size());
        }

        @Override
        protected void valueChanged(int row, int column) {
            contentsChanged(row, row);
            // we're not expecting any value changes since we don't have any
            // detail bindings for JComboBox
        }

        @Override
        protected void added(int index, int length) {
            ListDataEvent e = new ListDataEvent(this, ListDataEvent.INTERVAL_ADDED, index, index + length - 1);
            int size = listeners.size();
            for (int i = size - 1; i >= 0; i--) {
                listeners.get(i).intervalAdded(e);
            }

            if (size() == length && selectedItem == null) {
                setSelectedItem(getElementAt(0));
            }
        }

        @Override
        protected void removed(int index, int length) {
            ListDataEvent e = new ListDataEvent(this, ListDataEvent.INTERVAL_REMOVED, index, index + length - 1);
            int size = listeners.size();
            for (int i = size - 1; i >= 0; i--) {
                listeners.get(i).intervalRemoved(e);
            }
            
            if (selectedModelIndex >= index && selectedModelIndex < index + length) {
                if (size() == 0) {
                    setSelectedItem(null);
                } else {
                    setSelectedItem(getElementAt(Math.max(index - 1, 0)));
                }
            }
        }

        @Override
        protected void changed(int row) {
            contentsChanged(row, row);
        }

        private void contentsChanged(int row0, int row1) {
            ListDataEvent e = new ListDataEvent(this, ListDataEvent.CONTENTS_CHANGED, row0, row1);
            int size = listeners.size();
            for (int i = size - 1; i >= 0; i--) {
                listeners.get(i).contentsChanged(e);
            }
        }
        
        public Object getElementAt(int index) {
            return valueAt(index, 0);
        }

//        public Object getElementAt(int index) {
//            return getElement(index);
//        }
        
        public void addListDataListener(ListDataListener l) {
            listeners.add(l);
        }
        
        public void removeListDataListener(ListDataListener l) {
            listeners.remove(l);
        }
        
        public int getSize() {
            return size();
        }
    }

    /**
     * Creates a named {@code DetailBinding} and sets it as the {@code DetailBinding}
     * for this {@code JListBinding}. A {@code DetailBinding} specifies the property
     * of the objects in the source {@code List} to be used as the elements of the
     * {@code JList}. If the {@code detailProperty} parameter is {@code null}, the
     * {@code DetailBinding} specifies that the objects themselves be used.
     *
     * @param detailProperty the property with which to derive each list value
     *        from its corresponding object in the source {@code List}
     * @return the {@code DetailBinding}
     */
    public DetailBinding setDetailBinding(Property<E, ?> detailProperty, String name) {
        throwIfBound();

        if (name == null && JXComboBoxBinding.this.getName() != null) {
            name = JXComboBoxBinding.this.getName() + ".DETAIL_BINDING";
        }

        detailBinding = detailProperty == null ?
                        new DetailBinding(ObjectProperty.<E>create(), name) :
                        new DetailBinding(detailProperty, name);
        return detailBinding;
    }

    /**
     * Returns the {@code DetailBinding} for this {@code JListBinding}.
     * A {@code DetailBinding} specifies the property of the source {@code List} elements
     * to be used as the elements of the {@code JList}.
     *
     * @return the {@code DetailBinding}
     * @see #setDetailBinding(Property, String)
     */
    public DetailBinding getDetailBinding() {
        return detailBinding;
    }

    private final Property DETAIL_PROPERTY = new Property() {
        public Class<Object> getWriteType(Object source) {
            return Object.class;
        }

        public Object getValue(Object source) {
            throw new UnsupportedOperationException();
        }

        public void setValue(Object source, Object value) {
            throw new UnsupportedOperationException();
        }

        public boolean isReadable(Object source) {
            throw new UnsupportedOperationException();
        }

        public boolean isWriteable(Object source) {
            return true;
        }

        public void addPropertyStateListener(Object source, PropertyStateListener listener) {
            throw new UnsupportedOperationException();
        }

        public void removePropertyStateListener(Object source, PropertyStateListener listener) {
            throw new UnsupportedOperationException();
        }

        public PropertyStateListener[] getPropertyStateListeners(Object source) {
            throw new UnsupportedOperationException();
        }
    };

}
