/*
 * Created on 01.04.2009
 *
 */
package org.jdesktop.swingx.plaf.basic;

import org.jdesktop.swingx.JXMonthView;
import org.jdesktop.swingx.decorator.Highlighter;
import org.jdesktop.swingx.decorator.PainterHighlighter;
import org.jdesktop.swingx.renderer.ComponentProvider;
import org.jdesktop.swingx.renderer.StringValue;
import org.jdesktop.swingx.renderer.StringValues;

public class JWMonthView extends JXMonthView {
    
    public void setStringValue(CalendarState calendarState, StringValue sv) {
        if (getUI() instanceof BasicWMonthViewUI)
            ((BasicWMonthViewUI) getUI()).getRenderingHandler().setStringValue(calendarState, sv);
    }
    
    public void setComponentProvider(CalendarState calendarState, ComponentProvider<?> provider) {
        if (getUI() instanceof BasicWMonthViewUI)
            ((BasicWMonthViewUI) getUI()).getRenderingHandler().setComponentProvider(calendarState, provider);
    }
    
    public StringValue createDayStringValue() {
        if (getUI() instanceof BasicWMonthViewUI)
            return ((BasicWMonthViewUI) getUI()).getRenderingHandler().createDayStringValue(getLocale());
        return StringValues.EMPTY;
        
    }

    public void addHighlighter(Highlighter hl) {
        if (getUI() instanceof BasicWMonthViewUI)
            ((BasicWMonthViewUI) getUI()).getRenderingHandler().addHighlighter(hl);
        
    }
}
