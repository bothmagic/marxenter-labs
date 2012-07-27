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

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JTabbedPane;

import org.jdesktop.appframework.swingx.SingleXFrameApplication;
import org.jdesktop.swingx.JXHeader;
import org.jdesktop.swingx.JXPanel;





/**
 * copied from JGoodies Binding and adjusted to Application.
 * 
 * 
 */
public final class AlbumManagerApplication extends SingleXFrameApplication {

    @Override
    public void startup() {
//        deleteSessionState();
        JComponent content = getContent();
        show(content);
    }

    /**
     * @return
     */
    private JComponent getContent() {
        JComponent content = new JXPanel(new BorderLayout());
        content.add(createHeader("header"), BorderLayout.NORTH);
        content.add(getAlbumManagerContent());
        content.add(createHeader("footer"), BorderLayout.SOUTH);
        return content;
    }
    
    /**
     * @return
     */
    private JTabbedPane getAlbumManagerContent() {
        AlbumManagerModel model = getAlbumManagerModel();
        JTabbedPane pane = new JTabbedPane();
        pane.setName("albumManagerTab");
        JComponent combined = new AlbumManagerView(model).buildPanel();
        JComponent panel = new TabularAlbumManagerView(model).buildPanel();
        pane.addTab("Edit in place", combined);
        pane.addTab("Edit in dialog", panel);
        return pane;
    }

    /**
     * @return
     */
    private AlbumManagerModel getAlbumManagerModel() {
        List<Album> exampleAlbums = Album.ALBUMS;
        AlbumManager albumManager = new AlbumManager(exampleAlbums);
        AlbumManagerModel model = new AlbumManagerModel(albumManager);
        return model;
    }


    /**
     * @param footerName
     * @return
     */
    private JXHeader createHeader(String footerName) {
        JXHeader footer = new JXHeader();
          footer.setName(footerName);
        return footer;
    }

    public static void main(String[] args) {
        launch(AlbumManagerApplication.class, args);
    }
        
}
