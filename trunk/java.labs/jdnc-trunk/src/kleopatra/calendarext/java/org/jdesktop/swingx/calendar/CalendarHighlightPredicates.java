/*
 * Created on 23.02.2010
 *
 */
package org.jdesktop.swingx.calendar;

import java.awt.Component;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.jdesktop.swingx.decorator.ComponentAdapter;
import org.jdesktop.swingx.decorator.HighlightPredicate;

/**
 * HighlightPredicates useful for Calendar.
 */
public class CalendarHighlightPredicates {

    public static final HighlightPredicate IS_MONTH_PAGE = new HighlightPredicate() {
        
        @Override
        public boolean isHighlighted(Component renderer,
                ComponentAdapter adapter) {
            if (!(adapter instanceof PageAdapter)) return false;
            PageAdapter pageAdapter = (PageAdapter) adapter;
            CalendarCellState cellState = pageAdapter.getCellState();
            return (FieldType.MONTH == cellState.getPageType());
        }
        
    };
    
    
    public static final HighlightPredicate IS_CONTENT_IN_MONTH_PAGE = new HighlightPredicate() {

        @Override
        public boolean isHighlighted(Component renderer,
                ComponentAdapter adapter) {
            if (!(adapter instanceof PageAdapter) || 
                    !IS_MONTH_PAGE.isHighlighted(renderer, adapter)) return false;
            return ((PageAdapter) adapter).getCellState().isContent();
        }
        
    };

  

    public static final HighlightPredicate IS_DAY_ON_PAGE = new HighlightPredicate() {

        @Override
        public boolean isHighlighted(Component renderer,
                ComponentAdapter adapter) {
            if (!(adapter instanceof PageAdapter) || 
                    !IS_MONTH_PAGE.isHighlighted(renderer, adapter)) return false;
            return ((PageAdapter) adapter).getCellState().isOnPage();
        }
        
    };

    public static final HighlightPredicate IS_UNSELECTABLE = new HighlightPredicate() {

        public boolean isHighlighted(Component renderer,
                ComponentAdapter adapter) {
            if (!(adapter instanceof PageAdapter)) 
                return false;
            return ((PageAdapter) adapter).isUnselectable();
        }
        
    };

    public static class DayOfWeekHighlightPredicate implements HighlightPredicate {
        
        private List<Integer> days;
        
        public DayOfWeekHighlightPredicate(int... dayOfWeek) {
            days = new ArrayList<Integer>();
            for (int i : dayOfWeek) {
                days.add(i);
            }
        }
        
        @Override
        public boolean isHighlighted(Component renderer,
                ComponentAdapter adapter) {
            if (!(adapter instanceof PageAdapter) || 
                    !IS_DAY_ON_PAGE.isHighlighted(renderer, adapter)) return false;
            Calendar calendar = (Calendar) adapter.getValue();
            return days.contains(calendar.get(Calendar.DAY_OF_WEEK));
        }
        
    }
    
    


    private CalendarHighlightPredicates() {};
}
