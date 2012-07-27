/*
 * $Id: JXMenuBarDemo.java 2616 2008-08-03 17:50:01Z oasuncion $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 */



package org.jdesktop.swingx;

import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.net.URL;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.jdesktop.swingx.painter.BusyPainter;
import org.jdesktop.swingx.painter.CompoundPainter;
import org.jdesktop.swingx.painter.MattePainter;
import org.jdesktop.swingx.painter.Painter;
import org.jdesktop.swingx.painter.PinstripePainter;

@SuppressWarnings("serial")
public class JXMenuBarDemo extends JFrame {

	
	static Icon i = null;
	
	public static void main(String[] args) {

		JXMenuBarDemo demo = new JXMenuBarDemo();
		final JXMenuBar menuBar = new JXMenuBar();
		final JSlider alphaSlider = new JSlider(0, 100, 100);
		final JXPanel panel = new JXPanel();
		JLabel image = new JLabel();		
		URL resource = ClassLoader.getSystemClassLoader().getResource("noel.jpg");
		ImageIcon fieldIcon = new ImageIcon(resource);
		
		JPanel grid = new JPanel(new GridLayout(2,3));
		
		
		panel.setSize(600, 518);
		demo.setTitle("JXMenuBar Demonstration");
		demo.setResizable(false);
		demo.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		demo.setSize(600, 518);
		demo.getContentPane().add(panel);

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = demo.getSize();
		if (frameSize.height > screenSize.height) {
			frameSize.height = screenSize.height;
		}
		if (frameSize.width > screenSize.width) {
			frameSize.width = screenSize.width;
		}

		demo.setLocation(screenSize.width / 2 - (frameSize.width / 2),
				screenSize.height / 2 - (frameSize.height / 2));

		// building the JXmenuBar
		JXMenu menu1 = new JXMenu("Demo1");
		menu1.setForeground(Color.WHITE);
		menu1.add(new JXMenuItem("action 1"));
		menu1.add(new JXMenuItem("action 2"));
		menu1.add(new JXMenuItem("action 3"));
		JXRadioButtonMenuItem radioButton = new JXRadioButtonMenuItem();
		radioButton.setText("radioButton 1");
		menu1.add(radioButton);
		JXCheckBoxMenuItem checkBox = new JXCheckBoxMenuItem();
		checkBox.setText("checkBox 1");
		menu1.add(checkBox);
		JXMenu menu2 = new JXMenu("Demo2");
		menu2.setForeground(Color.WHITE);
		menu2.add(new JXMenuItem("action 4"));
		menu2.add(new JXMenuItem("action 5"));
		menu2.add(new JXMenuItem("action 6"));
		menu2.addSeparator();
		menu2.add(new JXMenuItem("action 7"));
		menu2.add(new JXMenuItem("action 8"));
		JXMenu action9 = new JXMenu("action 9");
		action9.add(new JXMenuItem("action 10"));
		action9.add(new JXMenuItem("action 11"));
		action9.add(new JXMenuItem("action 12"));
		JXMenu action13 = new JXMenu("action 13");
		action9.add(action13);
		action13.add(new JXMenuItem("action 14"));
		menu2.add(action9);
		menuBar.add(menu1);
		menuBar.add(menu2);

		JButton leftRightBtn = new JButton(new AbstractAction("R2L/L2R") {

			public void actionPerformed(ActionEvent e) {
				if (menuBar.getComponentOrientation().equals(
						ComponentOrientation.RIGHT_TO_LEFT)) {
					menuBar
							.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
				} else { // L2R or UNK
					menuBar
							.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
				}
				menuBar.revalidate();

			}
		});
		
		JButton startStopBtn = new JButton(new AbstractAction("Start/Stop") {			
            public void actionPerformed(ActionEvent e) {
                if (i == null) {
                    i = menuBar.getBusyIcon().getIcon();
                    menuBar.getBusyIcon().setIcon(null);
                } else {
                	menuBar.getBusyIcon().setIcon(i);
                    i = null;
                }
                menuBar.getBusyIcon().setBusy(!menuBar.getBusyIcon().isBusy());
            }});
		
		JButton menusColorBtn = new JButton(new AbstractAction("Menus Background") {
			   public void actionPerformed(ActionEvent e) {
				   Color newColor = JColorChooser.showDialog(
		                    panel,
		                    "Choose Menus Color",
		                    menuBar.getMenusBackground());
				   menuBar.setMenusBackground(newColor);
	            }});
	
		JButton menusForegroundBtn = new JButton(new AbstractAction("Menus Foreground") {
			   public void actionPerformed(ActionEvent e) {
				   Color newColor = JColorChooser.showDialog(
		                    panel,
		                    "Choose Menus Foreground",
		                    menuBar.getMenusForeground());
				   menuBar.setMenusForeground(newColor);
	            }});
		
		JButton menusSelForegroundBtn = new JButton(new AbstractAction("Menus Selection Foreground") {
			   public void actionPerformed(ActionEvent e) {
				   Color newColor = JColorChooser.showDialog(
		                    panel,
		                    "Choose Menus Selection Foreground",
		                    menuBar.getMenusSelectionForeground());
				   menuBar.setMenusSelectionForeground(newColor);
	            }});

		Color blue = Color.DARK_GRAY;
		Color translucent = new Color(blue.getRed(), blue.getGreen(), blue
				.getBlue(), 100);

		Painter<Object> background = new MattePainter<Object>(blue);
		PinstripePainter<Object> pinstripes = new PinstripePainter<Object>(45);
		pinstripes.setStripeWidth(2);
		pinstripes.setPaint(Color.YELLOW);
		GradientPaint blueToTranslucent = new GradientPaint(new Point2D.Double(
				.4, 0), blue, new Point2D.Double(2, 0), translucent);
		MattePainter<Object> veil = new MattePainter<Object>(blueToTranslucent);
		veil.setPaintStretched(true);
		CompoundPainter<JXMenuBar> p = new CompoundPainter<JXMenuBar>(
				background, pinstripes, veil);

		menuBar.setBackgroundPainter(p);
		menuBar.getBusyIcon().setBusy(true);
		BusyPainter<?> painter = new BusyPainter(new Rectangle2D.Float(0, 0,
				8.0f, 8.0f), new Rectangle2D.Float(5.5f, 5.5f, 27.0f, 27.0f));
		painter.setTrailLength(4);
		painter.setPoints(8);
		painter.setFrame(-1);
		painter.setBaseColor(Color.YELLOW);
		painter.setHighlightColor(Color.ORANGE);
		menuBar.getBusyIcon().setBusyPainter(painter);
		demo.setJMenuBar(menuBar);

		alphaSlider.setOpaque(false);
		alphaSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				float alpha = alphaSlider.getValue() / 100f;
				menuBar.setAlpha(alpha);
				menuBar.repaint();
			}
		});
		
		
		
		fieldIcon.setImage(scale(fieldIcon.getImage(), 600, 400));
		image.setIcon(fieldIcon);
		panel.add(image);
		
        grid.add(leftRightBtn);
        grid.add(alphaSlider);
		
        grid.add(startStopBtn);
        grid.add(menusColorBtn);
        grid.add(menusForegroundBtn);
        grid.add(menusSelForegroundBtn);
        panel.add(grid);

		demo.setVisible(true);

	}

	/**
	 * Modify the dimension of an image
	 * 
	 * @param source
	 *            image to modify
	 * @param width
	 *            width of the target image
	 * @param height
	 *            height of the target image
	 * @return modified image
	 */
	public static Image scale(Image source, int width, int height) {
		/* We create a new image with the specified dimension */
		BufferedImage buf = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_ARGB);

		/* We draw on the Graphics of the bufferized image */
		Graphics2D g = buf.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
				RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g.drawImage(source, 0, 0, width, height, null);
		g.dispose();

		/* we return the bufferized image */
		return buf;
	}

}
