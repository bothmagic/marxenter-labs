/*
 * $Id: DemoPanel.java 139 2004-10-25 12:06:22Z kleopatra $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.jdnc.incubator.rbair.masterdetail;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import org.jdesktop.jdnc.incubator.rbair.JNTable;
import org.jdesktop.jdnc.incubator.rbair.swing.JTitledPanel;
import org.jdesktop.jdnc.incubator.rbair.swing.JXList;
import org.jdesktop.jdnc.incubator.rbair.swing.binding.ListBinding;
import org.jdesktop.jdnc.incubator.rbair.swing.binding.TableBinding;
import org.jdesktop.jdnc.incubator.rbair.swing.binding.TextBinding;
import org.jdesktop.jdnc.incubator.rbair.swing.data.DataModel;
import org.jdesktop.jdnc.incubator.rbair.swing.data.DataSource;
import org.jdesktop.jdnc.incubator.rbair.swing.data.Transaction;

/**
 * Contains common gui configuration code for the masterdetail demo. The demo
 * shows how a nearly identical configuration of DataModels (identical as far
 * as the components are concerned) can be used to display data from JavaBeans,
 * RowSets, and a mixture of the two. The master/detail demo contains 3
 * DataModels, forming a master->detail/master->detail chain.
 * 
 * @author Richard Bair
 */
public abstract class DemoPanel extends JPanel {
    private static final Color BACKGROUND = new Color(238, 238, 238);
    public static final Color BACKGROUND2 = new Color(236, 236, 237);
    public static final Color BACKGROUND3 = new Color(254, 254, 254);
	
    protected void preconfigure() {}
	protected abstract DataModel getItemsDM();
	protected abstract DataModel getUserDM();
	protected abstract DataModel getUserItemsDM();
	protected void postconfigure() {}
	
	private DataModel itemsDM;
	private DataModel userDM;
	private DataModel userItemsDM;
	
	protected DemoPanel() {
		preconfigure();
		initGui();
		postconfigure();
	}
	
