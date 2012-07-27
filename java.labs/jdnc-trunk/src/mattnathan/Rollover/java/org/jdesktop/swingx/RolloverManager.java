/*
 * $Id: RolloverManager.java 2628 2008-08-05 16:05:09Z mattnathan $
 *
 * Copyright 2005 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */

package org.jdesktop.swingx;

import org.jdesktop.swingx.rollover.DefaultIndexedRolloverModel;
import org.jdesktop.swingx.rollover.DefaultRolloverModel;
import org.jdesktop.swingx.rollover.IndexedRolloverModel;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Provides a mechanism for simplifying rollover effect support. Via the install
 * method, this class provides a generic support for adding similar effects to
 * those supported by AbstractButton and ButtonModel to a generic JComponent.
 * Additionally this provides limited rollover support for indexed components.
 * This includes JList, JTree and JTable and is supported via the
 * IndexedRolloverModel interface which is returned when install is called on a
 * component where isIndexRolloverSupported return true.
 * <p>
 * Utility methods for supporting common repaint conditions and property changes
 * based on rollover state changes are also provided.
 * <p>
 * <b>Example:</b>
 * To install rollover support on a JList do the following:
 * <blockquote>
 * <pre><code>
 * JList list = new JList(new Object[] {"Item 1", "Item 2", "Item 3"});
 * RolloverManager.install(list, true);
 * list.setCellRenderer(DefaultRolloverListCellRenderer.VISTA_LIST_CELL_RENDERER);
 * </code></pre>
 * </blockquote>
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 * @version 2.0
 * @see org.jdesktop.swingx.rollover.DefaultRolloverListCellRenderer
 * @see org.jdesktop.swingx.rollover.DefaultRolloverTreeCellRenderer
 * @todo Support JTables.
 */
public class RolloverManager {

    private static final String ROLLOVER_MODEL_CLIENT_PROPERTY = "rolloverModel";

    private static final Adapter ADAPTER = new Adapter();

    private static boolean printErrors = true;

    private RolloverManager() {
    }

    /**
     * Install support for rollover events onto the given component. If
     * isIndexRolloverSupported(target) returns true then an IndexedRolloverModel
     * is returned. If the rollover support has already been installed on this
     * component then an error is printed to the standard error output and the
     * existing RolloverModel is returned. To disable the error message set the
     * printErrors property to false.
     * <p>
     * This method delegates to {@code install(target, false)}.
     *
     * @param target The target for the rollover effects.
     * @return The model created to store rollover state. This may be an instance
     *   of IndexedRolloverModel.
     */
    public static RolloverModel install(JComponent target) {
        return install(target, false);
    }





    /**
     * Install support for rollover events onto the given component. If
     * isIndexRolloverSupported(target) returns true then an IndexedRolloverModel
     * is returned. If the rollover support has already been installed on this
     * component then an error is printed to the standard error output and the
     * existing RolloverModel is returned. To disable the error message set the
     * printErrors property to false;
     *
     * @param target The target for the rollover effects.
     * @param addRepaintSupport {@code true} to allow the RolloverManager to
     *   install listeners on the RolloverModel to manager common repaint state
     *   changes.
     * @return The model created to store rollover state. This may be an instance
     *   of IndexedRolloverModel
     */
    public static RolloverModel install(JComponent target, boolean addRepaintSupport) {
        if (target == null) {
            throw new NullPointerException("target cannot be null");
        }
        RolloverModel model = (RolloverModel) target.getClientProperty(ROLLOVER_MODEL_CLIENT_PROPERTY);
        if (model != null) {
            if (printErrors) {
                //noinspection UseOfSystemOutOrSystemErr
                System.err.println("Warning: a rollover model for " + target + " has already been installed");
            }
            return model;
        }
        if (isIndexRolloverSupported(target)) {
            model = new DefaultIndexedRolloverModel();
            target.addFocusListener(ADAPTER);
        } else {
            model = new DefaultRolloverModel();
        }
        target.putClientProperty(ROLLOVER_MODEL_CLIENT_PROPERTY, model);
        target.addMouseListener(ADAPTER);
        target.addMouseMotionListener(ADAPTER);
        if (addRepaintSupport) {
            installRepaintListeners(target, model);
        }
        return model;
    }





    /**
     * Installs the listeners responsible for repainting the target component
     * when rollover state changes.
     *
     * @param target JComponent
     * @param model RolloverModel
     */
    private static void installRepaintListeners(JComponent target, RolloverModel model) {
        if (target instanceof JList) {
            ListRolloverRepaintSupport rs = new ListRolloverRepaintSupport((JList) target);
            model.addChangeListener(rs);
            ((IndexedRolloverModel) model).addPropertyChangeListener(rs);
        } else if (target instanceof JTree) {
            TreeRolloverRepaintSupport rs = new TreeRolloverRepaintSupport((JTree) target);
            model.addChangeListener(rs);
            ((IndexedRolloverModel) model).addPropertyChangeListener(rs);
        } else {
            model.addChangeListener(new ComponentRolloverRepaintSupport(target));
        }
    }





