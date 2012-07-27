/*
 * $Id: JXEffectPanel.java 2629 2008-08-06 08:27:49Z mattnathan $
 *
 * Copyright 2005 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */

package org.jdesktop.swingx;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.LayoutManager2;
import java.awt.Rectangle;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.jdesktop.swingx.effect.EffectSource;
import org.jdesktop.swingx.effect.GraphicsEffect;

/**
 * A component that applies an effect to its content. The effect is updated in real-time as the child components are
 * repainted.
 *
 * @author Matt Nathan
 */
public class JXEffectPanel extends JComponent implements ExtensionRepaintManager.RepaintManagerExtension {
    /**
     * Defines this component as an EffectSource
     */
    private final EffectSource<JXEffectPanel> effectSource =
          new EffectSource<JXEffectPanel>() {
        public void paintSource(Graphics g) {
            JXEffectPanel.this.paintSuper(g);
        }





        public Rectangle getSourceBounds(Rectangle dest) {
            return JXEffectPanel.this.contentPane.getBounds(dest);
        }





        public JXEffectPanel getSource() {
            return JXEffectPanel.this;
        }
    };
    private GraphicsEffect<? super JXEffectPanel> graphicsEffect;

    /**
     * If true then the original image should be painted. The effect may ignore this property if desired.
     */
    private boolean originalVisible = true;

    private Container contentPane;
    /**
     * Property used to determin if container methods should forward to the contentPane.
     */
    private boolean contentPaneChecking = true;
    /**
     * Added to the graphicsEffect to provide repaint calls on property change.
     */
    private Repainter repainter;

    /**
     * Creates a new instance of JXEffectPanel
     */
    public JXEffectPanel() {
        if (!ExtensionRepaintManager.install()) {
            //noinspection UseOfSystemOutOrSystemErr
            System.err.println("Could not install RepaintManager - you may experience visual artifacts");
            // The following lines have been commented as this can cause problems
            // in GUI builders as these usually install their own RepaintManager
            // instances

//         throw new UnsupportedOperationException("This component cannot be used with a " +
//                                                 RepaintManager.currentManager(this).getClass().getName() +
//                                                 " RepaintManager");
        }
        init();
    }





    /**
     * Create a new instance of this class with the given effect.
     *
     * @param effect The effect to apply to all children of this container.
     * @see #setEffect
     */
    public JXEffectPanel(GraphicsEffect<? super JXEffectPanel> effect) {
        this();
        setEffect(effect);
    }





    /**
     * Initialises this component. Sets the layout and adds the content pane.
     */
    private void init() {
        setContentPaneChecking(false);
        try {
            setLayout(new EffectLayout());
        } finally {
            setContentPaneChecking(true);
        }
        JComponent contentPane = new JPanel(new BorderLayout());
        contentPane.setOpaque(false);
        setContentPane(contentPane);
    }





    /**
     * Extends the dirty region to include the area that the current region will affect in the graphicsEffect.
     *
     * @param original The original dirty component.
     * @param dirtyRegion The original dirty bounds.
     * @return {@code true} if the current GraphicsEffect is non-null and active.
     */
    public boolean extendDirtyRegion(JComponent original, Rectangle dirtyRegion) {
        GraphicsEffect<? super JXEffectPanel> graphicsEffect = getEffect();
        boolean result = graphicsEffect != null;
        if (result) {
            Rectangle r = (Rectangle) dirtyRegion.clone();
            result = graphicsEffect.transform(r, effectSource);
            if (result) {
                // need to do this as the repaint was requested on this area for a reason.
                SwingUtilities.computeUnion(r.x, r.y, r.width, r.height, dirtyRegion);
            }
        }
        return result;
    }





    /**
     * Invoked by Swing to draw components. Applications should not invoke <code>paint</code> directly, but should
     * instead use the <code>repaint</code> method to schedule the component for redrawing.
     * <p/>
     * This method actually delegates the work of painting to three protected methods: <code>paintComponent</code>,
     * <code>paintBorder</code>, and <code>paintChildren</code>.  They're called in the order listed to ensure that
     * children appear on top of component itself. Generally speaking, the component and its children should not paint
     * in the insets area allocated to the border. Subclasses can just override this method, as always.  A subclass that
     * just wants to specialise the UI (look and feel) delegate's <code>paint</code> method should just override
     * <code>paintComponent</code>.
     *
     * @param g the <code>Graphics</code> context in which to paint
     * @see #paintComponent
     * @see #paintBorder
     * @see #paintChildren
     * @see #getComponentGraphics
     * @see #repaint
     * @see #getEffect
     */
    @Override
    public void paint(Graphics g) {
        GraphicsEffect<? super JXEffectPanel> effect = getEffect();
        if (effect == null) {
            paintSuper(g);
        } else {
            effect.paintEffect(g, effectSource);
        }
    }





    /**
     * Call this method if you wish to override the paint method and don't want this classes paint code to be called.
     *
     * @param g The graphics passed to paint.
     */
    protected void paintSuper(Graphics g) {
        super.paint(g);
    }





