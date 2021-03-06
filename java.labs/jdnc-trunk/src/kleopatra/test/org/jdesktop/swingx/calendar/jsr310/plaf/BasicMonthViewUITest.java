/*
 * $Id: BasicMonthViewUITest.java 3341 2011-03-22 10:24:55Z kleopatra $
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
package org.jdesktop.swingx.calendar.jsr310.plaf;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Logger;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Box;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.time.calendar.DayOfWeek;
import javax.time.calendar.LocalDateTime;
import javax.time.calendar.MonthOfYear;

import org.jdesktop.swingx.InteractiveTestCase;
import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.calendar.jsr310.Date310SelectionModel.SelectionMode;
import org.jdesktop.swingx.calendar.jsr310.DateTimeUtils;
import org.jdesktop.swingx.calendar.jsr310.JXMonthView;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;


/**
 * Tests to expose known issues of BasicMonthViewUI.
 * 
 * @author Jeanette Winzenburg
 */
@RunWith(JUnit4.class)
public class BasicMonthViewUITest extends InteractiveTestCase {
    @SuppressWarnings("all")
    private static final Logger LOG = Logger
            .getLogger(BasicMonthViewUITest.class.getName());

    // duplicate hard-coded monthViewUI values
    @SuppressWarnings("unused")
    private static final int CALENDAR_SPACING = 10;
    
    public static void main(String[] args) {
//      setSystemLF(true);
      BasicMonthViewUITest  test = new BasicMonthViewUITest();
      try {
          test.runInteractiveTests();
//        test.runInteractiveTests(".*Simple.*");
      } catch (Exception e) {
          System.err.println("exception when executing interactive tests:");
          e.printStackTrace();
      }
  }
 
    /**
     * Issue #750-swingx: use rendering to side-step antialiase probs.
     * 
     * Debugging ...
     */
    public void interactiveRenderingOn() {
        // force default loading
        new JXMonthView();
        // this is global state - uncomment for debug painting completely
//        UIManager.put("JXMonthView.trailingDayForeground", Color.YELLOW);
//        UIManager.put("JXMonthView.leadingDayForeground", Color.ORANGE);
//        UIManager.put("JXMonthView.weekOfTheYearForeground", Color.GREEN);
//        UIManager.put("JXMonthView.unselectableDayForeground", Color.MAGENTA);
        String frameTitle = "Debug painting: rendering on";
        showDebugMonthView(frameTitle, null);
    }

    /**
     * Issue #750-swingx: use rendering to side-step antialiase probs.
     * 
     * Debugging ...
     */
    public void interactiveRenderingOff() {
        String frameTitle = "Debug painting: rendering off";
        Boolean disableRendering = Boolean.TRUE;
        showDebugMonthView(frameTitle, disableRendering);
    }
    /**
     * @param frameTitle
     * @param disableRendering
     */
    private void showDebugMonthView(String frameTitle, Boolean disableRendering) {
        final JXMonthView monthView = new JXMonthView();
        monthView.putClientProperty("disableRendering", disableRendering);
        monthView.setDayForeground(DayOfWeek.SUNDAY, Color.BLUE);
        monthView.setDaysOfTheWeekForeground(Color.RED);
        monthView.setFlaggedDayForeground(Color.CYAN);
        monthView.setSelectionBackground(Color.GRAY);
        monthView.setSelectionForeground(Color.GREEN);
        monthView.setTodayBackground(Color.PINK);
        monthView.setTraversable(true);
        monthView.setShowingWeekNumber(true);
        monthView.setShowingLeadingDays(true);
        monthView.setShowingTrailingDays(true);
        monthView.setSelectionMode(SelectionMode.SINGLE_INTERVAL_SELECTION);
        monthView.setPreferredColumnCount(2);
        monthView.setPreferredRowCount(2);
        final JXFrame frame = wrapInFrame(monthView, frameTitle);
        addComponentOrientationToggle(frame);
        Action toggleTraversable = new AbstractAction("toggle traversable") {

            public void actionPerformed(ActionEvent e) {
                monthView.setTraversable(!monthView.isTraversable());
                
            }
            
        };
        addAction(frame, toggleTraversable);
//        final JXDatePicker picker = new JXDatePicker();
//        picker.getMonthView().putClientProperty("disableRendering", disableRendering);
//        picker.addActionListener(new ActionListener() {
//
//            public void actionPerformed(ActionEvent e) {
//                if (e.getActionCommand().equals(JXDatePicker.CANCEL_KEY)) return;
//                if (picker.getDate() == null) return;
//                monthView.setFlaggedDates(picker.getDate());
//            }
//            
//        });
//        final JXDatePicker unselectable = new JXDatePicker();
//        unselectable.getMonthView().putClientProperty("disableRendering", disableRendering);
//        unselectable.addActionListener(new ActionListener() {
//
//            public void actionPerformed(ActionEvent e) {
//                if (e.getActionCommand().equals(JXDatePicker.CANCEL_KEY)) return;
//                if (unselectable.getDate() == null) return;
//                monthView.setUnselectableDates(unselectable.getDate());
//            }
//            
//        });
        final JComboBox zoneSelector = new JComboBox(Locale.getAvailableLocales());
        // Synchronize the monthView's and selector's zones.
        zoneSelector.setSelectedItem(monthView.getLocale());

        // Set the monthView's time zone based on the selected time zone.
        zoneSelector.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                Locale zone = (Locale) zoneSelector.getSelectedItem();
                monthView.setLocale(zone);
//                monthView.revalidate();
            }
        });


        JComponent pickers = Box.createHorizontalBox();
        pickers.add(new JLabel("Flagged: "));
        pickers.add(zoneSelector);
        frame.add(pickers, BorderLayout.SOUTH);
        show(frame);
    }


    /**
     * Issue #736-swingx: monthView cannot cope with minimalDaysInFirstWeek.
     * 
     * Debugging ...
     */
    public void interactiveDayAt() {
        final JXMonthView monthView = new JXMonthView();
        monthView.setTraversable(true);
        monthView.setShowingWeekNumber(true);
        monthView.setShowingLeadingDays(true);
        monthView.setShowingTrailingDays(true);
        monthView.setSelectionMode(SelectionMode.SINGLE_INTERVAL_SELECTION);
        monthView.setPreferredColumnCount(2);
        monthView.setPreferredRowCount(2);
        final BasicMonthViewUI ui = ((BasicMonthViewUI) monthView.getUI());
        monthView.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseReleased(MouseEvent e) {
//                LOG.info("calendar grid" + ui.calendarGrid);
//                LOG.info("dayAt " + e.getPoint() + ": "
//                        + "\n" + monthView.getDayAtLocation(e.getX(), e.getY()));
//                Calendar monthAtLocation = ui.getMonthAtLocation(e.getX(), e.getY());
//                LOG.info("month start " + 
//                        (monthAtLocation != null ? monthAtLocation.getTime() : null));
                
//                Point p = ui.getMonthGridPositionAtLocation(e.getX(), e.getY());
//                LOG.info("month bounds from logical " + 
//                        p + " \n " +
//                        ui.getMonthBounds(p.y, p.x));
//                LOG.info("month bounds at location" + 
//                        ui.getMonthBoundsAtLocation(e.getX(), e.getY()));
                LOG.info("day grid position " + 
                        ui.getDayGridPositionAtLocation(e.getX(), e.getY()) 
                      + "\nday bounds " + 
                        ui.getDayBoundsAtLocation(e.getX(), e.getY()));
            }
            
        });
        final JXFrame frame = wrapInFrame(monthView, "test mapping: printed on mouse release");
