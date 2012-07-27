/**
 * 
 */
package org.jdesktop.swingx;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.MutableComboBoxModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.jdesktop.swingx.combobox.ExtensibleComboBoxModel;

/**
 * 
 * @author Karl George Schaefer
 */
public class ExtensibleComboBoxModelDemo extends JFrame {
    private static class ExtensibleFileComboBoxModel extends ExtensibleComboBoxModel {
        /**
         * @param model
         */
        public ExtensibleFileComboBoxModel(MutableComboBoxModel model) {
            super(model);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected Object generateNewItem() {
            JFileChooser chooser = new JFileChooser();
            chooser.setMultiSelectionEnabled(false);
            
            int returnVal = chooser.showOpenDialog(null);
            
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                return chooser.getSelectedFile();
            }
            
            return newItem;
        }
    }
    
    protected void frameInit() {
        super.frameInit();
        
        setTitle("ExtensibleComboBoxModel Demo");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        ExtensibleComboBoxModel model = new ExtensibleFileComboBoxModel(new DefaultComboBoxModel());
        model.setContents("Add File...");
        JComboBox combo = new JComboBox(model);
        combo.setEditable(false);
        
        add(combo);
        
        pack();
    }
    /**
     * Application entry point.
     * 
     * @param args
     *            unused
     */
    public static void main(String[] args) throws Exception {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new ExtensibleComboBoxModelDemo().setVisible(true);
            }
        });
    }
}
