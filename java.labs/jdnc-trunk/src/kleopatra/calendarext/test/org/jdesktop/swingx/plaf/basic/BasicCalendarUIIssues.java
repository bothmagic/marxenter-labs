/*
 * $Id: BasicMonthViewUIIssues.java 3396 2009-07-20 12:20:34Z kleopatra $
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

import static org.junit.Assert.*;

import java.awt.ComponentOrientation;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.logging.Logger;

import javax.swing.CellRendererPane;
import javax.swing.JComponent;

import org.jdesktop.swingx.InteractiveTestCase;
import org.jdesktop.swingx.JXCalendar;
import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXHyperlink;
import org.jdesktop.swingx.calendar.CalendarCellState;
import org.jdesktop.swingx.calendar.CalendarUtils;
import org.jdesktop.swingx.calendar.DateSelectionModel;
import org.jdesktop.swingx.calendar.DaySelectionModel;
import org.jdesktop.swingx.calendar.FieldType;
import org.jdesktop.test.ActionReport;
import org.jdesktop.test.EDTRunner;
import org.jdesktop.test.PropertyChangeReport;
import org.jdesktop.test.TestUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests to expose known issues of BasicCalendarUI.
 * 
 * @author Jeanette Winzenburg
 */
@RunWith(EDTRunner.class)
public class BasicCalendarUIIssues extends InteractiveTestCase {
    @SuppressWarnings("all")
    private static final Logger LOG = Logger
            .getLogger(BasicCalendarUIIssues.class.getName());

    public static void main(String[] args) {
//      setSystemLF(true);
      BasicCalendarUIIssues  test = new BasicCalendarUIIssues();
      try {
          test.runInteractiveTests();
//        test.runInteractiveTests(".*Simple.*");
      } catch (Exception e) {
          System.err.println("exception when executing interactive tests:");
          e.printStackTrace();
      }
  }

    // pre-defined reference dates - all relative to current date at around 5 am
    private Date today;
    private Date tomorrow;
    private Date afterTomorrow;
    private Date yesterday;
    // calendar default instance init with today
    private Calendar calendar;

    // calendarView default instantiated
    private JXCalendar calendarView;
    //the ui of the calendarView, type casted for convenience
    private BasicCalendarUI calendarUI;
    
