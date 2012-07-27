/*
 * Created on 13.12.2005
 *
 */
package org.jdesktop.swingx.treetable;

import java.util.logging.Logger;

import junit.framework.TestCase;

import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.table.treetable.file.FileExtendsDefaultTTM;
import org.jdesktop.swingx.tree.ComponentTreeTableModel;
import org.jdesktop.swingx.treetable.AbstractMutableTreeTableNode;
import org.jdesktop.swingx.treetable.DefaultTreeTableModel;
import org.jdesktop.swingx.treetable.FileSystemModel;
import org.jdesktop.swingx.treetable.TreeTableModel;

public class TreeTableModelIssues extends TestCase {
    private static final Logger LOG = Logger
            .getLogger(TreeTableModelIssues.class.getName());
    
    public void testFileSystemTTM3() {
        TreeTableModel model = new FileExtendsDefaultTTM();
        assertColumnClassAssignableFromValue(model);
        
    }
    public void testFileSystemTTM() {
        TreeTableModel model = new FileSystemModel();
        assertColumnClassAssignableFromValue(model);
    }

    public void testNoteTreeTM() {
        TreeTableModel model = createTreeTableModel();
        assertColumnClassAssignableFromValue(model);
    }
    
    public void testComponentTTM() {
        TreeTableModel model = new ComponentTreeTableModel(new JXFrame());
        assertColumnClassAssignableFromValue(model);
    }
    /**
     * @param model
     */
    private void assertColumnClassAssignableFromValue(TreeTableModel model) {
        for (int i = 0; i < model.getColumnCount(); i++) {
            Class clazz = model.getColumnClass(i);
            Object value = model.getValueAt(model.getRoot(), i);
            if (value != null) {
                assertTrue("column class must be assignable to value class at column " + i + "\n" +
                                "columnClass = " + model.getColumnClass(i) + "\n" +
                                "valueClass = " + value.getClass()
                        , clazz.isAssignableFrom(value.getClass()));
            } else {
                LOG.info("column " + i + " not testable - value == null");
            }
        }
    }

    private TreeTableModel createTreeTableModel() {
        NoteTreeNode root = new NoteTreeNode
            (new Note("SwingLabs", "todo...", false));
        NoteTreeNode swingx = new NoteTreeNode(
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
        DefaultTreeTableModel model = new DefaultTreeTableModel(root);
        return model;
    }

    public static class NoteTreeNode extends AbstractMutableTreeTableNode {

        public NoteTreeNode(Note note) {
            super(note);
        }
        
        public Class getColumnClass(int column) {
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
                return new Boolean(note.complete);
            }
            return null;
        }

        public boolean isCellEditable(int column) {
            return true; //column != 0;
        }

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

        public int getHierarchicalColumn() {
            return 0;
        }
        
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

}