    /**
     * Uninstalls rollover from the given component. If no rollover support was
     * installed on the target component then this method returns silently. You
     * should ensure that you always call this after rollover support has become
     * unused (i.e. when a renderer is changed) as performance in both memory and
     * speed can be affected.
     *
     * @param target The component to uninstall rollover support from.
     * @return The model that support was uninstalled from. Use this instance to
     *   remove any listeners on that model that were added.
     */
    public static RolloverModel uninstall(JComponent target) {
        RolloverModel model = null;
        if (target != null) {
            model = (RolloverModel) target.getClientProperty(ROLLOVER_MODEL_CLIENT_PROPERTY);
            if (model != null) {
                target.putClientProperty(ROLLOVER_MODEL_CLIENT_PROPERTY, null);
                target.removeFocusListener(ADAPTER);
                target.removeMouseListener(ADAPTER);
                target.removeMouseMotionListener(ADAPTER);
            }
        }
        return model;
    }





    /**
     * Set to false to avoid printing errors when installation is performed twice
     * on a component. The default value is true.
     *
     * @param printErrors {@code false} to disable debug error messages.
     */
    public static void printErrors(boolean printErrors) {
        RolloverManager.printErrors = printErrors;
    }





    /**
     * Returns true if indexed rollover models are supported on the given
     * component. This will return true only if the given component is a JList or
     * JTree. Tables are not currently supported.
     *
     * @param c The component to support rollover effects.
     * @return {@code true} if c is a JList or JTree.
     */
    public static boolean isIndexRolloverSupported(JComponent c) {
        return c instanceof JList || c instanceof JTree;
    }





    /**
     * Gets the RolloverModel associated with the given component. This method
     * will return null if there is no model installed.
     *
     * @param target The controller for the rollover model.
     * @return The model responsible for maintaining rollover state.
     */
    public static RolloverModel getRolloverModel(JComponent target) {
        return (RolloverModel) target.getClientProperty(ROLLOVER_MODEL_CLIENT_PROPERTY);
    }





    /**
     * Provides listener support for all installed component. There is only one
     * instance of this class created. This class is a modified copy of the
     * {@link javax.swing.plaf.basic.BasicButtonListener} class adding support
     * for indexed rollover events.
     *
     * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
     * @version 2.0
     */
    private static class Adapter implements MouseListener, MouseMotionListener, FocusListener {

        private long lastPressedTimestamp = -1;
        private boolean shouldDiscardRelease = false;

        public void mouseMoved(MouseEvent e) {
            JComponent c = (JComponent) e.getSource();
            RolloverModel m = getRolloverModel(c);
            if (m instanceof IndexedRolloverModel) {
                IndexedRolloverModel im = (IndexedRolloverModel) m;
                if (c instanceof JList) {
                    JList l = (JList) c;
                    Point p = e.getPoint();

                    int index = l.locationToIndex(p);
                    Rectangle bounds = l.getCellBounds(index, index);
                    if (bounds != null && bounds.contains(p)) {
                        im.setRolloverIndex(index);
                    } else {
                        im.setRolloverIndex( -1);
                    }

                } else if (c instanceof JTree) {
                    JTree t = (JTree) c;
                    Point p = e.getPoint();

                    int row = t.getClosestRowForLocation(p.x, p.y);
                    Rectangle bounds = t.getRowBounds(row);
                    if (bounds.contains(p)) {
                        im.setRolloverIndex(row);
                    } else {
                        im.setRolloverIndex( -1);
                    }
                }
            }
        }





        public void mouseDragged(MouseEvent e) {
            mouseMoved(e);
        }





        public void mouseClicked(MouseEvent e) {
        }





        public void mousePressed(MouseEvent e) {
            if (SwingUtilities.isLeftMouseButton(e)) {
                JComponent c = (JComponent) e.getSource();

                if (c.contains(e.getX(), e.getY())) {
                    long multiClickThreshhold = 0;
                    long lastTime = lastPressedTimestamp;
                    long currentTime = lastPressedTimestamp = e.getWhen();
                    if (lastTime != -1 && currentTime - lastTime < multiClickThreshhold) {
                        shouldDiscardRelease = true;
                        return;
                    }

                    RolloverModel model = getRolloverModel(c);
                    if (!model.isEnabled()) {
                        // Disabled buttons ignore all input...
                        return;
                    }
                    if (!model.isArmed()) {
                        // button not armed, should be
                        model.setArmed(true);
                    }
                    model.setPressed(true);
                    if (!c.hasFocus() && c.isRequestFocusEnabled()) {
                        c.requestFocus();
                    }
                }
            }
        }





