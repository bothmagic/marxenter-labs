/*
 * Created on 06.09.2007
 *
 */
package org.jdesktop.swingbinding;

import java.util.List;

import javax.swing.JComboBox;

import org.jdesktop.beansbinding.AutoBinding;
import org.jdesktop.beansbinding.ObjectProperty;
import org.jdesktop.beansbinding.Property;

public class SwingXBindings {

    private SwingXBindings() {}

    /**
     * Creates a {@code JXComboBoxBinding} from direct references to a {@code List} and {@code JComboBox}.
     *
     * @param strategy the update strategy
     * @param sourceList the source {@code List}
     * @param targetJComboBox the target {@code JComboBox}
     * @return the {@code JXComboBoxBinding}
     */
    public static <E> JXComboBoxBinding<E, List<E>, JComboBox> createJComboBoxBinding(AutoBinding.UpdateStrategy strategy, List<E> sourceList, JComboBox targetJComboBox) {
        return new JXComboBoxBinding<E, List<E>, JComboBox>(strategy, sourceList, ObjectProperty.<List<E>>create(), targetJComboBox, ObjectProperty.<JComboBox>create(), null);
    }
    
    /**
     * Creates a named {@code JXComboBoxBinding} from direct references to a {@code List} and {@code JComboBox}.
     *
     * @param strategy the update strategy
     * @param sourceList the source {@code List}
     * @param targetJComboBox the target {@code JComboBox}
     * @return the {@code JXComboBoxBinding}
     */
    public static <E> JXComboBoxBinding<E, List<E>, JComboBox> createJComboBoxBinding(AutoBinding.UpdateStrategy strategy, List<E> sourceList, JComboBox targetJComboBox, String name) {
        return new JXComboBoxBinding<E, List<E>, JComboBox>(strategy, sourceList, ObjectProperty.<List<E>>create(), targetJComboBox, ObjectProperty.<JComboBox>create(), name);
    }
    
    
    /**
     * Creates a {@code JXComboBoxBinding} from an object and property that resolves to a {@code List} and a direct reference to a {@code JComboBox}.
     *
     * @param strategy the update strategy
     * @param sourceObject the source object
     * @param sourceListProperty a property on the source object that resolves to a {@code List}
     * @param targetJComboBox the target {@code JComboBox}
     * @return the {@code JXComboBoxBinding}
     * @throws IllegalArgumentException if {@code sourceListProperty} is {@code null}
     */
    public static <E, SS> JXComboBoxBinding<E, SS, JComboBox> createJComboBoxBinding(AutoBinding.UpdateStrategy strategy, SS sourceObject, Property<SS, List<E>> sourceListProperty, JComboBox targetJComboBox) {
        return new JXComboBoxBinding<E, SS, JComboBox>(strategy, sourceObject, sourceListProperty, targetJComboBox, ObjectProperty.<JComboBox>create(), null);
    }
    
    /**
     * Creates a named {@code JXComboBoxBinding} from an object and property that resolves to a {@code List} and a direct reference to a {@code JComboBox}.
     *
     * @param strategy the update strategy
     * @param sourceObject the source object
     * @param sourceListProperty a property on the source object that resolves to a {@code List}
     * @param targetJComboBox the target {@code JComboBox}
     * @return the {@code JXComboBoxBinding}
     * @throws IllegalArgumentException if {@code sourceListProperty} is {@code null}
     */
    public static <E, SS> JXComboBoxBinding<E, SS, JComboBox> createJComboBoxBinding(AutoBinding.UpdateStrategy strategy, SS sourceObject, Property<SS, List<E>> sourceListProperty, JComboBox targetJComboBox, String name) {
        return new JXComboBoxBinding<E, SS, JComboBox>(strategy, sourceObject, sourceListProperty, targetJComboBox, ObjectProperty.<JComboBox>create(), name);
    }
    
    
    /**
     * Creates a {@code JXComboBoxBinding} from a direct reference to a {@code List} and an object and property that resolves to a {@code JComboBox}.
     *
     * @param strategy the update strategy
     * @param sourceList the source {@code List}
     * @param targetObject the target object
     * @param targetJComboBoxProperty a property on the target object that resolves to a {@code JComboBox}
     * @return the {@code JXComboBoxBinding}
     * @throws IllegalArgumentException if {@code targetJComboBoxProperty} is {@code null}
     */
    public static <E, TS> JXComboBoxBinding<E, List<E>, TS> createJComboBoxBinding(AutoBinding.UpdateStrategy strategy, List<E> sourceList, TS targetObject, Property<TS, ? extends JComboBox> targetJComboBoxProperty) {
        return new JXComboBoxBinding<E, List<E>, TS>(strategy, sourceList, ObjectProperty.<List<E>>create(), targetObject, targetJComboBoxProperty, null);
    }
    
