/*
 */
package netbeans.xoutline.rendererdemo;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableModel;

import netbeans.xoutline.JXXTreeTable;
import netbeans.xoutline.rendererdemo.XRendererModels.SampleTableModel;
import netbeans.xoutline.rendererdemo.XRendererModels.WeatherTableModel;

import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.JXTree;
import org.jdesktop.swingx.JXTreeTable;
import org.jdesktop.swingx.decorator.AbstractHighlighter;
import org.jdesktop.swingx.decorator.ColorHighlighter;
import org.jdesktop.swingx.decorator.ComponentAdapter;
import org.jdesktop.swingx.decorator.HighlightPredicate;
import org.jdesktop.swingx.decorator.HighlighterFactory;
import org.jdesktop.swingx.decorator.HighlightPredicate.AndHighlightPredicate;
import org.jdesktop.swingx.decorator.HighlightPredicate.DepthHighlightPredicate;
import org.jdesktop.swingx.decorator.HighlightPredicate.IdentifierHighlightPredicate;
import org.jdesktop.swingx.graphics.GraphicsUtilities;
import org.jdesktop.swingx.icon.EmptyIcon;
import org.jdesktop.swingx.renderer.DefaultTableRenderer;
import org.jdesktop.swingx.renderer.DefaultTreeRenderer;
import org.jdesktop.swingx.renderer.IconValue;
import org.jdesktop.swingx.renderer.MappedValue;
import org.jdesktop.swingx.renderer.StringValue;
import org.jdesktop.swingx.renderer.StringValues;
import org.jdesktop.swingx.renderer.WrappingProvider;
import org.jdesktop.swingx.table.ColumnFactory;
import org.jdesktop.swingx.treetable.TreeTableModel;


/**
 * Example of how to make extreme use of SwingX rendering support, namely IconValue
 * and StringValue. It's work-in-progress, evaluating the benefits and some 
 * (current :-) limitations. Will be updated soon.p>
 * 
 * Here we add a JXXTreeTable tab to see any differences.
 * <ul>
 * <li> need api to define per-node icon, looks like the hierarchical column
 * renderer cannot be treated completely like the other columns? IconValue
 * of the wrapped (table) renderer is ambigous, might be interpreted 
 * as node icon or as content icon. 
 * <li> trying to see if a convenience TreeTableModel implementatio 
 * which wraps NodeModel/TreeModel has any advantages.
 * </ul>
 * 
 * @author Patrick Wright (with help from the Swing tutorial :))
 * @author Jeanette Winzenburg
 */
public class XRendererDemo  {
    
    @SuppressWarnings("unused")
    private static final Logger LOG = Logger.getLogger(XRendererDemo.class
            .getName());
    
    private Map<Object, IconValue> iconValues;

    private CustomColumnFactory factory;

    private Map<Object, AbstractHighlighter> highlighters;
    
    public XRendererDemo() {
        factory = createAndConfigureColumnFactory();
        prepareRenderering();
    }

