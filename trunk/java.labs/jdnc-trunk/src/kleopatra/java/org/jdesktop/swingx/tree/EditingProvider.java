/*
 * Created on 10.08.2007
 *
 */
package org.jdesktop.swingx.tree;

import java.awt.Component;
import java.awt.Font;
import java.awt.Rectangle;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.TreeCellEditor;
import javax.swing.tree.TreeCellRenderer;

import org.jdesktop.swingx.renderer.TreeCellContext;
import org.jdesktop.swingx.renderer.WrappingIconPanel;
import org.jdesktop.swingx.renderer.WrappingProvider;

/**
 * A wrapping provider specialized on editing. Takes the 
 * icon from the tree's renderer.  
 * 
 * PENDING: .... nearly everything, it's a first go ...
 * Not yet really a provider, bypassing the defaults for the
 * editorComp.
 * 
 * @author Jeanette Winzenburg
 */
public class EditingProvider extends WrappingProvider {

    private Rectangle delegateBounds;
    private Component iconSource;

    /**
     * Configures a editing component from the given context and 
     * TreeCellEditor.
     * 
     * @param cellContext the cellContext to configure from, 
     *     must not be null.
     * @param realEditor the provider of the editing component, 
     *     must not be null
     * @return a configured component used for editing.
     */
    public Component getEditingContainer(TreeCellContext cellContext, 
            TreeCellEditor realEditor) {
        Component editingComponent = realEditor.getTreeCellEditorComponent(
                cellContext.getComponent(), cellContext.getValue(),
                cellContext.isSelected(), cellContext.isExpanded(),
                cellContext.isLeaf(), cellContext.getRow());

        rendererComponent.setComponent((JComponent) editingComponent);
        rendererComponent.setIcon(getIcon(iconSource));
        return rendererComponent;
    }

    /**
     * Installs the icon source from the given cellContext.
     * 
     * @param cellContext the context to configure the iconsource from.
     */
    public void installIconSource(TreeCellContext cellContext) {
        iconSource = getRendererComponent(cellContext);
        if (iconSource != null) {
            rendererComponent.applyComponentOrientation(iconSource.getComponentOrientation());
        }    
        if (iconSource instanceof WrappingIconPanel) {
            delegateBounds = ((WrappingIconPanel) iconSource).getDelegateBounds();
        } else {
            delegateBounds = null;
        }
        
    }

    /**
     * Returns a boolean to indicate if the given x coordinate is
     * over the editing component. <p>
     * 
     * Note that clients must have installed the icon source before
     * this returns anything reasonable. 
     * 
     * @param bounds the pathBounds (in tree coordinates)
     * @param x the horizontal position (in tree coordinates)
     * @param y the vertical position (in tree coordinates) - unused
     * @return true if the position is over the real editing component, false
     *    otherwise.
     */
    public boolean inHitRegion(Rectangle bounds, int x, int y) {
        if ((bounds == null) || (delegateBounds == null)) return true;
        int childX = x - bounds.x;
        return delegateBounds.contains(childX, 5);
    }

    public void setFont(Font font) {
        rendererComponent.setFont(font);
        
    }


    /**
     * @param cellContext 
     * @return
     */
    private Component getRendererComponent(TreeCellContext cellContext) {
        JTree tree = cellContext.getComponent();
        if (tree == null) return null;
        TreeCellRenderer renderer = tree.getCellRenderer();
        Component rendererComponent = renderer.
        getTreeCellRendererComponent(tree, 
                cellContext.getValue(), 
                cellContext.isSelected(), 
                cellContext.isExpanded(), 
                cellContext.isLeaf(), 
                cellContext.getRow(), 
                cellContext.isFocused());
        return rendererComponent;
    }

    /**
     * Returns the icon to use in the editing component. Tries to
     * look-up the icon in the given component. Returns null if
     * not successful.
     * 
     * @param renderingComponent the component to try to get the icon from
     * @return the icon used by the given component or null if none is found.
     */
    private Icon getIcon(Component renderingComponent) {
        if (renderingComponent instanceof JLabel) {
            return ((JLabel) renderingComponent).getIcon();
        } else if (renderingComponent instanceof WrappingIconPanel) {
            return ((WrappingIconPanel) renderingComponent).getIcon();
        }
        return null;
    }


}
