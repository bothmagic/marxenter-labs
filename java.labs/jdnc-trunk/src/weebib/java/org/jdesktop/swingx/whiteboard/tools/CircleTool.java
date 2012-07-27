/*
 * $Id: CircleTool.java 830 2006-08-02 00:39:24Z weebib $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */

package org.jdesktop.swingx.whiteboard.tools;

import org.jdesktop.swingx.whiteboard.ShapeItem;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.event.MouseEvent;

/**
 * A tool which draws Circles.
 */
public class CircleTool extends AShapeTool {
	private Point theStartPoint;
	private Point theEndPoint;

	public CircleTool() {
		super("Circle");
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

		Rectangle rect = getSquare();
		Ellipse2D circle = new Ellipse2D.Float(rect.x, rect.y, rect.width, rect.height);
		reset();
		theEditableWhiteBoard.addPaintableItem(new ShapeItem(circle,
															 getForegroundColor(),
															 getBackgroundColor(),
															 getThickness(),
															 isFilled()));
	}

	public void mouseDragged(MouseEvent e) {
		if (theEditableWhiteBoard == null) return;
		if (theStartPoint == null) return;
		if (theEndPoint == null) return;
		Point endPoint = e.getPoint();
		Rectangle oldRect = getSquare();
		theEndPoint = endPoint;
		Rectangle newRect = getSquare();
		Rectangle rect = new Rectangle();
		Rectangle.union(oldRect, newRect, rect);
		rect.grow(MAX_WIDTH, MAX_WIDTH);
		theEditableWhiteBoard.onToolUpdate(this, rect);
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
		Rectangle rect = getSquare();
		Ellipse2D circle = new Ellipse2D.Float(rect.x, rect.y, rect.width, rect.height);
		store(g);
		if (isFilled()) {
			g.setColor(getBackgroundColor());
			g.fill(circle);
		}
		g.setColor(getForegroundColor());
		g.setStroke(new BasicStroke((float)getThickness(), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
		g.draw(circle);
		restore(g);
	}

	public Cursor getCursor() {
		return new Cursor(Cursor.CROSSHAIR_CURSOR);
	}

	private Rectangle getSquare() {
		int deltax = theEndPoint.x - theStartPoint.x;
		int deltay = theEndPoint.y - theStartPoint.y;
		int diameter = Math.max(Math.abs(deltax), Math.abs(deltay));
		Rectangle result;
		if (deltax < 0) {
			if (deltay < 0) {
				result = new Rectangle(theStartPoint.x - diameter, theStartPoint.y - diameter, diameter, diameter);
			} else {
				result = new Rectangle(theStartPoint.x - diameter, theStartPoint.y, diameter, diameter);
			}
		} else {
			if (deltay < 0) {
				result = new Rectangle(theStartPoint.x, theStartPoint.y - diameter, diameter, diameter);
			} else {
				result = new Rectangle(theStartPoint.x, theStartPoint.y, diameter, diameter);
			}
		}
		return result;
	}
}
