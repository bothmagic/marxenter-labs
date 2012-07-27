/*
 * Created on 18.06.2008
 *
 */
package netbeans.xoutline;

import java.awt.AWTError;
import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.LayoutManager2;
import java.awt.Rectangle;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.io.PrintStream;
import java.io.Serializable;
import java.util.logging.Logger;

import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SizeRequirements;
import javax.swing.SwingUtilities;
import javax.swing.Box.Filler;
import javax.swing.border.Border;

import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.painter.Painter;
import org.jdesktop.swingx.renderer.JRendererLabel;
import org.jdesktop.swingx.renderer.PainterAware;

/**
 * Quick shot for a panel specialized on painting the tree cell in a TreeTable.
 * It manages the delegate and icon directly (not via a WrappingIconPanel!),
 * adjusts the leading/trailing (dependent on
 * ComponentOrientation) indent with the help of a Filler component and paints
 * a handle on the Filler as appropriate. Has a custom (c&p from core) 
 * BoxLayout which doesn't skip invisible components. <p>
 * 
 * Supports routing of key strokes as used in JTable for non-focused editing
 * mode. <p>
 * 
 * NOTE: the difference between the BasicTreeTablePanelXX are
 * - this handles the children directly (aka duplicates code of 
 * WrappingIconPanel) and uses a modified BoxLayout
 * - XX = old: delegates to a WrappingIconPanel and uses a core BoxLayout
 * - XX = reverted: same as this except for using a core BoxLayout.
 * 
 *  
 * 
 * PENDING JW: This should be handled in the BasicTreeTableUI, maybe. <p> 
 * PENDING JW: Could fold into WrappingIconPanel? The filler width can be 0.
 * Doing so would allow to re-use for plain tree editing as well as for 
 * treetable editing. Plus, we'd need only one provider for all.<p>
 * 
 * 
 */
public class BasicTreeTablePanel extends JXPanel implements PainterAware {
    
    @SuppressWarnings("unused")
    private static final Logger LOG = Logger
            .getLogger(BasicTreeTablePanel.class.getName());
    protected JComponent delegate;
    private JRendererLabel iconLabel;
    private int labelPosition = 2;
    int iconLabelGap;
    private Border ltorBorder;
    private Border rtolBorder;
    private boolean dropHackEnabled;

    private Filler filler;
    private int indent = 20;
    private Icon handle;
    private int visualDepth;

    public BasicTreeTablePanel() {
        setLayout(new BoxLayout(this));
        filler = (Filler) Box.createHorizontalStrut(indent);
        add(filler);
        iconLabel = new JRendererLabel();
        iconLabelGap = iconLabel.getIconTextGap();
        iconLabel.setOpaque(false);
        add(iconLabel);
        installFocusListener();
        dropHackEnabled = false;
    }


    /**
     * Install a focusListener to transfer the focus down to 
     * the delegate. Happens if the wrappingPanel houses an editing
     * component. <p>
     * 
     * PENDING JW: Crazy to listen to itself .. but works. Revisit.
     */
    private void installFocusListener() {
        FocusListener focusListener = new FocusAdapter() {

            @Override
            public void focusGained(FocusEvent e) {
                if (delegate != null) {
                    delegate.requestFocus();
                }
                
            }
            
        };
        addFocusListener(focusListener);
    }


    @Override
    public void setComponentOrientation(ComponentOrientation o) {
        super.setComponentOrientation(o);
        updateIconBorder();
    }


    private void updateIconBorder() {
        if (ltorBorder == null) {
            ltorBorder = BorderFactory.createEmptyBorder(0, 0, 0, iconLabelGap);
            rtolBorder = BorderFactory.createEmptyBorder(0, iconLabelGap, 0, 0);
        } 
        if (getComponentOrientation().isLeftToRight()) {
            iconLabel.setBorder(ltorBorder);
        } else {
            iconLabel.setBorder(rtolBorder);
        }
        invalidate();
    }

    /**
     * Sets the icon.
     * 
     * @param icon the icon to use.
     */
    public void setIcon(Icon icon) {
        iconLabel.setIcon(icon);
        iconLabel.setText(null);
    }
 
    /**
     * Returns the icon used in this panel, may be null.
     * 
     * @return the icon used in this panel, may be null.
     */
    public Icon getIcon() {
        return iconLabel.getIcon();
    }


    /**
     * Sets the delegate component. 
     * 
     * @param comp the component to add as delegate.
     */
    public void setComponent(JComponent comp) {
        if (delegate != null) {
            remove(delegate);
        }
        delegate = comp;
        add(delegate, labelPosition);
        invalidate();
    }

