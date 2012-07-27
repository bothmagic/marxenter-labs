/*
 * $Id: JXMonthViewVisualCheck.java 3573 2010-01-08 12:57:24Z kleopatra $
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
package org.jdesktop.swingx;

import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.logging.Logger;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.UIManager;

import org.jdesktop.swingx.action.AbstractActionExt;
import org.jdesktop.swingx.calendar.CalendarUtils;
import org.jdesktop.swingx.calendar.DateSelectionModel;
import org.jdesktop.swingx.calendar.SingleDaySelectionModel;
import org.jdesktop.swingx.calendar.DateSelectionModel.SelectionMode;
import org.jdesktop.swingx.test.XTestUtils;

/**
 * Test to expose known issues with JXCalendar.
 * 
 * @author Jeanette Winzenburg
 */
public class JXCalendarVisualCheck extends InteractiveTestCase {
    private static final Logger LOG = Logger.getLogger(JXCalendarVisualCheck.class
            .getName());

    @SuppressWarnings("unused")
    private Calendar calendar;

    public static void main(String[] args) {
//        UIManager.put("JXDateChooser.forceZoomable", Boolean.TRUE);
      setSystemLF(true);
      JXCalendarVisualCheck  test = new JXCalendarVisualCheck();
      try {
          test.runInteractiveTests();
//        test.runInteractiveTests(".*Event.*");
//          test.runInteractiveTests("interactive.*Zoomable.*");
//          test.runInteractiveTests("interactive.*Title.*");
//        test.runInteractiveTests("interactive.*Locale.*");
      } catch (Exception e) {
          System.err.println("exception when executing interactive tests:");
          e.printStackTrace();
      }
  }


    /**
     * Issue #1028-swingx: calendarView title looks wrong if box paddings = 0
     */
    public void interactiveTitleBorder() {
        JXCalendar calendarView = new JXCalendar();
        calendarView.setBoxPaddingX(0);
        calendarView.setBoxPaddingY(0);
        JComponent comp = Box.createHorizontalBox();
        comp.add(calendarView);
        JXCalendar other = new JXCalendar();
        comp.add(other);
        showInFrame(comp, "calendarView title border");
        
    }

    /**
     * Issue 807-swingx: JXCalendar must have visual clue if enabled.
     */
    public void interactiveDisabled() {
        JXCalendar calendarView = new JXCalendar();
        calendarView.setEnabled(false);
        JComponent comp = Box.createHorizontalBox();
        comp.add(calendarView);
        comp.add(new JXCalendar());
        showInFrame(comp, "disabled <--> enabled");
    }
    
    /**
     * Issue #931-swingx: JXCalendar not repainted on property change.
     * 
     * look for
     * - selectionBackground
     * - selectionForeground (not taken?)
     * - flaggedDateForeground
     * - componentOrientation (duplicate #996-swingx)
     * 
     */
    public void interactiveRepaintOnPropertyChange() {
        final JXCalendar calendarView = new JXCalendar();
        final Font font = calendarView.getFont();
        calendarView.setSelectionDate(CalendarUtils.startOfWeek(calendar, new Date()));
        final Color selectionBackground = calendarView.getSelectionBackground();
        final Color selectionForeground = calendarView.getSelectionForeground();
        JXFrame frame = wrapInFrame(calendarView, "repaint on propertyChange");
        Action toggleBackground = new AbstractAction("toggleSelectionBack") {
            
            public void actionPerformed(ActionEvent e) {
                calendarView.setSelectionBackground(
                        Color.PINK.equals(calendarView.getSelectionBackground()) ? 
                                selectionBackground : Color.PINK);
            }
            
        };
        addAction(frame, toggleBackground);
        Action toggleForeground = new AbstractAction("toggleSelectionFore") {
            
            public void actionPerformed(ActionEvent e) {
                
                calendarView.setSelectionForeground(
                        Color.RED.equals(calendarView.getSelectionForeground()) ? 
                                selectionForeground : Color.RED);
            }
            
        };
        addAction(frame, toggleForeground);
        
        Action toggleCO = new AbstractAction("toggleCO") {
            
            public void actionPerformed(ActionEvent e) {
                
                if (calendarView.getComponentOrientation().isLeftToRight()) {
                    calendarView.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
                } else {
                    calendarView.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
                    
                }
            }
            
        };
        addAction(frame, toggleCO);
        Action toggleFont = new AbstractAction("toggleFont") {
            
            public void actionPerformed(ActionEvent e) {
                
                if (calendarView.getFont().isItalic()) {
                    calendarView.setFont(font);
                } else {
                    calendarView.setFont(font.deriveFont(Font.ITALIC));
                }
            }
            
        };
        addAction(frame, toggleFont);
        Action toggleEnabled = new AbstractAction("toggleEnabled") {
            
            public void actionPerformed(ActionEvent e) {
                calendarView.setEnabled(!calendarView.isEnabled());
            }
            
        };
        addAction(frame, toggleEnabled);
        show(frame);
        
    }

