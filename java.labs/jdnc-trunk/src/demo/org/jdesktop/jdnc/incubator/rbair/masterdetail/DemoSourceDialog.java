/*
 * $Id: DemoSourceDialog.java 109 2004-10-07 20:11:40Z rbair $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.jdnc.incubator.rbair.masterdetail;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;

import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import org.jdesktop.jdnc.incubator.rbair.util.ResourceBundle;
import org.jdesktop.jdnc.incubator.rbair.util.WindowUtils;

/**
 * @author Richard Bair
 */
public class DemoSourceDialog extends JDialog {
	public DemoSourceDialog() {
		initGui();
	}
	
	private void initGui() {
		JTabbedPane tabs = new JTabbedPane();
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(tabs, BorderLayout.CENTER);
		
		JPanel srcPanel = new JPanel(new BorderLayout());
		JEditorPane ep = new JEditorPane("text/rtf", getSourceFile("DemoPanel.rtf"));
		ep.setEditable(false);
		srcPanel.add(new JScrollPane(ep), BorderLayout.CENTER);
		tabs.add(srcPanel, "DemoPanel.java");

		srcPanel = new JPanel(new BorderLayout());
		ep = new JEditorPane("text/rtf", getSourceFile("JavaBeanPanel.rtf"));
		ep.setEditable(false);
		srcPanel.add(new JScrollPane(ep), BorderLayout.CENTER);
		tabs.add(srcPanel, "JavaBeanPanel.java");

		srcPanel = new JPanel(new BorderLayout());
		ep = new JEditorPane("text/rtf", getSourceFile("RowSetPanel.rtf"));
		ep.setEditable(false);
		srcPanel.add(new JScrollPane(ep), BorderLayout.CENTER);
		tabs.add(srcPanel, "RowSetPanel.java");

		srcPanel = new JPanel(new BorderLayout());
		ep = new JEditorPane("text/rtf", getSourceFile("MixedPanel.rtf"));
		ep.setEditable(false);
		srcPanel.add(new JScrollPane(ep), BorderLayout.CENTER);
		tabs.add(srcPanel, "MixedPanel.java");
		
		Dimension dim = new Dimension(800, 450);
		//is this a 1.5ism?
//		setPreferredSize(dim);
		setSize(dim);
		setLocation(WindowUtils.getPointForCentering(this));
	}
	
	private String getSourceFile(String fileName) {
		try {
			InputStream is = ResourceBundle.getInputStream("org/jdesktop/jdnc/incubator/rbair/resources/" + fileName);
			StringBuffer buffer = new StringBuffer();
			BufferedReader reader = new BufferedReader(new InputStreamReader(is));
			String line = null;
			while ((line = reader.readLine()) != null) {
				buffer.append(line);
			}
			return buffer.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return "<html>Failed to get " + fileName + " from the jar. Sorry :(</html>";
		}
	}
}
