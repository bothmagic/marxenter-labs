/*
 * $Id: MixedPanel.java 137 2004-10-22 11:57:57Z kleopatra $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.jdnc.incubator.rbair.masterdetail.kleopatra;

import java.util.List;

import org.jdesktop.jdnc.incubator.rbair.swing.data.*;
import org.jdesktop.jdnc.incubator.rbair.masterdetail.kleopatra.WJavaBeanPanel.Item;
/**
 * This panel contains a mixture of JavaBean and RowSet DataModels. The
 * main DataModel is a JavaBeanDataModel, and the two detail DataModels are
 * RowSetDataModels.
 * 
 * @author Richard Bair
 */
class MixedPanel extends DemoPanel {
	private JavaBeanDataModel itemsDM;
	private RowSetDataModel userDM;
	private RowSetDataModel userItemsDM;
	private RowSetDataSource ds;
	private Transaction tx;
	private DefaultDataSource dds;

	protected void preconfigure() {
		dds = new DefaultDataSource();
	}

	/* (non-Javadoc)
	 * @see org.jdesktop.jdnc.incubator.rbair.masterdetail.DemoPanel#getItemsDM()
	 */
	protected DataModel getItemsDM() {
		try {
			itemsDM = new JavaBeanDataModel(Item.class);
			itemsDM.setKeyFields(new String[]{"item_id"});
			itemsDM.setDataSource(dds);
			return itemsDM;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see org.jdesktop.jdnc.incubator.rbair.masterdetail.DemoPanel#getUserDM()
	 */
	protected DataModel getUserDM() {
		ds = new RowSetDataSource("org.hsqldb.jdbcDriver", 
				"jdbc:hsqldb:mem:test", "sa", "");
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
		mdh.addFieldName("seller.userId");
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
		userItemsDM.setSelectSql("select name, description, item_id from item where seller_id = ?");
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
		try {
			List items = WJavaBeanPanel.getItems();
			itemsDM.setJavaBean(items);
			ds.setConnected(true);
			tx = new DefaultTransaction();
			tx.addDataSource(ds);
			tx.addDataSource(dds);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
