/*
 * $Id: DropButtonDemo.java 1769 2007-09-25 11:12:50Z osbald $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */

package org.jdesktop.swingx.jxdropbutton;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import org.jdesktop.swingx.JXDropButton;
import org.jdesktop.swingx.VerticalLayout;

/**
 * Main demo for JXDropButton
 *
 * @author Sylvan Haas IV (syhaas [at] gmail.com)
 * @version 1
 */
public class DropButtonDemo extends JFrame
{
	private JLabel lblStatus;

	public DropButtonDemo()
	{
		super("DropButton Demo");
		setSize(800, 600);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		init();

		setVisible(true);
	}

	private JXDropButton createDropButton(String text, String iconName, JPopupMenu menu)
	{
		JXDropButton btn = new JXDropButton(text, menu);
		if(iconName != null) btn.setIcon(getIcon(iconName));
		return btn;
	}

	private JPopupMenu createNewPopup()
	{
		JPopupMenu menu = new JPopupMenu();
		menu.add("File");
		menu.add("Project");
		menu.add("User");
		return menu;
	}

	private JPopupMenu createPopup()
	{
		JPopupMenu menu = new JPopupMenu();
		menu.add("1");
		menu.add("2");
		menu.add("3");
		return menu;
	}

	private JPopupMenu createWindowsPopup()
	{
		JPopupMenu menu = new JPopupMenu();
		menu.add("Close");
		menu.add("Close All");
		menu.addSeparator();
		menu.add("Cascade");
		menu.add("Tile");
		return menu;
	}

	private Icon getIcon(String name)
	{
		return new ImageIcon(getClass().getResource(name));
	}

	private JToolBar initToolBar()
	{
		JToolBar tools = new JToolBar();
		tools.setFloatable(false);
		tools.setBorderPainted(true);

		tools.add(new JXDropButton(new AbstractAction("New", getIcon("new.gif")) { public void actionPerformed(ActionEvent e) { JOptionPane.showMessageDialog(null, "Action Invoked"); } }, createNewPopup()));
		tools.add(createDropButton("Save", "save.gif", createPopup()));
		JButton btn = tools.add(new AbstractAction("Cut", getIcon("cut.gif")) { public void actionPerformed(ActionEvent e) { JOptionPane.showMessageDialog(null, "Action Invoked"); } });
		btn.putClientProperty("hideActionText", Boolean.FALSE);
		tools.add(new AbstractAction("Copy", getIcon("copy.gif")) { public void actionPerformed(ActionEvent e) { JOptionPane.showMessageDialog(null, "Action Invoked"); } });
		tools.add(new AbstractAction("Paste", getIcon("paste.gif")) { public void actionPerformed(ActionEvent e) { JOptionPane.showMessageDialog(null, "Action Invoked"); } });
		tools.add(createDropButton("Undo", null, null));
		Component c = tools.add(createDropButton("Redo", null, null));
		c.setEnabled(false);
		tools.add(createDropButton("Windows", null, createWindowsPopup()));
		c = tools.add(createDropButton("disabled", null, createPopup()));
		c.setEnabled(false);
		tools.add(new AbstractAction("Options") { public void actionPerformed(ActionEvent e) { JOptionPane.showMessageDialog(null, "Action Invoked"); } });

		return tools;
	}

