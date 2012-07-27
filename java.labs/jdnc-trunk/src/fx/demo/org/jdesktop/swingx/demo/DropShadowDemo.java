/*
 * $Id$
 *
 * Copyright 2005 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */

package org.jdesktop.swingx.demo;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.jdesktop.swingx.CheckerboardPanel;
import org.jdesktop.swingx.DropShadowPanel;
import org.jdesktop.swingx.StackLayout;

/**
 * See method {@link #buildViewPane()} to see how to use
 * {@link org.jdesktop.swingx.StackLayout},
 * {@link org.jdesktop.swingx.CheckerboardPanel},
 * {@link org.jdesktop.swingx.DropShadowPanel} and
 * {@link org.jdesktop.swingx.graphics.ShadowRenderer}.
 *  
 * @author Romain Guy <romain.guy@mac.com>
 */

public class DropShadowDemo extends JFrame {
    private DropShadowPanel dropShadowPanel;
    private JLabel picture;
    private Color labelColor = Color.BLACK;

    public DropShadowDemo() throws HeadlessException {
        super("Drop Shadow");
        buildContentPane();

        setSize(new Dimension(640, 480));
        setLocationRelativeTo(null);
        
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private void buildContentPane() {
        JPanel viewPane = buildViewPane();
        //JPanel debugPane = buildDebugPane();
        
        add(new JScrollPane(viewPane), BorderLayout.CENTER);
        //add(debugPane, BorderLayout.EAST);
    }

    private JPanel buildDebugPane() {
        JPanel panel = new JPanel(new GridBagLayout());
        JSlider slider;
        JLabel label;
        
        panel.add(label = new JLabel("Angle:"),
                  new GridBagConstraints(0, 0,
                                         1, 1,
                                         1.0, 0.0,
                                         GridBagConstraints.LINE_START,
                                         GridBagConstraints.NONE, 
                                         new Insets(0, 6, 0, 0),
                                         0, 0));
        label.setForeground(labelColor);
        panel.add(slider = new JSlider(0, 360, 30),
                  new GridBagConstraints(0, 1,
                                         1, 1,
                                         1.0, 0.0,
                                         GridBagConstraints.LINE_START,
                                         GridBagConstraints.NONE, 
                                         new Insets(0, 6, 0, 6),
                                         0, 0));
        slider.setOpaque(false);
        slider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                final int value = ((JSlider) e.getSource()).getValue();
                new Thread(new Runnable() {
                    public void run() {
                        dropShadowPanel.setAngle(value);
                    }
                }).start();
            }
        });
        panel.add(label = new JLabel("Distance:"),
                  new GridBagConstraints(0, 2,
                                         1, 1,
                                         1.0, 0.0,
                                         GridBagConstraints.LINE_START,
                                         GridBagConstraints.NONE, 
                                         new Insets(0, 6, 0, 0),
                                         0, 0));
        label.setForeground(labelColor);
        panel.add(slider = new JSlider(1, 80, 5),
                  new GridBagConstraints(0, 3,
                                         1, 1,
                                         1.0, 0.0,
                                         GridBagConstraints.LINE_START,
                                         GridBagConstraints.NONE, 
                                         new Insets(0, 6, 0, 6),
                                         0, 0));
        slider.setOpaque(false);
        slider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                final int value = ((JSlider) e.getSource()).getValue();
                new Thread(new Runnable() {
                    public void run() {
                        dropShadowPanel.setDistance(value);
                    }
                }).start();
            }
        });
        panel.add(label = new JLabel("Size:"),
                  new GridBagConstraints(0, 4,
                                         1, 1,
                                         1.0, 0.0,
                                         GridBagConstraints.LINE_START,
                                         GridBagConstraints.NONE, 
                                         new Insets(0, 6, 0, 0),
                                         0, 0));
        label.setForeground(labelColor);
        panel.add(slider = new JSlider(1, 40, 5),
                  new GridBagConstraints(0, 5,
                                         1, 1,
                                         1.0, 0.0,
                                         GridBagConstraints.LINE_START,
                                         GridBagConstraints.NONE, 
                                         new Insets(0, 6, 0, 6),
                                         0, 0));
        slider.setOpaque(false);
        slider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                JSlider slider = (JSlider) e.getSource();
                if (slider.getValueIsAdjusting()) {
                    return;
                }
                final int value = slider.getValue();
                new Thread(new Runnable() {
                    public void run() {
                        dropShadowPanel.getShadowFactory().setSize(value);
                        dropShadowPanel.revalidate();
                    }
                }).start();
            }
        });
        panel.add(label = new JLabel("Opacity"),
                  new GridBagConstraints(0, 6,
                                         1, 1,
                                         1.0, 0.0,
                                         GridBagConstraints.LINE_START,
                                         GridBagConstraints.NONE, 
                                         new Insets(0, 6, 0, 0),
                                         0, 0));
        label.setForeground(labelColor);
        panel.add(slider = new JSlider(0, 100, 50),
                  new GridBagConstraints(0, 7,
                                         1, 1,
                                         1.0, 0.0,
                                         GridBagConstraints.LINE_START,
                                         GridBagConstraints.NONE, 
                                         new Insets(0, 6, 0, 6),
                                         0, 0));
        slider.setOpaque(false);
        slider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                JSlider slider = (JSlider) e.getSource();
                if (slider.getValueIsAdjusting()) {
                    return;
                }
                final int value = slider.getValue();
                new Thread(new Runnable() {
                    public void run() {
                        dropShadowPanel.getShadowFactory().setOpacity(value / 100.0f);
                    }
                }).start();
            }
        });
        panel.add(label = new JLabel("Color:"),
                  new GridBagConstraints(0, 8,
                                         1, 1,
                                         1.0, 0.0,
                                         GridBagConstraints.LINE_START,
                                         GridBagConstraints.NONE, 
                                         new Insets(0, 6, 0, 0),
                                         0, 0));
        label.setForeground(labelColor);
        JButton button;
        panel.add(button = new JButton(" "),
                  new GridBagConstraints(0, 9,
                                         1, 1,
                                         1.0, 0.0,
                                         GridBagConstraints.LINE_START,
                                         GridBagConstraints.HORIZONTAL, 
                                         new Insets(0, 6, 0, 6),
                                         0, 0));
        button.setBackground(dropShadowPanel.getShadowFactory().getColor());
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                final JButton source = (JButton) e.getSource();
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        Color color = JColorChooser.showDialog(DropShadowDemo.this,
                                                               "Shadow Color",
                                                               source.getBackground());
                        if (color != null) {
                            source.setBackground(color);
                            dropShadowPanel.getShadowFactory().setColor(color);
                        }
                    }
                });
            }
        });
        //button.setOpaque(false);
        panel.add(label = new JLabel("Picture:"),
                  new GridBagConstraints(0, 10,
                                         1, 1,
                                         1.0, 0.0,
                                         GridBagConstraints.LINE_START,
                                         GridBagConstraints.NONE, 
                                         new Insets(0, 6, 0, 0),
                                         0, 0));
        label.setForeground(labelColor);
        panel.add(button = new JButton("[Internal]"),
                  new GridBagConstraints(0, 11,
                                         1, 1,
                                         1.0, 0.0,
                                         GridBagConstraints.LINE_START,
                                         GridBagConstraints.HORIZONTAL, 
                                         new Insets(0, 6, 0, 6),
                                         0, 0));
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                final JButton source = (JButton) e.getSource();
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        JFileChooser chooser = new JFileChooser();
                        int result = chooser.showOpenDialog(DropShadowDemo.this);
                        if (result == JFileChooser.APPROVE_OPTION) {
                            File file = chooser.getSelectedFile();
                            source.setText(file.getName());
                            picture.setIcon(new ImageIcon(file.getAbsolutePath()));
                        }
                    }
                });
            }
        });
        label.setForeground(labelColor);
        panel.add(Box.createVerticalGlue(),
                  new GridBagConstraints(0, 12,
                                         1, 1,
                                         1.0, 1.0,
                                         GridBagConstraints.LINE_START,
                                         GridBagConstraints.NONE, 
                                         new Insets(0, 0, 0, 0),
                                         0, 0));
        return panel;
    }

    private JPanel buildViewPane() {
        JPanel panel = new JPanel(new StackLayout());
        panel.setOpaque(false);
        
        picture = new JLabel(new ImageIcon(getClass().getResource("images/subject.png"))); 
        dropShadowPanel = new DropShadowPanel(new BorderLayout());
        dropShadowPanel.setDistance(10);
        dropShadowPanel.add(picture);

        JPanel buttons = new JPanel();
        buttons.setOpaque(false);
        JButton button;
        buttons.add(button = new JButton("About"));
        button.setOpaque(false);
        buttons.add(button = new JButton("Exit"));
        button.setOpaque(false);

        dropShadowPanel.add(buttons, BorderLayout.SOUTH);
        JPanel debugPane = buildDebugPane();
        debugPane.setOpaque(false);
        dropShadowPanel.add(debugPane, BorderLayout.EAST);
        
        panel.add(new CheckerboardPanel(), StackLayout.BOTTOM);
        panel.add(dropShadowPanel, StackLayout.TOP);
        return panel;
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                DropShadowDemo demo = new DropShadowDemo();
                demo.setVisible(true);
            }
        });
    }
}
