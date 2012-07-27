package org.jdesktop.swingx;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Shape;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.UIManager;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.metal.MetalComboBoxButton;
import javax.swing.plaf.metal.MetalComboBoxUI;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.text.StyledEditorKit.ForegroundAction;

import org.jdesktop.swingx.JXBusyLabel;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.icon.EmptyIcon;
import org.jdesktop.swingx.painter.BusyPainter;
import org.jdesktop.swingx.painter.MattePainter;
import org.jdesktop.swingx.util.PaintUtils;

public class BusyDemo extends JFrame {
    public class GenerateCodeAction extends AbstractAction {
        public GenerateCodeAction() {
            super("Copy Code");
        }

        public void actionPerformed(ActionEvent e) {

            StringBuilder code = new StringBuilder();
            code.append("JXBusyLabel label = new JXBusyLabel(new Dimension(");
            code.append(label.getWidth()).append(",").append(label.getHeight());
            code.append("));\n");
            // General Path must be prepared upfront if used
            Object item = trajectoryCombo.getSelectedItem();
            float xFactor = xSlider.getValue();
            float yFactor = ySlider.getValue();
            xFactor /= 100f;
            yFactor /= 100f;
            int height = label.getHeight();
            float barLength = (height * 8) / 26;
            StringBuilder gp = new StringBuilder();
            if ("Quad".equals(item)) {
                gp.append("GeneralPath gp = new GeneralPath();\n");
                float hh = ((float) height) / 2;
                gp.append("gp.moveTo(").append(barLength).append(",")
                        .append(hh).append(");\n");
                gp.append("gp.quadTo(").append(hh / 2).append(", 0, ").append(
                        hh * 2 - barLength).append(", ").append(hh).append(
                        ");\n");
                gp.append("gp.quadTo(").append(hh / 2).append(", ").append(
                        hh * 2f).append(", ").append(barLength).append(", ")
                        .append(hh).append(");");
            }
            // GP End
            code.append("BusyPainter painter = new BusyPainter(\n");

            // point shape
            item = pointShapeCombo.getSelectedItem();
            xFactor = pxSlider.getValue();
            xFactor /= 100;
            yFactor = pySlider.getValue();
            yFactor /= 100;
            if ("Rect".equals(item)) {
                code.append("new Rectangle2D.Float(0,0,").append(
                        xFactor * CNSTS).append("f,").append(
                        yFactor * CNSTS / 5).append("f)");
            } else if ("RoundRect".equals(item)) {
                code.append("new RoundRectangle2D.Float(0, 0,").append(
                        xFactor * CNSTS).append("f,").append(
                        yFactor * CNSTS / 5).append("f,").append(CNSTS / 5)
                        .append("f,").append(CNSTS / 5).append("f)");
            } else if ("Ellipse".equals(item)) {
                code.append("new Ellipse2D.Float(0, 0,")
                        .append(xFactor * CNSTS).append("f,").append(
                                yFactor * CNSTS).append("f)");
            } else if ("Square".equals(item)) {
                code.append("new Rectangle2D.Float(0, 0,").append(
                        xFactor * CNSTS).append("f,").append(xFactor * CNSTS)
                        .append("f)");
            } else if ("Line".equals(item)) {
                code.append("new Rectangle2D.Float(0, 0,").append(
                        xFactor * CNSTS).append("f,1)");
            }
            code.append(",\n");
            // trajectory shape
            xFactor = xSlider.getValue();
            yFactor = ySlider.getValue();
            xFactor /= 100f;
            yFactor /= 100f;
            height = label.getHeight();
            barLength = (height * 8) / 26;
            item = trajectoryCombo.getSelectedItem();
            if ("Ellipse".equals(item)) {
                code.append("new Ellipse2D.Float(").append(barLength / 2)
                        .append("f,").append(barLength / 2).append("f,")
                        .append((height - barLength) * xFactor).append("f,")
                        .append((height - barLength) * yFactor).append("f)");
            } else if ("Quad".equals(item)) {
                code.append(gp);
            } else if ("Rect".equals(item)) {
                code.append("new Rectangle2D.Float(").append(barLength / 2)
                        .append("f,").append(barLength / 2).append("f,")
                        .append((height - barLength) * xFactor).append("f,")
                        .append((height - barLength) * yFactor).append("f)");
            } else if ("RoundRect".equals(item)) {
                code.append("new RoundRectangle2D.Float(")
                        .append(barLength / 2).append("f,").append(
                                barLength / 2).append("f,").append(
                                (height - barLength) * xFactor).append("f,")
                        .append((height - barLength) * yFactor).append("f,")
                        .append(10).append(",").append(10).append(")");
            } else {// line
                code.append("new Rectangle2D.Float(").append(barLength / 2)
                        .append("f,").append(barLength / 2).append("f,")
                        .append((height - barLength) * xFactor).append("f,")
                        .append(1).append(")");
            }

            code.append(");\n");
            BusyPainter old = label.getBusyPainter();
            code.append("painter.setTrailLength(").append(old.getTrailLength())
                    .append(");\n");
            code.append("painter.setPoints(").append(old.getPoints()).append(
                    ");\n");
            code.append("painter.setFrame(").append(old.getFrame()).append(
                    ");\n");
            // size
            float wFactor = wSlider.getValue();
            float hFactor = hSlider.getValue();
            wFactor /= 100f;
            hFactor /= 100f;
            // System.out.println("wF:" + wFactor);
            // System.out.println("hF:" + hFactor);
            code.append("label.setPreferredSize(new Dimension(").append(
                    (int) (W * wFactor)).append(",")
                    .append((int) (H * hFactor)).append("));\n");
            code.append("label.setIcon(new EmptyIcon(").append(
                    (int) (W * wFactor)).append(",")
                    .append((int) (H * hFactor)).append("));\n");
            code.append("label.setBusyPainter(painter);\n");

            Clipboard clipboard = Toolkit.getDefaultToolkit()
                    .getSystemClipboard();
            clipboard.setContents(new StringSelection(code.toString()), null);
        }
    }

