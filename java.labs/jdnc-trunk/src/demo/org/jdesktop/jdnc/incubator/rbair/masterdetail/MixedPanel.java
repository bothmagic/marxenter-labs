/*
 * $Id: MixedPanel.java 148 2004-10-29 20:43:46Z rbair $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.jdnc.incubator.rbair.masterdetail;

import org.hibernate.auction.model.Item;
import org.hibernate.auction.persistence.HibernateUtil;
import org.jdesktop.jdnc.incubator.rbair.hibernate.HibernateDataModel;
import org.jdesktop.jdnc.incubator.rbair.hibernate.HibernateDataSource;
import org.jdesktop.jdnc.incubator.rbair.swing.data.DataModel;
import org.jdesktop.jdnc.incubator.rbair.swing.data.DefaultTransaction;
import org.jdesktop.jdnc.incubator.rbair.swing.data.MetaData;
import org.jdesktop.jdnc.incubator.rbair.swing.data.RowSetDataModel;
import org.jdesktop.jdnc.incubator.rbair.swing.data.RowSetDataSource;
import org.jdesktop.jdnc.incubator.rbair.swing.data.RowSetMasterDetailHandler;
import org.jdesktop.jdnc.incubator.rbair.swing.data.Transaction;

/**
 * This panel contains a mixture of JavaBean and RowSet DataModels. The
 * main DataModel is a JavaBeanDataModel, and the two detail DataModels are
 * RowSetDataModels.
 * 
 * @author Richard Bair
 */
class MixedPanel extends DemoPanel {
	private HibernateDataModel itemsDM;
	private RowSetDataModel userDM;
	private RowSetDataModel userItemsDM;
	private RowSetDataSource ds;
	private Transaction tx;
	private HibernateDataSource hds;

	/* (non-Javadoc)
	 * @see org.jdesktop.jdnc.incubator.rbair.masterdetail.DemoPanel#getItemsDM()
	 */
	protected DataModel getItemsDM() {
	    hds = new HibernateDataSource();
		try {
			itemsDM = new HibernateDataModel(Item.class);
			itemsDM.setDataSource(hds);
			itemsDM.setQuery(HibernateUtil.getSession().createQuery("from org.hibernate.auction.model.Item"));
			itemsDM.addField(new MetaData("name", String.class, "Name"));
			itemsDM.addField(new MetaData("description", String.class, "Description"));
			itemsDM.addField(new MetaData("seller_id", long.class, "Seller #"));
			itemsDM.setKeyFields(new String[]{"id"});
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
		mdh.addFieldName("seller.id");
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
			hds.setConnected(true);
			ds.setConnected(true);
			tx = new DefaultTransaction();
			tx.addDataSource(ds);
			tx.addDataSource(hds);
			tx.setAutoCommit(false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
