/*
 * Created on 21.06.2007
 *
 */
package org.jdesktop.beansbindingx.example;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.text.JTextComponent;

import org.jdesktop.appframework.beansbinding.album.Album;
import org.jdesktop.appframework.beansbinding.album.AlbumModel;
import org.jdesktop.appframework.swingx.BBColumnFactory;
import org.jdesktop.beansbinding.BeanProperty;
import org.jdesktop.beansbinding.BindingGroup;
import org.jdesktop.beansbinding.Bindings;
import org.jdesktop.beansbinding.AutoBinding.UpdateStrategy;
import org.jdesktop.beansbindingx.LabelHandler;
import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.table.ColumnFactory;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * Example of how to bind to a bean via a buffering model. 
 * Here: simulate a "load/unload"
 */
public class SimpleAlbumModelForm {

    private JTextComponent titleField;
    private JTextComponent artistField;
    private JCheckBox      classicalBox;
    private JTextComponent composerField;
    private JComponent content;
    
    private AlbumModel album = new AlbumModel();
    private AbstractButton loadButton;
    protected BindingGroup context;
    private LabelHandler labelHandler;
    private JCheckBox bufferingBox;
    
    private JComponent getContent() {
        if (content == null) {
            initComponents();
            content = build();
            bindBasics();
            bindActions();
        }
        return content;
    }

    private void bindActions() {
        Action load = new AbstractAction("load") {
            boolean unload;
            public void actionPerformed(ActionEvent e) {
                if (!unload) {
                    album.setAlbum(Album.ALBUM1);
                    // first time around ... should be handled by context
//                    if (!context.isBound()) context.bind();
                } else {
                    album.setAlbum(null);
                }
                unload = !unload;
                putValue(Action.NAME, unload ? "unload" : "load");
                
            }
            
        };
        loadButton.setAction(load);
        
    }

    private void bindBasics() {
        context = new BindingGroup();
        // bind list selected element and elements to albumManagerModel
        context.addBinding(Bindings.createAutoBinding(UpdateStrategy.READ_WRITE,
                album, BeanProperty.create("title"), 
                titleField, BeanProperty.create("text")));
        context.addBinding(Bindings.createAutoBinding(UpdateStrategy.READ_WRITE,
                album, BeanProperty.create("composer"), 
                composerField, BeanProperty.create("text"))); 
        context.addBinding(Bindings.createAutoBinding(UpdateStrategy.READ_WRITE,
                album, BeanProperty.create("artist"), 
                artistField, BeanProperty.create("text"))); 
        context.addBinding(Bindings.createAutoBinding(UpdateStrategy.READ_WRITE,
                album, BeanProperty.create("classical"), 
                classicalBox, BeanProperty.create("selected")));
        context.addBinding(Bindings.createAutoBinding(UpdateStrategy.READ,
                album, BeanProperty.create("classical"), 
                composerField, BeanProperty.create("enabled")));
        // debug 
        context.addBinding(Bindings.createAutoBinding(UpdateStrategy.READ,
                album, BeanProperty.create("buffering"), 
                bufferingBox, BeanProperty.create("selected")));
        context.bind();
    }

    /**
     *  Creates and intializes the UI components.
     */
    private void initComponents() {
        titleField = new JTextField();
        artistField = new JTextField();
        classicalBox = new JCheckBox();
        composerField = new JTextField();
        loadButton = new JButton();
        bufferingBox = new JCheckBox("debug: buffering");
        
    }
    
    /**
     * Builds and returns the editor panel.
     * 
     * @return the built panel
     */
    private JComponent build() {
        initComponents();
        FormLayout layout = new FormLayout(
                "right:pref, 3dlu, 150dlu:grow",
                "p, 3dlu, p, 3dlu, p, 3dlu, p, 3dlu, p");
        layout.setRowGroups(new int[][]{{1, 3, 5, 7}});
        
        PanelBuilder builder = new PanelBuilder(layout);
        CellConstraints cc = new CellConstraints();
        
        JLabel label = builder.addLabel("artist",   cc.xy(1, 1));
        label.setName("artistLabel");
        builder.add(artistField,     cc.xy(3, 1));
        getLabelHandler().add(label, artistField);
        label = builder.addLabel("title",    cc.xy(1, 3));
        label.setName("titleLabel");
        builder.add(titleField,      cc.xy(3, 3));
        getLabelHandler().add(label, titleField);
        builder.add(classicalBox,    cc.xy(3, 5));
        classicalBox.setName("classicalBox");
        label = builder.addLabel("composer", cc.xy(1, 7));
        label.setName("composerLabel");
        builder.add(composerField,   cc.xy(3, 7));
        getLabelHandler().add(label, composerField);
        builder.add(bufferingBox, cc.xy(3, 9));
        JPanel panel = builder.getPanel();
//        panel.setName(getName());
        JComponent cont = new JPanel(new BorderLayout());
        cont.add(panel);
        cont.add(loadButton, BorderLayout.SOUTH);
        return cont;
        
    }
    private LabelHandler getLabelHandler() {
        if (labelHandler == null) {
            labelHandler = new LabelHandler();
        }
        return labelHandler;
    }


    public static void main(String[] args) {
        final JXFrame frame = new JXFrame("Bind to AlbumModel", true);
        ColumnFactory.setInstance(new BBColumnFactory());
        frame.add(new SimpleAlbumModelForm().getContent());
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
