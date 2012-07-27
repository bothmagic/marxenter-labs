/*
 * Created on 16.06.2008
 *
 */
package netbeans.xoutline;

import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.logging.Logger;

import javax.swing.JComponent;
import javax.swing.UIManager;
import javax.swing.event.MouseInputListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicTableUI;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

public class BasicTreeTableUI extends BasicTableUI {
    
    @SuppressWarnings("unused")
    private static final Logger LOG = Logger.getLogger(BasicTreeTableUI.class
            .getName());
    
    private int indentWidth;

    private TreeSelectionListener treeSelectionListener;

    private PropertyChangeListener treePropertyListener;
    
    public static ComponentUI createUI(JComponent comp) {
        return new BasicTreeTableUI();
    }

    
    @Override
    public void installUI(JComponent c) {
        if (!(c instanceof JXXTreeTable)) 
            throw new IllegalArgumentException("component must be of type JXXTreeTable");
        super.installUI(c);
    }

    protected JXXTreeTable getTreeTable() {
        return (JXXTreeTable) table;
    }
    
    @Override
    protected void installDefaults() {
        super.installDefaults();
        indentWidth = UIManager.getInt("Outline.indentWidth"); 
        if (indentWidth == 0) {
            indentWidth = 20;
        }
    }
    
    
    @Override
    protected void installListeners() {
        super.installListeners();
        getTreeTable().addPropertyChangeListener(getPropertyChangeListener());
    }


    private PropertyChangeListener getPropertyChangeListener() {
        if (treePropertyListener == null) {
            treePropertyListener = createPropertyChangeListener();
        }
        return treePropertyListener;
    }


