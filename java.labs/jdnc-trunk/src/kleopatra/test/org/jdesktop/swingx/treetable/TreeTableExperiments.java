/*
 * Created on 18.05.2007
 *
 */
package org.jdesktop.swingx.treetable;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import org.jdesktop.swingx.InteractiveTestCase;
import org.jdesktop.swingx.JXTreeTable;
import org.jdesktop.swingx.decorator.HighlighterFactory;
import org.jdesktop.swingx.table.treetable.NodeChangedMediator;
import org.jdesktop.swingx.table.treetable.NodeModel;
import org.jdesktop.swingx.table.treetable.TreeTableModelAdapter;

public class TreeTableExperiments extends InteractiveTestCase {

    public static void main(String args[]) {
//      setSystemLF(true);
      TreeTableExperiments test = new TreeTableExperiments();
      try {
        test.runInteractiveTests();
//         test.runInteractiveTests(".*Label.*");
      } catch (Exception e) {
          System.err.println("exception when executing interactive tests:");
          e.printStackTrace();
      }
  }

    
    public void interactiveTreeTableModelAdapter() {
         TreeTableModel model = new TreeTableModelAdapter(createDefaultTreeModel(), 
                 new NoteNodeModel(), NodeChangedMediator.DEFAULT);
         JXTreeTable treeTable = new JXTreeTable(model);
         treeTable.setRootVisible(true);
         JXTreeTable other = new JXTreeTable(createTreeTableModel());
         other.setRootVisible(true);
         showWithScrollingInFrame(treeTable, other, "JXTreeTable: adapted treeModel vs. TreeTableNode");
    }
    
    /**
     * Creates and returns a DefaultTreeModel with Note objects as userObject.
     * @return
     */
    private DefaultTreeModel createDefaultTreeModel() {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode(new Note(
                "SwingLabs", "todo...", false));
        DefaultMutableTreeNode swingx = new DefaultMutableTreeNode(new Note(
                "SwingX",
                "a collection of exciting new easy-to-use-components", false));
        swingx.add(new DefaultMutableTreeNode(new Note("JXTable",
                "sortable, highlightable, filterable", false)));
        swingx.add(new DefaultMutableTreeNode(new Note("JXTreeTableAlternate",
                "still very raw", false)));
        root.add(swingx);
        DefaultMutableTreeNode databinding = new DefaultMutableTreeNode(
                new Note(
                        "Databinding",
                        "everything you need to automagically bind data to widgets",
                        false));
        databinding.add(new DefaultMutableTreeNode(new Note("DataSet",
                "a nice easy way to model database data", true)));
        databinding.add(new DefaultMutableTreeNode(new Note("DataModel",
                "the generic data modeling layer", false)));
        root.add(databinding);
        root.add(new DefaultMutableTreeNode(new Note("JDNC",
                "simplified widget layer on top of SwingX", false)));
        root
                .add(new DefaultMutableTreeNode(
                        new Note(
                                "Markup",
                                "support for reading jdnc from XML (on-hold due to resource constraints)",
                                true)));
        DefaultTreeModel model = new DefaultTreeModel(root);
        return model;

    }
    
    /**
     * A NodeModel implementation expecting Note nodes. The Note can be 
     * the node or can be wrapped into a 
     * (DefaultMutable) TreeNode.
     */
    public static class NoteNodeModel implements NodeModel {

        public Class<?> getColumnClass(int column) {
            if (column == 2) return Boolean.class;
            return String.class;
        }

        public int getColumnCount() {
            return 3;
        }

        public String getColumnName(int column) {
            switch(column) {
            case 0:
                return "Title";
            case 1:
                return "Description";
            case 2:
                return "Complete";
            default:
                return "Column " + column;
            }
        }

        public int getHierarchicalColumn() {
            return 0;
        }

        public Object getValueAt(Object node, int column) {
            Note note = getNote(node);
            if (note == null) return note;
            switch (column) {
            case 0:
                return note.title;
            case 1:
                return note.content;
            case 2:
                return new Boolean(note.complete);
            }
            return null;
        }

        private Note getNote(Object node) {
            if (node instanceof DefaultMutableTreeNode) {
                node = ((DefaultMutableTreeNode) node).getUserObject();
            }
            if (node instanceof Note) return (Note) node;
            return null;
        }

        public boolean isCellEditable(Object node, int column) {
            return true;
        }

