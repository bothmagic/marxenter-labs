/*
 * $Id: AbstractTestDate310SelectionModel.java 3339 2011-02-04 17:03:23Z kleopatra $
 *
 * Copyright 2006 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 *
 */
package org.jdesktop.swingx.calendar.jsr310;

import java.util.Locale;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.logging.Logger;

import javax.time.calendar.Clock;
import javax.time.calendar.LocalDateTime;
import javax.time.calendar.Period;
import javax.time.calendar.TimeZone;
import javax.time.calendar.ZoneOffset;

import junit.framework.TestCase;

import org.jdesktop.swingx.calendar.jsr310.Date310SelectionEvent.EventType;
import org.jdesktop.swingx.calendar.jsr310.Date310SelectionModel.SelectionMode;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;


/**
 * Contains test for functionality implemeted on the AbstractDateXX level. <p>
 * 
 * NOTE: the "Test" name part must not be postFixed to not be included into
 * the auto-run during a build. For local convenience, a default model 
 * if created anyway, subclasses must be sure to instantiate the model they
 * actually want to test.
 * 
 * @author Jeanette Winzenburg
 */
@RunWith(JUnit4.class)
public abstract class AbstractTestDate310SelectionModel extends TestCase {
    @SuppressWarnings("unused")
    private static final Logger LOG = Logger
            .getLogger(AbstractTestDate310SelectionModel.class.getName());

    protected Date310SelectionModel model;
    // pre-defined dates - initialized in setUpCalendar
    protected LocalDateTime today;
    protected LocalDateTime tomorrow;
    @SuppressWarnings("unused")
    protected LocalDateTime afterTomorrow;
    protected LocalDateTime yesterday;
    // the calendar to use, its date is initialized with the today-field in setUpCalendar
//    protected Calendar calendar;
    private Clock clock;

    
    
    /**
     * Issue #808-swingx: setLowerBound doesn't clear selection after.
     * Not fire if upper bound set to same
     */
    @Test
    public void testSetLowerBoundSameNotFire() {
        LOG.info("" + model + today);
        model.setLowerBound(today);
        Date310SelectionReport report = new Date310SelectionReport(model);
        model.setLowerBound(today);
        assertEquals("same bound, no event fired", 0, report.getEventCount());
    }

    /**
     * Issue #808-swingx: setLowerBound doesn't clear selection after.
     * Fire if upper bound is removed.
     */
    @Test
    public void testSetLowerBoundFireRemove() {
        model.setLowerBound(today);
        Date310SelectionReport report = new Date310SelectionReport(model);
        model.setLowerBound(null);
        assertEquals("bound changed, event must be fired", 1, report.getEventCount(EventType.LOWER_BOUND_CHANGED));
    }
    
    /**
     * Issue #808-swingx: setLowerBound doesn't clear selection after.
     * Fire if upper bound is set.
     */
    @Test
    public void testSetLowerBoundFireSet() {
        Date310SelectionReport report = new Date310SelectionReport(model);
        model.setLowerBound(today);
        assertEquals("bound changed, event must be fired", 1, report.getEventCount(EventType.LOWER_BOUND_CHANGED));
    }
    
    /**
     * Issue #808-swingx: setUpperBound doesn't clear selection after.
     * Not fire if upper bound set to same
     */
    @Test
    public void testSetUpperBoundSameNotFire() {
        model.setUpperBound(today);
        Date310SelectionReport report = new Date310SelectionReport(model);
        model.setUpperBound(today);
        assertEquals("same bound, no event fired", 0, report.getEventCount());
    }

    /**
     * Issue #808-swingx: setUpperBound doesn't clear selection after.
     * Fire if upper bound is removed.
     */
    @Test
    public void testSetUpperBoundFireRemove() {
        model.setUpperBound(today);
        Date310SelectionReport report = new Date310SelectionReport(model);
        model.setUpperBound(null);
        assertEquals("bound changed, event must be fired", 1, report.getEventCount(EventType.UPPER_BOUND_CHANGED));
    }
    
