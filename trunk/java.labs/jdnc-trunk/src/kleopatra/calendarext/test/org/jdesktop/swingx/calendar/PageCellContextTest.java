/*
 * Created on 12.02.2010
 *
 */
package org.jdesktop.swingx.calendar;

import java.util.Calendar;
import java.util.logging.Logger;

import org.jdesktop.swingx.InteractiveTestCase;
import org.jdesktop.swingx.JXCalendar;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class PageCellContextTest extends InteractiveTestCase {

    @SuppressWarnings("unused")
    private static final Logger LOG = Logger
            .getLogger(PageCellContextTest.class.getName());
    
    private PageCellContext context;
    private JXCalendar calendarView;

    /**
     * 
     */
    @Test
    public void testFont() {
        context.installContext(calendarView, Calendar.getInstance(), 
                false, false, CalendarCellState.DAY_CELL);
        assertEquals("sanity, normal day", calendarView.getFont(), context.getFont());
        if (calendarView.getFont().equals(context.getDerivedFont(calendarView.getFont()))) {
            LOG.fine("cannot run - LAF font is the same for base and derived");
            return;
        }
        context.installContext(calendarView, Calendar.getInstance(), 
                false, false, CalendarCellState.PAGE_TITLE);
        assertEquals("derived for title", context.getDerivedFont(calendarView.getFont()), 
                context.getFont());
        context.installContext(calendarView, Calendar.getInstance(), 
                false, false, CalendarCellState.DAY_OF_WEEK_TITLE);
        assertEquals("derived for day-of-week", context.getDerivedFont(calendarView.getFont()), 
                context.getFont());
        
    }

    /** 
     * @inherited <p>
     */
    @Override
    @Before
    public void setUp() throws Exception {
        context = new PageCellContext();
        calendarView = new JXCalendar();
    }
    
    @BeforeClass
    public static void setLAF() {
        try {
            // try set a LAF with derived != normal font in calendar
            setLookAndFeel("Windows");
        } catch (Exception e) {
            // TODO: handle exception
        }
    }
}
