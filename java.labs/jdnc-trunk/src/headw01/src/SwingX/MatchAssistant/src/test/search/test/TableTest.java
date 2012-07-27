package search.test;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.decorator.Filter;
import org.jdesktop.swingx.decorator.FilterPipeline;
import org.jdesktop.swingx.decorator.PatternFilter;
import org.jdesktop.swingx.renderer.DefaultTableRenderer;
import org.jdesktop.swingx.renderer.StringValue;

public class TableTest {

    public static void main(String[] args) {

        TestObject [][] data = new TestObject [][] {
                {new TestObject("Alpha")},
                {new TestObject("Bravo")},
                {new TestObject("Charlie")},
                {new TestObject("Delta")},
                {new TestObject("Echo")},
                {new TestObject("Foxtrot")},
                {new TestObject("Golf")},
                {new TestObject("Hotel")},
                {new TestObject("India")},
                {new TestObject("Juliett")},
                {new TestObject("Kilo")},
                {new TestObject("Lima")},
                {new TestObject("Mike")},
                {new TestObject("November")},
                {new TestObject("Oscar")},
                {new TestObject("Papa")},
                {new TestObject("Quebec")},
                {new TestObject("Romeo")},
                {new TestObject("Sierra")},
                {new TestObject("Tango")},
                {new TestObject("Uniform")},
                {new TestObject("Victor")},
                {new TestObject("Whiskey")},
                {new TestObject("X-ray")},
                {new TestObject("Yankee")},
                {new TestObject("Zulu")}
        };
        JXTable table = null;
        
        table = new JXTable(data, new String [] {"TheColumn"});
        
        table.setDefaultRenderer(Object.class, new DefaultTableRenderer(new StringValue() {
            @Override
            public String getString(Object value) {
                return null != value?((TestObject)value).getText():null;
            }
        }));
        
        Filter [] filters = new Filter[] {
                new PatternFilter(".*o.*", 0, 0)
        };
        table.setFilters(new FilterPipeline(filters));

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
