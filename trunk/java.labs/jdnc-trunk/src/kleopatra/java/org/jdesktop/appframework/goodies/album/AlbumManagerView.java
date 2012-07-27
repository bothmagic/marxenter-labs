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

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.KeyStroke;
import javax.swing.RowFilter;
import javax.swing.border.EmptyBorder;

import org.jdesktop.appframework.VetoableSingleSelectionAdapter;
import org.jdesktop.application.Application;
import org.jdesktop.application.ApplicationContext;
import org.jdesktop.swingx.JXList;
import org.jdesktop.swingx.renderer.DefaultListRenderer;
import org.jdesktop.swingx.renderer.StringValue;

import com.jgoodies.binding.adapter.BasicComponentFactory;
import com.jgoodies.binding.beans.PropertyConnector;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.factories.ButtonBarFactory;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * 
 * Changed to use side-by-side list and editor.
 * 
 * <p>
 * 
 * PENDING: revise separation - this "view" isn't dumb enough. 
 * <p>
 * 
 * Builds a user interface for managing Albums using a table to display
 * the Albums and buttons to add, edit, and delete the selected album.
 * The models and Actions are provided by an underlying AlbumManagerModel.
 *
 * @author  Karsten Lentzsch
 * @version $Revision: 3359 $
 *
 * @see AlbumManagerModel 
 * @see com.jgoodies.binding.PresentationModel
 */

public final class AlbumManagerView {
    @SuppressWarnings("unused")
    private static final Logger LOG = Logger
            .getLogger(AlbumManagerView.class.getName());
    /**
     * Provides a list of Albums with selection and Action
     * for operating on the managed Albums.
     */
    private final AlbumManagerModel albumManagerModel;

    private JXList  albumList;
    private JButton newButton;
    private JButton deleteButton;

    private AlbumEditorView detailsView;

    private JButton applyButton;
    private JButton discardButton;
    private JComboBox filterCombo;
    
    private RowFilter<?, ?> filter;
    
    // Instance Creation ******************************************************
    
    /**
     * Constructs a list editor for editing the given list of albums.
     * 
     * @param albumManagerModel   the list of albums to edit
     */
    public AlbumManagerView(AlbumManagerModel albumManagerModel) {
        this.albumManagerModel = albumManagerModel;
    }
    
    /**
     * JXList filtering/sorting not yet available.
     * @param filter
     */
    public void setFilter(RowFilter<?, ?> filter) {
        this.filter = filter;
//        FilterPipeline pipeline = null;
//        if (filter != null) {
//            pipeline = new FilterPipeline(filter);
//        } 
//        albumList.setFilters(pipeline);
    }
    
    public RowFilter<?, ?> getFilter() {
        return filter;
    }
    

    // Component Creation and Initialization **********************************

    /**
     *  Creates and intializes the UI components.
     */
    private void initComponents() {
        albumList = new JXList(true);
        // Filtering the list is basically possible, but leads to loosing
        // selection on changes. That's because of the sub-optimal
        // event mapping in JXList
        albumList.setName("albumList");
        albumList.setModel(albumManagerModel.getAlbumSelection());
        StringValue format = new StringValue() {

            public String getString(Object arg0) {
                if (arg0 instanceof Album) {
                    return ((Album) arg0).getTitle();
                }
                return null;
            }
            
        };
        albumList.setCellRenderer(new DefaultListRenderer(format));
        VetoableSingleSelectionAdapter selectionAdapter = new VetoableSingleSelectionAdapter(
                new JXListSelectionConverter(
                        albumManagerModel.getAlbumSelection().getSelectionIndexHolder(), albumList));
//                        albumManagerModel.getAlbumSelection().getSelectionIndexHolder());
        selectionAdapter.addVetoableChangeListener(albumManagerModel.getNavigationController());
        
        albumList.setSelectionModel(selectionAdapter);
        filterCombo = BasicComponentFactory.createComboBox(
                albumManagerModel.getFilterSelection());
        PropertyConnector.connectAndUpdate(albumManagerModel.getFilterModel(), this, "filter"); 
        newButton = new JButton();
        deleteButton = new JButton();
        Application application = Application.getInstance(Application.class);
        ApplicationContext context = application.getContext();
        ActionMap actionMap = context.getActionMap(albumManagerModel.getClass(), 
                albumManagerModel);
        newButton.setAction(actionMap.get("newAlbum"));
        deleteButton.setAction(actionMap.get("deleteAlbum"));
        
        detailsView = new AlbumEditorView(albumManagerModel.getDetailsModel());
//        updateDetails(null);
        
        applyButton = new JButton();
        discardButton = new JButton();
        Object actionsObject = detailsView.getActionsObject();
        ActionMap detailsActions = context.getActionMap(actionsObject.getClass(), actionsObject);
        applyButton.setAction(detailsActions.get("apply"));
        discardButton.setAction(detailsActions.get("discard"));
        
        Action action = new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (!(albumList.getSelectedValue() instanceof Album)) return;
                Album album = (Album) albumList.getSelectedValue();
                album.setTitle(album.getTitle() + "X");
            }
            
        };
        detailsView.getContent().getActionMap().put("changeTitle", action);
        detailsView.getContent().getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
            .put(KeyStroke.getKeyStroke("F1"), "changeTitle");
        
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
        JComponent buttonBar = ButtonBarFactory.buildLeftAlignedBar(
                newButton,
                deleteButton);

        JComponent bar = Box.createHorizontalBox();
        bar.add(buttonBar);
        bar.add(filterCombo);
        return bar;

    }
    
    
}