    public JComponent getComponent() {
        return delegate;
    }

    /**
     * {@inheritDoc} <p>
     * 
     * Overridden to set the background of the delegate and icon label as well.
     */
    @Override
    public void setBackground(Color bg) {
        super.setBackground(bg);
        if (iconLabel != null) {
            iconLabel.setBackground(bg);
        }
        if (delegate != null) {
            delegate.setBackground(bg);
        }
    }

    /**
     * {@inheritDoc} <p>
     * 
     * Overridden to set the foreground of the delegate and icon label as well.
     */
    @Override
    public void setForeground(Color bg) {
        super.setForeground(bg);
        if (iconLabel != null) {
            iconLabel.setForeground(bg);
        }
        if (delegate != null) {
            delegate.setForeground(bg);
        }
    }


    
    
    /**
     * {@inheritDoc} <p>
     * 
     * Overridden to set the Font of the delegate as well.
     */
    @Override
    public void setFont(Font font) {
        if (delegate != null) {
            delegate.setFont(font);
        }
        super.setFont(font);
    }

    
    /**
     * {@inheritDoc}
     * <p>
     * 
     * Overridden to hack around #766-swingx: cursor flickering in DnD when
     * dragging over tree column. This is a core bug (#6700748) related to
     * painting the rendering component on a CellRendererPane. A trick around is
     * to let this return false.
     * <p>
     * 
     * Some LayoutManagers don't layout an invisible component, so need to make
     * the hack-enabled configurable. This implementation will return false 
     * if isDropHackEnabled, super.isVisible otherwise.
     */
    @Override
    public boolean isVisible() {
        return dropHackEnabled ? false : super.isVisible();
    }


    /**
     * {@inheritDoc}
     * <p>
     * 
     * Returns the delegate's Painter if it is of type PainterAware or null
     * otherwise.
     * 
     * @return the delegate's Painter or null.
     */
    public Painter<?> getPainter() {
        if (delegate instanceof PainterAware) {
            return ((PainterAware) delegate).getPainter();
        }
        return null;
    }


    /**
     * Sets the delegate's Painter if it is of type PainterAware. Does nothing otherwise.
     * 
     * @param painter the Painter to apply to the delegate.
     */
    public void setPainter(Painter painter) {
        if (delegate instanceof PainterAware) {
            ((PainterAware) delegate).setPainter(painter);
        }
        
    }
    
    /**
     * 
     * Returns the bounds of the delegate component or null if the delegate is null.
     * 
     * PENDING JW: where do we use it? Maybe it was for testing only?
     * 
     * @return the bounds of the delegate, or null if the delegate is null.
     */
    public Rectangle getDelegateBounds() {
        if (delegate == null) return null;
        return delegate.getBounds();
    }


    /**
     * Sets the dropHackEnabled property. <p>
     * 
     * The default value is true.
     * 
     * @param dropHackEnabled 
     * 
     * @see #isVisible()
     */
    public void setDropHackEnabled(boolean dropHackEnabled) {
        this.dropHackEnabled = dropHackEnabled;
    }

    public void setIndent(int visualDepth) {
        this.visualDepth = visualDepth;
        Dimension min = filler.getMinimumSize();
        min.width = visualDepth * indent;
        Dimension pref = filler.getPreferredSize();
        pref.width = min.width;
        Dimension max = filler.getMaximumSize();
        max.width = min.width;
        filler.changeShape(min, pref, max);
        // don't need to revalidate here, changeShape already does
    }

    public void setCellBorder(Border border) {
        if(delegate != null) {
            delegate.setBorder(border);
        }
    }
    
    
    @Override
    protected boolean processKeyBinding(KeyStroke ks, KeyEvent e,
            int condition, boolean pressed) {
        boolean processed = super.processKeyBinding(ks, e, condition, pressed);
        if (!processed) {
            processed = processKeyBinding(delegate, ks, e, condition, pressed);
        }
        return processed;
    }

    private boolean processKeyBinding(JComponent delegate, KeyStroke ks,
            KeyEvent e, int condition, boolean pressed) {
        InputMap map = delegate.getInputMap(condition);
        ActionMap am = delegate.getActionMap();

        if(map != null && am != null && isEnabled()) {
            Object binding = map.get(ks);
            Action action = (binding == null) ? null : am.get(binding);
            if (action != null) {
                return SwingUtilities.notifyAction(action, ks, e, delegate,
                                                   e.getModifiers());
            }
        }
        return false;
    }
    



