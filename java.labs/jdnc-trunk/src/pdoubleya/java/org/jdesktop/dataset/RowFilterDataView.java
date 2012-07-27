package org.jdesktop.dataset;
import bsh.EvalError;
import bsh.Interpreter;
import java.util.ArrayList;
import java.util.List;
import org.jdesktop.dataset.calc.BSHProxyBuilder;

/**
 * A RowFilterDataView is a DataView which excludes some rows from the underlying DataTable, for example,
 * to only return rows with status UNMODIFIED. Only the row-accessors are affected. By default, all
 * rows are returned from the underlying table; you can set a filter using the
 * {@link #RowFilterDataView(DataTable, RowFilter)} constructor, or change it on the fly by using
 * {@link #setFilter(RowFilter)}. Note that filters are non-destructive and don't actually change
 * anything in the base table. Also, filters are always evaluated on demand.
 *
 * @author Patrick Wright
 */
//TODO
//figure out expression filtering
//bind to column changes
//what to do when a row is requested(getValue(row, col)) which is currently filtered out?
public class RowFilterDataView extends DataView {
    private RowFilter rowFilter;
    public static final RowFilter ALL_ROWS_FILTER = new RowFilter() { public boolean accept(DataRow row) { return true; }};
    
    public interface RowFilter {
        boolean accept(DataRow row);
    }
    
    public RowFilterDataView(DataTable source) {
        this(source, ALL_ROWS_FILTER);
    }
    
    public RowFilterDataView(DataTable source, RowFilter filter) {
        super(source);
        this.rowFilter = filter;
        setupBSH();
    }
    
    public void setFilter(RowFilter newFilter) {
        this.rowFilter = newFilter;
    }
    
    public void filterByStatus(final DataRow.DataRowStatus status) {
        // TODO: should have an overridden version which takes DataColumn...
        // as an argument, and we listen to those columns for changes; this would be
        // important if the filter was dependent on column values and if this
        // view was bound to GUI components
        rowFilter = new RowFilterDataView.RowFilter() {
            public boolean accept(DataRow row) {
                return row.getStatus() == status;
            }
        };
    }
    
    public void filterBy(final RowFilter filter) {
        // TODO: should have an overridden version which takes DataColumn...
        // as an argument, and we listen to those columns for changes; this would be
        // important if the filter was dependent on column values and if this
        // view was bound to GUI components
        this.rowFilter = filter;
    }
    
    ELRowFilter elRowFilter;
    
    public void filterByExpression(final String expression) {
        elRowFilter = new ELRowFilter(intp, expression);
    }
    
    
    private class ELRowFilter {
        Interpreter intp;
        ELRowFilter(Interpreter intp, String expression) {
            String evaluator =
                    "class Exp implements BooleanVal { \n" +
                    "    boolean isTrue(row) { \n" +
                    BSHProxyBuilder.getPerRowColumnProxies(getSource(), 7) +
                    "       System.out.println(id); \n" + 
                    "       return " + expression + "; \n" +
                    "    }\n" +
                    "}\n";
            System.out.println("Evaluator " + evaluator);
            this.intp = intp;
            try {
                intp.eval(evaluator);
                intp.set("eval", intp.eval("new Exp()"));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        
        public List<DataRow> listRows() {
            try {
                return (List<DataRow>) intp.eval("filter(eval)");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
    Interpreter intp;
    private void setupBSH() {
        String tableProxy = BSHProxyBuilder.getTableFilterProxy(getSource());
        System.out.println(tableProxy);
        
        intp = new Interpreter();
        
        try {
            intp.set("dataTable", getSource());
            intp.eval(tableProxy);
        } catch (EvalError e) {
            throw new RuntimeException(e);
        }
    }
    
    /*
    TODO: need a way to apply an expression evaluated per-row
    private class ELRowFilter implements RowFilter {
        private String expression;
        private Generator<?> exprImpl = new Constant<Object>(null);
     
        ELRowFilter(String expression) {
            setExpression(expression);
        }
     
        public boolean accept(DataRow row) {
            return ((Boolean)exprImpl.gen()).booleanValue();
        }
     
        public void setExpression(String expression) {
            if (expression ==  null || expression.equals(""))
                exprImpl = new Constant<Object>(null);
            else {
                try {
                    exprImpl = getParser().parseDataValue(expression);
                } catch (ParseException x) { throw new UncheckedParseException(x); }
            }
            this.expression = expression;
        }
     
        Parser getParser() {
            return getSource().getDataSet().getParser();
        }
    }
     
    public void filterByExpression(final String expression) {
        // TODO: should have an overridden version which takes DataColumn...
        // as an argument, and we listen to those columns for changes; this would be
        // important if the filter was dependent on column values and if this
        // view was bound to GUI components
        rowFilter = new ELRowFilter(expression);
    }
     */
    
    public Object getValue(int index, String columnName) {
        return getRows().get(modelIndex(index)).getValue(columnName);
    }
    
    public void setValue(int index, String columnName, Object value) {
        getRows().get(modelIndex(index)).setValue(columnName, value);
    }
    
    public List<DataRow> getRows() {
        List<DataRow> rows = getSource().getRows();
        List<DataRow> filtered = new ArrayList();
        if ( elRowFilter == null ) {
            for ( DataRow row : rows ) {
                if ( rowFilter.accept(row)) filtered.add(row);
            }
        } else {
            filtered = elRowFilter.listRows();
        }
        return filtered;
    }
    
    public int getRowCount() {
        return getRows().size();
    }
}
