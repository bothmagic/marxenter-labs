/*
 * $Id: TestDataModel.java 71 2004-09-22 23:43:50Z rbair $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.jdnc.incubator.rbair.swing.data;

import java.sql.Connection;
import java.sql.DriverManager;

import javax.sql.rowset.CachedRowSet;

import junit.framework.TestCase;

import com.sun.rowset.CachedRowSetImpl;

/**
 * This test case is for testing the DataModel architecture and
 * implementations. In particular, it tests both the JavaBeanDataModel
 * and the RowSetDataModel. The JavaBeanDataModel may be loaded either
 * from xml, or from hibernate. The RowSetDataModel is loaded via straight
 * sql.
 * <p>
 * The test case is constructed against an hypersonic database, constructed
 * from the schema.sql file located in the resources directory.
 * <p>
 * TODO Currently, I am only testing RowSetDataModel.
 * @author Richard Bair
 */
public class TestDataModel extends TestCase {
	private Connection conn;
	private DataModel usersDM;
	private DataModel itemsDM;
	
	/**
	 * Constructor for TestDataModel.
	 * @param arg0
	 */
	public TestDataModel(String arg0) {
		super(arg0);
	}
	
	/*
	 * TODO This setUp isn't proper because the DataSource being used is
	 * asynchronous, and I need a synchronous DataSource while unit testing
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		RowSetDataSource ds = new RowSetDataSource("org.hsqldb.jdbcDriver", "jdbc:hsqldb:mem:aname", "sa", "");

		usersDM = new RowSetDataModel(ds);
		((RowSetDataModel)usersDM).setSelectSql("select * from users");
		
		itemsDM = new RowSetDataModel(ds);
		((RowSetDataModel)itemsDM).setSelectSql("select * from item");
		
		ds.setConnected(true);
	}

	/*
	 * @see TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		conn.close();
	}

	/**
	 * Test the "size()" method of DataModel.
	 */
	public void testSize() {
		assertEquals(usersDM.getRecordCount(), 4);
		assertEquals(itemsDM.getRecordCount(), 4);
	}
	
	/**
	 * Test the navigational methods. This test doesn't take any
	 * notification mechanism into considerations.
	 */
	public void testNavigation() {
		assertTrue(usersDM.firstRecord());
		assertTrue(usersDM.hasNext());
		assertFalse(usersDM.hasPrev());
		assertTrue(usersDM.nextRecord());
		assertTrue(usersDM.hasNext());
		assertTrue(usersDM.hasPrev());
		assertTrue(usersDM.nextRecord());
		assertTrue(usersDM.hasNext());
		assertTrue(usersDM.hasPrev());
		assertTrue(usersDM.nextRecord());
		assertFalse(usersDM.hasNext());
		assertTrue(usersDM.hasPrev());
		assertFalse(usersDM.nextRecord());
		assertTrue(usersDM.prevRecord());
		assertTrue(usersDM.prevRecord());
		assertTrue(usersDM.lastRecord());
		assertFalse(usersDM.hasNext());
		assertTrue(usersDM.hasPrev());
		assertTrue(usersDM.firstRecord());
		assertTrue(usersDM.hasNext());
		assertFalse(usersDM.hasPrev());
	}
	
	/**
	 * 
	 *
	 */
	public void testMetaData() {
//		usersDM.getFieldCount()
//		usersDM.getFieldNames()
//		usersDM.getMetaData(fieldName)
	}
	
	/**
	 * 
	 *
	 */
	public void testValues() {
//		usersDM.getValue(fieldName);
//		usersDM.getValue(fieldName, int index);
	}
	
}
