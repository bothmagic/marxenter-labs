/*
 * $Id: JXScrollMapDemo.java 3296 2010-08-03 17:52:57Z kschaefe $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */

package org.jdesktop.swingx;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * Demo class for {@code JXScrollMap}.
 * 
 * @author Karl Schaefer
 * @author weebib
 */
public class JXScrollMapDemo extends JFrame {
	@Override
    protected void frameInit() {
	    super.frameInit();
	    
	    setTitle("JXScrollPane Demo");
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    
        JLabel imageComponent = new JLabel(new ImageIcon(JXScrollMapDemo.class.getResource(
                "stonehenge-wallpaper-1.jpg")));

        JScrollPane scrollPane = new JScrollPane(imageComponent);
        JXScrollMap scrollMap = new JXScrollMap(scrollPane.getViewport());
        scrollMap.setSynchronizedScrolling(true);
        scrollPane.setCorner(ScrollPaneConstants.LOWER_TRAILING_CORNER, scrollMap);
        add(scrollPane, BorderLayout.CENTER);

        JLabel helpLabel = new JLabel("<html>Press the little button at the scrollPane's bottom right corner.  " +
                                      "Keep it pressed while moving the selection rectangle and then release " +
                                      "it to have the scrollPane scroll accordingly.</html>");
        helpLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        add(helpLabel, BorderLayout.SOUTH);

        pack();
        
        //ensure the window naturely scrolls
        Dimension pref = getSize();
        Dimension min = getMinimumSize();
        pref.width = Math.max((int)(pref.width * 0.5), min.width);
        pref.height = Math.max((int)(pref.height * 0.5), min.height);
        setSize(pref);
	}
	
	public static void main(String[] args) throws Exception {
	    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	    
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
			    new JXScrollMapDemo().setVisible(true);
			}
		});
	}
}