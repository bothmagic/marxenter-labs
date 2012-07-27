package org.jdesktop.demo;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.Locale;
import org.jdesktop.dataset.DataColumn;
import org.jdesktop.dataset.DataRow;
import org.jdesktop.dataset.DataSet;
import org.jdesktop.dataset.DataTable;

/**
 * Brute force sample dataset.  Not to be used a shining example of reading
 * datasets from tab-separated values.
 */

public class SampleBugDataSet {

    static public final String ISSUES_TBL = "issues";
    static public final String ID_COL = "issue_id";
    static public final String STATUS_COL = "issue_status";
    static public final String PRIORITY_COL = "priority";
    static public final String RESOLUTION_COL = "resolution";
    static public final String COMPONENT_COL = "component";
    static public final String ASSIGNED_COL = "assigned_to";
    static public final String DELTA_COL = "delta_ts";
    static public final String SUBCOMPONENT_COL = "subcomponent";
    static public final String REPORTER_COL = "reporter";
    static public final String TYPE_COL = "issue_type";
    static public final String CREATED_COL = "creation_ts";
    static public final String VOTES_COL = "votes";
    static public final String SHORT_DESC_COL = "short_desc";

    static public enum IssueStatus { OPEN, ASSIGNED, CLOSED, RESOLVED, DUPLICATE, REJECTED };
    static public enum IssueType { BUG, FEATURE, PATCH };
                                                   
    static private DataSet dataset;

    static public synchronized DataSet getDataSet(String urlString) {
        if (dataset != null)
            return dataset;

        dataset = createDataSet();
        try {
            loadDataSet(dataset, null);
        }
        catch (IOException x) {
            x.printStackTrace();
            System.err.println("\nContinuing with empty dataset");
        }

        return dataset;
    }

    
    static private DataSet createDataSet() {
        DataSet ds = new DataSet();
        DataTable issues = ds.createTable(ISSUES_TBL);
        DataColumn id = issues.createColumn(ID_COL);
        id.setType(Integer.class);
        
        DataColumn status = issues.createColumn(STATUS_COL);
        status.setType(IssueStatus.class);
        
        DataColumn priority = issues.createColumn(PRIORITY_COL);
        priority.setType(Integer.class);
        
        DataColumn resolution = issues.createColumn(RESOLUTION_COL);
        resolution.setType(String.class);
        
        DataColumn component = issues.createColumn(COMPONENT_COL);
        component.setType(String.class);
        
        DataColumn assigned = issues.createColumn(ASSIGNED_COL);
        assigned.setType(String.class);
        
        DataColumn deltaTS = issues.createColumn(DELTA_COL);
        deltaTS.setType(Long.class);
        
        DataColumn subcomponent = issues.createColumn(SUBCOMPONENT_COL);
        subcomponent.setType(String.class);
        
        DataColumn reporter = issues.createColumn(REPORTER_COL);
        reporter.setType(String.class);
        
        DataColumn type = issues.createColumn(TYPE_COL);
        type.setType(IssueType.class);
     
        DataColumn creationTS = issues.createColumn(CREATED_COL);
        creationTS.setType(Date.class);
        
        DataColumn votes = issues.createColumn(VOTES_COL);
        votes.setType(Integer.class);
        
        DataColumn shortDesc = issues.createColumn(SHORT_DESC_COL);
        shortDesc.setType(String.class);

        return ds;
    }

    
    static private void loadDataSet(DataSet ds, String url) throws IOException {
        DataTable tbl = ds.getTable(ISSUES_TBL);
        try {
            LineNumberReader lnr = new LineNumberReader(
                                      new InputStreamReader(getDataURL(url).openStream()));
            String line = lnr.readLine();
            while (( line = lnr.readLine()) != null ) {
                append(tbl, line.split("\t"));
            }
        }
        catch ( Exception e ) {
            e.printStackTrace();
//             loadDefaultData();
        }
    }

    
    static private void append(DataTable table, String[] values) {
        DataRow row = table.appendRowNoEvent();
        row.setValue(ID_COL,Integer.valueOf(values[0]));
        row.setValue(STATUS_COL,Enum.valueOf(IssueStatus.class, values[1]));
        row.setValue(PRIORITY_COL,Integer.valueOf(values[2]));
        row.setValue(RESOLUTION_COL,values[3]);
        row.setValue(COMPONENT_COL,values[4]);
        row.setValue(ASSIGNED_COL,values[5]);
        row.setValue(DELTA_COL,Long.valueOf(values[6]));
        row.setValue(SUBCOMPONENT_COL,values[7]);
        row.setValue(REPORTER_COL,values[8]);
        row.setValue(TYPE_COL,Enum.valueOf(IssueType.class, values[9]));
        row.setValue(CREATED_COL,new Date(values[10]));
        row.setValue(VOTES_COL,Integer.valueOf(values[11]));
        row.setValue(SHORT_DESC_COL,values[12]);
    }

    
    static private URL getDataURL(String urlString) throws MalformedURLException {
        if (urlString != null) {
            return new URL(urlString);
        }

        //TODO: do it this way!!
//          URL url = SampleTableModel.class.getResource("/org/jdesktop/demo/sample/resources/weather.txt");

        // fallback to hardcoded url values
//         URL baseURL = app.getBaseURL(); // will be non-null if deployed from applet or webstart
        URL dataURL = SampleBugDataSet.class.getResource("/resources/issues.tsv");
//             urlString != null ? : 
//             new URL(baseURL, "resources/issues.tsv") :
//             new URL("file:" + System.getProperty("user.dir") +
//                     "/issues.tsv");
//                     "/src/dhall/demo/resources/issues.tsv");
        return dataURL;
    }

    static public void main(String[] args) {
        DataSet ds = getDataSet(null);
    }
}
