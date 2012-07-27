/*
 * Created on 11.04.2007
 *
 */
package org.jdesktop.appframework.beansbinding.album;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;

import org.jdesktop.beans.AbstractBean;
import org.jdesktop.observablecollections.ObservableCollections;
import org.jdesktop.observablecollections.ObservableList;
import org.jdesktop.observablecollections.ObservableCollections.ObservableListHelper;

/**
 * 
 */
public class BAlbumManager extends AbstractBean {
    
    /**
     * Holds the List of Albums. Albums are added and removed from this List.
     * The ObservableList implements ListModel, and so, we can directly
     * use this List for the UI and can observe changes.<p>
     * 
     * In a real world application this List may be kept 
     * in synch with a database.
     */
    private List<Album> managedAlbums;
    private PropertyChangeListener elementListener;
//   private ObservableListHelper<Album> managedAlbums; 
   
    // Instance Creation ******************************************************
    
    /**
     * Constructs a AlbumManager for the given list of Albums.
     * 
     * @param albums   the list of Albums to manage
     */
    public BAlbumManager(List<Album> albums) {
//        this.managedAlbums = new ArrayList<Album>(albums);
        this.managedAlbums = ObservableCollections.observableList(new ArrayList<Album>(albums));
//        this.managedAlbums = ObservableCollections.observableListHelper(
//                new ArrayList<Album>(albums));
//        installElementListener();
    }
    
    
//    private void installElementListener() {
//        for (Object element : getManagedAlbums()) {
//            if (element instanceof AbstractBean) {
//                // this should be handled automatically by 
//                // the ArrayListModel
//                ((AbstractBean) element).addPropertyChangeListener(getElementListener());
//            }
//        }
//        
//    }
//
//
//    private PropertyChangeListener getElementListener() {
//        if (elementListener == null) {
//          elementListener = new PropertyChangeListener() {
//
//            public void propertyChange(PropertyChangeEvent evt) {
////                    final int index = getManagedAlbums().indexOf(evt.getSource());
////                    if (index >= 0) {
////                        SwingUtilities.invokeLater(new Runnable() {
////                            public void run() {
////                                // this should be handled automatically by
////                                // the ArrayListModel
////                                // weird side-effect if not invoked
////                                managedAlbums.fireElementChanged(index);
////                            }
////                        });
////                    }
//
//                }};  
//        }
//        return elementListener;
//    }
//

    // Exposing the ListModel of Albums ****************************************
    
    public List<Album> getManagedAlbums() {
        return managedAlbums;
//        return managedAlbums.getObservableList();
    }
    
    
    // Managing Albums *********************************************************
    
    /**
     * Creates and return a new Album.
     * 
     * @return the new Album
     */
    public Album createItem() {
        return new Album();
    }
    
    
    /**
     * Adds the given Album to the List of managed Albums
     * and notifies observers of the managed Albums ListModel
     * about the change.
     * 
     * @param albumToAdd   the Album to add
     */
    public void addItem(Album albumToAdd) {
        getManagedAlbums().add(albumToAdd);
//         this should be handled automatically by 
//         the ArrayListModel
//        albumToAdd.addPropertyChangeListener(getElementListener());
    }
    
    
    /**
     * Removes the given Album from the List of managed Albums
     * and notifies observers of the managed Albums ListModel
     * about the change.
     * 
     * @param albumToRemove    the Album to remove
     */
    public void removeItem(Album albumToRemove) {
        getManagedAlbums().remove(albumToRemove);
        // this should be handled automatically by 
        // the ArrayListModel
//        albumToRemove.removePropertyChangeListener(getElementListener());
    }

}
