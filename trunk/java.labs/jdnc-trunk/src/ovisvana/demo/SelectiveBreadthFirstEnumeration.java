package demo;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Document;
import org.w3c.dom.DOMException;
/* For creating a TreeModel */
import javax.swing.*;
import javax.swing.tree.*;
import javax.swing.event.*;
import javax.swing.text.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.table.TableCellRenderer;
import com.exalto.ExaltoXmlNode;

public class SelectiveBreadthFirstEnumeration implements Enumeration {
    protected Queue   queue;
    protected Queue   tempQueue;
    public boolean treeAware;
    public boolean dequeued;
    public boolean enqueued;
    JTree tree;
    
    XmlTreeModel treeTableModel;
    TreePath treePath;
    int selRow;
    boolean treeExpanded = false;
    boolean treeCollapsed = false;
    int rowClicked = 0;
    int nodeLevel = 0;
    boolean addToFront = false;
    boolean dequeued2 = false;
    boolean enqueuedTemp = false;
    boolean enqueuedReg = false;
    String nodeFunc;
    
    
    public SelectiveBreadthFirstEnumeration(XmlTreeModel treeTableModel, JTree tree) {
        
        super();
        Vector v = new Vector(1);
        TreeNode rootNode = (TreeNode) treeTableModel.getRoot();
        v.addElement(rootNode);   /* PENDING: don't really need a vector */
        queue = new Queue();
        tempQueue =  new Queue();
        this.treeTableModel = treeTableModel;
        this.tree= tree;
        queue.enqueue(v.elements());
        treeAware = true;
    }
    
    public SelectiveBreadthFirstEnumeration(XmlTreeModel treeTableModel, JTree tree, TreePath path, boolean
            expanded, boolean collapsed, int selRow) {
        super();
        Vector v = new Vector(1);
        TreeNode rootNode = (TreeNode) treeTableModel.getRoot();
        v.addElement(rootNode);   /* PENDING: don't really need a vector */
        queue = new Queue();
        this.treeTableModel = treeTableModel;
        this.tree= tree;
        queue.enqueue(v.elements());
        tempQueue =  new Queue();
        treeAware = true;
        treePath = path;
        treeExpanded = expanded;
        treeCollapsed = collapsed;
        rowClicked = selRow;
    }
    public SelectiveBreadthFirstEnumeration(XmlTreeModel treeTableModel, JTree tree, TreePath path) {
        super();
        Vector v = new Vector(1);
        TreeNode rootNode = (TreeNode) treeTableModel.getRoot();
        v.addElement(rootNode);   /* PENDING: don't really need a vector */
        queue = new Queue();
        this.treeTableModel = treeTableModel;
        this.tree= tree;
        queue.enqueue(v.elements());
        tempQueue =  new Queue();
        treeAware = true;
        treePath = path;
    }
    
    public SelectiveBreadthFirstEnumeration(TreeNode rootNode) {
        super();
        Vector v = new Vector(1);
        v.addElement(rootNode);   /* PENDING: don't really need a vector */
        queue = new Queue();
        tempQueue =  new Queue();
        queue.enqueue(v.elements());
        treeAware = false;
    }
    public boolean hasMoreElements() {
        return (!queue.isEmpty() &&
                ((Enumeration)queue.firstObject()).hasMoreElements());
    }
    
