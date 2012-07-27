/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.mx.nb.nodemngmt;

import de.mx.nb.nodemngmt.data.TestCase;
import javax.swing.Action;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.util.Lookup;

/**
 *
 * @author marxma
 */
public class TestCaseNode extends AbstractNode {
    
    private int id;
    
    public TestCaseNode(Lookup lookup, TestCase testCase) {
        super(Children.LEAF, lookup);
        init(testCase);
    }
    public TestCaseNode(TestCase testCase) {
        super(Children.LEAF);
        setName(testCase.getName());
        init(testCase);
    }
    
    private void init(TestCase testCase) {
        id = testCase.getId();
    }
    
    @Override
    public Action getPreferredAction() {
        
        return new OpenChildNode(this);
    }
    
}
