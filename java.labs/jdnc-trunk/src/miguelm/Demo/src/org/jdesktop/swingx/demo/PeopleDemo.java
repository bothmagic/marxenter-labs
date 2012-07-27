package org.jdesktop.swingx.demo;/*
 * $Id: PeopleDemo.java 2031 2007-12-11 11:02:48Z MiguelM $
 *
 * Copyright 2007 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */

/*
 * Done: 1) Fix date editing or specify a date editor.
 * *No*: 2) Try putting the DateFormat into the StringConverter. This might help with editing.
 *          (Bad idea. The String converter is only used for sorting.);
 * *No*: 3) Add a "Party" field to demonstrate another editor.
 * Todo: 4) Experiment with multiple tables sharing the same model and header.
 * Done: 5) Try building a JDK 1.5 version. Can I build separate versions?
 *          Can they use the same code?
 *          Question: How much of the code compiles under JDK 1.5?
 * Todo: 6) Add support for sorting in another Locale.
 */
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.GridLayout;
import java.awt.Component;
import java.awt.event.MouseListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.text.ParseException;
import java.util.*;
import javax.swing.*;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;
import javax.swing.table.TableCellRenderer;
import org.jdesktop.swingx.table.SeparatedTableModel;
import org.jdesktop.swingx.table.TableRowKeyedSorter;
import org.jdesktop.swingx.table.AbstractColumn;

/**
 * <br>Created by IntelliJ IDEA.
 * <br>User: Miguel
 * <br>Date: Nov 16, 2007
 * <br>Time: 12:13:54 AM
 */
@SuppressWarnings({"MagicNumber", "HardCodedStringLiteral", "HardcodedLineSeparator", "MagicCharacter", "StringConcatenation"})
public class PeopleDemo extends JPanel {
	private static JFrame sMf;
	private static String jdkVersion = System.getProperty("java.version");
	private static boolean isJDK1_6 = jdkVersion.compareTo("1.6") >= 0;
	private JDialog mCodeDisplayDlg;
	private static String sPersonCode = "\n\n" +
					"\tpublic interface Person {\n" +
					"\t\tpublic String getFirstName();\n" +
					"\t\tpublic void setFirstName(String firstName);\n" +
					"\t\tpublic CollationKey getFirstNameKey();\n" +
					"\t\tpublic String getLastName();\n" +
					"\t\tpublic void setLastName(String lastName);\n" +
					"\t\tpublic Date getBirthday();\n" +
					"\t\tpublic void setBirthday(Date birthday);\n" +
					"\t\tpublic long getAgeComparator();\n" +
					"\t\tpublic int getAge();\n" +
					"\t}";

	public static void main(String[] args) throws ParseException {
		sMf = new JFrame("PeopleDemo");
		sMf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		sMf.add(new PeopleDemo(), BorderLayout.CENTER);
		sMf.setBounds(10, 10, 380, 625);
		sMf.setVisible(true);
	}
	
	private PeopleDemo() throws ParseException {
		super(new BorderLayout());
		JPanel grid = new JPanel(new GridLayout(0, 1));
		add(grid, BorderLayout.CENTER);
		makeStandardModel(grid);
		makePropertyModel(grid);
		
		// Add header info:
		String infoMessage = "For the first table, control-click on any column head to see the code for " +
						"that column.<br><br>The purpose of the SeparatedTableModel is to " +
						"make your TableModel code more maintainable by using a separate class " +
						"for each column. ";
		JLabel infoLabel = new JLabel(wrapWithHtml(infoMessage));
		infoLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		sMf.getContentPane().add(infoLabel, BorderLayout.NORTH);
		
		// version info:
		JLabel versionInfo = new JLabel("JDK Version " + jdkVersion);
		add(versionInfo, BorderLayout.SOUTH);
	}

	private PeopleTableModel makeStandardModel(JPanel container) throws ParseException {
		PeopleTableModel model = new PeopleTableModel(getModelData());
		createTable(model, container, "Class");
		return model;
	}

