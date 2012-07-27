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

import java.awt.event.ActionEvent;
import java.util.logging.Logger;

import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.Box;
import javax.swing.DefaultRowSorter;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.RowFilter;
import javax.swing.table.TableModel;

import org.jdesktop.appframework.VetoableSingleSelectionAdapter;
import org.jdesktop.application.Application;
import org.jdesktop.application.ApplicationContext;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.action.AbstractActionExt;

import com.jgoodies.binding.adapter.BasicComponentFactory;
import com.jgoodies.binding.beans.Model;
import com.jgoodies.binding.beans.PropertyConnector;
import com.jgoodies.binding.list.SelectionInList;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.factories.ButtonBarFactory;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * 
 * Original - open a modal dialog for both editing and creating a new.
 * 
 * <p>
 * 
 * 
 * Builds a user interface for managing Albums using a table to display
 * the Albums and buttons to add, edit, and delete the selected album.
 * The models and Actions are provided by an underlying AlbumManagerModel.
 *
 * @author  Karsten Lentzsch
 * @version $Revision: 3198 $
 *
 * @see AlbumManagerModel 
 * @see com.jgoodies.binding.PresentationModel
 */

public final class TabularAlbumManagerView extends Model {
    @SuppressWarnings("unused")
    private static final Logger LOG = Logger
            .getLogger(TabularAlbumManagerView.class.getName());
    /**
     * Provides a list of Albums with selection and Action
     * for operating on the managed Albums.
     */
    private final AlbumManagerModel albumManagerModel;

    private JXTable  albumTable;
    private JButton newButton;
    private JButton editButton;
    private JButton deleteButton;
    private JComboBox filterCombo;
    private RowFilter<?, ?> filter;
    
    // Instance Creation ******************************************************
    
    /**
     * Constructs a list editor for editing the given list of albums.
     * 
     * @param albumManagerModel   the list of albums to edit
     */
    public TabularAlbumManagerView(AlbumManagerModel albumManagerModel) {
        this.albumManagerModel = albumManagerModel;
    }
 
    // -------------- bind filters
    
    public void setFilter(RowFilter<?, ?> filter) {
        this.filter = filter;
        ((DefaultRowSorter) albumTable.getRowSorter()).setRowFilter(filter);
    }
    
    public RowFilter getFilter() {
        return filter;
    }
    
    
    // Component Creation and Initialization **********************************

    /**
     *  Creates and intializes the UI components.
     */
    private void initComponents() {
        albumTable = new JXTable();
        albumTable.setName("albumTable");
        TableModel tableModel = TutorialUtils.createAlbumTableModel(albumManagerModel.getAlbumSelection());
        albumTable.setModel(tableModel);
        VetoableSingleSelectionAdapter selectionAdapter = new VetoableSingleSelectionAdapter(
                new JXTableSelectionConverter(
                        albumManagerModel.getAlbumSelection().getSelectionIndexHolder(), albumTable));
//                        albumManagerModel.getAlbumSelection().getSelectionIndexHolder());
//        selectionAdapter.addVetoableChangeListener(albumManagerModel.getNavigationController());
        
        albumTable.setSelectionModel(selectionAdapter);
        filterCombo = BasicComponentFactory.createComboBox(
                albumManagerModel.getFilterSelection());
        
        // quick check if changed to unselected items update the view
        
        Action update = new AbstractActionExt() {

            public void actionPerformed(ActionEvent e) {
                SelectionInList<Album> albums = albumManagerModel.getAlbumSelection();
                if (albums.getSize() == 0) return;
                Album album = albums.getElementAt(0);
                album.setClassical(!album.isClassical());
            }
            
        };
        albumTable.getActionMap().put("toggleClassical", update);
        albumTable.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
            .put(KeyStroke.getKeyStroke("F1"), "toggleClassical");   
        
        PropertyConnector.connectAndUpdate(albumManagerModel.getFilterModel(), this, "filter"); 
        newButton = new JButton();
        editButton = new JButton();
        deleteButton = new JButton();
        Application application = Application.getInstance(Application.class);
        ApplicationContext context = application.getContext();
        ActionMap actionMap = context.getActionMap(albumManagerModel.getClass(), 
                albumManagerModel);
        newButton.setAction(actionMap.get("newAlbum"));
        deleteButton.setAction(actionMap.get("deleteAlbum"));
        editButton.setAction(actionMap.get("editAlbum"));
    }
    

    // Building ***************************************************************

    /**
     * Builds and returns the panel for the Album Manager View.
     * 
     * @return the built panel
     */
    public JComponent buildPanel() {
        initComponents();

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
        JComponent buttonBar = ButtonBarFactory.buildLeftAlignedBar(
                newButton,
                editButton,
                deleteButton);
        JComponent bar = Box.createHorizontalBox();
        bar.add(buttonBar);
        bar.add(filterCombo);
        return bar;
    }
    
    
}
