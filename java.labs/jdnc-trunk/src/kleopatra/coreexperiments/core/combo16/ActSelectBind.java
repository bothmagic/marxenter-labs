/*
 * Created on 04.03.2009
 *
 */
package core.combo16;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Date;
import java.util.Enumeration;
import java.util.logging.Logger;

import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
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
 * Compares ActionEvent firing of core components (no external tweaks).
 */
public class ActSelectBind {
    @SuppressWarnings("unused")
    private static final Logger LOG = Logger.getLogger(ActSelectBind.class
            .getName());
    private Action action;
    private JButton button;

    private JComponent createTables() {
        String[] header = new String[] { "Combo (not editable)", "Combo (editable)", "Formatted"} ;
        Object[] data = new Object[] { "somestuff" , "otherstuff", "haha... "};
        DefaultTableModel model = new DefaultTableModel(header, 10);
        model.insertRow(0, data);
        JTable table = new JTable(model);
        int column = 0;
        JComboBox box = new JComboBox(new Object[] {"one", "two", "three"});
        table.getColumnModel().getColumn(column++).setCellEditor(new DefaultCellEditor(box));
        
        JComboBox boxEditable = new JComboBox(new Object[] {"one", "two", "three"});
        boxEditable.setEditable(true);
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
        JComboBox box = new JComboBox(new Object[] {"one", "two", "three"});
        box.setEditable(true);
        JComboBox nonEditableBox = new JComboBox(new Object[] {"one", "two", "three"});
        JSpinner spinner = new JSpinner();
        JCheckBox checkBox = new JCheckBox();
        button = new JButton();
        // picker has no action property
        picker.addActionListener(getAction());
        picker.addPropertyChangeListener(getDateListener());
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

    private PropertyChangeListener getDateListener() {
        PropertyChangeListener l = new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if ("date".equals(evt.getPropertyName())) {
                    LOG.info("  ++ date " + evt);
                }
            }};
        return l;
    }

    private Component createButtonGroup() {
        JComponent bar = Box.createHorizontalBox();
        final ButtonGroup group = new ButtonGroup();
        bar.add(createRadioButton(group, "one"));
        bar.add(createRadioButton(group, "two"));
        bar.add(createRadioButton(group, "three"));
        Action action = new AbstractAction("select first") {

            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
                Enumeration<AbstractButton> buttons = group.getElements();
                buttons.nextElement().setSelected(true);
            }
            
        };
        bar.add(new JButton(action));
        return bar;
    }

    /**
     * @param group
     * @param text
     * @return
     */
    private JRadioButton createRadioButton(ButtonGroup group, String text) {
        JRadioButton one = new JRadioButton(text);
        ActionListener l = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                LOG.info("from radioButton: " + e);
                
            }
            
        };
        ItemListener item = new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                LOG.info("from radioButton: " + e);
                
            }
            
        };
        one.addActionListener(l);
        one.addItemListener(item);
        group.add(one);
        return one;
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
        content.add(createButtonGroup(), BorderLayout.SOUTH);
        return content;
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                initLF(true);
                JXFrame frame = new JXFrame("ActSelectBind", true);
                ActSelectBind form = new ActSelectBind();
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
