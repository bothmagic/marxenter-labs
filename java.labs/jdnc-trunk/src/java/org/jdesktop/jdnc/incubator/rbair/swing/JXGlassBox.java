/*
 * $Id: JXGlassBox.java 46 2004-09-08 17:33:01Z rbair $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */

package org.jdesktop.jdnc.incubator.rbair.swing;

import java.applet.Applet;
import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.border.CompoundBorder;
import javax.swing.border.LineBorder;


/**
 * Component used to display transluscent user-interface content.
 * This component and all of its content will be displayed with the specified
 * &quot;alpha&quot; transluscency property value.  When this component is made visible,
 * it's content will fade in until the alpha transluscency level is reached.
 * <p>
 * If the glassbox's &quot;dismissOnClick&quot; property is <code>true</code>
 * (the default) then the glassbox will be made invisible when the user
 * clicks on it.</p>
 * <p>
 * This component is particularly useful for displaying transient messages
 * on the glasspane.</p>
 *
 * @author Amy Fowler
 * @version 1.0
 */

public class JXGlassBox extends JPanel {
    private static final int SHOW_DELAY = 30; // ms
    private static final int TIMER_INCREMENT = 5; // ms

    private float alphaStart = 0.01f;
    private float alphaEnd = 0.8f;

    private Timer animateTimer;
    private float animateAlpha = alphaStart;
    private float alphaIncrement = 0.02f;

    private boolean dismissOnClick = false;
    private MouseAdapter dismissListener = null;

    private transient Insets insets = new Insets(0,0,0,0); //scatch

    public JXGlassBox() {
        setOpaque(false);
        setBackground(Color.white);
        setDismissOnClick(true);

        animateTimer = new Timer(TIMER_INCREMENT, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                animateAlpha += alphaIncrement;
                paintImmediately(0, 0, getWidth(), getHeight());
            }
        });
    }

    public JXGlassBox(float alpha) {
        this();
        setAlpha(alpha);
    }

    public void setAlpha(float alpha) {
        this.alphaEnd = alpha;
        this.alphaIncrement = (alphaEnd - alphaStart)/(SHOW_DELAY/TIMER_INCREMENT);
    }

    public float getAlpha() {
        return alphaEnd;
    }

    public void setDismissOnClick(boolean dismissOnClick) {
        boolean oldDismissOnClick = this.dismissOnClick;
        this.dismissOnClick = dismissOnClick;
        if (dismissOnClick && !oldDismissOnClick) {
            if (dismissListener == null) {
                dismissListener = new MouseAdapter() {
                    public void mouseClicked(MouseEvent e) {
                        JComponent glassBox = JXGlassBox.this;
                        JComponent parent = (JComponent) glassBox.getParent();
                        Container toplevel = parent.getTopLevelAncestor();
                        parent.remove(glassBox);
                        toplevel.validate();
                        toplevel.repaint();
                    }
                };
            }
            addMouseListener(dismissListener);
        }
        else if (!dismissOnClick && oldDismissOnClick) {
            removeMouseListener(dismissListener);
        }
    }

    public void paint(Graphics g) {
        getInsets(insets);
        Graphics2D g2d = (Graphics2D)g;
        Composite oldComp = g2d.getComposite();
        Composite alphaComp = AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
            Math.min(alphaEnd, animateAlpha));
        g2d.setComposite(alphaComp);
        g2d.setColor(getBackground());
        g2d.fillRect(insets.left, insets.top,
                     getWidth() - insets.left - insets.right,
                     getHeight() - insets.top - insets.bottom);
        super.paint(g2d);
        g2d.setComposite(oldComp);

        if (!animateTimer.isRunning() && animateAlpha < alphaEnd ) {
            animateTimer.start();
        }
        if (animateTimer.isRunning() && animateAlpha >= alphaEnd) {
            animateTimer.stop();
        }
    }

    public void setVisible(boolean visible) {
        animateAlpha = alphaStart;
        super.setVisible(visible);
    }

    private Container getTopLevel() {
        Container p = getParent();
        while (p != null && !(p instanceof Window || p instanceof Applet)) {
            p = p.getParent();
        }
        return p;
    }

    public void showOnGlassPane(Container glassPane, Component component,
                                int componentX, int componentY, int positionHint) {
        Dimension boxPrefSize = getPreferredSize();
        Dimension glassSize = glassPane.getSize();
        Rectangle compRect = component.getBounds();
        int boxX = 0;
        int boxY = 0;
        int boxWidth = Math.min(boxPrefSize.width, glassSize.width);
        int boxHeight = Math.min(boxPrefSize.height, glassSize.height);

        Point compLocation = SwingUtilities.convertPoint(component.getParent(),
                                                compRect.x, compRect.y,
                                                glassPane);

        if (positionHint == SwingConstants.TOP) {
            if (compLocation.x + componentX + boxWidth <= glassSize.width) {
                boxX = compLocation.x + componentX;
            } else {
                boxX = glassSize.width - boxWidth;
            }
            boxY = compLocation.y - boxHeight;
            if (boxY < 0) {
                if (compLocation.y + compRect.height <= glassSize.height) {
                    boxY = compLocation.y + compRect.height;
                }
                else {
                    boxY = 0;
                }
            }
        }

        glassPane.setLayout(null);
        setBounds(boxX, boxY, boxWidth, boxHeight);
        glassPane.add(this);
        glassPane.setVisible(true);

        Container topLevel = getTopLevel();
        topLevel.validate();
        topLevel.repaint();

    }

    public void showOnGlassPane(Container glassPane, int originX, int originY) {
        Dimension boxPrefSize = getPreferredSize();
        Dimension glassSize = glassPane.getSize();
        int boxX = 0;
        int boxY = 0;
        int boxWidth = 0;
        int boxHeight = 0;

        boxWidth = Math.min(boxPrefSize.width, glassSize.width);
        boxHeight = Math.min(boxPrefSize.height, glassSize.height);

        if (originY - boxHeight >= 0) {
            boxY = originY - boxHeight;
        } else if (originY + boxHeight <= glassSize.height) {
            boxY = originY;
        } else {
            boxY = glassSize.height - boxHeight;
        }

        if (originX + boxWidth <= glassSize.width) {
            boxX = originX;
        } else if (originX >= boxWidth) {
            boxX = originX - boxWidth;
        } else {
            boxX = glassSize.width - boxWidth;
        }

        glassPane.setLayout(null);
        setBounds(boxX, boxY, boxWidth, boxHeight);
        glassPane.add(this);
        glassPane.setVisible(true);

        Container topLevel = getTopLevel();
        topLevel.validate();
        topLevel.repaint();
    }

}