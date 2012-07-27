/*
 * $Id: LineTool.java 830 2006-08-02 00:39:24Z weebib $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */

package org.jdesktop.swingx.whiteboard.tools;

import org.jdesktop.swingx.whiteboard.ShapeItem;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;

/**
 * A tool which draws Lines.
 */
public class LineTool extends AShapeTool {
	private Point theStartPoint;
	private Point theMovingPoint;

	public LineTool() {
		super("Ligne");
	}

	public void mousePressed(MouseEvent e) {
		if (theEditableWhiteBoard == null) return;
		if (theStartPoint == null) {
			theStartPoint = e.getPoint();
			theEditableWhiteBoard.storeMouseLocation(theStartPoint);
			theMovingPoint = e.getPoint();
			Rectangle rect = new Rectangle(theStartPoint);
			rect.grow(MAX_WIDTH, MAX_WIDTH);
			theEditableWhiteBoard.onToolUpdate(this, rect);
		} else {
			theMovingPoint = e.getPoint();
			if (theMovingPoint == null) return;
			Line2D.Float line = new Line2D.Float(theStartPoint.x, theStartPoint.y, theMovingPoint.x, theMovingPoint.y);
			theEditableWhiteBoard.addPaintableItem(new ShapeItem(line,
																 getForegroundColor(),
																 getBackgroundColor(),
																 getThickness(),
																 isFilled()));
			reset();
		}
	}

	public void mouseMoved(MouseEvent e) {
		if (theEditableWhiteBoard == null) return;
		if (theStartPoint == null) return;
		Point movingPoint = e.getPoint();
		if (theMovingPoint == null) {
			theMovingPoint = movingPoint;
		}
		Rectangle oldRect = new Rectangle();
		oldRect.setFrameFromDiagonal(theStartPoint, theMovingPoint);
		theMovingPoint = e.getPoint();
		Rectangle newRect = new Rectangle();
		newRect.setFrameFromDiagonal(theStartPoint, theMovingPoint);
		Rectangle rect = new Rectangle();
		Rectangle.union(oldRect, newRect, rect);
		rect.grow(MAX_WIDTH, MAX_WIDTH);
		theEditableWhiteBoard.onToolUpdate(this, rect);
	}

	public void paint(Graphics2D g) {
		if (theEditableWhiteBoard == null) return;
		if (theStartPoint == null) return;
		if (theMovingPoint == null) return;
		store(g);
		g.setColor(getForegroundColor());
		g.setStroke(new BasicStroke((float)getThickness(), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
		g.draw(new Line2D.Float(theStartPoint.x, theStartPoint.y, theMovingPoint.x, theMovingPoint.y));
		restore(g);
	}

	protected void reset() {
		super.reset();
		theStartPoint = null;
		theMovingPoint = null;
	}

	public Cursor getCursor() {
		return new Cursor(Cursor.CROSSHAIR_CURSOR);
	}
}