    /**
     * Issue #786-swingx: IllegalStateException when paintDays of April 2008.
     * 
     * Problem was in that particular timezone - traversing to April 
     * 
     * Assumption of staying at startOfWeek in paintDays is wrong if the month
     * is the month of turning on the DST. Remove the check for now.
     * 
     */
    public void interactiveTimeZoneDST() {
        JXCalendar calendarView = new JXCalendar();
        Calendar calendar = calendarView.getPage();
        calendar.set(2008, Calendar.MARCH, 31);
        calendarView.setSelectionDate(calendar.getTime());
        TimeZone cairo = TimeZone.getTimeZone("Africa/Cairo");
        calendarView.setTimeZone(cairo);
        JXFrame frame = showInFrame(calendarView, "MonthView: DST");
        addStatusMessage(frame, "IllegalState in April");
    }


    /**
     * Issue #749-swingx: enhanced flagged dates support (add/remove)
     * 
     * Visually check if the calendarView is updated on toggling several properties.
     */
    public void interactiveToggleProperties() {
        final JXCalendar calendarView = new JXCalendar(); 
        final JXFrame frame = showInFrame(calendarView, "MonthView - click property and see the change");
        final Calendar calendar = calendarView.getPage();
        calendar.add(Calendar.DATE, 5);
        Action unselectable = new AbstractActionExt("lowerbound") {
            public void actionPerformed(ActionEvent e) {
                calendarView.setLowerBound(calendarView.getLowerBound() == null ? calendar.getTime() : null);
            }
            
        };
        addAction(frame, unselectable);
        Action weekNumbers = new AbstractActionExt("weekNumbers") {
            public void actionPerformed(ActionEvent e) {
                calendarView.setShowingWeekNumber(!calendarView.isShowingWeekNumber());
            }
            
        };
        addAction(frame, weekNumbers);
        Action firstDay = new AbstractActionExt("firstDay") {
            public void actionPerformed(ActionEvent e) {
                int firstDay = calendarView.getFirstDayOfWeek();
                calendarView.setFirstDayOfWeek(firstDay == Calendar.SUNDAY ? 
                        Calendar.MONDAY : Calendar.SUNDAY);
            }
            
        };
        addAction(frame, firstDay);
        // PENDING JW: change to use StringValue
//        Action daysOfWeek = new AbstractActionExt("daysOfWeek") {
//            String[] days = {"S", "M", "D", "M", "D", "F", "S"};
//            public void actionPerformed(ActionEvent e) {
//                String[] dof = calendarView.getDaysOfTheWeek();
//                if (dof[0].equals(days[0])) {
//                    calendarView.setDaysOfTheWeek(null);
//                } else {
//                    calendarView.setDaysOfTheWeek(days);
//                }    
//            }
//            
//        };
//        addAction(frame, daysOfWeek);
        frame.pack();
    };
    
