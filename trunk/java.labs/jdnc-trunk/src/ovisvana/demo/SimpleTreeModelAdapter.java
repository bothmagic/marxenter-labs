package demo;

/*
 * Copyright 1997-1999 Sun Microsystems, Inc. All Rights Reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are permitted provided that the following
 * conditions are met:
 *
 * - Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * - Redistribution in binary form must reproduce the above
 *   copyright notice, this list of conditions and the following
 *   disclaimer in the documentation and/or other materials
 *   provided with the distribution.
 *
 * Neither the name of Sun Microsystems, Inc. or the names of
 * contributors may be used to endorse or promote products derived
 * from this software without specific prior written permission.
 *
 * This software is provided "AS IS," without a warranty of any
 * kind. ALL EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND
 * WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE HEREBY
 * EXCLUDED. SUN AND ITS LICENSORS SHALL NOT BE LIABLE FOR ANY
 * DAMAGES OR LIABILITIES SUFFERED BY LICENSEE AS A RESULT OF OR
 * RELATING TO USE, MODIFICATION OR DISTRIBUTION OF THIS SOFTWARE OR
 * ITS DERIVATIVES. IN NO EVENT WILL SUN OR ITS LICENSORS BE LIABLE
 * FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT,
 * SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER
 * CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF
 * THE USE OF OR INABILITY TO USE THIS SOFTWARE, EVEN IF SUN HAS
 * BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 *
 * You acknowledge that this software is not designed, licensed or
 * intended for use in the design, construction, operation or
 * maintenance of any nuclear facility.
 */
import com.exalto.ExaltoXmlNode;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Stack;

import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.exalto.org.jdesktop.swingx.JXTreeTable;
import java.awt.Rectangle;
import java.awt.event.MouseListener;
import java.util.List;
import java.util.Vector;
import javax.swing.JOptionPane;

/**
 * This is a wrapper class takes a TreeTableModel and implements
 * the table model interface. The implementation is trivial, with
 * all of the event dispatching support provided by the superclass:
 * the AbstractTableModel.
 *
 * @version 1.2 10/27/98
 *
 * @author Philip Milne
 * @author Scott Violet
 */
public class SimpleTreeModelAdapter extends JXTreeTable.TreeTableModelAdapter  implements MouseListener {
    JTree atree;
    XmlTreeModel treeTableModel;
    int maxCol = 0;
    int currCol = 0;
    int currRow = 0;
    
    boolean treeExpanded = false;
    boolean treeCollapsed = false;
    
    SelectiveBreadthFirstEnumeration senum;
    
