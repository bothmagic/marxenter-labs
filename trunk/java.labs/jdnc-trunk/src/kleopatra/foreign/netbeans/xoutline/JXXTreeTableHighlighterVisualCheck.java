/*
 * Created on 27.06.2008
 *
 */
package netbeans.xoutline;

import java.awt.Color;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTree;

import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.JXTree;
import org.jdesktop.swingx.decorator.BorderHighlighter;
import org.jdesktop.swingx.decorator.ColorHighlighter;
import org.jdesktop.swingx.decorator.HighlightPredicate;
import org.jdesktop.swingx.decorator.HighlighterFactory;
import org.jdesktop.swingx.decorator.PatternPredicate;
import org.jdesktop.swingx.decorator.ShadingColorHighlighter;
import org.jdesktop.swingx.decorator.HighlightPredicate.AndHighlightPredicate;
import org.jdesktop.swingx.decorator.HighlightPredicate.ColumnHighlightPredicate;
import org.jdesktop.swingx.decorator.HighlightPredicate.DepthHighlightPredicate;
import org.jdesktop.swingx.renderer.DefaultTreeRenderer;

/**
 * Visual checks: Highlighters, rowheight ...
 */
public class JXXTreeTableHighlighterVisualCheck extends JXXTreeTableUnitTest {

    @SuppressWarnings("unused")
    private static final Logger LOG = Logger
            .getLogger(JXXTreeTableHighlighterVisualCheck.class.getName());
    
    public static void main(String[] args) {
        JXXTreeTableHighlighterVisualCheck test = new JXXTreeTableHighlighterVisualCheck();
        try {
//            test.runInteractiveTests();
            test.runInteractiveTests("interactive.*Grid.*");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void interactiveNimbusProbs() {
        JXXTreeTable treeTable = new JXXTreeTable(treeTableModel);
        JTable table = new JXTable(treeTable.getModel());
        JXTree tree = new JXTree(treeTableModel);
        tree.setCellRenderer(new DefaultTreeRenderer());
        JTabbedPane pane = new JTabbedPane();
        pane.addTab("XTable", new JScrollPane(table));
        pane.addTab("Table", new JScrollPane(new JTable(treeTable.getModel())));
        pane.addTab("XTree", new JScrollPane(tree));
        pane.addTab("Tree", new JScrollPane(new JTree(treeTableModel)));
        pane.addTab("XXTreeTable", new JScrollPane(treeTable));
        JXFrame frame = wrapInFrame(pane, "Nimbus probs ", true);
        show(frame);
    }

    /**    issue #148
     *   did not work on LFs which normally respect lineStyle
     *   winLF does not respect it anyway...
     */    
    public void interactiveTestFilterHighlightAndLineStyle() {
        JXXTreeTable treeTable = new JXXTreeTable(treeTableModel);
        // issue #148
        // did not work on LFs which normally respect lineStyle
        // winLF does not respect it anyway...
        treeTable.putClientProperty("JTree.lineStyle", "Angled");
        treeTable.setRowHeight(22);
        showInFrame(new JScrollPane(treeTable),
                "LineStyle should follow LF?", true);
    }

    
    /**
     * table background and striping.
     */
    public void interactiveTestLedgerBackground() {
        JXXTreeTable treeTable = new JXXTreeTable(treeTableModel);
        treeTable.setBackground(new Color(0xF5, 0xFF, 0xF5)); // ledger
        treeTable.addHighlighter(
                HighlighterFactory.createSimpleStriping(HighlighterFactory.GENERIC_GRAY));
        treeTable.setGridColor(Color.cyan.darker());
        treeTable.setRowHeight(22);
        treeTable.setShowGrid(true, false);
        showWithScrollingInFrame(treeTable, "LedgerBackground and simple striping");
    }

    /**
     * Requirement: color the leafs of the hierarchical columns differently.
     * 
     * http://forums.java.net/jive/thread.jspa?messageID=165876
     * 
     *
     */
    public void interactiveTestHierarchicalColumnHighlightConditional() {
        JXXTreeTable treeTable = new JXXTreeTable(treeTableModel);
        HighlightPredicate hierarchical = new ColumnHighlightPredicate(0);
        treeTable.addHighlighter(new ShadingColorHighlighter(hierarchical));
        ColorHighlighter highlighter = new ColorHighlighter(
                new AndHighlightPredicate(hierarchical, HighlightPredicate.IS_LEAF),
                new Color(247,246,239), null);
        treeTable.addHighlighter(highlighter);
        showWithScrollingInFrame(treeTable,  "highlight hierarchical column of leafs");
    }
    

    public void interactiveTestHighlighters() {
        JXXTreeTable treeTable = new JXXTreeTable(treeTableModel);
        treeTable.setRowHeight(22);
        treeTable.setHighlighters(
                // unconditional unselected background
                new ColorHighlighter(Color.YELLOW, null), 
                // foreground color on pattern match
                new ColorHighlighter(new PatternPredicate(("D"), 0),
                    null, Color.RED, null, Color.RED),
                // another background for rows on depth 3    
                new ColorHighlighter(new DepthHighlightPredicate(3), Color.MAGENTA, null),
                new ColorHighlighter(
                        // first column if focused
                        new AndHighlightPredicate(HighlightPredicate.HAS_FOCUS, new ColumnHighlightPredicate(0)), 
                        Color.BLUE, Color.WHITE, Color.BLUE, Color.WHITE),
                new BorderHighlighter(
                        new AndHighlightPredicate(HighlightPredicate.ROLLOVER_ROW,  new ColumnHighlightPredicate(0)),
                        BorderFactory.createLineBorder(Color.BLUE))
                );

        // add another highlighter 
        treeTable.addHighlighter(
                // shade the first column
                new ShadingColorHighlighter(new ColumnHighlightPredicate(0)) 
                );
        showWithScrollingInFrame(treeTable,
                "Highlighters and predicates");
    }


}