    /**
     * Issue #808-swingx: setUpperBound doesn't clear selection after.
     * Fire if upper bound is set.
     */
    @Test
    public void testSetUpperBoundFireSet() {
        Date310SelectionReport report = new Date310SelectionReport(model);
        model.setUpperBound(today);
        assertEquals("bound changed, event must be fired", 1, report.getEventCount(EventType.UPPER_BOUND_CHANGED));
    }
    
    /**
     * Issue #808-swingx: setUpperBound doesn't clear selection after.
     *  NPE on removing upper bound when there is a selection.
     */
    @Test
    public void testSetUpperBoundNPE() {
        model.setUpperBound(today);
        model.setSelectionInterval(yesterday, yesterday);
        model.setUpperBound(null);
    }

    /**
     * Issue #808-swingx: setUpperBound doesn't clear selection after.
     * NPE on removing lower bound when there is a selection.
     */
    @Test
    public void testSetLowerBoundNPE() {
        model.setLowerBound(yesterday);
        model.setSelectionInterval(today, today);
        model.setLowerBound(null);
    }
    /**
     * Issue #808-swingx: setUpperBound doesn't clear selection after
     */
    @Test
    public void testSetUpperBoundClearsSelectionAfter() {
        model.setSelectionInterval(tomorrow, tomorrow);
        model.setUpperBound(today);
        assertTrue("future selection must be cleared", model.isSelectionEmpty());
    }
    
    /**
     * Issue #808-swingx: setUpperBound doesn't clear selection after.
     */
    @Test
    public void testSetLowerBoundClearsSelectionBefore() {
        model.setSelectionInterval(yesterday, yesterday);
        model.setLowerBound(today);
        assertTrue("past selection must be cleared", model.isSelectionEmpty());
    }
    
    /**
     * Issue #713-Swingx: DateSelectionModel needs richer api.
     * 
     * Add api to access first/last.
     */
    @Test
    public void testFirstSelectionDate() {
        model.setSelectionMode(SelectionMode.SINGLE_INTERVAL_SELECTION);
        model.setSelectionInterval(today, tomorrow);
        assertEquals(model.getSelection().first(), model.getFirstSelectionDate());
    }
    
    /**
     * Issue #713-Swingx: DateSelectionModel needs richer api.
     * 
     * Add api to access first/last.
     */
    @Test
    public void testLastSelectionDate() {
        model.setSelectionMode(SelectionMode.SINGLE_INTERVAL_SELECTION);
        model.setSelectionInterval(today, tomorrow);
        assertEquals(model.getSelection().last(), model.getLastSelectionDate());
    }

    /**
     * Issue ??-Swingx: DateSelectionModel needs richer api.
     * 
     * Add api to access first/last.
     */
    @Test
    public void testFirstSelectionDateEmpty() {
        assertEquals(null, model.getFirstSelectionDate());
    }
    
    /**
     * Issue ??-Swingx: DateSelectionModel needs richer api.
     * 
     * Add api to access first/last.
     */
    @Test
    public void testLastSelectionDateEmpty() {
        assertEquals(null, model.getLastSelectionDate());
    }

    /**
     * Issue #618-swingx: JXMonthView displays problems with non-default
     * timezones.
     * 
     * Unselectable dates are "start of day" in the timezone they had been 
     * set. As such they make no sense in a new timezone: must
     * either be adjusted or cleared. Currently we clear them. 
     */
    @Test
    public void testTimeZoneChangeResetUnselectableDates() {
        TimeZone tz = TimeZone.of(ZoneOffset.ofHours(4));
        if (model.getTimeZone().equals(tz)) {
            tz = TimeZone.of(ZoneOffset.ofHours(5));
        }
        TreeSet<LocalDateTime> treeSet = new TreeSet<LocalDateTime>();
        treeSet.add(yesterday);
        model.setUnselectableDates(treeSet);
        model.setTimeZone(tz);
        // accidentally passes - because it is meaningful only in the timezone 
        // it was set ...
        assertFalse(model.isUnselectableDate(yesterday));
        // missing api on JXMonthView
        assertEquals("unselectable dates must have been cleared", 
                0, model.getUnselectableDates().size());
    }
    
