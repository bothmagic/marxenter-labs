/*
 * $Id: copyright.txt,v 1.3 2004/09/03 22:20:20 bcbeck Exp $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */

package org.jdesktop.swingx.decorator;

import junit.framework.TestCase;
import static org.jdesktop.swingx.decorator.SuperPatternFilter.MODE.*;


public class TestPatternFilter extends TestCase {
    private SuperPatternFilter filter;
    @Override protected void setUp() throws Exception {
        filter = new SuperPatternFilter(0);
    }

    public void testLiteralFind() {
        filter.setFilterStr("ABC", LITERAL_FIND);
        assertTrue(filter.testValue("ABCD"));
        assertFalse(filter.testValue("DCBA"));
    }

    public void testLiteralFindNull() {
        filter.setFilterStr(null, LITERAL_FIND);
        assertTrue(filter.testValue("ABCD"));
    }

    public void testLiteralFindEmpty() {
        filter.setFilterStr("", LITERAL_FIND);
        assertTrue(filter.testValue("ABCD"));
    }

    public void testLiteralMatch() {
        filter.setFilterStr("ABC", LITERAL_MATCH);
        assertTrue(filter.testValue("ABC"));
        assertFalse(filter.testValue("ABCD"));
    }

    public void testLiteralMatchNull() {
        filter.setFilterStr(null, LITERAL_MATCH);
        assertTrue(filter.testValue("ABCD"));
    }

    public void testLiteralMatchEmpty() {
        filter.setFilterStr("", LITERAL_MATCH);
        assertTrue(filter.testValue("ABCD"));
    }

    public void testRegexFind() {
        filter.setFilterStr("A.C", REGEX_FIND);
        assertTrue(filter.testValue("ABCD"));
        assertFalse(filter.testValue("A"));
    }

    public void testRegexMatch() {
        filter.setFilterStr("A.C", REGEX_MATCH);
        assertTrue(filter.testValue("A)C"));
        assertFalse(filter.testValue("ABCD"));
    }
}

