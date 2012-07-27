/*
 * Created on 23.02.2008
 *
 */
package org.jdesktop.swingx.renderer;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.Map;
import java.util.logging.Logger;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.table.TableModel;

import org.jdesktop.swingx.JXDatePicker;
import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXMonthView;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.color.ColorUtil;
import org.jdesktop.swingx.decorator.AbstractHighlighter;
import org.jdesktop.swingx.decorator.ColorHighlighter;
import org.jdesktop.swingx.decorator.ComponentAdapter;
import org.jdesktop.swingx.decorator.HighlightPredicate;
import org.jdesktop.swingx.decorator.HighlighterFactory;
import org.jdesktop.swingx.decorator.PainterHighlighter;
import org.jdesktop.swingx.incubatorutil.AncientSwingTeam;
import org.jdesktop.swingx.incubatorutil.InteractiveTestCase;
import org.jdesktop.swingx.painter.BusyPainter;

public class RendererExperiments extends InteractiveTestCase {
    @SuppressWarnings("unused")
    private static final Logger LOG = Logger
            .getLogger(RendererExperiments.class.getName());

    public static void main(String args[]) {
      setSystemLF(true);
        RendererExperiments test = new RendererExperiments();
      try {
//        test.runInteractiveTests();
         test.runInteractiveTests(".*Translucent.*");
      } catch (Exception e) {
          System.err.println("exception when executing interactive tests:");
          e.printStackTrace();
      }
  }
    
    
    public void interactiveTranslucentRenderer() {
        JXTable table = new JXTable(new AncientSwingTeam());
        table.setOpaque(false);
        LabelProvider provider = new LabelProvider() {
            /**
             * Quick override to synch the rendering component's opaqueness
             * Should probably be done in DefaultVisuals?
             */
            @Override
            protected void configureVisuals(CellContext context) {
                super.configureVisuals(context);
                rendererComponent.setOpaque(context.getComponent().isOpaque());
            }
            
        };
        // second column translucent
        table.getColumnExt(1).setCellRenderer(new DefaultTableRenderer(provider));
        // predicate if selected - TODO: add to swingx predicate zoo 
        HighlightPredicate predicate = new HighlightPredicate(){

            @Override
            public boolean isHighlighted(Component renderer,
                    ComponentAdapter adapter) {
                return adapter.isSelected();
            }};
        // Highlighter to force opacity   
        AbstractHighlighter hl = new AbstractHighlighter(predicate) {

            @Override
            protected Component doHighlight(Component component,
                    ComponentAdapter adapter) {
                if (component instanceof JComponent) {
                    ((JComponent) component).setOpaque(true);
                }
                return component;
            }
            
        };
        table.getColumnExt(1).setHighlighters(hl);
        JScrollPane pane = new JScrollPane(table);
        pane.getViewport().setBackground(Color.GREEN);
        showInFrame(pane, "highlight translucent property");
        
    }
    public void interactiveDefaultStriping() {
        JTable core = new JTable(new AncientSwingTeam());
//        Color nimbusAlternate = new Color(242, 242, 242, 255);
//        Color swingXGeneric = new Color(229, 229, 229, 255);
//        core.setBackground(swingXGeneric);
        final JXTable xTable = new JXTable(new AncientSwingTeam());
        JXFrame frame = showWithScrollingInFrame(core, xTable, "compare core <--> x");
        Action toggle = new AbstractAction("toggle swingx striping") {
            boolean on;
            @Override
            public void actionPerformed(ActionEvent e) {
                if (on) {
                    xTable.setHighlighters();
                } else {
                    xTable.setHighlighters(HighlighterFactory.createSimpleStriping());
                }
                on = !on;
            }
            
        };
        addAction(frame, toggle);
    }
    
    public void interactiveDebug() {
        JLabel label = new RedCrossLabel();
        label.setHorizontalAlignment(JLabel.LEADING);
        label.setText("something to paint");
        JXFrame frame = wrapInFrame(label, "just paint");
        addComponentOrientationToggle(frame);
        show(frame);
    }

    public void interactiveDebugWithPainter() {
        JRendererLabel label = new JRendererLabel();
        label.setHorizontalAlignment(JLabel.LEADING);
        label.setText("something to paint");
        label.setPainter(new TextCrossingPainter<JRendererLabel>());
        JXFrame frame = wrapInFrame(label, "just paint with painter");
        addComponentOrientationToggle(frame);
        show(frame);
    }
    