    private void prepareRenderering() {
        // init the converters for icon/text content
        iconValues = new HashMap<Object, IconValue>();
        // the icons to use for windDirection - we will fill this lazily
        LazyIconValue windIconValue = new LazyIconValue("compass/", ".png",
                null, null);
        iconValues.put("WIND_DIR", windIconValue);
        StringValue sv = new StringValue() {

            public String getString(Object value) {
                String key = value instanceof String ? (String) value
                        : StringValues.TO_STRING.getString(value);
                key = key.toLowerCase();
                key = key.replace(' ', '-');
                return key;
            }

        };
        LazyIconValue flagIconValue = new LazyIconValue("flags/", ".gif", sv,
                new EmptyIcon(30, 19));
        iconValues.put("COUNTRY", flagIconValue);
        
        // init some Highlighters:
        highlighters = new HashMap<Object, AbstractHighlighter>();
        // color foreground of negative temperature with frosty blue
        AbstractHighlighter temperature = new ColorHighlighter(
                new AndHighlightPredicate(
                        new IdentifierHighlightPredicate("TEMPERATURE"),
                        HighlightPredicate.INTEGER_NEGATIVE),
                null, Color.BLUE);
        highlighters.put("TEMPERATURE", temperature);
        AbstractHighlighter speed = new ValueBasedGradientHighlighter(10f, false);
        speed.setHighlightPredicate(new IdentifierHighlightPredicate("WIND_SPEED"));
        highlighters.put("WIND_SPEED", speed);
        // a tooltip to show on mouseover the winddir column
        // it is showing the value of another column (wind_degree)
        ToolTipHighlighter windDir = new ToolTipHighlighter();
        windDir.setHighlightPredicate(new IdentifierHighlightPredicate("WIND_DIR"));
        windDir.addStringValue(new LabeledStringValue("Wind Degree: "), "WIND_DEG");
        highlighters.put("WIND_DIR", windDir);
        // a tooltip to show on mouseover the station column
        // it's composed of values of other columns in the view row
        ToolTipHighlighter station = createStationToolTipHighlighter();
        station.setHighlightPredicate(new IdentifierHighlightPredicate("STATION"));
        highlighters.put("STATION", station);
        // the treetable has a merged station/country column. So the predicate 
        // is different - the only reason we need another instance of the highlighter
        ToolTipHighlighter stationTreeTable = createStationToolTipHighlighter();
        stationTreeTable.setHighlightPredicate(new AndHighlightPredicate(
                new IdentifierHighlightPredicate("COUNTRY"),
                        HighlightPredicate.ROLLOVER_ROW));
        highlighters.put("COUNTRY", stationTreeTable);
        
    }

    private ToolTipHighlighter createStationToolTipHighlighter() {
        ToolTipHighlighter station = new ToolTipHighlighter();
        station.addStringValue(new LabeledStringValue("Elevation: "), "ELEVATION");
        station.addStringValue(new LabeledStringValue("Latitude: "), "LATITUDE");
        station.addStringValue(new LabeledStringValue("Longitude: "), "LONGITUDE");
        station.setDelimiter("; ");
        return station;
    }

    /**
     * A custom IconValue which maps cell value to an icon. The value is mapped
     * to a filename with a StringValue. The icons are loaded lazyly.
     */
    public static class LazyIconValue implements IconValue {
        Class<?> baseClass;
        StringValue keyToFileName;
        String dir;
        String extension;
        Map<Object, Icon> iconCache;
        Icon emptyIcon;
        
        public LazyIconValue(String dir, String extension, StringValue sv, Icon emptyIcon) {
           iconCache = new HashMap<Object, Icon>(); 
           this.dir = dir;
           this.extension = extension;
           this.keyToFileName = sv;
           this.emptyIcon = emptyIcon != null ? emptyIcon : NULL_ICON; 
        }
        
        public Icon getIcon(Object value) {
            if (value == null) return null;
            String key = value.toString();
            Icon icon = iconCache.get(key);
            if (icon ==  null) {
                icon = loadIcon(key);
             }
            return icon;
        }
        
        private Icon loadIcon(String key) {
            Icon icon = loadFromResource(getKeyFromValue(key));
            if (icon == null) {
                // no luck
                icon = emptyIcon;
            }
            iconCache.put(key, icon);
            return icon;
        }
        
        private String getKeyFromValue(String key) {
            return keyToFileName == null ? key : keyToFileName.getString(key);
        }

        protected Icon loadFromResource(String name) {
            URL url = getClass().getResource("resources/images/" + dir + name + extension);
            if (url == null) return null;
            try {
                BufferedImage image = ImageIO.read(url);
                if (image.getHeight() > 30) {
                    image = GraphicsUtilities.createThumbnail(image, 30);
                }
                return new ImageIcon(image);
            } catch (IOException e) {
            }
            return null;
        }

        
    }
    
    /**
     * A StringValue which prepends a fixed text to the value.
     */
    public static class LabeledStringValue implements StringValue {
        
        private String label;

        public LabeledStringValue(String text) {
            this.label = text;
        }

        public String getString(Object value) {
            return value != null ? label + value : "";
        }
        
    }
    