//        Action action = new AbstractActionExt("toggle minimal") {
//            
//            public void actionPerformed(ActionEvent e) {
//                int minimal = monthView.getSelectionModel().getMinimalDaysInFirstWeek();
//                monthView.getSelectionModel().setMinimalDaysInFirstWeek(minimal > 1 ? 1 : 4);
//            }
//            
//        };
//        addAction(frame, action);
        addComponentOrientationToggle(frame);
        frame.pack();
        frame.setVisible(true);
    }

    /**
     * Issue #736-swingx: monthView cannot cope with minimalDaysInFirstWeek.
     * 
     * Here: look at impact of forcing the minimalDays to a value different
     * from the calendar. Days must be displayed in starting from the 
     * first row under the days-of-week.
     * 
     * Not yet completely fixed: for very late firstDayOfWeek, the Jan is incompletely
     * painted for mininalDays > 1. Rare enough to ignore for now?
     */
    public void interactiveMinimalDaysInFirstWeek() {
        final JXMonthView monthView = new JXMonthView();
        monthView.setTraversable(true);
        monthView.setShowingWeekNumber(true);
        monthView.setShowingLeadingDays(true);
        monthView.setShowingTrailingDays(true);
        monthView.setSelectionMode(SelectionMode.SINGLE_INTERVAL_SELECTION);
        final JXFrame frame = wrapInFrame(monthView, "click unselectable fires ActionEvent");
//        Action action = new AbstractActionExt("toggle minimal") {
//            
//            public void actionPerformed(ActionEvent e) {
//                int minimal = monthView.getSelectionModel().getMinimalDaysInFirstWeek();
//                monthView.getSelectionModel().setMinimalDaysInFirstWeek(minimal > 1 ? 1 : 4);
//            }
//            
//        };
//        addAction(frame, action);
        addComponentOrientationToggle(frame);
//        final JComboBox dayOfWeekComboBox = new JComboBox(new String[]{
//                
//                "Sunday", "Monday", "Tuesday",
//                "Wednesday", "Thursday", "Friday", "Saturday"});
//        dayOfWeekComboBox.addActionListener(new ActionListener() {
//
//            public void actionPerformed(ActionEvent e) {
//                int selected = dayOfWeekComboBox.getSelectedIndex();
//                monthView.setFirstDayOfWeek(selected + Calendar.SUNDAY);
//                
//            }
//            
//        });
//        dayOfWeekComboBox.setSelectedIndex(monthView.getFirstDayOfWeek() - Calendar.SUNDAY);
//        addStatusComponent(frame, dayOfWeekComboBox);
        frame.pack();
        frame.setVisible(true);
    }

