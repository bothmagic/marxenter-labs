/*
 * $Id: IconUtils.java 712 2005-09-28 21:03:47Z dleuck $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 *
 * This code is part of the Java icon package (JIC) donated by Ikayzo.com
 */
package org.jdesktop.jdnc.incubator.dleuck.icon;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import javax.swing.CellRendererPane;
import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 * A set of icon related utility methods.
 * 
 * @author Daniel Leuck
 */
public class IconUtils {

	/**
	 * This is a utility class.
	 */
	private IconUtils() {}
	
	/**
	 * Colorize an image using the hue from the given color.
	 * 
	 * @param icon 	The image to colorize
	 * @param color The new hue
	 * @return 		The newly colorize image
	 */
	public static Image colorize(Image icon, Color color) {
		Icon imageIcon = new ImageIcon(icon);
		BufferedImage bi=new BufferedImage(imageIcon.getIconWidth(),
				imageIcon.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
		imageIcon.paintIcon(null, bi.createGraphics(),0,0);	
	
		int iw=bi.getWidth();
		int ih=bi.getHeight();

		float[] hsb=new float[3];
		Color.RGBtoHSB(color.getRed(),color.getGreen(),color.getBlue(),hsb);
		
		float chue = hsb[0];
		
		for(int y=0; y<ih; y++) {
			for(int x=0; x<iw; x++) {
				int rgb=bi.getRGB(x,y);
				
				int red=(rgb >> 16) & 0xFF;
				int green=(rgb >> 8) & 0xFF;
				int blue=(rgb >> 0) & 0xFF;
				int alpha=(rgb >> 24) & 0xFF;
				
				Color.RGBtoHSB(red,green,blue,hsb);
				//int hue=hsb[0];
				
				rgb = HSBtoRGB(chue, hsb[1], hsb[2],alpha);
				bi.setRGB(x,y,rgb);
			}
		}
	
		return bi;	
	}

	/**
	 * Creates an ImageIcon using a snapshot of the given component.  This
	 * method is mostly commonly used with JLabel, but will work with any
	 * component that does not have children.
	 * 
	 * @param component The component to be painted
	 * @param width The width to use when painting the component.  A value
	 *     of -1 will use the component's preferred width.
	 * @param height The height to use when painting the component.  A 
	 *     value of -1 will use the component's preferred height.
	 * @param forceAntialiasing If true, the component will be painted using
	 *     antialiasing.
	 */
	public static ImageIcon makeIconFromComponent(Component component, 
			int width, int height, boolean forceAntialiasing) {
		
		CellRendererPane renderer= new CellRendererPane();
		renderer.add(component);
		
		Dimension d = component.getPreferredSize();
		int iconWidth=(width==-1)? d.width : width;
		int iconHeight=(height==-1)? d.height : height;
		
		BufferedImage bi=new BufferedImage(iconWidth, iconHeight, 
                (component.isOpaque()) ? BufferedImage.TYPE_INT_RGB
                : BufferedImage.TYPE_INT_ARGB);
		
		Graphics2D g = (Graphics2D)bi.createGraphics();
		
		if(forceAntialiasing) {
			Object oldAA = g.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
			Object oldFM = g.getRenderingHint(
					RenderingHints.KEY_FRACTIONALMETRICS);
			Object oldRQ = g.getRenderingHint(RenderingHints.KEY_RENDERING);
			
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
			g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS,
				RenderingHints.VALUE_FRACTIONALMETRICS_ON);
			g.setRenderingHint(RenderingHints.KEY_RENDERING,
					RenderingHints.VALUE_RENDER_QUALITY);
			
			renderer.paintComponent(g,(Component)component, null, 0, 0,
					iconWidth, iconHeight, true);
				
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, oldAA);
			g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, oldFM);
			g.setRenderingHint(RenderingHints.KEY_RENDERING, oldRQ);
		} else {
			renderer.paintComponent(g,(Component)component, null, 0, 0,
					iconWidth, iconHeight, true);
		}
		
		return new ImageIcon(bi);		
	}
	
	/**
	 * Convert from an HSB to an RGB model
	 */
    private static int HSBtoRGB(float hue, float saturation, float brightness,
    	int alpha) {
    			
		int r = 0, g = 0, b = 0;
			if (saturation == 0) {
			r = g = b = (int) (brightness * 255.0f + 0.5f);
		} else {
			float h = (hue - (float)Math.floor(hue)) * 6.0f;
			float f = h - (float)java.lang.Math.floor(h);
			float p = brightness * (1.0f - saturation);
			float q = brightness * (1.0f - saturation * f);
			float t = brightness * (1.0f - (saturation * (1.0f - f)));
			switch ((int) h) {
				case 0:
					r = (int) (brightness * 255.0f + 0.5f);
					g = (int) (t * 255.0f + 0.5f);
					b = (int) (p * 255.0f + 0.5f);
					break;
				case 1:
					r = (int) (q * 255.0f + 0.5f);
					g = (int) (brightness * 255.0f + 0.5f);
					b = (int) (p * 255.0f + 0.5f);
					break;
				case 2:
					r = (int) (p * 255.0f + 0.5f);
					g = (int) (brightness * 255.0f + 0.5f);
					b = (int) (t * 255.0f + 0.5f);
					break;
				case 3:
					r = (int) (p * 255.0f + 0.5f);
					g = (int) (q * 255.0f + 0.5f);
					b = (int) (brightness * 255.0f + 0.5f);
					break;
				case 4:
					r = (int) (t * 255.0f + 0.5f);
					g = (int) (p * 255.0f + 0.5f);
					b = (int) (brightness * 255.0f + 0.5f);
					break;
				case 5:
					r = (int) (brightness * 255.0f + 0.5f);
					g = (int) (p * 255.0f + 0.5f);
					b = (int) (q * 255.0f + 0.5f);
					break;
			}
		}
		
		return ((alpha & 0xFF) << 24) |
                	((r & 0xFF) << 16) |
					((g & 0xFF) << 8)  |
					((b & 0xFF) << 0);
    }

}
