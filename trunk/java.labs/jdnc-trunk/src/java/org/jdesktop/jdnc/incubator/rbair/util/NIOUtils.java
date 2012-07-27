/*
 * @(#)NIOUtils.java.java
 * 
 * Copyright 2003 Richard Bair. All rights reserved. SHOPLOGIC
 * PROPRIETARY/CONFIDENTIAL. Unauthorized use is prohibited. All authorizations
 * for use must be in written form.
 */
package org.jdesktop.jdnc.incubator.rbair.util;
import java.io.IOException;
import java.nio.ByteBuffer;
/**
 * @author Richard Bair
 */
public class NIOUtils {
	/**
	 *  
	 */
	private NIOUtils() {
	}
	/**
	 * This method will cruise through the given channel starting at the current
	 * channel position until it finds the given sequence of bytes (find). It
	 * will then return the beginning position in the channel of the first byte
	 * of (find). This method returns -1 if it was not found.
	 * 
	 * @param buffer
	 * @param channel
	 * @param find
	 * @return
	 */
	public static long locateStartPositionBackward(ByteBuffer buffer, byte[] find) throws IOException {
		int originalPosition = buffer.position();
		int startingPosition = (originalPosition + find.length) > buffer.limit() ? buffer.limit() - find.length : originalPosition;
		for (int i=startingPosition; i>=0; i--) {
			//check to see if they match
			boolean found = true;
			for (int j=0; j<find.length; j++) {
				if (buffer.get(i + j) != find[j]) {
					found = false;
					break;
				}
			}
			if (found) {
				buffer.position(originalPosition);
				return i;
			}
		}
		return -1;
	}
	
	public static long locateEndPositionForward(ByteBuffer buffer, byte[] find) {
		int originalPosition = buffer.position();
		for (int i=originalPosition; i<buffer.limit() - find.length; i++) {
			//check to see if they match
			boolean found = true;
			for (int j=0; j<find.length; j++) {
				if (buffer.get(i + j) != find[j]) {
					found = false;
					break;
				}
			}
			if (found) {
				buffer.position(originalPosition);
				return i + find.length;
			}
		}
		return -1;
	}
}