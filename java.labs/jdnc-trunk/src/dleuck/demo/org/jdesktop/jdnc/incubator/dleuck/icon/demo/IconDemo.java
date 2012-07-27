/*
 * $Id: IconDemo.java 710 2005-09-27 21:22:08Z dleuck $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 *
 * This code is part of the Java icon package (JIC) donated by Ikayzo.com
 */
package org.jdesktop.jdnc.incubator.dleuck.icon.demo;

import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GradientPaint;
import java.io.IOException;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import static javax.swing.SwingConstants.*;

import java.awt.geom.Ellipse2D;

import org.jdesktop.jdnc.incubator.dleuck.icon.*;
import org.jdesktop.swingx.util.WindowUtils;

/**
 * This demo demonstrates
 * <ul>
 *     <li>Reading icons using JIconFile</li>
 *     <li>Creating new icon sizes</li>
 *     <li>Colorizing icons</li>
 *     <li>Creating layered icons</li>
 *     <li>Creating compound icons</li>
 *     <li>Creating icons with gradient borders</li>
 *     <li>Creating icons with anti-aliased shapes</li>
 *     <li>Creating an icon from a component</li>
 * </ul>
 * 
 * Note: This demo uses one of the free icons from Incors
 * (http://www.incors.com) - they also have excellent commercial collections
 * 
 * @author Daniel Leuck
 */
public class IconDemo extends JFrame {

	/**
	 * Create all the icons and add them to the frame.
	 */
	private void setup() {
		Container pane = getContentPane();
		pane.setLayout(new FlowLayout());
		
		add(new JLabel(getBigAndMediumIcon()));
		add(new JLabel(getLayeredIcon()));
	}
	
	/**
	 * Read 2 icons from the resources/goblet.jic file, colorize one, and
	 * composit them.
	 * 
	 * @return The composit icon
	 */
	private Icon getBigAndMediumIcon() {
		// goblet.jic contains four icons
		// - 32 pixel default
		// - 16 pixel default
		// - 32 pixel shadow variant
		// - 16 pixel shadow variant
		
		JIconFile file = null;
		try {
			file = new JIconFile(IconDemo.class
				.getResource("resources/goblet.jic"));
		} catch(IOException ioe) {
			handle("Problem reading demo icon 'goblet.jic'", ioe);
			System.exit(1);
		}
		
		// fetch the 32 pixel default icon
		ImageIcon bigIcon = file.getIcon(32);
		
		// fetch a 24 pixel shadow variant
		// because no 24 pixel icon exists in the .jic file, JIconFile will
		// create a high quality resized icon
		ImageIcon mediumIcon = file.getIcon("shadow", 24);
		
		// colorize the goblet
		ImageIcon mediumIconRed = new ImageIcon(
				IconUtils.colorize(mediumIcon.getImage(), Color.red));
		
		// composit the icons in a side by side layout
		return new CompoundIcon(bigIcon, mediumIconRed);
	}
	
	/**
	 * We create a layered icon having (from bottom to top)
	 * <ul>
	 *     <li>A red square background with a gradient border</li>
	 *     <li>A yellow circle in the upper left corner offset 4 pixels</li>
	 *     <li>A white letter J</li>
	 * </ul>
	 * 
	 * @return The layeredIcon
	 */
	private Icon getLayeredIcon() {
		
		Icon redSquare = new ShapeIcon(30,30,Color.red);
		Icon redSquareWithBorder = new PaddedIcon(redSquare, 2, 2, 2, 2,
			new GradientPaint(75, 75, Color.white, 95, 95, Color.gray, true));
		
		Icon yellowCircle = ShapeIcon.ellipse(10,10,Color.yellow);
		
		JLabel jLabel = new JLabel("J");
		jLabel.setForeground(Color.white);
		jLabel.setFont(new Font("serif", Font.BOLD, 16));
		Icon jIcon = IconUtils.makeIconFromComponent(jLabel,-1,-1,true);
		
		LayeredIcon back = new LayeredIcon(
			redSquareWithBorder, // bottomIcon
			yellowCircle, // topIcon
			LEFT, // halign
			TOP, // valign
			4, // xOffset
			4  // yOffset
		);

		return new LayeredIcon(back, jIcon, CENTER, CENTER, 0, 0);
	}
	
	/**
	 * Print the message and a stacktrace.
	 */
	public void handle(String message, Throwable err) {
		System.err.println(message);
		err.printStackTrace();
	}
	
	/**
	 * Start the demo.  
	 * 
	 * @param args ignored (no commandline arguments)
	 */
	public static void main(String[] args) {      
		IconDemo demo = new IconDemo();	
		demo.setTitle("Icon Demo");
		demo.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		demo.setup();
		demo.setSize(800, 600);
		demo.setLocation(WindowUtils.getPointForCentering(demo));
		demo.setVisible(true);
	}
}
