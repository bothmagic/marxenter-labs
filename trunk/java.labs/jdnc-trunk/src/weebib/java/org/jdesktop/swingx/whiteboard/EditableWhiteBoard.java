/*
 * $Id: EditableWhiteBoard.java 830 2006-08-02 00:39:24Z weebib $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */

package org.jdesktop.swingx.whiteboard;

import org.jdesktop.swingx.whiteboard.tools.ITool;

import javax.swing.*;
import javax.swing.event.MouseInputListener;
import java.awt.event.MouseEvent;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.font.TextLayout;
import java.util.*;
import java.util.List;

/**
 * A convenience class which holds a WhiteBoard and a ITool instance. The WhiteBoard is displayed with a vertical and
 * an horizontal Ruler which show the current mouse coordinates.
 */
public class EditableWhiteBoard extends JPanel {
	private WhiteBoard theWhiteBoard;
	private ITool theEditingTool;
	private Ruler theHorizontalRuler;
	private Ruler theVerticalRuler;

//-- Inner classes ------
	private enum Direction{VERTICAL, HORIZONTAL};

	private static class LabeledPoint {
		private String theString;
		private int theLocation;

		public LabeledPoint(String aString, int aLocation) {
			theString = aString;
			theLocation = aLocation;
		}

		public String getString() {
			return theString;
		}

		public int getLocation() {
			return theLocation;
		}
	}

	private static class Ruler extends JPanel {
		private static final Insets TEXT_INSETS = new Insets(2, 2, 2, 2);
		private static final int GAP = 5;
		private Direction theDirection;
		private List<LabeledPoint> theStoredLabeledPoints;
		private LabeledPoint theLabeledPoint;
		private int theMaxLocation;
		private Dimension thePrototype;

		public Ruler(Direction aDirection, int aMaxLocation) {
			super(null);
			setOpaque(true);
			setBackground(Color.DARK_GRAY);
			theStoredLabeledPoints = new LinkedList<LabeledPoint>();
			theLabeledPoint = null;
			thePrototype = null;
			theDirection = aDirection;
			theMaxLocation = aMaxLocation;
		}

		public Dimension getPreferredSize() {
			if (thePrototype == null) {
				computePrototype();
			}
			if (theDirection == Direction.VERTICAL) {
				return new Dimension(thePrototype.width, 0);
			} else {
				return new Dimension(0, thePrototype.height);
			}
		}

		private void computePrototype() {
			Graphics2D g = (Graphics2D)getGraphics();
			double maxWidth = 0.0;
			double maxHeight = 0.0;
			for (int i = 0; i < theMaxLocation; i++) {
				TextLayout tl = new TextLayout("" + i, g.getFont(), g.getFontRenderContext());
				Rectangle2D bounds = tl.getBounds();
				maxWidth = Math.max(maxWidth, bounds.getWidth());
				maxHeight = Math.max(maxHeight, bounds.getHeight());
			}
			g.dispose();
			thePrototype = new Dimension((int)Math.ceil(maxWidth) + TEXT_INSETS.left + TEXT_INSETS.right + GAP,
										 (int)Math.ceil(maxHeight) + TEXT_INSETS.top + TEXT_INSETS.bottom + GAP);
		}

		public void updateMouseLocation(LabeledPoint aLabeledPoint) {
			Rectangle2D oldRect = getRectangle(theLabeledPoint);
			theLabeledPoint = aLabeledPoint;
			Rectangle2D newRect = getRectangle(theLabeledPoint);
			if (oldRect == null && newRect == null) return;
			Rectangle clip;
			if (oldRect == null) {
				clip = newRect.getBounds();
			} else if (newRect == null) {
				clip = oldRect.getBounds();
			} else {
				clip = new Rectangle();
				Rectangle.union(oldRect, newRect, clip);
			}
			clip.grow(2, 2);
			paintImmediately(clip);
		}

		public void storeMouseLocation(LabeledPoint aLabeledPoint) {
			theStoredLabeledPoints.add(aLabeledPoint);
			repaint();
		}

		public void clearStoredLocations() {
			theStoredLabeledPoints.clear();
			repaint();
		}

		protected void paintComponent(Graphics g1d) {
			super.paintComponent(g1d);
			if (theLabeledPoint == null) return;
			Graphics2D g = ((Graphics2D)g1d);
			Color tmpColor = g.getColor();
			Object antiAlias = g.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
			for (LabeledPoint storedLabeledPoint : theStoredLabeledPoints) {
				paintLabel(g, storedLabeledPoint, Color.GRAY);
			}
			paintLabel(g, theLabeledPoint, Color.WHITE);
			g.setColor(tmpColor);
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, antiAlias);
		}

