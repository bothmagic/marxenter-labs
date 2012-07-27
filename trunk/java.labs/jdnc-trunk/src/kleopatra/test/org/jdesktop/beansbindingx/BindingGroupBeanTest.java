/*
 * Created on 30.10.2007
 *
 */
package org.jdesktop.beansbindingx;

import javax.swing.JCheckBox;
import javax.swing.JTextField;

import junit.framework.TestCase;

import org.jdesktop.beansbinding.BeanProperty;
import org.jdesktop.beansbinding.Binding;
import org.jdesktop.beansbinding.Bindings;
import org.jdesktop.beansbinding.AutoBinding.UpdateStrategy;
import org.jdesktop.beansbindingx.BeanTestUtils.TestBean;
import org.jdesktop.test.PropertyChangeReport;
import org.jdesktop.test.TestUtils;

/**
 * Test BindingGroupBean.
 */
public class BindingGroupBeanTest extends TestCase {

    private BindingGroupBean context;
    private TestBean bean;
    private JTextField target;
    private JCheckBox boolTarget;

 
    /**
     * Test setSourceObject: update bindings to common sourceObject.
     */
    public void testSetSourceObject() {
        Binding binding = Bindings.createAutoBinding(UpdateStrategy.READ,
                bean, BeanProperty.create("value"), 
                target, BeanProperty.create("text"));
        context.addBinding(binding);
        Binding boolBinding = Bindings.createAutoBinding(UpdateStrategy.READ,
                bean, BeanProperty.create("active"), 
                boolTarget, BeanProperty.create("selected"));
        context.addBinding(boolBinding);
        context.bind();
        TestBean otherBean = new TestBean("otherValue", true);
        // sanity
        assertEquals(!bean.isActive(), otherBean.isActive());
        context.setSourceObject(otherBean);
        assertEquals(otherBean.getValue(), target.getText());
        assertEquals(otherBean.isActive(), boolTarget.isSelected());
    }

    /**
     * Test setSourceObject: 
     * auto-install unreadableSourceValue for 
     * non-primitives (and not equivalent to primitives)
     */
    public void testSetSourceObjectNull() {
        Binding binding = Bindings.createAutoBinding(UpdateStrategy.READ,
                bean, BeanProperty.create("value"), 
                target, BeanProperty.create("text"));
        context.addBinding(binding);
        Binding boolBinding = Bindings.createAutoBinding(UpdateStrategy.READ,
                bean, BeanProperty.create("active"), 
                boolTarget, BeanProperty.create("selected"));
        context.addBinding(boolBinding);
        context.bind();
        context.setSourceObject(null);
        assertEquals("", target.getText());
    }

    /**
     * Test initial null bean: 
     * auto-install unreadableSourceValue for 
     * non-primitives (and not equivalent to primitives)
     */
    public void testInitialSourceObjectNull() {
        TestBean bean = null;
        Binding binding = Bindings.createAutoBinding(UpdateStrategy.READ,
                bean, BeanProperty.create("value"), 
                target, BeanProperty.create("text"));
        context.addBinding(binding);
        Binding boolBinding = Bindings.createAutoBinding(UpdateStrategy.READ,
                bean, BeanProperty.create("active"), 
                boolTarget, BeanProperty.create("selected"));
        context.addBinding(boolBinding);
        context.bind();
        assertNull("what do we expect for an initial null?", boolBinding.refresh());
    }

    /**
     * unbind edited property must reset dirty.
     * What should happen on READ/READ_WRITE strategies?
     */
    public void testEditPropertyUnbind() {
        Binding binding = Bindings.createAutoBinding(UpdateStrategy.READ_ONCE,
                bean, BeanProperty.create("value"), 
                target, BeanProperty.create("text"));
        context.addBinding(binding);
        context.bind();
        target.setText("othervalue");
        assertEquals("", "othervalue", binding.getTargetValueForSource().getValue());
        PropertyChangeReport report = new PropertyChangeReport();
        context.addPropertyChangeListener(report);
        context.unbind();
        TestUtils.assertPropertyChangeEvent(report, "dirty", true, false, false);
    }

    /**
     * edit property must fire dirty notification.
     * What should happen on READ/READ_WRITE strategies?
     */
    public void testEditProperty() {
        Binding binding = Bindings.createAutoBinding(UpdateStrategy.READ_ONCE,
                bean, BeanProperty.create("value"), 
                target, BeanProperty.create("text"));
        context.addBinding(binding);
        context.bind();
        PropertyChangeReport report = new PropertyChangeReport();
        context.addPropertyChangeListener(report);
        target.setText("othervalue");
        assertEquals("", "othervalue", binding.getTargetValueForSource().getValue());
        TestUtils.assertPropertyChangeEvent(report, "dirty", false, true, false);
    }

    /**
     * bind must reset bound property to true and notification.
     */
    public void testBind() {
        context.addBinding(Bindings.createAutoBinding(UpdateStrategy.READ_WRITE,
                bean, BeanProperty.create("value"), 
                target, BeanProperty.create("text_ON_ACTION_OR_FOCUS_LOST")));
        PropertyChangeReport report = new PropertyChangeReport();
        context.addPropertyChangeListener(report);
        context.bind();
        TestUtils.assertPropertyChangeEvent(report, "bound", false, true, false);
    }
    
    /**
     * bind must reset bound property to true and notification.
     */
    public void testUnbind() {
        context.addBinding(Bindings.createAutoBinding(UpdateStrategy.READ_WRITE,
                bean, BeanProperty.create("value"), 
                target, BeanProperty.create("text_ON_ACTION_OR_FOCUS_LOST")));
        context.bind();
        PropertyChangeReport report = new PropertyChangeReport();
        context.addPropertyChangeListener(report);
        context.unbind();
        TestUtils.assertPropertyChangeEvent(report, "bound", true, false, false);
    }
    /**
     * Adding a bound binding throws.
     */
    public void testAddBindingBoundThrows() {
        Binding binding = Bindings.createAutoBinding(UpdateStrategy.READ_WRITE,
                bean, BeanProperty.create("value"), 
                target, BeanProperty.create("text_ON_ACTION_OR_FOCUS_LOST"));
        binding.bind();
        try {
            context.addBinding(binding);
            fail("adding a bound binding must throw IllegalStateException");
        } catch (IllegalStateException e) {
            // expected
        } 
    }

    @Override
    protected void setUp() throws Exception {
        context = new BindingGroupBean(true);
        bean = new TestBean("testvalue");
        target = new JTextField();
        boolTarget = new JCheckBox();
    }
    

}
