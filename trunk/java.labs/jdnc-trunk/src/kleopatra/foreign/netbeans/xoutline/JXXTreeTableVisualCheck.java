/*
 * $Id: JXXTreeTableVisualCheck.java 3029 2009-03-18 11:01:06Z kleopatra $
 * 
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle, Santa Clara,
 * California 95054, U.S.A. All rights reserved.
 * 
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA
 */
package netbeans.xoutline;


import gregtan.matchingtexthighlighter.MatchingTextSearchExample.XMatchingTextHighlighter;

import java.awt.Color;
import java.awt.Component;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.ToolTipManager;
import javax.swing.UIManager;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.tree.TreePath;

import org.jdesktop.swingx.JXDatePicker;
import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.JXTree;
import org.jdesktop.swingx.action.AbstractActionExt;
import org.jdesktop.swingx.decorator.AbstractHighlighter;
import org.jdesktop.swingx.decorator.BorderHighlighter;
import org.jdesktop.swingx.decorator.ColorHighlighter;
import org.jdesktop.swingx.decorator.ComponentAdapter;
import org.jdesktop.swingx.decorator.HighlightPredicate;
import org.jdesktop.swingx.decorator.Highlighter;
import org.jdesktop.swingx.decorator.HighlighterFactory;
import org.jdesktop.swingx.decorator.SearchPredicate;
import org.jdesktop.swingx.decorator.ShadingColorHighlighter;
import org.jdesktop.swingx.decorator.HighlightPredicate.AndHighlightPredicate;
import org.jdesktop.swingx.decorator.HighlightPredicate.ColumnHighlightPredicate;
import org.jdesktop.swingx.painter.MattePainter;
import org.jdesktop.swingx.painter.Painter;
import org.jdesktop.swingx.renderer.DefaultTableRenderer;
import org.jdesktop.swingx.renderer.PainterAware;
import org.jdesktop.swingx.renderer.StringValue;
import org.jdesktop.swingx.renderer.StringValues;
import org.jdesktop.swingx.renderer.WrappingIconPanel;
import org.jdesktop.swingx.search.AbstractSearchable;
import org.jdesktop.swingx.search.SearchFactory;
import org.jdesktop.swingx.test.ComponentTreeTableModel;
import org.jdesktop.swingx.treetable.DefaultMutableTreeTableNode;
import org.jdesktop.swingx.treetable.FileSystemModel;
import org.jdesktop.swingx.treetable.TreeTableModel;

/**
 * @author Jeanette Winzenburg
 */
public class JXXTreeTableVisualCheck extends JXXTreeTableUnitTest {
    @SuppressWarnings("all")
    private static final Logger LOG = Logger
            .getLogger(JXXTreeTableVisualCheck.class.getName());
    public static void main(String[] args) {
        setSystemLF(true);
        JXXTreeTableVisualCheck test = new JXXTreeTableVisualCheck();
        try {
//            test.runInteractiveTests();
//            test.runInteractiveTests("interactive.*Hierarchical.*");
               test.runInteractiveTests("interactive.*Search.*");
//           test.runInteractiveTests("interactive.*DnD.*");
//             test.runInteractiveTests("interactive.*Compare.*");
//             test.runInteractiveTests("interactive.*RowHeightCompare.*");
//             test.runInteractiveTests("interactive.*RToL.*");
//             test.runInteractiveTests("interactive.*Insert.*");
//             test.runInteractiveTests("interactive.*SetModel.*");
        } catch (Exception ex) {

        }
    }

    public void interactiveSubcellSearchMarkerTreeTable() {
        SearchFactory.getInstance().setUseFindBar(true);
        JXXTreeTable table = new JXXTreeTable(new FileSystemModel());
        ((AbstractSearchable) table.getSearchable()).setMatchHighlighter(createMatchingTextHighlighter());
        JXFrame frame = wrapWithScrollingInFrame(table, "SubcellTextMatch");
        Action action = new AbstractActionExt("toggle search mode") {
            public void actionPerformed(ActionEvent e) {
                boolean useFindBar = !SearchFactory.getInstance().isUseFindBar(null, null);
                SearchFactory.getInstance().setUseFindBar(useFindBar);
            }
            
            
        };
        addAction(frame, action);
        show(frame);
    }

