/*
 * Created on 21.06.2007
 *
 */
package org.jdesktop.appframework.beansbinding.divers;

import java.awt.BorderLayout;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import org.jdesktop.appframework.beansbinding.album.Album;
import org.jdesktop.appframework.beansbinding.album.BAlbumManager;
import org.jdesktop.appframework.beansbinding.album.BAlbumManagerModel;
import org.jdesktop.appframework.swingx.BBColumnFactory;
import org.jdesktop.beansbinding.BeanProperty;
import org.jdesktop.beansbinding.BindingGroup;
import org.jdesktop.beansbinding.Bindings;
import org.jdesktop.beansbinding.AutoBinding.UpdateStrategy;
import org.jdesktop.swingbinding.JListBinding;
import org.jdesktop.swingbinding.JTableBinding;
import org.jdesktop.swingbinding.SwingBindings;
import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXList;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.table.ColumnFactory;

/**
 * Bind navigation shared by JXList/JXTable.
 */
public class CoupledListTableBinding {

    private JComponent content;
    private JXTable table;
    private BAlbumManagerModel albumManager;
    private JTextField textField;
    private JXList list;

    private JComponent getContent() {
        if (content == null) {
            initComponents();
            content = build();
            initData();
            bindBasics();
        }
        return content;
    }

    private void bindBasics() {
        BindingGroup context = new BindingGroup();
        // bind table selected element and elements to albumManagerModel
        JTableBinding tableBinding = SwingBindings.createJTableBinding(UpdateStrategy.READ,
                albumManager.getManagedAlbums(), table);
        tableBinding.addColumnBinding(BeanProperty.create("artist"));
        tableBinding.addColumnBinding(BeanProperty.create("title"));
        tableBinding.addColumnBinding(BeanProperty.create("classical"))
             .setColumnClass(Boolean.class);
        tableBinding.addColumnBinding(BeanProperty.create("composer"));
        context.addBinding(tableBinding);
        context.addBinding(Bindings.createAutoBinding(UpdateStrategy.READ_WRITE,
                albumManager.getNavigation(), BeanProperty.create("selectedElement"), 
                table, BeanProperty.create("selectedElement")));
        
        JListBinding listBinding = SwingBindings.createJListBinding(UpdateStrategy.READ,
                albumManager.getManagedAlbums(), list);
        listBinding.setDetailBinding(BeanProperty.create("title"));
        context.addBinding(listBinding);
        context.addBinding(Bindings.createAutoBinding(UpdateStrategy.READ_WRITE,
                albumManager.getNavigation(), BeanProperty.create("selectedElement"), 
                list, BeanProperty.create("selectedElement")));
               

        // quick check for binding table's selected element directly
//        context.addBinding(Bindings.createAutoBinding(UpdateStrategy.READ_WRITE,
//                table, BeanProperty.create("selectedElement.artist"), 
//                textField, BeanProperty.create("text")));
        // quick check for binding text to navigation
        context.addBinding(Bindings.createAutoBinding(UpdateStrategy.READ_WRITE,
                albumManager.getNavigation(), BeanProperty.create("selectedElement.artist"), 
                textField, BeanProperty.create("text")));
        context.bind();
    }

    private JComponent build() {
        JComponent comp = new JPanel(new BorderLayout());
        comp.add(new JScrollPane(table));
        comp.add(list, BorderLayout.WEST);
        comp.add(textField, BorderLayout.NORTH);
        return comp;
    }

    private void initData() {
        albumManager = new BAlbumManagerModel(new BAlbumManager(Album.ALBUMS));
    }

    private void initComponents() {
        table = new JXTable();
        textField = new JTextField();
        list = new JXList();
    }

    public static void main(String[] args) {
        final JXFrame frame = new JXFrame("Binding Painters", true);
        ColumnFactory.setInstance(new BBColumnFactory());
        frame.add(new CoupledListTableBinding().getContent());
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