    /**
     * 
     */
    public static class ToolTipHighlighter extends AbstractHighlighter {
        
        private List<StringValue> stringValues;
        private List<Object> sourceColumns;
        private String delimiter; 
        
        
        /**
         * Adds a StringValue to use on the given sourceColumn.
         * 
         * @param sv the StringValue to use.
         * @param sourceColumn the column indices in 
         */
        public void addStringValue(StringValue sv, Object sourceColumn) {
           if (stringValues == null) {
               stringValues = new ArrayList<StringValue>();
               sourceColumns = new ArrayList<Object>();
           }
           stringValues.add(sv);
           sourceColumns.add(sourceColumn);
        }

        /**
         * Sets the delimiter to use between StringValues.
         * 
         * @param delimiter the delimiter to use between StringValues, if there are more than one.
         */
        public void setDelimiter(String delimiter) {
            this.delimiter = delimiter;
        }
        
        @Override
        protected Component doHighlight(Component component,
                ComponentAdapter adapter) {
            String toolTip = getToolTipText(component, adapter);
            if (toolTip != null) {
                ((JComponent) component).setToolTipText(toolTip);
            } else {
                if (component instanceof JXTree) {
                    // Hack around #794-swingx: 
                    // PENDING: treetableCellRenderer doesn't reset tooltip
                    ((JComponent) component).setToolTipText(toolTip);
                }
            }
            return component;
        }
        
        private String getToolTipText(Component component,
                ComponentAdapter adapter) {
            if ((stringValues == null) || stringValues.isEmpty()) return null;
            String text = "";
            for (int i = 0; i < stringValues.size(); i++) {
                int modelIndex = adapter.getColumnIndex(sourceColumns.get(i));
                if (modelIndex >= 0) {
                   text += stringValues.get(i).getString(adapter.getValue(modelIndex));
                   if ((i != stringValues.size() - 1) && !isEmpty(text)){
                       text += delimiter;
                   }
                }
            }
            return isEmpty(text) ? null : text;
        }

        private boolean isEmpty(String text) {
            return text.length() == 0;
        }

        /**
         * Overridden to check for JComponent type.
         */
        @Override
        protected boolean canHighlight(Component component,
                ComponentAdapter adapter) {
            return component instanceof JComponent;
        }
        
    }


    /**
     * Creates, configures and installs a custom ColumnFactory. 
     */
    private CustomColumnFactory createAndConfigureColumnFactory() {
        CustomColumnFactory factory = new CustomColumnFactory();
        factory.addExcludeNames("REGION", "ICAO",
                "TIMESTAMP", //"DEWPOINT", "HUMIDITY",
                "VISIBILITY_QUAL", "VISIBILITY", "GUST_SPEED" );
        factory.addHiddenNames("LATITUDE", 
                "LONGITUDE", "DEWPOINT", "HUMIDITY",
                "WIND_DEG");
        StringValue sv = new StringValue() {

            public String getString(Object value) {
                if (!(value instanceof String)) return StringValues.TO_STRING.getString(value);
                String title = (String) value;
                title = title.substring(0, 1).toUpperCase() + title.substring(1).toLowerCase();
                return title.replace('_', ' ');
            }
            
        };
        factory.setTitleStringValue(sv);
        return factory;
    }
    