    /**
     * @return
     */
    private AbstractHighlighter createMatchingTextHighlighter() {
        Painter painter = new MattePainter();
        AbstractHighlighter hl = new XMatchingTextHighlighter(painter) {
            @Override
            protected boolean canHighlight(Component component,
                    ComponentAdapter adapter) {
                return 
                    component instanceof PainterAware
                        && getPainter() != null
                        && getHighlightPredicate() instanceof SearchPredicate
                        && isLabel(component)
                        ;
            }

            /**
             * @param component
             * @return
             */
            private boolean isLabel(Component component) {
                return (isPainterAwareLabel(component)) 
                    || (component instanceof WrappingIconPanel 
                            && isPainterAwareLabel(((WrappingIconPanel) component).getComponent()))
                    || (component instanceof BasicTreeTablePanel
                            && isPainterAwareLabel(((BasicTreeTablePanel) component).getComponent()))
                            ;
            }

            /**
             * @param component
             * @return
             */
            private boolean isPainterAwareLabel(Component component) {
                return component instanceof JLabel && component instanceof PainterAware;
            }


        };
        return hl;
    }

    /**
     * Issue #??-swingx: select tree cell only (no focus border).
     */
    public void interactiveTreeCellSelection() {
        final JXXTreeTable tree = new JXXTreeTable(new FileSystemModel());
        // use SwingX renderer which is aware of per-tree selection colors
//        tree.setTreeCellRenderer(new DefaultTreeRenderer());
        Highlighter hl = new BorderHighlighter(HighlightPredicate.HAS_FOCUS, 
                BorderFactory.createEmptyBorder(1, 1, 1, 1), false);
        tree.addHighlighter(hl);
        ColorHighlighter colorHighlighter = new ColorHighlighter(
                new AndHighlightPredicate(
                new ColumnHighlightPredicate(0), HighlightPredicate.HAS_FOCUS),
                tree.getSelectionBackground(), null);
        tree.addHighlighter(new NodeHighlighter(colorHighlighter));
//        tree.setCellSelectionEnabled(true);
        tree.setRowSelectionAllowed(false);
        tree.setColumnSelectionAllowed(false);
        JXFrame frame = wrapWithScrollingInFrame(tree, "no focus border");
        show(frame);
    }

    public static class NodeHighlighter extends AbstractHighlighter {

        Highlighter delegate;
        
        public NodeHighlighter(Highlighter delegate) {
            this.delegate = delegate;
        }
        
        @Override
        protected Component doHighlight(Component component,
                ComponentAdapter adapter) {
            Component nodeComponent = ((BasicTreeTablePanel) component).getComponent();
             delegate.highlight(nodeComponent, adapter);
             return component;
        }

        @Override
        protected boolean canHighlight(Component component,
                ComponentAdapter adapter) {
            return (delegate != null) && component instanceof BasicTreeTablePanel;
        }
        
        
    }
    /**
     * compare treeTable/tree height
     *
     */
    public void interactiveTestHighlightAndRowHeightCompareTree() {
        final JXXTreeTable treeTable = new JXXTreeTable(treeTableModel);
        treeTable.setRowHeight(22);
        treeTable.setShowGrid(true, false);
        Highlighter hl = new ShadingColorHighlighter(
                new ColumnHighlightPredicate(0));

        treeTable.setHighlighters(
                HighlighterFactory.createSimpleStriping(HighlighterFactory.GENERIC_GRAY),
                hl);
        final JXTree tree = new JXTree(treeTableModel);
        tree.setRowHeight(treeTable.getRowHeight());

        JFrame frame = wrapWithScrollingInFrame(treeTable, tree, 
                "compare rowheight treetable vs tree");
        Action action = new AbstractActionExt("increment rowheight") {

            @Override
            public void actionPerformed(ActionEvent e) {
                int rowHeight = treeTable.getRowHeight() + 10;
                if (rowHeight > 50) rowHeight = 22;
                treeTable.setRowHeight(rowHeight);
                tree.setRowHeight(rowHeight);
                
            }
            
        };
        frame.setVisible(true);
    }


