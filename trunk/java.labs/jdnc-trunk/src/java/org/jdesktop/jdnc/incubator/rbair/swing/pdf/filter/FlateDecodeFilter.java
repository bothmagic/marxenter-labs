/*
 * $Id: FlateDecodeFilter.java 331 2005-02-01 19:05:26Z rbair $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.jdnc.incubator.rbair.swing.pdf.filter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.zip.Inflater;

/**
 * @author Richard Bair
 */
public class FlateDecodeFilter implements PDFFilter {
	private Inflater helper;
	/**
	 * 
	 */
	public FlateDecodeFilter() {
		helper = new Inflater();
	}

	/* (non-Javadoc)
	 * @see PDFFilter#decode(byte[])
	 */
	public byte[] decode(byte[] input) throws Exception {
		helper.setInput(input);
		byte[] data = new byte[4096];
		List buffer = new ArrayList();
		int read = 0;
		int totalRead = 0;
		while ((read = helper.inflate(data)) > 0) {
			buffer.add(data);
			data = new byte[4096];
			totalRead += read;
		}
		
		data = new byte[totalRead];
		int index = 0;
		Iterator itr = buffer.iterator();
		while (itr.hasNext()) {
			byte[] b = (byte[])itr.next();
			for (int i=0; i<b.length; i++) {
				if (index < totalRead) {
					data[index++] = b[i];
				}
			}
		}
		return data;
	}
}