    /**
     * Issue #618-swingx: JXMonthView displays problems with non-default
     * timezones.
     * 
     * Selected dates are "start of day" in the timezone they had been 
     * selected. As such they make no sense in a new timezone: must
     * either be adjusted or cleared. Currently we clear the selection. 
     */
    @Test
    public void testTimeZoneChangeClearSelection() {
        TimeZone tz = TimeZone.of(ZoneOffset.ofHours(4));
        if (model.getTimeZone().equals(tz)) {
            tz = TimeZone.of(ZoneOffset.ofHours(5));
        }
        LocalDateTime date = clock.dateTime();
        model.setSelectionInterval(date, date);
        // sanity
        assertTrue(model.isSelected(date));
        model.setTimeZone(tz);
        // accidentally passes - because it is meaningful only in the timezone 
        // it was set ...
        assertFalse(model.isSelected(date));
        assertTrue("selection must have been cleared", model.isSelectionEmpty());
    }
    

    /**
     * Issue #618-swingx: JXMonthView displays problems with non-default
     * timezones.
     * 
     * Bound dates are "start of day" in the timezone they had been 
     * set. As such they make no sense in a new timezone: must
     * either be adjusted or cleared. Currently we clear the bound. 
     */
    @Test
    public void testTimeZoneChangeResetLowerBound() {
        TimeZone tz = TimeZone.of(ZoneOffset.ofHours(4));
        if (model.getTimeZone().equals(tz)) {
            tz = TimeZone.of(ZoneOffset.ofHours(5));
        }
        model.setLowerBound(yesterday);
        model.setTimeZone(tz);
        assertEquals("lowerBound must have been reset", null, model.getLowerBound());
    }
    
    /**
     * Issue #618-swingx: JXMonthView displays problems with non-default
     * timezones.
     * 
     * Bound dates are "start of day" in the timezone they had been 
     * set. As such they make no sense in a new timezone: must
     * either be adjusted or cleared. Currently we clear the bound. 
     */
    @Test
    public void testTimeZoneChangeResetUpperBound() {
        TimeZone tz = TimeZone.of(ZoneOffset.ofHours(4));
        if (model.getTimeZone().equals(tz)) {
            tz = TimeZone.of(ZoneOffset.ofHours(5));
        }
        model.setUpperBound(yesterday);
        model.setTimeZone(tz);
        assertEquals("upperbound must have been reset", null, model.getUpperBound());
    }
    

    /**
     * Issue #694-swingx: setLocale must respect timezone.
     * 
     * test that locale update respects timezone.
     */
    @Test
    public void testCalendarTimeZoneLocale() {
        TimeZone tz = TimeZone.of(ZoneOffset.ofHours(4));
        if (model.getTimeZone().equals(tz)) {
            tz = TimeZone.of(ZoneOffset.ofHours(5));
        }
        model.setTimeZone(tz);
        Locale[] locales = Locale.getAvailableLocales();
        for (Locale locale : locales) {
            model.setLocale(locale);
            assertEquals(tz, model.getTimeZone());
        }
        
    }
    /**
     * test synch of model properties with its calendar's properties.
     * Here: null locale falls back to Locale.default.
     */
    @Test
    public void testCalendarLocaleNull() {
        // config with a known timezone and date
        Locale tz = Locale.GERMAN;
        if (model.getLocale().equals(tz)) {
            tz = Locale.FRENCH;
        }
        // different from default
        model.setLocale(tz);
        model.setLocale(null);
        assertEquals(Locale.getDefault(), model.getLocale());
    }

