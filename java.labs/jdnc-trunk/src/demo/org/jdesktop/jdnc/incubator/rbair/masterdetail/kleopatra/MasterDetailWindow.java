/*
 * $Id: MasterDetailWindow.java 137 2004-10-22 11:57:57Z kleopatra $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.jdnc.incubator.rbair.masterdetail.kleopatra;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;

import javax.swing.*;

import org.jdesktop.jdnc.incubator.rbair.swing.JXFrame;
import org.jdesktop.jdnc.incubator.rbair.swing.data.Transaction;
import org.jdesktop.jdnc.incubator.rbair.util.ResourceBundle;
import org.jdesktop.jdnc.incubator.rbair.util.WindowUtils;

/**
 * This demo is for testing out basic master/detail functionality. The master
 * is a collection of Items. Each item has a seller. The seller is a User.
 * The User is represented in a detail DataModel.
 * <p>
 * This file has code for both sql based data models and java bean based
 * data models. Only the javabean stuff is working correctly right now.
 * So don't let the file size scare you, less than half of it is necessary
 * for the java bean demo, and almost all of that is GridBag :)
 * @author Richard Bair
 */
public class MasterDetailWindow extends JXFrame {
	private CardLayout cards = new CardLayout();
	private JPanel cardPanel;
	private DemoPanel currentPanel;
	private RowSetPanel rowSetPanel;
	private WJavaBeanPanel javaBeanPanel;
	private HibernatePanel hibernatePanel;
	private MixedPanel mixedPanel;
	
