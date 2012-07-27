/*
 * Created on 04.03.2009
 *
 */
package core.combo16;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.util.logging.Logger;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Box;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.plaf.basic.BasicComboBoxEditor;
import javax.swing.table.DefaultTableModel;

import core.combo16.plaf.TweakedBasicComboBoxUI;


public class TableComboEditor {

    @SuppressWarnings("unused")
    private static final Logger LOG = Logger.getLogger(TableComboEditor.class
            .getName());
    private JButton defaultButton;
    private JComponent createContent() {
        Object[] data = new Object[] { "somestuff" , "otherstuff", "haha... "};
        DefaultTableModel model = new DefaultTableModel(10, data.length);
        model.insertRow(0, data);
        JTable table = new JTable(model);
        // not-editable combo as cell editor
        JComboBox box = new JComboBox(new Object[] {"one", "two", "three"});
        configureComboBox(box, false);
        table.getColumnModel().getColumn(0).setCellEditor(new DefaultCellEditor(box));
        
        // editable combo as cell editor
        JComboBox editable = new JComboBox(new Object[] {"one", "two", "three"});
        configureComboBox(editable, true);
        table.getColumnModel().getColumn(1).setCellEditor(new DefaultCellEditor(editable));
        
        
        JComboBox standAlone = new JComboBox(new Object[] {"one", "two", "three"});
        configureComboBox(standAlone, false);
        
        JComboBox standAloneEditable = new JComboBox(new Object[] {"one", "two", "three"});
        configureComboBox(standAloneEditable, true);
        
        Action dummy = new AbstractAction("default button") {

            @Override
            public void actionPerformed(ActionEvent e) {
                LOG.info("action " + e);
                
            }
            
        };
        standAloneEditable.addActionListener(dummy);
        standAlone.addActionListener(dummy);
        defaultButton = new JButton(dummy);
        
        JComponent bar = Box.createHorizontalBox();
        bar.add(standAlone);
        bar.add(standAloneEditable);
        JComponent content = new JPanel(new BorderLayout());
        content.add(bar, BorderLayout.NORTH);
        content.add(new JScrollPane(table));
        content.add(defaultButton, BorderLayout.SOUTH);
        return content;
    }
    
    public void configureComboBox(JComboBox box, boolean editable) {
        box.setUI(new TweakedBasicComboBoxUI());
        box.setEditable(editable);
    }

    public static class QuickOverrideComboBoxEditor  extends BasicComboBoxEditor {
        
        public QuickOverrideComboBoxEditor() {
            super();
            editor = new JFormattedTextField();
        }

        @Override
        public void setItem(Object anObject) {
            ((JFormattedTextField) editor).setValue(anObject);
        }
        
        
    }
    
    protected  JButton getDefaltButton() {
        return defaultButton;
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JFrame frame = new JFrame("TableComboEditor");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                TableComboEditor tableComboEditor = new TableComboEditor();
                frame.add(tableComboEditor.createContent());
                frame.getRootPane().setDefaultButton(tableComboEditor.getDefaltButton());
                frame.pack();
                frame.setVisible(true);
            }
        });
    }
    
}
