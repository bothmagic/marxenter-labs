/*
 * Copyright (c) 2002-2006 JGoodies Karsten Lentzsch. All Rights Reserved.
 *
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 * 
 *  o Redistributions of source code must retain the above copyright notice, 
 *    this list of conditions and the following disclaimer. 
 *     
 *  o Redistributions in binary form must reproduce the above copyright notice, 
 *    this list of conditions and the following disclaimer in the documentation 
 *    and/or other materials provided with the distribution. 
 *     
 *  o Neither the name of JGoodies Karsten Lentzsch nor the names of 
 *    its contributors may be used to endorse or promote products derived 
 *    from this software without specific prior written permission. 
 *     
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" 
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, 
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR 
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR 
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, 
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, 
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; 
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, 
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE 
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, 
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. 
 */

package org.jdesktop.appframework.goodies.album;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.util.logging.Logger;

import javax.swing.RowFilter;

import org.jdesktop.appframework.FormDialog;
import org.jdesktop.application.Action;
import org.jdesktop.swingx.sort.RowFilters.GeneralFilter;

import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.list.SelectionInList;
import com.jgoodies.binding.value.ValueModel;

/**
 * Updated to use the app framework (jsr-296). <p>
 * 
 * Provides the models and Actions for managing and editing Albums.
 * Works with an underlying AlbumManager that provides a ListModel
 * for the Albums and operations to add, remove, and change a Album.
 * In other words, this class turns the raw data and operations
 * form the AlbumManager into a form usable in a user interface.<p>
 * 
 * This model keeps the Albums in a SelectionInList, refers to another
 * PresentationModel for editing the selected Album, and provides 
 * Actions for the Album operations: add, remove and edit the selected Album.
 * 
 * @author  Karsten Lentzsch
 * @version $Revision: 3198 $
 * 
 * @see AlbumManager
 * @see com.jgoodies.binding.PresentationModel
 */

public final class AlbumManagerModel extends PresentationModel<AlbumManagerModel> {
    @SuppressWarnings("unused")
    private static final Logger LOG = Logger.getLogger(AlbumManagerModel.class
            .getName());
    /**
     * Holds the List of Albums and provides operations 
     * to create, add, remove and change a Album.
     */
    private final AlbumManager albumManager;
    
    /**
     * Holds the list of managed albums plus a single selection.
     */
    private SelectionInList<Album> albumSelection;

    private AlbumModel albumModel;

    private VetoableChangeListener navigationController;
    
    private static final String FILTER_NONE = "none";
    private static final String FILTER_CLASSICAL= "classicalOnly";
    private static final String FILTER_NOTCLASSICAL = "notClassical";
    private String[] filterNames = {FILTER_NONE, FILTER_CLASSICAL, FILTER_NOTCLASSICAL};
    private String filterName;
    private SelectionInList<String> filterSelection;
    

    // Instance Creation ******************************************************
    
    /**
     * Constructs an AlbumManager for editing the given list of Albums.
     * 
     * @param albumManager   the list of albums to edit
     */
    public AlbumManagerModel(AlbumManager albumManager) {
        // feels funny ... probably the wrong-thing-to-do
        super(null);
        setBean(this);
        this.albumManager = albumManager;
        initModels();
        initEventHandling();
        initFilters();
    }
    
    
    private void initFilters() {
        filterName = FILTER_NONE;
    }


    /**
     * Initializes the SelectionInList and Action.
     * In this case we eagerly initialize the Actions. 
     * As an alternative you can create the Actions lazily 
     * in the Action getter methods. To synchronize the Action enablement 
     * with the selection state, we update the enablement now. 
     */
    private void initModels() {
        albumSelection = new SelectionInList<Album>(albumManager.getManagedAlbums());
        getDetailsModel();
    }
    
    
    /**
     * Initializes the event handling by just registering a
     * handler that updates the Action enablement if the
     * albumSelection's 'selectionEmpty' property changes.
     */
    private void initEventHandling() {
        albumSelection.addPropertyChangeListener(
                SelectionInList.PROPERTYNAME_SELECTION,
                new SelectionHandler());
    }
    
   // properties on this model's level
    
    /**
     * Read-only property because a filter is not shareable between views.
     */
    public RowFilter getFilter() {
        return createFilter(filterName);
    }

    
    public void setFilterByName(String filterName) {
        String old = getFilterByName();
        this.filterName = filterName;
        firePropertyChange("filterByName", old, getFilterByName());
        firePropertyChange("filter", null, null);
    }
    
    private RowFilter createFilter(String filterName) {
        if (FILTER_CLASSICAL.equals(filterName)) {
            RowFilter filter = new GeneralFilter() {

                @Override
                protected boolean include(
                        Entry <? extends Object, ? extends Object> value,
                        int index) {
                    Album album = getAlbumSelection().getElementAt((Integer) value.getIdentifier());
                    return album.isClassical();
                }
                
            };
            return filter;
        } 
        if (FILTER_NOTCLASSICAL.equals(filterName)) {
            RowFilter filter = new GeneralFilter() {

                @Override
                protected boolean include(
                        Entry <? extends Object, ? extends Object> value,
                        int index) {
                    Album album = getAlbumSelection().getElementAt((Integer) value.getIdentifier());
                    return !album.isClassical();
                }
                
            };
            return filter;
        }
        return null;
    }