        public void mouseReleased(MouseEvent e) {
            if (SwingUtilities.isLeftMouseButton(e)) {
                // Support for multiClickThreshhold
                if (shouldDiscardRelease) {
                    shouldDiscardRelease = false;
                    return;
                }
                JComponent c = (JComponent) e.getSource();
                RolloverModel model = getRolloverModel(c);
                model.setPressed(false);
                model.setArmed(false);
            }
        }





        public void mouseEntered(MouseEvent e) {
            JComponent c = (JComponent) e.getSource();
            RolloverModel model = getRolloverModel(c);
            if (!SwingUtilities.isLeftMouseButton(e)) {
                model.setRollover(true);
                if (model instanceof IndexedRolloverModel) {
                    mouseMoved(e);
                }

            }
            if (model.isPressed()) {
                model.setArmed(true);
            }
        }





        public void mouseExited(MouseEvent e) {
            JComponent c = (JComponent) e.getSource();
            RolloverModel model = getRolloverModel(c);
            model.setRollover(false);
            model.setArmed(false);
            if (model instanceof IndexedRolloverModel) {
                ((IndexedRolloverModel) model).setRolloverIndex( -1);
            }
        }





        public void focusGained(FocusEvent e) {
            ((JComponent) e.getSource()).repaint();
        }





        public void focusLost(FocusEvent e) {
            ((JComponent) e.getSource()).repaint();
        }

    }







    /**
     * Default repaint behaviour for rollover state changes. This listener will
     * repaint the whole source component when any state change occurs.
     *
     * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
     * @version 2.0
     */
    public static class ComponentRolloverRepaintSupport implements ChangeListener {
        private JComponent target;
        public ComponentRolloverRepaintSupport(JComponent target) {
            this.target = target;
        }





        public void stateChanged(ChangeEvent e) {
            target.repaint();
        }
    }







    /**
     * Provides common support for indexed rollover components.
     *
     * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
     * @version 2.0
     */
    private abstract static class AbstractIndexedRolloverRepaintSupport<T extends JComponent> implements ChangeListener, PropertyChangeListener {
        protected T source;

        protected AbstractIndexedRolloverRepaintSupport(T source) {
            this.source = source;
        }





        public T getSource() {
            return source;
        }





        public void propertyChange(PropertyChangeEvent e) {
            String p = e.getPropertyName();
            if (p == IndexedRolloverModel.ROLLOVER_INDEX_PROPERTY) {
                int oldValue = (Integer) e.getOldValue();
                int newValue = (Integer) e.getNewValue();
                if (oldValue != -1 || newValue != -1) {
                    repaintIndex(source, oldValue == -1 ? newValue : oldValue, newValue == -1 ? oldValue : newValue);
                }
            }
        }





        public void stateChanged(ChangeEvent e) {
            IndexedRolloverModel m = (IndexedRolloverModel) getRolloverModel(source);
            int index = m.getRolloverIndex();
            if (index != -1) {
                repaintIndex(source, index, index);
            }
        }





        /**
         * Called when a repaint of the given indices has changed. This is called
         * when both property changes and state changes are made. It is guaranteed
         * that both indices will be valid ( > 0) indices but they may be the
         * same. No guarantee is given for the size order of the indices.
         *
         * @param source The source component.
         * @param index1 The original rollover index.
         * @param index2 The new rollover index.
         */
        public abstract void repaintIndex(T source, int index1, int index2);
    }







    /**
     * Provides common-case repaint listeners for JLists. This is the default
     * listener added to a RepaintModel when install is called with a {@code
     * true} argument.
     *
     * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
     * @version 2.0
     */
    public static class ListRolloverRepaintSupport extends AbstractIndexedRolloverRepaintSupport<JList> {
        public ListRolloverRepaintSupport(JList list) {
            super(list);
        }





        @Override
        public void repaintIndex(JList source, int index1, int index2) {
            source.repaint(source.getCellBounds(index1, index2));
        }
    }







    /**
     * Default rollover repaint support for JTrees. This is the default
     * listener added to a RepaintModel when install is called with a {@code
     * true} argument.
     *
     * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
     * @version 2.0
     */
    public static class TreeRolloverRepaintSupport extends AbstractIndexedRolloverRepaintSupport<JTree> {
        public TreeRolloverRepaintSupport(JTree tree) {
            super(tree);
        }





        @Override
        public void repaintIndex(JTree source, int index1, int index2) {
            Rectangle bounds = source.getRowBounds(index1);
            if (index1 != index2) {
                bounds = bounds.union(source.getRowBounds(index2));
            }
            source.repaint(bounds);
        }
    }
}
