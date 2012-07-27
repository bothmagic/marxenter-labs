/*
 * RepositoryNode.java
 * 
 * Created on 21.10.2007, 15:57:52
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.mt.jackrabbit.browser.ui;

import javax.jcr.Repository;
import javax.jcr.Session;
import org.openide.nodes.AbstractNode;

/**
 * nbm node wrapper for jcr repository.
 * @author markus
 */
public class RepositoryNode {
  
  /**
   */
  private Session session;
  
  private Repository repository;

  /**
   * @param session
   * @param repository
   */
  public RepositoryNode(Session session, Repository repository) {
    
    this.session = session;
    this.repository = repository;
  }

  public Repository getRepository() {
    return repository;
  }

  public Session getSession() {
    return session;
  }
  
}
