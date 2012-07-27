package search.test;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreePath;

import org.jdesktop.swingx.JXTree;
import org.jdesktop.swingx.JXTree.TreeToStringTranslator;
import org.jdesktop.swingx.JXTree.TreeSearchAssistant;

public class TreeTest_UsesStringTranslator {

    public static void main(String[] args) {
        TestObject [] data = new TestObject [] {
                new TestObject("Able"),
                new TestObject("Baker"),
                new TestObject("Charlie"),
                new TestObject("Dog"),
                new TestObject("Easy"),
                new TestObject("Fox")
        };
        JXTree tree = null;
        
        tree = new JXTree(data);
        
        tree.setCellRenderer(new DefaultTreeCellRenderer() {

            
            @Override
            public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
                Component comp = super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
                
                TreeSearchAssistant searchAssistant = ((JXTree)tree).getSearchable().getSearchAssistant();
                TreePath path = tree.getPathForRow(row);
                setText(searchAssistant.getStringTranslator((JXTree)tree).translateToString(path));
                
                return comp;
            }

        });
        TreeSearchAssistant searchAssistant = tree.getSearchable().getSearchAssistant();
        searchAssistant.setStringTranslator(tree, new TreeToStringTranslator(tree) {
            
            @Override
            public String translateToString(Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
                TestObject obj = null;
                if (value instanceof DefaultMutableTreeNode) {
                    obj = (TestObject)((DefaultMutableTreeNode)value).getUserObject();
                }
                return null != obj?((TestObject)obj).getText():null;
            }
        });
        
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
