/*
 * Created on 11.04.2007
 *
 */
package org.jdesktop.appframework.beansbinding.divers;

import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import org.jdesktop.appframework.beansbinding.album.Album;
import org.jdesktop.appframework.beansbinding.album.BAlbumManagerModel;
import org.jdesktop.application.Application;
import org.jdesktop.application.ApplicationContext;
import org.jdesktop.beans.AbstractBean;
import org.jdesktop.beansbinding.BeanProperty;
import org.jdesktop.beansbinding.BindingGroup;
import org.jdesktop.beansbinding.Bindings;
import org.jdesktop.beansbinding.ELProperty;
import org.jdesktop.beansbinding.AutoBinding.UpdateStrategy;
import org.jdesktop.beansbindingx.Navigation;
import org.jdesktop.beansbindingx.VetoableListSelectionModel;
import org.jdesktop.swingbinding.SwingBindings;
import org.jdesktop.swingx.JXList;
import org.jdesktop.swingx.renderer.DefaultListRenderer;
import org.jdesktop.swingx.renderer.StringValue;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.factories.ButtonBarFactory;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * quick test with vetoable Navigation. 
 */
public class BAlbumManagerNavigationView extends AbstractBean {
    private BAlbumManagerModel albumManager;
    private JXList  albumList;
    
   
    private JButton previousButton;
    private JButton nextButton;
    private JComponent content;

    private VetoableListSelectionModel vetoableSelection;
    private JCheckBox vetoCheckBox;
    private JTextField titleField;
    private Component titleLabel;
    

    public BAlbumManagerNavigationView(BAlbumManagerModel model) {
        this.albumManager = model;
    }

    public JComponent getContent() {
        if (content == null) {
            initComponents();
            content = build();
            bind();
        }
        return content;
    }


    private void bind() {
        Application application = Application.getInstance(Application.class);
        ApplicationContext context = application.getContext();
        Navigation navigation = albumManager.getNavigation();
        ActionMap navigationActions = context.getActionMap(navigation.getClass(),
                navigation);
        previousButton.setAction(navigationActions.get("previous"));
        nextButton.setAction(navigationActions.get("next"));
        BindingGroup bindingContext = new BindingGroup();
        vetoableSelection = new VetoableListSelectionModel();
        vetoableSelection.addVetoableChangeListener(albumManager.getNavigationController());
        albumList.setSelectionModel(vetoableSelection);
        // bind list selected element and elements to albumManagerModel
        bindingContext.addBinding(SwingBindings.createJListBinding(UpdateStrategy.READ,
                albumManager.getManagedAlbums(), 
                albumList));
        // actually, this should be the other way round as well
        // control list selection primarily by navigation
        // currently not working because synthetic swing properties read-only
        bindingContext.addBinding(Bindings.createAutoBinding(
                UpdateStrategy.READ,
                albumList, BeanProperty.create("selectedElement"),
                navigation, BeanProperty.create("selectedElement") 
                ));

        bindingContext.addBinding(Bindings.createAutoBinding(UpdateStrategy.READ_WRITE, 
                albumManager.getAlbumModel(), BeanProperty.create("title"), 
                titleField, BeanProperty.create("text")));
        bindingContext.bind();
        BindingGroup second = new BindingGroup();
        second.addBinding(Bindings.createAutoBinding(UpdateStrategy.READ, 
                albumManager.getAlbumModel(), ELProperty.create("${buffering}"), 
                vetoCheckBox, BeanProperty.create("selected" )));
        second.bind();
        // need to do after binding? why? - no longer after overhaul
        vetoCheckBox.setEnabled(false);
        // quick and dirty commit action
        Action titleFieldAction = new AbstractAction("enter to commit") {

            public void actionPerformed(ActionEvent e) {
                albumManager.getAlbumModel().apply();
                
            }
            
        };
        titleField.setAction(titleFieldAction);
    }

    
    private void initComponents() {
        albumList = new JXList();
        albumList.setName("albumList");
        StringValue format = new StringValue() {

            public String getString(Object arg0) {
                if (arg0 instanceof Album) {
                    return ((Album) arg0).getTitle();
                }
                return null;
            }
            
        };
        albumList.setCellRenderer(new DefaultListRenderer(format));
        previousButton = new JButton();
        previousButton.setFocusable(false);
        nextButton = new JButton();
        nextButton.setFocusable(false);
        vetoCheckBox = new JCheckBox("Veto");
        vetoCheckBox.setName("vetoingBox");
        titleField = new JTextField();
        titleLabel = new JLabel();
//        titleLabel.setName();
    }

    public JComponent build() {
        FormLayout layout = new FormLayout(
                "fill:100dlu:grow",
                "p, 1dlu, p, 6dlu, p");
                
        PanelBuilder builder = new PanelBuilder(layout);
        builder.setDefaultDialogBorder();
        CellConstraints cc = new CellConstraints();
        
        JLabel label = builder.addTitle("", cc.xy(1, 1));
        label.setName("albumsTitle");
        builder.add(new JScrollPane(albumList), cc.xy(1, 3));
        builder.add(buildButtonBar(),            cc.xy(1, 5));
        JComponent overview = builder.getPanel();
        return overview;
    }

    
    private JComponent buildButtonBar() {
        JComponent comp = ButtonBarFactory.buildLeftAlignedBar(
                previousButton,
                nextButton);
        JComponent bar = Box.createHorizontalBox();
        bar.add(comp);
        bar.add(Box.createHorizontalGlue());
        bar.add(titleLabel);
        bar.add(titleField);
        bar.add(vetoCheckBox);
        return bar;
    }
    

}