    public static final String APP_NAME = "EXALTO GRID EDITOR";
    protected boolean m_xmlChanged = false;
    boolean isEditing = false;
    protected int selectedRow;
    protected int selectedCol;
    protected GridHelper gridHelper;
    protected JTable    table;
   

    
    public SimpleTreeModelAdapter(XmlTreeModel treeTblModel, JTree tree, JTable table) {
        
        super(treeTblModel, tree);
        
        this.atree = tree;
        this.table = table;
        
        this.treeTableModel = (XmlTreeModel) treeTblModel;
        senum = new SelectiveBreadthFirstEnumeration(treeTableModel,
                atree);
        
        gridHelper = new GridHelper(this);
        
        tree.addTreeExpansionListener(new TreeExpansionListener() {
            
          /* Don't use fireTableRowsInserted() here; the selection model
          // would get updated twice.
           */
            public void treeExpanded(TreeExpansionEvent event) {
                /*    System.out.println(" in treeExpanded firing table changed treepath = " + event.getPath()); */
                fireTableDataChanged();
                treeExpanded = true;
                treeCollapsed = false;
                
                System.out.println(" tree expanded true%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% ");
                
                
                
            }
            public void treeCollapsed(TreeExpansionEvent event) {

               System.out.println(" tree collapsed true $$$$$$$$$$$$$$$$$$$$$$$$");

                treeCollapsed = true;
                treeExpanded = false;
                fireTableDataChanged();
            }
        });
        
        
      /* Installs a TreeSelectionListener that can update the table when
      // the tree node changes.
       */
        TreeSelectionListener lSel = new TreeSelectionListener() {
            
            public void valueChanged(TreeSelectionEvent e) {
                /*  ExaltoXmlNode node = getSelectedTreeNode(); */
                
                
                
                ExaltoXmlNode node = (ExaltoXmlNode) atree.getLastSelectedPathComponent();
                
                System.out.println(" in valuechanged node = " + node);
                
                if(node == null)
                    return;
                
                atree.startEditingAtPath(new TreePath(node.getPath()));
                System.out.println(" new value " + node);
                
            }
        };
        
        atree.addTreeSelectionListener(lSel);
        
      /* Installs a TreeModelListener that can update the table when
      // the tree changes. We use delayedFireTableDataChanged as we can
      // not be guaranteed the tree will have finished processing
      // the event before us.
       */
        treeTableModel.addTreeModelListener(new TreeModelListener() {
            
            public void treeNodesChanged(TreeModelEvent e) {
                /*   System.out.println(" in treeNodesChanged"); */
                delayedFireTableDataChanged();
                //     delayedFireTableDataChanged(e, 0);
            }
            public void treeNodesInserted(TreeModelEvent e) {
                /*     System.out.println(" in treeNodesInserted"); */
//            delayedFireTableDataChanged(e, 1);
                //              delayedFireTableDataChanged();
            }
            public void treeNodesRemoved(TreeModelEvent e) {
                /*         System.out.println(" in treeNodesRemoved"); */
//            delayedFireTableDataChanged(e, 2);
                delayedFireTableDataChanged();
            }
            public void treeStructureChanged(TreeModelEvent e) {
                /*      System.out.println(" in treeStructureChanged"); */
                delayedFireTableDataChanged();
            }
        });
    }
    /* Wrappers, implementing TableModel interface. */
    public int getColumnCount() {
        return treeTableModel.getColumnCount();
    }
    public String getColumnName(int column) {
        return treeTableModel.getColumnName(column);
    }
/*
    public Class getColumnClass(int column) {
      return treeTableModel.getColumnClass(column);
    }
 */
    public int getRowCount() {
        return atree.getRowCount();
    }
    
    protected Object nodeForRow(int row) {
        TreePath treePath = atree.getPathForRow(row);
        
        return treePath.getLastPathComponent();
    }
    
/*
    public Object getValueAt(int row, int column) {
            return treeTableModel.getValueAt(nodeForRow(row), column);
    }
 */
    public Object getValueAt(int row, int column) {
        return treeTableModel.getValueAt(nodeForRow(row), row, column);
    }
    
    public boolean isCellEditable(int row, int column) {
        return treeTableModel.isCellEditable(nodeForRow(row), row, column);
    }
    
