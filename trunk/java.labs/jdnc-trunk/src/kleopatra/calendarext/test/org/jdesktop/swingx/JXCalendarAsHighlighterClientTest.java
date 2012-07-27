/*
 * Created on 24.02.2010
 *
 */
package org.jdesktop.swingx;

import java.beans.PropertyChangeListener;
import java.util.Calendar;
import java.util.Date;

import org.jdesktop.swingx.decorator.AbstractTestHighlighterClient;
import org.jdesktop.swingx.decorator.Highlighter;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class JXCalendarAsHighlighterClientTest extends
        AbstractTestHighlighterClient {

    private JXCalendar calendarView;
    private Calendar calendar;
    private Date today;
    private Date yesterday;
    private Date tomorrow;
    private Date afterTomorrow;
    private Date midJune;

    
    
    /** 
     * @inherited <p>
     * 
     * Overridden to do nothing. Super assumption is invalid.
     * PENDING JW: change implementation of rendererHandler to hide internal
     * Highlighter (if any) from application code?
     */
    @Override 
    public void testSetHighlighters() {
    }

    @Override
    protected HighlighterClient createHighlighterClient() {
        // TODO Auto-generated method stub
        return createHighlighterClient(calendarView);
    }

    /** 
     * @inherited <p>
     */
    @Override
    @Before
    public void setUp() throws Exception {
        calendarView = new JXCalendar();
        calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 5);
        today = calendar.getTime();
        
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        yesterday = calendar.getTime();
        
        calendar.add(Calendar.DAY_OF_MONTH, 2);
        tomorrow = calendar.getTime();
        
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        afterTomorrow = calendar.getTime();
        
        calendar.set(Calendar.MONTH, Calendar.JUNE);
        calendar.set(Calendar.DATE, 15);
        midJune = calendar.getTime();
        
        calendar.setTime(today);
    }
    
    private HighlighterClient createHighlighterClient(final JXCalendar table) {
        HighlighterClient client = new HighlighterClient() {

            public void addHighlighter(Highlighter highlighter) {
                table.addHighlighter(highlighter);
            }

            public void addPropertyChangeListener(PropertyChangeListener l) {
                table.addPropertyChangeListener(l);
            }

            public Highlighter[] getHighlighters() {
                return table.getHighlighters();
            }

            public void removeHighlighter(Highlighter highlighter) {
                table.removeHighlighter(highlighter);
            }

            public void removePropertyChangeListener(PropertyChangeListener l) {
                table.removePropertyChangeListener(l);
            }

            public void setHighlighters(Highlighter... highlighters) {
                table.setHighlighters(highlighters);
            }

            public void updateUI() {
                table.updateUI();
            }
            
        };
        return client;
    }

}