    public static class RedCrossLabel extends JLabel {
        
        RedCrossIcon icon = new RedCrossIcon();
        Rectangle paintIconR = new Rectangle();
        Rectangle paintViewR = new Rectangle();
        Rectangle paintTextR = new Rectangle();
        Insets insetss = new Insets(0, 0, 0, 0);
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            int width = getWidth();
            int height = getHeight();
            Insets insets = getInsets(insetss);
            paintViewR.x = insets.left;
            paintViewR.y = insets.top;
            paintViewR.width = width - (insets.left + insets.right);
            paintViewR.height = height - (insets.top + insets.bottom);
            paintIconR.x = paintIconR.y = paintIconR.width = paintIconR.height = 0;
            paintTextR.x = paintTextR.y = paintTextR.width = paintTextR.height = 0;
            SwingUtilities.layoutCompoundLabel(this, getFontMetrics(getFont()), getText(), null,
                    getVerticalAlignment(), getHorizontalAlignment(), getVerticalTextPosition(), getHorizontalTextPosition(),
                    paintViewR, paintIconR, paintTextR, getIconTextGap());
            icon.setIconHeight(paintTextR.height);
            icon.setIconWidth(paintTextR.width);
            icon.paintIcon(this, g, paintTextR.x, paintTextR.y);

        }

        public static class RedCrossIcon implements Icon {

            private int height;
            private int width;

            public void setIconHeight(int height) {
                this.height = height;
            }
            
            public void setIconWidth(int width) {
                this.width = width;
            }
            
            public int getIconHeight() {
                return height;
            }

            public int getIconWidth() {
                return width;
            }

