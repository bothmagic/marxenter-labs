/*
 * Created on 16.04.2009
 *
 */
package org.jdesktop.swingx.event;

import java.awt.event.MouseEvent;
import java.awt.im.InputContext;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Date;
import java.util.logging.Logger;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;

import org.jdesktop.swingx.InteractiveTestCase;
import org.jdesktop.swingx.JXDatePicker;
import org.jdesktop.swingx.JXPanel;

public class InputContextVisualCheck extends InteractiveTestCase {

    @SuppressWarnings("unused")
    private static final Logger LOG = Logger
            .getLogger(InputContextVisualCheck.class.getName());
    
    public static void main(String[] args) {
        InputContextVisualCheck test = new InputContextVisualCheck();
        // KEEP this is global state - uncomment for debug painting completely
//        UIManager.put(JXMonthView.uiClassID, "org.jdesktop.swingx.plaf.basic.BasicSpinningMonthViewUI");
        UIManager.put("JXDatePicker.forceZoomable", Boolean.TRUE);
        try {
            test.runInteractiveTests();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    public void interactiveMouseDispatcher() {
        JXXPanel panel = new JXXPanel();
        panel.setName("dispatcher-aware-panel");
        // add some components inside
        JTextField comp = new JTextField("something to .... focus");
        comp.setEditable(false);
        panel.add(comp);
        panel.add(new JXDatePicker(new Date()));
        JComboBox combo = new JComboBox(new Object[] {"dooooooooo", 1, 2, 3, 4 });
        combo.setEditable(true);
        panel.add(new JButton("something else to ... focus"));
        panel.add(combo);
        JLabel label = new JLabel("with tooltip");
        label.setToolTipText("my Tooltip - how sweeeet");
        panel.add(label);
        label.setName("tooltip");
        panel.add(new JLabel("without tooltip"));
        panel.setBorder(new TitledBorder("has focus dispatcher"));
        // register the compound dispatcher
        AbstractInputEventDispatcher focusDispatcher = new AbstractInputEventDispatcher() {

            /** 
             * @inherited <p>
             */
            @Override
            protected void processMouseEvent(MouseEvent e) {
                LOG.info("got mouse from: " + e.getSource().getClass().getName() + e.getPoint());
            }
            
        };
        panel.setInputEventDispatcher(focusDispatcher);
            
            // add some components outside
            JXPanel other = new JXPanel();
            JTextField outside = new JTextField(" I'm outside ..");
            outside.setEditable(false);
            other.add(outside);
            other.add(new JButton("me tooooo!"));
            other.setBorder(new TitledBorder("outside"));
            JComponent box = Box.createVerticalBox();
            box.add(panel);
            box.add(other);
            showInFrame(box, "FocusDispatcher (on upper panel)");
    }
    
    
    public void interactiveFocusDispatcherFocusedProperty() {
        JXXPanel panel = new JXXPanel();
        panel.setName("dispatcher-aware-panel");
        // add some components inside
        panel.add(new JTextField("something to .... focus"));
        panel.add(new JXDatePicker(new Date()));
        JComboBox combo = new JComboBox(new Object[] {"dooooooooo", 1, 2, 3, 4 });
        combo.setEditable(true);
        panel.add(new JButton("something else to ... focus"));
        panel.add(combo);
        panel.setBorder(new TitledBorder("has focus dispatcher"));
        // register the compound dispatcher
        AbstractInputEventDispatcher focusDispatcher = new CompoundFocusEventDispatcher(panel);
        panel.setInputEventDispatcher(focusDispatcher);
        PropertyChangeListener l = new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                LOG.info("focused on panel" + evt.getNewValue());
                
            }};
        focusDispatcher.addPropertyChangeListener(l);    
        
        // add some components outside
        JXPanel other = new JXPanel();
        other.add(new JTextField(" I'm outside .."));
        other.add(new JButton("me tooooo!"));
        other.setBorder(new TitledBorder("outside"));
        JComponent box = Box.createVerticalBox();
        box.add(panel);
        box.add(other);
        showInFrame(box, "FocusDispatcher (on upper panel)");
    }

    /**
     * Quick JXPanel subclass with support for InputEventDispatcher.
     * Used for _really_ testing compound focused property.
     */
    public static class JXXPanel extends JXPanel {
      //---------------- hook for InputEventDispatcher
        
        private DispatchingInputContext dispatchingContext;
        
        public InputEventDispatcher getInputEventDispatcher() {
            return getDispatchingInputContext().getInputEventDispatcher();
        }

        public void setInputEventDispatcher(
                InputEventDispatcher dispatcher) {
            getDispatchingInputContext().setInputEventDispatcher(dispatcher);
        }
        
        @Override
        public InputContext getInputContext() {
            return getDispatchingInputContext().getInputContext(super.getInputContext());
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
