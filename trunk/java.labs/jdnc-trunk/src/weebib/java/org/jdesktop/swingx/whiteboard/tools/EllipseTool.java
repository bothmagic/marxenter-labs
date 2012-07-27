/*
 * $Id: EllipseTool.java 830 2006-08-02 00:39:24Z weebib $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */

package org.jdesktop.swingx.whiteboard.tools;

import org.jdesktop.swingx.whiteboard.ShapeItem;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;

/**
 * A tool which draws Ellipses.
 */
public class EllipseTool extends AShapeTool {
	private Point theStartPoint;
	private Point theEndPoint;

	public EllipseTool() {
		super("Ellipse");
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
		Ellipse2D ellipse2D = new Ellipse2D.Float(rect.x, rect.y, rect.width, rect.height);
		reset();
		theEditableWhiteBoard.addPaintableItem(new ShapeItem(ellipse2D,
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
		Rectangle oldRect = new Rectangle();
		oldRect.setFrameFromDiagonal(theStartPoint, theEndPoint);
		theEndPoint = endPoint;
		Rectangle newRect = new Rectangle();
		newRect.setFrameFromDiagonal(theStartPoint, theEndPoint);
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
		Rectangle rect = new Rectangle();
		rect.setFrameFromDiagonal(theStartPoint, theEndPoint);
		Ellipse2D ellipse = new Ellipse2D.Float(rect.x, rect.y, rect.width, rect.height);
		store(g);
		if (isFilled()) {
			g.setColor(getBackgroundColor());
			g.fill(ellipse);
		}
		g.setColor(getForegroundColor());
		g.setStroke(new BasicStroke((float)getThickness(), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
		g.draw(ellipse);
		restore(g);
	}

	public Cursor getCursor() {
		return new Cursor(Cursor.CROSSHAIR_CURSOR);
	}
}
