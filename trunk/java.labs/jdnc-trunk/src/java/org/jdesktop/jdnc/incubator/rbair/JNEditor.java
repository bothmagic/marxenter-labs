/*
 * $Id: JNEditor.java 46 2004-09-08 17:33:01Z rbair $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */

package org.jdesktop.jdnc.incubator.rbair;

import java.awt.Dimension;
import java.net.URL;

import javax.swing.JScrollPane;

import org.jdesktop.jdnc.incubator.rbair.swing.Application;
import org.jdesktop.jdnc.incubator.rbair.swing.JXEditorPane;

/**
 * Implementation of a text editor
 *
 * @author Mark Davidson
 *
 * @javabean.class
 *    displayName="Editor Component"
 *    name="JNEditor"
 *    shortDesctiption="A simple html editor component"
 *
 * @javabean.icons
 *    color16="/javax/swing/beaninfo/images/JEditorPaneColor16.gif"  
 *    color32="/javax/swing/beaninfo/images/JEditorPaneColor32.gif"  
 */
public class JNEditor extends JNComponent {

    private JXEditorPane editor;
    private String inputURL;
    private boolean readonly;
    private static final String DEFAULT_TYPE = "text/plain";

    private String type = DEFAULT_TYPE;

    public JNEditor() {
	editor = new JXEditorPane("text/html", "");
	//	setDocumentType("text/html");
	setComponent(editor);
	add(new JScrollPane(editor));
    }

    public JXEditorPane getEditor() {
	return editor;
    }

    public void setReadOnly(boolean ro) {
	this.readonly = ro;
	editor.setEditable(!readonly);
    }

    public boolean isReadOnly() {
	return readonly;
    }

    /**
     * @javabean.property bound="true" shortDescription="Sets the input url"
     */
    public void setInputURL(String inputURL) {
 	if (editor != null && inputURL != null) {
	    URL url = Application.getURL(inputURL, this);
	    if (url != null) {
		setInputURL(url);
	    }
	}
    }

    public void setInputURL(URL url) {
	if (editor != null && url != null) {
	    this.inputURL = url.toString();
	    try {
		editor.setPage(url);
	    } catch (Exception ex) {
		System.out.println("Error Setting page url: " + url);
		ex.printStackTrace();
	    }
	}
    }


    public String getInputURL() {
	return inputURL;
    }

    // XXX may be irrelevent.
    public void setDocumentType(String type) {
	if (!"text/html".equals(type)) {
	    type = DEFAULT_TYPE;
	}
	if (type == null || "".equals(type)) {
	    type = DEFAULT_TYPE;
	}
	this.type = type;

	editor.setContentType(type);
    }

    public String getDocumentType() {
	return type;
    }

    // XXX - The demo with bike.html seems to grow without bounds. Fix the maximim size
    // of the component.
    private static final Dimension MAX_SIZE = new Dimension(400, 300);

    public Dimension getPreferredSize() {
	return MAX_SIZE;
    }
}
