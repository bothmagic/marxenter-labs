/*
 * Created on 13.04.2007
 *
 */
package org.jdesktop.beansbindingx.example;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.util.Arrays;
import java.util.logging.Logger;

import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import org.jdesktop.beans.AbstractBean;
import org.jdesktop.beansbinding.BeanProperty;
import org.jdesktop.beansbinding.Binding;
import org.jdesktop.beansbinding.BindingGroup;
import org.jdesktop.beansbinding.Bindings;
import org.jdesktop.beansbinding.AutoBinding.UpdateStrategy;
import org.jdesktop.beansbindingx.BindingGroupBean;
import org.jdesktop.beansbindingx.Navigation;
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
public class SimpleIndirectBinding {
    private static final Logger LOG = Logger
            .getLogger(SimpleIndirectBinding.class.getName());
    private JComponent content;
    private JXList list;
    private JTextField textField;
    private ObservableList beanList;
    private Navigation navigation;
    private MyBeanContainer myBeanContainer;
    private JTextField indirectField;
    private MyBeanModel myBeanModel;
    private JTextField copiedField;
    private AbstractAction applyAction;
    private JButton applyButton;
    private JLabel copyInput;
    private JButton discardButton;
    private AbstractAction discardAction;
    private JCheckBox uncommittedBox;
    private JCheckBox activeBox;
    private AbstractAction navigateAction;
    private JButton navigateButton;


    /**
     * Binds list, navigation and simple direct/contained 
     * properties.
     */
    private void bindBasics() {
        // navigation is the central controller, simulates a more complext model
        BindingGroupBean context = new BindingGroupBean();
        // PENDING: this assumes that the navigation is wrapped around
        // the same beanList
        // doing it more strictly ... re-invent SelectionInList?
        JListBinding listBinding = SwingBindings.createJListBinding(UpdateStrategy.READ_WRITE,
                beanList, list);
        listBinding.setDetailBinding(BeanProperty.create("value"));
        context.addBinding(listBinding);
        // bind the navigation to list selection
        context.addBinding(Bindings.createAutoBinding(UpdateStrategy.READ_WRITE,
                navigation, BeanProperty.create("selectedElement"), 
                list, BeanProperty.create("selectedElement")));
        // bind an indirect path directly to a textField
        context.addBinding(Bindings.createAutoBinding(UpdateStrategy.READ_WRITE,
                navigation, BeanProperty.create("selectedElement.value"), 
                textField,  BeanProperty.create("text")));
        // bind the navigation selected to another wrapper
        context.addBinding(Bindings.createAutoBinding(UpdateStrategy.READ_WRITE,
                navigation, BeanProperty.create("selectedElement"), 
                myBeanContainer, BeanProperty.create("myBean")));
        // bind the container's content (through a path) to a textField
        context.addBinding(Bindings.createAutoBinding(UpdateStrategy.READ_WRITE,
                myBeanContainer, BeanProperty.create("myBean.value"), 
                indirectField, BeanProperty.create("text_ON_ACTION_OR_FOCUS_LOST")));
        context.bind();
        
        navigateButton.setAction(navigateAction);
        
    }


    /**
     * Binds buffered values. Unsuccessful...
     */
    private void bindSimulatedDialog() {
        // simulate a buffered edit - the last textfield
        BindingGroup dialogContext = new BindingGroup();
        // bind navigation selected to the copying container
        dialogContext.addBinding(Bindings.createAutoBinding(UpdateStrategy.READ_WRITE,
                navigation, BeanProperty.create("selectedElement"),
                myBeanModel, BeanProperty.create("myBean")));
        // bind the container's content (through a path) to a textField
        dialogContext.addBinding(Bindings.createAutoBinding(UpdateStrategy.READ_WRITE,
                myBeanModel, BeanProperty.create("value"), 
                copiedField, BeanProperty.create("text")));
        // bind the container's content (through a path) to a textField
        dialogContext.addBinding(Bindings.createAutoBinding(UpdateStrategy.READ_WRITE,
                myBeanModel, BeanProperty.create("active"), 
                activeBox, BeanProperty.create("selected")));
        // bind to label -  to visualize the typing effect
        dialogContext.addBinding(Bindings.createAutoBinding(UpdateStrategy.READ,
                myBeanModel, BeanProperty.create("value"), 
                copyInput, BeanProperty.create("text")));
        // bind buffering to action enabled
        dialogContext.addBinding(Bindings.createAutoBinding(UpdateStrategy.READ,
                myBeanModel, BeanProperty.create("buffering"), 
                applyAction, BeanProperty.create("enabled")));
        dialogContext.addBinding(Bindings.createAutoBinding(UpdateStrategy.READ,
                myBeanModel, BeanProperty.create("buffering"), 
                discardAction, BeanProperty.create("enabled")));
        dialogContext.addBinding(Bindings.createAutoBinding(UpdateStrategy.READ,
                myBeanModel, BeanProperty.create("buffering"), 
                uncommittedBox, BeanProperty.create("selected")));
        dialogContext.bind();

        applyButton.setAction(applyAction);
        discardButton.setAction(discardAction);
    }


