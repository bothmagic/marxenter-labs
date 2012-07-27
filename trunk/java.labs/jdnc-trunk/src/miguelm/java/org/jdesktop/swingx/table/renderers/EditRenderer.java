/*
 * $Id: EditRenderer.java 1917 2007-11-16 10:58:10Z MiguelM $
 *
 * Copyright 2007 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.table.renderers;

import java.awt.Component;
import java.awt.Color;

import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;
import javax.swing.JTable;

/**
 * @author Miguel Mu\u00f1oz
 * Copyright (c) 2004 by Miguel Munoz
 */

/**
 * This subclass of the DefaultTableCellRenderer is used to distinguish between
 * editable and non-editable cells. It leaves editable cells alone, but
 * slightly darkens cells that are read-only. All the work is done by a public
 * static method, so this Renderer may be used directly, or the static
 * method may be called from any other Cell Renderer.
 * @author Miguel Mu\u00f1oz
 */
public class EditRenderer extends DefaultTableCellRenderer {
	public EditRenderer() { super(); }

	/**
	 * Returns the default table cell renderer. Delegates the work to the static
	 * {@code shadeReadOnly()} method. Other renderers may do the same thing by
	 * calling the {@code shadeReadOnly()}
	 *
	 * @param table      the <code>JTable</code>
	 * @param value      the value to assign to the cell at <code>[row,
	 *                   column]</code>
	 * @param isSelected true if cell is selected
	 * @param hasFocus   true if cell has focus
	 * @param row        the row of the cell to render
	 * @param column     the column of the cell to render
	 * @return the default table cell renderer
	 */
	public Component getTableCellRendererComponent(
					JTable table,
					Object value,
					boolean isSelected,
					boolean hasFocus,
					int row,
					int column
	) {
		Component cmp = super.getTableCellRendererComponent(
						table, value, isSelected, hasFocus, row, column
		);
		shadeReadOnly(cmp, table, row, column, isSelected);
		return cmp;
	}

	/**
	 * Makes non-editable cells draw slightly darker than editable cells. This may
	 * be called from any table cell renderer. If the renderer extends from
	 * DefaultTableCellRenderer, call this after the call to the super method.
	 * <p> The parameters match those in the getTableCellRendererComponent().
	 *
	 * @param cmp        The renderer component.
	 * @param table      The JTable
	 * @param row        The row index
	 * @param column     The column index
	 * @param isSelected The selected state of the cell.
	 * @see #getTableCellRendererComponent(javax.swing.JTable,java.lang.Object,boolean,boolean,int,int)
	 */
	public static void shadeReadOnly(Component cmp, JTable table, int row, int column, boolean isSelected) {
		TableModel tMdl = table.getModel();
		Color bg;
		if (isSelected)
			bg = table.getSelectionBackground();
		else
			bg = table.getBackground();
		if (tMdl.isCellEditable(row, column))
			cmp.setBackground(bg);
		else
			cmp.setBackground(ash(bg));
	}

	/**
	 * Adapted from java.awt.Color.darker().
	 *
	 * @param clr The color to darken
	 * @return A slightly darkened version of the input color.
	 */
	private static Color ash(Color clr) {
		return new Color(
						Math.max((int) (clr.getRed() * sAshen), 0),
						Math.max((int) (clr.getGreen() * sAshen), 0),
						Math.max((int) (clr.getBlue() * sAshen), 0),
						clr.getAlpha());
	}

	private static final double sAshen = 0.95;
}
