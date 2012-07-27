/*
 * $Id: ShapeItem.java 830 2006-08-02 00:39:24Z weebib $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */

package org.jdesktop.swingx.whiteboard;

import java.awt.*;

/**
 * User: plelannic
 * Date: 19 juin 2006
 * Time: 17:35:42
 */
public class ShapeItem implements IPaintableItem {
	private Shape theShape;
	private Color theForegroundColor;
	private Color theBackgroundColor;
	private int theThickness;
	private boolean isFilled;

	private Color theStoredColor;
	private Stroke theStoredStroke;

	public ShapeItem(Shape aShape,
					 Color aForegroundColor,
					 Color aBackgroundColor,
					 int aThickness,
					 boolean isFilled) {
		theShape = aShape;
		theForegroundColor = aForegroundColor;
		theBackgroundColor = aBackgroundColor;
		theThickness = aThickness;
		this.isFilled = isFilled;
	}

	protected void store(Graphics2D g) {
		theStoredColor = g.getColor();
		theStoredStroke = g.getStroke();
	}

	protected void restore(Graphics2D g) {
		g.setColor(theStoredColor);
		g.setStroke(theStoredStroke);
	}

	public void paint(Graphics2D g) {
		store(g);
		if (isFilled) {
			g.setColor(theBackgroundColor);
			g.fill(theShape);
		}
		g.setColor(theForegroundColor);
		g.setStroke(new BasicStroke((float)theThickness, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
		g.draw(theShape);
		restore(g);
	}

	public String getDescription() {
		return theShape.toString();
	}
}
