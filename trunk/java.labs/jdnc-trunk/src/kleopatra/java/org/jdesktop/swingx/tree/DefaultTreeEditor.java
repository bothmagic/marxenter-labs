
/*
 * $Id: DefaultTreeEditor.java 1647 2007-08-16 11:10:48Z kleopatra $
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
import java.awt.Container;
import java.awt.Font;
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

import javax.swing.DefaultCellEditor;
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
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import org.jdesktop.swingx.renderer.TreeCellContext;

/**
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
 * This one is a refactored c&p job from core. Keeps the control
 * (timer, editor listening) and delegates the actual layout
 * to EditingProvider. It differs in
 * 
 * <ul>
 * <li> bidi-compliance (solving part of #4980473)
 * <li> support of dynamic icons (EditingProvider can cope 
 *    with any renderer that 
 *    returns a JLabel or WrappingIconPanel as rendererComponent)
 * <li> still has some open/unchecked spots (see section pending below :-)
 * </ul>
 *
 * <strong>Note: </strong> Basically, this is shareable 
 * between different instances of JTree - but only if developers take care
 * of terminating all edits in one tree before starting an edit on another! 
 * As long as JTree doesn't support this automatically (with a similar 
 * strategy as used in JTable, f.i.) it's recommended to use a per-tree
 * editor. <p>
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
 * @author Scott Violet
 * @author Jeanette Winzenburg
 */
public class DefaultTreeEditor implements ActionListener, TreeCellEditor,
            TreeSelectionListener {
    
    
    /** Editor handling the editing. */
    protected TreeCellEditor               realEditor;


    protected transient TreeCellContext cellContext;
    protected transient EditingProvider provider;

    /** <code>JTree</code> instance listening too. */
    protected transient JTree              tree;

    /** Last path that was selected. */
    protected transient TreePath           lastPath;

    /**
     * Row that was last passed into 
     * <code>getTreeCellEditorComponent</code> or calcuated from a 
     * MouseEvent in <code>isCellEditable</code>. It is used in calculating
     * the hitRegion for starting edits with the mouse.
     */
    protected transient int                lastRow;

    /** Used before starting the editing session. */
    protected transient Timer              timer;
    
    
    /** True if the border selection color should be drawn. */
    protected Color                        borderSelectionColor;


    /**
     * Font to paint with, <code>null</code> indicates
     * font of renderer is to be used. 
     */
    protected Font                         font;

    /**
     * Constructs a <code>DefaultTreeEditor</code> with 
     * a JTextField as editor.
     *
     */
    public DefaultTreeEditor() {
        this(null);
    }


    /**
     * Constructs a <code>DefaultTreeEditor</code>
     * object for a <code>JTree</code> using the
     * specified specified editor. (Use this constructor
     * for specialized editing.)
     *
     * @param editor the <code>TreeCellEditor</code> to delegate the editing
     *   to.
     */
    public DefaultTreeEditor(TreeCellEditor editor) {
        // PENDING: serialization? this is sure not serializable
        cellContext = new TreeCellContext();
        provider = createEditingProvider();
        if(editor == null)
            editor = createTreeCellEditor();
        realEditor = editor;
        setBorderSelectionColor(UIManager.getColor
                                ("Tree.editorBorderSelectionColor"));
    }

//-------------------- properties (unused so far ...)
    
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

// ------------------------- implement TreeCellEditor
    
    /**
     * Configures the editor and internal state.  
     */
    public Component getTreeCellEditorComponent(JTree tree, Object value,
                                                boolean isSelected,
                                                boolean expanded,
                                                boolean leaf, int row) {
        installContextAndProvider(tree, value, isSelected, expanded, leaf, row);
        provider.setFont(font);
        return provider.getEditingContainer(cellContext, realEditor);
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
     * Returns the value currently being edited.
     * @return the value currently being edited
     */
    public Object getCellEditorValue() {
        return realEditor.getCellEditorValue();
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



//---------------------------- install/control editing

    /**
     * 
     */
    private void cleanupAfterEditing() {
        setTree(null);
    }


    /**
     * 
     * Returns the renderer component used for rendering the given value.
     *  
     * As a side-effects, it updates internal state of this: 
     *  tree, lastRow, offset.
     *  
     *  PRE: context installed.
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
    private void installContextAndProvider(JTree tree, Object value, boolean isSelected, boolean expanded, boolean leaf, int row) {
       installContext(tree, value, isSelected, expanded, leaf, row);
       prepareProviderOffset();
    }

    private void prepareProviderOffset() {
        if (cellContext.getComponent() == null) return;
        provider.installIconSource(cellContext);
    }



    /**
     * @param tree
     * @param value
     * @param isSelected
     * @param expanded
     * @param leaf
     * @param row
     */
    private void installContext(JTree tree, Object value, boolean isSelected, boolean expanded, boolean leaf, int row) {
        cellContext.installContext(tree, value, row, 0, isSelected, false, expanded, leaf);
        setTree(tree);
        lastRow = row;
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
//            setTree((JTree) event.getSource());
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
                installContext(tree, path);
            }
            return path;
        }
        return null;
    }


    /**
     * @param tree
     * @param path
     */
    private void installContext(JTree tree, TreePath path) {
        int row = tree.getRowForPath(path);
        Object value = path.getLastPathComponent();
        boolean isSelected = tree.isRowSelected(row);
        boolean expanded = tree.isExpanded(path);
        TreeModel treeModel = tree.getModel();
        boolean leaf = treeModel.isLeaf(value);
        installContextAndProvider(tree, value, isSelected, expanded, leaf, row);
    }

//  -------------------- implement TreeSelectionListener

    /**
     * PENDING: remove from public api.
     * Resets <code>lastPath</code>.
     */
    public void valueChanged(TreeSelectionEvent e) {
        if(getTree() != null) {
            if(getTree().getSelectionCount() == 1)
                lastPath = getTree().getSelectionPath();
            else
                lastPath = null;
        }
        if(timer != null) {
            timer.stop();
        }
    }


    /**
     * @return
     */
    private JTree getTree() {
        return tree;
    }

    /**
     * Sets the tree currently editing for. This is needed to add
     * a selection listener.
     * @param newTree the new tree to be edited
     */
    private void setTree(JTree newTree) {
        if(getTree() != newTree) {
            // uninstall old listener, reset internal state
            if(getTree() != null)
                getTree().removeTreeSelectionListener(this);
            if(timer != null) {
                timer.stop();
            }
            lastRow = -1;
            lastPath = null;
            // install new
            tree = newTree;
            if(getTree() != null)
                getTree().addTreeSelectionListener(this);
        }
    }

    /**
     * @return
     */
    private int getLastRow() {
        return lastRow;
    }

//  --------------- implement Actionlistener (for Timer)

    /**
     * PENDING: remove from public api
     * Messaged when the timer fires, this will start the editing
     * session.
     */
    public void actionPerformed(ActionEvent e) {
        if(getTree() != null && lastPath != null) {
            getTree().startEditingAtPath(lastPath);
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
        if(getLastRow() != -1 && getTree() != null) {
            Rectangle bounds = getTree().getRowBounds(getLastRow());
            return provider.inHitRegion(bounds, x, y);
        }
        return true;
    }

//-------------------------- factory methods
    
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


    protected EditingProvider createEditingProvider() {
        return new EditingProvider();
    }

// -------------------------------- Serialization support.

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
    public static class DefaultTextField extends JTextField {
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

    }


}