    /**
     * test synch of model properties with its calendar's properties.
     * Here: null locale falls back to Locale.default, no fire if had.
     */
    @Test
    public void testCalendarLocaleNullNoNofify() {
        Date310SelectionReport report = new Date310SelectionReport(model);
        model.setLocale(null);
        assertEquals(0, report.getEventCount());
    }

    /**
     * test synch of model properties with its calendar's properties.
     * Here: changed timeZone.
     */
    @Test
    public void testCalendarLocaleNoChangeNoNotify() {
        // config with a known timezone and date
        Locale tz = model.getLocale();
        Date310SelectionReport report = new Date310SelectionReport(model);
        model.setLocale(tz);
        assertEquals(0, report.getEventCount());
    }

    /**
     * test synch of model properties with its calendar's properties.
     * Here: changed timeZone.
     */
    @Test
    public void testCalendarLocaleChangedNotify() {
        // config with a known timezone and date
        Locale tz = Locale.GERMAN;
        if (model.getLocale().equals(tz)) {
            tz = Locale.FRENCH;
        }
        Date310SelectionReport report = new Date310SelectionReport(model);
        model.setLocale(tz);
        assertEquals(1, report.getEventCount());
        assertEquals(EventType.CALENDAR_CHANGED, report.getLastEventType());
    }

    /**
     * test synch of model properties with its calendar's properties.
     * Here: changed timeZone.
     */
    @Test
    public void testCalendarLocaleChanged() {
        // config with a known timezone and date
        Locale tz = Locale.GERMAN;
        if (model.getLocale().equals(tz)) {
            tz = Locale.FRENCH;
        }
        model.setLocale(tz);
        assertEquals(tz, model.getLocale());
    }

    /**
     * test synch of model properties with its calendar's properties.
     * Here: initial timeZone.
     */
    @Test
    public void testCalendarLocaleInitial() {
        assertEquals(Locale.getDefault(), model.getLocale());
    }


    
    /**
     * test synch of model properties with its calendar's properties.
     * Here: changed timeZone.
     */
    @Test
    public void testCalendarTimeZoneNoChangeNoNotify() {
        // config with a known timezone and date
        TimeZone tz = model.getTimeZone();
        Date310SelectionReport report = new Date310SelectionReport(model);
        model.setTimeZone(tz);
        assertEquals(0, report.getEventCount());
    }

    /**
     * test synch of model properties with its calendar's properties.
     * Here: changed timeZone.
     */
    @Test
    public void testCalendarTimeZoneChangedNotify() {
        // config with a known timezone and date
        TimeZone tz = TimeZone.of(ZoneOffset.ofHours(4));
        if (model.getTimeZone().equals(tz)) {
            tz = TimeZone.of(ZoneOffset.ofHours(5));
        }
        Date310SelectionReport report = new Date310SelectionReport(model);
        model.setTimeZone(tz);
        assertEquals(1, report.getEventCount(EventType.CALENDAR_CHANGED));
    }

    /**
     * test synch of model properties with its calendar's properties.
     * Here: changed timeZone.
     */
    @Test
    public void testCalendarTimeZoneChanged() {
        // config with a known timezone and date
        TimeZone tz = TimeZone.of(ZoneOffset.ofHours(4));
        if (model.getTimeZone().equals(tz)) {
            tz = TimeZone.of(ZoneOffset.ofHours(5));
        }
        model.setTimeZone(tz);
        assertEquals(tz, model.getTimeZone());
    }

    /**
     * test synch of model properties with its calendar's properties.
     * Here: initial timeZone.
     */
    @Test
    public void testCalendarTimeZoneInitial() {
        fail("TODO: need to define default timezone in model");
//        assertEquals(calendar.getTimeZone(), model.getTimeZone());
//        assertEquals(model.getTimeZone(), model.getCalendar().getTimeZone());
    }




