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

import java.util.logging.Logger;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.text.JTextComponent;

import org.jdesktop.appframework.FormView;
import org.jdesktop.beans.AbstractBean;

import com.jgoodies.binding.adapter.BasicComponentFactory;
import com.jgoodies.binding.beans.PropertyConnector;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * Changed to implement FormView.<p>
 * 
 * 
 * Builds a form to display and edit Albums. The editor components are bound 
 * to the domain using BufferedValueModels returned by a PresentationModel 
 * that holds a Album. This PresentationModel is the AlbumManagerModel's
 * 'albumEditorModel'. Whenever the AlbumManagerModel changes the selected Album,
 * the PresentationModel updates its bean, in this case the Album.
 *
 * @author Karsten Lentzsch
 * @version $Revision: 2249 $
 * 
 * @see AlbumManagerModel
 * @see org.jdesktop.appframework.goodies.album.AlbumPresentationModel
 * @see AlbumModel
 * @see com.jgoodies.binding.PresentationModel
 */

public final class AlbumEditorView extends AbstractBean implements FormView {
    static final Logger LOG = Logger.getLogger(AlbumEditorView.class
            .getName());
    /**
     * Holds the edited Album and vends ValueModels that adapt Album properties.
     */
    private final AlbumModel presentationModel;

    private JTextComponent titleField;
    private JTextComponent artistField;
    private JCheckBox      classicalBox;
    private JTextComponent composerField;

    private JComponent content;
    private JLabel composerLabel;
    // Instance Creation ******************************************************
    
    /**
     * Constructs an AlbumEditorView for the given Album PresentationModel.
     * 
     * @param albumPresentationModel provides the adapting ValueModels
     */
    public AlbumEditorView(AlbumModel albumPresentationModel) {
        this.presentationModel = albumPresentationModel;
    }
    
//------------------ implement FormView
    
    public Object getActionsObject() {
        return presentationModel;
    }

    /**
     * Returns the editorPanel. Lazily creates one if not yet done.
     */
    public JComponent getContent() {
        if (content == null) {
            initComponents();
            initEventHandling();
            content = build();
        }
        return content;
    }
   

    public String getName() {
        return "editorView";
    }
    
    // Initialization *********************************************************

    /**
     *  Creates and intializes the UI components.
     */
    private void initComponents() {
        titleField = BasicComponentFactory.createTextField(
                presentationModel.getBufferedModel(Album.PROPERTYNAME_TITLE), false);
        artistField = BasicComponentFactory.createTextField(
                presentationModel.getBufferedModel(Album.PROPERTYNAME_ARTIST), false);
        classicalBox = BasicComponentFactory.createCheckBox(
                presentationModel.getBufferedModel(Album.PROPERTYNAME_CLASSICAL), 
                "");
        composerField = BasicComponentFactory.createTextField(
                presentationModel.getBufferedModel(Album.PROPERTYNAME_COMPOSER), false);
        composerLabel = new JLabel();
        composerLabel.setName("composerLabel");
        composerLabel.setLabelFor(composerField);
    }
    
    
    /**
     * Observes the presentation model's <em>bufferedComposerEnabled</em> 
     * property to update the composer field's enablement.
     * Also, initializes the field's enablement with the current value
     * of the buffered composer enabled property.
     */
    private void initEventHandling() {
        bindComposerEnabled(composerField);
        bindComposerEnabled(composerLabel);
    }

    /**
     * @param comp
     */
    private void bindComposerEnabled(JComponent comp) {
        PropertyConnector connector = PropertyConnector.connect(
                presentationModel, 
                AlbumModel.PROPERTYNAME_BUFFERED_COMPOSER_ENABLED,
                comp,
                "enabled");
        connector.updateProperty2();
    }
    
    
    // Building ***************************************************************

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
        builder.add(composerLabel, cc.xy(1, 7));
        builder.add(composerField,   cc.xy(3, 7));
        
        JPanel panel = builder.getPanel();
        panel.setName(getName());
        
        return panel;
        
    }

    
}
