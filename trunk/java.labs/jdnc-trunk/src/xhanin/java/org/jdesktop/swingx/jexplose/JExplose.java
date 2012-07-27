/*
 * $Id: JExplose.java 985 2006-12-22 19:40:13Z xhanin $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.jexplose;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.beans.PropertyVetoException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ListIterator;

import javax.swing.JDesktopPane;
import javax.swing.SwingUtilities;

import org.jdesktop.swingx.jexplose.animation.StandardAnimationStrategy;
import org.jdesktop.swingx.jexplose.core.AnimationUpdate;
import org.jdesktop.swingx.jexplose.core.BackgroundPainter;
import org.jdesktop.swingx.jexplose.core.EffectPanel;
import org.jdesktop.swingx.jexplose.core.ExploseLayout;
import org.jdesktop.swingx.jexplose.core.LayoutSelectionPainter;
import org.jdesktop.swingx.jexplose.core.LayoutStandardPainter;
import org.jdesktop.swingx.jexplose.core.Painter;
import org.jdesktop.swingx.jexplose.core.Thumbnail;
import org.jdesktop.swingx.jexplose.layout.StandardLayoutStrategy;


/**
 * This class is used to invoke the explose effect on a <code>JDesktopPane</code>.
 * The explose effect enables the user to view all its opened internal frames
 * and let him choose which internal frame he wants to bring to front.
 * <p>
 * For simple cases, prefer the use of the {@link org.jdesktop.swingx.jexplose.ExplosableDesktop}
 * directly.
 * </p>
 * <p>
 * Two types of effects are available.<br/> 
 * (1) Non intrusive, it can thus be used on any <code>JDesktopPane</code> or <code>JDesktopPane</code> subclass.<br/>
 * (2) Only avaible on <code>JDesktopPane</code> subclasses which follow
 * an implementation pattern, delegating some paint to an <code>Explosable</code>.
 * </p>
 * <p>
 * The advantages of the second type of effect are:<br/>
 * - better performance than the first one.<br/>
 * - animated internal frames are still animated during the explose effect<br/>
 * </p>
 * <p>
 * To invoke the effect, you should first obtain a <code>JExplose</code> instance, either
 * using the getInstance method, or by using its constructor. Then you simply have to call 
 * {@link #explose(JDesktopPane)} or {@link #explose(Explosable)}.<br/>
 * For instance:
 * <pre>
 * JDesktopPane desktop = new JDesktopPane();
 * ...
 * JExplose.getInstance().explose(desktop);
 * </pre>
 * or
 * <pre>
 * Explosable explosable = ...;
 * JExplose.getInstance().explose(explosable);
 * </pre>
 * </p>
 * If you want to customize the effect, you can do it when obtaining your JExplose instance
 * through its constructor: you then have to give it a <code>LayoutStrategy</code> and an
 * <code>AnimationStrategy</code>, giving you the possibility to configure these two
 * aspects of the effect.
 * 
 * Developer Note: This code has been written in early 2005 and does not take advantage of 
 * great libraries like the timing framework or Painters. Feel free to modify and improve it.
 *  
 * @see Explosable
 * @see ExplosableDesktop
 * @see LayoutStrategy
 * @see AnimationStrategy
 * 
 * @author Xavier Hanin
 */
public class JExplose {
    private static JExplose _instance = new JExplose(new StandardLayoutStrategy(), new StandardAnimationStrategy(7, 50));
    /**
     * Obtains an instance of JExplose with default configuration.
     * Use this instance to invoke simply the explose effect.
     * @return an immediately usable instance of JExplose
     */
    public static JExplose getInstance() {
        return _instance;
    }
    /**
     * Allow to change shared instance of <code>JExplose</code>.<br/>
     * This is usefull when yo want to change <code>JExplose</code> settings and configuration
     * 
     * @param explose new <code>JExplose</code> instance
     */
    public static void setInstance(JExplose explose) {
        _instance = explose;
        
    }
    private LayoutStrategy _layoutStrategy;
    private AnimationStrategy _animationStrategy;
    
    private EffectPanel _effectPanel;
    private ExploseLayout _layout;

    private ExplosedHandler _explosedHandler = new ExplosedHandler();
    private Explosable _explosable;
    private JDesktopPane _desktop;
    private Painter _painter;
    private Painter _selPainter;
    private static transient boolean _isExplosed = false;
    private long _start;
    private boolean _debug = false;
    private Painter _bgPainter;


