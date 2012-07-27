package org.jdesktop.jdnc.incubator.davidson1.actions;

import java.net.URL;

import java.util.Set;
import java.util.Iterator;

import junit.framework.TestCase;

import org.w3c.dom.Document;

import org.jdesktop.swing.Application;

import org.jdesktop.swing.actions.ActionFactory;
import org.jdesktop.swing.actions.ActionManager;

public class XMLActionFactoryTest extends TestCase {

    protected void setUp() {
    }

    protected void tearDown() {
    }

    public void testLoadActions() {
        URL url = XMLParserTest.getResource("resources/test2.xml");

        XMLActionFactory factory = XMLActionFactory.getInstance();
        factory.loadActions(url);

        ActionManager manager = Application.getInstance().getActionManager();

        Set set = manager.getActionIDs();
        assertNotNull(set);

        // Only 12 actions should be loaded.
        assertEquals(12, set.size());

        Iterator iter = set.iterator();
        while (iter.hasNext()) {
            assertNotNull(manager.getAction(iter.next()));
        }
    }

}
