/*
 * Created on 12.04.2007
 *
 */
package org.jdesktop.appframework.beansbinding.album;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.util.List;
import java.util.logging.Logger;

import org.jdesktop.appframework.FormDialog;
import org.jdesktop.application.Action;
import org.jdesktop.beans.AbstractBean;
import org.jdesktop.beansbinding.BeanProperty;
import org.jdesktop.beansbinding.BindingGroup;
import org.jdesktop.beansbinding.Bindings;
import org.jdesktop.beansbinding.ELProperty;
import org.jdesktop.beansbinding.AutoBinding.UpdateStrategy;
import org.jdesktop.beansbindingx.Navigation;

/**
 * The presentation for managing a list of Albums. 
 * 
 */
public class BAlbumManagerModel extends AbstractBean {
    @SuppressWarnings("unused")
    private static final Logger LOG = Logger.getLogger(BAlbumManagerModel.class
            .getName());
    /** List of albums. */
    private BAlbumManager albumManager;
    /** navigation across the list of albums. */
    private Navigation navigation;
    /** controller to veto a navigation. */
    private VetoableChangeListener navigationController;
    /** the presentation model around the current/selected album. */
    private AlbumModel albumModel;
    
    public BAlbumManagerModel(BAlbumManager albumManager) {
        initModels(albumManager);
        bind();
    }

    /**
     * Wire internal bindings.
     */
    private void bind() {
        BindingGroup context = new BindingGroup();
        context.addBinding(Bindings.createAutoBinding(UpdateStrategy.READ,
                navigation, BeanProperty.create("selectedElement"),
                albumModel, BeanProperty.create("album")));
        context.addBinding(Bindings.createAutoBinding(UpdateStrategy.READ,
                // EL - had logic
                albumModel, ELProperty.create("${!buffering}"), 
                this, BeanProperty.create("navigatable")));
        context.addBinding(Bindings.createAutoBinding(UpdateStrategy.READ,
                // EL - had logic ..
                navigation, ELProperty.create("${selectedElement != null}"), 
                this, BeanProperty.create("albumSelected")));
        context.bind();
    }

    /**
     * Initialize and wire all models/controllers.
     * @param albumManager
     */
    private void initModels(BAlbumManager albumManager) {
        this.albumManager = albumManager;
        navigation = new Navigation(albumManager.getManagedAlbums());
        navigation.addVetoableChangeListener(getNavigationController());
        albumModel = new AlbumModel();
    }

    /**
     * 
     * @return the raw list of albums.
     */
    public List<Album> getManagedAlbums() {
        return albumManager.getManagedAlbums();
    }
    
    /**
     * 
     * @return
     */
    public VetoableChangeListener getNavigationController() {
        if (navigationController == null) {
            navigationController = new VetoableChangeListener() {

                public void vetoableChange(PropertyChangeEvent evt)
                        throws PropertyVetoException {
                    if (!isNavigatable())
                        throw new PropertyVetoException("uncommitted changes",
                                evt);
                }

            };
        }
        return navigationController;
    }

    public Navigation getNavigation() {
        return navigation;
    }

    public AlbumModel getAlbumModel() {
        return albumModel;
    }
    
//--------------- Actions

    @Action (enabledProperty = "navigatable")
    public void newAlbum() {
        navigation.setSelectedElement(createAndAddItem());
    }


    /**
     * Returns whether we can navigate away from the current
     * selected album model. 
     * @return 
     */
    public boolean isNavigatable() {
        return !albumModel.isBuffering();
    }
 
    /**
     * Binding artefact: does nothing except firing a propertyChange.
     * @param navigate
     */
    public void setNavigatable(boolean navigate) {
        boolean navigatable = isNavigatable();
        firePropertyChange("navigatable", !navigatable, navigatable);
    }


    @Action (enabledProperty = "albumSelected")
    public void deleteAlbum() {
        albumModel.discard();
        albumManager.removeItem((Album) navigation.getSelectedElement());
    }

    @Action(enabledProperty = "albumSelected")
    public void editAlbum() {
        openAlbumEditor((Album) navigation.getSelectedElement());
    }

    /**
     * Returns whether there's a selected album. Duplication
     * of binding expression for the sake of action's enabled property!
     * @return
     */
    public boolean isAlbumSelected() {
        return navigation.getSelectedElement() != null;
    }
    
    /**
     * Binding artefact: does nothing except fires event.
     * @param selected
     */
    public void setAlbumSelected(boolean selected) {
        boolean sel = isAlbumSelected();
        firePropertyChange("albumSelected", !sel, sel);
    }
    
    /**
     * Opens a Dialog containing a BAlbumEditorView for the given Album.
     * 
     * PENDING: too much view? Creates the dialog.
     * 
     * @param album  the Album to be edited
     * @return true if the dialog has been canceled, false if accepted
     */
    private boolean openAlbumEditor(Album album) {
        albumModel.setAlbum(album);
        BAlbumEditorView view = new BAlbumEditorView(albumModel);
        FormDialog dialog = new FormDialog(null, view);
        System.out.println("********** name dialog " + dialog.getName());
//        dialog.setName("albumEditorDialog");
        dialog.open();
        boolean cancelled = dialog.hasBeenCanceled();
        return cancelled;
    }
    
    /**
     * Creates a new Album and opens it for editing. If the edit 
     * is committed, adds to the manager and returns the new album.
     * Returns null, if cancelled.
     * 
     * @return
     */
    private Album createAndAddItem() {
        Album newAlbum = albumManager.createItem();
        boolean canceled = openAlbumEditor(newAlbum);
        if (!canceled) {
            albumManager.addItem(newAlbum);
            return newAlbum;
        }
        return null;
    }
    

}
