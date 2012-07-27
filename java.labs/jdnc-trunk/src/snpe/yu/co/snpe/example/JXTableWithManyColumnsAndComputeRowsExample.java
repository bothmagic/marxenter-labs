package yu.co.snpe.example;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JScrollPane;

import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.icon.ColumnControlIcon;
import org.jdesktop.swingx.table.ColumnFactory;

import yu.co.snpe.dbtable.core.DbTableColumnControlButton;
import yu.co.snpe.dbtable.core.DbTableColumnFactory;

public class JXTableWithManyColumnsAndComputeRowsExample extends JXTable {

	private static final long serialVersionUID = -8540173280723947625L;

	private JComponent columnControlButton;

	private ColumnFactory columnFactory;

	public JXTableWithManyColumnsAndComputeRowsExample(Object[][] data, String[] columnNames) {
		super(data,columnNames);
	}

	@Override
	public JComponent getColumnControl() {
		if (columnControlButton == null) {
			columnControlButton = new DbTableColumnControlButton(this,
					new ColumnControlIcon());
		}
		return columnControlButton;
	}

	@Override
	protected ColumnFactory getColumnFactory() {
		if (columnFactory == null) {
			columnFactory = DbTableColumnFactory.getInstance();
		}
		return columnFactory;
	}

	public static void main(String[] args) {
		JFrame frame = new JFrame("JXTable demo");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// from Java tutorial
		String[] columnNames = { "First Name", "Last Name", "Sport",
				"# of Years", "Vegetarian" };

		Object[][] data = {
				{ "Mary", "Campione", "Snowboarding", new Integer(5),
						new Boolean(false) },
				{ "Alison", "Huml", "Rowing", new Integer(3), new Boolean(true) },
				{ "Kathy", "Walrath", "Knitting", new Integer(2),
						new Boolean(false) },
				{ "Sharon", "Zakhour", "Speed reading", new Integer(20),
						new Boolean(true) },
				{ "Philip", "Milne", "Pool", new Integer(10),
						new Boolean(false) } };

		JXTable table = new JXTableWithManyColumnsAndComputeRowsExample(data, columnNames);
		table.setColumnControlVisible(true);
		JScrollPane pane = new JScrollPane(table);
		frame.add(pane);
		frame.pack();
		frame.setVisible(true);
	}

}