    /**
     * Issue #736-swingx: calendarView cannot cope with minimalDaysInFirstWeek.
     * 
     * Here: look at impact of forcing the minimalDays to a value different
     * from the calendar. Days must be displayed in starting from the 
     * first row under the days-of-week.
     */
    public void interactiveMinimalDaysInFirstWeek() {
        final JXCalendar calendarView = new JXCalendar();
        calendarView.setShowingWeekNumber(true);
        Action action = new AbstractActionExt("toggle minimal") {

            public void actionPerformed(ActionEvent e) {
                int minimal = calendarView.getSelectionModel().getMinimalDaysInFirstWeek();
                calendarView.getSelectionModel().setMinimalDaysInFirstWeek(minimal > 1 ? 1 : 4);
            }
            
        };
        final JXFrame frame = wrapInFrame(calendarView, "click unselectable fires ActionEvent");
        addAction(frame, action);
        addComponentOrientationToggle(frame);
        JXStatusBar bar = getStatusBar(frame);
        final JComboBox dayOfWeekComboBox = new JComboBox(new String[]{"Sunday", "Monday", "Tuesday",
                "Wednesday", "Thursday", "Friday", "Saturday"});
        dayOfWeekComboBox.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                int selected = dayOfWeekComboBox.getSelectedIndex();
                calendarView.setFirstDayOfWeek(selected + Calendar.SUNDAY);
                
            }
            
        });
        dayOfWeekComboBox.setSelectedIndex(calendarView.getFirstDayOfWeek() - Calendar.SUNDAY);
        bar.add(dayOfWeekComboBox);
        frame.pack();
        frame.setVisible(true);
    }

    /**
     * Issue #736-swingx: calendarView cannot cope with minimalDaysInFirstWeek.
     * 
     * Here: look at impact of forcing the minimalDays to a value different
     * from the calendar. Days must be displayed in starting from the 
     * first row under the days-of-week. Selection must be reflected in the 
     * datepicker.
     */
