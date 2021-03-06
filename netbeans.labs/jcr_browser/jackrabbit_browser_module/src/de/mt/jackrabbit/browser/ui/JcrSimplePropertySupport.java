/*
 * JcrPropertySupport.java
 *
 * Created on 08.10.2007, 18:32:47
 *
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.mt.jackrabbit.browser.ui;

import java.beans.PropertyChangeSupport;
import java.lang.reflect.InvocationTargetException;
import java.util.Calendar;
import java.util.Date;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.ValueFormatException;
import org.openide.nodes.PropertySupport;
import org.openide.util.Exceptions;

/**
 * PropertySupport for String
 * @author Markus
 */
public class JcrSimplePropertySupport extends PropertySupport.ReadWrite {

  /**
   * store node that contains the property
   */
  private final javax.jcr.Node jcrNode;
  
  private final PropertyChangeSupport propSupport;

  /**
   * Construct a new property editor for the given node.
   *
   * @param aJcrNode node that contains the property.
   * @param aPropertyName name of that property.
   * @param aPropertyType type of that property.
   * @param aDisplayName show property name in editor.
   * @param aDescription show description in editor.
   */
  @SuppressWarnings(value = "unchecked")
  public JcrSimplePropertySupport(javax.jcr.Node aJcrNode, String aPropertyName, Class aPropertyType, String aDisplayName, String aDescription) {
    super(aPropertyName, aPropertyType, aDisplayName, aDescription);
    propSupport = new PropertyChangeSupport(this);
    jcrNode = aJcrNode;
    
  }

  public Object getValue() throws IllegalAccessException, InvocationTargetException {
    Object value = null;
    Class valueType;
    valueType = getValueType();

    try {
      if (valueType.getName().equals(String.class.getName())) {

        value = jcrNode.getProperty(getName()).getString();
      } else if (valueType.getName().equals(Double.class.getName())) {

        value = jcrNode.getProperty(getName()).getDouble();
      } else if (valueType.getName().equals(Long.class.getName())) {

        value = jcrNode.getProperty(getName()).getLong();
      } else if (valueType.getName().equals(Boolean.class.getName())) {

        value = jcrNode.getProperty(getName()).getBoolean();
      } else if (valueType.getName().equals(Date.class.getName())) {

        value = jcrNode.getProperty(getName()).getDate();
      } else {
        value = "unsupported";
      }
    } catch (ValueFormatException ex) {
      Exceptions.printStackTrace(ex);
    } catch (RepositoryException ex) {
      Exceptions.printStackTrace(ex);
    }
    return value;
  }

  public void setValue(Object aValue) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    Class valueType;
    valueType = getValueType();

    try {
      if (valueType.getName().equals(String.class.getName())) {

        jcrNode.setProperty(getName(), (String)aValue);
      } else if (valueType.getName().equals(Double.class.getName())) {

        jcrNode.setProperty(getName(), (Double)aValue);
      } else if (valueType.getName().equals(Long.class.getName())) {

        jcrNode.setProperty(getName(), (Long)aValue);
      } else if (valueType.getName().equals(Boolean.class.getName())) {

        jcrNode.setProperty(getName(), (Boolean)aValue);
      } else if (valueType.getName().equals(Date.class.getName())) {
        Calendar cal = Calendar.getInstance();
        cal.setTime((Date)aValue);
        jcrNode.setProperty(getName(), cal);
      } else {
        throw new UnsupportedOperationException();
      }
    } catch (ValueFormatException ex) {
      Exceptions.printStackTrace(ex);
    } catch (RepositoryException ex) {
      Exceptions.printStackTrace(ex);
    }
  }
}