    /**
     * Configures the table with the given model. Here we fine-tune properties
     * which might be model (meta-data) dependent.
     * 
     * @param table
     * @param weather
     */
    private void configureTable(JXTable table, TableModel weather, ColumnFactory factory) {
        // if we would want a per-table ColumnFactory we would have
        // to set it here, before setting the model
        table.setColumnFactory(factory);
        table.setModel(weather);
        // show both the string representation and the nation's flag
        table.getColumnExt("COUNTRY").setCellRenderer(
                new DefaultTableRenderer(new MappedValue(StringValues.TO_STRING,
                        iconValues.get("COUNTRY"))));
        table.getColumnExt("WIND_DIR").setCellRenderer(new DefaultTableRenderer(
                new MappedValue(null, iconValues.get("WIND_DIR")),
                JLabel.CENTER));
        // add highlighters
        // tooltips
         table.addHighlighter(highlighters.get("WIND_DIR"));
        table.addHighlighter(highlighters.get("STATION"));
        
        // highlight the temperature column with a frosty foreground
         table.addHighlighter(highlighters.get("TEMPERATURE"));
           
        // highlight the wind-speed column with a speed proportional gradient
        table.addHighlighter(highlighters.get("WIND_SPEED")); 
         
        // now that we have some data, configure properties which might depend
        // on the data
        int rowHeight = 0;
        for (int i = 0; i < table.getColumnCount(); i++) {
            Component comp = table.prepareRenderer(table.getCellRenderer(0, i),
                    0, i);
            rowHeight = Math.max(rowHeight, comp.getPreferredSize().height);
        }
        if (table.getRowHeight() < rowHeight) {
            int prefHeight = table.getPreferredScrollableViewportSize().height;
            table.setRowHeight(Math.max(table.getRowHeight(), rowHeight));
            table.setVisibleRowCount(prefHeight / rowHeight);
        }
        table.setVisibleColumnCount(table.getColumnCount(false));

        
        // icon loading is costly, so we don't want to load them just for
        // measuring
        // limit the number of rows for measuring
        table.putClientProperty("ColumnFactory.maxRowCount", 50);

        int margin = 5;
        table.packTable(margin);

        // we want the country name to always show, so we'll repack just that
        // column
        // we can set a max size; if -1, the column is forced to be as large as
        // necessary for the
        // text
        margin = 10;
        int max = -1;
        int viewIndex = table.convertColumnIndexToView(table.getColumnExt(
                "COUNTRY").getModelIndex());
        table.packColumn(viewIndex, margin, max);

    }

    private void configureTreeTable(TreeTableModel model,
            JXTreeTable treeTable, ColumnFactory factory, int rowHeight, int rowCount) {
        treeTable.setColumnFactory(factory);
        treeTable.setTreeTableModel(model);
        treeTable.expandAll();
//        treeTable.setShowGrid(false, true);
        treeTable.setLargeModel(true);
        // show both the string representation and the nation's flag
        StringValue sv = new StringValue() {

            public String getString(Object value) {
                if (value instanceof List) {
                    value = ((List) value).get(0);
                }
                return StringValues.TO_STRING.getString(value);
            }
            
        };
        treeTable.setTreeCellRenderer(
                new DefaultTreeRenderer(iconValues.get("COUNTRY"),  sv));

        treeTable.getColumnExt("WIND_DIR").setCellRenderer(new DefaultTableRenderer(
                new MappedValue(null, iconValues.get("WIND_DIR")),
                JLabel.CENTER));

        // add highlighters
        // color for parent (country only) nodes
        treeTable.addHighlighter(new ColorHighlighter(new DepthHighlightPredicate(1), 
                HighlighterFactory.FLORAL_WHITE, null));
        // tooltips
        treeTable.addHighlighter(highlighters.get("WIND_DIR"));
        treeTable.addHighlighter(highlighters.get("COUNTRY"));
        
        // highlight the temperature column with a frosty foreground
        treeTable.addHighlighter(highlighters.get("TEMPERATURE"));
           
        // highlight the wind-speed column with a speed proportional gradient
        treeTable.addHighlighter(highlighters.get("WIND_SPEED")); 

        
        treeTable.setRowHeight(rowHeight);
        treeTable.setVisibleRowCount(rowCount);
        // now that we have some data, configure properties which might depend
        // on the data
//        int rowHeight = 0;
//        for (int i = 0; i < treeTable.getColumnCount(); i++) {
//            Component comp = treeTable.prepareRenderer(treeTable.getCellRenderer(0, i),
//                    0, i);
//            rowHeight = Math.max(rowHeight, comp.getPreferredSize().height);
//        }
//        if (treeTable.getRowHeight() < rowHeight) {
//            int prefHeight = treeTable.getPreferredScrollableViewportSize().height;
//            treeTable.setRowHeight(Math.max(treeTable.getRowHeight(), rowHeight));
//            treeTable.setVisibleRowCount(prefHeight / rowHeight);
//        }
        treeTable.setVisibleColumnCount(treeTable.getColumnCount(false));

        
        // icon loading is costly, so we don't want to load them just for
        // measuring
        // limit the number of rows for measuring
        treeTable.putClientProperty("ColumnFactory.maxRowCount", 100);

//        int margin = 5;
//        treeTable.packTable(margin);

        // we want the country name to always show, so we'll repack just that
        // column
        // we can set a max size; if -1, the column is forced to be as large as
        // necessary for the
        // text
        int margin = 10;
        int max = -1;
        int viewIndex = treeTable.convertColumnIndexToView(treeTable.getColumnExt(
                "COUNTRY").getModelIndex());
        treeTable.packColumn(viewIndex, margin, max);
    }

