/*
 * $Id: Progression.java 29 2004-09-07 19:46:36Z rbair $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */

package org.jdesktop.jdnc.incubator.rbair.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * An object which represents a sequence, or progression, of objects.  A Progression
 * contains the next, previous, first, and last methods. A Progression does not support
 * the storing of null elements.
 * @author Richard Bair
 */
public class Progression {
	/**
	 * Index used when the current item pointer is before first
	 */
	public static final int BEFORE_FIRST = -1;
	/**
	 * Index used when the current item pointer is after last
	 */
	public static final int AFTER_LAST = -2;
	/**
	 * List of items in the Progression.
	 */
	private ArrayList list = new ArrayList();
	/**
	 * Indicates the index in the <code>list</code> of the current item.
	 */
	private int currentIndex = BEFORE_FIRST;
	/**
	 * List of filters to be applied
	 */
	private List filters = new ArrayList();
	
	/**
	 * Returns the current item in the Progression, or null if 
	 * either there are no items in the progression, or the current item
	 * pointer is BEFORE_FIRST or AFTER_LAST.
	 * @return
	 */
	public Object getCurrentItem() {
		return currentIndex < 0 ? null : list.get(currentIndex);
	}
	
	/**
	 * @return true if the Progression pointer is before first
	 */
	public boolean isBeforeFirst() {
		return currentIndex == BEFORE_FIRST;
	}
	
	/**
	 * @return true if the Progression pointer is after last
	 */
	public boolean isAfterLast() {
		return currentIndex == AFTER_LAST;
	}
	
	/**
	 * Returns the index of the current item in the progression, or -1
	 * if either there are no items in the progression, or the current
	 * item pointer is BEFORE_FIRST or AFTER_LAST
	 * @return
	 */
	public int getCurrentIndex() {
		return currentIndex < 0 ? -1 : currentIndex;
	}
	
	/**
	 * Sets the current index. If the index is less than 0, or greater 
	 * than the size of the list, an IllegalArgumentException is thrown.
	 * @param index
	 */
	public void setCurrentIndex(int index) {
		if (index < 0 || index >= list.size()) {
			throw new IllegalArgumentException("The index " + index + " passed into Progression.setCurrentIndex was invalid!");
		}
		currentIndex = index;
	}
	
	/**
	 * Sets the current index to be BEFORE_FIRST
	 */
	public void beforeFirst() {
		currentIndex = BEFORE_FIRST;
	}

	/**
	 * Sets the current index to be AFTER_LAST.
	 */
	public void afterLast() {
		currentIndex = AFTER_LAST;
	}
	
	/**
	 * Sets the current item to be the first item in the progression, and returns that item.
	 * If the list is empty, then beforeFirst will be called and null returned. Otherwise the
	 * first item in the list will become the current item.
	 * @return
	 */
	public Object first() {
		if (list.isEmpty()) {
			beforeFirst();
		} else {
			currentIndex = 0;
		}
		
		return getCurrentItem();
	}
	
	/**
	 * Sets the current item to be the last item in the progression, and returns that item.
	 * If the list is empty, then afterLast will be called, and null returned.
	 * @return
	 */
	public Object last() {
		if (list.isEmpty()) {
			afterLast();
		} else {
			currentIndex = list.size() - 1;
		}
		
		return getCurrentItem();
	} 
	
	/**
	 * Sets the current item to be the next item in sequence in the progression, and returns that
	 * item.  If there is no next item (hasNext() returns false), then progress to after last, and
	 * return null.
	 * @return
	 */
	public Object next() {
		if (hasNext()){
			currentIndex++;
		} else {
			afterLast();
		}
		return getCurrentItem();
	}
	
	/**
	 * Sets the current item to be the previous item in sequence in the progression, and returns that
	 * item.  If there is no previous item (hasPrevious() returns false), then progress to before first,
	 * and return null.
	 * @return
	 */
	public Object previous() {
		if (hasPrevious()) {
			if (currentIndex == AFTER_LAST) {
				currentIndex = list.size() - 1;
			} else {
				currentIndex--;
			}
		} else {
			beforeFirst();
		}
		return getCurrentItem();
	}
	
	/**
	 * Indicates whether there are any more items in the progression AFTER the current item.
	 * @return
	 */
	public boolean hasNext() {
		return currentIndex < list.size() - 1 && list.size() > 0 && currentIndex != AFTER_LAST;
	}
	
