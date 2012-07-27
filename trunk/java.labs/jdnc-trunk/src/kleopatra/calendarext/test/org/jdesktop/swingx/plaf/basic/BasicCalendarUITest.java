/*
 * $Id: BasicMonthViewUITest.java 3571 2010-01-05 15:07:17Z kleopatra $
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
package org.jdesktop.swingx.plaf.basic;

import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Rectangle;
import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Logger;

import javax.swing.JComponent;
import javax.swing.JLabel;

import org.jdesktop.swingx.InteractiveTestCase;
import org.jdesktop.swingx.JXCalendar;
import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.calendar.CalendarCellState;
import org.jdesktop.swingx.calendar.CalendarUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;


/**
 * Tests to expose known issues of BasicCalendarUI.
 * 
 * @author Jeanette Winzenburg
 */
@RunWith(JUnit4.class)
public class BasicCalendarUITest extends InteractiveTestCase {
    @SuppressWarnings("all")
    private static final Logger LOG = Logger
            .getLogger(BasicCalendarUITest.class.getName());

    // duplicate hard-coded monthViewUI values
    @SuppressWarnings("unused")
    private static final int CALENDAR_SPACING = 10;
    
    @Test
    public void testLocaleByProviderMonthRendering() {
        Locale serbianLatin = getLocal("sh");
        if (serbianLatin == null) {
            LOG.fine("can't run, no service provider for serbian latin" );
            return;
        }
        JXCalendar calendarView = new JXCalendar();
        calendarView.setLocale(serbianLatin);
        Calendar calendar = calendarView.getPage();
        int month = calendar.get(Calendar.MONTH);
        JComponent component = calendarView.prepareRendererComponent(calendar, 
                CalendarCellState.PAGE_TITLE);
        
        String[] monthNames = DateFormatSymbols.getInstance(calendarView.getLocale()).getMonths();
        String title = ((JLabel) component).getText();
        assertTrue("name must be taken from Locale, expected: " + monthNames[month] + " was: " + title, 
                title.startsWith(monthNames[month]));
        
    }

    /**
     * Issue #1245-swingx: incorrect month/dayofweek names for non-core-supported Locales.
     */
    @Test
    public void testLocaleByProviderDayOfTheWeekName() {
//        if (UIManager.get("JXCalendar.daysOfTheWeek") != null) {
//            LOG.fine("can't test, custom daysoftheweek");
//        }
//        Locale serbianLatin = getLocal("sh");
//        if (serbianLatin == null) {
//            LOG.fine("can't run, no service provider for serbian latin" );
//            return;
//        }
//        JXCalendar monthView = new JXCalendar();
//        monthView.setLocale(serbianLatin);
//        assertWeekdays(monthView, serbianLatin);
    }
    
    
    /**
     * @param string
     * @return
     */
    private Locale getLocal(String language) {
        Locale[] available = Locale.getAvailableLocales();
        for (Locale locale : available) {
            if (language.equals(locale.getLanguage())) return locale;
        }
        return null;
    }

    /**
     * Issue #1068-swingx: week numbering broken for some years and locales
     * 
     * f.i. Jan, 2011 in de: first of Jan is week 52
     */
    @Test
    public void testWeekNumbersWrapBack() {
        assertWeekNumbers(Locale.GERMAN, 2011, Calendar.JANUARY, Calendar.SATURDAY, 52, 6);
    }
    
    /**
     * Issue #1068-swingx: week numbering broken for some years and locales
     * 
     * f.i. Dec, 2008 in de: last of dec is first week of next year
     */
    @Test
    public void testWeekNumbersWrapForward() {
        assertWeekNumbers(Locale.GERMAN, 2008, Calendar.DECEMBER, Calendar.MONDAY, 49, 5);
    }

    /**
     * Issue #1068-swingx: week numbering broken for some years and locales
     * 
     * f.i. Oct, 2008 in de: must frequent case, days spread across 5 weeks
     */
    @Test
    public void testWeekNumbersNormal() {
        assertWeekNumbers(Locale.GERMAN, 2008, Calendar.OCTOBER, Calendar.WEDNESDAY, 40, 5);
    }

    /**
     * Creates a monthView with the given locale, sets the firstDisplayedDate to the 
     * first of the given year and month (sanity asserts dayOfWeek and weekofYear) and 
     * asserts the number of weeks in that month.
     * 
     * @param locale
     * @param year 
     * @param month
     * @param expectedDay day of week (sanity)
     * @param expectedWeek week of year of the day (sanity)
     * @param expectedWeekNumber number of weeks in the month
     */
    private void assertWeekNumbers(Locale locale, int year, int month, int expectedDay, int expectedWeek, int expectedWeekNumber) {
        JXCalendar monthView = new JXCalendar(locale);
        Calendar calendar = monthView.getPage();
        calendar.set(year, month, 1);
        assertEquals("sanity - day", expectedDay, calendar.get(Calendar.DAY_OF_WEEK));
        assertEquals("sanity - weekOfYear", expectedWeek, calendar.get(Calendar.WEEK_OF_YEAR));
        monthView.setSelectionDate(calendar.getTime());
        assertEquals("number of weeks in month", expectedWeekNumber, 
                ((BasicCalendarUI) monthView.getUI()).getWeeks());
    }
    /**
     * Issue #1068-swingx: week numbering broken for some years and locales
     * 
     * month with fully six weeks: April 2010
     */
    @Test
    public void testWeekNumbersFull6() {
        assertWeekNumbers(Locale.GERMAN, 2012, Calendar.APRIL, Calendar.SUNDAY, 13, 6);
    }