    public void setValueAt(Object value, int row, int column) {
        
    }
    /**
     * Invokes fireTableDataChanged after all the pending events have been
     * processed. SwingUtilities.invokeLater is used to handle this.
     */
    protected void delayedFireTableDataChanged() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                fireTableDataChanged();
            }
        });
    }
    /**
     * Invokes fireTableDataChanged after all the pending events have been
     * processed. SwingUtilities.invokeLater is used to handle this.
     */
    protected void delayedFireTableDataChanged(final TreeModelEvent
            tme,
            final int typeChange) {
/*
      SwingUtilities.invokeLater(new Runnable() {
          public void run() {
            fireTableDataChanged();
          }
      });
 */
        
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                
                int indices[] = tme.getChildIndices();
                TreePath path = tme.getTreePath();
                if (indices != null) {
                    if (atree.isExpanded(path)) { /* Dont bother to update if the parent
                                                    // node is collapsed */
                        
                        int startingRow =
                                atree.getRowForPath(path)+1;
                        int min = Integer.MAX_VALUE;
                        int max = Integer.MIN_VALUE;
                        for (int i=0;i<indices.length;i++) {
                            if (indices[i] < min) {
                                min = indices[i];
                            }
                            if (indices[i] > max) {
                                max = indices[i];
                            }
                        }
                        switch (typeChange) {
                            case 0 :
                                fireTableRowsUpdated(startingRow +
                                        min,
                                        startingRow+max);
                                break;
                            case 1:
                                fireTableRowsInserted(startingRow +
                                        min, startingRow+max);
                                break;
                            case 2:
                                fireTableRowsDeleted(startingRow +
                                        min,
                                        startingRow+max);
                                break;
                        }
                    } else {
                        /* not expanded - but change might effect appearance of parent
                        // Issue #82-swingx */
                        int row = atree.getRowForPath(path);
                        fireTableRowsUpdated(row, row);
                    }
                } else {  /* case where the event is fired to identify root. */
                    fireTableDataChanged();
                }
            }
        });
    }
    
    public ArrayList getColumnMappingList() {
        return treeTableModel.getColumnMappingList();
    }
    public ArrayList getParentList() {
        return treeTableModel.getParentList();
    }
    
    
    public SelectiveBreadthFirstEnumeration getSelectiveBreadthFirstEnumeration() {
        return senum;
    }
    
    public void updateColumnMapping(String opfunc) {
        
        HashMap rowMapper = new HashMap();
        
        int currRow = 0;
        int currCol = 0;
        ArrayList nodeList = new ArrayList();
        ArrayList parentList = new ArrayList();
        Hashtable  nodeMapTbl = new Hashtable();
        Stack colStack = new Stack();
        int tempCol = 0;
        boolean restored = false;
     //   System.out.println("TTMA currRow " + currRow);
     //   System.out.println("TTMA  currCol " + currCol);
        
        TreeNode root = (TreeNode) treeTableModel.getRoot();
        
        ExaltoXmlNode docnode = (ExaltoXmlNode) root;
        nodeMapTbl.put(docnode, currRow + "," + currCol);
        
        senum.reset(opfunc);
        while (senum.hasMoreElements()) {
            ExaltoXmlNode inode =
                    (ExaltoXmlNode) senum.nextElement();
            
            
        //    System.out.println("TTMA  child Node name = " +  inode.toString());
            
    /*
      //               System.out.println("TTMA  child Node type = " +
//    inode.getNodeType());
     */
            int nlev = inode.getLevel();
            
         //   System.out.println("TTMA  child Node level = " + nlev);
            
            if(inode.getNodeType() == Node.ELEMENT_NODE || inode.getNodeType() == Node.DOCUMENT_NODE) {
                
             /*    System.out.println("TTMA adding at currRow " + currRow);
         //        System.out.println("TTMA adding at currCol " + currCol); */
                nlev = 0;
                
                nodeMapTbl.put(inode, currRow + "," + nlev);
                
                if(nlev > maxCol)
                    maxCol = nlev;
                
                
                Node xmlNode = inode.getXmlNode();
                
                int g=0;
                if(inode.getNodeType() == Node.ELEMENT_NODE) {
                    
                      NamedNodeMap nmp = xmlNode.getAttributes(); 
                      for(;g<nmp.getLength();g++) {
                             Node attr = nmp.item(g);
                             ExaltoXmlNode anode = new ExaltoXmlNode(attr);

                             nodeMapTbl.put(anode, currRow + "," + (nlev+g+1));
                     }
                }
                
                /* OV 26/01/07
                if(xmlNode.hasChildNodes()) {
                    NodeList textChilds = xmlNode.getChildNodes();
                    int w=0;
                    for(int u=0;u<textChilds.getLength();u++) {
                        Node tc = textChilds.item(u);
                        if(tc.getNodeType() == 3) {
                            ExaltoXmlNode txtNode = new ExaltoXmlNode(tc);
                            
                            currCol = nlev+w+g+1;
                            if(currCol > maxCol)
                                maxCol = currCol;
                            nodeMapTbl.put(txtNode, currRow + "," + (nlev+w+g+1));
                            w++;
                        }
                    }
                }
                 */
                
                currRow++;
            }
        }
        
        Iterator iter = nodeMapTbl.keySet().iterator();
        int x = 0;
        int y = 0;
        while(iter.hasNext()) {
            StringBuffer sbuf = new StringBuffer();
            ExaltoXmlNode aptr = (ExaltoXmlNode) iter.next();
            
            String rc = (String) nodeMapTbl.get(aptr);
            
            int n = parentList.size();
            parentList.add(aptr);
            
            String [] rowCol = rc.split(",");
            
            int nrow = Integer.parseInt(rowCol[0]);
            
            ArrayList nlist = (ArrayList) rowMapper.get(new Integer(nrow));
            if(nlist != null) {
                sbuf.append((String) nlist.get(0));
                sbuf.append("|");
                sbuf.append(rc);
                sbuf.append(",");
                sbuf.append(y++);
                nlist.set(0, sbuf.toString());
                rowMapper.put(new Integer(nrow), nlist);
            } else {
                nlist = new ArrayList();
                sbuf.append(rc);
                sbuf.append(",");
                sbuf.append(y++);
                nlist.add(sbuf.toString());
                rowMapper.put(new Integer(nrow), nlist);
                StringBuffer strBuf = new StringBuffer();
                strBuf.append(rc);
                strBuf.append(",");
                strBuf.append(x++);
                nodeList.add(strBuf.toString());
            }
            
        }
        
        //        treeTableModel.setMaxCol(maxCol+1);
        treeTableModel.setRowMapper(rowMapper);
        treeTableModel.setParentList(parentList);
        
    }
    
    
    
    public Node getSelectedNode() {
        ExaltoXmlNode treeNode = getSelectedTreeNode();
        if (treeNode == null)
            return null;
        return treeNode.getXmlNode();
    }
    
    public ExaltoXmlNode getSelectedTreeNode() {
        
        TreePath path = atree.getSelectionPath();
        
        /*  System.out.println(" in getSelectedTreeNode path = " + path); */
        
        if (path == null)
            return null;
        
        Object obj = path.getLastPathComponent();
        if (!(obj instanceof ExaltoXmlNode))
            return null;
        
        return (ExaltoXmlNode)obj;
    }
    
    
    public void mousePressed(MouseEvent e) {
        System.out.println(" in mousePressed ");
        
        if(treeExpanded || treeCollapsed) {
            
            int selRow = atree.getRowForLocation(e.getX(), e.getY());
            selectedRow = atree.getRowForLocation(e.getX(), e.getY());
            
            System.out.println(" selRow =  " + selRow);
            
            Point p = e.getPoint();
            
            int sRow = table.rowAtPoint(p);
            int selCol = table.columnAtPoint(p);
            
            System.out.println(" sRow =  " + sRow);
            System.out.println(" selCol =  " + selCol);
            
            TreePath selPath = null;
            
            if(sRow != -1)  {
                
                ExaltoXmlNode xnode = gridHelper.getNodeForRowColumn(sRow, selCol);
                
                selPath = atree.getPathForLocation(e.getX(), e.getY());
                
                senum.setRow(sRow);
                senum.setPath(selPath);
                senum.setExpanded(treeExpanded);
                senum.setCollapsed(treeCollapsed);
                
                updateColumnMapping("");
                
            }
            
            treeExpanded = false;
            treeCollapsed = false;
            
        }
    }
    
    public void mouseClicked(MouseEvent e) {
    }
    
    public void mouseEntered(MouseEvent e) {
    }
    public void mouseExited(MouseEvent e) {
    }
    public void mouseReleased(MouseEvent e) {
    }
    
    
    public void treeStateChanged(MouseEvent e) {
 
        int selRow = atree.getRowForLocation(e.getX(), e.getY());
        selectedRow = atree.getRowForLocation(e.getX(), e.getY());
        
        System.out.println(" selRow =  " + selRow);
        
        Point p = e.getPoint();
        
        int sRow = table.rowAtPoint(p);
        int selCol = table.columnAtPoint(p);
        
        System.out.println(" sRow =  " + sRow);
        System.out.println(" selCol =  " + selCol);
        
        TreePath selPath = null;
        
        if(sRow != -1)  {
            
            ExaltoXmlNode xnode = gridHelper.getNodeForRowColumn(sRow, selCol);
            
            selPath = atree.getPathForLocation(e.getX(), e.getY());
            
            senum.setRow(sRow);
            senum.setPath(selPath);
            senum.setExpanded(treeExpanded);
            senum.setCollapsed(treeCollapsed);
            
            updateColumnMapping("");
            
        }
        
        treeExpanded = false;
        treeCollapsed = false;
        
    }
    
    public void treeStartEdit(int col, EventObject e) {
                 
                ExaltoXmlNode node = (ExaltoXmlNode) atree.getLastSelectedPathComponent();
                
                System.out.println(" in startedit  node = " + node);
                
                if(node == null)
                    return;
                
                atree.startEditingAtPath(new TreePath(node.getPath()));
                System.out.println(" new value " + node);
                
       
    }
 
     
    public void setEditing(boolean editing) {
        isEditing = editing;
    }
    
    public XmlTreeModel getModel() {
        return treeTableModel;
    }
    
}