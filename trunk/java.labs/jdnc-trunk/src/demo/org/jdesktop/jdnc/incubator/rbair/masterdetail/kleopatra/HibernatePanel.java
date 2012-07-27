/*
 * $Id: HibernatePanel.java 137 2004-10-22 11:57:57Z kleopatra $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.jdnc.incubator.rbair.masterdetail.kleopatra;

import org.hibernate.auction.model.Item;
import org.hibernate.auction.model.User;
import org.hibernate.auction.persistence.HibernateUtil;
import org.jdesktop.jdnc.incubator.rbair.hibernate.HibernateDataModel;
import org.jdesktop.jdnc.incubator.rbair.hibernate.HibernateDataSource;
import org.jdesktop.jdnc.incubator.rbair.swing.data.*;

/**
 * This demo panel implementation is configured for use with hibernate
 * @author Richard Bair
 */
class HibernatePanel extends DemoPanel {
	private HibernateDataModel itemsDM;
	private HibernateDataModel userDM;
	private HibernateDataModel userItemsDM;
	private HibernateDataSource ds;
	private Transaction tx;

	HibernatePanel() {
	}

	/* (non-Javadoc)
	 * @see org.jdesktop.jdnc.incubator.rbair.masterdetail.DemoPanel#getItemsDM()
	 */
	protected DataModel getItemsDM() {
		ds = new HibernateDataSource();
		try {
			itemsDM = new HibernateDataModel(Item.class);
			itemsDM.setDataSource(ds);
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
		try {
			userDM = new HibernateDataModel(User.class);
			userDM.setKeyFields(new String[]{"id"});
			userDM.addField(new MetaData("address.street", String.class, "Street"));
			userDM.addField(new MetaData("address.city", String.class, "City"));
			userDM.addField(new MetaData("address.zipcode", String.class, "Zip"));
			userDM.setKey("seller");
			userDM.setMasterFieldName("seller");
			userDM.setMasterDataModel(itemsDM);
			userDM.setDataSource(ds);
			return userDM;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see org.jdesktop.jdnc.incubator.rbair.masterdetail.DemoPanel#getUserItemsDM()
	 */
	protected DataModel getUserItemsDM() {
		try {
			userItemsDM = new HibernateDataModel(Item.class);
			userItemsDM.setKey("items");
			userItemsDM.setKeyFields(new String[]{"id"});
			userItemsDM.setMasterFieldName("items");
			userItemsDM.addField(new MetaData("name", String.class, "Name"));
			userItemsDM.addField(new MetaData("description", String.class, "Description"));
			userItemsDM.setMasterDataModel(userDM);
			userItemsDM.setDataSource(ds);
			return userItemsDM;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
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