    /**
     * Issue #1068-swingx: week numbering broken for some years and locales
     * 
     * month with minimal 4 weeks: Feb 2010
     */
    @Test
    public void testWeekNumbersMinimum4() {
        assertWeekNumbers(Locale.GERMAN, 2010, Calendar.FEBRUARY, Calendar.MONDAY, 5, 4);
    }
    
    
    /**
     * Sanity: uiClassID alike on CalendarHeaderHandler
     */
    @Test
    public void testCalendarHeaderHandlerID() {
        assertEquals("CalendarHeaderHandler", CalendarHeaderHandler.uiControllerID);
    }
    

    /**
     * Test that day position in details day-of-week header returns 
     * start of day of first week
     */
    @Test
    public void testDayInMonthForDayOfWeekHeader() {
        // This test will not work in a headless configuration.
        if (GraphicsEnvironment.isHeadless()) {
            LOG.fine("cannot run test - headless environment");
            return;
        }
        BasicCalendarUI ui = getRealizedMonthViewUI(ComponentOrientation.LEFT_TO_RIGHT);
        Calendar calendar = ui.getPage();
        CalendarUtils.endOfWeek(calendar);
        // last day of the first week
        CalendarUtils.startOfDay(calendar);
        assertEquals("header grid position must start of day of first week", 
                calendar.getTime(), 
                ui.getCell(BasicCalendarUI.DAY_HEADER_ROW, 
                        BasicCalendarUI.LAST_DAY_COLUMN).getTime());
     }

    /**
     * Test that day position in details weekNumber header 
     * start of week
     */
    @Test
    public void testDayInMonthForWeekNumberHeader() {
        // This test will not work in a headless configuration.
        if (GraphicsEnvironment.isHeadless()) {
            LOG.fine("cannot run test - headless environment");
            return;
        }
        BasicCalendarUI ui = getRealizedMonthViewUI(ComponentOrientation.LEFT_TO_RIGHT, true);
       // get a date in the first month
        Calendar calendar = ui.getPage();
        CalendarUtils.startOfWeek(calendar);
        calendar.add(Calendar.WEEK_OF_YEAR, 3);
        assertEquals("header grid position must return start of first day of week ", 
                calendar.getTime(), 
                ui.getCell(BasicCalendarUI.FIRST_WEEK_ROW + 3, 
                        BasicCalendarUI.WEEK_HEADER_COLUMN).getTime());
     }

    
    /**
     * Issue #787-swingx: hit detection of leading/trailing days.
     * Sanity: get day bounds for leading dates (here: first month) must succeed.
     */
    @Test
    public void testDayBoundsAtLocationLeadingFirstMonth() {
        // This test will not work in a headless configuration.
        if (GraphicsEnvironment.isHeadless()) {
            LOG.fine("cannot run test - headless environment");
            return;
        }
        BasicCalendarUI ui = getRealizedMonthViewUI(ComponentOrientation.LEFT_TO_RIGHT);
        // first month - Feb 2008 has leading dates
        Rectangle monthBounds = ui.getPageBounds();
        int firstDayY = monthBounds.y + ui.getPageHeaderHeight() + ui.getDaySize().height + 2;
        int row = ui.getCellGridPositionAtLocation(monthBounds.x + 2, firstDayY).y;
        assertEquals(BasicCalendarUI.FIRST_WEEK_ROW, row);
        Rectangle dayBounds = ui.getCellBoundsAtLocation(monthBounds.x + 2, firstDayY);
        assertNotNull(dayBounds);
    }

    /**
     * Test full circle: getDayBounds(Date)
     * PENDING JW: temporarily removed conversion Date -> bounds
     */
//    @Test
//    public void testDayBoundsFromDate() {
//        // This test will not work in a headless configuration.
//        if (GraphicsEnvironment.isHeadless()) {
//            LOG.fine("cannot run test - headless environment");
//            return;
//        }
//        BasicCalendarUI ui = getRealizedMonthViewUI(ComponentOrientation.LEFT_TO_RIGHT);
//        // second non-header row
//        // first day column
//        Point location = getLocationInGrid(ui, 2, 0);
//        Rectangle dayBounds = ui.getDetailBoundsAtLocation(location.x, location.y);
//        Date date = ui.getDetailDateAtLocation(location.x, location.y); 
//        assertEquals(dayBounds, ui.getDayBounds(date));
//     }


    /**
     * Test getDayGridPosition(Date) - 
     * here: had been incorrect calculation of first row.
     * PENDING JW: temporarily removed conversion Date -> bounds
     */
//    @Test
//    public void testDateToGridPosition6Apr2008() {
//        // This test will not work in a headless configuration.
//        if (GraphicsEnvironment.isHeadless()) {
//            LOG.fine("cannot run test - headless environment");
//            return;
//        }
//        BasicCalendarUI ui = getRealizedMonthViewUI(ComponentOrientation.LEFT_TO_RIGHT);
//        Calendar cal = ui.getCalendar();
//        // 3th feb was sunday ... it is a last day of the first week, but only at places where first day is Monday.
//        // so using this obsure notation we get last day of first week depending on when the week really starts.
//        cal.set(2008, Calendar.FEBRUARY, 1 + cal.getFirstDayOfWeek()); 
//        LOG.info("6apr 2008" + cal.getTime());
//        assertEquals(BasicCalendarUI.LAST_DAY_COLUMN, ui.getDayGridPosition(cal.getTime()).x);
//        assertEquals(BasicCalendarUI.FIRST_WEEK_ROW, ui.getDayGridPosition(cal.getTime()).y);
//        
//     }

