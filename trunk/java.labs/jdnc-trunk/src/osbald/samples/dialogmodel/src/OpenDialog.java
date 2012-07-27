package uk.co.osbald.sample;

import org.jdesktop.swingx.JXDialog2;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.util.WindowUtils;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;

/*
 * Created by IntelliJ IDEA.
 * User: Richard Osbaldeston (www.osbald.co.uk)
 * Date: 20-Nov-2006
 * Time: 14:10:03
 */

public class OpenDialog {

    // abstract factory for these.. should they be? feels odd not having an 'instance'

    public static JDialog createDialog(Frame parent, OpenDialogModel model, Action commitAction) {
        JComponent content = createUI(model);
        content.getActionMap().put(JXDialog2.EXECUTE_ACTION_COMMAND, commitAction);
        return new JXDialog2(parent, "Open Model", content);
    }

    static JComponent createUI(final OpenDialogModel model) {
        JPanel content = new JPanel(new GridBagLayout());
        content.setBorder(BorderFactory.createEmptyBorder());
        content.add(new JLabel("Available Modules:"),
                new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                        GridBagConstraints.NONE, new Insets(5, 5, 0, 5), 0, 0));

        final JXTable table = new JXTable(new MyTableModel(model.getModules()));
        table.setVisibleRowCount(10);
        TableCellRenderer dateRenderer = new JXTable.DateRenderer(new SimpleDateFormat("dd/MM/yyyy"));
        table.getColumnModel().getColumn(1).setCellRenderer(dateRenderer);
        content.add(new JScrollPane(table),
                new GridBagConstraints(0, 2, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
                        GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));

        class MySelectionListener implements ListSelectionListener {
            private JXTable table;

            public MySelectionListener(JXTable table) {
                this.table = table;
            }

            public void valueChanged(ListSelectionEvent event) {
                ListSelectionModel listSelectionModel = (ListSelectionModel) event.getSource();
                if (!event.getValueIsAdjusting()) {     //!isValueAdjusting()
                    if (!listSelectionModel.isSelectionEmpty()) {
                        int row = table.convertRowIndexToModel(table.getSelectedRow());
                        model.setSelectedModule(((MyTableModel) table.getModel()).getValue(row));
                    } else {
                        model.setSelectedModule(null);
                    }
                }
            }
        }
        table.getSelectionModel().addListSelectionListener(new MySelectionListener(table));

        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent event) {
                if (event.getClickCount() >= 2) {
                    // not happy with this because its based upon internal knowledge of how our actions are most likley to be registered
                    ActionMap actionMap = table.getRootPane().getActionMap();   //urgh!
                    Action action = actionMap.get(JXDialog2.EXECUTE_ACTION_COMMAND);
                    if (action != null && action.isEnabled()) {
                        action.actionPerformed(new ActionEvent(event.getSource(), event.getID(), JXDialog2.EXECUTE_ACTION_COMMAND));  //urgh2!
                    }
                }
            }
        });

        ActionMap actionMap = content.getActionMap();
        actionMap.put(JXDialog2.CLOSE_ACTION_COMMAND, new AbstractAction("Cancel") {
            public void actionPerformed(ActionEvent event) {
                Window source = WindowUtils.findWindow((Component) event.getSource());
                source.dispatchEvent(new WindowEvent(source, WindowEvent.WINDOW_CLOSING));
            }
        });
        return content;
    }


    static class MyTableModel extends AbstractTableModel {

        private final String[] columns = {"Name", "From", "Id"};
        private java.util.List<Module> items = new ArrayList<Module>();

        public MyTableModel(Collection<Module> items) {
            this.items = new ArrayList<Module>(items);
            fireTableDataChanged();
        }

        public int getRowCount() {
            return items.size();
        }

        public int getColumnCount() {
            return columns.length;
        }

        @Override
        public String getColumnName(int column) {
            return columns[column];
        }

        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }

        public Object getValueAt(int row, int column) {
            Module module = getValue(row);
            if (column == 0) {
                return module.getName();
            } else if (column == 1) {
                return module.getFrom();
            } else if (column == 2) {
                return module.getId();
            } else {
                return "";
            }
        }

        public Module getValue(int row) {
            return items.get(row);
        }
    }
}
