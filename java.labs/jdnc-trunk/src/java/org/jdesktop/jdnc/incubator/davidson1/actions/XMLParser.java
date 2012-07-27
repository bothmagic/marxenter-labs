/*
 * $Id: XMLParser.java 147 2004-10-29 17:01:32Z davidson1 $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.jdnc.incubator.davidson1.actions;

import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Generic XML parser which constructs a DOM from an XML document.
 *
 * @author Mark Davidson
 */
public class XMLParser {

    private static XMLParser INSTANCE;

    private static DocumentBuilderFactory docfactory;
    private static DocumentBuilder docbuilder;

    private XMLParser() {
    }

    public static XMLParser getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new XMLParser();
        }
        return INSTANCE;
    }

    /**
     * Takes a url to an xml document and returns a parsed DOM
     * Document.
     */
    public Document getDocument(URL url) {
        if (docfactory == null) {
            docfactory = DocumentBuilderFactory.newInstance();
        }

        try {
            if (docbuilder == null) {
                docbuilder = docfactory.newDocumentBuilder();
            }
            return docbuilder.parse(url.openStream());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

}

