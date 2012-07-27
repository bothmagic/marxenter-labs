/*
 * $Id: ExploseDemo.java 985 2006-12-22 19:40:13Z xhanin $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.jexplose.demo;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.beans.PropertyVetoException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Method;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.UIManager;

import org.jdesktop.swingx.jexplose.ExplosableDesktop;
import org.jdesktop.swingx.jexplose.JExplose;
import org.jdesktop.swingx.jexplose.JExploseUtils;
import org.jdesktop.swingx.jexplose.animation.StandardAnimationStrategy;
import org.jdesktop.swingx.jexplose.layout.StandardLayoutStrategy;

import com.jgoodies.animation.AnimationFunctions;
import com.jgoodies.animation.Animator;
import com.jgoodies.animation.animations.FanAnimation;
import com.jgoodies.animation.components.FanComponent;
import com.smardec.mousegestures.MouseGestures;
import com.smardec.mousegestures.MouseGesturesListener;



/**
 * JExplose Demo Application
 */
public class ExploseDemo {
    private static final class ExploseUpdater extends FocusAdapter {
        public void focusLost(FocusEvent e) {
            updateExploseConfiguration();
        }
        private void updateExploseConfiguration() {
            int s = Integer.valueOf(steps.getText()).intValue();
            int p = Integer.valueOf(period.getText()).intValue();
            System.out.println("using animation settings: steps="+s+" period="+p);
            JExplose.setInstance(new JExplose(new StandardLayoutStrategy(), new StandardAnimationStrategy(s, p)));
        }
    }

    static int i = 0;
    
    private static JTextField steps;
    private static JTextField period;
    
    private static void startDemo() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e1) {
        }
        
