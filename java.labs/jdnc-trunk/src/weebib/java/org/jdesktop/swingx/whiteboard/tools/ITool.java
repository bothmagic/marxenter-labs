/*
 * $Id: ITool.java 830 2006-08-02 00:39:24Z weebib $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */

package org.jdesktop.swingx.whiteboard.tools;

import org.jdesktop.swingx.whiteboard.EditableWhiteBoard;

import javax.swing.*;
import javax.swing.event.MouseInputListener;
import java.awt.*;

/**
 * An ITool instance is a means to add IPaintableItems to a WhiteBoard.
 */
public interface ITool extends MouseInputListener {
	/**
	 * @return a Name for this tool.
	 */
	public String getName();

	/**
	 * If anEditableWhiteBoard is not null, the tool is added to the EditableWhiteBoard as the current editing one.
	 * Otherwise the tool is reset.
	 * @param anEditableWhiteBoard
	 */
	public void setEditableWhiteBoard(EditableWhiteBoard anEditableWhiteBoard);

	/**
	 * @return the Cursor instance for this tool.
	 */
	public Cursor getCursor();

	/**
	 * Paints the tools on the EditableWhiteBoard.
	 * @param g
	 */
	public void paint(Graphics2D g);

	/**
	 * @return the panel containing the set of gui controls over the tool's parameters
	 */
	public JPanel getParameterPanel();
}
