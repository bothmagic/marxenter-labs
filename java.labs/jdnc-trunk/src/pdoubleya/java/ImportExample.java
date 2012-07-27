/*
 * ClipboardTest.java
 *
 * Created on March 3, 2005, 12:05 PM
 */

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import org.jdesktop.dataset.DataSet;

import org.jdesktop.dataset.DataTable;
import org.jdesktop.dataset.io.transfer.*;

/**
 *
 * @author patrick
 */
public class ImportExample {
    InputStream stream;
    
    /** Creates a new instance of ClipboardTest */
    public ImportExample(String resourceName) {
        try {
            stream = getClass().getResourceAsStream(resourceName);
        } catch (Exception e) {
            System.out.println("can't open resource " + resourceName);
            return;
        }
        if ( stream == null ) {
            System.err.println("resource does not exist: " + resourceName);
            return;
        }
    }
    
    private void run() {
        DataTable table = Importer.fromCSVWithHeaders(new DataSet(), "activity", new InputStreamReader(stream));
        Exporter.toSimpleXML("ActivityDS", table, new OutputStreamWriter(System.out));
    }
    
    
    public static void main(String args[]) {
        String fileName = ( args.length == 0 ? "resources/test-csv1.csv" : args[0] );
        new ImportExample(fileName).run();
    }
    
}
