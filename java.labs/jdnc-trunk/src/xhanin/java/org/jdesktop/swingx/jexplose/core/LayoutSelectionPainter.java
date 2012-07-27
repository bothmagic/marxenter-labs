/*
 * $Id: LayoutSelectionPainter.java 985 2006-12-22 19:40:13Z xhanin $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx.jexplose.core;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;

/**
 * For internal use only
 */
public class LayoutSelectionPainter implements Painter {
    private static final int PADDING = 5;
    private ExploseLayout _layout;

    public LayoutSelectionPainter(ExploseLayout layout) {
        _layout = layout;
    }

    public void prepare(JComponent comp) {
    }

    public void paint(Graphics g) {
      Thumbnail selected = _layout.getSelected();
      if (selected != null) {
          Graphics2D g2 = (Graphics2D)g;
          double scale = _layout.getScale();
          Rectangle bounds = new Rectangle(selected);
          bounds.x = (int)((bounds.x + _layout.getTotalOffset().x) * scale);
          bounds.y = (int)((bounds.y + _layout.getTotalOffset().y) * scale);
          bounds.width *= scale;
          bounds.height *= scale;
          
          Composite originalComposite = g2.getComposite();
          
          // too much time consuming  !!!
//          g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
//          g2.setPaint(Color.BLUE);
//          g2.fill(bounds);

          g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.6f));
          g2.setPaint(Color.black);

          int textLength = SwingUtilities.computeStringWidth(g2.getFontMetrics(), selected.getIFrame().getTitle());
          int textX = bounds.x + (bounds.width / 2) - textLength / 2;
          int textY = bounds.y + (bounds.height / 2) + g2.getFont().getSize() / 2;

          g2.fillRoundRect(textX - PADDING, textY - g2.getFont().getSize() - PADDING, textLength + 2 * PADDING, g2.getFont().getSize() + 2 * PADDING, 4, 4);
          g2.setComposite(originalComposite);
          g2.setPaint(Color.white);
          g2.drawString(selected.getIFrame().getTitle(), textX, textY);
      }
    }

    public void dispose() {
    }
}