    /**
     * Issue #862-swingx: JXTree - add api for selection colors.
     * Here: check colors when used in JXXTreeTable
     */
    public void interactiveSelectionColors() {
        final JXXTreeTable tree = new JXXTreeTable(new FileSystemModel());
        // use SwingX renderer which is aware of per-tree selection colors
        final Color uiBackground = tree.getSelectionBackground();
        final Color uiForeground = tree.getSelectionForeground();
        Action toggleSelectionColors = new AbstractAction("toggle selection colors") {
            
            public void actionPerformed(ActionEvent e) {
                if (tree.getSelectionBackground() == uiBackground) {
                    tree.setSelectionBackground(Color.BLUE);
                    tree.setSelectionForeground(Color.RED);
                } else {
                    tree.setSelectionBackground(uiBackground);
                    tree.setSelectionForeground(uiForeground);
                }
                
            }
            
        };
        JXFrame frame = wrapWithScrollingInFrame(tree, "selection colors");
        addAction(frame, toggleSelectionColors);
        show(frame);
    }
    

    
    /**
     * Issue #853-swingx: tree is not disabled.
     * 
     */
    public void interactiveDisabledTreeColumn() {
        final JXXTreeTable treeTable = new JXXTreeTable(new FileSystemModel());
        JXFrame frame = showWithScrollingInFrame(treeTable, "disabled - tree follows table");
        Action action = new AbstractActionExt("toggle enabled") {

            public void actionPerformed(ActionEvent e) {
                treeTable.setEnabled(!treeTable.isEnabled());
                
            }
            
        };
        addAction(frame, action);
        show(frame);
        
    }

    
    /**
     * Reported: toggling LAF doesn't update treetable?
     * WorksforMe.
     */
    public void interactiveToggleLAF() {
        JXXTreeTable table = new JXXTreeTable(treeTableModel);
        JXFrame frame = wrapInFrame(new JScrollPane(table), "Toggle LAF", true);
        show(frame);
    }
    
    /**
     * Issue #??-swingx: Tooltip by highlighter in hierarchical column
     *
     * Not reliably updated.
     * 
     * To reproduce: 
     * - move to some row over the hierarchical column where the tooltip is showing
     * - move the next row, typically the tooltip is not showing
     */
    public void interactiveHierarchicalToolTip() {
        final JXXTreeTable table = new JXXTreeTable(new ComponentTreeTableModel(new JXFrame()));
        table.setHorizontalScrollEnabled(true);
//        table.packAll();
        Highlighter toolTip = new AbstractHighlighter(
                new AndHighlightPredicate(
                new ColumnHighlightPredicate(0), HighlightPredicate.ROLLOVER_ROW)) {

            @Override
            protected Component doHighlight(Component component,
                    ComponentAdapter adapter) {
                ((JComponent) component).setToolTipText(adapter.getString());
                return component;
            }
            
        };
        table.addHighlighter(toolTip);
        StringValue sv = new StringValue() {

            @Override
            public String getString(Object value) {
                return "X" + StringValues.TO_STRING.getString(value);
            }};
        table.setDefaultRenderer(Object.class, new DefaultTableRenderer(sv));
        JXFrame frame = wrapWithScrollingInFrame(table, "ToolTip with Highlighter (hierarchical column)");
        addComponentOrientationToggle(frame);
        frame.setVisible(true);
    }
  
    /**
     * Issue #544-swingx: problem with simple striping in JXXTreeTable.
     * start with cross-platform == okay, bluish striping
     * toggle to system (win) == striping color silver, 
     *   but second row bluish, background not reset?
     * toggle back to cross-platform == no striping, all bluish
     * 
     * start with system (win) == okay, silver striping
     * toggle to cross-platform == okay, bluish striping
     * back to system == trouble as above
     * 
     * JXTable looks okay.
     */
    public void interactiveUIHighlight() {
        JXTable table = new JXTable(20, 4);
        JXXTreeTable treeTable = new JXXTreeTable(new FileSystemModel());
        treeTable.setHighlighters(HighlighterFactory.createSimpleStriping());
        table.setHighlighters(treeTable.getHighlighters());
        final JXFrame frame = wrapWithScrollingInFrame(treeTable, table, "update ui-specific striping");
        Action toggle = new AbstractActionExt("toggle LF") {
            boolean system;
            public void actionPerformed(ActionEvent e) {
                String lfName = system ? UIManager.getSystemLookAndFeelClassName() :
                    UIManager.getCrossPlatformLookAndFeelClassName();
                try {
                    UIManager.setLookAndFeel(lfName);
                    SwingUtilities.updateComponentTreeUI(frame);
                 } catch (Exception e1) { 
                     LOG.info("exception when setting LF to " + lfName);
                     LOG.log(Level.FINE, "caused by ", e1);
                } 
                system = !system; 
                
            }
            
        };
        addAction(frame, toggle);
        frame.setVisible(true);
    }
    

