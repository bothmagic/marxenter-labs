/*
 * Created on Sep 22, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.jdesktop.jdnc.incubator.rbair.masterdetail.kleopatra;

import java.awt.Color;
import java.awt.Font;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.plaf.ColorUIResource;

//import com.jgoodies.plaf.plastic.PlasticLookAndFeel;

/**
 * @author Richard Bair
 */
public class Main {

	public static void main(String[] args) {
		//Places the gui construction code where it belongs --
		//in the EDT. 
		//See http://java.sun.com/docs/books/tutorial/uiswing/misc/threads.html
		//for information. In particular, the halting problem noted near the
		//top of the piece -- I was experiencing this deadlock condition
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			//PlasticLookAndFeel.setMyCurrentTheme(new com.jgoodies.plaf.plastic.theme.ExperienceBlue());
			if (UIManager.getLookAndFeel().getClass().getName().equals("com.sun.java.swing.plaf.windows.WindowsLookAndFeel")) {
				UIManager.put("JTitledPanel.title.foreground", new ColorUIResource(Color.WHITE));
                UIManager.put("JTitledPanel.title.darkBackground", UIManager.getColor("InternalFrame.activeTitleBackground"));
                UIManager.put("JTitledPanel.title.lightBackground", UIManager.getColor("InternalFrame.activeTitleGradient"));
                UIManager.put("JTitledPanel.title.font", UIManager.getFont("Button.font").deriveFont(Font.BOLD));
			}
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					MasterDetailWindow window = new MasterDetailWindow();
					window.setVisible(true);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
