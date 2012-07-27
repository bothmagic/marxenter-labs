/*
 * $Id: MonthViewUI.java 3100 2008-10-14 22:33:10Z rah003 $
 *
 * Copyright 2008 Sun Microsystems, Inc., 4150 Network Circle,
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
package org.jdesktop.swingx.plaf;

import java.util.Calendar;

import javax.swing.plaf.ComponentUI;

import org.jdesktop.swingx.calendar.FieldType;
import org.jdesktop.swingx.calendar.CalendarCellState;

public abstract class CalendarUI extends ComponentUI {


    /**
     * Returns the cell date at the given location. May be null if the
     * coordinates don't map to a cell in the current page.
     * <p> 
     * Mapping pixel to date.<p>
     *
     * @param x the x position of the location in pixel
     * @param y the y position of the location in pixel
     * @return the Date at the given location or null if the location
     *   doesn't map to a cell in the current page. 
     *   
     */ 
    public abstract Calendar getCellAtLocation(int x, int y);


    /**
     * Returns the Calendar representing the Date at the logical 
     * grid coordinates. If the coordinates hit a header cell, the
     * returned Date is that of the nearest non-header cell. <p>
     * 
     * Mapping logical grid coordinates to Date.<p>
     * 
     * @param row the logical row index in grid of date cells, must be
     *   valid in the current page type
     * @param column the logical column index in the grid of date cells, must
     *    be valid in the current page type
     * 
     * @return the cell date at the logical grid coordinates
     * 
     * @throws IllegalArgumentException if row or column are invalid coordinates
     */
    public abstract Calendar getCell(int row, int column);


    /**
     * Returns the current page. The calendar is set to the start of the
     * period represented by the page.
     * 
     * @return
     */
    public abstract Calendar getPage();


    /**
     * Returns the field type of the current page.
     * 
     * @return
     */
    public abstract FieldType getPageType() ;


    /**
     * Returns the number of columns in the current page.
     * 
     * @return
     */
    public abstract int getColumnCount();


    /**
     * Returns the number of rows in the current page.
     * 
     * @return
     */
    public abstract int getRowCount();


    /**
     * Returns the lead date. 
     * 
     * @return the date of the current lead cell.
     */
    public abstract Calendar getLead();
    

    /**
     * Returns the cell state at the logical grid position.
     * 
     * @param row the logical row index of the cell
     * @param column the logical column index of the cell
     * @return the cell state at the logical grid position, maybe null (??) if ??
     */
    public abstract CalendarCellState getCellState(int row, int column);



}
