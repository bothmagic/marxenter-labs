/*
 * Created on 21.06.2007
 *
 */
package org.jdesktop.beansbindingx.example;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import org.jdesktop.appframework.beansbinding.album.Album;
import org.jdesktop.appframework.beansbinding.album.BAlbumManager;
import org.jdesktop.appframework.beansbinding.album.BAlbumManagerModel;
import org.jdesktop.appframework.swingx.BBColumnFactory;
import org.jdesktop.beans.AbstractBean;
import org.jdesktop.beansbinding.BeanProperty;
import org.jdesktop.beansbinding.BindingGroup;
import org.jdesktop.beansbinding.Bindings;
import org.jdesktop.beansbinding.AutoBinding.UpdateStrategy;
import org.jdesktop.swingbinding.JTableBinding;
import org.jdesktop.swingbinding.SwingBindings;
import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.table.ColumnFactory;

/**
 * Goal: bind the rowCount property.
 * 
 * Implement an adapter ... hmmm ...
 * 
 */
public class BindTableRowCount {

    private JComponent content;
    private JTable table;
    private BAlbumManagerModel albumManager;
    private JTextField textField;
    private JButton deleteButton;
    private JButton newButton;

    public static class TableRowCountAdapter extends AbstractBean {
        
        private JTable table;
        private TableModelListener tableModelListener;
        private PropertyChangeListener tableListener;
        
        public TableRowCountAdapter(JTable table) {
            this.table = table;
            table.getModel().addTableModelListener(getTableModelListener());
            table.addPropertyChangeListener(getTableListener());
        }

        private PropertyChangeListener getTableListener() {
            if (tableListener == null) {
                tableListener = new PropertyChangeListener() {

                    public void propertyChange(PropertyChangeEvent evt) {
                        if (!"model".equals(evt.getPropertyName())) return;
                        reinstallTableModelListener((TableModel) evt.getOldValue());
                    }
                    
                };
            }
            return tableListener;
        }

        protected void reinstallTableModelListener(TableModel oldValue) {
            oldValue.removeTableModelListener(getTableModelListener());
            table.getModel().addTableModelListener(getTableModelListener());
            
        }

        public int getRowCount() {
            return table.getRowCount();
        }
        
        private TableModelListener getTableModelListener() {
            if (tableModelListener == null) {
                tableModelListener = new TableModelListener() {

                    public void tableChanged(TableModelEvent e) {
                        if (isModification(e)) {
                            firePropertyChange("rowCount", -1, table.getRowCount());
                        }
                        
                    }

                    private boolean isModification(TableModelEvent e) {
                        // PENDING: this is not complete! Need to check for structure changed
                        if ((e == null) || (e.getType() == TableModelEvent.INSERT)
                                || (e.getType() == TableModelEvent.DELETE)) {
                            return true;
                        }
                        return false;
                    }
                    
                };
            }
            return tableModelListener;
        }
    }

    private void bindBasics() {
        BindingGroup context = new BindingGroup();
        // bind list selected element and elements to albumManagerModel
        // NOTE: binding navigation --> table.selectedElement not working for
        // default JTableAdapterProvider, so we need to bind READ only
        // and be sure to use the ignore-adjustion version - weird problems
        // with JXTable and 
//        context.addBinding(Bindings.createAutoBinding(UpdateStrategy.READ,
//                table, BeanProperty.create("selectedElement_IGNORE_ADJUSTING"),
//                albumManager.getNavigation(), BeanProperty.create("selectedElement"); 
//        ));
        // here is okay as we use JXTableAdapterProvider
        context.addBinding(Bindings.createAutoBinding(UpdateStrategy.READ_WRITE,
                albumManager.getNavigation(), BeanProperty.create("selectedElement"), 
                table, BeanProperty.create("selectedElement_IGNORE_ADJUSTING")
        ));
        JTableBinding tableBinding = SwingBindings.createJTableBinding(UpdateStrategy.READ_WRITE,
                albumManager.getManagedAlbums(), table);
        context.addBinding(tableBinding);
        tableBinding.addColumnBinding(BeanProperty.create("artist"));
        tableBinding.addColumnBinding(BeanProperty.create("title"));
        tableBinding.addColumnBinding(BeanProperty.create("classical"))
             .setColumnClass(Boolean.class);
        tableBinding.addColumnBinding(BeanProperty.create("composer"));

        TableRowCountAdapter adapter = new TableRowCountAdapter(table);
        
        context.addBinding(Bindings.createAutoBinding(UpdateStrategy.READ,
                adapter, BeanProperty.create("rowCount"), 
                textField, BeanProperty.create("text")));
        textField.setEditable(false);
        context.bind();
        // PENDING: this should be done via appframework
        Action delete = new AbstractAction("Delete Selected Album") {

            public void actionPerformed(ActionEvent e) {
                if (!albumManager.isAlbumSelected()) return;
                albumManager.deleteAlbum();
                
            }
            
        };
        deleteButton.setAction(delete);
        Action add = new AbstractAction("Create and Add Album") {

            public void actionPerformed(ActionEvent e) {
                albumManager.newAlbum();
                
            }
            
        };
        newButton.setAction(add);
        
    }

    private void initData() {
        albumManager = new BAlbumManagerModel(new BAlbumManager(Album.ALBUMS));
    }

    private JComponent getContent() {
        if (content == null) {
            ColumnFactory.setInstance(new BBColumnFactory());
            initComponents();
            content = build();
            initData();
            bindBasics();
        }
        return content;
    }

    private JComponent build() {
        JComponent comp = new JPanel(new BorderLayout());
        comp.add(new JScrollPane(table));
        comp.add(textField, BorderLayout.NORTH);
        JComponent buttons = Box.createHorizontalBox();
        buttons.add(deleteButton);
        buttons.add(newButton);
        comp.add(buttons, BorderLayout.SOUTH);
        return comp;
    }


    private void initComponents() {
        table = new JXTable();
//        table.setColumnControlVisible(true);
        textField = new JTextField();
        deleteButton = new JButton();
        newButton = new JButton();
    }

    public static void main(String[] args) {
        
        final JXFrame frame = new JXFrame("Bind Album List", true);
        ColumnFactory.setInstance(new BBColumnFactory());
        frame.add(new BindTableRowCount().getContent());
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
               frame.pack();
//               frame.setSize(800, 600);
               frame.setLocationRelativeTo(null);
               frame.setVisible(true);
            }
        });
    }

}
