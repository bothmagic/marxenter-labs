/*
 * Created on 21.06.2007
 *
 */
package org.jdesktop.beansbindingx.example;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.TableCellRenderer;

import org.jdesktop.appframework.beansbinding.album.Album;
import org.jdesktop.appframework.beansbinding.album.BAlbumManager;
import org.jdesktop.appframework.beansbinding.album.BAlbumManagerModel;
import org.jdesktop.appframework.swingx.BBColumnFactory;
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
 * Bind album list and navigation to xtable. 
 * Bind selected artist property to text 
 * (funny commit? jumps to field start on typing?)
 */
public class SimpleTableBinding {

    private JComponent content;
    private JXTable table;
    private BAlbumManagerModel albumManager;
    private JTextField textField;
    private JButton nextButton;


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

        // quick check for binding table's selected element directly
//        context.addBinding(Bindings.createAutoBinding(UpdateStrategy.READ_WRITE,
//                table, BeanProperty.create("selectedElement.artist"), 
//                textField, BeanProperty.create("text_ON_ACTION_OR_FOCUS_LOST")));
        // quick check for binding text to navigation
        context.addBinding(Bindings.createAutoBinding(UpdateStrategy.READ_WRITE,
                albumManager.getNavigation(), BeanProperty.create("selectedElement.artist"), 
                textField, BeanProperty.create("text_ON_ACTION_OR_FOCUS_LOST")));
        context.bind();
        Action next = new AbstractAction("Next") {

            public void actionPerformed(ActionEvent e) {
                albumManager.getNavigation().next();
                
            }
            
        };
        nextButton.setAction(next);
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
        comp.add(nextButton, BorderLayout.SOUTH);
        return comp;
    }


    private void initComponents() {
        table = new JXTable() 
//        {
//            public Component prepareRenderer(TableCellRenderer renderer,
//                    int row, int column) {
//                Component c = super.prepareRenderer(renderer, row, column);
//                Boolean b = (Boolean) getModel().getValueAt(row, 2);
//                if (b)
//                    c.setBackground(Color.ORANGE);
//                else 
//                    c.setBackground(Color.LIGHT_GRAY);
//                return c;
//            }
//
//        }
        ;
//        table.setColumnControlVisible(true);
        textField = new JTextField();
        nextButton = new JButton();
    }

    public static void main(String[] args) {
        
        final JXFrame frame = new JXFrame("Bind Album List", true);
        ColumnFactory.setInstance(new BBColumnFactory());
        frame.add(new SimpleTableBinding().getContent());
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
