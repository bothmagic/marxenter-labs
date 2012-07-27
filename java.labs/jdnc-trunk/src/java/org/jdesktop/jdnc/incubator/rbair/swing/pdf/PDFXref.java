/*
 * $Id: PDFXref.java 331 2005-02-01 19:05:26Z rbair $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.jdnc.incubator.rbair.swing.pdf;
import java.io.BufferedReader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * @author Richard Bair
 */
/**
 * 
 * @author Richard Bair
 * date: May 20, 2004
 */
final class PDFXref {
	/**
	 * Contains each and every entry for objects that are in use. The key is the object number, the value is the
	 * PDFXrefEntry.
	 */
	private Map table = new HashMap();
	/**
	 * Contains the PDFXrefEntry objects for those objects that are free.
	 */
	private LinkedList freeEntries = new LinkedList();
	
	/**
	 * Takes a string containing all of the subsections and xref entries for this xref.
	 * The first line will contain the token 'xref', followed by another line containing 
	 * a number (multiple digit number, possibly) representing
	 * the first object number in the xref subsection, followed by a space, followed by another
	 * number representing the number of elements in the subsection, followed by EOL.
	 * @param encodedString
	 */
	PDFXref(String encodedString) {
		BufferedReader br = new BufferedReader(new StringReader(encodedString));
		String subsectionHeading = "";
		try {
			//read off the first line and throw it away
			br.readLine();
			subsectionHeading = br.readLine();
			do {
				String[] chunks = subsectionHeading.split(" ");
				assert chunks.length == 2;
				int nextObjectNumber = Integer.parseInt(chunks[0]);
				int numEntries = Integer.parseInt(chunks[1]);
				for (int i=0; i<numEntries; i++) {
					PDFXrefEntry entry = new PDFXrefEntry(br.readLine(), nextObjectNumber++);
					if (entry.isInUse()) {
						table.put(new PDFIndirectReference(entry.getObjectNumber(), entry.getGenerationNumber()), entry);
					} else {
						freeEntries.add(entry);
					}
				}
			} while ( (subsectionHeading = br.readLine()) != null);
		} catch (Exception e) {
			System.err.println("Failed to parse the pdf file");
			e.printStackTrace();
		}
	}
	
	/**
	 * @return Returns the freeEntries.
	 */
	LinkedList getFreeEntries() {
		return freeEntries;
	}
	/**
	 * @return Returns the table.
	 */
	Map getTable() {
		return table;
	}
}