    /**
     * Issue #471-swingx: No selection on click into hierarchical column outside
     * node. 
     *
     * Check patch and bidi-compliance.
     */
    public void interactiveHierarchicalSelectionAndRToL() {
        final JXXTreeTable table = new JXXTreeTable(treeTableModel);
        final JXFrame frame = wrapWithScrollingInFrame(table, "Selection/Expansion Hacks and Bidi Compliance");
        addComponentOrientationToggle(frame);
        frame.setVisible(true);
    }

    
    /**
     * visual check what happens on toggling the largeModel property.
     * It's okay for ComponentTreeModel, blows up for FileSystemModel.
     *
     */
    public void interactiveLargeModelEdit() {
        final JXXTreeTable treeTable = new JXXTreeTable(treeTableModel); 

//        final JXXTreeTable treeTable = new JXXTreeTable(createMutableVisualizeModel());
        treeTable.setRootVisible(true);
        ToolTipManager.sharedInstance().unregisterComponent(treeTable);
        Action action = new AbstractAction("toggle largeModel") {

            public void actionPerformed(ActionEvent e) {
                treeTable.setLargeModel(!treeTable.isLargeModel());
               
            }
            
        };
        JXFrame frame = wrapWithScrollingInFrame(treeTable, "large model");
        addAction(frame, action);
        frame.setVisible(true);
    }

    private ComponentTreeTableModel createMutableVisualizeModel() {
        JXPanel frame = new JXPanel();
        frame.setName("somename for root");
        JTextField textField = new JTextField();
        textField.setName("firstchild");
        frame.add(textField);
        frame.add(textField);
        frame.add(new JComboBox());
        frame.add(new JXDatePicker());
        return new ComponentTreeTableModel(frame);
    }

    /**  
     * Issue #575-swingx: JXXTreeTable - scrollsOnExpand has no effect.
     * 
     * Compare tree/table: 
     * - tree expands if property is true and
     * expand triggered by mouse (not programmatically?). 
     * - treeTable never 
     * 
     * 
     * related issue #296-swingx: expose scrollPathToVisible in JXXTreeTable.
     */    
    public void interactiveScrollsOnExpand() {
        
        final JXXTreeTable treeTable = new JXXTreeTable(new FileSystemModel());
        final JXTree tree = new JXTree(treeTable.getTreeTableModel());
        treeTable.setScrollsOnExpand(tree.getScrollsOnExpand());
        tree.setRowHeight(treeTable.getRowHeight());
        Action toggleScrolls = new AbstractActionExt("Toggle ScrollsOnExpand") {

            public void actionPerformed(ActionEvent e) {
                boolean newValue = !tree.getScrollsOnExpand();
                tree.setScrollsOnExpand(newValue);
                treeTable.setScrollsOnExpand(tree.getScrollsOnExpand());
                setName("Toggle scrollsOnExpand - " + newValue);
            }
            
        };
         Action expand = new AbstractAction("Expand selected") {

            public void actionPerformed(ActionEvent e) {
                int[] selectedRows = tree.getSelectionRows();
                if (selectedRows.length > 0) {
                    tree.expandRow(selectedRows[0]);
                }
               int selected = treeTable.getSelectedRow();
               if (selected >= 0) {
                   treeTable.expandRow(selected);
               }
            }
            
        };
        Action largeModel = new AbstractActionExt("Toggle largeModel") {

            @Override
            public void actionPerformed(ActionEvent e) {
                boolean newValue = !tree.isLargeModel();
                tree.setLargeModel(newValue);
                treeTable.setLargeModel(newValue);
                setName("Toggle largeModel - " + newValue);
                
            }
            
        };
        JXFrame frame = wrapWithScrollingInFrame(tree, treeTable,
                "Compare Tree/Table scrollsOnExpand properties ");
        addAction(frame, toggleScrolls);
        addAction(frame, expand);
        addAction(frame, largeModel);
        show(frame);
    }