    /**
    * Test getDayGridPosition(Date) - first complete row
     * PENDING JW: temporarily removed conversion Date -> bounds
    */
//    @Test
//    public void testDateToGridPositionNonLeading() {
//        // This test will not work in a headless configuration.
//        if (GraphicsEnvironment.isHeadless()) {
//            LOG.fine("cannot run test - headless environment");
//            return;
//        }
//        BasicCalendarUI ui = getRealizedMonthViewUI(ComponentOrientation.LEFT_TO_RIGHT);
//        // get a date in the first month
//        Date month = ui.getMonth(0, 0);
//        // second row - cant be a leading date
//        assertDateToDayGrid(ui, month, BasicCalendarUI.FIRST_WEEK_ROW + 1, BasicCalendarUI.FIRST_DAY_COLUMN);
//     }

    /**
    * Test getDayGridPosition(Date) - somewhere in the middle (being paranoid ;-)
     * PENDING JW: temporarily removed conversion Date -> bounds
     */
//    @Test
//    public void testDateToGridPositionMiddle() {
//        // This test will not work in a headless configuration.
//        if (GraphicsEnvironment.isHeadless()) {
//            LOG.fine("cannot run test - headless environment");
//            return;
//        }
//        BasicCalendarUI ui = getRealizedMonthViewUI(ComponentOrientation.LEFT_TO_RIGHT);
//        // get a date in the first month
//        Date month = ui.getMonth(0, 0);
//        assertDateToDayGrid(ui, month, BasicCalendarUI.FIRST_WEEK_ROW + 3, BasicCalendarUI.FIRST_DAY_COLUMN);
//     }
//    
    /**
     * Full cylce: use getDayInMonth(...) to get the day from the logical 
     * grid position and assert that the reverse getDayGridPosition returns the same
     * logical coordinates. The given coordinates must map to a day contained in the
     * month, that is >= 0 and not representing leading/trailing dates.
     * PENDING JW: temporarily removed conversion Date -> bounds
     * 
     * @param ui the ui to use (must be realized)
     * @param month the month containing the grid to test
     * @param dayRow the logical row coordinate of the day
     * @param dayColumn the logical column coordinate of the day
     */
//    private void assertDateToDayGrid(BasicCalendarUI ui, Date month,
//            int dayRow, int dayColumn) {
//        Date day = ui.getDetailDate(dayRow, dayColumn);
//        assertEquals(dayRow, ui.getDayGridPosition(day).y);
//        assertEquals(dayColumn, ui.getDayGridPosition(day).x);
//    }




    /**
     * Full cylce: use getMonth(...) to get the month from the logical 
     * grid position and assert that the reverse getMonthGridPosition returns the same
     * logical coordinates. 
     * 
     * @param ui the ui to use (must be realized)
     * @param row the row index of the month
     * @param column the column index of the month
     */
//    private void assertDateToMonthGrid(BasicCalendarUI ui, int row, int column) {
//        // date of start of month from logical position
//        Date month = ui.getMonth(row, column);
//        assertEquals(row, ui.getMonthGridPosition(month).y);
//        assertEquals(column, ui.getMonthGridPosition(month).x);
//    }

    /**
     * Test full circle: getMonthBounds(Date)
     * 
     * PENDING: change to page bounds - only one "page" visible at any time
     */
//    @Test
//    public void testDateToMonthBounds() {
//        // This test will not work in a headless configuration.
//        if (GraphicsEnvironment.isHeadless()) {
//            LOG.fine("cannot run test - headless environment");
//            return;
//        }
//        BasicCalendarUI ui = getRealizedMonthViewUI(ComponentOrientation.LEFT_TO_RIGHT);
//        // month bounds from logical position in second row, second column
//        Rectangle bounds = ui.getPageBounds();
//        // date of start of month from logical position
//        Date month = ui.getMonth(0, 0);
//        assertEquals(bounds, ui.getMonthBounds(month));
//     }

    /**
     * Test  getMonthBounds(Date) for not visible dates are null.
     */
//    @Test
//    public void testDateToMonthBoundsNotVisible() {
//        // This test will not work in a headless configuration.
//        if (GraphicsEnvironment.isHeadless()) {
//            LOG.fine("cannot run test - headless environment");
//            return;
//        }
//        BasicCalendarUI ui = getRealizedMonthViewUI(ComponentOrientation.LEFT_TO_RIGHT);
//        // the ui's calendar is configured to the first displayed day
//        Calendar uiCalendar = ui.getCalendar();
//        int month = uiCalendar.get(Calendar.MONTH);
//        CalendarUtils.startOfWeek(uiCalendar);
//        assertFalse("sanity - we have leading dates in the month", month == uiCalendar.get(Calendar.MONTH));
//        assertEquals("leading dates must return null bounds", 
//                null, ui.getMonthBounds(uiCalendar.getTime()));
//    }

    /**
     * Test  getDayBounds(Date)  for null date must throw NPE.
     */
//    @Test
//    public void testMonthBoundsNullDate() {
//        // This test will not work in a headless configuration.
//        if (GraphicsEnvironment.isHeadless()) {
//            LOG.fine("cannot run test - headless environment");
//            return;
//        }
//        BasicCalendarUI ui = getRealizedMonthViewUI(ComponentOrientation.LEFT_TO_RIGHT);
//        try {
//            ui.getMonthBounds(null);
//            fail("date param null is not allowed - must fire NPE");
//        } catch (NullPointerException ex) {
//            // that's what we expect
//        }
//    }
    
