/*
 * Created on 21.06.2007
 *
 */
package org.jdesktop.beansbindingx.example;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComboBox;
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
import org.jdesktop.swingbinding.JXComboBoxBinding;
import org.jdesktop.swingbinding.SwingXBindings;
import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.table.ColumnFactory;

/**
 * Bind album list and navigation to x-combo. The x denotes a 
 * custom binding JXComboBoxBinding which tries to support detail binding
 */
public class SimpleXComboBinding {

    private JComponent content;
    private JComboBox comboBox;
    private BAlbumManagerModel albumManager;
    private JTextField textField;
    private JButton nextButton;


    private void bindBasics() {
        BindingGroup context = new BindingGroup();
        // bind list selected element and elements to albumManagerModel
        // NOTE: binding navigation --> table.selectedElement not working ...
        context.addBinding(Bindings.createAutoBinding(UpdateStrategy.READ_WRITE,
                comboBox, BeanProperty.create("selectedElement"),
                albumManager.getNavigation(), BeanProperty.create("selectedElement") 
        ));
        JXComboBoxBinding comboBoxBinding = SwingXBindings.createJComboBoxBinding(UpdateStrategy.READ,
                albumManager.getManagedAlbums(), comboBox);
        context.addBinding(comboBoxBinding);
        comboBoxBinding.setDetailBinding(BeanProperty.create("title"));
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
        comp.add(new JScrollPane(comboBox));
        comp.add(textField, BorderLayout.NORTH);
        comp.add(nextButton, BorderLayout.SOUTH);
        return comp;
    }


    private void initComponents() {
        comboBox = new JComboBox();
        textField = new JTextField();
        nextButton = new JButton("next");
    }

    public static void main(String[] args) {
        
        final JXFrame frame = new JXFrame("Bind Album List", true);
        ColumnFactory.setInstance(new BBColumnFactory());
        frame.add(new SimpleXComboBinding().getContent());
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
