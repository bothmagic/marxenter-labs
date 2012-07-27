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

import org.jdesktop.swingx.InteractiveTestCase;
import org.jdesktop.swingx.calendar.CalendarUtils;
import org.jdesktop.swingx.calendar.FieldType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Unit tests of CalendarNavigator.
 * 
 * @author Jeanette Winzenburg
 */
@RunWith(JUnit4.class)
public abstract class AbstractTestNavigator extends InteractiveTestCase {
    
    private Navigator navigator;
    
//-------------------- page/first
    
    @Test
    public void testFirstNotAfterPage() {
        Calendar calendar = getNavigator().getFirstCell();
        assertFalse("page must not before first: " 
                + calendar.getTime() + " / " + getPage().getTime(),
                 calendar.after(getPage()));
    }
//---------------------- calendar state    
    @Test
    public void testTimeZone() {
        TimeZone tz = TimeZone.getTimeZone("GMT+4");
        if (getNavigator().getLead().getTimeZone().equals(tz)) {
            tz = TimeZone.getTimeZone("GMT+5");
        }
        Calendar custom = Calendar.getInstance(tz);
        getNavigator().setCalendar(custom);
        assertEquals(tz, getNavigator().getLead().getTimeZone());
    }
    
    @Test
    public void testLeadFlushed() {
        getNavigator().setLeadValue(getNavigator().getMinimumLeadValue());
        Calendar lead = getNavigator().getLead();
        assertTrue("must be flushed but was: ", CalendarUtils.isFlushed(lead));
    }
    
    @Test
    public void testPageFlushed() {
        getNavigator().setLeadValue(getNavigator().getMaximumLeadValue());
        Calendar lead = getNavigator().getPage();
        assertTrue("must be flushed but was: ", CalendarUtils.isFlushed(lead));
    }
    
    @Test
    public void testLeadFlushedInitially() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DATE, 15);
        assertFalse(CalendarUtils.isFlushed(calendar));
        getNavigator().setCalendar(calendar);
        Calendar lead = getNavigator().getLead();
        assertTrue("must be flushed but was: ", CalendarUtils.isFlushed(lead));
    }
    
    
//------------------------- setters
    
    /**
     * Setting the lead value to minimum must not change the page and
     * lead cell value must have the same unit as the page.
     */
    @Test
    public void testSetMinimumLeadValue() {
        Calendar oldPage = getPage();
        getNavigator().setLeadValue(getNavigator().getMinimumLeadValue());
        assertEquals(oldPage, getPage());
        assertTrue(CalendarUtils.isSame(getPage(), 
                getNavigator().getLeadDate(), getCellCalendarField()));
    }
    
    /**
     * Setting the lead value to maximum must not change the page and
     * lead cell value must be one less then the next page.
     */
    @Test
    public void testSetMaximumLeadValue() {
        Calendar oldPage = getPage();
        getNavigator().setLeadValue(getNavigator().getMaximumLeadValue());
        assertEquals("maximum lead value is on same page", oldPage, getPage());
        Date leadDate = getNavigator().getLeadDate();
        getNavigator().nextPage();
        Calendar nextPage = getPage();
        CalendarUtils.add(nextPage, getCellCalendarField(), -1);
        assertTrue(CalendarUtils.isSame(nextPage, 
                leadDate, getCellCalendarField()));
    }
    
    @Test
    public void testLeadValueRollover() {
        Calendar calendar = getPage();
        getPageType().add(calendar, 1);
        assertTrue("sanity: start of  " + calendar.getTime(), 
                CalendarUtils.isStartOf(calendar, getPageCalendarField()));
        getNavigator().setLeadValue(getNavigator().getMaximumLeadValue() + 1);
        assertEquals(getNavigator().getMinimumLeadValue(), getNavigator().getLeadValue());
        assertEquals("rolled over to next page", calendar.getTime(), 
                getPage().getTime());
    }
    
    @Test
    public void testLeadValueRollunder() {
        Calendar calendar = getPage();
        getPageType().add(calendar, -1);
        assertTrue("sanity: start of " + calendar.getTime(), 
                CalendarUtils.isStartOf(calendar, getPageCalendarField()));
        getNavigator().setLeadValue(getNavigator().getMinimumLeadValue() - 1);
        assertEquals("rolled under to previous page", calendar.getTime(), 
                getPage().getTime());
        assertEquals(getNavigator().getMaximumLeadValue(), getNavigator().getLeadValue());
    }
   
