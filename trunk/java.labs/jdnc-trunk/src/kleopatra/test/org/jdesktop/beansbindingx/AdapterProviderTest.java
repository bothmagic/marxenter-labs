/*
 * Created on 31.10.2007
 *
 */
package org.jdesktop.beansbindingx;

import java.util.List;

import javax.swing.DefaultListSelectionModel;
import javax.swing.JList;
import javax.swing.JTable;

import junit.framework.TestCase;

import org.jdesktop.beans.AbstractBean;
import org.jdesktop.beansbinding.BeanProperty;
import org.jdesktop.beansbinding.Binding;
import org.jdesktop.beansbinding.Bindings;
import org.jdesktop.beansbinding.AutoBinding.UpdateStrategy;
import org.jdesktop.beansbindingx.BeanTestUtils.TestBean;
import org.jdesktop.swingbinding.JListBinding;
import org.jdesktop.swingbinding.JTableBinding;
import org.jdesktop.swingbinding.SwingBindings;

public class AdapterProviderTest extends TestCase {
    List<TestBean> beans;

    /**
     * Bug in JTableAdapterProvider: doesn't re-wire the selection model if changed.
     */
    public void testListProviderListSelectionModelChange() {
        Binding binding = createBoundList();
        JList list = (JList) binding.getTargetObject();
        Wrapper wrapper = new Wrapper();
        Binding selected = Bindings.createAutoBinding(UpdateStrategy.READ,
                list, BeanProperty.create("selectedElement_IGNORE_ADJUSTING"),
                wrapper, BeanProperty.create("wrappee"));
        selected.bind();
        list.setSelectionModel(new DefaultListSelectionModel());
        // sanity
        assertNull(wrapper.getWrappee());
        list.setSelectedIndex(0);
        assertEquals(beans.get(list.getSelectedIndex()), wrapper.getWrappee());
    }

    public void testListProviderListSelection() {
        Binding binding = createBoundList();
        JList list = (JList) binding.getTargetObject();
        Wrapper wrapper = new Wrapper();
        Binding selected = Bindings.createAutoBinding(UpdateStrategy.READ,
                list, BeanProperty.create("selectedElement_IGNORE_ADJUSTING"),
                wrapper, BeanProperty.create("wrappee"));
        selected.bind();
        // sanity
        assertNull(wrapper.getWrappee());

        list.setSelectedIndex(0);
        assertEquals(beans.get(list.getSelectedIndex()), wrapper.getWrappee());
        list.setSelectionModel(new DefaultListSelectionModel());
    }


    /**
     * Bug in JTableAdapterProvider: doesn't re-wire the selection model if changed.
     */
    public void testTableProviderListSelectionModelChange() {
        Binding binding = createBoundTable();
        JTable table = (JTable) binding.getTargetObject();
        Wrapper wrapper = new Wrapper();
        Binding selected = Bindings.createAutoBinding(UpdateStrategy.READ,
                table, BeanProperty.create("selectedElement_IGNORE_ADJUSTING"),
                wrapper, BeanProperty.create("wrappee"));
        selected.bind();
        table.setSelectionModel(new DefaultListSelectionModel());
        // sanity
        assertNull(wrapper.getWrappee());
        table.setRowSelectionInterval(0, 0);
        assertEquals(beans.get(table.getSelectedRows()[0]), wrapper.getWrappee());
    }

    public void testTableProviderListSelection() {
        Binding binding = createBoundTable();
        JTable table = (JTable) binding.getTargetObject();
        Wrapper wrapper = new Wrapper();
        Binding selected = Bindings.createAutoBinding(UpdateStrategy.READ,
                table, BeanProperty.create("selectedElement_IGNORE_ADJUSTING"),
                wrapper, BeanProperty.create("wrappee"));
        selected.bind();
        // sanity
        assertNull(wrapper.getWrappee());

        table.setRowSelectionInterval(0, 0);
        assertEquals(beans.get(table.getSelectedRows()[0]), wrapper.getWrappee());
        table.setSelectionModel(new DefaultListSelectionModel());
    }

    public static class Wrapper extends AbstractBean {
        Object wrappee;

        public Object getWrappee() {
            return wrappee;
        }

        public void setWrappee(Object wrappee) {
            Object old = wrappee;
            this.wrappee = wrappee;
            firePropertyChange("wrappee", old, wrappee);
        }
        
        
    }
    
    private Binding createBoundTable() {
        JTable table = new JTable();
        JTableBinding binding = SwingBindings.createJTableBinding(UpdateStrategy.READ_WRITE,
                beans, table);
        binding.addColumnBinding(BeanProperty.create("value"));
        binding.bind();
        assertEquals(1, table.getColumnCount());
        return binding;
    }

    private Binding createBoundList() {
        JList table = new JList();
        JListBinding binding = SwingBindings.createJListBinding(UpdateStrategy.READ_WRITE,
                beans, table);
        binding.setDetailBinding(BeanProperty.create("value"));
        binding.bind();
        return binding;
    }
    @Override
    protected void setUp() throws Exception {
        beans = BeanTestUtils.createBeans();
    }
    
    
}
