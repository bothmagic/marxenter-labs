/*
 * Created on 12.06.2008
 *
 */
package netbeans.xoutline;

import java.awt.event.ActionEvent;
import java.util.logging.Logger;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import org.jdesktop.swingx.treetable.DefaultMutableTreeTableNode;
import org.jdesktop.swingx.treetable.MutableTreeTableNode;
import org.jdesktop.swingx.treetable.TreeTableModel;
import org.jdesktop.swingx.treetable.TreeTableNode;

/**
 * Problem: update events from tree edits have incorrect row indices.
 */
public class XXTreeTableExperiment {
    @SuppressWarnings("unused")
    private static final Logger LOG = Logger.getLogger(XXTreeTableExperiment.class
            .getName());

    protected JComponent createContent() {
        // a tree to edit the nodes
        final JTree tree = new JTree();
        tree.setModel(XDefaultTTM.convertDefaultTreeModel((DefaultTreeModel) tree.getModel()));
        tree.setEditable(true);
        // use in outline and add table listener
        final JXXTreeTable outline = new JXXTreeTable((TreeTableModel) tree.getModel());
        outline.setRootVisible(true);
        outline.getModel().addTableModelListener(createTableModelListener());
        final Action delete = new AbstractAction("delete first selected") {

            @Override
            public void actionPerformed(ActionEvent e) {
                TreePath path = tree.getSelectionPath();
                if (path == null) return;
                ((XDefaultTTM) tree.getModel())
                    .removeNodeFromParent((MutableTreeTableNode) path.getLastPathComponent());
                
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
                MutableTreeTableNode first = (MutableTreeTableNode) path[0].getLastPathComponent();
                MutableTreeTableNode last = (MutableTreeTableNode) path[path.length -1].getLastPathComponent();
                MutableTreeTableNode parent = (MutableTreeTableNode) first.getParent();
                int firstIndex = parent.getIndex(first);
                int lastIndex = parent.getIndex(last);
                LOG.info("first/last ... " + first + "/" + last);
                if (parent != last.getParent()) return;
                XDefaultTTM model = (XDefaultTTM) tree.getModel();
                int[] childIndices = firstIndex < lastIndex ? 
                        new int[] { firstIndex, lastIndex} 
                    : new int[] {lastIndex, firstIndex};
                parent.remove(first);
                parent.remove(last);
                Object[] children = firstIndex < lastIndex ?
                    new Object[] {first, last}
                    : new Object[] {last, first};
//                model.getTreeModelSupport().fireChildrenRemoved(
//                        outline.get, indices, children)    
                model.nodesWereRemoved(parent, childIndices, children);
            }
            
        };
        Action editDis = new AbstractAction("edit first/last selected") {
            @Override
            public void actionPerformed(ActionEvent e) {
                TreePath[] path = tree.getSelectionPaths();
                if (path == null) return;
                if (path.length == 1) delete.actionPerformed(e);
                DefaultMutableTreeTableNode first = (DefaultMutableTreeTableNode) path[0].getLastPathComponent();
                DefaultMutableTreeTableNode last = (DefaultMutableTreeTableNode) path[path.length -1].getLastPathComponent();
                TreeTableNode parent = first.getParent();
                if (parent != last.getParent()) return;
                first.setUserObject(first.getUserObject() + "XX");
                last.setUserObject(last.getUserObject() + "YY");
                XDefaultTTM model = (XDefaultTTM) tree.getModel();
                int[] childIndices = new int[] { parent.getIndex(first), parent.getIndex(last)};
                model.nodesChanged(parent, childIndices);
                LOG.info("parent ... ");
            }
            
        };
        Action insert = new AbstractAction("insert nodes") {
            @Override
            public void actionPerformed(ActionEvent e) {
                TreePath path = tree.getSelectionPath();
                if (path == null) return;
                
                DefaultMutableTreeTableNode parent = (DefaultMutableTreeTableNode) path.getLastPathComponent();

                DefaultMutableTreeTableNode lastChild = new DefaultMutableTreeTableNode("new last");
                DefaultMutableTreeTableNode firstChild = new DefaultMutableTreeTableNode("new first");  
                int count = parent.getChildCount();
                int first = 0;
                int last = count + 1;
                XDefaultTTM model = (XDefaultTTM) tree.getModel();
                parent.insert(firstChild, first);
                parent.insert(lastChild, last);
                int[] childIndices = new int[] {first, last};
                model.nodesWereInserted(parent, childIndices, new Object[] {firstChild, lastChild});
                LOG.info("parent ... ");
            }
            
        };

        Action addNodes = new AbstractAction("add nodes") {
            @Override
            public void actionPerformed(ActionEvent e) {
                TreePath path = tree.getSelectionPath();
                if (path == null) return;
                
                DefaultMutableTreeTableNode parent = (DefaultMutableTreeTableNode) path.getLastPathComponent();

                DefaultMutableTreeTableNode lastChild = new DefaultMutableTreeTableNode("new last");
                DefaultMutableTreeTableNode firstChild = new DefaultMutableTreeTableNode("new first");  
                XDefaultTTM model = (XDefaultTTM) tree.getModel();
                model.addNodes(parent, new MutableTreeTableNode[] {firstChild, lastChild});
                LOG.info("parent ... ");
            }
            
        };

        Action structureChanged = new AbstractAction("structureChange on selected (in tree)") {

            @Override
            public void actionPerformed(ActionEvent e) {
                TreePath path = tree.getSelectionPath();
                if (path == null) return;
                XDefaultTTM model = (XDefaultTTM) tree.getModel();
                model.getTreeModelSupport().fireTreeStructureChanged(path);
            }
            
        };
        Action structureChangedTreeTable = new AbstractAction("structureChange on selected (in treetable)") {

            @Override
            public void actionPerformed(ActionEvent e) {
                int selected = outline.getSelectedRow();
                if (selected < 0) return;
                TreePath path = outline.getPathForRow(selected);
                if (path == null) return;
                XDefaultTTM model = (XDefaultTTM) tree.getModel();
                model.getTreeModelSupport().fireTreeStructureChanged(path);
            }
            
        };
        Action expanded = new AbstractAction("check expanded") {

            @Override
            public void actionPerformed(ActionEvent e) {
                int selected = outline.getSelectedRow();
                if (selected < 0) return;
                DefaultXOutlineModel mdl = (DefaultXOutlineModel) outline.getModel();
                TreePath path = mdl.getLayout().getPathForRow(selected);
                LOG.info("expanded (layout/pathSupport)" + path + 
                        "/" + mdl.getLayout().isExpanded(path)
                        + "/" + mdl.getTreePathSupport().isExpanded(path));
            }};

        Action expandHidden = new AbstractAction("expand hidden") {

            @Override
            public void actionPerformed(ActionEvent e) {
                TreePath collapsed = getFirstHiddenCollapsed();
                if (collapsed != null) {
                    outline.expandPath(collapsed);
                }
                
            }

            private TreePath getFirstHiddenCollapsed() {
                TreePath path = outline.getPathForRow(0);
                DefaultXOutlineModel mdl = (DefaultXOutlineModel) outline.getModel();
                Object parent = mdl.getRoot();
                for (int i = 0; i < mdl.getChildCount(parent); i++) {
                    if (outline.isCollapsed(path)) {
                        // construction site ...
                    }
                    
                }
                return null;
            }
            
        };
        expandHidden.setEnabled(false);
        Action toggleRootVisible = new AbstractAction("toggleRootVisible") {

            @Override
            public void actionPerformed(ActionEvent e) {
                outline.setRootVisible(!outline.isRootVisible());
                
            }
            
        };
        JComponent box = Box.createHorizontalBox();
        box.add(new JScrollPane(outline));
        box.add(new JScrollPane(tree));
        
        JComponent buttons = Box.createVerticalBox();
        buttons.add(new JButton(editDis));
        buttons.add(new JButton(delete));
        buttons.add(new JButton(deleteDis));
        buttons.add(new JButton(insert));
        buttons.add(new JButton(addNodes));
        buttons.add(new JButton(structureChanged));
        buttons.add(new JButton(structureChangedTreeTable));
        buttons.add(new JButton(expanded));
        buttons.add(new JButton(expandHidden));
        buttons.add(new JButton(toggleRootVisible));
        box.add(buttons);
        return box;
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
        updateLF();
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JFrame frame = new JFrame("Outline");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.add(new XXTreeTableExperiment().createContent());
                frame.setSize(800, 600);
                frame.setVisible(true);
            }
        });
    }

    private static void updateLF() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } 
        
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


}
