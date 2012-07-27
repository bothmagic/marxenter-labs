import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import org.jdesktop.dataset.DataSet;
import org.jdesktop.dataset.DataTable;
import org.jdesktop.dataset.io.schema.DataSetIOUtility;
import org.jdesktop.dataset.io.schema.XMLDataSetSchemaReader;
import org.jdesktop.dataset.provider.sql.JDBCDataConnection;
import org.jdesktop.dataset.io.transfer.Exporter;
import org.jdesktop.dataset.io.schema.DataSetIOUtility;
import org.jdesktop.dataset.util.DataSetUtility;


/**
 *
 * @author patrick
 */
public class DataSetIODemo {
    
    /**
     * Creates a new instance of DataSetIODemo
     */
    public DataSetIODemo() {
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        new DataSetIODemo().run();
        
    }
    
    private void run() {
        try {
            JDBCDataConnection conn = getDataConnection();
            if ( conn == null ) {
                System.err.println("No connection; stopping.");
                return;
            }
            conn.setConnected(true);
            
            DataSet dataSet = DataSetUtility.readDataSetFromJDBC(conn);
            
            OutputStream os = new ByteArrayOutputStream();
     
            DataSetUtility.writeDataSetAsXml(os, dataSet);
            
            String schemaContents = os.toString();
            Reader xmlSchemaReader = new StringReader(schemaContents);
            System.out.println("SCHEMA:\n" + schemaContents);
            
            DataSet newDataSet = DataSetUtility.readDataSetFromXml(xmlSchemaReader, "API", "PACKAGE", "CLASSTYPE");
            
            System.out.println("ADDED " + new XMLDataSetSchemaReader(new StringReader(schemaContents)).addTables(newDataSet, "DOC"));
            
            System.out.println("ADDED RELATIONS " + new XMLDataSetSchemaReader(new StringReader(schemaContents)).addRelations(newDataSet, "CLASSTYPE"));
            System.out.println("ADDED RELATIONS " + new XMLDataSetSchemaReader(new StringReader(schemaContents)).addRelations(newDataSet, "DOC"));
            
            DataSetUtility.writeDataSetAsXml(new PrintWriter(System.out), newDataSet);
            
            shutDownDB();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void dumpTable(DataSet dataSet, String tableName) {
        DataTable table = dataSet.getTable(tableName);
        if ( table == null ) {
            System.err.println("!!! TABLE " + tableName + " NOT IN DATASET");
            return;
        }
        table.loadAndWait();
        System.out.println("DATA FROM " + table.getName());
        Exporter.toCSV(table, new PrintWriter(System.out));
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
    
    private Connection getConnection() {
        Connection conn = null;
        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
        } catch (Exception e) {
            System.out.println("ERROR: failed to load Derby JDBC driver.");
            e.printStackTrace();
            return null;
        }
        try {
            conn = DriverManager.getConnection("jdbc:derby:ashkelon", "ashkelon", "ashkelon");
        } catch (Exception e) {
            System.err.println("ERROR: failed to get connection to DerbyDB database");
            e.printStackTrace();
        }
        return conn;
    }
    
    private void runOld() {
        try {
            // "org.hsqldb.jdbcDriver", "jdbc:hsqldb:hsql://localhost/ashkelon", "ashkelon", "ashkelon"
            Connection conn = null;
            try {
                Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            } catch (Exception e) {
                System.out.println("ERROR: failed to load Derby JDBC driver.");
                e.printStackTrace();
                return;
            }
            try {
                conn = DriverManager.getConnection("jdbc:derby:ashkelon", "ashkelon", "ashkelon");
            } catch (Exception e) {
                System.err.println("ERROR: failed to get connection to DerbyDB database");
                e.printStackTrace();
            }
            //createSampleTables(conn);
            ResultSet tableResultSet = null;
            ResultSet keysResultSet = null;
            ResultSet columnResultSet = null;
            ResultSet indicesResultSet = null;
            ResultSet fkResultSet = null;
            List primaryKeys = null;
            DatabaseMetaData databaseMetaData = null;
            ResultSet rs = null;
            databaseMetaData = conn.getMetaData();
            
            System.out.println("");
            System.out.println("SCHEMA DETAILS");
            // CLEAN DataSetIOUtility.dumpSchemaDetails(databaseMetaData, new PrintWriter(System.out));
            
            String defaultSchemaName = DataSetIOUtility.getDefaultSchemaName(databaseMetaData);
            defaultSchemaName = "ASHKELON";
            String[] typeArray = {"TABLE"};
            String tableName = "%";
            tableResultSet = databaseMetaData.getTables(null, defaultSchemaName, tableName, typeArray);
            
            System.out.println("");
            System.out.println("TABLE DETAILS");
            // CLEAN DataSetIOUtility.dumpResultSetDetails(tableResultSet, (Writer)new PrintWriter(System.out));
            tableResultSet.close();
            
            String columnPattern = "%";
            columnResultSet = databaseMetaData.getColumns(null, defaultSchemaName, tableName, columnPattern );
            
            System.out.println("");
            System.out.println("COLUMN DETAILS");
            // CLEAN DataSetIOUtility.dumpResultSetDetails(columnResultSet, (Writer)new PrintWriter(System.out));
            columnResultSet.close();
            
            boolean unique = false;
            indicesResultSet = databaseMetaData.getIndexInfo(null, defaultSchemaName, tableName, unique, true );
            
            System.out.println("");
            System.out.println("INDEXES - NON-UNIQUE");
            // CLEAN DataSetIOUtility.dumpResultSetDetails(indicesResultSet, (Writer)new PrintWriter(System.out));
            indicesResultSet.close();
            
            unique = true;
            indicesResultSet = databaseMetaData.getIndexInfo(null, defaultSchemaName, tableName, unique, true );
            
            System.out.println("");
            System.out.println("INDEXES - UNIQUE");
            // CLEAN DataSetIOUtility.dumpResultSetDetails(indicesResultSet, (Writer)new PrintWriter(System.out));
            indicesResultSet.close();
            
            fkResultSet = databaseMetaData.getImportedKeys( null, defaultSchemaName, tableName );
            
            System.out.println("");
            System.out.println("FOREIGN KEYS");
            // CLEAN DataSetIOUtility.dumpResultSetDetails(fkResultSet, (Writer)new PrintWriter(System.out));
            fkResultSet.close();
            
            Statement stmt = conn.createStatement();
            // CLEAN DataSetIOUtility.dumpResultSetDetails(stmt.executeQuery("select * from seqs"), new PrintWriter(System.out));
            
            conn.close();
            shutDownDB();
        } catch (Exception e) {
            e.printStackTrace();
        }
        shutDownDB();
    }
    
    private void shutDownDB() {
        try {
            // will always throw exception, expected
            DriverManager.getConnection("jdbc:derby:;shutdown=true");
        } catch (Exception e) {
            // OK
        }
    }
    private void createSampleTables(Connection conn) {
        try {
            conn.setAutoCommit(false);
            
            /*
               Creating a statement lets us issue commands against
               the connection.
             */
            Statement s = conn.createStatement();
            
            /*
               We create a table, add a few rows, and update one.
             */
            s.execute("create table derbyDB(num int, addr varchar(40))");
            System.out.println("Created table derbyDB");
            s.execute("insert into derbyDB values (1956,'Webster St.')");
            System.out.println("Inserted 1956 Webster");
            s.execute("insert into derbyDB values (1910,'Union St.')");
            System.out.println("Inserted 1910 Union");
            s.execute(
                    "update derbyDB set num=180, addr='Grand Ave.' where num=1956");
            System.out.println("Updated 1956 Webster to 180 Grand");
            
            s.execute(
                    "update derbyDB set num=300, addr='Lakeshore Ave.' where num=180");
            System.out.println("Updated 180 Grand to 300 Lakeshore");
            conn.commit();
            s.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void hold() {
         /*
          JDBCDataConnection jdbcConn = new JDBCDataConnection("org.hsqldb.jdbcDriver", "jdbc:hsqldb:hsql://localhost/ashkelon", "ashkelon", "ashkelon");
         jdbcConn.setConnected(true);
         DataSet dataSet = DataSetUtility.createDataSetSchema(jdbcConn, "API", "AUTHOR", "CLASSTYPE");
          
         List<DataTable> tables = dataSet.getTables();
         for ( DataTable table : tables ) {
            System.out.println(table.getName());
            table.loadAndWait();
            System.out.println("TABLE: " + table.getName());
            Exporter.toCSV(table);
         }
          */
    }
}
