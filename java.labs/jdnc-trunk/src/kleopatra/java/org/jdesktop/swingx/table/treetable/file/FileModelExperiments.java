/*
 * Created on 25.10.2007
 *
 */
package org.jdesktop.swingx.table.treetable.file;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.logging.Logger;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreePath;

import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.JXTree;
import org.jdesktop.swingx.JXTreeTable;
import org.jdesktop.swingx.action.AbstractActionExt;
import org.jdesktop.swingx.incubatorutil.InteractiveTestCase;
import org.jdesktop.swingx.renderer.CellContext;
import org.jdesktop.swingx.renderer.DefaultTableRenderer;
import org.jdesktop.swingx.renderer.DefaultTreeRenderer;
import org.jdesktop.swingx.renderer.IconValue;
import org.jdesktop.swingx.renderer.LabelProvider;
import org.jdesktop.swingx.renderer.MappedValue;
import org.jdesktop.swingx.renderer.StringValue;
import org.jdesktop.swingx.renderer.StringValues;
import org.jdesktop.swingx.renderer.WrappingProvider;
import org.jdesktop.swingx.treetable.TreeTableModel;

import sun.awt.shell.ShellFolder;

/**
 * Experimenting with different implementations of a TreeTableModel representing
 * and rendering the file system.
 * 
 * Requirements (not all met, and all formally untested):
 * - nodes are sorted alphabetically
 * - nodes appear as in the underlying OS (use FileSystemView methods)
 * - tree is build according to system rules (recognize aliases correctly)
 * - allow to edit the file name with subsequent re-sorting of children
 * 
 * So basically all implemetations are based on wrapping the files into nodes
 * and keep sorted lists of children. They differ in the type of nodes (custom
 * vs. TreeTableNode) and accordingly model's super (scratch/abstract vs. 
 * DefaultTTM).
 * 
 * The node type has implication on rendering
 * - WrappingProvider "unwraps" a TreeTableNode (same for DefaultMutableTreeNode)
 *   into the node's userObject, so displayValues ( == converters into String/Icon)
 *   can focus on the type of the userObject instead of TreeXXNode. Here we use 
 *   converters for showing a File with the system supplied icon/display names
 * - Custom node types need custom displayValues which do the "unwrapping" themselves.    
 * 
 */
public class FileModelExperiments extends InteractiveTestCase {
    @SuppressWarnings("unused")
    private static final Logger LOG = Logger
            .getLogger(FileModelExperiments.class.getName());
    public static void main(String args[]) {
      setSystemLF(false);
      FileModelExperiments test = new FileModelExperiments();
      try {
        test.runInteractiveTests();
//        test.runInteractiveTests(".*Wrapping.*");
//        test.runInteractiveTests("interactive.*Debug.*");
      } catch (Exception e) {
          System.err.println("exception when executing interactive tests:");
          e.printStackTrace();
      }
  }
    
