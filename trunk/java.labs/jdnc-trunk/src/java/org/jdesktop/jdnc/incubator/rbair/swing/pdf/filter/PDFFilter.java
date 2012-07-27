/*
 * $Id: PDFFilter.java 331 2005-02-01 19:05:26Z rbair $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.jdnc.incubator.rbair.swing.pdf.filter;

/**
 * An implementation of this class will decode a compressed or encrypted series of data into "normal" data.
 * @author Richard Bair
 */
public interface PDFFilter {
	public byte[] decode(byte[] input) throws Exception;
}
