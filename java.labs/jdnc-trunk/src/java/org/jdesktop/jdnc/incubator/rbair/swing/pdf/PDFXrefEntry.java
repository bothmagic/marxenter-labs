/*
 * $Id: PDFXrefEntry.java 331 2005-02-01 19:05:26Z rbair $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.jdnc.incubator.rbair.swing.pdf;

/**
 * @author Richard Bair
 */
final class PDFXrefEntry {
	/**
	 * 
	 */
	private long byteOffset;
	/**
	 * 
	 */
	private int generationNumber;
	/**
	 * 
	 */
	private boolean inUse;
	/**
	 * 
	 */
	private int objectNumber;
	
	/**
	 * 10 digits byte offset (leading 0's), space, 5 digits generation number (leading 0's), space, n if in use, f if not.
	 * @param encodedString
	 */
	PDFXrefEntry(String encodedString, int objectNumber) {
		this.objectNumber = objectNumber;
		byteOffset = Long.parseLong(encodedString.substring(0, 10));
		generationNumber = Integer.parseInt(encodedString.substring(11, 16));
		inUse = encodedString.charAt(17) == 'n';
	}
	/**
	 * @return Returns the byteOffset.
	 */
	long getByteOffset() {
		return byteOffset;
	}
	/**
	 * @return Returns the generationNumber.
	 */
	int getGenerationNumber() {
		return generationNumber;
	}
	/**
	 * @return Returns the inUse.
	 */
	boolean isInUse() {
		return inUse;
	}
	/**
	 * @return Returns the objectNumber.
	 */
	int getObjectNumber() {
		return objectNumber;
	}
}