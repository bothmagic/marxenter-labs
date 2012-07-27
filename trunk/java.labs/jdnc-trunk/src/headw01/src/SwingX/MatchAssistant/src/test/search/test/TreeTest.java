package search.test;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;

import org.jdesktop.swingx.JXTree;
import org.jdesktop.swingx.renderer.DefaultTreeRenderer;
import org.jdesktop.swingx.renderer.StringValue;

public class TreeTest {

    public static void main(String[] args) {
        TestObject [] data = new TestObject [] {
                new TestObject("Alpha"),
                new TestObject("Bravo"),
                new TestObject("Charlie"),
                new TestObject("Delta"),
                new TestObject("Echo"),
                new TestObject("Foxtrot"),
                new TestObject("Golf"),
                new TestObject("Hotel"),
                new TestObject("India"),
                new TestObject("Juliett"),
                new TestObject("Kilo"),
                new TestObject("Lima"),
                new TestObject("Mike"),
                new TestObject("November"),
                new TestObject("Oscar"),
                new TestObject("Papa"),
                new TestObject("Quebec"),
                new TestObject("Romeo"),
                new TestObject("Sierra"),
                new TestObject("Tango"),
                new TestObject("Uniform"),
                new TestObject("Victor"),
                new TestObject("Whiskey"),
                new TestObject("X-ray"),
                new TestObject("Yankee"),
                new TestObject("Zulu"),
        };
        JXTree tree = null;
        
        tree = new JXTree(data);
        tree.setCellRenderer(new DefaultTreeRenderer(new StringValue() {
            @Override
            public String getString(Object value) {
                if (value instanceof String) {
                    return (String)value;
                }
                TestObject obj = null;
                if (value instanceof TestObject) {
                    obj = (TestObject)value;
                }
                else if (value instanceof DefaultMutableTreeNode) {
                    Object userObject = ((DefaultMutableTreeNode)value).getUserObject();
                    if (obj instanceof TestObject) {
                        obj = (TestObject)userObject;
                    }
                }
                return null != obj?((TestObject)obj).getText():null;
            }
        }));
        
        final JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(new Dimension(400, 400));
        frame.setLocationRelativeTo(null);
        frame.getContentPane().add(new JScrollPane(tree));
        
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
