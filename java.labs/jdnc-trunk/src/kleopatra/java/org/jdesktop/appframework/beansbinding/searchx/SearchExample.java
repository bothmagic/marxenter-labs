/*
 * Created on 11.04.2007
 *
 */
package org.jdesktop.appframework.beansbinding.searchx;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.util.EventObject;

import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import org.jdesktop.appframework.FormDialog;
import org.jdesktop.appframework.swingx.SingleXFrameApplication;
import org.jdesktop.swingx.JXHeader;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.action.AbstractActionExt;
import org.jdesktop.swingx.incubatorutil.AncientSwingTeam;
import org.jdesktop.swingx.search.AbstractSearchable;

/**
 * Search support with beanbinding/appframework.
 */
public class SearchExample extends SingleXFrameApplication {
    @Override
    public void startup() {
        deleteSessionState();
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
        
        JTabbedPane pane = new JTabbedPane();
        pane.setName("searchTab");
        pane.addTab("Search with find bar", createSearchWithBarContent());
        pane.addTab("Search with find dialog", createSearchWithDialogContent());
        return pane;
    }

    private Component createSearchWithDialogContent() {
        JComponent tab = new JPanel(new BorderLayout());
        JXTable table = new JXTable(new AncientSwingTeam());
        tab.add(new JScrollPane(table));
        Action action = createFindAction(table);
        table.getActionMap().put("find", action);
        return tab;
    }

    private Action createFindAction(JXTable table) {
        FindModel model = new FindModel(table.getSearchable());
        final FindPanelView findView = new FindPanelView(model);
        Action action = new AbstractActionExt("openFind") {

            public void actionPerformed(ActionEvent e) {
                FormDialog dialog = new FormDialog(null, findView);
                dialog.addExitListener(new ExitListener() {

                    public boolean canExit(EventObject event) {
                        // hack around incomplete FormDialog logic
                        if (event == null) return false;
                        return true;
                    }

                    public void willExit(EventObject event) {
                        // TODO Auto-generated method stub
                        
                    }});
                
                dialog.open();
            }
            
        };
        return action;
    }

    private Component createSearchWithBarContent() {
//        SearchFactory.getInstance().setUseFindBar(true);
        JComponent tab = new JPanel(new BorderLayout());
        JXTable table = new JXTable(new AncientSwingTeam());
        table.getActionMap().remove("find");
        table.putClientProperty(AbstractSearchable.MATCH_HIGHLIGHTER, Boolean.TRUE);
        tab.add(new JScrollPane(table));
        FindModel model = new FindModel(table.getSearchable());
        model.setIncremental(true);
        FindBarView findView = new FindBarView(model);
        tab.add(findView.getContent(), BorderLayout.SOUTH);
        return tab;
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
        launch(SearchExample.class, args);
    }

}
