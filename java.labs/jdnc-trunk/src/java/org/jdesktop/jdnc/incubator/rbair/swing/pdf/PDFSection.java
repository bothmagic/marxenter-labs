/*
 * $Id: PDFSection.java 331 2005-02-01 19:05:26Z rbair $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.jdnc.incubator.rbair.swing.pdf;

/**
 * @author Richard Bair
 */
final class PDFSection {
	private PDFXref xref;
	private PDFTrailer trailer;
	
	PDFSection(PDFXref xref, PDFTrailer trailer) {
		assert xref != null;
		assert trailer != null;
		this.xref = xref;
		this.trailer = trailer;
	}
	/**
	 * @return Returns the trailer.
	 */
	PDFTrailer getTrailer() {
		return trailer;
	}
	/**
	 * @return Returns the xref.
	 */
	PDFXref getXref() {
		return xref;
	}
}