//------------------------------

    /**
     * Test that day position in details day-of-week header returns null date
     */
    @Test
    public void testDayInMonthForDayOfWeekHeader() {
        // This test will not work in a headless configuration.
        if (GraphicsEnvironment.isHeadless()) {
            LOG.info("cannot run test - headless environment");
            return;
        }
        BasicMonthViewUI ui = getRealizedMonthViewUI(ComponentOrientation.LEFT_TO_RIGHT);
        // get a date in the first month
        LocalDateTime month = ui.getMonth(1, 0);
        assertEquals("header grid position must return null date", null, ui.getDayInMonth(month, -1, 6));
     }

    /**
     * Test that day position in details weekNumber header returns null date
     */
    @Test
    public void testDayInMonthForWeekNumberHeader() {
        // This test will not work in a headless configuration.
        if (GraphicsEnvironment.isHeadless()) {
            LOG.info("cannot run test - headless environment");
            return;
        }
        BasicMonthViewUI ui = getRealizedMonthViewUI(ComponentOrientation.LEFT_TO_RIGHT, true);
        // get a date in the first month
        LocalDateTime month = ui.getMonth(0, 0);
        assertEquals("header grid position must return null date", null, ui.getDayInMonth(month, 3, -1));
     }

    /**
     * Test contract of getDayInMonth: grid position of leading dates must be null.
     * Here: first month in grid.
     */
    @Test
    public void testDayInMonthLeadingFirstMonth() {
        // This test will not work in a headless configuration.
        if (GraphicsEnvironment.isHeadless()) {
            LOG.info("cannot run test - headless environment");
            return;
        }
        BasicMonthViewUI ui = getRealizedMonthViewUI(ComponentOrientation.LEFT_TO_RIGHT);
        // get a date in the first month
        LocalDateTime month = ui.getMonth(0, 0);
        assertEquals("leading date grid position must return null date", null, ui.getDayInMonth(month, 0, 0));
     }
    
    /**
     * Test contract of getDayInMonth: grid position of leading dates must be null.
     * Here: second month in grid.
     */
    @Test
    public void testDayInMonthLeadingSecondMonth() {
        // This test will not work in a headless configuration.
        if (GraphicsEnvironment.isHeadless()) {
            LOG.info("cannot run test - headless environment");
            return;
        }
        BasicMonthViewUI ui = getRealizedMonthViewUI(ComponentOrientation.LEFT_TO_RIGHT);
        // get a date in the first month
        LocalDateTime month = ui.getMonth(1, 0);
        // this fails because 
        assertEquals("leading date grid position must return null date", null, ui.getDayInMonth(month, 0, 0));
     }
    
    /**
     * Issue #787-swingx: hit detection of leading/trailing days.
     * Sanity: get day bounds for leading dates (here: second month) must succeed.
     */
    @Test
    public void testDayBoundsAtLocationLeadingSecondMonth() {
        // This test will not work in a headless configuration.
        if (GraphicsEnvironment.isHeadless()) {
            LOG.info("cannot run test - headless environment");
            return;
        }
        BasicMonthViewUI ui = getRealizedMonthViewUI(ComponentOrientation.LEFT_TO_RIGHT);
        // second month - Mar 2008 has leading dates
        Rectangle monthBounds = ui.getMonthBoundsAtLocation(20 + ui.getMonthSize().width, 20);
        int firstDay = monthBounds.y + ui.getMonthHeaderHeight() + ui.getDaySize().height + 2;
        // second month should behave the same way as first 
        // first is outside and returns null.
        int row = ui.getDayGridPositionAtLocation(monthBounds.x + 2, firstDay).y;
        assertEquals(0, row);
        Rectangle dayBounds = ui.getDayBoundsAtLocation(monthBounds.x + 2, firstDay);
        assertNotNull(dayBounds);
    }

    /**
     * Issue #787-swingx: hit detection of leading/trailing days.
     * Sanity: get day bounds for leading dates (here: first month) must succeed.
     */
    @Test
    public void testDayBoundsAtLocationLeadingFirstMonth() {
        // This test will not work in a headless configuration.
        if (GraphicsEnvironment.isHeadless()) {
            LOG.info("cannot run test - headless environment");
            return;
        }
        BasicMonthViewUI ui = getRealizedMonthViewUI(ComponentOrientation.LEFT_TO_RIGHT);
        // first month - Feb 2008 has leading dates
        Rectangle monthBounds = ui.getMonthBoundsAtLocation(20, 20);
        int firstDayY = monthBounds.y + ui.getMonthHeaderHeight() + ui.getDaySize().height + 2;
        int row = ui.getDayGridPositionAtLocation(monthBounds.x + 2, firstDayY).y;
        assertEquals(0, row);
        Rectangle dayBounds = ui.getDayBoundsAtLocation(monthBounds.x + 2, firstDayY);
        assertNotNull(dayBounds);
    }
    /**
     * Issue #787-swingx: hit detection of leading/trailing days.
     * Must be null (second month).
     */
    @Test
    public void testDayAtLocationLeadingSecondMonth() {
        // This test will not work in a headless configuration.
        if (GraphicsEnvironment.isHeadless()) {
            LOG.info("cannot run test - headless environment");
            return;
        }
        BasicMonthViewUI ui = getRealizedMonthViewUI(ComponentOrientation.LEFT_TO_RIGHT);
        // second month - Mar 2008 has leading dates
        Rectangle monthBounds = ui.getMonthBoundsAtLocation(20 + ui.getMonthSize().width, 20);
        int firstDay = monthBounds.y + ui.getMonthHeaderHeight() + ui.getDaySize().height + 2;
        assertEquals("hit detection in leading date must return null", null, 
                ui.getDayAtLocation(monthBounds.x + 2, firstDay));
    }

    /**
     * Issue #787-swingx: hit detection of leading/trailing days.
     * Must be null (first month).
     */
    @Test
    public void testDayAtLocationLeadingFirstMonth() {
        // This test will not work in a headless configuration.
        if (GraphicsEnvironment.isHeadless()) {
            LOG.info("cannot run test - headless environment");
            return;
        }
        BasicMonthViewUI ui = getRealizedMonthViewUI(ComponentOrientation.LEFT_TO_RIGHT);
        // first month - Feb 2008 has leading dates
        Rectangle monthBounds = ui.getMonthBoundsAtLocation(20, 20);
        int firstDayY = monthBounds.y + ui.getMonthHeaderHeight() + ui.getDaySize().height + 2;
        assertEquals("hit detection in leading date must return null", null, 
                ui.getDayAtLocation(monthBounds.x + 2, firstDayY));
    }

    /**
     * Test full circle: getDayBounds(LocalDateTime)
     */
    @Test
    public void testDayBoundsFromDate() {
        // This test will not work in a headless configuration.
        if (GraphicsEnvironment.isHeadless()) {
            LOG.info("cannot run test - headless environment");
            return;
        }
        BasicMonthViewUI ui = getRealizedMonthViewUI(ComponentOrientation.LEFT_TO_RIGHT);
        Rectangle bounds = ui.getMonthBoundsAtLocation(20, 20);
        Dimension daySize = ui.getDaySize();
        // first day column
        int locationX = bounds.x + 2;
        // second non-header row
        int locationY = bounds.y + ui.getMonthHeaderHeight() + 2 * daySize.height + 2;
        Rectangle dayBounds = ui.getDayBoundsAtLocation(locationX, locationY);
        LocalDateTime date = ui.getDayAtLocation(locationX, locationY); 
        assertEquals(dayBounds, ui.getDayBounds(date));
     }


    /**
     * Test getDayGridPosition(LocalDateTime) - 
     * here: had been incorrect calculation of first row.
     */
    @Test
    public void testDateToGridPosition6Apr2008() {
        // This test will not work in a headless configuration.
        if (GraphicsEnvironment.isHeadless()) {
            LOG.info("cannot run test - headless environment");
            return;
        }
        BasicMonthViewUI ui = getRealizedMonthViewUI(ComponentOrientation.LEFT_TO_RIGHT);
        LocalDateTime cal = ui.getFirstDisplayedDay();
        fail("TODO: dayGridPosition: invalid test as the mapping of firstDayOfWeek to DayOfWeek.value is wrong");
//        Calendar cal = ui.getCalendar();
        // 6th April was sunday ... it is a last day of the first week, but only at places where first day is Monday.
        // so using this obsure notation we get last day of first week depending on when the week really starts.
//        cal.set(2008, Calendar.APRIL, 4 + cal.getFirstDayOfWeek()); 
        cal = cal.withYear(2008).withMonthOfYear(4)
            .withDayOfMonth(4 + DateTimeUtils.getFirstDayOfWeek(Locale.getDefault()).getValue());
        assertEquals(6, ui.getDayGridPosition(cal).x);
        assertEquals(0, ui.getDayGridPosition(cal).y);
        
     }

    /**
    * Test getDayGridPosition(LocalDateTime) - first complete row
    */
    @Test
    public void testDateToGridPositionNonLeading() {
        // This test will not work in a headless configuration.
        if (GraphicsEnvironment.isHeadless()) {
            LOG.info("cannot run test - headless environment");
            return;
        }
        BasicMonthViewUI ui = getRealizedMonthViewUI(ComponentOrientation.LEFT_TO_RIGHT);
        // get a date in the first month
        LocalDateTime month = ui.getMonth(0, 0);
        // second row - cant be a leading date
        assertDateToDayGrid(ui, month, 1, 0);
     }

    /**
    * Test getDayGridPosition(LocalDateTime) - somewhere in the middle (being paranoid ;-)
     */
    @Test
    public void testDateToGridPositionMiddle() {
        // This test will not work in a headless configuration.
        if (GraphicsEnvironment.isHeadless()) {
            LOG.info("cannot run test - headless environment");
            return;
        }
        BasicMonthViewUI ui = getRealizedMonthViewUI(ComponentOrientation.LEFT_TO_RIGHT);
        // get a date in the first month
        LocalDateTime month = ui.getMonth(0, 0);
        assertDateToDayGrid(ui, month, 2, 4);
     }
    
    /**
     * Full cylce: use getDayInMonth(...) to get the day from the logical 
     * grid position and assert that the reverse getDayGridPosition returns the same
     * logical coordinates. The given coordinates must map to a day contained in the
     * month, that is >= 0 and not representing leading/trailing dates.
     * 
     * @param ui the ui to use (must be realized)
     * @param month the month containing the grid to test
     * @param dayRow the logical row coordinate of the day
     * @param dayColumn the logical column coordinate of the day
     */
    private void assertDateToDayGrid(BasicMonthViewUI ui, LocalDateTime month,
            int dayRow, int dayColumn) {
        LocalDateTime day = ui.getDayInMonth(month, dayRow, dayColumn);
        assertEquals(dayRow, ui.getDayGridPosition(day).y);
        assertEquals(dayColumn, ui.getDayGridPosition(day).x);
    }




    
    /**
     * Test full circle: getMonthGridPosition(LocalDateTime) - had problems with first row?
     */
    @Test
    public void testMonthGridPositionFirstRowFromDate() {
        // This test will not work in a headless configuration.
        if (GraphicsEnvironment.isHeadless()) {
            LOG.info("cannot run test - headless environment");
            return;
        }
        BasicMonthViewUI ui = getRealizedMonthViewUI(ComponentOrientation.LEFT_TO_RIGHT);
        assertDateToMonthGrid(ui, 0, 1);
     }

    /**
     * Full cylce: use getMonth(...) to get the month from the logical 
     * grid position and assert that the reverse getMonthGridPosition returns the same
     * logical coordinates. 
     * 
     * @param ui the ui to use (must be realized)
     * @param row the row index of the month
     * @param column the column index of the month
     */
    private void assertDateToMonthGrid(BasicMonthViewUI ui, int row, int column) {
        // date of start of month from logical position
        LocalDateTime month = ui.getMonth(row, column);
        assertEquals(row, ui.getMonthGridPosition(month).y);
        assertEquals(column, ui.getMonthGridPosition(month).x);
    }

    /**
     * Test full circle: getMonthGridPosition(LocalDateTime) - had problems with first row?
     */
    @Test
    public void testDateToMonthGridPositionFirst() {
        // This test will not work in a headless configuration.
        if (GraphicsEnvironment.isHeadless()) {
            LOG.info("cannot run test - headless environment");
            return;
        }
        BasicMonthViewUI ui = getRealizedMonthViewUI(ComponentOrientation.LEFT_TO_RIGHT);
        assertDateToMonthGrid(ui, 0, 0);
     }
    /**
     * Test full circle: getMonthGridPosition(LocalDateTime) - had problems with first row?
     */
    @Test
    public void testDateToMonthGridPositionSecond() {
        // This test will not work in a headless configuration.
        if (GraphicsEnvironment.isHeadless()) {
            LOG.info("cannot run test - headless environment");
            return;
        }
        BasicMonthViewUI ui = getRealizedMonthViewUI(ComponentOrientation.LEFT_TO_RIGHT);
        assertDateToMonthGrid(ui, 1, 0);
     }

    /**
     * Test full circle: getMonthGridPosition(LocalDateTime)
     */
    @Test
    public void testDateToMonthGridPositionLast() {
        // This test will not work in a headless configuration.
        if (GraphicsEnvironment.isHeadless()) {
            LOG.info("cannot run test - headless environment");
            return;
        }
        BasicMonthViewUI ui = getRealizedMonthViewUI(ComponentOrientation.LEFT_TO_RIGHT);
        assertDateToMonthGrid(ui, 1, 1);
     }

    /**
     * Test full circle: getMonthBounds(LocalDateTime)
     */
    @Test
    public void testDateToMonthBounds() {
        // This test will not work in a headless configuration.
        if (GraphicsEnvironment.isHeadless()) {
            LOG.info("cannot run test - headless environment");
            return;
        }
        BasicMonthViewUI ui = getRealizedMonthViewUI(ComponentOrientation.LEFT_TO_RIGHT);
        // month bounds from logical position in second row, second column
        Rectangle bounds = ui.getMonthBounds(1, 1);
        // date of start of month from logical position
        LocalDateTime month = ui.getMonth(1, 1);
        assertEquals(bounds, ui.getMonthBounds(month));
     }

    /**
     * Test  getMonthBounds(LocalDateTime) for not visible dates are null.
     */
    @Test
    public void testDateToMonthBoundsNotVisible() {
        // This test will not work in a headless configuration.
        if (GraphicsEnvironment.isHeadless()) {
            LOG.info("cannot run test - headless environment");
            return;
        }
        BasicMonthViewUI ui = getRealizedMonthViewUI(ComponentOrientation.LEFT_TO_RIGHT);
        // the ui's calendar is configured to the first displayed day
        LocalDateTime uiCalendar = ui.getFirstDisplayedDay();
        MonthOfYear month = uiCalendar.getMonthOfYear();
        uiCalendar = DateTimeUtils.startOfWeek(uiCalendar);
        assertFalse("sanity - we have leading dates in the month", 
                month == uiCalendar.getMonthOfYear());
        assertEquals("leading dates must return null bounds", 
                null, ui.getMonthBounds(uiCalendar));
    }

    /**
     * Test  getDayBounds(LocalDateTime)  for null date must throw NPE.
     */
    @Test
    public void testMonthBoundsNullDate() {
        // This test will not work in a headless configuration.
        if (GraphicsEnvironment.isHeadless()) {
            LOG.info("cannot run test - headless environment");
            return;
        }
        BasicMonthViewUI ui = getRealizedMonthViewUI(ComponentOrientation.LEFT_TO_RIGHT);
        try {
            ui.getMonthBounds(null);
            fail("date param null is not allowed - must fire NPE");
        } catch (NullPointerException ex) {
            // that's what we expect
        }
    }
    
    /**
     * Test  getDayBounds(LocalDateTime) for leading dates are null.
     */
    @Test
    public void testDateToDayBoundsLeadingDate() {
        // This test will not work in a headless configuration.
        if (GraphicsEnvironment.isHeadless()) {
            LOG.info("cannot run test - headless environment");
            return;
        }
        BasicMonthViewUI ui = getRealizedMonthViewUI(ComponentOrientation.LEFT_TO_RIGHT);
        // the ui's calendar is configured to the first displayed day
        LocalDateTime uiCalendar = ui.getFirstDisplayedDay();
        MonthOfYear month = uiCalendar.getMonthOfYear();
        uiCalendar = DateTimeUtils.startOfWeek(uiCalendar);
        assertFalse("sanity - we have leading dates in the month", 
                month == uiCalendar.getMonthOfYear());
        assertEquals("leading dates must return null bounds", 
                null, ui.getDayBounds(uiCalendar));
    }

    /**
     * Test  getDayBounds(LocalDateTime)  for null date must fire NPE.
     */
    @Test
    public void testDayBoundsNullDate() {
        // This test will not work in a headless configuration.
        if (GraphicsEnvironment.isHeadless()) {
            LOG.info("cannot run test - headless environment");
            return;
        }
        BasicMonthViewUI ui = getRealizedMonthViewUI(ComponentOrientation.LEFT_TO_RIGHT);
        try {
            ui.getDayBounds(null);
            fail("date param null is not allowed - must fire NPE");
        } catch (NullPointerException ex) {
            // that's what we expect
        }
    }
    /**
     * Issue #781-swingx: reverse coordinate transformation.
     * Here: expose sizes
     */
    @Test
    public void testMonthSize() {
        if (GraphicsEnvironment.isHeadless()) {
            LOG.info("cannot run test - headless environment");
            return;
        }
        BasicMonthViewUI ui = getRealizedMonthViewUI(ComponentOrientation.RIGHT_TO_LEFT);
        Rectangle monthBounds = ui.getMonthBoundsAtLocation(20, 20);
        assertEquals(monthBounds.width, ui.getMonthSize().width);
        assertEquals(monthBounds.height, ui.getMonthSize().height);
    }
 
    /**
     * Issue #781-swingx: reverse coordinate transformation.
     * Here: expose sizes
     */
    @Test
    public void testDaySize() {
        if (GraphicsEnvironment.isHeadless()) {
            LOG.info("cannot run test - headless environment");
            return;
        }
        BasicMonthViewUI ui = getRealizedMonthViewUI(ComponentOrientation.RIGHT_TO_LEFT);
        Rectangle monthBounds = ui.getMonthBoundsAtLocation(20, 20);
        Rectangle dayBounds = ui.getDayBoundsAtLocation(
                monthBounds.x + 2, 
                monthBounds.y + ui.getMonthHeaderHeight() + 2); 
        assertEquals(dayBounds.width, ui.getDaySize().width);
        assertEquals(dayBounds.height, ui.getDaySize().height);
    }
    
    @Test
    public void testMonthBoundsFromLogicalRToL() {
        if (GraphicsEnvironment.isHeadless()) {
            LOG.info("cannot run test - headless environment");
            return;
        }
        BasicMonthViewUI ui = getRealizedMonthViewUI(ComponentOrientation.RIGHT_TO_LEFT);
        Rectangle monthBounds = ui.getMonthBoundsAtLocation(20, 20);
        // second row, first column in absolute coordinates
        Rectangle monthBounds11 = ui.getMonthBoundsAtLocation(
                20, 
                monthBounds.y + 20 + monthBounds.height);
        // second row, second column in logical coordinates
        assertEquals(monthBounds11, ui.getMonthBounds(1, 1));
    }

    @Test
    public void testMonthBoundsFromLogicalLToR() {
        if (GraphicsEnvironment.isHeadless()) {
            LOG.info("cannot run test - headless environment");
            return;
        }
        BasicMonthViewUI ui = getRealizedMonthViewUI(ComponentOrientation.LEFT_TO_RIGHT);
        Rectangle monthBounds = ui.getMonthBoundsAtLocation(20, 20);
        // second row, second column
        Rectangle monthBounds11 = ui.getMonthBoundsAtLocation(
                monthBounds.x + 20 + monthBounds.width, 
                monthBounds.y + 20 + monthBounds.height);
        // second row, second column in logical coordinates
        assertEquals(monthBounds11, ui.getMonthBounds(1, 1));
    }
    
    /**
     * Sanity test: inexpected pass if the realized frame isn't visible.
     */
    @Test
    public void testFirstMonthLocation() {
        if (GraphicsEnvironment.isHeadless()) {
            LOG.info("cannot run test - headless environment");
            return;
        }
        BasicMonthViewUI ui = getRealizedMonthViewUI(ComponentOrientation.LEFT_TO_RIGHT);
        Rectangle monthBounds = ui.getMonthBoundsAtLocation(20, 20);
        assertEquals(monthBounds.getLocation(), ui.calendarGrid.getLocation());
        assertNull("no hit - bounds must be null", ui.getMonthBoundsAtLocation(19, 20));
    }
 
    
    /**
     * Test day at location
     */
    @Test
    public void testDayAtLocationLToR() {
        // This test will not work in a headless configuration.
        if (GraphicsEnvironment.isHeadless()) {
            LOG.info("cannot run test - headless environment");
            return;
        }
        BasicMonthViewUI ui = getRealizedMonthViewUI(ComponentOrientation.LEFT_TO_RIGHT);
        Rectangle monthBounds = ui.getMonthBoundsAtLocation(20, 20);
        Rectangle dayBounds = ui.getDayBoundsAtLocation(
                monthBounds.x + 2, 
                monthBounds.y + ui.getMonthHeaderHeight() + 2); 
        // first column in second non-header row
        LocalDateTime date = ui.getDayAtLocation(
                monthBounds.x + 2, 
                monthBounds.y + ui.getMonthHeaderHeight() + 2 * dayBounds.height + 2);
        // the ui's calendar is configured to the first displayed day
        LocalDateTime uiCalendar = ui.getFirstDisplayedDay().plusWeeks(1);
        uiCalendar = DateTimeUtils.startOfWeek(uiCalendar);
        assertEquals("first logical column in LToR", uiCalendar, date);
     }

    /**
     * Test day at location
     */
    @Test
    public void testDayAtLocationRToL() {
        fail("TODO: think about implementing endOfWeek");
        // This test will not work in a headless configuration.
//        if (GraphicsEnvironment.isHeadless()) {
//            LOG.info("cannot run test - headless environment");
//            return;
//        }
//        BasicMonthViewUI ui = getRealizedMonthViewUI(ComponentOrientation.RIGHT_TO_LEFT);
//        Rectangle monthBounds = ui.getMonthBoundsAtLocation(20, 20);
//        Rectangle dayBounds = ui.getDayBoundsAtLocation(
//                monthBounds.x + 2, 
//                monthBounds.y + ui.getMonthHeaderHeight() + 2); 
        // first column in second non-header row
//        LocalDateTime date = ui.getDayAtLocation(
//                monthBounds.x + 2, 
//                monthBounds.y + ui.getMonthHeaderHeight() + 2 * dayBounds.height + 2); 
//        LocalDateTime endOfWeek = DateTimeUtils.endOfWeek(ui.getFirstDisplayedDay());
//        LocalDateTime uiCalendar = ui.getCalendar(ui.getMonthAtLocation(20, 20));
//        uiCalendar.add(Calendar.WEEK_OF_YEAR, 1);
//        CalendarUtils.endOfWeek(uiCalendar);
//        assertEquals("first day in first week", uiCalendar.getTime(), endOfWeek); 
     }


    /**
     * Test day at location: hitting days of week must return null.
     */
    @Test
    public void testDayAtLocationDayHeaderNull() {
        // This test will not work in a headless configuration.
        if (GraphicsEnvironment.isHeadless()) {
            LOG.info("cannot run test - headless environment");
            return;
        }
        BasicMonthViewUI ui = getRealizedMonthViewUI(ComponentOrientation.LEFT_TO_RIGHT);
        Rectangle monthBounds = ui.getMonthBoundsAtLocation(20, 20);
        // same for LToR
        LocalDateTime date = ui.getDayAtLocation(
                monthBounds.x + 2, 
                monthBounds.y + ui.getMonthHeaderHeight() + 2); 
        assertNull("hitting days-of-week must return null calendar", date);
     }
    
    @Test
    public void testDayBounds() {
        // This test will not work in a headless configuration.
        if (GraphicsEnvironment.isHeadless()) {
            LOG.info("cannot run test - headless environment");
            return;
        }
        BasicMonthViewUI uiRToL = getRealizedMonthViewUI(ComponentOrientation.RIGHT_TO_LEFT);
        BasicMonthViewUI uiLToR = getRealizedMonthViewUI(ComponentOrientation.LEFT_TO_RIGHT);
        Rectangle monthBounds = uiRToL.getMonthBoundsAtLocation(20, 20);
        assertNull("hit in header must return null bounds", 
                uiRToL.getDayBoundsAtLocation(monthBounds.x + 2, monthBounds.y + 2));
        // first column first row
        Rectangle dayBoundsRToL = uiRToL.getDayBoundsAtLocation(
                monthBounds.x + 2, monthBounds.y + uiRToL.getMonthHeaderHeight()); 
        // same for LToR
        Rectangle dayBoundsLToR = uiLToR.getDayBoundsAtLocation(
                monthBounds.x + 2, monthBounds.y + uiLToR.getMonthHeaderHeight()); 
        assertEquals("day bounds must be independent of orientation", 
                dayBoundsLToR, dayBoundsRToL);
        assertEquals(monthBounds.x, dayBoundsLToR.x);
        assertEquals(monthBounds.y + uiLToR.getMonthHeaderHeight(), dayBoundsLToR.y);
    }
 
    /**
     * days of week is mapped to row index -1.
     */
    @Test
    public void testDayGridPositionColumnHeader() {
        // This test will not work in a headless configuration.
        if (GraphicsEnvironment.isHeadless()) {
            LOG.info("cannot run test - headless environment");
            return;
        }
        BasicMonthViewUI uiRToL = getRealizedMonthViewUI(ComponentOrientation.RIGHT_TO_LEFT);
        Rectangle monthBounds = uiRToL.getMonthBoundsAtLocation(20, 20);
        // first row below month header == days of week header 
        Point dayGridRToL = uiRToL.getDayGridPositionAtLocation(
                monthBounds.x + 2, monthBounds.y + uiRToL.getMonthHeaderHeight() + 2); 
        assertEquals("first row below header must be day column header", -1, dayGridRToL.y);
     }

    /**
     * day grid rows >= 0
     */
    @Test
    public void testDayGridPositionRow() {
        // This test will not work in a headless configuration.
        if (GraphicsEnvironment.isHeadless()) {
            LOG.info("cannot run test - headless environment");
            return;
        }
        BasicMonthViewUI ui = getRealizedMonthViewUI(ComponentOrientation.RIGHT_TO_LEFT);
        Rectangle monthBounds = ui.getMonthBoundsAtLocation(20, 20);
        Rectangle dayBounds = ui.getDayBoundsAtLocation(
                monthBounds.x + 2, monthBounds.y + ui.getMonthHeaderHeight() +2); 

        // first column near bottom
        Point dayInGrid = ui.getDayGridPositionAtLocation(
                monthBounds.x + 2, 
                monthBounds.y + ui.getMonthHeaderHeight() + dayBounds.height + 2); 
       
        assertEquals("first row", 0, dayInGrid.y);
     }
 
    /**
     * Screen location mapped to logical day columns.
     */
    @Test
    public void testDayGridPositionColumn() {
        // This test will not work in a headless configuration.
        if (GraphicsEnvironment.isHeadless()) {
            LOG.info("cannot run test - headless environment");
            return;
        }
        BasicMonthViewUI uiRToL = getRealizedMonthViewUI(ComponentOrientation.RIGHT_TO_LEFT);
        BasicMonthViewUI uiLToR = getRealizedMonthViewUI(ComponentOrientation.LEFT_TO_RIGHT);
        Rectangle monthBounds = uiRToL.getMonthBoundsAtLocation(20, 20);
        // first column in first non-header row
        Point dayGridRToL = uiRToL.getDayGridPositionAtLocation(
                monthBounds.x + 2, 
                monthBounds.y + uiRToL.getMonthHeaderHeight() + 2); 
        assertEquals("last logical column in RToL", JXMonthView.DAYS_IN_WEEK - 1, 
                dayGridRToL.x);
        // same for LToR
        Point dayGridLToR = uiLToR.getDayGridPositionAtLocation(
                monthBounds.x + 2, monthBounds.y + monthBounds.height - 20); 
        assertEquals("first logical column in LToR", 0, dayGridLToR.x);
     }

    @Test
    public void testDayGridPositionWeekHeader() {
        // This test will not work in a headless configuration.
        if (GraphicsEnvironment.isHeadless()) {
            LOG.info("cannot run test - headless environment");
            return;
        }
        BasicMonthViewUI uiRToL = getRealizedMonthViewUI(ComponentOrientation.RIGHT_TO_LEFT, true);
        BasicMonthViewUI uiLToR = getRealizedMonthViewUI(ComponentOrientation.LEFT_TO_RIGHT, true);
        Rectangle monthBounds = uiRToL.getMonthBoundsAtLocation(20, 20);
        // first column near bottom
        Point dayGridRToL = uiRToL.getDayGridPositionAtLocation(
                monthBounds.x + monthBounds.width - 2, 
                monthBounds.y + uiRToL.getMonthHeaderHeight() + 2); 
        assertEquals("weeks of year column in RTL", -1, 
                dayGridRToL.x);
        // same for LToR
        Point dayGridLToR = uiLToR.getDayGridPositionAtLocation(
                monthBounds.x + 2, 
                monthBounds.y + uiRToL.getMonthHeaderHeight() + 2); 
        assertEquals("first logical column in LToR", -1, dayGridLToR.x);
     }
    
    /**
     * day grid returns null for hitting month header.
     */
    @Test
    public void testDayGridPositionMonthHeaderHitLToR() {
        // This test will not work in a headless configuration.
        if (GraphicsEnvironment.isHeadless()) {
            LOG.info("cannot run test - headless environment");
            return;
        }
        BasicMonthViewUI ui = getRealizedMonthViewUI(ComponentOrientation.LEFT_TO_RIGHT);
        Rectangle monthBounds = ui.getMonthBoundsAtLocation(20, 20);
        assertNull("hit in header must return null grid position", 
                ui.getDayGridPositionAtLocation(monthBounds.x + 2, monthBounds.y + 2));
    }
    
    /**
     * day grid returns null for hitting month header.
     */
    @Test
    public void testDayGridPositionMonthHeaderHitRToL() {
        // This test will not work in a headless configuration.
        if (GraphicsEnvironment.isHeadless()) {
            LOG.info("cannot run test - headless environment");
            return;
        }
        BasicMonthViewUI ui = getRealizedMonthViewUI(ComponentOrientation.RIGHT_TO_LEFT);
        Rectangle monthBounds = ui.getMonthBoundsAtLocation(20, 20);
        assertNull("hit in header must return null grid position", 
                ui.getDayGridPositionAtLocation(monthBounds.x + 2, monthBounds.y + 2));
    }
    
    /**
     * coordinate mapping: get calendar from logical grid 
     *   coordinates.
     */
    @Test
    public void testMonthFromGrid() {
        fail("TODO: re-visit monthfrom grid");
        // This test will not work in a headless configuration.
//        if (GraphicsEnvironment.isHeadless()) {
//            LOG.info("cannot run test - headless environment");
//            return;
//        }
//        BasicMonthViewUI uiRToL = getRealizedMonthViewUI(ComponentOrientation.RIGHT_TO_LEFT);
//        BasicMonthViewUI uiLToR = getRealizedMonthViewUI(ComponentOrientation.LEFT_TO_RIGHT);
//        LocalDateTime month = uiLToR.getMonth(0, 0);
//        LocalDateTime first = uiLToR.getFirstDisplayedDay();
//        MonthOfYear monthOfFirst = first.getMonthOfYear();
//        first = first.withMonths(MonthOf);
//        assertEquals(monthField, first.get(Calendar.MONTH));
//        LocalDateTime monthRL = uiRToL.getMonth(0, 0);
//        first.setTime(monthRL);
//        assertEquals("logical coordinates must be independent of orientation",
//                monthField, first.get(Calendar.MONTH));
    }

    /**
     * coordinate mapping: logical grid coordinates.
     */
    @Test
    public void testMonthGridPositionAtLocation() {
        // This test will not work in a headless configuration.
        if (GraphicsEnvironment.isHeadless()) {
            LOG.info("cannot run test - headless environment");
            return;
        }
        BasicMonthViewUI uiRToL = getRealizedMonthViewUI(ComponentOrientation.RIGHT_TO_LEFT);
        BasicMonthViewUI uiLToR = getRealizedMonthViewUI(ComponentOrientation.LEFT_TO_RIGHT);
        Point gridPositionLToR = uiLToR.getMonthGridPositionAtLocation(20, 20);
        assertEquals(0, gridPositionLToR.x);
        Point gridPositionRToL = uiRToL.getMonthGridPositionAtLocation(20, 20);
        assertEquals(1, gridPositionRToL.x);
    }
    
    

    /**
     * coordinate mapping: monthBounds in pixel.
     * 
     */
    @Test
    public void testMonthBoundsAtLocation() {
        // This test will not work in a headless configuration.
        if (GraphicsEnvironment.isHeadless()) {
            LOG.info("cannot run test - headless environment");
            return;
        }
        BasicMonthViewUI uiRToL = getRealizedMonthViewUI(ComponentOrientation.RIGHT_TO_LEFT);
        BasicMonthViewUI uiLToR = getRealizedMonthViewUI(ComponentOrientation.LEFT_TO_RIGHT);
        Rectangle monthBoundsRToL = uiRToL.getMonthBoundsAtLocation(20, 20);
        Rectangle monthBoundsLToR = uiLToR.getMonthBoundsAtLocation(20, 20);
        // bounds of first
        assertEquals("bounds of left-most month must be equal", 
                monthBoundsLToR, monthBoundsRToL);
        Rectangle monthBoundsTwoRToL = uiRToL.getMonthBoundsAtLocation(
                        monthBoundsRToL.width + 20, 20);
        Rectangle monthBoundsTwoLToR = uiRToL.getMonthBoundsAtLocation(
                monthBoundsLToR.width + 20, 20);
        assertEquals("bounds of right-most month must be equal", 
                monthBoundsTwoLToR, monthBoundsTwoRToL);
        
    }

    /**
     * coordinate mapping: monthBounds in pixel.
     * 
     */
    @Test
    public void testMonthHeaderBoundsAtLocation() {
        // This test will not work in a headless configuration.
        if (GraphicsEnvironment.isHeadless()) {
            LOG.info("cannot run test - headless environment");
            return;
        }
        JXMonthView monthView = new JXMonthView();
        monthView.setTraversable(true);
        JXFrame frame = new JXFrame();
        frame.add(monthView);
        frame.pack();
        BasicMonthViewUI ui = (BasicMonthViewUI) monthView.getUI();
        Rectangle monthBoundsLToR = ui.getMonthHeaderBoundsAtLocation(20, 20);
        assertEquals("", ui.getMonthHeaderHeight(), monthBoundsLToR.height);
    }

    /**
     * Returns the ui of a realized JXMonthView with 2 columns and the 
     * given componentOrientation without showingWeekNumbers.
     * 
     * NOTE: this must not be used in a headless environment.
     * 
     * @param co
     * @return
     */
    private BasicMonthViewUI getRealizedMonthViewUI(ComponentOrientation co) {
        return getRealizedMonthViewUI(co, false);
    }

    /**
     * Returns the ui of a realized JXMonthView with
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
    private BasicMonthViewUI getRealizedMonthViewUI(ComponentOrientation co,
            boolean isShowingWeekNumbers) {
        JXMonthView monthView = new JXMonthView();
        monthView.setPreferredColumnCount(2);
        monthView.setPreferredRowCount(2);
        monthView.setComponentOrientation(co);
        monthView.setShowingWeekNumber(isShowingWeekNumbers);
        LocalDateTime calendar = monthView.getFirstDisplayedDay()
            .withDate(2008, 2, 20);
        monthView.setFirstDisplayedDay(calendar);
        JXFrame frame = new JXFrame();
        frame.add(monthView);
        frame.pack();
        frame.setSize(frame.getWidth() + 40, frame.getHeight() + 40);
        frame.setVisible(true);
        BasicMonthViewUI ui = (BasicMonthViewUI) monthView.getUI();
        return ui;
    }

    /**
     * cleanup date representation as long: new api getDayAtLocation. will
     * replace getDayAt which is deprecated as a first step.
     */
    @SuppressWarnings("deprecation")
    @Test
    public void testGetDayAtLocation() {
        // This test will not work in a headless configuration.
        if (GraphicsEnvironment.isHeadless()) {
            LOG.info("cannot run test - headless environment");
            return;
        }
        JXMonthView monthView = new JXMonthView();
        JXFrame frame = new JXFrame();
        frame.add(monthView);
        frame.pack();
        Dimension pref = monthView.getPreferredSize();
        pref.width = pref.width / 2;
        pref.height = pref.height / 2;
        LocalDateTime date = monthView.getDayAtLocation(pref.width, pref.height);
        assertNotNull(date);
    }

    
    /**
     * Issue 711-swingx: today notify-only property.
     * Changed to read-only in monthView
     */
    @Test
    public void testTodayUpdate() {
        JXMonthView monthView = new JXMonthView();
        LocalDateTime first = ((BasicMonthViewUI) monthView.getUI()).getToday();
        monthView.updateUI();
        assertEquals(first, ((BasicMonthViewUI) monthView.getUI()).getToday());
    }



    @Test
    public void testCustomWeekdays() {
        fail("TODO: implement uiManager configured days of week");
//        String[] days = new String[] {"1", "2", "3", "4", "5", "6", "7"};
//        UIManager.put("JXMonthView.daysOfTheWeek", days);
//        JXMonthView monthView = new JXMonthView(Locale.GERMAN);
//        try {
//            assertWeekdays(monthView, days);
//            monthView.setLocale(Locale.FRENCH);
//            assertWeekdays(monthView, days);
//        } finally {
//            UIManager.put("JXMonthView.daysOfTheWeek", null);
//        }
    }
