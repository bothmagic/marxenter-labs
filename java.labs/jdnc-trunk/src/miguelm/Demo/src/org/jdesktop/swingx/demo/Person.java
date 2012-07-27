package org.jdesktop.swingx.demo;/*
 * $Id: Person.java 1952 2007-11-21 08:41:44Z MiguelM $
 *
 * Copyright 2007 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */

import java.text.CollationKey;
import java.util.*;

/**
 * <br>Created by IntelliJ IDEA.
 * <br>User: Miguel
 * <br>Date: Nov 17, 2007
 * <br>Time: 9:53:08 AM
 */
public interface Person {
	public String getFirstName();
	public void setFirstName(String firstName);
	public CollationKey getFirstNameKey();
	public String getLastName();
	public void setLastName(String lastName);
	public Date getBirthday();
	public void setBirthday(Date birthday);
	public long getAgeComparator();
	public int getAge();
}
