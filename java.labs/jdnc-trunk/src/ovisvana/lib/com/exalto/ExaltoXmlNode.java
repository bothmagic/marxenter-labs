package com.exalto;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;
import org.w3c.dom.*;
import java.util.*;

public class ExaltoXmlNode extends DefaultMutableTreeNode {
 
 protected ImageIcon imgIcon = null;
    protected String tipText;
	Node text;
    protected boolean isExpanded;
    protected String sortStatus;
 
 public ExaltoXmlNode(Node node) {
  super(node);
//  imgIcon = new ImageIcon(getClass().getResource("images/gridElement.gif"));
  tipText = node.toString();
 }

  public ExaltoXmlNode(Text node) {
	this.text = node;
  }


 public Node getXmlNode() {
	
	if(text != null)
		return (Node) text;
	
  Object obj = getUserObject();
  if (obj instanceof Node)
   return (Node)obj;
  return null;
 }
 
 public Icon getIcon() {
  return imgIcon; 
 }
 
 public String getToolTipText() {
  return tipText; 
 }
 
 public int getNodeType() {
  Object obj = getUserObject();
  if (obj instanceof Node) {
   Node nodeObj = (Node) obj;
   return nodeObj.getNodeType();
  }
 
  return -1;
 }

 public void addXmlNode(ExaltoXmlNode child)
  throws Exception {
  Node node = getXmlNode();
  if (node == null)
   throw new Exception(
    "Corrupted XML node");
 
  node.appendChild(child.getXmlNode());
  add(child);
 }

 public void addXmlNode(ExaltoXmlNode child, boolean addToTree)
  throws Exception {

  Node node = getXmlNode();
  if (node == null)
   throw new Exception(
    "Corrupted XML node");
 
  node.appendChild(child.getXmlNode());
  if(addToTree)
  	  add(child);
 }

 public void addAttrNode(ExaltoXmlNode child, boolean addToTree)
  throws Exception {

  Element node = (Element) getXmlNode();
  if (node == null)
   throw new Exception(
    "Corrupted XML node");

	  node.setAttribute(child.getXmlNode().getNodeName(), ((Attr)child.getXmlNode()).getValue());

  if(addToTree)
  	  add(child);
 }


 public void insertXmlNode(ExaltoXmlNode child, Node refChild, int index)
  throws Exception {

  Node node = getXmlNode();
  if (node == null)
   throw new Exception(
    "Corrupted XML node");
 
 
   NodeList nl = node.getChildNodes();
 
 /*
	 if(null == refChild) 
      System.out.println(" refChild is null "); 
     else
      System.out.println(" refChild is not null hash = " + refChild.hashCode()); 
  
   
   for(int h=0;h<nl.getLength();h++) {
      System.out.println(" item[h] hashcode = " + nl.item(h).hashCode()); 
      
     if(nl.item(h) == refChild) 
      System.out.println(" match found for ref at " + h); 
	}

	*/
 
   node.insertBefore(child.getXmlNode(), refChild);
   insert(child, index);
 }

 public void remove() throws Exception {
  Node node = getXmlNode();
  if (node == null)
   throw new Exception(
    "Corrupted XML node");
  Node parent = node.getParentNode();
  if (parent == null)
   throw new Exception(
    "Cannot remove root node");
  TreeNode treeParent = getParent();
  if (!(treeParent instanceof DefaultMutableTreeNode))
   throw new Exception(
    "Cannot remove tree node");
  parent.removeChild(node);
  ((DefaultMutableTreeNode)treeParent).remove(this);
 }

public void remove(boolean remFromTree) throws Exception {
  Node node = getXmlNode();
  if (node == null)
   throw new Exception(
    "Corrupted XML node");
  Node parent = node.getParentNode();
  if (parent == null)
   throw new Exception(
    "Cannot remove root node");

  TreeNode treeParent = getParent();
  parent.removeChild(node);

  if(remFromTree)
	  ((DefaultMutableTreeNode)treeParent).remove(this);
 
 }

 public void removeAll() throws Exception {

/*
  Node node = getXmlNode();
  if (node == null)
   throw new Exception(
    "Corrupted XML node");

	NodeList nl = node.getChildNodes();
	for(int e=0;e<nl.getLength();e++) {
		Node c = nl.item(e);
		node.removeChild(c);
	}
*/

	Enumeration en = children();
	while(en.hasMoreElements()) {
		ExaltoXmlNode tn = (ExaltoXmlNode) en.nextElement();
		tn.remove();
	}
 

 }


 public String toString () {
  Node node = getXmlNode();
  if (node == null)
   return getUserObject().toString();
  StringBuffer sb = new StringBuffer();
  switch (node.getNodeType()) {
  case Node.ELEMENT_NODE:
  /* sb.append('<'); */
   sb.append(node.getNodeName());
  /* sb.append('>'); */
   break;
  case Node.TEXT_NODE:
   sb.append(node.getNodeValue());
   break;
  case Node.DOCUMENT_NODE:
   sb.append("doc");
   break;
  }
  return sb.toString();
 }

public boolean hasElementChildren() {

	Node node = getXmlNode();
	NodeList nl = node.getChildNodes();
	for(int r=0;r<nl.getLength();r++) {
		Node t = nl.item(r);
		if(t.getNodeType() == 1)
			return true;
	}
 
   return false;	
}

/* 
 public Enumeration children() {
  
  Object obj = getUserObject();
  
        System.out.println(" obj type = " + obj.getClass().getName());
     
 // if(obj instanceof org.w3c.dom.Document) 
 // {
   org.w3c.dom.Node docNode = (org.w3c.dom.Node) obj;
            Vector v = new Vector();
            NodeList nl = docNode.getChildNodes();
            for(int k=0;k<nl.getLength();k++) {
                Node cnode = nl.item(k);
                XmlViewerNode anode = new XmlViewerNode(cnode);
                v.add(anode);
            }
            System.out.println(" v size = " + v.size());
            System.out.println(" vector v = " + v);
            return v.elements();
 // } 
 // else {
 //  Enumeration en = super.children();     
 //   System.out.println("in else xmlview ");
 //  for (;en.hasMoreElements();) {
   //       System.out.println("in else xmlview " + en.nextElement());
  //    }
     
     //     return en;
        
     }


	public boolean equals(Object obj) {

		if(obj.hashCode() == hashCode())
			return true;
		else
			return false;
	
	}
*/

	public int getElemChildrenCount()  {
		int incr = 0;
		Node d = getXmlNode();
		NodeList nl = d.getChildNodes();
	    for(int k=0;k<nl.getLength();k++) {
            Node cnode = nl.item(k);
            if(cnode.getNodeType() == 1)
            	incr++;
	    }
    
	    return incr;
	}


	public void setExpanded(boolean expanded) {
		this.isExpanded = expanded;
	}

	public boolean getExpanded() {
		return isExpanded;
	}

	public String getSortStatus() {
		return sortStatus;
	}

	public void setSortStatus(String sortStatus) {
		this.sortStatus = sortStatus;
	}

 }