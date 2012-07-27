/*
 * $Id: PDFVersion.java 331 2005-02-01 19:05:26Z rbair $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.jdnc.incubator.rbair.swing.pdf;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Richard Bair
 */
/**
 * Represents a version of the pdf file.
 * @author Richard Bair
 * date: May 20, 2004
 */
final class PDFVersion {
	/**
	 * 
	 */
	public static final PDFVersion VERSION_1_0 = new PDFVersion(1.0);
	/**
	 * 
	 */
	public static final PDFVersion VERSION_1_1 = new PDFVersion(1.1);
	/**
	 * 
	 */
	public static final PDFVersion VERSION_1_2 = new PDFVersion(1.2);
	/**
	 * 
	 */
	public static final PDFVersion VERSION_1_3 = new PDFVersion(1.3);
	/**
	 * 
	 */
	public static final PDFVersion VERSION_1_4 = new PDFVersion(1.4);
	/**
	 * 
	 */
	public static final PDFVersion VERSION_1_5 = new PDFVersion(1.5);
	/**
	 * A map containing all of the instantiated versions.
	 */
	private static Map VERSIONS;
	/**
	 * 
	 */
	private String versionString;
	/**
	 * 
	 */
	private double version;
	/**
	 * 
	 * @param version
	 */
	private PDFVersion(double version) {
		this.version = version;
		versionString = "%PDF-" + version;
		if (VERSIONS == null) {
			VERSIONS = new HashMap();
		}
		VERSIONS.put(new Double(version), this);
	}

	/**
	 * @param versionNumber
	 * @return
	 */
	public static PDFVersion getVersion(double versionNumber) {
		Double v = new Double(versionNumber);
		return (PDFVersion)VERSIONS.get(v);
	}

	public boolean isCompatible(double version) {
		return this.version >= version;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		return obj instanceof PDFVersion ? version == ((PDFVersion)obj).version : false;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return versionString.hashCode();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return versionString;
	}
	/**
	 * @return Returns the version.
	 */
	double getVersion() {
		return version;
	}
	/**
	 * @return Returns the versionString.
	 */
	String getVersionString() {
		return versionString;
	}
}