	public MasterDetailWindow() {
		try {
			try {
				//construct the database (not normally necessary in clients)
				Class.forName("org.hsqldb.jdbcDriver");
				Connection conn = DriverManager.getConnection("jdbc:hsqldb:mem:test", "sa", "");
//				FileInputStream fis = new FileInputStream("resources/schema.sql");
//				FileInputStream fis = new FileInputStream("src/java/org/jdesktop/jdnc/incubator/rbair/resources/schema.sql");
				InputStream fis = ResourceBundle.getInputStream("org/jdesktop/jdnc/incubator/rbair/resources/schema.sql");
				StringBuffer buffer = new StringBuffer();
				byte[] buff = new byte[4096];
				int len = -1;
				while ((len = fis.read(buff)) != -1) {
					buffer.append(new String(buff, 0, len));
				}
				conn.createStatement().execute(buffer.toString());
				conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			initGui();
		} catch (Exception e) {
			System.err.println("Failed to initialize the MasterDetailWindow");
			e.printStackTrace();
		}
	}
	
	private void initGui() {
		JMenuBar mb = new JMenuBar();
		JMenu mainMenu = new JMenu("Main");
		ButtonGroup group = new ButtonGroup();
		JRadioButtonMenuItem menuItem = new JRadioButtonMenuItem(new ViewJavaBeanPanelAction());
		menuItem.setSelected(true);
		group.add(menuItem);
		mainMenu.add(menuItem);
		menuItem = new JRadioButtonMenuItem(new ViewHibernatePanelAction());
		group.add(menuItem);
		mainMenu.add(menuItem);
		menuItem = new JRadioButtonMenuItem(new ViewRowSetPanelAction());
		group.add(menuItem);
		mainMenu.add(menuItem);
		menuItem = new JRadioButtonMenuItem(new ViewMixedPanelAction());
		group.add(menuItem);
		mainMenu.add(menuItem);
		mainMenu.addSeparator();
		mainMenu.add(new ViewSourceAction());
		mainMenu.addSeparator();
		mainMenu.add(new ExitAction());
		mb.add(mainMenu);
		setJMenuBar(mb);
		//configure with a card layout. Whenever the layout being used is
		//supposed to be changed, flip to the proper card
		getContentPane().setLayout(new BorderLayout());
		cardPanel = new JPanel(cards);
		getContentPane().add(cardPanel, BorderLayout.CENTER);
		javaBeanPanel = new WJavaBeanPanel();
		cardPanel.add(javaBeanPanel, "java-bean");
		hibernatePanel = new HibernatePanel();
		cardPanel.add(hibernatePanel, "hibernate");
		setTitle("Master/Detail Demo -- Using JavaBean backed DataModels");
		rowSetPanel = new RowSetPanel();
		cardPanel.add(rowSetPanel, "row-set");
		mixedPanel = new MixedPanel();
		cardPanel.add(mixedPanel, "mixed");
		
		JPanel buttonPanel = new JPanel();
		JButton commitButton = new JButton(new CommitAction());
		buttonPanel.add(commitButton);
		JButton rollbackButton = new JButton(new RollbackAction());
		buttonPanel.add(rollbackButton);
		getContentPane().add(buttonPanel, BorderLayout.SOUTH);
		
		Dimension dim = new Dimension(800, 450);
		//java 1.5 syntax, I suppose
//		setPreferredSize(dim);
		setSize(dim);
		setLocation(WindowUtils.getPointForCentering(this));
	}
	
	private final class ExitAction extends AbstractAction {
		public ExitAction() {
			super.putValue(AbstractAction.NAME, "Exit");
			super.putValue(AbstractAction.SHORT_DESCRIPTION, "Exit Demo");
		}

		
		/* (non-Javadoc)
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent e) {
			System.exit(0);
		}
		
	}
	
	private final class ViewJavaBeanPanelAction extends AbstractAction {
		public ViewJavaBeanPanelAction() {
			super.putValue(AbstractAction.NAME, "Use JavaBeans");
			super.putValue(AbstractAction.SHORT_DESCRIPTION, "Use JavaBean " +
					"backed DataModels");
		}

		
		/* (non-Javadoc)
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent e) {
			cards.show(cardPanel, "java-bean");
			currentPanel = javaBeanPanel;
			setTitle("Master/Detail Demo -- Using JavaBean backed DataModels");
		}
		
	}

	private final class ViewHibernatePanelAction extends AbstractAction {
		public ViewHibernatePanelAction() {
			super.putValue(AbstractAction.NAME, "Use Hibernate");
			super.putValue(AbstractAction.SHORT_DESCRIPTION, "Use Hibernate " +
					"backed DataModels");
		}

		/* (non-Javadoc)
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent e) {
			cards.show(cardPanel, "hibernate");
			currentPanel = hibernatePanel;
			setTitle("Master/Detail Demo -- Using Hibernate backed DataModels");
		}
		
	}

	private final class ViewRowSetPanelAction extends AbstractAction {
		public ViewRowSetPanelAction() {
			super.putValue(AbstractAction.NAME, "Use RowSets");
			super.putValue(AbstractAction.SHORT_DESCRIPTION, "Use RowSet " +
					"backed DataModels");
		}

		/* (non-Javadoc)
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent e) {
			cards.show(cardPanel, "row-set");
			currentPanel = rowSetPanel;
			setTitle("Master/Detail Demo -- Using RowSet backed DataModels");
		}
		
	}

	private final class ViewMixedPanelAction extends AbstractAction {
		public ViewMixedPanelAction() {
			super.putValue(AbstractAction.NAME, "Use JavaBeans & RowSets");
			super.putValue(AbstractAction.SHORT_DESCRIPTION, "Use a mixture " +
					"of JavaBean and RowSet backed DataModels");
		}

		/* (non-Javadoc)
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent e) {
			cards.show(cardPanel, "mixed");
			currentPanel = mixedPanel;
			setTitle("Master/Detail Demo -- Using both JavaBean and RowSet backed DataModels");
		}
		
	}
	
	private final class ViewSourceAction extends AbstractAction {
		public ViewSourceAction() {
			super.putValue(AbstractAction.NAME, "View Source");
			super.putValue(AbstractAction.SHORT_DESCRIPTION, "View the source" +
					" code for each of the three demo panels, and their " +
					"common parent, DemoPanel");
		}
		
		public void actionPerformed(ActionEvent e) {
			//open up the demo source dialog
			JDialog dlg = new DemoSourceDialog();
			dlg.setVisible(true);
		}
	}

	private final class CommitAction extends AbstractAction {
		public CommitAction() {
			super.putValue(AbstractAction.NAME, "Commit");
			super.putValue(AbstractAction.SHORT_DESCRIPTION, "Commit all changes" +
					" to the database");
		}
		
		public void actionPerformed(ActionEvent e) {
			Transaction tx = currentPanel.getTransaction();
			if (tx != null) {
				try {
					tx.commit();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}
	}

	private final class RollbackAction extends AbstractAction {
		public RollbackAction() {
			super.putValue(AbstractAction.NAME, "Rollback");
			super.putValue(AbstractAction.SHORT_DESCRIPTION, "Undo all changes" +
					" since last commit.");
		}
		
		public void actionPerformed(ActionEvent e) {
			Transaction tx = currentPanel.getTransaction();
			if (tx != null) {
				try {
					tx.rollback();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}
	}
}