    private void configureOutline(TreeTableModel model,
            JXXTreeTable treeTable, ColumnFactory factory, int rowHeight, int rowCount) {
        treeTable.setColumnFactory(factory);
        treeTable.setTreeTableModel(model);
        treeTable.expandAll();
//        treeTable.setShowGrid(false, true);
        treeTable.setLargeModel(true);
        // show both the string representation and the nation's flag
        StringValue sv = new StringValue() {

            public String getString(Object value) {
                if (value instanceof List) {
                    value = ((List) value).get(0);
                }
                return StringValues.TO_STRING.getString(value);
            }
            
        };
        
        WrappingProvider provider = new WrappingProvider(iconValues.get("COUNTRY"), sv);
        treeTable.getColumn(0).setCellRenderer(
                new DefaultTableRenderer(provider));

        treeTable.getColumnExt("WIND_DIR").setCellRenderer(new DefaultTableRenderer(
                new MappedValue(null, iconValues.get("WIND_DIR")),
                JLabel.CENTER));

        // add highlighters
        // color for parent (country only) nodes
        treeTable.addHighlighter(new ColorHighlighter(new DepthHighlightPredicate(1), 
                HighlighterFactory.FLORAL_WHITE, null));
        // tooltips
        treeTable.addHighlighter(highlighters.get("WIND_DIR"));
        treeTable.addHighlighter(highlighters.get("COUNTRY"));
        
        // highlight the temperature column with a frosty foreground
        treeTable.addHighlighter(highlighters.get("TEMPERATURE"));
           
        // highlight the wind-speed column with a speed proportional gradient
        treeTable.addHighlighter(highlighters.get("WIND_SPEED")); 

        
        treeTable.setRowHeight(rowHeight);
        treeTable.setVisibleRowCount(rowCount);
        // now that we have some data, configure properties which might depend
        // on the data
//        int rowHeight = 0;
//        for (int i = 0; i < treeTable.getColumnCount(); i++) {
//            Component comp = treeTable.prepareRenderer(treeTable.getCellRenderer(0, i),
//                    0, i);
//            rowHeight = Math.max(rowHeight, comp.getPreferredSize().height);
//        }
//        if (treeTable.getRowHeight() < rowHeight) {
//            int prefHeight = treeTable.getPreferredScrollableViewportSize().height;
//            treeTable.setRowHeight(Math.max(treeTable.getRowHeight(), rowHeight));
//            treeTable.setVisibleRowCount(prefHeight / rowHeight);
//        }
        treeTable.setVisibleColumnCount(treeTable.getColumnCount(false));

        
        // icon loading is costly, so we don't want to load them just for
        // measuring
        // limit the number of rows for measuring
        treeTable.putClientProperty("ColumnFactory.maxRowCount", 100);

//        int margin = 5;
//        treeTable.packTable(margin);

        // we want the country name to always show, so we'll repack just that
        // column
        // we can set a max size; if -1, the column is forced to be as large as
        // necessary for the
        // text
        int margin = 10;
        int max = -1;
        int viewIndex = treeTable.convertColumnIndexToView(treeTable.getColumnExt(
                "COUNTRY").getModelIndex());
        treeTable.packColumn(viewIndex, margin, max);
    }

