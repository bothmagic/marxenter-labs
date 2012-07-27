/*
 * Created on 12.09.2006
 *
 */
package org.jdesktop.swingx.table.asrenderer;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.HierarchyBoundsListener;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.AbstractListModel;
import javax.swing.Action;
import javax.swing.Box;
import javax.swing.ComboBoxModel;
import javax.swing.InputMap;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.table.TableModel;

import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXTable;

public class TableAsListRenderer {
    @SuppressWarnings("unused")
    private static final Logger LOG = Logger
            .getLogger(TableAsListRenderer.class.getName());
    
    
    private JComponent content;
    private MyTabularListModel model;
    private KeyBindingTableModel keyBindings;


    private JComponent getContent() {
        if (content == null) {
            content = build();
        }
        return content;
    }

    private JComponent build() {
        JComponent box = Box.createVerticalBox();
        box.add(new JLabel("KeyBindings in Table - three columns"));
        box.add(new JScrollPane(createTable()));
//        box.add(new JLabel("KeyBindings in List - two columns"));
//        JXList list = new JXList(getTabularListModel());
//        list.setCellRenderer(new TabularListRenderer(false, 2));
//        box.add(new JScrollPane(list));
        
        JComponent comboBar = Box.createHorizontalBox();
        comboBar.add(new JLabel("KeyBindings in ComboBox - three column popup: "));
        
        final Dimension fake = new Dimension();
        JComboBox combo = new JComboBox(getTabularListModel()) {
            // dirty trick by Santhosh
            //https://forums.oracle.com/forums/thread.jspa?threadID=2265297&tstart=0
            private boolean layingOut = false; 
            
            public void doLayout(){ 
                try{ 
                    layingOut = true; 
                    super.doLayout(); 
                }finally{ 
                    layingOut = false; 
                } 
            } 
         
            public Dimension getSize(){ 
                Dimension dim = super.getSize(); 
                if(!layingOut) 
                    dim.width = Math.max(dim.width, fake.width); 
                return dim; 
            } 
            
        };
        combo.setPrototypeDisplayValue("something ...");
        combo.setRenderer(new TabularListRenderer(true, getTabularListModel().getColumnNames()));
        combo.addPopupMenuListener(createPopupMenuListener());
        adjustScrollBar(combo);
        fake.setSize(adjustPopupWidth(combo));
        comboBar.add(combo);
        
        box.add(comboBar);
        return box;
    }

    private Component createTable() {
        JXTable table = new JXTable();
        table.setEditable(false);
        table.setModel(getKeyBindings());
        table.setVisibleRowCount(8);
        table.packAll();
        return table;
    }

    private KeyBindingTableModel getKeyBindings() {
        if (keyBindings == null) {
         JXTable table = new JXTable();
         keyBindings = new KeyBindingTableModel(
                table.getInputMap(JTable.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT), 
                table.getActionMap());
        }
        return keyBindings;
    }

