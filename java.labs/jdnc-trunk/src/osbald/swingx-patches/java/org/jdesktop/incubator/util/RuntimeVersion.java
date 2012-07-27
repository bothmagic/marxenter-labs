package org.jdesktop.incubator.util;

import java.util.StringTokenizer;

/**
 * Runtime version checker that doesn't depend on predefined enumerations.
 * Provides finer grained access to version fields as numerical values to allow for
 * more natural checks against version numbers.
 * <p/>
 * Version numbers are broken down as [&lt;prefix&gt;.]&lt;major&gt;.&lt;minor&gt;_&lt;update&gt;.
 * e.g. 1.6.0_05 would be presented as major=6, minor=0, update=5 (and release=5).
 * <p/>
 * The 1.x prefix is preserved by various methods if that's how the runtime reported it.
 * But I've also added support for the dropping of the old prefix (pure guesswork).
 * <p/>
 * For example <code>RuntimeVersion.getInstance().getPlatformVersion() > 1.4</code> or
 * <code>RuntimeVersion.getInstance().getPlatfrom() == 5</code>.
 */

/*
 * Backport of RuntimeVersion15 that ought to work with older jvms.
 * Created by IntelliJ IDEA.
 * User: Richard Osbaldeston
 * Date: 04-Apr-2008
 * Time: 15:30:01
 */

public class RuntimeVersion {
    private int prefix = 1;
    private int major = 0;
    private int minor;
    private int update;

    private static RuntimeVersion instance = new RuntimeVersion();
    public static RuntimeVersion getInstance() {
        return instance;
    }

    RuntimeVersion() {
        this(System.getProperty("java.version", ""));
    }

    public RuntimeVersion(String versionString) {
        parse(versionString);
    }

    // java 1.3 (1.1?) friendly version - i.e. without regex
    void parse(String versionString) {
        try {
            StringTokenizer tokens = new StringTokenizer(versionString, "._-AaBbUuVv ");
            for (int i = 0; tokens.hasMoreTokens(); i++) {
                String token = tokens.nextToken();
                if (token.length() > 0) {
                    if (i == 0) {
                        prefix = Integer.valueOf(token);
                        // how much longer will the 1.x numbering scheme last? why?
                        if (prefix > 1) {
                            major = prefix;
                            i++;
                        }
                    } else if (i == 1) {
                        major = Math.max(Integer.valueOf(token), 1);
                    } else if (i == 2) {
                        minor = Integer.valueOf(token);
                    } else if (i == 3) {
                        update = Integer.valueOf(token);
                    }
                }
            }
        } catch (NumberFormatException nfe) {
            System.err.println("Failed to parse java.version '" + versionString + "'");
            nfe.printStackTrace(System.err);
        }
    }

    /**
     * Gets the runtime prefix. A hangover from the persisting 1.x scheme.
     *
     * @return first digit of the platform version such as 1 for 1.4.2
     */
    public int getPrefix() {
        return prefix;
    }

    /**
     * Gets the runtime platform version, such as 4 for 1.4.2.
     *
     * @return major release version
     */
    public int getPlatform() {
        return major;
    }

    /**
     * Gets the runtime platform version, such as 4 for 1.4.2.
     *
     * @return major release version
     */
    // alias of getPlatform() couldn't decide on right name but as we've got a minor number..
    public int getMajor() {
        return major;
    }

    /**
     * Get the runtime minor revision, such as 2 for 1.4.2. These stopped being incremented after 5.0,
     * but are still used in java version strings for some unfathomable reason.
     *
     * @return runtime minor release version
     * @see #getRelease()
     */
    public int getMinor() {
        return minor;
    }

    /**
     * Gets the Runtime update version, such as 3 for "1.6.0_03".
     *
     * @return update version (if any)
     */
    public int getUpdate() {
        return update;
    }

    /**
     * Get the major version for this runtime, such as 1.5 for "1.5.0_13".
     *
     * @return major platform version of the runtime expressed as a numerical value
     */
    public float getPlatformVersion() {
        if (getPrefix() == 1) {
            return Float.valueOf("1." + getMajor());
        } else {
            // adding the minor here isn't strictly correct, but adding zero wont hurt and who really knows? 
            return Float.valueOf(getMajor() + "." + getMinor());
        }
    }

    /**
     * Synthetic property that returns the full version string as a numerical value.
     * The minor and update versions become decimal places after the platform version.
     * e.g. "1.4.2" returns 1.402 (minor=02) while "1.6.0_05" becomes 1.60005
	 * (minor=00, update=05).  This allows for the finest grained version 
	 * checks which can distinguish between minor and update releases.
	 * <p/>
     * Note update & minor were not really an either-or option here as 1.3.x and 1.4.x 
	 * often used both. See getRelease() which doesn't discriminate between these
	 * differences.
     *
     * @return full version string expressed as a numerical value
     */
    //PENDING would come undone if Java made it to version 10??!
    public float getFullVersion() {
        if (getPrefix() == 1) {
            return Float.valueOf("1." + getMajor() + zeroPadded(getMinor()) + zeroPadded(getUpdate()));
        } else {
            return Float.valueOf(getMajor() + "." + zeroPadded(getMinor()) + zeroPadded(getUpdate()));
        }
    }

    /**
     * From Java 5.0 Sun dropped the minor revision releases in favour of updates.
     * This returns another synthetic property that reports either the minor or update
     * version depending on which was specified. Expected getPlatform() to be paired 
	 * with getRelease() when necessary.
     * <p/>
     * Warning! minor version always supersedes update and 1.3.x and 1.4.x often used
     * both. e.g. 1.6.0_05 becomes platform=6 and release=5 and 1.4.2 becomes 
	 * platform=4 and release=2, but then so would 1.4.2_09! (but update=9).
     *
     * @return The minor release (if set) or update version
     */
    public int getRelease() {
        if (getMinor() > 0) {
            return getMinor();
        } else {
            return getUpdate();
        }
    }

    String zeroPadded(int value) {
        if (value >= 0 && value < 10) {
            return "0" + value;
        } else {
            return String.valueOf(value);
        }
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        if (getPrefix() == 1) {
            buffer.append(getPrefix()).append('.');
        }
        buffer.append(getMajor()).append('.').append(getMinor());
        if (getUpdate() > 0) {
            buffer.append('_').append(zeroPadded(getUpdate()));
        }
        return buffer.toString();
    }
}