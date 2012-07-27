/*
 * Created on 11.04.2007
 *
 */
package org.jdesktop.appframework.beansbinding.album;

import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.util.logging.Logger;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import org.jdesktop.application.Action;
import org.jdesktop.application.Application;
import org.jdesktop.application.ApplicationContext;
import org.jdesktop.beansbinding.BeanProperty;
import org.jdesktop.beansbinding.BindingGroup;
import org.jdesktop.beansbinding.Bindings;
import org.jdesktop.beansbinding.AutoBinding.UpdateStrategy;
import org.jdesktop.beansbindingx.VetoableListSelectionModel;
import org.jdesktop.swingbinding.JListBinding;
import org.jdesktop.swingbinding.SwingBindings;
import org.jdesktop.swingx.JXList;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.factories.ButtonBarFactory;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * Combined view to show list of Albums and edit the current selection.
 * <p>
 * PENDING: change navigation binding once the swing properties are back to
 * read/write.<p>
 * 
 * PENDING: sluggish ui - grey rect on closing the dialog, slow selection change
 *   okay (?totally) in Mustang 
 * 
 * @author Jeanette Winzenburg
 */
public class BAlbumManagerView {
    private static final Logger LOG = Logger.getLogger(BAlbumManagerView.class
            .getName());
    private BAlbumManagerModel albumManagerModel;
    private JXList  albumList;
    
    private BAlbumEditorView detailsView;
    private Container detailsContainer;
    
    private JButton newButton;
    private JButton deleteButton;
    private JComponent content;

    private JButton applyButton;
    private JButton discardButton;
    

