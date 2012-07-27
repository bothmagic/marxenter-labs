/*
 * Created on 14.07.2008
 *
 */
package netbeans.xoutline.rendererdemo;

import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;

import org.jdesktop.swingx.table.treetable.NodeChangedMediator;
import org.jdesktop.swingx.table.treetable.NodeModel;
import org.jdesktop.swingx.table.treetable.TreeTableModelAdapter;
import org.jdesktop.swingx.treetable.AbstractMutableTreeTableNode;
import org.jdesktop.swingx.treetable.DefaultMutableTreeTableNode;
import org.jdesktop.swingx.treetable.DefaultTreeTableModel;
import org.jdesktop.swingx.treetable.MutableTreeTableNode;
import org.jdesktop.swingx.treetable.TreeTableModel;
import org.jdesktop.swingx.treetable.TreeTableNode;

/**
 * 
 */
public class XRendererModels {

    public static TreeTableModel createWeatherTreeTableModelFrom(WeatherTableModel source) {
        WeatherNodeModel nodeModel = new WeatherNodeModel(source);
        WeatherTreeModel treeModel = new WeatherTreeModel(source, nodeModel.countryColumn, nodeModel.stationColumn);
        return new TreeTableModelAdapter(treeModel, nodeModel, NodeChangedMediator.DEFAULT);
        
    }
    
    static class WeatherTreeModel extends DefaultTreeModel {

        private int countryColumn;
        private int stationColumn;

        public WeatherTreeModel(WeatherTableModel source, int countryColumn, int stationColumn) {
            super(null);
            this.countryColumn = countryColumn;
            this.stationColumn = stationColumn;
            setRoot(createNodes(source));
        }
        
        private TreeNode createNodes(WeatherTableModel source) {
            DefaultMutableTreeNode root = new DefaultMutableTreeNode("Weather Stations");
            for (int i = 0; i < source.getRowCount(); i++) {
                Object country = source.getValueAt(i, countryColumn);
                MutableTreeNode parent = findParent(root, country);
                if (parent == null) {
                    parent = new DefaultMutableTreeNode(country);
                    root.insert(parent, root.getChildCount());
                }
                parent.insert(createChildNode(source, i), parent.getChildCount());
            }
            return root;
        }

        private MutableTreeNode createChildNode(WeatherTableModel source, int row) {
            List<Object> columns = new ArrayList<Object>();
            for (int i = 0; i < source.getColumnCount(); i++) {
               if (i != countryColumn) {
                   columns.add(source.getValueAt(row, i));
               }
            }
            Object station = columns.remove(stationColumn);
            columns.add(0, station);
            return new DefaultMutableTreeNode(columns);
        }

