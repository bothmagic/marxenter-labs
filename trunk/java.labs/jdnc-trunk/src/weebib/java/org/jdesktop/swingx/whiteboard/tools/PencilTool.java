/*
 * $Id: PencilTool.java 830 2006-08-02 00:39:24Z weebib $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */

package org.jdesktop.swingx.whiteboard.tools;

import org.jdesktop.swingx.whiteboard.ShapeItem;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.GeneralPath;
import java.awt.image.BufferedImage;


/**
 * A tool which draws General Paths in a free-hand drawing manner.
 */
public class PencilTool extends AShapeTool {
	private GeneralPath theGeneralPath;
	private Cursor theCursor;

	public PencilTool() {
		super("Pencil");
		BufferedImage bi = new BufferedImage(17, 17, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = bi.createGraphics();
		g.setColor(Color.WHITE);
		g.drawLine(0, 1, 15, 16);
		g.drawLine(1, 0, 16, 15);
		g.drawLine(0, 15, 15, 0);
		g.drawLine(1, 16, 16, 1);
		g.setColor(Color.BLACK);
		g.drawLine(0, 0, 16, 16);
		g.drawLine(0, 16, 16, 0);
		g.dispose();
		theCursor = Toolkit.getDefaultToolkit().createCustomCursor(bi, new Point(8, 8), "Pencil");
	}

	public Cursor getCursor() {
		return theCursor;
	}

	public void mousePressed(MouseEvent e) {
		if (theEditableWhiteBoard == null) return;
		Point endPoint = e.getPoint();
		theGeneralPath = new GeneralPath();
		theGeneralPath.moveTo(endPoint.x, endPoint.y);
	}

	public void mouseReleased(MouseEvent e) {
		if (theEditableWhiteBoard == null) return;
		if (theGeneralPath == null) return;
		theEditableWhiteBoard.addPaintableItem(new ShapeItem(theGeneralPath,
															 getForegroundColor(),
															 getBackgroundColor(),
															 getThickness(),
															 isFilled()));
		reset();
	}

	public void mouseDragged(MouseEvent e) {
		if (theEditableWhiteBoard == null) return;
		if (theGeneralPath == null) return;
		Rectangle oldRect = theGeneralPath.getBounds();
		Point endPoint = e.getPoint();
		theGeneralPath.lineTo(endPoint.x, endPoint.y);
		Rectangle newRect = theGeneralPath.getBounds();
		Rectangle rect = new Rectangle();
		Rectangle.union(oldRect, newRect, rect);
		rect.grow(MAX_WIDTH, MAX_WIDTH);
		theEditableWhiteBoard.onToolUpdate(this, rect);
	}

	protected void reset() {
		super.reset();
		theGeneralPath = null;
	}

	public void paint(Graphics2D g) {
		if (theEditableWhiteBoard == null) return;
		if (theGeneralPath == null) return;
		store(g);
		g.setColor(getForegroundColor());
		g.setStroke(new BasicStroke((float)getThickness(), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
		g.draw(theGeneralPath);
		restore(g);
	}
}
