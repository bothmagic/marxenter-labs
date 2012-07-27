package org.jdesktop.dataset.calc;

import java.util.List;
import org.jdesktop.dataset.DataColumn;
import org.jdesktop.dataset.DataSet;
import org.jdesktop.dataset.DataTable;

/**
 *
 * @author patrick
 */
public class BSHProxyBuilder {
    
    /** Creates a new instance of BSHProxyBuilder */
    private BSHProxyBuilder() {}

    public static String getDataSetProxy(DataSet set) {
        StringBuilder proxy = new StringBuilder();
        proxy.append("class " + set.getName().toLowerCase()).append(" { \n");
        
        for ( DataTable table : set.getTables()) {
            proxy.append("    " + getProxyTableName(table) + " = dataSet.getTable(\"" + table.getName() + "\"); \n");
        }
        proxy.append("\n");
        proxy.append("    count(DataTable table) { return table.getRowCount(); } \n");
        
        /*
        proxy.append(
                "    count(String exp) {  \n" +
                "        rows = count(" + getProxyTableName(table) + ");  \n" +
                "        cnt = 0; \n" +
                "        while ( --rows >= 0 ) {  \n" +
                "           if ( eval(exp) ) cnt++; \n" +
                "        } \n" +
                "        return cnt; \n" +
                "    } \n");
        */
        
        proxy.append(
                "    sum(col) {  \n" +
                "        tname = \"_\" +exp.substring(0, exp.indexOf('.')); \n" +
                "        rows = count(eval(tname));  \n" +
                "        cnt = 0; \n" +
                "        while ( --rows >= 0 ) {  \n" +
                "           if ( eval(exp) ) cnt++; \n" +
                "        } \n" +
                "        return cnt; \n" +
                "    } \n");

        proxy.append("}");
        return proxy.toString();
    }

    // assumes that a table "_" + table.getName() is pushed into BSH context
    // before these can be evaluated
    // Interpreter intp = new Interpreter();
    // intp.set("_" + table.getName().toLowerCase(), table);
    // intp.eval(countExpression);
    public static String getGlobalPerTableMethods() {
        StringBuilder methods = new StringBuilder();
        
        methods.append(
                "count(String exp) {  \n" +
                "    row = _table.getRowCount();  \n" +
                "    cnt = 0; \n" +
                "    while ( --row >= 0 ) {  \n" +
                "       if ( eval(exp) ) cnt++; \n" +
                "    } \n" +
                "    return cnt; \n" +
                "} \n\n");
        
        methods.append("interface BooleanVal { boolean isTrue(row); } \n"); 
        methods.append("interface NumericVal { boolean isTrue(row); } \n");

        methods.append(
                "count(BooleanVal method) {  \n" +
                "    row = _table.getRowCount();  \n" +
                "    cnt = 0; \n" +
                "    while ( --row >= 0 ) {  \n" +
                "       if ( method.isTrue(row) ) cnt++; \n" +
                "    } \n" +
                "    return cnt; \n" +
                "} \n\n");      

        methods.append(
                "sum(column) {  \n" +
                "    row = _table.getRowCount();  \n" +
                "    s = 0; \n" +
                "    while ( --row >= 0 ) {  \n" +
                "       s += _table.getRow(row).getValue(column); \n" +
                "    } \n" +
                "    return s; \n" +
                "} \n\n");           
        
        methods.append(
                "avg(column) {  \n" +
                "    row = _table.getRowCount();  \n" +
                "    s = 0; \n" +
                "    while ( --row >= 0 ) {  \n" +
                "       s += _table.getRow(row).getValue(column); \n" +
                "    } \n" +
                "    return s / _table.getRowCount(); \n" +
                "} \n\n");           

        methods.append(
                "max(column) {  \n" +
                "    row = _table.getRowCount();  \n" +
                "    mx = 0; \n" +
                "    while ( --row >= 0 ) {  \n" +
                "       v = _table.getRow(row).getValue(column); \n" +
                "       mx = ( v > mx ? v : mx );" +
                "    } \n" +
                "    return mx; \n" +
                "} \n\n");           

        methods.append(
                "min(column) {  \n" +
                "    row = _table.getRowCount();  \n" +
                "    mn = 0; \n" +
                "    while ( --row >= 0 ) {  \n" +
                "       v = _table.getRow(row).getValue(column); \n" +
                "       mn = ( v < mn ? v : mn );" +
                "    } \n" +
                "    return mn; \n" +
                "} \n\n");           

        return methods.toString();
    }
    
