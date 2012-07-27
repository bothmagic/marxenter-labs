/*
 * $Id: JXMenuBar.java 2617 2008-08-03 18:06:59Z oasuncion $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */

package org.jdesktop.swingx;

import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JMenuBar;

import org.jdesktop.swingx.painter.AbstractPainter;
import org.jdesktop.swingx.painter.Painter;

@SuppressWarnings("serial")
public class JXMenuBar extends JMenuBar {

	private float alpha = 1.0f;
	private JXBusyLabel busyLabel;
	private int y = 0;
	private int right = 0;
	private int left = 0;
	private Color menusBackground;
	private Color menusForeground;
	private Color menusSelectionForeground;

	/**
	 * Specifies the Painter to use for painting the background of this menu
	 * bar. If no painter is specified, the normal painting routine for JMenuBar
	 * is called. Old behavior is also honored for the time being if no
	 * backgroundPainter is specified
	 */
	private Painter<JXMenuBar> backgroundPainter;

	public JXMenuBar() {
		super();
		initPainterSupport();
		initBusyLabel();
		addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				if ("ancestor".equals(evt.getPropertyName())
						|| "border".equals(evt.getPropertyName())) {
					initBusyLabel();
				} else if ("componentOrientation".equals(evt.getPropertyName())) {
					initBusyLabel();
				}

			}
		});
	}

	private void initPainterSupport() {
		if (backgroundPainter == null) {
			backgroundPainter = new AbstractPainter<JXMenuBar>() {
				protected void doPaint(Graphics2D g, JXMenuBar component,
						int width, int height) {
					JXMenuBar.super.paintComponent(g);
				}
			};
		}
	}

	private void initBusyLabel() {
		Dimension d = getPreferredSize();
		Insets bi = getBorder().getBorderInsets(this);
		y = bi.top;
		right = bi.right;
		left = bi.left;
		int h = d.height - y - bi.bottom;
		boolean wasRunning = busyLabel != null && busyLabel.isBusy();
		busyLabel = new JXBusyLabel(new Dimension(h, h)) {
			@Override
			protected void frameChanged() {
				// FYI: repaint after frame change
				JXMenuBar.this.repaint(getBLX(), y, getWidth(), getHeight());
			}
		};
		busyLabel.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				if ("busy".equals(evt.getPropertyName())
						&& !((Boolean) evt.getNewValue()).booleanValue()) {
					// FYI: after reset to default repaint
					JXMenuBar.this.repaint();
				}
			}
		});
		busyLabel.setSize(busyLabel.getPreferredSize());
		busyLabel.setBusy(wasRunning);
	}

	public float getAlpha() {
		return alpha;
	}

	/**
	 * Sets a Painter to use to paint the background of this JXMenuBar. By
	 * default a JXMenuBar already has a single painter installed which draws
	 * the normal background for a menu bar according to the current Look and
	 * Feel. Calling <CODE>setBackgroundPainter</CODE> will replace that
	 * existing painter.
	 * 
	 * @param p
	 *            the new painter
	 * @see #getBackgroundPainter()
	 */
	public void setBackgroundPainter(Painter<JXMenuBar> p) {
		Painter<JXMenuBar> old = getBackgroundPainter();
		backgroundPainter = p;
		firePropertyChange("backgroundPainter", old, getBackgroundPainter());
		repaint();
	}

	/**
	 * Returns the current background painter. The default value of this
	 * property is a painter which draws the normal JPanel background according
	 * to the current look and feel.
	 * 
	 * @return the current painter
	 * @see #setBackgroundPainter(Painter)
	 * @see #isPaintBorderInsets()
	 */
	public Painter<JXMenuBar> getBackgroundPainter() {
		return backgroundPainter;
	}

	public void setAlpha(float alpha) {
		this.alpha = alpha;
		for (Component menu : getComponents())
			((JXMenu) menu).setAlpha(alpha);
	}

	/**
	 * Overridden to provide Painter support. It will call
	 * backgroundPainter.paint() if it is not null, else it will call
	 * super.paintComponent().
	 */
	@Override
	protected void paintComponent(Graphics g) {
		if (backgroundPainter != null) {
			if (isOpaque())
				super.paintComponent(g);

			Graphics2D g2 = (Graphics2D) g.create();

			Insets ins = this.getInsets();
			g2.translate(ins.left, ins.top);
			backgroundPainter.paint(g2, this, this.getWidth() - ins.left
					- ins.right, this.getHeight() - ins.top - ins.bottom);
			g2.translate(getBLX(), y);
			busyLabel.paint(g2);
			g2.dispose();
		} else {
			super.paintComponent(g);
			Graphics2D g2 = (Graphics2D) g.create();
			g2.translate(getBLX(), y);
			busyLabel.paint(g2);
		}
	}

	public JXBusyLabel getBusyIcon() {
		return busyLabel;
	}

	private int getBLX() {
		if (getComponentOrientation()
				.equals(ComponentOrientation.RIGHT_TO_LEFT)) {
			return left;
		} else { // L2R or UNK
			return getWidth() - busyLabel.getWidth() - right;
		}
	}

	/**
	 * To set the background of the submenus
	 * 
	 * @param color
	 *            the background of the submenus
	 */
	public void setMenusBackground(Color color) {
		menusBackground = color;
		for (Component menu : getComponents())
			((JXMenu) menu).setMenusBackground(color);
	}

	/**
	 * To get the background of the submenus
	 * 
	 * @return the background of the submenus
	 */
	public Color getMenusBackground() {
		return menusBackground;
	}

	/**
	 * To get the foreground of the submenus
	 * 
	 * @return the foreground of the submenus
	 */
	public Color getMenusForeground() {
		return menusForeground;
	}

	/**
	 * To get the foreground of the submenus's selected elements
	 * 
	 * @return the foreground of the submenus's selected elements
	 */
	public Color getMenusSelectionForeground() {
		return menusSelectionForeground;
	}

	/**
	 * To set the foreground of the submenus
	 * 
	 * @param color
	 *            the foreground of the submenus
	 */
	public void setMenusForeground(Color color) {
		menusForeground = color;
		for (Component menu : getComponents())
			((JXMenu) menu).setMenusForeground(color);
	}

	/**
	 * To set the foreground of the submenus's selected elements
	 * 
	 * @param color
	 *            the foreground of the submenus's selected elements
	 */
	public void setMenusSelectionForeground(Color color) {
		menusSelectionForeground = color;
		for (Component menu : getComponents())
			((JXMenu) menu).setMenusSelectionForeground(color);
	}

}
