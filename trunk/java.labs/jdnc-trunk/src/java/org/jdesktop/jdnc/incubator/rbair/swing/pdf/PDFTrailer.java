/*
 * $Id: PDFTrailer.java 331 2005-02-01 19:05:26Z rbair $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.jdnc.incubator.rbair.swing.pdf;
import java.util.List;
import java.util.Map;

/**
 * @author Richard Bair
 */
/**
 * The pdf trailer. This represents a single trailer in the pdf file
 * @author Richard Bair
 * date: May 20, 2004
 */
final class PDFTrailer {
	public static final String KEY_SIZE = "Size";
	public static final String KEY_PREV = "Prev";
	public static final String KEY_ROOT = "Root";
	public static final String KEY_ENCRYPT = "Encrypt";
	public static final String KEY_INFO = "Info";
	public static final String KEY_ID = "ID";
	/**
	 * The offset of the beginning of the xref section, from the beginning of the file
	 */
	private long startXrefOffset = 0;
	/**
	 * Contains various properties for the trailer
	 */
	private Map dictionary;
	
	/**
	 * This encrypted string contains every part of the trailer, from the keyword trailer to the keyword %%EOF
	 * @param encryptedString
	 */
	PDFTrailer(String encryptedString) {
		//parse off the startXrefOffset and the trailer dictionary
		List tokens = PDFParseUtils.parseTokens(encryptedString);
		assert tokens.size() == 5;
		dictionary = (Map)PDFParseUtils.parseToken(tokens.get(1).toString());
		startXrefOffset = Integer.parseInt(tokens.get(3).toString());
	}
	/**
	 * @return Returns the dictionary.
	 */
	Map getDictionary() {
		return dictionary;
	}
	/**
	 * @return Returns the startXrefOffset.
	 */
	long getStartXrefOffset() {
		return startXrefOffset;
	}
}