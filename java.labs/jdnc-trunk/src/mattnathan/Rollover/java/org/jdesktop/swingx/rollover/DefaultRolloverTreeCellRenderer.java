/*
 * $Id: DefaultRolloverTreeCellRenderer.java 2629 2008-08-06 08:27:49Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.rollover;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.Icon;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.UIResource;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellRenderer;

import org.jdesktop.swingx.color.ColorUtil;
import org.jdesktop.swingx.painter.ItemSelectionPainter;
import org.jdesktop.swingx.painter.Painter;
import org.jdesktop.swingx.RolloverModel;
import org.jdesktop.swingx.RolloverManager;

/**
 * Default TreeCellRenderer which adds support for rollover effects. The
 * rollover effects are provided by a collection of Painters which represent the
 * different rollover states.
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 * @version 2.0
 * @see org.jdesktop.swingx.RolloverManager
 */
public class DefaultRolloverTreeCellRenderer extends DefaultTreeCellRenderer {
    /**
     * Client property name for specifying that the focus should be painted.
     */
    public static final String PAINT_FOCUS_PROPERTY = "paintFocus";

    /**
     * Rollover renderer which resembles a Windows Vista tree.
     */
    public static final TreeCellRenderer VISTA_TREE_CELL_RENDERER = new DefaultRolloverTreeCellRenderer(
          null,
          ItemSelectionPainter.VISTA_SELECTION,
          ItemSelectionPainter.VISTA_SELECTION_NOFOCUS,
          ItemSelectionPainter.VISTA_PRESSED,
          ItemSelectionPainter.VISTA_ROLLOVER,
          ItemSelectionPainter.VISTA_SELECTED_ROLLOVER);

    private Painter<? super DefaultRolloverTreeCellRenderer> rolloverPainter;
    private Painter<? super DefaultRolloverTreeCellRenderer> selectedPainter;
    private Painter<? super DefaultRolloverTreeCellRenderer> selectedNoFocusPainter;
    private Painter<? super DefaultRolloverTreeCellRenderer> rolloverSelectedPainter;
    private Painter<? super DefaultRolloverTreeCellRenderer> pressedPainter;
    private Painter<? super DefaultRolloverTreeCellRenderer> noneSelectedPainter;

    private Painter<? super DefaultRolloverTreeCellRenderer> backgroundPainter;
    /**
     * Create a new DefaultRolloverTreeCellRenderer collecting the rollover
     * painters from similarly named UIResources prepended with {@code "Tree."}.
     * E.G. {@code "Tree.noneSelectedPainter"}.
     */
    @SuppressWarnings("unchecked") // cannot remove this warning via a cast
    public DefaultRolloverTreeCellRenderer() {
        this((Painter<? super DefaultRolloverTreeCellRenderer>) UIManager.get("Tree.noneSelectedPainter"),
             (Painter<? super DefaultRolloverTreeCellRenderer>) UIManager.get("Tree.selectedPainter"),
             (Painter<? super DefaultRolloverTreeCellRenderer>) UIManager.get("Tree.selectedNoFocusPainter"),
             (Painter<? super DefaultRolloverTreeCellRenderer>) UIManager.get("Tree.pressedPainter"),
             (Painter<? super DefaultRolloverTreeCellRenderer>) UIManager.get("Tree.rolloverPainter"),
             (Painter<? super DefaultRolloverTreeCellRenderer>) UIManager.get("Tree.rolloverSelectedPainter"));
    }





    /**
     * Creates a new DefaultRolloverTreeCellRenderer using the given painters for
     * rollover states.
     *
     * @param noneSelectedPainter the painter to use when not selected or
     *   rollover-ed.
     * @param selectedPainter The painter to use when selected.
     * @param selectedNoFocusPainter The painter to use when the tree is
     *   focusable and not in focus for selected rows.
     * @param pressedPainter The painter to use when pressed.
     * @param rolloverPainter The painter to use when rollover is true.
     * @param rolloverSelectedPainter The painter to use when selected and
     *   rollover is true
     */
    public DefaultRolloverTreeCellRenderer(Painter<? super DefaultRolloverTreeCellRenderer> noneSelectedPainter,
                                           Painter<? super DefaultRolloverTreeCellRenderer> selectedPainter,
                                           Painter<? super DefaultRolloverTreeCellRenderer> selectedNoFocusPainter,
                                           Painter<? super DefaultRolloverTreeCellRenderer> pressedPainter,
                                           Painter<? super DefaultRolloverTreeCellRenderer> rolloverPainter,
                                           Painter<? super DefaultRolloverTreeCellRenderer> rolloverSelectedPainter) {
        super();
        setOpaque(false);
        setNoneSelectedPainter(noneSelectedPainter);
        setSelectedPainter(selectedPainter);
        setSelectedNoFocusPainter(selectedNoFocusPainter);
        setPressedPainter(pressedPainter);
        setRolloverPainter(rolloverPainter);
        setRolloverSelectedPainter(rolloverSelectedPainter);
    }





