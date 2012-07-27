/*
 * $Id: JavaBeanMasterDetailHandler.java 64 2004-09-22 19:43:37Z rbair $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.jdnc.incubator.rbair.swing.data;

/**
 * This implementation of the MasterDetailHandler requires a JavaBeanDataModel
 * for both params in its <code>handleMasterChanged</code> method. It will
 * simply extract one or more java beans from the master and place them in
 * the detail.
 * @author Richard Bair
 */
public class JavaBeanMasterDetailHandler implements MasterDetailHandler {

	/**
	 * @inheritDoc
	 */
	public void handleMasterChanged(DataModel master, DataModel detail) {
		if (!(master instanceof JavaBeanDataModel)) {
			throw new IllegalArgumentException("The master DataModel must be " +
					"of type JavaBeanDataModel");
		}
		if (!(detail instanceof JavaBeanDataModel)) {
			throw new IllegalArgumentException("The detail DataModel must be " +
					"of type JavaBeanDataModel");
		}
		
		JavaBeanDataModel m = (JavaBeanDataModel)master;
		JavaBeanDataModel d = (JavaBeanDataModel)detail;
		
		//retrieve the master field
		Object val = m.getValue(d.getMasterFieldName());
		//set the value in the detail javabean. The detail will take care of
		//expanding the val if it is a collection or an array.
		d.setJavaBean(val);
	}

}