    public Object nextElement() {
        int rowid = 0;
        ExaltoXmlNode qnode = null;
        Enumeration   enumer = (Enumeration)queue.firstObject();
        TreeNode      node = (TreeNode)enumer.nextElement();
        dequeued = false;
        enqueued = false;
        addToFront = false;
        dequeued2 = false;
        enqueuedTemp = false;
        enqueuedReg = false;
        int rx = 0;
        ExaltoXmlNode anode =
                (ExaltoXmlNode) node;
        
        TreePath tpath = null;
        
        boolean match = false;
        if(treeAware) {
            
            XmlTreeModel domTreemodel = treeTableModel;
            
            tpath = new TreePath(domTreemodel.getPathToRoot(anode));
            /*      System.out.println(" processing path = " + tpath);
                  System.out.println(" rowClicked = " + rowClicked);
             */
            
            ArrayList nlist = (ArrayList) domTreemodel.getRowMapper().get(new Integer(rowClicked));
            String rowCol = (String) nlist.get(0);
            String [][] parts = null;
            StringTokenizer stok2 = new StringTokenizer(rowCol, "|");
            int num = stok2.countTokens();
            parts = new String[num][3];
            int ct=0;
            while(stok2.hasMoreTokens()) {
                String rwc = stok2.nextToken();
                StringTokenizer stok3 = new StringTokenizer(rwc, ",");
                parts[ct][0] = stok3.nextToken();
                parts[ct][1] = stok3.nextToken();
                parts[ct++][2] = stok3.nextToken();
            }
            
            ArrayList parentList = domTreemodel.getParentList();
            for(int t=0;t<1;t++) {
                
                Arrays.sort(parts, new ColumnComparator());
                
                                           /*      System.out.println(" parts[t][0] = " + parts[t][0]);
                                           //      System.out.println(" parts[t][1] = " + parts[t][1]);
                                           //      System.out.println(" parts[t][2] = " + parts[t][2]);
                                            */
                
                int prw = Integer.parseInt(parts[t][0]);
                int pcol = Integer.parseInt(parts[t][1]);
                int ppx = Integer.parseInt(parts[t][2]);
                
                qnode = ((ExaltoXmlNode) parentList.get(ppx));
                
            }
            
        }
        
        /*   System.out.println(" qnode is = " + qnode); */
        
        Enumeration children = null;
        children = node.children();
        
        if (!enumer.hasMoreElements()) {
            /*  System.out.println(" in enumer hasmore "); */
            dequeued = true;
            queue.dequeue();
            
            Enumeration em = null;
            if(treeAware) {
                if(!tempQueue.isEmpty() && ((Enumeration)tempQueue.firstObject()).hasMoreElements()) {
                    /*      System.out.println(" dequeueing temp q"); */
                    em = (Enumeration) tempQueue.dequeue();
                    dequeued2 = true;
                    queue.enqueue(em);
                }
            }
            
        }
        
        
        if(treeAware) {
            TreeNode [] tn = anode.getPath();
            TreePath tp = new TreePath(tn);
            int rw = tree.getRowForPath(tp);
            
            TreeNode [] tn1 = qnode.getPath();
            TreePath tp1 = new TreePath(tn1);
            int rw1 = tree.getRowForPath(tp1);
            
    /*  System.out.println(" row from tree " + rw);
      System.out.println(" cond 0 " + (!tree.isCollapsed(rw)));
        System.out.println(" cond 1 " + (tree.isExpanded(rw) && !dequeued));
      System.out.println(" cond 2 " + (tree.isExpanded(rw) && anode == qnode));
      System.out.println(" cond 3 " + (tree.hasBeenExpanded(tp) && !tree.isCollapsed(rw) && rw > rw1));
      System.out.println(" cond 4 " + (tree.hasBeenExpanded(tp) && !tree.isCollapsed(rw1)
              && anode.isNodeDescendant(qnode)));*/
            
            
            if(!tree.isCollapsed(rw)) {
                
                /*             if((tree.isExpanded(rw) && !dequeued)  // used earlier now commented */
                if((tree.isExpanded(rw))
                || (tree.isExpanded(rw) && anode == qnode)
                || (tree.hasBeenExpanded(tp) && !tree.isCollapsed(rw)
                && rw > rw1)
                || (tree.hasBeenExpanded(tp) && !tree.isCollapsed(rw)
                && anode.isNodeDescendant(qnode))) {
                    
                    addToFront = true;
                    
                }
            }
            
            /*	 else  */
            {
                if((nodeFunc != null && nodeFunc.intern() == "NODE_ADD".intern() && anode == qnode) || (nodeFunc != null && nodeFunc.intern() == "NODE_EXPAND".intern())
                || (nodeFunc != null && anode == qnode && nodeFunc.intern() == "NODE_EXPAND_SINGLE".intern())) {
                    addToFront = true;
                }
                
            }
            
        }
        
        /*   System.out.println(" addToFront " + addToFront); */
        
        if (children.hasMoreElements()) {
            /*  System.out.println(" in children hasmore "); */
            if(treeAware && addToFront) {
                /*  System.out.println(" in treeaware node is visible"); */
                queue.insertBefore(children);
            } else {
                if(treeAware && !dequeued) {
                    /*  System.out.println(" adding to tempqueue"); */
                    tempQueue.enqueue(children);
                    enqueuedTemp = true;
                } else {
                    /*    System.out.println(" adding to regular queue"); */
                    tempQueue.enqueue(children);
                    /*   System.out.println(" dequeueing temp q 2"); */
                    Enumeration em = (Enumeration) tempQueue.dequeue();
                    queue.enqueue(em);
                    enqueuedReg = true;
                }
                
                enqueued = true;
                /*   System.out.println(" setting enqueued = true "); */
            }
        }
        return node;
    }
    
