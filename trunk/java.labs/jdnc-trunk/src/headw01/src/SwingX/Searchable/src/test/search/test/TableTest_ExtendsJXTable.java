package search.test;

import java.awt.Component;
import java.awt.Dimension;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;

import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.Searchable;
import org.jdesktop.swingx.AbstractSearchable.SearchResult;

public class TableTest_ExtendsJXTable {

    public static void main(String[] args) {
        TestObject [][] data = new TestObject [][] {
                {new TestObject("Able")},
                {new TestObject("Baker")},
                {new TestObject("Charlie")},
                {new TestObject("Dog")},
                {new TestObject("Easy")},
                {new TestObject("Fox")}
        };
        JXTable table = null;
        
        boolean makeSearchWork = true;
        if (makeSearchWork) {
            table = new JXTableExtension(data, new String [] {"TheColumn"});
        }
        else {
            table = new JXTable(data, new String [] {"TheColumn"});
        }
        
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {

            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component comp = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                setText(((TestObject)value).getText());
                
                return comp;
            }
            
        });
        
        final JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(new Dimension(400, 400));
        frame.setLocationRelativeTo(null);
        frame.getContentPane().add(new JScrollPane(table));
        
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                frame.setVisible(true);
            }
        });
    }

    private static final class JXTableExtension extends JXTable {
        public JXTableExtension(Object[][] rowData, Object[] columnNames) {
            super(rowData, columnNames);
        }

        protected Searchable<TableSearchAssistant> createSearchable() {
            Searchable<TableSearchAssistant> searchable = new TableSearchable() {
                @Override protected SearchResult findMatchAt(Pattern pattern, int row, int column) {
                    return TableTest_ExtendsJXTable.findMatchAt(JXTableExtension.this, pattern, row, column);
                }
            };

            return searchable;
        }
    }
    private static  SearchResult findMatchAt(JXTable table, Pattern pattern, int row, int column) {
        TestObject value = (TestObject) table.getValueAt(row, column);

        if (value != null) {
            String txt = value.getText();

            if (null != txt) {
                Matcher matcher = pattern.matcher(txt);

                if (matcher.find()) {
                    return new SearchResult(matcher.pattern(), matcher.toMatchResult(), row, column);
                }
            }
        }

        return null;
    }
    
    private static class TestObject {
        private String text = null;
        
        public TestObject(String text) {
            this.text = text;
        }
        
        public String getText() {
            return text;
        }
    }
}
