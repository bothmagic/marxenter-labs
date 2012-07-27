/*
 * $Id: JXTransitionPanel.java 2629 2008-08-06 08:27:49Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx;

import java.lang.ref.SoftReference;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;

import java.util.ArrayList;
import java.util.List;

import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.TimingTarget;
import org.jdesktop.swingx.effect.TransitionEffect;
import org.jdesktop.swingx.effect.TransitionSource;
import org.jdesktop.swingx.image.ImageUtilities;

/**
 * Generated comment for JXTransitionPanel.
 *
 * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
 */
public class JXTransitionPanel extends JXPanel implements ExtensionRepaintManager.RepaintManagerExtension {

    /**
     * Defines the buffer strategy for transitions.
     */
    public static enum BufferStrategy {
        BUFFER_NONE, BUFFER_OLD, BUFFER_NEW, BUFFER_BOTH
    }







    private TransitionEffect<? super JXTransitionPanel, Object> effect;
    private int duration = 1000;
    private float acceleration = 0.5f;
    private float deceleration = 0.4f;
    private BufferStrategy bufferStrategy = BufferStrategy.BUFFER_BOTH;

    private final List<Page> pages = new ArrayList<Page>();

    /**
     * Manages the transition state.
     */
    private TransitionState transitionState = new TransitionState();

    public JXTransitionPanel() {
        this(null);
    }





    public JXTransitionPanel(TransitionEffect<? super JXTransitionPanel, Object> effect) {
        this.effect = effect;
        setLayout(new Layout());
        ExtensionRepaintManager.install();
    }





    public BufferStrategy getBufferStrategy() {
        return bufferStrategy;
    }





    public void setBufferStrategy(BufferStrategy bufferStrategy) {
        this.bufferStrategy = bufferStrategy;
    }





    public TransitionEffect<? super JXTransitionPanel, Object> getEffect() {
        return effect;
    }





    public void setEffect(TransitionEffect<? super JXTransitionPanel, Object> effect) {
        TransitionEffect<? super JXTransitionPanel, Object> old = getEffect();
        this.effect = effect;
        firePropertyChange("effect", old, getEffect());
    }





    @Override
    protected void addImpl(Component comp, Object constraints, int index) {
        if (!(constraints instanceof String)) {
            throw new IllegalArgumentException("constraints must be a String: " + constraints);
        }
        Page page = new Page(comp, (String) constraints);
        comp.setVisible(false);
        if (index == -1) {
            pages.add(page);
        } else {
            pages.add(index, page);
        }
        super.addImpl(comp, constraints, index); //To change body of overridden methods use File | Settings | File Templates.

        if (getComponentCount() == 1) {
            show(0);
        }
    }





    public void show(int index) {
        show(index, getEffect());
    }





    public void show(int index, TransitionEffect<? super JXTransitionPanel, Object> effect) {
        stopTransition();
        startTransition(getPage(index), effect);
    }





    public void show(String component) {
        show(component, getEffect());
    }





    public void show(String component, TransitionEffect<? super JXTransitionPanel, Object> effect) {
        stopTransition();
        startTransition(getPage(component), effect);
    }





    protected Page getPage(int index) {
        return pages.get(index);
    }





    protected Page getPage(String name) {
        Page result = null;
        for (Page page : pages) {
            if (name.equals(page.name)) {
                result = page;
                break;
            }
        }
        if (result == null) {
            throw new IllegalArgumentException("No component with the given name can be shown: " + name);
        }
        return result;
    }





    protected void stopTransition() {
        transitionState.stopTransition();
    }





    protected void cancelTransition() {
        transitionState.cancelTransition();
    }





    protected void startTransition(Page page, TransitionEffect<? super JXTransitionPanel, Object> effect) {
        transitionState.startTransition(page, effect);
    }





    @Override
    public void paint(Graphics g) {
        if (isOpaque()) {
            g.setColor(getBackground());
            ((Graphics2D) g).fill(g.getClipBounds());
        }
        super.paint(g);
    }





