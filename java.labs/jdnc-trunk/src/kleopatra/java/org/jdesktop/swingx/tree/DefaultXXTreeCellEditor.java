
/*
 * $Id: DefaultXXTreeCellEditor.java 1630 2007-08-10 15:05:41Z kleopatra $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */
package org.jdesktop.swingx.tree;

import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.EventObject;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultCellEditor;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.event.CellEditorListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.plaf.FontUIResource;
import javax.swing.tree.TreeCellEditor;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

/**
 * 
 * Unused ...
 * 
 * A <code>TreeCellEditor</code>. The icons to use are obtained by 
 * appropriately configuring the tree's renderer and check if it has any
 * icon set. You can optionally supply
 * a <code>TreeCellEditor</code> that will be layed out according
 * to the obtained icon.
 * If you do not supply a <code>TreeCellEditor</code>,
 * a <code>TextField</code> will be used. Editing is started
 * on a triple mouse click, or after a click, pause, click and
 * a delay of 1200 miliseconds.
 *<p>
 *
 * This one is mostly a refactored c&p job from core. It differs in
 * 
 * <ul>
 * <li> bidi-compliance (solving part of #4980473)
 * <li> support of dynamic icons, can cope with any renderer that 
 *    returns a JLabel as rendererComponent
 * <li> still has some open/unchecked spots (see section pending below :-)
 * </ul>
 *
 * <strong>Note: </strong> Basically, this is shareable 
 * between different instances of JTree - but only if developers take care
 * of terminating all edits in one tree before starting an edit on another! 
 * As long as JTree doesn't support this automatically (with a similar 
 * strategy as used in JTable, f.i.) it's recommended to use the per-tree
 * constructors. <p>
 * 
 * PENDING: Some open issues which are not yet checked/are difficult to solve
 * <ul>
 * <li> font setting consistent with core default?
 * <li> behaviour with different icon heights
 * <li> guarantee minimum width (tricky for RToL, 
 *      need to move the editingContainer? or need help from BasicTreeUI?)
 * <li> serialization support?
 * <li> behaviour with custom TreeCellEditors?
 * </ul>
 * 
 * <strong>Warning:</strong>
 * Serialized objects of this class will not be compatible with
 * future Swing releases. The current serialization support is
 * appropriate for short term storage or RMI between applications running
 * the same version of Swing.  As of 1.4, support for long term storage
 * of all JavaBeans<sup><font size="-2">TM</font></sup>
 * has been added to the <code>java.beans</code> package.
 * Please see {@link java.beans.XMLEncoder}.
 *
 * @see javax.swing.JTree
 *
 * @version 1.30 12/19/03
 * @author Scott Violet
 */