    /**
     * Configures the renderer based on the passed in components. The value is
     * set from messaging the tree with <code>convertValueToText</code>, which
     * ultimately invokes <code>toString</code> on <code>value</code>. The
     * foreground color is set based on the selection and the icon is set based
     * on on leaf and expanded.
     *
     * @param tree JTree
     * @param value Object
     * @param sel boolean
     * @param expanded boolean
     * @param leaf boolean
     * @param row int
     * @param hasFocus boolean
     * @return Component
     */
    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        Boolean b = (Boolean) tree.getClientProperty(PAINT_FOCUS_PROPERTY);
        if (b != null) {
            if (!b) {
                hasFocus = false;
            }
        }

        RolloverModel m = RolloverManager.getRolloverModel(tree);
        if (m instanceof IndexedRolloverModel) {
            IndexedRolloverModel im = (IndexedRolloverModel) m;
            if (im.getRolloverIndex() == row) {
                if (!im.isSelected() && (im.isPressed() || im.isArmed())) {
                    setBackgroundPainter(getPressedPainter());
                } else if (sel) {
                    setBackgroundPainter(getRolloverSelectedPainter());
                } else {
                    setBackgroundPainter(getRolloverPainter());
                }
            } else {
                if (sel) {
                    if (!tree.isFocusable() || tree.isFocusOwner() || getSelectedNoFocusPainter() == null) {
                        setBackgroundPainter(getSelectedPainter());
                    } else {
                        setBackgroundPainter(getSelectedNoFocusPainter());
                    }
                } else {
                    setBackgroundPainter(getNoneSelectedPainter());
                }
            }
        }