	private void createTable(SeparatedTableModel pModel, JPanel container, String basis) {
		JTable table = new JTable(pModel);
		if (isJDK1_6) {
			Sort16.prepareForSorting(table, pModel);
		} else {
			setUp1_5Sorting(table);
		}
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		SeparatedTableModel.setUpHeader(table);
		applyMouseListener(table);
		JScrollPane scroller = new JScrollPane(table);
		JPanel scrollContainer = new JPanel(new BorderLayout());
		scrollContainer.add(scroller, BorderLayout.CENTER);
		scrollContainer.add(makeShowModelPanel(((TableSource)pModel).getSource(), basis), BorderLayout.NORTH);
		container.add(scrollContainer);
	}

	private PeoplePropTableModel makePropertyModel(JPanel container) throws ParseException {
		PeoplePropTableModel model = new PeoplePropTableModel(getModelData());
		createTable(model, container, "Property");
		return model;
	}

	private void setUp1_5Sorting(final JTable tbl) {
		MouseListener lsnr = new MouseAdapter() {
			private boolean priorAscending=false;
			private int priorColumn = -1;
			@Override
			public void mouseClicked(MouseEvent e) {
				int mods = e.getModifiersEx();
				if (mods == 0) {
					JTableHeader source = (JTableHeader) e.getSource();
					final int column = source.columnAtPoint(e.getPoint());
					//noinspection unchecked
					final SeparatedTableModel<Person> model = (SeparatedTableModel<Person>) tbl.getModel();

					// now figure out if we need to reverse the sort.
					boolean descending = false;
					if (column == priorColumn) {
						if (priorAscending) {
							priorAscending = false;
							descending = true;
						} else {
							priorAscending = true;
						}
					} else {
						priorAscending = true;
					}
					priorColumn = column;

					final boolean ascending = !descending;
					Comparator<Person> cmp = new Comparator<Person>() {
						public int compare(Person o1, Person o2) {
							AbstractColumn<Person,?> theColumn = model.getColumn(column);
							Comparable c1 = theColumn.getComparableValue(o1);
							Comparable c2 = theColumn.getComparableValue(o2);
							//noinspection unchecked
							int comparison = c1.compareTo(c2);
							if (ascending)
								return comparison;
							else
								return -comparison;
						}
					};
					model.getRows().sort(cmp);
					model.fireTableRowsUpdated(0, model.getRowCount()-1);
				}
			}
		};
		tbl.getTableHeader().addMouseListener(lsnr);
	}
	
