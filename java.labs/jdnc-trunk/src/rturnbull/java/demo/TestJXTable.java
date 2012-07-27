package demo;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.decorator.ColorHighlighter;
import org.jdesktop.swingx.decorator.ComponentAdapter;
import org.jdesktop.swingx.decorator.HighlightPredicate;
import org.jdesktop.swingx.decorator.HighlighterFactory;
import org.jdesktop.swingx.table.JXTableRowSorter;

public class TestJXTable {

	// Take the dummy data from SwingSet.
	private String[]									names	=
																		{
			"First Name", "Last Name", "Favorite Color", "Favorite Number",
			"Vegetarian"												};

	private Object[][]									data	=
																		{
			{ "Mark", "Andrews", "Red", new Integer(2), Boolean.FALSE },
			{ "Tom", "Ball", "Blue", new Integer(99), Boolean.FALSE },
			{ "Alan", "Chung", "Green", new Integer(838), Boolean.FALSE },
			{ "Jeff", "Dinkins", "Turquois", new Integer(8), Boolean.FALSE },
			{ "Amy", "Fowler", "Yellow", new Integer(3), Boolean.FALSE },
			{ "Brian", "Gerhold", "Green", new Integer(0), Boolean.TRUE },
			{ "James", "Gosling", "Pink", new Integer(21), Boolean.FALSE },
			{ "David", "Karlton", "Red", new Integer(1), Boolean.FALSE },
			{ "Dave", "Kloba", "Yellow", new Integer(14), Boolean.FALSE },
			{ "Peter", "Korn", "Purple", new Integer(12), Boolean.FALSE },
			{ "Phil", "Milne", "Purple", new Integer(3), Boolean.TRUE },
			{ "Dave", "Moore", "Green", new Integer(88), Boolean.FALSE },
			{ "Hans", "Muller", "Maroon", new Integer(5), Boolean.FALSE },
			{ "Rick", "Levenson", "Blue", new Integer(2), Boolean.FALSE },
			{ "Tim", "Prinzing", "Blue", new Integer(22), Boolean.FALSE },
			{ "Chester", "Rose", "Black", new Integer(0), Boolean.FALSE },
			{ "Ray", "Ryan", "Gray", new Integer(77), Boolean.FALSE },
			{ "Georges", "Saab", "Red", new Integer(4), Boolean.FALSE },
			{ "Willie", "Walker", "Phthalo Blue", new Integer(4), Boolean.FALSE },
			{ "Kathy", "Walrath", "Blue", new Integer(8), Boolean.FALSE },
			{ "Leonard", "Wilson", "Green", new Integer(44), Boolean.FALSE } };
	
	private JFrame										frame;
	private JScrollPane									scrollpane;
	private JXTable										jxTable;
	private JTable										jTable;
	private AbstractTableModel							dataModel;
	private Action										jAction;
	private Action										jxAction;
	private Action										jxsAction;
	private TableRowSorter<? extends TableModel>		sorter;
	private ButtonGroup									bg2;
	private Action										filterAction;
	private JTextField									filter;
	private JRadioButton								noFilter;
	private JLabel										msg;