    @Override
    protected void paintChildren(Graphics g) {
        TransitionEffect<? super JXTransitionPanel, Object> effect = transitionState.effect;
        if (effect == null) {
            effect = getEffect();
        }
        paintTransition(g, transitionState.getFrom(), transitionState.getTo(), transitionState.getBias(), effect);
    }





    protected void paintTransition(Graphics g, Page from, Page to, float bias,
                                   TransitionEffect<? super JXTransitionPanel, Object> effect) {
        if (effect == null) {
            super.paintChildren(g);
        } else {
            effect.paintTransition(g, from, to, bias);
        }
    }





    public boolean extendDirtyRegion(JComponent original, Rectangle dirtyRegion) {
        if (transitionState == null) { // can happen when constructed
            return false;
        }
        TransitionEffect<? super JXTransitionPanel, Object> effect = transitionState.effect;
        if (effect == null) {
            effect = getEffect();
        }
        boolean result = false;
        if (effect != null) {
            int x = dirtyRegion.x;
            int y = dirtyRegion.y;
            int width = dirtyRegion.width;
            int height = dirtyRegion.height;
            result = effect.transform(dirtyRegion,
                                      transitionState.getFrom(), transitionState.getTo(), transitionState.getBias());
            if (result) {
                SwingUtilities.computeUnion(x, y, width, height, dirtyRegion);
            }
        }
        return result;
    }





    public int getDuration() {
        return duration;
    }





    public void setDuration(int duration) {
        int old = getDuration();
        this.duration = duration;
        firePropertyChange("duration", old, getDuration());
    }





    public float getAcceleration() {
        return acceleration;
    }





    public void setAcceleration(float acceleration) {
        float old = getAcceleration();
        this.acceleration = acceleration;
        firePropertyChange("acceleration", old, getAcceleration());
    }





    public float getDeceleration() {
        return deceleration;
    }





    public void setDeceleration(float deceleration) {
        float old = getDeceleration();
        this.deceleration = deceleration;
        firePropertyChange("deceleration", old, getDeceleration());
    }





    /**
     * Encapsulates the metadata about a page in this component.
     */
    protected class Page implements TransitionSource<JXTransitionPanel, Object> {
        private boolean buffered;
        private Component component;
        private SoftReference<BufferedImage> buffer;
        private String name;

        public Page() {
        }





        public Page(Component component, String name) {
            this.component = component;
            this.name = name;
        }





        public void paintSource(Graphics g) {
            if (isBuffered()) {
                BufferedImage img = getBuffer();
                g.drawImage(img, component.getX(), component.getY(), null);
            } else {
                component.paint(g);
            }
        }





        public Rectangle getSourceBounds(Rectangle dest) {
            return component.getBounds(dest);
        }





        public Object getSource() {
            return isBuffered() ? getBuffer() : component;
        }





        protected BufferedImage getBuffer() {
            BufferedImage img = buffer == null ? null : buffer.get();
            if (img == null || component.getWidth() != img.getWidth() || component.getHeight() != img.getHeight()) {
                if (img != null) {
                    ImageUtilities.releaseImage(img);
                }
                img = ImageUtilities.createCompatibleImage(component.getWidth(), component.getHeight(),
                      component.isOpaque() ? Transparency.OPAQUE : Transparency.TRANSLUCENT);

                Graphics g = img.getGraphics();
                g.setFont(component.getFont());
                g.setColor(component.getBackground());
                component.paint(g);
                g.dispose();

                buffer = new SoftReference<BufferedImage>(img);
            }
            return img;
        }





        public JXTransitionPanel getContainer() {
            return JXTransitionPanel.this;
        }





        public boolean isBuffered() {
            return buffered;
        }





        public void setBuffered(boolean buffered) {
            this.buffered = buffered;
            if (!buffered && this.buffer != null) {
                BufferedImage img = buffer.get();
                if (img != null) {
                    ImageUtilities.releaseImage(img);
                    this.buffer.clear();
                }
                this.buffer = null;
            }
        }
    }







