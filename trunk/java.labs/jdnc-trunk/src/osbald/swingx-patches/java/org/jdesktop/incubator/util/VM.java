package org.jdesktop.incubator.util;

/* ..Just a thought about how to make RuntimeVersion.getInstance().getPlatformVersion()
 a little less verbose: VM.current().getPlatfrom()>5 */

public final class VM {
    private static VM INSTANCE = new VM();
    private RuntimeVersion runtimeVersion = new RuntimeVersion();

    private VM() {
    }

    public static VM getInstance() {
        return INSTANCE;
    }

	// alias
    public static VM current() {
        return getInstance();
    }
	
	public static int getPlatform() {
        return getInstance().getRuntimeVersion().getPlatform();
    }

    public static int getRelease() {
        return getInstance().getRuntimeVersion().getRelease();
    }

    public static float getFullVersion() {
        return getInstance().getRuntimeVersion().getFullVersion();
    }

    public RuntimeVersion getRuntimeVersion() {
        return runtimeVersion;
    }

    //TODO ..thinks what other things might it be handy to ask of a VM?

    public static String getVendor() {
        return System.getProperty("java.vendor", "unknown");
    }

    public static boolean isSunImplementation() {
        return getVendor().indexOf("Sun Microsystems") >= 0;
    }
}

