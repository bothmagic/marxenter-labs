/*
 * Created on 22.07.2008
 *
 */
package netbeans.xoutline.rendererdemo;

import java.awt.Color;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.border.Border;
import javax.swing.tree.DefaultTreeModel;

import netbeans.xoutline.JXXTreeTable;

import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXTree;
import org.jdesktop.swingx.decorator.AbstractHighlighter;
import org.jdesktop.swingx.decorator.ComponentAdapter;
import org.jdesktop.swingx.decorator.Highlighter;
import org.jdesktop.swingx.incubatorutil.InteractiveTestCase;
import org.jdesktop.swingx.renderer.CheckBoxProvider;
import org.jdesktop.swingx.renderer.DefaultTableRenderer;
import org.jdesktop.swingx.renderer.DefaultTreeRenderer;
import org.jdesktop.swingx.renderer.StringValue;
import org.jdesktop.swingx.renderer.StringValues;
import org.jdesktop.swingx.renderer.WrappingIconPanel;
import org.jdesktop.swingx.renderer.WrappingProvider;
import org.jdesktop.swingx.table.treetable.NodeChangedMediator;
import org.jdesktop.swingx.table.treetable.NodeModel;
import org.jdesktop.swingx.table.treetable.TreeTableModelAdapter;
import org.jdesktop.swingx.treetable.TreeTableModelAdapterTest;

public class RendererVisualCheck extends InteractiveTestCase {
    public static void main(String[] args) {
        RendererVisualCheck test = new RendererVisualCheck();
        try {
            test.runInteractiveTests();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private DefaultTreeModel treeModel;
    private NodeModel nodeModel;
    private TreeTableModelAdapter treeTableModel;
    
    public void interactiveCheckTreeTable() {
        JXXTreeTable table = new JXXTreeTable(treeTableModel);
        table.setEditable(true);
        table.getColumn(0).setCellRenderer(new DefaultTableRenderer(
                new CheckBoxProvider(StringValues.TO_STRING, JLabel.LEADING)));
        JXFrame frame = showWithScrollingInFrame(table, "Check in JXxTreeTabel");
        addComponentOrientationToggle(frame);
        show(frame);
    }
    
    public void interactiveCheckXTree() {
        JXTree table = new JXTree(treeTableModel);
        table.setEditable(true);
        table.setCellRenderer(new DefaultTreeRenderer(new WrappingProvider(
                new CheckBoxProvider(StringValues.TO_STRING, JLabel.LEADING))));
        Highlighter hl = new AbstractHighlighter() {
            Border border = BorderFactory.createLineBorder(Color.RED);
            @Override
            protected Component doHighlight(Component component,
                    ComponentAdapter adapter) {
                if (component instanceof WrappingIconPanel) {
                    ((JComponent) ((WrappingIconPanel) component).getComponent(1)).setBorder(border);
                }
                return component;
            }
            
        };
        table.addHighlighter(hl);
        JXFrame frame = showWithScrollingInFrame(table, "Check in JXTree");
        addComponentOrientationToggle(frame);
        show(frame);
    }

    public void interactiveCheckTree() {
        JTree table = new JXTree(treeTableModel);
        table.setEditable(true);
        table.setCellRenderer(new DefaultTreeRenderer(new WrappingProvider(
                new CheckBoxProvider(StringValues.TO_STRING, JLabel.LEADING))));
        showWithScrollingInFrame(table, "Check in JXTree");
    }

    @Override
    protected void setUp() throws Exception {
        treeModel = (DefaultTreeModel) new JTree().getModel();
        nodeModel = TreeTableModelAdapterTest.createDefaultNodeModel();
        treeTableModel = new TreeTableModelAdapter(treeModel, nodeModel, NodeChangedMediator.DEFAULT);
    }

}
