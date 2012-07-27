/*
 * $Id: RowSetPanel.java 137 2004-10-22 11:57:57Z kleopatra $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.jdnc.incubator.rbair.masterdetail.kleopatra;

import org.jdesktop.jdnc.incubator.rbair.swing.data.*;

/**
 * This demo panel implementation is configured for use with a row set
 * @author Richard Bair
 */
class RowSetPanel extends DemoPanel {
	private RowSetDataModel itemsDM;
	private RowSetDataModel userDM;
	private RowSetDataModel userItemsDM;
	private RowSetDataSource ds;
	private Transaction tx;

	RowSetPanel() {
	}

	/* (non-Javadoc)
	 * @see org.jdesktop.jdnc.incubator.rbair.masterdetail.DemoPanel#getItemsDM()
	 */
	protected DataModel getItemsDM() {
		ds = new RowSetDataSource("org.hsqldb.jdbcDriver", 
				"jdbc:hsqldb:mem:test", "sa", "");
		itemsDM = new RowSetDataModel(ds);
		itemsDM.setTableName("item");
		itemsDM.setSelectSql("select * from item");
		itemsDM.addField(new MetaData("name", String.class, "Name"));
		itemsDM.addField(new MetaData("description", String.class, "Description"));
		itemsDM.addField(new MetaData("seller_id", long.class, "Seller #"));
		itemsDM.setKeyFields(new String[]{"item_id"});
		return itemsDM;
	}

	/* (non-Javadoc)
	 * @see org.jdesktop.jdnc.incubator.rbair.masterdetail.DemoPanel#getUserDM()
	 */
	protected DataModel getUserDM() {
		userDM = new RowSetDataModel(ds);
		userDM.setTableName("users");
		userDM.setSelectSql("select firstname, lastname, username, " +
				"email, street as \"address.street\", city as \"address.city\", " +
				"zipcode as \"address.zipcode\", user_id " +
				"from users where user_id = ?");
		userDM.addField(new MetaData("firstname", String.class, "First Name"));
		userDM.addField(new MetaData("lastname", String.class, "Last Name"));
		userDM.addField(new MetaData("username", String.class, "User Name"));
		userDM.addField(new MetaData("email", String.class, "Email"));
		userDM.addField(new MetaData("address.street", String.class, "Street"));
		userDM.addField(new MetaData("address.city", String.class, "City"));
		userDM.addField(new MetaData("address.zipcode", String.class, "Zip"));
		userDM.addField(new MetaData("user_id", long.class, "User #"));
		userDM.setKeyFields(new String[]{"user_id"});
		RowSetMasterDetailHandler mdh = new RowSetMasterDetailHandler();
		mdh.addFieldName("seller_id");
		userDM.setMasterDetailHandler(mdh);
		userDM.setKey("seller");
		userDM.setMasterDataModel(itemsDM);
		return userDM;
	}

	/* (non-Javadoc)
	 * @see org.jdesktop.jdnc.incubator.rbair.masterdetail.DemoPanel#getUserItemsDM()
	 */
	protected DataModel getUserItemsDM() {
		userItemsDM = new RowSetDataModel(ds);
		userItemsDM.setTableName("item");
		userItemsDM.setSelectSql("select name, description, item_id " +
				"from item where seller_id = ?");
		userItemsDM.addField(new MetaData("name", String.class, "Name"));
		userItemsDM.addField(new MetaData("description", String.class, "Description"));
		userItemsDM.setKeyFields(new String[]{"item_id"});
		RowSetMasterDetailHandler mdh = new RowSetMasterDetailHandler();
		mdh.addFieldName("user_id");
		userItemsDM.setMasterDetailHandler(mdh);
		userItemsDM.setKey("items");
		userItemsDM.setMasterDataModel(userDM);
		return userItemsDM;
	}
	
	/* (non-Javadoc)
	 * @see org.jdesktop.jdnc.incubator.rbair.masterdetail.DemoPanel#postconfigure()
	 */
	protected void postconfigure() {
		ds.setConnected(true);
		try {
			tx = new DefaultTransaction();
			tx.addDataSource(ds);
			tx.setAutoCommit(false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