	private void initGui() {
		setLayout(new BorderLayout());
		
		itemsDM = getItemsDM();
		userDM = getUserDM();
		userItemsDM = getUserItemsDM();
		
		JSplitPane sp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		sp.setBorder(BorderFactory.createEmptyBorder());
		add(sp, BorderLayout.CENTER);

		JXList list = new JXList();
		new ListBinding(list, itemsDM, "name");
		JTitledPanel leftPanel = new JTitledPanel("Items");
		JScrollPane scrollPane = new JScrollPane(list);
		scrollPane.setBorder(BorderFactory.createEmptyBorder());
		leftPanel.setContentContainer(scrollPane);
		sp.setLeftComponent(leftPanel);

		//add the item detail information, including the seller information
		JPanel detailPanel = new JPanel(new GridBagLayout());
		GridBagConstraints labelgbc = new GridBagConstraints();
		labelgbc.anchor = GridBagConstraints.CENTER;
		labelgbc.fill = GridBagConstraints.NONE;
		labelgbc.gridheight = 1;
		labelgbc.gridwidth = 1;
		labelgbc.gridx = 0;
		labelgbc.gridy = 0;
		labelgbc.weightx = 0;
		labelgbc.weighty = 0;
		labelgbc.insets = new Insets(11, 11, 0, 0);
		GridBagConstraints componentgbc = new GridBagConstraints();
		componentgbc.anchor = GridBagConstraints.CENTER;
		componentgbc.fill = GridBagConstraints.HORIZONTAL;
		componentgbc.gridheight = 1;
		componentgbc.gridwidth = 1;
		componentgbc.gridx = 1;
		componentgbc.gridy = 0;
		componentgbc.weightx = 1;
		componentgbc.weighty = 0;
		componentgbc.insets = new Insets(11, 5, 0, 12);
		
		JLabel label = new JLabel("Name");
		detailPanel.add(label, labelgbc);
		JTextField field = new JTextField();
		new TextBinding(field, itemsDM, "name");
		detailPanel.add(field, componentgbc);
		
		labelgbc.gridy++;
		componentgbc.gridy++;
		labelgbc.insets = new Insets(5, 11, 0, 0);
		componentgbc.insets = new Insets(5, 5, 0, 12);
		label = new JLabel("Description");
		detailPanel.add(label, labelgbc);
		field = new JTextField();
		new TextBinding(field, itemsDM, "description");
		detailPanel.add(field, componentgbc);

		labelgbc.gridy++;
		componentgbc.gridy++;
		label = new JLabel("First Name");
		detailPanel.add(label, labelgbc);
		field = new JTextField();
		new TextBinding(field, userDM, "firstname");
		detailPanel.add(field, componentgbc);
		
		labelgbc.gridy++;
		componentgbc.gridy++;
		label = new JLabel("Last Name");
		detailPanel.add(label, labelgbc);
		field = new JTextField();
		new TextBinding(field, userDM, "lastname");
		detailPanel.add(field, componentgbc);

		labelgbc.gridy++;
		componentgbc.gridy++;
		label = new JLabel("User Name");
		detailPanel.add(label, labelgbc);
		field = new JTextField();
		new TextBinding(field, userDM, "username");
		detailPanel.add(field, componentgbc);

		labelgbc.gridy++;
		componentgbc.gridy++;
		label = new JLabel("Email");
		detailPanel.add(label, labelgbc);
		field = new JTextField();
		new TextBinding(field, userDM, "email");
		detailPanel.add(field, componentgbc);

		labelgbc.gridy++;
		componentgbc.gridy++;
		label = new JLabel("Street");
		detailPanel.add(label, labelgbc);
		field = new JTextField();
		new TextBinding(field, userDM, "address.street");
		detailPanel.add(field, componentgbc);

		labelgbc.gridy++;
		componentgbc.gridy++;
		label = new JLabel("City");
		detailPanel.add(label, labelgbc);
		field = new JTextField();
		new TextBinding(field, userDM, "address.city");
		detailPanel.add(field, componentgbc);

		labelgbc.gridy++;
		componentgbc.gridy++;
		label = new JLabel("Zip");
		detailPanel.add(label, labelgbc);
		field = new JTextField();
		new TextBinding(field, userDM, "address.zipcode");
		detailPanel.add(field, componentgbc);

		labelgbc.gridy++;
		labelgbc.gridwidth = 2;
		componentgbc.gridy += 2;
		componentgbc.gridx = 0;
		componentgbc.weightx = 1.0;
		componentgbc.weighty = 1.0;
		componentgbc.gridwidth = 2;
		componentgbc.insets = new Insets(5, 11, 12, 12);
		componentgbc.fill = GridBagConstraints.BOTH;
		label = new JLabel("Items being sold by same user");
		detailPanel.add(label, labelgbc);
        JNTable table = new JNTable();
        table.setBackground(BACKGROUND);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setEvenRowBackground(BACKGROUND3);
        table.setShowHorizontalLines(false);
        table.setHasColumnControl(true);
        table.setPreferredVisibleRowCount(12);
        table.setHeaderBackground(BACKGROUND2);
//    bindTable(table.getTable(), userItemsDM, new String[] {"name", "description"});
		bindUserItemsTable(table, "items");
		detailPanel.add(table, componentgbc);
		JTitledPanel rightPanel = new JTitledPanel("Details");
		rightPanel.setContentContainer(detailPanel);
		sp.setRightComponent(rightPanel);
		
		sp.setDividerLocation(150);
	}
	
	protected void bindUserItemsTable(JNTable table, String tableFieldName) {
    new TableBinding(table.getTable(), userItemsDM, new String[]{"name", "description"});
  }
	
  public Transaction getTransaction() {
		DataSource ds = itemsDM.getDataSource();
		return ds == null ? null : ds.getTransaction();
	}
}
