/** AbstractSortableTreeTableNode.java                                       Swingx6
 *  Created 04/05/2008
 *  Author Ray Turnbull
 */
package org.jdesktop.swingx.tree;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import javax.swing.RowSorter.SortKey;
import javax.swing.tree.TreeNode;

import org.jdesktop.swingx.treetable.AbstractMutableTreeTableNode;
import org.jdesktop.swingx.treetable.MutableTreeTableNode;
import org.jdesktop.swingx.treetable.TreeTableNode;



/**
 *
 */
public abstract class AbstractSortableTreeTableNode extends
		AbstractMutableTreeTableNode {
	
	private TreeNodeSorter<AbstractSortableTreeTableNode> sorter = null;
	private List<Integer> unsortableColumns = null;
	private boolean sortable = false;

	// ========================================================= constructors
	
	public AbstractSortableTreeTableNode() {
	}

	/**
	 * @param userObject
	 */
	public AbstractSortableTreeTableNode(Object userObject) {
		super(userObject);
	}

	/**
	 * @param userObject
	 * @param allowsChildren
	 */
	public AbstractSortableTreeTableNode(Object userObject,
			boolean allowsChildren) {
		super(userObject, allowsChildren);
	}

	// ============================================================= sorting
	
	public void setSorter(TreeNodeSorter<AbstractSortableTreeTableNode> sorter) {
		this.sorter = sorter;
		this.sorter.setNode(this);
	}
	
	public void setSortable(boolean sortable) {
		this.sortable = sortable;
		if (sortable && sorter == null) {
			sorter = new TreeNodeSorter<AbstractSortableTreeTableNode>(this);
		}
		if (!sortable) {
			setSorter(null);
		}
	}
	
	public boolean isSortable() {
		return sortable;
	}
	
	public boolean sort(List<SortKey> sortKeys, Comparator<?>[] comparators, 
							boolean force) {
		if (!sortable) return false;
		return sorter.sort(sortKeys, comparators, force);
	}
	
	public void setUnsortableColumns(int[] unsortable) {
		Integer[] a = new Integer[unsortable.length];
		for (int i = 0; i < unsortable.length; i++) {
			a[i] = unsortable[i];
		}
		unsortableColumns = Arrays.asList(a);
	}
	
	/**
	 * Check if valid to sort this node on this column
	 * @param column
	 * @return true 
	 */
	public boolean canSort(int column) {
		if (!sortable) return false;
		if (column >= getChildColumnCount()) {
			return false;
		}
		if (unsortableColumns != null && unsortableColumns.contains(column)) {
			return false;
		}
		return true;
	}
	
	public int getChildColumnCount() {
		if (isLeaf()) return 0;
		TreeTableNode node = getModelChildAt(0);
		return node.getColumnCount();
	}
	
	public TreeTableNode getModelChildAt(int modelIndex) {
        return children.get(modelIndex);
	}
	
	public TreeNodeSorter<AbstractSortableTreeTableNode> getSorter() {
		return sorter;
	}
	
	private void updateSort() {
		if (sorter != null) {
			List<? extends SortKey> keys = sorter.getSortKeys();
			if (keys.size() > 0) {
				sorter.sort();
			}
		}
		
	}
	
	// ============================================================= overrides
	
	@Override
	public TreeTableNode getChildAt(int childIndex) {
		int modelIndex;
		if (sorter == null) {
			modelIndex = childIndex;
		} else {
			modelIndex = sorter.convertRowIndexToModel(childIndex);
		}
        return children.get(modelIndex);		
	}
	
    /**
     * {@inheritDoc}
     */
	@Override
    public void insert(MutableTreeTableNode child, int index) {
		super.insert(child, index);
		updateSort();
    }
    
    /**
     * {@inheritDoc}
     */
	@Override
	public void remove(int index) {
		super.remove(index);
		updateSort();
    }

    /**
     * {@inheritDoc}
     */
	@Override
    public void remove(MutableTreeTableNode node) {
        super.remove(node);
        updateSort();
    }
	
	@Override
	public int getIndex(TreeNode node) {
		int index = children.indexOf(node);
		if (sorter == null || index == -1 || index == sorter.getViewRowCount()) {
			return index;
		}
		return sorter.convertRowIndexToView(index);
	}

}