    public class DetailsPane extends JXPanel {


        private GridBagConstraints gbc = new GridBagConstraints(1, 0, 1, 1, .5, .5, GridBagConstraints.NORTHEAST,
                GridBagConstraints.HORIZONTAL, new Insets(1, 1, 3, 4), 0, 0);
        private Color foreground;

        public DetailsPane() {
            setLayout(new GridBagLayout());
            setOpaque(false);
        }
        
        public DetailsPane(String title, Color c) {
            this();
            if (title != null) {
                setBorder(new CompoundBorder(BorderFactory.createTitledBorder(null, title, TitledBorder.LEADING, TitledBorder.TOP, null, c ),BorderFactory.createEmptyBorder(3, 5, 5, 5)));
            }
            this.foreground = c;
        }

        public void addLine(JComponent one, JComponent two) {
            addLine(one, two, true);
        }
        public void addLine(JComponent one, JComponent two, boolean fill) {
            gbc.gridx = 1;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.gridy++;
            gbc.anchor = GridBagConstraints.NORTHEAST;
            if (one != null) {
                add(one, gbc);
                if (foreground != null) {
                    one.setForeground(this.foreground);
                }
            }
            gbc.gridx = 2;
            gbc.fill = fill ? GridBagConstraints.BOTH : GridBagConstraints.VERTICAL;
            gbc.anchor = GridBagConstraints.NORTHWEST;
            if (two != null) { 
                add(two, gbc);
                if (foreground != null) {
                    two.setForeground(this.foreground);
                }
            }
        }

        public void addLine(boolean fill, JComponent... list) {
            gbc.gridx = 0;
            gbc.gridy++;
            gbc.anchor = GridBagConstraints.NORTHEAST;
            boolean odd =true;
            for (JComponent c : list) {
                gbc.gridx++;
                if (odd) {
                    gbc.fill = GridBagConstraints.HORIZONTAL;
                    if (c != null) {
                        add(c, gbc);
                        if (foreground != null) {
                            c.setForeground(this.foreground);
                        }
                    }
                } else {
                    gbc.fill = fill ? GridBagConstraints.BOTH : GridBagConstraints.VERTICAL;
                    gbc.anchor = GridBagConstraints.NORTHWEST;
                    if (c != null) { 
                        add(c, gbc);
                        if (foreground != null) {
                            c.setForeground(this.foreground);
                        }
                    }
                }
                odd = !odd;
            }
        }

        public void addLine(JComponent one) {
            gbc.gridx = 1;
            gbc.gridy++;
            gbc.gridwidth = 2;
            gbc.anchor = GridBagConstraints.NORTHEAST;
            if (one != null) {
                add(one, gbc);
            }
            gbc.gridwidth = 1;
        }
        public void addLine(String one, String two) {
            addLine(new JLabel(one), new JLabel(two), true);
        }

        public void addLine(String one, String two, boolean fill) {
            addLine(new JLabel(one), new JLabel(two), fill);
        }

        public void addLine(String one, JComponent two) {
            addLine(one, two, true);
        }
        public void addLine(String one, JComponent two, boolean fill) {
            addLine(new JLabel(one), two, fill);
        }

