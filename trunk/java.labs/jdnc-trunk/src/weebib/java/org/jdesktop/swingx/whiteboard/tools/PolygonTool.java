/*
 * $Id: PolygonTool.java 830 2006-08-02 00:39:24Z weebib $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */

package org.jdesktop.swingx.whiteboard.tools;

import org.jdesktop.swingx.whiteboard.ShapeItem;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;

/**
 * A tool which combines free-hand drawing and line-drawing. Upon double-click the current General Path is closed.
 */
public class PolygonTool extends AShapeTool {
	private GeneralPath theGeneralPath;
	private Point theMovingPoint;
	private Point theEndPoint;

	public PolygonTool() {
		super("Polygon");
	}

	public void mouseClicked(MouseEvent e) {
		if (theEditableWhiteBoard == null) return;
		if (theGeneralPath == null) return;
		if (e.getClickCount() == 2) {
			theGeneralPath.closePath();
			theEditableWhiteBoard.addPaintableItem(new ShapeItem(theGeneralPath,
																 getForegroundColor(),
																 getBackgroundColor(),
																 getThickness(),
																 isFilled()));
			reset();
		}
	}

	public void mousePressed(MouseEvent e) {
		if (theEditableWhiteBoard == null) return;
		if (theGeneralPath == null || theEndPoint == null) {
			theEndPoint = e.getPoint();
			theEditableWhiteBoard.storeMouseLocation(theEndPoint);
			theGeneralPath = new GeneralPath();
			theGeneralPath.moveTo(theEndPoint.x, theEndPoint.y);
		} else {
			Point currentPoint = e.getPoint();
			theGeneralPath.lineTo(currentPoint.x, currentPoint.y);
			theEndPoint = currentPoint;
			theEditableWhiteBoard.storeMouseLocation(theEndPoint);
			Rectangle rect = theGeneralPath.getBounds();
			rect.grow(MAX_WIDTH, MAX_WIDTH);
			theEditableWhiteBoard.onToolUpdate(this, rect);
		}
	}

	public void mouseReleased(MouseEvent e) {
		if (theEditableWhiteBoard == null) return;
		if (theGeneralPath == null) return;
		theMovingPoint = null;
	}

	public void mouseMoved(MouseEvent e) {
		if (theEditableWhiteBoard == null) return;
		if (theGeneralPath == null) return;
		if (theEndPoint == null) return;
		Point aMovingPoint = e.getPoint();
		if (theMovingPoint == null) {
			theMovingPoint = aMovingPoint;
		}
		Rectangle oldRect = new Rectangle();
		oldRect.setFrameFromDiagonal(theEndPoint, theMovingPoint);
		theMovingPoint = aMovingPoint;
		Rectangle newRect = new Rectangle();
		newRect.setFrameFromDiagonal(theEndPoint, theMovingPoint);
		newRect.grow(MAX_WIDTH, MAX_WIDTH);
		Rectangle rect = new Rectangle();
		Rectangle.union(oldRect, newRect, rect);
		rect.grow(MAX_WIDTH, MAX_WIDTH);
		theEditableWhiteBoard.onToolUpdate(this, rect);
	}

	public void mouseDragged(MouseEvent e) {
		if (theEditableWhiteBoard == null) return;
		if (theGeneralPath == null) return;
		if (theEndPoint == null) return;
		theMovingPoint = null;
		Point endPoint = e.getPoint();
		theGeneralPath.lineTo(endPoint.x, endPoint.y);
		theEndPoint = endPoint;
		Rectangle rect = theGeneralPath.getBounds();
		rect.grow(MAX_WIDTH, MAX_WIDTH);
		theEditableWhiteBoard.onToolUpdate(this, rect);
	}

	protected void reset() {
		super.reset();
		theMovingPoint = null;
		theGeneralPath = null;
		theEndPoint = null;
	}

	public void paint(Graphics2D g) {
		if (theEditableWhiteBoard == null) return;
		if (theGeneralPath == null) return;
		store(g);
		if (isFilled()) {
			g.setColor(getBackgroundColor());
			g.fill(theGeneralPath);
		}
		g.setColor(getForegroundColor());
		g.setStroke(new BasicStroke((float)getThickness(), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
		g.draw(theGeneralPath);
		if (theEndPoint != null && theMovingPoint != null) {
			g.draw(new Line2D.Float(theEndPoint.x, theEndPoint.y, theMovingPoint.x, theMovingPoint.y));
		}
		restore(g);
	}

}
