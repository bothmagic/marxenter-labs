/*
 * Created on 11.04.2007
 *
 */
package org.jdesktop.appframework.beansbinding.album;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.JTextComponent;

import org.jdesktop.appframework.FormView;
import org.jdesktop.beansbinding.AutoBinding;
import org.jdesktop.beansbinding.BeanProperty;
import org.jdesktop.beansbinding.BindingGroup;
import org.jdesktop.beansbinding.Bindings;
import org.jdesktop.beansbinding.Validator;
import org.jdesktop.beansbinding.AutoBinding.UpdateStrategy;
import org.jdesktop.beansbindingx.LabelHandler;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * View for editing a single album. 
 */
public class BAlbumEditorView implements FormView {

    private AlbumModel albumModel;
    
    private JTextComponent titleField;
    private JTextComponent artistField;
    private JCheckBox classicalBox;
    private JTextComponent composerField;
    // debug: visualize buffering status
    private JCheckBox bufferingBox;
    
    private JComponent content;

    private LabelHandler labelHandler;

    public BAlbumEditorView(AlbumModel albumModel) {
        this.albumModel = albumModel;
    }
    
//-------------- implement FormView
    
    public Object getActionsObject() {
        return albumModel;
    }

    public JComponent getContent() {
        if (content == null) {
            content = build();
            bind();
        }
        return content;
    }
   
    public String getName() {
        return "editorView";
    }
    
    // Initialization *********************************************************

    private void bind() {
        bindProperties();
        bindEnabled();
    }

    /**
     * Bind widgets' enabled to model enabled where necessary.
     *
     */
    private void bindEnabled() {
        BindingGroup context = new BindingGroup();
        context.addBinding(Bindings.createAutoBinding(UpdateStrategy.READ,
                albumModel, BeanProperty.create("editEnabled"), 
                titleField, BeanProperty.create("enabled")));
        context.addBinding(Bindings.createAutoBinding(UpdateStrategy.READ,
                albumModel, BeanProperty.create("editEnabled"), 
                artistField, BeanProperty.create("enabled")));
        context.addBinding(Bindings.createAutoBinding(UpdateStrategy.READ,
                albumModel, BeanProperty.create("editEnabled"), 
                classicalBox, BeanProperty.create("enabled")));
        context.bind();
    }

    /**
     * Bind input to model properties.
     */
    private void bindProperties() {
        BindingGroup context = new BindingGroup();
        AutoBinding<Object, Object, Object, Object> titleBinding = Bindings.createAutoBinding(
                UpdateStrategy.READ_WRITE,
                        albumModel, BeanProperty.create("title"), 
                        titleField, BeanProperty.create("text"));
        titleBinding.setValidator(albumModel.getValidator("title"));
        context.addBinding(titleBinding);
       
        context.addBinding(Bindings.createAutoBinding(UpdateStrategy.READ_WRITE,
                albumModel, BeanProperty.create("artist"), 
                artistField, BeanProperty.create("text"))); 
        context.addBinding(Bindings.createAutoBinding(UpdateStrategy.READ_WRITE,
                albumModel, BeanProperty.create("classical"), 
                classicalBox, BeanProperty.create("selected")));
        context.addBinding(Bindings.createAutoBinding(UpdateStrategy.READ_WRITE,
                albumModel, BeanProperty.create("composer"), 
                composerField, BeanProperty.create("text"))); 
        context.addBinding(Bindings.createAutoBinding(UpdateStrategy.READ_WRITE,
                albumModel, BeanProperty.create("composerEnabled"), 
                composerField, BeanProperty.create("enabled")));
        // debug
        context.addBinding(Bindings.createAutoBinding(UpdateStrategy.READ_WRITE,
                albumModel, BeanProperty.create("buffering"), 
                bufferingBox, BeanProperty.create("selected")));
        context.bind();
        bufferingBox.setEnabled(false);
    }

    /**
     *  Creates and intializes the UI components.
     */
    private void initComponents() {
        titleField = new JTextField();
        artistField = new JTextField();
        classicalBox = new JCheckBox();
        composerField = new JTextField();
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
        
        JLabel label = builder.addLabel("",   cc.xy(1, 1));
        label.setName("artistLabel");
        builder.add(artistField,     cc.xy(3, 1));
        getLabelHandler().add(label, artistField);
        label = builder.addLabel("",    cc.xy(1, 3));
        label.setName("titleLabel");
        builder.add(titleField,      cc.xy(3, 3));
        getLabelHandler().add(label, titleField);
        builder.add(classicalBox,    cc.xy(3, 5));
        classicalBox.setName("classicalBox");
        label = builder.addLabel("", cc.xy(1, 7));
        label.setName("composerLabel");
        builder.add(composerField,   cc.xy(3, 7));
        getLabelHandler().add(label, composerField);
        builder.add(bufferingBox, cc.xy(3, 9));
        JPanel panel = builder.getPanel();
        panel.setName(getName());
        
        return panel;
        
    }

    private LabelHandler getLabelHandler() {
        if (labelHandler == null) {
            labelHandler = new LabelHandler();
        }
        return labelHandler;
    }


}
