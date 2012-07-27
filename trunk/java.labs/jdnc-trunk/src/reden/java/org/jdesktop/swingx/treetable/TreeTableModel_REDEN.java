package org.jdesktop.swingx.treetable;

import javax.swing.tree.TreeModel;


public interface TreeTableModel_REDEN extends TreeModel {
	/**
	 * Return the index of the column that will be represented as a tree.
	 */
	public int getHierarchicalColumn();

	public Class getColumnClass(int column);
    public int getColumnCount();
    public String getColumnName(int column);
    public Object getValueAt(Object node, int column);
    public boolean isCellEditable(Object node, int column);
    public void setValueAt(Object value, Object node, int column);
}