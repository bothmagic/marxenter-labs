/*
 * Created on 18.03.2009
 *
 */
package core.combo16.plaf;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeListener;
import java.util.logging.Logger;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicComboPopup;

import org.jdesktop.swingx.InteractiveTestCase;
import org.jdesktop.swingx.JXFrame;

import core.combo16.JXXComboBox;

public class ComboBoxUIExperiments extends InteractiveTestCase {

    @SuppressWarnings("unused")
    private static final Logger LOG = Logger
            .getLogger(ComboBoxUIExperiments.class.getName());
    private JComboBox comboBox;
    private TestComboPopup comboPopup;
    
    public static void main(String[] args) {
        ComboBoxUIExperiments test = new ComboBoxUIExperiments();
        try {
            test.runInteractiveTests();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    /**
     * 
     */
    public void interactiveComboPopupCompareNotEditable() {
        JComboBox comboBox = createComboBox(false);
        JComboBox box = createComboBoxWithXPopup(false);
        JComboBox xxBox = createXXComboBoxWithXPopup(false);
        JComboBox tweaked = createTweakedComboBoxWithXPopup(false);
        JComponent comp = new JPanel();
        comp.add(comboBox);
        comp.add(box);
        comp.add(xxBox);
        comp.add(tweaked);
        JXFrame frame = wrapInFrame(comp, "core <--> xpopup <--> xcombo <--> tweaked");
        Action defaultAction = new AbstractAction("default button") {

            @Override
            public void actionPerformed(ActionEvent e) {
                LOG.info(" default " + e);
            }};
        JButton button = new JButton(defaultAction);
        frame.getRootPane().setDefaultButton(button);
        addStatusComponent(frame, button);
        show(frame);
    }
    
    /**
     * 
     */
    public void interactiveComboPopupCompareEditable() {
        JComboBox comboBox = createComboBox(true);
        JComboBox box = createComboBoxWithXPopup(true);
        JComboBox xxBox = createXXComboBoxWithXPopup(true);
        JComboBox tweaked = createTweakedComboBoxWithXPopup(true);
        JComponent comp = new JPanel();
        comp.add(comboBox);
        comp.add(box);
        comp.add(xxBox);
        comp.add(tweaked);
        JXFrame frame = wrapInFrame(comp, "core <--> xpopup <--> xcombo <--> tweaked");
        Action defaultAction = new AbstractAction("default button") {

            @Override
            public void actionPerformed(ActionEvent e) {
                LOG.info(" default " + e);
            }};
        JButton button = new JButton(defaultAction);
        frame.getRootPane().setDefaultButton(button);
        addStatusComponent(frame, button);
        show(frame);
    }
    /**
     * Creates and configures a standard comboBox with standard ui (opened for 
     * testing access only).
     */
    private JComboBox createComboBox(boolean editable) {
        JComboBox comboBox = new JComboBox(new String[] {"one", "two", "three"});
        comboBox.setUI(new TestComboBoxUI());
        comboBox.setEditable(editable);
        addListeners(comboBox);
        return comboBox;
    }
    
    /**
     * Creates and configures a standard comboBox with xPopup.
     */
    private JComboBox createComboBoxWithXPopup(boolean editable) {
        JComboBox comboBox = new JComboBox(new String[] {"one", "two", "three"});
        comboBox.setUI(new TestXComboBoxUI());
        comboBox.setEditable(editable);
        addListeners(comboBox);
        return comboBox;
    }
    
    /**
     * Creates and configures a xComboBox with xPopup.
     * The goal is to reduce action event firing. 
     */
    private JComboBox createXXComboBoxWithXPopup(boolean editable) {
        JComboBox comboBox = new JXXComboBox(new String[] {"one", "two", "three"});
        comboBox.setUI(new TestXComboBoxUI());
        comboBox.setEditable(editable);
        addListeners(comboBox);
        return comboBox;
    }

    /**
     * Creates and configures a xComboBox with xPopup.
     * The goal is to reduce action event firing. 
     */
    private JComboBox createTweakedComboBoxWithXPopup(boolean editable) {
        JComboBox comboBox = new JXXComboBox(new String[] {"one", "two", "three"});
        comboBox.setUI(new TweakedBasicComboBoxUI());
        comboBox.setEditable(editable);
        addListeners(comboBox);
        return comboBox;
    }
    private void addListeners(JComboBox comboBox) {
        ActionListener l = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                LOG.info("action " + e);
            }};
        ItemListener item = new ItemListener(){

            @Override
            public void itemStateChanged(ItemEvent e) {
                LOG.info(" -- item " + e);
            }};
        comboBox.addActionListener(l);
        comboBox.addItemListener(item);
    }