    /**
     * issue #296-swingx: expose scrollPathToVisible in JXXTreeTable.
     * 
     * Treetable should behave exactly like Tree - so
     * simply passing through to the hierarchical renderer is not quite
     * enough - need to force a scrollTo after expanding. 
     * Not really: all scrolling is piped through scrollRectToVisible, 
     * so that looks like the central place to fix (f.i. delegate to
     * the enclosing treeTable). Related issue #575-swingx.
     * 
     */
    public void interactiveScrollPathToVisible() {
        // PENDING: FileSystemModel throws occasional NPE on getChildCount()
        final TreeTableModel model = new FileSystemModel();
        final JXXTreeTable table = new JXXTreeTable(model);
        table.setColumnControlVisible(true);
        final JXTree tree = new JXTree(model);
        Action action = new AbstractAction("path visible") {

            public void actionPerformed(ActionEvent e) {
                Rectangle visible = table.getVisibleRect();
                int lastRow = table.rowAtPoint(new Point(5, visible.y + visible.height + 100));
                TreePath path = table.getPathForRow(lastRow);
                if (path == null) return;
                Object last = path.getLastPathComponent();
                 while (model.isLeaf(last) || model.getChildCount(last) == 0) {
                    lastRow++;
                    path = table.getPathForRow(lastRow);
                    if (path == null) return;
                    last = path.getLastPathComponent();
                }
                // we have a node with children
                int childCount = model.getChildCount(last); 
                Object lastChild = model.getChild(last, childCount - 1); 
                path = path.pathByAddingChild(lastChild);
                table.scrollPathToVisible(path);
                tree.scrollPathToVisible(path);
                
            }
        };
        JXFrame frame = wrapWithScrollingInFrame(table, tree, "compare scrollPathtovisible");
        addAction(frame, action);
        show(frame);

    }

    /**
     * http://forums.java.net/jive/thread.jspa?threadID=13966&tstart=0
     * adjust hierarchical column width on expansion. The expansion
     * listener looks like doing the job. Important: auto-resize off, 
     * otherwise the table will run out of width to distribute!
     * 
     */
    public void interactiveUpdateWidthOnExpand() {
        
        final JXXTreeTable tree = new JXXTreeTable(treeTableModel);
        tree.setColumnControlVisible(true);

        
        tree.addTreeExpansionListener(new TreeExpansionListener(){

           public void treeCollapsed(TreeExpansionEvent event) {
           }

           public void treeExpanded(TreeExpansionEvent event) {
              
              SwingUtilities.invokeLater(new Runnable(){
                 
                 public void run() {
                     tree.packColumn(tree.getHierarchicalColumn(), -1);
//                    tree.getColumnModel().getColumn(0).setPreferredWidth(renderer.getPreferredSize().width);

                 }
              });            
           }
           
        });
        showWithScrollingInFrame(tree, "adjust column on expand");
    }
    /**
     * visualize editing of the hierarchical column, both
     * in a tree and a treeTable
     *
     */
    public void interactiveTreeTableModelEditing() {
        final TreeTableModel model = createMutableVisualizeModel();
        final JXXTreeTable table = new JXXTreeTable(model);
        JTree tree =  new JTree(model) {

            @Override
            public String convertValueToText(Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
                if (value instanceof Component) {
                    return ((Component) value).getName();
                }
                return super.convertValueToText(value, selected, expanded, leaf, row, hasFocus);
            }
            
        };
        tree.setEditable(true);
        final JXFrame frame = wrapWithScrollingInFrame(table, tree, "Editing: compare treetable and tree");
        addComponentOrientationToggle(frame);
        show(frame);
        
    }


    /**
     * Issue #248-swingx: update probs with insert into empty model when root
     * not visible.
     * 
     * Looks like a core JTree problem: a collapsed root is not automatically expanded
     * on hiding. Should it? Yes, IMO (JW).
     * 
     * this exposed a slight glitch in JXXTreeTable: toggling the initially invisible
     * root to visible did not result in showing the root in the the table. Needed
     * to modify setRootVisible to force a revalidate.
     *   
     */
    public void interactiveTestInsertNodeEmptyModel() {
        final DefaultMutableTreeTableNode root = new DefaultMutableTreeTableNode();
        final InsertTreeTableModel model = new InsertTreeTableModel(root, true);
        final JTree tree = new JTree(model);
        tree.setRootVisible(false);
        final JXXTreeTable treeTable = new JXXTreeTable(model);
        treeTable.addHighlighter(
                HighlighterFactory.createSimpleStriping(HighlighterFactory.LINE_PRINTER));

        treeTable.setColumnControlVisible(true);
        // treetable root invisible by default
        JXFrame frame = wrapWithScrollingInFrame(tree, treeTable, "JTree vs. JXXTreeTable: insert into empty model");
        Action insertAction = new AbstractAction("insert node") {

            public void actionPerformed(ActionEvent e) {
                model.addChild(root);
                
            }
            
        };
        addAction(frame, insertAction);
        Action toggleRoot = new AbstractAction("toggle root visible") {
            public void actionPerformed(ActionEvent e) {
                boolean rootVisible = !tree.isRootVisible();
                treeTable.setRootVisible(rootVisible);
                tree.setRootVisible(rootVisible);
            }
            
        };
        addAction(frame, toggleRoot);
        addMessage(frame, "model reports root as non-leaf");
        show(frame);
    }
 
