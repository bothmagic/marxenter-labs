/*
 * $Id: ListViewModel.java 878 2006-09-22 13:59:58Z rasto1968 $
 *
 * Copyright 2006 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx;

import javax.swing.ImageIcon;
import javax.swing.table.TableModel;

/**
 * Defines a list view model.
 *
 * @author Rob Stone
 */
public interface ListViewModel extends TableModel
{
    /**
     * Returns the value that should be displayed for the given row and column
     * @param rowIndex the row
     * @param columnIndex the column
     * @return the value to display, if <code>null</code> then the cell renderer value is used
     */
    String getDisplayValue(final int rowIndex, final int columnIndex);
    
    /**
     * Returns the small icon to be displayed for the given row.
     * @param rowIndex the row
     * @return the icon to used, can be <code>null</code> if no icon was found
     */
    ImageIcon getSmallIcon(final int rowIndex);

    /**
     * Returns the large icon to be displayed for the given row.
     * @param rowIndex the row
     * @return the icon to used, can be <code>null</code> if no icon was found
     */
    ImageIcon getLargeIcon(final int rowIndex);

    /**
     * Returns the tool tip to be displayed for the given row.
     * @param rowIndex the row
     * @return the tooltip to display, or <code>null</code> for no tooltip
     */
    String getToolTipText(final int rowIndex, final int columnIndex);
}