    /**
     * Core issue #??: BasicComboPopup doesn't synch list selection on deselection.
     */
    public void testPopupDeselection() {
        assertEquals(0, comboBox.getSelectedIndex());
        assertEquals(0, comboPopup.getList().getSelectedIndex());
        comboBox.getModel().setSelectedItem(null);
        assertEquals(-1, comboBox.getSelectedIndex());
        assertEquals(-1, comboPopup.getList().getSelectedIndex());
    }

    /**
     * Core issue #??: propertyChangeListener property of popup not nulled after uninstall.
     * 
     * That's inconsistent of how the listeners to the list (and the handler) are nulled. 
     * Not a big problem, though, because the reverse link (popup field) in the comboBoxUI is nulled. 
     * Should be gc'able. 
     */
    public void testPopupPropertyChangeListenerReleased() {
        assertNotNull(comboPopup.getPropertyChangeListener());
        PropertyChangeListener p = comboPopup.getPropertyChangeListener();
        assertPropertyChangeListenerAdded(comboBox, p);
        comboPopup.uninstallingUI();
        assertPropertyChangeListenerRemoved(comboBox, p);
        assertNull("property change listener must be nulled", comboPopup.getPropertyChangeListener());
    }
    
    private void assertPropertyChangeListenerRemoved(JComponent comboBox,
            PropertyChangeListener propertyChangeListener) {
        PropertyChangeListener[] listeners = comboBox.getPropertyChangeListeners();
        for (PropertyChangeListener l : listeners) {
            if (l == propertyChangeListener) 
            fail("property change listener not removed");
        }
    }

    private void assertPropertyChangeListenerAdded(JComponent comboBox,
            PropertyChangeListener propertyChangeListener) {
        PropertyChangeListener[] listeners = comboBox.getPropertyChangeListeners();
        for (PropertyChangeListener l : listeners) {
            if (l == propertyChangeListener) return;
        }
        fail("property change listener not added");
    }

    @Override
    protected void setUp() throws Exception {
        comboBox = new JComboBox(new String[] {"one", "two", "three"});
        comboBox.setUI(new TestComboBoxUI());
        comboPopup = ((TestComboBoxUI) comboBox.getUI()).getComboPopup();
//        addListeners(comboBox);
    }

//------------------ UI to test ext XComboPopup  
    
    /**
     * Overridden to plug-in a BasicXComboPopup.
     */
    public static class TestXComboBoxUI extends BasicComboBoxUI {
        public BasicXComboPopup getComboPopup() {
            return (BasicXComboPopup) popup;
        }

        @Override
        protected BasicXComboPopup createPopup() {
            return new BasicXComboPopup(comboBox);
        }
        
    }
    
//------------------ extending core classes to access internal state for testing    
    /**
     * Overridden to access internal state.
     */
    public static class TestComboBoxUI extends BasicComboBoxUI {
        
        public TestComboPopup getComboPopup() {
            return (TestComboPopup) popup;
        }

        @Override
        protected TestComboPopup createPopup() {
            return new TestComboPopup(comboBox);
        }
        
        
    };
    
    /**
     * Overridden to access internal state.
     */
    public static class TestComboPopup extends BasicComboPopup {

        public TestComboPopup(JComboBox combo) {
            super(combo);
        }
        
        protected PropertyChangeListener getPropertyChangeListener() {
            return propertyChangeListener;
        }
    }
    
}