    /**
     * test synch of model properties with its calendar's properties.
     * Here: initial minimalDaysInFirstWeek.
     */
    @Test
    public void testCalendarMinimalDaysInFirstWeekInitial() {
        fail("TODO: need to define replacement of minimalDaysInFirstWeek in model");
//        assertEquals(calendar.getMinimalDaysInFirstWeek(), model.getMinimalDaysInFirstWeek());
//        assertEquals(model.getMinimalDaysInFirstWeek(), model.getCalendar().getMinimalDaysInFirstWeek());
    }



    /**
     * test synch of model properties with its calendar's properties.
     * Here: initial firstDayOfWeek.
     */
    @Test
    public void testCalendarFirstDayOfWeekInitial() {
        assertEquals(DateTimeUtils.getFirstDayOfWeek(model.getLocale()), model.getFirstDayOfWeek());
    }


    /**
     * respect upper bound - the bound itself is a valid selection, the day
     * after is not. Remove bound allows the day after again.
     * 
     */
    @Test
    public void testUpperBoundAllowedFutureBlocked() {
        model.setUpperBound(today);
        // the bound itself is allowed
        model.setSelectionInterval(today, today);
        assertFalse("upper bound is selectable", model.isSelectionEmpty());
        assertTrue("upper bound must be selected", model.isSelected(today));
        Date310SelectionReport report = new Date310SelectionReport(model);
        model.setSelectionInterval(tomorrow, tomorrow);
        assertFalse("future must not be selected", model.isSelected(tomorrow));
        assertEquals("no event fired", 0, report.getEventCount());
        // remove bound allows selection of tomorrow
        model.setUpperBound(null);
        model.setSelectionInterval(tomorrow, tomorrow);
        assertTrue("tomorrow must be selected after removing upper bound ",
                model.isSelected(tomorrow));
    }
    
    /**
     * respect upper bound - the bound itself is a valid selection, the day
     * before is not. Remove bound allows the day before again.
     * 
     */
    @Test
    public void testLowerBoundAllowedPastBlocked() {
        model.setLowerBound(today);
        // the bound itself is allowed
        model.setSelectionInterval(today, today);
        assertFalse("lower bound is selectable", model.isSelectionEmpty());
        assertTrue("loweer bound must be selected", model.isSelected(today));
        Date310SelectionReport report = new Date310SelectionReport(model);
        model.setSelectionInterval(yesterday, yesterday);
        assertFalse("past must not be selected", model.isSelected(yesterday));
        assertEquals("no event fired", 0, report.getEventCount());
        // remove bound allows selection of tomorrow
        model.setLowerBound(null);
        model.setSelectionInterval(yesterday, yesterday);
        assertTrue("past must be selected after removing lower bound ",
                model.isSelected(yesterday));
    }
    
    /**
     * respect both bounds - overlapping: no selection.
     *
     */
    @Test
    public void testBothBoundsOverlap() {
        model.setLowerBound(today);
        model.setUpperBound(yesterday);
        Date310SelectionReport report = new Date310SelectionReport(model);
        model.setSelectionInterval(today, today);
        assertEquals("selection must be empty", 0, model.getSelection().size());
        assertEquals("no event fired", 0, report.getEventCount());
    }

    /**
     * respect both bounds - same date: single selection allowed.
     *
     */
    @Test
    public void testBothBoundsSame() {
        model.setLowerBound(today);
        model.setUpperBound(today);
        model.setSelectionInterval(today, today);
        assertTrue("selected bounds", model.isSelected(today));
    }


    /**
     * Common contract: the exact date marked as unselectable
     * 
     * first set the unselectables then set the selection to it must not change
     * the selection state (still empty).
     */
    @Test
    public void testUnselectableDates() {
        SortedSet<LocalDateTime> unselectableDates = new TreeSet<LocalDateTime>();
        unselectableDates.add(today);
        model.setUnselectableDates(unselectableDates);
        model.setSelectionInterval(today, today);
        assertTrue("selection must be empty", model.isSelectionEmpty());
    }

    
    /**
     * adding api: adjusting
     *
     */
    @Test
    public void testEventsCarryAdjustingFlagTrue() {
        LocalDateTime date = clock.dateTime();
        model.setAdjusting(true);
        Date310SelectionReport report = new Date310SelectionReport(model);
        model.setSelectionInterval(date, date);
        assertEquals(model.isAdjusting(), report.getLastEvent().isAdjusting());
        // sanity: revert 
        model.setAdjusting(false);
        report.clear();
        model.removeSelectionInterval(date, date);
        assertEquals(model.isAdjusting(), report.getLastEvent().isAdjusting());
        
    }

