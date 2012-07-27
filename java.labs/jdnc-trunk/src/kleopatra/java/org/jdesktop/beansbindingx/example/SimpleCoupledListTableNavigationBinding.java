/*
 * Created on 21.06.2007
 *
 */
package org.jdesktop.beansbindingx.example;

import java.awt.BorderLayout;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import org.jdesktop.beans.AbstractBean;
import org.jdesktop.beansbinding.BeanProperty;
import org.jdesktop.beansbinding.BindingGroup;
import org.jdesktop.beansbinding.Bindings;
import org.jdesktop.beansbinding.AutoBinding.UpdateStrategy;
import org.jdesktop.observablecollections.ObservableCollections;
import org.jdesktop.swingbinding.JListBinding;
import org.jdesktop.swingbinding.JTableBinding;
import org.jdesktop.swingbinding.SwingBindings;
import org.jdesktop.swingx.JXFrame;

/**
 * Try to drive list/table selection by a shared (navigation) controller.
 * 
 * NOTE: this is currently (aug2007) not possible, as the synthetic properties
 * selectedElement et al are _not_ writable.
 * 
 */
public class SimpleCoupledListTableNavigationBinding {
    private static final Logger LOG = Logger
            .getLogger(SimpleCoupledListTableNavigationBinding.class.getName());
    public class Controller extends AbstractBean {
        private Object selectedElement;
        private List list;
        public Controller(List list) {
            this.list = list;
        }
        
        public void setSelectedElement(Object selectedElement) {
            if ((selectedElement != null) && !list.contains(selectedElement)) {
                throw new IllegalArgumentException("list must contain element " + selectedElement);
            }
            Object old = getSelectedElement();
            this.selectedElement = selectedElement;
            LOG.info("selected old/new " + old + "/" + getSelectedElement()); 
            firePropertyChange("selectedElement", old, getSelectedElement());
        }
        
        public Object getSelectedElement() {
            return selectedElement;
        }

        @Override
        public String toString() {
            return String.valueOf(selectedElement);
        }

    }


    private void initData() {
        MyBean[] beans = new MyBean[] {
                new MyBean("first"), new MyBean("second"), new MyBean("third")
        };
        beanList = ObservableCollections.observableList(Arrays.asList(beans));
        // wrap a navigation object around the list
        navigation = new Controller(beanList);
    }


    private void bindBasics() {
        BindingGroup context = new BindingGroup();
        // bind bean list to table
        JTableBinding tableBinding = SwingBindings.createJTableBinding(
                UpdateStrategy.READ,
                beanList, table);
        tableBinding.addColumnBinding(BeanProperty.create("value"));
        tableBinding.addColumnBinding(BeanProperty.create("active"))
             .setColumnClass(Boolean.class);
        context.addBinding(tableBinding);
        // bind navigation selected to table selected
        context.addBinding(Bindings.createAutoBinding(UpdateStrategy.READ_WRITE,
                navigation, BeanProperty.create("selectedElement"), 
                table, BeanProperty.create("selectedElement")));
        // bind bean list to list
        JListBinding listBinding = SwingBindings.createJListBinding(UpdateStrategy.READ,
                beanList, list);
        listBinding.setDetailBinding(BeanProperty.create("value"));
        context.addBinding(listBinding);
        // bind navigation selected to list selected
        context.addBinding(Bindings.createAutoBinding(UpdateStrategy.READ_WRITE,
                navigation, BeanProperty.create("selectedElement"), 
                list, BeanProperty.create("selectedElement")));
        // bind navigation selected to textfield (to see where we are)    
        context.addBinding(Bindings.createAutoBinding(UpdateStrategy.READ_WRITE,
                navigation, BeanProperty.create("selectedElement.value"), 
                textField, BeanProperty.create("text")));
        context.bind();
    }

    private JComponent content;
    private JTable table;
    List<MyBean> beanList;
    private Controller navigation;
    private JTextField textField;
    private JList list;

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
        JComponent comp = new JPanel(new BorderLayout());
        comp.add(new JScrollPane(table));
        comp.add(list, BorderLayout.WEST);
        comp.add(textField, BorderLayout.NORTH);
        return comp;
    }

    private void initComponents() {
        table = new JTable();
        textField = new JTextField();
        list = new JList();
    }

    public static void main(String[] args) {
        final JXFrame frame = new JXFrame("Binding Painters", true);
        frame.add(new SimpleCoupledListTableNavigationBinding().getContent());
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
               frame.setSize(800, 600);
               frame.setLocationRelativeTo(null);
               frame.setVisible(true);
            }
        });
    }

    /**
     * Quick bean.
     */
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
        @Override
        public String toString() {
            return value;
        }
        
        
    }
    

}
