/*
 * $Id: ModelChangeEvent.java 15 2004-09-04 23:06:37Z rbair $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */

package org.jdesktop.jdnc.incubator.rbair.swing.data;

import java.util.EventObject;

/**
 * Event posted when the record set in a DataModel changes, such as whenever
 * records are added or removed.
 * TODO This needs to contain information regarding which rows were added
 * or removed.
 * @author Richard Bair
 */
public class ModelChangeEvent extends EventObject {
	/**
	 * @param source
	 */
	public ModelChangeEvent(Object source) {
		super(source);
	}

}