    public String getFilterByName() {
        return filterName;
    }


    public SelectionInList<String> getFilterSelection() {
        if (filterSelection == null) {
            filterSelection = new SelectionInList<String>(filterNames, 
                    getModel("filterByName"));
        }
        return filterSelection;
    }
    
    public ValueModel getFilterModel() {
        return getModel("filter");
    }

    // Exposing Models and Actions ********************************************
    
    /**
     * Returns the List of Albums with the current selection.
     * Useful to display the managed Albums in a JList or JTable.
     * 
     * @return the List of Albums with selection
     */
    public SelectionInList<Album> getAlbumSelection() {
        return albumSelection;
    }
    
    public AlbumModel getDetailsModel() {
        if (albumModel == null) {
            albumModel = new AlbumModel(null);
            albumModel.addPropertyChangeListener(new NavigatableHandler());
        }
        albumModel.setBean(getSelectedItem());
        return albumModel;
    }
    
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
    
    
    // Action Operations ******************************************************

    @Action (enabledProperty = "navigatable")
    public void newAlbum() {
        doNew();
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
     * Fires a propertyChange on the navigatable property.
     * Called from the listener to the album model's buffering property.
     */
    private void updateNavigatable() {
        boolean navigatable = isNavigatable();
        firePropertyChange("navigatable", !navigatable, navigatable);
    }

    @Action(enabledProperty = "albumSelected")
    public void editAlbum() {
        doEdit();
    }
    
    @Action(enabledProperty = "albumSelected")
    public void deleteAlbum() {
        doDelete();
    }
    
    public boolean isAlbumSelected() {
        return getAlbumSelection().hasSelection();
    }
    
    private void updateAlbumSelected() {
        boolean hasSelection = isAlbumSelected();
        firePropertyChange("albumSelected", !hasSelection, hasSelection);
        albumModel.setBean(albumSelection.getSelection());
    }

      
    // Event Handling *********************************************************
   
    /**
     * Enables or disables this model's Actions when it is notified
     * about a change in the <em>selection</em> property
     * of the SelectionInList.
     */
    private final class SelectionHandler implements PropertyChangeListener {

        
        public void propertyChange(PropertyChangeEvent evt) {
            updateAlbumSelected();
        }
    }


    /**
     * Enables or disables this model's Actions when it is notified
     * about a change in the <em>buffering</em> property
     * of the AlbumModel.
     */
    private class NavigatableHandler implements PropertyChangeListener {

        public void propertyChange(PropertyChangeEvent evt) {
            if ("buffering".equals(evt.getPropertyName()))
                updateNavigatable();
            
            
        }
        
    }

    // Managing Albums ********************************************************
    /**
     * Lets the AlbumManager removes the selected Album from the list of Albums.
     * The AlbumManager fires the list data change event. If the AlbumManager
     * wouldn't fire this event, we could use 
     * {@link SelectionInList#fireIntervalRemoved(int, int)}.
     */
    private void doDelete() {
        albumManager.removeItem(getSelectedItem());
    }
    
    
    private void doNew() {
        Album newAlbum = createAndAddItem();
        getAlbumSelection().setSelection(newAlbum);
    }
    /**
     * Edits the selected item and marks it as changed, 
     * if the editor dialog has not been canceled.
     */
    private void doEdit() {
        editSelectedItem();
    }
    
    
    /**
     * Lets the AlbumManager add the given Album to the list of Albums.
     * The AlbumManager fires the list data change event. If the AlbumManager
     * won't fire this event, we could use 
     * {@link SelectionInList#fireIntervalAdded(int, int)}.
     */
    private void addItem(Album albumToAdd) {
        albumManager.addItem(albumToAdd);
    }
    
    /**
     * Opens a AlbumEditorDialog for the given Album.
     * 
     * @param album  the Album to be edited
     * @return true if the dialog has been canceled, false if accepted
     */
    private boolean openAlbumEditor(Album album) {
        albumModel.setBean(album);
        AlbumEditorView view = new AlbumEditorView(albumModel);
        FormDialog dialog = new FormDialog(null, view);
        dialog.open();
        boolean cancelled = dialog.hasBeenCanceled();
        return cancelled;
    }
    
    private Album createAndAddItem() {
        Album newAlbum = albumManager.createItem();
        boolean canceled = openAlbumEditor(newAlbum);
        if (!canceled) {
            addItem(newAlbum);
            return newAlbum;
        }
        return null;
    }
    
    /**
     * Edits the selected item. If the editor dialog has not been canceled,
     * the presentations is notified that the contents has changed.<p>
     * 
     * This implementation fires the contents change event using
     * {@link SelectionInList#fireSelectedContentsChanged()}.
     * Since the album SelectionInList contains a ListModel, 
     * the <code>albumSelection</code> managed by the AlbumManager,
     * the AlbumManager could fire that event. However, I favored to fire
     * the contents change in the SelectionInList because this approach
     * works with underlying Lists, ListModels, and managers that don't
     * fire contents changes.
     */
    private void editSelectedItem() {
        boolean canceled = openAlbumEditor(getSelectedItem());
//        if (!canceled) {
//            getAlbumSelection().fireSelectedContentsChanged();
//        }
    }
    
    
    private Album getSelectedItem() {
        return getAlbumSelection().getSelection();
    }



}