		private void paintLabel(Graphics2D g, LabeledPoint aLabeledPoint, Color aColor) {
			g.setColor(aColor);
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			TextLayout tl = new TextLayout(aLabeledPoint.getString(), g.getFont(), g.getFontRenderContext());
			Rectangle2D bounds = tl.getBounds();
			double width = bounds.getWidth() + TEXT_INSETS.left + TEXT_INSETS.right;
			double height = bounds.getHeight() + TEXT_INSETS.top + TEXT_INSETS.bottom;
			if (theDirection == Direction.VERTICAL) {
				int location = aLabeledPoint.getLocation();
				double x = getWidth() - width - GAP;
				double y = location - height / 2.0;
				Rectangle2D frame = new Rectangle2D.Double(x, y, width, height);
				g.draw(frame);
				x += TEXT_INSETS.left;
				y += TEXT_INSETS.top + bounds.getHeight();
				tl.draw(g, (float)x, (float)y);
				g.drawLine(getWidth() - GAP, location, getWidth(), location);
			} else {
				int location = aLabeledPoint.getLocation();
				double x = location - width / 2.0;
				double y = getHeight() - height - GAP;
				Rectangle2D frame = new Rectangle2D.Double(x, y, width, height);
				g.draw(frame);
				x += TEXT_INSETS.left;
				y += TEXT_INSETS.top + bounds.getHeight();
				tl.draw(g, (float)x, (float)y);
				g.drawLine(location, getHeight() - GAP, location, getHeight());
			}
		}

		private Rectangle2D getRectangle(LabeledPoint aLabeledPoint) {
			if (aLabeledPoint == null) return null;
			Graphics2D g = (Graphics2D)getGraphics();
			TextLayout tl = new TextLayout(aLabeledPoint.getString(), g.getFont(), g.getFontRenderContext());
			Rectangle2D bounds = tl.getBounds();
			double width = bounds.getWidth() + TEXT_INSETS.left + TEXT_INSETS.right;
			double height = bounds.getHeight() + TEXT_INSETS.top + TEXT_INSETS.bottom;
			double x;
			double y;
			if (theDirection == Direction.VERTICAL) {
				width += GAP;
				x = (double)getWidth() - width;
				y = (double)aLabeledPoint.getLocation() - height / 2.0;
			} else {
				height += GAP;
				x = (double)aLabeledPoint.getLocation() - width / 2.0;
				y = (double)getHeight() - height;
			}
			g.dispose();
			return new Rectangle2D.Double(x, y, width, height);
		}
	}

	private class WhiteBoardLayoutManager implements LayoutManager {
		public void addLayoutComponent(String name, Component comp) {}

		public void removeLayoutComponent(Component comp) {}

		public Dimension preferredLayoutSize(Container parent) {
			synchronized(EditableWhiteBoard.this.getTreeLock()) {
				Insets insets = getInsets();
				Dimension whiteBoardPref = theWhiteBoard.getPreferredSize();
				Dimension hPref = theHorizontalRuler.getPreferredSize();
				Dimension vPref = theVerticalRuler.getPreferredSize();
				return new Dimension(whiteBoardPref.width + vPref.width + insets.left + insets.right,
									 whiteBoardPref.height + hPref.height + insets.top + insets.bottom);
			}
		}

		public Dimension minimumLayoutSize(Container parent) {
			return preferredLayoutSize(parent);
		}

		public void layoutContainer(Container parent) {
			synchronized(EditableWhiteBoard.this.getTreeLock()) {
				Insets insets = getInsets();
				Dimension whiteBoardPref = theWhiteBoard.getPreferredSize();
				Dimension hPref = theHorizontalRuler.getPreferredSize();
				Dimension vPref = theVerticalRuler.getPreferredSize();
				int x = insets.left;
				int y = insets.top;
				theHorizontalRuler.setBounds(x + vPref.width, y, whiteBoardPref.width, hPref.height);
				theVerticalRuler.setBounds(x, y + hPref.height, vPref.width, whiteBoardPref.height);
				x += vPref.width;
				y += hPref.height;
				theWhiteBoard.setBounds(x, y, whiteBoardPref.width, whiteBoardPref.height);
			}
		}
	}

	private class LocalMouseInputListener implements MouseInputListener {
		public void mouseClicked(MouseEvent e) {
			if (theEditingTool == null) return;
			theEditingTool.mouseClicked(translate(e));
		}

		public void mousePressed(MouseEvent e) {
			if (theEditingTool == null) return;
			theEditingTool.mousePressed(translate(e));
		}

		public void mouseReleased(MouseEvent e) {
			if (theEditingTool == null) return;
			theEditingTool.mouseReleased(translate(e));
		}

		public void mouseEntered(MouseEvent e) {
			if (theEditingTool == null) return;
			theEditingTool.mouseEntered(translate(e));
		}

		public void mouseExited(MouseEvent e) {
			updateRulers(null);
			if (theEditingTool == null) return;
			theEditingTool.mouseExited(translate(e));
		}

		public void mouseDragged(MouseEvent e) {
			updateRulers(translate(e).getPoint());
			if (theEditingTool == null) return;
			theEditingTool.mouseDragged(translate(e));
		}

		public void mouseMoved(MouseEvent e) {
			updateRulers(translate(e).getPoint());
			if (theEditingTool == null) return;
			theEditingTool.mouseMoved(translate(e));
		}

		private MouseEvent translate(MouseEvent e) {
			Insets insets = theWhiteBoard.getInsets();
			return new MouseEvent((Component)e.getSource(),
								  e.getID(),
								  e.getWhen(),
								  e.getModifiers(),
								  e.getX() - insets.left,
								  e.getY() - insets.right,
								  e.getClickCount(),
								  e.isPopupTrigger(),
								  e.getButton());
		}
	}