    /**
     * Issue #254-swingx: collapseAll/expandAll behaviour depends on 
     * root visibility (same for treeTable/tree)
     * 
     * initial: root not visible, all root children visible
     *  do: collapse all - has no effect, unexpected?
     *  do: toggle root - root and all children visible, expected
     *  do: collapse all - only root visible, expected
     *  do: toggle root - all nodes invisible, expected
     *  do: expand all - still all nodes invisible, unexpected?
     *  
     *   
     */
    public void interactiveTestInsertNodeEmptyModelExpand() {
        final DefaultMutableTreeTableNode root = new DefaultMutableTreeTableNode();
        final InsertTreeTableModel model = new InsertTreeTableModel(root, true);
        for (int i = 0; i < 5; i++) {
            model.addChild(root);
        }
        final JXTree tree = new JXTree(model);
        tree.setRootVisible(false);
        final JXXTreeTable treeTable = new JXXTreeTable(model);
        treeTable.addHighlighter(
                HighlighterFactory.createSimpleStriping(HighlighterFactory.LINE_PRINTER));

        treeTable.setColumnControlVisible(true);
        // treetable root invisible by default
        JXFrame frame = wrapWithScrollingInFrame(tree, treeTable, "collaps/expand root");
        Action toggleRoot = new AbstractAction("toggle root") {
            public void actionPerformed(ActionEvent e) {
                boolean rootVisible = !tree.isRootVisible();
                treeTable.setRootVisible(rootVisible);
                tree.setRootVisible(rootVisible);
            }
            
        };
        addAction(frame, toggleRoot);
        Action expandAll = new AbstractAction("expandAll") {
            public void actionPerformed(ActionEvent e) {
                treeTable.expandAll();
                tree.expandAll();
            }
            
        };
        addAction(frame, expandAll);
        Action collapseAll = new AbstractAction("collapseAll") {
            public void actionPerformed(ActionEvent e) {
                treeTable.collapseAll();
                tree.collapseAll();
            }
            
        };
        addAction(frame, collapseAll);
        show(frame);
    }
 

    /**
     * Issue #247-swingx: update probs with insert node.
     * The insert under a collapsed node fires a dataChanged on the table 
     * which results in the usual total "memory" loss (f.i. selection)
     * to reproduce: run example, select root's child in both the tree and the 
     * treetable (left and right view), press the insert button, treetable looses 
     * selection, tree doesn't (the latter is the correct behaviour)
     * 
     * couldn't reproduce the reported loss of expansion state. Hmmm..
     *
     */
    public void interactiveTestInsertUnderCollapsedNode() {
        final DefaultMutableTreeTableNode root = new DefaultMutableTreeTableNode();
        final InsertTreeTableModel model = new InsertTreeTableModel(root);
        DefaultMutableTreeTableNode childA = model.addChild(root);
        final DefaultMutableTreeTableNode childB = model.addChild(childA);
        model.addChild(childB);
        DefaultMutableTreeTableNode secondRootChild = model.addChild(root);
        model.addChild(secondRootChild);
        JXTree tree = new JXTree(model);
        final JXXTreeTable treeTable = new JXXTreeTable(model);
        treeTable.addHighlighter(
                HighlighterFactory.createSimpleStriping(HighlighterFactory.LINE_PRINTER));

        treeTable.setColumnControlVisible(true);
        treeTable.setRootVisible(true);
        JXFrame frame = wrapWithScrollingInFrame(tree, treeTable, "JXTree vs. JXXTreeTable insert node to nested child");
        Action insertAction = new AbstractAction("insert node") {

            public void actionPerformed(ActionEvent e) {
                model.addChild(childB);
           
            }
            
        };
        addAction(frame, insertAction);
        addMessage(frame, "insert nested child must not loose selection/expanseion state");
        frame.pack();
        frame.setVisible(true);
    }

