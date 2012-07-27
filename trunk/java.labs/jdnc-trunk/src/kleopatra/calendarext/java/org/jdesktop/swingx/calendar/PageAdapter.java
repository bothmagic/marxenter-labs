/*
 * Created on 15.02.2010
 *
 */
package org.jdesktop.swingx.calendar;

import java.util.Calendar;
import java.util.Date;

import org.jdesktop.swingx.JXCalendar;
import org.jdesktop.swingx.decorator.ComponentAdapter;

/**
 * ComponentAdapter to work with JXCalendar. The "model" is the grid of 
 * Dates as shown in the current page. The grid, the coordinates and the values are
 * context-dependent (aka: page dependent). 
 * 
 * <ul>
 * <li>MONTH: 7 rows, 8 columns (first represent the week/weekday headers), cell unit is DAY,
 *   value is the start-of-Day for non-header cells. Leading/trailing cells may be off the
 *   current page, depending on the concrete month
 *   PENDING: header cells?
 * <li>YEAR: 3 rows, 4 columns, cell unit is MONTH, value is the start-of-Month. 
 *      no leading/trailing
 * <li>DECADE: 3 rows, 4 columns, cell unit is YEAR, value is the start-of-year, 
 *      one leading/trailing year off the current decade.
 * </ul>
 * 
 * PENDING JW: exact grid-layout should be definable by ui? Keep as general as possible.
 * PENDING JW: view coordinates == model coordinates, even if week-headers are not visible.
 */
public class PageAdapter extends ComponentAdapter {

    public PageAdapter(JXCalendar component) {
        super(component);
    }
    
    /**
     * Returns this adapter with the view coordinates set to the given row and 
     * column.
     * 
     * @param row the row index of the cell
     * @param column the column index of the cell
     * @return this adapter with the view coordinates set to the given row
     *   and column index. 
     */
    public PageAdapter moveTo(int row, int column) {
        this.row = row;
        this.column = column;
        return this;
    }
    
    /** 
     * @inherited <p>
     */
    @Override
    public int getColumnCount() {
        return getComponent().getUI().getColumnCount();
    }


    /** 
     * @inherited <p>
     */
    @Override
    public int getRowCount() {
        return getComponent().getUI().getRowCount();
    }


    @Override
    public Calendar getValueAt(int row, int column) {
        return getComponent().getUI().getCell(row, column);
    }

    /**
     * Returns the cell state of the cell at given coordinates.
     * 
     * @param row the row index of the cell
     * @param column the column index of the cell
     * @return the cell state of the cell.
     */
    public CalendarCellState getCellState(int row, int column) {
        return getComponent().getUI().getCellState(row, column);
    }
    
    public CalendarCellState getCellState() {
        return getCellState(row, column);
    }

    public boolean isUnselectable() {
        if (!canBeUnselectable()) return false;
        Calendar cellDate = getValue();
        return cellDate != null ? getComponent().isUnselectableDate(cellDate.getTime()) : false;
    }
    
    
    @Override
    public boolean hasFocus() {
        Calendar cellDate = getValue();
        if ((cellDate == null) || !getCellState().isContent()) {
            return false;
        }
        Date lead = getComponent().getUI().getLead().getTime();
        return getCellState().isContainedInCell(cellDate, lead);
    }
    
    /**
     * @param dayState
     * @return
     */
    private boolean canBeUnselectable() {
        return (CalendarCellState.DAY_CELL == getCellState()) 
            || (CalendarCellState.TODAY_CELL == getCellState());
    }

    @Override
    public boolean isSelected() {
        Calendar cellDate = getValue();
        if (cellDate == null || getComponent().isSelectionEmpty()) {
            return false;
        }
        if (!getCellState().isContent()) return false;
        Date selected = getComponent().getSelectionDate();
        return getCellState().isContainedInCell(cellDate, selected);
    }

   
    /** 
     * @inherited <p>
     */
    @Override
    public JXCalendar getComponent() {
        return (JXCalendar) super.getComponent();
    }


    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }

    @Override
    public boolean isEditable() {
        return false;
    }

    @Override
    public Calendar getFilteredValueAt(int row, int column) {
        return getValueAt(row, column);
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
    public Calendar getValue(int modelColumnIndex) {
        return (Calendar) super.getValue(modelColumnIndex);
    }

    
}
