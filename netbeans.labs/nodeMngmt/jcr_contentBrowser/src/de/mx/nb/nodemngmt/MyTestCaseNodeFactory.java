/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.mx.nb.nodemngmt;

import de.mx.nb.nodemngmt.data.TestCase;
import java.util.List;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Node;

/**
 *
 * @author marxma
 */
class MyTestCaseNodeFactory extends ChildFactory<TestCase> {

    private List<TestCase> list;

    public MyTestCaseNodeFactory(List<TestCase> allTests) {
        this.list = allTests;
    }

    @Override
    protected boolean createKeys(List<TestCase> list) {

        for (TestCase ch : this.list) {
            list.add(ch);
        }
        return true;
    }

    @Override
    protected Node createNodeForKey(TestCase key) {

        TestCaseNode bn = new TestCaseNode(key);

        return bn;

    }
}