    public void setHandle(Icon handle) {
        this.handle = handle;
    }
    
    

    @Override
    protected void paintBorder(Graphics g) {
        super.paintBorder(g);
        paintHandle(g);
    }

    private void paintHandle(Graphics g) {
        if ((handle == null) || (visualDepth <= 0)) return;
        int iconX;
        if (getComponentOrientation().isLeftToRight()) {
            iconX = (visualDepth - 1) * indent;
        } else {
            iconX = getWidth() - ((visualDepth -1) * indent) - handle.getIconWidth();
        }
        int iconY = 0;
        if (handle.getIconHeight() < getHeight()) {
            iconY = (getHeight() / 2) - (handle.getIconHeight() / 2);
        } 
        handle.paintIcon(this, g, iconX, iconY);
    }
    
    public static class BoxLayout implements LayoutManager2, Serializable {

        /**
         * Specifies that components should be laid out left to right.
         */
        public static final int X_AXIS = 0;
        

        /**
         * Specifies that components should be laid out in the direction of 
         * a line of text as determined by the target container's
         * <code>ComponentOrientation</code> property.  
         */
        public static final int LINE_AXIS = 2;


        /**
         * Creates a layout manager that will lay out components along the
         * given axis.  
         *
         * @param target  the container that needs to be laid out
         * @param axis  the axis to lay out components along. Can be one of:
         *              <code>BoxLayout.X_AXIS</code>,
         *              <code>BoxLayout.Y_AXIS</code>,
         *              <code>BoxLayout.LINE_AXIS</code> or
         *              <code>BoxLayout.PAGE_AXIS</code>
         *
         * @exception AWTError  if the value of <code>axis</code> is invalid 
         */
        public BoxLayout(Container target) {
            this.axis = LINE_AXIS;
            this.target = target;
        }

        /**
         * Constructs a BoxLayout that 
         * produces debugging messages.
         *
         * @param target  the container that needs to be laid out
         * @param axis  the axis to lay out components along. Can be one of:
         *              <code>BoxLayout.X_AXIS</code>,
         *              <code>BoxLayout.Y_AXIS</code>,
         *              <code>BoxLayout.LINE_AXIS</code> or
         *              <code>BoxLayout.PAGE_AXIS</code>
         *
         * @param dbg  the stream to which debugging messages should be sent,
         *   null if none
         */
        BoxLayout(Container target, PrintStream dbg) {
            this(target);
            this.dbg = dbg;
        }

        /**
         * Returns the container that uses this layout manager.
         *
         * @return the container that uses this layout manager
         *
         * @since 1.6
         */
        public final Container getTarget() {
            return this.target;
        }

        /**
         * Returns the axis that was used to lay out components.
         * Returns one of:
         * <code>BoxLayout.LINE_AXIS</code> or
         * <code>BoxLayout.PAGE_AXIS</code>
         *
         * @return the axis that was used to lay out components
         *
         * @since 1.6
         */
        public final int getAxis() {
            return this.axis;
        }

        /**
         * Indicates that a child has changed its layout related information,
         * and thus any cached calculations should be flushed.
         * <p>
         * This method is called by AWT when the invalidate method is called
         * on the Container.  Since the invalidate method may be called 
         * asynchronously to the event thread, this method may be called
         * asynchronously.
         *
         * @param target  the affected container
         *
         * @exception AWTError  if the target isn't the container specified to the
         *                      BoxLayout constructor
         */
        public synchronized void invalidateLayout(Container target) {
            checkContainer(target);
            xChildren = null;
            yChildren = null;
            xTotal = null;
            yTotal = null;
        }

        /**
         * Not used by this class.
         *
         * @param name the name of the component
         * @param comp the component
         */
        public void addLayoutComponent(String name, Component comp) {
            invalidateLayout(comp.getParent());
        }

        /**
         * Not used by this class.
         *
         * @param comp the component
         */
        public void removeLayoutComponent(Component comp) {
            invalidateLayout(comp.getParent());
        }

        /**
         * Not used by this class.
         *
         * @param comp the component
         * @param constraints constraints
         */
        public void addLayoutComponent(Component comp, Object constraints) {
            invalidateLayout(comp.getParent());
        }