            public void paintIcon(Component c, Graphics g, int x, int y) {
                Color old = g.getColor();
                g.setColor(Color.RED);
                g.drawLine(x, y, x + width, y + height);
                g.drawLine(x + 1, y, x + width + 1, y + height);
                g.drawLine(x + width, y, x, y + height);
                g.drawLine(x + width - 1, y, x - 1, y + height);
                g.setColor(old);
            }
            
        };
        
    }
    
    public void interactiveMonthViewAntialised() {
        final JXMonthView monthView = new JXMonthView();
        JComponent comp = Box.createHorizontalBox();
        comp.add(monthView);
        comp.add(new JXMonthView());
        JXFrame frame = wrapInFrame(comp, "antialiased left (1.5)");
        show(frame);
    }
   

    public void interactiveMonthViewAntialisedPaint() {
        JXMonthView custom =  new JXMonthView() {
            
            @Override
            public void paint(Graphics g) {
                Toolkit tk = Toolkit.getDefaultToolkit ();
                Map<?, ?> map = (Map<?, ?>) (tk.getDesktopProperty ("awt.font.desktophints"));
                LOG.info("hints " + map);
                if (map != null) {
                    ((Graphics2D) g).addRenderingHints(map);
                }
                
                super.paint(g);
            }
            
        };
        
        final JXMonthView monthView = new JXMonthView();
        JComponent comp = Box.createHorizontalBox();
        comp.add(custom);
        comp.add(monthView);
        JXFrame frame = wrapInFrame(comp, "antialiased 1.6paint left");
        show(frame);
    }
    

    /**
     * Issue #913-swingx: Datepicker looks bad in some LFs (f.i. Nimbus)
     * 
     * not only Nimbus ...
     * - Metal: starts with different height than textfield, shrinks when coming
     *   back from another LF
     * - motif: button is shrunkenn always
     * - win: halfway okay, but should have buttons "nearer/integrated" to the field
     *   as spinner, combo does
     * - Vista: button should visually "merge" into field until rollover (as combo does,
     *   Spinner not
     * - Can't remember the reason why combo's button isn't re-used?
     *       
     */
    public void interactiveDatePickerNimbus() {
        LOG.info("label font: " + new JLabel().getFont());
        JXDatePicker picker = new JXDatePicker(new Date());
        JFormattedTextField field = new JFormattedTextField();
        field.setValue(picker.getDate());
        JSpinner spinner = new JSpinner(new SpinnerDateModel());
        spinner.setValue(picker.getDate());
        JComboBox box = new JComboBox(new Object[] {picker.getDate()});
        box.setEditable(true);
        JComponent panel = new JXPanel();
        panel.add(new JLabel("compare appearance/behaviour: "));
        panel.add(field);
        panel.add(picker);
        panel.add(spinner);
        panel.add(box);
        showInFrame(panel, "Nimbus and picker");
    }
    
    public void interactiveAlphaBackground() {
        Color color = ColorUtil.setAlpha(Color.ORANGE, 60);
        
        JCheckBox check = new JCheckBox("what's my color?");
//        check.setOpaque(true);
//        check.setContentAreaFilled(true);
        check.setBackground(color);
        JLabel label = new JLabel("and mine?");
        label.setOpaque(true);
        label.setBackground(color );
        JButton button = new JButton("the new kid on the block");
        button.setBackground(color);
        
        JRadioButton radio = new JRadioButton("radio, raadio ..");
        radio.setBackground(color);
        
        JTextField field = new JTextField(40);
        field.setBackground(color);
        JComponent box = Box.createVerticalBox();
        box.setBackground(Color.WHITE);
        box.add(check);
        box.add(radio);
        box.add(label);
        box.add(button);
        box.add(field);
        setOpaque(box);
        JXFrame frame = wrapInFrame(box, "alpha in plain ..", true);
        show(frame, 400, 200);
        
    }
    private void setOpaque(JComponent box) {
        for (int i = 0; i < box.getComponentCount(); i++) {
            ((JComponent) box.getComponent(i)).setOpaque(true);
        }
    }

    /**
     * Issue ??-swingx: Boolean renderer background is slightly darker if 
     * background color is part-transparent.
     * 
     */
    public void interactiveBooleanAlpha() {
        JXTable table = new JXTable(new AncientSwingTeam());
        LOG.info("table font" + table.getFont());
        table.getColumn(0).setCellRenderer(new DefaultTableRenderer(new HyperlinkProvider()));
        table.addHighlighter(new RowHighlighter(new HighlightPredicate() {
            public boolean isHighlighted(Component renderer, ComponentAdapter adapter) {
                return ((Boolean) adapter.getValue(4)).booleanValue();
            }
        }));
        showWithScrollingInFrame(table, "boolean renderer and alpha background");
        
    }

    static class RowHighlighter extends ColorHighlighter {
        Font BOLD_FONT;

        RowHighlighter(HighlightPredicate predicate) {
            super(predicate, ColorUtil.setAlpha(Color.ORANGE, 60), Color.RED);
        }

        @Override
        protected Component doHighlight(Component renderer, ComponentAdapter adapter) {
            renderer.setForeground(getForeground());
            if (BOLD_FONT == null) {
                BOLD_FONT = renderer.getFont().deriveFont(Font.BOLD);
            }
            renderer.setFont(BOLD_FONT);
            return super.doHighlight(renderer, adapter);
        }
    }

    public void interactiveNimbus() {
        JXTable table = new JXTable(new AncientSwingTeam());
        table.setHighlighters(
//                HighlighterFactory.createSimpleStriping(), 
                new ColorHighlighter(HighlightPredicate.ROLLOVER_ROW, 
                        Color.MAGENTA, null));
        showInFrame(new JScrollPane(table), "Nimbus rollover", true);
    }

    /**
     * Use highlighter with BusyPainter.
     */
    public void interactiveAnimatedBusyPainterHighlight()  {
        TableModel model = new AncientSwingTeam();
        JXTable table = new JXTable(model);
        table.getColumn(0).setCellRenderer(new DefaultTableRenderer(
                new HyperlinkProvider()));
        final BusyPainter busyPainter = new BusyPainter() {
            /**
             * Overridden to fix Issue #861-swingx: must notify on change
             * @param frame
             */
            @Override
            public void setFrame(int frame) {
                int old = getFrame();
                super.setFrame(frame);
                firePropertyChange("frame", old, getFrame());
            }
            
        };
        PainterHighlighter iconHighlighter = new PainterHighlighter();
        iconHighlighter.setHighlightPredicate(HighlightPredicate.ROLLOVER_ROW);
        iconHighlighter.setPainter(busyPainter);
        ActionListener l = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int frame = busyPainter.getFrame();
                frame = (frame+1)%busyPainter.getPoints();
                busyPainter.setFrame(frame);
            }
            
        };
        table.addHighlighter(iconHighlighter);
        table.addHighlighter(HighlighterFactory.createSimpleStriping());
        showWithScrollingInFrame(table, 
                "Animated highlighter: marching icon on rollover");
        Timer timer = new Timer(100, l);
        timer.start();
    }

}
