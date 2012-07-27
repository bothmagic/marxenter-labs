/*
 * $Id: PainterExperiments.java 3337 2011-01-18 12:47:05Z kleopatra $
 *
 * Copyright 2006 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 *
 */
package org.jdesktop.swingx.painter;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;

import javax.swing.Action;
import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JViewport;
import javax.swing.table.TableCellRenderer;

import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.action.AbstractActionExt;
import org.jdesktop.swingx.incubatorutil.InteractiveTestCase;
import org.jdesktop.swingx.incubatorutil.XTestUtils;
import org.jdesktop.swingx.painter.AbstractLayoutPainter.HorizontalAlignment;
import org.jdesktop.swingx.painter.AbstractLayoutPainter.VerticalAlignment;

/**
 * Quick experiment: concept of underlay/content layer.
 * 
 * TODO move somewhere else (incubator, doesn't belong into core!
 * 
 * @author Jeanette Winzenburg
 */
public class PainterExperiments extends InteractiveTestCase {
    public static void main(String args[]) {
      PainterExperiments test = new PainterExperiments();
      try {
          setLookAndFeel("Nimbus");
//        test.runInteractiveTests();
//          test.runInteractiveTests(".*Label.*");
         test.runInteractiveTests(".*Heade.*");
      } catch (Exception e) {
          System.err.println("exception when executing interactive tests:");
          e.printStackTrace();
      }
  }
    
    public void interactiveHeader() {
        JXTable table = new JXTable(20, 5);
        final TableCellRenderer core = table.getTableHeader().getDefaultRenderer();
        TableCellRenderer wrapper = new TableCellRenderer() {

            @Override
            public Component getTableCellRendererComponent(JTable table,
                    Object value, boolean isSelected, boolean hasFocus,
                    int row, int column) {
                Component comp = core.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                ((JLabel)comp).setHorizontalAlignment(JLabel.CENTER);
                return comp;
            }
            
        };
        table.getTableHeader().setDefaultRenderer(wrapper);
        showWithScrollingInFrame(table, "align");
        
    }
    public void interactiveViewportImage() {
        
        final ImagePainter imagePainter = new ImagePainter(XTestUtils.loadDefaultImage(getClass(), "moon.jpg"));
        imagePainter.setHorizontalAlignment(HorizontalAlignment.LEFT);
        imagePainter.setVerticalAlignment(VerticalAlignment.TOP);
        JViewport viewport = new JViewport() {

            /** 
             * @inherited <p>
             */
            @Override
            protected void paintComponent(Graphics g) {
                imagePainter.paint((Graphics2D) g, this, getWidth(), getHeight());
                super.paintComponent(g);
            }
             
        };
        JScrollPane pane = new JScrollPane() {
            
            /** 
             * @inherited <p>
             */
            @Override
            protected void paintComponent(Graphics g) {
//                imagePainter.paint((Graphics2D) g, this, getWidth(), getHeight());
                super.paintComponent(g);
            }
        };
        pane.setViewport(viewport);
        
        JTextArea area = new JTextArea(20, 20);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setText(
                "Used to display a 'Tip' for a Component. "
                + "Typically components provide api to automate the process of "
                + "using ToolTips. For example, any Swing component can use the "
                + "JComponent  setToolTipText method to specify the text for a standard tooltip."        
        );
        area.setOpaque(false);
        viewport.setOpaque(false);
        pane.setViewportView(area);
        showInFrame(pane, "viewport background");
        
    }
    /**
     * JXLabel restore default foreground painter.
     * Sequence: 
     *   compose the default with a transparent overlay
     *   try to reset to default
     *   try to compose the overlay again.
     */
    public void interactiveRestoreDefaultForegroundPainter() {
        final JXXLabel opaque = new JXXLabel();
        opaque.setOpaque(true);
        opaque.setBackground(Color.YELLOW);
        opaque.setText("setup: compound - default and overlay ");
       ShapePainter shapePainter = new ShapePainter();
       final AlphaPainter alpha = new AlphaPainter();
       alpha.setAlpha(0.2f);
       alpha.setPainters(shapePainter);
       CompoundPainter<JComponent> compoundA = new CompoundPainter<JComponent>(
               opaque.getDefaultUnderlayPainter(), 
               opaque.getDefaultContentPainter(), alpha);
       opaque.setPainter(compoundA);
        final JXXLabel notOpaque = new JXXLabel();
        notOpaque.setOpaque(false);
         notOpaque.setText("setup: compound - default and overlay ");
//        final AlphaPainter alpha = new AlphaPainter();
//        alpha.setAlpha(0.2f);
//        alpha.setPainters(new ShapePainter());
        CompoundPainter<JComponent> compound = new CompoundPainter<JComponent>(
                notOpaque.getDefaultUnderlayPainter(), 
                notOpaque.getDefaultContentPainter(), alpha);
        notOpaque.setPainter(compound);
        JComponent box = Box.createVerticalBox();
        box.add(opaque);
        box.add(notOpaque);
        Action action = new AbstractActionExt("reset default foreground") {
            boolean reset;
            public void actionPerformed(ActionEvent e) {
                if (reset) {
                    CompoundPainter<JComponent> painter = new CompoundPainter<JComponent>(
                            notOpaque.getDefaultUnderlayPainter(),
                           notOpaque.getDefaultContentPainter(), alpha);
                    notOpaque.setPainter(painter);
                    CompoundPainter<JComponent> painterA = new CompoundPainter<JComponent>(
                            opaque.getDefaultUnderlayPainter(),
                            opaque.getDefaultContentPainter(), alpha);
                     opaque.setPainter(painterA);
                } else {
                  // try to reset to default
                    notOpaque.setPainter(null);
                    opaque.setPainter(null);
                }
                reset = !reset;
                notOpaque.repaint();
                opaque.repaint();
            }

        };
        JXFrame frame = wrapInFrame(box, "foreground painters");
        addAction(frame, action);
        frame.pack();
        frame.setVisible(true);
    }
    