    /**
     * Test  getDayBounds(Date) for leading dates are null.
     * PENDING JW: temporarily removed conversion Date -> bounds
     */
//    @Test
//    public void testDateToDayBoundsLeadingDate() {
//        // This test will not work in a headless configuration.
//        if (GraphicsEnvironment.isHeadless()) {
//            LOG.fine("cannot run test - headless environment");
//            return;
//        }
//        BasicCalendarUI ui = getRealizedMonthViewUI(ComponentOrientation.LEFT_TO_RIGHT);
//        // the ui's calendar is configured to the first displayed day
//        Calendar uiCalendar = ui.getCalendar();
//        int month = uiCalendar.get(Calendar.MONTH);
//        CalendarUtils.startOfWeek(uiCalendar);
//        assertFalse("sanity - we have leading dates in the month", month == uiCalendar.get(Calendar.MONTH));
//        assertEquals("leading dates must return null bounds", 
//                null, ui.getDayBounds(uiCalendar.getTime()));
//    }

    /**
     * Test  getDayBounds(Date)  for null date must fire NPE.
     * PENDING JW: temporarily removed conversion Date -> bounds
     */
//    @Test
//    public void testDayBoundsNullDate() {
//        // This test will not work in a headless configuration.
//        if (GraphicsEnvironment.isHeadless()) {
//            LOG.fine("cannot run test - headless environment");
//            return;
//        }
//        BasicCalendarUI ui = getRealizedMonthViewUI(ComponentOrientation.LEFT_TO_RIGHT);
//        try {
//            ui.getDayBounds(null);
//            fail("date param null is not allowed - must fire NPE");
//        } catch (NullPointerException ex) {
//            // that's what we expect
//        }
//    }
    /**
     * Issue #781-swingx: reverse coordinate transformation.
     * Here: expose sizes
     */
    @Test
    public void testMonthSize() {
        if (GraphicsEnvironment.isHeadless()) {
            LOG.fine("cannot run test - headless environment");
            return;
        }
        BasicCalendarUI ui = getRealizedMonthViewUI(ComponentOrientation.RIGHT_TO_LEFT);
        Rectangle monthBounds = ui.getPageBounds();
        assertEquals(monthBounds.width, ui.getPageSize().width);
        assertEquals(monthBounds.height, ui.getPageSize().height);
    }
 
    /**
     * Issue #781-swingx: reverse coordinate transformation.
     * Here: expose sizes
     */
    @Test
    public void testDaySize() {
        if (GraphicsEnvironment.isHeadless()) {
            LOG.fine("cannot run test - headless environment");
            return;
        }
        BasicCalendarUI ui = getRealizedMonthViewUI(ComponentOrientation.RIGHT_TO_LEFT);
        Rectangle monthBounds = ui.getPageBounds();
        Rectangle dayBounds = ui.getCellBoundsAtLocation(
                monthBounds.x + 2, 
                monthBounds.y + ui.getPageHeaderHeight() + 2); 
        assertEquals(dayBounds.width, ui.getDaySize().width);
        assertEquals(dayBounds.height, ui.getDaySize().height);
    }
    
    
    /**
     * Test day at location
     */
    @Test
    public void testDayAtLocationLToR() {
        // This test will not work in a headless configuration.
        if (GraphicsEnvironment.isHeadless()) {
            LOG.fine("cannot run test - headless environment");
            return;
        }
        BasicCalendarUI ui = getRealizedMonthViewUI(ComponentOrientation.LEFT_TO_RIGHT);
        // first column in second non-header row
        Point location = getLocationInGrid(ui, 2, 0);
        Calendar date = ui.getCellAtLocation(location.x, location.y);
        // the ui's calendar is configured to the first displayed day
        Calendar uiCalendar = ui.getPage();
        uiCalendar.add(Calendar.WEEK_OF_YEAR, 1);
        CalendarUtils.startOfWeek(uiCalendar);
        assertEquals("first logical column in LToR", uiCalendar.getTime(), date.getTime());
     }

    /**
     * Test day at location
     */
    @Test
    public void testDayAtLocationRToL() {
        // This test will not work in a headless configuration.
        if (GraphicsEnvironment.isHeadless()) {
            LOG.fine("cannot run test - headless environment");
            return;
        }
        BasicCalendarUI ui = getRealizedMonthViewUI(ComponentOrientation.RIGHT_TO_LEFT);
        // first column in second non-header row
        Point location = getLocationInGrid(ui, 2, 0);
        Calendar date = ui.getCellAtLocation(location.x, location.y);
        Date endOfWeek = CalendarUtils.endOfWeek(ui.getPage(), date.getTime());
        Calendar uiCalendar = ui.getPage();
        uiCalendar.add(Calendar.WEEK_OF_YEAR, 1);
        CalendarUtils.endOfWeek(uiCalendar);
        assertEquals("first day in first week", uiCalendar.getTime(), endOfWeek); 
     }


    /**
     * Test day at location: hitting days of week must return same as "nearest"
     * non-header cell.
     */
    @Test
    public void testDayAtLocationDayHeaderNull() {
        // This test will not work in a headless configuration.
        if (GraphicsEnvironment.isHeadless()) {
            LOG.fine("cannot run test - headless environment");
            return;
        }
        BasicCalendarUI ui = getRealizedMonthViewUI(ComponentOrientation.LEFT_TO_RIGHT);
        Rectangle monthBounds = ui.getPageBounds();
        // same for LToR
        Calendar header = ui.getCellAtLocation(
                monthBounds.x + 2, 
                monthBounds.y + ui.getPageHeaderHeight() + 2); 
        Calendar cell = ui.getCellAtLocation(
                monthBounds.x + 2, 
                monthBounds.y + ui.getPageHeaderHeight() + 2 + ui.getDaySize().height); 
        assertEquals("hitting days-of-week must return nearest non-header calendar", 
                cell.getTime(), header.getTime());
     }
    
