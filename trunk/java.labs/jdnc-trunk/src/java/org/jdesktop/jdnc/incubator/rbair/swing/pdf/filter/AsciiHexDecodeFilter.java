/*
 * $Id: AsciiHexDecodeFilter.java 331 2005-02-01 19:05:26Z rbair $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.jdnc.incubator.rbair.swing.pdf.filter;

/**
 * @author Richard Bair
 */
public class AsciiHexDecodeFilter implements PDFFilter {
	/**
	 * 
	 */
	public AsciiHexDecodeFilter() {
	}

	/* (non-Javadoc)
	 * @see PDFFilter#decode(byte[])
	 */
	public byte[] decode(byte[] input) throws Exception {
		//remove any and all whitespace from the input, and the close delimiter
		int shrinkAmount = 0;
		for (int i=0; i<input.length; i++) {
			if (input[i] == ' ' || input[i] == '\n' || input[i] == '\r' || input[i] == '\f' || input[i] == '\t' || input[i] == '\0') {
				shrinkAmount++;
			} else if (input[i] == '>') {
				//found the EOD marker, so quit
				shrinkAmount++;
				break;
			} else {
				input[i - shrinkAmount] = input[i];
			}
		}
		int length = input.length - shrinkAmount;
		
		/*
		 * Wow, clever code. Take the length of the string minus 2 for the < and >, + 1 if
		 * the length is odd, divide by two. That will be the number of bytes this hexstring represents.
		 */
		int dataLength = (length - 2 + (length % 2)) / 2;
		
		//convert the hex string into a series of bytes
		byte[] data = new byte[dataLength];
		for (int i=0; i<data.length; i++) {
			int offset = (i * 2) + 1;
			char hi = (char)input[offset];
			char lo = (char)input[offset + 1] == '>' ? 0 : (char)input[offset + 1];
			byte hiValue = (byte)((hi >= 48 && hi <= 57 ? hi - 48 : (hi >= 65 && hi <= 70 ? hi - 55 : hi - 87)) * 16);
			byte loValue = (byte)(lo >= 48 && lo <= 57 ? lo - 48 : (lo >= 65 && lo <= 70 ? lo - 55 : lo - 87));
			data[i] = (byte)(hiValue + loValue);
		}
		
		return data;
	}
}
