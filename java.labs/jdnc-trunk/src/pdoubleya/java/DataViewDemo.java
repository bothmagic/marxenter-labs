import java.io.PrintWriter;
import java.util.List;
import org.jdesktop.dataset.DataSet;
import org.jdesktop.dataset.DataTable;
import org.jdesktop.dataset.RowFilterDataView;
import org.jdesktop.dataset.provider.sql.JDBCDataConnection;
import org.jdesktop.dataset.provider.sql.SQLDataProvider;
import org.jdesktop.dataset.util.DataGrid;
import org.jdesktop.dataset.util.DataSetUtility;

/**
 *
 * @author patrick
 */
public class DataViewDemo {
    DataSet dataSet;
    
    /**
     * Creates a new instance of DataViewDemo
     */
    public DataViewDemo() {
        init();
    }
    
    public static void main(String[] args) {
        new DataViewDemo().run();
    }
    
    private void run() {
        try {
            DataTable table = dataSet.getTable("CLASSTYPE");
            table.loadAndWait();
            
            System.out.println("table.getRowCount() " + table.getRowCount());
            
            RowFilterDataView filteredTable = new RowFilterDataView(table);
            System.out.println("filtered.getRowCount() (no filter applied)" + filteredTable.getRowCount());
            /*
            int cnt = 0;
            for ( DataRow row : table.getRows()) {
                if ( cnt++ >= ( table.getRowCount() / 2 ) ) break;
             
                row.setStatus(DataRow.DataRowStatus.UPDATED);
            }
             
            filteredTable.filterByStatus(DataRow.DataRowStatus.UNCHANGED);
            System.out.println("   unchanged " + filteredTable.getRowCount());
             
            filteredTable.filterByStatus(DataRow.DataRowStatus.UPDATED);
            System.out.println("   modified " + filteredTable.getRowCount());
             
            SortedDataView sortedTable = new SortedDataView(filteredTable);
            sortedTable.setSortingStatus(table.getColumn("PACKAGEID"), SortedDataView.ASCENDING);
            sortedTable.setSortingStatus(table.getColumn("DOCID"), SortedDataView.ASCENDING);
            DataGrid.newGrid(sortedTable).dump(new PrintWriter(System.out));
             
            sortedTable = new SortedDataView(table);
            sortedTable.setSortingStatus(table.getColumn("PACKAGEID"), SortedDataView.ASCENDING);
            sortedTable.setSortingStatus(table.getColumn("DOCID"), SortedDataView.ASCENDING);
             
            filteredTable = new RowFilterDataView(sortedTable);
            filteredTable.filterByStatus(DataRow.DataRowStatus.UPDATED);
             
            ColumnFilterDataView colFiltered = new ColumnFilterDataView(filteredTable);
            colFiltered.hideAll();
            colFiltered.showColumn("PACKAGEID");
            colFiltered.showColumn("DOCID");
            colFiltered.showColumn("SUPERCLASSNAME");
            DataGrid.newGrid(colFiltered).dump(new PrintWriter(System.out));
             */
            filteredTable.filterByExpression("true");
            DataGrid.newGrid(filteredTable).dump(new PrintWriter(System.out));

            filteredTable.filterByExpression("id > 200");
            DataGrid.newGrid(filteredTable).dump(new PrintWriter(System.out));
        } catch (Exception e) {
            e.printStackTrace();
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
}
