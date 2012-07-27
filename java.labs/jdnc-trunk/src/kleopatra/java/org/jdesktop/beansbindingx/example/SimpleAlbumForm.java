/*
 * Created on 21.06.2007
 *
 */
package org.jdesktop.beansbindingx.example;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.util.logging.Logger;

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
import org.jdesktop.appframework.swingx.BBColumnFactory;
import org.jdesktop.beansbinding.AutoBinding;
import org.jdesktop.beansbinding.BeanProperty;
import org.jdesktop.beansbinding.BindingGroup;
import org.jdesktop.beansbinding.Bindings;
import org.jdesktop.beansbinding.Validator;
import org.jdesktop.beansbinding.AutoBinding.UpdateStrategy;
import org.jdesktop.beansbindingx.BindingGroupBean;
import org.jdesktop.beansbindingx.LabelHandler;
import org.jdesktop.beansbindingx.validator.NotEmptyValidator;
import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.table.ColumnFactory;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * Example of how to bind to a bean directly. 
 * Here: simulate a "un/load" 
 * - need to prepare the bindings, but postpone the bind until after the load.
 * - problem: null source and clear the bound widgets 
 *    setSourceUnreadableValue? 
 */
public class SimpleAlbumForm {
    private static final Logger LOG = Logger.getLogger(SimpleAlbumForm.class
            .getName());
    private JTextComponent titleField;
    private JTextComponent artistField;
    private JCheckBox      classicalBox;
    private JTextComponent composerField;
    private JComponent content;
    
    private Album album;
    private AbstractButton loadButton;
    protected BindingGroupBean context;
    private LabelHandler labelHandler;
    private JLabel titleCopy;
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
                    context.setSourceObject(Album.ALBUM1);
                    // first time around ... should be handled by context
//                    if (!context.isBound()) context.bind();
                } else {
                    context.setSourceObject(null);
                }
                unload = !unload;
                putValue(Action.NAME, unload ? "unload" : "load");
                
            }
            
        };
        loadButton.setAction(load);
        
    }

    private void bindBasics() {
        Validator validator = new NotEmptyValidator();
        context = new BindingGroupBean(true);
        context.addBinding(Bindings.createAutoBinding(UpdateStrategy.READ,
                album, BeanProperty.create("title"), 
                titleCopy, BeanProperty.create("text")));
        AutoBinding<Object, Object, Object, Object> titleBinding = Bindings.createAutoBinding(UpdateStrategy.READ_WRITE,
                        album, BeanProperty.create("title"), 
                        titleField, BeanProperty.create("text"));
        titleBinding.setValidator(validator);
        context.addBinding(titleBinding);
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
        context.bind();
        BindingGroup bufferingContext = new BindingGroup();
        bufferingContext.addBinding(Bindings.createAutoBinding(UpdateStrategy.READ,
                context, BeanProperty.create("dirty"), 
                bufferingBox, BeanProperty.create("selected")));
        bufferingContext.bind();
    }

    /**
     *  Creates and intializes the UI components.
     */
    private void initComponents() {
        titleCopy = new JLabel("empty");
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
        cont.add(titleCopy, BorderLayout.NORTH);
        return cont;
        
    }
    private LabelHandler getLabelHandler() {
        if (labelHandler == null) {
            labelHandler = new LabelHandler();
        }
        return labelHandler;
    }


    public static void main(String[] args) {
        final JXFrame frame = new JXFrame("Bind to Album (direct)", true);
        ColumnFactory.setInstance(new BBColumnFactory());
        frame.add(new SimpleAlbumForm().getContent());
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