    /**
     * Issue #246-swingx: update probs with insert node.
     * 
     * The reported issue is an asymmetry in updating the parent: it's done only
     * if not expanded. With the arguments of #82-swingx, parent's appearance
     * might be effected by child changes if expanded as well.
     * <p>
     * Here's a test for insert: the crazy renderer removes the icon if
     * childCount exceeds a limit (here > 3). Select a node, insert a child,
     * expand the node and keep inserting children. Interestingly the parent is
     * always updated in the treeTable, but not in the tree
     * <p>
     * Quick test if custom icons provided by the renderer are respected. They
     * should appear and seem to do.
     * 
     */
//    public void interactiveTestInsertNodeAndChangedParentRendering() {
//        final Icon topIcon = XTestUtils.loadDefaultIcon("wellTop.gif");
//        final DefaultMutableTreeTableNode root = new DefaultMutableTreeTableNode();
//        final InsertTreeTableModel model = new InsertTreeTableModel(root);
//        JXTree tree = new JXTree(model);
//        final JXXTreeTable treeTable = new JXXTreeTable(model);
//        treeTable.setColumnControlVisible(true);
//        TreeCellRenderer renderer = new DefaultTreeCellRenderer() {
//
//            @Override
//            public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
//                Component comp = super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf,
//                        row, hasFocus);
//                TreePath path = tree.getPathForRow(row);
//                if (path != null) {
//                    Object node = path.getLastPathComponent();
//                    if ((node != null) && (tree.getModel().getChildCount(node) > 2)) {
//                        setIcon(topIcon);
//                    } 
//                }
//                return comp;
//            }
//            
//        };
//        tree.setCellRenderer(renderer);
//        treeTable.setTreeCellRenderer(renderer);
//        treeTable.setRootVisible(true);
//        JXFrame frame = wrapWithScrollingInFrame(tree, treeTable, "JXTree vs. JXXTreeTable - update parent on insert child");
//        Action insertAction = new AbstractAction("insert node selected treetable") {
//
//            public void actionPerformed(ActionEvent e) {
//                int selected = treeTable.getSelectedRow();
//                if (selected < 0 ) return;
//                TreePath path = treeTable.getPathForRow(selected);
//                DefaultMutableTreeTableNode parent = (DefaultMutableTreeTableNode) path.getLastPathComponent();
//                model.addChild(parent);
//                
//            }
//            
//        };
//        addAction(frame, insertAction);
//        addMessage(frame, " - rendering changed for > 2 children");
//        frame.pack();
//        frame.setVisible(true);
//    }
 
    /**
     * Issue #82-swingx: update probs with insert node.
     * 
     * Adapted from example code in report.
     * Insert node under selected in treetable (or under root if none selected)
     * Here: old problem with root not expanded because it's reported as a leaf.
     */
    public void interactiveTestInsertNode() {
        final DefaultMutableTreeTableNode root = new DefaultMutableTreeTableNode();
        final InsertTreeTableModel model = new InsertTreeTableModel(root);
        JTree tree = new JTree(model);
        tree.setRootVisible(false);
        final JXXTreeTable treeTable = new JXXTreeTable(model);
        treeTable.addHighlighter(
                HighlighterFactory.createSimpleStriping(HighlighterFactory.GENERIC_GRAY));
        Highlighter hl = new ShadingColorHighlighter(
                new ColumnHighlightPredicate(0));

        treeTable.addHighlighter(hl);
        JXFrame frame = wrapWithScrollingInFrame(tree, treeTable, "JTree vs. JXXTreeTable - insert to collapsed root");
        Action insertAction = new AbstractAction("insert node") {

            public void actionPerformed(ActionEvent e) {
                int selected = treeTable.getSelectedRow();
                DefaultMutableTreeTableNode parent;
                if (selected < 0 ) {
                    parent = root;
                } else {
                TreePath path = treeTable.getPathForRow(selected);
                 parent = (DefaultMutableTreeTableNode) path.getLastPathComponent();
                }
                model.addChild(parent);
                
            }
            
        };
        addAction(frame, insertAction);
        addMessage(frame, "insert into root-only model - does not show");
        show(frame);
    }
 

    /**
     * Issue #224-swingx: TreeTableEditor not bidi compliant.
     *
     * the textfield for editing is at the wrong position in RToL.
     */
    public void interactiveRToLTreeTableEditor() {
        final TreeTableModel model = createMutableVisualizeModel();
        final JXXTreeTable table = new JXXTreeTable(model);
        final JXFrame frame = wrapWithScrollingInFrame(table, "Editor: position follows Component orientation");
        addComponentOrientationToggle(frame);
        show(frame);
    }

    /**
     * Issue #223-swingx: Icons lost when editing.
     *  Regression after starting to fix #224-swingx? 
     *  
     *  
     */
    public void interactiveTreeTableEditorIcons() {
        final TreeTableModel model = createMutableVisualizeModel();
        final JXXTreeTable table = new JXXTreeTable(model);
        showWithScrollingInFrame(table, "Editor: icons showing");
    }

    
    
