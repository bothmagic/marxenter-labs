/*
 * Created on 22.02.2010
 *
 */
package org.jdesktop.swingx.calendar;

import java.awt.Color;
import java.beans.PropertyChangeListener;
import java.util.Calendar;
import java.util.Date;

import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.UIManager;
import javax.swing.plaf.UIResource;

import org.jdesktop.swingx.InteractiveTestCase;
import org.jdesktop.swingx.JXCalendar;
import org.jdesktop.swingx.decorator.ColorHighlighter;
import org.jdesktop.swingx.decorator.Highlighter;
import org.jdesktop.swingx.decorator.PainterHighlighter;
import org.jdesktop.swingx.decorator.AbstractTestHighlighterClient.HighlighterClient;
import org.jdesktop.swingx.plaf.UIManagerExt;
import org.jdesktop.swingx.renderer.StringValue;
import org.jdesktop.swingx.renderer.StringValues;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class RendererHandlerTest extends InteractiveTestCase {
  
    // pre-defined reference dates - all relative to current date at around 5 am
    private Date today;
    private Date tomorrow;
    private Date afterTomorrow;
    private Date yesterday;
    private Date midJune;
    // calendar default instance init with today
    private Calendar calendar;


    private JXCalendar calendarView;
    private RendererHandler controller;

    /**
     * Issue ??- : internal unselectable highlighter must be kept on setting 
     * client highlighters.
     */
    @Test
    public void testKeepsInternalHighlighter() {
        int count = controller.getHighlighters().length;
        assertEquals(1, count);
       controller.setHighlighters(new ColorHighlighter());
       assertEquals(count +1, controller.getHighlighters().length);
    }
    
    @Test
    public void testUpdateUnselectableColorUpdateUI() {
        Color color = UIManager.getColor("JXCalendar.unselectableDayForeground");
        assertTrue(color instanceof UIResource);
        try {
            // force init of internal highlighter (created lazily)
            controller.getHighlighters();
            Color custom = Color.BLUE;
            UIManager.put("JXCalendar.unselectableDayForeground", custom);
            controller.updateUI();
            assertEquals(1, controller.getHighlighters().length);
            Highlighter hl = controller.getHighlighters()[0];
            TextCrossingPainter p = (TextCrossingPainter) ((PainterHighlighter) hl).getPainter(); 
            assertEquals(custom, p.getForeground());
        } finally {
            UIManager.put("JXCalendar.unselectableDayForeground", color);
        }
    }
    
    @Test
    public void testCustomStringValue() {
        StringValue old = controller.getStringValue(CalendarCellState.DAY_OF_WEEK_TITLE);       
        controller.setStringValue(StringValues.TO_STRING, CalendarCellState.DAY_OF_WEEK_TITLE);
        StringValue sv = new StringValue() {
            
            @Override
            public String getString(Object value) {
                return "A";
            }
            
        };
        
        controller.setStringValue(sv, CalendarCellState.DAY_OF_WEEK_TITLE);
        assertEquals("custom converter registered", 
                sv, controller.getStringValue(CalendarCellState.DAY_OF_WEEK_TITLE));
        JLabel comp = (JLabel) controller.prepareRendererComponent(null, 0, 1);
        assertEquals("custom converter used", "A", comp.getText());
        controller.setStringValue(null, CalendarCellState.DAY_OF_WEEK_TITLE);
        comp = (JLabel) controller.prepareRendererComponent(null, 0, 1);
        assertEquals("fallback to default on null", 
                old.getString(calendarView.getUI().getCell(0, 1)), comp.getText());
    }
    
    @Test(expected = NullPointerException.class)
    public void testCustomStringValueNullState() {
        controller.setStringValue(StringValues.TO_STRING, null);
    }
    
    @Test
    public void testGetRenderingComponentByContext() {
        assertNotNull(controller.prepareRendererComponent(calendar, CalendarCellState.DAY_CELL));
    }
    
    @Test
    public void testGetRenderingComponentByCoordinates() {
        JComponent comp = controller.prepareRendererComponent(null, 2, 2);
        assertNotNull(comp);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetRenderingComponentByCoordinatesThrow() {
        controller.prepareRendererComponent(null, -1, -1);
    }
    
    
    @Test
    public void testDefault() {
        for (CalendarCellState state : CalendarCellState.values()) {
            assertNotNull("must have stringValue for " + state, controller.getStringValue(state));
        }
    }

    
    private boolean performAction(Object id) {
        Action action = calendarView.getActionMap().get(id);
        action.actionPerformed(null);
        return true;
    }
    
    
    /** 
     * @inherited <p>
     */
    @Override
    @Before
    public void setUp() throws Exception {
        calendarView = new JXCalendar();
        controller = new RendererHandler(calendarView);
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

//-------------------------- outdated, moved to JXCalender
//-------------------------- KEEP until decision is final
    
    protected HighlighterClient createHighlighterClient() {
        // TODO Auto-generated method stub
        return createHighlighterClient(controller);
    }
    
    private HighlighterClient createHighlighterClient(final RendererHandler table) {
        HighlighterClient client = new HighlighterClient() {

            public void addHighlighter(Highlighter highlighter) {
                table.addHighlighter(highlighter);
            }

            public void addPropertyChangeListener(PropertyChangeListener l) {
//                table.addPropertyChangeListener(l);
            }

            public Highlighter[] getHighlighters() {
                return table.getHighlighters();
            }

            public void removeHighlighter(Highlighter highlighter) {
                table.removeHighlighter(highlighter);
            }

            public void removePropertyChangeListener(PropertyChangeListener l) {
//                table.removePropertyChangeListener(l);
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
