/*
 * Created on 18.06.2008
 *
 */
package netbeans.xoutline;

import javax.swing.Icon;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.border.Border;

import org.jdesktop.swingx.renderer.TableCellContext;

/**
 * PENDING JW: need border around editor - here or ...?
 */
public class TreeTableCellContext extends TableCellContext {

    /** the icon to use for a leaf node. */
    protected Icon leafIcon;

    /** the default icon to use for a closed folder. */
    protected Icon closedIcon;

    /** the default icon to use for a open folder. */
    protected Icon openIcon;

    /** the border around a focused node. */
    private Border treeFocusBorder;
    
    private Icon expandedHandle;
    private Icon collapsedHandle;

    private int depth;
    private boolean editing;

    public JXXTreeTable getTreeTable() {
        if (getComponent() instanceof JXXTreeTable)
            return (JXXTreeTable) getComponent();
        return null;
    }
    
    
    
    @Override
    public void installContext(JTable component, Object value, int row,
            int column, boolean selected, boolean focused, boolean expanded,
            boolean leaf) {
        super.installContext(component, value, row, column, selected, focused,
                expanded, leaf);
        this.editing = false;
        updateDepth();
    }

    public void installEditingContext(JTable component, Object value, int row,
            int column, boolean selected, boolean focused, boolean expanded,
            boolean leaf) {
        installContext(component, value, row, column, selected, focused,
                expanded, leaf);
        this.editing = true;
    }


    private void updateDepth() {
        if (getTreeTable() == null) {
            depth = 0;
        } else {
            depth = getTreeTable().getVisualDepth(row);
        }
        
    }

    public boolean isEditing() {
        return editing;
    }
    
    public int getVisualDepth() {
        return depth;
    }

    
    @Override
    public boolean isSelected() {
        return isEditing() ? false : super.isSelected();
    }



    /**
     * Returns the default icon to use for leaf cell.
     * 
     * @return the icon to use for leaf cell.
     */
    protected Icon getLeafIcon() {
        return leafIcon != null ? leafIcon : UIManager
                .getIcon(("Tree.leafIcon"));
    }

    /**
     * Returns the default icon to use for open cell.
     * 
     * @return the icon to use for open cell.
     */
    protected Icon getOpenIcon() {
        return openIcon != null ? openIcon : UIManager
                .getIcon("Tree.openIcon");
    }

    /**
     * Returns the default icon to use for closed cell.
     * 
     * @return the icon to use for closed cell.
     */
    protected Icon getClosedIcon() {
        return closedIcon != null ? closedIcon : UIManager
                .getIcon("Tree.closedIcon");
    }

    /**
     * {@inheritDoc}
     * <p>
     * 
     * Overridden to return a default depending for the leaf/open cell state.
     */
    @Override
    public Icon getIcon() {
        if (isLeaf()) {
            return getLeafIcon();
        }
        if (isExpanded()) {
            return getOpenIcon();
        }
        return getClosedIcon();
    }

    /**
     * Returns the default icon to use for leaf cell.
     * 
     * @return the icon to use for leaf cell.
     */
    protected Icon getExpandedHandle() {
        return expandedHandle != null ? expandedHandle : UIManager
                .getIcon(("Tree.expandedIcon"));
    }

    /**
     * Returns the default icon to use for leaf cell.
     * 
     * @return the icon to use for leaf cell.
     */
    protected Icon getCollapsedHandle() {
        return collapsedHandle != null ? collapsedHandle : UIManager
                .getIcon(("Tree.collapsedIcon"));
    }

    public Icon getHandle() {
        if (isLeaf()) return null;
        if (isExpanded()) return getExpandedHandle();
        return getCollapsedHandle();
    }
}
