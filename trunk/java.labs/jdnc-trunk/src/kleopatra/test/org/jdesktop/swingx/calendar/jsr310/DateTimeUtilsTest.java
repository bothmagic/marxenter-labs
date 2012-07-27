/*
 * Created on 12.01.2009
 *
 */
package org.jdesktop.swingx.calendar.jsr310;

import java.util.Locale;
import java.util.logging.Logger;

import javax.time.calendar.Clock;
import javax.time.calendar.DayOfWeek;
import javax.time.calendar.LocalDateTime;
import javax.time.calendar.LocalTime;
import javax.time.calendar.Period;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class DateTimeUtilsTest extends TestCase {
    @SuppressWarnings("unused")
    private static final Logger LOG = Logger.getLogger(DateTimeUtilsTest.class
            .getName());
    private LocalDateTime dateTime;
    
//    @Test
//    public void testWeekyear53() {
//        LocalDateTime dateTime = Clock.system().currentDateTime();
//        for (int i = 0; i < 100; i++) {
//           dateTime = dateTime.plusYears(1); 
//           Weekyear wy = Weekyear.weekyear(dateTime);
//           LOG.info(" " + dateTime.getYear() + " weeks: " + wy.lengthInWeeks());
////           assertEquals("in year " + dateTime.getYear(), 53, wy.lengthInWeeks());
//        }
//    }
    
    @Override
    @Before
    public void setUp() {
        dateTime = Clock.systemDefaultZone().dateTime().withDayOfMonth(5).withHourOfDay(5);
    }
    
    @Test
    public void testStartOfMonthCheck() {
        LocalDateTime startOfMonth = DateTimeUtils.startOfMonth(dateTime);
        assertTrue(DateTimeUtils.isStartOfMonth(startOfMonth));
    }
    
    
    @Test
    public void testStartOfMonth() {
        LocalDateTime startOfMonth = DateTimeUtils.startOfMonth(dateTime);
        assertSame(LocalTime.MIDNIGHT, startOfMonth.toLocalTime());
        assertSame(dateTime.getMonthOfYear(), startOfMonth.getMonthOfYear());
        assertEquals(1, startOfMonth.getDayOfMonth());
    }
    
    @Test
    public void testEndOfMonth() {
        LocalDateTime endOfMonth = DateTimeUtils.endOfMonth(dateTime);
        assertTrue(DateTimeUtils.isStartOfMonth(endOfMonth.plusNanos(1L)));
        assertSame(dateTime.getMonthOfYear(), endOfMonth.getMonthOfYear());
    }
    
    @Test
    public void testEndOfMonthUnchanged() {
        LocalDateTime endOfMonth = DateTimeUtils.endOfMonth(dateTime);
        LocalDateTime endOfMonthAgain = DateTimeUtils.endOfMonth(endOfMonth);
        assertSame(endOfMonth.getMonthOfYear(), endOfMonthAgain.getMonthOfYear());
    }
    
    @Test
    public void testStartOfWeek() {
        Locale locale = Locale.getDefault();
        DayOfWeek firstDayOfWeek = DateTimeUtils.getFirstDayOfWeek(locale);
        if (dateTime.getDayOfWeek() == firstDayOfWeek) {
            dateTime = dateTime.plus(Period.ofDays(1));
        }
        LocalDateTime first = DateTimeUtils.startOfWeek(dateTime);
        assertSame(LocalTime.MIDNIGHT, first.toLocalTime());
        assertSame(firstDayOfWeek, first.getDayOfWeek());
    }

    @Test
    public void testStartOfWeekDayMonday() {
        DayOfWeek firstGerman = DateTimeUtils.getFirstDayOfWeek(Locale.GERMAN);
        assertSame(DayOfWeek.MONDAY, firstGerman);
    }

    @Test
    public void testStartOfWeekDaySunday() {
        DayOfWeek firstUS = DateTimeUtils.getFirstDayOfWeek(Locale.US);
        assertSame(DayOfWeek.SUNDAY, firstUS);
    }
}
