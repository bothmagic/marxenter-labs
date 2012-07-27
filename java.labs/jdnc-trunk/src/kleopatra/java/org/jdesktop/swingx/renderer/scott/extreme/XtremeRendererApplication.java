/*
 * Created on 12.01.2007
 *
 */
package org.jdesktop.swingx.renderer.scott.extreme;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.swing.AbstractListModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.ListModel;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;

import org.jdesktop.appframework.swingx.SingleXFrameApplication;
import org.jdesktop.swingx.JXHeader;
import org.jdesktop.swingx.JXList;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.JXTree;
import org.jdesktop.swingx.decorator.Highlighter;
import org.jdesktop.swingx.decorator.HighlighterFactory;
import org.jdesktop.swingx.renderer.ComponentProvider;
import org.jdesktop.swingx.renderer.DefaultListRenderer;
import org.jdesktop.swingx.renderer.DefaultTableRenderer;
import org.jdesktop.swingx.renderer.DefaultTreeRenderer;
import org.jdesktop.swingx.renderer.IconValue;
import org.jdesktop.swingx.renderer.MappedValue;
import org.jdesktop.swingx.renderer.WrappingProvider;

public class XtremeRendererApplication extends SingleXFrameApplication {
    // Alternating row color
    static final Color STRIPE_COLOR = new Color(237, 242, 249);
    List<Message> messages;

    
    @Override
    protected void initialize(String[] args) {
        super.initialize(args);
        try {
            messages = loadMessages();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (URISyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    private Component createXtremeContent() {
        MessageFormProvider messageFormProvider = new MessageFormProvider();
        // in list
        JXList list = new JXList(createListModel());
        list.setName("messageList");
        list.setCellRenderer(new DefaultListRenderer(messageFormProvider));
        Highlighter alternateRowHighlighter = 
            HighlighterFactory.createSimpleStriping(STRIPE_COLOR);
        list.addHighlighter(alternateRowHighlighter);
        list.setPrototypeCellValue("xxx");
        // in table
        JXTable table = new JXTable(createTableModel());
        table.setName("messageTable");
        table.addHighlighter(alternateRowHighlighter);
        table.setDefaultRenderer(Message.class, new DefaultTableRenderer(messageFormProvider));
        Component comp = table.prepareRenderer(table.getCellRenderer(0, 0), 0, 0);
        table.setRowHeight(comp.getPreferredSize().height);
        // in tree
        JXTree tree = new JXTree(createTreeModel());
        tree.setName("messageTree");
        tree.addHighlighter(alternateRowHighlighter);
        // let the wrapper show the image instead of the default icons
        // this looks funny, because they get scaled differently 
        MessageFormProvider treeMessageController = new MessageFormProvider(true);
        IconValue iv = new IconValue() {

            public Icon getIcon(Object value) {
                if (value instanceof Message) {
               try {
                  return new ImageIcon(((Message) value).getImageLocation().toURL());
              } catch (MalformedURLException e) {
                  // TODO Auto-generated catch block
                  e.printStackTrace();
              }
                    
                }
                return null;
            }
            
        };
        ComponentProvider provider = new WrappingProvider(treeMessageController);
        provider.setStringValue(new MappedValue(null, iv));
        tree.setCellRenderer(new DefaultTreeRenderer(provider));
        tree.setRowHeight(table.getRowHeight());
        JTabbedPane pane = new JTabbedPane();
        pane.addTab("JXTable", new JScrollPane(table));
        pane.addTab("JXList", new JScrollPane(list));
        pane.addTab("JXTree", new JScrollPane(tree));
        return pane;
    }

    public JComponent getContent() {
        JXPanel panel = new JXPanel(new BorderLayout());
        JXHeader header = new JXHeader();
        header.setName("header");
        panel.add(header, BorderLayout.NORTH);
        panel.add(createXtremeContent());
        JXHeader footer = new JXHeader();
        footer.setName("footer");
        panel.add(footer, BorderLayout.SOUTH);
        return panel;
    }


    /**
     * Wraps and returns a tree model around the messages.
     * 
     * @return
     */
    private TreeModel createTreeModel() {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode(messages.get(0));
        for (int i = 1; i < messages.size(); i++) {
            root.add(new DefaultMutableTreeNode(messages.get(i)));
        }
        return new DefaultTreeModel(root);
    }

    /**
     * Wraps and returns a table model around the messages.
     * 
     * @return
     */
    private  TableModel createTableModel() {
        TableModel model = new AbstractTableModel() {

            public int getColumnCount() {
                return 1;
            }

            public int getRowCount() {
                return messages.size();
            }

            public Object getValueAt(int rowIndex, int columnIndex) {
                return messages.get(rowIndex);
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return Message.class;
            }
            
            
            
        };
        return model;
    }

    /**
     * Wraps and returns a list model around the messages.
     * 
     * @return
     */
    private ListModel createListModel() {
        ListModel model = new AbstractListModel() {

            public Object getElementAt(int index) {
                return messages.get(index);
            }

            public int getSize() {
                return messages.size();
            }
            
        };
        return model;
    }


    private List<Message> loadMessages() throws IOException, URISyntaxException, ParseException {
        Properties props = new Properties();
        Class referenceClass = getClass();
        props.load(referenceClass.getResourceAsStream(
                "resources/messages.properties"));
        int messageCount = Integer.parseInt(props.getProperty("messages.count"));
        List<Message> messages = new ArrayList<Message>();
        for (int i = 0; i < messageCount; i++) {
            String key = Integer.toString(i) + ".";
            Message message = new Message(
                    props.getProperty(key + "subject"),
                    referenceClass.getResource("resources/" + props.getProperty(key + "image")).toURI(),
                    props.getProperty(key + "body"),
                    DateHelper.toDate(props.getProperty(key + "date")),
                    props.getProperty(key + "from"));
            messages.add(message);
        }
        return messages;
    }

    @Override
    public void startup() {
        deleteSessionState();
        show(getContent());
      }

    public static void main(String[] args) {
        launch(XtremeRendererApplication.class, args);
    }



}