        /**
         * Returns the preferred dimensions for this layout, given the components
         * in the specified target container.
         *
         * @param target  the container that needs to be laid out
         * @return the dimensions >= 0 && <= Integer.MAX_VALUE
         * @exception AWTError  if the target isn't the container specified to the
         *                      BoxLayout constructor
         * @see Container
         * @see #minimumLayoutSize
         * @see #maximumLayoutSize
         */
        public Dimension preferredLayoutSize(Container target) {
            Dimension size;
            synchronized(this) {
                checkContainer(target);
                checkRequests();
                size = new Dimension(xTotal.preferred, yTotal.preferred);
            }

            Insets insets = target.getInsets();
            size.width = (int) Math.min((long) size.width + (long) insets.left + (long) insets.right, Integer.MAX_VALUE);
            size.height = (int) Math.min((long) size.height + (long) insets.top + (long) insets.bottom, Integer.MAX_VALUE);
            return size;
        }

        /**
         * Returns the minimum dimensions needed to lay out the components
         * contained in the specified target container.
         *
         * @param target  the container that needs to be laid out 
         * @return the dimensions >= 0 && <= Integer.MAX_VALUE
         * @exception AWTError  if the target isn't the container specified to the
         *                      BoxLayout constructor
         * @see #preferredLayoutSize
         * @see #maximumLayoutSize
         */
        public Dimension minimumLayoutSize(Container target) {
            Dimension size;
            synchronized(this) {
                checkContainer(target);
                checkRequests();
                size = new Dimension(xTotal.minimum, yTotal.minimum);
            }

            Insets insets = target.getInsets();
            size.width = (int) Math.min((long) size.width + (long) insets.left + (long) insets.right, Integer.MAX_VALUE);
            size.height = (int) Math.min((long) size.height + (long) insets.top + (long) insets.bottom, Integer.MAX_VALUE);
            return size;
        }

        /**
         * Returns the maximum dimensions the target container can use
         * to lay out the components it contains.
         *
         * @param target  the container that needs to be laid out 
         * @return the dimenions >= 0 && <= Integer.MAX_VALUE
         * @exception AWTError  if the target isn't the container specified to the
         *                      BoxLayout constructor
         * @see #preferredLayoutSize
         * @see #minimumLayoutSize
         */
        public Dimension maximumLayoutSize(Container target) {
            Dimension size;
            synchronized(this) {
                checkContainer(target);
                checkRequests();
                size = new Dimension(xTotal.maximum, yTotal.maximum);
            }

            Insets insets = target.getInsets();
            size.width = (int) Math.min((long) size.width + (long) insets.left + (long) insets.right, Integer.MAX_VALUE);
            size.height = (int) Math.min((long) size.height + (long) insets.top + (long) insets.bottom, Integer.MAX_VALUE);
            return size;
        }

        /**
         * Returns the alignment along the X axis for the container.
         * If the box is horizontal, the default
         * alignment will be returned. Otherwise, the alignment needed
         * to place the children along the X axis will be returned.
         *
         * @param target  the container
         * @return the alignment >= 0.0f && <= 1.0f
         * @exception AWTError  if the target isn't the container specified to the
         *                      BoxLayout constructor
         */
        public synchronized float getLayoutAlignmentX(Container target) {
            checkContainer(target);
            checkRequests();
            return xTotal.alignment;
        }

        /**
         * Returns the alignment along the Y axis for the container.
         * If the box is vertical, the default
         * alignment will be returned. Otherwise, the alignment needed
         * to place the children along the Y axis will be returned.
         *
         * @param target  the container
         * @return the alignment >= 0.0f && <= 1.0f
         * @exception AWTError  if the target isn't the container specified to the
         *                      BoxLayout constructor
         */
        public synchronized float getLayoutAlignmentY(Container target) {
            checkContainer(target);
            checkRequests();
            return yTotal.alignment;
        }

