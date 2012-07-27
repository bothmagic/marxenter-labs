/*
 * $Id: BasicMonthViewVisualCheck.java 3333 2009-04-23 12:21:22Z kleopatra $
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
package org.jdesktop.swingx.plaf.basic;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.SortedSet;
import java.util.logging.Logger;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Box;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jdesktop.swingx.InteractiveTestCase;
import org.jdesktop.swingx.JXCalendar;
import org.jdesktop.swingx.JXDateChooser;
import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.SwingXUtilities;
import org.jdesktop.swingx.action.AbstractActionExt;
import org.jdesktop.swingx.calendar.CalendarCellState;
import org.jdesktop.swingx.calendar.CalendarStringValues.CalendarStringValue;
import org.jdesktop.swingx.calendar.DateSelectionModel.SelectionMode;
import org.jdesktop.swingx.decorator.ColorHighlighter;
import org.jdesktop.swingx.decorator.Highlighter;
import org.jdesktop.swingx.event.DateSelectionEvent;
import org.jdesktop.swingx.event.DateSelectionListener;
import org.jdesktop.swingx.graphics.GraphicsUtilities;
import org.jdesktop.swingx.plaf.basic.PagingAnimator.Direction;
import org.jdesktop.swingx.plaf.basic.PagingAnimator.DoubleImageIcon;
import org.jdesktop.swingx.renderer.StringValue;
import org.junit.Test;
import org.pushingpixels.trident.Timeline;
import org.pushingpixels.trident.swing.SwingRepaintCallback;

/**
 * TODO add type doc
 * 
 * @author Jeanette Winzenburg
 */
public class BasicCalendarVisualCheck extends InteractiveTestCase {

    @SuppressWarnings("unused")
    private static final Logger LOG = Logger
            .getLogger(BasicCalendarVisualCheck.class.getName());
    
