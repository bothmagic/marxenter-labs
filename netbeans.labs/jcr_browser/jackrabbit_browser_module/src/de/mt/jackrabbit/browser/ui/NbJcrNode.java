/*
 * JcrNode.java
 * 
 * Created on 08.10.2007, 07:12:15
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.mt.jackrabbit.browser.ui;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.Date;
import javax.jcr.AccessDeniedException;
import javax.jcr.InvalidItemStateException;
import javax.jcr.ItemExistsException;
import javax.jcr.Node;
import javax.jcr.PropertyIterator;
import javax.jcr.ReferentialIntegrityException;
import javax.jcr.RepositoryException;
import javax.jcr.lock.LockException;
import javax.jcr.nodetype.ConstraintViolationException;
import javax.jcr.nodetype.NoSuchNodeTypeException;
import javax.jcr.version.VersionException;
import org.openide.cookies.SaveCookie;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Node.Property;
import org.openide.nodes.Sheet;
import org.openide.util.Exceptions;
import org.openide.util.lookup.InstanceContent;

/**
 *
 * @author Markus
 */
public class NbJcrNode extends AbstractNode implements PropertyChangeListener {
  
  private javax.jcr.Node jcrNode;
  private final InstanceContent content;
  
  public NbJcrNode(javax.jcr.Node aJcrNode) {
      
    this(aJcrNode, new InstanceContent());
  }
  
  private NbJcrNode(javax.jcr.Node aJcrNode, InstanceContent aContent) {
    super(new JcrChildrenNodes(aJcrNode));
    
    jcrNode = aJcrNode;
    content = new InstanceContent();
    content.add(aJcrNode);
    
  }
  
  
  protected Sheet createSheet() {
    
    Sheet sheet = Sheet.createDefault();
    Sheet.Set set = sheet.createPropertiesSet();
    //Node node = getLookup().lookup(Node.class);
    Node node = jcrNode;
    Property prop = null;
    javax.jcr.Property jcrProp;
    
    try {
      
      for (PropertyIterator pit = node.getProperties(); pit.hasNext();) {
        jcrProp = pit.nextProperty();
        
        if (!jcrProp.getDefinition().isProtected()) {
          if ((prop = createReadWriteProperty(node, jcrProp)) == null)
            continue;
          
        } else {
          if ((prop = createReadOnlyProperty(node, jcrProp)) == null)
            continue;
        }
        
        set.put(prop);
        
      }
  
    } catch (RepositoryException ex) {
      Exceptions.printStackTrace(ex);
    }
    
    sheet.put(set);
    return sheet;
  }

  public void propertyChange(PropertyChangeEvent evt) {
    
    getCookieSet().add(new SaveCookie() {

      public void save() throws IOException {
        try {
          
          jcrNode.save();
          getCookieSet().remove(this);
          
        } catch (AccessDeniedException ex) {
          Exceptions.printStackTrace(ex);
        } catch (ItemExistsException ex) {
          Exceptions.printStackTrace(ex);
        } catch (ConstraintViolationException ex) {
          Exceptions.printStackTrace(ex);
        } catch (InvalidItemStateException ex) {
          Exceptions.printStackTrace(ex);
        } catch (ReferentialIntegrityException ex) {
          Exceptions.printStackTrace(ex);
        } catch (VersionException ex) {
          Exceptions.printStackTrace(ex);
        } catch (LockException ex) {
          Exceptions.printStackTrace(ex);
        } catch (NoSuchNodeTypeException ex) {
          Exceptions.printStackTrace(ex);
        } catch (RepositoryException ex) {
          Exceptions.printStackTrace(ex);
        }
      }
    });
  }

  private Property createReadWriteProperty(Node node, javax.jcr.Property jcrProp) throws RepositoryException {
    JcrSimplePropertyReadWriteSupport prop = null;
    if (jcrProp.getDefinition().isMultiple())
      return null;
    switch (jcrProp.getType()) {
      case javax.jcr.PropertyType.STRING:
        prop = new JcrSimplePropertyReadWriteSupport(node, jcrProp.getName(), String.class, jcrProp.getName(), jcrProp.getName());
        break;
      case javax.jcr.PropertyType.BOOLEAN:
        prop = new JcrSimplePropertyReadWriteSupport(node, jcrProp.getName(), Boolean.class, jcrProp.getName(), jcrProp.getName());
        break;
      case javax.jcr.PropertyType.DATE:
        prop = new JcrSimplePropertyReadWriteSupport(node, jcrProp.getName(), Date.class, jcrProp.getName(), jcrProp.getName());
        break;
      case javax.jcr.PropertyType.DOUBLE:
        prop = new JcrSimplePropertyReadWriteSupport(node, jcrProp.getName(), Double.class, jcrProp.getName(), jcrProp.getName());
        break;
      case javax.jcr.PropertyType.LONG:
        prop = new JcrSimplePropertyReadWriteSupport(node, jcrProp.getName(), Long.class, jcrProp.getName(), jcrProp.getName());
        break;
      default:
        prop = null;
    }
    
    if (prop != null) {
      prop.addPropertyChangeListener(this);
    }
    
    return prop;
  }
  
  private Property createReadOnlyProperty(Node node, javax.jcr.Property jcrProp) throws RepositoryException {
    Property prop = null;
    if (jcrProp.getDefinition().isMultiple())
      return null;
    switch (jcrProp.getType()) {
      case javax.jcr.PropertyType.STRING:
        prop = new JcrSimplePropertyReadOnlySupport(node, jcrProp.getName(), String.class, jcrProp.getName(), jcrProp.getName());
        break;
      case javax.jcr.PropertyType.BOOLEAN:
        prop = new JcrSimplePropertyReadOnlySupport(node, jcrProp.getName(), Boolean.class, jcrProp.getName(), jcrProp.getName());
        break;
      case javax.jcr.PropertyType.DATE:
        prop = new JcrSimplePropertyReadOnlySupport(node, jcrProp.getName(), Date.class, jcrProp.getName(), jcrProp.getName());
        break;
      case javax.jcr.PropertyType.DOUBLE:
        prop = new JcrSimplePropertyReadOnlySupport(node, jcrProp.getName(), Double.class, jcrProp.getName(), jcrProp.getName());
        break;
      case javax.jcr.PropertyType.LONG:
        prop = new JcrSimplePropertyReadOnlySupport(node, jcrProp.getName(), Long.class, jcrProp.getName(), jcrProp.getName());
        break;
      default:
        prop = null;
    }
    return prop;
  }
    
}