	private void init()
	{
		setLayout(new BorderLayout());

		JToolBar tools = initToolBar();

		final JComboBox cmbo = new JComboBox(new String[] { "System", "Metal", "Windows" } );
		cmbo.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				String name = null;
				try
				{
					switch(cmbo.getSelectedIndex())
					{
						case 0: name = UIManager.getSystemLookAndFeelClassName(); break;
						case 1: name = "javax.swing.plaf.metal.MetalLookAndFeel"; break;
						case 2: name = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel"; break;
					}
					if(name == null) return;
					UIManager.setLookAndFeel(name);
					SwingUtilities.updateComponentTreeUI(DropButtonDemo.this);
				}
				catch(Exception ex) { JOptionPane.showMessageDialog(DropButtonDemo.this, "Cannot find the LNF (" + name + ")"); }
			}
		});
		cmbo.setSelectedIndex(0);

		JPanel p = new JPanel(new BorderLayout());
		p.add(tools, BorderLayout.CENTER);
		p.add(cmbo, BorderLayout.EAST);
		add(p, BorderLayout.NORTH);

		JPanel p1 = new JPanel(new VerticalLayout(2));
			JPanel p11 = new JPanel();
			p11.add(createDropButton("enabled w/ menu", null, createPopup()));
			Component c = p11.add(createDropButton("disabled w/ menu", null, createPopup()));
			c.setEnabled(false);
			p11.add(createDropButton("enabled w/o menu", null, null));
			c = p11.add(createDropButton("disabled w/o menu", null, null));
			c.setEnabled(false);
		p1.add(p11);
			JPanel p12 = new JPanel();
			p12.add(new JXDropButton(new AbstractAction("action enabled w/ menu") { public void actionPerformed(ActionEvent e) { JOptionPane.showMessageDialog(null, "Action Invoked"); } }, createPopup()));
			c = p12.add(new JXDropButton(new AbstractAction("action disabled w/ menu") { public void actionPerformed(ActionEvent e) { JOptionPane.showMessageDialog(null, "Action Invoked"); } }, createPopup()));
			c.setEnabled(false);
			p12.add(new JXDropButton(new AbstractAction("action enabled w/o menu") { public void actionPerformed(ActionEvent e) { JOptionPane.showMessageDialog(null, "Action Invoked"); } }));
			c = p12.add(new JXDropButton(new AbstractAction("action disabled w/o menu") { public void actionPerformed(ActionEvent e) { JOptionPane.showMessageDialog(null, "Action Invoked"); } }));
			c.setEnabled(false);
		p1.add(p12);
			JPanel p13 = new JPanel();
			p13.add(new JButton(new AbstractAction("action button (enabled)") { public void actionPerformed(ActionEvent e) { JOptionPane.showMessageDialog(null, "Action Invoked"); } }));
			c = p13.add(new JButton(new AbstractAction("action button (disabled)") { public void actionPerformed(ActionEvent e) { JOptionPane.showMessageDialog(null, "Action Invoked"); } }));
			c.setEnabled(false);
		p1.add(p13);

		JPanel ptop = new JPanel(new BorderLayout());
		ptop.add(p1, BorderLayout.NORTH);

		JTextArea area = new JTextArea();
		area.setEditable(false);
		area.setWrapStyleWord(true);
		area.setLineWrap(true);
		area.setText("The JXDropButton is a JButton with a JPopupMenu allowing the developer to give the user more options and take" +
					 " up less screen realty. An action could be used to define the button. The behavior changes to allow the" +
					 " user to click the left side of the button (\"action area\") to invoke that action and not the popup." +
					 " On \"hover\", the user will notice a line drawn between the text and the down" +
					 " arrow. The user could also click the right side of the button (after the line) to only show the popup. If the" +
					 " user clicks the action area then only that part of the button will be \"pressed\" and the arrow side will remain" +
					 " as is.\n" +
					 "JXDropButtons look and behave exactly like JButtons if no menu is assigned to them.\n" +
					 "The bottom 2 buttons are regular JButtons, all others are JXDropButtons. Also, 'Undo' and 'Redo' buttons" +
					 " in the tool bar are JXDropButtons." +
					 "\nIn this example, the \'New\' button and the first button on the second row are JXDropButtons with Actions." +
					 " You will notice that when you hover, a line is drawn separating the button text from the arrow icon. The labels" +
					 " of the buttons will tell you what type of button and if there is an action and/or a menu associated with it." +
					 "\n\nThe creation of a JXDropButton:\n" +
					 "JXDropButton btnNew = new JXDropButton(actionNew, \"new_email.png\", createNewPopup());\n" +
					 "\nThen the button could be added to the ToolBar:\n" +
					 "toolbar.add(btnNew);\n\n" +
					 "actionNew is a javax.swing.Action that when invoked would invoke the delegated action (see below)\n" +
					 "createNewPopup() method would create a javax.swing.JPopupMenu with items for email, event, task, note," +
					 " folder, account, etc... allowing the user to select one. The application could then switch the \'actionNew\'s" +
					 " icon to that of the \'actionEvent\' and the \'actionNew\' would delegate to the \'actionEvent\' instead of" +
					 " the \'actionNewMessage\'.\n\n" +
					 "Action actionNew, actionNewMessage, actionNewEvent, ...;\n" +
					 "... //instantiate actions\n" +
					 "Action delegateAction = actionNewMessage;\n" +
					 "actionNew = new AbstractAction(\"New\", \"new_email.png\") { public void actionPerformed(ActionEvent e) {\n" +
					 "\tif(delegateAction != null) delegateAction.actionPerformed(e);//OK cause we\'re in the EDT\n});\n" +
					 "\nThen you could define actionNewEvent as:\n" +
					 "actionNewEvent = new AbstractAction(\"Event\", \"new_event.png\") { public void actionPerformed(ActionEvent e) {\n" +
					 "\tdelegateAction = actionNewEvent;\n\tactionNew.put(Action.SMALL_ICON, (Icon)actionNewEvent.get(Action.SMALL_ICON));" +
					 "\n\tdoNewEvent(e);//actually perform the new event\n});\n" +
					 "\nAll actions would be similar to this." +
					 "\nThis would then switch to JXDropButton\'s icon to the actionEvent\'s icon but the text would still be \'New \'" +
					 " and the default action when the action area is clicked would be the eventAction. The popup menu would only appear" +
					 " if the user clicked the arrow area (or to the right of the line since the line is only drawn when an action is" +
					 " assigned to the button).\n" +
					 "It\'s OK to reset the icon every time the action is called, but in production you might want to separate the popup" +
					 " menu actions and the actual action delegates just for clarity.\n"
					 );

		ptop.add(new JScrollPane(area), BorderLayout.CENTER);
		add(ptop, BorderLayout.CENTER);
	}

	public static void main(String[] args) { new DropButtonDemo(); }
}