        /**
         * Called by the AWT <!-- XXX CHECK! --> when the specified container
         * needs to be laid out.
         *
         * @param target  the container to lay out
         *
         * @exception AWTError  if the target isn't the container specified to the
         *                      BoxLayout constructor
         */
        public void layoutContainer(Container target) {
            checkContainer(target);
            int nChildren = target.getComponentCount();
            int[] xOffsets = new int[nChildren];
            int[] xSpans = new int[nChildren];
            int[] yOffsets = new int[nChildren];
            int[] ySpans = new int[nChildren];
                
            Dimension alloc = target.getSize();
            Insets in = target.getInsets();
            alloc.width -= in.left + in.right;
            alloc.height -= in.top + in.bottom;

            // Resolve axis to an absolute value (either X_AXIS or Y_AXIS)
            ComponentOrientation o = target.getComponentOrientation();
            int absoluteAxis = resolveAxis( axis, o );
            boolean ltr = (absoluteAxis != axis) ? o.isLeftToRight() : true;


            // determine the child placements
            synchronized(this) {
                checkRequests();
            
                if (absoluteAxis == X_AXIS) {
                    SizeRequirements.calculateTiledPositions(alloc.width, xTotal,
                                                             xChildren, xOffsets,
                                                             xSpans, ltr);
                    SizeRequirements.calculateAlignedPositions(alloc.height, yTotal,
                                                               yChildren, yOffsets,
                                                               ySpans);
                } else {
                    SizeRequirements.calculateAlignedPositions(alloc.width, xTotal,
                                                               xChildren, xOffsets,
                                                               xSpans, ltr);
                    SizeRequirements.calculateTiledPositions(alloc.height, yTotal,
                                                             yChildren, yOffsets,
                                                             ySpans);
                }
            }

            // flush changes to the container
            for (int i = 0; i < nChildren; i++) {
                Component c = target.getComponent(i);
                c.setBounds((int) Math.min((long) in.left + (long) xOffsets[i], Integer.MAX_VALUE),
                            (int) Math.min((long) in.top + (long) yOffsets[i], Integer.MAX_VALUE),
                            xSpans[i], ySpans[i]);

            }
            if (dbg != null) {
                for (int i = 0; i < nChildren; i++) {
                    Component c = target.getComponent(i);
                    dbg.println(c.toString());
                    dbg.println("X: " + xChildren[i]);
                    dbg.println("Y: " + yChildren[i]);
                }
            }
                
        }

        void checkContainer(Container target) {
            if (this.target != target) {
                throw new AWTError("BoxLayout can't be shared");
            }
        }
        
        void checkRequests() {
            if (xChildren == null || yChildren == null) {
                // The requests have been invalidated... recalculate
                // the request information.
                int n = target.getComponentCount();
                if (n != 3) throw new IllegalStateException("expected 3 children but have " + n);
                if (target.getComponent(2) instanceof JTextField) {
                    System.out.println("got text field" + target.getComponent(2).getPreferredSize());  
                };
                xChildren = new SizeRequirements[n];
                yChildren = new SizeRequirements[n];
                for (int i = 0; i < n; i++) {
                    Component c = target.getComponent(i);
//                    if (!c.isVisible()) {
//                        xChildren[i] = new SizeRequirements(0,0,0, c.getAlignmentX());
//                        yChildren[i] = new SizeRequirements(0,0,0, c.getAlignmentY());
//                        continue;
//                    }
                    Dimension min = c.getMinimumSize();
                    Dimension typ = c.getPreferredSize();
                    Dimension max = c.getMaximumSize();
                    xChildren[i] = new SizeRequirements(min.width, typ.width, 
                                                        max.width, 
                                                        c.getAlignmentX());
                    yChildren[i] = new SizeRequirements(min.height, typ.height, 
                                                        max.height, 
                                                        c.getAlignmentY());
                }
                
                // Resolve axis to an absolute value (either X_AXIS or Y_AXIS)
                int absoluteAxis = resolveAxis(axis,target.getComponentOrientation());

                if (absoluteAxis == X_AXIS) {
                    xTotal = SizeRequirements.getTiledSizeRequirements(xChildren);
                    yTotal = SizeRequirements.getAlignedSizeRequirements(yChildren);
                } else {
                    xTotal = SizeRequirements.getAlignedSizeRequirements(xChildren);
                    yTotal = SizeRequirements.getTiledSizeRequirements(yChildren);
                }
            }
        }
         
        /**
         * Given one of the 4 axis values, resolve it to an absolute axis.
         * The relative axis values, PAGE_AXIS and LINE_AXIS are converted
         * to their absolute couterpart given the target's ComponentOrientation
         * value.  The absolute axes, X_AXIS and Y_AXIS are returned unmodified.
         *
         * @param axis the axis to resolve
         * @param o the ComponentOrientation to resolve against
         * @return the resolved axis
         */
        private int resolveAxis( int axis, ComponentOrientation o ) {
//            int absoluteAxis;
//            if( axis == LINE_AXIS ) {
//                absoluteAxis = o.isHorizontal() ? X_AXIS : Y_AXIS;
//            } else if( axis == PAGE_AXIS ) {
//                absoluteAxis = o.isHorizontal() ? Y_AXIS : X_AXIS;
//            } else {
//                absoluteAxis = axis;
//            } 
            return X_AXIS;
       }


        private int axis;
        private Container target;

        private transient SizeRequirements[] xChildren;
        private transient SizeRequirements[] yChildren;
        private transient SizeRequirements xTotal;
        private transient SizeRequirements yTotal;
        
        private transient PrintStream dbg;
    }
    

    
}
