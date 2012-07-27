package org.jdesktop.swingx.demo;/*
 * $Id: PersonInst.java 1959 2007-11-21 11:11:40Z MiguelM $
 *
 * Copyright 2007 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */

import java.util.*;
import java.text.CollationKey;
import java.text.Collator;
import java.text.DateFormat;
import java.text.ParseException;

/**
 * <br>Created by IntelliJ IDEA.
 * <br>User: Miguel
 * <br>Date: Nov 15, 2007
 * <br>Time: 3:58:25 PM
 */
public class PersonInst implements Person {
	private String firstName;
	private String lastName;
	private Date birthday;
	private long bDayCal;
	private CollationKey firstNameKey;
	private CollationKey lastNameKey;
	private static Collator collatorInstance = Collator.getInstance();
	public final static DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.SHORT, Locale.US);
	PersonInst(String firstName, String lastName, String birthday) throws ParseException {
		setBirthday(dateFormat.parse(birthday));
		setFirstName(firstName);
		setLastName(lastName);
	}

	public String getFirstName() { return firstName; }
	public CollationKey getFirstNameKey() { return firstNameKey; }
	public void   setFirstName(String firstName) {
		this.firstName = firstName;
		firstNameKey = collatorInstance.getCollationKey(this.firstName);
	}
	public String getLastName() { return lastName; }
	public CollationKey getLastNameKey() { return lastNameKey; }
	public void   setLastName(String lastName) {
		this.lastName = lastName;
		lastNameKey = collatorInstance.getCollationKey(lastName);
	}
	public Date   getBirthday() { return birthday; }
	public void   setBirthday(Date birthday) {
		this.birthday = birthday;
		Calendar cal = Calendar.getInstance();
		cal.setTime(birthday);
		bDayCal = cal.getTimeInMillis();
	}
	public int    getAge() {
		Calendar now = new GregorianCalendar();
		Calendar bDay = new GregorianCalendar();
		bDay.setTime(birthday);
		int yearBorn = bDay.get(Calendar.YEAR);
		int thisYear = now.get(Calendar.YEAR);
		int yearDiff = thisYear - yearBorn;
		// set Calendar to this year's birthday.
		bDay.set(Calendar.YEAR, thisYear);
		if (bDay.getTime().after(now.getTime()))
			yearDiff--;
		return yearDiff;
	}
	public long getAgeComparator() { return bDayCal; }
}
