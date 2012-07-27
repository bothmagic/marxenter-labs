/*
 * Created on 12.12.2006
 *
 */
package org.jdesktop.swingx.link;

import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import org.jdesktop.swingx.JXHyperlink;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.hyperlink.LinkModel;
import org.jdesktop.swingx.hyperlink.LinkModelAction;
import org.jdesktop.swingx.incubatorutil.InteractiveTestCase;
import org.jdesktop.swingx.renderer.DefaultTableRenderer;
import org.jdesktop.swingx.renderer.HyperlinkProvider;

public class LinkDemo extends InteractiveTestCase {
    
    public static void main(String[] args) throws Exception {
//      setSystemLF(true);
      LinkDemo test = new LinkDemo();
      try {
          test.runInteractiveTests();
        } catch (Exception e) {
            System.err.println("exception when executing interactive tests:");
            e.printStackTrace();
        } 
  }


    /**
     * Compare LinkModel vs. URL in JXHyperlink.
     */
    public void interactiveHyperlink() {
        URL url = this.getClass().getResource("/resources/test.html");
        JComponent box = Box.createVerticalBox();
        box.add(new JLabel("BrowserAction - use URL directly:"));
        box.add(new JXHyperlink(new BrowserAction(url)));
        box.add(new JLabel("BrowserLinkAction - wrap URL in LinkModel:"));
        LinkModel link = new LinkModel("Click me!", null, url);
        LinkModelAction linkAction = new LinkModelAction<LinkModel>(link, new BrowserLinkVisitor());
        JXHyperlink hyperlink = new JXHyperlink(linkAction);
        box.add(hyperlink);
        JFrame frame = wrapInFrame(box, "Hyperlink: LinkModel vs. URL");
        frame.setSize(400, 200);
        frame.setVisible(true);
    }


    /**
     * Compare LinkModel vs URL in JXTable.
     *
     */
    public void interactiveTableWithHyperlinks() {
        JXTable table = new JXTable(createTableModel());
        table.setDefaultRenderer(URL.class, new DefaultTableRenderer(
                new HyperlinkProvider(new BrowserAction(), URL.class)));
        table.setDefaultRenderer(LinkModel.class, new DefaultTableRenderer(
                new HyperlinkProvider(new LinkModelAction<LinkModel>(new BrowserLinkVisitor()), LinkModel.class)));
        table.packColumn(1, -1);
        table.packColumn(2, -1);
        JFrame frame = wrapWithScrollingInFrame(table, "Table: LinkModel vs. URL");
        frame.setVisible(true);
    }

    private TableModel createTableModel() {
        String[] columnNames = { "text only", "LinkModel", "URL", "Bool" };
        
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public Class<?> getColumnClass(int column) {
                return getValueAt(0, column).getClass();
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                    return false;
            }
            
        };
        for (int i = 0; i < 4; i++) {
            try {
                URL url = new URL("http://some.dummy.url" + i);
                LinkModel link = new LinkModel("a link text " + i, null, url);
                if (i == 1) {
                    url = getClass().getResource("/resources/test.html");
                    link = new LinkModel("a link text " + i, null, url);
                }
                model.addRow(new Object[] {"text only " + i, link, url, Boolean.TRUE });
            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return model;
    }


}
