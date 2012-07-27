package search.test;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;

import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.JXTable.TableSearchAssistant;
import org.jdesktop.swingx.util.ToStringTranslator;

public class TableTest_UsesStringTranslator {

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
        
        table = new JXTable(data, new String [] {"TheColumn"});
        
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {

            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component comp = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                TableSearchAssistant searchAssistant = ((JXTable)table).getSearchable().getSearchAssistant();
                setText(searchAssistant.getStringTranslator(((JXTable)table).getColumnExt(column)).translateToString(value));
                
                return comp;
            }
            
        });
        TableSearchAssistant searchAssistant = table.getSearchable().getSearchAssistant();
        searchAssistant.setStringTranslator(table.getColumnExt(0),  new ToStringTranslator() {
            @Override
            public String translateToString(Object obj) {
                return null != obj?((TestObject)obj).getText():null;
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