    public static String getTableFilterProxy(DataTable table) {
        List<DataColumn> cols = table.getColumns();
        
        StringBuilder tableProxy = new StringBuilder();
        tableProxy.append(BSHProxyBuilder.getPerTableColumnProxies(table, 0));
        tableProxy.append("interface BooleanVal { boolean isTrue(row); } \n"); 
        tableProxy.append("interface NumericVal { boolean isTrue(row); } \n");

        tableProxy.append(
                "filter(BooleanVal method) {  \n" +
                "    cnt = dataTable.getRowCount();  \n" +
                "    rows = new ArrayList(); \n" +
                "    while ( --cnt >= 0 ) {  \n" +
                "       row = dataTable.getRow(cnt); \n" +
                "       if ( method.isTrue(row) ) rows.add(row); \n" +
                "    } \n" +
                "    return rows; \n" +
                "} \n\n");           

        return tableProxy.toString();
    }
        
    public static String getColumnProxies(DataTable table) {
        return getColumnProxies(table, 4);
    }
    
    public static String getColumnProxies(DataTable table, int indent) {
        List<DataColumn> cols = table.getColumns();
        StringBuilder script = new StringBuilder();
        for ( DataColumn col : cols ) {
            script.append(getColumnProxy(col, indent));
        }
        return script.toString();
    }
    
    public static String getPerTableColumnProxies(DataTable table, int indent) {
        List<DataColumn> cols = table.getColumns();
        StringBuilder script = new StringBuilder();
        for ( DataColumn col : cols ) {
            script.append(getPerTableColumnProxy(col, indent));
        }
        return script.toString();
    }
    
    public static String getPerRowColumnProxies(DataTable table, int indent) {
        List<DataColumn> cols = table.getColumns();
        StringBuilder script = new StringBuilder();
        for ( DataColumn col : cols ) {
            script.append(getPerRowColumnProxy(col, indent));
        }
        return script.toString();
    }

    private static final String space = "          ";
    public static String getColumnProxy(DataColumn col, int indent) {
        StringBuilder builder = new StringBuilder();
        builder.append(getIndent(indent) + col.getName().toLowerCase() + "(i) { return dataTable.getRow(i).getValue(\"" + col.getName() + "\"); } \n");
        builder.append(getIndent(indent) + "__" + col.getName().toLowerCase() + " = dataTable.getColumn(\"" + col.getName() + "\"); \n");
        return builder.toString();
    }

    public static String getPerTableColumnProxy(DataColumn col, int indent) {
        StringBuilder builder = new StringBuilder();
        // builder.append(getIndent(indent) + col.getName().toLowerCase() + " = row.getValue(\"" + col.getName() + "\"); \n");
        builder.append(getIndent(indent) + col.getName().toLowerCase() + "Column = dataTable.getColumn(\"" + col.getName() + "\"); \n");
        return builder.toString();
    }
    
    public static String getPerRowColumnProxy(DataColumn col, int indent) {
        StringBuilder builder = new StringBuilder();
        builder.append(getIndent(indent) + col.getName().toLowerCase() + " = row.getValue(\"" + col.getName() + "\"); \n");
        return builder.toString();
    }
    
    public static String getIndent(int i) {
        return space.substring(space.length() - i);
    }

    public static String getColumnProxy(DataColumn col) {
        return getColumnProxy(col, 4);
    }

    public static String getProxyTableName(DataTable table) {
        return "_" + table.getName().toLowerCase();
    }
        
}
