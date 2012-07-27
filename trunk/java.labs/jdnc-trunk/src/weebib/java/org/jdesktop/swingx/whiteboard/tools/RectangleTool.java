/*
 * $Id: RectangleTool.java 830 2006-08-02 00:39:24Z weebib $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */

package org.jdesktop.swingx.whiteboard.tools;

import org.jdesktop.swingx.whiteboard.ShapeItem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.LinkedList;
import java.util.List;

/**
 * A tool which draws Rectangles.
 */
public class RectangleTool extends AShapeTool {
	private Point theStartPoint;
	private Point theEndPoint;

	public RectangleTool() {
		super("Rectangle");
	}

	public void mousePressed(MouseEvent e) {
		if (theEditableWhiteBoard == null) return;
		theStartPoint = e.getPoint();
		theEditableWhiteBoard.storeMouseLocation(theStartPoint);
		theEndPoint = e.getPoint();
	}

	public void mouseReleased(MouseEvent e) {
		if (theEditableWhiteBoard == null) return;
		if (theStartPoint == null) return;
		if (theEndPoint == null) return;
		Rectangle rect = new Rectangle();
		rect.setFrameFromDiagonal(theStartPoint, theEndPoint);
		reset();
		theEditableWhiteBoard.addPaintableItem(new ShapeItem(rect,
															 getForegroundColor(),
															 getBackgroundColor(),
															 getThickness(),
															 isFilled()));
	}

	public void mouseDragged(MouseEvent e) {
		if (theEditableWhiteBoard == null) return;
		if (theStartPoint == null) return;
		if (theEndPoint == null) return;
		Point anEndPoint = e.getPoint();
		Rectangle oldRect = new Rectangle();
		oldRect.setFrameFromDiagonal(theStartPoint, theEndPoint);
		boolean isSameQuadrant = isSameQuadrant(anEndPoint);
		theEndPoint = anEndPoint;
		Rectangle newRect = new Rectangle();
		newRect.setFrameFromDiagonal(theStartPoint, theEndPoint);
		List<Rectangle> dirtyRegions = new LinkedList<Rectangle>();
		if (isSameQuadrant) {
			Rectangle[] newColored = SwingUtilities.computeDifference(newRect, oldRect);
			Rectangle[] oldColored = SwingUtilities.computeDifference(oldRect, newRect);
			for (Rectangle rectangle : oldColored) {
				rectangle.grow(10, 10);
				dirtyRegions.add(rectangle);
			}
			for (Rectangle rectangle : newColored) {
				rectangle.grow(10, 10);
				dirtyRegions.add(rectangle);
			}
		} else {
			oldRect.grow(10, 10);
			dirtyRegions.add(oldRect);
			newRect.grow(10, 10);
			dirtyRegions.add(newRect);
		}
		for (Rectangle rectangle : dirtyRegions) {
			theEditableWhiteBoard.onToolUpdate(this, rectangle);
		}
	}

	protected void reset() {
		super.reset();
		theStartPoint = null;
		theEndPoint = null;
	}

	public void paint(Graphics2D g) {
		if (theEditableWhiteBoard == null) return;
		if (theStartPoint == null) return;
		if (theEndPoint == null) return;
		Rectangle rect = new Rectangle();
		rect.setFrameFromDiagonal(theStartPoint, theEndPoint);
		store(g);
		if (isFilled()) {
			g.setColor(getBackgroundColor());
			g.fill(rect);
		}
		g.setColor(getForegroundColor());
		g.setStroke(new BasicStroke((float)getThickness(), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
		g.draw(rect);
		restore(g);
	}

	private boolean isSameQuadrant(Point anEndPoint) {
		int X1 = theEndPoint.x - theStartPoint.x;
		int X2 = anEndPoint.x - theStartPoint.x;
		int Y1 = theEndPoint.y - theStartPoint.y;
		int Y2 = anEndPoint.y - theStartPoint.y;
		if (X1 == 0 || X2 == 0) return false;
		if (Y1 == 0 || Y2 == 0) return false;
		if ((X1 > 0) && (X2 < 0)) return false;
		if ((X1 < 0) && (X2 > 0)) return false;
		if ((Y1 > 0) && (Y2 < 0)) return false;
		return !((Y1 < 0) && (Y2 > 0));
	}

	public Cursor getCursor() {
		return new Cursor(Cursor.CROSSHAIR_CURSOR);
	}
}