//    public void interactiveMinimalDaysInFirstWeekPicker() {
//        JXDateChooser picker = new JXDateChooser();
//        final JXCalendar calendarView = picker.getMonthView();
//        calendarView.setShowingWeekNumber(true);
//        calendarView.setShowingLeadingDays(true);
//        calendarView.setShowingTrailingDays(true);
//        Action action = new AbstractActionExt("toggle minimal") {
//
//            public void actionPerformed(ActionEvent e) {
//                int minimal = calendarView.getSelectionModel().getMinimalDaysInFirstWeek();
//                calendarView.getSelectionModel().setMinimalDaysInFirstWeek(minimal > 1 ? 1 : 4);
//            }
//            
//        };
//        final JXFrame frame = wrapInFrame(picker, "click unselectable fires ActionEvent");
//        addAction(frame, action);
//        frame.pack();
//        frame.setVisible(true);
//    }


    

    /**
     * Issue #706-swingx: picker doesn't update calendarView.
     * 
     * Here: visualize weird side-effects of calendarView.updateUI - year 
     * incremented.
     */
    public void interactiveUpdateUIMonthView() {
//        calendar.set(1955, 10, 9);
        final JXCalendar calendarView = new JXCalendar(); 
        final JXFrame frame = showInFrame(calendarView, "MonthView update ui - visible month kept");
        Action action = new AbstractActionExt("toggleUI") {
            public void actionPerformed(ActionEvent e) {
                calendarView.updateUI();
            }
            
        };
        addAction(frame, action);
        frame.pack();
    };

    /**
     * Issue #706-swingx: picker doesn't update calendarView.
     * 
     * Show toggle of UI (selectin color)
     */
    public void interactiveUpdateUIMonthViewCustomUI() {
        final JXCalendar calendarView = new JXCalendar();
        calendarView.setSelectionDate(new Date());
        final JXFrame frame = showInFrame(calendarView, "MonthView custom ui (selection color)");
        Action action = new AbstractActionExt("toggleUI") {
            public void actionPerformed(ActionEvent e) {
                String uiClass = (String) UIManager.get(JXCalendar.uiClassID);
                boolean custom = uiClass.indexOf("Custom") > 0;
                if (!custom) {
                    UIManager.put(JXCalendar.uiClassID, "org.jdesktop.swingx.test.CustomMonthViewUI");
                } else {
                    UIManager.put(JXCalendar.uiClassID, null);
                }
                calendarView.updateUI();
                custom = !custom;
            }
            
        };
        addAction(frame, action);
        frame.pack();
    };
    

    /**
     * #703-swingx: set date to first of next doesn't update the view.
     * 
     * Behaviour is consistent with core components. Except that it is doing 
     * too much: revalidate most probably shouldn't change the scrolling state?
     * 
     * Simulated misbehaviour here: multi-month spanning selection, travers into the future and
     * add selection at the end - jumps back to first. Auto-scroll in the delegates
     * selection listener would have the effect.
     * 
     */
    public void interactiveAutoScrollOnSelectionSim() {
        final JXCalendar us = new JXCalendar();
        us.setSelectionMode(SelectionMode.SINGLE_INTERVAL_SELECTION);
        final Calendar today = Calendar.getInstance();
        CalendarUtils.endOfMonth(today);
        Date start = today.getTime();
        today.add(Calendar.DAY_OF_MONTH, 60);
        us.setSelectionInterval(start, today.getTime());
        JXFrame frame = wrapInFrame(us, "Simulate autoscroll on selection");
        Action nextMonthInterval = new AbstractActionExt("add selected") {

            public void actionPerformed(ActionEvent e) {
                if (us.isSelectionEmpty()) return;
                Date start = us.getSelectionDate();
                
                today.setTime(us.getLastSelectionDate());
                today.add(Calendar.DAY_OF_MONTH, 5);
                us.addSelectionInterval(start, today.getTime());
            }
            
        };
        addAction(frame, nextMonthInterval);
        frame.pack();
        frame.setVisible(true);
    }

    /**
     * #681-swingx: first row overlaps days.
     * 
     * Looks like a problem with the constructor taking a locale? 
     * Default is okay (even if German), US is okay, explicit german is wrong.
     */
    public void interactiveFirstRowOfMonthSetLocale() {
        JPanel p = new JPanel();
        // default constructor
        p.add(new JXCalendar());
        // explicit us locale
        JXCalendar us = new JXCalendar();
        us.setLocale(Locale.US);
        p.add(us);
        // explicit german locale
        JXCalendar german = new JXCalendar();
        german.setLocale(Locale.GERMAN);
        p.add(german);
        showInFrame(p, "first row overlapping - setLocale");
    }

   
    /**
     * #681-swingx: first row overlaps days.
     * 
     * Looks like a problem with the constructor taking a locale? 
     * Default is okay (even if German), US is okay, explicit german is wrong.
     */
    public void interactiveFirstRowOfMonthLocaleConstructor() {
        JPanel p = new JPanel();
        // default constructor
        p.add(new JXCalendar());
        // explicit us locale
        p.add(new JXCalendar(Locale.US));
//         explicit german locale
        p.add(new JXCalendar(Locale.GERMAN));
        showInFrame(p, "first row overlapping - constructor");
    }
    /**
     * #681-swingx: first row overlaps days.
     * Here everything looks okay.
     * 
     * @see #interactiveFirstRowOfMonthLocaleDependent()
     */
    public void interactiveFirstRowOfMonth() {
        JXCalendar calendarView = new JXCalendar();
        calendar.set(2008, 0, 1);
        calendarView.setSelectionDate(calendar.getTime());
        showInFrame(calendarView, "first row");
    }

    /**
     * Issue #618-swingx: JXCalendar displays problems with non-default
     * timezones.
     * 
     */
    public void interactiveUpdateLocale() {
        JComponent panel = Box.createVerticalBox();

        final JXDateChooser picker = new JXDateChooser(new Date());
        final JXCalendar calendarView = new JXCalendar();
        calendarView.setShowingWeekNumber(true);
        final JComboBox zoneSelector = new JComboBox(Locale.getAvailableLocales());
        // Synchronize the calendarView's and selector's zones.
        zoneSelector.setSelectedItem(calendarView.getLocale());

        // Set the calendarView's time zone based on the selected time zone.
        zoneSelector.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                Locale zone = (Locale) zoneSelector.getSelectedItem();
                calendarView.setLocale(zone);
                picker.setLocale(zone);
//                if ("sh".equals(zone.getLanguage()) ){
//                    String[] months = DateFormatSymbols.getInstance(zone).getMonths();
//                    SimpleDateFormat simple = new SimpleDateFormat("MMMM", zone);
//                    DateFormat format = DateFormat.getDateInstance(DateFormat.FULL, zone);
//                    LOG.info("serbian latin: " + zone + 
//                            "/" + format.format(new Date()) +
//                            " / " + months[0] + 
//                            " / " + simple.format(new Date()));
//                }
//                
                
            }
        });

        panel.add(picker);
        panel.add(calendarView);
        panel.add(zoneSelector);
        showInFrame(panel, "Locale");
    }

    
    /**
     * Issue #618-swingx: JXCalendar displays problems with non-default
     * timezones.
     * 
     */
    public void interactiveUpdateTodayOnSetTimeZone() {
        JComponent panel = Box.createVerticalBox();
        
        final JComboBox zoneSelector = new JComboBox(TimeZone.getAvailableIDs());
        final JXCalendar calendarView = new JXCalendar();
        // Synchronize the picker and selector's zones.
        zoneSelector.setSelectedItem(calendarView.getTimeZone().getID());
        
        // Set the picker's time zone based on the selected time zone.
        zoneSelector.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                String zone = (String) zoneSelector.getSelectedItem();
                TimeZone tz = TimeZone.getTimeZone(zone);
                calendarView.setTimeZone(tz);
                
                assertEquals(tz, calendarView.getPage().getTimeZone());
            }
        });
        
        panel.add(calendarView);
        panel.add(zoneSelector);
        showInFrame(panel, "today - on setTimeZone");
    }
    
    /**
     * Issue #618-swingx: JXCalendar displays problems with non-default
     * timezones.
     * Issue #1143-swingx: JXCalendar NPE on setTimeZone/setModel
     * 
     */
    public void interactiveUpdateTodayOnSetModelTimeZone() {
        JComponent panel = Box.createVerticalBox();

        final JComboBox zoneSelector = new JComboBox(TimeZone.getAvailableIDs());
        final JXCalendar calendarView = new JXCalendar();
        // Synchronize the picker and selector's zones.
        zoneSelector.setSelectedItem(calendarView.getTimeZone().getID());

        // Set the picker's time zone based on the selected time zone.
        zoneSelector.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                String zone = (String) zoneSelector.getSelectedItem();
                TimeZone tz = TimeZone.getTimeZone(zone);
                DateSelectionModel model = new SingleDaySelectionModel();
                model.setTimeZone(tz);
                calendarView.setSelectionModel(model);
              
                assertEquals(tz, calendarView.getPage().getTimeZone());
            }
        });

        panel.add(calendarView);
        panel.add(zoneSelector);
        showInFrame(panel, "today - on setModel with different TimeZone");
    }
    
    /**
     * Issue #618-swingx: JXCalendar displays problems with non-default
     * timezones.
     * Issue #658-swingx: JXDateChooser today is not updated on timezone.
     * 
     * 
     */
    public void interactiveTimeZoneClearDateState() {
        JPanel panel = new JPanel();

        final JComboBox zoneSelector = new JComboBox(TimeZone.getAvailableIDs());
        final JXDateChooser picker = new JXDateChooser(new Date());
        final JXCalendar calendarView = new JXCalendar();
        calendarView.setSelectionDate(picker.getDate());
        calendarView.setLowerBound(XTestUtils.getStartOfToday(-10));
        calendarView.setUpperBound(XTestUtils.getStartOfToday(10));
        calendarView.setUnselectableDates(XTestUtils.getStartOfToday(2));
        // Synchronize the picker and selector's zones.
        zoneSelector.setSelectedItem(picker.getTimeZone().getID());

        // Set the picker's time zone based on the selected time zone.
        zoneSelector.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                String zone = (String) zoneSelector.getSelectedItem();
                TimeZone tz = TimeZone.getTimeZone(zone);
                picker.setTimeZone(tz);
                calendarView.setTimeZone(tz);
              
                assertEquals(tz, calendarView.getPage().getTimeZone());
            }
        });

        panel.add(zoneSelector);
        panel.add(picker);
        panel.add(calendarView);
        JXFrame frame = showInFrame(panel, "clear internal date-related state");
