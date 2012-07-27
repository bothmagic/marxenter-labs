/*
 * $Id: TableSortTest.java 1939 2007-11-20 07:25:26Z MiguelM $
 *
 * Copyright 2007 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */

import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.text.CollationKey;
import java.text.Collator;
import javax.swing.*;
import javax.swing.table.TableModel;

import org.jdesktop.swingx.table.SeparatedTableModel;
import org.jdesktop.swingx.table.AbstractColumn;
import org.jdesktop.swingx.table.TableRowKeyedSorter;
import org.jdesktop.swingx.table.TableRowHeader;

/**
 * @author Miguel Mu\u00f1oz
 */
@SuppressWarnings({"HardCodedStringLiteral", "MagicNumber"})
public class TableSortTest extends JPanel {
	public static void main(String[] args) {
		JFrame mainFrame = new JFrame("Table Sort Test"); // NON-NLS
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.add(new TableSortTest());
		//noinspection MagicNumber
		mainFrame.setBounds(10, 10, 650, 402);
		mainFrame.setVisible(true);
	}


	@SuppressWarnings({"unchecked"})
	public TableSortTest() {
		setLayout(new BorderLayout());
		ShowNumber[] numbers = {
						new ShowNumber(1, "One"),
						new ShowNumber(2, "two"),
						new ShowNumber(3, "Three"),
						new ShowNumber(4, "four"),
						new ShowNumber(5, "Five"),
						new ShowNumber(6, "six"),
						new ShowNumber(7, "Seven"),
						new ShowNumber(8, "eight"),
						new ShowNumber(9, "Nine"),
						new ShowNumber(10, "ten"),
		};
		SeparatedTableModel model = new SeparatedTableModel<ShowNumber>(numbers) {

			public Object getRowName(int ii) {

				return getColumn(1).getValue(getRows().getRow(ii));
			}
		};
		model.addColumn(new NumberColumn());
		model.addColumn(new NameColumn());
		model.addColumn(new SortingNameColumn());
		ComparatorNameColumn comparatorNameColumn = new ComparatorNameColumn();
		model.addColumn(comparatorNameColumn);
		model.addColumn(new NameKeyColumn());
//		int compColumnIndex = model.getColumnCount();
//		model.addColumn(new FactorialColumn());
		JTable table = new JTable(model);
//		table.setAutoCreateRowSorter(true);
//		DefaultRowSorter<? extends TableModel, Integer> sorter = (DefaultRowSorter<? extends TableModel, Integer>) table.getRowSorter();
//		sorter.setComparator(compColumnIndex, comparatorNameColumn.getCustomComparator());
		JScrollPane scroller = TableRowHeader.createScroller(table);

		SeparatedTableModel.setUpHeader(table);
		table.setRowSorter(new TableRowKeyedSorter(model));
		add(scroller, BorderLayout.CENTER);
//		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

		// instructions:
		//noinspection StringConcatenation
		String iText = "<html>This tests the following:<br><br>" +
						"Sorting by the getComparableValue() method:<br>" +
						"<nbsp><nbsp>The name column marked 'alphabetic sort' should sort the names " +
						"alphabetically, while the name column marked 'numeric sort' should behave " +
						"the same as the Number column. The Factorial column should sort the " +
						"numbers alphabetically. This means all numbers starting with a 1 should come" +
						"first, etc.<br><br>" +
						"Column minWidth and maxWidth:<br>" +
						"The name column marked 'alphabetic sort' has sharp limits on how narrow and " +
						"wide it can go. The limits are 20 points apart. All the other columns have no" +
						"limits.</html>";
		JLabel instructions = new JLabel(iText);
		add(instructions, BorderLayout.SOUTH);

		addButtonPanel(model, table);
	}