    /**
     * Creates a JXTable and configure default properties. This methods
     * simulates a component factory which should be the central place 
     * to get all components from. It guarantees that the defaults
     * are always the same and easily changeable for all.
     *  
     * @return 
     */
    private JXTable createTable() {
        JXTable table = new JXTable();
        configureDefaults(table);
        return table;
    }

    /**
     * Creates a JXTreeTable and configures default properties. 
     * @return
     */
    private JXTreeTable createTreeTable() {
        JXTreeTable treeTable = new JXTreeTable();
        configureDefaults(treeTable);
        return treeTable;
    }
    
    /**
     * Creates a JXTreeTable and configures default properties. 
     * @return
     */
    private JXXTreeTable createOutline() {
        JXXTreeTable treeTable = new JXXTreeTable();
        configureDefaults(treeTable);
        return treeTable;
    }

    private void configureDefaults(JXTable table) {
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        // This turns horizontal scrolling on or off. If the table is too large for the scrollpane,
        // and horizontal scrolling is off, columns will be resized to fit within the pane, which can
        // cause them to be unreadable. Setting this flag causes the table to be scrollable right to left.
        table.setHorizontalScrollEnabled(true);

        // This shows the column control on the right-hand of the header.
        // All there is to it--users can now select which columns to view
        table.setColumnControlVisible(true);
        
        // set the number of visible rows
        table.setVisibleRowCount(30);
        // set the number of visible columns
        table.setVisibleColumnCount(8);
        
        // We'll add a highlighter to offset different row numbers
        // Note the setHighlighters() takes an array parameter; you can chain these together.
        table.setHighlighters(
                HighlighterFactory.createSimpleStriping());
        // read-only
//        table.setEditable(false);
    }
    

    /** 
     * Creates and configures a JXTable. 
     * 
     */
    private JXTable initTable() {
        JXTable table = createTable();
        configureTable(table, createWeatherData(), factory);
        return table;
    }


    private JXTreeTable initTreeTable(WeatherTableModel model, int rowHeight, int rowCount) {
        JXTreeTable treeTable = createTreeTable();
        TreeTableModel treeTableModel = XRendererModels.createWeatherTreeTableModelFrom(model);//new WeatherTreeTableModel(model);
        configureTreeTable(treeTableModel, treeTable, factory, rowHeight, rowCount);
        return treeTable;
    }

    private JXXTreeTable initOutline(TreeTableModel model, int rowHeight, int rowCount){
        JXXTreeTable treeTable = createOutline();
        configureOutline(model, treeTable, factory, rowHeight, rowCount);
        return treeTable;
    }

    private JComponent initUI() {
        JTabbedPane pane = new JTabbedPane();
        JXTable jxTable = initTable();
        pane.add("JXTable", new JScrollPane(jxTable));
        JXTreeTable treeTable = initTreeTable((WeatherTableModel) jxTable.getModel(), jxTable.getRowHeight(), jxTable.getVisibleRowCount());
        pane.add("JXTreeTable", new JScrollPane(treeTable));

        JXXTreeTable outline = initOutline(treeTable.getTreeTableModel(), jxTable.getRowHeight(), jxTable.getVisibleRowCount());
        pane.add("JXXTreeTable", new JScrollPane(outline));
        JComponent content = new JPanel(new BorderLayout());
        content.add(pane, BorderLayout.CENTER);
         return content;
    }

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    private static void createAndShowGUI() {
//        String nimbus = "com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel";
//        try {
//            UIManager.setLookAndFeel(nimbus);
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        } 
         //Make sure we have nice window decorations.
        JFrame.setDefaultLookAndFeelDecorated(true);
        
        //Create and set up the window.
        JXFrame frame = new JXFrame("XTableDemo", true);
        
        //Create and set up the content pane.
        JComponent newContentPane = new XRendererDemo().initUI();
        newContentPane.setOpaque(true); //content panes must be opaque
        frame.setContentPane(newContentPane);
        
        //Display the window.
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }

    

    private WeatherTableModel createWeatherData() {
        SampleTableModel model = new SampleTableModel();
        model.loadData();
        WeatherTableModel weather = new WeatherTableModel();
        weather.setWeatherData(model);
        return weather;
    }


}
