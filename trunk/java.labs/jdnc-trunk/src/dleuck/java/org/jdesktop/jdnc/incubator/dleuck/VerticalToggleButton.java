/*
 * $Id: VerticalToggleButton.java 721 2005-10-03 22:04:37Z dleuck $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 * 
 * This code was donated by Ikayzo.com.
 */
package org.jdesktop.jdnc.incubator.dleuck;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;

import javax.swing.Action;
import javax.swing.ButtonModel;
import javax.swing.CellRendererPane;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JToggleButton;
import javax.swing.UIManager;
import javax.swing.border.Border;

/**
 * A toggle button that paints its content rotated either right or left by
 * 90 degrees.  Note: the vertical and horizontal text position and alignment
 * properties relate to the pre-rotated orientation.
 * 
 * @see javax.swing.JToggleButton
 * @author Daniel Leuck
 */
public class VerticalToggleButton extends JToggleButton
	implements VerticalComponent {

	private JToggleButton buttonDelegate;
	private JLabel labelDelegate;
	private CellRendererPane renderer;
	private Dimension preferredSize, minimumSize, maximumSize;
	private int rotation;

    /**
     * Creates a vertical toggle button with no set text or icon.
     */
	public VerticalToggleButton() {
		super();
		configure();
	}
	
    /**
     * Creates a vertical toggle button with text.
     *
     * @param text  the text of the button
     */
	public VerticalToggleButton(String text) {
		super(text);
		configure();
	}
	
    /**
     * Creates a vertical toggle button with the specified text
     * and selection state.
     *
     * @param text  the string displayed on the toggle button
     * @param selected  if true, the button is initially selected;
     *                  otherwise, the button is initially unselected
     */
    public VerticalToggleButton (String text, boolean selected) {
        super(text, selected);
        configure();
    }	
	
    /**
     * Creates a vertical toggle button where properties are taken from the 
     * <code>Action</code> supplied.
     *
     * @param action the <code>Action</code> used to specify the new button
     */
	public VerticalToggleButton(Action action) {
		super(action);
		configure();
	}

    /**
     * Creates a vertical toggle button with an icon.
     *
     * @param icon  the Icon image to display on the button
     */
	public VerticalToggleButton(Icon icon) {
		super(icon);
		configure();
	}
	
    /**
     * Creates a vertical toggle button with the specified image 
     * and selection state, but no text.
     *
     * @param icon  the image that the button should display
     * @param selected  if true, the button is initially selected;
     *                  otherwise, the button is initially unselected
     */
    public VerticalToggleButton(Icon icon, boolean selected) {
        super(icon, selected);
        configure();
    }

    /**
     * Creates a vertical toggle button with initial text and an icon.
     *
     * @param text  the text of the button
     * @param icon  the Icon image to display on the button
     */
	public VerticalToggleButton(String text, Icon icon) {
		super(text, icon);
		configure();
	}
	
    /**
     * Creates a vertical toggle button with the specified text, image, and
     * selection state.
     *
     * @param text the text of the toggle button
     * @param icon  the image that the button should display
     * @param selected  if true, the button is initially selected;
     *                  otherwise, the button is initially unselected
     */
    public VerticalToggleButton (String text, Icon icon, boolean selected) {
    	super(text, icon, selected);
    	configure();
    }	
	
	/**
	 * Get the preferred size of this component.
	 * 
	 * @return the value of the <code>preferredSize</code> property
	 */
	@Override
	public Dimension getPreferredSize() {
		Dimension size = super.getPreferredSize();

		preferredSize.width = size.height;
		preferredSize.height = size.width;

		return preferredSize;
	}

	/**
	 * Get the minimum size of this component.
	 * 
	 * @return the value of the <code>minimumSize</code> property
	 */	
	@Override
	public Dimension getMinimumSize() {
		Dimension size = super.getMinimumSize();

		minimumSize.width = size.height;
		minimumSize.height = size.width;

		return minimumSize;
	}

	/**
	 * Get the maximum size of this component.
	 * 
	 * @return the value of the <code>maximumSize</code> property
	 */	
	@Override
	public Dimension getMaximumSize() {
		Dimension size = super.getMaximumSize();

		maximumSize.width = size.height;
		maximumSize.height = size.width;

		return maximumSize;
	}

	/**
	 * Paint the component.
	 * 
	 * @param g The graphics context for painting
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;

		renderer.paintComponent(g2, buttonDelegate, this, 0, 0,
				getWidth(), getHeight(), true);

		ButtonModel model = getModel();
		boolean doOffset = model.isPressed() && model.isArmed();
		int offsetAmount = UIManager.getInt("Button.textShiftOffset");
		double degrees = Math.toRadians(-90);
		
		if(rotation==LEFT) {
			int h = getHeight();
			g2.translate(0, h);
			g2.rotate(degrees);
			if(doOffset)
				g2.translate(-offsetAmount,offsetAmount);
			renderer.paintComponent(g2, labelDelegate, this, 0, 0,
					getHeight(), getWidth(), true);
			if(doOffset)
				g2.translate(offsetAmount,-offsetAmount);
			g2.rotate(-degrees);
			
			g2.translate(0, -h);
		} else {
			int w = getWidth();
			g2.translate(w, 0);
			g2.rotate(-degrees);
			if(doOffset)
				g2.translate(offsetAmount,-offsetAmount);
			renderer.paintComponent(g2, labelDelegate, this, 0, 0,
					getHeight(), getWidth(), true);
			if(doOffset)
				g2.translate(-offsetAmount,offsetAmount);
			g2.rotate(degrees);
			g2.translate(-w, 0);
		}
	}
	
    /**
     * Get the rotation (SwingConstants.LEFT for a -90 degree rotation or
     * SwingConstants.RIGHT for a 90 degree rotation)
     * 
     * @return The rotation
     */	
	public int getRotation() {
		return rotation;
	}
	
    /**
     * Set the rotation (SwingConstants.LEFT for a -90 degree rotation or
     * SwingConstants.RIGHT for a 90 degree rotation)
     * 
     * @param rotation The rotation
     */	
	public void setRotation(int rotation) {
	    if (this.rotation == rotation) return;
	    int oldValue = this.rotation;
	    this.rotation = checkRotationKey(rotation, "rotation");
	    firePropertyChange(ROTATION_CHANGED_PROPERTY, oldValue, rotation);       
	    repaint();		
	}
	
    /**
     * Verify that key is a legal value for the
     * <code>rotationAlignment</code> property.
     *
     * @param key the property value to check, one of the following values:
     * <ul>
     * <li> SwingConstants.LEFT (the default)
     * <li> SwingConstants.RIGHT 
     * </ul>
     * 
     * @param exception the <code>IllegalArgumentException</code>
     *		detail message 
     * @exception IllegalArgumentException if key is not one of the legal
     *		values listed above
     * @see #setRotation
     */
    protected int checkRotationKey(int key, String exception) {
        if (key == LEFT || key == RIGHT) {
            return key;
        } else {
            throw new IllegalArgumentException(exception);
        }
    }
	
	////////////////////////////////////////////////////////////////////////////
    
	private void configure() {
		rotation = LEFT;
		
		buttonDelegate = new JToggleButton() {
			public String getText() {
				return null;
			}

			public boolean isEnabled() {
				return VerticalToggleButton.this.isEnabled();
			}

			public boolean isSelected() {
				return VerticalToggleButton.this.isSelected();
			}
			
			public boolean isFocusPainted() {
				return VerticalToggleButton.this.isFocusPainted();
			}

			public boolean isContentAreaFilled() {
				return VerticalToggleButton.this.isContentAreaFilled();
			}

			public boolean isBorderPainted() {
				return VerticalToggleButton.this.isBorderPainted();
			}
			
			public Border getBorder() {
				return VerticalToggleButton.this.getBorder();
			}
			
			public boolean isOpaque() {
				return VerticalToggleButton.this.isOpaque();
			}
			
			public Color getBackground() {
				return VerticalToggleButton.this.getBackground();
			}
			
			public Icon getIcon() { return null; }
			public Icon getPressedIcon() { return null; }
			public Icon getSelectedIcon() { return null; }
			public Icon getRolloverIcon() { return null; }
			public Icon getRolloverSelectedIcon() { return null; }
			public Icon getDisabledIcon() { return null; }
		};

		labelDelegate = new JLabel() {
			public String getText() {
				return VerticalToggleButton.this.getText();
			}

			public Color getForeground() {
				return VerticalToggleButton.this.getForeground();
			}
			
			public boolean isEnabled() {
				return VerticalToggleButton.this.isEnabled();
			}

			public int getIconTextGap() {
				return VerticalToggleButton.this.getIconTextGap();
			}

			public int getVerticalAlignment() {
				return VerticalToggleButton.this.getVerticalAlignment();
			}

			public int getVerticalTextPosition() {
				return VerticalToggleButton.this.getVerticalTextPosition();
			}

			public int getHorizontalAlignment() {
				return VerticalToggleButton.this.getHorizontalAlignment();
			}

			public int getHorizontalTextPosition() {
				return VerticalToggleButton.this.getHorizontalTextPosition();
			}
			
			public Border getBorder() {
				return null;
			}
			
			public int getMnemonic() {
				return VerticalToggleButton.this.getMnemonic();
			}
			
			public int getDisplayedMnemonicIndex() {
				return VerticalToggleButton.this.getDisplayedMnemonicIndex();
			}			
			
			private Insets insets = new Insets(0,0,0,0);
			public Insets getInsets() {
				Border b = VerticalToggleButton.this.getBorder();
				
				Insets minsets = VerticalToggleButton.this.getMargin();
				
				insets.left=minsets.left;
				insets.top=minsets.top;
				insets.right=minsets.right;
				insets.bottom=minsets.bottom;					
				
				if(b!=null) {
					Insets binsets =
						b.getBorderInsets(VerticalToggleButton.this);
					insets.left+=binsets.left;
					insets.top+=binsets.top;
					insets.right+=binsets.right;
					insets.bottom+=binsets.bottom;
				}	
				
				return insets;
			}

			public Icon getIcon() {
				ButtonModel model = VerticalToggleButton.this.getModel();
				Icon subIcon = null;
				
				if (!model.isEnabled()) {
					if (model.isSelected()) {
						subIcon = getDisabledSelectedIcon();
					} else {
						subIcon = getDisabledIcon();
					}
				} else if (model.isPressed() && model.isArmed()) {
					subIcon = getPressedIcon();
				} else if (isRolloverEnabled() && model.isRollover()) {
					if (model.isSelected()) {
						subIcon = getRolloverSelectedIcon();
					} else {
						subIcon = getRolloverIcon();
					}
				} else if (model.isSelected()) {
					subIcon = getSelectedIcon();
				}

				if(subIcon==null)
					subIcon=VerticalToggleButton.this.getIcon();
				
				return subIcon;
			}

			public Icon getDisabledIcon() {
				return VerticalToggleButton.this.getDisabledIcon();
			}
		};

		labelDelegate.setOpaque(false);
		
		// we paint our own border
		buttonDelegate.setBorderPainted(false);
		buttonDelegate.setModel(getModel());
		
		renderer = new CellRendererPane();
		renderer.add(buttonDelegate);
		renderer.add(labelDelegate);
		
		
		preferredSize = new Dimension();
		minimumSize = new Dimension();
		maximumSize = new Dimension();

		Action action = getAction();
		if (action != null)
			buttonDelegate.setAction(action);
	}
}