//-- Constructor ------
	/**
	 * Constructs an EditableWhiteBoard whose WhiteBoard will have the given size.
	 * @param aSize
	 */
	public EditableWhiteBoard(Dimension aSize) {
		super(null);
		setLayout(new WhiteBoardLayoutManager());
		theWhiteBoard = new WhiteBoard(aSize);
		theVerticalRuler = new Ruler(Direction.VERTICAL, aSize.height);
		add(theVerticalRuler);
		theHorizontalRuler = new Ruler(Direction.HORIZONTAL, aSize.width);
		add(theHorizontalRuler);
		add(theWhiteBoard);
		LocalMouseInputListener lmil = new LocalMouseInputListener();
		theWhiteBoard.addMouseListener(lmil);
		theWhiteBoard.addMouseMotionListener(lmil);
	}

//-- Public methods ------
	/**
	 * Sets the current editing tool.
	 * @param aTool
	 */
	public void setEditingTool(ITool aTool) {
		if (theEditingTool != null) {
			theEditingTool.setEditableWhiteBoard(null);
			theWhiteBoard.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}
		theEditingTool = aTool;
		if (theEditingTool != null) {
			theEditingTool.setEditableWhiteBoard(this);
			theWhiteBoard.setCursor(theEditingTool.getCursor());
		}
	}

	/**
	 * Method called by the current editing tool which causes an immediate repaint of the region determined by
	 * aRectangle.
	 * @param aTool
	 * @param aRectangle
	 */
	public void onToolUpdate(ITool aTool, Rectangle aRectangle) {
		if (aTool != theEditingTool) return;
		Point p = theWhiteBoard.getLocation();
		Insets insets = theWhiteBoard.getInsets();
		aRectangle.x += insets.left + p.x;
		aRectangle.y += insets.top + p.y;
		paintImmediately(aRectangle);
	}

	/**
	 * Clears the underlying WhiteBoard.
	 */
	public void clearWhiteBoard() {
		theWhiteBoard.clear();
	}

	/**
	 * Adds the given IPaintableItem to the underlying WhiteBoard.
	 * @param aPaintableItem
	 */
	public void addPaintableItem(IPaintableItem aPaintableItem) {
		theWhiteBoard.addPaintableItem(aPaintableItem);
	}

	/**
	 * Method called by the current editing tool to store the given location on the Rulers
	 * @param aPoint
	 */
	public void storeMouseLocation(Point aPoint) {
		Insets insets = theWhiteBoard.getInsets();
		theHorizontalRuler.storeMouseLocation(new LabeledPoint("" + aPoint.x, aPoint.x + insets.left));
		theVerticalRuler.storeMouseLocation(new LabeledPoint("" + aPoint.y, aPoint.y + insets.top));
	}

	/**
	 * Clears all stored locations within the Rulers.
	 */
	public void clearStoredLocations() {
		theHorizontalRuler.clearStoredLocations();
		theVerticalRuler.clearStoredLocations();
	}

//-- JComponent overriden methods ------
	protected void paintChildren(Graphics g) {
		super.paintChildren(g);
		Graphics2D g2D = (Graphics2D)g;
		if (theEditingTool != null) {
			Shape tmpClip = g2D.getClip();
			Rectangle tmpClipBounds = tmpClip.getBounds();
			Rectangle whiteBoardBounds = theWhiteBoard.getBounds();
			Insets insets = theWhiteBoard.getInsets();
			whiteBoardBounds.x += insets.left;
			whiteBoardBounds.y += insets.top;
			whiteBoardBounds.width -= insets.left + insets.right;
			whiteBoardBounds.height -= insets.top - insets.bottom;
			Rectangle clip = new Rectangle();
			Rectangle.intersect(tmpClipBounds, whiteBoardBounds, clip);
			g2D.setClip(clip);
			Point p = theWhiteBoard.getLocation();
			int deltaX = p.x + insets.left;
			int deltaY = p.y + insets.right;
			g.translate(deltaX, deltaY);
			theEditingTool.paint(g2D);
			g.translate(-deltaX, -deltaY);
			g2D.setClip(tmpClip);
		}
	}

//-- Private methods ------
	private void updateRulers(Point aPoint) {
		if (aPoint == null) {
			theHorizontalRuler.updateMouseLocation(null);
			theVerticalRuler.updateMouseLocation(null);
		} else {
			Insets insets = theWhiteBoard.getInsets();
			theHorizontalRuler.updateMouseLocation(new LabeledPoint("" + aPoint.x, aPoint.x + insets.left));
			theVerticalRuler.updateMouseLocation(new LabeledPoint("" + aPoint.y, aPoint.y + insets.top));
		}
	}
}