	/**
	 * Indicates whether there are any more items in the progression BEFORE the current item.
	 * @return
	 */
	public boolean hasPrevious() {
		//there is no previous ONLY when on the first item, or before first
		return currentIndex != BEFORE_FIRST && currentIndex != 0;
	}

	/**
	 * Returns the number of items in the progression.
	 * @return
	 */
	public int size() {
		return list.size();
	}
	
	/**
	 * Goes to the specified object in the progression.  If no such object is currently in the progression,
	 * then the object is added to the end of the progression, and made the current item.
	 * @param obj
	 */
	public void gotoObject(Object obj) {
		if (passedFilters(obj)) {
			int index = indexOf(obj);
			if (index >= 0) {
				currentIndex = index;
			} else {
				list.add(obj);
				last();
			}
		}
	}

	/**
	 * Adds an item to the progression.
	 * @param obj
	 */
	public void add(Object obj) {
		if (passedFilters(obj)) {
			list.add(obj);
		}
	}
	
	/**
	 * Inserts the given object at the given index, if it passes all of the filters.
	 * @param obj
	 * @param index
	 */
	public void add(Object obj, int index) {
		if (passedFilters(obj)) {
			list.add(index, obj);
		}
	}

	/**
	 * Adds an entire collection of objects to the progression.
	 * @param c
	 */
	public void addAll(Collection c) {
		if (c == list) {
			throw new IllegalArgumentException("Progression cannot addAll " +
					"recursively. The collection passed to addAll is the " +
					"progression's own list.");
		}
		
		Iterator itr = c.iterator();
		while (itr.hasNext()) {
			Object obj = itr.next();
			if (passedFilters(obj)) {
				list.add(obj);
			}
		}
	}
	
	/**
	 * Adds an entire array of objects to the progression
	 * @param items
	 */
	public void addAll(Object[] items) {
		addAll(Arrays.asList(items));
	}
	
	/**
	 * Adds an entire progression to the current progression.  Follows the same rules/logic as addAll(Collection c)
	 * @param p
	 */
	public void addAll(Progression p) {
		if (p == this) {
			throw new IllegalArgumentException("Progression cannot addAll " +
					"recursively. The progression passed to addAll is the " +
					"progression itself.");
		}
		addAll(p.list);
	}

	/**
	 * Removes the specified object from the progression.  An attempt is made to find the next closest
	 * object to be the current item. 
	 * @param obj
	 * @return
	 */
	public boolean remove(Object obj) {
		int index = indexOf(obj);
		if (index == currentIndex && currentIndex != BEFORE_FIRST) {
			//the current item is what is to be removed - set the currentIndex to be the index
			//as close to the index being removed as possible
			if (index >= 0) {
				list.remove(obj);
				if (list.size() <= index) {
					last();
					return true;
				} else {
					currentIndex = index;
					return true;
				}
			} else {
				return false;
			}
		} else {
			//simply remove the item
			return list.remove(obj);
		}
	}
	
	/**
	 * Removes an entire collection of objects from the progression.
	 * An attempt is made to find the next closest object to be the current item. 
	 * @param c
	 * @return
	 */
	public boolean removeAll(Collection c) {
		if (c == list) {
			throw new IllegalArgumentException("Progression cannot removeAll " +
					"recursively. The collection passed to removeAll is the " +
					"progression's own list.");
		}

		Object currentItem = getCurrentItem();
		if (currentItem != null && c.contains(currentItem)) {
			//the current item is in the list of stuff to remove, so update the currentIndex pointer
			//set the pointer to the index as close to the current index as possible
			int index = list.indexOf(currentItem);
			list.removeAll(c);
			if (list.size() <= index) {
				last();
				return true;
			} else {
				currentItem = list.get(index);
				return true;
			}
		} else {
			//simply remove the items
			return list.removeAll(c);
		}
	}

	/**
	 * Removes from this progression any item equaling an item in the given
	 * array
	 * @param items
	 * @return
	 */
	public boolean removeAll(Object[] items) {
		return removeAll(Arrays.asList(items));
	}
	
	/**
	 * Removes from this progression any item equaling an item in the given 
	 * progression
	 * @param items
	 * @return
	 */
	public boolean removeAll(Progression p) {
		if (p == this) {
			throw new IllegalArgumentException("Progression cannot removeAll " +
					"recursively. The progression passed to removeAll is the " +
					"progression itself.");
		}
		return removeAll(p.list);
	}
	
