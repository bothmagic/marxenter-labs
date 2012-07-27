/*
 * Created on 11.04.2007
 *
 */
package org.jdesktop.appframework.beansbinding.divers;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.border.EmptyBorder;

import org.jdesktop.appframework.beansbinding.album.Album;
import org.jdesktop.appframework.beansbinding.album.BAlbumManagerModel;
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

public class BAlbumManagerWriteThroughView {
    private BAlbumManagerModel albumManagerModel;
    private JXList  albumList;
    
    private BAlbumWriteThroughEditorView detailsView;
    
    private JButton newButton;
    private JButton editButton;
    private JButton deleteButton;
    private JComponent content;

    private JButton applyButton;
    private JButton discardButton;
    private VetoableListSelectionModel vetoableSelection;
    

    public BAlbumManagerWriteThroughView(BAlbumManagerModel model) {
        this.albumManagerModel = model;
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
        Navigation navigation = albumManagerModel.getNavigation();
        BindingGroup bindingContext = new BindingGroup();
        vetoableSelection = new VetoableListSelectionModel();
        vetoableSelection.addVetoableChangeListener(albumManagerModel.getNavigationController());
        albumList.setSelectionModel(vetoableSelection);
        // is this a two-way synch? Seems so.
        albumList.setSelectionModel(vetoableSelection);
        // bind list selected element and elements to albumManagerModel
        bindingContext.addBinding(SwingBindings.createJListBinding(UpdateStrategy.READ,
                albumManagerModel.getManagedAlbums(), 
                albumList));
        // actually, this should be the other way round as well
        // control list selection primarily by navigation
        // currently not working because synthetic swing properties read-only
        bindingContext.addBinding(Bindings.createAutoBinding(
                UpdateStrategy.READ,
                albumList, BeanProperty.create("selectedElement"),
                navigation, BeanProperty.create("selectedElement") 
                ));

        bindingContext.bind();
        BindingGroup second = new BindingGroup();
        second.addBinding(Bindings.createAutoBinding(UpdateStrategy.READ, 
                bindingContext, ELProperty.create("${!hasEditedTargetBindings}"), 
                albumManagerModel, BeanProperty.create("navigatable")));
        second.bind();
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
        newButton = new JButton();
        deleteButton = new JButton();
        // dummy for now
        detailsView = new BAlbumWriteThroughEditorView(albumManagerModel.getAlbumModel());
        applyButton = new JButton();
        discardButton = new JButton();
//        Object actionsObject = detailsView.getActionsObject();
//        ActionMap detailsActions = context.getActionMap(actionsObject.getClass(), actionsObject);
//        applyButton.setAction(detailsActions.get("apply"));
//        discardButton.setAction(detailsActions.get("discard"));
        
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
        JComponent panel = buildDetailsPane();
        JSplitPane splitPane = new JSplitPane();
        splitPane.setLeftComponent(overview);
        splitPane.setRightComponent(panel);
        return splitPane;
    }


    private JPanel buildDetailsButtonBar() {
        return ButtonBarFactory.buildCenteredBar(applyButton, discardButton);
    }
    
    private JComponent buildDetailsPane() {
        FormLayout layout = new FormLayout(
                "fill:pref", 
                "fill:pref, 6dlu, pref");
        PanelBuilder builder = new PanelBuilder(layout);
        builder.getPanel().setBorder(new EmptyBorder(18, 12, 12, 12));
        CellConstraints cc = new CellConstraints();
        builder.add(detailsView.getContent(), cc.xy(1, 1));
        builder.add(buildDetailsButtonBar(),   cc.xy(1, 3));
        return builder.getPanel();
    }
    
    private JComponent buildButtonBar() {
        return ButtonBarFactory.buildLeftAlignedBar(
                newButton,
                deleteButton);
    }
    

}
