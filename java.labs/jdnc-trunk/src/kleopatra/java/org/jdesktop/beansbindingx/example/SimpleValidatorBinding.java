/*
 * Created on 13.04.2007
 *
 */
package org.jdesktop.beansbindingx.example;

import java.awt.BorderLayout;
import java.util.Arrays;
import java.util.logging.Logger;

import javax.swing.Box;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import org.jdesktop.beans.AbstractBean;
import org.jdesktop.beansbinding.BeanProperty;
import org.jdesktop.beansbinding.Binding;
import org.jdesktop.beansbinding.BindingGroup;
import org.jdesktop.beansbinding.Bindings;
import org.jdesktop.beansbinding.Validator;
import org.jdesktop.beansbinding.AutoBinding.UpdateStrategy;
import org.jdesktop.beansbindingx.BindingGroupBean;
import org.jdesktop.beansbindingx.Navigation;
import org.jdesktop.beansbindingx.validator.NotEmptyValidator;
import org.jdesktop.observablecollections.ObservableCollections;
import org.jdesktop.observablecollections.ObservableList;
import org.jdesktop.swingbinding.JListBinding;
import org.jdesktop.swingbinding.SwingBindings;
import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXList;
import org.jdesktop.swingx.JXTitledSeparator;

/**
 * example for 
 * text commit strategies 
 *  first textfield: on type, second textfield: on focusLost/action
 *  
 * buffering (apply/discard).
 * 
 * Note: navigation binding not effective, synthetic swing properties
 * are read-only.
 *  
 */
public class SimpleValidatorBinding {
    @SuppressWarnings("unused")
    private static final Logger LOG = Logger
            .getLogger(SimpleValidatorBinding.class.getName());
    private JComponent content;
    private JXList list;
    private JTextField valueField;
    private JCheckBox activityBox;
    private ObservableList<MyBean> beanList;
    private Navigation navigation;
    private JCheckBox uncommittedBox;


    /**
     * Binds list, navigation and simple direct
     * properties.
     * 
     */
    @SuppressWarnings("unchecked")
    private void bindBasics() {
        // bind the list
        JListBinding listBinding = SwingBindings.createJListBinding(UpdateStrategy.READ_WRITE,
                beanList, list);
        listBinding.setDetailBinding(BeanProperty.create("value"));
        listBinding.bind();
        
        // bind the properties
        Validator notEmpty = new NotEmptyValidator();
        BindingGroupBean context = new BindingGroupBean();
        // bind the navigation to list selection
        context.addBinding(Bindings.createAutoBinding(UpdateStrategy.READ,
                list, BeanProperty.create("selectedElement"),
                navigation, BeanProperty.create("selectedElement"))); 
        Binding valueBinding = Bindings.createAutoBinding(UpdateStrategy.READ_WRITE,
                navigation, BeanProperty.create("selectedElement.value"), 
                valueField,  BeanProperty.create("text"));
        valueBinding.setSourceUnreadableValue(null);
        context.addBinding(valueBinding);
        valueBinding.setValidator(notEmpty);
        Binding activityBinding = Bindings.createAutoBinding(UpdateStrategy.READ_WRITE,
                navigation, BeanProperty.create("selectedElement.active"), 
                activityBox,  BeanProperty.create("selected"));
        activityBinding.setSourceUnreadableValue(Boolean.FALSE);
        context.addBinding(activityBinding);
        context.bind();
        
        BindingGroup bufferingContext = new BindingGroup();
        bufferingContext.addBinding(Bindings.createAutoBinding(UpdateStrategy.READ,
                context, BeanProperty.create("dirty"), 
                uncommittedBox, BeanProperty.create("selected")));
        bufferingContext.bind();
        
    }



    private void initData() {
        MyBean[] beans = new MyBean[] {
                new MyBean("first"), new MyBean("second"), new MyBean("third")
        };
        beanList = ObservableCollections.observableList(Arrays.asList(beans));
        // wrap a navigation object around the list
        navigation = new Navigation(beanList);
    }
    
    public static class MyBean extends AbstractBean {
        private String value;
        private boolean active;
        public MyBean(String value) {
            this.value = value;
        }
        public String getValue() {
            return value;
        }
        
        public void setValue(String value) {
            Object old = getValue();
            this.value = value;
            firePropertyChange("value", old, getValue());
        }
        
        public void setActive(boolean active) {
            boolean old = isActive();
            this.active = active;
            firePropertyChange("active", old, isActive());
        }
        
        public boolean isActive() {
            return active;
        }
    }
    

    private JComponent getContent() {
        if (content == null) {
            initComponents();
            content = build();
            initData();
            bindBasics();
        }
        return content;
    }

    private JComponent build() {
        JComponent comp = Box.createVerticalBox();
        comp.add(new JScrollPane(list));
        comp.add(valueField, BorderLayout.SOUTH);
        comp.add(activityBox);
        comp.add(new JXTitledSeparator("below: edited bindings"));
        comp.add(uncommittedBox);
        return comp;
    }


    private void initComponents() {
        list = new JXList();
        valueField = new JTextField();
        activityBox = new JCheckBox("active");
        uncommittedBox = new JCheckBox("uncommitted");
    }

    public static void main(String[] args) {
        final JXFrame frame = new JXFrame("TextFields and buffering", true);
        frame.add(new SimpleValidatorBinding().getContent());
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
               frame.pack();
               frame.setSize(400, 300);
               frame.setLocationRelativeTo(null);
               frame.setVisible(true);
            }
        });
    }


}