    private PopupMenuListener createPopupMenuListener() {
        PopupMenuListener l = new PopupMenuListener() {

            public void popupMenuCanceled(PopupMenuEvent e) {
            }

            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
            }

            public void popupMenuWillBecomeVisible(final PopupMenuEvent e) {
                if (!(e.getSource() instanceof JComboBox)) {
                    return;
                }
//                adjustScrollBar((JComboBox) e.getSource());
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
//                        adjustPopupWidth((JComboBox) e.getSource());
                        
                    }
                });
            }

        };
        return l;
    }

    private void adjustScrollBar(JComboBox box) {
        if (box.getItemCount() == 0) return;
        Object comp = box.getUI().getAccessibleChild(box, 0);
        if (!(comp instanceof JPopupMenu)) {
            return;
        }
        JPopupMenu popup = (JPopupMenu) comp;
        JScrollPane scrollPane = (JScrollPane) popup.getComponent(0);
        scrollPane.setHorizontalScrollBar(new JScrollBar(JScrollBar.HORIZONTAL));
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    }
    
    boolean installed;
    // doesnt work anymore since 6u26
    // see             
    //https://forums.oracle.com/forums/thread.jspa?threadID=2265297&tstart=0
    private Dimension adjustPopupWidth(JComboBox box) {
        if (box.getItemCount() == 0) return new Dimension(0, 0);
        Object comp = box.getUI().getAccessibleChild(box, 0);
        if (!(comp instanceof JPopupMenu)) {
            return new Dimension(0, 0);
        }
        
        JPopupMenu popup = (JPopupMenu) comp;
        if (!installed) {
            installed = true;
//            popup.addHierarchyListener(getHierarchyListener());
            popup.addHierarchyBoundsListener(getHierarchyBoundsListener());
        }
        JScrollPane scrollPane = (JScrollPane) popup.getComponent(0);
        Object value = box.getItemAt(0);
        Component rendererComp = box.getRenderer().getListCellRendererComponent(null, value, 0, false, false);
        if (rendererComp instanceof JXTable) {
            scrollPane.setColumnHeaderView(((JTable) rendererComp).getTableHeader());
        }
        Dimension prefSize = rendererComp.getPreferredSize();
        Dimension size = scrollPane.getPreferredSize();
        size.width = Math.max(size.width, prefSize.width) + 2;
        scrollPane.setPreferredSize(size);
        scrollPane.setMaximumSize(size);
        scrollPane.revalidate();
        LOG.info("rend/scrollPane/combo" + prefSize + size+ box.getPreferredSize());
        return size;
    }
    

    private HierarchyListener getHierarchyListener() {
        HierarchyListener l = new HierarchyListener() {
            
            @Override
            public void hierarchyChanged(HierarchyEvent e) {
                LOG.info("" + e);
            }
        };
        return l;
    }

    private HierarchyBoundsListener getHierarchyBoundsListener() {
        HierarchyBoundsListener l = new HierarchyBoundsListener() {
            
            @Override
            public void ancestorResized(HierarchyEvent e) {
                LOG.info("" + e);
            }
            
            @Override
            public void ancestorMoved(HierarchyEvent e) {
                // TODO Auto-generated method stub
                
            }
        };
        return l;
        
    }
    private MyTabularListModel getTabularListModel() {
        if (model == null) {
            model = new MyTabularListModel(getKeyBindings());
//            model = new AncientSwingTeamList(5000);
        }
        return model;
    }
    
    public static class MyTabularListModel extends AbstractListModel implements ComboBoxModel {

        Object selected;
        List<Object[]> data;
        List<Object[]> columnNames;
        
        public MyTabularListModel() {
           initData(); 
        }
        
        public MyTabularListModel(TableModel model) {
            initData(model);
        }

        public Object[] getColumnNames() {
            return columnNames.get(0);
        }
        
        public Object getSelectedItem() {
            return selected;
        }

        public void setSelectedItem(Object anItem) {
            selected = anItem;
            fireContentsChanged(this, -1, -1);
        }

        public Object[] getElementAt(int index) {
            return data.get(index);
        }

        private void initData(TableModel model) {
            Object[] names = new Object[model.getColumnCount()];
            for (int i = 0; i < names.length; i++) {
                names[i] = model.getColumnName(i);
            }
            columnNames = new ArrayList<Object[]>();
            columnNames.add(names);
            
            data = new ArrayList<Object[]>();
            for (int row = 0; row < model.getRowCount(); row++) {
                Object[] rowData = new Object[names.length];
                for (int column = 0; column < rowData.length; column++) {
                    rowData[column] = model.getValueAt(row, column);
                }
                data.add(rowData);
            }
        }
        private void initData() {
            columnNames = new ArrayList<Object[]>();
            columnNames.add(new Object[] {"KeyStroke", "Action Name", "Action Command"});
            data = new ArrayList<Object[]>();
            
            JXTable table = new JXTable();
            InputMap im = table.getInputMap(JXTable.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
            KeyStroke[] allKeys = im.allKeys();
            for (KeyStroke stroke : allKeys) {
                Object actionKey = im.get(stroke);
                Action action = table.getActionMap().get(actionKey);
                Object name = (action != null) ? action.getValue(Action.NAME) : "none";
                data.add(new Object[] {stroke, actionKey, name});
            }
            
        }
        public int getSize() {
            return data.size();
        }

        
    }

    public static void main(String[] args) {
        JXFrame frame = new JXFrame("Use Table as ListCellRenderer", true);
        frame.add(new TableAsListRenderer().getContent());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

}
