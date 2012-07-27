/*
 * Created on 04.02.2010
 *
 */
package org.jdesktop.swingx.plaf.basic;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.jdesktop.swingx.calendar.CalendarUtils;
import org.jdesktop.swingx.calendar.FieldType;


/**
 * A Navigator which adds navigation in the Z-direction.
 * 
 */
public class ZoomableNavigator implements Navigator {

    private Date memoryDate;

    private List<Navigator> navigators;

    private int zoomLevel;

    
    public ZoomableNavigator() {
        this(Calendar.getInstance());
    }

    public ZoomableNavigator(Calendar calendar) {
        navigators = new ArrayList<Navigator>();
        navigators.add(new MonthPageNavigator(calendar));
//        navigators.add(new CalendarNavigator(calendar, 
//                FieldType.MONTH, 7, 5));
        navigators.add(new CalendarNavigator(calendar,
                FieldType.YEAR, 4, 3));
        navigators.add(new DecadePageNavigator(calendar));
//        navigators.add(new CalendarNavigator(calendar,
//                FieldType.DECADE, 4, 3));
        commitLead();
        
    }
    
    
    // ------- adding zoomable and commit/revert logic

    /**
     * Returns the memoryDate.
     */
    public Date getMemoryDate() {
        return memoryDate;
    }

    /**
     * Updates the memoryDate to be the same as the lead.
     */
    public void commitLead() {
        memoryDate = getLeadDate();
    }

    /**
     * Reverts the lead to the last memory date.
     */
    public void cancelLead() {
        setLeadDate(memoryDate);
        zoomLevel = 0;
    }

    public boolean isZoomed() {
        return zoomLevel > 0;
    }

    public void zoomOut() {
        if (zoomLevel == (navigators.size() - 1))
            return;
        zoomLevel++;
    }

    public void zoomIn() {
        if (zoomLevel == 0)
            return;
        zoomLevel--;
    }

    /**
     * Testing only - DO NOT USE!
     * @return
     */
    protected int getMaxZoomLevel() {
        return navigators.size();
    }

    
///-------------------- implement Navigator
    
    /** 
     * @inherited <p>
     * 
     * 
     * Implemented to update all contained navigators' to use the given calendar. 
     * Commits itself to the date of the calendar and resets the zoom to first.
     */
    @Override
    public void setCalendar(Calendar calendar) {
        for (Navigator navigator : navigators) {
            navigator.setCalendar(calendar);
        }
        commitLead();
        zoomLevel = 0;
    }


    // ----------------- private helpers

    private void updateNavigatorsToCurrentLeadDate() {
        for (Navigator navigator : navigators) {
            if (getCurrent() == navigator)
                continue;
            navigator.setLeadDate(getLeadDate());
        }

    }

    private Navigator getCurrent() {
        return navigators.get(zoomLevel);
    }

    // ---------------- getters/setters

    /**
     * {@inheritDoc}
     * <p>
     */
    @Override
    public Calendar getLead() {
        return getCurrent().getLead();
    }

    /**
     * {@inheritDoc}
     * <p>
     * 
     * Implemented to delegate to the navigator at the current zoom level.
     */
    @Override
    public Date getLeadDate() {
        return getCurrent().getLeadDate();
    }

    /**
     * {@inheritDoc}
     * <p>
     * 
     * Implemented to delegate to the navigator at the current zoom level.
     */
    @Override
    public int getLeadValue() {
        return getCurrent().getLeadValue();
    }

    /**
     * {@inheritDoc}
     * <p>
     * 
     * Implemented to delegate to the navigator at the current zoom level.
     */
    @Override
    public void setLeadDate(Date date) {
        getCurrent().setLeadDate(date);
        updateNavigatorsToCurrentLeadDate();
    }

    /**
     * {@inheritDoc}
     * <p>
     * 
     * Implemented to delegate to the navigator at the current zoom level.
     */
    @Override
    public void setLeadValue(int leadValue) {
        getCurrent().setLeadValue(leadValue);
        updateNavigatorsToCurrentLeadDate();
    }

    /**
     * {@inheritDoc}
     * <p>
     * 
     * Implemented to delegate to the navigator at the current zoom level.
     */
    @Override
    public int getMaximumLeadValue() {
        return getCurrent().getMaximumLeadValue();
    }

    /**
     * {@inheritDoc}
     * <p>
     * 
     * Implemented to delegate to the navigator at the current zoom level.
     */
    @Override
    public int getMinimumLeadValue() {
        return getCurrent().getMinimumLeadValue();
    }

    /**
     * {@inheritDoc}
     * <p>
     * 
     * Implemented to delegate to the navigator at the current zoom level.
     */
    @Override
    public int getVerticalCellUnit() {
        return getCurrent().getVerticalCellUnit();
    }

    /**
     * {@inheritDoc}
     * <p>
     * 
     * Implemented to delegate to the navigator at the current zoom level.
     */
    @Override
    public int getVerticalPageUnit() {
        return getCurrent().getVerticalPageUnit();
    }

    /**
     * {@inheritDoc}
     * <p>
     * 
     * Implemented to delegate to the navigator at the current zoom level.
     */
    @Override
    public Calendar getPage() {
        return getCurrent().getPage();
    }

    
    
    /** 
     * @inherited <p>
     * Implemented to delegate to the navigator at the current zoom level.
     */
    @Override
    public Calendar getFirstCell() {
         return getCurrent().getFirstCell();
    }

