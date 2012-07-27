package org.jdesktop.jdnc.incubator.davidson1.actions;

import java.net.URL;

import junit.framework.TestCase;

import org.w3c.dom.Document;

public class XMLParserTest extends TestCase {

    protected void setUp() {
    }

    protected void tearDown() {
    }

    /**
     * Shared method.
     */
    public static URL getResource(String resource) {
        URL url = XMLParserTest.class.getResource(resource);
        assertNotNull(url);
        return url;
    }

    public void testGetDocument() {
        XMLParser parser = XMLParser.getInstance();

        assertNotNull(parser);

        assertNotNull(parser.getDocument(getResource("resources/test1.xml")));
        assertNotNull(parser.getDocument(getResource("resources/editorDemo.jdnc")));
    }
}
