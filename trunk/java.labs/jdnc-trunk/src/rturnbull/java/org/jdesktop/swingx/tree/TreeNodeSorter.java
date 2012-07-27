/** TreeNodeSorter.java                                       Swingx6
 *  Created 04/05/2008
 *  Author Ray Turnbull
 */
package org.jdesktop.swingx.tree;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.swing.DefaultRowSorter;

import org.jdesktop.swingx.treetable.TreeTableNode;

/**
 * 
 */
public class TreeNodeSorter<M extends AbstractSortableTreeTableNode> 
		extends DefaultRowSorter<M, Integer> {

    private static final Comparator<?> COMPARABLE_COMPARATOR =
        new ComparableComparator();

	private M node = null;
	private List<SortKey> newKeys;
	
	public TreeNodeSorter() {
	}
	
	public TreeNodeSorter(M node) {
		setNode(node);
	}
	
	public void setNode(M node) {
		this.node = node;
		setModelWrapper(new NodeSorterModelWrapper());
	}
	
	@Override
	public int convertRowIndexToModel(int viewIndex) {
		int count = getViewRowCount();
		if (viewIndex == count) {
			// inserted node not yet sorted
			return viewIndex;
		} else {
			return super.convertRowIndexToModel(viewIndex);
		}
	}
	
	public boolean sort(List<SortKey> sortKeys, Comparator<?>[] comparators, 
			boolean force) {
		boolean changed = buildSortKeys(sortKeys);
		if (!force && !changed) return false;
		if (comparators != null) {
			// load comparators
			for (int i = 0; i < comparators.length; i++) {
				setComparator(i, comparators[i]);
			}
		}
		if (changed) {
			setSortKeys(newKeys);
		} else {
			sort();
		}
		return true;
	}
	
	@Override
	public Comparator<?> getComparator(int column) {
		Comparator<?> c = super.getComparator(column);
		if (c != null) {
			return c;
		}
		TreeTableNode n = node.getChildAt(0);
		if (n == null) {
			return null;
		}
		Object o = n.getValueAt(column);
		if (o == null) {
			return null;
		}
		Class<?> cl = o.getClass();
		if (Comparable.class.isAssignableFrom(cl)) {
            return COMPARABLE_COMPARATOR;
		}
        return null;
    }

	// ====================================================== internal methods
	
	/*
	 * Extract sortkeys valid for this node and see if new keys different from
	 * current keys<br>
	 * Ignore any columns unsortable here, or outside range
	 */
	private boolean buildSortKeys(List<SortKey> sortKeys) {
		// build new keys
		newKeys = new ArrayList<SortKey>(sortKeys.size());
		for (SortKey key : sortKeys) {
			int column = key.getColumn();
			if (node.canSort(column)) {
				newKeys.add(key);
			}
		}
		// now see if keys have changed
		if (newKeys.equals(getSortKeys())) {
			return false;			
		} else {
			return true;
		}
	}
	
	// ========================================================= ModelWrapper
	
	/**
     * Implementation of DefaultRowSorter.ModelWrapper that delegates to a
     * TreeTableNode.
     */
    private class NodeSorterModelWrapper 
    			extends ModelWrapper<M,Integer> {

    	/**
    	 * want columns in child node
    	 */
    	@Override
		public int getColumnCount() {
    		return node.getChildColumnCount();
		}

		/* (non-Javadoc)
		 * @see javax.swing.DefaultRowSorter.ModelWrapper#getIdentifier(int)
		 */
		@Override
		public Integer getIdentifier(int row) {
			return row;
		}

		/* (non-Javadoc)
		 * @see javax.swing.DefaultRowSorter.ModelWrapper#getModel()
		 */
		@Override
		public M getModel() {
			return node;
		}

		/* (non-Javadoc)
		 * @see javax.swing.DefaultRowSorter.ModelWrapper#getRowCount()
		 */
		@Override
		public int getRowCount() {
			return node.getChildCount();
		}

		/* (non-Javadoc)
		 * @see javax.swing.DefaultRowSorter.ModelWrapper#getValueAt(int, int)
		 */
		@Override
		public Object getValueAt(int row, int column) {
			return node.getModelChildAt(row).getValueAt(column);
		}

    }

    // ======================================================================
    @SuppressWarnings("unchecked")
	private static class ComparableComparator implements Comparator {
        public int compare(Object o1, Object o2) {
            return ((Comparable)o1).compareTo(o2);
        }
    }
}
