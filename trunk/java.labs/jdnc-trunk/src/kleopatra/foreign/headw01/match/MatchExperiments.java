/*
 * Created on 19.02.2008
 *
 */
package headw01.match;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.DefaultRowSorter;
import javax.swing.RowFilter;

import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.incubatorutil.AncientSwingTeam;
import org.jdesktop.swingx.incubatorutil.InteractiveTestCase;
import org.jdesktop.swingx.renderer.DefaultTableRenderer;
import org.jdesktop.swingx.renderer.StringValue;
import org.jdesktop.swingx.renderer.StringValues;
import org.jdesktop.swingx.search.SearchFactory;
import org.jdesktop.swingx.sort.RowFilters;

public class MatchExperiments extends InteractiveTestCase {

    public static void main(String args[]) {
//      setSystemLF(true);
        MatchExperiments test = new MatchExperiments();
        SearchFactory.getInstance().setUseFindBar(true);
      try {
        test.runInteractiveTests();
//          test.runInteractiveTests("interactive.*ColumnControl.*");
      } catch (Exception e) {
          System.err.println("exception when executing interactive tests:");
          e.printStackTrace();
      }
  }
    
    public void interactiveMultipleFilterMatch() {
        JXTable table = new JXTable(new AncientSwingTeam());
        RowFilter other = RowFilters.regexFilter(".*o.*", 0);
        RowFilter filter = RowFilters.regexFilter(".*e.*", 0);
        List<RowFilter<Object, Object>> filters = new ArrayList<RowFilter<Object, Object>>();
        filters.add(filter);
        filters.add(other);
        ((DefaultRowSorter) table.getRowSorter()).setRowFilter(RowFilter.andFilter(filters));
        JXFrame frame = wrapWithScrollingInFrame(table, "match and filters: first must be George");
        frame.setVisible(true);
    }

    public void interactiveHiddenColumnFilterMatch() {
        JXTable table = new JXTable(new AncientSwingTeam());
        table.setColumnControlVisible(true);
        StringValue sv = new StringValue() {

            public String getString(Object arg0) {
                return "x" + StringValues.TO_STRING.getString(arg0);
            }
            
        };
        table.getColumnExt(0).setCellRenderer(new DefaultTableRenderer(sv));
        table.getColumnExt(0).setVisible(false);
        RowFilter filter = RowFilters.regexFilter(".*x.*", 0);
        ((DefaultRowSorter) table.getRowSorter()).setRowFilter(filter);
        JXFrame frame = wrapWithScrollingInFrame(table, "match and filters: all must be shown");
        frame.setVisible(true);
    }
    
    public void testMultipleFilter() {
        JXTable otherTable = new JXTable(new AncientSwingTeam());
        RowFilter otherFilter = RowFilters.regexFilter(".*eo.*", 0);
        ((DefaultRowSorter) otherTable.getRowSorter()).setRowFilter(otherFilter);
        
        JXTable table = new JXTable(new AncientSwingTeam());
        RowFilter other = RowFilters.regexFilter(".*o.*", 0);
        RowFilter filter = RowFilters.regexFilter(".*e.*", 0);
        List<RowFilter<Object, Object>> filters = new ArrayList<RowFilter<Object, Object>>();
        filters.add(filter);
        filters.add(other);
        ((DefaultRowSorter) table.getRowSorter()).setRowFilter(RowFilter.andFilter(filters));
//        assertEquals(otherFilter.getValueAt(0, 0), table.getValueAt(0,0));
    }
}
