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

import java.util.Calendar;

import org.jdesktop.swingx.calendar.CalendarUtils;
import org.jdesktop.swingx.calendar.FieldType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Unit tests of CalendarNavigator in its default configuration, that is with
 * FieldType.MONTH as page.
 * 
 * @author Jeanette Winzenburg
 */
@RunWith(JUnit4.class)
public class CalendarNavigatorDefaultTest extends AbstractTestNavigator {

    @Test
    public void testFirstCell() {
        Calendar page = getPage();
        // month with leading days: first is Thursday
        page.set(2010, Calendar.JULY, 2);
        getNavigator().setLeadDate(page.getTime());
        CalendarUtils.startOfWeek(page);
        Calendar calendar = getNavigator().getFirstCell();
        assertTrue("first cell must be before page but was: " 
                + calendar.getTime() + " / " + getPage().getTime(),
                 calendar.before(getPage()));
        assertEquals("first cell must be start of week", page.getTime(), calendar.getTime());
    }
    
    @Test
    public void testLeadDate() {
        Calendar calendar = getNavigator().getLead();
        assertEquals("sanity", calendar.getTime(), getNavigator().getLeadDate());
        // fixed date in the past ... safe enough?
        int dayOfMonth = 15;
        calendar.set(2009, Calendar.JUNE, dayOfMonth);
        CalendarUtils.startOf(calendar, getCellCalendarField());
        getNavigator().setLeadDate(calendar.getTime());
        assertEquals("sanity", calendar.getTime(), getNavigator().getLeadDate());
        assertEquals(dayOfMonth, getNavigator().getLeadValue());
        assertEquals(calendar.getActualMaximum(getCellCalendarField()),
                getNavigator().getMaximumLeadValue());
    }

    @Test
    public void testUpperCellWeekDay() {
        getNavigator().setLeadValue(getNavigator().getMaximumLeadValue());
        Calendar lead = getNavigator().getLead();
        Calendar page = getNavigator().getPage();
        int dayOfWeek = lead.get(Calendar.DAY_OF_WEEK);
        lead.add(getCellCalendarField(), -7);
        assertEquals("sanity: same weekday", dayOfWeek, lead.get(Calendar.DAY_OF_WEEK));
        getNavigator().upperCell();
        assertEquals(lead.get(getCellCalendarField()), getNavigator().getLeadValue());
        assertEquals(page, getNavigator().getPage());
        assertEquals(dayOfWeek, getNavigator().getLead().get(Calendar.DAY_OF_WEEK));
    }
    
    
    @Test
    public void testLowerCellWeekDay() {
        getNavigator().setLeadValue(getNavigator().getMinimumLeadValue());
        assertEquals("sanity: ", 1, getNavigator().getLeadValue());
        Calendar lead = getNavigator().getLead();
        Calendar page = getNavigator().getPage();
        int dayOfWeek = lead.get(Calendar.DAY_OF_WEEK);
        lead.add(getCellCalendarField(), 7);
        assertEquals("sanity: same weekday", dayOfWeek, lead.get(Calendar.DAY_OF_WEEK));
        getNavigator().lowerCell();
        assertEquals(lead.get(getCellCalendarField()), getNavigator().getLeadValue());
        assertEquals(page, getNavigator().getPage());
        assertEquals(dayOfWeek, getNavigator().getLead().get(Calendar.DAY_OF_WEEK));
    }
    
    @Test
    public void testUpperPageWeekDay() {
        getNavigator().setLeadValue(getNavigator().getMaximumLeadValue());
        Calendar lead = getNavigator().getLead();
        Calendar page = getNavigator().getPage();
        page.add(getPageCalendarField(), -1);
        int dayOfWeek = lead.get(Calendar.DAY_OF_WEEK);
        lead.add(getCellCalendarField(), -(7 * 5));
        
        assertEquals("sanity: same weekday", dayOfWeek, lead.get(Calendar.DAY_OF_WEEK));
        getNavigator().upperPage();
        assertEquals(lead.get(getCellCalendarField()), getNavigator().getLeadValue());
        assertEquals(page, getNavigator().getPage());
        assertEquals(dayOfWeek, getNavigator().getLead().get(Calendar.DAY_OF_WEEK));
    }

    
    @Test
    public void testLowerPageWeekDay() {
        getNavigator().setLeadValue(getNavigator().getMinimumLeadValue());
        assertEquals("sanity: ", 1, getNavigator().getLeadValue());
        Calendar lead = getNavigator().getLead();
        Calendar page = getNavigator().getPage();
        page.add(getPageCalendarField(), 1);
        int dayOfWeek = lead.get(Calendar.DAY_OF_WEEK);
        lead.add(getCellCalendarField(), 7 * 5);
        assertEquals("sanity: same weekday", dayOfWeek, lead.get(Calendar.DAY_OF_WEEK));
        getNavigator().lowerPage();
        assertEquals(lead.get(getCellCalendarField()), getNavigator().getLeadValue());
        assertEquals(page, getNavigator().getPage());
        assertEquals(dayOfWeek, getNavigator().getLead().get(Calendar.DAY_OF_WEEK));
    }
    

    @Test
    public void testParamConstructor() {
        CalendarNavigator navigator = new CalendarNavigator(Calendar.getInstance(), 
                FieldType.MONTH, 7, 5);
        assertDefaults(navigator);
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
    }
    
    @Override
    protected CalendarNavigator createNavigator() {
        return new CalendarNavigator();
    }
    
}