    /**
     * {@inheritDoc}
     * <p>
     * 
     * Implemented to delegate to the navigator at the current zoom level.
     */
    @Override
    public FieldType getCellType() {
        return getCurrent().getCellType();
    }

    /**
     * {@inheritDoc}
     * <p>
     * 
     * Implemented to delegate to the navigator at the current zoom level.
     */
    @Override
    public FieldType getPageType() {
        return getCurrent().getPageType();
    }

    // ----------------- Navigation

    /**
     * {@inheritDoc}
     * <p>
     * 
     * Implemented to delegate to the navigator at the current zoom level.
     */
    @Override
    public void lowerCell() {
        getCurrent().lowerCell();
        updateNavigatorsToCurrentLeadDate();
    }

    /**
     * {@inheritDoc}
     * <p>
     * 
     * Implemented to delegate to the navigator at the current zoom level.
     */
    @Override
    public void lowerPage() {
        getCurrent().lowerPage();
        updateNavigatorsToCurrentLeadDate();
    }

    /**
     * {@inheritDoc}
     * <p>
     * 
     * Implemented to delegate to the navigator at the current zoom level.
     */
    @Override
    public void nextCell() {
        getCurrent().nextCell();
        updateNavigatorsToCurrentLeadDate();
    }

    /**
     * {@inheritDoc}
     * <p>
     * 
     * Implemented to delegate to the navigator at the current zoom level.
     */
    @Override
    public void nextPage() {
        getCurrent().nextPage();
        updateNavigatorsToCurrentLeadDate();
    }

    /**
     * {@inheritDoc}
     * <p>
     * 
     * Implemented to delegate to the navigator at the current zoom level.
     */
    @Override
    public void previousCell() {
        getCurrent().previousCell();
        updateNavigatorsToCurrentLeadDate();
    }

    /**
     * {@inheritDoc}
     * <p>
     * 
     * Implemented to delegate to the navigator at the current zoom level.
     */
    @Override
    public void previousPage() {
        getCurrent().previousPage();
        updateNavigatorsToCurrentLeadDate();
    }

    /**
     * {@inheritDoc}
     * <p>
     * 
     * Implemented to delegate to the navigator at the current zoom level.
     */
    @Override
    public void upperCell() {
        getCurrent().upperCell();
        updateNavigatorsToCurrentLeadDate();
    }

    /**
     * {@inheritDoc}
     * <p>
     * 
     * Implemented to delegate to the navigator at the current zoom level.
     */
    @Override
    public void upperPage() {
        getCurrent().upperPage();
        updateNavigatorsToCurrentLeadDate();
    }

//------------------- specialized navigators
// -----FIXME: move up? make CalendarNavigator abstract? re-visit verticalPageUnit?
    
    public static class MonthPageNavigator extends CalendarNavigator {
        
        public MonthPageNavigator(Calendar calendar) {
            super(calendar, FieldType.MONTH, 7, 5);
        }

        /** 
         * @inherited <p>
         * 
         * Overridden to not "step over" a month. <p>
         * 
         * PENDING JW: still not good enough - we want to keep as near to 
         * the original week as possible.
         */
        @Override
        public void lowerPage() {
            Calendar page = getPage();
            // expect to land in next page
            getPageType().add(page, getPageType().getUnitIncrement());
            super.lowerPage();
            if (!getPageType().isContainedInPage(page, getLeadDate())) {
                // overshooting - revert one
                upperCell();
            }
        }

        /** 
         * @inherited <p>
         * 
         * Overridden to not "step over" a month. <p>
         * 
         * PENDING JW: still not good enough - we want to keep as near to 
         * the original week as possible.
         */
        @Override
        public void upperPage() {
            Calendar page = getPage();
            // expect to land in previous page
            getPageType().add(page, - getPageType().getUnitIncrement());
            super.upperPage();
            if (!getPageType().isContainedInPage(page, getLeadDate())) {
                // overshooting - revert one
                lowerCell();
            }
        }
        
    }
    
    public static class DecadePageNavigator extends CalendarNavigator {
        
        public DecadePageNavigator(Calendar calendar) {
            super(calendar, FieldType.DECADE, 4, 3);
        }

        /** 
         * @inherited <p>
         */
        @Override
        public void lowerPage() {
            nextPage();
         }

        /** 
         * @inherited <p>
         */
        @Override
        public void upperPage() {
            previousPage();
        }

        /** 
         * @inherited <p>
         */
        @Override
        public void lowerCell() {
            Calendar page = getPage();
            int yearInDecade = getLeadValue();
            super.lowerCell();
            if (!getPageType().isContainedInPage(page, getLeadDate())) {
                if ((yearInDecade >= 6)) {
                    if (yearInDecade < 8) {
                        yearInDecade -= 4;
                    } else  {
                        yearInDecade -= 8;
                    }
                    CalendarUtils.set(calendar, getPageType().getCellCalendarField(), yearInDecade);
                }
            }
        }

        /** 
         * @inherited <p>
         */
        @Override
        public void upperCell() {
            Calendar page = getPage();
            int yearInDecade = getLeadValue();
            super.upperCell();
            if (!getPageType().isContainedInPage(page, getLeadDate())) {
                if (yearInDecade <= 3) {
                    if (yearInDecade < 2) {
                        yearInDecade += 8;
                    } else {
                        yearInDecade += 4;
                    }
                    CalendarUtils.set(calendar, getPageType().getCellCalendarField(), yearInDecade);
                }
            }
        }
        
        
    }
    
}