        return super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
    }





    /**
     * Paints the value. The background is filled based on rollover status.
     *
     * @param g Graphics
     */
    @Override
    public void paint(Graphics g) {
        Painter<? super DefaultRolloverTreeCellRenderer> p = getBackgroundPainter();
        if (p != null) {
            int imageOffset = getLabelStart();
            Graphics2D g2 = (Graphics2D) g;
            if (getComponentOrientation().isLeftToRight()) {
                if (imageOffset != 0) {
                    g2 = (Graphics2D) g2.create();
                    g2.translate(imageOffset, 0);
                }
                p.paint(g2, this, getWidth() - imageOffset, getHeight());
                if (imageOffset != 0) {
                    g2.dispose();
                }
            } else {
                p.paint(g2, this, getWidth() - imageOffset, getHeight());
            }

        }
        super.paint(g);
    }





    /**
     * Copy of the parent classes getLabelStart() method.
     *
     * @return The start pixel for the label.
     */
    private int getLabelStart() {
        Icon currentI = getIcon();
        if (currentI != null && getText() != null) {
            return currentI.getIconWidth() + Math.max(0, getIconTextGap() - 1);
        }
        return 0;
    }





    /**
     * Sets the background color to be used for non selected nodes.
     *
     * @param newColor Color
     */
    @Override
    public void setBackgroundNonSelectionColor(Color newColor) {
        Color tcolor = ColorUtil.setAlpha(newColor, 0);
        super.setBackgroundNonSelectionColor(newColor instanceof UIResource ? new ColorUIResource(tcolor) : tcolor);
    }





    /**
     * Sets the color to use for the background if node is selected.
     *
     * @param newColor Color
     */
    @Override
    public void setBackgroundSelectionColor(Color newColor) {
        Color tcolor = ColorUtil.setAlpha(newColor, 0);
        super.setBackgroundSelectionColor(newColor instanceof UIResource ? new ColorUIResource(tcolor) : tcolor);

    }





    /**
     * Get the painter to use as the current background.
     *
     * @return The painter to paint as the background.
     * @see #setBackgroundPainter
     */
    public Painter<? super DefaultRolloverTreeCellRenderer> getBackgroundPainter() {
        return backgroundPainter;
    }





    /**
     * Get the painter to use when not selected, pressed or in rollover state.
     *
     * @return The default painter.
     * @see #setNoneSelectedPainter
     */
    public Painter<? super DefaultRolloverTreeCellRenderer> getNoneSelectedPainter() {
        return noneSelectedPainter;
    }





    /**
     * Get the painter used when the rollover index is pressed.
     *
     * @return The pressed painter.
     * @see #setPressedPainter
     */
    public Painter<? super DefaultRolloverTreeCellRenderer> getPressedPainter() {
        return pressedPainter;
    }





    /**
     * Get the painter used for painting the rollover indexes background.
     *
     * @return The rollover painter.
     * @see #setRolloverPainter
     */
    public Painter<? super DefaultRolloverTreeCellRenderer> getRolloverPainter() {
        return rolloverPainter;
    }





    /**
     * The painter to use when the rollover index is selected.
     *
     * @return The rollover selected painter.
     * @see #setRolloverSelectedPainter
     */
    public Painter<? super DefaultRolloverTreeCellRenderer> getRolloverSelectedPainter() {
        return rolloverSelectedPainter;
    }





    /**
     * Get the painter to use when a row is selected.
     *
     * @return The selected painter.
     * @see #setSelectedPainter
     */
    public Painter<? super DefaultRolloverTreeCellRenderer> getSelectedPainter() {
        return selectedPainter;
    }





    /**
     * Get the painter to use when a row is selected and the tree is focusable
     * but not focused.
     *
     * @return The no-focus selected painter.
     * @see #setSelectedNoFocusPainter
     */
    public Painter<? super DefaultRolloverTreeCellRenderer> getSelectedNoFocusPainter() {
        return selectedNoFocusPainter;
    }





    /**
     * Set the painter used to paint the background. This property is usually set
     * from within the getTreeCellRendererComponent method with one of the state
     * specific painters.
     *
     * @param backgroundPainter The painter used to paint the background.
     * @see #getBackgroundPainter
     */
    public void setBackgroundPainter(Painter<? super DefaultRolloverTreeCellRenderer> backgroundPainter) {
        this.backgroundPainter = backgroundPainter;
    }





    /**
     * Sets the painter to use when the row is not selected, pressed or in the
     * rollover state.
     *
     * @param noneSelectedPainter The default painter.
     * @see #getNoneSelectedPainter
     */
    public void setNoneSelectedPainter(Painter<? super DefaultRolloverTreeCellRenderer> noneSelectedPainter) {
        this.noneSelectedPainter = noneSelectedPainter;
    }





    /**
     * Set the painter to use when the rollover row is pressed.
     *
     * @param pressedPainter The pressed painter.
     * @see #getPressedPainter
     */
    public void setPressedPainter(Painter<? super DefaultRolloverTreeCellRenderer> pressedPainter) {
        this.pressedPainter = pressedPainter;
    }





    /**
     * Set the painter to use when the row has the mouse over it.
     *
     * @param rolloverPainter The rollover painter.
     * @see #getRolloverPainter
     */
    public void setRolloverPainter(Painter<? super DefaultRolloverTreeCellRenderer> rolloverPainter) {
        this.rolloverPainter = rolloverPainter;
    }





    /**
     * Set the painter to use when the rollover row is selected.
     *
     * @param rolloverSelectedPainter The rollover selected painter.
     * @see #getRolloverSelectedPainter
     */
    public void setRolloverSelectedPainter(Painter<? super DefaultRolloverTreeCellRenderer> rolloverSelectedPainter) {
        this.rolloverSelectedPainter = rolloverSelectedPainter;
    }





    /**
     * Set the painter to use when a row is selected.
     *
     * @param selectedPainter The selected painter.
     * @see #getSelectedPainter
     */
    public void setSelectedPainter(Painter<? super DefaultRolloverTreeCellRenderer> selectedPainter) {
        this.selectedPainter = selectedPainter;
    }





    /**
     * Set the painter to use when a row is selected and the tree is focusable
     * but not currently focused. If null the the selectionPainter will be used
     * instead.
     *
     * @param selectedNoFocusPainter The no-focus selection painter.
     * @see #getSelectedNoFocusPainter
     * @see #setSelectedPainter
     */
    public void setSelectedNoFocusPainter(Painter<? super DefaultRolloverTreeCellRenderer> selectedNoFocusPainter) {
        this.selectedNoFocusPainter = selectedNoFocusPainter;
    }

}
