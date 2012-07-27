/*
 * $Id: PeoplePropTableModel.java 2031 2007-12-11 11:02:48Z MiguelM $
 *
 * Copyright 2007 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.demo;

import java.util.*;
import org.jdesktop.swingx.table.SeparatedTableModel;
import org.jdesktop.swingx.table.PropertyColumn;

/**
 * <br>Created by IntelliJ IDEA.
 * <br>User: Miguel
 * <br>Date: Dec 10, 2007
 * <br>Time: 3:40:01 PM
 */
public class PeoplePropTableModel extends SeparatedTableModel<Person>
				implements PeopleDemo.TableSource
{
	@SuppressWarnings({"HardCodedStringLiteral", "MagicNumber"})
	PeoplePropTableModel(Person[] rows) {
		super(rows);
		addColumn(new PropertyColumn<Person, String>(
						"firstName", "First Name", Person.class, String.class, 100 ));
		addColumn(new PropertyColumn<Person, String>(
						"lastName", "LastName", Person.class, String.class, 100 ));
		addColumn(new PropertyColumn<Person, Date>(
						"birthday", "Birthday", Person.class, Date.class, 90,
						null, new PeopleTableModel.DateEditor(), true, false, false ));
		addColumn(new PropertyColumn<Person, Integer>(
						"age", "Age", Person.class, Integer.TYPE, 40 ));
	}

	@SuppressWarnings({"HardCodedStringLiteral", "HardcodedLineSeparator", "StringConcatenation", "MagicCharacter"})
	public String getSource() {
		return "public class PeoplePropTableModel extends SeparatedTableModel<Person>\n" +
						"{\n" +
						"\tPeoplePropTableModel(Person[] rows) {\n" +
						"\t\tsuper(rows);\n" +
						"\t\taddColumn(new PropertyColumn<Person, String>(\n" +
						"\t\t\t\t\t\t\"firstName\", \"First Name\", Person.class, String.class, 100 ));\n" +
						"\t\taddColumn(new PropertyColumn<Person, String>(\n" +
						"\t\t\t\t\t\t\"lastName\", \"LastName\", Person.class, String.class, 100 ));\n" +
						"\t\taddColumn(new PropertyColumn<Person, Date>(\n" +
						"\t\t\t\t\t\t\"birthday\", \"Birthday\", Person.class, Date.class, 90,\n" +
						"\t\t\t\t\t\tnull, new PeopleTableModel.DateEditor(), true, false, false ));\n" +
						"\t\taddColumn(new PropertyColumn<Person, Integer>(\n" +
						"\t\t\t\t\t\t\"age\", \"Age\", Person.class, Integer.TYPE, 40 ));\t}\n" +
						'}';
	}
}
