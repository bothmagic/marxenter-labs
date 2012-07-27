/*
 * $Id$
 *
 * Copyright 2009 Sun Microsystems, Inc., 4150 Network Circle,
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

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.jdesktop.swingx.calendar.CalendarUtils;
import org.jdesktop.swingx.calendar.FieldType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Unit tests of CalendarNavigator.
 * 
 * @author Jeanette Winzenburg
 */
@RunWith(JUnit4.class)
public class ZoomableNavigatorTest extends AbstractTestNavigator {
    
    @Test
    public void testCalendarSetCommitsMemory() {
        TimeZone tz = TimeZone.getTimeZone("GMT+4");
        if (Calendar.getInstance().getTimeZone().equals(tz)) {
            tz = TimeZone.getTimeZone("GMT+5");
        }
        Calendar custom = Calendar.getInstance(tz);
        getNavigator().nextCell();
        getNavigator().commitLead();
        getNavigator().setCalendar(custom);
        assertEquals(custom.getTime(), getNavigator().getLeadDate());
        assertEquals(custom.getTime(), getNavigator().getMemoryDate());
    }
    
    @Test
    public void testCalendarResetsZoomLevel() {
        TimeZone tz = TimeZone.getTimeZone("GMT+4");
        if (Calendar.getInstance().getTimeZone().equals(tz)) {
            tz = TimeZone.getTimeZone("GMT+5");
        }
        Calendar custom = Calendar.getInstance(tz);
        getNavigator().zoomOut();
        getNavigator().commitLead();
        getNavigator().setCalendar(custom);
        assertEquals(custom.getTime(), getNavigator().getLeadDate());
        assertEquals(custom.getTime(), getNavigator().getMemoryDate());
        assertSame(FieldType.MONTH, getPageType());
    }
    
    @Test
    public void testCustomCalendarConstructor() {
        TimeZone tz = TimeZone.getTimeZone("GMT+4");
        if (Calendar.getInstance().getTimeZone().equals(tz)) {
            tz = TimeZone.getTimeZone("GMT+5");
        }
        Calendar custom = Calendar.getInstance(tz);
        ZoomableNavigator navi = new ZoomableNavigator(custom);
        assertTimeZones(navi, tz);
    }
    
    @Test
    public void testCustomCalendar() {
        TimeZone tz = TimeZone.getTimeZone("GMT+4");
        if (Calendar.getInstance().getTimeZone().equals(tz)) {
            tz = TimeZone.getTimeZone("GMT+5");
        }
        Calendar custom = Calendar.getInstance(tz);
        getNavigator().setCalendar(custom);
        assertTimeZones(getNavigator(), tz);
    }

    
    /**
     * @param navi
     * @param tz
     */
    private void assertTimeZones(ZoomableNavigator navi, TimeZone tz) {
        for (int i = 0; i < navi.getMaxZoomLevel(); i++) {
            assertEquals(tz, navi.getLead().getTimeZone());
            navi.zoomOut();
        }
    }
    
    @Test
    public void testCalendarDefault() {
        assertEquals(Calendar.getInstance(), getNavigator().getLead());
    }
    
    @Test
    public void testCancelZoomsIn() {
        Date memory = getNavigator().getMemoryDate();
        Calendar page = getNavigator().getPage();
        FieldType type = getNavigator().getPageType();
        getNavigator().zoomOut();
        getNavigator().nextCell();
        getNavigator().cancelLead();
        assertEquals(memory, getNavigator().getLeadDate());
        assertSame(type, getNavigator().getPageType());
    }
    
    @Test
    public void testZoomOutNextCellZoomInEqualsNextPage() {
        Calendar lead = getNavigator().getLead();
        lead.add(getPageCalendarField(), 1);
        getNavigator().zoomOut();
        getNavigator().nextCell();
        getNavigator().zoomIn();
        assertEquals(lead.getTime(), getNavigator().getLeadDate());
    }
    
    @Test
    public void testZoomLeadDateUnchanged() {
        getNavigator().nextCell();
        Date leadDate = getNavigator().getLeadDate();
        getNavigator().zoomOut();
        assertEquals(leadDate, getNavigator().getLeadDate());
        getNavigator().zoomIn();
        assertEquals(leadDate, getNavigator().getLeadDate());
    }

    @Test
    public void testZoomOutPageType() {
        assertPageTypeOnZoomOut();
        assertPageTypeOnZoomOut();
    }

    /**
     * 
     */
    private void assertPageTypeOnZoomOut() {
        FieldType oldPageType = getNavigator().getPageType();
        getNavigator().zoomOut();
        assertSame(oldPageType, getNavigator().getCellType());
        assertNotSame("saniy: pagetype change", oldPageType, getPageType());
    }
    
    @Test
    public void testIsZoomed() {
        assertFalse(getNavigator().isZoomed());
        getNavigator().zoomOut();
        assertTrue(getNavigator().isZoomed());
        getNavigator().zoomIn();
        assertFalse(getNavigator().isZoomed());
    }
    
    
    @Test
    public void testMemoryUnchangedInNavigation() {
        Date memory = getNavigator().getMemoryDate();
        getNavigator().nextCell();
        assertEquals(memory, getNavigator().getMemoryDate());
    }
    
    @Test
    public void testMemoryUpdatedOnCommit() {
        getNavigator().nextCell();
        getNavigator().commitLead();
        assertEquals(getNavigator().getLeadDate(), getNavigator().getMemoryDate());
    }

    @Test
    public void testMemoryRevertedOnCancel() {
        Date memory = getNavigator().getMemoryDate();
        getNavigator().nextCell();
        getNavigator().cancelLead();
        assertEquals(memory, getNavigator().getMemoryDate());
        assertEquals(memory, getNavigator().getLeadDate());
    }
    
//------------------------- setters
    
    @Test
    public void testLeadDate() {
        Calendar calendar = getNavigator().getLead();
        assertEquals("sanity", calendar.getTime(), getNavigator().getLeadDate());
        // fixed date in the past ... safe enough?
        int dayOfMonth = 15;
        calendar.set(2009, Calendar.JUNE, dayOfMonth);
        CalendarUtils.startOfDay(calendar);
        getNavigator().setLeadDate(calendar.getTime());
        assertEquals("sanity", calendar.getTime(), getNavigator().getLeadDate());
        assertEquals(dayOfMonth, getNavigator().getLeadValue());
        assertEquals(calendar.getActualMaximum(Calendar.DATE),
                getNavigator().getMaximumLeadValue());
    }

    
    private void assertDefaults(Navigator navigator) {
        assertSame(FieldType.MONTH, navigator.getPageType());
        assertSame(FieldType.DAY, navigator.getCellType());
        assertTrue(CalendarUtils.isSameDay(Calendar.getInstance(), navigator.getLeadDate()));
        assertTrue(CalendarUtils.isStartOfMonth(navigator.getPage()));
        assertEquals(7, navigator.getVerticalCellUnit());
        assertEquals(35, navigator.getVerticalPageUnit());
    }

    @Test
    public void testDefaultConstructor() {
        assertDefaults(getNavigator());
        assertEquals(3, getNavigator().getMaxZoomLevel());
    }


    
    /**
     * @return the navigator
     */
    @Override
    protected ZoomableNavigator getNavigator() {
        return (ZoomableNavigator) super.getNavigator();
    }

    @Override
    protected Navigator createNavigator() {
        return new ZoomableNavigator();
    }
    
    
}
