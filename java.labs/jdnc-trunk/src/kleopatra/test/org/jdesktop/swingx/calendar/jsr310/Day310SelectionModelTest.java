/*
 * $Id: Day310SelectionModelTest.java 3339 2011-02-04 17:03:23Z kleopatra $
 *
 * Copyright 2007 Sun Microsystems, Inc., 4150 Network Circle,
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

import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.time.calendar.DateTimeProvider;
import javax.time.calendar.LocalDateTime;
import javax.time.calendar.Period;

import org.jdesktop.swingx.calendar.jsr310.Date310SelectionModel.SelectionMode;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;


/**
 * Test DaySelectionModel.
 * 
 * @author Jeanette Winzenburg
 */
@RunWith(JUnit4.class)
public class Day310SelectionModelTest extends AbstractTestDate310SelectionModel {


    @Before
    public void setUpJ4() throws Exception {
        setUp();
    }
    
    @After
    public void tearDownJ4() throws Exception {
        tearDown();
    }

    @Test
    public void interactiveSortedProvider() {
        SortedSet<DateTimeProvider> set = new TreeSet<DateTimeProvider>(new LocalProviderComparator());
        set.add(today);
        assertEquals(1, set.size());
        assertSame(today, set.first());
        set.add(yesterday);
        assertEquals(2, set.size());
        assertSame(yesterday, set.first());
    }

    public static class LocalProviderComparator implements Comparator<DateTimeProvider> {

        public int compare(DateTimeProvider o1, DateTimeProvider o2) {
            return o1.toLocalDateTime().toLocalDate().compareTo(o2.toLocalDateTime().toLocalDate());
        }
        
    }
    
    @Test
    public void interactiveSortedPartly() {
        SortedSet<LocalDateTime> set = new TreeSet<LocalDateTime>(new LocalDateComparator());
        set.add(today.plusHours(3));
        assertEquals(1, set.size());
        assertEquals(today.plusHours(3), set.first());
        set.add(today);
        assertEquals(1, set.size());
        assertEquals(today.plusHours(3), set.first());
    }
    
    @Test
    public void interactiveSortedPartAndMultiple() {
        SortedSet<LocalDateTime> set = new TreeSet<LocalDateTime>(new LocalDateComparator());
        set.add(today);
        LocalDateTime startDate = today;
        LocalDateTime endDate = tomorrow;
        LocalDateTime date = startDate;
        while (!date.isAfter(endDate)) {
            set.add(date);
            date = date.plus(Period.ofDays(1));
        };
        assertEquals(2, set.size());
        assertEquals(today, set.first());
    }
    
    public static class LocalDateComparator implements Comparator<LocalDateTime> {

        public int compare(LocalDateTime o1, LocalDateTime o2) {
            return o1.toLocalDate().compareTo(o2.toLocalDate());
        }
        
    }

    
    /**
     * DaySelectionModel normalizes to start of day.
     */
//    @Test
//    public void testNormalizedDateStartOfDay() {
//        assertEquals(startOfDay(today), model.getNormalizedDate(today));
//        assertNotSame(startOfDay(today), model.getNormalizedDate(today));
//    }


    /**
     * setSelectionInterval must throw NPE if given date is null
     */
    @Test
    public void testSetIntervalNulls() {
        try {
            model.setSelectionInterval(null, null);
            fail("normalizedDate must throw NPE if date is null");
        } catch (NullPointerException e) {
            // expected 
        } catch (Exception e) {
            fail("unexpected exception " + e);
        }
        
    }
    /**
     * setSelectionInterval must throw NPE if given date is null
     */
    @Test
    public void testAddIntervalNulls() {
        try {
            model.addSelectionInterval(null, null);
            fail("normalizedDate must throw NPE if date is null");
        } catch (NullPointerException e) {
            // expected 
        } catch (Exception e) {
            fail("unexpected exception " + e);
        }
        
    }
    
    /**
     * removeSelectionInterval must throw NPE if given date is null
     */
    @Test
    public void testRemoveIntervalNulls() {
        try {
            model.removeSelectionInterval(null, null);
            fail("normalizedDate must throw NPE if date is null");
        } catch (NullPointerException e) {
            // expected 
        } catch (Exception e) {
            fail("unexpected exception " + e);
        }
        
    }

    /**
     * set selection on same day: 
     * Compare behaviour of single vs. multiple selection mode
     * 
     * Problem is the internal implementation of intervalled selections: they
     * clear before adding (legacy?)
     */
    @Test
    public void testSetSelectionIntervalMultipleSameDay() {
        // select a day
        model.setSelectionInterval(today, today);
        // add a selection on the same day
        model.setSelectionInterval(today.plusHours(1), today.plusHours(1));
        // 
        LocalDateTime selectedSingle = model.getFirstSelectionDate();
        assertEquals("multiple setSelection on same day must not change", 
                today, selectedSingle);
        model.clearSelection();
        model.setSelectionMode(SelectionMode.MULTIPLE_INTERVAL_SELECTION);
        model.setSelectionInterval(today, today);
        // this should have the same in multiple as in singleSelectionMode
        // that is: not overwrite the old
        model.setSelectionInterval(today.plusHours(1), today.plusHours(1));
        assertEquals("multiple setSelection on same day must not change", 
                selectedSingle, model.getFirstSelectionDate());
    }
    