    /* A simple queue with a linked list data structure. */
    final class Queue {
        QNode head;   /* null if empty */
        QNode tail;
        final class QNode {
            public Object     object;
            public QNode      next; /* null if end */
            public QNode(Object object, QNode next) {
                this.object = object;
                this.next = next;
            }
        }
        public void enqueue(Object anObject) {
            if (head == null) {
                head = tail = new QNode(anObject, null);
            } else {
                tail.next = new QNode(anObject, null);
                tail = tail.next;
            }
        }
        public void insertBefore(Object anObject) {
            if (head == null) {
                head = tail = new QNode(anObject, null);
            } else {
                QNode newhead = new QNode(anObject, null);
                newhead.next = head;
                head = newhead;
            }
        }
        
        public Object dequeue() {
            if (head == null) {
                throw new NoSuchElementException("No more elements");
            }
            Object retval = head.object;
            QNode oldHead = head;
            head = head.next;
            if (head == null) {
                tail = null;
            } else {
                oldHead.next = null;
            }
            return retval;
        }
        public Object firstObject() {
            if (head == null) {
                throw new NoSuchElementException("No more elements");
            }
            return head.object;
        }
        public boolean isEmpty() {
            return head == null;
        }
    } /* End of class Queue */
    
    public int getIndex(DefaultMutableTreeNode dmnode) {
        if(dmnode.getSiblingCount() > 0) {
        }
        
        return 0;
    }
    public void setRow(int r) {
        rowClicked = r;
    }
    public void setPath(TreePath path) {
        treePath = path;
    }
    public void setExpanded(boolean expanded) {
        treeExpanded = expanded;
    }
    public void setCollapsed(boolean collapsed) {
        treeCollapsed = collapsed;
    }
    public void reset() {
        Vector v = new Vector(1);
        TreeNode rootNode = (TreeNode) treeTableModel.getRoot();
        v.addElement(rootNode);   /* PENDING: don't really need a vector */
        queue = new Queue();
        tempQueue = new Queue();
        queue.enqueue(v.elements());
        treeAware = true;
        
    }
    
    public void reset(String funcOp) {
        Vector v = new Vector(1);
        TreeNode rootNode = (TreeNode) treeTableModel.getRoot();
        v.addElement(rootNode);   /* PENDING: don't really need a vector */
        queue = new Queue();
        tempQueue = new Queue();
        queue.enqueue(v.elements());
        treeAware = true;
        /*          System.out.println(" in senum reset opfunc  = " + funcOp); */
        nodeFunc = funcOp;
        
    }
 
    public class ColumnComparator implements Comparator {
 
	public int compare(Object o1, Object o2) {
	
		String [] str1 = (String []) o1;
		String [] str2 = (String []) o2;

		int result = 0;

	
	   /* Sort on first element of each array (last name) */
	   if ((result = str1[1].compareTo(str2[1])) == 0)
	   {
			return result;
	   }

		return result;
	}

 }
    
}