        public void addLast(JComponent one, JComponent two) {
            gbc.gridx = 1;
            gbc.gridy++;
            gbc.weighty = 100;
            gbc.anchor = GridBagConstraints.NORTHEAST;
            if (one != null) {
                add(one, gbc);
            }
            gbc.gridx = 2;
            gbc.anchor = GridBagConstraints.NORTHWEST;
            if (two != null) { 
                add(two, gbc);
            }
        }

        public void addLast(JComponent one) {
            gbc.gridx = 1;
            gbc.gridy++;
            gbc.weighty = 100;
            gbc.gridwidth = 2;
            gbc.anchor = GridBagConstraints.NORTHEAST;
            if (one != null) {
                add(one, gbc);
            }
            gbc.gridwidth = 1;
        }
        public void addLast(String one, String two) {
            addLast(new JLabel(one), new JLabel(two));
        }
        @Override
        public void removeAll() {
            super.removeAll();
            gbc = new GridBagConstraints(1, 0, 1, 1, .5, .5, GridBagConstraints.NORTHEAST,
                    GridBagConstraints.HORIZONTAL, new Insets(1, 1, 1, 1), 0, 0);
        }
    }
    
    private static final Color FOREGROUND = new Color(168, 204, 241).brighter();

    private static final float W = 100;

    private static final float H = 100;

    private static final float CNSTS = 50;
    
    public class ComboBoxUI extends MetalComboBoxUI {
        @Override
        public PropertyChangeListener createPropertyChangeListener() {
            return new MetalPropertyChangeListener();
        }
        public class MetalPropertyChangeListener extends BasicComboBoxUI.PropertyChangeHandler {
            public void propertyChange(PropertyChangeEvent e) {
                super.propertyChange( e );
                String propertyName = e.getPropertyName();

                if ( propertyName == "background" ) {
                    Color color = (Color)e.getNewValue();
                    listBox.setBackground(color);
                    
                } else if ( propertyName == "foreground" ) {
                    Color color = (Color)e.getNewValue();
                    listBox.setForeground(color);
                }
            }
        }

    }
    public class FactorChangeListener implements ChangeListener, ItemListener {

        public void stateChanged(ChangeEvent ce) {
            int height = label.getHeight();
            int width = label.getWidth();
            float barLength = (height * 8) / 26;
            float xFactor = xSlider.getValue();
            float yFactor = ySlider.getValue();
            float wFactor = wSlider.getValue();
            final float hFactorO = hSlider.getValue();
            xFactor /= 100f;
            yFactor /= 100f;
            wFactor /= 100f;
            float hFactor = hFactorO / 100f;
            Shape trajectory = null;
            Object item = trajectoryCombo.getSelectedItem();
            if ("Ellipse".equals(item)) {
                trajectory = new Ellipse2D.Float(barLength / 2, barLength / 2,
                        (width - barLength) * xFactor, (height - barLength)
                                * yFactor);
            } else if ("Rect".equals(item)) {
                trajectory = new Rectangle2D.Float(barLength / 2,
                        barLength / 2, (width - barLength) * xFactor,
                        (height - barLength) * yFactor);
            } else if ("RoundRect".equals(item)) {
                trajectory = new RoundRectangle2D.Float(barLength / 2,
                        barLength / 2, (width - barLength) * xFactor,
                        (height - barLength) * yFactor, 10, 10);
            } else if ("Quad".equals(item)) {
                GeneralPath gp = new GeneralPath();
                float hh = ((float) height) / 2;
                float wh = ((float) width) / 2;
                gp.moveTo(barLength, hh);
                gp.quadTo(wh / 2, 0, wh * 2 - barLength, hh);
                gp.quadTo(wh / 2, hh * 2f, barLength, hh);
                trajectory = gp;

            } else {// line
                trajectory = new Rectangle2D.Float(barLength / 2,
                        barLength / 2, (width - barLength) * xFactor, 1);
            }

            Shape pointShape = getPointShape();
            BusyPainter bp = new BusyPainter(pointShape, trajectory);
            BusyPainter old = label.getBusyPainter();
            bp.setTrailLength(old.getTrailLength());
            bp.setPoints(old.getPoints());
            bp.setFrame(old.getFrame());
            label.setPreferredSize(new Dimension((int) (W * wFactor),
                    (int) (H * hFactor)));
            label.setIcon(new EmptyIcon((int) (W * wFactor),
                    (int) (H * hFactor)));
            label.setBusyPainter(bp);
            // there's bug somewhere - can't set colors before setting painter
            // on label
            bp.setHighlightColor(old.getHighlightColor());
            bp.setBaseColor(old.getBaseColor());

            label.repaint();
        }

