/**
 * Copyright 2010 Cuprak Enterprise LLC.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.jdesktop.swingx;

import junit.framework.Assert;
import org.junit.Test;

/**
 * Verifies that the LongFieldDocument works as expected.
 */
public class    LongFieldDocumentTest {

    @Test
    public void testIsValidLong() {
         System.out.println(Long.MIN_VALUE);
        LongFieldDocument lfd = new LongFieldDocument();
        Assert.assertTrue("Number should have exceeded the maximum.",lfd.isValidLong("9823372036854775807"));
        Assert.assertTrue("Number should be less than the minimum.",lfd.isValidLong("-9223372036854775809"));
        Assert.assertFalse("Number should be less than the maximum.",lfd.isValidLong("0"));
        Assert.assertFalse("Ten is a valid long.",lfd.isValidLong("10"));
        Assert.assertTrue("Negative ten is a valid long.",lfd.isValidLong("-10"));
    }

    @Test
    public void testDocument() {
        LongFieldDocument lfd = new LongFieldDocument();
        lfd.setMaximumValue(Long.MAX_VALUE);
        Assert.assertTrue("Number should have exceeded the maximum.",lfd.checkMax("9823372036854775807"));
        Assert.assertFalse("Number should NOT have exceeded the maximum.",lfd.checkMax("9123372036854775807"));
        Assert.assertFalse("Number should have matched the maximum.",lfd.checkMax("9223372036854775807"));
        Assert.assertFalse("Number should be less than the maximum.",lfd.checkMax("-19223372036854775807"));
        lfd.setMaximumValue(0l);
        Assert.assertFalse("0 == 0",lfd.checkMax("0"));
        lfd.setMaximumValue(Long.MAX_VALUE);
        lfd.setMaximumEqualTo(false);
        Assert.assertTrue("Equals to should have failed it.",lfd.checkMax("9223372036854775807"));
        lfd.setMaximumEqualTo(true);
        // Less than zero comparisons
        lfd.setMaximumValue(-1l);
        Assert.assertFalse("-2 is less than -1",lfd.checkMax("-2"));
        Assert.assertFalse("-1 is less than or equal to -1",lfd.checkMax("-1"));
    }

    @Test
    public void testInsert() throws Exception {
        LongFieldDocument lfd = new LongFieldDocument();
        lfd.insertString(0,"abc",null);
        Assert.assertTrue("No text should have been inserted: " + lfd.getText(0,lfd.getLength()),(lfd.getText(0,lfd.getLength()).length()==0));
        lfd.insertString(0,"1",null);
        Assert.assertTrue("No text should have been inserted: " + lfd.getText(0,lfd.getLength()),lfd.getText(0,lfd.getLength()).equals("1"));
    }

}