    /**
     * set selection on same day: 
     * Must not fire if already had been selected.
     * 
     * Problem is the internal implementation of intervalled selections: they
     * clear before adding (legacy?)
     */
    @Test
    public void testSetSelectionMultipleIntervalNoChangeNoFire() {
        model.setSelectionMode(SelectionMode.MULTIPLE_INTERVAL_SELECTION);
        model.setSelectionInterval(today, today);
        Date310SelectionReport report = new Date310SelectionReport(model);
        model.setSelectionInterval(today.plusHours(1), today.plusHours(2));
        assertEquals(today, model.getFirstSelectionDate());
        assertEquals(1, model.getSelection().size());
        assertEquals("same bound, no event fired", 0, report.getEventCount());
    }
    
    /**
     */
    @Test
    public void testSetSelectionIntervalNoChangeNoFire() {
        model.setSelectionInterval(today, today);
        Date310SelectionReport report = new Date310SelectionReport(model);
        model.setSelectionInterval(today.plusHours(1), today.plusHours(2));
        assertEquals(today, model.getFirstSelectionDate());
        assertEquals(1, model.getSelection().size());
        assertEquals("same bound, no event fired", 0, report.getEventCount());
    }

    /**
     * Set unselectable and test that all dates of the day are unselectable.
     */
    @Test
    public void testUnselectableDatesCompleteDay() {
        SortedSet<LocalDateTime> unselectableDates = new TreeSet<LocalDateTime>();
        unselectableDates.add(today);
        model.setUnselectableDates(unselectableDates);
        // all dates in today must be rejected
        assertTrue("raw today must be unselectable", 
                model.isUnselectableDate(today));
        assertTrue("start of today must be unselectable", 
                model.isUnselectableDate(startOfDay(today)));
        assertTrue("end of today must be unselectable", 
                model.isUnselectableDate(endOfDay(today)));
        // remove the unselectable 
        model.setUnselectableDates(new TreeSet<LocalDateTime>());
        assertFalse(model.isUnselectableDate(today));
        assertFalse(model.isUnselectableDate(startOfDay(today)));
        assertFalse(model.isUnselectableDate(endOfDay(today)));
    }

   
    @Test
    public void testEmptySelectionInitial() {
        assertTrue(model.isSelectionEmpty());
        SortedSet<LocalDateTime> selection = model.getSelection();
        assertTrue(selection.isEmpty());
    }
    
    @Test
    public void testEmptySelectionClear() {
        model.setSelectionInterval(today, today);
        // sanity
        assertTrue(1 == model.getSelection().size());

        model.clearSelection();
        assertTrue(model.isSelectionEmpty());
        assertTrue(model.getSelection().isEmpty());
    }

    @Test
    public void testSingleSelection() {
        model.setSelectionMode(SelectionMode.SINGLE_SELECTION);

        model.setSelectionInterval(today, today);
        assertTrue(1 == model.getSelection().size());
//        assertEquals(startOfDay(today), model.getFirstSelectionDate());
        assertEquals(today, model.getFirstSelectionDate());

        model.setSelectionInterval(today, afterTomorrow);
        assertTrue(1 == model.getSelection().size());
//        assertEquals(startOfDay(today), model.getFirstSelectionDate());
        assertEquals(today, model.getFirstSelectionDate());
    }
    
    @Test
    public void testSingleIntervalSelection() {
        model.setSelectionMode(SelectionMode.SINGLE_INTERVAL_SELECTION);

//        model.setSelectionInterval(today, today);
//        assertTrue(1 == model.getSelection().size());
////        assertEquals(startOfDay(today), model.getFirstSelectionDate());
//        assertEquals(today, model.getFirstSelectionDate());

        model.setSelectionInterval(today, tomorrow);
        
//        assertEquals(2, model.getSelection().size());
//        assertEquals(startOfDay(today), model.getFirstSelectionDate());
//        assertEquals(startOfDay(tomorrow), model.getLastSelectionDate());
        assertEquals(today, model.getFirstSelectionDate());
        assertEquals(tomorrow, model.getLastSelectionDate());
    }

    @Test
    public void testWeekIntervalSelection() {
        //TODO...
    }

    @Test
    public void testMultipleIntervalSelection() {
        model.setSelectionMode(SelectionMode.MULTIPLE_INTERVAL_SELECTION);

        model.setSelectionInterval(yesterday, yesterday);
        model.addSelectionInterval(afterTomorrow, afterTomorrow);
        
        assertEquals(2, model.getSelection().size());
//        assertEquals(startOfDay(yesterday), model.getFirstSelectionDate());
//        assertEquals(startOfDay(afterTomorrow), model.getLastSelectionDate());
        assertEquals(yesterday, model.getFirstSelectionDate());
        assertEquals(afterTomorrow, model.getLastSelectionDate());
        
    }

//    @Before
//    @Override
//    public void setUp() throws Exception {
//        setUpCalendar();
//        model = new Day310SelectionModel();
//    }

    @Override
    protected Date310SelectionModel createDateSelectionModel() {
        // TODO Auto-generated method stub
        return new Day310SelectionModel();
    }

    
}
