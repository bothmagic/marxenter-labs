/*
 * $Id: AShapeTool.java 830 2006-08-02 00:39:24Z weebib $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */

package org.jdesktop.swingx.whiteboard.tools;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

/**
 * Abstract class for all tools which are meant to add ShapeItems to the WhiteBoard.
 */
public abstract class AShapeTool extends ATool {
	protected static final int MAX_WIDTH = 10;
	private static final Color[] DEFAULT_COLORS = {
			Color.BLACK,
			Color.WHITE,
			Color.RED,
			Color.BLUE,
			Color.GREEN,
			Color.YELLOW,
			Color.PINK};

	private int theThickness;
	private ColorComboBoxModel theForegroundColorComboBoxModel;
	private ColorComboBoxModel theBackgroundColorComboBoxModel;
	private boolean isFilled;
	private JPanel theParameterPanel;

	private Color theStoredColor;
	private Stroke theStoredStroke;

	private class ColorIcon implements Icon {
		private Color theColor;

		public ColorIcon(Color aColor) {
			theColor = aColor;
		}

		public void paintIcon(Component c, Graphics g, int x, int y) {
			Color tempColor = g.getColor();
			if (theColor == null) {
				g.setColor(Color.BLACK);
				String s = "?";
				FontMetrics fm = ((Graphics2D)g).getFontMetrics();
				g.drawString(s, x + (16 - fm.stringWidth(s)) / 2, y + (16 + fm.getAscent()) / 2);
			} else {
				g.setColor(theColor);
				g.fillRect(x, y, 16, 16);
				g.setColor(Color.BLACK);
				g.drawRect(x, y, 15, 15);
			}
			g.setColor(tempColor);
		}

		public int getIconWidth() {
			return 16;
		}

		public int getIconHeight() {
			return 16;
		}
	}

	private class ColorWrapper {
		private Color theColor;
		private ColorIcon theColorIcon;

		public ColorWrapper(Color aColor) {
			theColor = aColor;
			theColorIcon = new ColorIcon(aColor);
		}

		public Color getColor() {
			return theColor;
		}

		public ColorIcon getColorIcon() {
			return theColorIcon;
		}
	}

	private class ColorComboBoxModel extends AbstractListModel implements ComboBoxModel {
		private List<ColorWrapper> theColorWrappers;
		private ColorWrapper theChooserColorWrapper;
		private ColorWrapper theSelectedColorWrapper;

		public ColorComboBoxModel() {
			theColorWrappers = new LinkedList<ColorWrapper>();
			for (Color color : DEFAULT_COLORS) {
				theColorWrappers.add(new ColorWrapper(color));
			}
			theChooserColorWrapper = new ColorWrapper(null);
			theColorWrappers.add(theChooserColorWrapper);
			theSelectedColorWrapper = theColorWrappers.get(0);
		}

		public int getSize() {
			return theColorWrappers.size();
		}

		public Object getElementAt(int index) {
			return theColorWrappers.get(index);
		}

		public void setSelectedItem(Object anItem) {
			if (anItem == theChooserColorWrapper) {
				Color newColor = JColorChooser.showDialog(theParameterPanel,
														  "Pick a color",
														  theSelectedColorWrapper.getColor());
				if (newColor == null) return;
				theSelectedColorWrapper = new ColorWrapper(newColor);
				fireContentsChanged(this, -1, -1);
			} else {
				theSelectedColorWrapper = (ColorWrapper)anItem;
				fireContentsChanged(this, -1, -1);
			}
		}

		public Object getSelectedItem() {
			return theSelectedColorWrapper;
		}

		public Color getColor() {
			return theSelectedColorWrapper.getColor();
		}
	}

	private class ColorComboBoxRenderer extends DefaultListCellRenderer {
		public Component getListCellRendererComponent(JList list, Object value, int index,
													  boolean isSelected, boolean cellHasFocus) {
			super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
			setText("");
			setIcon(((ColorWrapper)value).getColorIcon());
			return this;
		}
	}

	private class ThickNessComboBoxModel extends AbstractListModel implements ComboBoxModel {
		public int getSize() {
			return MAX_WIDTH;
		}

		public Object getElementAt(int index) {
			return index + 1;
		}

		public void setSelectedItem(Object anItem) {
			theThickness = (Integer)anItem;
		}

		public Object getSelectedItem() {
			return theThickness;
		}
	}

	protected AShapeTool(String aName) {
		super(aName);
		theThickness = 1;
		isFilled = false;
		theParameterPanel = new JPanel(new GridBagLayout());

		GridBagConstraints l = new GridBagConstraints();
		GridBagConstraints r = new GridBagConstraints();
		l.gridx = 0;
		l.gridy = 0;
		l.weightx = 0.0;
		l.weighty = 0.0;
		l.anchor = GridBagConstraints.NORTHEAST;
		l.insets = new Insets(5, 5, 5, 5);
		r.gridx = 1;
		r.gridy = 0;
		r.weightx = 1.0;
		r.weighty = 0.0;
		r.anchor = GridBagConstraints.NORTHWEST;
		r.insets = new Insets(5, 0, 5, 5);

		theParameterPanel.add(new JLabel("Foreground :"), l);
		theForegroundColorComboBoxModel = new ColorComboBoxModel();
		JComboBox foreGroundComboBox = new JComboBox(theForegroundColorComboBoxModel);
		foreGroundComboBox.setRenderer(new ColorComboBoxRenderer());
		theParameterPanel.add(foreGroundComboBox, r);

		l.gridy = 1;
		l.insets = new Insets(0, 5, 5, 5);
		r.gridy = 1;
		r.insets = new Insets(0, 0, 5, 5);
		theParameterPanel.add(new JLabel("Background :"), l);
		theBackgroundColorComboBoxModel = new ColorComboBoxModel();
		JComboBox backGroundComboBox = new JComboBox(theBackgroundColorComboBoxModel);
		backGroundComboBox.setRenderer(new ColorComboBoxRenderer());
		theParameterPanel.add(backGroundComboBox, r);

		l.gridy = 2;
		r.gridy = 2;
		theParameterPanel.add(new JLabel("Thickness :"), l);
		theParameterPanel.add(new JComboBox(new ThickNessComboBoxModel()), r);

		l.gridy = 3;
		l.weighty = 1.0;
		r.gridy = 3;
		r.weighty = 1.0;
		theParameterPanel.add(new JLabel("Filled :"), l);
		final JCheckBox filledCheckBox = new JCheckBox("", isFilled);
		filledCheckBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				isFilled = filledCheckBox.isSelected();
			}
		});
		theParameterPanel.add(filledCheckBox, r);
	}

	public JPanel getParameterPanel() {
		return theParameterPanel;
	}

	protected void store(Graphics2D g) {
		theStoredColor = g.getColor();
		theStoredStroke = g.getStroke();
	}

	protected void restore(Graphics2D g) {
		g.setColor(theStoredColor);
		g.setStroke(theStoredStroke);
	}

	protected Color getForegroundColor() {
		return theForegroundColorComboBoxModel.getColor();
	}

	protected Color getBackgroundColor() {
		return theBackgroundColorComboBoxModel.getColor();
	}

	protected boolean isFilled() {
		return isFilled;
	}

	protected int getThickness() {
		return theThickness;
	}
}