public class DefaultXXTreeCellEditor implements ActionListener, TreeCellEditor,
            TreeSelectionListener {
    /** Editor handling the editing. */
    protected TreeCellEditor               realEditor;


    /** Editing container, will contain the <code>editorComponent</code>. */
    protected EditorContainer                    editingContainer;


    /**
     * Used in editing. Indicates x position to place
     * <code>editingComponent</code>.
     */
    protected transient int                offset;

    /** <code>JTree</code> instance listening too. */
    protected transient JTree              tree;

    /** Last path that was selected. */
    protected transient TreePath           lastPath;

    /** Used before starting the editing session. */
    protected transient Timer              timer;

    /**
     * Row that was last passed into 
     * <code>getTreeCellEditorComponent</code> or calcuated from a 
     * MouseEvent in <code>isCellEditable</code>. It is used in calculating
     * the hitRegion for starting edits with the mouse.
     */
    protected transient int                lastRow;

    /** True if the border selection color should be drawn. */
    protected Color                        borderSelectionColor;


    /**
     * Font to paint with, <code>null</code> indicates
     * font of renderer is to be used. 
     */
    protected Font                         font;

    public DefaultXXTreeCellEditor() {
        this(null);
    }

    /**
     * Constructs a <code>DefaultTreeCellEditor</code>
     * object for a JTree using the specified renderer and
     * a default editor. (Use this constructor for normal editing.)
     *
     * @param tree      a <code>JTree</code> object
     * @param xtable  a <code>DefaultTreeCellRenderer</code> object
     */
    public DefaultXXTreeCellEditor(JTree tree) {
        this(tree, null);
    }

    /**
     * Constructs a <code>DefaultTreeCellEditor</code>
     * object for a <code>JTree</code> using the
     * specified renderer and the specified editor. (Use this constructor
     * for specialized editing.)
     *
     * @param tree      a <code>JTree</code> object
     * @param xtable  a <code>DefaultTreeCellRenderer</code> object
     * @param editor    a <code>TreeCellEditor</code> object
     */
    public DefaultXXTreeCellEditor(JTree tree, TreeCellEditor editor) {
        realEditor = editor;
        if(realEditor == null)
            realEditor = createTreeCellEditor();
        editingContainer = createContainer();
        setTree(tree);
        setBorderSelectionColor(UIManager.getColor
                                ("Tree.editorBorderSelectionColor"));
    }

    /**
      * Sets the color to use for the border.
      * @param newColor the new border color
      */
    public void setBorderSelectionColor(Color newColor) {
        borderSelectionColor = newColor;
    }

    /**
      * Returns the color the border is drawn.
      * @return the border selection color
      */
    public Color getBorderSelectionColor() {
        return borderSelectionColor;
    }

    /**
     * Sets the font to edit with. <code>null</code> indicates
     * the renderers font should be used. This will NOT
     * override any font you have set in the editor
     * the receiver was instantied with. If <code>null</code>
     * for an editor was passed in a default editor will be
     * created that will pick up this font.
     *
     * @param font  the editing <code>Font</code>
     * @see #getFont
     */
    public void setFont(Font font) {
        this.font = font;
    }

    /**
     * Gets the font used for editing.
     *
     * @return the editing <code>Font</code>
     * @see #setFont
     */
    public Font getFont() {
        return font;
    }

    //
    // TreeCellEditor
    //

    /**
     * Configures the editor.  Passed onto the <code>realEditor</code>.
     */
    public Component getTreeCellEditorComponent(JTree tree, Object value,
                                                boolean isSelected,
                                                boolean expanded,
                                                boolean leaf, int row) {
        Component rendererComponent = 
            getRendererComponent(tree, value, isSelected, expanded, leaf, row);

        Component editingComponent = realEditor.getTreeCellEditorComponent(tree, value,
                                        isSelected, expanded,leaf, row);


        Font            font = getFont();

        if(font == null) {
            font = rendererComponent.getFont();
            if(font == null)
                font = tree.getFont();
        }
        editingContainer.setFont(font);
        editingContainer.prepareForEditing(editingComponent, rendererComponent);
        return editingContainer;
    }

    /**
     * 
     * Returns the renderer component used for rendering the given value.
     *  
     * As a side-effects, it updates internal state of this: 
     *  tree, lastRow, offset.
     *  
     * PRE: tree != null.  
     * 
     * @param tree
     * @param value
     * @param isSelected
     * @param expanded
     * @param leaf
     * @param row
     * @return
     */
    private Component getRendererComponent(JTree tree, Object value, boolean isSelected, boolean expanded, boolean leaf, int row) {
        setTree(tree);
        lastRow = row;
        TreeCellRenderer renderer = tree.getCellRenderer();
        Component rendererComponent = renderer.
            getTreeCellRendererComponent(tree, value, isSelected, expanded, leaf, row, false);
       offset = editingContainer.getOffset(rendererComponent);
       return rendererComponent;
    }

    /**
     * Returns the value currently being edited.
     * @return the value currently being edited
     */
    public Object getCellEditorValue() {
        return realEditor.getCellEditorValue();
    }

    /**
     * If the <code>realEditor</code> returns true to this message,
     * <code>prepareForEditing</code> is messaged and true is returned.
     */
    public boolean isCellEditable(EventObject event) {

        boolean editable = isEditable(event);

        if (!realEditor.isCellEditable(event)) return false;
        
        if (canEditImmediately(event)) {
            return true;
        } 
        if (editable && shouldStartEditingTimer(event)) {
            startEditingTimer();
        } else if (timer != null && timer.isRunning()) {
            timer.stop();
        }
        return false;
    }

    /**
     * returns editable as far as this can decide from the given eventObject.
     * 
     * As a side-effect, it may update internal state of this: 
     *  tree, lastRow, offset.
     * 
     * @param event
     * @return true if the treePath for the event is the same (!= null) 
     *  as the last selected, false otherwise.
     */
    private boolean isEditable(EventObject event) {
        if (event == null) return false;
        if (event.getSource() instanceof JTree) {
            setTree((JTree) event.getSource());
            TreePath path = getTreePath(event);
            return (lastPath != null && path != null &&
                    lastPath.equals(path));

        }
        return false;
    }

    /**
     * 
     * returns a TreePath for the given EventObject.
     * Here: tree.getPathForLocation(mouse_location) is messaged if the
     *   event is a MouseEvent.
     * 
     * 
     * As a side-effects, it may update internal state of this 
     * if the returned path != null: 
     *  tree, lastRow, offset.
     * 
     * Pre: event != null AND event.getSource() instance of JTree.
     * 
     * @param event
     * @return the path for the given EventObject, may be null.
     */
    private TreePath getTreePath(EventObject event) {
        JTree tree = (JTree)event.getSource();
        if (event instanceof MouseEvent) {
            TreePath path = tree.getPathForLocation(
                                 ((MouseEvent)event).getX(),
                                 ((MouseEvent)event).getY());
            if (path != null) {
                int row = tree.getRowForPath(path);
                Object value = path.getLastPathComponent();
                boolean isSelected = tree.isRowSelected(row);
                boolean expanded = tree.isExpanded(path);
                TreeModel treeModel = tree.getModel();
                boolean leaf = treeModel.isLeaf(value);
                getRendererComponent(tree, value, isSelected, expanded, leaf, row);
            }
            return path;
        }
        return null;
    }


    /**
     * Messages the <code>realEditor</code> for the return value.
     */
    public boolean shouldSelectCell(EventObject event) {
        return realEditor.shouldSelectCell(event);
    }

    /**
     * If the <code>realEditor</code> will allow editing to stop,
     * the <code>realEditor</code> is removed and true is returned,
     * otherwise false is returned.
     */
    public boolean stopCellEditing() {
        if(realEditor.stopCellEditing()) {
            cleanupAfterEditing();
            return true;
        }
        return false;
    }

    /**
     * 
     */
    private void cleanupAfterEditing() {
        editingContainer.cleanupAfterEditing();
    }

    /**
     * Messages <code>cancelCellEditing</code> to the 
     * <code>realEditor</code> and removes it from this instance.
     */
    public void cancelCellEditing() {
        cleanupAfterEditing();
        realEditor.cancelCellEditing();
    }

    /**
     * Adds the <code>CellEditorListener</code>.
     * @param l the listener to be added
     */
    public void addCellEditorListener(CellEditorListener l) {
        realEditor.addCellEditorListener(l);
    }

    /**
      * Removes the previously added <code>CellEditorListener</code>.
      * @param l the listener to be removed
      */
    public void removeCellEditorListener(CellEditorListener l) {
        realEditor.removeCellEditorListener(l);
    }

    /**
     * Returns an array of all the <code>CellEditorListener</code>s added
     * to this DefaultTreeCellEditor with addCellEditorListener().
     *
     * @return all of the <code>CellEditorListener</code>s added or an empty
     *         array if no listeners have been added
     * @since 1.4
     */
    public CellEditorListener[] getCellEditorListeners() {
        return ((DefaultCellEditor)realEditor).getCellEditorListeners();
    }

    //
    // TreeSelectionListener
    //

    /**
     * Resets <code>lastPath</code>.
     */
    public void valueChanged(TreeSelectionEvent e) {
        if(tree != null) {
            if(tree.getSelectionCount() == 1)
                lastPath = tree.getSelectionPath();
            else
                lastPath = null;
        }
        if(timer != null) {
            timer.stop();
        }
    }

    //
    // ActionListener (for Timer).
    //

    /**
     * Messaged when the timer fires, this will start the editing
     * session.
     */
    public void actionPerformed(ActionEvent e) {
        if(tree != null && lastPath != null) {
            tree.startEditingAtPath(lastPath);
        }
    }

    //
    // Local methods
    //

    /**
     * Sets the tree currently editing for. This is needed to add
     * a selection listener.
     * @param newTree the new tree to be edited
     */
    protected void setTree(JTree newTree) {
        if(tree != newTree) {
            // uninstall old listener, reset internal state
            if(tree != null)
                tree.removeTreeSelectionListener(this);
            if(timer != null) {
                timer.stop();
            }
            lastRow = -1;
            lastPath = null;
            // install new
            tree = newTree;
            if(tree != null)
                tree.addTreeSelectionListener(this);
        }
    }

    /**
     * Returns true if <code>event</code> is a <code>MouseEvent</code>
     * and the click count is 1.
     * @param event  the event being studied
     */
    protected boolean shouldStartEditingTimer(EventObject event) {
        if((event instanceof MouseEvent) &&
            SwingUtilities.isLeftMouseButton((MouseEvent)event)) {
            MouseEvent        me = (MouseEvent)event;

            return (me.getClickCount() == 1 &&
                    inHitRegion(me.getX(), me.getY()));
        }
        return false;
    }

    /**
     * Starts the editing timer.
     */
    protected void startEditingTimer() {
        if(timer == null) {
            timer = new Timer(1200, this);
            timer.setRepeats(false);
        }
        timer.start();
    }

    /**
     * Returns true if <code>event</code> is <code>null</code>,
     * or it is a <code>MouseEvent</code> with a click count > 2
     * and <code>inHitRegion</code> returns true.
     * @param event the event being studied
     */
    protected boolean canEditImmediately(EventObject event) {
        if((event instanceof MouseEvent) &&
           SwingUtilities.isLeftMouseButton((MouseEvent)event)) {
            MouseEvent       me = (MouseEvent)event;

            return ((me.getClickCount() > 2) &&
                    inHitRegion(me.getX(), me.getY()));
        }
        return (event == null);
    }

    /**
     * Returns true if the passed in location is a valid mouse location
     * to start editing from. This is implemented to return false if
     * <code>x</code> is <= the width of the icon and icon gap displayed
     * by the renderer. In other words this returns true if the user
     * clicks over the text part displayed by the renderer, and false
     * otherwise.
     * @param x the x-coordinate of the point
     * @param y the y-coordinate of the point
     * @return true if the passed in location is a valid mouse location
     */
    protected boolean inHitRegion(int x, int y) {
        if(lastRow != -1 && tree != null) {
            Rectangle bounds = tree.getRowBounds(lastRow);
            ComponentOrientation treeOrientation = tree.getComponentOrientation();
            
            if ( treeOrientation.isLeftToRight() ) {
                if (bounds != null && x <= (bounds.x + offset) &&
                    offset < (bounds.width - 5)) {
                    return false;
                }
            } else if ( bounds != null &&
                        ( x >= (bounds.x+bounds.width-offset+5) ||
                          x <= (bounds.x + 5) ) &&
                        offset < (bounds.width - 5) ) {
                return false;
            }
        }
        return true;
    }


    /**
     * Creates the container to manage placement of 
     * <code>editingComponent</code>.
     */
    protected EditorContainer createContainer() {
        return new EditorContainer();
    }

    /**
     * This is invoked if a <code>TreeCellEditor</code>
     * is not supplied in the constructor.
     * It returns a <code>TextField</code> editor.
     * @return a new <code>TextField</code> editor
     */
    protected TreeCellEditor createTreeCellEditor() {
        Border              aBorder = UIManager.getBorder("Tree.editorBorder");
        DefaultCellEditor   editor = new DefaultCellEditor
            (new DefaultTextField(aBorder)); 
        // One click to edit.
        editor.setClickCountToStart(1);
        return editor;
    }


    // Serialization support.
    @SuppressWarnings("unchecked")
    private void writeObject(ObjectOutputStream s) throws IOException {
        Vector      values = new Vector();

        s.defaultWriteObject();
        // Save the realEditor, if its Serializable.
        if(realEditor != null && realEditor instanceof Serializable) {
            values.addElement("realEditor");
            values.addElement(realEditor);
        }
        s.writeObject(values);
    }

    private void readObject(ObjectInputStream s)
        throws IOException, ClassNotFoundException {
        s.defaultReadObject();

        Vector          values = (Vector)s.readObject();
        int             indexCounter = 0;
        int             maxCounter = values.size();

        if(indexCounter < maxCounter && values.elementAt(indexCounter).
           equals("realEditor")) {
            realEditor = (TreeCellEditor)values.elementAt(++indexCounter);
            indexCounter++;
        }
    }


    /**
     * <code>TextField</code> used when no editor is supplied.
     * This textfield locks into the border it is constructed with.
     * It also prefers its parents font over its font. And if the
     * renderer is not <code>null</code> and no font
     * has been specified the preferred height is that of the renderer.
     */
    public class DefaultTextField extends JTextField {
        /** Border to use. */
        protected Border         border;
        /**
         * Constructs a
         * <code>DefaultTreeCellEditor.DefaultTextField</code> object.
         *
         * @param border  a <code>Border</code> object
         */
        public DefaultTextField(Border border) {
            setBorder(border);
        }

        /**
         * Sets the border of this component.<p>
         * This is a bound property.
         *
         * @param border the border to be rendered for this component
         * @see Border
         * @see CompoundBorder
         * @beaninfo
         *        bound: true
         *    preferred: true
         *    attribute: visualUpdate true
         *  description: The component's border.
         */
        public void setBorder(Border border) {
            super.setBorder(border);
            this.border = border;
        }

        /**
         * Overrides <code>JComponent.getBorder</code> to
         * returns the current border.
         */
        public Border getBorder() {
            return border;
        }

        // implements java.awt.MenuContainer
        public Font getFont() {
            Font     font = super.getFont();

            // Prefer the parent containers font if our font is a
            // FontUIResource
            if(font instanceof FontUIResource) {
                Container     parent = getParent();

                if(parent != null && parent.getFont() != null)
                    font = parent.getFont();
            }
            return font;
        }

        /**
         * Overrides <code>JTextField.getPreferredSize</code> to
         * return the preferred size based on current font, if set,
         * or else use renderer's font.
         * @return a <code>Dimension</code> object containing
         *   the preferred size
         */
//        public Dimension getPreferredSize() {
//            Dimension      size = super.getPreferredSize();
//
//            // If not font has been set, prefer the renderers height.
//            if(renderer != null &&
//               DefaultXXTreeCellEditor.this.getFont() == null) {
//                Dimension     rSize = renderer.getPreferredSize();
//
//                size.height = rSize.height;
//            }
//            return size;
//        }
    }


    /**
     * Container responsible for placing the <code>editingComponent</code>.
     */
    public class EditorContainer extends JPanel {
        
        private int gap = 4;
        private JLabel iconLabel;
        private Border rtolIconBorder; 
        private Border ltorIconBorder;

        /**
         * Constructs an <code>EditorContainer</code> object.
         */
        public EditorContainer() {
            setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
            iconLabel = new JLabel();
            add(iconLabel);
            createIconBorders(gap);
        }

        private void removeEditingComponent() {
            if (getComponentCount() > 1) {
                remove(1);
            }
        }
        
        private void addEditingComponent(Component editingComponent) {
            removeEditingComponent();
            if (editingComponent != null) {
                add(editingComponent);
            }
        }

        /**
         * Invoked just before editing is to start. Will add the
         * <code>editingComponent</code> to the
         * <code>editingContainer</code>.
         */
        public void prepareForEditing(Component editingComponent, Component rendererComponent) {
            Icon icon = null;
            int iconGap = -1;
            if (rendererComponent instanceof JLabel) {
                icon = ((JLabel) rendererComponent).getIcon();
                iconGap = ((JLabel) rendererComponent).getIconTextGap();
            }
            iconLabel.setIcon(icon);
            setBackground(rendererComponent.getBackground());
            addEditingComponent(editingComponent);
            updateIconBorders(iconGap);
            if (tree != null) {
                applyComponentOrientation(tree.getComponentOrientation());
            }
            revalidate();
            // visual debugging
//            setBorder(BorderFactory.createLineBorder(Color.RED));
        }
        
        public int getOffset(Component rendererComponent) {
            if (rendererComponent instanceof JLabel) {
                JLabel label = (JLabel) rendererComponent;
                int iconWidth = (label.getIcon() != null) ? 
                        label.getIcon().getIconWidth() : 0;
                return label.getIconTextGap() + iconWidth;
            }
            return 0;
        }
        private void updateIconBorders(int iconGap) {
            if (iconGap < 0) {
                iconGap = gap;
            }
            Insets borderInsets = ltorIconBorder.getBorderInsets(null);
            if (iconGap == borderInsets.right) return;
            createIconBorders(iconGap);
        }

        /**
         * hack to keep some gap between the label and the editing component.
         * CHECK: shouldn't borders be painted orientation-aware always?
         * 
         * PENDING: the "left" inset for RToL needs to be 2 pixels larger than the 
         * "right" inset for LToR to keep the icon horizontally stable. Hmmm...
         * 
         * @param iconGap
         */
        private void createIconBorders(int iconGap) {
            ltorIconBorder = BorderFactory.createEmptyBorder(0, 0, 0, iconGap);
            rtolIconBorder = BorderFactory.createEmptyBorder(0, iconGap + 2, 0, 0);
            
        }

        /**
         * Cleans up any state after editing has completed. Removes the
         * <code>editingComponent</code> the <code>editingContainer</code>.
         */
        public void cleanupAfterEditing() {
            removeEditingComponent();
        }

        @Override
        public void applyComponentOrientation(ComponentOrientation o) {
            super.applyComponentOrientation(o);
            if (o.isLeftToRight()) {
                iconLabel.setBorder(ltorIconBorder);
            } else {
                iconLabel.setBorder(rtolIconBorder);
            }
        }

    }  
}