    /**
     * Simple layout that lays all children exactly over the contents of the parent component within its insets.
     */
    private static class Layout implements LayoutManager {

        public void addLayoutComponent(String name, Component comp) {
        }





        public void removeLayoutComponent(Component comp) {
        }





        public Dimension preferredLayoutSize(Container parent) {
            Dimension result = new Dimension();
            for (Component component : parent.getComponents()) {
                Dimension dim = component.getPreferredSize();
                if (result.width < dim.width) {
                    result.width = dim.width;
                }
                if (result.height < dim.height) {
                    result.height = dim.height;
                }
            }
            Insets i = parent.getInsets();
            result.width += i.left + i.right;
            result.height += i.top + i.bottom;
            return result;
        }





        public Dimension minimumLayoutSize(Container parent) {
            Dimension result = new Dimension();
            for (Component component : parent.getComponents()) {
                Dimension dim = component.getMinimumSize();
                if (result.width < dim.width) {
                    result.width = dim.width;
                }
                if (result.height < dim.height) {
                    result.height = dim.height;
                }
            }
            Insets i = parent.getInsets();
            result.width += i.left + i.right;
            result.height += i.top + i.bottom;
            return result;
        }





        public void layoutContainer(Container parent) {
            int width = parent.getWidth();
            int height = parent.getHeight();
            Insets i = parent.getInsets();
            for (Component c : parent.getComponents()) {
                c.setBounds(i.left, i.top, width - i.left - i.right, height - i.top - i.bottom);
            }
        }
    }







    private class TransitionState {
        private Page from;
        private Page to;
        private float bias;

        private Animator animator;
        private TransitionEffect<? super JXTransitionPanel, Object> effect;

        public Page getFrom() {
            return from;
        }





        public Page getTo() {
            return to;
        }





        public float getBias() {
            return bias;
        }





        public Animator createDefaultAnimator() {
            Animator animator = new Animator(getDuration());
            animator.setResolution(60);
            animator.setAcceleration(getAcceleration());
            animator.setDeceleration(getDeceleration());
            animator.addTarget(new BiasChangeTask());
            return animator;
        }





        public void stopTransition() {
            if (animator != null) {
                animator.stop();
            }
        }





        public void cancelTransition() {
            if (animator != null) {
                animator.cancel();
            }
        }





        public void startTransition(Page page, TransitionEffect<? super JXTransitionPanel, Object> effect) {
            from = to;
            to = page;

            if (effect == null) {
                if (from != null) {
                    from.component.setVisible(false);
                }
                if (to != null) {
                    to.component.setVisible(true);
                }
                repaint();
            } else {
                if (animator == null) {
                    animator = createDefaultAnimator();
                }

                this.effect = effect;
                animator.start();
            }

        }





        public void setBias(float bias) {
            this.bias = bias;
            repaint();
        }





        private class BiasChangeTask implements TimingTarget {

            public void timingEvent(float fraction) {
                setBias(fraction);
            }





            public void begin() {
                // ensure both components are visible
                BufferStrategy buf = getBufferStrategy();
                if (from != null) {
                    from.component.setVisible(true);
                    from.setBuffered(buf == BufferStrategy.BUFFER_BOTH || buf == BufferStrategy.BUFFER_OLD);
                }
                if (to != null) {
                    to.component.setVisible(true);
                    to.setBuffered(buf == BufferStrategy.BUFFER_BOTH || buf == BufferStrategy.BUFFER_NEW);
                }
            }





            public void end() {
                if (from != null) {
                    from.component.setVisible(false);
                    from.setBuffered(false);
                }
                if (to != null) {
                    to.component.setVisible(true);
                    to.setBuffered(false);
                }
            }





            public void repeat() {
                throw new UnsupportedOperationException();
            }
        }
    }
}