	private void applyMouseListener(final JTable tbl) {
		if (((SeparatedTableModel)tbl.getModel()).getColumn(0) instanceof PeopleTableModel.DemoColumn) {
			MouseListener lsnr = new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					int mods = e.getModifiersEx();
					if ((mods & MouseEvent.CTRL_DOWN_MASK) == MouseEvent.CTRL_DOWN_MASK) {
						JTableHeader source = (JTableHeader) e.getSource();
						int column = source.columnAtPoint(e.getPoint());
						e.consume();
						SeparatedTableModel model = (SeparatedTableModel) tbl.getModel();
						PeopleTableModel.DemoColumn demoColumn = (PeopleTableModel.DemoColumn) model.getColumn(column);
						StringBuilder className = new StringBuilder(demoColumn.getClass().toString());
						String removal = "org.jdesktop.swingx.demo.PeopleTableModel$Demo";
						int rmStart = className.indexOf(removal);
						className.replace(rmStart, rmStart + removal.length(), ""); 
						String commentText = demoColumn.getCommentText();
						commentText = commentText.replace("\t *", "");
						String codeText = convertAngleBrackets(demoColumn.getCodeText()) + sPersonCode;
						codeText = codeText.replace("\t", "  ");
						JLabel commentLabel = new JLabel(wrapWithHtml(wrapWithTag(commentText, "body")));
						JLabel codeLabel = new JLabel(wrapWithHtml(wrapWithPre(codeText)));
						codeLabel.setFont(Font.getFont(Font.MONOSPACED));
						commentLabel.setFont(Font.getFont(Font.SERIF));
						JPanel panel = new CodeView();
						panel.add(commentLabel);
						panel.add(codeLabel);
						panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
	//					panel.setSize(800, 800);
						getDialog(className.toString(), panel);
	//					mCodeDisplayDlg.pack();
	//					Dimension size = mCodeDisplayDlg.getSize();
	//					mCodeDisplayDlg.setSize(800, size.height);
						mCodeDisplayDlg.setVisible(true);
					}
				}
			};
			tbl.getTableHeader().addMouseListener(lsnr);
		}
	}
	
	private JPanel makeShowModelPanel(final String modelText, String basis) {
		//noinspection StringConcatenation,StringContatenationInLoop,MagicCharacter,HardcodedLineSeparator
		System.err.println(modelText); // NON-NLS
		ActionListener showLsnr = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String fmtText = convertAngleBrackets(modelText);
				fmtText = fmtText.replace("\t", "  ");
				fmtText = wrapWithPre(fmtText);
				fmtText = wrapWithHtml(fmtText);
				JLabel label = new JLabel(fmtText);
				label.setFont(Font.getFont(Font.MONOSPACED));
				JPanel codeView = new CodeView();
				codeView.add(label);
				getDialog("TableModel", codeView);
				mCodeDisplayDlg.setVisible(true);
			}
		};
		String buttonFmt = "Show %1$s-Based TableModel";
		String buttonText =String.format(buttonFmt, basis);
		JButton showModel = new JButton(buttonText);
		showModel.addActionListener(showLsnr);
		JPanel mPanel = new JPanel(new BorderLayout());
		mPanel.add(showModel, BorderLayout.WEST);
		mPanel.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
		return mPanel;
	}

	private void getDialog(String title, JComponent content) {
		if (mCodeDisplayDlg == null) {
			mCodeDisplayDlg = new JDialog(sMf, false);
			mCodeDisplayDlg.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
			mCodeDisplayDlg.setLocation(sMf.getX() + sMf.getWidth(), sMf.getY());
			mCodeDisplayDlg.setSize(800, 555);
		} else {
			mCodeDisplayDlg.getContentPane().removeAll();
		}
		mCodeDisplayDlg.setTitle(title);
		JScrollPane scroller = new JScrollPane(content);
		scroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		mCodeDisplayDlg.add(scroller);
	}


	private String wrapWithHtml(String rawText) {
		return wrapWithTag(rawText, "html");
	}
	
	private String wrapWithPre(String rawText) {
		return wrapWithTag(rawText, "pre");
	}
	
	private String wrapWithTag(String text, String tag) {
		return String.format("<%2$s>%1$s</%2$s>", text, tag);
	}
	
	private String convertAngleBrackets(String input) {
		String output = input.replace("<", "&lt;");
		output = output.replace(">", "&gt;");
		return output;
	}

	private PersonInst[] getModelData() throws ParseException {
		//noinspection HardcodedFileSeparator,HardCodedStringLiteral
		return new PersonInst[] {
						new PersonInst("George W.", "Bush", "7/6/1946"),
						new PersonInst("William J.", "Clinton", "8/19/1946"),
						new PersonInst("George H. W.", "Bush", "6/12/1924"),
						new PersonInst("Ronald", "Reagan", "2/6/1911"),
						new PersonInst("James", "Carter", "10/1/1924"),
						new PersonInst("gerald", "ford", "7/14/1913"),
						new PersonInst("Richard", "Nixon", "1/9/1913"),
						new PersonInst("Lyndon", "Johnson", "8/27/1908"),
						new PersonInst("John", "Kennedy", "5/29/1917"),
						new PersonInst("Dwight", "Eisenhower", "10/14/1890"),
		};
	}
	private class CodeView extends JPanel implements Scrollable {


		public CodeView() {
			super();
			setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		}

		public Dimension getPreferredScrollableViewportSize() { return new Dimension(100, 100); }

		public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) { return 20; }

		public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
			return visibleRect.height-20;
		}

		public boolean getScrollableTracksViewportWidth() { return true; }

		public boolean getScrollableTracksViewportHeight() { return false; }
	}
	/**
	 * This call needs to be placed into its own class or the demo won't
	 * run under JDK 1.5, because it won't load the outer class if it
	 * references something from jdk 1.6.
	 */
	static class Sort16 {
		private Sort16() {}

		static void prepareForSorting(JTable pTable, SeparatedTableModel pModel) {
			pTable.setRowSorter(new TableRowKeyedSorter<SeparatedTableModel>(pModel));
		}
	}
	
	public interface TableSource extends TableModel {
		public String getSource();
	}
}