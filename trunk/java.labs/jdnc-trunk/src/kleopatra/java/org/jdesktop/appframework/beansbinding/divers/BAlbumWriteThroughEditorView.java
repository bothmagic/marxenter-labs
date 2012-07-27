/*
 * Created on 11.04.2007
 *
 */
package org.jdesktop.appframework.beansbinding.divers;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.JTextComponent;

import org.jdesktop.appframework.FormView;
import org.jdesktop.appframework.beansbinding.album.AlbumModel;
import org.jdesktop.beansbinding.BeanProperty;
import org.jdesktop.beansbinding.BindingGroup;
import org.jdesktop.beansbinding.Bindings;
import org.jdesktop.beansbinding.AutoBinding.UpdateStrategy;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

public class BAlbumWriteThroughEditorView implements FormView {

    private AlbumModel albumModel;
    
    private JTextComponent titleField;
    private JTextComponent artistField;
    private JCheckBox      classicalBox;
    private JTextComponent composerField;

    private JComponent content;

    public BAlbumWriteThroughEditorView() {
        albumModel = new AlbumModel();
    }
    
    public BAlbumWriteThroughEditorView(AlbumModel model) {
        this.albumModel = model;
    }
    
    public Object getActionsObject() {
        // TODO Auto-generated method stub
        return albumModel;
    }

    public JComponent getContent() {
        if (content == null) {
            initComponents();
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
        if (albumModel == null) return;
        BindingGroup context = new BindingGroup();
        context.addBinding(Bindings.createAutoBinding(UpdateStrategy.READ_WRITE,
                albumModel, BeanProperty.create("title"), 
                titleField, BeanProperty.create("text")));
        context.addBinding(Bindings.createAutoBinding(UpdateStrategy.READ_WRITE,
                albumModel, BeanProperty.create("composer"), 
                composerField, BeanProperty.create("text"))); 
        context.addBinding(Bindings.createAutoBinding(UpdateStrategy.READ_WRITE,
                albumModel, BeanProperty.create("artist"), 
                artistField, BeanProperty.create("text"))); 
        context.addBinding(Bindings.createAutoBinding(UpdateStrategy.READ_WRITE,
                albumModel, BeanProperty.create("classical"), 
                classicalBox, BeanProperty.create("selected")));
        
        context.bind();
    }

    /**
     *  Creates and intializes the UI components.
     */
    private void initComponents() {
        titleField = new JTextField() {

            @Override
            public void setText(String t) {
                // TODO Auto-generated method stub
                super.setText(t);
            }
            
        };
        artistField = new JTextField();
        classicalBox = new JCheckBox();
        composerField = new JTextField();
    }
    
    /**
     * Builds and returns the editor panel.
     * 
     * @return the built panel
     */
    private JComponent build() {
        FormLayout layout = new FormLayout(
                "right:pref, 3dlu, 150dlu:grow",
                "p, 3dlu, p, 3dlu, p, 3dlu, p");
        layout.setRowGroups(new int[][]{{1, 3, 5, 7}});
        
        PanelBuilder builder = new PanelBuilder(layout);
        CellConstraints cc = new CellConstraints();
        
        JLabel label = builder.addLabel("",   cc.xy(1, 1));
        label.setName("artistLabel");
        builder.add(artistField,     cc.xy(3, 1));
        label = builder.addLabel("",    cc.xy(1, 3));
        label.setName("titleLabel");
        builder.add(titleField,      cc.xy(3, 3));
        builder.add(classicalBox,    cc.xy(3, 5));
        classicalBox.setName("classicalBox");
        JLabel myLabel = new JLabel();
        builder.add(myLabel, cc.xy(1, 7));
        myLabel.setName("composerLabel");
        myLabel.setLabelFor(composerField);
        builder.add(composerField,   cc.xy(3, 7));
        
        JPanel panel = builder.getPanel();
        panel.setName("editorView");
        
        return panel;
        
    }


}
