/*
 * $Id: PDFHeader.java 331 2005-02-01 19:05:26Z rbair $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.jdnc.incubator.rbair.swing.pdf;

/**
 * @author Richard Bair
 */
/**
 * Represents the header of a pdf file
 * @author Richard Bair
 * date: May 20, 2004
 */
final class PDFHeader {
	private PDFVersion version;
	
	PDFHeader(String encodedString) {
		double versionNumber = Double.parseDouble(encodedString.substring(5));
		if (!PDFParser.CURRENT_SUPPORTED_VERSION.isCompatible(versionNumber)) {
			System.err.println("This version of the PDF file is not supported");
		} else {
			version = PDFVersion.getVersion(versionNumber);
		}
	}
	/**
	 * @return Returns the version.
	 */
	PDFVersion getVersion() {
		return version;
	}
}