/*
 * Created on 12.06.2008
 *
 */
package netbeans.outline;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.util.logging.Logger;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import org.netbeans.swing.outline.DefaultOutlineModel;
import org.netbeans.swing.outline.Outline;
import org.netbeans.swing.outline.OutlineModel;
import org.netbeans.swing.outline.RowModel;

/**
 * Problem: update events from tree edits have incorrect row indices.
 */
public class OutlineUpdate {
    @SuppressWarnings("unused")
    private static final Logger LOG = Logger.getLogger(OutlineUpdate.class
            .getName());

    private JComponent createOutlineBox() {
        // a tree to edit the nodes
        final JTree tree = new JTree();
        tree.setEditable(true);
        final Action delete = new AbstractAction("delete first selected") {

            @Override
            public void actionPerformed(ActionEvent e) {
                TreePath path = tree.getSelectionPath();
                if (path == null) return;
                ((DefaultTreeModel) tree.getModel())
                    .removeNodeFromParent((MutableTreeNode) path.getLastPathComponent());
                
            }
            
        };
        
        Action deleteDis = new AbstractAction("delete first and last selected") {

            @Override
            public void actionPerformed(ActionEvent e) {
                TreePath[] path = tree.getSelectionPaths();
                if (path == null) return;
                if (path.length == 1) {
                    delete.actionPerformed(e);
                    return;
                }
                MutableTreeNode first = (MutableTreeNode) path[0].getLastPathComponent();
                MutableTreeNode last = (MutableTreeNode) path[path.length -1].getLastPathComponent();
                MutableTreeNode parent = (MutableTreeNode) first.getParent();
                int firstIndex = parent.getIndex(first);
                int lastIndex = parent.getIndex(last);
                LOG.info("first/last ... " + first + "/" + last);
                if (parent != last.getParent()) return;
                DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
                int[] childIndices = firstIndex < lastIndex ? 
                        new int[] { firstIndex, lastIndex} 
                    : new int[] {lastIndex, firstIndex};
                parent.remove(first);
                parent.remove(last);
                Object[] children = firstIndex < lastIndex ?
                    new Object[] {first, last}
                    : new Object[] {last, first};
                model.nodesWereRemoved(parent, childIndices, children);
            }
            
        };
        Action editDis = new AbstractAction("edit first/last selected") {
            @Override
            public void actionPerformed(ActionEvent e) {
                TreePath[] path = tree.getSelectionPaths();
                if (path == null) return;
                if (path.length == 1) delete.actionPerformed(e);
                DefaultMutableTreeNode first = (DefaultMutableTreeNode) path[0].getLastPathComponent();
                DefaultMutableTreeNode last = (DefaultMutableTreeNode) path[path.length -1].getLastPathComponent();
                TreeNode parent = first.getParent();
                if (parent != last.getParent()) return;
                first.setUserObject(first.getUserObject() + "XX");
                last.setUserObject(last.getUserObject() + "YY");
                DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
                int[] childIndices = new int[] { parent.getIndex(first), parent.getIndex(last)};
                model.nodesChanged(parent, childIndices);
                LOG.info("parent ... ");
            }
            
        };
        
        // outline model (with empty table part)
        final DefaultOutlineModel mdl = (DefaultOutlineModel) DefaultOutlineModel.createOutlineModel(
                tree.getModel(),  createEmptyRowModel());
        // use in outline and add table listener
        final Outline outline = new Outline(mdl);
        outline.setFillsViewportHeight(true);
        outline.getViewModel().addTableModelListener(createTableModelListener());
        
        Action expanded = new AbstractAction("check expanded") {

            @Override
            public void actionPerformed(ActionEvent e) {
                int selected = outline.getSelectedRow();
                if (selected < 0) return;
                TreePath path = mdl.getLayout().getPathForRow(selected);
                LOG.info("expanded (layout/pathSupport)" + path + 
                        "/" + mdl.getLayout().isExpanded(path)
                        + "/" + mdl.getTreePathSupport().isExpanded(path));
            }};
        JComponent box = Box.createHorizontalBox();
        box.add(new JScrollPane(outline));
        box.add(new JScrollPane(tree));
        
        JComponent buttons = Box.createVerticalBox();
        buttons.add(new JButton(editDis));
        buttons.add(new JButton(delete));
        buttons.add(new JButton(deleteDis));
        buttons.add(new JButton(expanded));
        box.add(buttons);
        return box;
    }

    


    private Component createTableBox() {
        final DefaultTableModel model = new DefaultTableModel(10, 2);
        for (int i = 0; i < model.getRowCount(); i++) {
            model.setValueAt(i, i, 0);
        }
        final JTable table = new JTable(model);
        table.setFillsViewportHeight(true);
        Action delete = new AbstractAction("deleteSelected") {

            @Override
            public void actionPerformed(ActionEvent e) {
                int selected = table.getSelectedRow();
                if (selected < 0) return;
                model.removeRow(selected);
            }};
        JComponent box = Box.createHorizontalBox();
        box.add(new JScrollPane(table));
        box.add(new JButton(delete));
        return box;
    }

    protected JComponent createContent() {
        JTabbedPane pane = new JTabbedPane();
        pane.addTab("outline", createOutlineBox());
        pane.addTab("core table", createTableBox());
        return pane;
    }


    private TableModelListener createTableModelListener() {
        TableModelListener l = new TableModelListener() {

            @Override
            public void tableChanged(TableModelEvent e) {
                LOG.info("event " + printEvent(e));
                
            }};
        return l;
    }

    
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JFrame frame = new JFrame("Outline");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.add(new OutlineUpdate().createContent());
                frame.setSize(800, 600);
                frame.setVisible(true);
            }
        });
    }

    public static String printEvent(TableModelEvent e) {
        if (e == null) return "null";
        StringBuffer buffer = new StringBuffer();
        buffer.append("Type: ");
        buffer.append(typeStrings[e.getType() + 1]);
        buffer.append(", firstRow: ");
        buffer.append(e.getFirstRow());
        buffer.append(", lastRow: ");
        buffer.append(e.getLastRow());
        buffer.append(", column: ");
        buffer.append(e.getColumn());
        return buffer.toString();
    }
    private static String[] typeStrings = { "DELETE", "UPDATE", "INSERT" };


    private RowModel createEmptyRowModel() {
        RowModel rowModel = new RowModel() {

            @Override
            public Class<?> getColumnClass(int column) {
                return Object.class;
            }

            @Override
            public int getColumnCount() {
                return 0;
            }

            @Override
            public String getColumnName(int column) {
                return "Default";
            }

            @Override
            public Object getValueFor(Object node, int column) {
                return node;
            }

            @Override
            public boolean isCellEditable(Object node, int column) {
                return true;
            }

            @Override
            public void setValueFor(Object node, int column, Object value) {
                
            }};
        return rowModel;
    }


}