	/**
	 * removes all of the elements from the progression
	 * @return
	 */
	public boolean removeAll() {
		currentIndex = BEFORE_FIRST;
		list.clear();
		return true;
	}

	/**
	 * Checks the given object against all of the filters.  If it passes all of the filters, then
	 * this method returns true.
	 * @param obj
	 * @return
	 */
	private boolean passedFilters(Object obj) {
		for (int i=0; i<filters.size(); i++) {
			ProgressionFilter filter = (ProgressionFilter)filters.get(i);
			if (filter.filter(obj)) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Returns the index of the given object in the progression, or -1 if it isn't there.
	 * @param obj
	 * @return
	 */
	public int indexOf(Object obj) {
		return list.indexOf(obj);
	}

	/**
	 * Returns the object at the specified index.  Throws an ArrayIndexOutOfBounds exception if the
	 * index is bad.
	 * @param index
	 * @return
	 */
	public Object getElementAt(int index) {
		return list.get(index);
	}
	
	/**
	 * Returns true if the given object is in the progression.
	 * @param obj
	 * @return
	 */
	public boolean contains(Object obj) {
		return list.contains(obj);
	}
	
	/**
	 * Returns the progression as a List.
	 * @return
	 */
	public List getList() {
		return list;
	}

	/**
	 * Adds a filter (if it is not already in the list of filters) to the list of filters
	 * @param filter
	 */
	public void addFilter(ProgressionFilter filter) {
		if (!filters.contains(filter)) {
			filters.add(filter);
		}
	}
	
	/**
	 * Removes the given filter from the list of filters, if its in there
	 * @param filter
	 */
	public void removeFilter(ProgressionFilter filter) {
		filters.remove(filter);
	}
//TODO turn into unit test
//	public static final void main(String[] args) {
//		Progression p = new Progression();
//		System.out.println("null=" + p.first());
//		System.out.println("null=" + p.next());
//		System.out.println("null=" + p.previous());
//		System.out.println("null=" + p.last());
//		System.out.println("null=" + p.current());
//		p.add("Item 1");
//		System.out.println("Item 1=" + p.current());
//		System.out.println("Item 1=" + p.first());
//		System.out.println("Item 1=" + p.next());
//		System.out.println("Item 1=" + p.previous());
//		System.out.println("Item 1=" + p.last());
//		p.add("Item 2");
//		System.out.println("Item 1=" + p.current());
//		System.out.println("Item 1=" + p.first());
//		System.out.println("Item 2=" + p.next());
//		System.out.println("Item 1=" + p.previous());
//		System.out.println("Item 2=" + p.last());
//		p.addAll(Arrays.asList(new String[]{"Item 3", "Item 4", "Item 5"}));
//		System.out.println("Item 2=" + p.current());
//		System.out.println("Item 1=" + p.first());
//		System.out.println("Item 2=" + p.next());
//		System.out.println("Item 3=" + p.next());
//		System.out.println("Item 2=" + p.previous());
//		System.out.println("Item 3=" + p.next());
//		System.out.println("Item 5=" + p.last());
//		p.remove("Item 5");
//		System.out.println("Item 4=" + p.current());
//		System.out.println("Item 1=" + p.first());
//		System.out.println("Item 2=" + p.next());
//		System.out.println("Item 3=" + p.next());
//		System.out.println("Item 2=" + p.previous());
//		System.out.println("Item 3=" + p.next());
//		System.out.println("Item 4=" + p.last());
//		p.remove("Item 3");
//		System.out.println("Item 4=" + p.current());
//		System.out.println("Item 1=" + p.first());
//		System.out.println("Item 2=" + p.next());
//		System.out.println("Item 4=" + p.next());
//		System.out.println("Item 2=" + p.previous());
//		System.out.println("Item 4=" + p.next());
//		System.out.println("Item 4=" + p.last());
//		p.removeAll(Arrays.asList(new String[]{"Item 1", "Item 4"}));
//		System.out.println("Item 2=" + p.current());
//		System.out.println("Item 2=" + p.first());
//		System.out.println("Item 2=" + p.next());
//		System.out.println("Item 2=" + p.next());
//		System.out.println("Item 2=" + p.previous());
//		System.out.println("Item 2=" + p.next());
//		System.out.println("Item 2=" + p.last());
//	}
}
