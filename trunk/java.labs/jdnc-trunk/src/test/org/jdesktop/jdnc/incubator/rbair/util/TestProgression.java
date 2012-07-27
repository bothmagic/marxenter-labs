/*
 * $Id: TestProgression.java 31 2004-09-07 20:20:42Z rbair $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.jdnc.incubator.rbair.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import junit.framework.TestCase;

import org.jdesktop.jdnc.incubator.rbair.util.Progression;
import org.jdesktop.jdnc.incubator.rbair.util.ProgressionFilter;

/**
 * Test class for Progression and ProgressionFilter.
 * @author Richard Bair
 */
public class TestProgression extends TestCase {
	private ProgressionFilter nameFilter;
	private ProgressionFilter ageFilter;
	private Person snowWhite = new Person("Snow White", new Date(Date.parse("07/17/82")));
	private Person robinHood = new Person("Robin Hood", new Date(Date.parse("02/01/68")));
	private Person littleJohn = new Person("Little John", new Date(Date.parse("10/13/55")));
	private Person princeJohn = new Person("Prince John", new Date(Date.parse("12/12/78")));
	private Person peterPan = new Person("Peter Pan", new Date(Date.parse("04/21/90")));
	private Person captainHook = new Person("Captain Hook", new Date(Date.parse("07/07/42")));
	
	private static final class Person {
		String name;
		Date birthDate;
		
		public Person(String n, Date bd) {
			this.name = n;
			this.birthDate = bd;
		}
	}
	