    public void interactiveDebugDisplayNames() {
//        UIManager.put("FileChooser.useSystemExtensionHiding",
//                Boolean.TRUE);
        TreeTableModel model = new FileExtendsDefaultTTM();
//        UIManager.put("FileChooser.useSystemExtensionHiding",
//                null);
        TreeCellRenderer renderer = new DefaultTreeRenderer(
                DisplayValues.FILE_ICON_VALUE,
                DisplayValues.FILE_NAME_VALUE);
        final JXTree tree = new JXTree(model);
        tree.setCellRenderer(renderer);
        tree.expandRow(0);
        tree.expandRow(1);
        tree.expandRow(2);
        tree.expandRow(3);
        JXFrame frame = showWithScrollingInFrame(tree, "displayed? ");
        Action log = new AbstractActionExt("print displayname") {

            @Override
            public void actionPerformed(ActionEvent e) {
                TreePath path = tree.getSelectionPath();
                if (path == null) {
                    path = tree.getPathForRow(4);
                }
                FileScratchTTNode node = (FileScratchTTNode) path.getLastPathComponent();
                File f = node.getFile();
                FileSystemView view = FileSystemView.getFileSystemView();
                String name = view.getSystemDisplayName(f);
                boolean useSystemExtensionsHiding = UIManager.getDefaults().getBoolean("FileChooser.useSystemExtensionHiding");
               JFileChooser c;
                if (f != null) {
                    name = f.getName();
                    if (!name.equals("..") && !name.equals(".") &&
//                        (useSystemExtensionsHiding ||
//                         !view.isFileSystem(f) 
//                         /* ||
//                         view.isFileSystemRoot(f) */) &&
                        ((f instanceof ShellFolder) ||
                         f.exists())) {

                        name = getShellFolder(f).getDisplayName();
                        if (name == null || name.length() == 0) {
                            name = f.getPath(); // e.g. "/"
                        }
                    }
                }

                LOG.info("name/display" + f.getName() + name + useSystemExtensionsHiding);
            }

            private ShellFolder getShellFolder(File f) {
                    try {
                        return ShellFolder.getShellFolder(f);
                    } catch (FileNotFoundException e) {
                        System.err.println("FileSystemView.getShellFolder: f="+f);
                        e.printStackTrace();
                        return null;
                    } catch (InternalError e) {
                        System.err.println("FileSystemView.getShellFolder: f="+f);
                        e.printStackTrace();
                        return null;
                    }
            }
            
        };
        addAction(frame, log);
        Action toggleFlag = new AbstractActionExt("toggle extension flag") {
            boolean forced;

            @Override
            public void actionPerformed(ActionEvent e) {
                if (forced) {
                    UIManager.put("FileChooser.useSystemExtensionHiding", null);
                } else {
                    UIManager.put("FileChooser.useSystemExtensionHiding",
                            Boolean.TRUE);
                }
                forced = !forced;

            }
        };
         addAction(frame, toggleFlag);   
            
    }
    /**
     * 
     * SwingX renderer: use custom converter. Share renderer. 
     * 
     * Model extends DefaultTTM, that is the nodes are instances of 
     * type TreeTableNode, so the wrapping provider's logic replaces
     * the value with the userObject which allows to use the
     * pre-defined display values.
     */
    public void interactiveFileExtendsDefaultTTMWrappingRenderer() {
        final TreeTableModel model = new FileExtendsDefaultTTM();
        TreeCellRenderer renderer = new DefaultTreeRenderer(
                DisplayValues.FILE_ICON_VALUE,
                DisplayValues.FILE_NAME_VALUE);
        JXTreeTable table = new JXTreeTable(model);
        table.setTreeCellRenderer(renderer);
        table.setRowHeight(25);
        JXFrame frame = wrapWithScrollingInFrame(table, "TreeTable (extend DTTM): WrappingProvider");
        frame.setVisible(true);
        JXTree tree = new JXTree(model);
        tree.setRootVisible(false);
        tree.setCellRenderer(renderer);
        tree.setEditable(true);
        tree.setRowHeight(table.getRowHeight());
        JXFrame treeFrame = wrapWithScrollingInFrame(tree, "Tree (extend DTTM): WrappingProvider");
        treeFrame.setVisible(true);
    }

    /**
     * 
     * SwingX renderer: use custom converter. Share renderer. 
     * 
     * Model implements TTM from scratch. The nodes are not TreeTableNodes.
     * Need to implement custom display values which are aware of 
     * custom node types.
     */
    public void interactiveFileScratchTTMWrappingRenderer() {
        final TreeTableModel model = new FileScratchTTM();
        TreeCellRenderer renderer = new DefaultTreeRenderer(
                createFileNodeIconValue(), 
                createFileNodeStringValue());
        JXTreeTable table = new JXTreeTable(model);
        table.setTreeCellRenderer(renderer);
        JXFrame frame = wrapWithScrollingInFrame(table, "TreeTable (from scratch): WrappingProvider");
        frame.setVisible(true);
        JXTree tree = new JXTree(model);
        tree.setRootVisible(false);
        tree.setCellRenderer(renderer);
        tree.setEditable(true);
        JXFrame treeFrame = wrapWithScrollingInFrame(tree, "Tree (from scratch): WrappingProvider");
        treeFrame.setVisible(true);
    }

    /**
     * Creates and returns a IconValue rendering custom FileNode types.
     * @return
     */
    private IconValue createFileNodeIconValue() {
        IconValue iv = new IconValue() {

            public Icon getIcon(Object value) {
                if (value instanceof FileNode) {
                    FileNode file = (FileNode) value;
                    return file.getFileIcon();
                } 
                return null;
            }
            
        };
        return iv;
    }

    /**
     * Creates and returns a IconValue rendering custom FileNode types.
     * @return
     */
    private StringValue createFileNodeStringValue() {
        StringValue sv = new StringValue() {

            public String getString(Object value) {
                if (value instanceof FileNode) {
                    FileNode file = (FileNode) value;
                    return file.getDisplayName();
                } 
                return StringValues.TO_STRING.getString(value);
            }
            
        };
        return sv;
    }

