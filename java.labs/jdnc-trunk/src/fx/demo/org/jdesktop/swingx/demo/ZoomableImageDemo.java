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
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.jdesktop.swingx.CheckerboardPanel;
import org.jdesktop.swingx.StackLayout;
import org.jdesktop.swingx.ZoomableImagePanel;

/**
 * See method {@link #buildViewPane()} to see how to use
 * {@link org.jdesktop.swingx.StackLayout},
 * {@link org.jdesktop.swingx.CheckerboardPanel},
 * {@link org.jdesktop.swingx.ZoomableImagePanel} and
 * {@link org.jdesktop.swingx.graphics.ShadowRenderer}.
 *  
 * @author Romain Guy <romain.guy@mac.com>
 */

public class ZoomableImageDemo extends JFrame {
    private Color labelColor = Color.BLACK;
    private ZoomableImagePanel zoomPanel;

    public ZoomableImageDemo() throws HeadlessException {
        super("Zoomable Image");
        buildContentPane();

        setSize(new Dimension(640, 480));
        setLocationRelativeTo(null);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private void buildContentPane() {
        JPanel viewPane = buildViewPane();
        add(new JScrollPane(viewPane), BorderLayout.CENTER);
        JPanel debugPane = buildDebugPane();
        add(debugPane, BorderLayout.EAST);
    }

    private JPanel buildDebugPane() {
        JPanel panel = new JPanel(new GridBagLayout());
        final JSlider slider;
        JLabel label;
        
        panel.add(label = new JLabel("Zoom Level:"),
                  new GridBagConstraints(0, 0,
                                         1, 1,
                                         1.0, 0.0,
                                         GridBagConstraints.LINE_START,
                                         GridBagConstraints.NONE, 
                                         new Insets(0, 6, 0, 0),
                                         0, 0));
        label.setForeground(labelColor);
        panel.add(slider = new JSlider(25, 400, 100),
                  new GridBagConstraints(0, 1,
                                         1, 1,
                                         1.0, 0.0,
                                         GridBagConstraints.LINE_START,
                                         GridBagConstraints.NONE, 
                                         new Insets(0, 6, 0, 6),
                                         0, 0));
        zoomPanel.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                //noinspection StringEquality
                if (evt.getPropertyName() == ZoomableImagePanel.ZOOM_CHANGED_PROPERTY) {
                    slider.setValue((int) (((Double) evt.getNewValue()) * 100));
                }
            }
        });
        slider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                int value = ((JSlider) e.getSource()).getValue();
                zoomPanel.setZoom(value / 100.0);
            }
        });
        
        panel.add(label = new JLabel("Picture:"),
                  new GridBagConstraints(0, 2,
                                         1, 1,
                                         1.0, 0.0,
                                         GridBagConstraints.LINE_START,
                                         GridBagConstraints.NONE, 
                                         new Insets(0, 6, 0, 0),
                                         0, 0));
        label.setForeground(labelColor);
        
        JButton button;
        panel.add(button = new JButton("[Internal]"),
                  new GridBagConstraints(0, 3,
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
                        int result = chooser.showOpenDialog(ZoomableImageDemo.this);
                        if (result == JFileChooser.APPROVE_OPTION) {
                            File file = chooser.getSelectedFile();
                            source.setText(file.getName());
                            try {
                                zoomPanel.setImage(ImageIO.read(file));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
            }
        });

//        JCheckBox checkbox;
//        panel.add(checkbox = new JCheckBox("Drop Shadow", false),
//                  new GridBagConstraints(0, 4,
//                                         1, 1,
//                                         1.0, 0.0,
//                                         GridBagConstraints.LINE_START,
//                                         GridBagConstraints.HORIZONTAL,
//                                         new Insets(0, 6, 0, 6),
//                                         0, 0));
//        checkbox.setOpaque(false);
//        checkbox.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                JCheckBox source = (JCheckBox) e.getSource();
//                if (source.isSelected()) {
//                    dropShadowPanel.add(zoomPanel);
//                    viewPane.add(dropShadowPanel, StackLayout.TOP);
//                } else {
//                    viewPane.remove(dropShadowPanel);
//                    viewPane.add(zoomPanel, StackLayout.TOP);
//                }
//                viewPane.revalidate();
//                SwingUtilities.invokeLater(new Runnable() {
//                    public void run() {
//                        viewPane.repaint();
//                    }
//                });
//            }
//        });

        panel.add(Box.createVerticalGlue(),
                  new GridBagConstraints(0, 5,
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
        
        //DropShadowPanel dropShadowPanel = new DropShadowPanel(new BorderLayout());
        //dropShadowPanel.setDistance(10);
        
        zoomPanel = new ZoomableImagePanel();
        try {
            BufferedImage image = ImageIO.read(getClass().getResource("images/deathvalley.jpg"));
            zoomPanel.setImage(image);
        } catch (IOException e) {
            e.printStackTrace();            
        }
        //dropShadowPanel.add(zoomPanel);

        panel.add(new CheckerboardPanel(), StackLayout.BOTTOM);
        panel.add(zoomPanel, StackLayout.TOP);
        return panel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                ZoomableImageDemo demo = new ZoomableImageDemo();
                demo.setVisible(true);
            }
        });
    }
}
