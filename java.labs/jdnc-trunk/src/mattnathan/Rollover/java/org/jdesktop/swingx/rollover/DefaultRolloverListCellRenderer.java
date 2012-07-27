/*
 * $Id: DefaultRolloverListCellRenderer.java 2629 2008-08-06 08:27:49Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.rollover;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.UIManager;

import org.jdesktop.swingx.painter.ItemSelectionPainter;
import org.jdesktop.swingx.painter.Painter;
import org.jdesktop.swingx.RolloverModel;
import org.jdesktop.swingx.RolloverManager;

/**
 * Default ListCellRenderer which adds support for rollover effects. The rollover effects are provided by a collection
 * of Painters which represent the different rollover states.
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 * @version 2.0
 * @see org.jdesktop.swingx.RolloverManager
 */
public class DefaultRolloverListCellRenderer extends DefaultListCellRenderer {
    /**
     * Client property name for specifying that the focus should be painted.
     */
    public static final String PAINT_FOCUS_PROPERTY = "paintFocus";

    /**
     * Rollover renderer which resembles a Windows Vista list.
     */
    public static final ListCellRenderer VISTA_LIST_CELL_RENDERER = new DefaultRolloverListCellRenderer(
          null,
          ItemSelectionPainter.VISTA_SELECTION,
          ItemSelectionPainter.VISTA_SELECTION_NOFOCUS,
          ItemSelectionPainter.VISTA_PRESSED,
          ItemSelectionPainter.VISTA_ROLLOVER,
          ItemSelectionPainter.VISTA_SELECTED_ROLLOVER);

    private Painter<? super DefaultListCellRenderer> rolloverPainter;
    private Painter<? super DefaultListCellRenderer> selectedPainter;
    private Painter<? super DefaultListCellRenderer> selectedNoFocusPainter;
    private Painter<? super DefaultListCellRenderer> rolloverSelectedPainter;
    private Painter<? super DefaultListCellRenderer> pressedPainter;
    private Painter<? super DefaultListCellRenderer> noneSelectedPainter;

    private Painter<? super DefaultListCellRenderer> backgroundPainter;

    /**
     * Create a new DefaultRolloverListCellRenderer collecting the rollover painters from similarly named UIResources
     * prepended with {@code "List."}. E.G. {@code "List.noneSelectedPainter"}.
     */
    @SuppressWarnings("unchecked")
    // cannot remove this warning via a cast
    public DefaultRolloverListCellRenderer() {
        this((Painter<? super DefaultListCellRenderer>) UIManager.get("List.noneSelectedPainter"),
             (Painter<? super DefaultListCellRenderer>) UIManager.get("List.selectedPainter"),
             (Painter<? super DefaultListCellRenderer>) UIManager.get("List.selectedNoFocusPainter"),
             (Painter<? super DefaultListCellRenderer>) UIManager.get("List.pressedPainter"),
             (Painter<? super DefaultListCellRenderer>) UIManager.get("List.rolloverPainter"),
             (Painter<? super DefaultListCellRenderer>) UIManager.get("List.rolloverSelectedPainter"));
    }





    /**
     * Creates a new DefaultRolloverListCellRenderer using the given painters for rollover states.
     *
     * @param noneSelectedPainter the painter to use when not selected or rollover-ed.
     * @param selectedPainter The painter to use when selected.
     * @param selectedNoFocusPainter the painter used when an item is selected and the list is not focused.
     * @param pressedPainter The painter to use when pressed.
     * @param rolloverPainter The painter to use when rollover is true.
     * @param rolloverSelectedPainter The painter to use when selected and rollover is true
     */
    public DefaultRolloverListCellRenderer(Painter<? super DefaultListCellRenderer> noneSelectedPainter,
                                           Painter<? super DefaultListCellRenderer> selectedPainter,
                                           Painter<? super DefaultListCellRenderer> selectedNoFocusPainter,
                                           Painter<? super DefaultListCellRenderer> pressedPainter,
                                           Painter<? super DefaultListCellRenderer> rolloverPainter,
                                           Painter<? super DefaultListCellRenderer> rolloverSelectedPainter) {
        setOpaque(false);
        setNoneSelectedPainter(noneSelectedPainter);
        setSelectedPainter(selectedPainter);
        setSelectedNoFocusPainter(selectedNoFocusPainter);
        setPressedPainter(pressedPainter);
        setRolloverPainter(rolloverPainter);
        setRolloverSelectedPainter(rolloverSelectedPainter);
    }





