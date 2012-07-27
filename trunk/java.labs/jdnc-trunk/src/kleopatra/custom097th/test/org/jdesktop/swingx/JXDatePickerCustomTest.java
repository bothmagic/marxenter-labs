/*
 * Created on 16.04.2009
 *
 */
package org.jdesktop.swingx;

import java.awt.AWTEvent;
import java.awt.GraphicsEnvironment;
import java.awt.event.FocusEvent;
import java.awt.im.InputContext;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Logger;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JTextField;

import org.jdesktop.swingx.event.AbstractInputEventDispatcher;
import org.jdesktop.swingx.event.CompoundFocusEventDispatcher;
import org.jdesktop.swingx.event.DispatchingInputContext;
import org.jdesktop.swingx.event.InputEventDispatcher;
import org.jdesktop.test.InputEventDispatcherReport;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Tests a custom JXXDatePicker which would support InputEventDispatcher. 
 * 
 * @author Jeanette Winzenburg
 */
@RunWith(JUnit4.class)
public class JXDatePickerCustomTest extends InteractiveTestCase {

    
    @SuppressWarnings("unused")
    private static final Logger LOG = Logger
            .getLogger(JXDatePickerCustomTest.class.getName());
    
    private InputEventDispatcher emptyInputEventDispatcher;
    private InputEventDispatcherReport report;
    private JXXDatePicker picker;
    
    public static void main(String[] args) {
        JXDatePickerCustomTest test = new JXDatePickerCustomTest();
        try {
            test.runInteractiveTests();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void interactiveFocusDispatcher() {
        JXXDatePicker picker = new JXXDatePicker();
        AbstractInputEventDispatcher focusDispatcher = new CompoundFocusEventDispatcher(picker);
        picker.setInputEventDispatcher(focusDispatcher);
        PropertyChangeListener l = new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                LOG.info(evt.getPropertyName() + evt.getNewValue());
                
            }};
        focusDispatcher.addPropertyChangeListener(l);    
        JComboBox box = new JComboBox(new Object[] { 1, 2, 3, 4, "something longer ....." });
        box.setEditable(true);
        JComponent comp = new JXPanel();
        comp.add(new JTextField("intial focus goes here, maybe ..."));
        comp.add(picker);
        comp.add(box);
        JXFrame frame = wrapInFrame(comp, "simple focus dispatch");
        show(frame);
    }

    
    @Test
    public void testEventDispatched() {
        final JXXDatePicker picker = getRealizedDatePicker();
        picker.setInputEventDispatcher(report);
        FocusEvent event = new FocusEvent(picker, FocusEvent.FOCUS_GAINED);
        picker.dispatchEvent(event);
        // too specific for the real world: we get more events ...
//        assertEquals(1, report.getEventCount());
        assertTrue(report.hasEvents());
        // hmm ... event sequence un-specified?
        assertSame(event, report.getLastEvent());
    }

    @Test
    public void testInputContext() {
        final JXXDatePicker picker = getRealizedDatePicker();
        picker.setInputEventDispatcher(emptyInputEventDispatcher);
        assertTrue("input context must be dispatching but was " + picker.getInputContext(), 
                picker.getInputContext() instanceof DispatchingInputContext);
    }
    
    @Test
    public void testDispatcher() {
        picker.setInputEventDispatcher(emptyInputEventDispatcher);
        assertSame(emptyInputEventDispatcher, picker.getInputEventDispatcher());
    }
    
   
    @Test
    public void testDispatcherInitial() {
        assertNull(picker.getInputEventDispatcher());
    }
    
    /**
     * fails ... no window parent, so we have no input context.
     */
//    @Test
//    public void testInputContext() {
//        JXDatePicker picker = new JXDatePicker();
//        assertNotNull(picker.getInputContext());
//    }
    
    /**
     * Fails if there is no realized window parent, so we have no input context.
     * @throws InvocationTargetException 
     * @throws InterruptedException 
     */
    @Test
    public void testInputContextWindow()  {
        // This test will not work in a headless configuration.
        if (GraphicsEnvironment.isHeadless()) {
            LOG.info("cannot run testInputContextWindow - headless environment");
            return;
        }
        final JXDatePicker picker = getRealizedDatePicker();
        assertNotNull(picker.getInputContext());
        assertFalse(picker.getInputContext() instanceof DispatchingInputContext);
    }

    /**
     * @return
     */
    private JXXDatePicker getRealizedDatePicker() {
        JXFrame frame = new JXFrame();
        final JXXDatePicker picker = new JXXDatePicker();
        frame.add(picker);
        frame.pack();
        frame.setVisible(true);
        return picker;
    }

    private InputEventDispatcher createEmptyDispatcher() {
        InputEventDispatcher d = new InputEventDispatcher() {

            public void dispatchEvent(AWTEvent e) {
                // TODO Auto-generated method stub
                
            }};
        return d;
    }
    
    
    @Before
    @Override
    public void setUp() {
        picker = new JXXDatePicker();
        emptyInputEventDispatcher = createEmptyDispatcher();
        report = new InputEventDispatcherReport();
    }

    /**
     * * This subclass version allows to set an InputEventDispatcher which can
     * be implemented to get notification of Input- and FocusEvents for this and
     * all children.
     */
    public static class JXXDatePicker extends JXDatePicker {
        // ---------------- hook for InputEventDispatcher

        private DispatchingInputContext dispatchingContext;

        public InputEventDispatcher getInputEventDispatcher() {
            return getDispatchingInputContext().getInputEventDispatcher();
        }

        public void setInputEventDispatcher(InputEventDispatcher dispatcher) {
            getDispatchingInputContext().setInputEventDispatcher(dispatcher);
        }

        @Override
        public InputContext getInputContext() {
            return getDispatchingInputContext().getInputContext(
                    super.getInputContext());
        }

        /**
         * @return the dispatchingContext
         */
        private DispatchingInputContext getDispatchingInputContext() {
            if (dispatchingContext == null) {
                dispatchingContext = new DispatchingInputContext();
            }
            return dispatchingContext;
        }

    }

}
