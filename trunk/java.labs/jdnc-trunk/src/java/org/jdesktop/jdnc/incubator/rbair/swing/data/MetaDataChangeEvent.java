/*
 * $Id: MetaDataChangeEvent.java 15 2004-09-04 23:06:37Z rbair $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */

package org.jdesktop.jdnc.incubator.rbair.swing.data;

import java.util.EventObject;

/**
 * Event fired when the set of MetaData for a DataModel has changed.
 * TODO This event *may* in the future report which MetaData has
 * been removed, or added.
 * @author Richard Bair
 */
public class MetaDataChangeEvent extends EventObject {
	/**
	 * @param source
	 */
	public MetaDataChangeEvent(Object source) {
		super(source);
	}

}