    /**
     * Creates a named {@code JXComboBoxBinding} from a direct reference to a {@code List} and an object and property that resolves to a {@code JComboBox}.
     *
     * @param strategy the update strategy
     * @param sourceList the source {@code List}
     * @param targetObject the target object
     * @param targetJComboBoxProperty a property on the target object that resolves to a {@code JComboBox}
     * @return the {@code JXComboBoxBinding}
     * @throws IllegalArgumentException if {@code targetJComboBoxProperty} is {@code null}
     */
    public static <E, TS> JXComboBoxBinding<E, List<E>, TS> createJComboBoxBinding(AutoBinding.UpdateStrategy strategy, List<E> sourceList, TS targetObject, Property<TS, ? extends JComboBox> targetJComboBoxProperty, String name) {
        return new JXComboBoxBinding<E, List<E>, TS>(strategy, sourceList, ObjectProperty.<List<E>>create(), targetObject, targetJComboBoxProperty, name);
    }
    
    
    /**
     * Creates a {@code JXComboBoxBinding} from an object and property that resolves to a {@code List} and an object and property that resolves to a {@code JComboBox}.
     *
     * @param strategy the update strategy
     * @param sourceObject the source object
     * @param sourceListProperty a property on the source object that resolves to a {@code List}
     * @param targetObject the target object
     * @param targetJComboBoxProperty a property on the target object that resolves to a {@code JComboBox}
     * @return the {@code JXComboBoxBinding}
     * @throws IllegalArgumentException if {@code sourceListProperty} or {@code targetJComboBoxProperty} is {@code null}
     */
    public static <E, SS, TS> JXComboBoxBinding<E, SS, TS> createJComboBoxBinding(AutoBinding.UpdateStrategy strategy, SS sourceObject, Property<SS, List<E>> sourceListProperty, TS targetObject, Property<TS, ? extends JComboBox> targetJComboBoxProperty) {
        return new JXComboBoxBinding<E, SS, TS>(strategy, sourceObject, sourceListProperty, targetObject, targetJComboBoxProperty, null);
    }
    
    /**
     * Creates a named {@code JXComboBoxBinding} from an object and property that resolves to a {@code List} and an object and property that resolves to a {@code JComboBox}.
     *
     * @param strategy the update strategy
     * @param sourceObject the source object
     * @param sourceListProperty a property on the source object that resolves to a {@code List}
     * @param targetObject the target object
     * @param targetJComboBoxProperty a property on the target object that resolves to a {@code JComboBox}
     * @return the {@code JXComboBoxBinding}
     * @throws IllegalArgumentException if {@code sourceListProperty} or {@code targetJComboBoxProperty} is {@code null}
     */
    public static <E, SS, TS> JXComboBoxBinding<E, SS, TS> createJComboBoxBinding(AutoBinding.UpdateStrategy strategy, SS sourceObject, Property<SS, List<E>> sourceListProperty, TS targetObject, Property<TS, ? extends JComboBox> targetJComboBoxProperty, String name) {
        return new JXComboBoxBinding<E, SS, TS>(strategy, sourceObject, sourceListProperty, targetObject, targetJComboBoxProperty, name);
    }

}
