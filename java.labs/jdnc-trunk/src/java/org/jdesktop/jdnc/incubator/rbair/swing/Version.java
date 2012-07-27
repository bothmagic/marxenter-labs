/*
 * $Id: Version.java 21 2004-09-06 18:37:36Z rbair $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.jdnc.incubator.rbair.swing;

/**
 * Represents a version number.  This is particularly useful for denoting the
 * version of an application, or a plugin, or any other such versioned piece
 * of code.
 * 
 * <br>This class is based on the comments made by Mr. Phil Brown, and can be found
 * in their entirety at <a href="http://www.howtodothings.com/showarticle.asp?article=156"/>
 * <br>Following is an excerpt from that article.
 * <br>
 * <p>Since the earliest computer programs were deployed, developers wanted to have 
 * some way of identifying which version a customer was running; after all, it’s obviously 
 * worthwhile to know if a bug they’re reporting has already been fixed. Fairly quickly 
 * (and obviously) a numerical system evolved, simply incrementing the version number each 
 * time a release was made. As the frequency of releases increased, developers wanted to 
 * have some way of distinguishing a major new release compared with an interim one: the 
 * concept of major and minor versions of a program was introduced - going from V1.12 to 
 * V2.0 conveyed a lot more (even to lay users) than going from V12 to V13. For many years 
 * the major and minor version was adequate and commonplace, but as programs grew larger and 
 * complex requiring more frequent bug-fixes a tertiary number was tacked on to the end of 
 * the version to indicate a "release". All releases with the same major and minor version 
 * numbers should have an identical feature list, but should include cumulative bug fixes and 
 * other enhancements without adding new features as such. Latterly, it has become good 
 * practice to append the build number to the release resulting in a complete version number 
 * with four components, X.Y.Z.B, where X represents the major version, Y the minor version, 
 * Z the release number and B the build number. Note that although the minor and release 
 * versions can be reset to zero (with major and minor releases), the build number is never 
 * reset and therefore always increases with each new release (although not necessarily 
 * strictly incremental; it is very possible to have internal builds within a company that 
 * are not released to clients). Listing 1 summarises when each version number component 
 * should be incremented.</p><br>
 * 
 * <table border=1><tr><td>Component</td><td>Describes</td><td>When To Increment</td></tr>
 * <tr><td>X</td><td>Major</td><td>After significant changes have been made to the application</td></tr>
 * <tr><td>Y</td><td>Minor</td><td>After new features have been added to the application</td></tr>
 * <tr><td>Z</td><td>Release</td><td>Every time a minor release is made available with bug fixes</td></tr>
 * <tr><td>B</td><td>Build</td><td>Every time the build process completes</td></tr></table>
 * Listing 1 - Components of version number X.Y.Z.B<br>
 * @author Richard Bair
 */
public final class Version implements Comparable {
	/**
	 * major part of the version number.  For example, the 3 in the version number 3.1.4.2532
	 */
	private int majorVersion;
	/**
	 * minor part of the version number.  For example, the 1 in the version number 3.1.4.2532
	 */
	private int minorVersion;
	/**
	 * release part of the version number.  For example, the 4 in the version number 3.1.4.2532
	 */
	private int release;
	/**
	 * the build part of the version number.  For example, the 2532 in the version number 3.1.4.2532
	 */
	private int build;
	/**
	 * cached hash code
	 */
	private int cachedHash;
	/**
	 * cached string value
	 */
	private String cachedAsString = "";
	
	/**
	 * Create a new version object
	 * @param majorVersion
	 * @param minorVersion
	 * @param release
	 * @param build
	 */
	public Version(int majorVersion, int minorVersion, int release, int build) {
		this.majorVersion = majorVersion;
		this.minorVersion = minorVersion;
		this.release = release;
		this.build = build;
		cachedAsString = majorVersion + "." + minorVersion + "." + release + "." + build;
		cachedHash = (majorVersion * 1000) + (minorVersion * 100) + (release * 10) + build;
	}
	
	/**
	 * Get the build number
	 * @return int
	 */
	public int getBuild() {
		return build;
	}

	/**
	 * get the major version number
	 * @return int
	 */
	public int getMajorVersion() {
		return majorVersion;
	}

	/**
	 * get the minor version number
	 * @return int
	 */
	public int getMinorVersion() {
		return minorVersion;
	}
	
	/**
	 * get the release number
	 * @return int
	 */
	public int getRelease() {
		return release;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		if (obj instanceof Version) {
			Version v = (Version)obj;
			return majorVersion == v.majorVersion && minorVersion == v.minorVersion 
							&& release == v.release && build == v.build;
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return cachedHash;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return cachedAsString;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 * 
	 * First compare the majorVersion, then the minorVersion, then the release, then the build.
	 * If all of these are the same then the Versions are equal, and it returns 0.
	 */
	public int compareTo(Object o) {
		if (o == null) {
			throw new NullPointerException("Null pointer in Version.compareTo");
		}
		
		if (o instanceof Version) {
			Version v = (Version)o;
			
			//compare major versions
			if (majorVersion < v.majorVersion) {
				return -1;
			} else if (majorVersion > v.majorVersion) {
				return 1;
			}
			
			//compare minor versions
			if (minorVersion < v.minorVersion) {
				return -1;
			} else if (minorVersion > v.minorVersion) {
				return 1;
			}
			
			//compare releases
			if (release < v.release) {
				return -1;
			} else if (release > v.release) {
				return 1;
			}
			
			//compare builds
			if (build < v.build) {
				return -1;
			} else if (build > v.build) {
				return 1;
			}
			return 0;
		}
		return 0;
	}

	/**
	 * TODO decoding doesn't work
	 * @param versionString
	 * @return
	 */
	public static Version decode(String versionString) {
		return new Version(1, 1, 1, 1);
	}

}
