package yu.co.snpe.dbtable.ext;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;

import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.icon.ColumnControlIcon;
import org.jdesktop.swingx.table.ColumnFactory;

import yu.co.snpe.dbtable.core.DbTableColumnFactory;
import yu.co.snpe.dbtable.ext.ScrollableCCB;

public class JXTableWithManyColumnsAndComputeRowsExample extends JXTable {

	private static final long serialVersionUID = -8540173280723947625L;

	private JComponent columnControlButton;

	private ColumnFactory columnFactory;

	public JXTableWithManyColumnsAndComputeRowsExample() {
	}

	
	@Override
    protected JComponent createDefaultColumnControl() {
        return new ScrollableCCB(this, new ColumnControlIcon());
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
		JXTable table = new JXTableWithManyColumnsAndComputeRowsExample();
		table.setColumnControlVisible(true);
                table.setModel(new DefaultTableModel(10, 35));
		JScrollPane pane = new JScrollPane(table);
		frame.add(pane);
		frame.pack();
		frame.setVisible(true);
	}

}

