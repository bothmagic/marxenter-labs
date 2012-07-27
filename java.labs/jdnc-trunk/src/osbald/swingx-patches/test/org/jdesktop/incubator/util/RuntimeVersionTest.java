package org.jdesktop.incubator.util;

/*
 * Created by IntelliJ IDEA.
 * User: Richard Osbaldeston
 * Date: 25-May-2007
 * Time: 15:14:23
 */

import junit.framework.TestCase;

public class RuntimeVersionTest extends TestCase {

    public void testGetFullVersion() {
        RuntimeVersion version = new RuntimeVersion("1.6.0_05");
        assertEquals(6, version.getPlatform());
        assertEquals(5, version.getRelease());
        assertEquals(6, version.getMajor());
        assertEquals(0, version.getMinor());
        assertEquals(5, version.getUpdate());
        assertEquals(1.6f, version.getPlatformVersion());
        assertEquals(1.60005f, version.getFullVersion());
        assertTrue(version.getFullVersion() > 1.6 && version.getFullVersion() < 1.60006);
        assertEquals("1.6.0_05", version.toString());

        version = new RuntimeVersion("1.5.0");
        assertEquals(5, version.getPlatform());
        assertEquals(0, version.getRelease());
        assertEquals(5, version.getMajor());
        assertEquals(0, version.getMinor());
        assertEquals(0, version.getUpdate());
        assertEquals(1.5f, version.getPlatformVersion());
        assertEquals(1.5f, version.getFullVersion());
        assertTrue(version.getFullVersion() == version.getPlatformVersion());
        assertEquals("1.5.0", version.toString());

        version = new RuntimeVersion("1.4.2_13");
        assertEquals(4, version.getPlatform());
        assertEquals(2, version.getRelease());
        assertEquals(4, version.getMajor());
        assertEquals(2, version.getMinor());
        assertEquals(13, version.getUpdate());
        assertEquals(1.4f, version.getPlatformVersion());
        assertEquals(1.40213f, version.getFullVersion());
        assertTrue(version.getPlatformVersion() < version.getFullVersion());
        assertEquals("1.4.2_13", version.toString());

        version = new RuntimeVersion("1.3.1_03-74");
        assertEquals(3, version.getPlatform());
        assertEquals(1, version.getRelease());
        assertEquals(3, version.getMajor());
        assertEquals(1, version.getMinor());
        assertEquals(3, version.getUpdate());
        assertEquals(1.3f, version.getPlatformVersion());
        assertEquals(1.30103f, version.getFullVersion());
        assertEquals("1.3.1_03", version.toString());

        version = new RuntimeVersion("1.1.7A");
        assertEquals(1, version.getPlatform());
        assertEquals(7, version.getRelease());
        assertEquals(1, version.getMajor());
        assertEquals(7, version.getMinor());
        assertEquals(0, version.getUpdate());
        assertEquals(1.1f, version.getPlatformVersion());
        assertEquals(1.107f, version.getFullVersion());
        assertEquals("1.1.7", version.toString());
    }

    public void testTheFuture() {
        RuntimeVersion onePointSeven = new RuntimeVersion("1.7.0");
        assertEquals(7, onePointSeven.getPlatform());
        assertEquals(0, onePointSeven.getRelease());
        assertEquals(7, onePointSeven.getMajor());
        assertEquals(0, onePointSeven.getMinor());
        assertEquals(0, onePointSeven.getUpdate());
        assertEquals(1.7f, onePointSeven.getPlatformVersion());
        assertEquals(1.7f, onePointSeven.getFullVersion());
        assertEquals("1.7.0", onePointSeven.toString());

        RuntimeVersion sevenPointNought = new RuntimeVersion("7.0");
        assertEquals(onePointSeven.getPlatform(), sevenPointNought.getPlatform());
        assertEquals(onePointSeven.getRelease(), sevenPointNought.getRelease());
        assertEquals(onePointSeven.getMajor(), sevenPointNought.getMajor());
        assertEquals(onePointSeven.getMinor(), sevenPointNought.getMinor());
        assertEquals(onePointSeven.getUpdate(), sevenPointNought.getUpdate());
        assertEquals(7f, sevenPointNought.getPlatformVersion());
        assertEquals(7.0f, sevenPointNought.getFullVersion());
        assertEquals("7.0", sevenPointNought.toString());

        RuntimeVersion version = new RuntimeVersion("9.999_01");
        assertEquals(9, version.getPlatform());
        assertEquals(999, version.getRelease());
        assertEquals(9, version.getMajor());
        assertEquals(999, version.getMinor());
        assertEquals(1, version.getUpdate());
        assertEquals(9.999f, version.getPlatformVersion());  // guessing - its what I'd do once 1.x was resigned to history
        assertEquals(9.99901f, version.getFullVersion());
        assertEquals("9.999_01", version.toString());
    }

    public void testForMarketingMadness() {
        try {
            /*
             Takes a licking but keeps on ticking..
             
             Reasoning :- Failing might lead to unfortunate incidents when faced with exotic Java vendor
             implementations. In general use of runtime version specific code should be discouraged..
             But caught between JDK differences lookandfeel bugs and stubborn customer deployments
             its often an ugly fact of client-side life.
             */
            RuntimeVersion version = new RuntimeVersion("");
            version = new RuntimeVersion("...---...");
            version = new RuntimeVersion("Java MVM 8.0²");

            // returned values hardly matter at this point.. what should it report?
            assertEquals(0, version.getPlatform());
            assertEquals(0, version.getRelease());
            assertEquals(0, version.getMajor());
            assertEquals(0, version.getMinor());
            assertEquals(0, version.getUpdate());
            assertEquals(1f, version.getPlatformVersion());
            assertEquals("1.0.0", version.toString());
            version = new RuntimeVersion("");
        } catch (Throwable t) {
            fail("Shouldh've kept going, but didnt? - " + t.getMessage());
        }
    }
}