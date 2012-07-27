package org.jdesktop.swingx.demo;/*
 * $Id: TableDemo.java 1959 2007-11-21 11:11:40Z MiguelM $
 *
 * Copyright 2007 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */

/**
 * <br>Created by IntelliJ IDEA.
 * <br>User: Miguel
 * <br>Date: Nov 8, 2007
 * <br>Time: 12:23:34 PM
 */

import java.text.DateFormat;
import java.text.ParseException;
import java.text.CollationKey;
import java.awt.BorderLayout;
import java.awt.Component;
import java.util.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableCellEditor;
import org.jdesktop.swingx.table.SeparatedTableModel;
import org.jdesktop.swingx.table.AbstractColumn;
import org.jdesktop.swingx.table.TableRowHeader;
import org.jdesktop.swingx.table.TableRowKeyedSorter;
import org.jdesktop.swingx.table.renderers.MoneyRenderer;

/**
 * Demonstrate the use of a SeparatedTableModel.
 * This class uses two additional features of my custom packages. These are
 * <code>LandF</code> and
 * <code>TableRowHeader</code>. I need to remove these before submitting this
 * for publication. I may also want to remove the HilightRenderer, since it's
 * beyond the scope of this article
 *
 * @author Miguel Mu\u00f1oz
 */
