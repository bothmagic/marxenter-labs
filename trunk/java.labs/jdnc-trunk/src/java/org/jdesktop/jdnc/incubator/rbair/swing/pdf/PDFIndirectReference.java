/*
 * $Id: PDFIndirectReference.java 331 2005-02-01 19:05:26Z rbair $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.jdnc.incubator.rbair.swing.pdf;

/**
 * @author Richard Bair
 */
/**
 * Represents an IndirectReference in a PDF file.
 * @author Richard Bair
 * date: May 21, 2004
 */
final class PDFIndirectReference {
	/**
	 * The object number of the reference
	 */
	private int objectNumber;
	/**
	 * The generation number of the reference
	 */
	private int generationNumber;
	/**
	 * A cached encoded string representation of the indirect reference of the form NN NN R where N is some number
	 */
	private String encodedString;
	
	/**
	 * Create a new indirect reference by supplying the object number and generation number of the object
	 * @param objectNumber
	 * @param generationNumber
	 */
	PDFIndirectReference(int objectNumber, int generationNumber) {
		this.objectNumber = objectNumber;
		this.generationNumber = generationNumber;
		encodedString = objectNumber + " " + generationNumber + " R";
	}
	
	/**
	 * Create a new indirect reference by supplying the encoded string
	 * @param encodedString
	 */
	PDFIndirectReference(String encodedString) {
		this.encodedString = encodedString;
		String[] tokens = encodedString.split(" ");
		objectNumber = Integer.parseInt(tokens[0]);
		generationNumber = Integer.parseInt(tokens[1]);
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		if (obj instanceof PDFIndirectReference) {
			PDFIndirectReference ref = (PDFIndirectReference)obj;
			return objectNumber == ref.objectNumber && generationNumber == ref.generationNumber;
		}
		return false;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return encodedString.hashCode();
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return encodedString;
	}
	/**
	 * @return Returns the encodedString.
	 */
	String getEncodedString() {
		return encodedString;
	}
	/**
	 * @return Returns the generationNumber.
	 */
	int getGenerationNumber() {
		return generationNumber;
	}
	/**
	 * @return Returns the objectNumber.
	 */
	int getObjectNumber() {
		return objectNumber;
	}
}