    /**
     * Get the effect that is applied to this components children. This can return {@code null}.
     *
     * @return the effect applied to this component.
     */
    public GraphicsEffect<? super JXEffectPanel> getEffect() {
        return graphicsEffect;
    }





    /**
     * Sets the graphicsEffect that is to be applied to this component.
     *
     * @param graphicsEffect The new effect.
     */
    public void setEffect(GraphicsEffect<? super JXEffectPanel> graphicsEffect) {
        GraphicsEffect old = getEffect();
        if (old != graphicsEffect) {
            if (old != null) {
                old.removePropertyChangeListener(repainter);
            }
            this.graphicsEffect = graphicsEffect;
            if (graphicsEffect != null) {
                if (repainter == null) {
                    repainter = new Repainter();
                }
                graphicsEffect.addPropertyChangeListener(repainter);
            }
            firePropertyChange("graphicsEffect", old, getEffect());
            revalidate();
            repaint();
        }
    }





    /**
     * {@inheritDoc}
     */
    @Override
    protected void addImpl(Component comp, Object constraints, int index) {
        if (isContentPaneChecking()) {
            getContentPane().add(comp, constraints, index);
        } else {
            super.addImpl(comp, constraints, index);
        }
    }





    /**
     * Sets the layout manager for this container. If {@code isContentPaneChecking()} returns {@code true} then this is
     * forwarded to the {@code contentPane} otherwise this components layout is set.
     *
     * @param layout the specified layout manager
     * @see #doLayout
     * @see #getLayout
     * @see #isContentPaneChecking()
     * @see #setContentPaneChecking(boolean)
     */
    @Override
    public void setLayout(LayoutManager layout) {
        if (isContentPaneChecking()) {
            getContentPane().setLayout(layout);
        } else {
            super.setLayout(layout);
        }
    }





    /**
     * Removes the specified component from this container. This method also notifies the layout manager to remove the
     * component from this container's layout via the <code>removeLayoutComponent</code> method.
     * <p/>
     * <p>The target for this method is determined by the {@code contentPaneChecking} property. If {@code true} then
     * this method is forwarded to the {@code contentPane} otherwise the super classes {@code remove} method is
     * called.</p>
     *
     * @param comp the component to be removed
     * @see #add
     * @see #remove(int)
     * @see #setContentPaneChecking(boolean)
     * @see #isContentPaneChecking()
     */
    @Override
    public void remove(Component comp) {
        if (isContentPaneChecking()) {
            contentPane.remove(comp);
        } else {
            super.remove(comp);
        }
    }





    /**
     * Removes the component, specified by <code>index</code>, from this container. This method also notifies the layout
     * manager to remove the component from this container's layout via the <code>removeLayoutComponent</code> method.
     * <p/>
     * <p>The target for this method is determined by the {@code contentPaneChecking} property. If {@code true} then
     * this method is forwarded to the {@code contentPane} otherwise the super classes {@code remove} method is
     * called.</p>
     *
     * @param index the index of the component to be removed
     * @see #add
     * @see #isContentPaneChecking()
     * @see #setContentPaneChecking(boolean)
     * @since JDK1.1
     */
    @Override
    public void remove(int index) {
        if (isContentPaneChecking()) {
            getContentPane().remove(index);
        } else {
            super.remove(index);
        }
    }





    /**
     * Removes all the components from this container. This method also notifies the layout manager to remove the
     * components from this container's layout via the <code>removeLayoutComponent</code> method.
     * <p/>
     * <p>The target of this method is determined by the contentPaneChecking property. If {@code true} then this method
     * is forwarded to the {@code contentPane} else the super classes {@code removeAll} method is called.</p>
     *
     * @see #add
     * @see #remove
     * @see #isContentPaneChecking()
     * @see #setContentPaneChecking(boolean)
     */
    @Override
    public void removeAll() {
        if (isContentPaneChecking()) {
            getContentPane().removeAll();
        } else {
            super.removeAll();
        }
    }





    /**
     * Get the content pane for this component. The content pane is the component that contains all this components
     * children. Note that calls to {@link #add}, {@link #remove} and {@link #setLayout} are all forwarded to the
     * content pane by default.
     *
     * @return The contentPane for this component.
     */
    public Container getContentPane() {
        return contentPane;
    }





    /**
     * Sets the container for this component children.
     *
     * @param contentPane This components children container.
     */
    public void setContentPane(Container contentPane) {
        Container old = getContentPane();
        if (old != null) {
            setContentPaneChecking(false);
            try {
                remove(old);
            } finally {
                setContentPaneChecking(true);
            }
        }
        setContentPaneChecking(false);
        try {
            add(contentPane, BorderLayout.CENTER);
        } finally {
            setContentPaneChecking(true);
        }
        this.contentPane = contentPane;
    }





    /**
     * Returns true if calls to {@link #add}, {@link #remove} and {@link #setLayout} should be forwarded to the
     * contentPane instance.
     *
     * @return {@code true} is calls should be forwarded.
     */
    protected boolean isContentPaneChecking() {
        return contentPaneChecking;
    }