    public BAlbumManagerView(BAlbumManagerModel model) {
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
        albumList.setSelectionModel(vetoableSelection);
        BindingGroup bindingContext = new BindingGroup();
        // bind list selected element and elements to albumManagerModel
        JListBinding listBinding = SwingBindings.createJListBinding(
                UpdateStrategy.READ,
                albumManagerModel.getManagedAlbums(), 
                albumList);
        bindingContext.addBinding(listBinding);
        listBinding.setDetailBinding(BeanProperty.create("title"));
        // actually, this should be the other way round as well
        // control list selection primarily by navigation
        // currently not working because synthetic swing properties read-only
//        bindingContext.addBinding(Bindings.createAutoBinding(
//                UpdateStrategy.READ,
//                albumList, BeanProperty.create("selectedElement"),
//                albumManagerModel.getNavigation(), BeanProperty.create("selectedElement") 
//                ));
        // here is okay as we use JXTableAdapterProvider
        bindingContext.addBinding(Bindings.createAutoBinding(UpdateStrategy.READ_WRITE,
                albumList, BeanProperty.create("selectedElement_IGNORE_ADJUSTING"),
                albumManagerModel.getNavigation(), BeanProperty.create("selectedElement") 
        ));
        bindingContext.bind();

        javax.swing.Action action = new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                LOG.info("in action?" + (albumList.getSelectedValue() != null ? 
                        albumList.getSelectedValue().getClass() : "null"));
                if (!(albumList.getSelectedValue() instanceof Album)) return;
                Album album = (Album) albumList.getSelectedValue();
                album.setTitle(album.getTitle() + "X");
            }
            
        };


        content.getActionMap().put("changeTitle", action);
        content.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
            .put(KeyStroke.getKeyStroke("F1"), "changeTitle");
        


    }

    @Action
    public void toggleSort() {
        albumList.toggleSortOrder();
    }
    
    @Action
    public void resetSort() {
       albumList.resetSortOrder(); 
    }
    
    private void initComponents() {
        albumList = new JXList();
        albumList.setName("albumList");
        newButton = new JButton();
        deleteButton = new JButton();
        Application application = Application.getInstance(Application.class);
        ApplicationContext context = application.getContext();
        ActionMap actionMap = context.getActionMap(albumManagerModel.getClass(), 
                albumManagerModel);
        newButton.setAction(actionMap.get("newAlbum"));
        deleteButton.setAction(actionMap.get("deleteAlbum"));
        // PENDING: bind later ...
        detailsView = new BAlbumEditorView(albumManagerModel.getAlbumModel());
        applyButton = new JButton();
        discardButton = new JButton();
        Object actionsObject = detailsView.getActionsObject();
        ActionMap detailsActions = context.getActionMap(actionsObject.getClass(), actionsObject);
        applyButton.setAction(detailsActions.get("apply"));
        discardButton.setAction(detailsActions.get("discard"));

        // trying to sort enable list sorting
        // not working ... the binding to selected is the problem
        // probably because the JXList wraps the "real" model 
        // so the assumptions in JListBinding/Provider are wrong
//        albumList.setFilterEnabled(true);
//        ActionMap sortActions = context.getActionMap(this.getClass(), this);
//        albumList.getActionMap().put("toggleSort", sortActions.get("toggleSort"));
//        albumList.getInputMap().put(KeyStroke.getKeyStroke("F4"), "toggleSort");
//        albumList.getActionMap().put("resetSort", sortActions.get("resetSort"));
//        albumList.getInputMap().put(KeyStroke.getKeyStroke("F5"), "resetSort");
        
        
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
        detailsContainer = buildDetailsPane();
        JSplitPane splitPane = new JSplitPane();
        splitPane.setLeftComponent(overview);
        splitPane.setRightComponent(detailsContainer);
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

    
    // not working ...
//  SwingUtilities.invokeLater(new Runnable() {
//      public void run() {
//          KeyboardFocusManager manager = KeyboardFocusManager
//                  .getCurrentKeyboardFocusManager();
//          manager.addVetoableChangeListener("permanentFocusOwner",
//                  new FocusAwayControl());
//      }
//  });        

    /**
     * Attempt to veto focus to "outside" of editor.
     * 
     * Very brittle at best, and doesn't work to prevent 
     * changing the tab by mouseclick (because that doesn't
     * trigger a focuschange ...)
     */
//    private class FocusAwayControl implements VetoableChangeListener {
//
//        private Component previousFocusedComp;
//
//        public void vetoableChange(PropertyChangeEvent evt) throws PropertyVetoException {
//            if (!detailsView.getContent().isShowing()) return;
//            if (albumList.isSelectionEmpty()) return;
//            LOG.info("vetoable focus change (old): " + evt.getOldValue());
//            LOG.info("vetoable focus change (new): " + evt.getNewValue());
//
//            LOG.info("vetoable focus change entry: " + previousFocusedComp);
//            if (inEditor((Component) evt.getOldValue())) {
//                previousFocusedComp = (Component) evt.getOldValue();      
//                LOG.info("     vetoable focus change - marker: " + previousFocusedComp);
//            }
//            if (outsideEditor((Component) evt.getNewValue())) {
//                LOG.info("vetoable focus change - outside: " + evt.getNewValue());
//                try {
//                    albumManagerModel.getNavigationController().vetoableChange(evt);
//                    // no veto, reset previous
////                    previousFocusedComp = null;
//                } catch (PropertyVetoException ex) {
//                    SwingUtilities.invokeLater(new Runnable() {
//                        public void run() {
//                            if (previousFocusedComp != null)
//                                previousFocusedComp.requestFocusInWindow();
//                            previousFocusedComp = null;
//                        }
//                    });                    
//                    throw new PropertyVetoException("uncommitted changes", evt);
//                }    
//            }
//            
//        }
//        
//        private boolean inEditor(Component comp) {
//            if (comp == null) return false;
//            Container parent = comp.getParent();
//            while (parent != null) {
//                if (parent == detailsContainer) {
//                    return true;
//                }
//                parent = parent.getParent();
//            }
//            return false;
//        }
//        
//        private boolean outsideEditor(Component comp) {
//            if (comp == null) return false;
//            Container parent = comp.getParent();
//            while (parent != null) {
//                if (parent == detailsContainer) {
//                    return false;
//                }
//                parent = parent.getParent();
//            }
//            return true;
//        }
//    }

}
