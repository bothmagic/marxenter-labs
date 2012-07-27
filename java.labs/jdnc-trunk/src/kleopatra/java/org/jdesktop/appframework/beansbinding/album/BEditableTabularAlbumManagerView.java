/*
 * Created on 11.04.2007
 *
 */
package org.jdesktop.appframework.beansbinding.album;

import javax.swing.ActionMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

import org.jdesktop.application.Application;
import org.jdesktop.application.ApplicationContext;
import org.jdesktop.beansbinding.BeanProperty;
import org.jdesktop.beansbinding.BindingGroup;
import org.jdesktop.beansbinding.Bindings;
import org.jdesktop.beansbinding.AutoBinding.UpdateStrategy;
import org.jdesktop.beansbindingx.VetoableListSelectionModel;
import org.jdesktop.swingbinding.JTableBinding;
import org.jdesktop.swingbinding.SwingBindings;
import org.jdesktop.swingx.JXTable;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.factories.ButtonBarFactory;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * Tabular view to show list of Albums and in-cell edit.
 * 
 * PENDING: change navigation binding once the swing properties are back to
 * read/write.
 * 
 * @author Jeanette Winzenburg
 */
public class BEditableTabularAlbumManagerView {

    private BAlbumManagerModel albumManagerModel;
    private JXTable  albumTable;
    private JButton newButton;
    private JButton deleteButton;
    private JComponent content;

    public BEditableTabularAlbumManagerView(BAlbumManagerModel model) {
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
        // bind list selection to model vetoable
        VetoableListSelectionModel vetoableSelection = new VetoableListSelectionModel();
        vetoableSelection.addVetoableChangeListener(albumManagerModel.getNavigationController());
        albumTable.setSelectionModel(vetoableSelection);
        BindingGroup context = new BindingGroup();
        // bind list selected element and elements to albumManagerModel
        JTableBinding tableBinding = SwingBindings.createJTableBinding(
                UpdateStrategy.READ,
                albumManagerModel.getManagedAlbums(), albumTable);
        context.addBinding(tableBinding);
        // actually, this should be the other way round as well
        // control table selection primarily by navigation
        // currently not working because synthetic swing properties read-only
        context.addBinding(Bindings.createAutoBinding(
                UpdateStrategy.READ_WRITE,
                albumTable, BeanProperty.create("selectedElement"),
                albumManagerModel.getNavigation(), BeanProperty.create("selectedElement") 
                ));
        tableBinding.addColumnBinding(BeanProperty.create("artist"));
        tableBinding.addColumnBinding(BeanProperty.create("title"));
        tableBinding.addColumnBinding(BeanProperty.create("classical")).
           setColumnClass(Boolean.class);
        tableBinding.addColumnBinding(BeanProperty.create("composer"));
        context.bind();
    }

    private JComponent build() {
        FormLayout layout = new FormLayout(
                "fill:100dlu:grow",
                "p, 1dlu, p, 6dlu, p");
                
        PanelBuilder builder = new PanelBuilder(layout);
        builder.setDefaultDialogBorder();
        CellConstraints cc = new CellConstraints();
        
        JLabel label = builder.addTitle("", cc.xy(1, 1));
        label.setName("albumsTitle");
        builder.add(new JScrollPane(albumTable), cc.xy(1, 3));
        builder.add(buildButtonBar(),            cc.xy(1, 5));
        JComponent overview = builder.getPanel();
        return overview;
        
    }
    
    private JComponent buildButtonBar() {
        return ButtonBarFactory.buildLeftAlignedBar(
                newButton,
                deleteButton);
    }

    private void initComponents() {
        albumTable = new JXTable();
        albumTable.setName("editableTable");
        newButton = new JButton();
        deleteButton = new JButton();
        Application application = Application.getInstance(Application.class);
        ApplicationContext context = application.getContext();
        ActionMap actionMap = context.getActionMap(albumManagerModel.getClass(), 
                albumManagerModel);
        newButton.setAction(actionMap.get("newAlbum"));
        deleteButton.setAction(actionMap.get("deleteAlbum"));
        
    }

}
