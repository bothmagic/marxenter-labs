/** TreeTableSorter.java                                       Swingx6
 *  Created 04/05/2008
 *  Author Ray Turnbull
 */
package org.jdesktop.swingx.tree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.List;

import javax.swing.JTree;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.table.TableModel;
import javax.swing.tree.TreePath;

import org.jdesktop.swingx.treetable.AbstractTreeTableModel;
import org.jdesktop.swingx.treetable.TreeTableNode;

/**
 * This is a pseudo sorter to be attached to a JXTreeTable<br>
 * Most methods are no-ops, the remainder pass through to the nodes where the
 * actual sorting takes place.
 */
public class TreeTableSorter<M extends TableModel> extends RowSorter<M>
		implements TreeModelListener, TreeExpansionListener  {

	private M						model;
	private AbstractTreeTableModel	ttModel;
	/**
	 * If true, sort order toggles through ascending, descending, unsorted, else
	 * default sequence ascending, descending
	 */
	private boolean					toggleToUnsorted	= false;
	/**
	 * Whether or not the specified column is sortable, by column. This is a
	 * global setting. Individual nodes may include extra columns
	 */
	private boolean[]				isSortable;
	/**
	 * The sort keys.
	 */
	private List<SortKey>			sortKeys;
	/**
	 * Comparators specified by column.
	 */
	private Comparator<?>[]			comparators;

	/**
	 * To maintain expanded nodes
	 */
	private JTree					tree = null;
	private List<TreePath>			expandedPaths;

	private TreeModelSupport		supporter;
	private boolean					needSort;
	private boolean					internal = false;

	/**
	 * 
	 */
	public TreeTableSorter(M model, AbstractTreeTableModel ttModel) {
		this.model = model;
		this.ttModel = ttModel;
		sortKeys = Collections.emptyList();
		supporter = new TreeModelSupport(ttModel);
		ttModel.addTreeModelListener(this);
		expandedPaths = new ArrayList<TreePath>();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.RowSorter#getViewRowCount()
	 */
	@Override
	public int getViewRowCount() {
		return model.getRowCount();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.RowSorter#getSortKeys()
	 */
	@Override
	public List<? extends RowSorter.SortKey> getSortKeys() {
		return sortKeys;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.DefaultRowSorter#setSortKeys(java.util.List)
	 */
	@Override
	public void setSortKeys(List<? extends RowSorter.SortKey> keys) {
		if (!(ttModel instanceof AbstractTreeTableModel)) return;
		List<SortKey> old = this.sortKeys;
		if (keys != null && keys.size() > 0) {
			int max = ttModel.getColumnCount();
			for (SortKey key : keys) {
				if (key == null || key.getColumn() < 0 ||
						key.getColumn() >= max) { throw new IllegalArgumentException(
						"Invalid SortKey"); }
			}
			this.sortKeys =
					Collections.unmodifiableList(new ArrayList<SortKey>(keys));
		} else {
			this.sortKeys = Collections.emptyList();
		}
		if (!this.sortKeys.equals(old)) {
			sort(false);
		}
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
	 * 
	 * @param column in model coordinatese
	 */
	public void toggleSort(int column) {
		checkColumn(column);
		if (isSortable(column)) {
			List<SortKey> keys = new ArrayList<SortKey>(getSortKeys());
			SortKey sortKey;
			int sortIndex;
			for (sortIndex = keys.size() - 1; sortIndex >= 0; sortIndex--) {
				if (keys.get(sortIndex).getColumn() == column) {
					break;
				}
			}
			if (sortIndex == -1) {
				// Key doesn't exist
				checkNeedSort(column);
				if (!needSort) return;
				sortKey = new SortKey(column, SortOrder.ASCENDING);
				keys.add(0, sortKey);
			} else if (sortIndex == 0) {
				// It's the primary sorting key, toggle it
				SortKey key = keys.get(0);
				if (key.getSortOrder() == SortOrder.DESCENDING &&
						toggleToUnsorted) {
					keys.remove(0);
				} else {
					keys.set(0, toggle(keys.get(0)));
				}
			} else {
				// It's not the first, but was sorted on, remove old
				// entry, insert as first with ascending.
				keys.remove(sortIndex);
				keys.add(0, new SortKey(column, SortOrder.ASCENDING));
			}
			setSortKeys(keys);
		}
	}

	/**
	 * Remove sorting from column
	 * 
	 * @param column in model coordinates
	 */
	public void removeSort(int column) {
		checkColumn(column);
		boolean changed = false;
		List<? extends RowSorter.SortKey> keys = getSortKeys();
		List<RowSorter.SortKey> newKeys = new ArrayList<RowSorter.SortKey>();
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

	public void setToggleToUnsorted(boolean toggleToUnsorted) {
		this.toggleToUnsorted = toggleToUnsorted;
	}

	public boolean getToggleToUnsorted() {
		return toggleToUnsorted;
	}

	/**
	 * Sets whether or not the specified column is sortable. The specified value
	 * is only checked when <code>toggleSortOrder</code> is invoked. It is
	 * still possible to sort on a column that has been marked as unsortable by
	 * directly setting the sort keys. Individual nodes may override a 'true'
	 * setting here. The default is true.
	 * 
	 * @param column the column to enable or disable sorting on, in terms of the
	 *        underlying model
	 * @param sortable whether or not the specified column is sortable
	 * @throws IndexOutOfBoundsException if <code>column</code> is outside the
	 *         range of the model
	 * @see #toggleSortOrder
	 * @see #setSortKeys
	 */
	public void setSortable(int column, boolean sortable) {
		checkColumn(column);
		if (isSortable == null) {
			isSortable = new boolean[model.getColumnCount()];
			for (int i = isSortable.length - 1; i >= 0; i--) {
				isSortable[i] = true;
			}
		}
		isSortable[column] = sortable;
	}

	/**
	 * Sets the <code>Comparator</code> to use when sorting the specified
	 * column. This does not trigger a sort. If you want to sort after setting
	 * the comparator you need to explicitly invoke <code>sort</code>.
	 * 
	 * @param column the index of the column the <code>Comparator</code> is to
	 *        be used for, in terms of the underlying model
	 * @param comparator the <code>Comparator</code> to use
	 * @throws IndexOutOfBoundsException if <code>column</code> is outside the
	 *         range of the underlying model
	 */
	public void setComparator(int column, Comparator<?> comparator) {
		checkColumn(column);
		if (comparators == null) {
			comparators = new Comparator[model.getColumnCount()];
		}
		comparators[column] = comparator;
	}

	public void sort() {
		sort(true);
	}

	// ====================================================== internal methods

	/**
	 * This passes sortKeys to all nodes of the TreeTable for individual sorts
	 */
	private void sort(boolean force) {
		TreeTableNode root = (TreeTableNode) ttModel.getRoot();
		processNode(root, force, true, -1);
	}

	/**
	 * As actual sorting is removed from setting of sort keys, check that column
	 * can be sorted, to avoid column header showing as sorted when not. <br>
	 * Loop through all nodes to see if at least one can be sorted.
	 */
	private void checkNeedSort(int column) {
		needSort = false;
		TreeTableNode root = (TreeTableNode) ttModel.getRoot();
		processNode(root, false, false, column);
	}

	private void processNode(TreeTableNode parent, boolean force, boolean sort,
			int column) {
		if (parent.isLeaf()) return;
		AbstractSortableTreeTableNode node = castNode(parent);
		if (node != null) {
			if (sort) {
				sortNode(node, force);
			} else {
				if (!needSort) {
					checkNode(node, column);
				} else {
					return;
				}
			}
		}
		// check children
		Enumeration<? extends TreeTableNode> children = parent.children();
		while (children.hasMoreElements()) {
			TreeTableNode child = (TreeTableNode) children.nextElement();
			processNode(child, force, sort, column);
		}
	}

	private void sortNode(AbstractSortableTreeTableNode node, boolean force) {
		if (node.isSortable()) {
			boolean sorted = node.sort(sortKeys, comparators, force);
			if (sorted) {
				notifyChange(node);
			}
		}
	}

	private void checkNode(AbstractSortableTreeTableNode node, int column) {
		if (node.isSortable()) {
			if (node.canSort(column)) {
				needSort = true;
			}
		}
	}

	/**
	 * Returns true if the specified column is sortable; otherwise, false.<br>
	 * This checks the global boolean 'sortable' array. A column may also be set
	 * not sortable for a node with {@link TreeNodeSorter} That is not checked
	 * here.
	 * 
	 * @param column the column to check sorting for, in terms of the underlying
	 *        model
	 * @return true if the column is sortable
	 */
	private boolean isSortable(int column) {
		checkColumn(column);
		return (isSortable == null) ? true : isSortable[column];
	}

	private void checkColumn(int column) {
		if (column < 0 || column >= model.getColumnCount()) { throw new IndexOutOfBoundsException(
				"column beyond range of TreeTableModel"); }
	}

	private SortKey toggle(SortKey key) {
		if (key.getSortOrder() == SortOrder.ASCENDING) { return new SortKey(key
				.getColumn(), SortOrder.DESCENDING); }
		return new SortKey(key.getColumn(), SortOrder.ASCENDING);
	}

	private TreePath getPath(TreeTableNode aNode) {
		List<TreeTableNode> path = new ArrayList<TreeTableNode>();
		TreeTableNode rootNode = (TreeTableNode) ttModel.getRoot();
		TreeTableNode node = aNode;
		while (node != rootNode) {
			path.add(0, node);
			node = (TreeTableNode) node.getParent();
		}
		path.add(0, node);
		return new TreePath(path.toArray());
	}

	private void validateListeners() {
		TreeModelListener[] list = supporter.getTreeModelListeners();
		if (list.length > 0) return; // listeners already loaded
		list = ttModel.getTreeModelListeners();
		for (int i = 0; i < list.length; i++) {
			TreeModelListener listener = list[i];
			if (listener != this) {
				supporter.addTreeModelListener(listener);
			}
		}
	}

	private AbstractSortableTreeTableNode castNode(Object node) {
		if (node instanceof AbstractSortableTreeTableNode) {
			return (AbstractSortableTreeTableNode) node;
		} else {
			return null;
		}
	}

	private void notifyChange(AbstractSortableTreeTableNode node) {
		TreePath path = getPath(node);
		// need this because TreeSupport in model not accessible
		validateListeners();
		supporter.fireTreeStructureChanged(path);
		if (tree != null && expandedPaths.size() > 0) {
			internal = true;
			for (TreePath path1 : expandedPaths) {
				if (tree.isCollapsed(path1)) {
					tree.expandPath(path1);
				}
			}
			internal = false;
		}
	}

	// ==================================================== TreeModelListener

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.event.TreeModelListener#treeNodesChanged(javax.swing.event.TreeModelEvent)
	 */
	@Override
	public void treeNodesChanged(TreeModelEvent e) {
		return;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.event.TreeModelListener#treeNodesInserted(javax.swing.event.TreeModelEvent)
	 */
	@Override
	public void treeNodesInserted(TreeModelEvent e) {
		processChange(e);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.event.TreeModelListener#treeNodesRemoved(javax.swing.event.TreeModelEvent)
	 */
	@Override
	public void treeNodesRemoved(TreeModelEvent e) {
		processChange(e);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.event.TreeModelListener#treeStructureChanged(javax.swing.event.TreeModelEvent)
	 */
	@Override
	public void treeStructureChanged(TreeModelEvent e) {
		return;
	}

	private void processChange(TreeModelEvent e) {
		TreePath path = e.getTreePath();
		Object o = path.getLastPathComponent();
		final AbstractSortableTreeTableNode node = castNode(o);
		if (node != null && node.isSortable()) {
			TreeNodeSorter<AbstractSortableTreeTableNode> sorter =
				node.getSorter();
			if (sorter.getSortKeys().size() > 0) {
				sorter.sort();
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						notifyChange(node);
					}
				});
			}
		}
	}

	// ================================================ TreeExpansionListener
	
	/* (non-Javadoc)
	 * @see javax.swing.event.TreeExpansionListener#treeCollapsed(javax.swing.event.TreeExpansionEvent)
	 */
	@Override
	public void treeCollapsed(TreeExpansionEvent e) {
		TreePath path = e.getPath();
		expandedPaths.remove(path);
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.event.TreeExpansionListener#treeExpanded(javax.swing.event.TreeExpansionEvent)
	 */
	@Override
	public void treeExpanded(TreeExpansionEvent e) {
		if (internal) return;
		if (tree == null) {
			tree = (JTree) e.getSource();
		}
		TreePath path = e.getPath();
		expandedPaths.add(path);
	}
	
	// =============================================================== no-ops

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.RowSorter#allRowsChanged()
	 */
	@Override
	public void allRowsChanged() {
		return;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.RowSorter#convertRowIndexToModel(int)
	 */
	@Override
	public int convertRowIndexToModel(int index) {
		return index;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.RowSorter#convertRowIndexToView(int)
	 */
	@Override
	public int convertRowIndexToView(int index) {
		return index;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.RowSorter#getModel()
	 */
	@Override
	public M getModel() {
		return model;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.RowSorter#getModelRowCount()
	 */
	@Override
	public int getModelRowCount() {
		return -1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.RowSorter#modelStructureChanged()
	 */
	@Override
	public void modelStructureChanged() {
		return;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.RowSorter#rowsDeleted(int, int)
	 */
	@Override
	public void rowsDeleted(int firstRow, int endRow) {
		return;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.RowSorter#rowsInserted(int, int)
	 */
	@Override
	public void rowsInserted(int firstRow, int endRow) {
		return;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.RowSorter#rowsUpdated(int, int)
	 */
	@Override
	public void rowsUpdated(int firstRow, int endRow) {
		return;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.RowSorter#rowsUpdated(int, int, int)
	 */
	@Override
	public void rowsUpdated(int firstRow, int endRow, int column) {
		return;
	}

}
