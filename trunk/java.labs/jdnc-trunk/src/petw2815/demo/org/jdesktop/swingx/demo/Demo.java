package org.jdesktop.swingx.demo;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import com.jgoodies.looks.plastic.Plastic3DLookAndFeel;
import com.jgoodies.looks.plastic.PlasticXPLookAndFeel;
import com.jgoodies.looks.windows.WindowsLookAndFeel;

public class Demo {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.getProperties().setProperty("swing.auxiliarylaf", "org.jdesktop.swingx.demo.AuxLnF");
		
		Plastic3DLookAndFeel.setHighContrastFocusColorsEnabled(true);
		UIManager.installLookAndFeel("JGoodies Windows", WindowsLookAndFeel.class.getName());
		UIManager.installLookAndFeel("JGoodies Plastic", Plastic3DLookAndFeel.class.getName());
		UIManager.installLookAndFeel("JGoodies Plastic XP", PlasticXPLookAndFeel.class.getName());
		
		JFrame f = new JFrame("xswingx Demo");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JMenuBar mb = new JMenuBar();
		mb.add(Demo.createLookAndFeelMenu(f));
		f.setJMenuBar(mb);
		
		JTabbedPane tb = new JTabbedPane();
		tb.add("Text Field", new TextFieldCustomizer());
		tb.add("Text Area", new TextAreaCustomizer());
		tb.add("Search Field", new SearchFieldCustomizer());
		tb.add("Contact Demo", new ContactPanel());
		f.add(tb);
		f.pack();
		f.setVisible(true);
	}
	
	public static JMenu createLookAndFeelMenu(final Component toUpdate) {
		final JMenu lnf = new JMenu("Look and Feel");
		for (final LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
			final JMenuItem mi = new JMenuItem(info.getName());
			lnf.add(mi);

			mi.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						UIManager.setLookAndFeel(info.getClassName());
						SwingUtilities.updateComponentTreeUI(toUpdate);
					} catch (Exception ex) {
						mi.setEnabled(false);
						ex.printStackTrace();
					}
				}
			});
		}
		return lnf;
	}

}
