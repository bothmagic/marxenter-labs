/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.mx.nb.nodemngmt;

import java.beans.IntrospectionException;
import java.util.ArrayList;
import java.util.List;
import org.openide.nodes.BeanNode;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;

/**
 *
 * @author marxma
 */
class MyChildFactory extends ChildFactory<MyChildNode> {

    private List<MyChildNode> mylist;
    
    public MyChildFactory(List<MyChildNode> list) {
        mylist = list;
    }

    @Override
    protected boolean createKeys(List<MyChildNode> list) {
        for (MyChildNode ch : mylist) {
            list.add(ch);
        }
        return true;
    }

    @Override
    protected Node createNodeForKey(MyChildNode key) {
        try {
            BeanNode bn = new MyChildBeanNode(key);
            
            return bn;
        } catch (IntrospectionException ex) {
            Exceptions.printStackTrace(ex);
            return null;
        }
        
    }
    
    
    
}