        public void itemStateChanged(ItemEvent e) {
            stateChanged(null);
        }

    }

    public static void main(String[] args) {
        BusyDemo d = new BusyDemo();
        d.setDefaultCloseOperation(EXIT_ON_CLOSE);
        d.setPreferredSize(new Dimension(500, 530));
        d.pack();
        d.setVisible(true);
        d.fcl.itemStateChanged(null);
        d.label.setBusy(false);

    }

    private JXBusyLabel label;

    private JXPanel control;

    private JSlider xSlider;

    private JSlider ySlider;

    private JSlider wSlider;

    private JSlider hSlider;

    private JSlider pxSlider;

    private JSlider pySlider;

    JComboBox trajectoryCombo = new JComboBox(new Object[] { "Ellipse", "Quad",
            "RoundRect", "Rect", "Line" });

    JComboBox pointShapeCombo = new JComboBox(new Object[] { "RoundRect", "Rect", "Ellipse",
            "Square", "Line" });

    FactorChangeListener fcl = new FactorChangeListener();

    public BusyDemo() {
        // have to use custom ui due to the fact default metal ui sets background for arrow button together with background for list
        trajectoryCombo.setUI(new ComboBoxUI());
        pointShapeCombo.setUI(new ComboBoxUI());
        JXPanel content = new JXPanel(new BorderLayout());
        getContentPane().add(content);
        MattePainter bkground = new MattePainter(PaintUtils.BLUE_EXPERIENCE,
                true);
        content.setBackgroundPainter(bkground);
        // set average of gradient for the combo list background
        Color c1 = PaintUtils.BLUE_EXPERIENCE.getColor1();
        Color c2 = PaintUtils.BLUE_EXPERIENCE.getColor2();
        Color c = new Color((c1.getRed() - c2.getRed())/2 + c2.getRed(),
                (c1.getGreen() - c2.getGreen())/2 + c2.getGreen(),
                (c1.getBlue() - c2.getBlue())/2 + c2.getBlue());
        initCombo(pointShapeCombo, c);
        initCombo(trajectoryCombo, c);
        
        label = new JXBusyLabel(new Dimension((int) (W / 2), (int) (H / 2)));
        label.getBusyPainter().setHighlightColor(
                new Color(44, 61, 146).darker());
        label.getBusyPainter()
                .setBaseColor(new Color(168, 204, 241).brighter());
        label.setOpaque(false);
        label.setHorizontalAlignment(JLabel.CENTER);
        JXHeader header = new JXHeader(
                "Busy Label Demo",
                "Drag slider controls to change the shape and speed of the animation.\n"
                        + "Click the Copy Code button to generate the corresponding Java code.");
        content.add(header, BorderLayout.NORTH);
        header.setOpaque(false);
        JPanel center = new JPanel();
        center.add(label);
        center.setOpaque(false);
        content.add(center, BorderLayout.CENTER);
        control = new JXPanel(new GridBagLayout());
        control.setBorder(new EmptyBorder(10, 10, 10, 10));
        control.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints(1, 1, 2, 1, 1, 1,
                GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 0, 0), GridBagConstraints.NONE,
                GridBagConstraints.NONE);
        content.add(control, BorderLayout.SOUTH);

