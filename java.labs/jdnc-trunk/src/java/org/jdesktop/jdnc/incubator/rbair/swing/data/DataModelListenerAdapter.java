/*
 * $Id: DataModelListenerAdapter.java 15 2004-09-04 23:06:37Z rbair $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.jdnc.incubator.rbair.swing.data;

/**
 * Simple adapter class for the DataModelListener. If you have a listener
 * that only needs to implement one or two methods of the DataModeListener
 * interface, then you may want to extend this class and override the method
 * you are interested in.
 * @author Richard Bair
 */
public class DataModelListenerAdapter implements DataModelListener {

	/**
	 * @inheritDoc
	 */
	public void valueChanged(ValueChangeEvent e) {
	}

	/**
	 * @inheritDoc
	 */
	public void modelChanged(ModelChangeEvent e) {
	}

	/**
	 * @inheritDoc
	 */
	public void metaDataChanged(MetaDataChangeEvent e) {
	}

}