    /**
     * Returns true if the component effect should paint the original contents as well as the effect. This is only a
     * hint to the component effect and may not be supported in all cases.
     * <p/>
     * E.G. with the translucent effect it makes no sense to paint the original component over the effect component and
     * so this property is ignored.
     * <p/>
     * Effects should however try to enforce this property when possible.
     *
     * @return {@code true} if the original component image should be painted as well as the effect image.
     */
    public boolean isOriginalVisible() {
        return originalVisible;
    }





    /**
     * Sets whether calls to {@link #add}, {@link #remove} and {@link #setLayout} should be forwarded to the
     * contentPane. This is a protected property as it is only available for subclasses to provide extended
     * functionality.
     *
     * @param contentPaneChecking {@code true} if calls should be forwarded to the contentPane.
     * @see #setOriginalVisible
     */
    protected void setContentPaneChecking(boolean contentPaneChecking) {
        this.contentPaneChecking = contentPaneChecking;
    }





    /**
     * Hints to the effect that the original component image should be painted as well as the effect component image.
     * This may not be supported by all effects.
     *
     * @param originalVisible {@code true} if the original component should be painted as well as the effect.
     * @see #isOriginalVisible
     */
    public void setOriginalVisible(boolean originalVisible) {
        boolean old = isOriginalVisible();
        this.originalVisible = originalVisible;
        firePropertyChange("originalVisible", old, isOriginalVisible());
        repaint();
    }





    /**
     * Can be added to the source effect to repaint this component when a property has changed.
     */
    private class Repainter implements PropertyChangeListener {
        public void propertyChange(PropertyChangeEvent evt) {
            revalidate();
            repaint();
        }
    }







    private static class EffectLayout implements LayoutManager2 {
        private Rectangle transformedRect = null;

        public EffectLayout() {
        }





        public Dimension preferredLayoutSize(Container c) {
            JXEffectPanel fx = (JXEffectPanel) c;
            Insets i = c.getInsets();
            Dimension result = new Dimension();
            Dimension d = fx.getContentPane().getPreferredSize();
            if (transformedRect == null) {
                transformedRect = new Rectangle(0, 0, d.width, d.height);
//            fx.extendDirtyRegion(fx, transformedRect);

                GraphicsEffect<? super JXEffectPanel> graphicsEffect = fx.getEffect();
                boolean allowed = graphicsEffect != null;
                if (allowed) {
                    Rectangle r = (Rectangle) transformedRect.clone();
                    allowed = graphicsEffect.transform(r, new StaticEffectSource(fx, transformedRect));
                    if (allowed) {
                        transformedRect.setBounds(r);
                    }
                }

            }
            result.width = Math.max(d.width, transformedRect.x < 0 ?
                                    Math.max(d.width - transformedRect.x, transformedRect.width) : transformedRect.x + transformedRect.width);
            result.height = Math.max(d.height, transformedRect.y < 0 ?
                                     Math.max(d.height - transformedRect.y, transformedRect.height) : transformedRect.y + transformedRect.height);

            result.width += i.left + i.right;
            result.height += i.top + i.bottom;
            return result;
        }





        public void addLayoutComponent(String name, Component comp) {
        }





        public void removeLayoutComponent(Component comp) {
        }





        public Dimension minimumLayoutSize(Container parent) {
            return preferredLayoutSize(parent);
        }





        public void layoutContainer(Container parent) {
            preferredLayoutSize(parent); // validates the cache
            JXEffectPanel fx = (JXEffectPanel) parent;
            Container contentPane = fx.contentPane;

            int x = 0;
            int y = 0;
            int w;
            int h;

            if (transformedRect.x < 0) {
                x = -transformedRect.x;
            }
            if (transformedRect.y < 0) {
                y = -transformedRect.y;
            }

            Dimension d = contentPane.getPreferredSize();
            w = d.width;
            h = d.height;

            Insets i = parent.getInsets();
            contentPane.setBounds(i.left + x, i.top + y, w, h);
        }





        public void addLayoutComponent(Component comp, Object constraints) {
        }





        public Dimension maximumLayoutSize(Container target) {
            return preferredLayoutSize(target);
        }





        public float getLayoutAlignmentX(Container target) {
            return 0.5F;
        }





        public float getLayoutAlignmentY(Container target) {
            return 0.5F;
        }





        public void invalidateLayout(Container target) {
            transformedRect = null;
        }





        private static class StaticEffectSource implements EffectSource<JXEffectPanel> {
            private JXEffectPanel source;
            private Rectangle bounds;

            public StaticEffectSource(JXEffectPanel source, Rectangle bounds) {
                this.source = source;
                this.bounds = bounds;
            }





            public void paintSource(Graphics g) {
                throw new UnsupportedOperationException();
            }





            public Rectangle getSourceBounds(Rectangle dest) {
                if (dest == null) {
                    dest = new Rectangle(bounds);
                } else {
                    dest.setBounds(bounds);
                }
                return dest;
            }





            public JXEffectPanel getSource() {
                return source;
            }
        }
    }
}