    /**
     * 
     * SwingX renderer: use custom converter. Share renderer. 
     * 
     * Model extends AbstractTTM. The nodes are not TreeTableNodes.
     * Need to implement custom display values which are aware of 
     * custom node types.
     */
    public void interactiveFileExtendsAbstractTTMWrappingRenderer() {
        final TreeTableModel model = new FileExtendsAbstractTTM();
        TreeCellRenderer renderer = new DefaultTreeRenderer(
                createFileNodeIconValue(), 
                createFileNodeStringValue());
        JXTreeTable table = new JXTreeTable(model);
        table.setTreeCellRenderer(renderer);
        JXFrame frame = wrapWithScrollingInFrame(table, "TreeTable (extend AbstractTTM): WrappingProvider");
        frame.setVisible(true);
        JXTree tree = new JXTree(model);
        tree.setRootVisible(false);
        tree.setCellRenderer(renderer);
        tree.setEditable(true);
        JXFrame treeFrame = wrapWithScrollingInFrame(tree, "Tree (extend AbstractTTM): WrappingProvider");
        treeFrame.setVisible(true);
    }

    
    /**
     * 
     * SwingX renderer: use custom converters to  show icon/string in 
     * table renderer.
     * 
     * Plain Table containing custom nodes. Render with custom display
     * values, aware of node-type.
     */
    public void interactiveFileValuesInTableRenderer() {
        // quick configure: items with per-value icons
        JXTreeTable treeTable = new JXTreeTable(new FileScratchTTM());
        treeTable.expandRow(0);
        DefaultTableModel tableModel = new DefaultTableModel(treeTable.getRowCount(), 1);
        for (int i = 0; i < treeTable.getRowCount(); i++) {
            tableModel.setValueAt(treeTable.getPathForRow(i).getLastPathComponent(), i, 0);
        }
        final JXTable table = new JXTable(tableModel);
        final IconValue iv = createFileNodeIconValue();
        table.setDefaultRenderer(Object.class, new DefaultTableRenderer(new MappedValue(
                createFileNodeStringValue(), 
                iv)));
        JXFrame treeFrame = wrapWithScrollingInFrame(table, "FileNodes as values in JXTable");
        final JLabel label = new JLabel("icon of selected node");
        ListSelectionListener l = new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting()) return;
                if (table.getSelectedRow() < 0) return;
                label.setIcon(iv.getIcon(table.getValueAt(table.getSelectedRow(), 0)));
                
            } };
        table.getSelectionModel().addListSelectionListener(l);    
        addStatusComponent(treeFrame, label);
        show(treeFrame);
    }

    /**
     * 
     * SwingX renderer: use custom provider to access
     * getValueAt(). Share renderer. 
     */
    public void interactiveScratchTTMGetValueRenderer() {
        final TreeTableModel model = new FileScratchTTM();
        LabelProvider provider = new LabelProvider() {

            @Override
            protected String getValueAsString(CellContext context) {
                // this is dirty because the design idea was to keep the renderer 
                // unaware of the context type
                TreeTableModel model = (TreeTableModel) ((JXTree) context.getComponent()).getModel();
                // beware: currently works only if the node is not a DefaultMutableTreeNode
                // otherwise the WrappingProvider tries to be smart and replaces the node
                // by the userObject before passing on to the wrappee! 
                Object nodeValue = model.getValueAt(context.getValue(), 0);
                return formatter.getString(nodeValue);
            }
            
        };
        WrappingProvider controller = new WrappingProvider(provider);
        controller.setStringValue(new MappedValue(null, createFileNodeIconValue()));
        TreeCellRenderer renderer = new DefaultTreeRenderer(controller);
        JXTreeTable table = new JXTreeTable(model);
        table.setTreeCellRenderer(renderer);
        JXFrame frame = wrapWithScrollingInFrame(table, "TreeTable: wrapping Renderer - getValueAt");
        frame.setVisible(true);
        JXTree tree = new JXTree(model);
        tree.setCellRenderer(renderer);
        tree.setEditable(true);
        JXFrame treeFrame = wrapWithScrollingInFrame(tree, "Tree: wrapping Renderer - getValueAt");
        treeFrame.setVisible(true);
    }

    /**
     * Traditional renderer.
     *
     */
    public void interactiveFileExtendsDefaultTTMCoreRenderer() {
        final TreeTableModel model = new FileExtendsDefaultTTM();
        FileNodeTreeCellRenderer renderer = new FileNodeTreeCellRenderer();
        JXTreeTable table = new JXTreeTable(model);
        table.setTreeCellRenderer(renderer);
        JXFrame frame = wrapWithScrollingInFrame(table, "TreeTable - extends DTTM, core renderer");
        frame.setVisible(true);
        JXTree tree = new JXTree(model);
        tree.setCellRenderer(renderer);
        tree.setEditable(true);
        JXFrame treeFrame = wrapWithScrollingInFrame(tree, "Tree - extends DTTM, core renderer");
        treeFrame.setVisible(true);
    }

    /**
     * Standard File renderer extending DefaultTreeCellRenderer.
     */
    public class FileNodeTreeCellRenderer extends DefaultTreeCellRenderer {

        public Component getTreeCellRendererComponent(JTree tree, Object value,
                boolean sel, boolean expanded, boolean leaf, int row,
                boolean hasFocus) {
                if (value instanceof FileScratchTTNode) {
                    FileScratchTTNode fileNode = (FileScratchTTNode) value;
                    super.getTreeCellRendererComponent(tree, 
                            fileNode.getDisplayName(), sel, expanded,
                            leaf, row, hasFocus);
                    setIcon(fileNode.getFileIcon());
                    
                } else {
                    super.getTreeCellRendererComponent(tree, value, sel, expanded,
                            leaf, row, hasFocus);
                   
                }
                return this;
        }
        
    }


}
