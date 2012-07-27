/*
 * $Id: LZWDecodeFilter.java 331 2005-02-01 19:05:26Z rbair $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */

package org.jdesktop.jdnc.incubator.rbair.swing.pdf.filter;

/**
 * @author Richard Bair
 */
public class LZWDecodeFilter implements PDFFilter {
	/**
	 * 
	 */
	public LZWDecodeFilter() {
	}

	/* (non-Javadoc)
	 * @see PDFFilter#decode(byte[])
	 */
	public byte[] decode(byte[] input) throws Exception {
		/*
		 * Read OLD_CODE
		 * output OLD_CODE
		 * CHARACTER = OLD_CODE
		 * WHILE there are still input characters DO
		 *     Read NEW_CODE
		 *     IF NEW_CODE is not in the translation table THEN
		 *         STRING = get translation of OLD_CODE
		 *         STRING = STRING+CHARACTER
		 *     ELSE
		 *         STRING = get translation of NEW_CODE
		 *     END of IF
		 *     output STRING
		 *     CHARACTER = first character in STRING
		 *     add OLD_CODE + CHARACTER to the translation table
		 *     OLD_CODE = NEW_CODE
		 * END of WHILE
		 */
		//handle the exceptional cases of null input, or an input byte array that is just too dang small 
		if (input == null || input.length <= 1) {
			return input;
		}
				
		/*
		 * This is the decompression table. The table is defaulted to its maximum size. Each String is located at table[code - 258].
		 * Since on decompression, the table is only accessed by its code, it makes sense to have the strings in an array where the index
		 * in the array indicates the code!
		 */
		String[] table = new String[65535];
		
		//set up my input buffer
		InputBuffer inputBuffer = new InputBuffer(input);
		//create the output buffer
		StringBuffer outputBuffer = new StringBuffer(input.length * 10);
		//create a variable that holds the next code to use in the table
		int nextCode = 258;
		//create a variable that holds the old code (input[i])
		int oldCode = inputBuffer.next(9);
		//if the oldCode is the clear code, read the next input
		if (oldCode == 256) {
			oldCode = inputBuffer.next(9);
		}
		//output the oldCode (note that at this stage, it is a normal byte character)
		outputBuffer.append((char)oldCode);
		//create my character
		char character = (char)oldCode;
		//while there are still input characters
		while (inputBuffer.hasNext()) {
			int newCode = inputBuffer.next(nextCode >= 2048 ? 12 : nextCode >= 1024 ? 11 : nextCode >= 512 ? 10 : 9);
			//if the newCode is the clear table code (256), then reset the nextCode variable and skip the rest of the loop
			if (newCode == 256) {
				nextCode = 258;
			} else {
				String s = "";
				if (newCode >= nextCode) {
					s = oldCode <= 255 ? ("" + (char)oldCode) : table[oldCode - 258];
					s = s + character;
				} else {
					if (newCode <= 255) {
						s = "" + (char)newCode;
					} else {
						s = table[newCode - 258];
					}
				}
				outputBuffer.append(s);
				character = s.charAt(0);
				table[nextCode++ - 258] = "" + (oldCode <= 255 ? "" + (char)oldCode : table[oldCode - 258]) + character;
				oldCode = newCode;
			}
		}
		return outputBuffer.toString().getBytes();
	}
	
	private static final class InputBuffer {
		private byte[] input;
		/**
		 * This variable keeps track of the next bit to be read.
		 * That is, if I am reading 9 bits at a time, for instance, 
		 * and I have read 3 of these, then bitPosition would be 27
		 */
		private int bitPosition = 0;
		
		InputBuffer(byte[] input) {
			assert input != null;
			this.input = input;
		}

		/**
		 * 
		 * @param numbits The number of bits to read where numbits is greater than 8 and less than 16
		 * @return
		 * @throws Exception
		 */
		int next(int numbits) throws Exception {
			//don't worry about an array out of bounds, because java will do it for me
			/*
			 * Read the specified number of bits (numbits) from the input byte array into c.
			 * This is accomplished by:
			 * 1) calculating the index in 'input' from which to get the first byte and
			 * 2) calculating the index in 'input' from which to get the second byte and
			 * 3) getting those bytes as chars (so they are big enough to combine) and
			 * 4) combining those bytes to get a char of the proper length
			 * 
			 * To combine, I need to shift b1 left by however many bits necessary to accomidate
			 * the "carryover" of bits in b2. I also need to shift b2 right by however many
			 * bits necessary to hack off the unwanted portion of b2. Then I need to add the
			 * two numbers together.
			 * 
			 * 5) increment the bitPosition
			 */
			int index = bitPosition / 8;
			char b1 = (char)(((int)input[index]) & 0xFF);
			char b2 = (char)(((int)input[index + 1]) & 0xFF);
			
			int numCarryoverBits = (bitPosition % 8) + 1;
			int mask = (1 << numbits) - 1;
			char c = (char)(((b1 << numCarryoverBits) & mask) + (b2>>(8 - numCarryoverBits)));
			
			bitPosition += numbits;
			
			return c;
		}
		
		/**
		 * Indicates whether there are remaining bits to be read or not. This method makes no gaurantee that
		 * you can actually read X number of bits (since there may only be one bit remaining, but
		 * when you call 'next' you tell it to read 9 bits, for instance).
		 * @return
		 */
		boolean hasNext() {
			return bitPosition < (input.length * 8) - 8;
		}
	}
}
