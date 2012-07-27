/*
 * Created on 01.11.2007
 *
 */
package org.jdesktop.beansbindingx;

import javax.swing.JTextField;

import junit.framework.TestCase;

import org.jdesktop.beansbinding.BeanProperty;
import org.jdesktop.beansbinding.Binding;
import org.jdesktop.beansbinding.Binding.SyncFailure;
import org.jdesktop.beansbinding.Bindings;
import org.jdesktop.beansbinding.AutoBinding.UpdateStrategy;
import org.jdesktop.beansbinding.Binding.SyncFailure;
import org.jdesktop.beansbindingx.BeanTestUtils.TestBean;

public class BindingTest extends TestCase {

    private TestBean bean;
    private JTextField target;

    /**
     * try to set the source to null and clear out the target.
     */
    public void testNullSource() {
        Binding binding = Bindings.createAutoBinding(UpdateStrategy.READ_WRITE,
                bean, BeanProperty.create("value"), 
                target, BeanProperty.create("text"));
        binding.bind();
        // sanity
        assertEquals(bean.getValue(), target.getText());
        // need to unbind before setting source
        binding.unbind();
        binding.setSourceObject(null);
        binding.setSourceUnreadableValue(null);
        // re-bind or refresh to update the target
        binding.bind();
//        SyncFailure result = binding.refresh();
//        assertNull("refresh must have succeeded", result);
        assertEquals("target reset", "", target.getText());
    }

    @Override
    protected void setUp() throws Exception {
        bean = new TestBean("testvalue");
        target = new JTextField();
    }

}