	private void addButtonPanel(final SeparatedTableModel<ShowNumber> model, 
	                            final JTable table
	) {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

		JButton fBtn = new JButton("Add Factorial Column");
		ActionListener al = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				model.addColumn(new FactorialColumn());
			}
		};
		fBtn.addActionListener(al);
		panel.add(fBtn);
		
		JButton rmBtn = new JButton("Remove last column");
		ActionListener alRm = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				model.removeColumn(model.getColumnCount()-1);
			}
		};
		rmBtn.addActionListener(alRm);
		panel.add(rmBtn);
		
		IntName[] resizeValues = {
						new IntName(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS, "AUTO_RESIZE_SUBSEQUENT_COLUMNS"),
						new IntName(JTable.AUTO_RESIZE_ALL_COLUMNS, "AUTO_RESIZE_ALL_COLUMNS"),
						new IntName(JTable.AUTO_RESIZE_LAST_COLUMN, "AUTO_RESIZE_LAST_COLUMN"),
						new IntName(JTable.AUTO_RESIZE_NEXT_COLUMN, "AUTO_RESIZE_NEXT_COLUMN"),
						new IntName(JTable.AUTO_RESIZE_OFF, "AUTO_RESIZE_OFF"),
		};

		final JComboBox autoResizeControl = new JComboBox(resizeValues);
		ActionListener resizeLsnr = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				IntName chosen = (IntName) autoResizeControl.getSelectedItem();
				table.setAutoResizeMode(chosen.mInt);
			}
		};
		autoResizeControl.addActionListener(resizeLsnr);
		panel.add(autoResizeControl);

		add(panel, BorderLayout.NORTH);
	}
	
	private class IntName {
		int mInt;
		String mName;
		private IntName(int ii, String ss) {
			mInt = ii;
			mName = ss;
		}
		@Override public String toString() { return mName; }
	}

	private class NumberColumn extends AbstractColumn<ShowNumber, Integer> {
		private NumberColumn() { super("Number", Integer.class, 40); }

		public Integer getValue(ShowNumber pRow) { return pRow.mNumber; }
	}

	private class NameColumn extends AbstractColumn<ShowNumber, String> {
		private NameColumn() {
			this("Name (alphabetic sort)");
			setMinWidth(getPreferredWidth() - 10);
			setMaxWidth(getPreferredWidth() + 10);
		}

		private NameColumn(String pName) { super(pName, String.class, 140); }

		public String getValue(ShowNumber pRow) { return pRow.mName; }
	}

	private class SortingNameColumn extends NameColumn {
		private SortingNameColumn() { super("Name (numeric sort)"); }

		@Override
		public Comparable<?> getComparableValue(ShowNumber pRow) { return pRow.mNumber; }
	}

	private class ComparatorNameColumn extends NameColumn {
		ComparatorNameColumn() { super("Name (String sort)"); }

//		private Comparator<ShowNumber> getCustomComparator() {
//			return new Comparator<ShowNumber>() {
//				public int compare(ShowNumber o1, ShowNumber o2) {
//					return o1.mNumber - o2.mNumber;
//				}
//			};
//		}

		@Override
		public Comparable<?> getComparableValue(ShowNumber pRow) {
			return pRow.mName;
		}
	}

	private class NameKeyColumn extends NameColumn {
		NameKeyColumn() { super("Name Key-Sort"); }

		@Override
		public Comparable<?> getComparableValue(ShowNumber pRow) { return pRow.mKey; }
	}

	private class FactorialColumn extends AbstractColumn<ShowNumber, String> {
		public FactorialColumn() { super("Factorial", Integer.class, 60); }

		public String getValue(ShowNumber pRow) { return Integer.toString(pRow.mFactorial); }
	}

	private class ShowNumber {
		private final int mNumber;
		private final String mName;
		private final int mFactorial;
		private final CollationKey mKey;

		private ShowNumber(int number, String name) {
			mNumber = number;
			mName = name;
			int fct = 1;
			for (int ii = 2; ii <= number; ++ii)
				fct *= ii;
			mFactorial = fct;
			mKey = Collator.getInstance().getCollationKey(mName);
		}
	}
}
