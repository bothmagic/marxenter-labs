package org.jdesktop.swingx.jxdropbutton;// org.jdesktop.swingx.jxdropbutton.DropButtonDemo1.java
// User: Sylvan Haas IV
// Date: Dec 4, 2006 3:19:58 PM

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.AbstractAction;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import org.jdesktop.swingx.JXDropButton;
import org.jdesktop.swingx.plaf.LookAndFeelAddons;

/**
 * [Enter Description Here]
 *
 * @version 1
 */
public class DropButtonDemo1
{
	public static void main(String[] args)
	{
		final JFrame frame = new JFrame("DropButton Demo1");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(400, 160);

		final JComboBox cmbo = new JComboBox(new String[] { "Metal", "System", "SwingX System", "Office2003", "OfficeXP"} );
		cmbo.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				String name = null;
				try
				{
					switch(cmbo.getSelectedIndex())
					{
						case 0: name = "javax.swing.plaf.metal.MetalLookAndFeel"; break;
						case 1: name = UIManager.getSystemLookAndFeelClassName(); break;
						case 2:
							name = LookAndFeelAddons.getSystemAddonClassName();
							LookAndFeelAddons.setAddon(name);
							break;
						case 3: name = "org.fife.plaf.Office2003.Office2003LookAndFeel"; break;
						case 4: name = "org.fife.plaf.OfficeXP.OfficeXPLookAndFeel"; break;
					}
					if(name == null) return;
					UIManager.setLookAndFeel(name);
					SwingUtilities.updateComponentTreeUI(frame);
				}
				catch(Exception ex) { JOptionPane.showMessageDialog(frame, "Cannot find the LNF (" + name + ")"); }
			}
		});

		JXDropButton drop1 = new JXDropButton("enabled w/ popup");
		drop1.setPopupMenu(createPopup());

		JXDropButton drop2 = new JXDropButton("disabled w/ popup");
		drop2.setEnabled(false);
		drop2.setPopupMenu(createPopup());

		JXDropButton drop3 = new JXDropButton("enabled w/o popup");

		JXDropButton drop4 = new JXDropButton(new AbstractAction("action w/ popup") { public void actionPerformed(ActionEvent e) { JOptionPane.showMessageDialog(frame, "Clicked!"); } });
		drop4.setPopupMenu(createPopup());

		frame.setLayout(new GridLayout(5, 1, 2, 2));
		frame.add(cmbo);
		frame.add(drop1);
		frame.add(drop2);
		frame.add(drop3);
		frame.add(drop4);

		frame.setVisible(true);
	}

	private static JPopupMenu createPopup()
	{
		JPopupMenu menu = new JPopupMenu();
		menu.add("One");
		menu.add("Two");
		return menu;
	}
}
