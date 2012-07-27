/*
 * Created on 09.03.2009
 *
 */
package core.combo16;

import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Date;
import java.util.logging.Logger;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.jdesktop.swingx.InteractiveTestCase;
import org.jdesktop.swingx.JXDatePicker;
import org.jdesktop.swingx.JXFrame;

public class ComboExperiments extends InteractiveTestCase {
    @SuppressWarnings("unused")
    private static final Logger LOG = Logger.getLogger(ComboExperiments.class
            .getName());
    public static void main(String[] args) {
        setSystemLF(true);
        ComboExperiments test = new ComboExperiments();
        try {
//            test.runInteractiveTests();
            test.runInteractiveTests("interactive.*Debug.*");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void interactiveDebugComboWindows() {
        JXXComboBox box = new JXXComboBox(new Object[]{100, 3000, 400000000});
        box.setEditable(true);
        showInFrame(box, "xcombo");
    }
    
    /**
     * Core issue #6550847: Regression - default button triggered if enter used for commit.
     * This is non-catastrophic by accident only: the combo's setSelectedItem is called 
     * before the default action is triggered. As notification sequence is undefined ...
     */
    public void interactiveDefaultButton() {
        final JComboBox box = new JComboBox(new Object[] {"one", "two", "three"});
        box.setEditable(true);
        box.setAction(new AbstractAction("what?") {

            @Override
            public void actionPerformed(ActionEvent e) {
                LOG.info("in action from: " + e.getSource().getClass().getName() + 
                        "\n   --  " + e);

            } });
        Action defaultAction = new AbstractAction("save selected") {

            @Override
            public void actionPerformed(ActionEvent e) {
                LOG.info("save selected: " + box.getSelectedItem());
                
            }
            
        };
        JButton button = new JButton(defaultAction);
        JComponent content = new JPanel();
        content.add(box);
        content.add(button);
        JTextField field = new JTextField("I'm editable");
        JTextField other = new JTextField("I'm not editable");
        other.setEditable(false);
        content.add(field);
        content.add(other);
        JXFrame frame = wrapInFrame(content, "actions fired - default triggered");
        frame.getRootPane().setDefaultButton(button);
        addStatusMessage(frame, "edit combo value, press enter");
        show(frame);
        
    }
    /**
     * Core issue #??: setting model doesn't fire ItemEvent.
     */
    public void interactiveSetModel() {
        final ComboBoxModel model1 = new DefaultComboBoxModel(new Object[] {"one", "two", "three"});
        final ComboBoxModel model2 = new DefaultComboBoxModel(new Object[] {"four", "six", "eight"});
        final JComboBox box = new JComboBox(model1);
        ItemListener l = new ItemListener(){

            @Override
            public void itemStateChanged(ItemEvent e) {
                LOG.info("item changed" + e);
                
            }};
        box.addItemListener(l);
        JXFrame frame = wrapInFrame(box, "ItemEvent");
        Action setByModel = new AbstractAction("toggelModel") {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                box.setModel(box.getModel() == model1 ? model2 : model1);
                
            }
            
        };
        addAction(frame, setByModel);
        Action unselectByModel = new AbstractAction("unselectByModel") {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                Object selected = box.getSelectedItem();
                box.getModel().setSelectedItem(selected == null ? box.getItemAt(0) : null);
                
            }
            
        };
        addAction(frame, unselectByModel);
        Action unselectByCombo = new AbstractAction("unselectByCombo") {

            @Override
            public void actionPerformed(ActionEvent e) {
               Object selected = box.getSelectedItem();
               box.setSelectedItem(selected == null ? box.getItemAt(0) : null);
                
            }
            
        };
        addAction(frame, unselectByCombo);
        show(frame);
    }
    
    /**
     * Core issue #??: combo doesn't respect model (different behaviour of 
     * setSelectedItem by model vs. by combo)
     */
    public void interactiveSetSelected() {
        final JComboBox box = new JComboBox(new Object[] {"one", "two", "three"});
        JXFrame frame = wrapInFrame(box, "set selected");
        Action setByModel = new AbstractAction("by model") {

            @Override
            public void actionPerformed(ActionEvent e) {
               Object selected = box.getSelectedItem();
               box.getModel().setSelectedItem("a" + selected);
                
            }
            
        };
        addAction(frame, setByModel);
        Action setByCombo = new AbstractAction("by view") {

            @Override
            public void actionPerformed(ActionEvent e) {
                Object selected = box.getSelectedItem();
                box.setSelectedItem("a" + selected);
                
            }
            
        };
        addAction(frame, setByCombo);
        show(frame);
    }
    
    /**
     * Core Issue #??: combo's default action command not reset after nulling action
     */
    public void testActionCommand() {
        JComboBox box = new JComboBox();
        String actionCommand = box.getActionCommand();
        // sanity
        assertEquals("default actionCommand", "comboBoxChanged", actionCommand);
        Action action = new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                
            }
            
        };
        String customCommand = "customCommand";
        action.putValue(Action.ACTION_COMMAND_KEY, customCommand);
        box.setAction(action);
        assertEquals("custom command", customCommand, box.getActionCommand());
        box.setAction(null);
        assertEquals("default actionCommand", actionCommand, box.getActionCommand());
    }
    
    
}