    @Test
    public void testDayBounds() {
        // This test will not work in a headless configuration.
        if (GraphicsEnvironment.isHeadless()) {
            LOG.fine("cannot run test - headless environment");
            return;
        }
        BasicCalendarUI uiRToL = getRealizedMonthViewUI(ComponentOrientation.RIGHT_TO_LEFT);
        BasicCalendarUI uiLToR = getRealizedMonthViewUI(ComponentOrientation.LEFT_TO_RIGHT);
        Rectangle monthBounds = uiRToL.getPageBounds();
        assertNull("hit in header must return null bounds", 
                uiRToL.getCellBoundsAtLocation(monthBounds.x + 2, monthBounds.y + 2));
        // first column first row
        Rectangle dayBoundsRToL = uiRToL.getCellBoundsAtLocation(
                monthBounds.x + 2, monthBounds.y + uiRToL.getPageHeaderHeight()); 
        // same for LToR
        Rectangle dayBoundsLToR = uiLToR.getCellBoundsAtLocation(
                monthBounds.x + 2, monthBounds.y + uiLToR.getPageHeaderHeight()); 
        assertEquals("day bounds must be independent of orientation", 
                dayBoundsLToR, dayBoundsRToL);
        assertEquals(monthBounds.x, dayBoundsLToR.x);
        assertEquals(monthBounds.y + uiLToR.getPageHeaderHeight(), dayBoundsLToR.y);
    }
 
//------------------------ methods to test getDayGridPositionAtLocation (bulk)
// PENDING JW:  should we test the corner cases separatedly? Had been historical
    
    /**
     * Map pixel to logical grid, test rows. 
     */
    @Test
    public void testDayGridPositionAtLocationRows() {
        // This test will not work in a headless configuration.
        if (GraphicsEnvironment.isHeadless()) {
            LOG.fine("cannot run test - headless environment");
            return;
        }
        BasicCalendarUI ui = getRealizedMonthViewUI(ComponentOrientation.RIGHT_TO_LEFT);
        int calendarRow = BasicCalendarUI.DAY_HEADER_ROW;
        for (int row = 0; row <= BasicCalendarUI.WEEKS_IN_MONTH; row++) {
            Point location = getLocationInRow(ui, row);
            // first row below month header == days of week header 
            Point dayGridPosition = ui.getCellGridPositionAtLocation(location.x, location.y); 
            assertEquals("calendarRow at absolute row " + row, 
                    calendarRow, dayGridPosition.y);
            calendarRow++;
        }
     }

    /**
     * Map pixel to logical grid, test columns in LToR. 
     * 
     */
    @Test
    public void testDayGridPositionAtLocationColumnsLToR() {
        // This test will not work in a headless configuration.
        if (GraphicsEnvironment.isHeadless()) {
            LOG.fine("cannot run test - headless environment");
            return;
        }
        BasicCalendarUI ui = getRealizedMonthViewUI(ComponentOrientation.LEFT_TO_RIGHT);
        int calendarColumn = BasicCalendarUI.FIRST_DAY_COLUMN;
        for (int column = 0; column < BasicCalendarUI.DAYS_IN_WEEK; column++) {
            Point location = getLocationInColumn(ui, column);
            // first row below month header == days of week header 
            Point dayGridPosition = ui.getCellGridPositionAtLocation(location.x, location.y); 
            assertEquals("calendarColumn at absolute column " + column, 
                    calendarColumn, dayGridPosition.x);
            calendarColumn++;
        }
     }
    
    /**
     * Map pixel to logical grid, test columns with weekNumbers in LToR. 
     */
    @Test
    public void testDayGridPositionAtLocationColumnsLToRWithWeekNumber() {
        // This test will not work in a headless configuration.
        if (GraphicsEnvironment.isHeadless()) {
            LOG.fine("cannot run test - headless environment");
            return;
        }
        BasicCalendarUI ui = getRealizedMonthViewUI(ComponentOrientation.LEFT_TO_RIGHT, true);
        int calendarColumn = BasicCalendarUI.WEEK_HEADER_COLUMN;
        for (int column = 0; column <= BasicCalendarUI.DAYS_IN_WEEK; column++) {
            Point location = getLocationInColumn(ui, column);
            // first row below month header == days of week header 
            Point dayGridPosition = ui.getCellGridPositionAtLocation(location.x, location.y); 
            assertEquals("calendarColumn at absolute column " + column, 
                    calendarColumn, dayGridPosition.x);
            calendarColumn++;
        }
     }

    /**
     * Map pixel to logical grid, test columns in RToL. 
     */
    @Test
    public void testDayGridPositionAtLocationColumnsRToL() {
        // This test will not work in a headless configuration.
        if (GraphicsEnvironment.isHeadless()) {
            LOG.fine("cannot run test - headless environment");
            return;
        }
        BasicCalendarUI ui = getRealizedMonthViewUI(ComponentOrientation.RIGHT_TO_LEFT);
        int calendarColumn = BasicCalendarUI.LAST_DAY_COLUMN;
        for (int column = 0; column < BasicCalendarUI.DAYS_IN_WEEK; column++) {
            Point location = getLocationInColumn(ui, column);
            // first row below month header == days of week header 
            Point dayGridPosition = ui.getCellGridPositionAtLocation(location.x, location.y); 
            assertEquals("calendarColumn at absolute column " + column, 
                    calendarColumn, dayGridPosition.x);
            calendarColumn--;
        }
     }
    
