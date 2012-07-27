/*
 * $Id: BasicRowHeaderModel.java 1917 2007-11-16 10:58:10Z MiguelM $
 *
 * Copyright 2007 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.table;

import javax.swing.table.TableModel;

/**
 * A model to support the use of a TableRowHeader.
 *
 * @see TableRowHeader
 * @author Miguel Mu\u00f1oz
 */
public interface BasicRowHeaderModel {
	/**
	 * Returns the number of rows. This is identical to a method in TableModel.
	 *
	 * @return The number of rows.
	 */
	public int getRowCount();

	/**
	 * Gets the name of the row from the row model index. This name appears in the TableRowHeader.
	 * If you need to name the row based on the view index, be sure to convert the model index
	 * to the view index.
	 *
	 * @param rowModelIndex The model index of the row
	 * @return The name of the row to appear in the TableRowHeader.
	 */
	public Object getRowName(int rowModelIndex);

	/**
	 * A convenience model that supports both the basic TableModel and the BasicRowHeaderModel.
	 * Your table model may implement this interface to support both the table
	 * and its header.
	 */
	public interface RowHeaderTableModel extends BasicRowHeaderModel, TableModel {
	}
}
