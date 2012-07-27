/*
 * $Id: WhiteBoard.java 830 2006-08-02 00:39:24Z weebib $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */

package org.jdesktop.swingx.whiteboard;

import javax.swing.*;
import javax.swing.undo.*;
import java.util.List;
import java.util.LinkedList;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

/**
 * WhiteBoard is a component holding a collection of IPaintableItems which can be added dynamically.
 * It supports undo/redo actions.
 * @author weebib
 */
public class WhiteBoard extends JPanel {
	private List<IPaintableItem> thePaintableItems;
	private Dimension theImageSize;
	private BufferedImage theBufferedImage;
	private UndoManager theUndoManager;
	private UndoAction theUndoAction;
	private RedoAction theRedoAction;

//-- Inner classes ------
	private class UndoAction extends AbstractAction {
		public UndoAction() {
			super("Undo");
			setEnabled(false);
		}

		public void actionPerformed(ActionEvent e) {
			try {
				theUndoManager.undo();
			} catch (CannotUndoException ex) {
				ex.printStackTrace();
			}
			updateUndoState();
			theRedoAction.updateRedoState();
		}

		protected void updateUndoState() {
			if (theUndoManager.canUndo()) {
				setEnabled(true);
				putValue(Action.NAME, theUndoManager.getUndoPresentationName());
			} else {
				setEnabled(false);
				putValue(Action.NAME, "Undo");
			}
		}
	}

	private class RedoAction extends AbstractAction {
		public RedoAction() {
			super("Redo");
			setEnabled(false);
		}

		public void actionPerformed(ActionEvent e) {
			try {
				theUndoManager.redo();
			} catch (CannotRedoException ex) {
				ex.printStackTrace();
			}
			updateRedoState();
			theUndoAction.updateUndoState();
		}

		protected void updateRedoState() {
			if (theUndoManager.canRedo()) {
				setEnabled(true);
				putValue(Action.NAME, theUndoManager.getRedoPresentationName());
			} else {
				setEnabled(false);
				putValue(Action.NAME, "Redo");
			}
		}
	}

	private class AddShapeUndoableEdit extends AbstractUndoableEdit implements UndoableEdit {
		private IPaintableItem thePaintableItem;

		public AddShapeUndoableEdit(IPaintableItem aPaintableItem) {
			thePaintableItem = aPaintableItem;
		}

		public void undo() throws CannotUndoException {
			super.undo();
			int index = thePaintableItems.indexOf(thePaintableItem);
			if (index == -1) return;
			while (index < thePaintableItems.size()) {
				thePaintableItems.remove(index);
			}
			reloadImage();
			repaint();
		}

		public void redo() throws CannotRedoException {
			super.redo();
			thePaintableItems.add(thePaintableItem);
			addToImage(thePaintableItem);
			repaint();
		}
	}

//-- Constructor ------
	/**
	 * Constructs a Whiteboard of the given size.
	 * @param aSize
	 */
	public WhiteBoard(Dimension aSize) {
		super(null);
		if (aSize == null || aSize.width <= 0 || aSize.height <= 0) {
			throw new IllegalArgumentException("Invalid image size : " + aSize);
		}
		setOpaque(true);
		theImageSize = aSize;
		thePaintableItems = new LinkedList<IPaintableItem>();
		reloadImage();
		theUndoAction = new UndoAction();
		theRedoAction = new RedoAction();
		InputMap inputMap = getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);

		// todo : these keystrokes aren't platform independent
		inputMap.put(KeyStroke.getKeyStroke("control Z"), "undo");
		inputMap.put(KeyStroke.getKeyStroke("control shift Z"), "redo");
		ActionMap actionMap = getActionMap();
		actionMap.put("undo", theUndoAction);
		actionMap.put("redo", theRedoAction);
		theUndoManager = new UndoManager();
	}

//-- Public methods ------
	/**
	 * Adds the IPaintableItem to the list. The whiteboard is then repainted.
	 * @param aPaintableItem
	 */
	public void addPaintableItem(IPaintableItem aPaintableItem) {
		thePaintableItems.add(aPaintableItem);
		addToImage(aPaintableItem);
		repaint();
		theUndoManager.addEdit(new AddShapeUndoableEdit(aPaintableItem));
		theUndoAction.updateUndoState();
		theRedoAction.updateRedoState();
	}

	/**
	 * Clears the Whiteboard.
	 */
	public void clear() {
		thePaintableItems.clear();
		reloadImage();
		repaint();
	}

	public Action getRedoAction() {
		return theRedoAction;
	}

	public Action getUndoAction() {
		return theUndoAction;
	}

//-- JComponent overriden methods ------
	public Dimension getPreferredSize() {
		Insets insets = getInsets();
		Dimension pref = new Dimension(theImageSize);
		pref.width += insets.left + insets.right;
		pref.height += insets.top + insets.bottom;
		return pref;
	}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (theBufferedImage == null) return;
		Graphics2D g2D = (Graphics2D)g;
		Insets insets = getInsets();
		g.translate(insets.left, insets.right);
		g2D.drawImage(theBufferedImage, 0, 0, null);
		g.translate(-insets.left, -insets.right);
	}

//-- Private methods ------
	private void reloadImage() {
		theBufferedImage = new BufferedImage(theImageSize.width, theImageSize.height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = theBufferedImage.createGraphics();
		Color tmpColor = g.getColor();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, theImageSize.width, theImageSize.height);
		g.setColor(tmpColor);
		for (IPaintableItem paintableItem : thePaintableItems) {
			paintableItem.paint(g);
		}
		g.dispose();
	}

	private void addToImage(IPaintableItem aPaintableItem) {
		if (aPaintableItem == null) return;
		Graphics2D g = theBufferedImage.createGraphics();
		aPaintableItem.paint(g);
		g.dispose();
	}
}