//        RepaintManager.setCurrentManager(new ThreadCheckingRepaintManager());
        final JFrame frame = new JFrame("JExplose demo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        final ExplosableDesktop desktop = new ExplosableDesktop();
        JPanel content = new JPanel(new BorderLayout());
        content.add(new JScrollPane(desktop), BorderLayout.CENTER);
        JToolBar toolBar = new JToolBar();
        content.add(toolBar, BorderLayout.NORTH);
        frame.setContentPane(content);
        
        steps = new JTextField(2);
        steps.setMaximumSize(new Dimension(50,50));
        steps.setText("7");
        steps.addFocusListener(new ExploseUpdater());
        period = new JTextField(2);
        period.setMaximumSize(new Dimension(50,50));
        period.setText("50");
        period.addFocusListener(new ExploseUpdater());
        toolBar.add(new AbstractAction("Create frames (6)") {
            public void actionPerformed(ActionEvent e) {
                int dx = i * 40;
                int dy = i * 25;
                newIconInternalFrame(desktop, "Jayasoft Logo "+i, "logo.png", 15+dx, 15+dy);
                newInternalFrame(desktop, "TreeView "+i, 60+dx,80+dy, createTree());
                newIconInternalFrame(desktop, "JExplose "+i, "jexplose.jpg", 120+dx, 450+dy);
                newIconInternalFrame(desktop, "UIState "+i, "uistate.jpg", 500+dx, 350+dy);
                newIconInternalFrame(desktop, "Ivy "+i, "ivy.jpg", 300+dx, 0+dy);
                newIconInternalFrame(desktop, "UM Webdesign "+i, "um-webdesign.jpg", 450+dx, 50+dy);
                i++;
            }
        });        
        toolBar.add(new AbstractAction("Create a lot of frames (11)") {
            public void actionPerformed(ActionEvent e) {
                int dx = i * 40;
                int dy = i * 25;
                newIconInternalFrame(desktop, "Eclipse "+i, "eclipse.png", 100+dx, 15+dy);
                newIconInternalFrame(desktop, "Jayasoft Logo "+i, "logo.png", 15+dx, 15+dy);
                newIconInternalFrame(desktop, "JBazaar "+i, "jbazaar.jpg", 15+dx, 200+dy);
                newIconInternalFrame(desktop, "Jayasoft "+i, "texte.png", 300+dx, 100+dy);
                newIconInternalFrame(desktop, "Calendar "+i, "calendar.jpg", 150+dx, 150+dy);
                newIconInternalFrame(desktop, "Keyboard "+i, "keyboard.jpg", 200+dx, 50+dy);
                newIconInternalFrame(desktop, "UIState "+i, "uistate.jpg", 500+dx, 350+dy);
                newIconInternalFrame(desktop, "JExplose "+i, "jexplose.jpg", 120+dx, 450+dy);
                newIconInternalFrame(desktop, "UM Webdesign "+i, "um-webdesign.jpg", 450+dx, 50+dy);
                newIconInternalFrame(desktop, "Ivy "+i, "ivy.jpg", 300+dx, 0+dy);
                newInternalFrame(desktop, "TreeView "+i, 60+dx,80+dy, createTree());
                i++;
            }
        });
        toolBar.add(new AbstractAction("Create animated frame") {
            public void actionPerformed(ActionEvent e) {
                JInternalFrame[] frames = desktop.getAllFrames();
                for (int i = 0; i < frames.length; i++) {
                    if (frames[i].getTitle().equals("Anim")) {
                        return;
                    }
                }
                newInternalFrame(desktop, "Anim", 600, 150, createFan(i));
            }
        });
        toolBar.add(new AbstractAction("Close all") {
            public void actionPerformed(ActionEvent e) {
                JInternalFrame[]all = desktop.getAllFrames();
                for (int i = 0; i < all.length; i++) {
                    JInternalFrame frame2 = all[i];
                    desktop.getDesktopManager().closeFrame(frame2);
                }
            }
        });  

        toolBar.addSeparator();
        //Hot key install
        JExploseUtils.installThunderHotKey(desktop, KeyEvent.VK_F11);
        JExploseUtils.installLightningHotKey(desktop.getExplosable(), KeyEvent.VK_F12);
        
        //Actions
        Action thunderAction = JExploseUtils.getThunderAction(desktop);        
        thunderAction.putValue(Action.SHORT_DESCRIPTION, "<html>This version of JExplose has good performance in most case and it is not <B>intrusive</b>. <br>You can use it with JDesktopPane or any of its subclass");
        thunderAction.putValue(Action.NAME,"JExplose Thunder");
        
        Action light = JExploseUtils.getLightningAction(desktop.getExplosable());
        light.putValue(Action.NAME,"JExplose Lightning");
        light.putValue(Action.SHORT_DESCRIPTION, "<html>This version of JExplose is <B>very fast</B> but your JDesktopPane subclass need to delegate paint method to jexplose");
        JButton thunderBtn = toolBar.add(thunderAction);
        thunderBtn.setHorizontalTextPosition(SwingConstants.RIGHT);
        thunderBtn.setText("Thunder");
        JButton lightBtn = toolBar.add(light);
        lightBtn.setHorizontalTextPosition(SwingConstants.RIGHT);
        lightBtn.setText("Lightning");
        toolBar.addSeparator();
        toolBar.add(new JLabel("Steps: "));
        toolBar.add(steps);
        toolBar.add(new JLabel("Period: "));
        toolBar.add(period);
        toolBar.addSeparator();
        toolBar.add(new AbstractAction("Help") {
            public void actionPerformed(ActionEvent e) {
                showHelp(desktop);
            }
        });  
        MouseGestures mouseGestures = new MouseGestures();
        mouseGestures.addMouseGesturesListener(new MouseGesturesListener() {
            public void processGesture(String gesture) {
                if ("R".equals(gesture)) {
                    desktop.requestFocus(); // to ensure focus lost on formatted text fields
                    JExplose.getInstance().explose(desktop);
                } else {
                    desktop.requestFocus(); // to ensure focus lost on formatted text fields
                    JExplose.getInstance().explose(desktop.getExplosable());
                }
            }

            public void gestureMovementRecognized(String gesture) {
            }
        });
        mouseGestures.start();
        showHelp(desktop);
        frame.pack();
        //JDK1.3 compliant way to maximize frame
        try {
            Method setExtendedStateMethod = JFrame.class.getMethod("setExtendedState", new Class[]{int.class});
            setExtendedStateMethod.invoke(frame, new Object[]{new Integer(6)});// JFrame.MAXIMIZED_BOTH
        } catch (Exception e2) {
            // method not found use default way to make frame big
            frame.setBounds(0,0, Toolkit.getDefaultToolkit().getScreenSize().width, Toolkit.getDefaultToolkit().getScreenSize().height); 
        }
        frame.show();        
    }

    /**
     * @param desktop
     */
    private static void showHelp(final ExplosableDesktop desktop) {
        newInternalFrame(desktop, "About this demo",  30, 30, createAboutComponent());
    }

    /**
     * @return
     */
    protected static JComponent createAboutComponent() {
        String doc ="<html>"+"<h1><center>Demo instructions</center></h1>"+
                    "<h2><u>Overview </u>:</h2>"+
                    "JExpose is provided with two implementations."+
                    "<ul><li><b><font color='blue'>Thunder</font></b></li>"+
                    "This easiest way to use JExplose. You can install it on any JDesktopPane or subclass"+
                    "<li><b><font color='blue'>Lightning</font></b></li>"+
                    "This is the fastest implementation, work best but requires to use a particular JDesktopPane subclass."+
                    "</ul>"+
                    "<h2><u>Usage </u>:</h2>"+
                    "First create some internal frame by using one of the button on menubar.<br>"+
                    "Then call JExplose by one of the following methods:"+
                    "<ul><li>Menubar button</li><li>Mouse gesture, right click + right move(Thunder) or left move(Lightning)</li><li>Press F11 (Thunder) or F12 (Lightning) key.</li></ul>" +
                    "Finally you can also play with JExplose parameters:"+
                    "<ul><li>Steps: the number of steps between desktop and jexplose view.</li>" +
                    "<li>Period: The time in millisecond between two steps.</li></ul>" +
                    "<b>Note:</b> that change are read on textfield focus lost!<br><br>"+
                    "This demo makes use of <b>jgoodies animation</b> (see http://www.jgoodies.com/)<br>"+
                    "and <b>smardec mouse gestures</b> (see http://www.smardec.com/)"+
                    "";
        JLabel label = new JLabel(doc);
        label.setOpaque(true);
        return label;
    }

    private static JTree createTree() {
        JTree tree = new JTree();
        for (int i = 0; i < tree.getRowCount(); i++) {
            tree.expandRow(i);
        }
        return tree;
    }
    private static JComponent createAnimatedComponent() {
        final JTextArea tarea = new JTextArea();
        final JScrollPane scrollPane = new JScrollPane(tarea);
        tarea.setAutoscrolls(true);
        tarea.setPreferredSize(new Dimension(300,300));
        final Reader reader = new InputStreamReader(ExploseDemo.class.getResourceAsStream("story.txt"));
        new Timer(300, new ActionListener() {
            StringBuffer buf = new StringBuffer();
            public void actionPerformed(ActionEvent e) {
                try {
                    buf.setLength(0);
                    for (int i = 0; i < 5; i++) {
                        int c = reader.read();
                        if (c == -1) {
                            ((Timer)e.getSource()).stop();
                            break;
                        } else {
                            buf.append((char)c);
                        }
                    }
                    tarea.setText(tarea.getText() + buf);
                } catch (IOException ex) {
                }
            }
        }).start();
        return scrollPane;
    }
    private static JComponent createFan(int i2) {
        FanComponent comp = new FanComponent(5, Color.red);
        Object[] angles = new Object[100];
        for (int i = 0; i < angles.length; i+=2) {
            angles[i] = new Float(0);
            angles[i+1] = new Float(Math.PI * 2.0f);
        }
        Animator animator = new Animator(new FanAnimation(comp, 500000, AnimationFunctions.linear(500000, angles)), 30);
        animator.start();
        comp.setPreferredSize(new Dimension(300,300));
        return comp;
    }
    
    private static void newIconInternalFrame(final JDesktopPane desktop, String title, String iconFileName, int x, int y) {
        newInternalFrame(desktop, title, x, y, new JLabel(new ImageIcon(ExploseDemo.class.getResource(iconFileName))));
    }

    private static void newInternalFrame(final JDesktopPane desktop, String title, int x, int y, JComponent content) {
        JInternalFrame internalFrame = new JInternalFrame(title, true, true, true, true);
        internalFrame.getContentPane().add(content);
        desktop.add(internalFrame);
        internalFrame.setVisible(true);
        internalFrame.setLocation(x, y);
        try {
            internalFrame.pack();
            internalFrame.setSelected(true);
        } catch (PropertyVetoException ex) {
        }
    }

    public static void main(String[] args) throws IOException {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                startDemo();
            }
        });
    }

}
