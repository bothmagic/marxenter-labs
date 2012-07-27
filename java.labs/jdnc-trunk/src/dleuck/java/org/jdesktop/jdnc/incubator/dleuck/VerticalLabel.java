/*
 * $Id: VerticalLabel.java 722 2005-10-03 22:29:31Z dleuck $
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

import javax.swing.ButtonModel;
import javax.swing.CellRendererPane;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.UIManager;
import javax.swing.border.Border;

/**
 * A label that paints its content rotated right or left by 90 degrees.  Note: 
 * the vertical and horizontal text position and alignment properties relate to
 * the pre-rotated orientation.
 * 
 * @see javax.swing.JButton
 * @author Daniel Leuck
 */
public class VerticalLabel extends JLabel implements VerticalComponent {

	private int rotation;	
	private Insets insets;
	private Dimension preferredSize, minimumSize, maximumSize;
	private CellRendererPane renderer;
	private JLabel delegate;
	
    /**
     * Creates a <code>VerticalLabel</code> instance with 
     * no image and with an empty string for the title.
     * The label is centered vertically 
     * in its display area.
     * The label's contents, once set, will be displayed on the leading edge 
     * of the label's display area.
     */
    public VerticalLabel() {
		super();
		configure();
	}

    /**
     * Creates a <code>VerticalLabel</code> instance with the specified
     * image and horizontal alignment.
     * The label is centered vertically in its display area.
     *
     * @param image  The image to be displayed by the label.
     * @param horizontalAlignment  One of the following constants
     *           defined in <code>SwingConstants</code>:
     *           <code>LEFT</code>,
     *           <code>CENTER</code>, 
     *           <code>RIGHT</code>,
     *           <code>LEADING</code> or
     *           <code>TRAILING</code>.
     */
	public VerticalLabel(Icon image, int horizontalAlignment) {
		super(image, horizontalAlignment);
		configure();
	}

    /**
     * Creates a <code>VerticalLabel</code> instance with the specified image.
     * The label is centered vertically and horizontally
     * in its display area.
     *
     * @param image  The image to be displayed by the label.
     */
	public VerticalLabel(Icon image) {
		super(image);
		configure();
	}

    /**
     * Creates a <code>VerticalLabel</code> instance with the specified
     * text, image, and horizontal alignment.
     * The label is centered vertically in its display area.
     * The text is on the trailing edge of the image.
     *
     * @param text  The text to be displayed by the label.
     * @param icon  The image to be displayed by the label.
     * @param horizontalAlignment  One of the following constants
     *           defined in <code>SwingConstants</code>:
     *           <code>LEFT</code>,
     *           <code>CENTER</code>,
     *           <code>RIGHT</code>,
     *           <code>LEADING</code> or
     *           <code>TRAILING</code>.
     */
	public VerticalLabel(String text, Icon icon, int horizontalAlignment) {
		super(text, icon, horizontalAlignment);
		configure();
	}

    /**
     * Creates a <code>VerticalLabel</code> instance with the specified
     * text and horizontal alignment.
     * The label is centered vertically in its display area.
     *
     * @param text  The text to be displayed by the label.
     * @param horizontalAlignment  One of the following constants
     *           defined in <code>SwingConstants</code>:
     *           <code>LEFT</code>,
     *           <code>CENTER</code>,
     *           <code>RIGHT</code>,
     *           <code>LEADING</code> or
     *           <code>TRAILING</code>.
     */
	public VerticalLabel(String text, int horizontalAlignment) {
		super(text, horizontalAlignment);
		configure();
	}

    /**
     * Creates a <code>VerticalLabel</code> instance with the specified text.
     * The label is aligned against the leading edge of its display area,
     * and centered vertically.
     *
     * @param text  The text to be displayed by the label.
     */
	public VerticalLabel(String text) {
		super(text);
		configure();
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

	/**
	 * Paint the component.
	 * 
	 * @param g The graphics context for painting
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
    @Override
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;

		double degrees = Math.toRadians(-90);
		Border b = getBorder();
		Insets i = (b==null) ? insets : b.getBorderInsets(this);
		
		if(rotation==LEFT) {
			int h = getHeight();
			g2.translate(0, h);
			g2.rotate(degrees);

			renderer.paintComponent(g2, delegate, this, i.bottom, 
					i.left, getHeight()-i.top-i.bottom, 
					getWidth()-i.right-i.left, true);		
			
			g2.rotate(-degrees);
			g2.translate(0, -h);
		} else {
			int w = getWidth();
			g2.translate(w, 0);
			g2.rotate(-degrees);
			
			renderer.paintComponent(g2, delegate, this, i.top, 
					i.right, getHeight()-i.top-i.bottom, 
					getWidth()-i.right-i.left, true);	
			
			g2.rotate(degrees);
			g2.translate(-w, 0);
		}
		
		paintBorder(g);
	}
    
    ////////////////////////////////////////////////////////////////////////////

	private void configure() {
		rotation = LEFT;
		insets = new Insets(0,0,0,0);
		preferredSize = new Dimension();
		minimumSize = new Dimension();
		maximumSize = new Dimension();
		
		delegate = new JLabel() {
			
			public String getText() {
				return VerticalLabel.this.getText();
			}

			public Color getForeground() {
				return VerticalLabel.this.getForeground();
			}
			
			public Color getBackground() {
				return VerticalLabel.this.getForeground();
			}		
			
			public int getDisplayedMnemonic() {
				return VerticalLabel.this.getDisplayedMnemonic();
			}
			
			public int getDisplayedMnemonicIndex() {
				return VerticalLabel.this.getDisplayedMnemonicIndex();
			}			
			
			public boolean isOpaque() {
				return VerticalLabel.this.isOpaque();
			}
			
			public boolean isEnabled() {
				return VerticalLabel.this.isEnabled();
			}

			public int getIconTextGap() {
				return VerticalLabel.this.getIconTextGap();
			}

			public int getVerticalAlignment() {
				return VerticalLabel.this.getVerticalAlignment();
			}

			public int getVerticalTextPosition() {
				return VerticalLabel.this.getVerticalTextPosition();
			}

			public int getHorizontalAlignment() {
				return VerticalLabel.this.getHorizontalAlignment();
			}

			public int getHorizontalTextPosition() {
				return VerticalLabel.this.getHorizontalTextPosition();
			}
			
			public Border getBorder() {
				return null;
			}

			public Insets getInsets() {
				return null;
			}

			public Icon getIcon() {
				return VerticalLabel.this.getIcon();
			}

			public Icon getDisabledIcon() {
				return VerticalLabel.this.getDisabledIcon();
			}
		};
		
		renderer = new CellRendererPane();
		renderer.add(delegate);		
	}
}