	public TestJXTable() {
		frame = new JFrame();
		frame.addWindowListener(new WindowAdapter() {

			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		init();
		buildActions();

		ButtonGroup bg1 = new ButtonGroup();
		JRadioButton b = new JRadioButton(jAction);
		JRadioButton bx = new JRadioButton(jxAction);
		bg1.add(b);
		bg1.add(bx);
		bx.setSelected(true);
		JPanel p1 = new JPanel();
		p1.add(b);
		p1.add(bx);
		frame.getContentPane().add(p1, BorderLayout.NORTH);

		scrollpane = new JScrollPane();
		scrollpane.setPreferredSize(new Dimension(500, 500));
		frame.getContentPane().add(scrollpane, BorderLayout.WEST);
		jxTableChosen();

		JPanel p2 = new JPanel(new BorderLayout());
		JPanel p2a = new JPanel();
		msg = new JLabel("Favourite Number Column is not sortable");
		p2.add(msg, BorderLayout.NORTH);
		bg2 = new ButtonGroup();
		for (int i = 0; i < names.length; i++) {
			String name = names[i];
			JRadioButton b2 = new JRadioButton();
			bg2.add(b2);
			p2a.add(b2);
			if (name.equals(names[4])) {
				b2.setAction(new AbstractAction(name) {

					@Override
					public void actionPerformed(ActionEvent arg0) {
						msg.setText("Enter 'True' or 'False'");
					}
				});
			} else if (name.equals(names[3])) {
				b2.setAction(new AbstractAction(name) {

					@Override
					public void actionPerformed(ActionEvent arg0) {
						msg.setText("Enter an Integer");
					}
				});
			} else {
				b2.setAction(new AbstractAction(name) {

					@Override
					public void actionPerformed(ActionEvent arg0) {
						msg.setText("Enter a Regular Expression");
					}
				});
			}
			b2.getModel().setActionCommand(name);
		}
		noFilter = new JRadioButton();
		noFilter.setAction(new AbstractAction("None") {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				msg.setText(" ");
			}
		});
		noFilter.getModel().setActionCommand("None");
		noFilter.setSelected(true);
		bg2.add(noFilter);
		p2a.add(noFilter);
		p2a.add(new JLabel("Value"));
		filter = new JTextField(20);
		p2a.add(filter);
		p2.add(p2a, BorderLayout.CENTER);
		JPanel p2b = new JPanel();
		p2b.add(new JButton(filterAction));
		p2.add(p2b, BorderLayout.SOUTH);
		frame.getContentPane().add(p2, BorderLayout.SOUTH);

		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	private void init() {

		// Create a model of the data.
		dataModel = new AbstractTableModel() {

			// These methods always need to be implemented.
			public int getColumnCount() {
				return names.length;
			}

			public int getRowCount() {
				return data.length;
			}

			public Object getValueAt(int row, int col) {
				return data[row][col];
			}

			// The default implementations of these methods in
			// AbstractTableModel would work, but we can refine them.
			public String getColumnName(int column) {
				return names[column];
			}

			@SuppressWarnings("unchecked")
			public Class getColumnClass(int col) {
				return getValueAt(0, col).getClass();
			}

			public boolean isCellEditable(int row, int col) {
				return (col == 4);
			}

			public void setValueAt(Object aValue, int row, int column) {
				data[row][column] = aValue;
				fireTableRowsUpdated(row, row);
			}
		};
		
	}

	private void initJTable() {
		jTable = new JTable();
		jTable.setModel(dataModel);
		jTable.setAutoCreateRowSorter(true);
		sorter = (TableRowSorter<? extends TableModel>) jTable.getRowSorter();
		sorter.setSortable(3, false);
	}

	@SuppressWarnings("unchecked")
	private void initJXTable() {
		jxTable = new JXTable();
		jxTable.setModel(dataModel);
		jxTable.getColumnExt(3).setSortable(false);
		jxTable.setAutoCreateRowSorter(true);
		JXTableRowSorter sorter1 = (JXTableRowSorter)jxTable.getRowSorter();
		sorter = (TableRowSorter)sorter1;
		sorter1.setSortsOnUpdates(true);
		sorter1.setToggleToUnsorted(true);
		jxTable.setColumnControlVisible(true);
		jxTable.setHorizontalScrollEnabled(true);
		HighlightPredicate predicate = new HighlightPredicate() {

			public boolean isHighlighted(Component renderer,
					ComponentAdapter adapter) {
				return ((Boolean) adapter.getFilteredValueAt(adapter.row, 4))
						.booleanValue();
			}
		};

		ColorHighlighter hl = new ColorHighlighter(predicate, null, Color.RED);

		jxTable.addHighlighter(HighlighterFactory
				.createSimpleStriping(HighlighterFactory.BEIGE));
		jxTable.addHighlighter(hl);
	}
	
	private void buildActions() {
		jAction = new AbstractAction("JTable") {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				frame.setTitle("JTable");
				initJTable();
				scrollpane.setViewportView(jTable);
			}

		};
		jxAction = new AbstractAction("JXTable") {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				jxTableChosen();
				noFilter.setSelected(true);
				filter.setText(" ");
			}

		};
		filterAction = new AbstractAction("Filter") {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				setFilter();
			}

		};
	}

	private void jxTableChosen() {
		frame.setTitle("JXTable");
		initJXTable();
		scrollpane.setViewportView(jxTable);
	}

	private void setFilter() {
		msg.setText(" ");
		String name = bg2.getSelection().getActionCommand();
		if (name.equals("None")) {
			applyFilter(null);
			return;
		}
		int col = -1;
		for (int i = 0; i < names.length; i++) {
			if (name.equals(names[i])) {
				col = i;
				break;
			}
		}
		if (col == -1) {
			msg.setText("Invalid column selected - " + name);
			return;
		}
		String value = filter.getText().trim();
		if (value.equals("")) {
			msg.setText("Filter value must be entered");
			return;
		}
		if (col <= 2) {
			applyFilter(RowFilter.regexFilter(value, col));
			return;
		}
		if (col == 3) {
			Integer v;
			try {
				v = new Integer(value);
			} catch (NumberFormatException e) {
				msg.setText("Input '" + value + "' is not an Integer");
				return;
			}
			applyFilter(RowFilter.numberFilter(
					RowFilter.ComparisonType.EQUAL, v, col));
			return;
		}
		if (col == 4) {
			if (!(value.equals("True") || value.equals("False"))) {
				msg.setText("Value must be 'True' or 'False'");
				return;
			}
			boolean option = true;
			if (value.equals("False")) {
				option = false;
			}
			applyFilter(new LogicFilter(option, col));
		}
	}
	
	@SuppressWarnings("unchecked")
	private void applyFilter(RowFilter filter) {
		sorter.setRowFilter(filter);
	}

	private class LogicFilter extends RowFilter<TableModel, Integer> {

		private boolean	option;
		private int		column;

		protected LogicFilter(boolean option, int column) {
			super();
			this.option = option;
			this.column = column;
		}

		/**
		 * @see javax.swing.RowFilter#include(javax.swing.RowFilter.Entry)
		 */
		@Override
		public boolean include(
				javax.swing.RowFilter.Entry<? extends TableModel, ? extends Integer> entry) {
			boolean value = (Boolean) entry.getValue(column);
			if (value == option) { return true; }
			return false;
		}

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new TestJXTable();
	}

}
