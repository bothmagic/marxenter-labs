package datatablemodel;

import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JScrollPane;

import org.jdesktop.databuffer.DataColumn;
import org.jdesktop.databuffer.DataRow;
import org.jdesktop.databuffer.DataSet;
import org.jdesktop.databuffer.DataTable;
import org.jdesktop.swingx.JXTable;
 
public class TestJXTable {
 
	private Object[][] data = {
			{ "Mark", "Andrews", "Red", new Integer(2), Boolean.FALSE },
			{ "Tom", "Ball", "Blue", new Integer(99), Boolean.FALSE },
			{ "Alan", "Chung", "Green", new Integer(838), Boolean.FALSE },
			{ "Jeff", "Dinkins", "Turquois", new Integer(8), Boolean.FALSE },
			{ "Amy", "Fowler", "Yellow", new Integer(3), Boolean.TRUE },
			{ "Brian", "Gerhold", "Green", new Integer(0), Boolean.FALSE },
			{ "James", "Gosling", "Pink", new Integer(21), Boolean.FALSE },
			{ "David", "Karlton", "Red", new Integer(1), Boolean.FALSE },
			{ "Dave", "Kloba", "Yellow", new Integer(14), Boolean.TRUE },
			{ "Peter", "Korn", "Purple", new Integer(12), Boolean.FALSE },
			{ "Phil", "Milne", "Purple", new Integer(3), Boolean.FALSE },
			{ "Dave", "Moore", "Green", new Integer(88), Boolean.FALSE },
			{ "Hans", "Muller", "Maroon", new Integer(5), Boolean.FALSE },
			{ "Rick", "Levenson", "Blue", new Integer(2), Boolean.FALSE },
			{ "Tim", "Prinzing", "Blue", new Integer(22), Boolean.FALSE },
			{ "Chester", "Rose", "Black", new Integer(0), Boolean.FALSE },
			{ "Ray", "Ryan", "Gray", new Integer(77), Boolean.FALSE },
			{ "Georges", "Saab", "Red", new Integer(4), Boolean.FALSE },
			{ "Willie", "Walker", "Phthalo Blue", new Integer(4), Boolean.FALSE },
			{ "Kathy", "Walrath", "Blue", new Integer(8), Boolean.FALSE },
			{ "Arnaud", "Weber", "Green", new Integer(44), Boolean.FALSE } };
 
	private JXTable table = new JXTable();
	private DataTableModel dataModel;
	private DataTable dTable;
 
	public TestJXTable() {
		JFrame frame = new JFrame("JXTable");
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		init();
 
		JScrollPane scrollpane = new JScrollPane(table);
 
		scrollpane.setPreferredSize(new Dimension(700, 500));
		frame.getContentPane().add(scrollpane);
		frame.setLocation(200, 200);
		frame.pack();
		frame.setVisible(true);
	}
 
	public void init() {
		buildDataTable();
		dataModel = new DataTableModel(dTable, "lastname", "firstname", "color",
						"vegetarian", "number");
		table.setModel(dataModel);
		table.setColumnControlVisible(true);
	}
 
	private void buildDataTable() {
		dTable = new DataTable(new DataSet("ds"), "datatable");
		DataColumn col = dTable.createColumn(String.class, "firstname");
		col.setKeyColumn(true);
		col = dTable.createColumn(String.class, "lastname");
		col.setKeyColumn(true);
		dTable.createColumn(String.class, "color");
		dTable.createColumn(Integer.class, "number");
		dTable.createColumn(Boolean.class, "vegetarian");
		
		for (Object[] line : data) {
			DataRow row = dTable.appendRowNoEvent();
			row.setValue("firstname", line[0]);
			row.setValue("lastname", line[1]);
			row.setValue("color", line[2]);
			row.setValue("number", line[3]);
			row.setValue("vegetarian", line[4]);
		}
	}
 
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new TestJXTable();
	}
}
