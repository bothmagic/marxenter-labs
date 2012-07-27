/*
 * JcrChildrenNodes.java
 * 
 * Created on 06.10.2007, 19:35:09
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.mt.jackrabbit.browser.ui;

import java.util.ArrayList;
import java.util.List;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import org.apache.jackrabbit.core.NodeIdIterator;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.util.Exceptions;
import org.openide.util.lookup.Lookups;

/**
 *
 * @author markus
 */
public class JcrChildrenNodes extends Children.Keys {

  private final Node node;
  
  public JcrChildrenNodes(Node aNode) {
    node = aNode;
  }

  @Override
  protected void addNotify() {
    super.addNotify();
    setKeys(new Node[]{node});
  }
  
  
  
  protected org.openide.nodes.Node[] createNodes(Object arg0) {
    try {
      Node currentNode = (Node) arg0;
      AbstractNode node = null;
      List<org.openide.nodes.Node> nodeList = new ArrayList<org.openide.nodes.Node>();
      
      for (NodeIterator nit = currentNode.getNodes(); nit.hasNext();) {
        Node nn = nit.nextNode();
        node = new NbJcrNode(nn);
        node.setDisplayName(nn.getName());
        nodeList.add(node);
      }
      org.openide.nodes.Node[] nodeArr = new org.openide.nodes.Node[nodeList.size()];
      
      return nodeList.toArray(nodeArr);
    } catch (RepositoryException ex) {
      Exceptions.printStackTrace(ex);
    }
    return new org.openide.nodes.Node[]{};
  }

}