	public TestProgression(String string) {
		super(string);
	}
	
	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		nameFilter = new ProgressionFilter() {
			/* (non-Javadoc)
			 * @see org.jdesktop.jdnc.incubator.rbair.util.ProgressionFilter#filter(java.lang.Object)
			 */
			public boolean filter(Object obj) {
				if (obj instanceof Person && ((Person)obj).name.endsWith("John")) {
					return true;
				}
				return false;
			}
		};
		ageFilter = new ProgressionFilter() {
			/* (non-Javadoc)
			 * @see org.jdesktop.jdnc.incubator.rbair.util.ProgressionFilter#filter(java.lang.Object)
			 */
			public boolean filter(Object obj) {
				if (obj instanceof Person && ((Person)obj).birthDate.before(new Date(0))) {
					return true;
				}
				return false;
			}
		};
	}
	
	/* (non-Javadoc)
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		nameFilter = null;
		ageFilter = null;
	}
	
	public void testAdd() {
		//create a new progression. It should have an initial size of 0
		Progression p = new Progression();
		assertTrue(p.size() == 0);
		//add a single object. p should now have a size of 1. robinHood
		//should be the first object (index 0)
		p.add(robinHood);
		assertTrue(p.size() == 1);
		assertTrue(p.getElementAt(0) == robinHood);
		//add another single object, but at index 0. Size should now be 2.
		//snowWhite should be at index 0, and robinHood at index 1.
		p.add(snowWhite, 0);
		assertTrue(p.size() == 2);
		assertTrue(p.getElementAt(0) == snowWhite);
		assertTrue(p.getElementAt(1) == robinHood);
		//add both littleJohn and princeJohn as an array. Size should now be 4,
		//and it should contain snowWhite, robinHood, littleJohn and princeJohn,
		//in that order.
		p.addAll(new Object[]{littleJohn, princeJohn});
		assertTrue(p.size() == 4);
		assertTrue(p.getElementAt(0) == snowWhite);
		assertTrue(p.getElementAt(1) == robinHood);
		assertTrue(p.getElementAt(2) == littleJohn);
		assertTrue(p.getElementAt(3) == princeJohn);
		//add peterPan as part of a List. Size should be 6; peterPan and
		//captainHook should be at the end, in that order.
		List list = new ArrayList();
		list.add(peterPan);
		list.add(captainHook);
		p.addAll(list);
		assertTrue(p.size() == 6);
		assertTrue(p.getElementAt(0) == snowWhite);
		assertTrue(p.getElementAt(1) == robinHood);
		assertTrue(p.getElementAt(2) == littleJohn);
		assertTrue(p.getElementAt(3) == princeJohn);
		assertTrue(p.getElementAt(4) == peterPan);
		assertTrue(p.getElementAt(5) == captainHook);
		//finally, add all 6 of the characters again.
		Progression dupProgression = new Progression();
		dupProgression.addAll(p);
		p.addAll(dupProgression);
		assertTrue(p.size() == 12);
		assertTrue(p.getElementAt(0) == snowWhite);
		assertTrue(p.getElementAt(1) == robinHood);
		assertTrue(p.getElementAt(2) == littleJohn);
		assertTrue(p.getElementAt(3) == princeJohn);
		assertTrue(p.getElementAt(4) == peterPan);
		assertTrue(p.getElementAt(5) == captainHook);
		assertTrue(p.getElementAt(6) == snowWhite);
		assertTrue(p.getElementAt(7) == robinHood);
		assertTrue(p.getElementAt(8) == littleJohn);
		assertTrue(p.getElementAt(9) == princeJohn);
		assertTrue(p.getElementAt(10) == peterPan);
		assertTrue(p.getElementAt(11) == captainHook);
	}
	
	public void testRemove() {
		//create and preload the progression
		Progression p = new Progression();
		p.addAll(new Object[]{snowWhite, robinHood, littleJohn, princeJohn,
				peterPan, captainHook});
		//remove snowWhite
		p.remove(snowWhite);
		assertTrue(p.size() == 5);
		assertTrue(p.getElementAt(0) == robinHood);
		assertTrue(p.getElementAt(1) == littleJohn);
		assertTrue(p.getElementAt(2) == princeJohn);
		assertTrue(p.getElementAt(3) == peterPan);
		assertTrue(p.getElementAt(4) == captainHook);
		//remove princejohn
		p.removeAll(new Object[]{princeJohn});
		assertTrue(p.size() == 4);
		assertTrue(p.getElementAt(0) == robinHood);
		assertTrue(p.getElementAt(1) == littleJohn);
		assertTrue(p.getElementAt(2) == peterPan);
		assertTrue(p.getElementAt(3) == captainHook);
		//remove robinHood and peterPan and snowWhite again (she isn't in the
		//list this time
		p.removeAll(Arrays.asList(new Object[]{robinHood,peterPan,snowWhite}));
		assertTrue(p.size() == 2);
		assertTrue(p.getElementAt(0) == littleJohn);
		assertTrue(p.getElementAt(1) == captainHook);
		//remove captainHook and littleJohn
		Progression temp = new Progression();
		temp.add(littleJohn);
		temp.add(captainHook);
		p.removeAll(temp);
		assertTrue(p.size() == 0);
		//refill the list and remove all
		p.addAll(new Object[]{snowWhite, robinHood, littleJohn, princeJohn,
				peterPan, captainHook});
		assertTrue(p.size() == 6);
		assertTrue(p.removeAll());
		assertTrue(p.size() == 0);
	}
	
	public void testNavigation() {
		//create and preload the progression, omitting snowWhite
		Progression p = new Progression();
		p.addAll(new Object[]{robinHood, littleJohn, princeJohn,
				peterPan, captainHook});
		//make sure p is in beforeFirst state
		assertTrue(p.isBeforeFirst());
		//navigate the progression from beforeFirst to afterLast
		assertTrue(p.hasNext());
		assertTrue(p.next() == robinHood);
		assertFalse(p.hasPrevious());
		assertFalse(p.isAfterLast());
		assertFalse(p.isBeforeFirst());
		assertTrue(p.hasNext());
		assertTrue(p.next() == littleJohn);
		assertTrue(p.hasPrevious());
		assertFalse(p.isAfterLast());
		assertFalse(p.isBeforeFirst());
		assertTrue(p.hasNext());
		assertTrue(p.next() == princeJohn);
		assertTrue(p.hasPrevious());
		assertFalse(p.isAfterLast());
		assertFalse(p.isBeforeFirst());
		assertTrue(p.hasNext());
		assertTrue(p.next() == peterPan);
		assertTrue(p.hasPrevious());
		assertFalse(p.isAfterLast());
		assertFalse(p.isBeforeFirst());
		assertTrue(p.hasNext());
		assertTrue(p.next() == captainHook);
		assertFalse(p.hasNext());
		assertTrue(p.hasPrevious());
		assertFalse(p.isAfterLast());
		assertFalse(p.isBeforeFirst());
		assertTrue(p.next() == null);
		assertFalse(p.hasNext());
		assertTrue(p.isAfterLast());
		assertTrue(p.hasPrevious());
		assertTrue(p.previous() == captainHook);
		assertTrue(p.hasPrevious());
		assertFalse(p.isAfterLast());
		assertFalse(p.isBeforeFirst());
		assertFalse(p.hasNext());
		assertTrue(p.previous() == peterPan);
		assertTrue(p.hasPrevious());
		assertFalse(p.isAfterLast());
		assertFalse(p.isBeforeFirst());
		assertTrue(p.hasNext());
		assertTrue(p.previous() == princeJohn);
		assertTrue(p.hasPrevious());
		assertFalse(p.isAfterLast());
		assertFalse(p.isBeforeFirst());
		assertTrue(p.hasNext());
		assertTrue(p.previous() == littleJohn);
		assertTrue(p.hasPrevious());
		assertFalse(p.isAfterLast());
		assertFalse(p.isBeforeFirst());
		assertTrue(p.hasNext());
		assertTrue(p.previous() == robinHood);
		assertTrue(p.previous() == null);
		assertFalse(p.hasPrevious());
		assertTrue(p.isBeforeFirst());
		assertTrue(p.hasNext());
	}
	
	public void testFilters() {
		//create the progression
		Progression p = new Progression();
		//apply the name filter
		p.addFilter(nameFilter);
		//add the items
		p.addAll(new Object[]{snowWhite, robinHood, littleJohn, princeJohn,
				peterPan, captainHook});
		//the size should be 4; snowWhite, robinHood, peterPan, captainHook
		assertTrue(p.size() == 4);
		assertTrue(p.getElementAt(0) == snowWhite);
		assertTrue(p.getElementAt(1) == robinHood);
		assertTrue(p.getElementAt(2) == peterPan);
		assertTrue(p.getElementAt(3) == captainHook);
		//clear out the progression
		p.removeAll();
		//remove the name filter and add the age filter
		p.removeFilter(nameFilter);
		p.addFilter(ageFilter);
		//add the items
		p.addAll(new Object[]{snowWhite, robinHood, littleJohn, princeJohn,
				peterPan, captainHook});
		//size should be 3, snowWhite, princeJohn and peterPan
		assertTrue(p.size() == 3);
		assertTrue(p.getElementAt(0) == snowWhite);
		assertTrue(p.getElementAt(1) == princeJohn);
		assertTrue(p.getElementAt(2) == peterPan);
		//clear out the progression. Add the name filter back in
		p.removeAll();
		p.addFilter(nameFilter);
		//add the items
		p.addAll(new Object[]{snowWhite, robinHood, littleJohn, princeJohn,
				peterPan, captainHook});
		//size should be 2, snowWhite and peterPan
		assertTrue(p.size() == 2);
		assertTrue(p.getElementAt(0) == snowWhite);
		assertTrue(p.getElementAt(1) == peterPan);
	}
}
