/*
 * XmlTreeTableModel.java
 *
 * Created on October 31, 2006, 5:44 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package demo;

import com.exalto.TreeTableModel;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.event.TreeModelEvent;
import javax.swing.tree.TreeNode;
import org.w3c.dom.Document;

/**
 *
 * @author omprakash.v
 */
public interface XmlTreeModel extends TreeTableModel {

    public ArrayList getParentList();
    
    public Document getDocument();
    
    public TreeNode[] getPathToRoot(TreeNode en);
    
    public void setRowMapper(HashMap mapper);
    
    public void setParentList(ArrayList plist);
    
    public Object getRoot();
    
    public boolean getRootVisible();
    
    public HashMap getRowMapper();
    
    public Object getValueAt(Object node, int row, int col);
    
    public boolean isCellEditable(Object node, int row, int col);
    
    public ArrayList getColumnMappingList();

    public void fireTreeNodesInserted(TreeModelEvent e);

    public void fireTreeNodesRemoved(TreeModelEvent e);

}