    /**
     * JExplose constructor. Use this constructor to construct a JExplose
     * instance with the configuration you want. You can indeed configure
     * the layout and animation strategy. See corresponding interfaces for details.
     * <p>
     * Prefer {@link #getInstance()} if you do not want to customize the effect.
     * </p>
     * @param layoutStrategy the layout strategy the instance of JExplose will use
     * to layout the internal frames
     * @param animationStrategy the animation strategy the instance of JExplose will
     * use to animate the internal frames from their current state to the explose state,
     * and vice versa. 
     */
    public JExplose(LayoutStrategy layoutStrategy, AnimationStrategy animationStrategy) {
        _layoutStrategy = layoutStrategy;
        _animationStrategy = animationStrategy;
    }
    
    /**
     * Executes the explose effect on the given <code>JDesktopPane</code>.
     * Nothing particular is need on the desktop pane, but if
     * it is possible for you, prefer the {@link #explose(Explosable)}
     * method, which provides better performances.
     * @param desktop the desktop pane on which the effect must be run.
     */
    public void explose(final JDesktopPane desktop) {
        explose(null, desktop);
    }
    /**
     * Executes the explose effect on the given <code>Explosable</code>.
     * If you do not have the possibility to obtain an <code>Explosable</code>
     * instance for the desktop you want to apply the effect on,
     * you can use {@link #explose(JDesktopPane)} instead. 
     * @param explosable the explosable on which the effect must be run.
     */
    public void explose(final Explosable explosable) {
        explose(explosable, explosable.getDesktop());
    }
    
    /**
     * Configure the image to display as background when the JExplose effect
     * has been invoked. By default, the JExplose logo is displayed.
     * Any image loadable by ImageIcon can be used. The image is resized
     * to fit 80% of the desktop when the effect is invoked.
     * If the url given here is null, no background will be displayed.
     * <em>Note: this feature is only available with the Standard Edition</em>
     * @param backgroundURL the url of the image to display as background
     */
    public void setBackground(URL backgroundURL) {
        BackgroundPainter.setBackgroundURL(backgroundURL);
    }
    
    private void explose(final Explosable explosable, final JDesktopPane desktop) {
        if (desktop.getAllFrames().length <= 1) {
            return;
        }
        Dimension dim = desktop.getSize();
        if (dim.width <= 10 || dim.height <= 10) {
            return;
        }
        _start = System.currentTimeMillis();
        synchronized (getClass()) {
            if (_isExplosed) {
                return;
            }
            _isExplosed = true;
        }
        _explosable = explosable;
        _desktop = desktop;
        _isExplosed = true;
        
        // first we use an effect panel in front of the desktop
        _effectPanel = new EffectPanel();
        _effectPanel.install(_desktop);
        if (_explosable != null) {
            _effectPanel.setOpaque(false);
            _effectPanel.setBackground(new Color(200,200,200,50));
        } else {
            _effectPanel.setOpaque(true);
        }

        msg("starting thread");
        // now we can actually start the effect in a separate thread
        Thread t = new Thread() {
            public void run() {
                msg("thread launched: doing layout");
                _layout = getLayoutStrategy().layout(_desktop);
                msg("layout done");
                if (_explosable == null) {
//                    _effectPanel.bePatient();
                }
                msg("launching anim");
                getAnimationStrategy().explose(_layout, new AnimationUpdate() {
                    boolean addedPainter = false;
                    public void step() {
                        msg("stepping");
                        if (_explosable != null) {
                            _explosable.start();                            
                        }
                        _layout.moved = true;
                        if (!addedPainter) {
                            _effectPanel.setPainter(getPainter()); 
                            addedPainter = true;
                        } else {
                            _effectPanel.repaint();
                        }
                    }
                    public void end() {
                        msg("reached anim end");
                        if (_explosable != null) {
                            _explosable.start();                            
                        }
                        _layout.setFinal(true);
                        _layout.computeDimension();
                        _layout.updateScale();
                        _layout.moved = true;
                        msg("set final state: scale="+_layout.getScale());
                        synchronized (_effectPanel) {
                            _effectPanel.removeAllPainters();
                            if (_explosable == null) {
                                _effectPanel.addPainter(getBackgroundPainter());
                            } else {
                                _explosable.addPainter(getBackgroundPainter());
                            }
                            _effectPanel.addPainter(getPainter());
                            _effectPanel.addPainter(getSelectionPainter());
                        }
                        msg("explosed !!!");
                        _explosedHandler.install(_effectPanel);
                    }
                });
            }
        };
        t.start();
    }
    
    private void msg(String msg) {
        if (_debug) {
            System.out.println(System.currentTimeMillis() + ": "+msg+" ("+(System.currentTimeMillis()-_start)+"ms)");
        }
    }