    /**
     * Get the painter to use as the current background.
     *
     * @return The painter to paint as the background.
     * @see #setBackgroundPainter
     */
    public Painter<? super DefaultListCellRenderer> getBackgroundPainter() {
        return backgroundPainter;
    }





    /**
     * Get the painter to use when not selected, pressed or in rollover state.
     *
     * @return The default painter.
     * @see #setNoneSelectedPainter
     */
    public Painter<? super DefaultListCellRenderer> getNoneSelectedPainter() {
        return noneSelectedPainter;
    }





    /**
     * Get the painter used when the rollover index is pressed.
     *
     * @return The pressed painter.
     * @see #setPressedPainter
     */
    public Painter<? super DefaultListCellRenderer> getPressedPainter() {
        return pressedPainter;
    }





    /**
     * Get the painter used for painting the rollover indexes background.
     *
     * @return The rollover painter.
     * @see #setRolloverPainter
     */
    public Painter<? super DefaultListCellRenderer> getRolloverPainter() {
        return rolloverPainter;
    }





    /**
     * The painter to use when the rollover index is selected.
     *
     * @return The rollover selected painter.
     * @see #setRolloverSelectedPainter
     */
    public Painter<? super DefaultListCellRenderer> getRolloverSelectedPainter() {
        return rolloverSelectedPainter;
    }





    /**
     * Get the painter to use when a row is selected.
     *
     * @return The selected painter.
     * @see #setSelectedPainter
     */
    public Painter<? super DefaultListCellRenderer> getSelectedPainter() {
        return selectedPainter;
    }





    /**
     * Get the painter to use when an item is selected and the list is focusable but not focused.
     *
     * @return The no focus selection painter
     * @see #setSelectedNoFocusPainter
     */
    public Painter<? super DefaultListCellRenderer> getSelectedNoFocusPainter() {
        return selectedNoFocusPainter;
    }





    /**
     * Set the painter used to paint the background. This property is usually set from within the
     * getTreeCellRendererComponent method with one of the state specific painters.
     *
     * @param backgroundPainter The painter used to paint the background.
     * @see #getBackgroundPainter
     */
    public void setBackgroundPainter(Painter<? super DefaultListCellRenderer> backgroundPainter) {
        this.backgroundPainter = backgroundPainter;
    }





    /**
     * Sets the painter to use when the row is not selected, pressed or in the rollover state.
     *
     * @param noneSelectedPainter The default painter.
     * @see #getNoneSelectedPainter
     */
    public void setNoneSelectedPainter(Painter<? super DefaultListCellRenderer> noneSelectedPainter) {
        this.noneSelectedPainter = noneSelectedPainter;
    }





    /**
     * Set the painter to use when the rollover row is pressed.
     *
     * @param pressedPainter The pressed painter.
     * @see #getPressedPainter
     */
    public void setPressedPainter(Painter<? super DefaultListCellRenderer> pressedPainter) {
        this.pressedPainter = pressedPainter;
    }





    /**
     * Set the painter to use when the row has the mouse over it.
     *
     * @param rolloverPainter The rollover painter.
     * @see #getRolloverPainter
     */
    public void setRolloverPainter(Painter<? super DefaultListCellRenderer> rolloverPainter) {
        this.rolloverPainter = rolloverPainter;
    }





    /**
     * Set the painter to use when the rollover row is selected.
     *
     * @param rolloverSelectedPainter The rollover selected painter.
     * @see #getRolloverSelectedPainter
     */
    public void setRolloverSelectedPainter(Painter<? super DefaultListCellRenderer> rolloverSelectedPainter) {
        this.rolloverSelectedPainter = rolloverSelectedPainter;
    }





