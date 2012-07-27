/*
 * $Id: JXMonthViewVisualTest.java 3339 2011-02-04 17:03:23Z kleopatra $
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

import java.awt.GraphicsEnvironment;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Logger;

import javax.swing.SwingUtilities;
import javax.time.calendar.LocalDateTime;

import org.jdesktop.swingx.InteractiveTestCase;
import org.jdesktop.swingx.JXFrame;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;


/**
 * JXMonthView unit tests which are expected to pass and are part of a stable build.
 *  
 * This one does not use mocks but some methods 
 * of InteractiveTestCase. That's why the passing methods could not be moved
 * into JXMonthViewTest.
 * 
 * @author Jeanette Winzenburg
 */
@RunWith(JUnit4.class)
public class JXMonthViewVisualTest extends InteractiveTestCase {
    @SuppressWarnings("all")
    private static final Logger LOG = Logger.getLogger(JXMonthViewVisualTest.class
            .getName());




//----------------------
    
    /**
     * Issue #659-swingx: lastDisplayedDate must be synched.
     * test that lastDisplayed from monthView is same as lastDisplayed from ui.
     * 
     * Here: initial packed size - one month shown.
     * 
     * @throws InvocationTargetException 
     * @throws InterruptedException 
     */
    @Test
    public void testLastDisplayedDateInitial() throws InterruptedException, InvocationTargetException {
        if (GraphicsEnvironment.isHeadless()) {
            LOG.info("cannot run lastDisplayedDate - headless");
            return;
        }
        final JXMonthView monthView = new JXMonthView();
        final JXFrame frame = wrapInFrame(monthView, "");
        frame.setVisible(true);
        SwingUtilities.invokeAndWait(new Runnable() {
            public void run() {
                LocalDateTime uiLast = monthView.getUI().getLastDisplayedDay();
                LocalDateTime viewLast = monthView.getLastDisplayedDay();
                assertEquals(uiLast, viewLast);
            }
        });
    }
    
    /**
     * 
     * Issue #659-swingx: lastDisplayedDate must be synched.
     * 
     * test that lastDisplayed from monthView is same as lastDisplayed from ui.
     * 
     * Here: change the size of the view which allows the ui to display more
     * columns/rows.
     * 
     * @throws InvocationTargetException 
     * @throws InterruptedException 
     */
    @Test
    public void testLastDisplayedDateSizeChanged() throws InterruptedException, InvocationTargetException {
        if (GraphicsEnvironment.isHeadless()) {
            LOG.info("cannot run lastDisplayedDate - headless");
            return;
        }
        final JXMonthView monthView = new JXMonthView();
        final JXFrame frame = wrapInFrame(monthView, "");
        frame.setVisible(true);
        frame.setSize(frame.getWidth() * 3, frame.getHeight() * 2);
        // force a revalidate
        frame.invalidate();
        frame.validate();
        SwingUtilities.invokeAndWait(new Runnable() {
            public void run() {
                LocalDateTime uiLast = monthView.getUI().getLastDisplayedDay();
                LocalDateTime viewLast = monthView.getLastDisplayedDay();
                assertEquals(uiLast, viewLast);
            }
        });
    }
    

    /**
     * 
     * Issue #659-swingx: lastDisplayedDate must be synched.
     * 
     * test that ensureDateVisible works as doc'ed if multiple months shown: 
     * if the new date is in the
     * month following the last visible then the first must be set in a manner that
     * the date must be visible in the last month. 
     * 
     * @throws InvocationTargetException 
     * @throws InterruptedException 
     */
    @Test
    public void testLastDisplayedDateSizeChangedEnsureVisible() throws InterruptedException, InvocationTargetException {
        if (GraphicsEnvironment.isHeadless()) {
            LOG.info("cannot run lastDisplayedDate - headless");
            return;
        }
        final JXMonthView monthView = new JXMonthView();
        final JXFrame frame = wrapInFrame(monthView, "");
        frame.setVisible(true);
        frame.setSize(frame.getWidth() * 3, frame.getHeight() * 2);
        // force a revalidate
        frame.invalidate();
        frame.validate();
        SwingUtilities.invokeAndWait(new Runnable() {
            public void run() {
                LocalDateTime first = monthView.getFirstDisplayedDay();
                LocalDateTime uiLast = monthView.getUI().getLastDisplayedDay();
                // sanity: more than one month shown
                assertNotSame(first.getMonthOfYear(), uiLast.getMonthOfYear());
                // first day of next month 
                LocalDateTime nextAfterLast = uiLast.plusDays(1);
                // sanity
                assertNotSame(uiLast.getMonthOfYear(), nextAfterLast.getMonthOfYear());
                monthView.ensureDateVisible(nextAfterLast);
//                LocalDateTime endOfMonthOfNextAfterLast = DateTimeUtils.endOfMonth(nextAfterLast);
                LocalDateTime newUILast = monthView.getUI().getLastDisplayedDay();
                assertEquals(newUILast, monthView.getLastDisplayedDay());
            }
        });
    }
    


//    @Override
//    protected void setUp() throws Exception {
//        calendar = Calendar.getInstance();
//    }

  
}