//------------------------- navigation    
    @Test
    public void testUpperCell() {
        getNavigator().setLeadValue(getNavigator().getMaximumLeadValue());
        Calendar lead = getNavigator().getLead();
        Calendar page = getPage();
        lead.add(getCellCalendarField(), -getVerticalCellUnit());
        getNavigator().upperCell();
        assertEquals(CalendarUtils.get(lead, getPageType().getCellCalendarField()),
                 getNavigator().getLeadValue());
        assertEquals(page, getPage());
    }
    
    @Test
    public void testLowerCell() {
        getNavigator().setLeadValue(getNavigator().getMinimumLeadValue());
        Calendar lead = getNavigator().getLead();
        Calendar page = getPage();
        lead.add(getCellCalendarField(), getVerticalCellUnit());
        getNavigator().lowerCell();
        assertEquals(CalendarUtils.get(lead, getPageType().getCellCalendarField()),
                getNavigator().getLeadValue());
        assertEquals(page, getPage());
    }
    
    @Test
    public void testUpperPage() {
        getNavigator().setLeadValue(getNavigator().getMaximumLeadValue());
        Calendar lead = getNavigator().getLead();
        Calendar page = getPage();
        getPageType().add(page, -1);
        lead.add(getCellCalendarField(), -getVerticalPageUnit());
        getNavigator().upperPage();
        assertEquals(CalendarUtils.get(lead, getPageType().getCellCalendarField()),
                getNavigator().getLeadValue());
        assertEquals(page, getPage());
    }

    
    @Test
    public void testLowerPage() {
        getNavigator().setLeadValue(getNavigator().getMinimumLeadValue());
        Calendar lead = getNavigator().getLead();
        Calendar page = getPage();
        getPageType().add(page, 1);
        lead.add(getCellCalendarField(), getVerticalPageUnit());
        getNavigator().lowerPage();
        assertEquals(CalendarUtils.get(lead, getPageType().getCellCalendarField()),
                getNavigator().getLeadValue());
        assertEquals(page.getTime(), getPage().getTime());
    }
    
    @Test
    public void testNextPage() {
        Calendar page = getPage();
        // manually move to next date
        getPageType().add(page, 1);
        getNavigator().nextPage();
        assertEquals(getPageType().get(page), getPageType().get(getPage()));
    }
    
    @Test
    public void testPreviousPage() {
        Calendar page = getPage();
        // manually move to next date
        getPageType().add(page, -1);
        getNavigator().previousPage();
        assertEquals(page.getTime(), getPage().getTime());
        assertEquals(getPageType().get(page), getPageType().get(getPage()));
    }

    
    @Test
    public void testNextCell() {
        Date date = getNavigator().getLeadDate();
        Calendar page = getPage();
        page.setTime(date);
        // manually move to next date
        page.add(getCellCalendarField(), 1);
        getNavigator().nextCell();
        assertTrue(CalendarUtils.isSame(page, getNavigator().getLeadDate(), getCellCalendarField()));
    }
    
    @Test
    public void testPreviousCell() {
        Date date = getNavigator().getLeadDate();
        Calendar page = getPage();
        page.setTime(date);
        // manually move to previous date
        page.add(getCellCalendarField(), -1);
        getNavigator().previousCell();
        assertTrue(CalendarUtils.isSame(page, getNavigator().getLeadDate(), getCellCalendarField()));
    }

//------------- accessors/convenience for subclasses    
    /**
     * @return
     */
    protected int getCellCalendarField() {
        return getNavigator().getCellType().getCalendarField();
    }
    
    /**
     * @return
     */
    protected int getPageCalendarField() {
        return getPageType().getCalendarField();
    }
    
    protected FieldType getPageType() {
        return getNavigator().getPageType();
    }

    /**
     * @return the navigator
     */
    protected Navigator getNavigator() {
        return navigator;
    }
    
    
    protected int getVerticalCellUnit() {
        return navigator.getVerticalCellUnit();
    }

    protected int getVerticalPageUnit() {
        return navigator.getVerticalPageUnit();
    }
 
    /**
     * @return
     */
    protected Calendar getPage() {
        return getNavigator().getPage();
    }

    
    
    @Override
    @Before
    public void setUp() throws Exception {
        navigator = createNavigator();
    }
    
    protected abstract Navigator createNavigator();
}
