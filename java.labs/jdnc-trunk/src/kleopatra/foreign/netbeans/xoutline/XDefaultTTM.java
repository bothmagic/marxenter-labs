/*
 * Created on 30.06.2008
 *
 */
package netbeans.xoutline;

import java.util.Enumeration;
import java.util.Vector;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import org.jdesktop.swingx.tree.TreeModelSupport;
import org.jdesktop.swingx.treetable.DefaultMutableTreeTableNode;
import org.jdesktop.swingx.treetable.DefaultTreeTableModel;
import org.jdesktop.swingx.treetable.MutableTreeTableNode;
import org.jdesktop.swingx.treetable.TreeTableNode;
import org.jdesktop.swingx.util.Contract;

/**
 * Convenience model for testing. Exposes the model support.
 * 
 * TODO: add methods to super for manipulating multiple nodes.
 */
public class XDefaultTTM extends DefaultTreeTableModel {

    
    public XDefaultTTM(TreeTableNode root, Vector<String> v) {
        super(root, v);
    }

    public TreeModelSupport getTreeModelSupport() {
        return modelSupport;
    }
    
    public static XDefaultTTM convertDefaultTreeModel(DefaultTreeModel model) {
        Vector<String> v = new Vector<String>();
        v.add("A");
        XDefaultTTM ttModel = new XDefaultTTM(null, v);
        
        ttModel.setRoot(convertDefaultMutableTreeNode((DefaultMutableTreeNode) model.getRoot()));
        
        return ttModel;
    }
    
    private static DefaultMutableTreeTableNode convertDefaultMutableTreeNode(DefaultMutableTreeNode node) {
        DefaultMutableTreeTableNode ttNode = new DefaultMutableTreeTableNode(node.getUserObject());
        
        Enumeration<DefaultMutableTreeNode> children = node.children();
        
        while (children.hasMoreElements()) {
            ttNode.add(convertDefaultMutableTreeNode(children.nextElement()));
        }
        
        return ttNode;
    }

    public void nodesChanged(TreeTableNode parent, int[] childIndices) {
        Object[] children = new Object[childIndices.length];
        for (int j = 0; j < children.length; j++) {
            children[j] = parent.getChildAt(childIndices[j]);
        }
        getTreeModelSupport().fireChildrenChanged(new TreePath(getPathToRoot(parent)), 
                childIndices, children);
        
    }

    public void nodesWereRemoved(MutableTreeTableNode parent,
            int[] childIndices, Object[] children) {
        getTreeModelSupport().fireChildrenRemoved(
                new TreePath(getPathToRoot(parent)), childIndices, children);
        
    }

    public void nodesWereInserted(MutableTreeTableNode parent,
            int[] childIndices, Object[] children) {
        getTreeModelSupport().fireChildrenAdded(
                new TreePath(getPathToRoot(parent)), childIndices, children);
        
    }

    public void addNodes(MutableTreeTableNode parent, MutableTreeTableNode[] children){
        Contract.asNotNull(children, "children must not be null");
        if (children.length == 0) return;
        int last = getChildCount(parent);
        int[] childIndices = new int[children.length];
        
        for (int i = 0; i < childIndices.length; i++) {
            childIndices[i] = last + i;
        }
        insertNodes(parent, childIndices, children);
    }

    // PENDING: indices ??
    private void insertNodes(MutableTreeTableNode parent, int[] childIndices,
            MutableTreeTableNode[] children) {
        for (int i = 0; i < children.length; i++) {
            
            parent.insert(children[i], childIndices[i]);
        }
        nodesWereInserted(parent, childIndices, children);
    }
}
