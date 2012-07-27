/*
 * $Id: TestFrame.java 2758 2008-10-09 10:51:35Z mattnathan $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */
package org.jdesktop.swingx;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GradientPaint;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.Border;

import org.jdesktop.swingx.painter.BackgroundPainter;
import org.jdesktop.swingx.painter.CheckerboardPainter;
import org.jdesktop.swingx.painter.MattePainterTmp;
import org.jdesktop.swingx.painter.Painter;
import org.jdesktop.swingx.util.*;

public class TestFrame extends JFrame implements ComponentListener {
    private String titleBase;
    private boolean frameSizeShowing = true;

    public TestFrame(String title, Component contents, Color background) {
        this(title, contents, new MattePainterTmp(background));
    }





    public TestFrame(String title, Component contents, Painter background) {
        this(title, contents);
        setBackgroundPainter(background);
    }





    @SuppressWarnings({"unchecked"})
    public TestFrame(String title, Component contents) {
        super(title);
        this.titleBase = title;
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        JXPanel contentPane = new JXPanel(new BorderLayout());
        contentPane.setBackgroundPainter(new CheckerboardPainter());
        contentPane.add(contents, BorderLayout.CENTER);
        setContentPane(contentPane);

        JMenuBar menuBar = new JMenuBar();
        JMenu windowMenu = new JMenu("Window");
        windowMenu.add(new AbstractAction("Pack") {
            public void actionPerformed(ActionEvent e) {
                pack();
            }
        });
        windowMenu.add(new AbstractAction("Size: 800x600") {
            public void actionPerformed(ActionEvent e) {
                setSize(800, 600);
            }
        });
        windowMenu.add(new AbstractAction("Size: 1024x768") {
            public void actionPerformed(ActionEvent e) {
                setSize(1024, 768);
            }
        });
        windowMenu.add(new AbstractAction("Size: 1280x1024") {
            public void actionPerformed(ActionEvent e) {
                setSize(1280, 1024);
            }
        });
        windowMenu.addSeparator();
        windowMenu.add(new AbstractAction("Center") {
            public void actionPerformed(ActionEvent e) {
                setLocationRelativeTo(null);
            }
        });

        JMenu lnfMenu = new JMenu("Look and Feel");
        LookAndFeelInfo[] lnfs = UIManager.getInstalledLookAndFeels();
        class LNFAction extends AbstractAction {
            private LookAndFeelInfo info;

            public LNFAction(LookAndFeelInfo info) {
                super(info.getName());
                this.info = info;
            }





            public void actionPerformed(ActionEvent e) {
                try {
                    String classname = info.getClassName();
                    /**
                     * Its a shame but they seem to have put the wrong classname into the info for nimbus
                     */
                    if ("sun.swing.plaf.nimbus.NimbusLookAndFeel".equals(classname)){
                        classname = "com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel";
                    }
                    UIManager.setLookAndFeel(classname);
                    SwingUtilities.updateComponentTreeUI(TestFrame.this);
                } catch (UnsupportedLookAndFeelException ex) {
                    assert false;
                } catch (IllegalAccessException ex) {
                    assert false;
                } catch (InstantiationException ex) {
                    assert false;
                } catch (ClassNotFoundException ex) {
                    //noinspection CallToPrintStackTrace
                    ex.printStackTrace();
                    assert false;
                }
            }
        }
        ButtonGroup lnfGroup = new ButtonGroup();
        for (LookAndFeelInfo info : lnfs) {
            JRadioButtonMenuItem item = new JRadioButtonMenuItem(new LNFAction(info));
            if (lnfGroup.getButtonCount() == 0) {
                item.setSelected(true);
            }
            lnfGroup.add(item);
            lnfMenu.add(item);
        }

        JMenu editMenu = new JMenu("Edit");
        editMenu.add(new JCheckBoxMenuItem(new AbstractAction("Border Spacing") {
            public void actionPerformed(ActionEvent e) {
                AbstractButton b = (AbstractButton) e.getSource();
                setBorderSpacingVisible(b.isSelected());
            }
        }));

        JMenu bgColorMenu = new JMenu("Background");
        Binding[] bgPainters = {
                               new Binding<String, Painter>("Look and Feel", new BackgroundPainter()),
                               new Binding<String, Painter>("Checked", new CheckerboardPainter()),
                               null,
                               new Binding<String, Painter>("White", new MattePainterTmp(Color.WHITE)),
                               new Binding<String, Painter>("Black", new MattePainterTmp(Color.BLACK)),
                               new Binding<String, Painter>("Red", new MattePainterTmp(Color.RED)),
                               new Binding<String, Painter>("Green", new MattePainterTmp(Color.GREEN)),
                               new Binding<String, Painter>("Blue", new MattePainterTmp(Color.BLUE)),
                               new Binding<String, Painter>("Yellow", new MattePainterTmp(Color.YELLOW)),
                               new Binding<String, Painter>("Magenta", new MattePainterTmp(Color.MAGENTA)),
                               new Binding<String, Painter>("Cyan", new MattePainterTmp(Color.CYAN)),
                               null,
                               new Binding<String, Painter>("Aerith", new MattePainterTmp(PaintUtils.AERITH)),
                               new Binding<String, Painter>("Blue Experience", new MattePainterTmp(PaintUtils.BLUE_EXPERIENCE)),
                               new Binding<String, Painter>("Gray", new MattePainterTmp(PaintUtils.GRAY)),
                               new Binding<String, Painter>("Mac OSX", new MattePainterTmp(PaintUtils.MAC_OSX)),
                               new Binding<String, Painter>("Mac OSX Selected", new MattePainterTmp(PaintUtils.MAC_OSX_SELECTED)),
                               new Binding<String, Painter>("Night Gray", new MattePainterTmp(PaintUtils.NIGHT_GRAY)),
                               new Binding<String, Painter>("Night Gray Light", new MattePainterTmp(PaintUtils.NIGHT_GRAY_LIGHT)),
                               new Binding<String, Painter>("Red XP", new MattePainterTmp(PaintUtils.RED_XP)),
                               new Binding<String, Painter>("Seascape",
              new MattePainterTmp(new GradientPaint(0, 0.47f, new Color(20, 114, 168),
              0, 1, new Color(140, 253, 249), false)))
        };
        class PainterAction extends AbstractAction {
            private Painter painter;

            public PainterAction(String name, Painter painter) {
                super(name);
                this.painter = painter;
            }





            public void actionPerformed(ActionEvent e) {
                setBackgroundPainter(painter);
            }
        }

        for (Binding<String, Painter> b : bgPainters) {
            if (b == null) {
                bgColorMenu.addSeparator();
            } else {
                JMenuItem item = new JMenuItem(new PainterAction(b.getValue1(), b.getValue2()));
                bgColorMenu.add(item);
            }
        }
        bgColorMenu.addSeparator();
        bgColorMenu.add(new AbstractAction("Choose Color...") {
            public void actionPerformed(ActionEvent e) {
                Color newColor = JColorChooser.showDialog(TestFrame.this, "Select Background Color", Color.white);
                if (newColor != null) {
                    setBackgroundPainter(new MattePainterTmp(newColor));
                }
            }
        });
        editMenu.add(bgColorMenu);

        menuBar.add(editMenu);
        menuBar.add(lnfMenu);
        menuBar.add(windowMenu);

        setJMenuBar(menuBar);

        pack();
        setLocationRelativeTo(null);
        addComponentListener(this);
    }





