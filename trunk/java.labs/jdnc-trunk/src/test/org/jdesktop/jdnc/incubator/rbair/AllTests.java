/*
 * $Id: AllTests.java 41 2004-09-08 00:14:49Z rbair $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.jdnc.incubator.rbair;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestSuite;

import org.jdesktop.jdnc.incubator.rbair.swing.data.TestDataModel;
import org.jdesktop.jdnc.incubator.rbair.util.TestProgression;
import org.jdesktop.jdnc.incubator.rbair.util.TestResourceBundle;
import org.jdesktop.jdnc.incubator.rbair.util.TestStringUtils;

/**
 * This test suite is intended to run all of the tests for
 * the code in the rbair package and subpackages. This test
 * suite manages connecting to the database server and
 * constructing the database.
 * 
 * @author Richard Bair
 */
public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(
				"Test for org.jdesktop.jdnc.incubator.rbair");

		suite.addTestSuite(TestResourceBundle.class);
		suite.addTestSuite(TestProgression.class);
		suite.addTestSuite(TestStringUtils.class);
		suite.addTestSuite(TestDataModel.class);

		TestSetup wrapper = new TestSetup(suite) {
            protected void setUp() {
                oneTimeSetUp();
            }
            protected void tearDown() {
                oneTimeTearDown();
            }
        };

        return wrapper;
	}
	
	public static void oneTimeSetUp() {
		try {
			//form the connection to the database
			Class.forName("org.hsqldb.jdbcDriver");
			Connection conn = DriverManager.getConnection("jdbc:hsqldb:mem:aname", "sa", "");
			
			//construct the database
//			FileInputStream fis = new FileInputStream("resources/schema.sql");
			FileInputStream fis = new FileInputStream("src/java/org/jdesktop/jdnc/incubator/rbair/resources/schema.sql");
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
    }

    public static void oneTimeTearDown() {
        // one-time cleanup code
    }
}