    /**
     * Map pixel to logical grid, test columns with weekNubmers in RToL. 
     */
    @Test
    public void testDayGridPositionAtLocationColumnsRToLWithWeekNumber() {
        // This test will not work in a headless configuration.
        if (GraphicsEnvironment.isHeadless()) {
            LOG.fine("cannot run test - headless environment");
            return;
        }
        BasicCalendarUI ui = getRealizedMonthViewUI(ComponentOrientation.RIGHT_TO_LEFT, true);
        int calendarColumn = BasicCalendarUI.LAST_DAY_COLUMN;
        for (int column = 0; column <= BasicCalendarUI.DAYS_IN_WEEK; column++) {
            Point location = getLocationInColumn(ui, column);
            // first row below month header == days of week header 
            Point dayGridPosition = ui.getCellGridPositionAtLocation(location.x, location.y); 
            assertEquals("calendarColumn at absolute column " + column, 
                    calendarColumn, dayGridPosition.x);
            calendarColumn--;
        }
     }

//----------------- test get DayGridPositionAtLocation (special boundary points)
    
    /**
     * Map pixel to logical grid. 
     * day grid row == DAY_HEADER_ROW.
     */
    @Test
    public void testDayGridPositionAtLocationDayHeader() {
        // This test will not work in a headless configuration.
        if (GraphicsEnvironment.isHeadless()) {
            LOG.fine("cannot run test - headless environment");
            return;
        }
        BasicCalendarUI ui = getRealizedMonthViewUI(ComponentOrientation.RIGHT_TO_LEFT);
        Point location = getLocationInRow(ui, 0);
        // first row below month header == days of week header 
        Point dayGridPosition = ui.getCellGridPositionAtLocation(location.x, location.y); 
        assertEquals("first row below header must be day column header", 
                BasicCalendarUI.DAY_HEADER_ROW, dayGridPosition.y);
     }

    /**
     * Map pixel to logical grid. 
     * day grid rows == FIRST_WEEK_ROW
     */
    @Test
    public void testDayGridPositionAtLocationFirstWeekRow() {
        // This test will not work in a headless configuration.
        if (GraphicsEnvironment.isHeadless()) {
            LOG.fine("cannot run test - headless environment");
            return;
        }
        BasicCalendarUI ui = getRealizedMonthViewUI(ComponentOrientation.RIGHT_TO_LEFT);
        // location in second (geometrical, calculated from day size) grid row
        Point location = getLocationInRow(ui, 1);
        Point dayInGrid = ui.getCellGridPositionAtLocation(location.x, location.y); 
       
        assertEquals("first row", BasicCalendarUI.FIRST_WEEK_ROW, dayInGrid.y);
     }
 
    /**
     * Map pixel to logical grid. 
     * day grid rows == LAST_WEEK_ROW
     */
    @Test
    public void testDayGridPositionAtLocationLastWeekRow() {
        // This test will not work in a headless configuration.
        if (GraphicsEnvironment.isHeadless()) {
            LOG.fine("cannot run test - headless environment");
            return;
        }
        BasicCalendarUI ui = getRealizedMonthViewUI(ComponentOrientation.RIGHT_TO_LEFT);
        Point location = getLocationInRow(ui, BasicCalendarUI.WEEKS_IN_MONTH); 
        Point dayInGrid = ui.getCellGridPositionAtLocation(location.x, location.y);
       
        assertEquals("first row", BasicCalendarUI.LAST_WEEK_ROW, dayInGrid.y);
     }

    
    /**
     * Map pixel to logical grid. 
     * day grid column == LAST_DAY_COLUMN in RToL and FIRST_DAY_COLUMN in LToR
     */
    @Test
    public void testDayGridPositionAtLocationLastColumnRToL() {
        // This test will not work in a headless configuration.
        if (GraphicsEnvironment.isHeadless()) {
            LOG.fine("cannot run test - headless environment");
            return;
        }
        BasicCalendarUI ui = getRealizedMonthViewUI(ComponentOrientation.RIGHT_TO_LEFT);
        Point location = getLocationInColumn(ui, 0);
        Point dayGridPosition = ui.getCellGridPositionAtLocation(location.x, location.y); 
        assertEquals("last logical column in RToL", BasicCalendarUI.LAST_DAY_COLUMN, 
                dayGridPosition.x);
     }

    /**
     * Map pixel to logical grid. 
     * day grid column == LAST_DAY_COLUMN in RToL and FIRST_DAY_COLUMN in LToR
     */
    @Test
    public void testDayGridPositionAtLocationFirstColumnRToL() {
        // This test will not work in a headless configuration.
        if (GraphicsEnvironment.isHeadless()) {
            LOG.fine("cannot run test - headless environment");
            return;
        }
        BasicCalendarUI ui = getRealizedMonthViewUI(ComponentOrientation.RIGHT_TO_LEFT);
        Point location = getLocationInColumn(ui, BasicCalendarUI.DAYS_IN_WEEK - 1);
        Point dayGridPosition = ui.getCellGridPositionAtLocation(location.x, location.y); 
        assertEquals("last logical column in RToL", BasicCalendarUI.FIRST_DAY_COLUMN, 
                dayGridPosition.x);
     }

    /**
     * Map pixel to logical grid. 
     * day grid column == LAST_DAY_COLUMN in RToL and FIRST_DAY_COLUMN in LToR
     */
    @Test
    public void testDayGridPositionAtLocationFirstColumnRToLWithWeekNumber() {
        // This test will not work in a headless configuration.
        if (GraphicsEnvironment.isHeadless()) {
            LOG.fine("cannot run test - headless environment");
            return;
        }
        BasicCalendarUI ui = getRealizedMonthViewUI(ComponentOrientation.RIGHT_TO_LEFT, true);
        Point location = getLocationInColumn(ui, BasicCalendarUI.DAYS_IN_WEEK - 1);
        Point dayGridPosition = ui.getCellGridPositionAtLocation(location.x, location.y); 
        assertEquals("last logical column in RToL", BasicCalendarUI.FIRST_DAY_COLUMN, 
                dayGridPosition.x);
     }

