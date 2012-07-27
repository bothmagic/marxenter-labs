/** JXTableRowSorter.java                                   org.jdesktop.swingx.table
 * 
 *  Created 12/04/2008
 *
 *  @author Ray Turnbull
 */

package org.jdesktop.swingx.table;

import java.util.ArrayList;
import java.util.List;

import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;


/**
 * 
 */
public class JXTableRowSorter<M extends TableModel> extends TableRowSorter<M> {
	
	/**
	 * If true, sort order toggles through ascending, descending, unsorted, 
	 * else default sequence ascending, descending
	 */
	private boolean toggleToUnsorted = false;

	/**
	 * 
	 */
	public JXTableRowSorter() {
	}

	/**
	 * @param model
	 */
	public JXTableRowSorter(M model) {
		super(model);
	}
	
	/**
	 * Overridden to switch off click from BasicTableHeaderUI
	 */
	@Override
	public void toggleSortOrder(int column) {
		return;
	}
	
	/**
	 * Call this to toggle the sort order
	 * @param column in model coordinatese
	 */
	public void toggleSort(int column) {
		if (toggleToUnsorted) {
			List<? extends RowSorter.SortKey> keys = getSortKeys();
			for (SortKey key : keys) {
				if (key.getColumn() == column) {
					if (key.getSortOrder() == SortOrder.DESCENDING) {
						removeSort(column);
						return;
					} else {
						break;
					}
				}
			}
		}
		super.toggleSortOrder(column);
		
	}
	
	public void setToggleToUnsorted(boolean toggleToUnsorted) {
		this.toggleToUnsorted = toggleToUnsorted;
	}
	
	public boolean getToggleToUnsorted() {
		return toggleToUnsorted;
	}

	/**
	 * Remove sorting from column
	 * @param column in model coordinates
	 */
	public void removeSort(int column) {
        if (column == -1) {
        	return;
        }
        boolean changed = false;
        List<? extends RowSorter.SortKey> keys = getSortKeys();
        List<RowSorter.SortKey> newKeys = 
        				new ArrayList<RowSorter.SortKey>();
        for (SortKey sortKey : keys) {
			if (sortKey.getColumn() != column) {
				newKeys.add(sortKey);
			} else {
				changed = true;
			}
		}
        if (changed == true) {
        	setSortKeys(newKeys);
        }
    }

}
