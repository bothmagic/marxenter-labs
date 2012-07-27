/*
 * Created on 08.03.2010
 *
 */
package org.jdesktop.swingx.calendar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.logging.Logger;

import org.jdesktop.swingx.InteractiveTestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class DateFormatIssues extends InteractiveTestCase {
    @SuppressWarnings("unused")
    private static final Logger LOG = Logger.getLogger(DateFormatIssues.class
            .getName());

    @Test(expected = ParseException.class)
    public void testLenientYear() throws ParseException {
        // set the format to use as a constructor argument
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        LOG.info("parsed " + dateFormat.parse("20099-03-01"));
    }
}
