/*
 * $Id: PDFTestFrame.java 331 2005-02-01 19:05:26Z rbair $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.jdnc.incubator.rbair.swing.pdf.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import org.jdesktop.swing.util.WindowUtils;

/**
 * 
 * @author Richard Bair
 */
public class PDFTestFrame extends JFrame {
	public PDFTestFrame() {
		initGui();
	}
	
	private void initGui() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("PDF Test Viewer");

        //use a border layout for the content pane
        JPanel contentPane = new JPanel(new BorderLayout());
        setContentPane(contentPane);

        contentPane.add(new PDFPreview(), BorderLayout.CENTER);
        
        setSize(new Dimension(800, 600));
        setLocation(WindowUtils.getPointForCentering(this));
	}

	public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                new PDFTestFrame().setVisible(true);
            }
        });
	}
}