    private PropertyChangeListener createPropertyChangeListener() {
        PropertyChangeListener l = new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if ("treeSelectionModel".equals(evt.getPropertyName())) {
                    
                    updateTreeSelectionModel(
                            (TreeSelectionModel) evt.getOldValue());
                }
                
            }};
        return l;
    }
    
    protected void updateTreeSelectionModel(TreeSelectionModel oldModel) {
        if (oldModel != null) {
            oldModel.removeTreeSelectionListener(getTreeSelectionListener());
        }
        if (getTreeTable().getTreeSelectionModel() != null) {
            getTreeTable().getTreeSelectionModel().addTreeSelectionListener(
                    getTreeSelectionListener());
        }
        XOutlineModel outline = (XOutlineModel) getTreeTable().getModel();
        outline.getLayout().setSelectionModel(getTreeTable().getTreeSelectionModel());
    }


    private TreeSelectionListener getTreeSelectionListener() {
        if (treeSelectionListener == null) {
            treeSelectionListener = createTreeSelectionListener();
        }
        return treeSelectionListener;
    }


    private TreeSelectionListener createTreeSelectionListener() {
        TreeSelectionListener l = new TreeSelectionListener() {

            @Override
            public void valueChanged(TreeSelectionEvent e) {
                expandSelectedPaths();
            }
            
        };
        return l;
    }


    protected void expandSelectedPaths() {
        if (!getTreeTable().getExpandsSelectedPaths() ||
                (getTreeTable().getTreeSelectionModel() == null)) return;
       
            TreePath[]           paths = getTreeTable().getTreeSelectionModel()
                                     .getSelectionPaths();

            if(paths == null) return; 
                for(int counter = paths.length - 1; counter >= 0;
                    counter--) {
                    TreePath path = paths[counter].getParentPath();
                    boolean expand = true;

                    while (path != null) {
                        // Indicates this path isn't valid anymore,
                        // we shouldn't attempt to expand it then.
                        if (getTreeTable().getTreeTableModel().isLeaf(path.getLastPathComponent())){
                            expand = false;
                            path = null;
                        }
                        else {
                            path = path.getParentPath();
                        }
                    }
                    if (expand) {
                        getTreeTable().makeVisible(paths[counter]);
                    }
                }
        
    }


    @Override
    protected void uninstallListeners() {
        if (treeSelectionListener != null) {
            getTreeTable().getTreeSelectionModel().removeTreeSelectionListener(treeSelectionListener);
        }
        super.uninstallListeners();
    }


    /**
     * Expands path if it is not expanded, or collapses row if it is expanded.
     * If expanding a path and JTree scrolls on expand, ensureRowsAreVisible
     * is invoked to scroll as many of the children to visible as possible
     * (tries to scroll to last visible descendant of path).
     */
    protected void toggleExpandState(TreePath path) {
        if(!getTreeTable().isExpanded(path)) {
            int       row = getTreeTable().getRowForPath(path);

            getTreeTable().expandPath(path);
//            updateSize();
            if(row != -1) {
                if(getTreeTable().getScrollsOnExpand())
                    ensureRowsAreVisible(row, row + getTreeTable().
                                         getVisibleChildCount(path));
                else
                    ensureRowsAreVisible(row, row);
            }
        }
        else {
            getTreeTable().collapsePath(path);
//            updateSize();
        }
    }
    

    
    private void ensureRowsAreVisible(int row, int last) {
        // PENDING JW: brute force implementation ...
        getTreeTable().scrollRowToVisible(last);
        if (last > row) {
            Rectangle visibleRect = getTreeTable().getVisibleRect();
            Rectangle firstCell = getTreeTable().getCellRect(row, 0, false);
            if (firstCell.y < visibleRect.y) {
                getTreeTable().scrollRowToVisible(row);
            }
        }
    }

   

    @Override
    protected MouseInputListener createMouseInputListener() {
        
        MouseInputListener l = super.createMouseInputListener();
        return createWrapper(l);
    }

    protected MouseInputListener createWrapper(MouseInputListener l) {
        MouseInputListener wrapper = new OutlineMouseInputListener(l) ;
        return wrapper;
    }
    
    

    public class OutlineMouseInputListener implements MouseInputListener {
        private MouseInputListener delegate;
        private boolean inToggle;
        
        public OutlineMouseInputListener(MouseInputListener delegate) {
            this.delegate = delegate;
        }

        public void mouseClicked(MouseEvent e) {
            if (isHandle(e)) return;
            delegate.mouseClicked(e);
        }

        public void mouseDragged(MouseEvent e) {
            if (inToggle) return;
            delegate.mouseDragged(e);
        }

        public void mouseEntered(MouseEvent e) {
            inToggle = false;
            delegate.mouseEntered(e);
        }

        public void mouseExited(MouseEvent e) {
            inToggle = false;
            delegate.mouseExited(e);
        }

        public void mouseMoved(MouseEvent e) {
            if (inToggle) return;
            delegate.mouseMoved(e);
        }

        public void mousePressed(MouseEvent e) {
            if (isHandle(e)) {
               toggleExpansion(e); 
            } else {
               delegate.mousePressed(e);
            }
        }

        private void toggleExpansion(MouseEvent e) {
            inToggle = true;
//            XOutlineModel model = (XOutlineModel) table.getModel();
            TreePath path = getTreeTable().getPathForLocation(e.getX(), e.getY());
            toggleExpandState(path);
//            int row = table.rowAtPoint(e.getPoint());
////            model.toggleExpansion(row);
            
        }

        private boolean isHandle(MouseEvent e) {
            if (!table.isEnabled()) return false;
            JXXTreeTable outline = getTreeTable();
            int row = outline.rowAtPoint(e.getPoint());
            if (row < 0) return false;
            int column = outline.columnAtPoint(e.getPoint());
            if ((column < 0) || !outline.isHierarchical(column)) return false;
            Rectangle cell = outline.getCellRect(row, column, false);
            if (outline.getComponentOrientation().isLeftToRight()) {
                int startX = cell.x + (outline.getVisualDepth(row) - 1) * indentWidth;
                int endX = startX + indentWidth;
                return (e.getX() < endX) && (e.getX() >= startX);
            } else {
                int endX = cell.x + cell.width - (outline.getVisualDepth(row) - 1) * indentWidth;
                int startX = endX - indentWidth;
                return (e.getX() < endX) && (e.getX() >= startX);
            }
        }

        public void mouseReleased(MouseEvent e) {
            if (inToggle) {
                inToggle = false;
                return;
            }
            delegate.mouseReleased(e);
        }
    }
}
