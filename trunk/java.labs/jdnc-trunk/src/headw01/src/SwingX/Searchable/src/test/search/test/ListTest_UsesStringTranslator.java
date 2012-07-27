package search.test;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import org.jdesktop.swingx.JXList;
import org.jdesktop.swingx.JXList.ListSearchAssistant;
import org.jdesktop.swingx.util.ToStringTranslator;

public class ListTest_UsesStringTranslator {

    public static void main(String[] args) {
        TestObject [] data = new TestObject [] {
                new TestObject("Able"),
                new TestObject("Baker"),
                new TestObject("Charlie"),
                new TestObject("Dog"),
                new TestObject("Easy"),
                new TestObject("Fox")
        };
        JXList jxlist = null;
        
        jxlist = new JXList(data);
        
        jxlist.setCellRenderer(new DefaultListCellRenderer() {

            @Override
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                Component comp = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                
                ListSearchAssistant searchAssistant = ((JXList)list).getSearchable().getSearchAssistant();
                setText(searchAssistant.getStringTranslator().translateToString(value));
                
                return comp;
            }

        });
        ListSearchAssistant searchAssistant = jxlist.getSearchable().getSearchAssistant();
        searchAssistant.setStringTranslator(new ToStringTranslator() {
            @Override
            public String translateToString(Object obj) {
                return null != obj?((TestObject)obj).getText():null;
            }
        });
        
        final JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(new Dimension(400, 400));
        frame.setLocationRelativeTo(null);
        frame.getContentPane().add(new JScrollPane(jxlist));
        
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
