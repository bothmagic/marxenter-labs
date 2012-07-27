/*
 * Created on 28.11.2007
 *
 */
package miguelm.table;

import java.awt.Color;
import java.awt.Component;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.CollationKey;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.swing.DefaultCellEditor;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.decorator.ColorHighlighter;
import org.jdesktop.swingx.decorator.HighlightPredicate;
import org.jdesktop.swingx.decorator.Highlighter;
import org.jdesktop.swingx.decorator.ShadingColorHighlighter;
import org.jdesktop.swingx.demo.Stock;
import org.jdesktop.swingx.renderer.DefaultTableRenderer;
import org.jdesktop.swingx.renderer.FormatStringValue;
import org.jdesktop.swingx.table.AbstractColumn;
import org.jdesktop.swingx.table.ColumnFactory;
import org.jdesktop.swingx.table.SeparatedTableModel;
import org.jdesktop.swingx.table.TableColumnExt;
import org.jdesktop.swingx.table.renderers.MoneyRenderer;

/**
 * Exercise SwingX with Miguel's SeparatedTableModel.
 * 
 * - use custom ColumnFactory
 * - use SwingX renderers and highlighters 
 * 
 * NOTE: this will not compile without 
 * - add Miguel's section to the classpath
 * - widening access to some of his classes (IDE will tell which) 
 */
public class XTableDemo {

    /**
     * Custom ColumnFactory taking config info from SeparatedTableModel
     * if available.
     */
    public static class MiguelColumnFactory extends ColumnFactory {

        @Override
        public void configureTableColumn(TableModel model,
                TableColumnExt columnExt) {
            super.configureTableColumn(model, columnExt);
            if (!(model instanceof SeparatedTableModel)) return;
            AbstractColumn modelColumn = ((SeparatedTableModel) model).getColumn(columnExt.getModelIndex());
            if (modelColumn.getRenderer() != null) {
                columnExt.setCellRenderer(modelColumn.getRenderer());
            }
            if (modelColumn.getEditor() != null) {
                columnExt.setCellEditor(modelColumn.getEditor());
            }
            if (modelColumn.getMaxWidth() > 0) {
                columnExt.setMaxWidth(modelColumn.getMaxWidth());
            }
            if (modelColumn.getMinWidth() >= 0) {
                columnExt.setMinWidth(modelColumn.getMinWidth());
            }
            
        }

        @Override
        protected int calcPrototypeWidth(JXTable table, TableColumnExt columnExt) {
            if (!(table.getModel() instanceof SeparatedTableModel)) {
                return super.calcPrototypeWidth(table, columnExt);
            }
            AbstractColumn modelColumn = ((SeparatedTableModel) table.getModel()).getColumn(columnExt.getModelIndex());
            return modelColumn.getPreferredWidth();
        }
    }

    /**
     * Creates and returns a renderer for currency formatting.
     * @return
     */
    private TableCellRenderer createXMoneyRenderer() {
        return new DefaultTableRenderer(
                new FormatStringValue(NumberFormat.getCurrencyInstance()), 
                JLabel.RIGHT);
    }

    /**
     * Creates and returns a Highlighter for shading non-editable cells.
     * @return
     */
    private Highlighter createNotEditableShader() {
        // PENDING: apply shade to selected
        return new ShadingColorHighlighter(HighlightPredicate.READ_ONLY);
    }

    /**
     * Creates and returns a Highlighter displaying negative Numbers in red.
     * @return
     */
    private Highlighter createNegativeRedder() {
        return new ColorHighlighter(HighlightPredicate.BIG_DECIMAL_NEGATIVE, null, Color.RED, null, 
                Color.RED);
    }

    /**
     * Creates and configures the UI.
     * @return
     */
    private Component createContent() {
        // set up table with core renderers
        JXTable table = new JXTable(mTMdl);
        table.setColumnControlVisible(true);
        table.putClientProperty(JXTable.USE_DTCR_COLORMEMORY_HACK, null);
        // setup table with x
        // PENDING: why doesn't the datepicker come us ad default date editor?
        //    not installed by default - DO 
        JXTable xtable = new JXTable(xModel);
        xtable.setColumnControlVisible(true);
        xtable.addHighlighter(createNotEditableShader());
        xtable.addHighlighter(createNegativeRedder());
        JTabbedPane pane = new JTabbedPane();
        pane.addTab("DefaultTCRenderer", new JScrollPane(table));
        pane.addTab("SwingX Renderer", new JScrollPane(xtable));
        return pane;
    }
    

