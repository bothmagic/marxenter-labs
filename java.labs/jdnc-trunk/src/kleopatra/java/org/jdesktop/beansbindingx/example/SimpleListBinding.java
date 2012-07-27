/*
 * Created on 13.04.2007
 *
 */
package org.jdesktop.beansbindingx.example;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import org.jdesktop.beans.AbstractBean;
import org.jdesktop.beansbinding.AutoBinding.UpdateStrategy;
import org.jdesktop.beansbinding.BeanProperty;
import org.jdesktop.beansbinding.Bindings;
import org.jdesktop.beansbindingx.BindingGroupBean;
import org.jdesktop.observablecollections.ObservableCollections;
import org.jdesktop.swingbinding.JListBinding;
import org.jdesktop.swingbinding.SwingBindings;
import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXList;

/**
 *  Quick check for ListBinding. Programmatic/input induced 
 *  property changes must be reflected in the list.
 *  Use ObservableList.
 *  
 */
    public class SimpleListBinding {
        @SuppressWarnings("unused")
        private static final Logger LOG = Logger
                .getLogger(SimpleListBinding.class.getName());
        private JComponent content;
        private JXList list;
        private JTextField textField;
        
        private List<MyBean> beanList;
        private JButton modifyButton;
        private JButton addButton;
    
    
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
            context.addBinding(Bindings.createAutoBinding(UpdateStrategy.READ_WRITE,
                    list, BeanProperty.create("selectedElement.value"), 
                    textField,  BeanProperty.create("text")));
            context.bind();
            Action action = new AbstractAction("programatically change") {
                public void actionPerformed(ActionEvent e) {
                    int selectedBean = list.getSelectedIndex();
                    if (selectedBean < 0) return;
                    MyBean bean = beanList.get(selectedBean); 
                    bean.setValue(bean.getValue() + "*");
                }
                
            };
            modifyButton.setAction(action); 
            
            Action add = new AbstractAction("add bean") {
                int count = 0;
                @Override
                public void actionPerformed(ActionEvent e) {
                    beanList.add(new MyBean("added: " + count++));
                }
                
            };
            addButton.setAction(add);
        }
    
    
    
        private void initData() {
            MyBean[] beans = new MyBean[] {
                    new MyBean("first"), new MyBean("second"), new MyBean("third")
            };
            beanList = ObservableCollections.observableList(new ArrayList(Arrays.asList(beans)));
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
            }
            return content;
        }
    
        private JComponent build() {
            JComponent comp = Box.createVerticalBox();
            comp.add(new JScrollPane(list));
            comp.add(textField, BorderLayout.SOUTH);
            comp.add(modifyButton);
            comp.add(addButton);
            return comp;
        }
    
    
        private void initComponents() {
            list = new JXList();
            textField = new JTextField();
            modifyButton = new JButton("modify programmtically");
            addButton = new JButton();
        }
    
        public static void main(String[] args) {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    final JXFrame frame = new JXFrame("List binding", true);
                    frame.add(new SimpleListBinding().getContent());
                    frame.pack();
                    frame.setSize(400, 300);
                    frame.setLocationRelativeTo(null);
                    frame.setVisible(true);
                }
            });
        }
    
    
    }