        DetailsPane genPane = new DetailsPane("General settings:", FOREGROUND);
        control.add(genPane, gbc);
        final int SPEED_MAX = 51;
        final JSlider speedSlider = getSlider(1, SPEED_MAX - 1, SPEED_MAX - label.getDelay() / 10);
        genPane.addLine("Speed:", speedSlider);
        speedSlider.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent e) {
                label.setDelay((SPEED_MAX - speedSlider.getValue()) * 10);
                label.repaint();
            }
        });
        final JSlider slider = getSlider(1, 50, label.getBusyPainter()
                .getPoints());
        genPane.addLine("Points Count:", slider);
        slider.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent e) {
                label.getBusyPainter().setPoints(slider.getValue());
                label.repaint();
            }
        });
        final JSlider tSlider = getSlider(1, 20, label.getBusyPainter()
                .getTrailLength());
        genPane.addLine("Trail Length:", tSlider);
        tSlider.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent e) {
                label.getBusyPainter().setTrailLength(tSlider.getValue());
                label.repaint();
            }
        });
        DetailsPane base = new DetailsPane(null, FOREGROUND);
        JXColorSelectionButton baseColBtn = new JXColorSelectionButton();
        baseColBtn.setBackground(label.getBusyPainter().getBaseColor());
        baseColBtn.addPropertyChangeListener("background", new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                label.getBusyPainter().setBaseColor((Color) evt.getNewValue());
                label.repaint();
            }});
        JXColorSelectionButton highColBtn = new JXColorSelectionButton();
        highColBtn.setBackground(label.getBusyPainter().getHighlightColor());
        highColBtn.addPropertyChangeListener("background", new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                label.getBusyPainter().setHighlightColor((Color) evt.getNewValue());
                label.repaint();
            }});

        JXColorSelectionButton backColBtn = new JXColorSelectionButton();
        backColBtn.setBackground(PaintUtils.BLUE_EXPERIENCE.getColor1());
        backColBtn.addPropertyChangeListener("background", new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                label.setBackground((Color) evt.getNewValue());
                label.setOpaque(true);
            }});
        base.addLine(false, new JLabel("Base color:"), baseColBtn,new JLabel("Highlight color:"),
        highColBtn,new JLabel("Background:"),backColBtn);

        genPane.addLine(base);
        gbc.gridy++;
        gbc.gridwidth = 1;
        DetailsPane trajectoryPane = new DetailsPane("Trajectory:", FOREGROUND);
        control.add (trajectoryPane, gbc);
        trajectoryPane.addLine("Shape:", trajectoryCombo);
        trajectoryCombo.addItemListener(fcl);
        xSlider = getSlider(1, 100, 100);
        trajectoryPane.addLine("x shift:", xSlider);
        xSlider.addChangeListener(fcl);
        ySlider = getSlider(1, 100, 100);
        trajectoryPane.addLine("y shift:", ySlider);
        ySlider.addChangeListener(fcl);
        wSlider = getSlider(1, 100, 50);
        trajectoryPane.addLine("width:", wSlider);
        wSlider.addChangeListener(fcl);
        hSlider = getSlider(1, 100, 50);
        trajectoryPane.addLine("height:", hSlider);
        hSlider.addChangeListener(fcl);

        // points
        DetailsPane pointsPane = new DetailsPane("Point shape:", FOREGROUND);
        gbc.gridx++;
        control.add(pointsPane, gbc);
        pointShapeCombo.addItemListener(fcl);
        pointsPane.addLine("Shape:", pointShapeCombo);
        
        pxSlider = getSlider(1, 100, 30);
        pointsPane.addLine("width:", pxSlider);
        pxSlider.addChangeListener(fcl);

        pySlider = getSlider(1, 100, 50);
        pointsPane.addLine("height:", pySlider);
        pySlider.addChangeListener(fcl);

        DetailsPane buttons = new DetailsPane();

        gbc.gridy++;
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        control.add(buttons, gbc);
        buttons.addLine(new JButton(new AbstractAction("Start") {
            public void actionPerformed(ActionEvent e) {
                label.setBusy(!label.isBusy());
                putValue(Action.NAME, label.isBusy() ? "Stop" : "Start");
            }
        }), new JButton(new GenerateCodeAction()));
    }

    private void initCombo(JComboBox combo, Color c) {
        combo.setOpaque(false);
        combo.setBackground(c);
        combo.setRenderer(new DefaultListCellRenderer() {
            public boolean isOpaque() {
                return false;
            }

        });
    }

    private JSlider getSlider(int i, int j, int k) {
        JSlider s = new JSlider(i, j, k);
        s.setOpaque(false);
        return s;
    }

    private Shape getPointShape() {
        Object item = pointShapeCombo.getSelectedItem();
        float xFactor = pxSlider.getValue();
        xFactor /= 100;
        float yFactor = pySlider.getValue();
        yFactor /= 100;
        Shape s = null;
        if ("Rect".equals(item)) {
            s = new Rectangle2D.Float(0, 0, xFactor * CNSTS, yFactor * CNSTS
                    / 5);
        } else if ("RoundRect".equals(item)) {
            s = new RoundRectangle2D.Float(0, 0, xFactor * CNSTS, yFactor
                    * CNSTS / 5, CNSTS / 5, CNSTS / 5);
        } else if ("Ellipse".equals(item)) {
            s = new Ellipse2D.Float(0, 0, xFactor * CNSTS, yFactor * CNSTS);
        } else if ("Square".equals(item)) {
            s = new Rectangle2D.Float(0, 0, xFactor * CNSTS, xFactor * CNSTS);
        } else if ("Line".equals(item)) {
            s = new Rectangle2D.Float(0, 0, xFactor * CNSTS, 1);
        }

        return s;
    }
}