    /**
     * Map pixel to logical grid. 
     * day grid column == LAST_DAY_COLUMN in RToL and FIRST_DAY_COLUMN in LToR
     */
    @Test
    public void testDayGridPositionAtLocationFirstColumnLToR() {
        // This test will not work in a headless configuration.
        if (GraphicsEnvironment.isHeadless()) {
            LOG.fine("cannot run test - headless environment");
            return;
        }
        BasicCalendarUI ui = getRealizedMonthViewUI(ComponentOrientation.LEFT_TO_RIGHT);
        Point location = getLocationInColumn(ui, 0);
        Point dayGridPosition = ui.getCellGridPositionAtLocation(location.x, location.y);
        assertEquals("first logical column in LToR", BasicCalendarUI.FIRST_DAY_COLUMN, dayGridPosition.x);
     }
    
    /**
     * Map pixel to logical grid. 
     * day grid column == LAST_DAY_COLUMN in RToL and FIRST_DAY_COLUMN in LToR
     */
    @Test
    public void testDayGridPositionAtLocationLastColumnLToR() {
        // This test will not work in a headless configuration.
        if (GraphicsEnvironment.isHeadless()) {
            LOG.fine("cannot run test - headless environment");
            return;
        }
        BasicCalendarUI ui = getRealizedMonthViewUI(ComponentOrientation.LEFT_TO_RIGHT);
        Point location = getLocationInColumn(ui, BasicCalendarUI.DAYS_IN_WEEK - 1);
        Point dayGridPosition = ui.getCellGridPositionAtLocation(location.x, location.y);
        assertEquals("first logical column in LToR", BasicCalendarUI.LAST_DAY_COLUMN, dayGridPosition.x);
     }
    /**
     * Map pixel to logical grid. 
     * day grid column == LAST_DAY_COLUMN in RToL and FIRST_DAY_COLUMN in LToR
     */
    @Test
    public void testDayGridPositionAtLocationWeekHeaderRToL() {
        // This test will not work in a headless configuration.
        if (GraphicsEnvironment.isHeadless()) {
            LOG.fine("cannot run test - headless environment");
            return;
        }
        BasicCalendarUI ui = getRealizedMonthViewUI(ComponentOrientation.RIGHT_TO_LEFT, true);
        // right == week header
        Point location = getLocationInColumn(ui, BasicCalendarUI.DAYS_IN_WEEK); 
        Point dayGridPosition = ui.getCellGridPositionAtLocation(location.x, location.y);
        assertEquals("weeks of year column in RTL", BasicCalendarUI.WEEK_HEADER_COLUMN, 
                dayGridPosition.x);
     }

    
    /**
     * Map pixel to logical grid. 
     * day grid column == LAST_DAY_COLUMN in RToL and FIRST_DAY_COLUMN in LToR
     */
    @Test
    public void testDayGridPositionAtLocationWeekHeaderLToR() {
        // This test will not work in a headless configuration.
        if (GraphicsEnvironment.isHeadless()) {
            LOG.fine("cannot run test - headless environment");
            return;
        }
        BasicCalendarUI ui = getRealizedMonthViewUI(ComponentOrientation.LEFT_TO_RIGHT, true);
        // left == week header
        Point location = getLocationInColumn(ui, 0);
        Point dayGridPosition = ui.getCellGridPositionAtLocation(location.x, location.y); 
        assertEquals("first logical column in LToR", BasicCalendarUI.WEEK_HEADER_COLUMN, dayGridPosition.x);
     }

    /**
     * day grid returns null for hitting month header.
     */
    @Test
    public void testDayGridPositionAtLocationMonthHeaderHitLToR() {
        // This test will not work in a headless configuration.
        if (GraphicsEnvironment.isHeadless()) {
            LOG.fine("cannot run test - headless environment");
            return;
        }
        BasicCalendarUI ui = getRealizedMonthViewUI(ComponentOrientation.LEFT_TO_RIGHT);
        Point location = getLocationInMonthHeader(ui);
        assertNull("hit in header must return null grid position", 
                ui.getCellGridPositionAtLocation(location.x, location.y));
    }
    
    /**
     * day grid returns null for hitting month header.
     */
    @Test
    public void testDayGridPositionAtLocationMonthHeaderHitRToL() {
        // This test will not work in a headless configuration.
        if (GraphicsEnvironment.isHeadless()) {
            LOG.fine("cannot run test - headless environment");
            return;
        }
        BasicCalendarUI ui = getRealizedMonthViewUI(ComponentOrientation.RIGHT_TO_LEFT);
        Point location = getLocationInMonthHeader(ui);
        assertNull("hit in header must return null grid position", 
                ui.getCellGridPositionAtLocation(location.x, location.y));
    }
    
    /**
     * coordinate mapping: monthBounds in pixel.
     * 
     */
    @Test
    public void testMonthBoundsAtLocation() {
        // This test will not work in a headless configuration.
        if (GraphicsEnvironment.isHeadless()) {
            LOG.fine("cannot run test - headless environment");
            return;
        }
        BasicCalendarUI uiRToL = getRealizedMonthViewUI(ComponentOrientation.RIGHT_TO_LEFT);
        BasicCalendarUI uiLToR = getRealizedMonthViewUI(ComponentOrientation.LEFT_TO_RIGHT);
        Rectangle monthBoundsRToL = uiRToL.getPageBounds();
        Rectangle monthBoundsLToR = uiLToR.getPageBounds();
        // bounds of first
        assertEquals("bounds of left-most month must be equal", 
                monthBoundsLToR, monthBoundsRToL);
        Rectangle monthBoundsTwoRToL = uiRToL.getPageBounds();
        Rectangle monthBoundsTwoLToR = uiRToL.getPageBounds();
        assertEquals("bounds of right-most month must be equal", 
                monthBoundsTwoLToR, monthBoundsTwoRToL);
        
    }

    
    
