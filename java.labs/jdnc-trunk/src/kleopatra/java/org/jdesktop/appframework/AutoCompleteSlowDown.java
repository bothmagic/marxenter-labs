/*
 * Created on 31.01.2008
 *
 */
package org.jdesktop.appframework;

import javax.swing.ActionMap;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.jdesktop.application.Application;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

public class AutoCompleteSlowDown extends SingleFrameApplication {
    
    private JPanel    panel;
    private JComboBox combo;
    private JButton   button;
    
    @org.jdesktop.application.Action
    public void simpleAction() {
        JOptionPane.showMessageDialog(getMainFrame(), "Action!");
    }
    
    @Override
    protected void startup() {
        getMainFrame().setTitle("BasicSingleFrameApp");

        String[] data = new String[500];
        for (int i = 0; i < data.length; i++) {
            data[i] = "Test data row " + i;
        }
        
        combo = new JComboBox();
        combo.setModel(new DefaultComboBoxModel(data));
        AutoCompleteDecorator.decorate(combo);
        
        
        // This is the code that slows down navigating in the combo
        // vvv
        ResourceMap resource = getContext().getResourceMap(AutoCompleteSlowDown.class);
        resource.injectComponents(getMainFrame());
        button = new JButton();
        ActionMap map = getContext().getActionMap(); // <-- Why there is slow down after getting the action map?
        // ^^^
        
        button.setAction(map.get("simpleAction"));
        
        panel = new JPanel();
        panel.add(combo);
        panel.add(button);
        
        show(panel);
    }
 
    public static void main(String[] args) {
        Application.launch(AutoCompleteSlowDown.class, args);
    }
}
