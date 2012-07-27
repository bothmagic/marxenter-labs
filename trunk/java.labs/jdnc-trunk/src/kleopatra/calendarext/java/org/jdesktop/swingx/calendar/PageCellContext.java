/*
 * $Id: CalendarCellContext.java 3286 2009-03-10 12:13:43Z kleopatra $
 *
 * Copyright 2007 Sun Microsystems, Inc., 4150 Network Circle,
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
 */
package org.jdesktop.swingx.calendar;

import java.awt.Color;
import java.awt.Font;
import java.util.Calendar;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.Border;

import org.jdesktop.swingx.JXCalendar;
import org.jdesktop.swingx.border.IconBorder;
import org.jdesktop.swingx.plaf.UIManagerExt;
import org.jdesktop.swingx.renderer.CellContext;

/**
 * JXCalendar specific CellContext. This is used by CalendarRenderer to configure
 * the visual default properties of the cell rendering components.<p>
 * 
 * PENDING JW: MonthView/Calendar is the first component which has per-state
 * Font, so should change the base CellContext to return the font as well.
 * 
 * @author Jeanette Winzenburg
 */
public class PageCellContext extends CellContext {

    /**
     * The padding for month traversal icons.
     * PENDING JW: decouple rendering and hit-detection. As is, these are 
     * hard-coded "magic numbers" which must be the same in both 
     * the ui-delegate (which does the hit-detection) and here (which
     * returns the default title border)
     * 
     * Added as preliminary fix for #1028-swingx: title border incorrect if box-padding 0
     */
    private int arrowPaddingX = 3;
    private int arrowPaddingY = 3;

    private CalendarCellState dayState;

    public void installContext(JXCalendar component, Calendar value,
            boolean selected, boolean focused, CalendarCellState dayState) {
        this.component = component;
        this.dayState = dayState;
        installState(value, -1, -1, selected, focused, true, true);
    }

    
    @Override
    public JXCalendar getComponent() {
        return (JXCalendar) super.getComponent();
    }


    public CalendarCellState getCalendarState() {
        return dayState;
    }
     

    /** 
     * @inherited <p>
     */
    @Override
    public Calendar getValue() {
        return (Calendar) super.getValue();
    }


    
    /** 
     * @inherited <p>
     */
    @Override
    protected Font getFont() {
        Font font = super.getFont();
        if (isTitleState()) {
            font = getDerivedFont(font);
        }
        return font;
    }


    private boolean isTitleState() {
        return (CalendarCellState.DAY_OF_WEEK_TITLE == getCalendarState() || 
                CalendarCellState.PAGE_TITLE == getCalendarState());
    }


    @Override
    protected Color getBackground() {
        Color background = super.getBackground();
        if (dayState.isContent() && !dayState.isOnPage()) {
            background = UIManagerExt.getColor("Label.disabledBackground");
        } 
//        else if (isFocused()) {
//            background = getSelectionBackground();
//        }
        return background;
    }

    
    /** 
     * @inherited <p>
     */
    @Override
    protected Color getForeground() {
        Color background = super.getForeground();
        if ((dayState.isContent() && !dayState.isOnPage()) ) {
            background = UIManagerExt.getColor("Label.disabledForeground");
        } 
//        else if (isFocused()) {
//            background = getSelectionForeground();
//        }
        return background;
    }


    @Override
    protected Color getSelectionBackground() {
//        if (CalendarCellState.DAY_OFF_CELL == dayState) 
//            return getBackground();
        return getComponent() != null ? getComponent().getSelectionBackground() : null;
    }

    @Override
    protected Color getSelectionForeground() {
//        if (CalendarCellState.DAY_OFF_CELL == dayState) 
//            return getBackground();
        return getComponent() != null ? getComponent().getSelectionForeground() : null;
    }

    
    
    @Override
    protected Border getBorder() {
        if (getComponent() == null) {
            return super.getBorder();
        }
        if (CalendarCellState.PAGE_TITLE == dayState) {
            return getTitleBorder();
        }
        if (isFocused()) {
            int x = getComponent().getBoxPaddingX();
            int y = getComponent().getBoxPaddingY();
            Border todayBorder = BorderFactory.createLineBorder(getComponent().getTodayBackground());
            Border empty = BorderFactory.createEmptyBorder(y - 1, x - 1, y - 1, x -1);
            return BorderFactory.createCompoundBorder(todayBorder, empty);
        }
        if (isToday()) {
            int x = getComponent().getBoxPaddingX();
            int y = getComponent().getBoxPaddingY();
           Border todayBorder = BorderFactory.createLineBorder(getComponent().getTodayBackground());
           Border empty = BorderFactory.createEmptyBorder(y - 1, x - 1, y - 1, x -1);
           return BorderFactory.createCompoundBorder(todayBorder, empty);
        }
        return BorderFactory.createEmptyBorder(getComponent().getBoxPaddingY(), getComponent().getBoxPaddingX(), getComponent().getBoxPaddingY(), getComponent().getBoxPaddingX());
    }

    /**
     * Note: this is temporarily used to measure the header's border during layout. 
     * Must be done in the calendar header itself.
     */
    private Border getTitleBorder() {
        Icon downIcon = UIManager.getIcon("JXMonthView.monthDownFileName");
        Icon upIcon = UIManager.getIcon("JXMonthView.monthUpFileName");

        // fix for #1028-swingx: title border whacky for boxpadding 0
        // in fact there had been a deeper issue - without using the
        // arrowPadding here
        // the hit-detection of the buttons is slightly off target
        IconBorder up = new IconBorder(upIcon, SwingConstants.EAST,
                arrowPaddingX);
        IconBorder down = new IconBorder(downIcon, SwingConstants.WEST,
                arrowPaddingX);
        Border compound = BorderFactory.createCompoundBorder(up, down);
        Border empty = BorderFactory.createEmptyBorder(2 * arrowPaddingY, 0,
                2 * arrowPaddingY, 0);
        return BorderFactory.createCompoundBorder(compound, empty);
    }

    /**
     * @return
     */
    protected boolean isToday() {
        return CalendarCellState.TODAY_CELL == dayState;
    }

    @Override
    protected String getUIPrefix() {
        return "JXMonthView.";
    }

    /**
     * @param font
     * @return
     */
    protected Font getDerivedFont(Font font) {
        return font != null ? font.deriveFont(Font.BOLD) : font;
    }


    /**
     * @param key
     * @return
     */
    private Color getUIColor(String key) {
        return UIManagerExt.getColor(getUIPrefix() + key);
    }

    
}