    private void initData() {
        MyBean[] beans = new MyBean[] {
                new MyBean("first"), new MyBean("second"), new MyBean("third")
        };
        beanList = ObservableCollections.observableList(Arrays.asList(beans));
        // wrap a navigation object around the list
        navigation = new Navigation(beanList);
        // yet another container
        myBeanContainer = new MyBeanContainer();
        myBeanModel = new MyBeanModel();
        // simulate buffered edit
        applyAction = new AbstractAction("applyAction buffered") {

            public void actionPerformed(ActionEvent e) {
                myBeanModel.apply();
                
            }
            
        };
        discardAction = new AbstractAction("discard changes") {

            public void actionPerformed(ActionEvent e) {
                myBeanModel.discard();
                
            }
            
        };
        
        navigateAction = new AbstractAction("next") {

            public void actionPerformed(ActionEvent e) {
                navigation.next();
            }
            
        };
    }
    /**
     * Container with write-through.
     */
    public static class MyBeanContainer extends AbstractBean {
        private MyBean myBean;

        public MyBean getMyBean() {
            return myBean;
        }

        public void setMyBean(MyBean myBean) {
            Object old = getMyBean();
            this.myBean = myBean;
            firePropertyChange("myBean", old, getMyBean());
        }
    }
    
    /**
     * Container with buffering.
     */
    public static class MyBeanModel extends MyBean {
        private MyBean nullBean = new MyBean(null);
        private MyBean wrappee;
        private BindingGroupBean context;
        private boolean buffering;
        private BindingGroup bufferingContext;
        
        public MyBeanModel() {
            super(null);
            initBindingContext();
        }
        
        public void apply() {
            if ((wrappee == null) || (wrappee == nullBean)) return;
            context.saveAndNotify();
            LOG.info("has edited? " + context.isDirty());
            // need to unbind/bind to force reset of hasEditedTargetBindings
//            discard();
        }

        public void discard() {
            if (wrappee == null) return;
            context.unbind();
            context.bind();
        }
        
        private void initBindingContext() {
            context = new BindingGroupBean();
            context.addBinding(Bindings.createAutoBinding(UpdateStrategy.READ,
                    wrappee, BeanProperty.create("value"), 
                    this, BeanProperty.create("value")));
            context.addBinding(Bindings.createAutoBinding(UpdateStrategy.READ,
                    wrappee, BeanProperty.create("active"), 
                    this, BeanProperty.create("active")));
            bufferingContext = new BindingGroup();
            bufferingContext.addBinding(Bindings.createAutoBinding(UpdateStrategy.READ,
                    context, BeanProperty.create("dirty"), 
                    this, BeanProperty.create("buffering")));
            bufferingContext.bind();
        }

        public void setMyBean(MyBean wrappee) {
            Object old = getMyBean();
            if (old != null) {
                context.unbind();
            }
            if (wrappee == null) {
                wrappee = nullBean;
            }
            this.wrappee = wrappee;
            for (Binding binding : context.getBindings()) {
                binding.setSourceObject(wrappee);
            }
            context.bind();
            firePropertyChange("myBean", old, getMyBean());
        }

        
        public boolean isBuffering() {
            return buffering;
        }

        public void setBuffering(boolean buffering) {
            boolean old = isBuffering();
            this.buffering = buffering;
            firePropertyChange("buffering", old, isBuffering());
        } 

        /**
         * Public as an implementation artefact - binding cannot handle
         * write-only properrties?
         * @return
         */
        private MyBean getMyBean() {
            return wrappee;
        }

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
            boolean old = getActive();
            this.active = active;
            firePropertyChange("active", old, getActive());
        }
        
        public boolean getActive() {
            return active;
        }
    }
    

    private JComponent getContent() {
        if (content == null) {
            initComponents();
            content = build();
            initData();
            bindBasics();
            bindSimulatedDialog();
        }
        return content;
    }

    private JComponent build() {
        JComponent comp = Box.createVerticalBox();
        comp.add(new JScrollPane(list));
        comp.add(textField, BorderLayout.SOUTH);
        comp.add(indirectField);
        comp.add(navigateButton);
        comp.add(new JXTitledSeparator("below: simulate dialog"));
        comp.add(copiedField);
        comp.add(activeBox);
        comp.add(copyInput);
        comp.add(uncommittedBox);
        comp.add(applyButton);
        comp.add(discardButton);
        return comp;
    }


    private void initComponents() {
        list = new JXList();
        textField = new JTextField();
        indirectField = new JTextField();
        copiedField = new JTextField();
        copyInput = new JLabel();
        applyButton = new JButton();
        discardButton = new JButton();
        navigateButton = new JButton();
        uncommittedBox = new JCheckBox("uncommitted");
        activeBox = new JCheckBox("active");
    }

    public static void main(String[] args) {
        final JXFrame frame = new JXFrame("TextFields and buffering", true);
        frame.add(new SimpleIndirectBinding().getContent());
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
