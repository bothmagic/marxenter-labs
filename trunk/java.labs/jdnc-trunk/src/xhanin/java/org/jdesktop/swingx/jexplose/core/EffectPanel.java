/*
 * $Id: EffectPanel.java 985 2006-12-22 19:40:13Z xhanin $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.jexplose.core;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import javax.swing.JComponent;
import javax.swing.JRootPane;
import javax.swing.SwingUtilities;

/**
 * This class is able to show effects on top of a component.
 * To use it, show it, it will then hide 
 * component true ui and be ready for effect display.
 * Then you can dispose it whenever you want for
 * restoring component true UI. No mouse interaction
 * with the component is possible when it is shown.
 */
public class EffectPanel extends JComponent {
    private JComponent _comp;
    private Component _glassPaneBackup;
    private JRootPane _rootPaneBackup;
    private List _painters = new ArrayList();
    
    //  setting this to true is very time consuming for painting
    private boolean _paintBackgroundWhenNotOpaque = false;
    private Thread _thread;  

    public void install(JComponent comp) {
        setBackground(comp.getBackground());
        _comp = comp;
        _rootPaneBackup = comp.getRootPane();
        _glassPaneBackup = _rootPaneBackup.getGlassPane();
        _rootPaneBackup.setGlassPane(this);
        setVisible(true);
        requestFocus();
        _thread = new Thread() {
                    public void run() {
                        while (true) {
                            repaint();
                            try {
                                sleep(300);
                            } catch (InterruptedException e) {
                                return;
                            }
                        }
                    }
                };
        _thread.start(); // used to repaint periodically the effect panel, to update its state
    }
    
    public void dispose() {
        _thread.interrupt();
        try {
            Runnable disposeRunnable = new Runnable() {
                public void run() {
                    boolean visible = _glassPaneBackup.isVisible();
                    _rootPaneBackup.setGlassPane(_glassPaneBackup);
                    setVisible(false);
                    // restores glass pane visible state since jrootpane container force it
                    _glassPaneBackup.setVisible(visible);
                    _rootPaneBackup.repaint();
                }
            };
            if (SwingUtilities.isEventDispatchThread()) {
                disposeRunnable.run();
            } else {
                SwingUtilities.invokeAndWait(disposeRunnable);
            }
        } catch (InterruptedException e) {
        } catch (InvocationTargetException e) {
        }
    }
    
    public void paint(Graphics g) {
//        super.paint(g);
        if (isOpaque() || _paintBackgroundWhenNotOpaque) {
            Color c = g.getColor();
            g.setColor(getBackground());
            g.fillRect(0,0,getWidth(), getHeight());
            g.setColor(c);
        }
        List painters;
        synchronized(this) {
            painters = new ArrayList(_painters);
        }
        for (ListIterator it = painters.listIterator(); it.hasNext();) {
            Painter painter = (Painter)it.next();
            painter.paint(g.create());
        }
        
    }
    public synchronized void addPainter(Painter painter) {
        painter.prepare(this);
        _painters.add(painter);
        repaint();
    }

    public synchronized void removePainter(Painter painter) {
        if (_painters.remove(painter)) {
            painter.dispose();
        }
        repaint();
    }

    public synchronized void setPainter(Painter painter) {
        removeAllPainters();
        if (painter != null) {
            painter.prepare(this);
            _painters.add(painter);
        }
    }

    public void removeAllPainters() {
        for (ListIterator it = _painters.listIterator(); it.hasNext();) {
            Painter p = (Painter)it.next();
            p.dispose();
            it.remove();
        }
    }

    public void bePatient() {
        setPainter(new WaitPainter());
    }

}