    private static Border spaceBorder = BorderFactory.createEmptyBorder(12, 12, 12, 12);

    public void setBorderSpacingVisible(boolean b) {
        if (b) {
            ((JComponent) getContentPane()).setBorder(spaceBorder);
        } else {
            ((JComponent) getContentPane()).setBorder(null);
        }

    }





    public void setBackgroundPainter(Painter painter) {
        ((JXPanel) getContentPane()).setBackgroundPainter(painter);
    }





    public void center() {
        setLocationRelativeTo(null);
    }





    public void componentResized(ComponentEvent e) {
        if (isFrameSizeShowing()) {
            setTitle(titleBase + " [" + getWidth() + " x " + getHeight() + ']');
        }
    }





    public void componentMoved(ComponentEvent e) {
    }





    public void componentShown(ComponentEvent e) {
    }





    public void componentHidden(ComponentEvent e) {
    }





    public boolean isFrameSizeShowing() {
        return frameSizeShowing;
    }





    public void setVisible(boolean b, int w, int h) {
        setSize(w, h);
        setLocationRelativeTo(null);
        super.setVisible(b);
    }





    public void setFrameSizeShowing(boolean frameSizeShowing) {
        boolean old = isFrameSizeShowing();
        if (old != frameSizeShowing) {
            this.frameSizeShowing = frameSizeShowing;
            if (frameSizeShowing) {
                componentResized(null);
            } else {
                setTitle(titleBase);
            }
            firePropertyChange("frameSizeShowing", old, isFrameSizeShowing());
        }
    }





    /**
     * Simple class which creates a binding between two objects.
     *
     * @author <a href="mailto:mattnathan@dev.java.net">Matt Nathan</a>
     * @version 1.0
     */
    private static class Binding<T1, T2> {

        /**
         * The object to bind from.
         */
        protected T1 value1;
        /**
         * The object to bind to.
         */
        protected T2 value2;

        /**
         * Create a new binding from value1 to value 2. value2 cannot be null.
         *
         * @param value1 T1
         * @param value2 T2
         * @throws IllegalArgumentException when value2 == null.
         */
        public Binding(T1 value1, T2 value2) {
            if (value2 == null) {
                throw new IllegalArgumentException("Cannot bind to a null value");
            }
            this.value1 = value1;
            this.value2 = value2;
        }





        /**
         * The the binding source.
         *
         * @return T1
         */
        public T1 getValue1() {
            return value1;
        }





        /**
         * Get the binding target.
         *
         * @return T2
         */
        public T2 getValue2() {
            return value2;
        }

    }

}
