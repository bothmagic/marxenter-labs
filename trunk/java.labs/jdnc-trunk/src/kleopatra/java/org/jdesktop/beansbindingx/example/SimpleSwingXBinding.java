/*
 * Created on 13.04.2007
 *
 */
package org.jdesktop.beansbindingx.example;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import org.jdesktop.beans.AbstractBean;
import org.jdesktop.beansbinding.BeanProperty;
import org.jdesktop.beansbinding.Bindings;
import org.jdesktop.beansbinding.ELProperty;
import org.jdesktop.beansbinding.AutoBinding.UpdateStrategy;
import org.jdesktop.beansbindingx.BindingGroupBean;
import org.jdesktop.observablecollections.ObservableCollections;
import org.jdesktop.swingbinding.JListBinding;
import org.jdesktop.swingbinding.SwingBindings;
import org.jdesktop.swingx.JXDatePicker;
import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXList;

/**
 *  Quick check for ListBinding. Programmatic/input induced 
 *  property changes must be reflected in the list.
 *  Use ObservableList.
 *  
 */
public class SimpleSwingXBinding {
    @SuppressWarnings("unused")
    private static final Logger LOG = Logger
            .getLogger(SimpleSwingXBinding.class.getName());
    private JComponent content;
    private JXList list;
    private JXDatePicker picker;
    
    List<MyBean> beanList;


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
//        listBinding.setDetailBinding(BeanProperty.create("birthDay"));
        listBinding.setDetailBinding(ELProperty.create("${name} : ${birthDay}"));
        context.addBinding(listBinding);
        context.addBinding(Bindings.createAutoBinding(UpdateStrategy.READ_WRITE,
                list, BeanProperty.create("selectedElement.birthDay"), 
                picker,  BeanProperty.create("date")));
        context.bind();
    }



    private void initData() {
        MyBean[] beans = new MyBean[] {
                new MyBean("first"), new MyBean("second"), new MyBean("third")
        };
        beanList = ObservableCollections.observableList(Arrays.asList(beans));
    }
    
    
    public static class MyBean extends AbstractBean {
        private String name;
        private boolean active;
        private Date birthDay;
        
        public MyBean(String value) {
            this.name = value;
            birthDay = new Date();
        }
        
        public String getName() {
            return name;
        }
        
        public void setName(String value) {
            Object old = getName();
            this.name = value;
            firePropertyChange("name", old, getName());
        }
        
        public Date getBirthDay() {
            return birthDay;
        }

        public void setBirthDay(Date birthDay) {
            Object old = getBirthDay();
            this.birthDay = birthDay;
            firePropertyChange("birthDay", old, getBirthDay());
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
        comp.add(picker);
        return comp;
    }


    private void initComponents() {
        list = new JXList();
        picker = new JXDatePicker();
    }

    public static void main(String[] args) {
        final JXFrame frame = new JXFrame("SwingX Component Binding", true);
        frame.add(new SimpleSwingXBinding().getContent());
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
