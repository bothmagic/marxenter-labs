/*
 * $Id: JHyperLink.java 22 2004-09-06 18:38:26Z rbair $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.jdnc.incubator.rbair.swing;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import org.jdesktop.jdnc.incubator.rbair.util.StringUtils;

/**
 * This class represents a hyper link.<br>
 * There are several different behaviors possible from a hyperlink:
 * <ol>
 * <li>Link looks like normal text unless the mouse rolls over it, at
 * which time it changes colors and shows an underline</li>
 * <li>Always looks like a link (different color and underlined)</li>
 * </ol>
 * <br>
 * <br>
 * It is possible to set what color the link is before having
 * been followed, and what color the link is after having been followed.
 * <br>
 * <b>Warning: Unlike JLabel, a JHyperLink cannot be passed html for the
 * text.  This is similiar to how hyperlinks work in html.  As an aside,
 * this was necessitated because in order to get the "underline" in the
 * text, I had to switch the text to HTML.  Hence, if you pass in html
 * then it will end up getting two &lt;html&gt tags.</b>
 * <br>
 * There are two UIManager properties associated with this object, 
 * JHyperLink.clickedColor and JHyperLink.unclickedColor.
 * @author Richard Bair
 */
public class JHyperLink extends JLabel {
	/**
	 * Color for the hyper link if it has not yet been clicked.
	 * This color can be set both in code, and through the UIManager
	 * with the property "JHyperLink.unclickedColor".
	 */
	private Color unclickedColor = new Color(0, 0x33, 0xFF);
	/**
	 * Color for the hyper link if it has already been clicked.
	 * This color can be set both in code, and through the UIManager
	 * with the property "JHyperLink.clickedColor".
	 */
	private Color clickedColor = new Color(0x99, 0, 0x99);
	/**
	 * Indicates whether this hyperlink looks like a hyperlink when the mouse
	 * is NOT over it.  In other words, if the hyperlink is "hidden", it appears
	 * to be normal text unles the user hovers over the link with the mouse.  Hence,
	 * the hyperlink is hiding its true nature, like Superman/Clark Kent :-)
	 */
	private boolean hidden = true;
	/**
	 * Indicates whether this hyperlink has been clicked.
	 */
	private boolean hasBeenClicked = false;
	/**
	 * Indicates whether the mouse has entered this hyper link
	 */
	private boolean hasBeenEntered = false;
	/**
	 * ActionListener for handling hyperlink click events
	 * NOTE: In the future I may deprecate this in favor of a
	 * traditional List of listeners and have an addActionListener
	 * and removeActionListner methods.
	 */
	private ActionListener clickListener;
	/**
	 * If constructed based on an action, this will hold that variable
	 */
	protected Action action;
	
	/**
	 * Create a new HyperLink.  Pass in the text and the actionListener for
	 * hyperlink click events.
	 * @param text
	 * @param clickListener
	 */
	public JHyperLink(String text, ActionListener clickListener) {
		this(text, JLabel.LEADING, clickListener);
	}
	
	/**
	 * Create a new HyperLink.  Pass in the text, the alignment, and 
	 * the actionListener for hyperlink click events.
	 * @param text
	 * @param horizontalAlignment
	 * @param clickListener
	 */
	public JHyperLink(String text, int horizontalAlignment, ActionListener clickListener) {
		super();
		setOpaque(false);
		assert StringUtils.hasHtml(text) == false;
		setText(text);
		setHorizontalAlignment(horizontalAlignment);
		addMouseListener(new HyperLinkMouseListener(this));
		this.clickListener = clickListener;

		//set the UIManager colors for this component to some defaults if they are not already set
//		UIManagerUtils.initDefault("JHyperLink.unclickedColor", new Color(0, 0x33, 0xFF));
//		UIManagerUtils.initDefault("JHyperLink.clickedColor", new Color(0x99, 0, 0x99));
	}
	
	/**
	 * Create a new JHyperLink based on the given action.  The click event
	 * handler is the action's handler, and so forth.
	 * @param a
	 */
	public JHyperLink(Action a) {
		this((String)a.getValue(Action.NAME), JLabel.LEADING, new ActionEventHelper(a));
		super.setIcon((Icon)a.getValue(Action.SMALL_ICON));
		this.action = a;
	}
	
