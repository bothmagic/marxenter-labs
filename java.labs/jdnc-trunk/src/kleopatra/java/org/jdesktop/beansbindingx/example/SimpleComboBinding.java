/*
 * Created on 21.06.2007
 *
 */
package org.jdesktop.beansbindingx.example;

import java.awt.BorderLayout;

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
import org.jdesktop.beans.AbstractBean;
import org.jdesktop.beansbinding.BeanProperty;
import org.jdesktop.beansbinding.BindingGroup;
import org.jdesktop.beansbinding.Bindings;
import org.jdesktop.beansbinding.AutoBinding.UpdateStrategy;
import org.jdesktop.swingbinding.JComboBoxBinding;
import org.jdesktop.swingbinding.SwingBindings;
import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.renderer.DefaultListRenderer;
import org.jdesktop.swingx.renderer.StringValue;
import org.jdesktop.swingx.table.ColumnFactory;

/**
 * Bind album list and navigation ComboBox. 
 * Issue: no detail binding for combo, need custom renderer.
 */
public class SimpleComboBinding {

    private JComponent content;
    private JComboBox comboBox;
    private BAlbumManagerModel albumManager;
    private JTextField textField;


    private void bindBasics() {
        BindingGroup context = new BindingGroup();
        // bind list selected element and elements to albumManagerModel
        // NOTE: binding navigation --> table.selectedElement not working ...
        context.addBinding(Bindings.createAutoBinding(UpdateStrategy.READ,
                comboBox, BeanProperty.create("selectedItem"),
                albumManager.getNavigation(), BeanProperty.create("selectedElement") 
        ));
        JComboBoxBinding comboBoxBinding = SwingBindings.createJComboBoxBinding(UpdateStrategy.READ,
                albumManager.getManagedAlbums(), comboBox);
        context.addBinding(comboBoxBinding);
//        comboBoxBinding.setDetailBinding(BeanProperty.create("title"));
        // quick check for binding table's selected element directly
//        context.addBinding(Bindings.createAutoBinding(UpdateStrategy.READ_WRITE,
//                table, BeanProperty.create("selectedItem.artist"), 
//                textField, BeanProperty.create("text_ON_ACTION_OR_FOCUS_LOST")));
        // quick check for binding text to navigation
        context.addBinding(Bindings.createAutoBinding(UpdateStrategy.READ_WRITE,
                albumManager.getNavigation(), BeanProperty.create("selectedElement.artist"), 
                textField, BeanProperty.create("text_ON_ACTION_OR_FOCUS_LOST")));
        context.bind();
        StringValue value = new StringValue() {

            public String getString(Object value) {
                if (value instanceof Album) {
                    return ((Album) value).getTitle();
                }
                return null;
            }
            
        };
        comboBox.setRenderer(new DefaultListRenderer(value));
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
        return comp;
    }


    private void initComponents() {
        comboBox = new JComboBox();
        textField = new JTextField();
    }

    public static class Person extends AbstractBean {
        String name;
        ColorDesc colorDesc;
        public Person(String name, ColorDesc colorDesc) {
            this.name = name;
            this.colorDesc = colorDesc;
        }
        public ColorDesc getColorDesc() {
            return colorDesc;
        }
        public void setColorDesc(ColorDesc colorDesc) {
            ColorDesc old = getColorDesc();
            this.colorDesc = colorDesc;
            firePropertyChange("colorDesc", old, getColorDesc());
        }
        
        public String getName() {
            return name;
        }
        
        public void setName(String name) {
            String old = getName();
            this.name = name;
            firePropertyChange("name", old, getName());
        }
        
        
    }
    public static class ColorDesc {
        int color;

        
        String description;

        public ColorDesc(int color, String description) {
            this.color = color;
            this.description = description;
        }

        public int getColor() {
            return color;
        }

        public String getDescription() {
            return description;
        }
    }

    public static void main(String[] args) {
        
        final JXFrame frame = new JXFrame("Bind Album List", true);
        ColumnFactory.setInstance(new BBColumnFactory());
        frame.add(new SimpleComboBinding().getContent());
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
