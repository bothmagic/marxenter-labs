/*
 * Created on 11.04.2007
 *
 */
package org.jdesktop.appframework.beansbinding.album;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JTabbedPane;

import org.jdesktop.appframework.swingx.SingleXFrameApplication;
import org.jdesktop.swingx.JXHeader;
import org.jdesktop.swingx.JXPanel;

/**
 * Fowler's example for presentation model: AlbumBrower.
 */
public class BAlbumBrowser extends SingleXFrameApplication {
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
        
        BAlbumManager albumManager = getBAlbumManager();
        BAlbumManagerModel model = new BAlbumManagerModel(albumManager);
        JTabbedPane pane = new JTabbedPane();
        pane.setName("albumManagerTab");
        // use one only ... synthetic properties (table/list selectedElement)
        // are read only (currently, will change)
        // so sharing model/navigation between the tabs is not working
        // in fact - confuses the setup ...
        pane.addTab("Edit in editor panel", new BAlbumManagerView(model).getContent());
        pane.addTab("Edit in dialog", new BTabularAlbumManagerView(model).getContent());
//        pane.addTab("Edit in table", new BEditableTabularAlbumManagerView(model).getContent());
        return pane;
    }

    /**
     * @return
     */
    private BAlbumManager getBAlbumManager() {
        List<Album> exampleAlbums = Album.ALBUMS;
          BAlbumManager albumManager = new BAlbumManager(exampleAlbums);
        return albumManager;
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
        launch(BAlbumBrowser.class, args);
    }

}
