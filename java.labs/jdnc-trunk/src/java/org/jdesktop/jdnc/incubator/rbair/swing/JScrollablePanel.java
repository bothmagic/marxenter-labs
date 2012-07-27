/*
 * $Id: JScrollablePanel.java 148 2004-10-29 20:43:46Z rbair $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.jdnc.incubator.rbair.swing;

import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.Rectangle;

import javax.swing.JPanel;
import javax.swing.Scrollable;

/**
 * @author Richard Bair
 */
public class JScrollablePanel extends JPanel implements Scrollable {
	private boolean scrollableTracksViewportHeight;
	private boolean scrollableTracksViewportWidth;
	/**
	 * 
	 */
	public JScrollablePanel() {
		super();
	}

	/**
	 * @param isDoubleBuffered
	 */
	public JScrollablePanel(boolean isDoubleBuffered) {
		super(isDoubleBuffered);
	}

	/**
	 * @param layout
	 */
	public JScrollablePanel(LayoutManager layout) {
		super(layout);
	}

	/**
	 * @param layout
	 * @param isDoubleBuffered
	 */
	public JScrollablePanel(LayoutManager layout, boolean isDoubleBuffered) {
		super(layout, isDoubleBuffered);
	}

	/* (non-Javadoc)
	 * @see javax.swing.Scrollable#getScrollableTracksViewportHeight()
	 */
	public boolean getScrollableTracksViewportHeight() {
		return scrollableTracksViewportHeight;
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.Scrollable#getScrollableTracksViewportWidth()
	 */
	public boolean getScrollableTracksViewportWidth() {
		return scrollableTracksViewportWidth;
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.Scrollable#getPreferredScrollableViewportSize()
	 */
	public Dimension getPreferredScrollableViewportSize() {
		return getPreferredSize();
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.Scrollable#getScrollableBlockIncrement(java.awt.Rectangle, int, int)
	 */
	public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
		return 10;
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.Scrollable#getScrollableUnitIncrement(java.awt.Rectangle, int, int)
	 */
	public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
		return 10;
	}
	/**
	 * @param scrollableTracksViewportHeight The scrollableTracksViewportHeight to set.
	 */
	public void setScrollableTracksViewportHeight(boolean scrollableTracksViewportHeight) {
		this.scrollableTracksViewportHeight = scrollableTracksViewportHeight;
	}
	/**
	 * @param scrollableTracksViewportWidth The scrollableTracksViewportWidth to set.
	 */
	public void setScrollableTracksViewportWidth(boolean scrollableTracksViewportWidth) {
		this.scrollableTracksViewportWidth = scrollableTracksViewportWidth;
	}
}
