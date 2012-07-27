/*
 * $Id: Ascii85DecodeFilter.java 331 2005-02-01 19:05:26Z rbair $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.jdnc.incubator.rbair.swing.pdf.filter;

/**
 * @author Richard Bair
 */
public class Ascii85DecodeFilter implements PDFFilter {
	private static final long EIGHTY_FIVE_FOURTH = (long)Math.pow(85, 4);
	private static final long EIGHTY_FIVE_CUBED = (long)Math.pow(85, 3);
	private static final long EIGHTY_FIVE_SQUARED = (long)Math.pow(85, 2);
	/**
	 * 
	 */
	public Ascii85DecodeFilter() {
	}

	/* (non-Javadoc)
	 * @see PDFFilter#decode(byte[])
	 */
	public byte[] decode(byte[] input) throws Exception {
		/*
		 * Calculate the size of the finished array. Do this by totalling up the number of 'z' character occurances, and normal ascii characters.
		 * Take the number of z's, mutliple by 4, and then add the number of ascii characters / 4 (dealing with those stupid remainders,
		 * of course). For the sake of performance, this task has been multiplexed with the removal of the whitespace.
		 */
		int numZs = 0;
		//remove any and all whitespace from the input
		int shrinkAmount = 0;
		for (int i=0; i<input.length; i++) {
			//if I run in to the EOD sequence, make the input shrink by 2 more, and break out of this loop (no point in continuing!)
			if (input[i] == '~' && input.length > i+1 && input[i+1] == '>') {
				shrinkAmount += 2;
				break;
			} else if (input[i] == ' ' || input[i] == '\n' || input[i] == '\r' || input[i] == '\f' || input[i] == '\t' || input[i] == '\0') {
				shrinkAmount++;
			} else {
				//count the number of z's
				numZs += input[i] == 'z' ? 1 : 0;
				//move the value up by shrinkAmount
				input[i - shrinkAmount] = input[i];
			}
		}
		int length = input.length - shrinkAmount;
		int remainder = (length - numZs) % 5;
		int dataLength = (numZs * 4) + ((length - numZs - remainder)/5*4);
		dataLength += (remainder == 0 ? 0 : remainder - 1);
		byte[] data = new byte[dataLength];
		/*
		 * Read from the input stream until one of 2 things happen:
		 * 1) we get an error
		 * 2) we reach the end
		 * 
		 * Note that the EOD sequence has been removed (inasmuch as the input byte array must contain no data after it anyway)
		 */
		int index = 0;
		int dataIndex = 0;
		while (index < length) {
			if (input[index] == 'z') {
				//special marker meaning 4 0's
				data[dataIndex++] = 0;
				data[dataIndex++] = 0;
				data[dataIndex++] = 0;
				data[dataIndex++] = 0;
				index ++;
			} else {
				//read 5 ascii characters and convert them into 4 bytes. Save the 4 bytes.
				//if there are NOT 5 characters left to be read, then handle the odd case
				if (length - index >= 5) {
					char c1 = (char)(input[index++] - 33);
					char c2 = (char)(input[index++] - 33);
					char c3 = (char)(input[index++] - 33);
					char c4 = (char)(input[index++] - 33);
					char c5 = (char)(input[index++] - 33);
					long total = (c1 * EIGHTY_FIVE_FOURTH) + (c2 * EIGHTY_FIVE_CUBED) + (c3 * EIGHTY_FIVE_SQUARED) + (c4 * 85) + c5;
					char b4 = (char)(total % 256);
					total = (total - b4)/256;
					char b3 = (char)(total % 256);
					total = (total - b3)/256;
					char b2 = (char)(total % 256);
					total = (total - b2)/256;
					char b1 = (char)total;
					data[dataIndex++] = (byte)(b1 & 0xFF);
					data[dataIndex++] = (byte)(b2 & 0xFF);
					data[dataIndex++] = (byte)(b3 & 0xFF);
					data[dataIndex++] = (byte)(b4 & 0xFF);
				} else {
					//odd case
					System.out.println("The Odd Case");
					/*
					 * Assume that all remaining c values (after values run out) are zeros. Then, compute as usual. Then,
					 * compute as normal. However, keep only n-1 bytes (where n is the number of chars actually read).
					 * Then, as a matter of experimental necessity, add 1 to the highest b value being kept. 
					 */
					int numCs = length - index;
					char c1 = (char)(index >= length ? 0 : input[index++] - 33);
					char c2 = (char)(index >= length ? 0 : input[index++] - 33);
					char c3 = (char)(index >= length ? 0 : input[index++] - 33);
					char c4 = (char)(index >= length ? 0 : input[index++] - 33);
					char c5 = (char)(index >= length ? 0 : input[index++] - 33);
					long total = (c1 * EIGHTY_FIVE_FOURTH) + (c2 * EIGHTY_FIVE_CUBED) + (c3 * EIGHTY_FIVE_SQUARED) + (c4 * 85) + c5;
					char b4 = (char)(total % 256);
					total = (total - b4)/256;
					char b3 = (char)(total % 256);
					total = (total - b3)/256;
					char b2 = (char)(total % 256);
					total = (total - b2)/256;
					char b1 = (char)total;
					switch (numCs) {
						case 2:
							b1++;
							data[dataIndex++] = (byte)(b1 & 0xFF);
							break;
						case 3:
							b2++;
							data[dataIndex++] = (byte)(b1 & 0xFF);
							data[dataIndex++] = (byte)(b2 & 0xFF);
							break;
						case 4:
							b3++;
							data[dataIndex++] = (byte)(b1 & 0xFF);
							data[dataIndex++] = (byte)(b2 & 0xFF);
							data[dataIndex++] = (byte)(b3 & 0xFF);
							break;
					}
				}
			}
		}
		return data;
	}
}
