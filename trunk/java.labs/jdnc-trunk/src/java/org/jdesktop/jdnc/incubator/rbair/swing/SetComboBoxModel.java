/*
 * $Id: SetComboBoxModel.java 19 2004-09-06 18:23:20Z rbair $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */

package org.jdesktop.jdnc.incubator.rbair.swing;

import java.util.Set;
import java.util.TreeSet;

import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;

/**
 * A combo box model based on a java.util.Set.
 * @author Richard Bair
 */
public class SetComboBoxModel extends AbstractListModel implements ComboBoxModel {
	/**
	 * The data for the combo box.
	 */
	private Set data;
	/**
	 * A cache.  The Cache is used to hold references to the items in the Set, since
	 * the Set interface does not provide get methods necessary for using a Set in the
	 * ComboBoxModel interface.
	 */
	private Object[] cache;
	/**
	 * Represents which object is selected in the combo box.
	 */
	private Object selectedObject;
	
	/**
	 * Create a new SetComboBoxModel.  Pass in the data for this model as a Set.
	 * @param data
	 */
	public SetComboBoxModel(Set data) {
		this.data = data;
		loadCache();
		selectedObject = cache.length > 0 ? cache[0] : null;
	}
	
	/**
	 * Add a single object to the Set of objects.
	 * @param obj
	 */
	public void addObject(Object obj) {
		data.add(obj);
		loadCache();
		fireContentsChanged(this, 0, cache.length - 1);
	}
	
	/**
	 * Returns a copy of the data as a Set.
	 * @return
	 */
	public Set getData() {
		return new TreeSet(data);
	}
	
	/**
	 * Private utility method that caches references to the items in
	 * the set to an array.
	 */
	private void loadCache() {
		cache = data.toArray();
	}

	/* (non-Javadoc)
	 * @see javax.swing.ComboBoxModel#getSelectedItem()
	 */
	public Object getSelectedItem() {
		return selectedObject;
	}

	/* (non-Javadoc)
	 * @see javax.swing.ComboBoxModel#setSelectedItem(java.lang.Object)
	 */
	public void setSelectedItem(Object anItem) {
		data.add(anItem);
		loadCache();
		selectedObject = anItem;
	}

	/* (non-Javadoc)
	 * @see javax.swing.ListModel#getSize()
	 */
	public int getSize() {
		return cache.length;
	}

	/* (non-Javadoc)
	 * @see javax.swing.ListModel#getElementAt(int)
	 */
	public Object getElementAt(int index) {
		if (index > cache.length - 1 || index < 0) {
			return null;
		}
		return cache[index];
	}
}