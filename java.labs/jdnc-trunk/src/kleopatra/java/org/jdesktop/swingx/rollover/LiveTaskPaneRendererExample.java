package org.jdesktop.swingx.rollover;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Box;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import org.jdesktop.beans.AbstractBean;
import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXHeader;
import org.jdesktop.swingx.JXList;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXTaskPane;
import org.jdesktop.swingx.renderer.CellContext;
import org.jdesktop.swingx.renderer.StringValue;
import org.jdesktop.swingx.util.WindowUtils;

/**
 * Quick driver for extended Rollover (live component) behaviour. 
 * Uses the extended SwingX renderer support.
 * 
 * @author Jeanette Winzenburg
 */
public class LiveTaskPaneRendererExample {
    @SuppressWarnings("unused")
    private static final Logger LOG = Logger
            .getLogger(LiveTaskPaneRendererExample.class.getName());

    private JComponent getContent() {
        JXPanel panel = new JXPanel(new BorderLayout());
        JXHeader header = new JXHeader();
        enableHTML(header, false);
        header.setTitle("TaskPane as Renderer Component");
        Icon icon = new ImageIcon(getClass().getResource("/resources/kleopatra.jpg"));
        header.setIcon(icon );
        String description = "This example implements an active RolloverRenderer " +
                        "using a JXTaskPane as component. " +                        
                        "The JXTaskPane fully reacts to mouseEvents " +
                        "(expand/collapse, normal mouseOver effects). " +
                        "<br/> ";
        header.setDescription(description);
        panel.add(header, BorderLayout.NORTH);
        final JXList list = createList();
        list.setCellRenderer(createRolloverRenderer());
        list.setFixedCellHeight(-1);
        list.setRolloverEnabled(true);

        ListModel model = createTaskPaneModel(10);

        ListDataListener listener = new ListDataListener() {

            public void contentsChanged(ListDataEvent e) {
                list.setFixedCellHeight(10);
                list.setFixedCellHeight(-1);
                list.revalidate();
                list.repaint();
                
            }

            public void intervalAdded(ListDataEvent e) {
                // TODO Auto-generated method stub
                
            }

            public void intervalRemoved(ListDataEvent e) {
                // TODO Auto-generated method stub
                
            }
            
        };
        model.addListDataListener(listener);

        list.setModel(model); 
        JComponent box = Box.createVerticalBox();
        JScrollPane pane = new JScrollPane(list);
        box.add(pane);
        JXTaskPane taskPane = getDebugTaskPane(list);
        box.add(taskPane);
        panel.add(box);
        JXHeader footer = new JXHeader();
        enableHTML(footer, true);
        footer.setIcon(null);
        footer.setTitle("Notes: ");
        String footerText = "<html><body>" +
            " <li> code: in the jdnc-incubator, section kleopatra, package rollover. <br/>" +
            " <li> link: relevant requirement discussion http://forums.java.net/jive/thread.jspa?threadID=16816 <br/>" +
//            " <li> link: <a href=\"http://forums.java.net/jive/thread.jspa?threadID=16816\">relevant requirement discussion </a> <br/>" +
            " <li> technique: use custom RolloverController to add the rendererComponent on cellEnter <br/>" +
            " <li> beware: repaint/validation is not yet reliable <br/>" +
            "</body></html>";

        footer.setDescription(footerText);
        panel.add(footer, BorderLayout.SOUTH);
        
        return panel;
    }


    private JXTaskPane getDebugTaskPane(JXList list) {
        JXTaskPane taskPane = new JXTaskPane();
        taskPane.setAnimated(false);
        taskPane.setScrollOnExpand(false);
        SampleTaskPaneModel model = (SampleTaskPaneModel) list.getElementAt(0);
        taskPane.removeAll();
        for (Action item : model.getActions()) {
            // obviously this is a no-no in the real world 
            taskPane.add(item);
        }
        taskPane.setTitle(model.getTitle());
        taskPane.setCollapsed(!model.isExpanded());
        
        LiveTaskPaneProvider provider = new LiveTaskPaneProvider();
        StringValue sv = new StringValue() {

            public String getString(Object value) {
                if (value instanceof SampleTaskPaneModel) {
                    return ((SampleTaskPaneModel) value).getTitle();
                }
                return "";
            }
            
        };
        provider.setStringValue(sv);
        LiveListCellContext context = new LiveListCellContext();
        context.replaceValue(model);
        return provider.getLiveRendererComponent();
    }


    private void enableHTML(JXHeader header, boolean hyperlinkEndabled) {
        JEditorPane editor = null;
        for (int i = 0; i < header.getComponentCount(); i++) {
            if (header.getComponent(i) instanceof JEditorPane) {
                editor = (JEditorPane) header.getComponent(i);
                break;
            }
        }
        if (editor == null) return;
        editor.setContentType("text/html");
    }
    
    private JXList createList() {
        final JXList list = new JXList() {

            @Override
            protected ListRolloverController createLinkController() {
                return new XXListRolloverController();
            }

            
        };
        return list;
    }
    
    private LiveTaskPaneListRenderer createRolloverRenderer() {
        LiveTaskPaneListRenderer renderer = new LiveTaskPaneListRenderer();
        StringValue sv = new StringValue() {
            
            public String getString(Object value) {
                if (value instanceof SampleTaskPaneModel) {
                    return ((SampleTaskPaneModel) value).getTitle();
                }
                return "";
            }
            
        };
        renderer.getComponentProvider().setStringValue(sv);
        return renderer;
    }


    private ListModel createTaskPaneModel(int count) {
        final DefaultListModel l = new ContentListeningListModel();
        for (int i = 0; i < count; i++) {
            SampleTaskPaneModel item = new SampleTaskPaneModel();
            item.setExpanded(false);
            item.setTitle("TaskPane - " + i); 
            List<Action> actions = createItemList(i);
            item.setActions(actions);
            l.addElement(item);
        }
        return l;
    }

    private List<Action> createItemList(int i) {
        List<Action> items = new ArrayList<Action>();
        for (int j = 0; j < i; j++) {
            items.add(createAction(i, j));
        }
        return items;
    }

    private Action createAction(int i, int j) {
        Action action = new AbstractAction("taskPane - " + i + " :: action at - " + j) {

            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
                
            }
            
        };
        return action;
    }

    public static class ContentListeningListModel extends DefaultListModel {
        private PropertyChangeListener p;

        @Override
        public void addElement(Object obj) {
            super.addElement(obj);
            if (obj instanceof AbstractBean) {
                ((AbstractBean) obj).addPropertyChangeListener(getPropertyChangeListener());
            }
        }

        protected void fireContentPropertyChanged(PropertyChangeEvent evt) {
            int index = indexOf(evt.getSource());
            fireContentsChanged(this, index, index);

        }
        protected PropertyChangeListener getPropertyChangeListener() {
            if (p == null) {
                 p = new PropertyChangeListener() {
                    public void propertyChange(PropertyChangeEvent evt) {
                        fireContentPropertyChanged(evt);
                    }
                };
            }
            return p;
        }
        
    }

    public static void main(String[] args) {
        setUpLookAndFeel();
        final JXFrame frame = new JXFrame(
                "JXList :: Advanced Customization",
                true);
        frame.add(new LiveTaskPaneRendererExample().getContent());
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                frame.setSize(600, 600);
                frame.setLocation(WindowUtils.getPointForCentering(frame));
                frame.setVisible(true);
            }
        });        
    }


    private static void setUpLookAndFeel() {
        try {
            UIManager.setLookAndFeel("com.jgoodies.looks.plastic.PlasticXPLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        } 
        
    }

}
