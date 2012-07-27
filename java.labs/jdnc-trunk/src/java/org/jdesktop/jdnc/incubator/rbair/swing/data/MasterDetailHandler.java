/*
 * $Id: MasterDetailHandler.java 64 2004-09-22 19:43:37Z rbair $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.jdnc.incubator.rbair.swing.data;

/**
 * An interface who's implementations contain logic used by detail DataModels
 * to decide how to handle changes in master DataModel's. When the master
 * DataModel changes current records, or changes MetaData, etc, it notifies
 * detail DataModels that a change has occured requiring the detail DataModel
 * to reload itself. Detail DataModel's use their MasterDetailHandler to handle
 * this logic. The Handler can be implemented to use caches, or reload from
 * the server, or perform a filtering operation, or any number of custom
 * logic for handling custom scenarios.
 * @author Richard Bair
 */
public interface MasterDetailHandler {
	/**
	 * This method is called by the detail DataModel when a change has occured
	 * on the master. This handle will handle the logic for this situation.
	 * @param master
	 * @param detail
	 */
	public void handleMasterChanged(DataModel master, DataModel detail);
}