        public void setValueAt(Object value, Object node, int column) {
            Note note = getNote(node);
            if (note == null) return;
            switch(column) {
            case 0:
                note.title = String.valueOf(value);
                break;
            case 1:
                note.content = (String) value;
                break;
            case 2:
                note.complete = ((Boolean) value).booleanValue();
                
            }
            
        }
        
    }
    private DefaultTreeTableModel createTreeTableModel() {
        NoteTreeNode root = new NoteTreeNode
            (new Note("SwingLabs", "todo...", false));
        final NoteTreeNode swingx = new NoteTreeNode(
             new Note("SwingX", "a collection of exciting new easy-to-use-components", false));
        swingx.add(new NoteTreeNode(new Note("JXTable", "sortable, highlightable, filterable", false)));
        swingx.add(new NoteTreeNode(new Note("JXTreeTableAlternate", "still very raw", false)));
        root.add(swingx);
        NoteTreeNode databinding = new NoteTreeNode(
                new Note("Databinding", "everything you need to automagically bind data to widgets", false));
        databinding.add(new NoteTreeNode(new Note("DataSet", "a nice easy way to model database data", true)));
        databinding.add(new NoteTreeNode(new Note("DataModel", "the generic data modeling layer", false)));
        root.add(databinding);
        root.add(new NoteTreeNode(new Note("JDNC", "simplified widget layer on top of SwingX", false)));
        root.add(new NoteTreeNode(new Note("Markup", "support for reading jdnc from XML (on-hold due to resource constraints)", true)));
        List<String> columnIdentifiers = new ArrayList<String>();
        for (int i = 0; i < swingx.getColumnCount(); i++) {
            columnIdentifiers.add(swingx.getColumnName(i));
        }
        // hack around missing api: no way to set the column classes
        DefaultTreeTableModel model = new DefaultTreeTableModel(root, columnIdentifiers) {

            @Override
            public Class<?> getColumnClass(int column) {
                return swingx.getColumnClass(column);
            }
            
        };
        return model;
    }

    public static class NoteTreeNode extends AbstractMutableTreeTableNode {

        public NoteTreeNode(Note note) {
            super(note);
        }
        
        public Class<?> getColumnClass(int column) {
            if (column == 2) return Boolean.class;
            return String.class;
        }

        public int getColumnCount() {
            return 3;
        }

        public String getColumnName(int column) {
            switch(column) {
            case 0:
                return "Title";
            case 1:
                return "Description";
            case 2:
                return "Complete";
            default:
                return "Column " + column;
            }
        }

        public Object getValueAt(int column) {
            Note note = (Note) getUserObject();
            switch (column) {
            case 0:
                return note.title;
            case 1:
                return note.content;
            case 2:
                return note.complete;
            }
            return null;
        }

        @Override
        public boolean isEditable(int column) {
            return true; //column != 0;
        }

        @Override
        public void setValueAt(Object value, int column) {
            Note note = (Note) getUserObject();
            switch(column) {
            case 0:
                note.title = String.valueOf(value);
                break;
            case 1:
                note.content = (String) value;
                break;
            case 2:
                note.complete = ((Boolean) value).booleanValue();
                
            }
            
        }

//        @Override
//        public int getHierarchicalColumn() {
//            return 0;
//        }
        
    }
    
    public static class Note {
        String content;
        String title;
        boolean complete;
        public Note(String title, String content, boolean complete) {
             this.content = content;
            this.title = title;
            this.complete = complete;
        }
    }

    /**
     * Requirement: don't show horizontal grid lines in hierarchical
     * column.
     * http://forums.java.net/jive/thread.jspa?messageID=217177
     * 
     * Hack: overpaint with table's background color. Here done in
     * paint for lazyness - maybe to c&p the BasicTableUI grid painting
     * and paint the lines in paintComponent.
     *
     */
    public void interactiveOverpaintHorizontalLines() {
        JXTreeTable treeTable = new JXTreeTable(new FileSystemModel()) {

            private boolean dirty;

            @Override
            public void paint(Graphics g) {
                super.paint(g);
                if (!dirty) {
                    dirty(g);
                }
            }

            private void dirty(Graphics g) {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        int treeColumn = getHierarchicalColumn();
                        // hierarchical not visible
                        if (treeColumn < 0)
                            return;
                        dirty = true;
                        Rectangle cellRect = getCellRect(0, treeColumn, true);
                        cellRect.y = 0;
                        cellRect.height = getHeight();
                        Rectangle visible = cellRect
                                .intersection(getVisibleRect());
                        visible.width--;
                        Color oldGrid = gridColor;
                        gridColor = getBackground();
                        paintImmediately(visible);
                        gridColor = oldGrid;
                        dirty = false;
                    }
                });                
            }
        };
        treeTable.setShowGrid(true, true);
        treeTable.setRolloverEnabled(false);
        showWithScrollingInFrame(treeTable, "remove horizontal line in tree column");
    }
    
    public void interactiveVerticalGridAndHighlighter() {
        JXTreeTable treeTable = new JXTreeTable(new FileSystemModel());
        treeTable.setShowGrid(false, true);
        treeTable.addHighlighter(HighlighterFactory.createSimpleStriping());
        showWithScrollingInFrame(treeTable, "horizontal guide by striping");
    }
}
