package org.jdesktop.swingx.demo;/*
 * $Id: PeopleTableModel.java 2031 2007-12-11 11:02:48Z MiguelM $
 *
 * Copyright 2007 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */

import java.util.*;
import java.awt.Component;
import java.text.DateFormat;
import java.text.ParseException;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.*;
import org.jdesktop.swingx.table.SeparatedTableModel;
import org.jdesktop.swingx.table.AbstractColumn;

/**
 * <br>Created by IntelliJ IDEA.
 * <br>User: Miguel
 * <br>Date: Nov 15, 2007
 * <br>Time: 4:00:15 PM
 */
@SuppressWarnings({"HardCodedStringLiteral", "HardcodedLineSeparator", "StringConcatenation", "HardcodedFileSeparator", "MagicNumber", "MagicCharacter"})
public class PeopleTableModel extends SeparatedTableModel<Person>
	implements PeopleDemo.TableSource
{
	// The parameter can be an array or a List:
	PeopleTableModel(Person[] rows) {
		super(rows);
		addColumn(new DemoFirstNameColumn());
		addColumn(new DemoLastNameColumn());
		addColumn(new DemoBirthdayColumn());
		addColumn(new DemoAgeColumn());
	}

	/** <br>
	 * Note the following. <p style="margin-left:.25in;text-indent:-.25in">
	 * 1) The constructor passes true for the <code>isEditable</code> parameter. For the column to be 
	 * editable, this must be true and the <code>setValue()</code> must be overridden. The 
	 * <code>isEditable</code> property is false by default.
	 * <p style="margin-left:.25in;text-indent:-.25in">
	 * 2) The method <code>People.getFirstNameKey()</code> returns a <code>java.text.CollationKey</code>. 
	 * This makes for faster sorting when the <code>CollationKey</code>s are generated in advance. 
	 * <p style="margin-left:.25in;text-indent:-.25in">
	 * 3) The <code>getComparableValue()</code> method returns an instance of 
	 * <code>Comparable&lt;?&gt;</code>. By default, it returns the same thing as getValue(), unless 
	 * <code>getValue()</code>'s return value doesn't implements <code>Comparable&lt;?&gt;</code>. In 
	 * that case, <code>getComparableValue()</code> converts the value to a <code>String</code> and 
	 * returns that. Although Sorting <code>String</code>s normally fails to mix upper and lower case 
	 * values properly, the <code>TableRowKeyedSorter</code> uses a <code>Comparator</code> that 
	 * sorts upper and lower case properly. See the Last Name Column for an example.
	 * <p style="margin-left:.25in;text-indent:-.25in">
	 * 4) One of the names in the list uses all lower case letters to confirm that the sorting is 
	 * case insensitive. <br>
	 **/
	public static class FirstNameColumn extends AbstractColumn<Person, String> {
		FirstNameColumn() { 
			super(
					"First Name",   // Column title 
					String.class,   // Column class
					100,            // Column width
					true            // isEditable
			);
		}
		public String getValue(Person row) { return row.getFirstName(); }
		public void setValue(String firstName, Person row) { row.setFirstName(firstName); }
		// This method returns a pre-generated CollationKey for fast sorting.
		public Comparable<?> getComparableValue(Person row) { return row.getFirstNameKey(); }
	}
	/** <br>
	 * Note the following. 
	 * <p style="margin-left:.25in;text-indent:-.25in">
	 * 1) The constructor passes true for the <code>isEditable</code> parameter. For the column to be 
	 * editable, this must be true and the <code>setValue()</code> must be overridden. The 
	 * <code>isEditable</code> property is false by default.
	 * <p style="margin-left:.25in;text-indent:-.25in">
	 * 2) Unlike the FirstNameColumn class, this class doesn't implement <code>getComparableValue()</code>. 
	 * Instead it simply uses the default sorting for Strings. However, the <code>TableRowKeyedSorter</code>
	 * class doesn't simply compare Strings, because that would produce a sort that fails to account properly 
	 * for upper and lower case letters. Instead, the sorter uses a special comparator that works fast, but 
	 * properly compares upper and lower case letters. So <code>CollationKey</code>s aren't needed to get 
	 * case-insensitive sorting, although using pre-generated <code>CollationKey</code>s would still produce 
	 * a faster sort, as the FirstNameColumn does. See the FirstNameColumn for an example.
	 * <p style="margin-left:.25in;text-indent:-.25in">
	 * 3) One of the names in the list uses all lower case letters to confirm that the sorting is 
	 * case insensitive. <br>
	 **/
	private static class LastNameColumn extends AbstractColumn<Person, String> {
		LastNameColumn() { 
			super(
					"Last Name",    // Column title
					String.class,   // Column class
					100,            // Column width
					true            // Is Editable
			);
		}
		public String getValue(Person row) { return row.getLastName(); }
		public void setValue(String lastName, Person row) { row.setLastName(lastName); }
	}
	/** <br>
	 * Note the following. <p style="margin-left:.25in;text-indent:-.25in">
	 * 1) The constructor passes true for the <code>isEditable</code> parameter. For the column to be 
	 * editable, this must be true and the <code>setValue()</code> must be overridden. The 
	 * <code>isEditable</code> property is false by default.
	 * <p style="margin-left:.25in;text-indent:-.25in">
	 * 2) This class does not override <code>getComparableValue()</code> because <code>getValue()</code>
	 * returns a <code>Date</code> instance, which implements <code>Comparable&lt;?&gt;</code>. So 
	 * the default implementation of <code>getComparableValue()</code> will return a Date, which will
	 * produce a valid sort.
	 * <p style="margin-left:.25in;text-indent:-.25in">
	 * 3) This class also supplies a custom renderer and editor for this column. For these to be used, 
	 * the developer must call <code>SeparatedTableModel.setUpHeader()</code> after installing the 
	 * <code>TableModel</code> into the <code>JTable</code>. This is because the renderer isn't used
	 * by the table, it's used by the <code>javax.swing.table.TableColumnModel</code> class.
	 * <p style="margin-left:.25in;text-indent:-.25in">
	 * 4) This class overrides <code>getUpdateRow()</code> to return true instead of false. This 
	 * forces the table to redraw the row when the birthday field changes, in order to also update
	 * the Age column. Try modifying a birth year to see this. There are other "update" methods 
	 * that may also be overridden for more flexibility. <br>
	 **/
	private static class BirthdayColumn extends AbstractColumn<Person, Date> {
		BirthdayColumn() { 
			super(
					"Birthday",           // Column title 
					Date.class,           // Column class
					90,                   // Column width
					null,   // Column renderer
					new DateEditor()      // Column cell editor
			);
		}
		public Date getValue(Person row) { return row.getBirthday(); }
		public void setValue(Date value, Person row) { row.setBirthday(value); }
		public boolean getUpdateRow(Person pRow) { return true; }
	}
	/** <br>
	 * Note the following. 
	 * <p style="margin-left:.25in;text-indent:-.25in">
	 * 1) The <code>isEditable()</code> method is not overridden, and no <code>setValue()</code> method
	 * exists because this column is not editable. Instead, the age is calculated from the information
	 * in the Birthday column.
	 * <p style="margin-left:.25in;text-indent:-.25in">
	 * 2) This class overrides <code>getComparableValue()</code> to return a Long value that produces a
	 * valid sort by age. Since Long implements <code>Comparable&lt;?&gt;</code>, it is a valid return 
	 * type. This produces a sort-by-age that correctly sorts people who are same age in years.
	 * The point here is to illustrate how you can easily customize the sorting of any column.<br>
	 **/
	private static class AgeColumn extends AbstractColumn<Person, Integer> {
		AgeColumn() {
			super(
					"Age",          // Column title 
					Integer.class,  // Column class
					40              // Column width
			);
		}
		public Integer getValue(Person row) { return row.getAge(); }
		public Comparable<?> getComparableValue(Person row) { return -row.getAgeComparator(); }
	}
	
	public final static DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.US);
	public static class DateEditor extends DefaultCellEditor {

		public DateEditor() { super(new JTextField()); }

		@Override
		public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
			String editorValue = dateFormat.format((Date) value);
			return super.getTableCellEditorComponent(table, editorValue, isSelected, row, column);
		}

		@Override
		public Object getCellEditorValue() {
			String sValue = super.getCellEditorValue().toString();
			try {
				return dateFormat.parseObject(sValue);
			} catch (ParseException e) {
				return new Date();
			}
		}
	}
	private static class DateRenderer extends DefaultTableCellRenderer {

		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			String formatted = dateFormat.format((Date) value);
			return super.getTableCellRendererComponent(
							table, 
							formatted, 
							isSelected, 
							hasFocus, 
							row, 
							column
			);
		}
	}
	
	private class DemoFirstNameColumn extends FirstNameColumn implements DemoColumn {
		public String getCommentText() {
			return "\t/** <br>\n" +
							"\t * Note the following. <p style=\"margin-left:.25in;text-indent:-.25in\">\n" +
							"\t * 1) The constructor passes true for the <code>isEditable</code> parameter. For the column to be \n" +
							"\t * editable, this must be true and the <code>setValue()</code> must be overridden. The \n" +
							"\t * <code>isEditable</code> property is false by default.\n" +
							"\t * <p style=\"margin-left:.25in;text-indent:-.25in\">\n" +
							"\t * 2) The method <code>People.getFirstNameKey()</code> returns a <code>java.text.CollationKey</code>. \n" +
							"\t * This makes for faster sorting when the <code>CollationKey</code>s are generated in advance. \n" +
							"\t * <p style=\"margin-left:.25in;text-indent:-.25in\">\n" +
							"\t * 3) The <code>getComparableValue()</code> method returns an instance of \n" +
							"\t * <code>Comparable&lt;?&gt;</code>. By default, it returns the same thing as getValue(), unless \n" +
							"\t * <code>getValue()</code>'s return value doesn't implements <code>Comparable&lt;?&gt;</code>. In \n" +
							"\t * that case, <code>getComparableValue()</code> converts the value to a <code>String</code> and \n" +
							"\t * returns that. Although Sorting <code>String</code>s normally fails to mix upper and lower case \n" +
							"\t * values properly, the <code>TableRowKeyedSorter</code> uses a <code>Comparator</code> that \n" +
							"\t * sorts upper and lower case properly. See the Last Name Column for an example.\n" +
							"\t * <p style=\"margin-left:.25in;text-indent:-.25in\">\n" +
							"\t * 4) One of the names in the list uses all lower case letters to confirm that the sorting is \n" +
							"\t * case insensitive. <br>\n" +
							"\t **/";
		}
		public String getCodeText() {
			return "\tpublic static class FirstNameColumn extends AbstractColumn<Person, String> {\n" +
							"\t\tFirstNameColumn() { \n" +
							"\t\t\tsuper(\n" +
							"\t\t\t\t\t\"First Name\",   // Column title \n" +
							"\t\t\t\t\tString.class,   // Column class\n" +
							"\t\t\t\t\t100,            // Column width\n" +
							"\t\t\t\t\ttrue            // isEditable\n" +
							"\t\t\t);\n" +
							"\t\t}\n" +
							"\t\tpublic String getValue(Person row) { return row.getFirstName(); }\n" +
							"\t\tpublic void setValue(String firstName, Person row) { row.setFirstName(firstName); }\n" +
							"\t\t// This method returns a pre-generated CollationKey for fast sorting.\n" +
							"\t\tpublic Comparable<?> getComparableValue(Person row) { return row.getFirstNameKey(); }\n" +
							"\t}";
		}
	}
	
	private class DemoLastNameColumn extends LastNameColumn implements DemoColumn {
		public String getCommentText() {
			return "\t/** <br>\n" +
							"\t * Note the following. \n" +
							"\t * <p style=\"margin-left:.25in;text-indent:-.25in\">\n" +
							"\t * 1) The constructor passes true for the <code>isEditable</code> parameter. For the column to be \n" +
							"\t * editable, this must be true and the <code>setValue()</code> must be overridden. The \n" +
							"\t * <code>isEditable</code> property is false by default.\n" +
							"\t * <p style=\"margin-left:.25in;text-indent:-.25in\">\n" +
							"\t * 2) Unlike the FirstNameColumn class, this class doesn't implement <code>getComparableValue()</code>. \n" +
							"\t * Instead it simply uses the default sorting for Strings. However, the <code>TableRowKeyedSorter</code>\n" +
							"\t * class doesn't simply compare Strings, because that would produce a sort that fails to account properly \n" +
							"\t * for upper and lower case letters. Instead, the sorter uses a special comparator that works fast, but \n" +
							"\t * properly compares upper and lower case letters. So <code>CollationKey</code>s aren't needed to get \n" +
							"\t * case-insensitive sorting, although using pre-generated <code>CollationKey</code>s would still produce \n" +
							"\t * a faster sort, as the FirstNameColumn does. See the FirstNameColumn for an example.\n" +
							"\t * <p style=\"margin-left:.25in;text-indent:-.25in\">\n" +
							"\t * 3) One of the names in the list uses all lower case letters to confirm that the sorting is \n" +
							"\t * case insensitive. <br>\n" +
							"\t **/";
		}
		public String getCodeText() {
			return "\tprivate static class LastNameColumn extends AbstractColumn<Person, String> {\n" +
							"\t\tLastNameColumn() { \n" +
							"\t\t\tsuper(\n" +
							"\t\t\t\t\t\"Last Name\",    // Column title\n" +
							"\t\t\t\t\tString.class,   // Column class\n" +
							"\t\t\t\t\t100,            // Column width\n" +
							"\t\t\t\t\ttrue            // Is Editable\n" +
							"\t\t\t);\n" +
							"\t\t}\n" +
							"\t\tpublic String getValue(Person row) { return row.getLastName(); }\n" +
							"\t\tpublic void setValue(String lastName, Person row) { row.setLastName(lastName); }\n" +
							"\t}";
		}
	}
	
	private class DemoBirthdayColumn extends BirthdayColumn implements DemoColumn {
		public String getCommentText() {
			return "\t/** <br>\n" +
							"\t * Note the following. <p style=\"margin-left:.25in;text-indent:-.25in\">\n" +
							"\t * 1) The constructor passes true for the <code>isEditable</code> parameter. For the column to be \n" +
							"\t * editable, this must be true and the <code>setValue()</code> must be overridden. The \n" +
							"\t * <code>isEditable</code> property is false by default.\n" +
							"\t * <p style=\"margin-left:.25in;text-indent:-.25in\">\n" +
							"\t * 2) This class does not override <code>getComparableValue()</code> because <code>getValue()</code>\n" +
							"\t * returns a <code>Date</code> instance, which implements <code>Comparable&lt;?&gt;</code>. So \n" +
							"\t * the default implementation of <code>getComparableValue()</code> will return a Date, which will\n" +
							"\t * produce a valid sort.\n" +
							"\t * <p style=\"margin-left:.25in;text-indent:-.25in\">\n" +
							"\t * 3) This class also supplies a custom renderer and editor for this column. For these to be used, \n" +
							"\t * the developer must call <code>SeparatedTableModel.setUpHeader()</code> after installing the \n" +
							"\t * <code>TableModel</code> into the <code>JTable</code>. This is because the renderer isn't used\n" +
							"\t * by the table, it's used by the <code>javax.swing.table.TableColumnModel</code> class.\n" +
							"\t * <p style=\"margin-left:.25in;text-indent:-.25in\">\n" +
							"\t * 4) This class overrides <code>getUpdateRow()</code> to return true instead of false. This \n" +
							"\t * forces the table to redraw the row when the birthday field changes, in order to also update\n" +
							"\t * the Age column. Try modifying a birth year to see this. There are other \"update\" methods \n" +
							"\t * that may also be overridden for more flexibility. <br>\n" +
							"\t **/";
		}
		public String getCodeText() {
			return "\tprivate static class BirthdayColumn extends AbstractColumn<Person, Date> {\n" +
							"\t\tBirthdayColumn() { \n" +
							"\t\t\tsuper(\n" +
							"\t\t\t\t\t\"Birthday\",           // Column title \n" +
							"\t\t\t\t\tDate.class,           // Column class\n" +
							"\t\t\t\t\t90,                   // Column width\n" +
							"\t\t\t\t\tnew DateRenderer(),   // Column renderer\n" +
							"\t\t\t\t\tnew DateEditor()      // Column cell editor\n" +
							"\t\t\t);\n" +
							"\t\t}\n" +
							"\t\tpublic Date getValue(Person row) { return row.getBirthday(); }\n" +
							"\t\tpublic void setValue(Date value, Person row) { row.setBirthday(value); }\n" +
							"\t\tpublic boolean getUpdateRow(Person pRow) { return true; }\n" +
							"\t}";
		}
	}
	
	private class DemoAgeColumn extends AgeColumn implements DemoColumn {
		public String getCommentText() {
			return "\t/** <br>\n" +
							"\t * Note the following. \n" +
							"\t * <p style=\"margin-left:.25in;text-indent:-.25in\">\n" +
							"\t * 1) The <code>isEditable()</code> method is not overridden, and no <code>setValue()</code> method\n" +
							"\t * exists because this column is not editable. Instead, the age is calculated from the information\n" +
							"\t * in the Birthday column.\n" +
							"\t * <p style=\"margin-left:.25in;text-indent:-.25in\">\n" +
							"\t * 2) This class overrides <code>getComparableValue()</code> to return a Long value that produces a\n" +
							"\t * valid sort by age. Since Long implements <code>Comparable&lt;?&gt;</code>, it is a valid return \n" +
							"\t * type. This produces a sort-by-age that correctly sorts people who are same age in years.\n" +
							"\t * The point here is to illustrate how you can easily customize the sorting of any column.<br>\n" +
							"\t **/";
		}
		public String getCodeText() {
			return "\tprivate static class AgeColumn extends AbstractColumn<Person, Integer> {\n" +
							"\t\tAgeColumn() {\n" +
							"\t\t\tsuper(\n" +
							"\t\t\t\t\t\"Age\",          // Column title \n" +
							"\t\t\t\t\tInteger.class,  // Column class\n" +
							"\t\t\t\t\t40              // Column width\n" +
							"\t\t\t);\n" +
							"\t\t}\n" +
							"\t\tpublic Integer getValue(Person row) { return row.getAge(); }\n" +
							"\t\tpublic Comparable<?> getComparableValue(Person row) { return -row.getAgeComparator(); }\n" +
							"\t}";
		}
	}

	interface DemoColumn {
		public String getCommentText();
		public String getCodeText();
	}
	
	public String getSource() {
		return "public class PeopleTableModel extends SeparatedTableModel<Person> {\n\n" +
						"\t// The parameter can be an array or a List:\n" +
						"\tPeopleTableModel(Person[] rows) {\n" +
						"\t\tsuper(rows);\n" +
						"\t\taddColumn(new FirstNameColumn());\n" +
						"\t\taddColumn(new LastNameColumn());\n" +
						"\t\taddColumn(new BirthdayColumn());\n" +
						"\t\taddColumn(new AgeColumn());\n" +
						"\t}\n" +
						"\t/* Column classes may be added as inner classes here.\n\n" +
						"\tControl-click on the column headers to see the code for each column class. */\n" +
						"}\n";
	}
}