    public void interactiveJXXLabel() {
        JXXLabel label = new JXXLabel();
        label.setText("XXLable: permanent translate");
        Painter<JComponent> permanentTranslate = new Painter<JComponent>() {

            public void paint(Graphics2D g, JComponent object, int width, int height) {
                g.translate(50, 0); 
            }
            
        };
        // the permanentTranslate has no effect because CompoundPainter.paint
        // creates a graphics for each painter
        CompoundPainter<JComponent> painter = new CompoundPainter<JComponent>(
                label.getDefaultUnderlayPainter(), permanentTranslate, 
                label.getDefaultContentPainter(), new ShapePainter());
        label.setPainter(painter);
        JComponent box = Box.createVerticalBox();
        box.add(label);
        showInFrame(box, "JXXLabel - underlay/content/overlay");
        
    }

    
    public static class JXXLabel extends JLabel {
        Painter<JComponent> defaultUnderlayPainter;
        Painter<JComponent> defaultContentPainter;
        
        Painter<JComponent> painter;
        
        public JXXLabel() {
            initDefaultPainters();
        }

        /**
         * 
         */
        private void initDefaultPainters() {
            defaultUnderlayPainter = new Painter<JComponent>() {

                public void paint(Graphics2D g, JComponent comp, int width, int height) {
                    if (comp.isOpaque()) {
                        g.setColor(comp.getBackground());
                        g.fillRect(0, 0, width, height);
                    }
                }
                
            };
            defaultContentPainter = new Painter<JComponent>() {

                public void paint(Graphics2D g, JComponent object, int width, int height) {
                    uiPaint(g);
                }
                
            };
            
        }

        /**
         * Delegates content painting to the ui. Note that this method
         * most probably will change the graphics state. It's up to calling
         * code to guarantee any state invariants.
         * 
         * @param g the graphics to paint on.
         */
        protected void uiPaint(Graphics2D g) {
            if (ui != null) {
                ui.paint(g, this);
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            Painter<JComponent> painter = getPainter();
            if (painter != null) {
                paintComponentWithPainter(g, painter);
            } else {
                super.paintComponent(g);
            }
        }

        /**
         * @param g
         */
        private void paintComponentWithPainter(Graphics g, Painter<JComponent> painter) {
            Graphics2D scratch = (Graphics2D) g.create();
            try {
                painter.paint(scratch, this, getWidth(), getHeight());
            } finally {
                scratch.dispose();
            }
        }

        /**
         * @return
         */
        private Painter<JComponent> getPainter() {
            return painter;
        }
        
        protected void setPainter(Painter<JComponent> painter) {
            this.painter = painter;
        }
        
        public Painter<JComponent> getDefaultUnderlayPainter() {
            return defaultUnderlayPainter;
        }
        
        public Painter<JComponent> getDefaultContentPainter() {
            return defaultContentPainter;
        }
    }

    /**
     * 
     * Painters and textfield. Not showing? 
     *
     */
    public void interactiveXTextFieldShareForegroundPainter() {
        final CompoundPainter<JComponent> painter = new CompoundPainter<JComponent>();
        JXTextField label = new JXTextField();
        label.setText("Painter in textfield: source for shared painter");
        ShapePainter shapePainter = new ShapePainter();
        AlphaPainter alpha = new AlphaPainter();
        alpha.setAlpha(0.2f);
        alpha.setPainters(shapePainter);
        painter.setPainters(label.getPainter(), alpha);
        label.setPainter(painter);
        JXTextField labelAP = new JXTextField();
        labelAP.setText("Painter: use shared from above");
        labelAP.setPainter(painter);
        JComponent box = Box.createVerticalBox();
        box.add(label);
        box.add(labelAP);
        showInFrame(box, "shared ui painting in textfield");
    }
    
    public static class JXTextField extends JTextField {
        
        private Painter<JComponent> painter;
        
        @Override
        protected void paintComponent(Graphics g) {
            Painter<JComponent> painter = getPainter();
            Graphics2D scratch = (Graphics2D) g.create();
            try {
                painter.paint(scratch, this, getWidth(), getHeight());
                ui.paint(scratch, this);
            } finally {
                scratch.dispose();
            }
        }

        public Painter<JComponent> getPainter() {
            if (painter == null) {
                painter = new AbstractPainter<JComponent>() {

                    @Override
                    protected void doPaint(Graphics2D g, JComponent component, int width, int height) {
                        JXTextField.super.paintComponent(g);
                    }
                    
                };
            }
            return painter;
        }
        
        public void setPainter(Painter<JComponent> painter) {
            this.painter = painter;
            repaint();
        }
    }

}
