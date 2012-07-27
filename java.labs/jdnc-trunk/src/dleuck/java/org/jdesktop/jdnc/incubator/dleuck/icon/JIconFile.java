/*
 * $Id: JIconFile.java 712 2005-09-28 21:03:47Z dleuck $
 *
 * Copyright 2004 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 *
 * This code is part of the Java icon package (JIC) donated by Ikayzo.com
 */
package org.jdesktop.jdnc.incubator.dleuck.icon;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 * <p>A group of icons stored in a "JIC" icon archive.  Icon archives are zip
 * archives containing multiple physical icons as PNG or JPEG images.  Each 
 * image has the name "image_(size).(png|jpg)" where (size) is the image size.   
 * All images have square dimensions (same width and height.)  Variants such as 
 * icons with drop shadows are stored within the archive in a folder named 
 * after the variant.  For example, an icon variant having a drop shadow with a
 * size of 24 should be stored as "shadow/image_24.png".  Variant names are all
 * lowercase by convention.</p>
 * 
 * @author Daniel Leuck
 */
public class JIconFile {
	
	/*
	Open Question: Should this class load in all the physical icons in the
	constructor (as it currently does), or should it load them on demand from
	the getIcon(...) methods to save memory?  The disadvantage if the latter
	approach is the need to throw IOExceptions from all the getIcon methods
	and redundant reads for applications using multiple icons sizes or variants.
	*/

	/*
	Open Question: Should we also support physical icons in some sort of
	vector format?
	*/
	
	private SortedMap<String, List<ImageIcon>> icons =
		new TreeMap<String, List<ImageIcon>>();
	
	/**
	 * Create an icon group from a file at the given URL.  The stream is closed
	 * after the archive is read.
	 * 
	 * @param url The location of the archive
	 * @throws IOException If an IO error occurs while reading in the jic
	 *     archive
	 * @throws EncodingException If the archive contains unsupported image
	 *     formats (anything other than PNG or JPEG images.)
	 */
	public JIconFile(URL url) throws IOException, EncodingException {
		this(url.openStream());
	}
	
	/**
	 * Create an icon group from the given stream.  The stream is closed
	 * after the archive is read.
	 * 
	 * @param input A stream containing an JIC archive.
	 * @throws IOException If an IO error occurs while reading in the xic
	 *     archive
	 * @throws EncodingException If the archive contains unsupported image
	 *     formats (anything other than PNG or JPEG images.)
	 */
	public JIconFile(InputStream input) throws IOException, EncodingException {
		
		try {
			ZipInputStream zis = new ZipInputStream(input);
			ZipEntry entry = null;
			
			while((entry=zis.getNextEntry())!=null) {
				
				try {
					if(!entry.isDirectory()) {
						String name = entry.getName();
						if(!name.endsWith("png") && !name.endsWith("jpg")
								&& !name.endsWith("jpeg")) {
							
							throw new EncodingException("\"" + name +
									"\" uses an unsupported image format." +
									"  The JIC format supports PNG and JPEG.");
						}
						
						int slashIndex = name.indexOf("/");
						String variant = (slashIndex==-1)
							? "default"
							: name.substring(0, slashIndex);
						
						List<ImageIcon> iconList = icons.get(variant);
						if(iconList==null) {
							icons.put(variant, iconList=
								new ArrayList<ImageIcon>());
						}
						
						iconList.add(new ImageIcon(ImageIO.read(zis)));
					}
				} finally {
					zis.closeEntry();
				}
			}
		} finally {
			// squash
			try { input.close(); } catch(Exception e) {}
		}
	}

	/**
	 * Get a list of the icons stored in the .xic icon archive.  If no icons
	 * exist for the given variant return null.
	 * 
	 * @param variant The icon variant or "default" for the default icons
	 * @return A list of icons for the given variant or null
	 */
	public List<ImageIcon> getPhysicalIcons(String variant) {
		return icons.get(variant);
	}
	
	/**
	 * Get a physical (stored) icon having the given size.  If the variant or
	 * size does not exist return null.
	 * 
	 * @param variant The name of the icon variant
	 * @param size The size of the physical (stored) icon
	 * @return The icon or null if no such icon exists
	 */
	public ImageIcon getPhysicalIcon(String variant, int size) {
		List<ImageIcon> icons = getPhysicalIcons(variant);
		if(icons==null)
			return null;
		for(ImageIcon icon:icons) {
			if(icon.getIconWidth()==size)
				return icon;
		}
		
		return null;
	}
	
	/**
	 * Get the physical (stored) icon sizes from smallest to largest.  If the
	 * variant does not exist return null.
	 * 
	 * @param variant The name of the icon variant
	 * @return The sizes or null if the variant does not exist
	 */
	public SortedSet<Integer> getPhysicalSizes(String variant) {
		List<ImageIcon> icons = getPhysicalIcons(variant);
		if(icons==null)
			return null;
		
		SortedSet<Integer> sizes = new TreeSet<Integer>();
		for(ImageIcon icon:icons) {
			sizes.add(icon.getIconWidth());
		}
		
		return sizes;
	}
	
	/**
	 * Get the variants for this XIcon group.  The default icon group will be
	 * included as "default"
	 * 
	 * @return The variants stored in this XIcon group.
	 */
	public Set<String> getVariants() {
		return icons.keySet();
	}
	
	/**
	 * Get an icon (physical or derived) for the given size.  If the size does
	 * not exist an icon will be created from one of the physical (stored)
	 * icons.
	 * 
	 * @param size The size of the icon
	 */
	public ImageIcon getIcon(int size) {
		return getIcon("default", size);
	}
	
	/**
	 * Get an icon (physical or derived) for the given variant and size.  If
	 * no such variant exists return null.  If the size does not exist an icon
	 * will be created from one of the physical (stored) icons.
	 * 
	 * @param variant The name of the icon variant
	 * @param size The size of the icon
	 */
	public ImageIcon getIcon(String variant, int size) {
		ImageIcon icon = getPhysicalIcon(variant, size);
		if(icon!=null) {
			return icon;
		}
		
		if(icon==null) {
			SortedSet<Integer> pIconSizes = getPhysicalSizes(variant);
			if(pIconSizes==null)
				return null;
			
			// find the first larger icon
			for(int pSize:pIconSizes) {
				if(pSize>size) {
					return resize(getPhysicalIcon(variant, pSize), size);
				}
			}
			
			// there are no larger icons so get the largest icon			
			return resize(getPhysicalIcon(variant, pIconSizes.last()), size);
		}
		
		return null;
	}
	
	/**
	 * Resize the image using bicubic interpolation.
	 * 
	 * @param original The image to resize
	 * @param newDimension The width and height of the new image
	 * @return A resized image
	 */
	private ImageIcon resize(ImageIcon original, int newDimension) {
        BufferedImage scaledImg = new BufferedImage(newDimension, newDimension, 
                BufferedImage.TYPE_INT_ARGB);

        Graphics2D gScaledImg = scaledImg.createGraphics();
		gScaledImg.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
		RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		gScaledImg.drawImage(original.getImage(), 0, 0, newDimension,
				newDimension, null);

		return new ImageIcon(scaledImg);
	}
	
	/**
	 * An exception thrown if the java icon file is improperly encoded.
	 * 
	 * @author Daniel Leuck
	 */
	@SuppressWarnings("serial")
	public static class EncodingException extends IOException {
		
		/**
		 * Create an encoding exception with the given message.
		 * 
		 * @param message A detailed message
		 */
		public EncodingException(String message) {
			super(message);
		}
	}
}