    private static void createAndShowGUI() {
        ColumnFactory.setInstance(new MiguelColumnFactory());
        JXFrame frame = new JXFrame("XTableDemo", true);
        frame.add(new XTableDemo().createContent());
        frame.pack();
        frame.setVisible(true);
    }

    private SeparatedTableModel<Purchase> xModel;
    private TableCellRenderer xMoneyRenderer;
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
    

    /**
     * Inits models.
     */
    public XTableDemo() {
        List<Purchase> rowList = createPurchaseList();
        mTMdl = new SeparatedTableModel<Purchase>(rowList);
        // populate with columns using default renderers
        populateColumns(mTMdl);
        // populate with xColumns using SwingX highlighters/renderers
        List<Purchase> purchases = createPurchaseList();
        xModel = new SeparatedTableModel<Purchase>(purchases);
        populateColumnsX(xModel);
    }

    /**
     * Populates the model with columns which use SwingX renderers/editors.
     * @param mdl
     */
    private void populateColumnsX(SeparatedTableModel<Purchase> mdl) {
        mdl.addColumn(new TickerColumn());
        mdl.addColumn(new NameColumn());
        mdl.addColumn(new PriceColumn());
        mdl.addColumn(new PurchasePriceColumn());
        mdl.addColumn(new SharesColumn());
        DateColumn dateColumn = new DateColumn() {
            /**
             * need to override - super doesn't respect setter
             */
            @Override
            public TableCellEditor getEditor() {
                return null;
            }
            
        };
        mdl.addColumn(dateColumn);
        ValueColumn valueColumn = new ValueColumn();
        valueColumn.setRenderer(getXMoneyRenderer());
        mdl.addColumn(valueColumn);
        ProfitColumn profitColumn = new ProfitColumn();
        profitColumn.setRenderer(getXMoneyRenderer());
        mdl.addColumn(profitColumn);
}

    private TableCellRenderer getXMoneyRenderer() {
        if (xMoneyRenderer == null) {
            xMoneyRenderer = createXMoneyRenderer();
        }
        return xMoneyRenderer;
    }

//-------------------- c&p from miguelm TableDemo
    
    private static final TableCellRenderer mMoneyRenderer = new MoneyRenderer();
    //      private static       HilightRenderer sHilightRenderer;
    private static final int sMoneyWidth = 100;
    private static final int sTickerWidth = 60;
    private static final DateFormat sDateShr = DateFormat.getDateInstance(DateFormat.SHORT);
    private static final DateFormat sDateMed = DateFormat.getDateInstance(DateFormat.MEDIUM);
    private SeparatedTableModel<Purchase> mTMdl;

    private List<Purchase> createPurchaseList() {
        // Now, create the individual rows, and put them in a List.
        List<Purchase> rowList = new ArrayList<Purchase>();
        // First, create the table data.
        
        // These instances may be shared by multiple rows. 
        // (The constructor caches them.)
        new Stock("ACME", 32.50);
        new Stock("WEED", 56.25);
        new Stock("WAND", 42.00);
        new Stock("SPCE", 34.56);
        new Stock("ROXY", 5.00);
        
        for (int ii = 0; ii < 3; ++ii) {
                // These throw ParseException
                try {
                    rowList.add(new Purchase("ACME", 2000.0 * (ii + 1), 10.0 + (ii * 5), reformat("03/" + 18 + (ii * 1000) + "/01")));
                    rowList.add(new Purchase("WEED", 1000.0 * (ii + 1), 5.0 + (ii * 5), reformat("12/" + 18 + (ii * 1000) + "/1999")));
                    rowList.add(new Purchase("WEED", 2000.0 * (ii + 1), 10.0 + (ii * 5), reformat("3/" + 18 + (ii * 1000) + "/2001")));
                    rowList.add(new Purchase("WAND", 3000.0 * (ii + 1), 45.0 + (ii * 5), reformat("10/" + 4 + (ii * 1000) + "/1957")));
                    rowList.add(new Purchase("SPCE", 4000.0 * (ii + 1), 26.0 + (ii * 5), reformat("1/" + 28 + (ii * 1000) + "/1969")));
                    rowList.add(new Purchase("ROXY", 10000.0 * (ii + 1), 4203 + (ii * 5), reformat("4/" + 1 + (ii * 100) + "/1998")));
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
        }
        return rowList;
    }
    

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
