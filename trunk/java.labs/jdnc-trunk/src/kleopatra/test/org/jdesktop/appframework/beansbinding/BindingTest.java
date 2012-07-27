/*
 * Created on 27.08.2007
 *
 */
package org.jdesktop.appframework.beansbinding;

import java.awt.event.ActionEvent;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTable;

import junit.framework.TestCase;

import org.jdesktop.beans.AbstractBean;
import org.jdesktop.beansbinding.BeanProperty;
import org.jdesktop.beansbinding.Binding;
import org.jdesktop.beansbinding.BindingGroup;
import org.jdesktop.beansbinding.Bindings;
import org.jdesktop.beansbinding.AutoBinding.UpdateStrategy;
import org.jdesktop.observablecollections.ObservableCollections;
import org.jdesktop.swingbinding.JListBinding;
import org.jdesktop.swingbinding.JTableBinding;
import org.jdesktop.swingbinding.SwingBindings;

public class BindingTest extends TestCase {
    private static final Logger LOG = Logger.getLogger(BindingTest.class
            .getName());
    private List<MyBean> beanList;
    private Controller navigation;
    
    /**
     * sanity: controller selects as expected
     *
     */
    public void testController() {
       navigation.setSelectedElement(beanList.get(0));
       assertEquals(beanList.get(0), navigation.getSelectedElement());
    }

    /**
     * control selection: JList - change in list selection.
     * This is okay: read from list selection and synch navigation.
     * The other way round is not implemented (synthetic properties
     * not writable currently)
     */
    public void testListSelectionBindingListChange() {
        JList list = new JList();
        BindingGroup context = new BindingGroup();
        JListBinding listBinding = SwingBindings.createJListBinding(
                UpdateStrategy.READ, beanList, list);
        listBinding.setDetailBinding(BeanProperty.create("value"));
        context.addBinding(listBinding);
        // bind navigation selected to list selected
        context.addBinding(Bindings.createAutoBinding(
                UpdateStrategy.READ, 
                list, BeanProperty.create("selectedElement"),
                navigation, BeanProperty.create("selectedElement")
                ));
        context.bind();
        // sanity: no selection initially
        assertNull(navigation.getSelectedElement());
        assertTrue(list.getSelectionModel().isSelectionEmpty());
        // set selection in list
        list.setSelectedIndex(0);
        assertEquals(beanList.get(0), navigation.getSelectedElement());
    }

    /**
     * control selection: JList - change in controller.
     * Not implemented (synthetic properties
     * not writable currently)
     *
     */
    public void testListSelectionBinding() {
        JList list = new JList();
        BindingGroup context = new BindingGroup();
        JListBinding listBinding = SwingBindings.createJListBinding(
                UpdateStrategy.READ, beanList, list);
        listBinding.setDetailBinding(BeanProperty.create("value"));
        context.addBinding(listBinding);
        // bind navigation selected to list selected
        context.addBinding(Bindings.createAutoBinding(
                UpdateStrategy.READ_WRITE, 
                list, BeanProperty.create("selectedElement"),
                navigation, BeanProperty.create("selectedElement") 
                ));
        context.bind();
        // sanity: no selection initially
        assertNull(navigation.getSelectedElement());
        assertTrue(list.getSelectionModel().isSelectionEmpty());
        // set selection in controller
        navigation.setSelectedElement(beanList.get(0));
        assertEquals("list selection must be first element", 
                beanList.get(0), list.getSelectedValue());
        assertEquals("navigation selection must be first element", 
                beanList.get(0), navigation.getSelectedElement());
    }

    /**
     * control selection: shared JList/JTable - change in controller.
     *
     */
    public void testCoupledSelectionBinding() {
        JTable table = new JTable();
        JList list = new JList();
        BindingGroup context = new BindingGroup();
        // bind bean list to table
        JTableBinding tableBinding = SwingBindings.createJTableBinding(
                UpdateStrategy.READ,
                beanList, table);
        tableBinding.addColumnBinding(BeanProperty.create("value"));
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
        context.bind();
        // sanity: no selection initially
        assertNull(navigation.getSelectedElement());
        assertTrue(table.getSelectionModel().isSelectionEmpty());
        assertTrue(list.getSelectionModel().isSelectionEmpty());
        // set selection in controller
        navigation.setSelectedElement(beanList.get(0));
        assertEquals(beanList.get(0), navigation.getSelectedElement());
    }

    /**
     * control selection: JTable - change in controller.
     *
     */
    public void testTableSelectionBinding() {
        JTable table = new JTable();
        BindingGroup context = new BindingGroup();
        // bind bean list to table
        JTableBinding tableBinding = SwingBindings.createJTableBinding(
                UpdateStrategy.READ,
                beanList, table);
        tableBinding.addColumnBinding(BeanProperty.create("value"));
        context.addBinding(tableBinding);
        // bind navigation selected to table selected
        context.addBinding(Bindings.createAutoBinding(UpdateStrategy.READ_WRITE,
                navigation, BeanProperty.create("selectedElement"), 
                table, BeanProperty.create("selectedElement")));
        context.bind();
        // sanity: no selection initially
        assertNull(navigation.getSelectedElement());
        assertTrue(table.getSelectionModel().isSelectionEmpty());
        // set selection in controller
        navigation.setSelectedElement(beanList.get(0));
        assertEquals(beanList.get(0), navigation.getSelectedElement());
    }
    /**
     * Simulate navigation control (for binding table/list selection)
     */
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

    }

    /**
     * Quick bean.
     */
    public static class MyBean extends AbstractBean {
        private String value;
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
        @Override
        public String toString() {
            return value;
        }
        
        
    }

    @Override
    protected void setUp() throws Exception {
        MyBean[] beans = new MyBean[] {
                new MyBean("first"), new MyBean("second"), new MyBean("third")
        };
        beanList = ObservableCollections.observableList(Arrays.asList(beans));
        // wrap a navigation object around the list
        navigation = new Controller(beanList);
    }

    
}