//        Action assertAction = new AbstractActionExt("assert dates") {
//
//            public void actionPerformed(ActionEvent e) {
//                Calendar cal = calendarView.getPage();
//                DateFormat format = DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.FULL);
//                format.setTimeZone(calendarView.getTimeZone());
//                LOG.info("cal/firstDisplayed " + 
//                        format.format(cal.getTime()) + " / " 
//                        +format.format(calendarView.getFirstDisplayedDay()));
//            }
//            
//        };
//        addAction(frame, assertAction);
        frame.pack();
    }
    

    /**
     * Issue #637-swingx: make JXCalendar Locale-aware.
     * 
     * Applied the patch as provided by pes17.
     * 
     */
    public void interactiveLocale() {
        JXCalendar calendarView = new JXCalendar(Locale.GERMAN);
        JXCalendar other = new JXCalendar(Locale.FRANCE);
        JComponent comp = new JPanel();
        comp.add(calendarView);
        comp.add(other);
        showInFrame(comp, "Localized calendarView");
    }

    /**
     * Issue #563-swingx: arrow keys active even if not focused.
     * focus the button and use the arrow keys: selection moves.
     * Reason was that the WHEN_IN_FOCUSED_WINDOW key bindings
     * were always installed. 
     * 
     * Fixed by dynamically bind/unbind component input map bindings
     * based on the JXCalendar's componentInputMapEnabled property.
     *
     */
    public void interactiveMistargetedKeyStrokes() {
        JXCalendar month = new JXCalendar();
        JComponent panel = new JPanel();
        panel.add(new JButton("something to focus"));
        panel.add(month);
        showInFrame(panel, "default - for debugging only");
    }
    
    /**
     * Issue #563-swingx: arrow keys active even if not focused.
     * focus the button and use the arrow keys: selection moves.
     *
     * Fixed by dynamically bind/unbind component input map bindings
     * based on the JXCalendar's componentInputMapEnabled property.
     */
    public void interactiveMistargetedKeyStrokesPicker() {
        JXCalendar month = new JXCalendar();
        JComponent panel = new JPanel();
        JXDateChooser button = new JXDateChooser();
        panel.add(button);
        panel.add(month);
        showInFrame(panel, "default - for debugging only");
    }
    
    /**
     * Informally testing adjusting property on mouse events.
     * 
     * Hmm .. not formally testable without mocks/ui unit tests?
     *
     */
    public void interactiveAdjustingOnMouse() {
        final JXCalendar month = new JXCalendar();
        // we rely on being notified after the ui delegate ... brittle.
        MouseAdapter m = new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                LOG.info("pressed - expect true " + month.getSelectionModel().isAdjusting());
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                LOG.info("released - expect false" + month.getSelectionModel().isAdjusting());
            }
            
        };
        month.addMouseListener(m);
        showInFrame(month, "Mouse and adjusting - state on pressed/released");
    }



//----------------------
    @Override
    protected void setUp() throws Exception {
        calendar = Calendar.getInstance();
    }

    
    /**
     * do nothing test - keep the testrunner happy.
     */
    public void testDummy() {
    }

}