@SuppressWarnings({"HardCodedStringLiteral", "StringConcatenation", "MagicCharacter", "HardcodedLineSeparator", "CallToSystemExit", "HardcodedFileSeparator", "MultiplyOrDivideByPowerOfTwo"})
public class TableDemo
				extends JPanel {
	//  static { Locale.setDefault(Locale.UK); }
	private static final JFrame mf = new JFrame("Demo of SeparatedTableModel and AbstractColumn");

	private static final TableCellRenderer mMoneyRenderer = new MoneyRenderer();
	//	private static       HilightRenderer sHilightRenderer;
	private static final int sMoneyWidth = 100;
	private static final int sTickerWidth = 60;
	private static final DateFormat sDateShr = DateFormat.getDateInstance(DateFormat.SHORT);
	private static final DateFormat sDateMed = DateFormat.getDateInstance(DateFormat.MEDIUM);
	private SeparatedTableModel<Purchase> mTMdl;

	public static void main(String[] args) {
		processArgs(args);
		System.out.println("Java version = " + System.getProperty("java.version"));
		mf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		TableDemo view = null;
		try { view = new TableDemo(); }
		catch (ParseException e) {
			e.printStackTrace();
			System.exit(1);
		}
		mf.getContentPane().setLayout(new BorderLayout());
		mf.getContentPane().add(view, BorderLayout.CENTER);
		//noinspection MagicNumber
		mf.setBounds(10, 10, 750, 400);
		mf.setVisible(true);
	}

	protected static void processArgs(String[] args) {
		if (args.length == 0)
			LandF.setLF(LandF.Metal);
		else {
			String arg = args[0].toLowerCase();
			String os = "os";
			String metal = "metal";
			String ocean = "ocean";
			String win = "windowsclassic";
			String motif = "motif";
			if (args[0].indexOf('?') >= 0) {
				String dm = "  java com.mm.demos.TableDemo ";
				String line = "\n";
				System.out.println("Usage: \n"
								+ dm + os + "    (Platform default)" + line
								+ dm + metal + line
								+ dm + ocean + line
								+ dm + win + line
								+ dm + motif + line
								+ dm + "? | -? | /?" + line
								+ "    (substrings are allowed)"
				);
				System.exit(0);
			}
			// I really should place these shortcut definitions in LandF
			String platform = "platform";
			String gtk = "gtk";
			if (platform.contains(arg) || os.contains(arg))
				LandF.setLF(LandF.Platform);
			else if (metal.contains(arg))
				LandF.setLF(LandF.Metal);
			else if (ocean.contains(arg))
				LandF.setLF(LandF.Ocean);
			else if (motif.contains(arg))
				LandF.setLF(LandF.Motif);
			else if (win.contains(arg))
				LandF.setLF(LandF.WindowsClassic);
			else if (gtk.contains(arg))
				LandF.setLF(LandF.GTK);
		}
	}

	@SuppressWarnings({"ResultOfObjectAllocationIgnored", "MagicNumber", "ObjectAllocationInLoop", "StringContatenationInLoop"})
	protected TableDemo() throws ParseException {
		super(new BorderLayout());

		// First, create the table data.

		// These instances may be shared by multiple rows. 
		// (The constructor caches them.)
		new Stock("ACME", 32.50);
		new Stock("WEED", 56.25);
		new Stock("WAND", 42.00);
		new Stock("SPCE", 34.56);
		new Stock("ROXY", 5.00);

		// Now, create the individual rows, and put them in a List.
		List<Purchase> rowList = new ArrayList<Purchase>();
		for (int ii = 0; ii < 3; ++ii) {
			// These throw ParseException
			rowList.add(new Purchase("ACME", 2000.0 * (ii + 1), 10.0 + (ii * 5), reformat("03/" + 18 + (ii * 1000) + "/01")));
			rowList.add(new Purchase("WEED", 1000.0 * (ii + 1), 5.0 + (ii * 5), reformat("12/" + 18 + (ii * 1000) + "/1999")));
			rowList.add(new Purchase("WEED", 2000.0 * (ii + 1), 10.0 + (ii * 5), reformat("3/" + 18 + (ii * 1000) + "/2001")));
			rowList.add(new Purchase("WAND", 3000.0 * (ii + 1), 45.0 + (ii * 5), reformat("10/" + 4 + (ii * 1000) + "/1957")));
			rowList.add(new Purchase("SPCE", 4000.0 * (ii + 1), 26.0 + (ii * 5), reformat("1/" + 28 + (ii * 1000) + "/1969")));
			rowList.add(new Purchase("ROXY", 10000.0 * (ii + 1), 4203 + (ii * 5), reformat("4/" + 1 + (ii * 100) + "/1998")));
		}
//		sHilightRenderer = new HilightRenderer(mTMdl, tbl);
//		NameColumn nCol = (NameColumn)mTMdl.getColumn(1);
//		AbstractColumn<Purchase, ?> nCol = mTMdl.getColumn(1);
//		nCol.setRenderer(sHilightRenderer);
		//noinspection AssignmentToStaticFieldFromInstanceMethod
//		sHilightRenderer = new HilightRenderer();

		// Create the table model from the list of rows.
		mTMdl = buildTableModel(rowList);
		// Now, create the table. 
//		populateColumns(mTMdl);
		JTable table = new JTable(mTMdl);
//		tbl.setModel(mTMdl);
		mTMdl.setUpHeader(table);
		table.setRowSorter(new TableRowKeyedSorter<SeparatedTableModel>(mTMdl));
//		mTable.getTableHeader().setDefaultRenderer(new ToolTipHeaderRenderer());
//		table.getTableHeader().setDefaultRenderer(new DbgHdrRenderer());

//		sHilightRenderer.setHiliteColor(table);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
//		mTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.setColumnSelectionAllowed(true);
		table.getTableHeader().setReorderingAllowed(false);

		final JScrollPane scr = TableRowHeader.createScroller(table);

		add(scr, BorderLayout.CENTER);
	}

	protected SeparatedTableModel<Purchase> getTblModel() { return mTMdl; }

	private SeparatedTableModel<Purchase> buildTableModel(List<Purchase> pRowList) {
		SeparatedTableModel<Purchase> tMdl = new SeparatedTableModel<Purchase>(pRowList);
		populateColumns(tMdl);
		return tMdl;
	}

	private void populateColumns(SeparatedTableModel<Purchase> mdl) {
		mdl.addColumn(new TickerColumn());
		mdl.addColumn(new NameColumn());
		mdl.addColumn(new PriceColumn());
		mdl.addColumn(new PurchasePriceColumn());
		mdl.addColumn(new SharesColumn());
		mdl.addColumn(new DateColumn());
		mdl.addColumn(new ValueColumn());
		mdl.addColumn(new ProfitColumn());
	}

	private static class TickerColumn extends AbstractColumn<Purchase, String> {
		TickerColumn() { super("Symbol", String.class, sTickerWidth); }

		public String getValue(Purchase o) { return o.getTicker(); }

		public void setValue(String value, Purchase pPurchase) { pPurchase.setTicker(value); }

		public boolean getUpdateRow(Purchase pRow) { return true; }

		public boolean isEditable(Purchase pPurchase) { return true; }
	}

	private static class NameColumn extends AbstractColumn<Purchase, String> {
		public static final int NAME_WIDTH = 180;

		NameColumn() { super("Name", String.class, NAME_WIDTH); }

		public String getValue(Purchase pRow) { return pRow.getName(); }

		public void setValue(String value, Purchase pPurchase) { pPurchase.setName(value); }

		public boolean getUpdateTable(Purchase pRow) { return true; }

		public boolean isEditable(Purchase pName) { return true; }

		@Override
		public CollationKey getComparableValue(Purchase pRow) {
			return Stock.getKey(pRow.getTicker());
		}
	}

	private static class SharesColumn extends AbstractColumn<Purchase, BigDecimal> {
		SharesColumn() { super("Shares", BigDecimal.class, sMoneyWidth * 3 / 4); }

		public BigDecimal getValue(Purchase pRow) { return pRow.getShares(); }

		public void setValue(BigDecimal sh, Purchase pRow) { pRow.setShares(sh); }

		public boolean getUpdateRow(Purchase pRow) { return true; }

		public boolean isEditable(Purchase pNum) { return true; }
	}

	private static class PurchasePriceColumn extends AbstractColumn<Purchase, BigDecimal> {
		PurchasePriceColumn() { super("Buy Price", BigDecimal.class, sMoneyWidth * 2 / 3, mMoneyRenderer); }

		public BigDecimal getValue(Purchase pRow) { return pRow.getPurchasePrice(); }

		public void setValue(BigDecimal pr, Purchase pRow) { pRow.setPurchasePrice(pr); }

		public boolean getUpdateRow(Purchase pRow) { return true; }

		public boolean isEditable(Purchase pNum) { return true; }
	}

	private static class PriceColumn extends AbstractColumn<Purchase, BigDecimal> {
		PriceColumn() { super("Price", BigDecimal.class, sMoneyWidth * 2 / 3, mMoneyRenderer); }

		public BigDecimal getValue(Purchase pRow) { return pRow.getPrice(); }

		public void setValue(BigDecimal prc, Purchase pRow) { pRow.setPrice(prc); }

		public boolean getUpdateTable(Purchase pRow) { return true; }

		public boolean isEditable(Purchase pNum) { return true; }
	}

	private static class DateColumn extends AbstractColumn<Purchase, Date> {
		private TableCellEditor mEd = new DateEditor();
		public static final int DATE_WIDTH = 80;

		DateColumn() { super("Date", Date.class, DATE_WIDTH); }

		public Date getValue(Purchase pRow) { return pRow.getPurchaseDate(); }

		public void setValue(Date pDate, Purchase pRow) { pRow.setPurchaseDate(pDate); }

		public TableCellEditor getEditor() { return mEd; }

		public boolean isEditable(Purchase pPurchase) { return true; }
	}

	private static class ValueColumn extends AbstractColumn<Purchase, BigDecimal> {
		ValueColumn() { super("Value", BigDecimal.class, sMoneyWidth, mMoneyRenderer); }

		public BigDecimal getValue(Purchase pRow) { return pRow.getValue(); }
	}

	protected static class ProfitColumn extends AbstractColumn<Purchase, BigDecimal> {
		ProfitColumn() { super("Profit", BigDecimal.class, sMoneyWidth, mMoneyRenderer); }

		public BigDecimal getValue(Purchase pRow) { return pRow.getProfit(); }
	}

	private static class Purchase {
		private Stock mStock;
		private BigDecimal mShares;
		private BigDecimal mPurchasePrice;
		private Date mPurchaseDate;

		private Purchase(String pTicker,
		                 BigDecimal pShares,
		                 BigDecimal pPurchasePrice,
		                 Date pDate) {
			mShares = pShares;
			mStock = Stock.findStock(pTicker);
			mPurchasePrice = pPurchasePrice;
			mPurchaseDate = pDate;
		}

		private Purchase(String pTicker, double pShares, double pPurchasePrice, String pDate)
						throws ParseException {
			this(pTicker, new BigDecimal(pShares), new BigDecimal(pPurchasePrice), sDateShr.parse(pDate));
		}

		private Purchase(String pTicker, double pShares, int pPurchasePriceCents, String pDate)
						throws ParseException {
			this(pTicker, new BigDecimal(pShares), new BigDecimal(new BigInteger(Integer.toString(pPurchasePriceCents), 10), 2), sDateShr.parse(pDate));
		}

		public String getTicker() { return mStock.getTicker(); }

		public void setTicker(String pTk) { mStock = Stock.findStock(pTk.toUpperCase()); }

		public String getName() { return mStock.getName(); }

		public void setName(String pName) { mStock.setName(pName); }

		public BigDecimal getShares() { return mShares; }

		public void setShares(BigDecimal pShares) { mShares = pShares; }

		public BigDecimal getPrice() { return mStock.getPrice(); }

		public void setPrice(BigDecimal pPrice) { mStock.setPrice(pPrice); }

		public BigDecimal getPurchasePrice() { return mPurchasePrice; }

		public void setPurchasePrice(BigDecimal pPurchasePrice) { mPurchasePrice = pPurchasePrice; }

		public BigDecimal getValue() { return mStock.getPrice().multiply(mShares); }

		public BigDecimal getProfit() { return mStock.getPrice().subtract(mPurchasePrice).multiply(mShares); }

		public Date getPurchaseDate() { return mPurchaseDate; }

		public void setPurchaseDate(Date pPurchaseDate) { mPurchaseDate = pPurchaseDate; }
	}

	private static class DateEditor extends DefaultCellEditor {
		DateEditor() { super(new JTextField()); }

		public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
			try { return super.getTableCellEditorComponent(table, sDateMed.format(value), isSelected, row, column); }
			catch (IllegalArgumentException e) // happens if this is not a valid date.
			{
				return super.getTableCellEditorComponent(table, value, isSelected, row, column);
			}
		}

		public Object getCellEditorValue() {
			String val = super.getCellEditorValue().toString();
			try { return sDateMed.parse(val); }
			catch (ParseException e) {
				try { return sDateShr.parse(val); }
				catch (ParseException ex) { return ""; }
			}
		}
	}

	/**
	 */
	/**
	 * Reformats the given date from the US format to the current Locale's short format.
	 *
	 * @param usDate A date formatted for the US.
	 * @return A date that is properly formatted for the current locale.
	 */
	private static String reformat(String usDate) {
		DateFormat fDate = DateFormat.getDateInstance(DateFormat.SHORT, Locale.US);
		try {
			Date dtf = fDate.parse(usDate);
			return sDateShr.format(dtf);
		}
		catch (ParseException e) {
			throw new AssertionError("Parsing problem with " + usDate);
		}
	}
}

/**
 * Column Width Notes:
 * AUTO_RESIZE_OFF
 *   Min   15
 *   Max   MaxInt
 *   Pref  same as width
 *
 * AUTO_RESIZE_NEXT_COLUMN
 *   As we size a column, the width and prefWidth change by equal amounts,
 *   until an extreme is reached. The difference can be positive or negative.
 *   Maybe we should save the PrefWidth as it changes?
 *
 * AUTO_RESIZE_SUBSEQUENT_COLUMNS
 *   All columns start proprotionatly bigger or smaller than the prefWidth
 *   As one gets bigger, the others get proportionatly smaller. PrefWidth
 *   always ajusts.
 *
 * AUTO_RESIZE_LAST_COLUMN
 *   All columns start proportionately bigger or smaller than prefWidth.
 *   Pref with of resized column always adjusts.
 *
 * AUTO_RESIZE_ALL_COLUMNS
 */
