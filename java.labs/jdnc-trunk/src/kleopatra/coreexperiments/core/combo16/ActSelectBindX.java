/*
 * Created on 04.03.2009
 *
 */
package core.combo16;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Date;
import java.util.logging.Logger;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Box;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultTreeCellEditor;
import javax.swing.tree.DefaultTreeCellRenderer;

import org.jdesktop.swingx.JXDatePicker;
import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.util.WindowUtils;

/**
 * Compares ActionEvent firing of core components (with external tweaks).
 */
public class ActSelectBindX {
    @SuppressWarnings("unused")
    private static final Logger LOG = Logger.getLogger(ActSelectBindX.class
            .getName());
    private Action action;
    private JButton button;
    private ItemListener itemListener;
    
    private void configureComboBox(JComboBox box, boolean editable) {
//        box.setUI(new TweakedWindowsComboBoxUI());
        box.setEditable(editable);
        box.setAction(getAction());
        box.addItemListener(getItemListener());
    }
    private ItemListener getItemListener() {
        if (itemListener == null) {
            itemListener = new ItemListener() {

                @Override
                public void itemStateChanged(ItemEvent e) {
                    // TODO Auto-generated method stub
                    LOG.info(" --- " + e);
                }};
        }
        return itemListener;
    }
    /**
     * @param editable
     * @return
     */
    private JComboBox createComboBox(boolean editable) {
        JComboBox box = new JXXComboBox(new Object[] {"one", "two", "three"}) {

            @Override
            protected void fireActionEvent() {
                super.fireActionEvent();
            }

            @Override
            public void actionPerformed(ActionEvent e) {
                super.actionPerformed(e);
            }
            
            
            
        };
        configureComboBox(box, editable);
        return box;
    }


    private JComponent createTables() {
        String[] header = new String[] { "Combo (not editable)", "Combo (editable)", "Formatted"} ;
        Object[] data = new Object[] { "somestuff" , "otherstuff", "haha... "};
        DefaultTableModel model = new DefaultTableModel(header, 10);
        model.insertRow(0, data);
        JTable table = new JTable(model);
        int column = 0;
        JComboBox box = createComboBox(false);
        table.getColumnModel().getColumn(column++).setCellEditor(new DefaultCellEditor(box));
        
        JComboBox boxEditable = createComboBox(true);
        table.getColumnModel().getColumn(column++).setCellEditor(new DefaultCellEditor(boxEditable));
        
        JFormattedTextField field = new JFormattedTextField();
        table.getColumnModel().getColumn(column++).setCellEditor(new DefaultCellEditor(field));
        
        JComponent content = Box.createHorizontalBox();
        content.add(new JScrollPane(table));
        return content;
    }
    
    private JComponent createTrees() {
        JTree treeCombo = new JTree();
        treeCombo.getModel().valueForPathChanged(treeCombo.getPathForRow(0), "Combo (not editable)");
        treeCombo.setEditable(true);
        JComboBox box = new JComboBox(new Object[] {"one", "two", "three"});
        treeCombo.setCellEditor(new DefaultTreeCellEditor(treeCombo, 
                (DefaultTreeCellRenderer) treeCombo.getCellRenderer(), 
                new DefaultCellEditor(box)));
        
        JTree treeComboEditable = new JTree();
        treeComboEditable.getModel().valueForPathChanged(treeComboEditable.getPathForRow(0), "Combo (editable)");
        treeComboEditable.setEditable(true);
        JComboBox boxEditable = new JComboBox(new Object[] {"one", "two", "three"});
        boxEditable.setEditable(true);
        treeComboEditable.setCellEditor(new DefaultTreeCellEditor(treeComboEditable, 
                (DefaultTreeCellRenderer) treeComboEditable.getCellRenderer(), 
                new DefaultCellEditor(boxEditable)));
        
        JTree treeFormatted = new JTree();
        treeFormatted.getModel().valueForPathChanged(treeFormatted.getPathForRow(0), "FormattedTextField");
        treeFormatted.setEditable(true);
        JFormattedTextField field = new JFormattedTextField();
        treeFormatted.setCellEditor(new DefaultTreeCellEditor(treeFormatted, 
                (DefaultTreeCellRenderer) treeFormatted.getCellRenderer(),
                new DefaultCellEditor(field)));
        
        JComponent content = Box.createHorizontalBox();
        content.add(new JScrollPane(treeCombo));
        content.add(new JScrollPane(treeComboEditable));
        content.add(new JScrollPane(treeFormatted));
        return content;
    }
    
    
    
    /**
     * @return
     */
    private JComponent createInputActors() {
        JXDatePicker picker = new JXDatePicker(new Date());
        JTextField simpleField = new JTextField("simple field");
        JTextField simpleFieldWithoutAction = new JTextField("simpleFieldWithoutAction");
        JFormattedTextField textField = new JFormattedTextField(); 
        textField.setValue("formatted field");
        JComboBox box = createComboBox(true);
        JComboBox nonEditableBox = createComboBox(false);
        JSpinner spinner = new JSpinner();
        JCheckBox checkBox = new JCheckBox();
        button = new JButton();
        // picker has no action property
        picker.addActionListener(getAction());
        simpleField.setAction(getAction());
        textField.setAction(getAction());
        box.setAction(getAction());
        nonEditableBox.setAction(getAction());
        // spinner has no action property and no actionListener
//        spinner.setAction(getAction());
        checkBox.setAction(getAction());
        button.setAction(getAction());
        JComponent bar = new JPanel();
        bar.add(picker);
        bar.add(simpleField);
        bar.add(simpleFieldWithoutAction);
        bar.add(textField);
        bar.add(box);
        bar.add(nonEditableBox);
        bar.add(spinner);
        bar.add(checkBox);
        bar.add(button);
        return bar;
    }

    private Action getAction() {
        if (action == null) {
            action = new AbstractAction("sharedAction") {

                @Override
                public void actionPerformed(ActionEvent e) {
                    LOG.info("got action from: " + e.getSource().getClass().getName() + 
                            "\n   --  " + e);

                }
                
            };
            LOG.info("command " + action.getValue(Action.ACTION_COMMAND_KEY));
        }
        return action;
    }

    
    private JButton getDefaultButton() {
        return button;
    }
    
    private JComponent createContent() {
        JComponent content = new JPanel(new BorderLayout());
        content.add(createInputActors(), BorderLayout.NORTH);
        content.add(createTrees(), BorderLayout.WEST);
        content.add(createTables(), BorderLayout.EAST);
        return content;
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                initLF(true);
                JXFrame frame = new JXFrame("ActSelectBind - tweaked", true);
                ActSelectBindX form = new ActSelectBindX();
                frame.add(form.createContent());
                frame.getRootPane().setDefaultButton(form.getDefaultButton());
                frame.pack();
                frame.setLocation(WindowUtils.getPointForCentering(frame));
                frame.setVisible(true);
            }

            private void initLF(boolean useSystemLAF) {
                String laf = useSystemLAF ? UIManager.getSystemLookAndFeelClassName() : 
                    UIManager.getCrossPlatformLookAndFeelClassName();
                try {
                    UIManager.setLookAndFeel(laf);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } 
                
            }
        });
    }

}
