/*
 * $Id: ProgressionFilter.java 14 2004-09-04 21:41:28Z rbair $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */

package org.jdesktop.jdnc.incubator.rbair.util;

/**
 * @author John Bair
 */
public interface ProgressionFilter {
	/**
	 * Returns true if the object passed in should be filtered, false otherwise
	 * @param obj
	 * @return
	 */
	public boolean filter(Object obj);
}