    public static void main(String[] args) {
        BasicCalendarVisualCheck test = new BasicCalendarVisualCheck();
        try {
//            test.runInteractiveTests();
//            test.runInteractiveTests(".*Rendering.*");
            test.runInteractiveTests(".*Animation.*");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void interactiveAnimation() {
        final JXCalendar calendarView = new JXCalendar();
        final BasicCalendarUI ui = (BasicCalendarUI) calendarView.getUI();
        final PagingAnimator animator = new PagingAnimator(ui);
        final JLabel label = new JLabel("text...");
        label.setIcon(animator.getIcon());
        final JComponent comp = new JPanel(new GridLayout());
        comp.add(calendarView);
        comp.add(label);
        JXFrame frame = wrapInFrame(comp, "image animation");
        Action createImage = new AbstractAction("next") {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                animator.beforeMove(Direction.FORWARD);
                ui.performAction(Navigator.NEXT_PAGE_KEY);
                animator.afterMove(Direction.FORWARD);
            }
        };
        addAction(frame, createImage);
        
        Action previous = new AbstractAction("previous") {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                animator.beforeMove(Direction.BACKWARD);
                ui.performAction(Navigator.PREVIOUS_PAGE_KEY);
                animator.afterMove(Direction.BACKWARD);
            }
        };
        addAction(frame, previous);
        Action printBounds = new AbstractAction("printBounds") {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
                LOG.info("pageBounds " + ui.getPageBounds());
            }
        };
        addAction(frame, printBounds);
        addComponentOrientationToggle(frame);
        show(frame, 800, 400);
    }
    
    
    /**
     * Issue #750-swingx: use rendering to side-step antialiase probs.
     * 
     * Debugging ...
     */
    public void interactiveRenderingOn() {
        new JXCalendar();
        // KEEP this is global state - uncomment for debug painting completely
//        UIManager.put("JXCalendar.trailingDayForeground", Color.YELLOW);
//        UIManager.put("JXCalendar.leadingDayForeground", Color.ORANGE);
//        UIManager.put("JXCalendar.weekOfTheYearForeground", Color.GREEN);
//        UIManager.put("JXCalendar.unselectableDayForeground", Color.MAGENTA);
        String frameTitle = "Debug painting: rendering on";
        showDebugMonthView(frameTitle);
    }

    /**
     * @param frameTitle
     * @param disableRendering
     */
    private void showDebugMonthView(String frameTitle) {
        final JXCalendar calendarView = new JXCalendar();
//        calendarView.setSelectionModel(new SingleDaySelectionModel());
        calendarView.setSelectionDate(new Date());
        String pattern = "MMM yy";
        StringValue sv = new CalendarStringValue(calendarView.getLocale(), pattern);
        calendarView.setStringValue(sv, CalendarCellState.MONTH_CELL);
        Highlighter hl = new ColorHighlighter(
                null, null, null, Color.MAGENTA);
        calendarView.setHighlighters(hl);
//        monthView.setSelectionBackground(Color.GRAY);
//        monthView.setSelectionForeground(Color.GREEN);
//        monthView.setTodayBackground(Color.PINK);
        calendarView.setShowingWeekNumber(true);
        DateSelectionListener l = new DateSelectionListener() {
            
            @Override
            public void valueChanged(DateSelectionEvent ev) {
                Date unselectable = null;
                SortedSet<Date> unselect = calendarView.getSelectionModel().getUnselectableDates();
                if (!unselect.isEmpty()) {
                    unselectable = unselect.first();
                }
                LOG.info("type/selection/lead/unselectable " + 
                        ev.getEventType() + calendarView.getSelectionDate()
                        + " / " + calendarView.getUI().getLead().getTime()
                        + " / " + unselectable
                        );
            }
        };
        calendarView.getSelectionModel().addDateSelectionListener(l);
        
        ActionListener al = new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
//                LOG.info("got actionEvent:" + e.getActionCommand() + calendarView.getSelectionDate());
            }
        };
        calendarView.addActionListener(al);
        final JXFrame frame = wrapInFrame(calendarView, frameTitle);
        addComponentOrientationToggle(frame);
        final JXDateChooser unselectable = new JXDateChooser();
        Action toggleShowingWeekNumbers = new AbstractAction("toggle weekNumbers") {
            
            public void actionPerformed(ActionEvent e) {
                calendarView.setShowingWeekNumber(!calendarView.isShowingWeekNumber());
                unselectable.getCalendarView().setShowingWeekNumber(calendarView.isShowingWeekNumber());
            }
            
        };
        addAction(frame, toggleShowingWeekNumbers);
        
        Action setDate = new AbstractAction("set selection") {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                calendarView.setSelectionDate(new Date());
            }
        };
        addAction(frame, setDate);
        
        unselectable.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (e.getActionCommand().equals(JXDateChooser.CANCEL_KEY)) return;
                if (unselectable.getDate() == null) return;
                LOG.info("unselectable? " + unselectable.getDate());
                calendarView.setUnselectableDates(unselectable.getDate());
                calendarView.repaint();
            }
            
        });
        final JComboBox zoneSelector = new JComboBox(Locale.getAvailableLocales());
        // Synchronize the monthView's and selector's zones.
        zoneSelector.setSelectedItem(calendarView.getLocale());

        // Set the monthView's time zone based on the selected time zone.
        zoneSelector.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                Locale zone = (Locale) zoneSelector.getSelectedItem();
                SwingXUtilities.setComponentTreeLocale(frame, zone);
            }
        });


        JComponent pickers = Box.createHorizontalBox();
        pickers.add(new JLabel("Unselectable: "));
        pickers.add(unselectable);
        pickers.add(new JLabel("Locale: "));
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
        final JXCalendar monthView = new JXCalendar();
        monthView.setShowingWeekNumber(true);
        monthView.setSelectionMode(SelectionMode.SINGLE_INTERVAL_SELECTION);
        final BasicCalendarUI ui = ((BasicCalendarUI) monthView.getUI());
        monthView.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseReleased(MouseEvent e) {
                LOG.info("day grid position " + 
                        ui.getCellGridPositionAtLocation(e.getX(), e.getY()) 
                      + "\nday bounds " + 
                        ui.getCellBoundsAtLocation(e.getX(), e.getY()));
            }
            
        });
        Action action = new AbstractActionExt("toggle minimal") {

            public void actionPerformed(ActionEvent e) {
                int minimal = monthView.getSelectionModel().getMinimalDaysInFirstWeek();
                monthView.getSelectionModel().setMinimalDaysInFirstWeek(minimal > 1 ? 1 : 4);
            }
            
        };
        final JXFrame frame = wrapInFrame(monthView, "test mapping: printed on mouse release");
        addAction(frame, action);
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
        final JXCalendar monthView = new JXCalendar();
        monthView.setShowingWeekNumber(true);
        monthView.setSelectionMode(SelectionMode.SINGLE_INTERVAL_SELECTION);
        Action action = new AbstractActionExt("toggle minimal") {

            public void actionPerformed(ActionEvent e) {
                int minimal = monthView.getSelectionModel().getMinimalDaysInFirstWeek();
                monthView.getSelectionModel().setMinimalDaysInFirstWeek(minimal > 1 ? 1 : 4);
            }
            
        };
        final JXFrame frame = wrapInFrame(monthView, "click unselectable fires ActionEvent");
        addAction(frame, action);
        addComponentOrientationToggle(frame);
        final JComboBox dayOfWeekComboBox = new JComboBox(new String[]{"Sunday", "Monday", "Tuesday",
                "Wednesday", "Thursday", "Friday", "Saturday"});
        dayOfWeekComboBox.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                int selected = dayOfWeekComboBox.getSelectedIndex();
                monthView.setFirstDayOfWeek(selected + Calendar.SUNDAY);
                
            }
            
        });
        dayOfWeekComboBox.setSelectedIndex(monthView.getFirstDayOfWeek() - Calendar.SUNDAY);
        addStatusComponent(frame, dayOfWeekComboBox);
        frame.pack();
        frame.setVisible(true);
    }


    @Test
    public void testDummy() {
        // keep the runner happy
    }
}