    /**
     * adding api: adjusting
     *
     */
    @Test
    public void testEventsCarryAdjustingFlagFalse() {
        LocalDateTime date = clock.dateTime();
        Date310SelectionReport report = new Date310SelectionReport(model);
        model.setSelectionInterval(date, date);
        assertEquals(model.isAdjusting(), report.getLastEvent().isAdjusting());
    }
    
    /**
     * adding api: adjusting.
     *
     */
    @Test
    public void testAdjusting() {
        // default value
        assertFalse(model.isAdjusting());
        Date310SelectionReport report = new Date310SelectionReport(model);
        // set adjusting
        model.setAdjusting(true);
        assertTrue("model must be adjusting", model.isAdjusting());
        assertEquals(1, report.getEventCount());
        assertEquals(Date310SelectionEvent.EventType.ADJUSTING_STARTED, 
                report.getLastEventType());
        // next round - reset to default adjusting
        report.clear();
        model.setAdjusting(false);
        assertFalse("model must not be adjusting", model.isAdjusting());
        assertEquals(1, report.getEventCount());
        assertEquals(Date310SelectionEvent.EventType.ADJUSTING_STOPPED, 
                report.getLastEventType());
        
    }
    
    /**
     * test that isSelected with null date throws NPE
     */
    @Test
    public void testIsSelectedNull() {
//        model.setSelectionInterval(today, today);
        try {
            model.isSelected(null);
            fail("null is not allowed");
        } catch (NullPointerException e) {
            // expected
        } catch (Exception e) {
            fail("expected NPE instead of " + e);
        }
    }
    
    /**
     * null unselectables not allowed.
     */
    @Test
    public void testUnselectableDatesNull() {
        try {
            model.setUnselectableDates(null);
            fail("must fail with null set of unselectables");
            // expected
        } catch (NullPointerException e) {
            // expected
        } catch (Exception e) {
            fail("expected NPE instead of " + e);
        }
    }

//------------------------ convenience and setup    
    /**
     * Convience to return the start of day relative to the calendar.
     * 
     * @param date
     * @return
     */
    protected LocalDateTime startOfDay(LocalDateTime date) {
        return LocalDateTime.ofMidnight(date);
    }

    /**
     * Convience to return the end of day relative to the calendar.
     * 
     * @param date
     * @return
     */
    protected LocalDateTime endOfDay(LocalDateTime date) {
        LocalDateTime next = LocalDateTime.ofMidnight(date.plus(Period.ofDays(1)));
        return next.minus(Period.ofNanos(1));
    }

    /**
     * Initializes the calendar to the default instance and the predefined dates
     * in the coordinate system of the calendar. Note that the hour is set
     * to "about 5" in all dates, to be reasonably well into the day. The time
     * fields of all dates are the same, the calendar is pre-set with the
     * today field.
     */
    protected void setUpCalendar() {
        clock = Clock.systemDefaultZone();
        today = clock.dateTime();
        today = today.withHourOfDay(5);
        
        yesterday = today.minus(Period.ofDays(1));
        tomorrow = today.plus(Period.ofDays(1));
        afterTomorrow = today.plus(Period.ofDays(2));
    }
    
    @Before
    @Override
    public void setUp() throws Exception {
        setUpCalendar();
        model = createDateSelectionModel();
    }

    protected abstract Date310SelectionModel createDateSelectionModel();

    
}