	/**
	 * Default Constructor.  Has no text, and does nothing on click events.
	 */
	public JHyperLink() {
		this("", JLabel.LEADING, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//does nothing
			}
		});
	}
	
	/**
	 * Method for changing the action associated with this JHyperLink.
	 * @param action
	 */
	public void setAction(Action action) {
		setText((String)action.getValue(Action.NAME));
		clickListener = new ActionEventHelper(action);
		super.setIcon((Icon)action.getValue(Action.SMALL_ICON));
	}
	
	/**
	 * A wrapper class that calls an action's actionPerformed method.
	 * @author Richard Bair
	 * date: Jun 27, 2003
	 */
	private static final class ActionEventHelper implements ActionListener {
		private Action a;
		public ActionEventHelper(Action a) {
			this.a = a;
		}
		public void actionPerformed(ActionEvent ae) {
			a.actionPerformed(ae);
		}
	}
	
	/**
	 * Inner utility class that provides MouseListener capabilities for this
	 * hyperlink.  Changes the color of the hyperlink when selected, the cursor
	 * for the hyperlink when the mouse is over it, and so forth.
	 * @author Richard Bair
	 */
	private static final class HyperLinkMouseListener extends MouseAdapter {
		/**
		 * the parent JHyperLink component
		 */
		private JHyperLink parent;
		private Cursor oldCursor = null;
		
		private HyperLinkMouseListener(JHyperLink parent) {
			assert parent != null;
			this.parent = parent;
		}
		
		/* (non-Javadoc)
		 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
		 */
		public void mouseClicked(MouseEvent e) {
			//construct an ActionEvent and pass it to the
			//parent's clickListener
			ActionEvent ae = new ActionEvent(parent, ActionEvent.ACTION_PERFORMED, "");
			parent.clickListener.actionPerformed(ae);
			parent.hasBeenClicked = true;
			parent.repaint();
		}

		/* (non-Javadoc)
		 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
		 */
		public void mouseEntered(MouseEvent e) {
			if (parent.hidden) {
				//need to change the label's appearance so it looks like a hyperlink
				/*
				 * TODO really need to check and make sure this label isn't
				 * already using html and that it doesn't already have a font tag...
				 * if I want to support html text in the hyper link, that is
				 */
				parent.hasBeenEntered = true;
				parent.repaint();
			}
			//change the cursor
			oldCursor = SwingUtilities.getWindowAncestor(parent).getCursor();
			SwingUtilities.getWindowAncestor(parent).setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		}

		/* (non-Javadoc)
		 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
		 */
		public void mouseExited(MouseEvent e) {
			if (parent.hidden) {
				//need to change the label's appearance so it looks like a normal label
				parent.hasBeenEntered = false;
				parent.repaint();
			}
			if (oldCursor != null) {
				SwingUtilities.getWindowAncestor(parent).setCursor(oldCursor);
			}
		}
	}

	/**
	 * @return
	 */
	public Color getClickedColor() {
		return clickedColor;
	}

	/**
	 * @return
	 */
	public ActionListener getClickListener() {
		return clickListener;
	}

	/**
	 * @return
	 */
	public boolean isHasBeenClicked() {
		return hasBeenClicked;
	}

	/**
	 * @return
	 */
	public boolean isHidden() {
		return hidden;
	}

	/**
	 * @return
	 */
	public Color getUnclickedColor() {
		return unclickedColor;
	}

	/**
	 * @param color
	 */
	public void setClickedColor(Color color) {
		clickedColor = color;
	}

	/**
	 * @param listener
	 */
	public void setClickListener(ActionListener listener) {
		clickListener = listener;
	}

	/* (non-Javadoc)
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	protected void paintComponent(Graphics g) {
		if (isOpaque()) {
			g.setColor(getBackground());
			g.fillRect(0, 0, getWidth(), getHeight());
		}
		
		//if there is an icon, I'd better draw the icon
		Icon ico = getIcon();
		if (ico != null) {
			ico.paintIcon(this, g, 0, 0);
		}
		
		String text = getText();
		if (text != null && text.length() > 0) {
			g.setColor(hasBeenClicked ? clickedColor : unclickedColor);
			int y = getHeight();
			int x = ico == null ? 0 : ico.getIconWidth() + 1;
			g.drawString(text, x, y-2);
//			SwingUtilities2.drawString(this, g, text, x, y-2);
			if (hasBeenEntered || !hidden) {
				g.drawLine(0, getHeight()-1, SwingUtilities.computeStringWidth(g.getFontMetrics(), text), getHeight()-1);
			}
		}
	}

	/**
	 * @param b
	 */
	public void setHidden(boolean b) {
		if (b) {
			hidden = true;
		} else if (!b && hidden) {
			hidden = false;
		}
	}

	/**
	 * @param color
	 */
	public void setUnclickedColor(Color color) {
		unclickedColor = color;
	}
	
	/**
	 * Sets a flag to indicate if the link has been visited. The state of this
	 * flag can be used to render the color of the link.
	 */
	public void setVisited(boolean visited) {
		this.hasBeenClicked = visited;
	}

	public boolean getVisited() {
		return hasBeenClicked;
	}
	
}