//    private void assertWeekdays(JXMonthView monthView, String[] weekdays) {
//        // sanity
//        for (int i = 0; i < 7; i++) {
//            assertEquals(weekdays[i], monthView.getDaysOfTheWeek()[i]);
//        }
//    }
    /**
     * test localized weekday names.
     */
    @Test
    public void testLocaleWeekdays() {
        Locale french = Locale.FRENCH;
        JXMonthView monthView = new JXMonthView(french);
        assertWeekdays(monthView, french);
        Locale german = Locale.GERMAN;
        monthView.setLocale(german);
        assertWeekdays(monthView, german);
    }

    private void assertWeekdays(JXMonthView monthView, Locale french) {
        // sanity
        assertEquals(french, monthView.getLocale());
        Map<DayOfWeek, String> weekDays = DateTimeUtils.getShortDayOfWeekTexts(french);
        for (DayOfWeek day : DayOfWeek.values()) {
            assertEquals(weekDays.get(day), monthView.getDayOfTheWeekText(day));
        }
    }
    
    /**
     * Issue ??-swingx: zero millis are valid.
     * 
     * bad marker in ui-delegate ... but looks okay? 
     */
    @Test
    public void testZeroFirstDisplayedDate() {
        JXMonthView monthView = new JXMonthView();
        LocalDateTime first = monthView.getUI().getLastDisplayedDay();
        monthView.updateUI();
        assertEquals(first, monthView.getUI().getLastDisplayedDay());
    }


    /**
     * Issue #708-swingx: updateUI changes state.
     * 
     * Here: test that lastDisplayedDate is unchanged.
     */
    @Test
    public void testUpdateUILast() {
        final JXMonthView monthView = new JXMonthView();
        LocalDateTime first = monthView.getUI().getLastDisplayedDay();
        monthView.updateUI();
        assertEquals(first, monthView.getUI().getLastDisplayedDay());
    };

    /**
     * Issue #708-swingx: updateUI changes state.
     * 
     * Here: test that firstDisplayedDate is unchanged.
     */
    @Test
    public void testUpdateUIFirstDate() {
        final JXMonthView monthView = new JXMonthView();
        LocalDateTime first = ((BasicMonthViewUI) monthView.getUI()).getFirstDisplayedDay();
        monthView.updateUI();
        assertEquals(first, ((BasicMonthViewUI) monthView.getUI()).getFirstDisplayedDay());
    };
    
//    /**
//     * Issue #708-swingx: updateUI changes state.
//     * 
//     * Here: test that firstDisplayedYear is unchanged.
//     */
//    @Test
//    public void testUpdateUIFirstYear() {
//        final JXMonthView monthView = new JXMonthView();
//        long first = ((BasicMonthViewUI) monthView.getUI()).getFirstDisplayedYear();
//        monthView.updateUI();
//        assertEquals(first, ((BasicMonthViewUI) monthView.getUI()).getFirstDisplayedYear());
//    };
//    
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
//        final JXMonthView monthView = new JXMonthView(cal.getTime());
//        long first = ((BasicMonthViewUI) monthView.getUI()).getFirstDisplayedMonth();
//        monthView.updateUI();
//        assertEquals(first, ((BasicMonthViewUI) monthView.getUI()).getFirstDisplayedMonth());
//    };
    

}