    /**
     * @param ui
     * @return
     */
    private Point getLocationInMonthHeader(BasicCalendarUI ui) {
         Rectangle monthBounds = ui.getPageBounds();
        return new Point(monthBounds.x + 2, monthBounds.y + 2);
    }

    /**
     * Returns a location in the given day grid row/column (absolute coordinates)
     * 
     * @param ui the ui to get the location from
     * @param columnOfDayBoxes the absolute grid column
     * @return a location in pixel, calcualated from the month location and
     *    day size.
     */
    private Point getLocationInGrid(BasicCalendarUI ui, int rowOfDayBoxes, int columnOfDayBoxes) {
        Rectangle monthBounds = ui.getPageBounds();
        Dimension dayBounds = ui.getDaySize();
        Point location = new Point(
                monthBounds.x + columnOfDayBoxes * dayBounds.width + 2, 
                monthBounds.y + ui.getPageHeaderHeight() + rowOfDayBoxes * dayBounds.height + 2);
        return location;
    }

    /**
     * Returns a location in the given day grid column (absolute coordinates)
     * 
     * @param ui the ui to get the location from
     * @param columnOfDayBoxes the absolute grid column
     * @return a location in pixel, calcualated from the month location and
     *    day size.
     */
    private Point getLocationInColumn(BasicCalendarUI ui, int columnOfDayBoxes) {
        Rectangle monthBounds = ui.getPageBounds();
        Dimension dayBounds = ui.getDaySize();
        Point location = new Point(
                monthBounds.x + columnOfDayBoxes * dayBounds.width + 2, 
                monthBounds.y + ui.getPageHeaderHeight() + 2);
        return location;
    }
    
    /**
     * Returns a location in the given day grid row (absolute coordinates)
     * 
     * @param ui
     * @param rowOfDayBoxes the absolute grid row
     * @return a location in pixel, calcualated from the month location and
     *    day size.
     */
    private Point getLocationInRow(BasicCalendarUI ui, int rowOfDayBoxes) {
        Rectangle monthBounds = ui.getPageBounds();
        Dimension dayBounds = ui.getDaySize();
        Point location = new Point(
            monthBounds.x + 2, 
            monthBounds.y + ui.getPageHeaderHeight() + rowOfDayBoxes * dayBounds.height + 2);
        return location;
    }

   

    /**
     * Returns the ui of a realized JXCalendar with 2 columns and the 
     * given componentOrientation without showingWeekNumbers.
     * 
     * NOTE: this must not be used in a headless environment.
     * 
     * @param co
     * @return
     */
    private BasicCalendarUI getRealizedMonthViewUI(ComponentOrientation co) {
        return getRealizedMonthViewUI(co, false);
    }

    /**
     * Returns the ui of a realized JXCalendar with
     * given componentOrientation and showingWeekNumbers flag.
     * It's prefColumns/Rows are set to 2. The first displayedDate is 
     * 20. Feb. 2008 (to have fixed leading/trailing dates)
     * 
     * The frame is packed and it's size extended by 40, 40 to
     * give a slight off-position (!= 0) of the months shown. 
     * 
     * NOTE: this must not be used in a headless environment.
     * 
     * @param co the componentOrientation to use
     * @return
     */
    private BasicCalendarUI getRealizedMonthViewUI(ComponentOrientation co,
            boolean isShowingWeekNumbers) {
        JXCalendar monthView = new JXCalendar();
        if (co != null)
            monthView.setComponentOrientation(co);
        monthView.setShowingWeekNumber(isShowingWeekNumbers);
        Calendar calendar = monthView.getPage();
        calendar.set(2008, Calendar.FEBRUARY, 20);
        monthView.setSelectionDate(calendar.getTime());
        JXFrame frame = new JXFrame();
        frame.add(monthView);
        frame.pack();
        frame.setSize(frame.getWidth() + 40, frame.getHeight() + 40);
        frame.setVisible(true);
        BasicCalendarUI ui = (BasicCalendarUI) monthView.getUI();
        return ui;
    }

    /**
     * cleanup date representation as long: new api getDayAtLocation. will
     * replace getDayAt which is deprecated as a first step.
     */
    @Test
    public void testGetDayAtLocation() {
        // This test will not work in a headless configuration.
        if (GraphicsEnvironment.isHeadless()) {
            LOG.fine("cannot run test - headless environment");
            return;
        }
        JXCalendar monthView = new JXCalendar();
        monthView.getSelectionModel().setMinimalDaysInFirstWeek(1);
        JXFrame frame = new JXFrame();
        frame.add(monthView);
        frame.pack();
        Dimension pref = monthView.getPreferredSize();
        pref.width = pref.width / 2;
        pref.height = pref.height / 2;
        Calendar date = monthView.getCellAtLocation(pref.width, pref.height);
        assertNotNull(date);
    }

    

//    /**
//     * Issue #708-swingx: updateUI changes state.
//     * 
//     * Here: test that firstDisplayedMonth is unchanged.
//     */
//    @Test
//    public void testUpdateUIFirstMonth() {
//        Calendar cal = Calendar.getInstance();
//        cal.set(Calendar.MONTH, 5);
//        // need to instantiate with a month different from jan
//        final JXCalendar monthView = new JXCalendar(cal.getTime());
//        long first = ((BasicCalendarUI) monthView.getUI()).getFirstDisplayedMonth();
//        monthView.updateUI();
//        assertEquals(first, ((BasicCalendarUI) monthView.getUI()).getFirstDisplayedMonth());
//    };
    

}
