package org.jdesktop.jdnc.incubator.vprise.candy;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.plaf.metal.MetalButtonUI;

/**
 * A hyperlink component that derives from JButton to provide compatibility mostly for binding actions
 * enabled/disabled behavior accesility i18n etc...
 *
 * @author Shai Almog
 */
public class JHyperlink extends JButton {
    private boolean hasBeenClicked = false;

	/**
	 * Indicates whether the mouse has entered this hyper link
	 */
	private boolean hasBeenEntered = false;

    /**
	 * Color for the hyper link if it has not yet been clicked.
	 * This color can be set both in code, and through the UIManager
	 * with the property "JXHyperlink.unclickedColor".
	 */
	private Color unclickedColor = new Color(0, 0x33, 0xFF);
	
    /**
	 * Color for the hyper link if it has already been clicked.
	 * This color can be set both in code, and through the UIManager
	 * with the property "JXHyperlink.clickedColor".
	 */
	private Color clickedColor = new Color(0x99, 0, 0x99);
    
    /** Creates a new instance of JHyperlink */
    public JHyperlink() {
        super();
    }
    
    public JHyperlink(Action action) {
        super(action);
        init();
    }

    public JHyperlink(Icon icon) {
        super(icon);
        init();
    }

    public JHyperlink(String text, Icon icon) {
        super(text, icon);
        init();
    }
    
    public JHyperlink(String text) {
        super(text);
        init();
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
	 * @return
	 */
	public Color getClickedColor() {
		return clickedColor;
	}

    /**
	 * @param color
	 */
	public void setUnclickedColor(Color color) {
		unclickedColor = color;
	}
    
    private void init() {
        // null can't be used since a L&F might replace it
        setBorder(BorderFactory.createEmptyBorder());
        setContentAreaFilled(false);
        setHorizontalAlignment(LEADING);
		addMouseListener(new HyperlinkMouseListener());
        addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                hasBeenClicked = true;
            }
        });
    }
    
	/**
	 * Inner utility class that provides MouseListener capabilities for this
	 * hyperlink.  Changes the color of the hyperlink when selected, the cursor
	 * for the hyperlink when the mouse is over it, and so forth.
	 * @author Richard Bair
	 */
	private final class HyperlinkMouseListener extends MouseAdapter {
		private Cursor oldCursor = null;
		
		/* (non-Javadoc)
		 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
		 */
		public void mouseEntered(MouseEvent e) {
            JHyperlink.this.hasBeenEntered = true;
            JHyperlink.this.repaint();
            
			//change the cursor
			oldCursor = SwingUtilities.getWindowAncestor(JHyperlink.this).getCursor();
			SwingUtilities.getWindowAncestor(JHyperlink.this).setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		}

		/* (non-Javadoc)
		 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
		 */
		public void mouseExited(MouseEvent e) {
            //need to change the label's appearance so it looks like a normal label
            JHyperlink.this.hasBeenEntered = false;
            JHyperlink.this.repaint();
			if (oldCursor != null) {
				SwingUtilities.getWindowAncestor(JHyperlink.this).setCursor(oldCursor);
			}
		}
	}

	protected void paintComponent(Graphics g) {
        Color c = hasBeenClicked ? clickedColor : unclickedColor;
        
        //set the foreground color based on the click state
        setForeground(c);

        super.paintComponent(g);

        //add an underline to the font if necessary
        if(isEnabled() && isVisible()) {
            String text = getText();
            if (text != null && text.length() > 0) {
                if (hasBeenEntered) {
                    g.setColor(c);        
                    Icon ico = getIcon();
                    int y = getHeight();
                    int x = ico == null ? 0 : ico.getIconWidth() + 1;
                    g.drawLine(x, getHeight()-1, SwingUtilities.computeStringWidth(g.getFontMetrics(), text) + x, getHeight()-1);
                }
            }
        }
	}

}