        private MutableTreeNode findParent(
                DefaultMutableTreeNode root, Object country) {
            for (int i = 0; i < root.getChildCount(); i++) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) root.getChildAt(i);
                if (country.equals(node.getUserObject())) {
                    return (MutableTreeNode) node;
                }
            }
            return null;
        }
        
    }
    
    static class WeatherNodeModel implements NodeModel {

        private int countryColumn;
        private List<Class<?>> classes;
        private int stationColumn;
        private List<String> rawIds;

        public WeatherNodeModel(WeatherTableModel source) {
            countryColumn = getCountryColumn(source);
            rawIds = createColumnIdentifiers(source);
            stationColumn = rawIds.indexOf("STATION");
            if (stationColumn < 0) throw new IllegalStateException("no station column");
            rawIds.remove(stationColumn);
            rawIds.add(0, "COUNTRY");
            classes = createColumnClasses(source);
        }

        private List<Class<?>> createColumnClasses(WeatherTableModel source) {
            List<Class<?>> classes = new ArrayList<Class<?>>();
            for (int i = 0; i < source.getColumnCount(); i++) {
                if (i != countryColumn) {
                    classes.add(source.getColumnClass(i));
                }
            }
            Class<?> station = classes.remove(stationColumn);
            classes.add(0, station);
            return classes;
        }

        private int getCountryColumn(WeatherTableModel source) {
            for (int i = 0; i < source.getColumnCount(); i++) {
                if ("COUNTRY".equals(source.getColumnName(i))) {
                    return i;
                }
               
            }
            throw new IllegalStateException("could not find country column");
        }

        private List<String> createColumnIdentifiers(WeatherTableModel source) {
            List<String> ids = new ArrayList<String>();
            for (int i = 0; i < source.getColumnCount(); i++) {
                if (!"COUNTRY".equals(source.getColumnName(i))) {
                    ids.add(source.getColumnName(i));
                }
            }
            return ids;
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            return classes.get(columnIndex);
        }

        @Override
        public int getColumnCount() {
            return classes.size();
        }

        @Override
        public String getColumnName(int column) {
            return rawIds.get(column);
        }

        @Override
        public int getHierarchicalColumn() {
            return 0;
        }

        @Override
        public Object getValueAt(Object node, int column) {
            DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) node; 
            Object userObject = treeNode.getUserObject();
            if (userObject instanceof List<?>) {
                return ((List<?>) userObject).get(column);
            }
            if (column == getHierarchicalColumn()) {
                return userObject;
            }
            return null;
        }

        @Override
        public boolean isCellEditable(Object node, int column) {
            return column > 0;
        }

        @Override
        public void setValueAt(Object value, Object node, int column) {
            DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) node; 
            Object userObject = treeNode.getUserObject();
            if (userObject instanceof List) {
                ((List<Object>) userObject).set(column, value);
            }
        }
        
    }
    
    static class WeatherTreeTableModel extends DefaultTreeTableModel {
        
        private int countryColumn;
        private List<Class<?>> classes;
        private int stationColumn;

        public WeatherTreeTableModel(WeatherTableModel source) {
            super();
            countryColumn = getCountryColumn(source);
            List<String> rawIds = createColumnIdentifiers(source);
            stationColumn = rawIds.indexOf("STATION");
            if (stationColumn < 0) throw new IllegalStateException("no station column");
            rawIds.remove(stationColumn);
            rawIds.add(0, "COUNTRY");
            setColumnIdentifiers(rawIds);
            classes = createColumnClasses(source);
            setRoot(createNodes(source));
        }

        
        @Override
        public Class<?> getColumnClass(int column) {
            return classes.get(column);
        }

        private List<Class<?>> createColumnClasses(WeatherTableModel source) {
            List<Class<?>> classes = new ArrayList<Class<?>>();
            for (int i = 0; i < source.getColumnCount(); i++) {
                if (i != countryColumn) {
                    classes.add(source.getColumnClass(i));
                }
            }
            Class<?> station = classes.remove(stationColumn);
            classes.add(0, station);
            return classes;
        }

        private int getCountryColumn(WeatherTableModel source) {
            for (int i = 0; i < source.getColumnCount(); i++) {
                if ("COUNTRY".equals(source.getColumnName(i))) {
                    return i;
                }
               
            }
            throw new IllegalStateException("could not find country column");
        }

        private List<String> createColumnIdentifiers(WeatherTableModel source) {
            List<String> ids = new ArrayList<String>();
            for (int i = 0; i < source.getColumnCount(); i++) {
                if (!"COUNTRY".equals(source.getColumnName(i))) {
                    ids.add(source.getColumnName(i));
                }
            }
            return ids;
        }

        private TreeTableNode createNodes(WeatherTableModel source) {
            DefaultMutableTreeTableNode root = new DefaultMutableTreeTableNode("Weather Stations");
            for (int i = 0; i < source.getRowCount(); i++) {
                Object country = source.getValueAt(i, countryColumn);
                MutableTreeTableNode parent = findParent(root, country);
                if (parent == null) {
                    parent = new DefaultMutableTreeTableNode(country);
                    root.insert(parent, root.getChildCount());
                }
                parent.insert(createChildNode(source, i), parent.getChildCount());
            }
            return root;
        }

        private MutableTreeTableNode createChildNode(WeatherTableModel source, int row) {
            List<Object> columns = new ArrayList<Object>();
            for (int i = 0; i < source.getColumnCount(); i++) {
               if (i != countryColumn) {
                   columns.add(source.getValueAt(row, i));
               }
            }
            Object station = columns.remove(stationColumn);
            columns.add(0, station);
            return new ListMutableTreeTableNode(columns);
        }

        private MutableTreeTableNode findParent(
                DefaultMutableTreeTableNode root, Object country) {
            for (int i = 0; i < root.getChildCount(); i++) {
                TreeTableNode node = root.getChildAt(i);
                if (country.equals(node.getUserObject())) {
                    return (MutableTreeTableNode) node;
                }
            }
            return null;
        }
        
    }
    
    public static class ListMutableTreeTableNode extends AbstractMutableTreeTableNode {
        

        public ListMutableTreeTableNode(List<Object> items) {
            super(items);
        }

        public int getColumnCount() {
            return getItems().size();
        }

        public Object getValueAt(int column) {
            return getItems().get(column);
        }

        @SuppressWarnings("unchecked")
        private List<Object> getItems() {
             return (List<Object>) getUserObject();
        }
        
        
    }
    // quick & dirty wrapper to get typed columns
    static class WeatherTableModel extends DefaultTableModel {
        String[] ids = { "ICAO", "STATION", "REGION", "COUNTRY", "ELEVATION", "LATITUDE", 
                "LONGITUDE", "TIMESTAMP", "TEMPERATURE", "DEWPOINT", "HUMIDITY",
                "VISIBILITY_QUAL", "VISIBILITY", "WIND_DIR", "WIND_DEG", "WIND_SPEED", "GUST_SPEED" };
        Class<?>[] classes = { null, null, null, null, Float.class, Float.class, 
                Float.class, Date.class, Float.class, Float.class, Integer.class,
                null, Float.class, null, Integer.class, Double.class, Double.class };
        
        @SuppressWarnings("unchecked")
        public void setWeatherData(DefaultTableModel data) {
            checkCompatibility(data);
            for (int i = 0; i < ids.length; i++) {
                addColumn(ids[i]);
            }
            for (int i = 0; i < data.getRowCount(); i++) {
                add((Vector<String>) data.getDataVector().get(i));
            }
        }

        
        @Override
        public Class<?> getColumnClass(int columnIndex) {
            return classes[columnIndex] == null ? super.getColumnClass(columnIndex)
                    : classes[columnIndex];
        }


        private void add(Vector<String> dataRow) {
           Object[] row = new Object[getColumnCount()]; 
           for (int i = 0; i < row.length; i++) {
               row[i] = getParsedObject(i, dataRow.get(i));
           }
           addRow(row);
        }

        private Object getParsedObject(int i, String value) {
            if ((value == null) || (value.length() == 0)) return null;
            if (Float.class.equals(classes[i])) {
                return Float.valueOf(value);
            } else if (Double.class.equals(classes[i])) {
                return Double.valueOf(value);
            } else if (Integer.class.equals(classes[i])) {
                return Integer.valueOf(value);
            } else if (Date.class.equals(classes[i])){
                return new Date(value);
            }
            return value;
        }


        // coarse check for compatibility
        private void checkCompatibility(TableModel data) {
            if (data.getColumnCount() != ids.length) 
                throw new IllegalArgumentException("unexpected column count");
            for (int i = 0; i < ids.length; i++) {
                if (!ids[i].equals(data.getColumnName(i))) 
                    throw new IllegalArgumentException("unexpected column id");
            }
            
        }
        
    }
    
    // simple loading
    static class SampleTableModel extends DefaultTableModel {
     
        void loadData() {
            try {
                URL url = XRendererDemo.class.getResource("resources/weather.txt");
                loadDataFromCSV(url);
            } catch ( Exception e ) {
                e.printStackTrace();
                loadDefaultData();
            }
        }
        
        private void loadDataFromCSV(URL url) {
            try {
                LineNumberReader lnr = new LineNumberReader(new InputStreamReader(url.openStream()));
                String line = lnr.readLine();
                String[] cols = line.split("\t");
                for ( String col : cols ) {
                    addColumn(col);
                }
                while (( line = lnr.readLine()) != null ) {
                    addRow(line.split("\t"));
                }
            } catch ( Exception e ) {
                e.printStackTrace();
                loadDefaultData();
            }
        }
        
        private void loadDefaultData() {
            int colCnt = 6;
            int rowCnt = 10;
            for ( int i=0; i < colCnt; i++ ) {
                addColumn("Column-" + (i + 1));
            }
            for ( int i=0; i <= rowCnt; i++ ) {
                String[] row = new String[colCnt];
                for ( int j=0; j < colCnt; j++ ) {
                    row[j] = "Row-" + i + "Column-" + (j + 1);
                }
                addRow(row);
            }
        }
    }
    

}