    private void implose(final Thumbnail thumb) {
        _start = System.currentTimeMillis();
        synchronized (getClass()) {
            if (!_isExplosed) {
                throw new IllegalStateException("impossible to call implose when not explosed");
            }
        }
        new Thread() {
            public void run() {
                _layout.setFinal(false);
                _effectPanel.removePainter(getSelectionPainter());
                if (_explosable == null) {
                    _effectPanel.removePainter(getBackgroundPainter());
                } else {
                    _explosable.removePainter(getBackgroundPainter());
                }
                if (thumb != null) {
                    try {
                        SwingUtilities.invokeAndWait(new Runnable() {
                            public void run() {
                                try {
                                    thumb.getIFrame().toFront();
                                    thumb.getIFrame().setSelected(true);
                                    thumb.getIFrame().requestFocus();
                                } catch (PropertyVetoException e1) {}
                            }
                        });
                    } catch (InterruptedException e) {
                    } catch (InvocationTargetException e) {
                    }
                }
                getAnimationStrategy().implose(_layout, new AnimationUpdate() {
                    public void step() {
                        _layout.moved = true;
                        _effectPanel.repaint();
                    }
                    public void end() {
                        SwingUtilities.invokeLater(new Runnable() {
                            public void run() {
                                if (_explosable != null) {
                                    // restores original state
                                    _explosable.setScale(1.0);
                                    _explosable.end();
                                    for (ListIterator it = _layout.getThumbs().listIterator(); it.hasNext();) {
                                        Thumbnail t = (Thumbnail)it.next();
                                        t.getIFrame().setLocation(t.getOriginalLocation());
                                        if (thumb != t) {
                                            t.restore();
                                        }
                                    }
                                    _explosable.getDesktop().repaint();
                                } else {
                                    for (ListIterator it = _layout.getThumbs().listIterator(); it.hasNext();) {
                                        Thumbnail t = (Thumbnail)it.next();
                                        if (thumb != t) {
                                            t.restore();
                                        }
                                    }                                    
                                }
                                _effectPanel.dispose();
                                _layout.dispose();

                                _effectPanel = null;
                                _layout = null;
                                
                                _explosable = null;

                                _painter = null;
                                _selPainter = null;
                                _bgPainter = null;
                                
                                synchronized (getClass()) {
                                    _isExplosed = false;
                                }

                                _desktop.getRootPane().revalidate();
                                _desktop.getRootPane().repaint();
                                _desktop = null;
                                msg("implosed !!!");
                            }
                        });
                    }
                }); 
            }}.start();
    }

    private AnimationStrategy getAnimationStrategy() {
        return _animationStrategy;
    }

    private LayoutStrategy getLayoutStrategy() {
        return _layoutStrategy;
    }
    private Painter getSelectionPainter() {
        if (_selPainter == null) {
            _selPainter = new LayoutSelectionPainter(_layout);
        }
        return _selPainter;
    }
    
    private Painter getBackgroundPainter() {
        if (_bgPainter == null) {
            _bgPainter = new BackgroundPainter(_layout);
        }
        return _bgPainter;
    }
    
    private Painter getPainter() {
        if (_painter == null) {
            if (_explosable == null) {
                _painter = new LayoutStandardPainter(_desktop, _layout, _layout.computeScale(_layout.getFinalDimension(), _desktop.getSize()));
            } else {
                _painter = new LayoutExplosablePainter(_layout, _explosable);
            }
        }
        return _painter;
    }

    private final class ExplosedHandler implements MouseListener, MouseMotionListener, KeyListener {
        public void install(EffectPanel effectPanel) {
            effectPanel.addMouseListener(this);
            effectPanel.addKeyListener(this);
            effectPanel.addMouseMotionListener(this);
        }
        private void dispose() {
            _effectPanel.removeMouseListener(this);
            _effectPanel.removeKeyListener(this);
            _effectPanel.removeMouseMotionListener(this);
        }
        public void mouseClicked(MouseEvent e) {
            Thumbnail selected = _layout.getThumbnailAt(e.getPoint());
            if (selected != null) {
                dispose();
                implose(selected);
            }
            e.consume();
        }

        public void keyReleased(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                dispose();
                implose(null);
            }
            e.consume();
        }

        public void mouseMoved(MouseEvent e) {
            Thumbnail selected = _layout.getThumbnailAt(e.getPoint());
            if (_layout.select(selected)) {
                _effectPanel.repaint();
            }
            e.consume();
        }

        public void mouseEntered(MouseEvent e) {
            e.consume();
        }
        public void mouseExited(MouseEvent e) {
            e.consume();
        }
        public void mousePressed(MouseEvent e) {
            e.consume();
        }
        public void mouseReleased(MouseEvent e) {
            e.consume();
        }
        public void mouseDragged(MouseEvent e) {
            e.consume();
        }
        public void keyPressed(KeyEvent e) {
            e.consume();
        }
        public void keyTyped(KeyEvent e) {
            e.consume();
        }
    }
}