    @Test (expected = IllegalArgumentException.class)
    public void testThrowOnInvalidRowCoordinate() {
        calendarUI.getCell(calendarUI.getRowCount(), 0);
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void testThrowOnInvalidColumnCoordinate() {
        calendarUI.getCell(0, calendarUI.getColumnCount());
    }
    
    @Test
    public void testHeaderZoomoutDisabledOnDecade() {
        JXHyperlink link = findHeaderHyperlink();
        while (FieldType.DECADE != calendarUI.getPageType()) {
            calendarUI.performAction(Navigator.ZOOM_OUT_KEY);
        }
        assertEquals(false, link.isEnabled());
        
    }
    
    private JXHyperlink findHeaderHyperlink() {
        JComponent header = null;
        for (int i = 0; i < calendarView.getComponentCount(); i++) {
            if (!(calendarView.getComponent(i) instanceof CellRendererPane)) {
                header = (JComponent) calendarView.getComponent(i);
                break;
            }
        }
        return (JXHyperlink) header.getComponent(2);
    }

    
    @Test
    public void testZoomoutWrapperEnabledOnNotDecade() {
        // initialize to DECADE
        while (FieldType.DECADE != calendarUI.getPageType()) {
            calendarUI.performAction(Navigator.ZOOM_OUT_KEY);
        }
        // zoomin once
        calendarUI.performAction(Navigator.ZOOM_IN_KEY);
        assertEquals(true, calendarView.getActionMap().get(CalendarHeader.ZOOM_OUT_KEY).isEnabled());
    }
    
    @Test
    public void testZoomoutWrapperDisabledOnDecade() {
        while (FieldType.DECADE != calendarUI.getPageType()) {
            calendarUI.performAction(Navigator.ZOOM_OUT_KEY);
        }
        assertEquals(false, calendarView.getActionMap().get(CalendarHeader.ZOOM_OUT_KEY).isEnabled());
    }
    
    

    @Test
    public void testZoomoutDisabledOnDecade() {
        while (FieldType.DECADE != calendarUI.getPageType()) {
            calendarUI.performAction(Navigator.ZOOM_OUT_KEY);
        }
        assertEquals(false, calendarView.getActionMap().get(Navigator.ZOOM_OUT_KEY).isEnabled());
    }
    
    
    @Test
    public void testZoomoutEnabledOnNotDecade() {
        // initialize to DECADE
        while (FieldType.DECADE != calendarUI.getPageType()) {
            calendarUI.performAction(Navigator.ZOOM_OUT_KEY);
        }
        // zoomin once
        calendarUI.performAction(Navigator.ZOOM_IN_KEY);
        assertEquals(true, calendarView.getActionMap().get(Navigator.ZOOM_OUT_KEY).isEnabled());
    }
    
    @Test
    public void testTodayCellState() {
        Point todayCell = getTodayCell(calendarView);
        assertSame("today " + today,
                CalendarCellState.TODAY_CELL, 
                calendarUI.getCellState(todayCell.y, todayCell.x));
    }
    
    /**
     * Working around missing back-conversion (Date --> logical cell coordinates).
     * @param calendarView
     * @return
     */
    private Point getTodayCell(JXCalendar calendarView) {
        for (int row = BasicCalendarUI.FIRST_WEEK_ROW; row <= BasicCalendarUI.LAST_WEEK_ROW; row++) {
            for (int column = BasicCalendarUI.FIRST_DAY_COLUMN; column <= BasicCalendarUI.LAST_DAY_COLUMN; column++) {
                Calendar calendar = calendarUI.getCell(row, column);
                if (CalendarUtils.isSameDay(calendar, today)) {
                    return new Point(column, row);
                }
                
            }
        }
        return null;
    }


    @Test
    public void testRemoveHeaderOnUpdateUI() {
        int childCount = calendarView.getComponentCount();
        assertEquals("sanity: two children (header and rendererPane)", 2, childCount);
        calendarView.updateUI();
        // PENDING JW: the actual removal is done by the CalendarHeader.uninstall
        // that's asymetric to install (added by the ui) - revisit!
        assertEquals("old header must be removed", 2, 
                calendarView.getComponentCount());
    }
    
    @Test
    public void testUnselectableLead() {
        calendarView.setSelectionDate(today);
        calendarView.setUnselectableDates(tomorrow);
        calendarUI.performAction(Navigator.NEXT_CELL_KEY);
        assertTrue("same day: unselectable/lead " + tomorrow + " / " + calendarUI.getLead().getTime(), 
                CalendarUtils.isSameDay(calendarUI.getLead(), tomorrow));
    }
    
    @Test
    public void testUnselectableLeadMustNotCommit() {
        calendarView.setSelectionDate(today);
        calendarView.setUnselectableDates(tomorrow);
        calendarUI.performAction(Navigator.NEXT_CELL_KEY);
        ActionReport report = new ActionReport();
        calendarView.addActionListener(report);
        calendarUI.performAction(JXCalendar.COMMIT_KEY);
        assertEquals("commit on unselectable must fail", 0, report.getEventCount());
    }
    
    @Test
    public void testFlushedPage() {
        Calendar calendar = calendarUI.getPage();
        assertTrue("expected flushed, but was: " + calendar, CalendarUtils.isFlushed(calendar));
    }
    
    @Test
    public void testNavigatorCalendarSynchedOnUpdateUI() {
        TimeZone tz = TimeZone.getTimeZone("GMT+4");
        if (calendarView.getTimeZone().equals(tz)) {
            tz = TimeZone.getTimeZone("GMT+5");
        }
        DateSelectionModel model = new DaySelectionModel();
        model.setTimeZone(tz);
        calendarView.setSelectionModel(model);
        calendarView.updateUI();
        assertNotSame(calendarUI, calendarView.getUI());
        assertEquals(tz, ((BasicCalendarUI)calendarView.getUI()).navigator.getLead().getTimeZone());
    }
    
    
    @Test
    public void testNavigatorCalendarSynchedOnSetModel() {
        TimeZone tz = TimeZone.getTimeZone("GMT+4");
        if (calendarView.getTimeZone().equals(tz)) {
            tz = TimeZone.getTimeZone("GMT+5");
        }
        DateSelectionModel model = new DaySelectionModel();
        model.setTimeZone(tz);
        calendarView.setSelectionModel(model);
        assertEquals(tz, calendarUI.navigator.getLead().getTimeZone());
    }
    
    @Test
    public void testNavigatorCalendarSynchedOnFirstDayOfWeek() {
        int old = calendarView.getFirstDayOfWeek();
        calendarView.setSelectionDate(afterTomorrow);
        calendarView.setFirstDayOfWeek(old +1);
        assertEquals(calendarView.getSelectionDate(), calendarUI.navigator.getLeadDate());
        assertEquals(old +1, calendarUI.navigator.getLead().getFirstDayOfWeek());
    }
    
    @Test
    public void testNavigatorCalendarSynchedOnTimeZone() {
        TimeZone tz = TimeZone.getTimeZone("GMT+4");
        if (calendarView.getTimeZone().equals(tz)) {
            tz = TimeZone.getTimeZone("GMT+5");
        }
        TimeZone old = calendarView.getPage().getTimeZone();
        assertEquals(old, calendarUI.navigator.getLead().getTimeZone());
        calendarView.setTimeZone(tz);
        assertEquals(tz, calendarUI.navigator.getLead().getTimeZone());
    }
    
    @Test
    public void testAutoScrollOnSetModel() {
        calendarView.setSelectionDate(today);
        DateSelectionModel model = new DaySelectionModel();
        Calendar viewPage = calendarView.getPage();
        viewPage.add(Calendar.MONTH, 2);
        model.setSelectionInterval(viewPage.getTime(), viewPage.getTime());
        calendarView.setSelectionModel(model);
        assertEquals("same after change selection", 
                viewPage.getTime(), calendarView.getPage().getTime());
    }
    
    @Test
    public void testSynchLeadSelectionOnSetModel() {
        calendarView.setSelectionDate(today);
        DateSelectionModel model = new DaySelectionModel();
        Calendar viewPage = calendarView.getPage();
        viewPage.add(Calendar.MONTH, 2);
        model.setSelectionInterval(viewPage.getTime(), viewPage.getTime());
        calendarView.setSelectionModel(model);
        assertEquals(calendarView.getSelectionDate(), 
                ((BasicCalendarUI) calendarView.getUI()).navigator.getLeadDate());
    }
    
    @Test
    public void testCellStatesInMonth() {
        assertSame(CalendarCellState.DAY_OF_WEEK_TITLE, calendarUI.getCellState(0, 1));
        assertSame(CalendarCellState.WEEK_OF_YEAR_TITLE, calendarUI.getCellState(1, 0));
        assertSame(CalendarCellState.DAY_CELL, calendarUI.getCellState(2, 1));
        // last row last column is always off
        assertSame(CalendarCellState.DAY_OFF_CELL, calendarUI.getCellState(
                calendarUI.getRowCount() - 1, calendarUI.getColumnCount() - 1));
    }
    
    @Test
    public void testCellStatesInYear() {
        calendarUI.performAction(Navigator.ZOOM_OUT_KEY);
        assertSame(CalendarCellState.MONTH_CELL, calendarUI.getCellState(0, 0));
        assertSame(CalendarCellState.MONTH_CELL, calendarUI.getCellState(
                calendarUI.getRowCount() - 1, calendarUI.getColumnCount() -1 ));
    }
    
    @Test
    public void testCellStatesInDecade() {
        calendarUI.performAction(Navigator.ZOOM_OUT_KEY);
        calendarUI.performAction(Navigator.ZOOM_OUT_KEY);
        LOG.info("firstCell/page: " + calendarUI.getCell(0, 0));
        assertSame(CalendarCellState.YEAR_OFF_CELL, calendarUI.getCellState(0, 0));
        assertSame(CalendarCellState.YEAR_CELL, calendarUI.getCellState(1, 1));
        assertSame(CalendarCellState.YEAR_OFF_CELL, calendarUI.getCellState(
                calendarUI.getRowCount() - 1, calendarUI.getColumnCount() -1 ));
    }
    
    @Test
    public void testPageType() {
        assertSame(FieldType.MONTH, calendarView.getUI().getPageType());
        calendarUI.performAction(Navigator.ZOOM_OUT_KEY);
        assertSame(FieldType.YEAR, calendarView.getUI().getPageType());
        calendarUI.performAction(Navigator.ZOOM_OUT_KEY);
        assertSame(FieldType.DECADE, calendarView.getUI().getPageType());
    }
    
    @Test
    public void testColumnCounts() {
        assertEquals(BasicCalendarUI.DAYS_IN_WEEK + 1, calendarUI.getColumnCount());
        calendarUI.performAction(Navigator.ZOOM_OUT_KEY);
        assertEquals(4, calendarUI.getColumnCount());
        calendarUI.performAction(Navigator.ZOOM_OUT_KEY);
        assertEquals(4, calendarUI.getColumnCount());
    }
    
    @Test
    public void testRowCounts() {
        assertEquals(BasicCalendarUI.WEEKS_IN_MONTH + 1, calendarUI.getRowCount());
        calendarUI.performAction(Navigator.ZOOM_OUT_KEY);
        assertEquals(3, calendarUI.getRowCount());
        calendarUI.performAction(Navigator.ZOOM_OUT_KEY);
        assertEquals(3, calendarUI.getRowCount());
    }
    
    @Test
    public void testCellDateInDecade() {
        calendarUI.performAction(Navigator.ZOOM_OUT_KEY);
        calendarUI.performAction(Navigator.ZOOM_OUT_KEY);
        LOG.info("firstCell/page: " + calendarUI.getCell(0, 0).getTime());
        Calendar page = calendarUI.getPage();
        page.add(Calendar.YEAR, -1);
        assertEquals(page.getTime(), calendarUI.getCell(0, 0).getTime());
        
    }
    
    
    @Test
    public void testPageTypeScrollNotification() {
        PropertyChangeReport report = new PropertyChangeReport(calendarView);
        calendarUI.performAction(Navigator.ZOOM_OUT_KEY);
        TestUtils.assertPropertyChangeEvent(report, "pageType", 
                FieldType.MONTH, FieldType.YEAR, false);
        report.clear();
        calendarUI.performAction(Navigator.ZOOM_OUT_KEY);
        TestUtils.assertPropertyChangeEvent(report, "pageType", 
                FieldType.YEAR, FieldType.DECADE, false);
    }
    
    @Test
    public void testPage() {
        Calendar viewPage = calendarView.getPage();
        assertEquals("sanity: same on startup", 
                 viewPage.getTime(), calendarView.getUI().getPage().getTime());
        viewPage.add(Calendar.MONTH, 2);
        calendarView.setSelectionDate(viewPage.getTime());
        assertEquals("same after change selection", 
                viewPage.getTime(), calendarView.getUI().getPage().getTime());
    }
    
    @Test
    public void testAutoscrollOnSelection() {
        Calendar viewPage = calendarView.getPage();
        viewPage.add(Calendar.MONTH, 2);
        calendarView.setSelectionDate(viewPage.getTime());
        assertEquals("same after change selection", 
                viewPage.getTime(), calendarView.getPage().getTime());
        
    }
    
    @Test
    public void testSynchLeadSelection() {
        calendarView.setSelectionDate(tomorrow);
        assertEquals(calendarView.getSelectionDate(), 
                ((BasicCalendarUI) calendarView.getUI()).navigator.getLeadDate());
    }
    
    @Test
    public void testDetailsSize() {
        // This test will not work in a headless configuration.
        if (GraphicsEnvironment.isHeadless()) {
            LOG.fine("cannot run ui test - headless environment");
            return;
        }
        BasicCalendarUI ui = getRealizedMonthViewUI(null);
        assertTrue("day size: " + ui.getDaySize(), ui.getDaySize().width > 0);
        assertEquals("details width", ui.getPageSize().width / 4, ui.getDetailsSize().width);
        assertEquals("details height", (ui.getDaySize().height * 7)/ 3, ui.getDetailsSize().height);
    }
    
    /**
     * Issue #786-swingx: IllegalStateException when paintDays of April 2008.
     * 
     * Set the default timezone and get the default calendar.
     * 
     */
    public void testTimeZoneCairoCalendarUtils() {
        TimeZone cairo = TimeZone.getTimeZone("Africa/Cairo");
        Calendar calendar = Calendar.getInstance(cairo);
        assertEquals(cairo, calendar.getTimeZone());
        calendar.set(2008, Calendar.MARCH, 31);
        CalendarUtils.startOfMonth(calendar);
        calendar.add(Calendar.MONTH, 1);
        assertTrue(CalendarUtils.isStartOfMonth(calendar));
        CalendarUtils.startOfWeek(calendar);
        // simulate the painting loop
        for (int week = 0; week < 6; week++) {
            for (int day = 0; day < 7; day++) {
                calendar.add(Calendar.DAY_OF_MONTH, 1);
                assertTrue("must be start of day " + calendar.getTime(),
                        CalendarUtils.isStartOfDay(calendar));
            }
            assertTrue("must be start of week " + calendar.getTime(),
                    CalendarUtils.isStartOfWeek(calendar));
        }
    }
    
    /**
     * Test getDayBounds(Date) for leading dates are null. The assumption is
     * wrong for a leading date in the second month - it's contained in the
     * first!
     * 
     * PENDING JW: temporarily removed conversion Date -> bounds
     */
//    public void testDayBoundsLeadingDatesNull() {
//        // This test will not work in a headless configuration.
//        if (GraphicsEnvironment.isHeadless()) {
//            LOG.fine("cannot run test - headless environment");
//            return;
//        }
//        BasicCalendarUI ui = getRealizedMonthViewUI(ComponentOrientation.LEFT_TO_RIGHT);
//        // the ui's calendar is configured to the first displayed day
//        Calendar calendar = ui.getCalendar();
//        calendar.add(Calendar.MONTH, 1);
//        int month = calendar.get(Calendar.MONTH);
//        CalendarUtils.startOfWeek(calendar);
//        assertFalse("sanity - we have leading dates in the month", month == calendar.get(Calendar.MONTH));
//        assertEquals("leading dates must return null bounds", null, 
//                ui.getDayBounds(calendar.getTime()));
//    }


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
     * 
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
     * @inherited <p>
     */
    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();
        calendarView = new JXCalendar();
        calendarUI = (BasicCalendarUI) calendarView.getUI();
        calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 5);
        today = calendar.getTime();
        
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        yesterday = calendar.getTime();
        
        calendar.add(Calendar.DAY_OF_MONTH, 2);
        tomorrow = calendar.getTime();
        
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        afterTomorrow = calendar.getTime();
        
        calendar.setTime(today);

    }

    /**
     * Do nothing - just to keep the test runner from complaining 
     * if there are no issues.
     *
     */
    public void testDummy() {
        
    }
}