    /**
     * Set the painter to use when a row is selected.
     *
     * @param selectedPainter The selected painter.
     * @see #getSelectedPainter
     */
    public void setSelectedPainter(Painter<? super DefaultListCellRenderer> selectedPainter) {
        this.selectedPainter = selectedPainter;
    }





    /**
     * Set the painter to use when an item is selected and the list is focusable but not currently focused. If null then
     * the standard selection painter will be used.
     *
     * @param selectedNoFocusPainter The no-focus selection painter.
     * @see #getSelectedNoFocusPainter
     * @see #setSelectedPainter
     */
    public void setSelectedNoFocusPainter(Painter<? super DefaultListCellRenderer> selectedNoFocusPainter) {
        this.selectedNoFocusPainter = selectedNoFocusPainter;
    }





    /**
     * Configures the renderer based on the passed in components. The value is set from messaging the tree with
     * <code>convertValueToText</code>, which ultimately invokes <code>toString</code> on <code>value</code>. The
     * foreground color is set based on the selection and the icon is set based on on leaf and expanded.
     *
     * @param list The source list.
     * @param value The current value.
     * @param index The current index
     * @param isSelected {@code true} if selected.
     * @param cellHasFocus {@code true} if the cell has focus
     * @return The component used for rendering.
     */
    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        Boolean b = (Boolean) list.getClientProperty(PAINT_FOCUS_PROPERTY);
        if (b != null) {
            if (!b) {
                cellHasFocus = false;
            }
        }

        RolloverModel m = RolloverManager.getRolloverModel(list);
        if (m instanceof IndexedRolloverModel) {
            IndexedRolloverModel im = (IndexedRolloverModel) m;
            if (im.getRolloverIndex() == index) {
                if (!im.isSelected() && (im.isPressed() || im.isArmed())) {
                    setBackgroundPainter(getPressedPainter());
                } else if (isSelected) {
                    setBackgroundPainter(getRolloverSelectedPainter());
                } else {
                    setBackgroundPainter(getRolloverPainter());
                }
            } else {
                if (isSelected) {
                    if (!list.isFocusable() || list.isFocusOwner() || getSelectedNoFocusPainter() == null) {
                        setBackgroundPainter(getSelectedPainter());
                    } else {
                        setBackgroundPainter(getSelectedNoFocusPainter());
                    }
                } else {
                    setBackgroundPainter(getNoneSelectedPainter());
                }
            }
        }

        return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
    }





    /**
     * Calls the UI delegate's paint method, if the UI delegate is non-<code>null</code>.  We pass the delegate a copy of
     * the <code>Graphics</code> object to protect the rest of the paint code from irrevocable changes (for example,
     * <code>Graphics.translate</code>).
     * <p/>
     * If you override this in a subclass you should not make permanent changes to the passed in <code>Graphics</code>.
     * For example, you should not alter the clip <code>Rectangle</code> or modify the transform. If you need to do these
     * operations you may find it easier to create a new <code>Graphics</code> from the passed in <code>Graphics</code>
     * and manipulate it. Further, if you do not invoker super's implementation you must honour the opaque property, that
     * is if this component is opaque, you must completely fill in the background in a non-opaque color. If you do not
     * honour the opaque property you will likely see visual artifacts.
     * <p/>
     * The passed in <code>Graphics</code> object might have a transform other than the identify transform installed on
     * it.  In this case, you might get unexpected results if you cumulatively apply another transform.
     *
     * @param g the <code>Graphics</code> object to protect
     * @see #paint
     */
    @Override
    protected void paintComponent(Graphics g) {
        Painter<? super DefaultListCellRenderer> p = getBackgroundPainter();
        if (p != null) {
            p.paint((Graphics2D) g, this, getWidth(), getHeight());
        }
        super.paintComponent(g);
    }

}
