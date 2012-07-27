/*
 * $Id: MasterDetailWindow.java 148 2004-10-29 20:43:46Z rbair $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.jdnc.incubator.rbair.masterdetail;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;

import javax.swing.AbstractAction;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;

import org.jdesktop.jdnc.incubator.rbair.swing.Application;
import org.jdesktop.jdnc.incubator.rbair.swing.JXFrame;
import org.jdesktop.jdnc.incubator.rbair.swing.ProgressManager;
import org.jdesktop.jdnc.incubator.rbair.swing.Progressable;
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
	private JavaBeanPanel javaBeanPanel;
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
			Application.getInstance().setMainFrame(this);
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
//		JRadioButtonMenuItem menuItem = new JRadioButtonMenuItem(new ViewJavaBeanPanelAction());
//		menuItem.setSelected(true);
//		group.add(menuItem);
//		mainMenu.add(menuItem);
		JRadioButtonMenuItem menuItem = new JRadioButtonMenuItem(new ViewHibernatePanelAction());
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
		javaBeanPanel = new JavaBeanPanel();
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
		
//		ProgressManager pm = Application.getInstance().getProgressManager();
//        pm.addProgressable(new Test("<html><h3><b>Loading Customers</b></h3></html>", null, "Loading 27 of 54", 0, 54, 27, false, false));
//        pm.addProgressable(new Test("<html><h3><b>Saving Order #1031</b></h3></html>", null, "Connecting to server...", 0, 100, 0, true, true));
//        pm.addProgressable(new Test("<html><h3><b>Loading Order #2500</b></h3></html>", null, "Reading Data...", 0, 100, 15, false, false));
//	    

	}
	
    private static final class Test implements Progressable {
        private String d;
        private ImageIcon i;
        private String m;
        private int min;
        private int max;
        private int progress;
        private boolean ind;
        private boolean modal;
        public Test(String d, ImageIcon i, String m, int min, int max, int p, boolean ind, boolean modal) {
            this.d = d;
            this.i = i;
            this.m = m;
            this.min = min;
            this.max = max;
            this.progress = p;
            this.ind = ind;
            this.modal = modal;
        }
        /**
         * @inheritDoc
         */
        public String getDescription() {
            return d;
        }

        /**
         * @inheritDoc
         */
        public Icon getIcon() {
            return i;
        }

        /**
         * @inheritDoc
         */
        public String getMessage() {
            return m;
        }

        /**
         * @inheritDoc
         */
        public int getMinimum() {
            return min;
        }

        /**
         * @inheritDoc
         */
        public int getMaximum() {
            return max;
        }

        /**
         * @inheritDoc
         */
        public int getProgress() {
            return progress;
        }

        /**
         * @inheritDoc
         */
        public boolean isModal() {
            return modal;
        }

        /**
         * @inheritDoc
         */
        public boolean cancel() throws Exception {
            return false;
        }

        /**
         * @inheritDoc
         */
        public boolean canCancel() {
            return true;
        }
        /**
         * @inheritDoc
         */
        public boolean isIndeterminate() {
            return ind;
        }
        
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
