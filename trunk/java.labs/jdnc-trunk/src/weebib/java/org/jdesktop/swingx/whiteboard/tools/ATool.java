/*
 * $Id: ATool.java 830 2006-08-02 00:39:24Z weebib $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */

package org.jdesktop.swingx.whiteboard.tools;

import org.jdesktop.swingx.whiteboard.EditableWhiteBoard;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * Convinience super class for all ITool instances.
 */
public abstract class ATool implements ITool {
	protected EditableWhiteBoard theEditableWhiteBoard;
	protected boolean isActive;
	private String theName;

	protected ATool(String aName) {
		theEditableWhiteBoard = null;
		theName = aName;
		isActive = false;
	}

	public String getName() {
		return theName;
	}

	public void setEditableWhiteBoard(EditableWhiteBoard anEditableWhiteBoard) {
		theEditableWhiteBoard = anEditableWhiteBoard;
		if (theEditableWhiteBoard == null) {
			reset();
		}
	}

	public Cursor getCursor() {
		return Cursor.getDefaultCursor();
	}

	public void mouseClicked(MouseEvent e) {}

	public void mousePressed(MouseEvent e) {}

	public void mouseReleased(MouseEvent e) {}

	public void mouseEntered(MouseEvent e) {}

	public void mouseExited(MouseEvent e) {}

	public void mouseDragged(MouseEvent e) {}

	public void mouseMoved(MouseEvent e) {}

	public JPanel getParameterPanel() {
		return null;
	}

	protected void reset() {
		if (theEditableWhiteBoard != null) {
			theEditableWhiteBoard.clearStoredLocations();
		}
	}
}
