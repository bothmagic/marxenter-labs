/*
 * TestCalcs.java
 *
 * Created on September 1, 2005, 9:21 AM
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package org.jdesktop.dataset.calc;

import bsh.EvalError;
import bsh.Interpreter;
import java.util.List;
import org.jdesktop.dataset.DataColumn;
import org.jdesktop.dataset.DataSet;
import org.jdesktop.dataset.DataTable;
import org.jdesktop.dataset.provider.sql.JDBCDataConnection;
import org.jdesktop.dataset.provider.sql.SQLDataProvider;
import org.jdesktop.dataset.util.DataSetUtility;

/**
 *
 * @author patrick
 */
public class TestCalcs {
    DataSet dataSet;
    Interpreter intp;
    
    /** Creates a new instance of TestCalcs */
    public TestCalcs() {
        init();
        intp = new Interpreter();
    }
    
    public static void main(String[] args) {
        new TestCalcs().run();
    }
    
    private void run() {
        try {
            DataTable table = dataSet.getTable("DOC");
            table.loadAndWait();
            intp.set("dataSet", dataSet);
            
            setForTableContext(dataSet.getTable("DOC"));
            //intp.set("_table", table);
            
            /*
            String dataSetProxy = BSHProxyBuilder.getDataSetProxy(dataSet);
            String tableProxy = BSHProxyBuilder.getTableProxy(table);
            String methods = BSHProxyBuilder.getGlobalPerTableMethods();
            String colProxies = BSHProxyBuilder.getColumnProxies(table, 0);
             
            // System.out.println(dataSetProxy);
            //System.out.println(tableProxy);
            System.out.println(methods);
            System.out.println(colProxies);
            //intp.eval(tableProxy);
            intp.eval(methods);
            intp.eval(colProxies);
             */
            
            String calc = null;
            
            System.out.println("BSH Namespace");
            String[] names = intp.getNameSpace().getAllNames();
            for ( String name : names ) {
                System.out.println("   " + name);
            }
            
            System.out.println("\n\n");
            System.out.println("Evaluating against table " + table.getName() + " with " + table.getRowCount() + " rows");
            calc = "sum(__id);";
            dump(calc);
            calc = "avg(__id);";
            dump(calc);
            calc = "min(__id);";
            dump(calc);
            calc = "max(__id);";
            dump(calc);
            
            calc = "count(\"_id(row) > 1000\");";
            dump(calc);
            calc = "count(\"_id(row) < 1000\");";
            dump(calc);
            calc = "count(\"_id(row) > 1000 && _id(row) < 1500\");";
            dump(calc);
            calc = "count(\"_description(row).length() > 100\");";
            dump(calc);
            calc = "_description(1).startsWith(\"The\");";
            dump(calc);
            calc = "count(\"_description(row).startsWith(\\\"A \\\")\");";
            dump(calc);
            
            // this is a hack: here, we don't want BSH to re-evaluate the expression
            // as it loops through the rows. so we create an class that we can pass
            // in to evaluate the expression. This gets compiled as a nested class
            // within the BSH environment, and it's that class that gets executed
            //
            // Generally, this might be how we could optimize rows--create one of these
            // container objects whenever a calculated expression is set, and then reuse
            // that later
            String expression = "_description(row).startsWith(\"A \")";
            String evaluator =
                    "class Exp implements BooleanVal { \n" +
                    "    boolean isTrue(row) { \n" +
                    "       return " + expression + "; \n" +
                    "    }\n" +
                    "}\n";

            intp.eval(evaluator);
            intp.set("eval", intp.eval("new Exp()"));
            intp.eval("value() { return count(eval); }");
            
            calc = "value();";
            dump(calc);
            
            // Time the repeated requests for evaluation. Note that each call is
            // looping through all rows in the table, so it's a little bit slower
            // than you might otherwise expect
            int cnt = 10;
            long str = System.currentTimeMillis();
            System.out.println("Running count() " + cnt + " times against " + table.getRowCount() + " rows");
            System.out.println("   evaluating for " + expression);
            for ( int i=0; i < cnt; i++ ) {
                intp.eval(calc);
            }
            long elp = System.currentTimeMillis() - str;
            System.out.println("   elapsed time: " + elp + "ms, \n" + "     avg. " + (elp / cnt) + "ms for each pass thru table");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void setForTableContext(DataTable table) {
        try {
            intp.set("_table", table);
            String methods = BSHProxyBuilder.getGlobalPerTableMethods();
            String colProxies = BSHProxyBuilder.getColumnProxies(table, 0);
            
            System.out.println(methods);
            System.out.println(colProxies);
            intp.eval(methods);
            intp.eval(colProxies);
        } catch (Exception e) {
            throw new RuntimeException("Could not set context to table " + table.getName(), e);
        }
    }
    
    private void dump(String expression) {
        try {
            long str = System.currentTimeMillis();
            String exp = expression + " = " + intp.eval(expression);
            long elp = System.currentTimeMillis() - str;
            
            System.out.println(exp + " (" + elp + "ms)");
        } catch (EvalError e) {
            System.err.println("Could not evaluate expression: " + expression);
            System.err.println("   " + e.getMessage());
        }
    }
    
    private void init() {
        try {
            JDBCDataConnection conn = getDataConnection();
            if ( conn == null ) {
                System.err.println("No connection; stopping.");
                return;
            }
            conn.setConnected(true);
            
            dataSet = DataSetUtility.readDataSetFromJDBC(conn);
            dataSet.setName("ashkelon");
            List<DataTable> tables = dataSet.getTables();
            for ( DataTable table : tables ) {
                //System.out.println("TABLE: " + table.getName());
                SQLDataProvider dataProvider = new SQLDataProvider(table.getName());
                dataProvider.setConnection(conn);
                table.setDataProvider(dataProvider);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed on init()", e);
        }
    }
    
    private JDBCDataConnection getDataConnection() {
        JDBCDataConnection conn = null;
        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
        } catch (Exception e) {
            System.out.println("ERROR: failed to load Derby JDBC driver.");
            e.printStackTrace();
            return null;
        }
        try {
            conn = new JDBCDataConnection("org.apache.derby.jdbc.EmbeddedDriver", "jdbc:derby:classpath:/ashkelon", "ashkelon", "ashkelon");
        } catch (Exception e) {
            System.err.println("ERROR: failed to get connection to DerbyDB database");
            e.printStackTrace();
        }
        return conn;
    }
    
    private void todo() {
            /*
            //Exporter.toCSV(table, new PrintWriter(System.out));
             
            List<DataColumn> cols = table.getColumns();
             
            for ( DataColumn col : cols ) {
                System.out.println("Col " + col.getName() + " " + col.getType());
            }
            //System.out.println("Eval " + _rval = DOC_Table.getRow(i).getValue(\"ID\");
            //intp.eval("id(i) { return _DataTable.getRow(i).getValue(\"ID\"); }");
            //i.eval("_rval = id(1)");
            StringBuffer script = new StringBuffer();
            for ( DataColumn col : cols ) {
                script.append(col.getName().toLowerCase() + "(i) { return _DataTable.getRow(i).getValue(\"" + col.getName() + "\"); } \n");
            }
            System.out.println(script);
            //String dsProxy = getDataSetProxy(dataSet);
            //System.out.println(dsProxy);
             
            //intp.eval(dsProxy);
            //intp.set("ashkelon", intp.eval("new ashkelon()"));
            //System.out.println(intp.eval("ashkelon.countX(\"doc.id > 1000\");"));
            //intp.eval(script[1]);
            /*
            for ( int i = 0; i < table.getRowCount(); i++ ) {
                System.out.println(intp.eval("id(" + i + ")"));
            }
             */
    }
}
