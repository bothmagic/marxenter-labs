/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.mx.nb.nodemngmt;

import java.awt.event.ActionEvent;
import java.beans.IntrospectionException;
import javax.swing.AbstractAction;
import javax.swing.Action;
import org.openide.nodes.BeanNode;
import org.openide.nodes.Children;
import org.openide.util.Lookup;

/**
 *
 * @author marxma
 */
public class MyChildBeanNode extends BeanNode<MyChildNode> {

    public MyChildBeanNode(MyChildNode bean, Children children, Lookup lkp) throws IntrospectionException {
        super(bean, children, lkp);
    }

    public MyChildBeanNode(MyChildNode bean, Children children) throws IntrospectionException {
        super(bean, children);
    }

    public MyChildBeanNode(MyChildNode bean) throws IntrospectionException {
        super(bean);
    }

    @Override
    public Action getPreferredAction() {
        
        return new OpenChildNode(this);
    }
    
    
    
}