    /**
     * see effect of switching treeTableModel.
     * Problem when toggling back to FileSystemModel: hierarchical 
     * column does not show filenames, need to click into table first.
     * JW: fixed. The issue was updating of the conversionMethod 
     * field - needed to be done before calling super.setModel().
     * 
     */
    public void interactiveTestSetModel() {
        final JXXTreeTable treeTable = new JXXTreeTable(treeTableModel);
        treeTable.setColumnControlVisible(true);
        JXFrame frame = wrapWithScrollingInFrame(treeTable, "toggle model");
        final TreeTableModel model = new ComponentTreeTableModel(frame);
        Action action = new AbstractAction("Toggle model") {

            public void actionPerformed(ActionEvent e) {
                TreeTableModel myModel = treeTable.getTreeTableModel();
                treeTable.setTreeTableModel(myModel == model ? treeTableModel : model);
                
            }
            
        };
        addAction(frame, action);
        show(frame);
    }
    /**
     * Issue #168-jdnc: dnd enabled breaks node collapse/expand.
     * Regression? Dnd doesn't work at all?
     * 
     */
    public void interactiveToggleDnDEnabled() {
        final JXXTreeTable treeTable = new JXXTreeTable(treeTableModel);
        treeTable.setColumnControlVisible(true);
        final JXTree tree = new JXTree(treeTableModel);

        Action action = new AbstractActionExt("Toggle dnd: false") {
            
            public void actionPerformed(ActionEvent e) {
                
                boolean dragEnabled = !treeTable.getDragEnabled();
                treeTable.setDragEnabled(dragEnabled);
                tree.setDragEnabled(dragEnabled);
                setName("Toggle dnd: " + dragEnabled);
            }
            
        };
        JXFrame frame = wrapWithScrollingInFrame(treeTable, tree, "toggle dragEnabled (starting with false)");
        addAction(frame, action);
        show(frame);
    }


    
    /**    
     * setting tree properties: tree not updated correctly.
     * 
     */    
    public void interactiveTestTreeProperties() {
        final JXXTreeTable treeTable = new JXXTreeTable(treeTableModel);
        Action toggleHandles = new AbstractAction("Toggle Handles") {

            public void actionPerformed(ActionEvent e) {
                treeTable.setShowsRootHandles(!treeTable.getShowsRootHandles());
                
            }
            
        };
        Action toggleRoot = new AbstractAction("Toggle Root") {

            public void actionPerformed(ActionEvent e) {
                treeTable.setRootVisible(!treeTable.isRootVisible());
                
            }
            
        };
        treeTable.setRowHeight(22);
        JXFrame frame = wrapWithScrollingInFrame(treeTable,
                "Toggle Tree properties (TBD: implement showRoothandles)");
        addAction(frame, toggleRoot);
        addAction(frame, toggleHandles);
        frame.setVisible(true);
    }

    /**    
     * Issue #242: CCE when setting icons. Not reproducible? 
     * Another issue: icon setting does not repaint (with core default renderer)
     * Does not work at all with SwingX renderer (not surprisingly, the
     * delegating renderer in JXTree looks for a core default to wrap).
     * Think: tree/table should trigger repaint?
     */    
//    public void interactiveTestTreeIcons() {
//        final JXXTreeTable treeTable = new JXXTreeTable(treeTableModel);
//        final Icon downIcon = XTestUtils.loadDefaultIcon("wellbottom.gif");
//        final Icon upIcon = XTestUtils.loadDefaultIcon("welltop.gif");
//        Action toggleClosedIcon = new AbstractAction("Toggle closed icon") {
//            boolean down;
//            public void actionPerformed(ActionEvent e) {
//                if (down) {
//                    treeTable.setClosedIcon(downIcon);
//                } else {
//                    treeTable.setClosedIcon(upIcon);
//                }
//                down = !down;
//                // need to force - but shouldn't that be done in the
//                // tree/table itself? and shouldn't the tree fire a 
//                // property change?
//                //treeTable.repaint();
//            }
//            
//        };
//        treeTable.setRowHeight(22);
//        JXFrame frame = wrapWithScrollingInFrame(treeTable,
//                "Toggle Tree icons ");
////        addAction(frame, toggleRoot);
//        addAction(frame, toggleClosedIcon);
//        frame.setVisible(true);
//    }


}
